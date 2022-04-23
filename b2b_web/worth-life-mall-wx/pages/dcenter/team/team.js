const util = require('../../../utils/util.js')

//获取应用实例
const app = getApp()

Page({

  data: {
    activeId: 0,
    teamListFL:[], //一级团队列表 
    teamListSL:[], //二级团队列表
    current: 1,
    limit: 100000,
    pages: 1,
  },
  onShow: function () {
    this.initial();
  },
  initial: function () {
    let that = this;
    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');

    // 页面显示
    if (userInfo && token) {
      app.globalData.userInfo = userInfo;
      app.globalData.token = token;
    }
    that.getTeamList("FL");
  },
  // 获取团队列表
  getTeamList: function (type) {
    let that = this;
    util.request('distributor/myDistTeam', {
      type: type,
      id: that.data.activeId,
      current: 1,
      limit: that.data.limit * that.data.current,
    }).then(function(res) {
      if (res.code === 0) {
        if (type == 'FL') {
          that.setData({
            teamListFL: res.data.records
          });
        } else {
          that.setData({
            teamListSL: res.data.records
          });
        }
      }
    });
  },
  getSLTeam: function (event) {
    let id = event.currentTarget.dataset.id
    let teamCount = event.currentTarget.dataset.teamCount
    if (teamCount == 0) return false
    if (id == this.data.activeId) {
      this.setData({
        activeId: 0
      })
      return false
    }
    this.setData({
      activeId: id
    })
    this.getTeamList("SL");
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;
    
    that.setData({
      activeId: 0
    });

    that.getTeamList("FL");
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
})