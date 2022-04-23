var util = require('../../../utils/util.js');

Page({
  data: {
    couponId: '',
    couponList: []
  },
  onLoad: function () {
    this.loadListData()
  },
  loadListData: function () {
    let that = this;
    util.request('coupon/couponList').then(function (res) {
      if (res.code === 0) {
        that.setData({
          couponList: res.data
        });
      }
    });
  },
  getCouponUser(e) {
    util.request('coupon/getCouponUser', {
      couponId: e.currentTarget.dataset.id
    }).then(res => {
      if (res.code === 0) {
        util.showMsg('领取成功');
        setTimeout(() => {
          var pages = getCurrentPages();
          var currPage = pages[pages.length - 1];  //当前页面
          var prevPage = pages[pages.length - 2]; //上一个页面
          prevPage.loadListData()
          wx.navigateBack({
            fail: (res) => {
              wx.switchTab({
                url: '/pages/index/index',
              })
            }
          })
        }, 1000);
      }
    });
  },
  onPullDownRefresh: function () {
    setTimeout(() => {
      this.setData({
        pullUpOn: true
      })
      wx.stopPullDownRefresh()
    }, 200);
  },
  onReachBottom: function () {
    if (!this.data.pullUpOn) return;
    this.setData({
      loadding: true
    })
    setTimeout(() => {
      this.setData({
        loadding: false,
        pullUpOn: false
      })
    }, 1000)
  }
})
