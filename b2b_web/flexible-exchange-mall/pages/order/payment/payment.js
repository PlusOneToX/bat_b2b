// pages/order/payment/payment.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderNo: '',
    orderPrice: 0,
    enterFlag: '',
    enterParams: {},
  },

  // 立即支付
  handlePayment() {
    if (this.data.enterParams) {
      let enterParams = this.data.enterParams;

      wx.requestPayment({
        timeStamp: enterParams.timeStamp,
        nonceStr: enterParams.nonceStr,
        package: enterParams.package.replaceAll("123456789", "="),
        signType: enterParams.signType,
        paySign: enterParams.paySign.replaceAll("123456789", "="),
        success(res) {
          wx.redirectTo({
            url: '/pages/order/result/result',
          })
        },
        fail(res) {
          wx.redirectTo({
            url: '/pages/webview/webview?enterFlag=orderList&sid=1',
          })
        }
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      enterFlag: options.enterFlag,
      enterParams: JSON.parse(options.enterParams)
    })

    this.setData({
      orderNo: this.data.enterParams.orderNo,
      orderPrice: this.data.enterParams.orderPrice
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})