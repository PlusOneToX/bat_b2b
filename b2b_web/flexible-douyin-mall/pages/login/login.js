const app = getApp();
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({
  data: {
    config: {
      tipsshow1: true,
      tipsshow2: false
    },
    userInfo: {},
    enterFlag: "",
    enterParams: "",
  },
  
  // 获取用户信息
  getUserInfo() {
    let that = this;
    tt.login({
      success(_res) {
        console.log(_res)
        // 调用 getUserInfo 前, 请确保登录成功
        tt.setStorageSync('code', _res.code); // 临时登录凭证, 有效期 3 分钟

        // 获取用户信息
        tt.getUserInfo({
          withCredentials: true,
          withRealNameAuthenticationInfo: true,
          success(res) {
            console.log(res)
            let userInfo = res.userInfo;
            userInfo.encryptedData = res.encryptedData;
            userInfo.iv = res.iv;
            that.setData({
              userInfo: userInfo
            })
            that.handleLogin();
          },
          fail(res) {
            console.log(res.errMsg);
          },
        });
      },
    });
  },

  handleLogin() {
    let that = this;
    // 接收跳转参数
    let enterParams = that.data.enterParams ? JSON.parse(that.data.enterParams) : {};

    let appId = app.globalData.appId;
    $request
      .post(api.authLogin, {
        'code': tt.getStorageSync('code'),
        'encryptedData': that.data.userInfo.encryptedData,
        'iv': that.data.userInfo.iv,
        'avatarUrl': that.data.userInfo.avatarUrl,
        'gender': that.data.userInfo.gender,
        'language': that.data.userInfo.language,
        'nickName': that.data.userInfo.nickName,
        'appId': appId
      })
      .then((res2) => {
        if (res2.success) {
          tt.removeStorageSync('code'); // 移除缓存code

          enterParams.phone = res2.data.phone;
          enterParams.userId = res2.data.id;
          enterParams.userNo = res2.data.no;
          enterParams.openid = res2.data.openId;

          tt.setStorageSync('phone', res2.data.phone); // 缓存 phone
          tt.setStorageSync('userId', res2.data.id); // 缓存 userId
          tt.setStorageSync('userNo', res2.data.no); // 缓存 userNo
          tt.setStorageSync('openid', res2.data.openId); // 缓存 openId

          that.setData({
            phone: res2.data.phone,
            config: {
              tipsshow1: false,
              tipsshow2: false,
            }
          })


          // 加入购物车 - 授权登陆 - 购物车 参数处理
          let auth = tt.getStorageSync('auth')
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
                  tt.hideLoading()
                  let pid = res.data.id;
                  tt.redirectTo({
                    url: '/pages/shopcart/shopcart?pid=' + pid,
                  })
                } else {
                  tt.hideLoading()
                  tt.showToast({
                    icon: 'error',
                    title: res.errMessage
                  })

                  setTimeout(() => {
                    tt.redirectTo({
                      url: '/pages/shopcart/shopcart',
                    })
                  }, 500);
                }
              })
          } else if (that.data.enterFlag === 'cartTab') {
            // 购物车 tab
            tt.switchTab({
              url: '/pages/shopcart/shopcart',
            })
          } else if (that.data.enterFlag === 'coupon') {
            // 优惠券
            tt.redirectTo({
              url: '/pages/coupon/coupon',
            })
          } else {
            tt.switchTab({
              url: '/pages/index/index',
            })
          }
        } else {
          tt.hideLoading()
          tt.showToast({
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
  },

  onLoad: function (options) {
    var that = this;
    if (options.enterFlag) {
      that.setData({
        enterFlag: options.enterFlag,
        enterParams: options.enterParams
      })
    }
  },
})