package com.bat.financial.refund.executor;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.dao.refund.RefundDistributorMapper;
import com.bat.financial.dao.refund.dataobject.RefundBillsBaseDO;
import com.bat.financial.refund.constant.ErpRefundBillType;
import com.bat.financial.refund.constant.RefundDsitributorStatus;
import com.bat.financial.refund.convertor.RefundDistributorConvertor;
import com.bat.financial.voucher.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.financial.refund.dto.data.CreateRefundBillRequest;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillEntry;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderVoucherErpDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.common.dto.BaseBillEntity;
import com.bat.financial.api.refund.dto.RefundQry;
import com.bat.financial.api.refund.dto.data.RefundDistributorDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;
import com.bat.financial.refund.constant.ErpRefundItemType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:59
 */
@Component
@Slf4j
public class RefundDistributorQryExc {

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private RefundDistributorMapper refundDistributorMapper;

    @Resource
    private RefundBillsQryExc refundBillsQryExc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private OrderServiceRpc orderServiceRpc;

    public RefundDistributorDTO getRefundById(Integer id) {
        return RefundDistributorConvertor.toRefundDistributorDTO(refundDistributorMapper.selectByPrimaryKey(id));
    }

    public PageInfo<RefundDistributorDTO> listRefund(RefundQry qry) {
//        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
//        map.put("distributorIds", distributorIds);
        List<RefundDistributorDO> refundDistributorDOS = refundDistributorMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(refundDistributorDOS);
        List<RefundDistributorDTO> list = RefundDistributorConvertor.toRefundDistributorDTOList(pageInfo.getList());
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<RefundDistributorDO> listRefundDO(RefundQry qry) {
        BeanMap map = BeanMap.create(qry);
        return refundDistributorMapper.selectByParams(map);
    }

    public RefundDistributorDO getRefundDO(Integer id) {
        return refundDistributorMapper.selectByPrimaryKey(id);
    }

    public RefundBillSyncDTO getRefundDistributor(String outRefundNo) {
        RefundQry qry = new RefundQry();
        qry.setOutRefundNo(outRefundNo);
        qry.setRefundStatus(RefundDsitributorStatus.WAIT_CONFIRM);
        List<RefundDistributorDO> refundDistributorDOS = listRefundDO(qry);
        if (refundDistributorDOS != null && refundDistributorDOS.size() == 1) {
            RefundDistributorDO aDo = refundDistributorDOS.get(0);
            DistributorRpcDTO distributorInfo = commonService.getDistributorInfo(aDo.getDistributorId());
            if (distributorInfo != null) {
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String time = ft.format(date);
                CreateRefundBillRequest req = new CreateRefundBillRequest();
                req.setFDATE(time);
                req.setFPAYORGID(distributorInfo.getOrganizationErp());
                req.setFSETTLEORGID(distributorInfo.getOrganizationErp());
                req.setFCONTACTUNIT(distributorInfo.getErpNo());
                req.setFRECTUNIT(distributorInfo.getErpNo());
                List<RefundBillEntry> list = new ArrayList<>();

                RefundBillsBaseDO refundBillsBaseDO = refundBillsQryExc.getRefundBillsBaseDO(outRefundNo);
                if (StringUtils.isNotBlank(aDo.getBusinessId())) {
                    List<Integer> orderIds = Arrays.stream(aDo.getBusinessId().split(",")).map(Integer::valueOf)
                        .collect(Collectors.toList());
                    Response<List<OrderVoucherErpDTO>> listResponse = orderServiceRpc.orderVoucherErp(orderIds);
                    if (listResponse.isSuccess()) {
                        List<OrderVoucherErpDTO> data = listResponse.getData();
                        boolean b = data.stream()
                            .allMatch(orderVoucherErpDTO -> StringUtils.isNotBlank(orderVoucherErpDTO.getOrderErpNo()));
                        if (b) {
                            data.stream().forEach(orderVoucherErpDTO -> {
                                // 根据订单id 获取ERP
                                RefundBillEntry entry = new RefundBillEntry();
                                entry.setFREFUNDAMOUNTFOR(aDo.getAmount().doubleValue());
                                entry.setFREALREFUNDAMOUNTFOR_D(aDo.getAmount().doubleValue());
                                // private String FPURPOSEID; //原收款用途，收款用途，订单收款:SFKYT01_SYS，充值收款:SFKYT02_SYS
                                entry.setFPURPOSEID(ErpRefundItemType.ORDER_REFUND);
                                entry.setFNOTE("订单退款");
                                entry.setFORDERBILLNO(orderVoucherErpDTO.getOrderErpNo());
                                BaseBillEntity entity = commonService.getEntity(distributorInfo.getOrganizationId(),
                                    aDo.getRefundWay(), refundBillsBaseDO.getOutTradeNo());
                                entry.setFACCOUNTID(entity.getFACCOUNTID());
                                entry.setFSETTLETYPEID(entity.getFSETTLETYPEID());
                                list.add(entry);
                            });
                        } else {
                            log.error("data:{}", data);
                            log.error("该收款单对应的订单，没有全部同步ERP，请先同步ERP 且订单为已确认（已审核）");
                            throw FinancialException.buildException(
                                ErrorCode.B_VOUCHER_CORRESPONDING_ORDER_NOT_SYNC_ERP,
                                MessageUtils.get(ErrorCode.B_VOUCHER_CORRESPONDING_ORDER_NOT_SYNC_ERP));
                        }
                    }
                } else {
                    RefundBillEntry entry = new RefundBillEntry();
                    entry.setFREFUNDAMOUNTFOR(aDo.getAmount().doubleValue());
                    entry.setFREALREFUNDAMOUNTFOR_D(aDo.getAmount().doubleValue());
                    entry.setFPURPOSEID(ErpRefundItemType.RECHARGE_REFUND);
                    entry.setFNOTE("充值退款");
                    entry.setFORDERBILLNO("");
                    BaseBillEntity entity = commonService.getEntity(distributorInfo.getOrganizationId(),
                        aDo.getRefundWay(), refundBillsBaseDO.getOutTradeNo());
                    entry.setFACCOUNTID(entity.getFACCOUNTID());
                    entry.setFSETTLETYPEID(entity.getFSETTLETYPEID());
                    list.add(entry);
                }
                req.setRefundBillDetail(list);
                req.setFBillTypeID(ErpRefundBillType.ORDER_REFUND);
                req.setFB2B_BILLNO(aDo.getId() + "");
                RefundBillSyncDTO dto = new RefundBillSyncDTO();
                dto.setId(aDo.getId());
                dto.setRefundBillReq(req);
                return dto;
            } else {
                log.info("分销商 不存在");
                throw FinancialException
                    .buildException(com.bat.financial.refund.executor.ErrorCode.B_REFUND_DISTRIBUTOR_NOT_EXISTS);
            }
        } else {
            log.info("没有查询到退款单 或退款单不处于待确认状态");
            throw FinancialException.buildException(com.bat.financial.refund.executor.ErrorCode.B_REFUND_NOT_EXISTS,
                MessageUtils.get(com.bat.financial.refund.executor.ErrorCode.B_REFUND_NOT_EXISTS));
        }
    }
}
