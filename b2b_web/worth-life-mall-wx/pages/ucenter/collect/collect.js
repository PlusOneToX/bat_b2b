var util = require('../../../utils/util.js');

Page({
  data: {
    collectList: [],
    actions: [{
      name: '删除',
      color: '#fff',
      fontsize: '22',
      width: 80,
      //icon: 'like.png',//此处为图片地址
      background: '#ed3f14'
    }]
  },
  getCollectList() {
    let that = this;
    util.request('user/collectList').then(function (res) {
      if (res.code === 0) {
        that.setData({
          collectList: res.data
        });
      }
    });
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  onLoad: function (options) {

  },
  onReady: function () {

  },
  onShow: function () {
    this.getCollectList();
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getCollectList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  },
  deleteItem(event) {
    let that = this;
    let id = event.currentTarget.dataset.id;
    wx.showModal({
      title: '',
      content: '确定删除收藏吗？',
      success: function (res) {
        if (res.confirm) {
          util.request('user/addOrDelete', {
            goodsId: id
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              that.getCollectList();
            }
          });
        }
      }
    });
  }
})