package com.bat.thirdparty.quanyi.service.executor;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.quanyi.QuanyiErrorCode;
import com.bat.thirdparty.suning.api.dto.OrderDispatchCmd;
import com.bat.thirdparty.suning.common.SuNingConstant;
import com.bat.thirdparty.suning.response.OrderSignInResponse;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcCmd;
import com.bat.thirdparty.quanyi.common.QuanyiConstant;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiLogDO;
import com.bat.thirdparty.suning.api.SuNingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

@Component
public class ThirdQuanyiRpcExe {

    @Autowired
    private ThirdQuanyiQryExe thirdQuanyiQryExe;

    @Autowired
    private ThirdQuanyiCmdExe thirdQuanyiCmdExe;

    @Autowired
    private SuNingServiceI suNingServiceI;

    @Transactional(rollbackFor = Exception.class)
    public Response signIn(ThirdQuanyiRpcCmd cmd) {
        try {
            ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findById(cmd.getId());
            if (thirdQuanyiDO == null) {
                throw new ThirdPartyException();
            }
            if (thirdQuanyiDO.getExchangeCodeId() != null) {
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_HAS_GET);
            }
            if (thirdQuanyiDO.getSignInFlag() != null && thirdQuanyiDO.getSignInFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_HAS_GET);
            }
            if (thirdQuanyiDO.getCancelFlag() != null && thirdQuanyiDO.getCancelFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_FAIL);
            }
            if (thirdQuanyiDO.getDestroyFlag() != null && thirdQuanyiDO.getDestroyFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_FAIL);
            }
            //派工则进行派工
            if (thirdQuanyiDO.getDispatchFlag() == null || thirdQuanyiDO.getDispatchFlag() == ThirdCommonConstant.COMMON_FLAG_NO) {
                OrderDispatchCmd orderDispatchCmd = new OrderDispatchCmd();
                orderDispatchCmd.setThirdQuanyiId(thirdQuanyiDO.getId());
                orderDispatchCmd.setOrderId(thirdQuanyiDO.getThirdCode());
                suNingServiceI.orderDispatch(orderDispatchCmd);
            }
            thirdQuanyiDO.setCustomerId(cmd.getCustomerId());
            thirdQuanyiDO.setCustomerNo(cmd.getCustomerNo());
            thirdQuanyiDO.setExchangeId(cmd.getExchangeId());
            thirdQuanyiDO.setExchangeName(cmd.getExchangeName());
            thirdQuanyiDO.setExchangeCodeId(cmd.getExchangeCodeId());
            thirdQuanyiDO.setPlainCode(cmd.getPlainCode());
            thirdQuanyiDO.setSecretCode(cmd.getSecretCode());
            thirdQuanyiDO.setUpdateTime(new Date());
            thirdQuanyiDO.setSignInFlag(ThirdCommonConstant.COMMON_FLAG_YES);
            thirdQuanyiCmdExe.updateByPrimaryKeySelective(thirdQuanyiDO);
            //增加对应操作日志
            ThirdQuanyiLogDO thirdQuanyiLogDO = new ThirdQuanyiLogDO();
            thirdQuanyiLogDO.setThirdQuanyiId(cmd.getId());
            thirdQuanyiLogDO.setOperatorUserType(QuanyiConstant.operatorUserType4);
            thirdQuanyiLogDO.setCreateTime(new Date());
            thirdQuanyiLogDO.setOperateType(QuanyiConstant.operateType3);
            thirdQuanyiCmdExe.insertSelectiveLog(thirdQuanyiLogDO);
            //在苏宁端进行签到
            OrderSignInResponse response = suNingServiceI.sign(thirdQuanyiDO.getThirdCode(), cmd.getId());
            //判断是否派工成功
            if (response.getSuccess() && response.getData().getReturnCode().equals(SuNingConstant.returnCodeY)) {
                return Response.buildSuccess();
            } else {
                throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_FAIL);
            }
        } catch (ThirdPartyException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Response.buildFailure(ThirdCommonErrorCode.COMMON_OPERATE_FAIL, e.getMessage());
        }

    }


}
