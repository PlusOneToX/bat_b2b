var util = require('../../utils/util.js');
const utils = require('../../components/utils/utils');

Page({
  data: {
    cartGoods: [],
    footprintList: [],
    cartTotal: {
      "goodsCount": 0,
      "goodsAmount": 0.00,
      "checkedGoodsCount": 0,
      "checkedGoodsAmount": 0.00
    },
    isEditCart: false,
    checkedAllStatus: true,
    editCartList: [],
    actions: [{
      name: '删除',
      color: '#fff',
      fontsize: '22',
      width: 80,
      //icon: 'like.png',//此处为图片地址
      background: '#ed3f14'
    }]
  },
  onLoad: function (options) {
    wx.setStorageSync("navUrl", "/pages/cart/cart");
  },
  onReady: function () {
    // 页面渲染完成

  },
  onShow: function () {
    // 页面显示
    this.getCartList();
  },
  onHide: function () {
    // 页面隐藏
    this.setData({
      isEditCart:false
    })

  },
  onUnload: function () {
    // 页面关闭

  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;
    this.getCartList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  toIndexPage: function () {
    wx.switchTab({
      url: "/pages/index/index"
    });
  },
  getFootprintList() {
    let that = this;
    util.request('user/footprintList').then(function (res) {
      if (res.code === 0) {
        if (res.data.records) {
          that.setData({
            footprintList: res.data.records
          });
        }
      }
    });
  },
  getCartList: function () {
    let that = this;
    util.request('cart/myCart', {}).then(function (res) {
      if (res.code === 0) {
        that.setData({
          cartGoods: res.data.cartList,
          cartTotal: res.data.cartTotal
        });
      }

      that.setData({
        checkedAllStatus: that.isCheckedAll(),
        isEditCart:false
      });

      // that.getFootprintList();
    });
  },
  isCheckedAll: function () {
    //判断购物车商品已全选
    return this.data.cartGoods.every(function (element, index, array) {
      if (element.checked == true) {
        return true;
      } else {
        return false;
      }
    });
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  checkedItem: function (event) {
    let itemIndex = event.target.dataset.itemIndex;
    let that = this;

    if (!this.data.isEditCart) {
      util.request('cart/checked', {
        id: that.data.cartGoods[itemIndex].id,
        isChecked: that.data.cartGoods[itemIndex].checked ? 0 : 1
      }, 'POST').then(function (res) {
        if (res.code === 0) {
          that.setData({
            cartGoods: res.cartList,
            cartTotal: res.cartTotal
          });
        }

        that.setData({
          checkedAllStatus: that.isCheckedAll()
        });
      });
    } else {
      //编辑状态
      let tmpCartData = this.data.cartGoods.map(function (element, index, array) {
        if (index == itemIndex) {
          element.checked = !element.checked;
        }

        return element;
      });

      that.setData({
        cartGoods: tmpCartData,
        checkedAllStatus: that.isCheckedAll(),
        'cartTotal.checkedGoodsCount': that.getCheckedGoodsCount()
      });
    }
  },
  getCheckedGoodsCount: function () {
    let checkedGoodsCount = 0;
    this.data.cartGoods.forEach(function (v) {
      if (v.checked === true) {
        checkedGoodsCount += v.number;
      }
    });
    return checkedGoodsCount;
  },
  checkedAll: function () {
    let that = this;
    if (!this.data.isEditCart) {
      var cartIds = this.data.cartGoods.map(function (v) {
        return v.id;
      });
      util.request('cart/checked', {
        cartIds: cartIds,
        isChecked: that.isCheckedAll() ? 0 : 1
      }, 'POST').then(function (res) {
        if (res.code === 0) {
          that.setData({
            cartGoods: res.cartList,
            cartTotal: res.cartTotal
          });
        }

        that.setData({
          checkedAllStatus: that.isCheckedAll()
        });
      });
    } else {
      //编辑状态
      let checkedAllStatus = that.isCheckedAll();
      let tmpCartData = this.data.cartGoods.map(function (v) {
        v.checked = !checkedAllStatus;
        return v;
      });

      that.setData({
        cartGoods: tmpCartData,
        checkedAllStatus: that.isCheckedAll(),
        'cartTotal.checkedGoodsCount': that.getCheckedGoodsCount()
      });
    }

  },
  editCart: function () {
    var that = this;
    if (this.data.isEditCart) {
      this.getCartList();
      this.setData({
        isEditCart: !this.data.isEditCart
      });
    } else {
      //编辑状态
      let tmpCartList = this.data.cartGoods.map(function (v) {
        v.checked = false;
        return v;
      });
      this.setData({
        editCartList: this.data.cartGoods,
        cartGoods: tmpCartList,
        isEditCart: !this.data.isEditCart,
        checkedAllStatus: that.isCheckedAll(),
        'cartTotal.checkedGoodsCount': that.getCheckedGoodsCount()
      });
    }

  },
  updateCart: function (goodsId, number, id) {
    let that = this;

    util.request('cart/update', {
      goodsId: goodsId,
      number: number,
      id: id
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        that.setData({
          cartGoods: res.cartList,
          cartTotal: res.cartTotal
        });
      }

      that.setData({
        checkedAllStatus: that.isCheckedAll()
      });
    });

  },
  changeNumber: function (event) {
    let itemIndex = event.target.dataset.itemIndex;
    let cartItem = this.data.cartGoods[itemIndex];
    let number = event.detail.value;
    cartItem.number = number;
    this.setData({
      cartGoods: this.data.cartGoods
    });
    this.updateCart(cartItem.goodsId, number, cartItem.id);
  },
  checkoutOrder: function () {
    //获取已选择的商品
    let that = this;

    var checkedGoods = that.data.cartGoods.filter(function (element, index, array) {
      if (element.checked == true) {
        return true;
      } else {
        return false;
      }
    });

    if (checkedGoods.length <= 0) {

      let options = {
        msg: '您还没有选择商品哦',
        duration: 2000,
        type: 'translucent'
      };
      utils.toast(options);
      return false;
    }

    wx.navigateTo({
      url: '../shopping/checkout/checkout'
    })
  },
  deleteCartByItem: function (event) {
    //获取已选择的商品
    let that = this;

    let cartIds = [event.target.dataset.cartId];
    let goodsName = event.target.dataset.goodsName;

    wx.showModal({
      title: '',
      content: '确定要删除' + goodsName + '？',
      success: function (res) {
        if (res.confirm) {
          util.request('cart/delete', {
            cartIds: cartIds.join(',')
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              that.setData({
                cartGoods: res.cartList,
                cartTotal: res.cartTotal
              });
            }

            that.setData({
              checkedAllStatus: that.isCheckedAll()
            });
          });
        }
      }
    })
  },
  deleteCart: function () {
    //获取已选择的商品
    let that = this;

    let cartSelectGoods = this.data.cartGoods.filter(function (element, index, array) {
      if (element.checked == true) {
        return true;
      } else {
        return false;
      }
    });

    if (cartSelectGoods.length <= 0) {
      return false;
    }

    let cartIds = cartSelectGoods.map(function (element, index, array) {
      if (element.checked == true) {
        return element.id;
      }
    });

    wx.showModal({
      title: '',
      content: '确定要删除选中的商品？',
      success: function (res) {
        if (res.confirm) {
          util.request('cart/delete', {
            cartIds: cartIds.join(',')
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              that.setData({
                cartGoods: res.cartList,
                cartTotal: res.cartTotal
              });
            }

            that.setData({
              checkedAllStatus: that.isCheckedAll()
            });
          });
        }
      }
    })
  }
})