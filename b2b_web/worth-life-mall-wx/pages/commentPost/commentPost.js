var util = require('../../utils/util.js');

Page({
  data: {
    type: 0,
    evalLevel: 5, //服务态度
    deliveryLevel: 5, //配送速度
    goodsList: [{
      goodsLevel: 5,
      comment: '',
      pics: []
    }, {
      goodsLevel: 5,
      comment: '',
      pics: []
    }],
    orderId: 0,
    orderInfo: {}, // 订单详情
    orderGoods: {}, // 订单商品
    handleDesc: ['很差', '一般', '满意', '很满意', '非常满意']
  },
  handleScore(e) {
    let aim = e.currentTarget.dataset.aim;
    let goodsLevel = e.currentTarget.dataset.goodsLevel;
    let obj = {};
    obj[aim] = goodsLevel;
    this.setData(obj);
  },
  previewPic(e) {
    let urls = e.currentTarget.dataset.urls;
    wx.previewImage({
      urls
    })
  },
  handleDelete(e) {
    let that = this;
    let src = e.currentTarget.dataset.src;
    let goodsIndex = e.currentTarget.dataset.goodsIndex;
    let pics = e.currentTarget.dataset.pics;
    pics = pics.filter(item => item != src);
    //
    let goodsList = that.data.goodsList;
    goodsList[goodsIndex].pics = pics;
    this.setData({
      goodsList: goodsList
    });
  },
  bindInpuntValue(event) {
    let that = this;
    let goodsIndex = event.target.dataset.goodsIndex;
    let value = event.detail.value;
    let goodsList = that.data.goodsList;

    goodsList[goodsIndex].comment = value.length>140? value.substring(0,140):value
    this.setData({
      goodsList: goodsList
    })
  },
  chooseImageTap: function(e) {
    let that = this;
    let goodsIndex = e.currentTarget.dataset.goodsIndex;
    let pics = e.currentTarget.dataset.pics;
    wx.showActionSheet({
      itemList: ['从相册中选择', '拍照'],
      itemColor: "#f7982a",
      success: function(res) {
        if (!res.cancel) {
          if (res.tapIndex == 0) {
            that.chooseWxImage('album', goodsIndex, pics)
          } else if (res.tapIndex == 1) {
            that.chooseWxImage('camera', goodsIndex, pics)
          }
        }
      }
    })
  },
  chooseWxImage: function(type, goodsIndex, pics) {
    let _this = this;
    wx.chooseImage({
      sizeType: ['original', 'compressed'],
      sourceType: [type],
      success: function(res) {
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url: util.API_BASE_URL + 'upload/upload',
          filePath: tempFilePaths[0],
          name: 'file',
          header: {
            'content-type': 'multipart/form-data',
            'token': wx.getStorageSync('token')
          },
          success: function(res) {
            var pic = JSON.parse(res.data);
            pics.unshift(pic.url);
            let goodsList = _this.data.goodsList;
            goodsList[goodsIndex].pics = pics;
            _this.setData({
              goodsList: goodsList
            })
          }
        })
      }
    })
  },
  onLoad: function(options) {
    var that = this;
    that.setData({
      type: options.type,
      orderId: options.orderId
    });
    that.getOrderDetail();
  },
  onClose() {
    wx.navigateBack({
      delta: 1,
      fail: (res) => {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    });
  },
  onPost() {
    let that = this;
    util.request('comment/post', {
      orderId: that.data.orderId,
      goodsList: that.data.goodsList,
      evalLevel: that.data.evalLevel,
      deliveryLevel: that.data.deliveryLevel,
      type: that.data.type
    }, 'POST').then(function(res) {
      if (res.code === 0) {
        util.showMsg('评论成功');
        setTimeout(function() {
          wx.navigateBack({
            delta: 2,
            fail: (res) => {
              wx.switchTab({
                url: '/pages/index/index',
              })
            }
          });
        }, 2000);
      }
      console.log(res)
    });
  },
  couponClickBack: function() {
    wx.navigateBack({
      delta: 2,
      fail: (res) => {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    });
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  },
  getOrderDetail() {
    let that = this;
    util.request('order/detail', {
      orderId: that.data.orderId
    }).then(function(res) {
      if (res.code === 0) {
        that.setData({
          orderInfo: res.orderInfo,
          orderGoods: res.orderGoods,
        });
        var goodsList = new Array();
        for (var i = 0; i < res.orderGoods.length; i++) {
          let goodVo = {};
          goodVo.goodsId = res.orderGoods[i].goodsId;
          goodVo.goodsName = res.orderGoods[i].goodsName;
          goodVo.listPicUrl = res.orderGoods[i].listPicUrl;
          goodVo.skuId = res.orderGoods[i].skuId;
          goodVo.goodsSpecifitionNameValue = null != res.orderGoods[i].goodsSpecifitionNameValue ? res.orderGoods[i].goodsSpecifitionNameValue : "";
          goodVo.goodsLevel = 5;
          goodVo.pics = [];
          goodVo.comment = '';
          goodsList.push(goodVo);
        }
        that.setData({
          goodsList: goodsList
        });
      }
    });
  }
})
