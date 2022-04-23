var util = require('../../../utils/util.js');

Page({
  data: {
    footprintList: [],
    actions: [{
      name: '删除',
      color: '#fff',
      fontsize: '22',
      width: 80,
      //icon: 'like.png',//此处为图片地址
      background: '#ed3f14'
    }]
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  getFootprintList() {
    let that = this;
    var tmpFootPrint;
    util.request('user/footprintList').then(function (res) {
      if (res.code === 0) {

        if (res.data != undefined) {
          tmpFootPrint = res.data.records;
        } else {
          tmpFootPrint = [];
        }
        that.setData({
          footprintList: tmpFootPrint
        });
      }
    });
  },
  deleteItem(event) {
    let that = this;
    let id = event.currentTarget.dataset.id;
    wx.showModal({
      title: '',
      content: '要删除所选足迹？',
      success: function (res) {
        if (res.confirm) {
          util.request('user/deleteFootPrint', {
            footprintId: id
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              that.getFootprintList();
            } else {
              util.showMsg(res.msg);
            }
          });
        }
      }
    });
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getFootprintList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  onLoad: function (options) {
    this.getFootprintList();
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