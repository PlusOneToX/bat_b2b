var util = require('../../../utils/util.js');
var app = getApp();

Page({
  data: {
    shippingNo: '',
    shippingName: '',
    shippingCode: '',
    logistics: []
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      shippingNo: options.id,
      shippingCode: options.code,
      shippingName: options.name
    });
  },
  onShow: function () {
    var that = this;

    //使用插件调用物流轨迹
    var logisticsPlugin = requirePlugin("wt-logistics");
    logisticsPlugin.reglogis(that.data.shippingNo, that.data.shippingCode, app.globalData.kdnBusinessId, app.globalData.kdnAppKey).then(function (response) {
      if (response.Success) {
        that.setData({
          logistics: response.Traces
        });
      }
    })
  }
})
