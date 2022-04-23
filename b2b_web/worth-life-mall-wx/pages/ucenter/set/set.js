var util = require('../../../utils/util.js');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    bindPhoneText: '绑定手机'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this;
    util.request('user/userInfo').then(function (res) {
      if (res.data.mobile) {
        that.setData({
          bindPhoneText: '修改密码'
        });
      }
      that.setData({
        userInfo: res.data
      });
    });
  },
  bindPhone: function () {
    wx.navigateTo({
      url: '/pages/auth/mobile/mobile?type=' + 1,
    })
  },
  openSetting: function () {
    wx.openSetting()
  },
  aboutUs: function () {
    wx.navigateTo({
      url: '/pages/ucenter/about/about',
    })
  },
  exit() {
    app.globalData.userInfo = {
      nickname: 'Hi,游客,点击头像登录',
      headImgUrl: '/static/images/150547696d798c.png'
    }
    wx.clearStorage({
      success() {
        util.showMsg('退出成功');
        setTimeout(function () {
          wx.reLaunch({
            url: '/pages/ucenter/index/index',
          })
        }, 1000)
      }
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})
