/*
 * @Author: yaowei
 * @Date: 2019-09-18 14:26:28
 * @LastEditors: xuyiyang
 * @LastEditTime: 2019-07-04 8:30:20
 */
Page({
  data: {
    orderNo: '',
    orderPrice: 0,
    enterFlag: '',
    enterParams: {},
    isChecked: 2, // 支付方式：2 微信，1 支付宝

    showFlexibl: false
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

    console.log("点击立即支付");

    if (this.data.enterParams) {
      let enterParams = this.data.enterParams;

      my.tradePay({
        tradeNO: enterParams.tradeNo,
        success(res) {
          if (res.resultCode === '9000') {
            // 支付成功处理逻辑，支付成功
            my.redirectTo({
              url: '/pages/order/result/result',
            })
          } else {
            my.redirectTo({
              url: '/pages/webview/webview?enterFlag=orderList&sid=1',
            })
          }
        },
        fail(res) {
          my.redirectTo({
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
        orderPrice: this.data.enterParams.orderPrice
      })
    }
  },
  
  //关闭柔性弹窗
  clickFlexible: function(){
    this.setData({
      showFlexible: false
    });
  }
})