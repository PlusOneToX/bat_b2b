var util = require('../../utils/util.js');

Page({
  data: {
    comments: [],
    picCommentList: [],
    type: 0,
    goodsId: '',
    orderId: '',
    showType: 0,
    goodsCount: 0,
    picCount: 0,
    allPage: 1,
    picPage: 1,
    limit: 20
  },
  getCommentCount: function() {
    let that = this;
    util.request('comment/count', {
      goodsId: that.data.goodsId,
      type: that.data.type,
      orderId: that.data.orderId
    }).then(function(res) {
      if (res.code === 0) {
        that.setData({
          goodsCount: res.goodsCount,
          picCount: res.picCount
        });
      }
    });
  },
  getCommentList: function() {
    let that = this;
    util.request('comment/list', {
      goodsId: that.data.goodsId,
      type: that.data.type,
      orderId: that.data.orderId,
      limit: that.data.limit,
      current: (that.data.showType == 0 ? that.data.allPage : that.data.picPage),
      showType: that.data.showType
    }).then(function(res) {
      if (res.code === 0) {
        if (that.data.showType == 0) {
          that.setData({
            allPage: res.data.pages,
            comments: res.data.records
          });
        } else {
          that.setData({
            picPage: res.data.pages,
            comments: res.data.records
          });
        }
      }
    });
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options.type) {
      this.setData({
        type: options.type
      });
    }
    if (options.goodsId) {
      this.setData({
        goodsId: options.goodsId,
      });
    }
    if (options.orderId) {
      this.setData({
        orderId: options.orderId
      });
    }
    this.getCommentCount();
    this.getCommentList();
  },
  onReady: function() {
    // 页面渲染完成

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
  switchTab: function() {
    this.setData({
      showType: this.data.showType == 1 ? 0 : 1
    });

    this.getCommentList();
  },
  onReachBottom: function() {
    if (this.data.showType == 0) {

      if (this.data.goodsCount / this.data.limit < this.data.allPage) {
        return false;
      }

      this.setData({
        'allPage': this.data.allPage + 1
      });
    } else {
      if (this.data.picCount / this.data.limit < this.data.picPage) {
        return false;
      }

      this.setData({
        'picPage': this.data.picPage + 1
      });
    }
    this.getCommentList();
  },
  previewPic(e) {
    let url = e.currentTarget.dataset.url;
    let urls = [];
    urls[0] = url;
    wx.previewImage({
      urls
    })
  }
})