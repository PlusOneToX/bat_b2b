var util = require('../../utils/util.js');

Page({
  data: {
    bannerInfo: {
      'img_url': '',
      'name': ''
    },
    categoryFilter: false,
    filterCategory: [],
    goodsList: [],
    categoryId: '',
    currentSortType: '',
    currentSortOrder: 'desc',
    current: 1,
    limit: 1000
  },
  getData: function () {
    let that = this;
    util.request('goods/newBanner').then(function (res) {
      if (res.code === 0) {
        that.setData({
          bannerInfo: res.data,
        });
        that.getGoodsList();
      }
    });
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  getGoodsList() {
    var that = this;
    util.request('goods/list', {
        type: 'IS_NEW',
        current: that.data.current,
        limit: that.data.limit,
        sortType: that.data.currentSortOrder,
        order: that.data.currentSortType,
        categoryId: that.data.categoryId
      })
      .then(function (res) {
        if (res.code === 0) {
          that.setData({
            goodsList: res.data.records,
            filterCategory: res.filterCategory
          });
        }
      });
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getData();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.getData();
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
  openSortFilter: function (event) {
    let currentId = event.currentTarget.id;
    switch (currentId) {
      case 'categoryFilter':
        this.setData({
          'categoryFilter': !this.data.categoryFilter,
          'currentSortType': 'category_id',
          'currentSortOrder': 'asc'
        });
        break;
      case 'priceSort':
        let tmpSortOrder = 'asc';
        if (this.data.currentSortOrder == 'asc') {
          tmpSortOrder = 'desc';
        }
        this.setData({
          'currentSortType': 'RETAIL_PRICE',
          'currentSortOrder': tmpSortOrder,
          'categoryFilter': false
        });

        this.getData();
        break;
      default:
        //综合排序
        this.setData({
          'currentSortType': '',
          'currentSortOrder': 'desc',
          'categoryFilter': false
        });
        this.getData();
    }
  },
  selectCategory: function (event) {
    let currentIndex = event.target.dataset.categoryIndex;
    this.setData({
      'categoryFilter': false,
      'categoryId': this.data.filterCategory[currentIndex].id
    });
    this.getData();

  }
})