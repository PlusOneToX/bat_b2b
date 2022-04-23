var util = require('../../../utils/util.js');

Page({
  data: {
    orderId: 0,
    orderInfo: {},
    orderGoods: [],
    handleOption: {}
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.id
    });
    this.getOrderDetail();
  },
  getOrderDetail() {
    let that = this;
    util.request('order/detail', {
      orderId: that.data.orderId
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          orderInfo: res.orderInfo,
          orderGoods: res.orderGoods,
          handleOption: res.orderInfo.handleOption
        });
        that.payTimer();
      }
    });
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
        util.showMsg(res.msg);
      }
    });
  },
  postComment() {
    let that = this;
    wx.navigateTo({
      url: '/pages/commentPost/commentPost?type=0' + '&orderId=' + that.data.orderId,
    })
  },
  lookComment() {
    let that = this;
    wx.navigateTo({
      url: '/pages/comment/comment?type=0' + '&orderId=' + that.data.orderId,
    })
  },
  payTimer() {
    let that = this;
    let orderInfo = that.data.orderInfo;
    if ((orderInfo.payStatus == 1 || orderInfo.payStatus == 2) && orderInfo.orderStatus === 0) {
      util.countdown(that, orderInfo, 'orderInfo')
    }
  },
  goodsDetail(event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  cancelOrder() {
    let that = this;
    let orderInfo = that.data.orderInfo;

    var orderStatus = orderInfo.orderStatus;

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
            orderId: orderInfo.id
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
  payOrder() {
    let that = this;
    wx.navigateTo({
      url: '/pages/pay/pay?orderId=' + that.data.orderInfo.id + '&actualPrice=' + that.data.orderInfo.actualPrice,
    })
  },
  wuliu() {
    let that = this;
    wx.navigateTo({
      url: `/pages/ucenter/wuliu/wuliu?id=${that.data.orderInfo.shippingNo}&code=${that.data.orderInfo.shippingCode}&name=${that.data.orderInfo.shippingName}`,
    })
  },
  goMiniProgram() {
    let that = this;
    wx.navigateToMiniProgram({
      appId: 'wx6885acbedba59c14',
      path: `pages/result/result?nu=${that.data.orderInfo.shippingNo}&querysource=third_xcx`,
      envVersion: 'release',
      success(res) {
      }
    })
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
  confirmOrder() {
    let that = this;
    util.request('order/confirmOrder', {
      orderId: that.data.orderId
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
  }
})
