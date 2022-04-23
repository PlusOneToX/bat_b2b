// pages/ucenter/kefu/kefu.js
Page({

  /**
   * 页面的初始数据
   */
  data: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },
  /**
   * 拨打客服电话
   */
  openCall: function() {
    wx.makePhoneCall({
      phoneNumber: '4007166768'
    })
  }
})