package com.bat.flexible.manager.exchange.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.dao.exchange.*;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeUserCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeShareCarryOutCmd;
import com.bat.flexible.api.exchange.dto.ExchangeShareCarryOutDTO;
import com.bat.flexible.api.exchange.dto.ExchangeShareCmd;
import com.bat.flexible.dao.exchange.*;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import com.bat.flexible.dao.exchange.dataobject.*;
import com.bat.flexible.manager.common.ConfigQry;
import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.dubbo.DistributorConstant;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@Component
public class ExchangeShareCmdExe {

    @Autowired
    private ExchangeShareMapper exchangeShareMapper;

    @Autowired
    private ExchangeShareDistributorMapper exchangeShareDistributorMapper;

    @Autowired
    private ExchangeSpecialMapper exchangeSpecialMapper;

    @Autowired
    private ExchangeSpecialDistributorMapper exchangeSpecialDistributorMapper;

    @Autowired
    private ExchangeSpecialReleaseMapper exchangeSpecialReleaseMapper;

    @Autowired
    private ExchangeSpecialReceiveMapper exchangeSpecialReceiveMapper;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WxServiceRpc wxServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @Resource
    private ConfigQry configQry;

    @Autowired
    private ExchangeCodeUserCmdExe exchangeCodeUserCmdExe;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeShareCmdExe.class);

    public void add(ExchangeShareCmd exchangeShareCmd) {
        ExchangeShareDO exchangeShareDO = new ExchangeShareDO();
        BeanUtils.copyProperties(exchangeShareCmd, exchangeShareDO);
        exchangeShareDO.setStatus(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        exchangeShareDO.setCreateTime(new Date());
        exchangeShareMapper.insertSelective(exchangeShareDO);
        if (exchangeShareCmd.getDistributorIds() != null && exchangeShareCmd.getDistributorIds().size() > 0) {
            short type = FlexibleCommonConstant.COMMON_OPEN_FLAG_YES;
            // 指定全部
            if (exchangeShareCmd.getDistributorVisualType() == FlexibleCommonConstant.ALL_TYPE) {
                type = FlexibleCommonConstant.COMMON_OPEN_FLAG_NO;
            }
            List<ExchangeShareDistributorDO> exchangeShareDistributorDOS = new ArrayList<>();
            for (Integer distributorId : exchangeShareCmd.getDistributorIds()) {
                ExchangeShareDistributorDO exchangeSpecialDistributorDO = new ExchangeShareDistributorDO();
                exchangeSpecialDistributorDO.setDistributorId(distributorId);
                exchangeSpecialDistributorDO.setExchangeShareId(exchangeShareDO.getId());
                exchangeSpecialDistributorDO.setType(type);
                exchangeShareDistributorDOS.add(exchangeSpecialDistributorDO);
            }
            exchangeShareDistributorMapper.insertList(exchangeShareDistributorDOS);
        }
    }

    public void update(ExchangeShareCmd exchangeShareCmd) {
        ExchangeShareDO exchangeShareDO = new ExchangeShareDO();
        BeanUtils.copyProperties(exchangeShareCmd, exchangeShareDO);
        exchangeShareDO.setUpdateTime(new Date());
        exchangeShareMapper.updateByPrimaryKeySelective(exchangeShareDO);
        if (exchangeShareCmd.getDistributorIds() != null && exchangeShareCmd.getDistributorIds().size() > 0) {
            short type = FlexibleCommonConstant.COMMON_OPEN_FLAG_YES;
            // 指定全部
            if (exchangeShareCmd.getDistributorVisualType() == FlexibleCommonConstant.ALL_TYPE) {
                type = FlexibleCommonConstant.COMMON_OPEN_FLAG_NO;
            }
            // 查出已有的分销商关系信息
            List<ExchangeShareDistributorDO> hasExchangeShareDistributorDOS =
                exchangeShareDistributorMapper.listByExchangeShareIdAndType(exchangeShareDO.getId(), type);
            List<Integer> hasDistributorIds = hasExchangeShareDistributorDOS.stream()
                .map(ExchangeShareDistributorDO::getDistributorId).collect(Collectors.toList());

            // 需要删除的集合
            List<Integer> deleteExchangeShareDistributorIds = new ArrayList<>();
            for (ExchangeShareDistributorDO exchangeShareDistributorDO : hasExchangeShareDistributorDOS) {
                if (!exchangeShareCmd.getDistributorIds().contains(exchangeShareDistributorDO.getDistributorId())) {
                    deleteExchangeShareDistributorIds.add(exchangeShareDistributorDO.getId());
                }
            }
            if (deleteExchangeShareDistributorIds.size() > 0) {
                exchangeShareDistributorMapper.deleteByPrimaryKeys(deleteExchangeShareDistributorIds);
            }

            // 需要添加的分销商集合
            List<Integer> addDistributorIds = new ArrayList<>();
            for (Integer distributorId : exchangeShareCmd.getDistributorIds()) {
                if (!hasDistributorIds.contains(distributorId)) {
                    addDistributorIds.add(distributorId);
                }
            }
            List<ExchangeShareDistributorDO> exchangeShareDistributorDOS = new ArrayList<>();
            for (Integer distributorId : addDistributorIds) {
                ExchangeShareDistributorDO exchangeSpecialDistributorDO = new ExchangeShareDistributorDO();
                exchangeSpecialDistributorDO.setDistributorId(distributorId);
                exchangeSpecialDistributorDO.setExchangeShareId(exchangeShareDO.getId());
                exchangeSpecialDistributorDO.setType(type);
                exchangeShareDistributorDOS.add(exchangeSpecialDistributorDO);
            }
            if(exchangeShareDistributorDOS.size()>0) {
                exchangeShareDistributorMapper.insertList(exchangeShareDistributorDOS);
            }
        }

    }

    public void delete(Integer id) {
        exchangeShareMapper.deleteByPrimaryKey(id);
        exchangeShareDistributorMapper.deleteByExchangeShareId(id);
    }

    public ExchangeShareCarryOutDTO carryOut(ExchangeShareCarryOutCmd exchangeShareCarryOutCmd, Integer userId) {
        if (userId <= 0) {
            throw new FlexibleCustomException("用户未登录");
        }
        ExchangeShareDO exchangeShareDO = exchangeShareMapper.selectByPrimaryKey(exchangeShareCarryOutCmd.getId());
        if (exchangeShareDO == null) {
            LOGGER.error("找不到转发配置信息，id{}", exchangeShareCarryOutCmd.getId());
            throw new FlexibleCustomException("找不到记录");
        }
        ExchangeSpecialDO exchangeSpecialDO =
            exchangeSpecialMapper.selectByPrimaryKey(exchangeShareDO.getExchangeSpecialId());
        if (exchangeSpecialDO == null) {
            LOGGER.error("找不到专题配置信息，id{}", exchangeShareDO.getExchangeSpecialId());
            throw new FlexibleCustomException("找不到记录");
        }
        // 转发配置转发次数+1
        exchangeShareDO.setForwardNum(exchangeShareDO.getForwardNum() + 1);
        exchangeShareMapper.updateByPrimaryKey(exchangeShareDO);
        List<ExchangeSpecialDistributorCO> list = exchangeSpecialDistributorMapper
            .listCOByCondition(exchangeSpecialDO.getId(), exchangeShareCarryOutCmd.getDistributorId());
        Integer exchangeSpecialDistributorId = null;
        if (list.size() > 0) {
            exchangeSpecialDistributorId = list.get(0).getId();
            ExchangeSpecialDistributorCO exchangeSpecialDistributorCO = list.get(0);
            ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = new ExchangeSpecialDistributorDO();
            exchangeSpecialDistributorDO.setId(exchangeSpecialDistributorCO.getId());
            exchangeSpecialDistributorDO.setOneForwardTimes(exchangeSpecialDistributorCO.getOneForwardTimes() + 1);
            // 增加分销商记录一次转发的次数
            exchangeSpecialDistributorMapper.updateByPrimaryKeySelective(exchangeSpecialDistributorDO);
        }
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO = new ExchangeShareCarryOutDTO();
        BeanUtils.copyProperties(exchangeSpecialDO, exchangeShareCarryOutDTO);
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO = new ExchangeSpecialReleaseDO();
        exchangeSpecialReleaseDO.setExchangeShareId(exchangeShareDO.getId());
        exchangeSpecialReleaseDO.setExchangeSpecialId(exchangeShareDO.getExchangeSpecialId());
        exchangeSpecialReleaseDO.setExchangeSpecialDistributorId(exchangeSpecialDistributorId);
        exchangeSpecialReleaseDO.setDistributorId(exchangeShareCarryOutCmd.getDistributorId());
        exchangeSpecialReleaseDO.setUserId(userId);
        exchangeSpecialReleaseDO.setCreateTime(new Date());
        exchangeSpecialReleaseMapper.insert(exchangeSpecialReleaseDO);
        BeanUtils.copyProperties(exchangeSpecialReleaseDO, exchangeShareCarryOutDTO);
        exchangeShareCarryOutDTO.setExchangeSpecialReleaseId(exchangeSpecialReleaseDO.getId());
        return exchangeShareCarryOutDTO;
    }

    public void receive(Integer userId, Integer exchangeSpecialReleaseId, AdminResponse currentAdmin) {
        if (userId <= 0) {
            throw new FlexibleCustomException("用户未登录");
        }
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO =
            exchangeSpecialReleaseMapper.selectByPrimaryKey(exchangeSpecialReleaseId);
        LOGGER.info("发布记录:{}", JSONObject.toJSONString(exchangeSpecialReleaseDO));
        if (exchangeSpecialReleaseDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        // 查看该用户是否领取过当前专题以及当前分销商转发的卡
        int receiveTimes = exchangeSpecialReleaseMapper.countReceive(exchangeSpecialReleaseDO.getExchangeSpecialId(),
            exchangeSpecialReleaseDO.getDistributorId(), userId);
        if (receiveTimes > 0) {
            throw new FlexibleCustomException("已领取过,不能重复领取");
        }
        if (exchangeSpecialReleaseDO.getExchangeSpecialDistributorId() != null) {
            ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = exchangeSpecialDistributorMapper
                .selectByPrimaryKey(exchangeSpecialReleaseDO.getExchangeSpecialDistributorId());
            if (exchangeSpecialDistributorDO != null) {
                // 领取次数+1
                exchangeSpecialDistributorDO.setReceiveTimes(exchangeSpecialDistributorDO.getReceiveTimes() + 1);
                exchangeSpecialDistributorMapper.updateByPrimaryKeySelective(exchangeSpecialDistributorDO);
            }
        }
        ExchangeSpecialDO exchangeSpecialDO =
            exchangeSpecialMapper.selectByPrimaryKey(exchangeSpecialReleaseDO.getExchangeSpecialId());
        if (exchangeSpecialDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        if (exchangeSpecialDO.getEndTime().before(new Date())) {
            throw new FlexibleCustomException("专题活动已过期！");
        }
        // 生成兑换码
        List<ExchangeCodeDO> exchangeCodeDOS = exchangeCodeServiceI.createEleActivateCode(
            exchangeSpecialDO.getExchangeId(), exchangeSpecialReleaseDO.getDistributorId(), 1, currentAdmin);
        if (exchangeCodeDOS.size() != 1) {
            throw new FlexibleCustomException("兑换码生成错误！");
        }
        Date date = new Date();
        ExchangeSpecialReceiveDO exchangeSpecialReceiveDO = new ExchangeSpecialReceiveDO();
        exchangeSpecialReceiveDO.setSpecialReleaseId(exchangeSpecialReleaseDO.getId());
        exchangeSpecialReceiveDO.setCreateTime(date);
        exchangeSpecialReceiveDO.setUserId(userId);
        exchangeSpecialReceiveDO.setExchangeCodeId(exchangeCodeDOS.get(0).getId());
        // 保存领取记录
        exchangeSpecialReceiveMapper.insert(exchangeSpecialReceiveDO);

        ExchangeCodeUserDO exchangeCodeUserDO = new ExchangeCodeUserDO();
        exchangeCodeUserDO.setExchangeCodeId(exchangeCodeDOS.get(0).getId());
        exchangeCodeUserDO.setUserId(userId);
        exchangeCodeUserDO.setCreateTime(date);
        // 绑定记录
        exchangeCodeUserCmdExe.create(exchangeCodeUserDO);

    }

    public ExchangeShareCarryOutDTO releaseDetail(Integer exchangeShareReleaseId, Integer userId) {
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO =
            exchangeSpecialReleaseMapper.selectByPrimaryKey(exchangeShareReleaseId);
        if (exchangeSpecialReleaseDO == null) {
            LOGGER.error("找不到发布记录:{}", exchangeShareReleaseId);
            throw new FlexibleCustomException("找不到记录");
        }
        ExchangeSpecialDO exchangeSpecialDO =
            exchangeSpecialMapper.selectByPrimaryKey(exchangeSpecialReleaseDO.getExchangeSpecialId());
        if (exchangeSpecialDO == null) {
            LOGGER.error("找不到专题记录:{}", exchangeSpecialReleaseDO.getExchangeSpecialId());
            throw new FlexibleCustomException("找不到记录");
        }
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO = new ExchangeShareCarryOutDTO();
        BeanUtils.copyProperties(exchangeSpecialDO, exchangeShareCarryOutDTO);
        BeanUtils.copyProperties(exchangeSpecialReleaseDO, exchangeShareCarryOutDTO);
        exchangeShareCarryOutDTO.setExchangeSpecialReleaseId(exchangeSpecialReleaseDO.getId());
        ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = exchangeSpecialDistributorMapper
            .selectByPrimaryKey(exchangeSpecialReleaseDO.getExchangeSpecialDistributorId());
        if (exchangeSpecialReleaseDO.getExchangeSpecialDistributorId() != null) {
            if (exchangeSpecialDistributorDO != null) {
                // 查看次数+1
                exchangeSpecialDistributorDO.setPageVisits(exchangeSpecialDistributorDO.getPageVisits() + 1);
                exchangeSpecialDistributorMapper.updateByPrimaryKeySelective(exchangeSpecialDistributorDO);
            }
        }
        LOGGER.info("当前用户id:{}", userId);
        if (userId != null && userId > 0) {
            // 查看该用户是否领取过当前专题以及当前分销商转发的卡
            int receiveTimes = exchangeSpecialReleaseMapper.countReceive(
                exchangeSpecialReleaseDO.getExchangeSpecialId(), exchangeSpecialReleaseDO.getDistributorId(), userId);
            LOGGER.info("当前领取次数:{}", receiveTimes);
            if (receiveTimes > 0) {
                exchangeShareCarryOutDTO.setIsReceive(FlexibleCommonConstant.FLAG_YES);
            }
        }
        String qrCodeUrl = qrCodeUrl(exchangeSpecialReleaseDO);
        exchangeShareCarryOutDTO.setQrCodeUrl(qrCodeUrl);
        LOGGER.info("转发详情:{}", JSONObject.toJSONString(exchangeShareCarryOutDTO));
        return exchangeShareCarryOutDTO;
    }

    public ExchangeShareCarryOutDTO secondCarryOut(Integer exchangeShareReleaseId, Integer userId) {
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO =
            exchangeSpecialReleaseMapper.selectByPrimaryKey(exchangeShareReleaseId);
        if (exchangeSpecialReleaseDO == null) {
            LOGGER.error("找不到发布记录:{}", exchangeShareReleaseId);
            throw new FlexibleCustomException("找不到记录");
        }
        ExchangeSpecialDO exchangeSpecialDO =
            exchangeSpecialMapper.selectByPrimaryKey(exchangeSpecialReleaseDO.getExchangeSpecialId());
        if (exchangeSpecialDO == null) {
            LOGGER.error("找不到专题记录:{}", exchangeSpecialReleaseDO.getExchangeSpecialId());
            throw new FlexibleCustomException("找不到记录");
        }
        ExchangeShareCarryOutDTO exchangeShareCarryOutDTO = new ExchangeShareCarryOutDTO();
        BeanUtils.copyProperties(exchangeSpecialDO, exchangeShareCarryOutDTO);
        BeanUtils.copyProperties(exchangeSpecialReleaseDO, exchangeShareCarryOutDTO);
        exchangeShareCarryOutDTO.setExchangeSpecialReleaseId(exchangeSpecialReleaseDO.getId());
        ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = exchangeSpecialDistributorMapper
            .selectByPrimaryKey(exchangeSpecialReleaseDO.getExchangeSpecialDistributorId());
        if (exchangeSpecialReleaseDO.getExchangeSpecialDistributorId() != null) {
            if (exchangeSpecialDistributorDO != null) {
                // 二次转发次数+1
                exchangeSpecialDistributorDO.setTwoForwardTimes(exchangeSpecialDistributorDO.getTwoForwardTimes() + 1);
                exchangeSpecialDistributorMapper.updateByPrimaryKeySelective(exchangeSpecialDistributorDO);
            }
        }
        LOGGER.info("当前用户id:{}", userId);
        if (userId != null && userId > 0) {
            // 查看该用户是否领取过当前专题以及当前分销商转发的卡
            int receiveTimes = exchangeSpecialReleaseMapper.countReceive(
                exchangeSpecialReleaseDO.getExchangeSpecialId(), exchangeSpecialReleaseDO.getDistributorId(), userId);
            LOGGER.info("当前领取次数:{}", receiveTimes);
            if (receiveTimes > 0) {
                exchangeShareCarryOutDTO.setIsReceive(FlexibleCommonConstant.FLAG_YES);
            }
        }
        String qrCodeUrl = qrCodeUrl(exchangeSpecialReleaseDO);
        exchangeShareCarryOutDTO.setQrCodeUrl(qrCodeUrl);
        LOGGER.info("转发详情:{}", JSONObject.toJSONString(exchangeShareCarryOutDTO));
        return exchangeShareCarryOutDTO;
    }

    public String qrCodeUrl(ExchangeSpecialReleaseDO exchangeSpecialReleaseDO) {
        // 获取ppid
        // 平台类型 1、公众号、2小程序
        FlexibleConfig flexibleConfig= configQry.getTenantExchangeDistributorId();
        com.bat.dubboapi.distributor.common.Response<List<WxPlatformRpcDTO>> distributorResponse =
            wxPlatformServiceRpc.listByDistributorIdAndType(flexibleConfig.getExchangeDistributorId(),
                DistributorConstant.DISTRIBUTOR_WECHAT_TYPE_MINI_PROGRAM);
        if (distributorResponse == null || !distributorResponse.isSuccess()) {
            throw new FlexibleCustomException(FlexibleDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        List<WxPlatformRpcDTO> platformRpcDTOList = distributorResponse.getData();
        if (platformRpcDTOList == null || platformRpcDTOList.size() == 0) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL_DISTRIBUTOR_RELA_WECHAT_NULL);
        }
        com.bat.dubboapi.thirdparty.common.Response<String> response = wxServiceRpc.createWechatProgramQrCode(platformRpcDTOList.get(0).getAppId(),  exchangeSpecialReleaseDO.getId().toString(), "pages/activity/activity",
                System.currentTimeMillis() + ".jpg", "exchange/qr/special/distributor/",null);
        if (response == null || !response.isSuccess() || StringUtils.isBlank(response.getData())) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL);
        }
        return response.getData();
    }


}
