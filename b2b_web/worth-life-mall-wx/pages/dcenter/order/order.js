const util = require('../../../utils/util.js')

//获取应用实例
const app = getApp()

Page({

  data: {
    orderType: 'ALL',
    orderNav: [
      {type: 'ALL', name: '全部'},
      {type: 'YFK', name: '已付款'},
      {type: 'YJS', name: '已结算'},
      {type: 'YSX', name: '已失效'},
    ],
    orderList: [],
    current: 1,
    limit: 10,
    pages: 1,
  },
  /**
   * 点击导航栏
   */
  onNavBarTap: function (event) {
    let type = event.currentTarget.dataset.navbarIndex
    if (type == this.data.orderType) {
      return false
    }
    this.setData({
      orderType: type
    })
    this.getOrderList();
  },
  onShow: function () {
    this.initial();
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
    that.getOrderList();
  },
  // 获取订单列表
  getOrderList: function () {
    let that = this;
    util.request('distributor/distOrderList', {
      type: that.data.orderType,
      current: 1,
      limit: that.data.limit * that.data.current,
    }).then(function(res) {
      if (res.code === 0) {
        that.setData({
          orderList: res.data.records,
          pages: res.data.pages,
        });
      }
    });
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;

    that.getOrderList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  onReachBottom() {
    if (this.data.pages > this.data.current) {
      this.setData({
        current: this.data.current + 1
      });
    } else {
      return false;
    }
    this.getOrderList();
  },
})