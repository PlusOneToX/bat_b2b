/**
 * 支付相关服务
 */
const util = require('../utils/util.js');

/**
 * 统一下单请求
 */
function payOrder(orderId) {
  return new Promise(function (resolve, reject) {
    util.request('pay/prepay', {
      orderId: orderId,
      tradeType: 'JSAPI'
    }, 'POST').then((res) => {
      if (res.code === 0) {
        const payParam = res.data;
        wx.requestPayment({
          'timeStamp': payParam.timeStamp,
          'nonceStr': payParam.nonceStr,
          'package': payParam.packageValue,
          'signType': payParam.signType,
          'paySign': payParam.paySign,
          'success': function (res) {
            resolve(res);
          },
          'fail': function (res) {
            reject(res);
          },
          'complete': function (res) {
            reject(res);
          }
        });
      } else {
        reject(res);
      }
    });
  });
}


module.exports = {
  payOrder,
};