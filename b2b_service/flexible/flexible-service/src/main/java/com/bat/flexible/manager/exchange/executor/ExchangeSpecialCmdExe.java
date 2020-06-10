package com.bat.flexible.manager.exchange.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

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
import com.bat.flexible.api.exchange.dto.ExchangeSpecialCmd;
import com.bat.flexible.api.exchange.dto.ExchangeSpecialDistributorAddCmd;
import com.bat.flexible.api.exchange.dto.ExchangeSpecialDistributorUpdateCmd;
import com.bat.flexible.dao.exchange.ExchangeSpecialDistributorMapper;
import com.bat.flexible.dao.exchange.ExchangeSpecialMapper;
import com.bat.flexible.dao.exchange.ExchangeSpecialReleaseMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialDistributorDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialReleaseDO;
import com.bat.flexible.manager.common.ConfigQry;
import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.dubbo.DistributorConstant;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@Component
public class ExchangeSpecialCmdExe {

    @Autowired
    private ExchangeSpecialMapper exchangeSpecialMapper;

    @Autowired
    private ExchangeSpecialDistributorMapper exchangeSpecialDistributorMapper;

    @Autowired
    private ExchangeSpecialReleaseMapper exchangeSpecialReleaseMapper;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WxServiceRpc wxServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @Resource
    private ConfigQry configQry;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeSpecialCmdExe.class);

    public void add(ExchangeSpecialCmd exchangeSpecialCmd) {
        Date date = new Date();
        ExchangeSpecialDO exchangeSpecialDO = new ExchangeSpecialDO();
        BeanUtils.copyProperties(exchangeSpecialCmd, exchangeSpecialDO);
        exchangeSpecialDO.setStatus(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        exchangeSpecialDO.setCreateTime(date);
        exchangeSpecialMapper.insert(exchangeSpecialDO);
        List<ExchangeSpecialDistributorDO> exchangeSpecialDistributorDOS = new ArrayList<>();
        for (Integer distributorId : exchangeSpecialCmd.getDistributorIds()) {
            ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = new ExchangeSpecialDistributorDO();
            exchangeSpecialDistributorDO.setDistributorId(distributorId);
            exchangeSpecialDistributorDO.setExchangeSpecialId(exchangeSpecialDO.getId());
            exchangeSpecialDistributorDO.setStatus(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            exchangeSpecialDistributorDOS.add(exchangeSpecialDistributorDO);
        }
        if (exchangeSpecialDistributorDOS.size() == 0) {
            throw new FlexibleCustomException("必须选择分销商!");
        }
        exchangeSpecialDistributorMapper.insertList(exchangeSpecialDistributorDOS);
    }

    public void update(ExchangeSpecialCmd exchangeSpecialCmd) {
        ExchangeSpecialDO exchangeSpecialDO = new ExchangeSpecialDO();
        BeanUtils.copyProperties(exchangeSpecialCmd, exchangeSpecialDO);
        exchangeSpecialDO.setUpdateTime(new Date());
        exchangeSpecialMapper.updateByPrimaryKeySelective(exchangeSpecialDO);
    }

    public void distributorAdd(ExchangeSpecialDistributorAddCmd exchangeSpecialDistributorAddCmd) {
        List<ExchangeSpecialDistributorDO> exchangeSpecialDistributorDOS = new ArrayList<>();
        for (Integer distributorId : exchangeSpecialDistributorAddCmd.getDistributorIds()) {
            ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = new ExchangeSpecialDistributorDO();
            exchangeSpecialDistributorDO.setDistributorId(distributorId);
            exchangeSpecialDistributorDO.setExchangeSpecialId(exchangeSpecialDistributorAddCmd.getExchangeSpecialId());
            exchangeSpecialDistributorDO.setStatus(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            exchangeSpecialDistributorDOS.add(exchangeSpecialDistributorDO);
        }
        if (exchangeSpecialDistributorDOS.size() == 0) {
            throw new FlexibleCustomException("必须选择分销商!");
        }
        exchangeSpecialDistributorMapper.insertList(exchangeSpecialDistributorDOS);
    }

    public void distributorUpdate(ExchangeSpecialDistributorUpdateCmd exchangeSpecialDistributorUpdateCmd) {
        ExchangeSpecialDistributorDO exchangeSpecialDistributorDO = new ExchangeSpecialDistributorDO();
        BeanUtils.copyProperties(exchangeSpecialDistributorUpdateCmd, exchangeSpecialDistributorDO);
        exchangeSpecialDistributorMapper.updateByPrimaryKeySelective(exchangeSpecialDistributorDO);
    }

    public String qrCodeUrl(Integer exchangeSpecialDistributorId) {
        ExchangeSpecialDistributorDO exchangeSpecialDistributorDO =
            exchangeSpecialDistributorMapper.selectByPrimaryKey(exchangeSpecialDistributorId);
        if (exchangeSpecialDistributorDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        if (StringUtils.isNotBlank(exchangeSpecialDistributorDO.getQrCodeUrl())) {
            return exchangeSpecialDistributorDO.getQrCodeUrl();
        }
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO =
            exchangeSpecialReleaseMapper.findCodeRecordBySpecialDistributorId(exchangeSpecialDistributorDO.getId());
        if (exchangeSpecialReleaseDO == null) {
            exchangeSpecialReleaseDO = new ExchangeSpecialReleaseDO();
            exchangeSpecialReleaseDO.setExchangeShareId(0);
            exchangeSpecialReleaseDO.setExchangeSpecialId(exchangeSpecialDistributorDO.getExchangeSpecialId());
            exchangeSpecialReleaseDO.setExchangeSpecialDistributorId(exchangeSpecialDistributorDO.getId());
            exchangeSpecialReleaseDO.setDistributorId(exchangeSpecialDistributorDO.getDistributorId());
            exchangeSpecialReleaseDO.setUserId(0);
            exchangeSpecialReleaseDO.setCreateTime(new Date());
            exchangeSpecialReleaseMapper.insert(exchangeSpecialReleaseDO);
        }
        // 获取ppid
        // 平台类型 1、公众号、2小程序
        FlexibleConfig flexibleConfig=configQry.getTenantExchangeDistributorId();
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
        LOGGER.info("发布详情:{}",JSONObject.toJSONString(exchangeSpecialReleaseDO));
        com.bat.dubboapi.thirdparty.common.Response<String> response = wxServiceRpc.createWechatProgramQrCode(platformRpcDTOList.get(0).getAppId(), exchangeSpecialReleaseDO.getId().toString(), "pages/activity/activity",
                System.currentTimeMillis() + ".jpg", "exchange/qr/special/distributor/",null);
        if (response == null || !response.isSuccess() || StringUtils.isBlank(response.getData())) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL);
        }
        exchangeSpecialDistributorDO.setQrCodeUrl(response.getData());
        exchangeSpecialDistributorMapper.updateByPrimaryKey(exchangeSpecialDistributorDO);
        return exchangeSpecialDistributorDO.getQrCodeUrl();
    }

    public String shortLink(Integer exchangeSpecialDistributorId) {
        ExchangeSpecialDistributorDO exchangeSpecialDistributorDO =
            exchangeSpecialDistributorMapper.selectByPrimaryKey(exchangeSpecialDistributorId);
        if (exchangeSpecialDistributorDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        if (StringUtils.isNotBlank(exchangeSpecialDistributorDO.getShortLink())) {
            return exchangeSpecialDistributorDO.getShortLink();
        }
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO =
            exchangeSpecialReleaseMapper.findCodeRecordBySpecialDistributorId(exchangeSpecialDistributorDO.getId());
        if (exchangeSpecialReleaseDO == null) {
            exchangeSpecialReleaseDO = new ExchangeSpecialReleaseDO();
            exchangeSpecialReleaseDO.setExchangeShareId(0);
            exchangeSpecialReleaseDO.setExchangeSpecialId(exchangeSpecialDistributorDO.getExchangeSpecialId());
            exchangeSpecialReleaseDO.setExchangeSpecialDistributorId(exchangeSpecialDistributorDO.getId());
            exchangeSpecialReleaseDO.setDistributorId(exchangeSpecialDistributorDO.getDistributorId());
            exchangeSpecialReleaseDO.setUserId(0);
            exchangeSpecialReleaseDO.setCreateTime(new Date());
            exchangeSpecialReleaseMapper.insert(exchangeSpecialReleaseDO);
        }
        exchangeSpecialDistributorDO.setShortLink("thirdparty/lk?id=" + exchangeSpecialReleaseDO.getId().toString());
        exchangeSpecialDistributorMapper.updateByPrimaryKey(exchangeSpecialDistributorDO);
        return exchangeSpecialDistributorDO.getShortLink();
    }

    public String link(Integer exchangeSpecialDistributorId) {
        ExchangeSpecialDistributorDO exchangeSpecialDistributorDO =
            exchangeSpecialDistributorMapper.selectByPrimaryKey(exchangeSpecialDistributorId);
        if (exchangeSpecialDistributorDO == null) {
            throw new FlexibleCustomException("找不到记录");
        }
        if (StringUtils.isNotBlank(exchangeSpecialDistributorDO.getLink())) {
            return exchangeSpecialDistributorDO.getLink();
        }
        ExchangeSpecialReleaseDO exchangeSpecialReleaseDO =
            exchangeSpecialReleaseMapper.findCodeRecordBySpecialDistributorId(exchangeSpecialDistributorDO.getId());
        if (exchangeSpecialReleaseDO == null) {
            exchangeSpecialReleaseDO = new ExchangeSpecialReleaseDO();
            exchangeSpecialReleaseDO.setExchangeShareId(0);
            exchangeSpecialReleaseDO.setExchangeSpecialId(exchangeSpecialDistributorDO.getExchangeSpecialId());
            exchangeSpecialReleaseDO.setExchangeSpecialDistributorId(exchangeSpecialDistributorDO.getId());
            exchangeSpecialReleaseDO.setDistributorId(exchangeSpecialDistributorDO.getDistributorId());
            exchangeSpecialReleaseDO.setUserId(0);
            exchangeSpecialReleaseDO.setCreateTime(new Date());
            exchangeSpecialReleaseMapper.insert(exchangeSpecialReleaseDO);
        }
        exchangeSpecialDistributorDO
            .setLink("pages/activity/activity?id=" + exchangeSpecialReleaseDO.getId().toString());
        exchangeSpecialDistributorMapper.updateByPrimaryKey(exchangeSpecialDistributorDO);
        return exchangeSpecialDistributorDO.getLink();
    }
}
