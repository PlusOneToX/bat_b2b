// import wx from 'weixin-js-sdk'

/* export function weixinPay (appId, timestamp, nonceStr, packages, signType, paySign) {
  // let appId = data.appId
  // let timestamp = data.timeStamp
  // let nonceStr = data.nonceStr
  // let signature = data.signature
  // let packages = data.package
  // let paySign = data.paySign
  // let signType = data.signType
   wx.config({
    debug: false,
    appId: appId, // 公众号唯一标识
    timestamp: timestamp, // 生成签名的时间戳
    nonceStr: nonceStr, // 生成签名的随机串
    // signature: signature, // 签名
    jsApiList: ['chooseWXPay']
  })
  wx.ready(function () {
    wx.chooseWXPay({
      timestamp: timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
      nonceStr: nonceStr, // 支付签名随机串，不长于 32 位
      package: packages, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
      signType: signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
      paySign: paySign, // 支付签名
      success: function (res) {
        // 支付成功后的回调函数
        // cb(res)
      },
      cancel: function () {
        // 订单取消
        this.$toast('订单取消成功')
        setTimeout(() => {
          location.reload()
        }, 1500)
      },
      fail: function (res) {
        // 失败回调函数
        // errorCb(res)
        this.$toast('订单支付失败')
        location.reload()
      }
    })
  })
  wx.error(function (error) {
    console.log(error)
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
  })
  // wx.chooseWXPay({
  //   timestamp: data.timeStamp,
  //   nonceStr: data.nonceStr,
  //   package: data.package,
  //   signType: data.signType,
  //   paySign: data.paySign,
  //   success: function (res) {
  //     // 支付成功
  //   },
  //   cancel: function (res) {
  //     // 订单取消
  //     this.$toast('订单取消成功')
  //     // 取消订单
  //     setTimeout(() => {
  //       location.reload()
  //     }, 1500)
  //   },
  //   fail: function (res) {
  //     // 支付失败
  //     this.$toast('订单支付失败')
  //     location.reload()
  //   }
  // })
} */

export function aliPay (data) {
  const div = document.createElement('div')
  div.innerHTML = data
  document.body.appendChild(div)
  document.forms[0].submit()
  // AlipayJSBridge.call('tradePay', {
  //   orderStr: data.responseStr
  // }, function (result) {
  //   if (result.resultCode === '9000') {
  //     // 支付成功
  //   } else if (result.resultCode === '6001') {
  //     // 取消订单
  //     this.$toast('订单取消成功')
  //
  //   } else {
  //     // 支付失败
  //     this.$toast('订单支付失败')
  //     location.reload()
  //   }
  // })
}
