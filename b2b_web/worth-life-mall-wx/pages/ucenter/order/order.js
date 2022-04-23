var util = require('../../../utils/util.js');

Page({
  data: {
    orderStatus: -2,
    evaluate_status: '',
    orderList: [],
    current: 1,
    limit: 10,
    pullUpOn: false,
    tabs: [{
      name: "全部"
    }, {
      name: "待付款"
    }, {
      name: "待发货"
    }, {
      name: "待收货"
    }, {
      name: "待评价"
    }],
    currentTab: 0
  },
  change(e) {
    this.setData({
      currentTab: e.detail.index,
      orderList: [],
      current: 1,
    });
    this.switchOrderType(e.detail.index);
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options.currentTab) {
      this.setData({
        currentTab: options.currentTab
      });
    }
  },
  orderDetail(event) {
    wx.navigateTo({
      url: '/pages/ucenter/orderDetail/orderDetail?id=' + event.currentTarget.dataset.id,
    })
  },

  cancelOrder(event) {
    let that = this;

    var orderStatus = event.currentTarget.dataset.orderStatus;

    var errorMessage = '';
    switch (orderStatus) {
      case 300: {
        errorMessage = '订单已发货';
        break;
      }
      case 301: {
        errorMessage = '订单已收货';
        break;
      }
      case 100: {
        errorMessage = '订单已过期';
        break;
      }
      case 101: {
        errorMessage = '订单已取消';
        break;
      }
      case 102: {
        errorMessage = '订单已删除';
        break;
      }
      case 401: {
        errorMessage = '订单已退款';
        break;
      }
      case 402: {
        errorMessage = '订单已退货';
        break;
      }
    }
    if (errorMessage != '') {
      util.showMsg(errorMessage);
      return false;
    }
    wx.showModal({
      title: '',
      content: '确定要取消此订单？',
      success: function (res) {
        if (res.confirm) {
          util.request('order/cancelOrder', {
            orderId: event.currentTarget.dataset.id
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              wx.showModal({
                title: '提示',
                content: res.msg,
                showCancel: false,
                confirmText: '继续',
                success: function (res) {
                  wx.switchTab({
                    url: '/pages/ucenter/index/index',
                  });
                }
              });
            }
          });
        }
      }
    });
  },
  goodsDetail(event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
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
      limit: that.data.limit
    }).then(function (res) {
      if (res.code === 0) {
        let orderList = res.data.records;
        that.setData({
          orderList: orderList
        });
        //fixme 有问题，暂时注释获取待付款倒计时
        // that.data.orderList.forEach((item, num) => {
        //   if ((item.payStatus == 1 || item.payStatus == 2) && item.orderStatus === 0) {
        //     util.countdown(that, that.data.orderList, 'orderList', num)
        //   }
        // })
      }
    });
  },
  payOrder(event) {
    wx.navigateTo({
      url: '/pages/pay/pay?orderId=' + event.target.dataset.orderId + '&actualPrice=' + event.target.dataset.actualPrice
    })
  },
  againBuy(event) {
    let orderId = event.target.dataset.orderId;
    util.request('cart/addByOrder', {
      orderId: orderId
    }).then(function (res) {
      if (res.code === 0) {
        wx.switchTab({
          url: '/pages/cart/cart',
        });
      } else if (res.code === 300) {
        wx.navigateTo({
          url: '/pages/cartShops/cartShops?shopsId=' + res.msg,
        });
      } else {
        util.showMsg(res.msg)
      }
    });
  },
  confirmOrder(event) {
    util.request('order/confirmOrder', {
      orderId: event.target.dataset.orderId
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        util.showMsg('订单完成')
        setTimeout(function () {
          wx.redirectTo({
            url: '/pages/ucenter/order/order',
          });
        }, 2000);
      }
    });
  },
  switchOrderType(currentTab) {
    let that = this;
    if (currentTab == 0) {
      that.setData({
        orderStatus: -2
      });
    } else if (currentTab == 1) {
      that.setData({
        orderStatus: 0
      });
    } else if (currentTab == 2) {
      that.setData({
        orderStatus: 201
      });
    } else if (currentTab == 3) {
      that.setData({
        orderStatus: 300
      });
    } else if (currentTab == 4) {
      that.setData({
        orderStatus: 301
      });
    }
    that.getOrderList();
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    let that = this;
    that.switchOrderType(that.data.currentTab);
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