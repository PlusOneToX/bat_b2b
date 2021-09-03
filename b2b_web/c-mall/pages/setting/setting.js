// pages/setting/setting.js

let app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  logout() {

    wx.removeStorageSync('token');
    wx.removeStorageSync('phone');
    wx.removeStorageSync('userId');
    wx.removeStorageSync('userNo');
    wx.removeStorageSync('openid');

    wx.switchTab({
      url: '/pages/home/home'
    })

  }
})