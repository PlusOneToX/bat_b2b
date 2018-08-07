package com.bat.financial.voucher;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.voucher.constant.VoucherStatus;
import com.bat.financial.voucher.executor.ErrorCode;
import com.bat.financial.voucher.executor.VoucherCmdExc;
import com.bat.financial.voucher.executor.VoucherQryExc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryReq;
import com.bat.dubboapi.financial.voucher.dto.data.ErpVoucherDetailsDTO;
import com.bat.dubboapi.financial.voucher.dto.data.VoucherDistributorRpcDTO;
import com.bat.dubboapi.order.order.api.OrderDistributorDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
import com.bat.financial.dao.voucher.dataobject.VoucherDistributorDO;
import com.bat.financial.pay.constant.BillsBusinessType;
import com.bat.financial.pay.constant.CustomerFlag;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/1 10:38
 */
@DubboService
@Slf4j
public class VoucherServiceRpcImpl implements FinancialVoucherServiceRpc {

    @Resource
    private VoucherQryExc voucherQryExc;

    @Resource
    private VoucherCmdExc voucherCmdExc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private OrderDistributorDataServiceRpc orderDistributorDataServiceRpc;

    /**
     * ERP->B2B 创建收款单
     * 
     * @param dtos
     */
    @Override
    public Response createVouchers(List<ErpVoucherDetailsDTO> dtos) {
        log.info("ERP->B2B 创建收款单:{}", dtos);
        try {
            dtos.forEach(voucherDetailsDTO -> {
                String orderNo = voucherDetailsDTO.getOrderNo();
                String[] split = orderNo.split(",");
                if (split.length != 0) {
                    Integer orderId = Integer.valueOf(split[0]);
                    log.info("待创建收款单 B2B单号：{}", orderId);
                    com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> listResponse =
                        orderDistributorDataServiceRpc.listByOrderIdAndErpFlag(orderId, (short)1);
                    if (listResponse.isSuccess()) {
                        OrderDistributorDataRpcDTO orderDistributorDataRpcDTO = listResponse.getData().get(0);
                        log.info("B2B单号：{},对应的订单信息：{}", orderId, orderDistributorDataRpcDTO);
                        VoucherDistributorDO aDo = new VoucherDistributorDO();
                        aDo.setDistributorId(orderDistributorDataRpcDTO.getDistributorId());
                        aDo.setDistributorName(orderDistributorDataRpcDTO.getDistributorName());
                        aDo.setCompanyName(orderDistributorDataRpcDTO.getCompanyName());
                        aDo.setAmount(BigDecimal.valueOf(voucherDetailsDTO.getAmount()));
                        aDo.setPayWay(orderDistributorDataRpcDTO.getPayWay());
                        aDo.setOutTradeNo(null);
                        aDo.setCustomerFlag(CustomerFlag.IS_NOT_CUSTOMER);
                        aDo.setCurrencyType("CNY");
                        aDo.setBusinessType(BillsBusinessType.ORDER);
                        aDo.setBusinessId(orderNo);
                        aDo.setVoucherStatus(VoucherStatus.CONFIRMED);
                        aDo.setRemark(null);
                        aDo.setVoucherErpNo(voucherDetailsDTO.getVoucherNo());
                        Date date = new Date();
                        aDo.setCreateTime(date);
                        aDo.setUpdateTime(date);
                        voucherCmdExc.createVoucher(aDo);
                    } else {
                        log.info("获取订单信息出错");
                        throw FinancialException.buildException(listResponse.getErrCode(),
                            listResponse.getErrMessage());
                    }
                } else {
                    log.info("没有订单号");
                    throw FinancialException.buildException(ErrorCode.B_VOUCHER_BUSINESS_ID_NULL,
                        MessageUtils.get(ErrorCode.B_VOUCHER_BUSINESS_ID_NULL));
                }
            });
        } catch (FinancialException e) {
            log.info("异常已经捕获处理");
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("异常已经捕获处理");
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
        return Response.buildSuccess();
    }

    /**
     * 同步ERP的返回结果
     * 
     * @param dtos
     * @return
     */
    @Override
    public Response updateVouchers(List<ErpVoucherDetailsDTO> dtos) {
        log.info("财务开始更新收款单");
        try {
            dtos.forEach(voucherDetailsDTO -> {
                String orderNo = voucherDetailsDTO.getOrderNo();
                String voucherNo = voucherDetailsDTO.getVoucherNo();
                log.info("财务更新收款单：收款单id：{},收款单号：{}", orderNo, voucherNo);
                VoucherDistributorDO voucherById = voucherQryExc.getVoucherDOById(Integer.parseInt(orderNo));
                log.info("财务原有收款单数据：{}", voucherById);
                if (voucherById != null && voucherById.getVoucherErpNo() == null) {
                    voucherById.setVoucherErpNo(voucherDetailsDTO.getVoucherNo());
                    voucherById.setVoucherStatus(VoucherStatus.CONFIRMED);
                    voucherById.setUpdateTime(new Date());
                    voucherCmdExc.updateVoucher(voucherById);
                    log.info("财务更新收款单：收款单id：{},收款单号：{}。成功", orderNo, voucherNo);
                } else {
                    log.info("财务更新收款单：收款单id：{},收款单号：{}。原收款单收款单号已存在", orderNo, voucherNo);
                    // throw FinancialException.buildException(ErrorCode.B_VOUCHER_IS_EXISTS,
                    // "财务更新收款单：收款单id：" + orderNo + ",收款单号：" + voucherNo + "。原收款单收款单号已存在");
                }
            });
        } catch (FinancialException e) {
            log.info("异常已经捕获处理");
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.info("异常已经捕获处理");
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
        return Response.buildSuccess();
    }

    /**
     * TODO 拼装没有验证
     * 
     * @param voucherIds
     * @return
     */
    @Override
    public Response<List<CreateReceiveBillEntryReq>> listErpVoucherById(List<Integer> voucherIds) {
        log.info("财务服务 同步收款单 voucherIds：{}", JSON.toJSONString(voucherIds));
        try {
            List<VoucherDistributorDO> voucherDistributorDOS = voucherQryExc.listVoucherById(voucherIds);
            List<CreateReceiveBillEntryReq> collect = voucherDistributorDOS.stream()
                .map(voucherDistributorDO -> voucherCmdExc.getCreateReceiveBillEntryRequest(voucherDistributorDO))
                .collect(Collectors.toList());
            return Response.of(collect);
        } catch (FinancialException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
    }

    @Override
    public Response<VoucherDistributorRpcDTO> listVouchersByBusinessId(Integer businessId) {
        try {
            VoucherQry qry = new VoucherQry();
            qry.setBusinessId(businessId + "");
            List<VoucherDistributorDTO> voucherDistributorDTOS = voucherQryExc.getVoucherDTO(qry);
            if (!CollectionUtils.isEmpty(voucherDistributorDTOS)) {
                if (voucherDistributorDTOS.size() == 1) {
                    VoucherDistributorDTO dto1 = voucherDistributorDTOS.get(0);
                    VoucherDistributorRpcDTO dto = new VoucherDistributorRpcDTO();
                    BeanUtils.copyProperties(dto1, dto);
                    return Response.of(dto);
                } else {
                    return Response.buildFailure(ErrorCode.B_VOUCHER_NOT_UNIQUE,
                        MessageUtils.get(ErrorCode.B_VOUCHER_NOT_UNIQUE));
                }
            } else {
                return Response.buildFailure(ErrorCode.B_VOUCHER_NULL, MessageUtils.get(ErrorCode.B_VOUCHER_NULL));
            }
        } catch (BeansException e) {
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
    }
}
