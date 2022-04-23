var util = require('../../../utils/util.js');

Page({
  data: {},
  onLoad: function (options) {},
  onShareAppMessage: function () {
    return {
      title: '价值人生酒',
      desc: '关于我们',
      path: 'pages/ucenter/about/about'
    }
  },
  onShareTimeline: function () {
    return {
      title: '价值人生酒-关于我们',
      path: 'pages/ucenter/about/about'
    }
  },
  copy: function (e) {
    let text = e.currentTarget.dataset.text;
    wx.setClipboardData({
      data: text,
      success(res) {
        wx.getClipboardData({
          success(res) {
            util.showMsg('链接已复制');
          }
        })
      }
    })
  }
})