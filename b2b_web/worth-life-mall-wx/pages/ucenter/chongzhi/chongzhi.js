var util = require('../../../utils/util.js');
const utils = require('../../../components/utils/utils');
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    nowMoney: 0,
    visible: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    var that = this;
    if (app.globalData.rechargeStatus != 1) {
      util.showMsg('暂未开通');
      that.goIndex();
    } else {
      util.request('user/userInfo').then(function (res) {
        if (res.code === 0) {
          that.setData({
            nowMoney: res.data.balance
          })
        }
      });
    }
  },
  goIndex: function () {
    wx.switchTab({
      url: '/pages/index/index'
    })
  },
  submitSub: function (e) {
    var that = this;
    if (!e.detail.value.number) {
      util.showMsg('请输入充值金额');
      return false;
    }
    if (!util.isNumber(e.detail.value.number)) {
      util.showMsg('请输入正确的充值金额');
      return false;
    }

    util.request('pay/prepayYue', {
      fromType: 1,
      tradeType: 'JSAPI',
      price: e.detail.value.number
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        var jsConfig = res.data;
        wx.requestPayment({
          timeStamp: jsConfig.timeStamp,
          nonceStr: jsConfig.nonceStr,
          package: jsConfig.packageValue,
          signType: jsConfig.signType,
          paySign: jsConfig.paySign,
          success: function (res) {
            util.showMsg('支付成功', true, 1000);
            that.setData({
              nowMoney: parseFloat(that.data.nowMoney) + parseFloat(e.detail.value.number)
            });
            setTimeout(function () {
              wx.navigateTo({
                url: '/pages/ucenter/yue/yue?now=' + that.data.nowMoney,
              })
            }, 1200)
          },
          fail: function (res) {
            util.showMsg('支付失败', true, 1000);
          },
          complete: function (res) {
            if (res.errMsg == 'requestPayment:cancel') {
              util.showMsg('取消支付', true, 1000);
            }
          },
        })
      } else {
        util.showMsg(res.msg, true, 1000);
      }
    })
  }
})
