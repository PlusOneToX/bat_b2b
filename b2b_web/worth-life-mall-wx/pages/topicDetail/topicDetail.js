var WxParse = require('../../lib/wxParse/wxParse.js');
var util = require('../../utils/util.js');

Page({
  data: {
    id: 0,
    topic: {},
    topicList: [],
    commentCount: 0,
    commentList: []
  },
  onShareAppMessage: function() {
    var that = this;
    return {
      title: that.data.topic.title,
      desc: that.data.topic.subtitle,
      path: '/pages/topicDetail/topicDetail?id=' + that.data.topic.id
    }
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    var that = this;
    that.setData({
      id: options.id
    });

    util.request('topic/detail', { id: that.data.id}).then(function (res) {
      if (res.code === 0) {
        that.setData({
          topic: res.data,
        });
        WxParse.wxParse('topicDetail', 'html', res.data.content, that);
      }
    });

    util.request('topic/related', { id: that.data.id}).then(function (res) {
      if (res.code === 0) {
        that.setData({
          topicList: res.data.records
        });
      }
    });
  },
  getCommentList(){
    let that = this;
    util.request('comment/list', { valueId: that.data.id, type: 1, size: 5 ,showType: 0}).then(function (res) {
      if (res.code === 0) {
        that.setData({
          commentList: res.data.records,
          commentCount: res.data.total
        });
      }
    });
  },
  postComment (){
    wx.navigateTo({
      url: '/pages/commentPost/commentPost?orderId='+this.data.id + '&type=1',
    })
  },
  goodsDetail: function (e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url
    })
  },
  onReady: function () {

  },
  onShow: function () {
    // 页面显示
    this.getCommentList();
  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  }
})
