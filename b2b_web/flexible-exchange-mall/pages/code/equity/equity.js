// pages/code/equity/equity.js
// Toast
import Toast from '../../../miniprogram_npm/@vant/weapp/toast/toast';
// Dialog
import Dialog from '../../../miniprogram_npm/@vant/weapp/dialog/dialog';
// Api
import $request from '../../../assets/api/request'
import api from '../../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    thirdCode: "", // 第三方编码
    distributorId: 7252, // 苏宁分销商id
    hasClicked: false, // 是否点击按钮（防重点）
    // platform: 58, // 苏宁平台编码
  },

  // 监听输入框
  bindInput(e) {
    let thirdCode = e.detail.value;

    this.setData({
      thirdCode: thirdCode
    });
  },

  // 确认验证
  handleVerify() {
    let userId = wx.getStorageSync('userId');
    let userNo = wx.getStorageSync('userNo');

    if (!this.data.thirdCode) {
      wx.showToast({
        icon: 'none',
        title: '请输入苏宁订单号',
      });
      return;
    }

    if (!this.data.hasClicked) {
      this.setData({
        hasClicked: true,
      });

      $request
        .post(api.exchangeEquity, {
          thirdCode: this.data.thirdCode,
          distributorId: this.data.distributorId,
          userId: userId,
          userNo: userNo,
        })
        .then((res) => {
          if (res.success) {
            // 领取成功
            wx.navigateTo({
              url: '/pages/code/equityReceived/equityReceived?codeId=' + res.data,
            });
          } else {
            if (res.errCode === 'B_AUTH_FAIL') {
              let enterParams = JSON.stringify({
                thirdCode: this.data.thirdCode,
                distributorId: this.data.distributorId,
              });
              wx.navigateTo({
                url: '/pages/login/login?enterFlag=equity&enterParams=' + enterParams,
              })
            } else {
              // 领取失败
              if (res.errCode === 'EXCHANGE_HAS_RECEICE') {
                // 已验证过
                Dialog.confirm({
                  title: '温馨提示',
                  message: "该订单已验证过，请勿重复输入。\n如有疑问，请联系苏宁在线客服。",
                  className: "pop-dialog",
                  confirmButtonColor: "#333333",
                  showCancelButton: false,
                }).then(() => {}).catch(() => {});
              } else if (res.errCode === 'EXCHANGE_QUANYI_NOT_ESIST') {
                // 权益码不存在
                Dialog.confirm({
                  title: '温馨提示',
                  message: "订单号错误，请确认订单号是否输入正确。\n如有疑问，请联系苏宁在线客服。",
                  className: "pop-dialog",
                  confirmButtonColor: "#333333",
                  showCancelButton: false,
                }).then(() => {}).catch(() => {});
              } else {
                Toast.fail(res.errMessage);
              }
            }
          }
          this.setData({
            hasClicked: false,
          });
        })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 获取设备信息（手机型号）
    wx.getSystemInfo({
      success: (result) => {
        let mobile = result.model.split('<')[0]
        wx.setStorageSync('modelName', mobile)
      },
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