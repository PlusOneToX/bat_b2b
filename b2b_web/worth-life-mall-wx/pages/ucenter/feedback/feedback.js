var util = require('../../../utils/util.js');

Page({
  data: {
    array: ['请选择反馈类型', '商品相关', '物流状况', '客户服务', '优惠活动', '功能异常', '产品建议', '其他'],
    feedType: 0,
    content: '',
    contentLength: 0,
    mobile: ''
  },
  bindPickerChange: function(e) {
    this.setData({
      feedType: e.detail.value
    });
  },
  mobileInput: function(e) {
    let that = this;
    this.setData({
      mobile: e.detail.value,
    });
  },
  contentInput: function(e) {

    let that = this;
    this.setData({
      contentLength: e.detail.cursor,
      content: e.detail.value,
    });
  },
  sbmitFeedback: function(e) {
    let that = this;
    if (that.data.feedType == 0) {
      util.showMsg('请选择反馈类型');
      return false;
    }

    if (that.data.content == '') {
      util.showMsg('请输入反馈内容');
      return false;
    }
    
    if (!util.isMobile(that.data.mobile)) {
      util.showMsg('请输入正确的手机号码');
      return false;
    }
    util.request('user/saveFeedBack', {
      mobile: that.data.mobile,
      feedType: that.data.feedType,
      content: that.data.content
    }, 'POST').then(function(res) {
      if (res.code === 0) {
        wx.showModal({
          content: '我们已收到您的反馈！',
          showCancel: false,
          success: function (res) {
            if (res.confirm) {
              wx.switchTab({
                url: '/pages/ucenter/index/index'
              })
            }
          }
        })
      } else {
        util.showMsg(res.data);
      }

    });
  },
  onLoad: function(options) {},
  onReady: function() {

  },
  onShow: function() {

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  }
})
