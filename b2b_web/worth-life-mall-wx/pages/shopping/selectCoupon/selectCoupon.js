var util = require('../../../utils/util.js');

Page({
  data: {
    couponList: [],
    checkedGoodsIdList: '',
    checkedBrandIdList: ''
  },
  onLoad: function (options) {
    this.setData({
      checkedGoodsIdList: options.checkedGoodsIdList,
      checkedBrandIdList: options.checkedBrandIdList
    })
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
  loadListData: function () {
    let that = this;
    util.request('coupon/list', {
      status: 0,
      goodsIdList: that.data.checkedGoodsIdList,
      brandIdList: that.data.checkedBrandIdList
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          couponList: res.data
        });
      }
    });
  },
  selectCoupon: function (e) {
    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1]; //当前页面
    var prevPage = pages[pages.length - 2]; //上一个页面

    //直接调用上一个页面的setData()方法，把数据存到上一个页面中去
    prevPage.setData({
      couponId: e.currentTarget.dataset.id
    })
    wx.navigateBack({
      fail() {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    })
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
