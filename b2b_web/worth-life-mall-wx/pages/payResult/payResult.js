const pay = require('../../services/pay.js');
const util = require('../../utils/util.js');

Page({
  data: {
    status: false,
    orderId: 0,
    errmsg: ''
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.orderId,
      status: options.status,
      errmsg: options.msg
    })
  },
  onReady: function () {

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
  go(e) {
    let page = e.currentTarget.dataset.page
    if (page == 1) {
      wx.switchTab({
        url: "/pages/index/index"
      })
    } else {
      wx.switchTab({
        url: '/pages/ucenter/index/index'
      })
    }
  },
  payOrder() {
    let that = this;
    pay.payOrder(this.data.orderId).then(res => {
      that.setData({
        status: true
      });
    }).catch(res => {
      util.showMsg('付款失败！');
      that.setData({
        errmsg: res.errMsg
      });
    });
  }
})
