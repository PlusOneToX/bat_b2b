package com.bat.distributor.service.user.executor;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.UserApplyDTO;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceMapper;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceSpecialBrandCategoryMapper;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceSpecialMapper;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialBrandCategoryDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialDO;
import com.bat.distributor.service.Message.MessageSendService;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.Constant;
import com.bat.distributor.service.common.DistributorConfig;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import com.bat.distributor.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserCmdExe {

    @Resource
    private DistributorMapper distributorMapper;

    @Resource
    private DistributorContactsMapper contactsMapper;

    @Resource
    private DistributorExtendDataMapper extendDataMapper;

    @Resource
    private DistributorAddressMapper addressMapper;

    @Resource
    private DistributorTreePathMapper treePathMapper;

    @Resource
    private NextScalePriceMapper nextScalePriceMapper;

    @Resource
    private NextScalePriceSpecialMapper scalePriceSpecialMapper;

    @Resource
    private NextScalePriceSpecialBrandCategoryMapper specialBrandCategoryMapper;

    @Resource
    private DistributorNextScalePriceMapper distributorNextScalePriceMapper;

    @Resource
    private DistributorGoodsRelevanceNoMapper goodsRelevanceNoMapper;

    @Resource
    private DistributorBrandRelevanceNoMapper brandRelevanceNoMapper;

    @Resource
    private DistributorBusinessMapper businessMapper;

    @Resource
    private DistributorSalesAreaMapper distributorSalesAreaMapper;

    @Resource
    private UserRpcCmdExe rpcCmdExe;

    @Resource
    private MessageSendService sendService;

    @Resource
    private DistributorConfig config;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCmdExe.class);

    /**
     * 一级分销商注册申请
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserApplyDTO oneLevelApply(UserOneLevelApplyCmd cmd) {
        DistributorDO distributorDO = UserConvertor.toDistributorDO(cmd);
        String operateDes = "申请成功";
        boolean isFail = false;
        try {
            distributorMapper.insert(distributorDO);
        } catch (DuplicateKeyException e) {
            operateDes = "申请失败！用户名已存在，请使用其他名称";
            isFail = true;
        }
        sendService.distributorLogPackage(distributorDO.getId(), "一级分销商注册申请", operateDes,
            JSONObject.toJSONString(distributorDO));
        if (isFail) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_NAME_ERROR);
        }
        saveDistributorData(cmd, distributorDO.getId());
        UserApplyDTO dto = new UserApplyDTO();
        dto.setId(distributorDO.getId());
        dto.setUserName(distributorDO.getName());
        dto.setPassword(distributorDO.getPassword());
        return dto;
    }

    /**
     * 保存分销商相关信息(一级分销商)
     *
     * @param cmd
     * @param distributorId
     */
    private void saveDistributorData(UserOneLevelApplyCmd cmd, Integer distributorId) {
        /** 分销商联系人 */
        DistributorContactsDO contactsDO = UserConvertor.toDistributorContactsDO(cmd, distributorId);
        try {
            contactsMapper.insert(contactsDO);
        } catch (DuplicateKeyException e) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
        }
        /** 分销商扩展信息 */
        DistributorExtendDataDO extendDataDO = UserConvertor.toDistributorExtendDataDO(cmd, distributorId);
        extendDataMapper.insert(extendDataDO);
        // 分销商地址信息
        DistributorAddressDO addressDO = UserConvertor.toDistributorAddressDO(cmd, distributorId);
        if (addressDO.getCountryId() == null) {
            addressDO.setCountryId(config.getCountryChina());
        }
        addressMapper.insert(addressDO);
    }

    /**
     * 新增分销商联系人
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createContact(UserContactCmd cmd) {
        DistributorContactsDO contactsDO = UserConvertor.toDistributorContactsDO(cmd);
        try {
            contactsMapper.insert(contactsDO);
        } catch (DuplicateKeyException e) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
        }
        sendService.distributorLogPackage(contactsDO.getDistributorId(), "新增分销商联系人", "新增成功",
            JSONObject.toJSONString(cmd));
        return contactsDO.getId();
    }

    /**
     * 修改分销商联系人
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateContact(UserContactCmd cmd) {
        DistributorContactsDO beforeContactsDO = contactsMapper.selectByPrimaryKey(cmd.getId());
        if (beforeContactsDO == null) {
            throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_CONTACTS_ERROR);
        }
        DistributorContactsDO afterContactsDO = UserConvertor.toDistributorContactsDO(cmd);
        afterContactsDO.setCreateTime(beforeContactsDO.getCreateTime());
        afterContactsDO.setUpdateTime(new Date());
        // 清除openId
        afterContactsDO.setOpenId(null);
        contactsMapper.updateByPrimaryKey(afterContactsDO);
        sendService.distributorLogPackage(afterContactsDO.getDistributorId(), "修改分销商联系人", "修改成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 删除分销商联系人
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteContact(BaseId cmd) {
        DistributorContactsDO beforeContactsDO = contactsMapper.selectByPrimaryKey(cmd.getId());
        contactsMapper.deleteByPrimaryKey(cmd.getId());
        sendService.distributorLogPackage(beforeContactsDO.getDistributorId(), "删除分销商联系人", "删除成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 分销商登录账户修改密码(原密码或验证码修改密码)
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserPasswordCmd cmd) {
        /** 先判断原密码是否准确，再更新新密码 */
        DistributorContactsDO contactsDO = null;
        DistributorDO distributorDO = null;
        if (cmd.getChangeType().equals(Constant.CHANGE_PASSWORD_TYPE_1)) {
            if (StringUtils.isBlank(cmd.getOldPassword())) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_OLD_PASSWORD_NULL);
            }
            if (cmd.getAccountType().equals(Constant.ACCOUNT_TYPE_2)) {
                contactsDO = contactsMapper.selectByPrimaryKey(cmd.getId());
                if (StringUtils.isNotBlank(contactsDO.getPassword())
                    && !contactsDO.getPassword().equals(cmd.getOldPassword())) {
                    throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_OLD_PASSWORD_ERROR);
                } else {
                    contactsMapper.updatePassword(cmd.getId(), cmd.getNewPassword());
                }
            } else if (cmd.getAccountType().equals(Constant.ACCOUNT_TYPE_1)) {
                distributorDO = distributorMapper.selectByPrimaryKey(cmd.getId());
                if (StringUtils.isNotBlank(distributorDO.getPassword())
                    && !distributorDO.getPassword().equals(cmd.getOldPassword())) {
                    throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_OLD_PASSWORD_ERROR);
                } else {
                    distributorMapper.updatePassword(cmd.getId(), cmd.getNewPassword());
                }
            }
        } else if (cmd.getChangeType().equals(Constant.CHANGE_PASSWORD_TYPE_2)) {
            if (StringUtils.isBlank(cmd.getCode())) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_PHONE_CODE_NULL);
            }
            if (StringUtils.isBlank(cmd.getPhone())) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_PHONE_NULL);
            }
            if (cmd.getCodeType() == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_PHONE_CODE_TYPE_NULL);
            }
            contactsDO = contactsMapper.selectByPhoneOrEmail(cmd.getPhone());
            if (contactsDO == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_PHONE_ERROR);
            }
            UserPhoneVerifyCodeCmd codeCmd = new UserPhoneVerifyCodeCmd();
            BeanUtils.copyProperties(cmd, codeCmd);
            rpcCmdExe.checkPhoneVerifyCode(codeCmd);
            // 只有账号拥有者 才能改分销商主账号密码
            if (cmd.getAccountType().equals(Constant.ACCOUNT_TYPE_1)) {
                if (contactsDO.getOwnerFlag().equals(Constant.OWNER_FLAG_1)) {
                    distributorMapper.updatePassword(contactsDO.getDistributorId(), cmd.getNewPassword());
                } else {
                    throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CONTACT_NOT_ALLOW_CHANGE_PASSWORD);
                }
            } else if (cmd.getAccountType().equals(Constant.ACCOUNT_TYPE_2)) {
                contactsMapper.updatePassword(contactsDO.getId(), cmd.getNewPassword());
            }
        }
        sendService.distributorLogPackage(cmd.getId(), "分销商登录账户修改密码(原密码或验证码修改密码)", "修改成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 新增分销商收货地址
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createAddress(UserAddressCmd cmd) {
        if (cmd.getDefaultFlag() == null
            || (cmd.getDefaultFlag() != null && cmd.getDefaultFlag().equals(Constant.DEFAULT_FLAG_1))) {
            List<DistributorAddressDO> addressDOS =
                addressMapper.listByDistributorIdAndDefaultFlag(cmd.getDistributorId(), Constant.DEFAULT_FLAG_1);
            if (!CollectionUtils.isEmpty(addressDOS)) {
                addressDOS.get(0).setDefaultFlag(Constant.DEFAULT_FLAG_0);
                addressMapper.updateByPrimaryKey(addressDOS.get(0));
            } else {
                cmd.setDefaultFlag(Constant.DEFAULT_FLAG_1);
            }
        }
        DistributorAddressDO addressDO = UserConvertor.toDistributorAddressDO(cmd);
        if (addressDO.getCountryId() == null) {
            addressDO.setCountryId(config.getCountryChina());
        }
        rpcCmdExe.checkRegion(addressDO.getCountryId(), addressDO.getProvinceId(), addressDO.getCityId(),
            addressDO.getDistrictId());
        addressMapper.insert(addressDO);
        sendService.distributorLogPackage(addressDO.getDistributorId(), "新增分销商收货地址", "新增成功",
            JSONObject.toJSONString(cmd));
        return addressDO.getId();
    }

    /**
     * 修改分销商收货地址
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(UserAddressCmd cmd) {
        if (cmd.getDefaultFlag() == null
            || (cmd.getDefaultFlag() != null && cmd.getDefaultFlag().equals(Constant.DEFAULT_FLAG_1))) {
            List<DistributorAddressDO> addressDOS =
                addressMapper.listByDistributorIdAndDefaultFlag(cmd.getDistributorId(), Constant.DEFAULT_FLAG_1);
            if (!CollectionUtils.isEmpty(addressDOS) && !cmd.getId().equals(addressDOS.get(0).getId())) {
                addressDOS.get(0).setDefaultFlag(Constant.DEFAULT_FLAG_0);
                addressMapper.updateByPrimaryKey(addressDOS.get(0));
            } else {
                cmd.setDefaultFlag(Constant.DEFAULT_FLAG_1);
            }
        }
        DistributorAddressDO addressDO = UserConvertor.toDistributorAddressDO(cmd);
        rpcCmdExe.checkRegion(addressDO.getCountryId(), addressDO.getProvinceId(), addressDO.getCityId(),
            addressDO.getDistrictId());
        addressMapper.updateByPrimaryKey(addressDO);
        sendService.distributorLogPackage(addressDO.getDistributorId(), "修改分销商收货地址", "修改成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 设置分销商收货地址为默认地址
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void defaultAddress(BaseId cmd) {
        DistributorAddressDO addressDO = addressMapper.selectByPrimaryKey(cmd.getId());
        List<DistributorAddressDO> addressDOS =
            addressMapper.listByDistributorIdAndDefaultFlag(addressDO.getDistributorId(), Constant.DEFAULT_FLAG_1);
        if (!CollectionUtils.isEmpty(addressDOS) && !cmd.getId().equals(addressDOS.get(0).getId())) {
            addressDOS.get(0).setDefaultFlag(Constant.DEFAULT_FLAG_0);
            addressMapper.updateByPrimaryKey(addressDOS.get(0));
        }
        addressDO.setDefaultFlag(Constant.DEFAULT_FLAG_1);
        addressMapper.updateByPrimaryKey(addressDO);
        sendService.distributorLogPackage(addressDO.getDistributorId(), "设置分销商收货地址为默认地址", "设置成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 删除分销商收货地址
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(BaseId cmd) {
        DistributorAddressDO addressDO = addressMapper.selectByPrimaryKey(cmd.getId());
        if (addressDO == null) {
            throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_USER_ADDRESS_ERROR);
        }
        addressMapper.deleteByPrimaryKey(addressDO.getId());
        sendService.distributorLogPackage(addressDO.getDistributorId(), "删除分销商收货地址", "设置成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 更新平台显示语言和币种
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLanguage(UserLanguageCmd cmd) {
        extendDataMapper.updateLanguageAndCurrencyType(cmd.getId(), cmd.getLanguage(), cmd.getCurrencyType());
    }

    /**
     * 下级分销商前台注册申请
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public UserApplyDTO nextLevelApply(UserNextLevelApplyCmd cmd) {
        /** 分销级数 */
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(cmd.getDistributorId());
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(cmd.getDistributorId());
        if (extendDataDO.getDistributionFlag().equals(Constant.DISTRIBUTION_FLAG_0)) {
            throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.B_DISTRIBUTOR_USER_NO_DISTRIBUTION_FLAG);
        }
        DistributorDO nextDistributorDO = UserConvertor.toDistributorDO(cmd);
        Integer nextTreeNode = distributorDO.getTreeNode() + 1;
        // TODO 判断平台分销级数是否支持
        nextDistributorDO.setTreeNode(nextTreeNode);
        try {
            distributorMapper.insert(nextDistributorDO);
        } catch (DuplicateKeyException e) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_NAME_ERROR);
        }
        saveDistributorData(cmd, nextDistributorDO.getId());
        /** 分销商级数关系 */
        List<DistributorTreePathDO> treePathDOS = treePathMapper.listByDistributorDescendantId(distributorDO.getId());
        List<DistributorTreePathDO> nextTreePathDOS =
            UserConvertor.toNextTreePathDOList(treePathDOS, distributorDO, nextDistributorDO);
        String operateDes = "申请成功";
        boolean isFail = false;
        // 根据最上级分销商业务数据（如业务员、商务员等信息，生成当前分销商业务数据,和分销商区域）
        if (!CollectionUtils.isEmpty(nextTreePathDOS)) {
            treePathMapper.insertList(nextTreePathDOS);
            DistributorTreePathDO maxTreePathDO =
                nextTreePathDOS.stream().max(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
            DistributorTreePathDO minTreePathDO =
                nextTreePathDOS.stream().min(Comparator.comparing(DistributorTreePathDO::getTreeNode)).get();
            DistributorBusinessDO maxBusinessDO =
                businessMapper.getByDistributorId(maxTreePathDO.getDistributorAncestorId());
            DistributorBusinessDO minBusinessDO =
                businessMapper.getByDistributorId(minTreePathDO.getDistributorAncestorId());
            DistributorExtendDataDO minExtendDataDO =
                extendDataMapper.getByDistributorId(minTreePathDO.getDistributorAncestorId());
            if (maxBusinessDO != null) {
                DistributorBusinessDO nextBusinessDO = UserConvertor.toDistributorBusinessDO(maxBusinessDO,
                    minBusinessDO, minExtendDataDO, nextDistributorDO.getId());
                businessMapper.insert(nextBusinessDO);
            }
            List<DistributorSalesAreaDO> salesAreaDOS =
                distributorSalesAreaMapper.listByDistributorId(maxTreePathDO.getDistributorAncestorId());
            if (!CollectionUtils.isEmpty(salesAreaDOS)) {
                salesAreaDOS.forEach(salesAreaDO -> {
                    salesAreaDO.setId(null);
                    salesAreaDO.setDistributorId(nextDistributorDO.getId());
                });
                distributorSalesAreaMapper.insertList(salesAreaDOS);
            }
        } else {
            operateDes = "申请失败！非常抱歉！！分销商注册申请异常啦！！！";
            isFail = true;
        }
        sendService.distributorLogPackage(nextDistributorDO.getId(), "下级分销商前台注册申请", operateDes,
            JSONObject.toJSONString(cmd));
        if (isFail) {
            throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.B_DISTRIBUTOR_USER_NEXT_ERROR);
        }
        UserApplyDTO dto = new UserApplyDTO();
        dto.setId(nextDistributorDO.getId());
        dto.setUserName(nextDistributorDO.getName());
        dto.setPassword(nextDistributorDO.getPassword());
        sendService.distributorExamineMsg(nextDistributorDO.getId());
        return dto;
    }

    /**
     * 保存分销商相关信息(多级分销商)
     *
     * @param cmd
     * @param distributorId
     */
    private void saveDistributorData(UserNextLevelApplyCmd cmd, Integer distributorId) {
        /** 分销商联系人 */
        DistributorContactsDO contactsDO = UserConvertor.toDistributorContactsDO(cmd, distributorId);
        try {
            contactsMapper.insert(contactsDO);
        } catch (DuplicateKeyException e) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
        }
        /** 分销商扩展信息 */
        DistributorExtendDataDO extendDataDO = UserConvertor.toDistributorExtendDataDO(cmd, distributorId);
        extendDataMapper.insert(extendDataDO);
    }

    /**
     * 保存分销商分销二维码
     * 
     * @param qrCodeUrl
     * @param distributorId
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDistributionQrCode(String qrCodeUrl, Integer distributorId) {
        extendDataMapper.updateDistributionQrUrlByDistributorId(distributorId, qrCodeUrl);
    }

    /**
     * 新增分销商价格等级
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createNextScalePrice(UserNextScalePriceCmd cmd) {
        NextScalePriceDO nextScalePriceDO = UserConvertor.toNextScalePriceDO(cmd);
        nextScalePriceMapper.insert(nextScalePriceDO);
        sendService.distributorLogPackage(nextScalePriceDO.getDistributorId(), "新增分销商价格等级", "新增成功",
            JSONObject.toJSONString(cmd));
        return nextScalePriceDO.getId();
    }

    /**
     * 修改分销商价格等级
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateNextScalePrice(UserNextScalePriceCmd cmd) {
        NextScalePriceDO beforeNextScalePriceDO = nextScalePriceMapper.selectByPrimaryKey(cmd.getId());
        if (beforeNextScalePriceDO == null) {
            throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_USER_NEXT_SCALE_PRICE_NULL);
        }
        NextScalePriceDO afterNextScalePriceDO = UserConvertor.toNextScalePriceDO(cmd);
        afterNextScalePriceDO.setCreateTime(beforeNextScalePriceDO.getCreateTime());
        nextScalePriceMapper.updateByPrimaryKey(afterNextScalePriceDO);
        sendService.distributorLogPackage(beforeNextScalePriceDO.getDistributorId(), "修改分销商价格等级", "修改成功",
            JSONObject.toJSONString(cmd));
        return afterNextScalePriceDO.getId();
    }

    /**
     * 删除价格等级
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteNextScalePrice(BaseId cmd) {
        NextScalePriceDO beforeNextScalePriceDO = nextScalePriceMapper.selectByPrimaryKey(cmd.getId());
        List<DistributorNextScalePriceDO> scalePriceDOS =
            distributorNextScalePriceMapper.listByNextScalePriceId(cmd.getId());
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            throw DistributorException.buildException(com.bat.distributor.service.user.executor.ErrorCode.B_DISTRIBUTOR_USER_DELETE_NEXT_SCALE_PRICE_ERROR);
        }
        deleteNextScalePriceRelevance(cmd);
        nextScalePriceMapper.deleteByPrimaryKey(cmd.getId());
        sendService.distributorLogPackage(beforeNextScalePriceDO.getDistributorId(), "删除价格等级", "删除成功",
            JSONObject.toJSONString(cmd));
    }

    /**
     * 新增价格等级特殊公式
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void createNextScalePriceSpecial(UserNextScalePriceSpecialCmd cmd) {
        NextScalePriceSpecialDO scalePriceSpecialDO = UserConvertor.toNextScalePriceSpecialDO(cmd);
        scalePriceSpecialMapper.insert(scalePriceSpecialDO);
        if (!CollectionUtils.isEmpty(cmd.getBrandCategorys())) {
            List<NextScalePriceSpecialBrandCategoryDO> brandCategoryDOS = UserConvertor
                .toNextScalePriceSpecialBrandCategoryDO(cmd.getBrandCategorys(), scalePriceSpecialDO.getId());
            specialBrandCategoryMapper.insertList(brandCategoryDOS);
            try {
                NextScalePriceDO beforeNextScalePriceDO =
                    nextScalePriceMapper.selectByPrimaryKey(scalePriceSpecialDO.getNextScalePriceId());
                sendService.distributorLogPackage(beforeNextScalePriceDO.getDistributorId(), "新增价格等级特殊公式", "新增成功",
                    JSONObject.toJSONString(cmd));
            } catch (Exception e) {
                log.error("记录分销商日志出现异常:{}");
            }
        }
    }

    /**
     * 修改价格等级特殊公司
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateNextScalePriceSpecial(UserNextScalePriceSpecialCmd cmd) {
        NextScalePriceSpecialDO scalePriceSpecialDO = UserConvertor.toNextScalePriceSpecialDO(cmd);
        specialBrandCategoryMapper.deleteByNextScalePriceSpecialId(scalePriceSpecialDO.getId());
        scalePriceSpecialMapper.updateByPrimaryKey(scalePriceSpecialDO);
        if (!CollectionUtils.isEmpty(cmd.getBrandCategorys())) {
            List<NextScalePriceSpecialBrandCategoryDO> brandCategoryDOS = UserConvertor
                .toNextScalePriceSpecialBrandCategoryDO(cmd.getBrandCategorys(), scalePriceSpecialDO.getId());
            specialBrandCategoryMapper.insertList(brandCategoryDOS);
            try {
                NextScalePriceDO beforeNextScalePriceDO =
                    nextScalePriceMapper.selectByPrimaryKey(scalePriceSpecialDO.getNextScalePriceId());
                sendService.distributorLogPackage(beforeNextScalePriceDO.getDistributorId(), "修改价格等级特殊公式", "修改成功",
                    JSONObject.toJSONString(cmd));
            } catch (Exception e) {
                log.error("记录分销商日志出现异常:{}");
            }
        }
    }

    /**
     * 删除价格等级特殊公司
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteNextScalePriceSpecial(BaseId cmd) {
        NextScalePriceSpecialDO nextScalePriceSpecialDO = scalePriceSpecialMapper.selectByPrimaryKey(cmd.getId());
        specialBrandCategoryMapper.deleteByNextScalePriceSpecialId(cmd.getId());
        scalePriceSpecialMapper.deleteByPrimaryKey(cmd.getId());
        try {
            NextScalePriceDO beforeNextScalePriceDO =
                nextScalePriceMapper.selectByPrimaryKey(nextScalePriceSpecialDO.getNextScalePriceId());
            sendService.distributorLogPackage(beforeNextScalePriceDO.getDistributorId(), "删除价格等级特殊公式", "删除成功",
                JSONObject.toJSONString(cmd));
        } catch (Exception e) {
            log.error("记录分销商日志出现异常:{}");
        }
    }

    /**
     * 删除分销商价格等级与特殊公式关系数据
     * 
     * @param cmd
     */
    private void deleteNextScalePriceRelevance(BaseId cmd) {
        List<NextScalePriceSpecialDO> nextScalePriceSpecialDOS =
            scalePriceSpecialMapper.listByNextScalePriceId(cmd.getId());
        if (!CollectionUtils.isEmpty(nextScalePriceSpecialDOS)) {
            List<Integer> nextScalePriceSpecialIds =
                nextScalePriceSpecialDOS.stream().map(NextScalePriceSpecialDO::getId).collect(Collectors.toList());
            specialBrandCategoryMapper.deleteByNextScalePriceSpecialIds(nextScalePriceSpecialIds);
        }
        scalePriceSpecialMapper.deleteByNextScalePriceId(cmd.getId());
    }

    /**
     * 分销商审核下级分销商
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkDistributor(UserCheckCmd cmd) {
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(cmd.getId());
        if (distributorDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_NULL);
        }
        Date date = new Date(System.currentTimeMillis());
        distributorDO.setApplyStatus(cmd.getApplyStatus());
        distributorDO.setApplyTime(date);
        distributorMapper.applyDistributor(distributorDO);
        if (cmd.getApplyStatus().equals(Constant.APPLY_STATUS_2) && cmd.getScalePriceId() != null
            && cmd.getScalePriceId() != 0) {
            DistributorNextScalePriceDO scalePriceDO = new DistributorNextScalePriceDO();
            scalePriceDO.setDistributorId(cmd.getId());
            scalePriceDO.setNextScalePriceId(cmd.getScalePriceId());
            distributorNextScalePriceMapper.insert(scalePriceDO);
        }
        sendService.distributorLogPackage(distributorDO.getId(), "分销商审核下级分销商", "审核成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 调整下级分销商价格等级
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDistributorScalePrice(UserDistributorScalePriceCmd cmd) {
        //根据分销商id查询该分销商的价格等级
        DistributorNextScalePriceDO scalePriceDO = distributorNextScalePriceMapper.selectBydistributorId(cmd.getId());
        if (scalePriceDO == null && cmd.getScalePriceId() != null && cmd.getScalePriceId() != 0) {
            scalePriceDO = new DistributorNextScalePriceDO();
            scalePriceDO.setDistributorId(cmd.getId());
            scalePriceDO.setNextScalePriceId(cmd.getScalePriceId());
            distributorNextScalePriceMapper.insert(scalePriceDO);
        } else if (scalePriceDO != null && cmd.getScalePriceId() != null && cmd.getScalePriceId() != 0) {
            scalePriceDO.setDistributorId(cmd.getId());
            scalePriceDO.setNextScalePriceId(cmd.getScalePriceId());
            distributorNextScalePriceMapper.updateByPrimaryKey(scalePriceDO);
        } else if ((cmd.getScalePriceId() == null || cmd.getScalePriceId() == 0) && scalePriceDO != null) {
            distributorNextScalePriceMapper.deleteByPrimaryKey(scalePriceDO.getId());
        }
        sendService.distributorLogPackage(cmd.getId(), "调整下级分销商价格等级", "调整成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 设置下级分销商不可视品牌
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void setNoBrand(UserNextNoBrandCmd cmd) {
        DistributorBrandRelevanceNoDO relevanceNoDO =
            brandRelevanceNoMapper.selectByDistributorId(cmd.getDistributorId());
        if (CollectionUtils.isEmpty(cmd.getBrandIds()) && relevanceNoDO != null) {
            brandRelevanceNoMapper.deleteByDistributorId(cmd.getDistributorId());
            return;
        }
        if (!CollectionUtils.isEmpty(cmd.getBrandIds())) {
            relevanceNoDO =
                UserConvertor.toDistributorBrandRelevanceNoDO(cmd.getBrandIds(), cmd.getDistributorId(), relevanceNoDO);
            if (relevanceNoDO.getId() == null) {
                brandRelevanceNoMapper.insert(relevanceNoDO);
            } else {
                brandRelevanceNoMapper.updateByPrimaryKey(relevanceNoDO);
            }
        }
        sendService.distributorLogPackage(cmd.getDistributorId(), "设置下级分销商不可视品牌", "设置成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 设置下级分销商不可视商品
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void setNoGoods(UserNextNoGoodsCmd cmd) {
        DistributorGoodsRelevanceNoDO relevanceNoDO =
            goodsRelevanceNoMapper.selectByDistributorId(cmd.getDistributorId());
        if (CollectionUtils.isEmpty(cmd.getGoodsIds()) && relevanceNoDO != null) {
            goodsRelevanceNoMapper.deleteByDistributorId(cmd.getDistributorId());
            return;
        }
        if (!CollectionUtils.isEmpty(cmd.getGoodsIds())) {
            relevanceNoDO =
                UserConvertor.toDistributorGoodsRelevanceNoDO(cmd.getGoodsIds(), cmd.getDistributorId(), relevanceNoDO);
            if (relevanceNoDO.getId() == null) {
                goodsRelevanceNoMapper.insert(relevanceNoDO);
            } else {
                goodsRelevanceNoMapper.updateByPrimaryKey(relevanceNoDO);
            }
        }
        sendService.distributorLogPackage(cmd.getDistributorId(), "设置下级分销商不可视商品", "设置成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 分销商修改手机号
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePhone(UserChangePhoneCmd cmd) {
        DistributorContactsDO contactsDO = contactsMapper.selectByPhoneOrEmail(cmd.getOldPhone());
        if (contactsDO == null) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NULL);
        }
        UserPhoneVerifyCodeCmd codeCmd = new UserPhoneVerifyCodeCmd();
        codeCmd.setPhone(cmd.getOldPhone());
        codeCmd.setCodeType(cmd.getCodeType());
        codeCmd.setCode(cmd.getCode());
        rpcCmdExe.checkPhoneVerifyCode(codeCmd);
        contactsDO.setPhone(cmd.getNewPhone());
        contactsDO.setUpdateTime(new Date());
        // 每次都清除
        contactsDO.setOpenId(null);
        contactsMapper.updateByPrimaryKey(contactsDO);
        sendService.distributorLogPackage(contactsDO.getDistributorId(), "分销商修改手机号", "修改成功",
            JSONObject.toJSONString(cmd));
    }

    @Transactional(rollbackFor = Exception.class)
    public void logOut(Integer id) {
        DistributorContactsDO contactsDO = contactsMapper.selectByPrimaryKey(id);
        if (contactsDO == null) {
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        contactsDO.setOpenId(null);
        contactsDO.setUpdateTime(new Date());
        contactsMapper.updateByPrimaryKey(contactsDO);
    }

    public void updateOpenId(UserOpenIdCmd cmd) {
        LOGGER.info("获取到前端的openId:{}", cmd.getOpenId());
        DistributorExtendDataDO distributorExtendDataDO = extendDataMapper.getByDistributorId(cmd.getId());
        if (distributorExtendDataDO != null) {
            distributorExtendDataDO.setOpenId(cmd.getOpenId());
            extendDataMapper.updateByPrimaryKey(distributorExtendDataDO);
        }
    }
}
