const util = require('../../../utils/util.js')

//获取应用实例
const app = getApp()

Page({
  data: {
    mallDistEntity: {
      nickname: null,
      superiorNickname: null,
      joinTime: null,
      amountAvailable: 0,
      amountWithdrawn: 0,
      amountTotal: 0,
      headImgUrl: null,
    },
    orderCount: 0,
    teamCount: 0,
  },
  onShow: function () {
    this.initial();
    wx.setStorageSync("navUrl", "/pages/ucenter/index/index");
  },
  initial: function () {
    let that = this;
    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');

    // 页面显示
    if (userInfo && token) {
      app.globalData.userInfo = userInfo;
      app.globalData.token = token;
    }

    util.request('distributor/getDistributorInfo').then(function (res) { 
      that.setData({
        mallDistEntity: res.data.mallDistEntity,
        teamCount: res.data.teamCount,
        orderCount: res.data.orderCount,
      });
    });
  },
  goWithdraw() {
    wx.navigateTo({
      url: '/pages/dcenter/withdraw/withdraw'
    })
  },
  goDetail () {
    wx.navigateTo({
      url: '/pages/dcenter/detail/detail'
    })
  },
  goOrder () {
    wx.navigateTo({
      url: '/pages/dcenter/order/order'
    })
  },
  goTeam () {
    wx.navigateTo({
      url: '/pages/dcenter/team/team'
    })
  },
  goInvitation () {
    wx.navigateTo({
      url: '/pages/dcenter/invitation/invitation'
    })
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;

    that.initial();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
})