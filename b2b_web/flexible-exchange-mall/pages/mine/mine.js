// pages/mine/mine.js
// Toast
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
// Dialog
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    avatarImg: '../../assets/images/my_avator.png', // 头像
    nikeName: '游客', // 昵称
    showDialog: false, // 在线客服弹窗
    phoneNumber: "17302671334", // 客服电话
    wechatStr: "w-666357", // 微信客服
  },

  // 获取用户信息
  getUserInfo(userId) {
    $request
      .get(api.getUserInfo, {
        id: userId,
      })
      .then((res) => {
        if (res.success) {
          if (
            res.data.nikeName !== null &&
            res.data.nikeName !== "" &&
            res.data.nikeName !== undefined
          ) {
            this.setData({
              nikeName: res.data.nikeName
            })
          }
          if (
            res.data.headPortrait !== null &&
            res.data.headPortrait !== "" &&
            res.data.headPortrait !== undefined
          ) {
            this.setData({
              avatarImg: res.data.headPortrait
            })
          }
        } else {
          Toast.fail(res.errMessage);
        }
      });
  },

  // 在线客服弹窗 - 显示/隐藏
  handleDialog(e) {
    let status = e.currentTarget.dataset.status;

    this.setData({
      showDialog: status
    })
  },

  // 客服热线
  handlePhone() {
    Dialog.confirm({
      title: "拨打热线",
      message: this.data.phoneNumber,
      className: "pop-dialog",
      confirmButtonColor: "#333333",
      cancelButtonColor: "#999999",
    }).then(() => {
      // 拨打电话
      wx.makePhoneCall({
        phoneNumber: this.data.phoneNumber
      })

      this.setData({
        showDialog: false
      })
    }).catch(() => {
      // on cancel
    });
  },

  // 微信客服
  handleWechat() {
    Dialog.confirm({
      title: "客服微信号：" + this.data.wechatStr,
      message: "是否复制微信号？",
      className: "pop-dialog",
      confirmButtonText: "复制",
      confirmButtonColor: "#333333",
      cancelButtonColor: "#999999",
    }).then(() => {
      // 复制微信号
      wx.setClipboardData({
        data: this.data.wechatStr,
        success(res) {
          wx.showToast({
            title: '复制成功'
          });
        }
      });

      this.setData({
        showDialog: false
      })
    }).catch(() => {
      // on cancel
    });
  },

  // 企业微信客服
  handleCompanyWechat() {
    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;

    if (appId === "wx5d6f4e2g6ef5ess2") {
      // BAT小程序
      this.handleWechat();
    } else {
      // 其他
      let url = ''; // 客服链接
      let corpId = ''; // 企业id

      $request
        .get(api.getWechatInfo + '?key=diy_customer_service_url&key=enterprise_id')
        .then((res) => {
          if (res.success && res.data.length > 0) {
            res.data.forEach(item => {
              if (item.key === 'enterprise_id') {
                corpId = item.value
              }
              if (item.key === 'diy_customer_service_url') {
                url = item.value
              }
            })
            wx.openCustomerServiceChat({
              extInfo: {
                url: url
              },
              corpId: corpId,
              success(res) {
                console.log(res)
              },
              fail(err) {
                Toast.fail(err);
              }
            })
          } else {
            Toast.fail(res.errMessage);
          }
        });
    }
  },

  /* 页面跳转 */
  // 订单
  goOrder(e) {
    let sid = e.currentTarget.dataset.sid;
    let enterParams = JSON.stringify({
      sid: sid
    });
    wx.navigateTo({
      url: "/pages/webview/webview?enterFlag=orderList&enterParams=" + enterParams,
    });
  },

  // 购物车
  goShopcart() {
    wx.navigateTo({
      url: '/pages/shopcart/shopcart',
    })
  },

  // 地址管理
  goAddress() {
    let enterParams = JSON.stringify({});
    wx.navigateTo({
      url: '/pages/webview/webview?enterFlag=address&enterParams=' + enterParams,
    })
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
    let userId = wx.getStorageSync('userId')
    if (userId) {
      this.getUserInfo(userId)
    }
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