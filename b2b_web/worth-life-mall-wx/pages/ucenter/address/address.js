var util = require('../../../utils/util.js');

Page({
  data: {
    addressList: [],
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数

  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.getAddressList();
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getAddressList();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  getAddressList() {
    let that = this;
    util.request('address/list').then(function (res) {
      if (res.code === 0) {
        that.setData({
          addressList: res.data
        });
      }
    });
  },
  addressAddOrUpdate(event) {
    wx.navigateTo({
      url: '/pages/ucenter/addressAdd/addressAdd?id=' + event.currentTarget.dataset.addressId
    })
  },
  deleteAddress(event) {
    let that = this;
    wx.showModal({
      title: '',
      content: '确定要删除地址？',
      success: function (res) {
        if (res.confirm) {
          let addressId = event.target.dataset.addressId;
          util.request('address/delete', {
            id: addressId
          }, 'POST').then(function (res) {
            if (res.code === 0) {
              that.getAddressList();
            }
          });
        }
      }
    })
    return false;

  },
  getWtAddress() {
    let that = this;
    wx.chooseAddress({
      success: function (res) {
        that.syncAddress(res);
      },
      fail: function (res) {
        wx.getSetting({
          success(res) {
            if (!res.authSetting['scope.address']) {
              wx.openSetting({
                success: (res) => {
                  wx.chooseAddress({
                    success: function (res) {
                      that.syncAddress(res);
                    }
                  })
                }
              })
            }
          }
        })
      }
    })
  },
  syncAddress: function (res) {
    let that = this;
    util.request('address/syncAddress', {
      userName: res.userName,
      mobile: res.telNumber,
      provinceName: res.provinceName,
      cityName: res.cityName,
      countyName: res.countyName,
      detailInfo: res.detailInfo,
      postalCode: res.postalCode,
      nationalCode: res.nationalCode
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        that.getAddressList();
      }
    });
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  }
})