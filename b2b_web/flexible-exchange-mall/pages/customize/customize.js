/*
 * @Author: yaowei
 * @Date: 2019-07-21 09:16:54
 * @LastEditors: yaowei
 * @LastEditTime: 2019-08-03 10:21:24
 */
// pages/customize/customize.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  // 立即定制
  goCustom() {
    let enterParams = JSON.stringify({
      materialId: wx.getStorageSync('materialId'),
      modelId: wx.getStorageSync('modelId'),
      modelName: encodeURIComponent(encodeURIComponent(wx.getStorageSync('modelName'))),
    });

    wx.navigateTo({
      url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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