var util = require('../../../utils/util.js');

// 实例化API核心类
Page({
  data: {
    addressList: [],
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.getAddressList();
  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
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
    util.request('address/list').then(function(res) {
      if (res.code === 0) {
        that.setData({
          addressList: res.data
        });
      }
    });
  },
  addressAddOrUpdate(event) {

    var addressId = event.currentTarget.dataset.addressId;
    try {
      wx.navigateTo({
        url: '/pages/shopping/addressAdd/addressAdd?id=' + addressId,
        success: function(res) {},
        fail: function(res) {}
      })
    } catch (e) {}
  },
  selectAddress(event) {
    var that = this;
    var addressId = event.currentTarget.dataset.addressId;

    try {
      wx.setStorageSync('addressId', addressId);
    } catch (e) {

    }
    var selectAddressVo = that.data.addressList.filter(function(v) {
      if (v.id == addressId) {
        return true;
      } else {
        return false;
      }
    }).map(function(v) {
      return v;
    });

    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1];  //当前页面
    var prevPage = pages[pages.length - 2]; //上一个页面

    //直接调用上一个页面的setData()方法，把数据存到上一个页面中去
    prevPage.setData({
      addressVo: selectAddressVo[0]
    })

    wx.navigateBack({
      fail() {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    })
  },
  getWtAddress() {
    let that = this;
    wx.chooseAddress({
      success: function(res) {
        util.request('address/syncAddress', {
          userName: res.userName,
          mobile: res.telNumber,
          provinceName: res.provinceName,
          cityName: res.cityName,
          countyName: res.countyName,
          detailInfo: res.detailInfo,
          postalCode: res.postalCode,
          nationalCode: res.nationalCode,
          latitude: res.latitude,
          longitude: res.longitude
        }, 'POST').then(function(res) {
          if (res.code === 0) {
            that.getAddressList();
          }
        });
      }
    })
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  }
})
