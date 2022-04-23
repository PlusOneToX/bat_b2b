var util = require('../../../utils/util.js');

Page({
  data: {
    orderStatus: -2,
    evaluate_status: '',
    orderList: [],
    current: 1,
    limit: 10
  },
  onLoad: function (options) {},
  postComment(event) {
    let that = this;
    wx.navigateTo({
      url: '/pages/commentPost/commentPost?type=0' + '&orderId=' + event.target.dataset.orderId,
    })
  },
  lookComment(event) {
    wx.navigateTo({
      url: '/pages/comment/comment?type=0' + '&orderId=' + event.target.dataset.orderId,
    })
  },
  orderDetail(event) {
    wx.navigateTo({
      url: '/pages/ucenter/skillDetail/skillDetail?id=' + event.currentTarget.dataset.id,
    })
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getOrderList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  getOrderList() {
    let that = this;
    util.request('order/list', {
      orderStatus: that.data.orderStatus,
      current: that.data.current,
      limit: that.data.limit,
      orderType: 3
    }).then(function (res) {
      if (res.code === 0) {
        let orderList = res.data.records;
        that.setData({
          orderList: orderList
        });
        //获取待付款倒计时
        that.data.orderList.forEach((item, num) => {
          if ((item.payStatus == 1 || item.payStatus == 2) && item.orderStatus === -1) {
            util.countdown(that, that.data.orderList, 'orderList', num)
          }
        })
      }
    });
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面初始化 options为页面跳转所带来的参数
    this.getOrderList()
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  onReachBottom() {
    var that = this;
    that.setData({
      limit: that.data.limit + 10,
    });
    that.getOrderList();
  }
})