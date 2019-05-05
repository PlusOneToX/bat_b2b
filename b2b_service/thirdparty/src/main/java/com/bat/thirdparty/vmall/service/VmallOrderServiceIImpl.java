package com.bat.thirdparty.vmall.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bat.thirdparty.alibaba.taobao.api.dto.TaoBaoHttpRequestDTO;
import com.bat.thirdparty.common.error.vmall.VmallErrorCode;
import com.bat.thirdparty.common.util.Sha1Handler;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.platform.api.DistributorSysPlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.AddressQry;
import com.bat.thirdparty.order.api.dto.common.UserInfoQry;
import com.bat.thirdparty.vmall.api.VmallOrderServiceI;
import com.bat.thirdparty.vmall.dto.TimeDTO;
import com.bat.thirdparty.vmall.request.BopOcidDecryptRequest;
import com.bat.thirdparty.vmall.request.BopOrderFulfillInfoRequest;
import com.bat.thirdparty.vmall.request.BopOrderStatusUpdateRequest;
import com.bat.thirdparty.vmall.request.BopQueryShipmentRequest;
import com.bat.thirdparty.vmall.response.BopAddressDecryptResponse;
import com.bat.thirdparty.vmall.response.BopOrderFulfillInfoResponse;
import com.bat.thirdparty.vmall.response.BopOrderStatusUpdateResponse;
import com.bat.thirdparty.vmall.response.BopShipmentLisResponse;
import com.bat.thirdparty.vmall.service.executor.VmallExe;

@Service
public class VmallOrderServiceIImpl implements VmallOrderServiceI {

    @Autowired
    private VmallExe vmallExe;

    @Autowired
    private OrderOpenServiceI orderOpenServiceI;

    private static final Logger LOGGER = LoggerFactory.getLogger(VmallOrderServiceIImpl.class);

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorSysPlatformServiceRpc distributorSysPlatformServiceRpc;

    @Value("${huawei.distributor.id}")
    private Integer huaweiDistributorId;

    @Value("${huawei.orderSource}")
    private Integer huaweiOrderSource;

    @Override
    public List<OrderBaseOnIdCmd> vmallOrderPull(TimeDTO time) {
        BopOrderFulfillInfoRequest request = new BopOrderFulfillInfoRequest();
        LOGGER.info("当前租户:{}", TenantContext.getTenantNo());

        Calendar cal = Calendar.getInstance();
        // 获取两个小时的时间
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 2);
        Date twoHoursAgo = cal.getTime();
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(twoHoursAgo) + " +0800";

        // 定义当前时间
        Date nowDate = new Date();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate) + " +0800";

        if (StringUtils.isNotBlank(time.getStartTime()) && StringUtils.isNotBlank(time.getEndTime())) {
            LOGGER.info("调度中心执行时候增加了时间参数:{}", JSONObject.toJSONString(time));
            startTime = time.getStartTime();
            endTime = time.getEndTime();
        }

        request.setStartPosition("");
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        List<Integer> orderStatusList = new ArrayList<>();
        orderStatusList.add(5);
        orderStatusList.add(9);
        orderStatusList.add(10);
        orderStatusList.add(11);
        request.setOrderStatusList(orderStatusList);
        List<OrderBaseOnIdCmd> orderBaseOnIdCmds = new ArrayList<>();
        while (true) {
            LOGGER.info("vmall拉取订单传参:{}", JSONObject.toJSONString(request));
            BopOrderFulfillInfoResponse response = vmallExe.orderFulfillInfoList(request);
            LOGGER.info("vmall订单拉取返回:{}", JSONObject.toJSONString(response));
            // 跳出循环
            if (!response.isSuccess()) {
                break;
            }
            LOGGER.debug("vmall下一个仓库:{}", response.getData().getNextPosition());
            request.setStartPosition(response.getData().getNextPosition());
            List<BopOrderFulfillInfoResponse.OrderFulfillInfoList> orderFulfillInfoList =
                response.getData().getOrderFulfillInfoList();
            if (orderFulfillInfoList == null || orderFulfillInfoList.size() == 0) {
                if (StringUtils.isBlank(response.getData().getNextPosition())) {
                    break;
                }
                continue;
            }
            // 开始订单遍历
            for (BopOrderFulfillInfoResponse.OrderFulfillInfoList orderFulfillInfo : orderFulfillInfoList) {
                if (orderFulfillInfo.getOrderExtendFulfillInfoList() == null
                    || orderFulfillInfo.getOrderExtendFulfillInfoList().size() == 0) {
                    // 订单如若没有扩展信息则直接遍历下一个
                    continue;
                }
                // 循环获取定制信息
                for (BopOrderFulfillInfoResponse.OrderExtendFulfillInfoList orderExtendFulfillInfo : orderFulfillInfo
                    .getOrderExtendFulfillInfoList()) {
                    LOGGER.debug("vmall判断当前订单是否bat订单:{}", JSONObject.toJSONString(orderExtendFulfillInfo));
                    if (orderExtendFulfillInfo.getParamCode().equals("thirdPartyCustomParameter")) {
                        LOGGER.debug("当前订单是bat订单，开始组装订单。。。。");
                        // 开始组装订单
                        OrderBaseOnIdCmd addModel = new OrderBaseOnIdCmd();
                        addModel.setOrderNo(orderFulfillInfo.getOrderCode());
                        OrderDetailBaseOnIdQry orderDetailBaseOnIdQry = JSONObject
                            .parseObject(orderExtendFulfillInfo.getParamValue(), OrderDetailBaseOnIdQry.class);
                        LOGGER.debug("vmall抓取到数量更改前的定制信息:{}", JSONObject.toJSONString(orderDetailBaseOnIdQry));
                        // 处理定制数据的数量
                        String str = orderDetailBaseOnIdQry.getExtendField().getSkuCodeAndQtys();
                        String[] strs = str.split(":");
                        // 得到真正的数量
                        Integer count = Integer.valueOf(strs[1]);
                        orderDetailBaseOnIdQry.setCount(count);
                        orderDetailBaseOnIdQry.setTotalPrice(
                            orderDetailBaseOnIdQry.getPrice().multiply(new BigDecimal(count.toString())));
                        LOGGER.info("vmall抓取到数量更改后的定制信息:{}", JSONObject.toJSONString(orderDetailBaseOnIdQry));
                        if (orderFulfillInfo.getPaymentFulfillInfoList() != null && orderFulfillInfo.getPaymentFulfillInfoList().size() == 1) {
                            //c端客户支付总价
                            BigDecimal payTotal = orderFulfillInfo.getPaymentFulfillInfoList().get(0).getPaymentAmount();
                            LOGGER.info("得到用户付款的总价格:{}",payTotal);
                            if (payTotal != null && payTotal.compareTo(BigDecimal.ZERO) >= 0 && count >= 1) {
                                BigDecimal salePrice = payTotal.divide(new BigDecimal(count.toString()), 2, BigDecimal.ROUND_HALF_UP);
                                LOGGER.info("得到用户付款的商品单价:{}",salePrice);
                                orderDetailBaseOnIdQry.setSalePrice(salePrice);
                            }
                        }
                        BopOcidDecryptRequest bopOcidDecryptRequest = new BopOcidDecryptRequest();
                        bopOcidDecryptRequest.setOcid(orderFulfillInfo.getOcid());
                        bopOcidDecryptRequest.setOrderCode(orderFulfillInfo.getOrderCode());
                        bopOcidDecryptRequest.setScense("2002");
                        BopAddressDecryptResponse bopAddressDecryptResponse =
                            vmallExe.ocidDecrypt(bopOcidDecryptRequest);
                        if (bopAddressDecryptResponse.getResultCode() == null
                            || !bopAddressDecryptResponse.getResultCode().equals("1")) {
                            LOGGER.info("获取不到解密地址数据;订单编号:{}", orderFulfillInfo.getOrderCode());
                            break;
                        }
                        if (!bopAddressDecryptResponse.getData().isValid()) {
                            LOGGER.info("本条收货人信息无效;订单编号:{}", orderFulfillInfo.getOrderCode());
                            break;
                        }
                        // 处理手机号
                        String phone = getPhone(bopAddressDecryptResponse.getData());
                        if (StringUtils.isBlank(phone)) {
                            LOGGER.info("获取不到手机号;订单编号:{}", orderFulfillInfo.getOrderCode());
                            break;
                        }
                        // 组装用户信息
                        UserInfoQry userInfo = new UserInfoQry(bopAddressDecryptResponse.getData().getConsignee(),
                            phone, orderFulfillInfo.getUserId(), bopAddressDecryptResponse.getData().getEmail());
                        LOGGER.info("vmall组装得到的用户信息:{}", JSONObject.toJSONString(userInfo));
                        // 组装收货地址
                        AddressQry addressQry = createAddress(bopAddressDecryptResponse.getData(),
                            orderFulfillInfo.getOrderDeliveryAddressFulfillInfo());
                        addModel.setAddress(addressQry);
                        List<OrderDetailBaseOnIdQry> orderDetailBaseOnIdQrys = new ArrayList<>();
                        orderDetailBaseOnIdQrys.add(orderDetailBaseOnIdQry);
                        addModel.setOrderDetails(orderDetailBaseOnIdQrys);
                        addModel.setUserInfo(userInfo);
                        LOGGER.debug("vmall组装后的下单参数:{}", JSONObject.toJSONString(addModel));
                        orderBaseOnIdCmds.add(addModel);
                        // 跳出获取定制信息循环
                        break;
                    }
                }
            }
            if (StringUtils.isBlank(response.getData().getNextPosition())) {
                break;
            }
        }
        addOrder(orderBaseOnIdCmds);
        return orderBaseOnIdCmds;
    }

    @Override
    public ResponseBaseBean sendGoodsCallBack(OrderLogistics logistics, String otherOrderNo) {
        LOGGER.info("vmall发货信息回调,logistics:{},otherOrderNo:{}", JSONObject.toJSONString(logistics), otherOrderNo);
        BopOrderStatusUpdateRequest request = new BopOrderStatusUpdateRequest();
        BopOrderStatusUpdateRequest.shipmentUpdate shipmentUpdate = new BopOrderStatusUpdateRequest.shipmentUpdate();
        shipmentUpdate.setExpressNo(logistics.getExpressNo());
        shipmentUpdate.setOrderCode(otherOrderNo);
        shipmentUpdate.setLogisticsCompanyCode(logistics.getExpressCode());
        shipmentUpdate.setStatus("20");
        List<BopOrderStatusUpdateRequest.shipmentUpdate> shipmentUpdates = new ArrayList<>();
        shipmentUpdates.add(shipmentUpdate);
        request.setShipmentList(shipmentUpdates);
        LOGGER.debug("vmall发货信息回调请求参数:{}", JSONObject.toJSONString(request));
        BopOrderStatusUpdateResponse response = vmallExe.orderStatusUpdate(request);
        LOGGER.info("vmall发货信息回调返回:{}", JSONObject.toJSONString(response));
        if (response.getResultCode() == null || !response.getResultCode().equals("1")) {
            LOGGER.info("vmall发货信息推送失败:{}", otherOrderNo);
            LOGGER.debug("查询发货单后再次回调.....");
            BopQueryShipmentRequest bopQueryShipmentRequest = new BopQueryShipmentRequest();
            bopQueryShipmentRequest.setOrderNo(otherOrderNo);
            bopQueryShipmentRequest.setStatus((short)10);
            BopShipmentLisResponse bopShipmentLisResponse = vmallExe.queryShipmentLis(bopQueryShipmentRequest);
            if (bopShipmentLisResponse.getResultCode() == null || !bopShipmentLisResponse.getResultCode().equals("1")) {
                return ResponseBaseBean.responseBean(VmallErrorCode.VMALL_PUSH_LOGISTICS_FAIL_CODE,
                    VmallErrorCode.VMALL_PUSH_LOGISTICS_FAIL);
            }
            if (!CollectionUtils.isEmpty(bopShipmentLisResponse.getData().getShipmentInfoList())) {
                String shipmentNo =
                    bopShipmentLisResponse.getData().getShipmentInfoList().get(0).getBasicInfo().getShipmentNo();
                LOGGER.info("当前发货单为:{}", shipmentNo);
                shipmentUpdate.setShipmentNo(shipmentNo);
                LOGGER.debug("再次请求发货信息回调请求参数:{}", JSONObject.toJSONString(request));
                response = vmallExe.orderStatusUpdate(request);
                LOGGER.debug("再次请求发货信息回调返回:{}", JSONObject.toJSONString(response));
                if (response.getResultCode() == null || !response.getResultCode().equals("1")) {
                    LOGGER.info("发货信息再次推送失败:{}", otherOrderNo);
                    return ResponseBaseBean.responseBean(VmallErrorCode.VMALL_PUSH_LOGISTICS_FAIL_CODE,
                        VmallErrorCode.VMALL_PUSH_LOGISTICS_FAIL);
                }
            } else {
                return ResponseBaseBean.responseBean(VmallErrorCode.VMALL_PUSH_LOGISTICS_FAIL_CODE,
                    VmallErrorCode.VMALL_PUSH_LOGISTICS_FAIL);
            }
        }
        return ResponseBaseBean.responseBean();
    }

    /**
     * 组装地址
     */
    private AddressQry createAddress(BopAddressDecryptResponse.OrderConsignee orderConsignee,
        BopOrderFulfillInfoResponse.OrderDeliveryAddressFulfillInfo OrderDeliveryAddressFulfillInfo) {
        LOGGER.debug("vmall组装地址前：{}", JSONObject.toJSONString(orderConsignee));
        String street="";
        if(StringUtils.isNotBlank(OrderDeliveryAddressFulfillInfo.getStreet())){
            street=OrderDeliveryAddressFulfillInfo.getStreet();
        }
        AddressQry address =
            new AddressQry(OrderDeliveryAddressFulfillInfo.getProvince(), OrderDeliveryAddressFulfillInfo.getCity(),
                OrderDeliveryAddressFulfillInfo.getDistrict(), street+orderConsignee.getAddress());
        LOGGER.info("vmall组装地址后：{}", JSONObject.toJSONString(address));
        return address;
    }

    /**
     * 处理手机号
     */
    private String getPhone(BopAddressDecryptResponse.OrderConsignee orderConsignee) {
        if (orderConsignee.isUseVirtualMobile()) {
            return orderConsignee.getVisualMobile();
        }
        String phone = orderConsignee.getMobile();
        if (StringUtils.isBlank(phone)) {
            phone = orderConsignee.getPhone();
        }
        return phone;
    }

    /**
     * 进行下单
     *
     * @param orderBaseOnIdCmds
     */
    private void addOrder(List<OrderBaseOnIdCmd> orderBaseOnIdCmds) {
        for (OrderBaseOnIdCmd orderBaseOnIdCmd : orderBaseOnIdCmds) {
            try {
                TaoBaoHttpRequestDTO taoBaoHttpRequestDTO = createHeader(orderBaseOnIdCmd);
                LOGGER.debug("vmall创建请求头参数组装结果:{}", JSONObject.toJSONString(taoBaoHttpRequestDTO));
                orderOpenServiceI.createOrderBaseOnId(null, orderBaseOnIdCmd, taoBaoHttpRequestDTO, null);
            } catch (Exception e) {
                LOGGER.error("vmall下单出现异常:{}", e);
            }
        }
    }

    /**
     * 组装请求头
     */
    private TaoBaoHttpRequestDTO createHeader(OrderBaseOnIdCmd addModel) {
        String distributorId = huaweiDistributorId.toString();
        String orderSource = huaweiOrderSource.toString();
        String timestamp = System.currentTimeMillis() + "";
        String orderString = JSON.toJSONString(addModel);
        LOGGER.debug("vmall开始获取平台信息,orderSource:{},distributorId:{}", orderSource, distributorId);
        SysPlatformRpcDTO sysPlatformRpcDTO = distributorSysPlatformServiceRpc
            .getByPlatformAndDistributorId(orderSource, Integer.valueOf(distributorId)).getData();
        LOGGER.debug("vmall平台信息返回:{}", JSONObject.toJSONString(sysPlatformRpcDTO));
        String signStr = distributorId + sysPlatformRpcDTO.getAppKey() + orderSource + orderString + timestamp;
        LOGGER.info("vmall调取下单 签名串：{}", signStr);
        TaoBaoHttpRequestDTO HttpRequest = new TaoBaoHttpRequestDTO();
        String orderSign = Sha1Handler.encryption(signStr);
        HttpRequest.setDistributorId(distributorId);
        HttpRequest.setOrderSource(orderSource);
        HttpRequest.setTimestamp(timestamp);
        HttpRequest.setOrderSign(orderSign);
        return HttpRequest;
    }
}
