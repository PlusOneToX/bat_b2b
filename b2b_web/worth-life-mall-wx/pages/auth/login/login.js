const util = require('../../../utils/util.js');

Page({
  data: {
    mobile: '',
    password: ''
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    // 页面渲染完成

  },
  onReady: function () {

  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  },
  startLogin: function () {
    let that = this;
    if (!util.isMobile(that.data.mobile)) {
      return
    }

    if (that.data.password.length < 1) {
      util.showMsg('请输入密码');
      return false;
    }
    util.request('auth/loginByMobile', {
      mobile: that.data.mobile,
      password: that.data.password
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        //存储用户信息
        wx.setStorageSync('userInfo', res.userInfo);
        wx.setStorageSync('token', res.token);
        wx.setStorageSync('userId', res.user.id);
        wx.switchTab({
          url: '/pages/ucenter/index/index'
        });
      } else {
        util.showMsg(res.msg);
      }
    });
  },
  bindUsernameInput: function (e) {
    this.setData({
      mobile: e.detail.value
    });
  },
  bindPasswordInput: function (e) {
    this.setData({
      password: e.detail.value
    });
  },
  clearInput: function (e) {
    switch (e.currentTarget.id) {
      case 'clear-mobile':
        this.setData({
          mobile: ''
        });
        break;
      case 'clear-password':
        this.setData({
          password: ''
        });
        break;
    }
  }
})
