package com.bat.distributor.api.user;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.distributor.dto.data.DistributorIdsDTO;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.dataobject.DistributorBusinessDO;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;

import java.util.List;

public interface UserServiceI {

    /**
     * 分销商账号或分销商联系人登录
     * 
     * @param qry
     * @return
     */
    public UserLoginDTO userLogin(UserLoginQry qry);

    /**
     * 分销商第三方获取联系人手机号登录
     *
     * @param qry
     * @return
     */
    public UserLoginDTO phoneLogin(UserPhoneQry qry);

    /**
     * 分销微信小程序授权登录
     * 
     * @param cmd
     * @return
     */
    UserLoginDTO wxMiniProgramLogin(UserWxMiniProgramLoginCmd cmd);

    /**
     * 分销微信小程序授权openid
     * 
     * @param cmd
     * @return
     */
    String wxMiniProgramOpenId(UserWxMiniProgramOpenIdCmd cmd);

    /**
     * 一级分销商注册申请
     * 
     * @param cmd
     * @return
     */
    public UserApplyDTO oneLevelApply(UserOneLevelApplyCmd cmd);

    /**
     * 新增分销商联系人
     * 
     * @param cmd
     * @return
     */
    Integer createContact(UserContactCmd cmd);

    /**
     * 修改分销商联系人
     * 
     * @param cmd
     */
    void updateContact(UserContactCmd cmd);

    /**
     * 删除分销商联系人
     * 
     * @param cmd
     */
    void deleteContact(BaseId cmd);

    /**
     * 查询分销商联系人列表
     * 
     * @param qry
     * @return
     */
    PageInfo<ContactDTO> listContact(UserContactListQry qry);

    /**
     * 分销商登录账户修改密码
     * 
     * @param cmd
     */
    void updatePassword(UserPasswordCmd cmd);

    /**
     * 获取分销商信息
     * 
     * @param qry
     * @return
     */
    UserInfoDTO getUserInfo(BaseId qry);

    /**
     * 查询分销商收货地址列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<UserAddressDTO> listAddress(UserAddressListQry qry);

    /**
     * 新增分销商收货地址
     *
     * @param cmd
     * @return
     */
    Integer createAddress(UserAddressCmd cmd);

    /**
     * 修改分销商收货地址
     *
     * @param cmd
     * @return
     */
    void updateAddress(UserAddressCmd cmd);

    /**
     * 设置分销商收货地址为默认地址
     *
     * @param cmd
     * @return
     */
    void defaultAddress(BaseId cmd);

    /**
     * 删除分销商收货地址
     *
     * @param cmd
     * @return
     */
    void deleteAddress(BaseId cmd);

    /**
     * 更新平台显示语言和币种
     * 
     * @param cmd
     */
    void updateLanguage(UserLanguageCmd cmd);

    /**
     * 分销商联系人查询
     * 
     * @param qry
     * @return
     */
    PageInfo<UserContactRoleDTO> listContactRole(UserAddressListQry qry);

    /**
     * 下级分销商前台注册申请
     * 
     * @param cmd
     */
    UserApplyDTO nextLevelApply(UserNextLevelApplyCmd cmd);

    /**
     * 分销商前台手机号获取验证码
     * 
     * @param cmd
     * @return
     */
    boolean getPhoneVerifyCode(UserPhoneVerify cmd);

    /**
     * 分销商前台手机号验证验证码
     * 
     * @param cmd
     * @return
     */
    Boolean checkPhoneVerifyCode(UserPhoneVerifyCodeCmd cmd);

    /**
     * 分销商修改手机号
     * 
     * @param cmd
     */
    void changePhone(UserChangePhoneCmd cmd);

    /**
     * 分销商获取分销二维码
     * 
     * @param qry
     * @return
     */
    UserQrCodeDTO distributionQrCode(UserQrCodeQry qry);

    /**
     * 新增分销商价格等级
     * 
     * @param cmd
     * @return
     */
    Integer createNextScalePrice(UserNextScalePriceCmd cmd);

    /**
     * 更新分销商价格等级
     *
     * @param cmd
     * @return
     */
    Integer updateNextScalePrice(UserNextScalePriceCmd cmd);

    /**
     * 删除价格等级
     * 
     * @param cmd
     */
    void deleteNextScalePrice(BaseId cmd);

    /**
     * 新增价格等级特殊公式
     *
     * @param cmd
     * @return
     */
    void createNextScalePriceSpecial(UserNextScalePriceSpecialCmd cmd);

    /**
     * 修改价格等级特殊公司
     *
     * @param cmd
     * @return
     */
    void updateNextScalePriceSpecial(UserNextScalePriceSpecialCmd cmd);

    /**
     * 删除价格等级特殊公司
     *
     * @param cmd
     */
    void deleteNextScalePriceSpecial(BaseId cmd);

    /**
     * 分销商获取价格等级列表(不分页)
     * 
     * @param qry
     * @return
     */
    List<UserNextScalePriceDTO> listUserNextScalePrice(UserNextScalePriceListQry qry);

    /**
     * 分销商获取价格等级详情
     * 
     * @param qry
     * @return
     */
    UserNextScalePriceDTO getUserNextScalePrice(BaseId qry);

    /**
     * 分销商审核下级分销商
     * 
     * @param cmd
     */
    void checkDistributor(UserCheckCmd cmd);

    /**
     * 调整下级分销商价格等级
     * 
     * @param cmd
     */
    void updateDistributorScalePrice(UserDistributorScalePriceCmd cmd);

    /**
     * 查询下级分销商列表
     * 
     * @param qry
     * @return
     */
    PageInfo<UserNextListDTO> nextList(UserNextListQry qry);

    /**
     * 查看下级分销商详情
     * 
     * @param qry
     * @return
     */
    UserNextDTO getNextDistributor(BaseId qry);

    /**
     * 根据分销商id查询品牌列表
     * 
     * @param qry
     * @return
     */
    List<UserBrandDTO> listBrand(UserId qry);

    /**
     * 根据分销商id查询商品列表
     *
     * @param qry
     * @return
     */
    PageInfo<UserGoodsDTO> listGoods(UserGoodsListQry qry);

    /**
     * 获取下级分销商不可视品牌id列表(不分页)
     * 
     * @param qry
     * @return
     */
    List<Integer> listNoBrand(UserId qry);

    /**
     * 获取下级分销商不可视商品id列表(不分页)
     * 
     * @param qry
     * @return
     */
    List<Integer> listNoGoods(UserId qry);

    /**
     * 设置下级分销商不可视品牌
     * 
     * @param cmd
     */
    void setNoBrand(UserNextNoBrandCmd cmd);

    /**
     * 设置下级分销商不可视商品
     * 
     * @param cmd
     */
    void setNoGoods(UserNextNoGoodsCmd cmd);

    /**
     * 分销商获取支付方式
     * 
     * @param qry
     * @return
     */
    UserPayMentModeDTO paymentMode(String qry);

    /**
     * 根据分销商id查询分销商业务数据
     * 
     * @param distributorId
     * @return
     */
    DistributorBusinessDO getDistributorBusinessDOByDistributorId(Integer distributorId);

    /**
     * 
     * @param distributorId
     * @return
     */
    List<UserWxPlatformListDTO> listWxPlatform(String distributorId, UserWxPlatformListQry qry);

    /**
     * 使用openId和平台编码登录
     * @param openId
     * @return
     */
    UserLoginDTO wxMiniProgramLoginByOpenId(String openId);

    /**
     * 分销微信小程序根据手机号码和验证码登录
     * @param userMobileLoginQry
     * @return
     */
    UserLoginDTO wxMiniProgramLoginByPhoneAndSmsCode(UserMobileLoginQry userMobileLoginQry);

    /**
     * 小程序退出登录、清除联系人openId
     * @param id
     * @return
     */
    void logOut(Integer id);

    /**
     * 根据分销商id查找分销商基本数据
     * @param id
     * @return
     */
    DistributorIdsDTO getBaseMsgByDistributorId(Integer id);

    void updateOpenId(UserOpenIdCmd cmd);

    /**
     * 获取分销商联系人
     * @param qry
     * @return
     */
    ContactDTO getContact(UserContactQry qry);
}
