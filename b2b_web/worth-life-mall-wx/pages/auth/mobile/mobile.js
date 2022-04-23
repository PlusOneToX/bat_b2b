var util = require('../../../utils/util.js');
var app = getApp()

Page({
  data: {
    title: '绑定手机',
    mobile: '',
    password: '',
    userInfo: {
      headImgUrl: '',
      nickname: ''
    },
    type: 0,
    isSend: false,
    disableGetMobileCode: false,
    disableSubmitMobileCode: true,
    getCodeButtonText: '获取验证码'
  },

  onShow: function () {},

  onLoad: function (options) {
    var that = this
    var type = Number(options.type)
    that.setData({
      type: type
    });
    if (type === 1) {
      util.request('user/userInfo').then(function (res) {
        if (res.code === 0) {
          that.setData({
            userInfo: res.data
          });
          if (res.data.mobile) {
            that.setData({
              mobile: res.data.mobile,
              isSend: true,
              title: '修改密码'
            });
          }
        }
      });
      if (!app.globalData.token) {
        var token = wx.getStorageSync('userToken')
        if (token) {
          app.globalData.token = token
        }
      }
    } else if (type === 2) {
      that.setData({
        title: '重置密码'
      });
    }
  },

  bindGetPassCode: function (e) {
    var that = this
    that.setData({
      disableGetMobileCode: true
    })
  },

  bindInputMobile: function (e) {
    this.setData({
      mobile: e.detail.value,
    })
  },

  bindInputPw: function (e) {
    this.setData({
      password: e.detail.value,
    })
  },

  countDownPassCode: function () {
    let that = this;
    if (!util.isMobile(that.data.mobile)) {
      return
    }
    wx.showLoading({
      title: '发送中...'
    })
    util.request('index/smsCode', {
        phone: that.data.mobile
      }, 'POST', 'application/json')
      .then(function (res) {
        if (res.code == 0) {
          that.setData({
            isSend: true
          })
          util.showMsg(res.msg)
          var pages = getCurrentPages()
          var i = 60;
          var intervalId = setInterval(function () {
            i--
            if (i <= 0) {
              pages[pages.length - 1].setData({
                disableGetMobileCode: false,
                disableSubmitMobileCode: false,
                getCodeButtonText: '获取验证码'
              })
              clearInterval(intervalId)
            } else {
              pages[pages.length - 1].setData({
                getCodeButtonText: i,
                disableGetMobileCode: true,
                disableSubmitMobileCode: false
              })
            }
          }, 1000);
        } else {
          util.showMsg('发送失败');
        }
      });

  },

  bindLoginMobilecode: function (e) {
    var mobile = this.data.mobile;
    var password = this.data.password;
    var that = this;

    if (!util.isMobile(mobile)) {
      return
    }
    if (password.length < 4 || password.length > 8) {
      util.showMsg('请输入4-8位登录密码');
      return
    }
    if (!e.detail.value.code) {
      util.showMsg('请输入验证码');
      return
    }
    if (e.detail.value.code.length != 4) {
      util.showMsg('请输入4位验证码');
      return
    }
    wx.showLoading({
      title: '提交中...'
    })
    var url = that.data.type == 2 ? 'index/modifyPw' : 'index/bindMobile';

    util.request(url, {
        mobileCode: e.detail.value.code,
        mobile: mobile,
        password: password
      }, 'POST')
      .then(function (res) {
        wx.hideLoading();
        if (res.code == 0) {
          wx.showModal({
            title: '提示',
            content: res.msg,
            showCancel: false,
            success: function (res) {
              if (res.confirm) {
                if (that.data.type == 1) {
                  wx.reLaunch({
                    url: '/pages/auth/login/login'
                  });
                } else {
                  wx.reLaunch({
                    url: '/pages/ucenter/index/index'
                  });
                }
              }
            }
          })
        } else {
          util.showMsg(res.msg);
        }
      })
  }
})
