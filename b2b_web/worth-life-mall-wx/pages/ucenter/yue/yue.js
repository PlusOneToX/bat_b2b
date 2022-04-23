var util = require('../../../utils/util.js');
const app = getApp()

Page({
  data: {
    nowMoney: '',
    mainArray: [],
    payFaceToFace: [],
    chongzhi:2
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      chongzhi: app.globalData.rechargeStatus
    })
    that.getMycount()
  },
  
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that=this;
    that.getUserInfo()

  },
  getUserInfo:function(){
    var that=this;
    util.request('user/userInfo').then(function (res) {
     that.setData({
      nowMoney: res.data.balance
    });
    })
  },
  getMycount() {
    var that = this;
    util.request('user/accountLogList').then(function (res) {
      if (res.code === 0) {
        that.setData({
          mainArray: res.data
        })
      }
    });
    util.request('user/payFaceToFaceList').then(function(res) {
      if (res.code === 0) {
        that.setData({
          payFaceToFace: res.data
        })
      }
    });
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getMycount();
    this.getUserInfo()
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  indexs: function () {
    wx.switchTab({
      url: '/pages/index/index',
    })
  },
  goPayment: function () {
    wx.navigateTo({
      url: '/pages/ucenter/chongzhi/chongzhi',
    })
  },
  goBankCard: function () {
    wx.navigateTo({
      url: '/pages/ucenter/bankCard/bankCard',
    })
  },
  carry:function(){
    wx.navigateTo({
      url: '/pages/ucenter/carry/carry',
    })
  },
  
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  }

})
