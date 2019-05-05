package com.bat.thirdparty.order.controller;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.order.OrderHeaderConstant;
import com.bat.dubboapi.order.order.dto.OrderCancelCmd;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.moleji.MolejiOrderCreateCmd;
import com.bat.thirdparty.order.api.dto.provisional.ProvisionalOrderInfo;
import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/open/order")
@Api(tags = "第三方对外订单接口")
public class OrderOpenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderOpenController.class);

    @Autowired
    private OrderOpenServiceI orderOpenServiceI;

    /**
     * 和佳海订单确认
     *OrderAddModel改为 JiuJiOrderCmd
     *
     * @return
     */
    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.OrderOpen,value = CommonLogTypeConstantDTO.OrderOpenOrderConfirm)
    @PostMapping("/confirm")
    @ApiOperation(value = "系统订单确认、基于id对接")
    public ResponseEntity<Object> orderConfirm(HttpServletRequest request, @RequestBody OrderBaseOnIdCmd orderBaseOnIdCmd)  {
        LOGGER.info("订单确认同步参数："+JSON.toJSONString(orderBaseOnIdCmd));
        return  orderOpenServiceI.createOrderBaseOnId(request,orderBaseOnIdCmd, null,null);
    }

    /**
     * 
     */
    //@SysLog(businessFunction= CommonLogTypeTitleConstantDTO.OrderOpen,value = CommonLogTypeConstantDTO.OrderOpenSyncOrder)
    @PostMapping("/syncOrder")
    @ApiOperation(value = "（基于编码的对接）")
    public ResponseEntity<Object> syncOrder(HttpServletRequest request, @RequestBody OrderBaseOnCodeCmd orderBaseOnCodeCmd)  {
        /*try {
            //0.校验参数信息
            if (order == null) {
                String message = "订单数据异常";
                throw new ApiException(message);
            } else if (order.getAddress() == null) {
                String message = "地址信息异常";
                throw new ApiException(message);
            } else if (order.getUserInfo() == null) {
                String message = "收件人信息异常";
                throw new ApiException(message);
            } else if (order.getOrderDetails() == null || order.getOrderDetails().size() == 0) {
                String message = "订单信息异常，订单详情信息不可为空";
                throw new ApiException(message);
            } else {
                //1. 将所有约定的数据信息进行读取
                String distributorIdStr = request.getHeader(DISTRIBUTOR_HEADER_NAME);
                String orderSource = request.getHeader("orderSource");
                String timestamp = request.getHeader("timestamp");
                String sign = request.getHeader("sign");
                DistributorInfo distributorInfo = distributorInfoService.get(Long.parseLong(distributorIdStr));
                String jsonString = JSON.toJSONString(order);
                if(distributorInfo == null || distributorInfo.getFreezeStatus() == 1 || distributorInfo.getCapitalStatus() != 2){
                    String message = "分销商不存在，或已冻结，或非合作分销商";
                    throw new ApiException(message);
                }
                PlatformInfo platformInfo = platformInfoDBManager.findByExtIdAndStatus(distributorIdStr, 1);
                if(platformInfo == null){
                    String message = "平台参数未配置，请先配置平台参数";
                    throw new ApiException(message);
                }
                //2. 校验签名是否正确
                String signStr = distributorIdStr +platformInfo.getAppKey() +orderSource +jsonString +timestamp;
                logger.info("订单同步："+ signStr);
                String checkSignature = Sha1Handler.encryption(signStr);
                logger.info("sign："+ checkSignature);
                if (!sign.equalsIgnoreCase(checkSignature)) {
                    String message = "签名信息错误";
                    throw new ApiException(message);
                } else {
                    OrderRespVo orderRespVo = goodsDesignService.thirdOrderConfirm(distributorInfo, order ,Integer.valueOf(orderSource));
                    return ResponseEntity.ok(orderRespVo);
                }
            }
        } catch (Exception e) {
            logger.error("同步订单至B2B出现异常{},异常信息{}", JSON.toJSONString(order),e.getMessage());
            e.printStackTrace();
            String message = e.getMessage();
            throw new ApiException(message);
        }*/
        return orderOpenServiceI.createOrderBaseOnCode(orderBaseOnCodeCmd,request,null);
    }

    /**
     * 订单取消接口
     *
     * @return
     */
    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.OrderOpen,value = CommonLogTypeConstantDTO.OrderOpenCancel)
    @PostMapping("/cancel/{orderNo}")
    @ApiOperation(value = "第三方取消订单接口、原来旧系统、后续对接请基于下面的订单取消")
    public ResponseEntity<Object> cancel(@PathVariable(name = "orderNo",required = false) String orderNo, HttpServletRequest request)  {
       /* Long distributorId = Long.parseLong(request.getHeader("distributorId").trim());
        String userNo = request.getHeader("userNo");
        if(orderId == null){
            throw new CustomException("订单编号不能为空");
        }
        OrderCancelModel orderCancelModel = orderUserService.cancelOrderNew(distributorId, userNo, orderId);
        return ResponseEntity.ok(orderCancelModel);*/
        LOGGER.info("第三方取消订单:{}",orderNo);
        String distributorId = request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID);
        return orderOpenServiceI.cancelOrderFromDistributor(distributorId,orderNo);
    }


    /**
     * 订单取消接口（标准）
     *
     * @return
     */
    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.OrderOpen,value = CommonLogTypeConstantDTO.OrderOpenCancel)
    @PostMapping("/cancel")
    @ApiOperation(value = "第三方取消订单接口(后续对接都用这个)")
    public Response standardCancel(@RequestBody OrderCancelCmd orderCancelCmd, HttpServletRequest request)  {
       /* Long distributorId = Long.parseLong(request.getHeader("distributorId").trim());
        String userNo = request.getHeader("userNo");
        if(orderId == null){
            throw new CustomException("订单编号不能为空");
        }
        OrderCancelModel orderCancelModel = orderUserService.cancelOrderNew(distributorId, userNo, orderId);
        return ResponseEntity.ok(orderCancelModel);*/
        return orderOpenServiceI.cancelOrderFromDistributorStandard(orderCancelCmd,request);
    }

    /**
     * 订单数据提交
     * 接口地址为: /api/order/create
     *
     * @return
     */
    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.OrderOpen,value = CommonLogTypeConstantDTO.OrderOpenCreate)
    @PostMapping("/create")
    @ApiOperation(value = "创建临时订单、提交到第三方接口")
    public Response create(@RequestBody ProvisionalOrderInfo orderInformation, HttpServletRequest servletRequest)  {


        return orderOpenServiceI.submitToThird(orderInformation,servletRequest);
        //   return ResponseEntity.ok(data);
    }

    /**
     * 摩乐吉
     * @param molejiOrderCreateCmd
     * @return
     * @throws ApiException
     */

    @PostMapping("/syncOrderFromMoleji")
    public ResponseEntity<Object> syncOrderFromMoleji(@RequestBody MolejiOrderCreateCmd molejiOrderCreateCmd)  {

        return orderOpenServiceI.createOrderFromMoleji(molejiOrderCreateCmd);
    }

}
