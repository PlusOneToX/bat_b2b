package com.bat.flexible.manager.exchange;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeShareDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeShareDistributorDO;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCardQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.manager.exchange.executor.ExchangeShareCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeShareQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.ExchangeShareServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeSharePageCO;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.exchange.executor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeShareServiceImpl implements ExchangeShareServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeShareServiceImpl.class);

    @Autowired
    private ExchangeShareQryExe exchangeShareQryExe;

    @Autowired
    private ExchangeShareCmdExe exchangeShareCmdExe;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeCardQryExe exchangeCardQryExe;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Override
    public PageInfo<List<ExchangeSharePageCO>> page(ExchangeSharePageQry exchangeSharePageQry) {
        PageHelper.startPage(exchangeSharePageQry.getPage(), exchangeSharePageQry.getSize());
        List<ExchangeSharePageCO> list = exchangeShareQryExe.listCOByCondition(exchangeSharePageQry.getActivityPlatform(), exchangeSharePageQry.getPreferName(),exchangeSharePageQry.getSeat());
        return new PageInfo(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response add(ExchangeShareCmd exchangeShareCmd) {
        if (exchangeShareCmd.getReduceFlag() != null && exchangeShareCmd.getReduceFlag() == FlexibleCommonConstant.FLAG_YES && exchangeShareCmd.getReduceAmount() != null && exchangeShareCmd.getReduceAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FlexibleCustomException("减免邮费不能小于等于0");
        }
        exchangeShareCmdExe.add(exchangeShareCmd);
        return Response.buildSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response update(ExchangeShareCmd exchangeShareCmd) {
        exchangeShareCmdExe.update( exchangeShareCmd);
        return Response.buildSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        exchangeShareCmdExe.delete(id);
    }

    @Override
    public Response<ExchangeShareDetailDTO> detailById(Integer id) {
        return exchangeShareQryExe.detailById(id);
    }

    @Override
    public ExchangeShareDTO find(ExchangeShareQry exchangeShareQry) {
        if (exchangeShareQry.getSecretCodeList()== null || exchangeShareQry.getSecretCodeList().size() == 0) {
            return null;
        }
        //查看是否有合适的活动
        ExchangeShareDO exchangeShareDO = exchangeShareQryExe.findSuitable(exchangeShareQry.getActivityPlatform(), exchangeShareQry.getSeat());
        LOGGER.info("查找活动返回:{}", JSONObject.toJSONString(exchangeShareDO));
        if (exchangeShareDO == null) {
            return null;
        }
        List<ExchangeCodeDO> exchangeCodeDOS = exchangeCodeServiceI.listBySecretCodeList(exchangeShareQry.getSecretCodeList());
        //过滤掉非收运费（赠卡）
        exchangeCodeDOS.removeIf(
                li -> {
                    ExchangeCardDO exchangeCardDO = exchangeCardQryExe.getById(li.getExchangeId());
                    return exchangeCardDO.getMailSetting() != ExchangeConstant.EXCHANGE_MAIL_TYPE_COLLECT_FREIGHT.shortValue();
                }
        );
        //兑换卡支持的发卡分销商
        List<Integer> distributorIds = exchangeCodeDOS.stream().map(ExchangeCodeDO::getDistributorId).collect(Collectors.toList());
        List<ExchangeShareDistributorDO> exchangeShareDistributorDOS;
        //指定全部
        if (exchangeShareDO.getDistributorVisualType() == FlexibleCommonConstant.ALL_TYPE) {
            //查找出不可视的分销商
            exchangeShareDistributorDOS = exchangeShareQryExe.listByExchangeShareIdAndType(exchangeShareDO.getId(), FlexibleCommonConstant.COMMON_OPEN_FLAG_NO);
            List<Integer> disableDistributorIds = exchangeShareDistributorDOS.stream().map(ExchangeShareDistributorDO::getDistributorId).collect(Collectors.toList());
            for (Integer disableDistributorId : disableDistributorIds) {
                List<Integer> distributorTreePaths = flexibleDistributorQryExe.getDistributorTreePaths(disableDistributorId);
                //过滤掉不合适的
                distributorIds.removeIf(distributorTreePaths::contains);
            }
        }
        //指定部分
        if (exchangeShareDO.getDistributorVisualType() == FlexibleCommonConstant.APPOINT_TYPE) {
            //查找可视烦人分销商
            exchangeShareDistributorDOS = exchangeShareQryExe.listByExchangeShareIdAndType(exchangeShareDO.getId(), FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            List<Integer> ableDistributorIds = exchangeShareDistributorDOS.stream().map(ExchangeShareDistributorDO::getDistributorId).collect(Collectors.toList());
            for (Integer ableDistributorId : ableDistributorIds) {
                List<Integer> distributorTreePaths = flexibleDistributorQryExe.getDistributorTreePaths(ableDistributorId);
                //过滤掉不合适的
                distributorIds.removeIf(distributorId -> !distributorTreePaths.contains(distributorId));
            }
        }
        if (distributorIds.size() == 0) {
            return null;
        }
        //减免金额
        BigDecimal reduction = BigDecimal.ZERO;
        if (exchangeShareDO.getReduceFlag() == FlexibleCommonConstant.FLAG_YES && exchangeShareDO.getReduceAmount() != null && exchangeShareDO.getReduceAmount().compareTo(BigDecimal.ZERO) > 0) {
            reduction = exchangeShareDO.getReduceAmount().multiply(new BigDecimal(String.valueOf(distributorIds.size())));
        }
        ExchangeShareDTO exchangeShareDTO = new ExchangeShareDTO();
        BeanUtils.copyProperties(exchangeShareDO, exchangeShareDTO);
        //设置减免金额
        exchangeShareDTO.setReduceAmount(reduction);
        exchangeShareDTO.setDistributorIds(distributorIds);
        LOGGER.info("活动查找返回:{}",JSONObject.toJSONString(exchangeShareDTO));
        return exchangeShareDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExchangeShareCarryOutDTO carryOut(ExchangeShareCarryOutCmd exchangeShareCarryOutCmd, Integer userId) {
        LOGGER.info("转发传参:{},userId:{}",JSONObject.toJSONString(exchangeShareCarryOutCmd),userId);
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO= exchangeShareCmdExe.carryOut(exchangeShareCarryOutCmd,userId);
        return exchangeShareCarryOutDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receive(Integer userId, Integer exchangeSpecialReleaseId, AdminResponse currentAdmin) {
        exchangeShareCmdExe.receive(userId,exchangeSpecialReleaseId,currentAdmin);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExchangeShareCarryOutDTO releaseDetail(Integer exchangeShareReleaseId, Integer userId) {
        return exchangeShareCmdExe.releaseDetail(exchangeShareReleaseId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExchangeShareCarryOutDTO secondCarryOut(Integer exchangeShareReleaseId, Integer userId) {
        return exchangeShareCmdExe.secondCarryOut(exchangeShareReleaseId, userId);
    }

}
