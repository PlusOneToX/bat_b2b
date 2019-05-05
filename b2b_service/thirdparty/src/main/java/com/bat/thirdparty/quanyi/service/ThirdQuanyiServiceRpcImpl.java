package com.bat.thirdparty.quanyi.service;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.quanyi.QuanyiErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiCmdExe;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiRpcExe;
import com.bat.thirdparty.suning.common.SuNingConstant;
import com.bat.thirdparty.suning.response.OrderDestroryResponse;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.quanyi.api.ThirdQuanyiServiceRpc;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcCmd;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcDTO;
import com.bat.thirdparty.quanyi.api.QuanyiServiceI;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiQryExe;
import com.bat.thirdparty.suning.api.SuNingServiceI;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 权益接口
 */
@DubboService
public class ThirdQuanyiServiceRpcImpl implements ThirdQuanyiServiceRpc {

    @Autowired
    private ThirdQuanyiQryExe thirdQuanyiQryExe;

    @Autowired
    private ThirdQuanyiCmdExe thirdQuanyiCmdExe;

    @Autowired
    private SuNingServiceI suNingServiceI;

    @CreateCache(name = ThirdKeyConstant.EXCHANGE_QUANYI_PRE)
    private Cache<String, Integer> exchangeQuanyiCache;

    @Autowired
    private ThirdQuanyiRpcExe thirdQuanyiRpcExe;

    @Autowired
    private QuanyiServiceI quanyiServiceI;

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdQuanyiServiceRpcImpl.class);

    @Override
    public Response<ThirdQuanyiRpcDTO> findByDistributorIdAndThirdCode(Integer distributorId, String thirdCode) {
        ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findByDistributorIdAndThirdCode(distributorId, thirdCode);
        if (thirdQuanyiDO == null) {
            return Response.of(null);
        }
        ThirdQuanyiRpcDTO thirdQuanyiRpcDTO = new ThirdQuanyiRpcDTO();
        BeanUtils.copyProperties(thirdQuanyiDO, thirdQuanyiRpcDTO);
        return Response.of(thirdQuanyiRpcDTO);
    }

    @Override
    public Response signIn(ThirdQuanyiRpcCmd cmd) {
        AutoReleaseLock autoReleaseLock = null;
        try {
            // 分布式锁
            autoReleaseLock =
                    exchangeQuanyiCache.tryLock(TenantContext.getTenantNo() + ":" + cmd.getId(), 10, TimeUnit.SECONDS);
            if (autoReleaseLock == null) {
                // 加锁
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_SIGN_ERROR);
            }
            return thirdQuanyiRpcExe.signIn(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(QuanyiErrorCode.QUANYI_SIGN_ERROR, MessageUtils.get(QuanyiErrorCode.QUANYI_SIGN_ERROR));
        } finally {
            try {
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Response destroy(Integer exchangeCodeId, Integer orderId) {
        AutoReleaseLock autoReleaseLock = null;
        try {
            ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findByExchangeCodeId(exchangeCodeId);
            if (thirdQuanyiDO == null) {
                return Response.buildSuccess();
            }
            // 分布式锁
            autoReleaseLock =
                    exchangeQuanyiCache.tryLock(TenantContext.getTenantNo() + ":" + thirdQuanyiDO.getId(), 10, TimeUnit.SECONDS);
            if (autoReleaseLock == null) {
                // 加锁
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_DESTROY_ERROR);
            }
            if (thirdQuanyiDO.getDestroyFlag() != null && thirdQuanyiDO.getDestroyFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                //已核销的情况下；不经过苏宁直接覆盖原有订单
                quanyiServiceI.destroyAndLog(thirdQuanyiDO.getId(), thirdQuanyiDO.getThirdUserRemark(), orderId);
                return Response.buildSuccess();
            }
            if (thirdQuanyiDO.getCancelFlag() != null && thirdQuanyiDO.getCancelFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                LOGGER.error("该权益已被取消,不能核销,第三方单号:{}",thirdQuanyiDO.getThirdCode());
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_DESTROY_ERROR);
            }
            OrderDestroryResponse orderDestroryResponse = suNingServiceI.destroy(thirdQuanyiDO.getThirdCode(), thirdQuanyiDO.getId(), thirdQuanyiDO.getThirdUserRemark(), orderId);
            if (orderDestroryResponse.getSuccess() && orderDestroryResponse.getData().getReturnCode().equals(SuNingConstant.returnCodeY)) {
                return Response.buildSuccess();
            } else {
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_DESTROY_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(QuanyiErrorCode.QUANYI_DESTROY_ERROR, MessageUtils.get(QuanyiErrorCode.QUANYI_DESTROY_ERROR));
        } finally {
            try {
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Response<List<ThirdQuanyiRpcDTO>> findByExchangeCodeIds(List<Integer> exchangeCodeIds) {
        List<ThirdQuanyiDO> list = thirdQuanyiQryExe.findByExchangeCodeIds(exchangeCodeIds);
        List<ThirdQuanyiRpcDTO> thirdQuanyiRpcDTOS = new ArrayList<>();
        for (ThirdQuanyiDO thirdQuanyiDO : list) {
            ThirdQuanyiRpcDTO thirdQuanyiRpcDTO = new ThirdQuanyiRpcDTO();
            BeanUtils.copyProperties(thirdQuanyiDO, thirdQuanyiRpcDTO);
            thirdQuanyiRpcDTOS.add(thirdQuanyiRpcDTO);
        }
        return Response.of(thirdQuanyiRpcDTOS);
    }
}
