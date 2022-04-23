const util = require('../../utils/util.js');

const app = getApp()
Page({
  data: {
    height: 64, //header高度
    top: 0, //标题图标距离顶部距离
    scrollH: 0, //滚动总高度
    opcity: 0,
    iconOpcity: 0.5,
    // text:"这是一个页面"
    rooms: []
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.getRooms();
    this.setData({
      width: app.globalData.customBar.width,
      height: app.globalData.customBar.height,
      top: app.globalData.customBar.top,
      scrollH: app.globalData.customBar.scrollH
    })
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  onPageScroll(e) {
    let scroll = e.scrollTop <= 0 ? 0 : e.scrollTop;
    let opcity = scroll / this.data.scrollH;
    if (this.data.opcity >= 1 && opcity >= 1) {
      return;
    }
    this.setData({
      opcity: opcity,
      iconOpcity: 0.5 * (1 - opcity < 0 ? 0 : 1 - opcity)
    })
  },
  getRooms: function () {
    let that = this;
    that.setData({
      rooms: []
    });
    util.request('live/roomList').then(res => {
      if (res.code === 0) {
        let roomList = res.data.records;
        roomList.forEach(function (item) {
          item.endTime = util.transDate(item.endTime, 'yyyy-MM-dd hh:mm:ss')
          item.startTime = util.transDate(item.startTime, 'yyyy-MM-dd hh:mm:ss')
        })
        that.setData({
          rooms: roomList
        });
      }
    });
  },
  livePlayer:function(e){
    wx.navigateTo({
      url: 'plugin-private://wx2b03c6e691cd7370/pages/live-player-plugin?room_id=' + e.currentTarget.dataset.id,
    })
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getRooms();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  onShareAppMessage: function (e) {
    let index = e.target.dataset.index;
    let title = this.data.rooms[index].name;
    let roomid = this.data.rooms[index].roomid;
    let coverImg = this.data.rooms[index].coverImg;
    return {
      title: title,
      imageUrl: coverImg,
      path: 'plugin-private://wx2b03c6e691cd7370/pages/live-player-plugin?room_id=' + roomid
    }
  }
})