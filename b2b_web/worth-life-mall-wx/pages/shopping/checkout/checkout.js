var util = require('../../../utils/util.js');
const pay = require('../../../services/pay.js');
const app = getApp()

Page({
  data: {
    cartArr: [{
      "name": "微信支付",
      "img": "wxpay.png",
      value: 'weixin'
    }],
    checkedGoodsList: [],
    addressId: '',
    checkedAddress: {},
    goodsTotalPrice: 0.00, //商品总价
    shippingFee: 0.00, //快递费
    orderTotalPrice: 0.00, //订单总价
    actualPrice: 0.00, //实际需要支付的总价
    sumSubPrice: 0, //共计优惠金额
    payPrice:0,// 支付金额
    postscript: "",
    payType: 'weixin',
    isBuy: false,
    shopsId: '',
    tmplIds: [],
    couponId: '',
    checkedGoodsIdList: '',
    checkedBrandIdList: '',
    promoUserId: ''
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options.shopsId) {
      this.setData({
        shopsId: options.shopsId
      });
    }
    if (options.couponId) {
      this.setData({
        couponId: options.couponId
      });
    }
    if(options.promoUserId) {
      this.setData({
        promoUserId: options.promoUserId
      });
    }
    if (options.isBuy != null) {
      this.data.isBuy = options.isBuy
    }
    this.data.buyType = this.data.isBuy ? 'buy' : 'cart'
    if (app.globalData.rechargeStatus == 1) {
      this.setData({
        cartArr: [{
            "name": "微信支付",
            "img": "wxpay.png",
            value: 'weixin'
          },
          {
            "name": "余额支付",
            "img": "yue.png",
            value: 'yue'
          }
        ]
      });
    }
  },

  bindPostscriptBlur(e) {
    let postscript = e.detail.value;
    this.setData({
      postscript: postscript
    });
  },
  getCheckoutInfo: function () {
    let that = this;
    // console.log(that.data.promoUserId)
    let buyType = this.data.isBuy ? 'buy' : 'cart'
    util.request('cart/checkout', {
      addressId: that.data.addressId,
      shopsId: that.data.shopsId,
      type: buyType,
      couponId: that.data.couponId
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          payPrice:(res.discount * res.actualPrice).toFixed(2),
          sumSubPrice: res.sumSubPrice,
          checkedGoodsList: res.checkedGoodsList,
          checkedAddress: res.checkedAddress,
          actualPrice: res.actualPrice,
          shippingFee: res.shippingFee,
          goodsTotalPrice: res.goodsTotalPrice,
          orderTotalPrice: res.orderTotalPrice,
          checkedGoodsIdList: res.checkedGoodsIdList.join(","),
          checkedBrandIdList: res.checkedBrandIdList.join(",")
        });
      }
    });
  },
  selectAddress() {
    wx.navigateTo({
      url: '/pages/shopping/address/address',
    })
  },
  addAddress() {
    wx.navigateTo({
      url: '/pages/shopping/addressAdd/addressAdd',
    })
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    let addressId = wx.getStorageSync('addressId');
    this.setData({
      addressId: addressId
    });
    this.getCheckoutInfo();
    let that = this;
    util.request('index/getTemplateId', {
      templateTypes: '0,1,5'
    }).then(res => {
      if (res.code === 0) {
        that.setData({
          tmplIds: res.data
        })
      }
    });
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  submitOrder: function (e) {
    let that = this;
    if (util.isEmpty(this.data.checkedAddress) && that.data.shopsId === '') {
      util.showMsg('请添加收货地址');
      return false;
    }

    if (that.data.payType == '') {
      util.showMsg('请选择支付方式');
      return false;
    }
    wx.requestSubscribeMessage({
      tmplIds: that.data.tmplIds,
      success(res) {

      },
      fail(res) {

      },
      complete() {
        that.orderSubmit(e);
      }
    })
  },

  radioChange: function (e) {
    this.setData({
      payType: e.currentTarget.dataset.value
    })
  },
  /**
   * 付款请求
   */
  orderSubmit: function (e) {
    let that = this;
    let buyType = this.data.isBuy ? 'buy' : 'cart'
    util.request('order/submitOrder', {
      fromType: 1,
      checkedAddress: this.data.checkedAddress,
      postscript: this.data.postscript,
      shopsId: that.data.shopsId,
      couponId: that.data.couponId,
      type: buyType,
      promoUserId: that.data.promoUserId
    }, 'POST').then(res => {
      if (res.code === 0) {
        const orderId = res.data.id;
        if (that.data.payType == 'weixin') {
          pay.payOrder(orderId).then(res => {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=1&orderId=' + orderId
            });
          }).catch(res => {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=0&orderId=' + orderId + '&msg=' + res.errMsg
            });
          });
        }
        if (that.data.payType == 'yue') {
          util.request('pay/buyByYue', {
            fromType: 1,
            orderId: orderId
          }, 'POST').then((res) => {
            if (res.code === 0) {
              util.showMsg('支付成功');
              setTimeout(function () {
                wx.redirectTo({
                  url: '/pages/payResult/payResult?status=1&orderId=' + orderId
                });
              }, 1000)
            } else {
              if (res.code === 300) {
                wx.showModal({
                  title: res.msg,
                  content: '是否充值？',
                  success(res) {
                    if (res.confirm) {
                      wx.navigateTo({
                        url: '/pages/ucenter/chongzhi/chongzhi'
                      })
                    } else if (res.cancel) {
                      wx.reLaunch({
                        url: '/pages/ucenter/order/order'
                      })
                    }
                  }
                })
              } else {
                util.showMsg(res.msg);
              }
            }
          });
        }
      } else {
        util.showMsg(res.msg);
      }
    });
  },
  navigateToCoupon: function () {
    let that = this
    wx.navigateTo({
      url: '/pages/shopping/selectCoupon/selectCoupon?checkedGoodsIdList=' + that.data.checkedGoodsIdList + '&checkedBrandIdList=' + that.data.checkedBrandIdList,
    })
  }
})
