var util = require('../../utils/util.js');

Page({
  data: {
    brandList: [],
    current: 1,
    limit: 10,
    pages: 1
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.getBrandList();
  },
  getBrandList: function() {
    let that = this;
    util.request('brand/brandList', {
      current: 1,
      limit: that.data.limit * that.data.current
    }).then(function(res) {
      if (res.code === 0) {
        that.setData({
          brandList: res.data.records,
          pages: res.data.pages
        });
      }
    });
  },
  onReachBottom() {
    if (this.data.pages > this.data.current) {
      this.setData({
        current: this.data.current + 1
      });
    } else {
      return false;
    }

    this.getBrandList();
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示

  },
  onHide: function() {
    // 页面隐藏

  },
  // 下拉刷新
  onPullDownRefresh: function() {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;
    that.setData({
      current: 1
    });
    this.getBrandList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  onUnload: function() {
    // 页面关闭

  }
})
