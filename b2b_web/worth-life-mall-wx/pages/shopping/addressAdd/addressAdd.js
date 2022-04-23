var util = require('../../../utils/util.js');
// 引入SDK核心类
var QQMapWX = require('../../../lib/qqmap/qqmap-wx-jssdk.js');
// https://apis.map.qq.com
var app = getApp();

// 实例化API核心类
var qqMap;
Page({
  data: {
    address: {
      id: '',
      provinceName: '安徽省',
      cityName: '合肥市',
      countyName: '蜀山区',
      provinceId: 0,
      cityId: 0,
      countyId: 0,
      detailInfo: '',
      userName: '',
      mobile: '',
      isDefault: 0
    },
    addressId: 0,
    region: ['安徽省', '合肥市', '蜀山区']
  },
  bindinputTelNumber(event) {
    let address = this.data.address;
    address.mobile = event.detail.value;
    this.setData({
      address: address
    });
  },
  bindinputDetailInfo(event) {
    let address = this.data.address;
    address.detailInfo = event.detail.value;
    this.setData({
      address: address
    });
  },
  bindinputUserName(event) {
    let address = this.data.address;
    address.userName = event.detail.value;
    this.setData({
      address: address
    });
  },
  bindIsDefault() {
    let address = this.data.address;
    if(address.isDefault === 1){
      address.isDefault = 0;
    } else {
      address.isDefault = 1;
    }
    this.setData({
      address: address
    });
  },
  getAddressDetail() {
    let that = this;
    util.request('address/detail', {
      id: that.data.addressId
    }).then(function(res) {
      if (res.code === 0 && null != res.data) {
        that.setData({
          address: res.data
        });
      }
      that.initRegion();
    });
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options.id && options.id != "0") {
      this.setData({
        addressId: options.id
      });
      this.getAddressDetail();
    }

    this.initRegion();
  },
  onReady: function() {

  },
  cancelAddress() {
    wx.navigateBack({
      fail() {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    })
  },
  saveAddress() {
    let address = this.data.address;

    if (address.userName.trim() == '') {
      util.showMsg('请输入姓名');
      return false;
    }

    if (!util.isMobile(address.mobile)) {
      util.showMsg('请输入正确的手机号码');
      return false;
    }


    if (address.countyName == 0) {
      util.showMsg('请输入省市区');
      return false;
    }

    if (address.detailInfo == '') {
      util.showMsg('请输入详细地址');
      return false;
    }


    let that = this;
    util.request('address/saveOrUpdate', {
      id: address.id,
      userName: address.userName.trim(),
      mobile: address.mobile,
      provinceName: address.provinceName,
      cityName: address.cityName,
      countyName: address.countyName,
      detailInfo: address.detailInfo,
      isDefault: address.isDefault,
      latitude: address.latitude,
      longitude: address.longitude,
    }, 'POST').then(function(res) {
      wx.setStorageSync('addressId', res.data.id);
      if (res.code === 0) {
        var pages = getCurrentPages();
        var currPage = pages[pages.length - 1];  //当前页面
        var prevPage = pages[pages.length - 2]; //上一个页面
        wx.navigateBack({
          fail() {
            wx.switchTab({
              url: '/pages/index/index',
            })
          }
        })
      }
    });

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
  /**
   * 初始化省市区
   */
  initRegion() {
    let that = this;
    if (null == that.data.addressId || that.data.addressId == 0) {

      wx.getLocation({
        success: function(res) {
          // 调用接口
          qqMap = new QQMapWX({
            key: app.globalData.qqMapKey
          })
          qqMap.reverseGeocoder({
            location: {
              latitude: res.latitude,
              longitude: res.longitude
            },
            success: function(rs) {
              var address = that.data.address;
              address.provinceName = rs.result.address_component.province;
              address.cityName = rs.result.address_component.city;
              address.countyName = rs.result.address_component.district;
              address.detailInfo = rs.result.address_component.street + rs.result.formatted_addresses.recommend
              that.setData({
                address: address
              });
              if(address.provinceName && address.cityName && address.countyName){
                that.setData({
                  region: [address.provinceName, address.cityName, address.countyName]
                });
              }
            },
            fail: function(rs) {},
            complete: function(rs) {}
          });
        },
        fail: function (rs) {
          var address = that.data.address;
          address.provinceName = "安徽省";
          address.cityName = "合肥市";
          address.countyName = "蜀山区";
          that.setData({
            address: address
          });
          if(address.provinceName && address.cityName && address.countyName){
            that.setData({
              region: [address.provinceName, address.cityName, address.countyName]
            });
          }
        }
      })
    } else {
      let address = that.data.address;

      that.setData({
        address: address
      });
      if(address.provinceName && address.cityName && address.countyName){
        that.setData({
          region: [address.provinceName, address.cityName, address.countyName]
        });
      }
    }
  },
  bingAddressTap: function() {
    var that = this;
    wx.chooseLocation({
      success: function(res) {
        let address = that.data.address;
        if (null == res.latitude) {
          return;
        }
        // 解析省市区
        var regex = /^(北京市|天津市|重庆市|上海市|香港特别行政区|澳门特别行政区)/;
        let province = [];
        if (!(province = regex.exec(res.address))) {
          regex = /^(.*?(省|自治区))(.*?)$/;
          province = regex.exec(res.address);
          address.provinceName = province[1];
          address = that.regexAddress(province[3], address, res.name);
        } else {
          address.provinceName = province[1];
          address = that.regexAddress(res.address, address, res.name);
        }
        address.full_region = address.provinceName + address.cityName + address.countyName;
        address.latitude = res.latitude;
        address.longitude = res.longitude;
        that.setData({
          address: address
        });
        that.setData({
          region: [that.data.address.provinceName, that.data.address.cityName, that.data.address.countyName]
        });
      },
      fail:function(err){
        console.log(err);
        if(err.errMsg=="chooseLocation:fail auth deny"){
          wx.showToast({
            title: '请前往设置，开启授权',
            icon: 'none',
            duration:2000
          })
        }
      }
    })
  },
  regexAddress: function(address, addressBean, name) {
    var regex = /^(.*?[市州]|.*?地区|.*?特别行政区)(.*?[市区县])(.*?)$/g;
    var addxress = regex.exec(address);
    addressBean.cityName = addxress[1];
    addressBean.countyName = addxress[2];
    addressBean.detailInfo = addxress[3] + "(" + name + ")";
    return addressBean;
  },
  bindRegionChange: function(e) {
    console.log(e);
    var addr = this.data.address
    addr.provinceName = e.detail.value[0]
    addr.cityName = e.detail.value[1]
    addr.countyName = e.detail.value[2]
    this.setData({
      address: addr
    })
  }
})
