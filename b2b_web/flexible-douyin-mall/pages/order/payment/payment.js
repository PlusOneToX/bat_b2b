/*
 * @Author: yaowei
 * @Date: 2019-09-18 14:26:28
 * @LastEditors: yaowei
 * @LastEditTime: 2019-09-24 10:59:20
 */
Page({
  data: {
    orderNo: '',
    orderPrice: 0,
    enterFlag: '',
    enterParams: {},
    isChecked: 2, // 支付方式：2 微信，1 支付宝
  },

  // 支付方式
  changePayment(e) {
    let type = e.currentTarget.dataset.type;
    this.setData({
      isChecked: type
    })
  },

  // 立即支付
  handlePayment() {
    if (this.data.enterParams) {
      let enterParams = this.data.enterParams;

      tt.pay({
        service: 5,
        orderInfo: {
          order_id: enterParams.orderId,
          order_token: enterParams.orderToken.replaceAll("123456789", "="),
        },
        success(res) {
          if (res.code === 0) {
            // 支付成功处理逻辑，只有res.code=0时，才表示支付成功
            wx.redirectTo({
              url: '/pages/order/result/result',
            })
          } else {
            wx.redirectTo({
              url: '/pages/webview/webview?enterFlag=orderList&sid=1',
            })
          }
        },
        fail(res) {
          wx.redirectTo({
            url: '/pages/webview/webview?enterFlag=orderList&sid=1',
          })
        },
      });
    }
  },
  
  onLoad: function (options) {
    if (options.enterFlag) {
      this.setData({
        enterFlag: options.enterFlag,
        enterParams: JSON.parse(options.enterParams)
      })
  
      this.setData({
        orderNo: this.data.enterParams.orderNo,
        orderPrice: this.data.enterParams.orderPrice
      })
    }
  }
})