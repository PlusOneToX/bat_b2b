// pages/code/equityReceived/equityReceived.js
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
    codeId: "", // 兑换码id
    codeData: {},
    materialId: "",
  },

  initData() {
    $request
      .get(api.getCodeDetail, {
        id: this.data.codeId,
      })
      .then((res) => {
        if (res.success) {
          this.setData({
            codeData: res.data.exchangeCodeUser,
            materialId: res.data.materialRelas[0].materialId,
          })
        } else {
          Toast.fail(res.errMessage);
        }
      })
  },

  // 进入卡包中心
  goCode() {
    wx.navigateTo({
      url: "/pages/code/codeList/codeList",
    });
  },

  // 前往定制
  goCustom() {
    let modelName = wx.getStorageSync('modelName');

    let enterParams = JSON.stringify({
      materialId: this.data.materialId,
      modelName: encodeURIComponent(encodeURIComponent(modelName)),
      canSelectMaterial: "no", // 是否可选材质
    });

    wx.navigateTo({
      url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      codeId: options.codeId || 558687
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
    wx.setStorageSync('distributorId', 2601);
    wx.setStorageSync('platform', 28);
    wx.setStorageSync('orderSource', 28);

    this.initData();
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