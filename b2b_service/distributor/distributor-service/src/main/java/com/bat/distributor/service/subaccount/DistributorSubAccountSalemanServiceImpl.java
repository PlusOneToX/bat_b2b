package com.bat.distributor.service.subaccount;

import static com.bat.distributor.service.common.Constant.PLATFORM_1;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.base.DistributorOpenFlagCmd;
import com.bat.distributor.api.subaccount.DistributorSubAccountSalemanServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanCmd;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanPageDTO;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanPageQry;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanWechatCmd;
import com.bat.distributor.dao.subaccount.co.SubAccountSalemanPageCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import com.bat.distributor.service.common.*;
import com.bat.distributor.service.distributor.executor.DistributorConfigExe;
import com.bat.distributor.service.distributor.executor.DistributorQryExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountLevelQryExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountSalemanCmdExe;
import com.bat.distributor.service.subaccount.validator.SubAccountValidator;
import com.bat.distributor.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.distributor.service.common.*;
import com.bat.distributor.service.common.financial.FinancialConstant;
import com.bat.distributor.service.common.subaccount.SubAccountErrorCode;
import com.bat.distributor.service.common.subaccount.SubAccountRelationTypeEnum;
import com.bat.distributor.service.common.subaccount.WxSubAccountTypeEnum;
import com.bat.distributor.service.customer.executor.CustomerQryExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountSalemanQryExe;
import com.bat.dubboapi.financial.account.dto.AccountWxDistributorRpcQryDTO;
import com.bat.dubboapi.financial.basesetting.api.FinancialDistributorAccountServiceRpc;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.dubboapi.thirdparty.wx.dto.subaccount.WxSubAccountReleCmd;

@Service
public class DistributorSubAccountSalemanServiceImpl implements DistributorSubAccountSalemanServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributorSubAccountSalemanServiceImpl.class);

    @Autowired
    private DistributorSubAccountSalemanCmdExe distributorSubAccountSalemanCmdExe;

    @Autowired
    private DistributorSubAccountSalemanQryExe distributorSubAccountSalemanQryExe;

    @Autowired
    private SubAccountValidator subAccountValidator;

    @Autowired
    private CustomerQryExe customerQryExe;

    @Autowired
    private DistributorQryExe distributorQryExe;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WxServiceRpc wxServiceRpc;

    // @Value("${wxProgram.appId:null}")
    // private String defaultWxAppId;
    //
    // @Value("${wxProgram.appSecret:null}")
    // private String defaultWxAppSecret;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private FinancialDistributorAccountServiceRpc financialDistributorAccountServiceRpc;

    @Autowired
    private CommonRpcExe commonRpcExe;

    @Autowired
    private DistributorSubAccountLevelQryExe distributorSubAccountLevelQryExe;

    @CreateCache(name = DistributorKeyConstant.DISTRIBUTOR_SALEMAN_QRCODE_URL_KEY)
    private Cache<String, String> qrCodeUrl;

    @Override
    public void updateLevelIdNull(Integer levelId, Integer userId, String userName) {
        distributorSubAccountSalemanCmdExe.updateLevelIdNull(levelId, userId, userName);
    }

    @Resource
    private DistributorConfigExe distributorConfigExe;

    /**
     * 运营后台分页查询
     *
     * @param subAccountSalemanPageQry
     * @return
     */
    @Override
    public PageInfo<SubAccountSalemanPageDTO> pageAdmin(SubAccountSalemanPageQry subAccountSalemanPageQry) {
        PageHelper.startPage(subAccountSalemanPageQry.getPage(), subAccountSalemanPageQry.getSize());
        List<SubAccountSalemanPageCO> list = distributorSubAccountSalemanQryExe.listCOByCondition(
            subAccountSalemanPageQry.getSearchType(), subAccountSalemanPageQry.getContent(),
            subAccountSalemanPageQry.getDistributorId(), subAccountSalemanPageQry.getLevelId());
        PageInfo pageInfo = new PageInfo<>(list);
        if (list != null && list.size() > 0) {
            pageInfo.setList(BeanUtils.copyList(list, SubAccountSalemanPageDTO.class));
        }
        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(SubAccountSalemanCmd subAccountSalemanCmd, String userId, String userName) {
        // 判断电话号码是否重复
        subAccountValidator.validateSalemanMobile(subAccountSalemanCmd.getId(), subAccountSalemanCmd.getMobile(),
            Integer.parseInt(userId));
        DistributorSubAccountSalemanDO distributorSubAccountSalemanDO =
            BeanUtils.copy(subAccountSalemanCmd, DistributorSubAccountSalemanDO.class);
        setAdminMsg(distributorSubAccountSalemanDO, Integer.parseInt(userId), userName);
        // 判断分销商是否创建了财务账户
        com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> response =
            financialDistributorAccountServiceRpc.listWxPayAccountByCondition(
                distributorSubAccountSalemanDO.getDistributorId(), null,
                FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
        if (!response.isSuccess()) {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
        List<AccountWxDistributorRpcQryDTO> qryDTOList = response.getData();
        AccountWxDistributorRpcQryDTO rpcQryDTO = qryDTOList.get(0);
        distributorSubAccountSalemanCmdExe.create(distributorSubAccountSalemanDO);
        if (StringUtils.isBlank(distributorSubAccountSalemanDO.getMerchantNumber())) {
            return;
        }
        bindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
    }

    /**
     * 绑定分账和接收方关系
     *
     * @param distributorSubAccountSalemanDO
     * @param rpcQryDTO
     */
    private void bindWxpaySubAccount(DistributorSubAccountSalemanDO distributorSubAccountSalemanDO,
        AccountWxDistributorRpcQryDTO rpcQryDTO) {
        WxSubAccountReleCmd wxSubAccountReleCmd = new WxSubAccountReleCmd();
        wxSubAccountReleCmd.setAccount(distributorSubAccountSalemanDO.getMerchantNumber());
        wxSubAccountReleCmd.setAppid(rpcQryDTO.getAppId());
        // 子商户号
        wxSubAccountReleCmd.setSub_mchid(rpcQryDTO.getSubMchid());
        // 默认是商户号
        wxSubAccountReleCmd.setType(WxSubAccountTypeEnum.MERCHANT_ID.name());
        wxSubAccountReleCmd.setRelation_type(SubAccountRelationTypeEnum.PARTNER.name());
        wxSubAccountReleCmd.setName(distributorSubAccountSalemanDO.getName());
        if (StringUtils.isBlank(distributorSubAccountSalemanDO.getMerchantNumber())) {
            // OPENID
            wxSubAccountReleCmd.setType(WxSubAccountTypeEnum.PERSONAL_OPENID.name());
            wxSubAccountReleCmd.setAccount(distributorSubAccountSalemanDO.getOpenId());
            // 个人的名称不用传
            wxSubAccountReleCmd.setName(null);
        }
        String name = distributorSubAccountSalemanDO.getName();
        String mobile = distributorSubAccountSalemanDO.getMobile();
        LOGGER.info("用户：{}，手机号：{}， 创建分账关联关系{}", name, mobile, JSON.toJSONString(wxSubAccountReleCmd));
        Response subAccountRela =
            wxServiceRpc.createSubAccountRela(wxSubAccountReleCmd, distributorSubAccountSalemanDO.getDistributorId());
        if (!subAccountRela.isSuccess()) {
            LOGGER.info("创建分账关联关系 失败 用户：{}，手机号：{}，错误代码：{}，错误描述：{} ", name, mobile, subAccountRela.getErrCode(),
                subAccountRela.getErrMessage());
            throw DistributorException.buildException(subAccountRela.getErrCode(), subAccountRela.getErrMessage());
        }
    }

    /**
     * 解绑分账和接收方关系
     *
     * @param distributorSubAccountSalemanDO
     * @param rpcQryDTO
     */
    private void unBindWxpaySubAccount(DistributorSubAccountSalemanDO distributorSubAccountSalemanDO,
        AccountWxDistributorRpcQryDTO rpcQryDTO) {
        WxSubAccountReleCmd wxSubAccountReleCmd = new WxSubAccountReleCmd();
        wxSubAccountReleCmd.setAccount(distributorSubAccountSalemanDO.getMerchantNumber());
        wxSubAccountReleCmd.setAppid(rpcQryDTO.getAppId());
        wxSubAccountReleCmd.setSub_mchid(rpcQryDTO.getSubMchid());
        // 默认是商户号
        wxSubAccountReleCmd.setType(WxSubAccountTypeEnum.MERCHANT_ID.name());
        wxSubAccountReleCmd.setRelation_type(SubAccountRelationTypeEnum.PARTNER.name());
        if (StringUtils.isBlank(distributorSubAccountSalemanDO.getMerchantNumber())) {
            // OPENID
            wxSubAccountReleCmd.setType(WxSubAccountTypeEnum.PERSONAL_OPENID.name());
            wxSubAccountReleCmd.setAccount(distributorSubAccountSalemanDO.getOpenId());
        }
        LOGGER.info("删除分账关联关系{}", JSON.toJSONString(wxSubAccountReleCmd));
        Response subAccountRela =
            wxServiceRpc.deleteSubAccountRela(wxSubAccountReleCmd, distributorSubAccountSalemanDO.getDistributorId());
        if (!subAccountRela.isSuccess()) {
            throw DistributorException.buildException(subAccountRela.getErrCode(), subAccountRela.getErrMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SubAccountSalemanCmd subAccountSalemanCmd, String userId, String userName) {
        // 判断电话号码是否重复
        subAccountValidator.validateSalemanMobile(subAccountSalemanCmd.getId(), subAccountSalemanCmd.getMobile(),
            Integer.parseInt(userId));
        DistributorSubAccountSalemanDO distributorSubAccountSalemanDO =
            distributorSubAccountSalemanQryExe.getById(subAccountSalemanCmd.getId());
        setAdminMsg(distributorSubAccountSalemanDO, Integer.parseInt(userId), userName);
        distributorSubAccountSalemanDO.setName(subAccountSalemanCmd.getName());
        distributorSubAccountSalemanDO.setType(subAccountSalemanCmd.getType());
        distributorSubAccountSalemanDO.setMobile(subAccountSalemanCmd.getMobile());
        distributorSubAccountSalemanDO.setLevelId(subAccountSalemanCmd.getLevelId());
        distributorSubAccountSalemanDO.setParentId(subAccountSalemanCmd.getParentId());
        // 判断分销商是否创建了财务账户
        com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> response =
            financialDistributorAccountServiceRpc.listWxPayAccountByCondition(
                distributorSubAccountSalemanDO.getDistributorId(), null,
                FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
        if (!response.isSuccess()) {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
        List<AccountWxDistributorRpcQryDTO> qryDTOList = response.getData();
        AccountWxDistributorRpcQryDTO rpcQryDTO = qryDTOList.get(0);
        // 原本有商户号 修改了商户号
        if (StringUtils.isNotBlank(distributorSubAccountSalemanDO.getMerchantNumber())
            && (StringUtils.isBlank(subAccountSalemanCmd.getMerchantNumber())
                || (StringUtils.isNotBlank(subAccountSalemanCmd.getMerchantNumber()) && !distributorSubAccountSalemanDO
                    .getMerchantNumber().equals(subAccountSalemanCmd.getMerchantNumber())))) {
            // 商户号出现变动、删除之前的关系
            unBindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
        }
        Boolean isUpdateFlag = false;
        // 原本没有商户号 修改有商户号
        if (StringUtils.isNotBlank(subAccountSalemanCmd.getMerchantNumber())
            && (StringUtils.isBlank(distributorSubAccountSalemanDO.getMerchantNumber()) || (!subAccountSalemanCmd
                .getMerchantNumber().equals(distributorSubAccountSalemanDO.getMerchantNumber())))) {
            // 修改的商户号不为空 且 原来商户号为空 或者原来商户号与后续商户号不一样
            isUpdateFlag = true;
        }
        distributorSubAccountSalemanDO.setMerchantNumber(subAccountSalemanCmd.getMerchantNumber());
        distributorSubAccountSalemanCmdExe.update(distributorSubAccountSalemanDO);
        if (isUpdateFlag) {
            bindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
        }
    }

    /**
     * 启用禁用
     *
     * @param subAccountSalemanCmd
     * @param userId
     * @param userName
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOpenFlag(DistributorOpenFlagCmd subAccountSalemanCmd, String userId, String userName) {
        DistributorSubAccountSalemanDO subAccountSalemanDO =
            distributorSubAccountSalemanQryExe.getById(subAccountSalemanCmd.getId());
        if (subAccountSalemanDO.getOpenFlag().equals(subAccountSalemanCmd.getOpenFlag())) {
            // 请勿重复操做
            throw DistributorException.buildException(CommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        setAdminMsg(subAccountSalemanDO, Integer.parseInt(userId), userName);
        subAccountSalemanDO.setOpenFlag(subAccountSalemanCmd.getOpenFlag());
        distributorSubAccountSalemanCmdExe.update(subAccountSalemanDO);
    }

    /**
     * 分销商业务员绑定微信小程序
     *
     * @param subAccountSalemanWechatCmd
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DistributorSubAccountSalemanDO bindWechat(SubAccountSalemanWechatCmd subAccountSalemanWechatCmd) {

        List<DistributorSubAccountSalemanDO> list = distributorSubAccountSalemanQryExe.listByCondition(
            subAccountSalemanWechatCmd.getDistributorId(), subAccountSalemanWechatCmd.getPhone(), null, null);
        if (list == null || list.size() == 0) {
            throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_SALEMAN_MOBILE_ERROR);
        }
        DistributorSubAccountSalemanDO distributorSubAccountSalemanDO = list.get(0);
        if (Constant.OPEN_FLAG_0.equals(distributorSubAccountSalemanDO.getOpenFlag())) {
            throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_SALEMAN_STATUS_FREEZE);
        }

        // 校验短信验证码
        commonRpcExe.checkPhoneVerifyCode(subAccountSalemanWechatCmd.getPhone(),
            subAccountSalemanWechatCmd.getCodeType(), subAccountSalemanWechatCmd.getCode());
        AccountWxDistributorRpcQryDTO rpcQryDTO = null;
        if (StringUtils.isNotBlank(distributorSubAccountSalemanDO.getOpenId())
            && !subAccountSalemanWechatCmd.getOpenId().equals(distributorSubAccountSalemanDO.getOpenId())) {
            // 修改了openid、删除原来的关系
            com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> response =
                financialDistributorAccountServiceRpc.listWxPayAccountByCondition(
                    distributorSubAccountSalemanDO.getDistributorId(), null,
                    FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
            if (!response.isSuccess()) {
                throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
            }
            List<AccountWxDistributorRpcQryDTO> qryDTOList = response.getData();
            rpcQryDTO = qryDTOList.get(0);
            unBindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
        }
        distributorSubAccountSalemanDO.setOpenId(subAccountSalemanWechatCmd.getOpenId());
        distributorSubAccountSalemanCmdExe.update(distributorSubAccountSalemanDO);
        if (StringUtils.isBlank(distributorSubAccountSalemanDO.getMerchantNumber())) {
            // 原来商户号是空、绑定了openid
            // 判断分销商是否创建了财务账户
            if (rpcQryDTO != null) {
                bindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
                return distributorSubAccountSalemanDO;
            }
            com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> response =
                financialDistributorAccountServiceRpc.listWxPayAccountByCondition(
                    distributorSubAccountSalemanDO.getDistributorId(), null,
                    FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
            if (!response.isSuccess()) {
                throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
            }
            List<AccountWxDistributorRpcQryDTO> qryDTOList = response.getData();
            rpcQryDTO = qryDTOList.get(0);
            bindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
        }

        return distributorSubAccountSalemanDO;
    }

    /**
     * 根据分销商ID和状态查询业务员
     *
     * @param distributorId
     * @param openFlag
     * @return
     */
    @Override
    public List<DistributorSubAccountSalemanDO> listByCondition(Integer distributorId, String mobile, Short openFlag,
        Integer sonLevelId) {
        return distributorSubAccountSalemanQryExe.listByCondition(distributorId, mobile, openFlag, sonLevelId);
    }

    /**
     * 获取分销商绑定业务员二维码
     *
     * 之前是小程序吗 现在改成H5网页
     *
     * @param distributorId
     * @return
     */
    @Override
    public String getWechatProgramCodeUrl(Integer distributorId) {
        // DistributorDO distributorDO = distributorQryExe.getDistributorById(distributorId);
        // scene携带多个参数、会将&转义、导致无法生存二维码、所以用下划线隔开pages/distributor/saleman
        // distributorId = 1879;
        String url = qrCodeUrl.get(TenantContext.getTenantNo() + ":" + distributorId + "mp");
        if (StringUtils.isNotBlank(url)) {
            return url;
        }
        String scene = String.valueOf(distributorId);
        com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> listResponse =
            financialDistributorAccountServiceRpc.listWxPayAccountByCondition(distributorId, null,
                FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
        if (!listResponse.isSuccess()) {
            throw DistributorException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
        List<AccountWxDistributorRpcQryDTO> rpcQryDTOS = listResponse.getData();
        scene = scene + "_" + rpcQryDTOS.get(0).getAppId();//
        Response<String> response =
            wxServiceRpc.createWechatProgramQrCode(distributorConfigExe.getConfig().getWxMiniProgramAppId(), scene,
                "pages/index/bindSaleman", System.currentTimeMillis() + ".jpg", "subAccount/qr/",
                distributorConfigExe.getConfig().getWxMiniProgramAppSecret());
        if (!response.isSuccess()) {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
        qrCodeUrl.put(TenantContext.getTenantNo() + ":" + String.valueOf(distributorId), response.getData());
        return response.getData();
    }

    @Override
    public String getWechatH5CodeUrl(Integer distributorId) {
        String url = qrCodeUrl.get(TenantContext.getTenantNo() + ":" + distributorId + "h5");
        if (StringUtils.isNotBlank(url)) {
            return url;
        }
        com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> listResponse =
            financialDistributorAccountServiceRpc.listWxPayAccountByCondition(distributorId, null,
                FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
        if (!listResponse.isSuccess()) {
            throw DistributorException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
        List<AccountWxDistributorRpcQryDTO> rpcQryDTOS = listResponse.getData();
        String appId = rpcQryDTOS.get(0).getAppId();
        Response<String> response = wxServiceRpc.createWechatH5QrCode(PLATFORM_1, distributorId, appId);
        if (!response.isSuccess()) {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
        qrCodeUrl.put(TenantContext.getTenantNo() + ":" + distributorId, response.getData());
        return response.getData();
    }

    @Override
    public void test() {
        WxSubAccountReleCmd releCmd = new WxSubAccountReleCmd();
        releCmd.setType("PERSONAL_OPENID");
        releCmd.setRelation_type("PARTNER");
        releCmd.setAppid("wx95f67ba8c0ca17d1");
        releCmd.setSub_mchid("1613278613");
        releCmd.setAccount("otq5R6MvlWpXbnB_R7nq7EpFgCh4");
        // releCmd.setName("");
        wxServiceRpc.createSubAccountRela(releCmd, 1879);
    }

    /**
     * 业务员excel导入
     * 
     * @param inputStream
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public com.bat.distributor.api.base.Response importSaleman(InputStream inputStream, Integer distributorId,
                                                                 String distributorName) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception ex) {
            LOGGER.error("EXCEL导入失败");
            throw DistributorException.buildException(CommonErrorCode.COMMON_EXCEL_PARSING_ERROR);
        }

        // 获取当前分销商的所有业务员
        List<DistributorSubAccountSalemanDO> salemanDOList =
            distributorSubAccountSalemanQryExe.listByCondition(distributorId, null, null, null);
        List<DistributorSubAccountLevelDO> levelDOList =
            distributorSubAccountLevelQryExe.listByDistributorId(distributorId);
        List<DistributorSubAccountSalemanDO> list =
            SubAccountValidator.validImportData(distributorId, distributorName, workbook, salemanDOList, levelDOList);
        if (list == null || list.size() == 0) {
            throw DistributorException.buildException(CommonErrorCode.COMMON_EXCEL_IMPORT_DATA_NULL);
        }
        list.stream().forEach(distributorSubAccountSalemanDO -> {
            setAdminMsg(distributorSubAccountSalemanDO, distributorId, distributorName);
        });
        distributorSubAccountSalemanCmdExe.batchCreate(list);
        // 处理商户号绑定
        com.bat.dubboapi.financial.common.Response<List<AccountWxDistributorRpcQryDTO>> response =
            financialDistributorAccountServiceRpc.listWxPayAccountByCondition(list.get(0).getDistributorId(), null,
                FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
        if (!response.isSuccess()) {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
        List<AccountWxDistributorRpcQryDTO> qryDTOList = response.getData();
        AccountWxDistributorRpcQryDTO rpcQryDTO = qryDTOList.get(0);
        list.stream().forEach(distributorSubAccountSalemanDO -> {
            if (StringUtils.isNotBlank(distributorSubAccountSalemanDO.getMerchantNumber())) {
                bindWxpaySubAccount(distributorSubAccountSalemanDO, rpcQryDTO);
            }
        });
        return com.bat.distributor.api.base.Response.buildSuccess();
    }

    private void setAdminMsg(DistributorSubAccountSalemanDO distributorSubAccountSalemanDO, Integer distributorId,
        String userName) {
        if (distributorSubAccountSalemanDO.getId() == null) {
            distributorSubAccountSalemanDO.setCreateTime(new Date());
            distributorSubAccountSalemanDO.setCreateUserId(distributorId);
            distributorSubAccountSalemanDO.setCreateUserName(userName);
            distributorSubAccountSalemanDO.setDistributorId(distributorId);
            distributorSubAccountSalemanDO.setOpenFlag(DistributorCommonConstant.COMMON_OPEN_FLAG_YES);
            distributorSubAccountSalemanDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_NO);
        }
        distributorSubAccountSalemanDO.setUpdateTime(new Date());
        distributorSubAccountSalemanDO.setUpdateUserId(distributorId);
        distributorSubAccountSalemanDO.setUpdateUserName(userName);
    }
}
