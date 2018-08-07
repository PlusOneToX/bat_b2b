package com.bat.distributor.service.user.executor;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.data.DistributorIdsDTO;
import com.bat.distributor.api.role.dto.data.RoleDTO;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceMapper;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceSpecialBrandCategoryMapper;
import com.bat.distributor.dao.nextscaleprice.NextScalePriceSpecialMapper;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialBrandCategoryDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialDO;
import com.bat.distributor.dao.platform.WxPlatformMapper;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.dao.role.RoleMapper;
import com.bat.distributor.dao.role.dataobject.RoleDO;
import com.bat.distributor.dao.trade.TradeMapper;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import com.bat.distributor.service.Message.MessageSendService;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.CommonRpcExe;
import com.bat.distributor.service.common.Constant;
import com.bat.distributor.service.common.DistributorConstant;
import com.bat.distributor.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserQryExe {

    @Resource
    private DistributorMapper distributorMapper;

    @Resource
    private DistributorContactsMapper contactsMapper;

    @Resource
    private DistributorBusinessMapper businessMapper;

    @Resource
    private DistributorFinancialMapper financialMapper;

    @Resource
    private DistributorAddressMapper addressMapper;

    @Resource
    private DistributorExtendDataMapper extendDataMapper;

    @Resource
    private TradeMapper tradeMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private NextScalePriceMapper scalePriceMapper;

    @Resource
    private NextScalePriceSpecialMapper scalePriceSpecialMapper;

    @Resource
    private NextScalePriceSpecialBrandCategoryMapper brandCategoryMapper;

    @Resource
    private DistributorTreePathMapper treePathMapper;

    @Resource
    private DistributorBrandRelevanceNoMapper brandRelevanceNoMapper;

    @Resource
    private DistributorGoodsRelevanceNoMapper goodsRelevanceNoMapper;

    @Resource
    private UserRpcQryExe rpcQryExe;
    @Resource
    private WxPlatformMapper wxPlatformMapper;

    @Resource
    private MessageSendService sendService;

    @Resource
    private CommonRpcExe commonRpcExe;

    /**
     * 分销商账号或分销商联系人登录(先判断是否通过分销商账号登录，如不是再判断是否联系人登录)
     * 
     * @param qry
     * @return
     */
    public UserLoginDTO userLogin(UserLoginQry qry) {
        String userName = qry.getUserName();
        String password = qry.getPassword();
        DistributorDO distributorDO = distributorMapper.selectByName(userName);
        /** 分销商用户名找不到时，再找联系人 */
        String userPassword = null;
        DistributorContactsDO contactsDO = null;
        if (distributorDO == null) {
            contactsDO = contactsMapper.selectByPhoneOrEmail(userName);
            if (contactsDO == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_NON_EXISTENT);
            }
            distributorDO = distributorMapper.selectByPrimaryKey(contactsDO.getDistributorId());
            userPassword = contactsDO.getPassword();
            if (StringUtils.isBlank(userPassword)) {
                userPassword = distributorDO.getPassword();
            }
        } else {
            userPassword = distributorDO.getPassword();
        }
        if (StringUtils.isBlank(userPassword)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_USER_LOGIN_ERROR);
        }
        if (!userPassword.equals(password)) {
            // 需要匹配分销商和业务员相同用户名、但密码不一样的情况
            if (contactsDO != null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_PASSWORD_ERROR);
            }
            contactsDO = contactsMapper.selectByPhoneOrEmail(userName);
            // 校验业务员的密码
            if (contactsDO == null || !password.equals(contactsDO.getPassword())) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_PASSWORD_ERROR);
            }
        }
        if (Constant.FREEZE_STATUS_2.equals(distributorDO.getFreezeStatus())) {
            throw DistributorException.buildException(ErrorCode.D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN);
        }
        if (contactsDO != null && Constant.FREEZE_STATUS_2.equals(contactsDO.getFreezeStatus())) {
            throw DistributorException.buildException(ErrorCode.D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN);
        }
        if (!Constant.APPLY_STATUS_2.equals(distributorDO.getApplyStatus())) {
            throw DistributorException.buildException(ErrorCode.D_DISTRIBUTOR_LOGIN_FAIL_APPLY_STATUS_NOT_PASS);
        }
        if (StringUtils.isNotBlank(qry.getOpenId()) && contactsDO != null
            && !qry.getOpenId().equals(contactsDO.getOpenId())) {
            // 覆盖openId
            contactsDO.setOpenId(qry.getOpenId());
            contactsDO.setUpdateTime(new Date());
            contactsMapper.updateByPrimaryKey(contactsDO);
        }
        /** 组装登录信息 */
        return getUserLoginDTO(distributorDO, contactsDO);
    }

    /**
     * 组装登录信息
     * 
     * @return
     */
    private UserLoginDTO getUserLoginDTO(DistributorDO distributorDO, DistributorContactsDO contactsDO) {
        if (distributorDO.getApplyStatus().equals(Constant.APPLY_STATUS_3)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_USER_NEXT_LOGIN_APPLY_STATUS_3_ERROR);
        } else if (!distributorDO.getApplyStatus().equals(Constant.APPLY_STATUS_2)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_USER_NEXT_LOGIN_APPLY_STATUS_ERROR);
        }
        UserLoginDTO dto = UserConvertor.toUserDTO(distributorDO);
        dto.setAccountType(Constant.ACCOUNT_TYPE_1);
        if (contactsDO != null) {
            dto.setAccountType(Constant.ACCOUNT_TYPE_2);
            dto.setContact(UserConvertor.toContactDTO(contactsDO));
        }
        DistributorBusinessDO businessDO = businessMapper.getByDistributorId(dto.getId());
        if (businessDO != null) {
            BeanUtils.copyProperties(businessDO, dto);
            dto.setId(distributorDO.getId());
        }
        if (distributorDO.getTreeNode() > 1) {

        }
        /** 如果分销商财务数据或币种未设置时，默认为人民币 */
        DistributorFinancialDO financialDO = financialMapper.getByDistributorId(dto.getId());
        if (financialDO != null && financialDO.getCoinType() != null) {
            dto.setCoinType(financialDO.getCoinType());
        } else {
            dto.setCoinType(Constant.COIN_TYPE_1);
        }
        /** 平台显示语言和币种 */
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(dto.getId());
        if (extendDataDO != null) {
            BeanUtils.copyProperties(extendDataDO, dto);
            dto.setId(distributorDO.getId());
            // 处理分账权限
            dto.setSubAccountFlag(DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_NO);
            if (DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_YES.equals(extendDataDO.getCustomerFlag())
                && DistributorConstant.DISTRIBUTOR_CUSTOMER_MODE_SELF.equals(extendDataDO.getCustomerMode())
                && extendDataDO.getSubAccountFlag() != null
                && DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_YES.equals(extendDataDO.getSubAccountFlag())) {
                // 开启了C端模式、且是分销商收款、开启分账功能才返回前端属于分账
                dto.setSubAccountFlag(DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_YES);
            }
        }
        sendService.distributorLogPackage(distributorDO.getId(), "分销商账号或分销商联系人登录", "登录成功", null);
        return dto;
    }

    /**
     * 第三方联系人手机号登录
     * 
     * @param qry
     * @return
     */
    public UserLoginDTO phoneLogin(UserPhoneQry qry) {
        UserLoginDTO userLoginDTO = phoneLogin(qry.getPhone(), qry.getOpenId());
        sendService.distributorLogPackage(userLoginDTO.getId(), "第三方联系人手机号登录", "登录成功", JSONObject.toJSONString(qry));
        return userLoginDTO;
    }

    /**
     * 第三方联系人手机号登录
     * 
     * @param phone
     * @param openId
     * @return
     */
    public UserLoginDTO phoneLogin(String phone, String openId) {
        DistributorContactsDO contactsDO = contactsMapper.selectByPhoneOrEmail(phone);
        if (contactsDO == null) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_USER_NON_EXISTENT);
        }
        if (Constant.FREEZE_STATUS_2.equals(contactsDO.getFreezeStatus())) {
            throw DistributorException.buildException(ErrorCode.D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN);
        }
        if (StringUtils.isNotBlank(openId) && !openId.equals(contactsDO.getOpenId())) {
            // 覆盖
            contactsDO.setOpenId(openId);
            contactsDO.setUpdateTime(new Date());
            contactsMapper.updateByPrimaryKey(contactsDO);
            DistributorExtendDataDO distributorExtendDataDO =
                extendDataMapper.getByDistributorId(contactsDO.getDistributorId());
            if (distributorExtendDataDO != null) {
                distributorExtendDataDO.setOpenId(openId);
                extendDataMapper.updateByPrimaryKey(distributorExtendDataDO);
            }
        }
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(contactsDO.getDistributorId());
        if (Constant.FREEZE_STATUS_2.equals(distributorDO.getFreezeStatus())) {
            throw DistributorException.buildException(ErrorCode.D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN);
        }
        if (!Constant.APPLY_STATUS_2.equals(distributorDO.getApplyStatus())) {
            throw DistributorException.buildException(ErrorCode.D_DISTRIBUTOR_LOGIN_FAIL_APPLY_STATUS_NOT_PASS);
        }
        return getUserLoginDTO(distributorDO, contactsDO);
    }

    /**
     * 分销微信小程序授权登录
     * 
     * @param cmd
     * @return
     */
    public UserLoginDTO wxMiniProgramLogin(UserWxMiniProgramLoginCmd cmd) {
        WxMiniProgramAuthorizeRpcDTO rpcDTO = rpcQryExe.wxMiniProgramAuthorize(cmd);
        UserLoginDTO userLoginDTO = phoneLogin(rpcDTO.getPhone(), rpcDTO.getOpenid());
        sendService.distributorLogPackage(userLoginDTO.getId(), "分销微信小程序授权登录", "登录成功", JSONObject.toJSONString(cmd));
        return userLoginDTO;
    }

    /**
     * 分销微信小程序授权openid
     * 
     * @param cmd
     * @return
     */
    public String wxMiniProgramOpenId(UserWxMiniProgramOpenIdCmd cmd) {
        WxMiniProgramAuthorizeRpcDTO rpcDTO = rpcQryExe.wxMiniProgramAuthorize(cmd);
        return rpcDTO.getOpenid();
    }

    /**
     * 获取分销商信息
     * 
     * @param qry
     * @return
     */
    public UserInfoDTO getUserInfo(BaseId qry) {
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(qry.getId());
        if (distributorDO == null) {
            throw DistributorException.buildException(com.bat.distributor.service.distributor.executor.ErrorCode.B_DISTRIBUTOR_NULL);
        }
        DistributorFinancialDO financialDO = financialMapper.getByDistributorId(qry.getId());
        List<DistributorAddressDO> addressDOS =
            addressMapper.listByDistributorIdAndAddressType(distributorDO.getId(), Constant.ADDRESS_TYPE_1);
        List<DistributorContactsDO> contactsDOS =
            contactsMapper.selectByDistributorIdAndOwnerFlag(qry.getId(), Constant.OWNER_FLAG_1);
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(qry.getId());
        UserInfoDTO dto =
            UserConvertor.toUserInfoDTO(distributorDO, financialDO, addressDOS, contactsDOS, extendDataDO);
        if (financialDO != null && financialDO.getTradeId() != null) {
            TradeDO tradeDO = tradeMapper.selectByPrimaryKey(financialDO.getTradeId());
            dto.getFinancial().setTrade(tradeDO.getName());
            dto.getFinancial().setTradeEn(tradeDO.getNameEn());
        }
        return dto;
    }

    /**
     * 查询分销商联系人列表
     *
     * @param qry
     * @return
     */
    public PageInfo<ContactDTO> listContact(UserContactListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DistributorContactsDO> contactsDOS = contactsMapper.listByDistributorId(qry.getId());
        PageInfo pageInfo = new PageInfo(contactsDOS);
        List<ContactDTO> contactDTOS = UserConvertor.toDistributorContactsDTOList(contactsDOS);
        pageInfo.setList(contactDTOS);
        return pageInfo;
    }

    /**
     * 查询分销商联系人
     * 
     * @param qry
     * @return
     */
    public ContactDTO getContact(UserContactQry qry) {
        String phoneOrEmail;
        if (StringUtils.isNotBlank(qry.getPhone())) {
            phoneOrEmail = qry.getPhone();
        } else if (StringUtils.isNotBlank(qry.getEmail())) {
            phoneOrEmail = qry.getEmail();
        } else {
            throw DistributorException
                .buildException(com.bat.distributor.service.distributor.executor.ErrorCode.B_DISTRIBUTOR_CONTACT_PHONE_EMAIL_CANNOT_BE_EMPTY_AT_THE_SAME_TIME);
        }
        DistributorContactsDO distributorContactsDO = contactsMapper.selectByPhoneOrEmail(phoneOrEmail);
        if (distributorContactsDO == null) {
            throw DistributorException.buildException(com.bat.distributor.service.distributor.executor.ErrorCode.B_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NOT_EXISTS);
        }
        return UserConvertor.toContactDTO(distributorContactsDO);
    }

    /**
     * 分销商联系人角色列表查询
     *
     * @param qry
     * @return
     */
    public PageInfo<UserContactRoleDTO> listContactRole(UserAddressListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<RoleDO> roleDOList = roleMapper.listRole(qryMap);
        PageInfo pageInfo = new PageInfo(roleDOList);
        List<RoleDTO> roleDTOS = UserConvertor.toRoleDTOList(pageInfo.getList());
        pageInfo.setList(roleDTOS);
        return pageInfo;
    }

    /**
     * 查询分销商收货地址列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserAddressDTO> listAddress(UserAddressListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DistributorAddressDO> addressDOS =
            addressMapper.listByDistributorIdAndAddressType(qry.getId(), Constant.ADDRESS_TYPE_2);
        PageInfo pageInfo = new PageInfo(addressDOS);
        List<UserAddressDTO> addressDTOS = UserConvertor.toUserAddressDTOList(pageInfo.getList());
        pageInfo.setList(addressDTOS);
        return pageInfo;
    }

    /**
     * 分销商获取分销二维码
     *
     * @param qry
     * @return
     */
    public String getDistributionQrCode(UserQrCodeQry qry) {
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(qry.getDistributorId());
        if (extendDataDO.getDistributionFlag().equals(Constant.DISTRIBUTION_FLAG_0)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_USER_NO_DISTRIBUTION_FLAG);
        }
        return extendDataDO.getDistributionQrUrl();
    }

    /**
     * 分销商获取价格等级列表(不分页)
     * 
     * @param qry
     * @return
     */
    public List<UserNextScalePriceDTO> listUserNextScalePrice(UserNextScalePriceListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        List<NextScalePriceDO> nextScalePriceDOS = scalePriceMapper.listNextScalePrice(qryMap);
        return UserConvertor.toNextScalePriceDTOList(nextScalePriceDOS);
    }

    /**
     * 分销商获取价格等级详情
     * 
     * @param qry
     * @return
     */
    public UserNextScalePriceDTO getUserNextScalePrice(BaseId qry) {
        NextScalePriceDO nextScalePriceDO = scalePriceMapper.selectByPrimaryKey(qry.getId());
        UserNextScalePriceDTO dto = UserConvertor.toUserNextScalePriceDTO(nextScalePriceDO);
        List<NextScalePriceSpecialDO> nextScalePriceSpecialDOS =
            scalePriceSpecialMapper.listByNextScalePriceId(nextScalePriceDO.getId());
        if (!CollectionUtils.isEmpty(nextScalePriceSpecialDOS)) {
            nextScalePriceSpecialDOS.forEach(nextScalePriceSpecialDO -> {
                UserNextScalePriceSpecialDTO scalePriceSpecialDTO =
                    UserConvertor.toUserNextScalePriceSpecialDTO(nextScalePriceSpecialDO);
                List<NextScalePriceSpecialBrandCategoryDO> brandCategoryDOS =
                    brandCategoryMapper.listByNextScalePriceSpecialId(scalePriceSpecialDTO.getId());
                if (!CollectionUtils.isEmpty(brandCategoryDOS)) {
                    List<UserNextScalePriceSpecialBrandCategoryDTO> dtos =
                        UserConvertor.toUserNextScalePriceSpecialBrandCategoryDTOList(brandCategoryDOS);
                    scalePriceSpecialDTO.setBrandCategorys(dtos);
                }
                List<UserNextScalePriceSpecialDTO> scalePriceSpecials = dto.getScalePriceSpecials();
                if (scalePriceSpecials == null) {
                    scalePriceSpecials = new ArrayList<>();
                    dto.setScalePriceSpecials(scalePriceSpecials);
                }
                scalePriceSpecials.add(scalePriceSpecialDTO);
            });
        }
        return dto;
    }

    /**
     * 查询下级分销商列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserNextListDTO> nextList(UserNextListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<UerNextListDO> uerNextListDOS = distributorMapper.listNextDistributorByDistributorId(qryMap);
        PageInfo pageInfo = new PageInfo(uerNextListDOS);
        List<UserNextListDTO> dtos = UserConvertor.toUserNextListDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 查看下级分销商详情
     * 
     * @param qry
     * @return
     */
    public UserNextDTO getNextDistributor(BaseId qry) {
        DistributorNextDO distributorNextDO = distributorMapper.selectNextDistributorById(qry.getId());
        UserNextDTO dto = UserConvertor.toUserNextDTO(distributorNextDO);
        return dto;
    }

    /**
     * 根据分销商id查询品牌列表
     * 
     * @param qry
     * @return
     */
    public List<UserBrandDTO> listBrand(UserId qry) {
        /** 最上级节点 */
        DistributorDO masterDistributorDO = null;
        /** 上一级节点 */
        DistributorTreePathDO upDistributorTreePathDO = null;

        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(qry.getDistributorId());
        /** 多级分销情况，需找上一级的可视品牌范围 */
        if (distributorDO.getTreeNode() > 1) {
            DistributorTreePathDO masterDistributorTreePathDO = treePathMapper
                .selectByDistributorDescendantIdAndTreeNode(qry.getDistributorId(), distributorDO.getTreeNode() - 1);
            masterDistributorDO =
                distributorMapper.selectByPrimaryKey(masterDistributorTreePathDO.getDistributorAncestorId());
            upDistributorTreePathDO =
                treePathMapper.selectByDistributorDescendantIdAndTreeNode(qry.getDistributorId(), 1);
        } else {
            masterDistributorDO = distributorDO;
        }
        /** 获取最上级分销商品牌范围 */
        List<UserBrandRpcDTO> rpcDTOS = rpcQryExe.listBrand(masterDistributorDO.getId());
        /** 排除上一级分销商不可视品牌数据 */
        if (!CollectionUtils.isEmpty(rpcDTOS) && upDistributorTreePathDO != null) {
            DistributorBrandRelevanceNoDO relevanceNoDO =
                brandRelevanceNoMapper.selectByDistributorId(upDistributorTreePathDO.getDistributorAncestorId());
            if (relevanceNoDO != null) {
                List<Integer> brandIds = Arrays
                    .stream(
                        relevanceNoDO.getBrandIds().substring(1, relevanceNoDO.getBrandIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                rpcDTOS = rpcDTOS.stream().filter(dto -> brandIds.contains(dto.getId())).collect(Collectors.toList());
            }
        }
        List<UserBrandDTO> dtos = UserConvertor.toUserBrandDTOList(rpcDTOS);
        return dtos;
    }

    /**
     * 根据分销商id查询商品列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsDTO> listGoods(UserGoodsListQry qry) {
        /** 最上级节点 */
        DistributorDO masterDistributorDO = null;
        /** 上一级节点 */
        DistributorTreePathDO upDistributorTreePathDO = null;

        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(qry.getDistributorId());
        /** 多级分销情况，需找上一级的可视品牌范围 */
        if (distributorDO.getTreeNode() > 1) {
            DistributorTreePathDO masterDistributorTreePathDO = treePathMapper
                .selectByDistributorDescendantIdAndTreeNode(qry.getDistributorId(), distributorDO.getTreeNode() - 1);
            masterDistributorDO =
                distributorMapper.selectByPrimaryKey(masterDistributorTreePathDO.getDistributorAncestorId());
            upDistributorTreePathDO =
                treePathMapper.selectByDistributorDescendantIdAndTreeNode(qry.getDistributorId(), 1);
        } else {
            masterDistributorDO = distributorDO;
        }

        /** 排除上一级分销商不可视商品数据 */
        DistributorGoodsRelevanceNoDO relevanceNoDO = null;
        if (upDistributorTreePathDO != null) {
            relevanceNoDO =
                goodsRelevanceNoMapper.selectByDistributorId(upDistributorTreePathDO.getDistributorAncestorId());
        }
        /** 获取最上级分销商商品范围,且排除上一级分销商不可视商品数据 */
        List<Integer> noGoodsIds = new ArrayList<>();
        if (relevanceNoDO != null) {
            noGoodsIds = Arrays
                .stream(relevanceNoDO.getGoodsIds().substring(1, relevanceNoDO.getGoodsIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }
        PageInfo pageInfo = rpcQryExe.listGoods(qry, noGoodsIds, masterDistributorDO.getId());
        List<UserGoodsDTO> dtos = UserConvertor.toUserGoodsDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 获取下级分销商不可视品牌id列表(不分页)
     * 
     * @param qry
     * @return
     */
    public List<Integer> listNoBrand(UserId qry) {
        List<Integer> ids = new ArrayList<>();
        DistributorBrandRelevanceNoDO relevanceNoDO =
            brandRelevanceNoMapper.selectByDistributorId(qry.getDistributorId());
        if (relevanceNoDO != null) {
            ids = Arrays
                .stream(relevanceNoDO.getBrandIds().substring(1, relevanceNoDO.getBrandIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }
        return ids;
    }

    /**
     * 获取下级分销商不可视商品id列表(不分页)
     * 
     * @param qry
     * @return
     */
    public List<Integer> listNoGoods(UserId qry) {
        List<Integer> ids = new ArrayList<>();
        DistributorGoodsRelevanceNoDO relevanceNoDO =
            goodsRelevanceNoMapper.selectByDistributorId(qry.getDistributorId());
        if (relevanceNoDO != null) {
            ids = Arrays
                .stream(relevanceNoDO.getGoodsIds().substring(1, relevanceNoDO.getGoodsIds().length() - 1).split(","))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }
        return ids;
    }

    /**
     * 分销商获取支付方式
     * 
     * @param userId
     * @return
     */
    public UserPayMentModeDTO paymentMode(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw DistributorException.buildException(CommonErrorCode.B_USER_LOGIN_ERROR);
        }
        Integer distributorId = Integer.valueOf(userId);
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(distributorId);
        if (distributorDO == null) {
            throw DistributorException.buildException(com.bat.distributor.service.distributor.executor.ErrorCode.B_DISTRIBUTOR_NULL);
        }
        UserPayMentModeDTO dto = new UserPayMentModeDTO();
        DistributorExtendDataDO extendDataDO = extendDataMapper.getByDistributorId(distributorId);
        if (extendDataDO != null && extendDataDO.getErpFlag().equals(Constant.ERP_FLAG_1)) {
            DistributorFinancialDO financialDO = financialMapper.getByDistributorId(distributorId);
            TradeDO tradeDO = tradeMapper.selectByPrimaryKey(financialDO.getTradeId());
            dto.setPayWay(tradeDO.getPayWay());
            dto.setOfflineFlag(Constant.OFFLINE_FLAG_1);
            dto.setPayName(tradeDO.getName());
            dto.setPayNameEn(tradeDO.getNameEn());
        } else {
            dto.setPayWay(Constant.PAY_WAY_1);
            dto.setOfflineFlag(Constant.OFFLINE_FLAG_0);
        }
        return dto;
    }

    /**
     * 根据分销商id获取业务数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorBusinessDO getDistributorBusinessDOByDistributorId(Integer distributorId) {
        return businessMapper.getByDistributorId(distributorId);
    }

    /**
     * 分销商查询微信平台列表
     * 
     * @param userId
     * @param qry
     * @return
     */
    public List<UserWxPlatformListDTO> listWxPlatform(String userId, UserWxPlatformListQry qry) {
        if (StringUtils.isBlank(userId)) {
            throw DistributorException.buildException(CommonErrorCode.B_USER_LOGIN_ERROR);
        }
        List<WxPlatformDO> wxPlatformDOS =
            wxPlatformMapper.listWxPlatformByDistributorId(Integer.valueOf(userId), qry.getType());
        return UserConvertor.toUserWxPlatformListDTOList(wxPlatformDOS);
    }

    public UserLoginDTO wxMiniProgramLoginByOpenId(String openId) {

        List<DistributorContactsDO> contactsDOList =
            contactsMapper.listByOpenIdAndFreezeStatus(openId, Constant.FREEZE_STATUS_1);
        if (contactsDOList == null || contactsDOList.size() == 0) {
            return null;
        }
        if (contactsDOList.size() > 1) {
            throw DistributorException
                .buildException(CommonErrorCode.B_DISTRIBUTOR_CONTACTS_USABLE_MORE_THEN_ONE_ERROR);
        }
        DistributorContactsDO contactsDO = contactsDOList.get(0);
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(contactsDO.getDistributorId());
        if (Constant.FREEZE_STATUS_2.equals(distributorDO.getFreezeStatus())) {
            throw DistributorException.buildException(ErrorCode.D_CUSTOMER_LOGIN_FAIL_OPEN_FLAG_FROZEN);
        }
        if (!Constant.APPLY_STATUS_2.equals(distributorDO.getApplyStatus())) {
            throw DistributorException.buildException(ErrorCode.D_DISTRIBUTOR_LOGIN_FAIL_APPLY_STATUS_NOT_PASS);
        }
        UserLoginDTO userLoginDTO = getUserLoginDTO(distributorDO, contactsDO);
        Map<String, String> map = new HashMap<>();
        map.put("openId", openId);
        sendService.distributorLogPackage(userLoginDTO.getId(), "分销微信小程序授权登录", "登录成功", JSONObject.toJSONString(map));
        return userLoginDTO;
    }

    /**
     *
     * @param userMobileLoginQry
     * @return
     */
    public UserLoginDTO wxMiniProgramLoginByPhoneAndSmsCode(UserMobileLoginQry userMobileLoginQry) {
        // 校验短信验证码
        commonRpcExe.checkPhoneVerifyCode(userMobileLoginQry.getPhone(), userMobileLoginQry.getCodeType(),
            userMobileLoginQry.getCode());

        UserLoginDTO userLoginDTO = phoneLogin(userMobileLoginQry.getPhone(), userMobileLoginQry.getOpenId());
        sendService.distributorLogPackage(userLoginDTO.getId(), "分销微信小程序授权登录", "登录成功",
            JSONObject.toJSONString(userMobileLoginQry));
        // 进行openId绑定
        return userLoginDTO;
    }

    public DistributorIdsDTO getBaseMsgByDistributorId(Integer id) {
        DistributorDO distributorDO = distributorMapper.selectByPrimaryKey(id);
        DistributorIdsDTO distributorIdsDTO = new DistributorIdsDTO();
        BeanUtils.copyProperties(distributorDO, distributorIdsDTO);
        return distributorIdsDTO;
    }
}
