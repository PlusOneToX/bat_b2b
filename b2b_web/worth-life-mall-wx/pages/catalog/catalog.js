var util = require('../../utils/util.js');
Page({
  data: {
    flist: [],
    categoryList: [],
    currentCategory: {},
    scrollLeft: 0,
    scrollTop: 0,
    goodsCount: 0,
    scrollHeight: 0
  },
  onLoad: function (options) {
    this.getCatalog();
  },
  search: function () {
    wx.navigateTo({
      url: '/pages/search/search'
    })
  },
  getCatalog: function () {
    //CatalogList
    let that = this;
    util.request('category/categoryList', {
      level: 1
    }).then(function (res) {
      that.setData({
        flist: res.data
      });
      that.getCurrentCategory(res.data[0].id)
    });
    util.request('goods/count').then(function (res) {
      that.setData({
        goodsCount: res.data
      });
    });

  },
  getCurrentCategory: function (id) {
    let that = this;
    util.request('category/current', {
        id: id
      })
      .then(function (res) {
        that.setData({
          currentCategory: res.data
        });
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
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getCatalog();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  switchCate: function (event) {
    if (this.data.currentCategory.id == event.currentTarget.dataset.id) {
      return false;
    }

    this.getCurrentCategory(event.currentTarget.dataset.id);
  }
})