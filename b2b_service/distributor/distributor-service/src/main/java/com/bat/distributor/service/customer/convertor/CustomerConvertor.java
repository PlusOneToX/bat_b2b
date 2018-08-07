package com.bat.distributor.service.customer.convertor;

import static com.bat.distributor.service.common.Constant.*;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.distributor.api.customer.dto.data.CustomerListDTO;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.api.user.dto.customer.data.CustomerAddressDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerLoginDTO;
import com.bat.distributor.api.user.dto.user.UserWxMiniProgramLoginCmd;
import com.bat.distributor.api.user.dto.user.UserWxMiniProgramOpenIdCmd;
import com.bat.distributor.dao.customer.dataobject.CustomerAddressDO;
import com.bat.distributor.dao.customer.dataobject.CustomerDO;
import com.bat.distributor.dao.customer.dataobject.CustomerPlatformDO;
import com.bat.distributor.dao.platform.dataobject.AlipayPlatformDO;
import com.bat.distributor.dao.platform.dataobject.DyPlatformDO;
import com.bat.distributor.dao.platform.dataobject.PlatformDO;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.AlipayProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data.AlipayProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.dy.dto.DyMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.distributor.service.common.CommonUtils;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.WxMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.WxOfficialProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 16:28
 */
public class CustomerConvertor {

    public static CustomerDO toCustomerDO(String phone, Integer distributorId, String platform) {
        CustomerDO customerDO = new CustomerDO();
        customerDO.setDistributorId(distributorId);
        customerDO.setPlatform(platform);
        customerDO.setStatus(CUSTOMER_STATUS_1);
        customerDO.setAgreementFlag(AGREEMENT_FLAG_0);
        if (StringUtils.isBlank(phone)) {
            customerDO.setCustomerType(CUSTOMER_TYPE_2);
        } else {
            customerDO.setPhone(phone);
            customerDO.setName(phone);
            customerDO.setCustomerType(CUSTOMER_TYPE_1);
        }
        Date date = new Date(System.currentTimeMillis());
        customerDO.setCreateTime(date);
        customerDO.setUpdateTime(date);
        return customerDO;
    }

    public static CustomerDO toCustomerDO(CustomerWxMiniProgramLoginCmd cmd, WxMiniProgramAuthorizeRpcDTO rpcDTO,
                                          Integer distributorId, String platform) {
        CustomerDO customerDO = new CustomerDO();
        customerDO.setDistributorId(distributorId);
        customerDO.setHeadPortrait(cmd.getAvatarUrl());
        customerDO.setSex(cmd.getGender());
        if (StringUtils.isNotBlank(cmd.getNickName())) {
            customerDO.setNikeName(CommonUtils.emojiConvert(cmd.getNickName()));
        }
        customerDO.setPlatform(platform);
        customerDO.setAgreementFlag(AGREEMENT_FLAG_0);
        customerDO.setStatus(CUSTOMER_STATUS_1);
        customerDO.setPhone(rpcDTO.getPhone());
        customerDO.setName(rpcDTO.getPhone());
        customerDO.setCustomerType(CUSTOMER_TYPE_1);
        Date date = new Date(System.currentTimeMillis());
        customerDO.setCreateTime(date);
        customerDO.setUpdateTime(date);
        return customerDO;
    }

    /**
     * 抖音小程序
     * 
     * @param cmd
     * @param rpcDTO
     * @param distributorId
     * @param platform
     * @return
     */
    public static CustomerDO toCustomerDO(CustomerDyProgramMiniLoginCmd cmd, DyMiniProgramAuthorizeRpcDTO rpcDTO,
                                          Integer distributorId, String platform) {
        CustomerDO customerDO = new CustomerDO();
        customerDO.setDistributorId(distributorId);
        customerDO.setHeadPortrait(cmd.getAvatarUrl());
        customerDO.setSex(cmd.getGender());
        if (StringUtils.isNotBlank(cmd.getNickName())) {
            customerDO.setNikeName(CommonUtils.emojiConvert(cmd.getNickName()));
        }
        customerDO.setPlatform(platform);
        customerDO.setAgreementFlag(AGREEMENT_FLAG_0);
        customerDO.setStatus(CUSTOMER_STATUS_1);
        customerDO.setPhone(rpcDTO.getPhone());
        customerDO.setName(rpcDTO.getPhone());
        customerDO.setCustomerType(CUSTOMER_TYPE_1);
        Date date = new Date(System.currentTimeMillis());
        customerDO.setCreateTime(date);
        customerDO.setUpdateTime(date);
        return customerDO;
    }

    /**
     * 支付宝小程序
     * 
     * @param cmd
     * @param rpcDTO
     * @param distributorId
     * @param platform
     * @return
     */
    public static CustomerDO toCustomerDO(CustomerAlipayProgramMiniLoginCmd cmd, AlipayProgramAuthorizeRpcDTO rpcDTO,
                                          Integer distributorId, String platform) {
        CustomerDO customerDO = new CustomerDO();
        customerDO.setDistributorId(distributorId);
        customerDO.setHeadPortrait(rpcDTO.getAvatar());
        if (StringUtils.isNotBlank(rpcDTO.getGender())) {
            if ("f".equals(rpcDTO.getGender())) {
                customerDO.setSex((short)2);
            } else if ("m".equals(rpcDTO.getGender())) {
                customerDO.setSex((short)1);
            }
        }
        if (StringUtils.isNotBlank(rpcDTO.getNickName())) {
            customerDO.setNikeName(CommonUtils.emojiConvert(rpcDTO.getNickName()));
        }
        customerDO.setPlatform(platform);
        customerDO.setAgreementFlag(AGREEMENT_FLAG_0);
        customerDO.setStatus(CUSTOMER_STATUS_1);
        customerDO.setPhone(rpcDTO.getPhone());
        customerDO.setName(rpcDTO.getPhone());
        customerDO.setCustomerType(CUSTOMER_TYPE_1);
        Date date = new Date(System.currentTimeMillis());
        customerDO.setCreateTime(date);
        customerDO.setUpdateTime(date);
        return customerDO;
    }

    public static CustomerLoginDTO toCustomerLoginDTO(CustomerDO customerDO, CustomerPlatformDO platformDO) {
        CustomerLoginDTO dto = new CustomerLoginDTO();
        BeanUtils.copyProperties(customerDO, dto);
        dto.setUserName(customerDO.getName());
        if (platformDO != null) {
            dto.setOpenId(platformDO.getOpenId());
            dto.setOtherId(platformDO.getOtherId());
            dto.setPlatformStatus(platformDO.getStatus());
        }
        return dto;
    }

    public static CustomerDTO toCustomerDTO(CustomerDO customerDO) {
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customerDO, dto);
        if (StringUtils.isNotBlank(dto.getNikeName())) {
            dto.setNikeName(CommonUtils.emojiRecovery(dto.getNikeName()));
        }
        dto.setUserName(customerDO.getNikeName());
        return dto;
    }

    public static CustomerPlatformDO toCustomerPlatformDO(Integer customerId, String openId, String otherUid,
        String platform) {
        CustomerPlatformDO platformDO = new CustomerPlatformDO();
        Date date = new Date(System.currentTimeMillis());
        platformDO.setCreateTime(date);
        platformDO.setUpdateTime(date);
        platformDO.setCustomerId(customerId);
        platformDO.setOpenId(openId);
        platformDO.setOtherId(otherUid);
        platformDO.setPlatform(platform);
        platformDO.setStatus(CUSTOMER_STATUS_1);
        return platformDO;
    }

    public static WxMiniProgramAuthorizeRpcCmd toWxMiniProgramAuthorizeRpcDTO(CustomerWxMiniProgramLoginCmd cmd,
        WxPlatformDO wxPlatformDO) {
        WxMiniProgramAuthorizeRpcCmd rpcCmd = new WxMiniProgramAuthorizeRpcCmd();
        BeanUtils.copyProperties(cmd, rpcCmd);
        rpcCmd.setAppid(wxPlatformDO.getAppId());
        rpcCmd.setSecret(wxPlatformDO.getAppSecret());
        return rpcCmd;
    }

    public static DyMiniProgramAuthorizeRpcCmd toDyMiniProgramAuthorizeRpcDTO(CustomerDyProgramMiniLoginCmd cmd,
        DyPlatformDO dyPlatformDO) {
        DyMiniProgramAuthorizeRpcCmd rpcCmd = new DyMiniProgramAuthorizeRpcCmd();
        BeanUtils.copyProperties(cmd, rpcCmd);
        rpcCmd.setAppid(dyPlatformDO.getAppId());
        rpcCmd.setSecret(dyPlatformDO.getAppSecret());
        return rpcCmd;
    }

    public static AlipayProgramAuthorizeRpcCmd toAlipayMiniProgramAuthorizeRpcDTO(CustomerAlipayProgramMiniLoginCmd cmd,
        AlipayPlatformDO alipayPlatformDO) {
        AlipayProgramAuthorizeRpcCmd rpcCmd = new AlipayProgramAuthorizeRpcCmd();
        BeanUtils.copyProperties(cmd, rpcCmd);
        rpcCmd.setAppId(alipayPlatformDO.getAppId());
        rpcCmd.setAppPrivateKey(alipayPlatformDO.getAppPrivateKey());
        rpcCmd.setAppPublicKey(alipayPlatformDO.getAppPublicKey());
        return rpcCmd;
    }

    public static WxMiniProgramAuthorizeRpcCmd toWxMiniProgramAuthorizeRpcDTO(UserWxMiniProgramLoginCmd cmd,
                                                                              String appId, String appSecret) {
        WxMiniProgramAuthorizeRpcCmd rpcCmd = new WxMiniProgramAuthorizeRpcCmd();
        BeanUtils.copyProperties(cmd, rpcCmd);
        rpcCmd.setAppid(appId);
        rpcCmd.setSecret(appSecret);
        return rpcCmd;
    }

    public static WxMiniProgramAuthorizeRpcCmd toWxMiniProgramAuthorizeRpcDTO(UserWxMiniProgramOpenIdCmd cmd,
                                                                              String appId, String appSecret) {
        WxMiniProgramAuthorizeRpcCmd rpcCmd = new WxMiniProgramAuthorizeRpcCmd();
        BeanUtils.copyProperties(cmd, rpcCmd);
        rpcCmd.setAppid(appId);
        rpcCmd.setSecret(appSecret);
        return rpcCmd;
    }

    public static WxOfficialProgramAuthorizeRpcCmd
        toWxOfficialProgramAuthorizeRpcDTO(CustomerWxOfficialProgramLoginCmd cmd, WxPlatformDO wxPlatformDO) {
        WxOfficialProgramAuthorizeRpcCmd rpcCmd = new WxOfficialProgramAuthorizeRpcCmd();
        rpcCmd.setAppid(wxPlatformDO.getAppId());
        rpcCmd.setCode(cmd.getCode());
        rpcCmd.setSecret(wxPlatformDO.getAppSecret());
        return rpcCmd;
    }

    public static CustomerDO toCustomerDO(CustomerDO beforeCustomerDO, CustomerCmd cmd) {
        CustomerDO customerDO = new CustomerDO();
        BeanUtils.copyProperties(beforeCustomerDO, customerDO);
        BeanUtils.copyProperties(cmd, customerDO);
        if (StringUtils.isNotBlank(cmd.getNikeName())) {
            customerDO.setNikeName(CommonUtils.emojiConvert(cmd.getNikeName()));
        }
        Date date = new Date(System.currentTimeMillis());
        customerDO.setUpdateTime(date);
        if (StringUtils.isBlank(cmd.getPhone())) {
            customerDO.setPhone(null);
            customerDO.setCustomerType(CUSTOMER_TYPE_2);
        } else {
            customerDO.setCustomerType(CUSTOMER_TYPE_1);
        }
        return customerDO;
    }

    public static List<CustomerListDTO> toCustomerListDTOList(List<CustomerDO> customerDOS,
                                                              List<PlatformDO> platformDOS) {
        List<CustomerListDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customerDOS)) {
            Map<String, PlatformDO> platformDOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(platformDOS)) {
                platformDOMap.putAll(platformDOS.stream()
                    .collect(Collectors.toMap(PlatformDO::getPlatformNo, platformDO -> platformDO)));
            }
            customerDOS.forEach(customerDO -> {
                CustomerListDTO dto = new CustomerListDTO();
                BeanUtils.copyProperties(customerDO, dto);
                if (StringUtils.isNotBlank(dto.getNikeName())) {
                    customerDO.setNikeName(CommonUtils.emojiConvert(dto.getNikeName()));
                }
                if (StringUtils.isNotBlank(customerDO.getPlatform()) && !CollectionUtils.isEmpty(platformDOMap)) {
                    PlatformDO platformDO = platformDOMap.get(customerDO.getPlatform());
                    if (platformDO != null) {
                        dto.setPlatformName(platformDO.getName());
                    }
                }
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static CustomerRpcDTO toCustomerRpcDTO(CustomerDO customerDO, PlatformDO platformDO,
        DistributorRpcDTO distributorRpcDTO, List<CustomerPlatformDO> customerPlatformDOS) {
        if (customerDO != null) {
            CustomerRpcDTO rpcDTO = new CustomerRpcDTO();
            rpcDTO.setId(customerDO.getId());
            rpcDTO.setName(customerDO.getName());
            rpcDTO.setNikeName(customerDO.getNikeName());
            rpcDTO.setHeadPortrait(customerDO.getHeadPortrait());
            if (platformDO != null) {
                rpcDTO.setPlatform(platformDO.getPlatformNo());
                rpcDTO.setPlatformOpenFlag(platformDO.getOpenFlag());
            }
            if (!CollectionUtils.isEmpty(customerPlatformDOS)) {
                CustomerPlatformDO customerPlatformDO = customerPlatformDOS.get(0);
                rpcDTO.setPlatformStatus(customerPlatformDO.getStatus());
                rpcDTO.setOtherId(customerPlatformDO.getOtherId());
                rpcDTO.setOpenId(customerPlatformDO.getOpenId());
            }
            if (distributorRpcDTO != null) {
                rpcDTO.setDistributor(distributorRpcDTO);
            }
            return rpcDTO;
        }
        return null;
    }

    public static CustomerAddressDO toCustomerAddressDO(CustomerAddressCmd cmd) {
        CustomerAddressDO addressDO = new CustomerAddressDO();
        BeanUtils.copyProperties(cmd, addressDO);
        Date date = new Date(System.currentTimeMillis());
        addressDO.setCreateTime(date);
        addressDO.setUpdateTime(date);
        return addressDO;
    }

    public static List<CustomerAddressDTO> toCustomerAddressDTOList(List<CustomerAddressDO> addressDOS) {
        List<CustomerAddressDTO> addressDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addressDOS)) {
            addressDOS.forEach(addressDO -> {
                CustomerAddressDTO addressDTO = new CustomerAddressDTO();
                BeanUtils.copyProperties(addressDO, addressDTO);
                addressDTOS.add(addressDTO);
            });
        }
        return addressDTOS;
    }
}
