const app = getApp();
// Api
import $request from '/assets/api/request'
import api from '/assets/api/allApi'

Page({
  data: {
    userInfo: {},
    enterFlag: "",
    enterParams: "",
  },

  // 获取用户信息
  getUserInfo() {
    my.getAuthCode({
      scopes: ['auth_user'],
      // 主动授权：auth_user，静默授权：auth_base或者其它scope。如需同时获取用户多项授权，可在 scopes 中传入多个 scope 值。
      success: (res) => {
        if (res.authCode) {
          this.handleLogin(res.authCode);
        }
      },
    });
  },

  handleLogin(authCode) {
    let that = this;
    // 接收跳转参数
    let enterParams = that.data.enterParams ? JSON.parse(that.data.enterParams) : {};

    let appId = app.globalData.appId;
    $request
      .post(api.authLogin, {
        'code': authCode,
        'appId': appId
      })
      .then((res) => {
        if (res.success) {

          enterParams.phone = res.data.phone;
          enterParams.userId = res.data.id;
          enterParams.userNo = res.data.no;
          enterParams.openid = res.data.openId;

          my.setStorageSync({
            key: 'phone',
            data: res.data.phone
          }); // 缓存 phone
          my.setStorageSync({
            key: 'userId',
            data: res.data.id
          }); // 缓存 userId
          my.setStorageSync({
            key: 'userNo',
            data: res.data.no
          }); // 缓存 userNo
          my.setStorageSync({
            key: 'openid',
            data: res.data.openId
          }); // 缓存 openId

          // 加入购物车 - 授权登陆 - 购物车 参数处理
          let auth = my.getStorageSync({key: 'auth'}).data
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
                  my.hideLoading()
                  let pid = res.data.id;
                  my.redirectTo({
                    url: '/pages/shopcart/shopcart?pid=' + pid,
                  })
                } else {
                  my.hideLoading()
                  my.showToast({
                    type: 'error',
                    content: res.errMessage
                  })

                  setTimeout(() => {
                    my.redirectTo({
                      url: '/pages/shopcart/shopcart',
                    })
                  }, 500);
                }
              })
          } else if (that.data.enterFlag === 'cartTab') {
            // 购物车 tab
            my.switchTab({
              url: '/pages/shopcart/shopcart',
            })
          } else if (that.data.enterFlag === 'coupon') {
            // 优惠券
            my.redirectTo({
              url: '/pages/coupon/coupon',
            })
          } else {
            my.switchTab({
              url: '/pages/index/index',
            })
          }
        } else {
          my.hideLoading()
          my.showToast({
            type: 'error',
            content: res.errMessage
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