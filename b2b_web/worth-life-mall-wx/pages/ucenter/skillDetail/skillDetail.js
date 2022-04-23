var util = require('../../../utils/util.js');

Page({
  data: {
    orderId: 0,
    orderInfo: {},
    orderGoods: [],
    handleOption: {},
    address: '',
    person: '',
    mobile: '',
    addressVo: {}
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.id
    });
    this.getOrderDetail();
    this.getAddressList()
  },
  getAddressList() {
    let that = this;
    util.request('address/list').then(function(res) {
      if (res.code === 0) {
        if(res.data && res.data.length>0){
          var addr;
         addr=res.data.find(item=>item.isDefault)
        that.setData({
          addressVo:addr || res.data[0]
        },()=>{
          that.setAddressVo()
        });
        }
      }
    });
  },
  getOrderDetail() {
    let that = this;
    util.request('order/detail', {
      orderId: that.data.orderId
    }).then(function (res) {
      if (res.code === 0) {
        let orderInfo = res.orderInfo;
        that.setData({
          person: orderInfo.consignee,
          mobile: orderInfo.mobile,
          address: (orderInfo.province || '') + (orderInfo.city || '') + (orderInfo.district || '') + (orderInfo.address || ''),
          orderInfo: orderInfo,
          handleOption: orderInfo.handleOption,
          orderGoods: res.orderGoods
        });
        that.payTimer();
      }
    });
  },
  chooseAddress() {
    // 未付款才能修改收货地址
    if (this.data.orderInfo.orderStatus === -1) {
      wx.navigateTo({
        url: '/pages/shopping/address/address',
      })
    }
  },
  goMiniProgram() {
    let that = this;
    wx.navigateToMiniProgram({
      appId: 'wx6885acbedba59c14',
      path: `pages/result/result?nu=${that.data.orderInfo.shippingNo}&querysource=third_xcx`,
      envVersion: 'release',
      success(res) {
      }
    })
  },
  postComment() {
    let that = this;
    wx.navigateTo({
      url: '/pages/commentPost/commentPost?type=0' + '&orderId=' + that.data.orderId,
    })
  },
  lookComment() {
    let that = this;
    wx.navigateTo({
      url: '/pages/comment/comment?type=0' + '&orderId=' + that.data.orderId,
    })
  },
  goodsDetail(event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  payTimer() {
    let that = this;
    let orderInfo = that.data.orderInfo;
    if ((orderInfo.payStatus == 1 || orderInfo.payStatus == 2) && orderInfo.orderStatus === -1) {
      util.countdown(that, orderInfo, 'orderInfo')
    }
  },
  cancelOrder() {
    let that = this;
    let orderInfo = that.data.orderInfo;

    var orderStatus = orderInfo.orderStatus;

    var errorMessage = '';
    switch (orderStatus) {
      case 300: {
        errorMessage = '订单已发货';
        break;
      }
      case 301: {
        errorMessage = '订单已收货';
        break;
      }
      case 100: {
        errorMessage = '订单已过期';
        break;
      }
      case 101: {
        errorMessage = '订单已取消';
        break;
      }
      case 102: {
        errorMessage = '订单已删除';
        break;
      }
      case 401: {
        errorMessage = '订单已退款';
        break;
      }
      case 402: {
        errorMessage = '订单已退货';
        break;
      }
    }

    if (errorMessage != '') {
      util.showMsg(errorMessage);
      return false;
    }

    wx.showModal({
      title: '',
      content: '确定要取消此订单？',
      success: function (res) {
        if (res.confirm) {

          util.request('order/cancelOrder', {
            orderId: orderInfo.id
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              wx.showModal({
                title: '提示',
                content: res.msg,
                showCancel: false,
                confirmText: '继续',
                success: function (res) {
                  // wx.navigateTo({
                  //   url: 'pages/ucenter/order/order',
                  // });
                  wx.navigateBack()
                }
              });
            }
          });

        }
      }
    });
  },
  payOrder() {
    let that = this;
    if (that.data.address) {
      wx.navigateTo({
        url: '/pages/pay/pay?orderId=' + that.data.orderInfo.id + '&actualPrice=' + that.data.orderInfo.actualPrice,
      })
    } else {
      util.showMsg('请选择收货地址');
    }
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.setAddressVo()
  },
  setAddressVo:function(){
    let that = this;
    if (that.data.addressVo.id) {
      that.setData({
        person: that.data.addressVo.userName,
        mobile: that.data.addressVo.mobile,
        address: that.data.addressVo.cityName + that.data.addressVo.countyName + that.data.addressVo.detailInfo
      })
      util.request('order/updateOrderAddress', {
        orderId: that.data.orderId,
        consignee: that.data.addressVo.userName,
        province: that.data.addressVo.provinceName,
        city: that.data.addressVo.cityName,
        district: that.data.addressVo.countyName,
        address: that.data.addressVo.detailInfo,
        mobile: that.data.addressVo.mobile
      }, 'POST').then(function (res) {
        if (res.code === 0) {
          util.showMsg('操作成功');
          that.getOrderDetail();
        }
      });
    }
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  confirmOrder() {
    let that = this;
    util.request('order/confirmOrder', {
      orderId: that.data.orderId
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        util.showMsg('订单完成');
        setTimeout(function () {
          wx.redirectTo({
            url: '/pages/ucenter/order/order',
          });
        }, 2000);
      }
    });
  }
})
