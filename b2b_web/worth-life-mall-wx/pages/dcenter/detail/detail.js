const util = require('../../../utils/util.js')

//获取应用实例
const app = getApp()

Page({
  data: {
    filter: 'ALL',
    monthIncome: '0.00',
    dayIncome: '0.00',
    incomeList: [],
    current: 1,
    limit: 20,
    pages: 1,
  },
  switchFilter (e) {
    const filter = e.currentTarget.dataset.index
    this.setData({filter})
    this.initial()
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

    util.request('distributor/getIncomeDetails').then(function (res) { 
      that.setData({
        monthIncome: res.data.monthIncome,
        dayIncome: res.data.dayIncome,
      });
    });
    that.getIncomeList();
  },
  // 获取收益列表
  getIncomeList: function () {
    let that = this;
    util.request('distributor/incomeList', {
      type: that.data.filter,
      current: 1,
      limit: that.data.limit * that.data.current,
    }).then(function(res) {
      if (res.code === 0) {
        that.setData({
          incomeList: res.data.records,
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

    that.initial();
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
    this.getIncomeList();
  },
})
