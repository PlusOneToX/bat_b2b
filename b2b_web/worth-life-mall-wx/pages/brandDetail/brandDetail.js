var util = require('../../utils/util.js');

Page({
  data: {
    id: '',
    brand: {},
    goodsList: [],
    current: 1,
    limit: 100
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    var that = this;
    that.setData({
      id: options.id
    });
    this.getBrand();
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getBrand();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  getBrand: function () {
    let that = this;
    util.request('brand/detail', {
      id: that.data.id
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          brand: res.data
        });

        that.getGoodsList();
      }
    });
  },
  getGoodsList() {
    var that = this;

    util.request('brand/brandGoodsList', {
        brandId: that.data.id,
        current: that.data.current,
        limit: that.data.limit
      })
      .then(function (res) {
        if (res.code === 0) {
          that.setData({
            goodsList: res.data.records
          });
        }
      });
  },
  onReady: function () {
    // 页面渲染完成

  },
  onShow: function () {
    // 页面显示

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  }
})