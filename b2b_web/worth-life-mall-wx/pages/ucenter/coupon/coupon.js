var util = require('../../../utils/util.js');

Page({
  data: {
    couponList: []
  },
  onLoad: function (options) {
    this.loadListData()
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;

    that.loadListData();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  goIndex() {
    wx.switchTab({
      url: '/pages/cart/cart',
    })
  },
  couponCenter() {
    wx.navigateTo({
      url: '/pages/shopping/couponCenter/couponCenter',
    })
  },
  loadListData: function () {
    let that = this;

    util.request('coupon/list').then(function (res) {
      if (res.code === 0) {
        that.setData({
          couponList: res.data
        });
      }
    });
  },

  onReady: function () {

  },
  onShow: function () {

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  }
})