// pages/transfer/transfer.js
// Toast
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
// Dialog
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    diyTitle: "定制专区", // 小程序名称
    receiveId: "", // 领取id
    headImg: "", // 转赠人头像
    nickName: "", // 转赠人昵称
    receiveText: "", // 收取文案
    receiveImg: "", // 收取卡片图
    isReceived: false, // 是否已领取
    receivedUserId: "", // 领取用户id
    exchangeId: "", // 兑换卡id
    isTheSame: false, // 是否是同一人（领取人与当前用户）
  },

  // 初始化数据
  initData() {
    Toast.loading({
      duration: 0, // 持续展示 toast
      message: '加载中...',
      forbidClick: true,
      loadingType: 'spinner',
      selector: '#van-toast',
    });
    $request
      .get(api.exchangeTransferDetail, {
        id: this.data.receiveId,
      })
      .then((res) => {
        if (res.success) {
          this.setData({
            headImg: res.data.headImg, // 转赠人头像
            nickName: res.data.nickName, // 转赠人昵称
            receiveText: res.data.receiveText, // 收取文案
            receiveImg: res.data.receiveImg, // 收取卡片图
            isReceived: res.data.receiveFlag ? true : false, // 是否已领取
            receivedUserId: res.data.toUserId, // 领取用户id
            exchangeId: res.data.exchangeId, // 兑换卡id
          })
          let userId = wx.getStorageSync('userId');
          if (Number(userId) === Number(this.data.receivedUserId)) {
            this.setData({
              isTheSame: true
            })
          } else {
            this.setData({
              isTheSame: false
            })
          }
        } else {
          if (res.errCode === "B_AUTH_FAIL") {
            let enterParams = JSON.stringify({
              receiveId: this.data.receiveId
            });
            wx.navigateTo({
              url: '/pages/login/login?enterFlag=transfer&enterParams=' + enterParams,
            })
          } else {
            Toast.fail(res.errMessage);
          }
        }
        Toast.clear();
      })
  },

  handleBtn() {
    let userId = wx.getStorageSync('userId');
    if (this.data.isReceived && Number(userId) === Number(this.data.receivedUserId)) {
      // 去定制
      this.goCustom();
    } else {
      // 开心收下
      this.handleReceive();
    }
  },

  // 去定制
  goCustom() {
    // 获取默认 exchangeId
    $request
      .get(api.getDefaultExchangeId, {
        id: this.data.exchangeId,
      })
      .then((res) => {
        if (res.success) {
          let exchangeId = res.data.exchangeId;
          let distributorId = 2601;
          if (res.data.distributorId) {
            distributorId = res.data.distributorId;
          }

          wx.setStorageSync("distributorId", distributorId);
          wx.setStorageSync("exchangeId", exchangeId);

          // 根据兑换码id获取可兑换材质
          $request
            .get(api.getMaterialByExchangeId, {
              id: exchangeId,
            })
            .then((res) => {
              if (res.success) {
                let params = "";
                if (res.data && res.data.length > 0) {
                  // 默认取第一个id
                  params = JSON.stringify({
                    materialId: res.data[0],
                    modelId: wx.getStorageSync('modelId'),
                    modelName: encodeURIComponent(encodeURIComponent(wx.getStorageSync('modelName'))),
                    canSelectMaterial: "no", // 是否可选材质
                  })
                } else {
                  params = JSON.stringify({
                    modelId: wx.getStorageSync('modelId'),
                    modelName: encodeURIComponent(encodeURIComponent(wx.getStorageSync('modelName'))),
                    canSelectMaterial: "no", // 是否可选材质
                  })
                }

                wx.navigateTo({
                  url: '/pages/webview/webview?enterFlag=diyCustom&enterParams=' + params,
                })
              } else {
                Toast.fail(res.errMessage);
              }
            });
        } else {
          Toast.fail(res.errMessage);
        }
      });
  },

  // 开心收下
  handleReceive() {
    $request
      .post(api.exchangeTransferReceive, {
        id: this.data.receiveId,
      })
      .then((res) => {
        if (res.success) {
          this.initData(); // 初始化数据
        } else {
          // 已被领取
          if (res.errCode === "EXCHANGE_HAS_RECEICE") {
            Dialog.confirm({
              title: "温馨提示",
              message: res.errMessage,
              className: "pop-dialog",
              confirmButtonText: "去定制看看",
              confirmButtonColor: "#333333",
              cancelButtonText: "我知道了",
              cancelButtonColor: "#999999",
            }).then(() => {
              // 去首页
              wx.switchTab({
                url: '/pages/index/index',
              })
            }).catch(() => {
              // on cancel
            });
          } else if (res.errCode === "B_AUTH_FAIL") {
            let enterParams = JSON.stringify({
              receiveId: this.data.receiveId
            });
            wx.navigateTo({
              url: '/pages/login/login?enterFlag=transfer&enterParams=' + enterParams,
            })
          } else {
            Toast.fail(res.errMessage);
          }
        }
      })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      // 获取转赠 receiveId
      receiveId: options.receiveId,
    })

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
    // 小程序名称
    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    if (appId === "wx20a6b4c4b10542e1") {
      this.setData({
        diyTitle: "BAT小程序"
      })
    } else {
      this.setData({
        diyTitle: "其他"
      })
    }

    app.getTenantInfo().then((res) => {
      this.initData(); // 初始化数据
    });
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