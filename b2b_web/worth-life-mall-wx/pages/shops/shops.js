var util = require('../../utils/util.js');

Page({
  data: {
    navList: [],
    goodsList: [],
    shopsSn: '',
    shopsCategoryId: '',
    shopsVo: {},
    footCart: {}
  },
  onLoad: function (options) {
    wx.setStorageSync("navUrl", "/pages/shops/shops?shopsSn=" + options.shopsSn);
    // 页面初始化 options为页面跳转所带来的参数
    var that = this;
    if (options.shopsSn) {
      that.setData({
        shopsSn: options.shopsSn
      });
    }
  },
  onShareAppMessage: function () {
    var that = this;
    return {
      title: that.shopsVo.name,
      desc: that.shopsVo.details,
      path: '/pages/shops/shops?shopsSn=' + that.shopsSn
    }
  },
  // 下拉刷新
  onPullDownRefresh: function () {
      // 显示顶部刷新图标
      wx.showNavigationBarLoading();
      this.getShopsDetail();
      // 隐藏导航栏加载框
      wx.hideNavigationBarLoading();
      // 停止下拉动作
      wx.stopPullDownRefresh();
  },
  getShopsDetail: function () {
    let that = this;
    util.request('shops/detailBySn', {
        shopsSn: that.data.shopsSn
      })
      .then(function (res) {
        if (res.code === 0) {
          that.setData({
            shopsVo: res.data
          });
          that.getMycart();

          util.request('shops/shopsCategory', {
            shopsId: that.data.shopsVo.id
          }).then(function (res) {
            if (res.code === 0) {
              that.setData({
                navList: res.data
              });
              if (res.data.length > 0) {
                that.getCurrentCategoryGoods(res.data[0].id);
              }
            } else {
              //显示错误信息
              util.showMsg(res.data.errmsg);
            }
          });
          wx.getLocation({
            type: 'gcj02',
            success(data) {
              let shopsVo = res.data;
              shopsVo.distant = util.getDistance(data.latitude, data.longitude, shopsVo.latitude, shopsVo.longitude)
              that.setData({
                shopsVo: shopsVo
              });
            }
          })
        } else {
          //显示错误信息
          util.showMsg(res.errmsg);
        }
      });
  },
  goHere: function (e) {
    wx.openLocation({
      name: e.currentTarget.dataset.name,
      address: e.currentTarget.dataset.address,
      latitude: parseFloat(e.currentTarget.dataset.latitude),
      longitude: parseFloat(e.currentTarget.dataset.longitude)
    })
  },
  getMycart: function () {
    let that = this;
    if (that.data.shopsVo && that.data.shopsVo.id) {
      util.request('cart/myCart', {
        shopsId: that.data.shopsVo.id
      }).then(function (resp) {
        if (resp.code === 0) {
          that.setData({
            footCart: resp.data,
          });
        }
      });
    }
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.getShopsDetail();
  },
  onHide: function () {
    // 页面隐藏
  },
  openCartPage: function () {
    let that = this;
    wx.navigateTo({
      url: '/pages/cartShops/cartShops?shopsId=' + that.data.shopsVo.id,
    })
  },
  switchCategory: function (event) {
    var that = this;
    var shopsCategoryId = event.currentTarget.dataset.id;
    if (that.data.shopsCategoryId == shopsCategoryId) {
      return false;
    }
    this.getCurrentCategoryGoods(event.currentTarget.dataset.id);
  },

  getCurrentCategoryGoods: function (shopsCategoryId) {
    let that = this;

    that.setData({
      shopsCategoryId: shopsCategoryId
    })
    util.request('shops/shopsCategoryGoods', {
        shopsCategoryId: shopsCategoryId
      })
      .then(function (res) {
        if (res.code === 0) {
          var goodsList = res.data;
          goodsList.forEach(function (val, index, arr) {
            if (that.data.footCart.cartList) {
              that.data.footCart.cartList.forEach(function (cartVal, cartIndex, cartArr) {
                if (val.goodsId == cartVal.goodsId) {
                  val.cartNum = cartVal.number;
                  goodsList[index] = val;
                }
              });
            }
          }, that);
          that.setData({
            goodsList: goodsList
          });
        }
      });
  },

  //购物车增加
  changeNumber: function (e) {
    let that = this;
    var goodsId = e.currentTarget.dataset.goodsId;
    if (e.detail.type === "reduce") {
      util.request('cart/minus', {
        goodsId: goodsId,
        number: 1,
        shopsId: that.data.shopsVo.id,
      }, 'POST').then(function (res) {
        if (res.code === 0 && null != res.data) {
          var goodsList = that.data.goodsList;
          goodsList.forEach(function (val, index, arr) {
            if (val.goodsId == goodsId) {
              val.cartNum = res.data;
              goodsList[index] = val;
            }
          }, that);
          that.setData({
            goodsList: goodsList
          });
          that.getMycart();
        }
      });
    }
    if (e.detail.type === "plus") {
      util.request('cart/add', {
        goodsId: goodsId,
        shopsId: that.data.shopsVo.id,
        number: 1
      }, 'POST').then(function (res) {
        if (res.code === 0 && null != res.cartList) {
          var goodsList = that.data.goodsList;
          goodsList.forEach(function (val, index, arr) {
            res.cartList.forEach(function (cartVal, cartIndex, cartArr) {
              if (val.goodsId == cartVal.goodsId) {
                val.cartNum = cartVal.number;
                goodsList[index] = val;
              }
            });
          }, that);
          that.setData({
            goodsList: goodsList
          });
          that.getMycart();
        }
      });
    }

  },
  openCall: function (e) {
    wx.makePhoneCall({
      phoneNumber: e.target.dataset.phone
    })
  }
})
