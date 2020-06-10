// pages/code/bindCode/bindCode.js
// Toast
import Toast from '../../../miniprogram_npm/@vant/weapp/toast/toast';
// Api
import $request from '../../../assets/api/request'
import api from '../../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    secretCode: '', // 兑换码
    userId: '', // 用户id
    enterFlag: '', // 进入页面来源
  },

  // 监听输入框
  bindInput(e) {
    let secretCode = e.detail.value
      .toUpperCase()
      .replace(/(^\s*)|(\s*$)/g, "");

    this.setData({
      secretCode: secretCode
    })
  },

  // 清空输入框
  clearInput() {
    this.setData({
      secretCode: ''
    })
  },

  // 扫码
  handleScan() {
    wx.showLoading({
      title: '正在扫码',
    })
    let that = this;
    wx.scanCode({
      success(res) {
        if (res.errMsg === "scanCode:ok") {
          that.setData({
            secretCode: res.result
          })
          wx.hideLoading()
          Toast.success('扫码成功');
        }
      },
      fail(res) {
        wx.hideLoading()
      }
    })
  },
  // 绑定兑换卡
  bindCode() {
    let that = this;

    if (!that.data.secretCode) {
      wx.showToast({
        icon: 'none',
        title: '请输入兑换码',
      })
      return;
    }

    wx.showLoading({
      title: '正在绑定',
    })

    $request
      .post(api.bindExchangeCard, {
        userId: that.data.userId,
        secretCode: that.data.secretCode,
      })
      .then((res) => {
        wx.hideLoading()
        if (res.success) {
          Toast.success('绑定成功');
          if (this.data.enterFlag === "submitOrder") {
            wx.navigateBack()
          }
          that.clearInput();
        } else {
          Toast(res.errMessage);
        }
      })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 获取进入页面来源
    if (options) {
      this.setData({
        enterFlag: options.enterFlag
      })
    }

    // 获取用户信息
    this.setData({
      userId: wx.getStorageSync('userId'), // 用户id
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