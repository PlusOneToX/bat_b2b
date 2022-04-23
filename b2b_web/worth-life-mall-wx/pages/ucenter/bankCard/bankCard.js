var util = require('../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cardList: [],
    unbindingId: '',
    unbindingShow: false,
  },
  goBinding() {
    wx.navigateTo({
      url: '/pages/ucenter/bindingBank/bindingBank',
    })
  },
  getCardList: function () {
    var that = this;
    util.request('user/getBankCard').then(function (res) {
      if (res.code === 0) {
        that.setData({
          cardList: res.data
        })
      }
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onShow: function (options) {
    this.getCardList()
  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    this.getCardList()

    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  showUnbinding: function (event) {
    let id = event.currentTarget.dataset.cardId
    if (id == null) {
      return false
    }
    this.setData({
      unbindingId: id,
      unbindingShow: true,
    })
  },
  cancelUnbinding: function () {
    this.setData({
      unbindingId: '',
      unbindingShow: false,
    })
  },
  unbinding: function () {
    let that = this
    let cardList = that.data.cardList
    let id = this.data.unbindingId
    util.request('user/unbindingCard', {
      id: id,
    }, 'POST').then(function (res) {
      if (res.code == 0) {
        wx.showToast({
          title: '解绑成功',
          icon: 'success',
          image: '',
          duration: 1000
        })

        that.getCardList()
      } else {
        util.showMsg(res.msg)
        return
      }
    });

    that.cancelUnbinding()
  }
})