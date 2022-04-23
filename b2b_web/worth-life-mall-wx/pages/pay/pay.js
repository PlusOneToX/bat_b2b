var util = require('../../utils/util.js');
const pay = require('../../services/pay.js');
const app = getApp()

Page({
  data: {
    cartArr: [{
      "name": "微信支付",
      "img": "wxpay.png",
      value: 'weixin'
    }],
    payType: 'weixin',
    orderId: 0,
    actualPrice: 0.00
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.orderId,
      actualPrice: options.actualPrice
    })
    if (app.globalData.rechargeStatus == 1) {
      this.setData({
        cartArr: [{
          "name": "微信支付",
          "img": "wxpay.png",
          value: 'weixin'
        },
          {
            "name": "余额支付",
            "img": "yue.png",
            value: 'yue'
          }
        ]
      });
    }
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  radioChange: function(e) {
    this.setData({
      payType: e.currentTarget.dataset.value
    })
  },
  //向服务请求支付参数
  requestPayParam() {
    let that = this;
    if (that.data.payType == 'weixin') {
      pay.payOrder(that.data.orderId).then(res => {
        wx.redirectTo({
          url: '/pages/payResult/payResult?status=1&orderId=' + that.data.orderId
        });
      }).catch(res => {
        wx.redirectTo({
          url: '/pages/payResult/payResult?status=0&orderId=' + that.data.orderId + '&msg=' + res.errMsg
        });
      });
    }
    if (that.data.payType == 'yue') {
      util.request('pay/buyByYue', {
        fromType: 1,
        orderId: that.data.orderId
      }, 'POST').then((res) => {
        if (res.code === 0) {
          util.showMsg('支付成功', true, 1000);
          setTimeout(function() {
            wx.switchTab({
              url: '/pages/ucenter/index/index'
            })
          }, 1000)
        } else {
          if (res.code === 300) {
            wx.showModal({
              title: res.msg,
              content: '是否充值？',
              success(res) {
                if (res.confirm) {
                  wx.navigateTo({
                    url: '/pages/ucenter/chongzhi/chongzhi'
                  })
                } else if (res.cancel) {
                  wx.switchTab({
                    url: '/pages/ucenter/index/index'
                  })
                }
              }
            })
          } else {
            util.showMsg(res.msg);
          }
        }
      });
    }
  },
  startPay() {
    this.requestPayParam();
  }
})
