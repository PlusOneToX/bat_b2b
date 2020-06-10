Page({
  data: {

  },

  // 返回首页
  goHome() {
    wx.switchTab({
      url: '/pages/index/index',
    })
  },

  // 查看订单
  goPath(e) {
    // 获取跳转path
    let path = e.currentTarget.dataset.path;
    // 订阅消息
    // tt.requestSubscribeMessage
    wx.redirectTo({
      url: '/pages/webview/webview?enterFlag=' + path + '&sid=2',
    })
  },

  onLoad: function (options) {

  }
})