// pages/activity/activity.js
// Toast
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    statusBarHeight: 0, // 状态栏高度
    customHeight: 0, // 自定义title高度
    isReceived: false, // 是否已领取
    diyTitle: "定制专区", // 小程序名称
    releaseId: "", // 发布id
    releaseDistriId: "", // 发卡分销商id
    cardImg: "", // 兑换卡
    exchangeShareData: {}, // 转发活动
    saveWrap: false, // 保存至相册弹窗
    saveImg: "", // 保存至相册图片地址
    comingFlag: "", // 进入来源
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

    let userId = wx.getStorageSync('userId'); // 获取缓存 userId
    $request
      .post(api.exchangeShareDetail, {
        id: this.data.releaseId,
        type: this.data.releaseType, // 打开类型：1 二次转发查看，2 普通查看
        userId: userId
      })
      .then((res) => {
        if (res.success) {
          this.setData({
            cardImg: res.data.pageImg,
            isReceived: res.data.isReceive ? true : false, // 是否已领取
            exchangeId: res.data.exchangeId, // 兑换卡id
            exchangeShareData: res.data
          })
        } else {
          if (res.errCode === "B_AUTH_FAIL") {
            let enterParams = JSON.stringify({
              releaseId: this.data.releaseId,
              releaseDistriId: this.data.releaseDistriId,
              releaseType: this.data.releaseType
            });
            wx.navigateTo({
              url: '/pages/login/login?enterFlag=activity&enterParams=' + enterParams,
            })
          } else {
            Toast.fail(res.errMessage);
          }
        }
        Toast.clear();
      })
  },

  handleBtn() {
    if (this.data.isReceived) {
      // 去定制
      this.goCustom();
    } else {
      // 领取
      this.handleReceive();
    }
  },

  // 去定制
  goCustom(e) {
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

  // 领取
  handleReceive() {
    $request
      .post(api.exchangeShareReceive, {
        id: this.data.releaseId,
      })
      .then((res) => {
        if (res.success) {
          if (this.data.comingFlag === "login") {
            wx.showToast({
              title: '您已经领取过啦~',
              icon: 'none'
            });
          } else {
            wx.showToast({
              title: '领取成功~',
              icon: 'none'
            });
          }
          this.initData(); // 初始化数据
        } else {
          if (res.errCode === "B_AUTH_FAIL") {
            let enterParams = JSON.stringify({
              releaseId: this.data.releaseId,
              releaseDistriId: this.data.releaseDistriId,
              releaseType: this.data.releaseType
            });
            wx.navigateTo({
              url: '/pages/login/login?enterFlag=activity&enterParams=' + enterParams,
            })
          } else {
            Toast.fail(res.errMessage);
          }
        }
      })
  },

  // 保存到相册
  saveToPhoto() {
    this.setData({
      saveWrap: true
    })

    // canvas 2d 方式
    let that = this;
    const query = wx.createSelectorQuery();
    query.select('#shareImg')
      .fields({
        node: true,
        size: true
      })
      .exec((res) => {
        const canvas = res[0].node
        const ctx = canvas.getContext('2d')
        const dpr = wx.getSystemInfoSync().pixelRatio
        canvas.width = res[0].width * dpr
        canvas.height = res[0].height * dpr
        ctx.scale(dpr, dpr)

        // 背景图
        wx.getImageInfo({
          src: that.data.exchangeShareData.friendImg,
          success: function (res) {
            let bgImg = canvas.createImage();
            bgImg.src = res.path;
            bgImg.onload = () => {
              ctx.drawImage(bgImg, 0, 0, canvas.width / dpr, canvas.height / dpr);
            }

            // 二维码
            wx.getImageInfo({
              src: that.data.exchangeShareData.qrCodeUrl,
              success: function (res) {
                let seal = canvas.createImage();
                seal.src = res.path;

                seal.onload = () => {
                  ctx.arc(54, canvas.height / dpr - 50, 32, 0, 2 * Math.PI)
                  ctx.fillStyle = "white";
                  ctx.fill();
                  ctx.clip(); //裁剪上面的圆形
                  ctx.drawImage(seal, 24, canvas.height / dpr - 80, 60, 60);

                  // 合并图片
                  wx.canvasToTempFilePath({
                    canvas: canvas, // 使用 2D 方式，需要传递的参数
                    success: function (res) {
                      that.setData({
                        saveImg: res.tempFilePath
                      })
                    },
                    fail: function (res) {
                      console.log(res)
                    }
                  })
                }
              },
              fail: function (res) {
                console.log(res)
              }
            })
          },
          fail: function (res) {
            console.log(res)
          }
        })
      })
  },
  handleSavePhoto() {
    let that = this;
    wx.saveImageToPhotosAlbum({
      filePath: that.data.saveImg,
      success: function (res) {
        wx.showModal({
          title: "友情提示",
          content: "保存成功，快去分享给好友吧~",
          confirmText: "好的",
          showCancel: false,
        })
      },
      fail: function (res) {
        console.log(res)
      }
    })
  },
  hiddenMask() {
    this.setData({
      saveWrap: false
    })
  },

  // 首页
  goHome() {
    wx.switchTab({
      url: '/pages/index/index',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options) {
      let releaseId = ""
      if (options.scene) {
        releaseId = decodeURIComponent(options.scene)
      } else if (options.id) {
        releaseId = options.id
      }
      this.setData({
        releaseId: releaseId,
        releaseDistriId: options.distributorId,
        releaseType: options.type ? Number(options.type) : 2,
        comingFlag: options.comingFlag || ""
      })
    }

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
    let capsuleObj = wx.getMenuButtonBoundingClientRect(); // 胶囊信息
    wx.getSystemInfo({
      success: (res) => {
        var statusBarHeight = res.statusBarHeight; // 顶部状态栏高度
        let customHeight = capsuleObj.height + (capsuleObj.top - statusBarHeight) * 2; // 自定义title高度
        this.setData({
          statusBarHeight: statusBarHeight,
          customHeight: customHeight
        })
      },
      failure() {}
    })

    // 小程序名称
    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    if (appId === "wx5d6f4e2g6ef5ess2") {
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
  onShareAppMessage: function (res) {
    if (res.from === "button") {
      // 邀好友领
      let id = this.data.exchangeShareData.exchangeSpecialReleaseId; // 转发专题发布id
      return $request
        .post(api.exchangeShareSecond, {
          id: id,
        })
        .then((res) => {
          if (res.success) {
            return {
              title: res.data.forwardText,
              path: "/pages/activity/activity?id=" + res.data.exchangeSpecialReleaseId + "&distributorId=" + res.data.distributorId + "&type=1",
              imageUrl: res.data.forwardImg,
            }
          } else {
            if (res.errCode === "B_AUTH_FAIL") {
              let enterParams = JSON.stringify({
                releaseId: this.data.releaseId,
                releaseDistriId: this.data.releaseDistriId,
                releaseType: this.data.releaseType
              });
              wx.navigateTo({
                url: '/pages/login/login?enterFlag=activity&enterParams=' + enterParams,
              })
            } else {
              Toast.fail(res.errMessage);
            }
          }
        })
    }
  }
})