// component/authorize/authorize.js

import httpRequest from '../../utils/request'
import api from '../../utils/allApi'

let app = getApp();

Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    showWxAuthButton: true,
    userInfo:{},
  },

  /**
   * 组件的方法列表
   */
  methods: {

    getUserProfile(e) {
      // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
      // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
      wx.getUserProfile({
        desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
        success: (res) => {
          this.setData({
            userInfo: res.userInfo,
            showWxAuthButton: false
          })

          wx.login({
            success: (res1) => {
              wx.setStorageSync('code', res1.code);
            }
          });
        }
      })
    },

    getPhoneNumber(e) {

      //手机号授权失败则不处理
      if (e.detail.errMsg !== "getPhoneNumber:ok") {
        return
      }
      
      let accountInfo = wx.getAccountInfoSync();
      let appId = accountInfo.miniProgram.appId;
      let code = wx.getStorageSync('code');
      httpRequest
      .post(api.authLogin,{
        'code': code,
        'encryptedData': e.detail.encryptedData,
        'iv': e.detail.iv,
        'avatarUrl': this.data.userInfo.avatarUrl,
        'gender': this.data.userInfo.gender,
        'language': this.data.userInfo.language,
        'nickName': this.data.userInfo.nickName,
        'appId': appId
      })
      .then((res) => {   
        if (res.data.success) {
          wx.setStorageSync('phone', res.data.phone); 
          wx.setStorageSync('userId', res.data.id); 
          wx.setStorageSync('userNo', res.data.no); 
          wx.setStorageSync('openid', res.data.openId);
          this.triggerEvent("authSuccess",{});
        }
      });

    },

    unAuthButtonClick() {

      wx.switchTab({
        url: '/pages/home/home'
      })
    }

  },

  /**
   * 组件生命周期函数--在组件实例进入页面节点树时执行
   */
  attached: function () {

  }
})
