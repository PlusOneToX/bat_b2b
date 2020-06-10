// pages/login.js
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
    config: {
      tipsshow1: true,
      tipsshow2: false
    },
    userInfo: {},
    hasUserInfo: false,
    canIUseGetUserProfile: false,
    enterFlag: "",
    enterParams: "",
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true,
          config: {
            tipsshow1: false,
            tipsshow2: true,
          }
        })

        wx.login({
          success: res => {
            wx.setStorageSync('code', res.code);
          },
          fail: function (err) {
            wx.hideLoading()
            console.log(err);
          }
        })
      }
    })
  },
  // 获取用户信息
  getUserInfo: function (e) {
    var that = this;
    if (e.detail.userInfo) {
      that.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true,
        config: {
          tipsshow1: false,
          tipsshow2: true,
        }
      })
      wx.login({
        success: res => {
          wx.setStorageSync('code', res.code);
        },
        fail: function (err) {
          wx.hideLoading()
          console.log(err);
        }
      })
    } else {
      console.log("获取信息失败")
    }
  },
  // 获取用户手机号
  getPhoneNumber: function (e) {
    var that = this;
    //1. 判断code
    if (wx.getStorageSync('code') == null || wx.getStorageSync('code') == '') {
      wx.showToast({
        icon: 'error',
        title: '获取不到用户code'
      })
      return;
    }
    //2. 获取phone、auth
    if (e.detail.errMsg === "getPhoneNumber:ok") {
      // 接收跳转参数
      let enterParams = that.data.enterParams ? JSON.parse(that.data.enterParams) : {};

      let accountInfo = wx.getAccountInfoSync();
      let appId = accountInfo.miniProgram.appId;
      $request
        .post(api.authLogin, {
          'code': wx.getStorageSync('code'),
          'encryptedData': e.detail.encryptedData,
          'iv': e.detail.iv,
          'avatarUrl': that.data.userInfo.avatarUrl,
          'gender': that.data.userInfo.gender,
          'language': that.data.userInfo.language,
          'nickName': that.data.userInfo.nickName,
          'appId': appId
        })
        .then((res2) => {
          if (res2.success) {
            enterParams.phone = res2.data.phone;
            enterParams.userId = res2.data.id;
            enterParams.userNo = res2.data.no;
            enterParams.openid = res2.data.openId;

            wx.setStorageSync('phone', res2.data.phone); // 缓存 phone
            wx.setStorageSync('userId', res2.data.id); // 缓存 userId
            wx.setStorageSync('userNo', res2.data.no); // 缓存 userNo
            wx.setStorageSync('openid', res2.data.openId); // 缓存 openId

            that.setData({
              phone: res2.data.phone,
              config: {
                tipsshow1: false,
                tipsshow2: false,
              }
            })


            // 加入购物车 - 授权登陆 - 购物车 参数处理
            let auth = wx.getStorageSync('auth')
            if (that.data.enterFlag === 'shopcart') {
              let shopcartParams = {};
              shopcartParams.phone = enterParams.phone;
              shopcartParams.userId = enterParams.userId;
              shopcartParams.userNo = enterParams.no;
              shopcartParams.auth = auth;
              shopcartParams.openid = enterParams.openId;

              enterParams.categoryName = decodeURIComponent(enterParams.categoryName);
              enterParams.brandName = decodeURIComponent(enterParams.brandName);
              enterParams.materialsName = decodeURIComponent(enterParams.materialsName);
              enterParams.modelName = decodeURIComponent(enterParams.modelName);
              enterParams.picName = decodeURIComponent(enterParams.picName);

              let info = {
                categoryId: enterParams.categoryId,
                categoryName: enterParams.categoryName,
                brandId: enterParams.brandId,
                brandName: enterParams.brandName,
                generateImage: enterParams.generateImage,
                materialId: enterParams.materialId,
                materialName: enterParams.materialsName,
                modelId: enterParams.modelId,
                modelName: enterParams.modelName,
                pictureId: enterParams.picId,
                pictureName: enterParams.picName,
                previewImage: enterParams.image,
                manufactors: enterParams.manufactor,
              };

              let dataInfo = {
                diy: info,
                itemCode: enterParams.itemCode,
                itemCount: enterParams.itemCount,
                itemType: enterParams.itemType, // 是否赠品：1 非赠品，2 赠品
                salePrice: enterParams.salePrice, // 价格
              }

              // 加入购物车
              $request
                .post(api.addToShopcart, dataInfo)
                .then((res) => {
                  if (res.success) {
                    wx.hideLoading()
                    let pid = res.data.id;
                    wx.redirectTo({
                      url: '/pages/shopcart/shopcart?pid=' + pid,
                    })
                  } else {
                    wx.hideLoading()
                    wx.showToast({
                      icon: 'error',
                      title: res.errMessage
                    })

                    setTimeout(() => {
                      wx.redirectTo({
                        url: '/pages/shopcart/shopcart',
                      })
                    }, 500);
                  }
                })
            } else if (that.data.enterFlag === 'codeList') {
              // 卡包中心
              wx.redirectTo({
                url: '/pages/code/codeList/codeList',
              })
            } else if (that.data.enterFlag === 'mine') {
              // 点击我的 tab
              wx.switchTab({
                url: '/pages/mine/mine',
              })
            } else if (that.data.enterFlag === 'transfer') {
              // 转赠
              wx.redirectTo({
                url: '/pages/transfer/transfer?receiveId=' + enterParams.receiveId,
              })
            } else if (that.data.enterFlag === 'activity') {
              // 转发
              wx.redirectTo({
                url: '/pages/activity/activity?id=' + enterParams.releaseId + "&distributorId=" + enterParams.releaseDistriId + "&type=" + enterParams.releaseType + "&comingFlag=login",
              })
            } else if (that.data.enterFlag === 'equity') {
              // 权益 - 确认验证
              $request
                .post(api.exchangeEquity, {
                  thirdCode: enterParams.thirdCode,
                  distributorId: enterParams.distributorId,
                  userId: enterParams.userId,
                  userNo: enterParams.userNo,
                })
                .then((res) => {
                  if (res.success) {
                    // 领取成功
                    wx.navigateTo({
                      url: '/pages/code/equityReceived/equityReceived?codeId=' + res.data,
                    });
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
                      }).then(() => {
                        wx.redirectTo({
                          url: '/pages/code/equity/equity',
                        });
                      }).catch(() => {});
                    } else if (res.errCode === 'EXCHANGE_QUANYI_NOT_ESIST') {
                      // 权益码不存在
                      Dialog.confirm({
                        title: '温馨提示',
                        message: "订单号错误，请确认订单号是否输入正确。\n如有疑问，请联系苏宁在线客服。",
                        className: "pop-dialog",
                        confirmButtonColor: "#333333",
                        showCancelButton: false,
                      }).then(() => {
                        wx.redirectTo({
                          url: '/pages/code/equity/equity',
                        });
                      }).catch(() => {});
                    } else {
                      wx.showToast({
                        icon: 'error',
                        title: res.errMessage
                      })
                      wx.redirectTo({
                        url: '/pages/code/equity/equity',
                      });
                    }

                  }
                });
            } else {
              wx.switchTab({
                url: '/pages/index/index',
              })
            }
          } else {
            wx.hideLoading()
            wx.showToast({
              icon: 'error',
              title: res2.errMessage
            })
            that.setData({
              config: {
                tipsshow1: true,
                tipsshow2: false
              }
            })
          }
        });
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (wx.getUserProfile) {
      this.setData({
        canIUseGetUserProfile: true
      })
    }

    var that = this;
    if (options.enterFlag) {
      that.setData({
        enterFlag: options.enterFlag,
        enterParams: options.enterParams
      })
    }

    // 获取手机型号
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          mobileModel: res.model
        })
      }
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