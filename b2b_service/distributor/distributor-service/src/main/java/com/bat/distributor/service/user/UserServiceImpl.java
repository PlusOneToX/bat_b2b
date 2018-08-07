package com.bat.distributor.service.user;

// package by domain, not by duty

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.distributor.dto.data.DistributorIdsDTO;
import com.bat.distributor.api.user.UserServiceI;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.dataobject.DistributorBusinessDO;
import com.bat.distributor.service.user.executor.UserCmdExe;
import com.bat.distributor.service.user.executor.UserQryExe;
import com.bat.distributor.service.user.executor.UserRpcCmdExe;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserServiceI {

    @Resource
    private UserQryExe qryExe;

    @Resource
    private UserCmdExe cmdExe;

    @Resource
    private UserRpcCmdExe rpcCmdExe;

    @Override
    public UserLoginDTO userLogin(UserLoginQry qry) {
        return qryExe.userLogin(qry);
    }

    @Override
    public UserLoginDTO phoneLogin(UserPhoneQry qry) {
        return qryExe.phoneLogin(qry);
    }

    @Override
    public UserLoginDTO wxMiniProgramLogin(UserWxMiniProgramLoginCmd cmd) {
        return qryExe.wxMiniProgramLogin(cmd);
    }

    @Override
    public String wxMiniProgramOpenId(UserWxMiniProgramOpenIdCmd cmd) {
        return qryExe.wxMiniProgramOpenId(cmd);
    }

    @Override
    public UserApplyDTO oneLevelApply(UserOneLevelApplyCmd cmd) {
        return cmdExe.oneLevelApply(cmd);
    }

    @Override
    public Integer createContact(UserContactCmd cmd) {
        return cmdExe.createContact(cmd);
    }

    @Override
    public void updateContact(UserContactCmd cmd) {
        cmdExe.updateContact(cmd);
    }

    @Override
    public void deleteContact(BaseId cmd) {
        cmdExe.deleteContact(cmd);
    }

    @Override
    public PageInfo<ContactDTO> listContact(UserContactListQry qry) {
        return qryExe.listContact(qry);
    }

    @Override
    public ContactDTO getContact(UserContactQry qry) {
        return qryExe.getContact(qry);
    }

    @Override
    public void updatePassword(UserPasswordCmd cmd) {
        cmdExe.updatePassword(cmd);
    }

    @Override
    public UserInfoDTO getUserInfo(BaseId qry) {
        return qryExe.getUserInfo(qry);
    }

    @Override
    public PageInfo<UserAddressDTO> listAddress(UserAddressListQry qry) {
        return qryExe.listAddress(qry);
    }

    @Override
    public Integer createAddress(UserAddressCmd cmd) {
        return cmdExe.createAddress(cmd);
    }

    @Override
    public void updateAddress(UserAddressCmd cmd) {
        cmdExe.updateAddress(cmd);
    }

    @Override
    public void defaultAddress(BaseId cmd) {
        cmdExe.defaultAddress(cmd);
    }

    @Override
    public void deleteAddress(BaseId cmd) {
        cmdExe.deleteAddress(cmd);
    }

    @Override
    public void updateLanguage(UserLanguageCmd cmd) {
        cmdExe.updateLanguage(cmd);
    }

    @Override
    public PageInfo<UserContactRoleDTO> listContactRole(UserAddressListQry qry) {
        return qryExe.listContactRole(qry);
    }

    @Override
    public UserApplyDTO nextLevelApply(UserNextLevelApplyCmd cmd) {
        return cmdExe.nextLevelApply(cmd);
    }

    @Override
    public boolean getPhoneVerifyCode(UserPhoneVerify cmd) {
        return rpcCmdExe.getPhoneVerifyCode(cmd);
    }

    @Override
    public Boolean checkPhoneVerifyCode(UserPhoneVerifyCodeCmd cmd) {
        return rpcCmdExe.checkPhoneVerifyCode(cmd);
    }

    @Override
    public void changePhone(UserChangePhoneCmd cmd) {
        cmdExe.changePhone(cmd);
    }

    @Override
    public UserQrCodeDTO distributionQrCode(UserQrCodeQry qry) {
        String qrCodeUrl = qryExe.getDistributionQrCode(qry);
        if (StringUtils.isBlank(qrCodeUrl)) {
            qrCodeUrl = rpcCmdExe.getDistributionQrCode(qry);
            cmdExe.saveDistributionQrCode(qrCodeUrl, qry.getDistributorId());
        }
        UserQrCodeDTO dto = new UserQrCodeDTO();
        dto.setQrCodeUrl(qrCodeUrl);
        return dto;
    }

    @Override
    public Integer createNextScalePrice(UserNextScalePriceCmd cmd) {
        return cmdExe.createNextScalePrice(cmd);
    }

    @Override
    public Integer updateNextScalePrice(UserNextScalePriceCmd cmd) {
        return cmdExe.updateNextScalePrice(cmd);
    }

    @Override
    public void deleteNextScalePrice(BaseId cmd) {
        cmdExe.deleteNextScalePrice(cmd);
    }

    @Override
    public void createNextScalePriceSpecial(UserNextScalePriceSpecialCmd cmd) {
        cmdExe.createNextScalePriceSpecial(cmd);
    }

    @Override
    public void updateNextScalePriceSpecial(UserNextScalePriceSpecialCmd cmd) {
        cmdExe.updateNextScalePriceSpecial(cmd);
    }

    @Override
    public void deleteNextScalePriceSpecial(BaseId cmd) {
        cmdExe.deleteNextScalePriceSpecial(cmd);
    }

    @Override
    public List<UserNextScalePriceDTO> listUserNextScalePrice(UserNextScalePriceListQry qry) {
        return qryExe.listUserNextScalePrice(qry);
    }

    @Override
    public UserNextScalePriceDTO getUserNextScalePrice(BaseId qry) {
        return qryExe.getUserNextScalePrice(qry);
    }

    @Override
    public void checkDistributor(UserCheckCmd cmd) {
        cmdExe.checkDistributor(cmd);
    }

    @Override
    public void updateDistributorScalePrice(UserDistributorScalePriceCmd cmd) {
        cmdExe.updateDistributorScalePrice(cmd);
    }

    @Override
    public PageInfo<UserNextListDTO> nextList(UserNextListQry qry) {
        return qryExe.nextList(qry);
    }

    @Override
    public UserNextDTO getNextDistributor(BaseId qry) {
        return qryExe.getNextDistributor(qry);
    }

    @Override
    public List<UserBrandDTO> listBrand(UserId qry) {
        return qryExe.listBrand(qry);
    }

    @Override
    public PageInfo<UserGoodsDTO> listGoods(UserGoodsListQry qry) {
        return qryExe.listGoods(qry);
    }

    @Override
    public List<Integer> listNoBrand(UserId qry) {
        return qryExe.listNoBrand(qry);
    }

    @Override
    public List<Integer> listNoGoods(UserId qry) {
        return qryExe.listNoGoods(qry);
    }

    @Override
    public void setNoBrand(UserNextNoBrandCmd cmd) {
        cmdExe.setNoBrand(cmd);
    }

    @Override
    public void setNoGoods(UserNextNoGoodsCmd cmd) {
        cmdExe.setNoGoods(cmd);
    }

    @Override
    public UserPayMentModeDTO paymentMode(String qry) {
        return qryExe.paymentMode(qry);
    }

    @Override
    public DistributorBusinessDO getDistributorBusinessDOByDistributorId(Integer distributorId) {
        return qryExe.getDistributorBusinessDOByDistributorId(distributorId);
    }

    @Override
    public List<UserWxPlatformListDTO> listWxPlatform(String userId, UserWxPlatformListQry qry) {
        return qryExe.listWxPlatform(userId, qry);
    }

    /**
     * 使用openId登录
     * @param openId
     * @return
     */
    @Override
    public UserLoginDTO wxMiniProgramLoginByOpenId(String openId) {
        return qryExe.wxMiniProgramLoginByOpenId(openId);
    }

    /**
     * 分销微信小程序根据手机号码和验证码登录
     * @param userMobileLoginQry
     * @return
     */
    @Override
    public UserLoginDTO wxMiniProgramLoginByPhoneAndSmsCode(UserMobileLoginQry userMobileLoginQry) {
        return qryExe.wxMiniProgramLoginByPhoneAndSmsCode(userMobileLoginQry);
    }

    /**
     * 小程序退出登录、清除联系人openId
     * @param id
     * @return
     */
    @Override
    public void logOut(Integer id) {
         cmdExe.logOut(id);
    }

    /**
     * 根据分销商id查找分销商基本数据
     * @param id
     * @return
     */
    @Override
    public DistributorIdsDTO getBaseMsgByDistributorId(Integer id) {
        return qryExe.getBaseMsgByDistributorId(id);
    }

    @Override
    public void updateOpenId(UserOpenIdCmd cmd) {
        cmdExe.updateOpenId(cmd);
    }
}