package com.bat.thirdparty.log.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.log.api.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.log.api.LogServiceI;
import com.bat.thirdparty.log.api.dto.*;
import com.bat.thirdparty.log.utils.PageHelper;
import com.bat.thirdparty.mongodb.executor.LogQryExe;

/**
 * 沙漠
 */
@Service
public class LogServiceImpl implements LogServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
    @Resource
    private LogQryExe logQryExe;

    @Override
    public PageInfo findPageCommonLogByParams(LogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 1) {
                qry.setBusinessModule(qry.getContent());
            }
            if (qry.getContentType() == 2) {
                qry.setBusinessFunction(qry.getContent());
            }
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageCommonLogByParams(qry);
        List<CommonLogDTO> commonLogDTOs = BeanUtils.copyList(pageHelper.getList(), CommonLogDTO.class);
        if (commonLogDTOs == null) {
            commonLogDTOs = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(commonLogDTOs);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }

    @Override
    public PageInfo findPageDistributorLogByParams(DistributorLogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageDistributorLogByParams(qry);
        List<DistributorLogDTO> distributorLogDTOS = BeanUtils.copyList(pageHelper.getList(), DistributorLogDTO.class);
        if (distributorLogDTOS == null) {
            distributorLogDTOS = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(distributorLogDTOS);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }

    @Override
    public PageInfo findPageOrderLogByParams(OrderLogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageOrderLogByParams(qry);
        List<OrderLogDTO> orderLogDTOS = BeanUtils.copyList(pageHelper.getList(), OrderLogDTO.class);
        if (orderLogDTOS == null) {
            orderLogDTOS = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(orderLogDTOS);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }

    @Override
    public PageInfo findPageVoucherLogByParams(VoucherLogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageVoucherLogByParams(qry);
        List<VoucherLogDTO> voucherLogDTOS = BeanUtils.copyList(pageHelper.getList(), VoucherLogDTO.class);
        if (voucherLogDTOS == null) {
            voucherLogDTOS = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(voucherLogDTOS);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }

    @Override
    public PageInfo findPageRefundLogByParams(RefundLogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageRefundLogByParams(qry);
        List<RefundLogDTO> refundLogDTOS = BeanUtils.copyList(pageHelper.getList(), RefundLogDTO.class);
        if (refundLogDTOS == null) {
            refundLogDTOS = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(refundLogDTOS);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }

    @Override
    public PageInfo findPageOrderDeliverBillLogByParams(OrderDeliverBillLogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageOrderDeliverBillLogByParams(qry);
        List<OrderDeliverBillLogDTO> orderDeliverBillLogDTOS =
            BeanUtils.copyList(pageHelper.getList(), OrderDeliverBillLogDTO.class);
        if (orderDeliverBillLogDTOS == null) {
            orderDeliverBillLogDTOS = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(orderDeliverBillLogDTOS);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }

    @Override
    public PageInfo findPageWithdrawApplyLogByParams(WithdrawApplyLogQry qry) {
        if (qry.getContentType() != null) {
            if (qry.getContentType() == 3) {
                qry.setOperateSource(qry.getContent());
            }
            if (qry.getContentType() == 4) {
                qry.setOperator(qry.getContent());
            }
            if (qry.getContentType() == 5) {
                qry.setOperateType(qry.getContent());
            }
            if (qry.getContentType() == 6) {
                qry.setOperateDes(qry.getContent());
            }
            if (qry.getContentType() == 7) {
                qry.setOperateData(qry.getContent());
            }
        }
        PageHelper pageHelper = logQryExe.findPageWithdrawApplyLogByParams(qry);
        List<WithdrawApplyLogDTO> withdrawApplyLogDTOS =
            BeanUtils.copyList(pageHelper.getList(), WithdrawApplyLogDTO.class);
        if (withdrawApplyLogDTOS == null) {
            withdrawApplyLogDTOS = new ArrayList<>();
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(withdrawApplyLogDTOS);
        pageInfo.setTotal(pageHelper.getTotal());
        pageInfo.setPages((int)pageHelper.getCurrentPage());
        pageInfo.setPageSize((int)pageHelper.getPageSize());
        return pageInfo;
    }
}
