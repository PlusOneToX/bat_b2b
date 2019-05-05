package com.bat.thirdparty.factory.maike.convertor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeDeliveryInfo;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsThirdMappingRpcDTO;
import com.bat.dubboapi.thirdparty.maike.dto.order.MaikeDeliveryMethod;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.factory.maike.common.MaikeBaseParamCmd;
import com.bat.thirdparty.factory.maike.common.MaikeConstant;
import com.bat.thirdparty.factory.maike.common.MaikeFactoryConfig;
import com.bat.thirdparty.factory.maike.response.MaikeResponse;

@Component
public class MaikeConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaikeConvertor.class);

    @Resource
    private HttpUtil httpUtil;

    @DubboReference(check = false, timeout = 10000)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    /**
     * 获取配送方式
     * 
     * @param province
     * @param isOverseas
     * @param logisticId
     *            配送方式id
     * @return
     */
    public MaikeDeliveryInfo getDeliveryInfo(String province, Boolean isOverseas, Integer logisticId,
        MaikeFactoryConfig maikeFactoryConfig) {
        String url = maikeFactoryConfig.getMaikeDomain() + maikeFactoryConfig.getDeliveryQueryUrl() + "?secret="
            + MaikeBaseParamCmd.secret + "&company_id=" + MaikeBaseParamCmd.company_id;
        if (!isOverseas) {
            // 国内
            url = url + "&province=" + province + "&is_abroad=" + MaikeConstant.OrderDeliveryInternal;
        } else {
            url = url + "&is_abroad=" + MaikeConstant.OrderDeliveryOverseas;
        }
        LOGGER.info("查询麦客配送方式,{}", url);
        String result = httpUtil.requestFor(url, HttpMethod.GET, String.class);
        LOGGER.info("获取配送方式返回结果：" + result);

        MaikeResponse deliveryResponse = JSON.parseObject(result, new TypeReference<MaikeResponse>() {});
        if (deliveryResponse == null || deliveryResponse.getStatus() != 1) {
            /*OrderLog log = new OrderLog();
            log.setOperateType(OrderConstant.OperateTypeYCSyncOrder);
            log.setOperateUid(0L);
            log.setOperateName(order.getDistributorName());
            log.setOrderId(order.getId());
            log.setCreateTime(System.currentTimeMillis());
            String remark = String.format("同步订单信息至麦客操作执行失败,订单编号: %s , 分销商编号: %s，具体原因: %s", order.getId(),
                    order.getDistributorId(), deliveryResponse.getMsg());
            log.setOperateRemark(remark);
            orderAdminDataManager.saveLog(log);*/
            return null;
        }
        String deliveryStr = String.valueOf(deliveryResponse.getData());
        List<MaikeDeliveryMethod> maikeDeliveryMethodList =
            JSONArray.parseArray(deliveryStr, MaikeDeliveryMethod.class);
        if (maikeDeliveryMethodList == null || maikeDeliveryMethodList.size() == 0) {
            /* OrderLog log = new OrderLog();
            log.setOperateType(OrderConstant.OperateTypeYCSyncOrder);
            log.setOperateUid(0L);
            log.setOperateName(order.getDistributorName());
            log.setOrderId(order.getId());
            log.setCreateTime(System.currentTimeMillis());
            String remark = String.format("同步订单信息至麦客操作执行失败,订单编号: %s , 分销商编号: %s，具体原因: %s", order.getId(),
                    order.getDistributorId(), "暂时未配置该省配送方式：" + province);
            log.setOperateRemark(remark);
            orderAdminDataManager.saveLog(log);*/
            return null;
        } else {
            Response<LogisticsThirdMappingRpcDTO> mappingRpcDTOResponse = systemLogisticsServiceRpc
                .getThirdLogisticsByThirdTypeAndLogisticsId(MaikeConstant.THIRD_DELIVERY_TYPE_MAIKE, logisticId);
            LogisticsThirdMappingRpcDTO logisticsThirdMappingRpcDTO = mappingRpcDTOResponse.getData();
            if (logisticsThirdMappingRpcDTO == null) {
                throw ThirdPartyException.buildException("B2B系统无法找到麦客系统定义的配送方式");
            }
            MaikeDeliveryInfo maikeDeliveryInfo = new MaikeDeliveryInfo();
            maikeDeliveryInfo.setDelivery_method_id(Integer.parseInt(logisticsThirdMappingRpcDTO.getThirdDeliveryNo()));
            maikeDeliveryInfo.setReceipt_time(new Date());
            maikeDeliveryInfo
                .setIs_abroad(isOverseas ? MaikeConstant.OrderDeliveryOverseas : MaikeConstant.OrderDeliveryInternal);
            return maikeDeliveryInfo;
        }
    }

}
