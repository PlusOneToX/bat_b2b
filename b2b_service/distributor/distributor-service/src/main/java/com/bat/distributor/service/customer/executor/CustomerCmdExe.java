package com.bat.distributor.service.customer.executor;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.customer.dto.CustomerStatusCmd;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.api.user.dto.customer.data.CustomerDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerLoginDTO;
import com.bat.distributor.dao.customer.CustomerAddressMapper;
import com.bat.distributor.dao.customer.CustomerMapper;
import com.bat.distributor.dao.customer.CustomerPlatformMapper;
import com.bat.distributor.dao.customer.dataobject.CustomerAddressDO;
import com.bat.distributor.dao.customer.dataobject.CustomerDO;
import com.bat.distributor.dao.customer.dataobject.CustomerPlatformDO;
import com.bat.distributor.dao.platform.AlipayPlatformMapper;
import com.bat.distributor.dao.platform.DyPlatformMapper;
import com.bat.distributor.dao.platform.WxPlatformMapper;
import com.bat.distributor.dao.platform.dataobject.AlipayPlatformDO;
import com.bat.distributor.dao.platform.dataobject.DyPlatformDO;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.service.customer.convertor.CustomerConvertor;
import com.bat.distributor.service.customer.validator.CustomerValidator;
import com.bat.distributor.service.distributor.validator.DistributorValidator;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.service.common.CommonRpcExe;
import com.bat.distributor.service.common.CommonUtils;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data.AlipayProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxOfficialProgramAuthorizeRpcDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.bat.distributor.service.common.Constant.*;
import static com.bat.distributor.service.customer.executor.ErrorCode.*;
import static com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_OLD_PASSWORD_ERROR;
import static com.bat.distributor.service.user.executor.ErrorCode.P_DISTRIBUTOR_USER_ADDRESS_ERROR;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 14:39
 */
@Component
@Slf4j
public class CustomerCmdExe {

    @Resource
    private DistributorValidator distributorValidator;
    @Resource
    private CommonRpcExe commonRpcExe;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CustomerPlatformMapper customerPlatformMapper;
    @Resource
    private CustomerRpcCmdExe rpcCmdExe;
    @Resource
    private CustomerValidator customerValidator;
    @Resource
    private WxPlatformMapper wxPlatformMapper;
    @Resource
    private CustomerAddressMapper customerAddressMapper;

    @Resource
    private DyPlatformMapper dyPlatformMapper;

    @Resource
    private AlipayPlatformMapper alipayPlatformMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCmdExe.class);

    @Value("${vx.mini.distributor.id}")
    private Integer vxMiniDistributorId;

    /**
     * C端客户获取验证码
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    public void verify(CustomerVerify cmd, String distributorId) {
        distributorValidator.checkDistributor(distributorId);
        commonRpcExe.getPhoneVerifyCode(cmd.getPhone(), cmd.getCodeType());
    }

    /**
     * C端客户验证码登录
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    public CustomerLoginDTO verifyLogin(CustomerVerifyCmd cmd, String distributorId, String platform) {
        distributorValidator.checkDistributor(distributorId, platform);
        commonRpcExe.checkPhoneVerifyCode(cmd.getPhone(), cmd.getCodeType(), cmd.getCode());
        CustomerDO customerDO =
            customerMapper.selectByPhoneAndDistributorId(cmd.getPhone(), Integer.valueOf(distributorId));
        // 新用户时，注册
        if (customerDO == null) {
            customerDO = createCustomerDO(cmd.getPhone(), Integer.valueOf(distributorId), platform);
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, null);
    }

    /**
     * 根据分销商id和手机号新建C端客户
     * 
     * @param distributorId
     * @param phone
     * @return
     */
    private CustomerDO createCustomerDO(String phone, Integer distributorId, String platform) {
        CustomerDO customerDO = CustomerConvertor.toCustomerDO(phone, distributorId, platform);
        try {
            customerDO.setNo(CommonUtils.getCustomerNo());
            customerMapper.insert(customerDO);
        } catch (DuplicateKeyException e) {
            createCustomerDO(phone, distributorId, platform);
        }
        return customerDO;
    }

    /**
     * C端客户第三方系统登录
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    public CustomerLoginDTO thirdpartyLogin(CustomerThirdPartyLoginCmd cmd, String distributorId, String platform) {
        distributorValidator.checkDistributor(distributorId, platform);
        return customerLogin(cmd.getPhone(), cmd.getOpenId(), cmd.getOtherUid(), Integer.valueOf(distributorId),
            platform);
    }

    /**
     * 获取C端客户登录信息
     * 
     * @param phone
     * @param openId
     * @param otherUid
     * @param distributorId
     * @param platform
     * @return
     */
    public CustomerLoginDTO customerLogin(String phone, String openId, String otherUid, Integer distributorId,
        String platform) {
        CustomerDO customerDO = null;
        if (StringUtils.isBlank(phone)) {
            customerDO = customerMapper.selectByOpenIdAndOtherUidAndDistributorIdAndPlatform(openId, otherUid,
                distributorId, platform);
        } else {
            customerDO = customerMapper.selectByPhoneAndDistributorId(phone, Integer.valueOf(distributorId));
        }
        // 新用户时，注册(手机号为空，游客)
        CustomerPlatformDO platformDO = null;
        if (customerDO == null) {
            customerDO = createCustomerDO(phone, Integer.valueOf(distributorId), platform);
            platformDO = CustomerConvertor.toCustomerPlatformDO(customerDO.getId(), openId, otherUid, platform);
            customerPlatformMapper.insert(platformDO);
        } else {
            platformDO = customerPlatformMapper.selectOneByOpenIdAndPlatform(openId, platform);
            // 假设uid不为空，且平台记录uid为空或不一样情况更新平台记录
            if (StringUtils.isNotBlank(otherUid)
                && (platformDO.getOtherId() == null || !platformDO.getOtherId().equals(otherUid))) {
                platformDO.setOtherId(otherUid);
                customerPlatformMapper.updateByPrimaryKey(platformDO);
            }
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, platformDO);
    }

    /**
     * 创建C端客户
     * 
     * @return
     */
    private CustomerDO createCustomerDO(CustomerWxMiniProgramLoginCmd cmd, WxMiniProgramAuthorizeRpcDTO rpcDTO,
                                        Integer distributorId, String platform) {
        CustomerDO customerDO = CustomerConvertor.toCustomerDO(cmd, rpcDTO, distributorId, platform);
        try {
            customerDO.setNo(CommonUtils.getCustomerNo());
            customerMapper.insert(customerDO);
        } catch (DuplicateKeyException e) {
            createCustomerDO(cmd, rpcDTO, distributorId, platform);
        }
        return customerDO;
    }

    /**
     * 创建C端客户(抖音小程序)
     *
     * @return
     */
    private CustomerDO createCustomerDO(CustomerDyProgramMiniLoginCmd cmd, DyMiniProgramAuthorizeRpcDTO rpcDTO,
                                        Integer distributorId, String platform) {
        CustomerDO customerDO = CustomerConvertor.toCustomerDO(cmd, rpcDTO, distributorId, platform);
        try {
            customerDO.setNo(CommonUtils.getCustomerNo());
            customerMapper.insert(customerDO);
        } catch (DuplicateKeyException e) {
            createCustomerDO(cmd, rpcDTO, distributorId, platform);
        }
        return customerDO;
    }

    /**
     * 创建C端客户(支付宝小程序)
     * 
     * @param cmd
     * @param rpcDTO
     * @param distributorId
     * @param platform
     * @return
     */
    private CustomerDO createCustomerDO(CustomerAlipayProgramMiniLoginCmd cmd, AlipayProgramAuthorizeRpcDTO rpcDTO,
        Integer distributorId, String platform) {
        CustomerDO customerDO = CustomerConvertor.toCustomerDO(null, rpcDTO, distributorId, platform);
        try {
            customerDO.setNo(CommonUtils.getCustomerNo());
            customerMapper.insert(customerDO);
        } catch (DuplicateKeyException e) {
            createCustomerDO(null, rpcDTO, distributorId, platform);
        }
        return customerDO;
    }

    /**
     * 创建C端客户(游客)
     *
     * @return
     */
    private CustomerDO createCustomerDO(Integer distributorId, String platform) {
        CustomerDO customerDO = CustomerConvertor.toCustomerDO(null, distributorId, platform);
        try {
            customerDO.setNo(CommonUtils.getCustomerNo());
            customerMapper.insert(customerDO);
        } catch (DuplicateKeyException e) {
            createCustomerDO(distributorId, platform);
        }
        return customerDO;
    }

    /**
     * 获取C端客户登录信息(微信公众号)
     *
     * @param platform
     * @return
     */
    public CustomerLoginDTO customerLogin(WxOfficialProgramAuthorizeRpcDTO rpcDTO, Integer distributorId,
        String platform) {
        CustomerDO customerDO = customerMapper.selectByOpenIdAndOtherUidAndDistributorIdAndPlatform(rpcDTO.getOpenid(),
            null, Integer.valueOf(distributorId), platform);
        // 新用户时，注册(手机号为空，游客)
        CustomerPlatformDO platformDO = null;
        if (customerDO == null) {
            customerDO = createCustomerDO(Integer.valueOf(distributorId), platform);
        } else {
            platformDO = customerPlatformMapper.selectByOpenIdAndOtherIdAndPlatformAndCustomerId(customerDO.getId(),
                rpcDTO.getOpenid(), null, platform);
        }
        if (platformDO == null) {
            platformDO = CustomerConvertor.toCustomerPlatformDO(customerDO.getId(), rpcDTO.getOpenid(), null, platform);
            customerPlatformMapper.insert(platformDO);
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, platformDO);
    }

    /**
     * 获取C端客户登录信息(抖音小程序)
     *
     * @param platform
     * @return
     */
    public CustomerLoginDTO customerLogin(CustomerDyProgramMiniLoginCmd cmd, DyMiniProgramAuthorizeRpcDTO rpcDTO,
        Integer distributorId, String platform) {
        Integer customerId = null;
        // 根据openId还有平台信息找到对应平台信息
        CustomerPlatformDO customerPlatformDO =
            customerPlatformMapper.selectOneByOpenIdAndPlatform(rpcDTO.getOpenid(), platform);
        if (customerPlatformDO != null) {
            // 获取平台信息得到的C端用户id
            customerId = customerPlatformDO.getCustomerId();
        }
        CustomerDO customerDO =
            customerMapper.selectByPhoneAndDistributorId(rpcDTO.getPhone(), Integer.valueOf(distributorId));
        if (customerDO == null && customerId != null) {
            // 用户信息为空且C端用户id不为空；则根据C端用户id重新查询
            customerDO = customerMapper.selectByPrimaryKey(customerId);
        }
        // 新用户时，注册(手机号为空，游客)
        CustomerPlatformDO platformDO = null;
        if (customerDO == null) {
            customerDO = createCustomerDO(cmd, rpcDTO, Integer.valueOf(distributorId), platform);
        } else {
            try {
                boolean changeFlag = false;
                if (StringUtils.isBlank(customerDO.getNikeName())) {
                    customerDO.setNikeName(cmd.getNickName());
                    changeFlag = true;
                }
                if (StringUtils.isBlank(customerDO.getHeadPortrait())) {
                    customerDO.setHeadPortrait(cmd.getAvatarUrl());
                    changeFlag = true;
                }
                if (changeFlag) {
                    customerMapper.updateByPrimaryKey(customerDO);
                }
            } catch (Exception e) {
                LOGGER.info("更新分销商信息出现异常:{}", e);
            }
            platformDO = customerPlatformMapper.selectByOpenIdAndOtherIdAndPlatformAndCustomerId(customerDO.getId(),
                rpcDTO.getOpenid(), null, platform);
        }
        if (platformDO == null) {
            platformDO = CustomerConvertor.toCustomerPlatformDO(customerDO.getId(), rpcDTO.getOpenid(), null, platform);
            customerPlatformMapper.insert(platformDO);
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, platformDO);
    }

    /**
     * 获取C端客户登录信息(微信小程序)
     *
     * @param platform
     * @return
     */
    public CustomerLoginDTO customerLogin(CustomerWxMiniProgramLoginCmd cmd, WxMiniProgramAuthorizeRpcDTO rpcDTO,
        Integer distributorId, String platform) {
        CustomerDO customerDO =
            customerMapper.selectByPhoneAndDistributorId(rpcDTO.getPhone(), Integer.valueOf(distributorId));
        // 新用户时，注册(手机号为空，游客)
        CustomerPlatformDO platformDO = null;
        if (customerDO == null) {
            customerDO = createCustomerDO(cmd, rpcDTO, Integer.valueOf(distributorId), platform);
        } else {
            try {
                boolean changeFlag = false;
                if (StringUtils.isBlank(customerDO.getNikeName())) {
                    customerDO.setNikeName(cmd.getNickName());
                    changeFlag = true;
                }
                if (StringUtils.isBlank(customerDO.getHeadPortrait())) {
                    customerDO.setHeadPortrait(cmd.getAvatarUrl());
                    changeFlag = true;
                }
                if (changeFlag) {
                    customerMapper.updateByPrimaryKey(customerDO);
                }
            } catch (Exception e) {
                LOGGER.info("更新分销商信息出现异常:{}", e);
            }
            platformDO = customerPlatformMapper.selectByOpenIdAndOtherIdAndPlatformAndCustomerId(customerDO.getId(),
                rpcDTO.getOpenid(), null, platform);
        }
        if (platformDO == null) {
            platformDO = CustomerConvertor.toCustomerPlatformDO(customerDO.getId(), rpcDTO.getOpenid(), null, platform);
            customerPlatformMapper.insert(platformDO);
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, platformDO);
    }

    private CustomerLoginDTO customerLogin(CustomerAlipayProgramMiniLoginCmd cmd, AlipayProgramAuthorizeRpcDTO rpcDTO,
        Integer distributorId, String platform) {
        Integer customerId = null;
        // 根据openId还有平台信息找到对应平台信息
        CustomerPlatformDO customerPlatformDO =
            customerPlatformMapper.selectOneByOpenIdAndPlatform(rpcDTO.getUserId(), platform);
        if (customerPlatformDO != null) {
            // 获取平台信息得到的C端用户id
            customerId = customerPlatformDO.getCustomerId();
        }
        CustomerDO customerDO =
            customerMapper.selectByPhoneAndDistributorId(rpcDTO.getPhone(), Integer.valueOf(distributorId));
        if (customerDO == null && customerId != null) {
            // 用户信息为空且C端用户id不为空；则根据C端用户id重新查询
            customerDO = customerMapper.selectByPrimaryKey(customerId);
        }
        // 新用户时，注册(手机号为空，游客)
        CustomerPlatformDO platformDO = null;
        if (customerDO == null) {
            customerDO = createCustomerDO(cmd, rpcDTO, Integer.valueOf(distributorId), platform);
        } else {
            try {
                boolean changeFlag = false;
                if (StringUtils.isBlank(customerDO.getNikeName())) {
                    customerDO.setNikeName(rpcDTO.getNickName());
                    changeFlag = true;
                }
                if (StringUtils.isBlank(customerDO.getHeadPortrait())) {
                    customerDO.setHeadPortrait(rpcDTO.getAvatar());
                    changeFlag = true;
                }
                if (changeFlag) {
                    customerMapper.updateByPrimaryKey(customerDO);
                }
            } catch (Exception e) {
                LOGGER.info("更新分销商信息出现异常:{}", e);
            }
            platformDO = customerPlatformMapper.selectByOpenIdAndOtherIdAndPlatformAndCustomerId(customerDO.getId(),
                rpcDTO.getUserId(), null, platform);
        }
        if (platformDO == null) {
            platformDO = CustomerConvertor.toCustomerPlatformDO(customerDO.getId(), rpcDTO.getUserId(), null, platform);
            customerPlatformMapper.insert(platformDO);
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, platformDO);
    }

    /**
     * C端客户微信公众号授权登录
     *
     * @param cmd
     * @param distributorId
     * @param platform
     * @return
     */
    public CustomerLoginDTO wxOfficialProgramLogin(CustomerWxOfficialProgramLoginCmd cmd, String distributorId,
        String platform) {
        distributorValidator.checkDistributor(distributorId);
        WxPlatformDO wxPlatformDO = wxPlatformMapper.selectByDistributorIdAndPlatformAndAppIdAndType(
            Integer.valueOf(distributorId), platform, cmd.getAppId(), WX_PLATFORM_TYPE_1);
        if (wxPlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_WX_PLATFORM_NULL);
        }
        WxOfficialProgramAuthorizeRpcDTO rpcDTO = rpcCmdExe.wxOfficialProgramAuthorize(cmd, wxPlatformDO);
        return customerLogin(rpcDTO, Integer.valueOf(distributorId), platform);
    }

    /**
     * C端客户微信小程序授权登录
     *
     * @param cmd
     * @param distributorId
     * @param platform
     * @return
     */
    public CustomerLoginDTO wxMiniProgramLogin(CustomerWxMiniProgramLoginCmd cmd, String distributorId,
        String platform) {
        distributorValidator.checkDistributor(distributorId);
        LOGGER.info("查询小程序平台传参,distributorId:{},platform:{},appId:{},type:{}", distributorId, platform, cmd.getAppId(),
            WX_PLATFORM_TYPE_2);
        WxPlatformDO wxPlatformDO = wxPlatformMapper.selectByDistributorIdAndPlatformAndAppIdAndType(
            vxMiniDistributorId, platform, cmd.getAppId(), WX_PLATFORM_TYPE_2);
        if (wxPlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_WX_PLATFORM_NULL);
        }
        WxMiniProgramAuthorizeRpcDTO rpcDTO = rpcCmdExe.wxMiniProgramAuthorize(cmd, wxPlatformDO);
        return customerLogin(cmd, rpcDTO, Integer.valueOf(distributorId), platform);
    }

    /**
     * C端客户抖音授权登录
     * 
     * @param cmd
     * @param distributorId
     * @param platform
     * @return
     */
    public CustomerLoginDTO dyProgramMiniLogin(CustomerDyProgramMiniLoginCmd cmd, String distributorId,
        String platform) {
        distributorValidator.checkDistributor(distributorId);
        DyPlatformDO dyPlatformDO = dyPlatformMapper.selectByDistributorIdAndPlatformAndAppIdAndType(
            Integer.valueOf(distributorId), platform, cmd.getAppId(), DY_PLATFORM_TYPE_1);
        if (dyPlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_DY_PLATFORM_NULL);
        }
        DyMiniProgramAuthorizeRpcDTO rpcDTO = rpcCmdExe.dyMiniProgramAuthorize(cmd, dyPlatformDO);
        return customerLogin(cmd, rpcDTO, Integer.valueOf(distributorId), platform);
    }

    /**
     * C端客户支付宝授权登录
     * 
     * @param cmd
     * @param distributorId
     * @param platform
     * @return
     */
    public CustomerLoginDTO alipayMiniProgramLogin(CustomerAlipayProgramMiniLoginCmd cmd, String distributorId,
        String platform) {
        distributorValidator.checkDistributor(distributorId);
        AlipayPlatformDO alipayPlatformDO = alipayPlatformMapper.selectByDistributorIdAndPlatformAndAppIdAndType(
            Integer.valueOf(distributorId), platform, cmd.getAppId(), ZFB_PLATFORM_TYPE_1);
        if (alipayPlatformDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_ALIPAY_PLATFORM_NULL);
        }
        AlipayProgramAuthorizeRpcDTO rpcDTO = rpcCmdExe.alipayMiniProgramAuthorize(cmd, alipayPlatformDO);
        return customerLogin(cmd, rpcDTO, Integer.valueOf(distributorId), platform);
    }

    /**
     * C端客户修改个人信息(修改手机情况，可能会合并账号)
     *
     * @param cmd
     * @return
     */
    public CustomerDTO updateCustomer(CustomerCmd cmd, String openId, String otherId, String platform) {
        CustomerDO beforeCustomerDO = customerValidator.checkCustomer(cmd.getId());
        CustomerDO afterCustomerDO = CustomerConvertor.toCustomerDO(beforeCustomerDO, cmd);
        try {
            customerMapper.updateByPrimaryKey(afterCustomerDO);
        } catch (DuplicateKeyException e) {// 手机号已经存在情况,合并账号
            afterCustomerDO =
                customerMapper.selectByPhoneAndDistributorId(cmd.getPhone(), afterCustomerDO.getDistributorId());
            CustomerPlatformDO platformDO = customerPlatformMapper
                .selectByOpenIdAndOtherIdAndPlatformAndCustomerId(cmd.getId(), openId, otherId, platform);
            if (platformDO != null) {
                platformDO.setCustomerId(afterCustomerDO.getId());
                customerPlatformMapper.updateByPrimaryKey(platformDO);
                // 冻结被合并的账号 TODO（更新被合并账号的业务数据归属）
                beforeCustomerDO.setParentId(afterCustomerDO.getParentId());
                beforeCustomerDO.setStatus(CUSTOMER_STATUS_1);
                customerMapper.updateByPrimaryKey(beforeCustomerDO);
            }
        }
        return CustomerConvertor.toCustomerDTO(afterCustomerDO);
    }

    /**
     * C端客户协议确认
     *
     * @param cmd
     */
    public void agreementCustomer(CustomerAgreementCmd cmd) {
        CustomerDO customerDO = customerValidator.checkCustomer(cmd.getId());
        customerDO.setAgreementFlag(cmd.getAgreementFlag());
        customerMapper.updateByPrimaryKey(customerDO);
    }

    /**
     * C端客户修改密码(旧密码修改新密码)
     *
     * @param cmd
     */
    public void passwordCustomer(CustomerPasswordCmd cmd) {
        CustomerDO customerDO = customerValidator.checkCustomer(cmd.getId());
        if (StringUtils.isBlank(customerDO.getPassword())) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_CHANGE_PASSWORD_NULL);
        } else if (!customerDO.getPassword().equals(cmd.getOldPassword())) {
            throw DistributorException.buildException(P_DISTRIBUTOR_OLD_PASSWORD_ERROR);
        }
        customerDO.setPassword(cmd.getNewPassword());
        customerMapper.updateByPrimaryKey(customerDO);
    }

    /**
     * C端客户通过验证码修改密码
     *
     * @param cmd
     */
    public void verifyPasswordCustomer(CustomerVerifyPasswordCmd cmd) {
        CustomerDO customerDO = customerValidator.checkCustomer(cmd.getId(), cmd.getPhone());
        commonRpcExe.checkPhoneVerifyCode(cmd.getPhone(), cmd.getCodeType(), cmd.getCode());
        customerDO.setPassword(cmd.getNewPassword());
        customerMapper.updateByPrimaryKey(customerDO);
    }

    /**
     * 根据id冻结解冻C端客户
     *
     * @param cmd
     */
    public void statusCustomer(CustomerStatusCmd cmd) {
        CustomerDO customerDO = customerMapper.selectByPrimaryKey(cmd.getId());
        if (customerDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_NULL);
        }
        customerDO.setStatus(cmd.getStatus());
        customerMapper.updateByPrimaryKey(customerDO);
    }

    /**
     * 新增C端客户收货地址
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createAddress(CustomerAddressCmd cmd) {
        if (cmd.getDefaultFlag() == null
            || (cmd.getDefaultFlag() != null && cmd.getDefaultFlag().equals(DEFAULT_FLAG_1))) {
            List<CustomerAddressDO> addressDOS =
                customerAddressMapper.listByCustomerIdAndDefaultFlag(cmd.getCustomerId(), DEFAULT_FLAG_1);
            if (!CollectionUtils.isEmpty(addressDOS)) {
                addressDOS.get(0).setDefaultFlag(DEFAULT_FLAG_0);
                customerAddressMapper.updateByPrimaryKey(addressDOS.get(0));
            } else {
                cmd.setDefaultFlag(DEFAULT_FLAG_1);
            }
        }
        CustomerAddressDO addressDO = CustomerConvertor.toCustomerAddressDO(cmd);
        rpcCmdExe.checkRegion(null, addressDO.getProvinceId(), addressDO.getCityId(), addressDO.getDistrictId());
        customerAddressMapper.insert(addressDO);
        return addressDO.getId();
    }

    /**
     * 修改C端客户收货地址
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(CustomerAddressCmd cmd) {
        CustomerAddressDO beforeAddressDO = customerAddressMapper.selectByPrimaryKey(cmd.getId());
        if (beforeAddressDO == null) {
            throw DistributorException.buildException(P_DISTRIBUTOR_USER_ADDRESS_ERROR);
        }
        if (cmd.getDefaultFlag() == null
            || (cmd.getDefaultFlag() != null && cmd.getDefaultFlag().equals(DEFAULT_FLAG_1))) {
            List<CustomerAddressDO> addressDOS =
                customerAddressMapper.listByCustomerIdAndDefaultFlag(cmd.getCustomerId(), DEFAULT_FLAG_1);
            if (!CollectionUtils.isEmpty(addressDOS) && !cmd.getId().equals(addressDOS.get(0).getId())) {
                addressDOS.get(0).setDefaultFlag(DEFAULT_FLAG_0);
                customerAddressMapper.updateByPrimaryKey(addressDOS.get(0));
            } else {
                cmd.setDefaultFlag(DEFAULT_FLAG_1);
            }
        }
        CustomerAddressDO addressDO = CustomerConvertor.toCustomerAddressDO(cmd);
        rpcCmdExe.checkRegion(null, addressDO.getProvinceId(), addressDO.getCityId(), addressDO.getDistrictId());
        addressDO.setCreateTime(beforeAddressDO.getCreateTime());
        customerAddressMapper.updateByPrimaryKey(addressDO);
    }

    /**
     * 设置C端客户收货地址为默认地址
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void defaultAddress(BaseId cmd) {
        CustomerAddressDO addressDO = customerAddressMapper.selectByPrimaryKey(cmd.getId());
        if (addressDO == null) {
            throw DistributorException.buildException(P_DISTRIBUTOR_USER_ADDRESS_ERROR);
        }
        List<CustomerAddressDO> addressDOS =
            customerAddressMapper.listByCustomerIdAndDefaultFlag(addressDO.getCustomerId(), DEFAULT_FLAG_1);
        if (!CollectionUtils.isEmpty(addressDOS) && !cmd.getId().equals(addressDOS.get(0).getId())) {
            addressDOS.get(0).setDefaultFlag(DEFAULT_FLAG_0);
            addressDOS.get(0).setUpdateTime(new Date(System.currentTimeMillis()));
            customerAddressMapper.updateByPrimaryKey(addressDOS.get(0));
        }
        addressDO.setDefaultFlag(DEFAULT_FLAG_1);
        addressDO.setUpdateTime(new Date(System.currentTimeMillis()));
        customerAddressMapper.updateByPrimaryKey(addressDO);
    }

    /**
     * 删除C端客户收货地址
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(BaseId cmd) {
        CustomerAddressDO addressDO = customerAddressMapper.selectByPrimaryKey(cmd.getId());
        if (addressDO == null) {
            throw DistributorException.buildException(P_DISTRIBUTOR_USER_ADDRESS_ERROR);
        }
        customerAddressMapper.deleteByPrimaryKey(addressDO.getId());
    }

    public String getWxOfficialProgramOpenId(String appId, String code, Integer distributorId) {
        List<WxPlatformDO> wxPlatformDOList =
            wxPlatformMapper.listDistributorWxPlatform(distributorId, appId, WX_PLATFORM_TYPE_1);
        if (wxPlatformDOList == null || wxPlatformDOList.size() == 0) {
            throw DistributorException.buildException(B_DISTRIBUTOR_WX_PLATFORM_NULL);
        }
        WxPlatformDO wxPlatformDO = wxPlatformDOList.get(0);
        CustomerWxOfficialProgramLoginCmd cmd = new CustomerWxOfficialProgramLoginCmd();
        cmd.setAppId(appId);
        cmd.setCode(code);
        WxOfficialProgramAuthorizeRpcDTO rpcDTO = rpcCmdExe.wxOfficialProgramAuthorize(cmd, wxPlatformDO);
        return rpcDTO.getOpenid();
    }
}
