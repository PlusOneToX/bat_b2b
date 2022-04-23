const util = require('../../utils/util.js');

//获取应用实例
const app = getApp();
Page({
  data: {
    goodsCount: 0,
    newGoods: [],
    hotGoods: [],
    seckillGoods: [],
    banner: [],
    brand: [],
    channel: [],
    topics: [],
    notice: '',
    shopsList: [],
    latitude: '',
    longitude: '',
    text: '距结束',
    tmplIds: [],
    hotKeyword: [],
    scrollTop: 0
  },
  onShareAppMessage: function () {
    return {
      title: '商业版',
      desc: '商业版',
      path: '/pages/index/index'
    }
  },
  onShareTimeline: function () {
    return {
      title: '价值人生酒',
      path: '/pages/index/index'
    }
  },
  //页面滚动执行方式
  onPageScroll(e) {
    this.setData({
      scrollTop: e.scrollTop
    })
  },
  //todo 订单列表页面使用有bug
  countdown: function (index) {

    let that = this;
    var seckillGood = that.data.seckillGoods[index]
    
    let EndTime = seckillGood.endTime || [];
    let StartTime = seckillGood.startTime || [];
    let NowTime = new Date().getTime();
    let para = {};

    let willEnd = false;
    if (Date.parse(StartTime.replace(/-/g, '/')) < new Date()) {
      para[`seckillGoods[${index}].text`] = '距结束：'
      willEnd = true;
    } else if (Date.parse(StartTime.replace(/-/g, '/')) > new Date()) {
      para[`seckillGoods[${index}].text`] = '距开始：'
      EndTime = StartTime
    }

    //IOS系统直接使用new Date('2018-10-29 11:25:21')，在IOS上获取不到对应的时间对象。
    let total_micro_second = Date.parse(EndTime.replace(/-/g, '/')) - NowTime || [];


    para[`seckillGoods[${index}].dateformat`] = util.dateformat(total_micro_second)


    if (total_micro_second <= 0 && willEnd) {
      para[`seckillGoods[${index}].show`] = false
      this.setData(para)
      return;
    }
    this.setData(para)
       setTimeout(function () {
      total_micro_second -= 1000;
      that.countdown(index);
    }, 1000)
  },
  setTimeoutCount: function () {
    //获取待付款倒计时
    var that=this;
    that.data.seckillGoods.forEach((item, num) => {
      that.setData({
        [`seckillGoods[${num}].show`]:true,
        [`seckillGoods[${num}].btn`]:item.stock > 0?true:false,
      })
      that.countdown(num)
    })
  },
  startSeckill: function (e) {
    let that = this;
    wx.requestSubscribeMessage({
      tmplIds: that.data.tmplIds,
      success(res) {

      },
      fail(res) {

      },
      complete() {
        util.request('seckill/startSeckill', {
          seckillId: e.currentTarget.dataset.id,
          fromType: 1
        }, 'POST', 'application/json').then(res => {
          if (res.code === 0) {
            //存储用户信息
            util.showMsg(res.msg);
            wx.navigateTo({
              url: '/pages/ucenter/skill/skill',
            })
          } else {
            util.showMsg(res.msg);
          }
        });
      }
    })
  },
  getIndexData: function () {
    wx.setStorageSync("navUrl", "/pages/index/index");
    let that = this;
    util.request('index/bannerList').then(function (res) {
      if (res.code === 0) {
        that.setData({
          banner: res.data
        });
      }
    });
    // util.request('index/channelList').then(function (res) {
    //   if (res.code === 0) {
    //     that.setData({
    //       channel: res.data
    //     });
    //   }
    // });
    // util.request('index/bulletinList').then(function (res) {
    //   if (res.code === 0) {
    //     let notice = '';
    //     for (let i = 0; i < res.data.length; i++) {
    //       notice += '   ' + (i + 1) + '：' + res.data[i].title + '；'
    //     }
    //     that.setData({
    //       notice: notice
    //     });
    //   }
    // });
    util.request('search/hotKeyword').then(function(res) {
      if (res.code === 0) {
        that.setData({
          hotKeyword: res.hotKeywordList
        });
      }
    });
    util.request('topic/list').then(res => {
      if (res.code === 0) {
        that.setData({
          topics: res.data.records
        });
      }
    });
    // util.request('brand/brandList').then(function (res) {
    //   if (res.code === 0) {
    //     that.setData({
    //       brand: res.data.records
    //     });
    //   }
    // });
    util.request('goods/count').then(function (res) {
      that.setData({
        goodsCount: res.data
      });
    });
    util.request('goods/list', {
      type: 'IS_HOT'
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          hotGoods: res.data.records
        });
      }
    });
    // util.request('seckill/list').then(function (res) {
    //   if (res.code === 0) {
    //     that.setData({
    //       seckillGoods: res.data
    //     },()=>{
    //       that.setTimeoutCount();
    //     });
    //   }
    // });
    // util.request('goods/list', {
    //   type: 'IS_NEW'
    // }).then(function (res) {
    //   if (res.code === 0) {
    //     that.setData({
    //       newGoods: res.data.records
    //     });
    //   }
    // });
    // util.request('shops/shopsList').then(function (res) {
    //   let shopsList = res.data;
    //   shopsList.forEach(function (item) {
    //     item.distant = util.getDistance(that.data.latitude, that.data.longitude, item.latitude, item.longitude)
    //   })
    //   that.setData({
    //     shopsList: shopsList
    //   });
    // });
  },

  onLoad: function (options) {
    this.findXy();
    this.getIndexData();
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    let that = this;
    util.request('index/getTemplateId', {
      templateTypes: '4,5'
    }).then(res => {
      if (res.code === 0) {
        that.setData({
          tmplIds: res.data
        })
      }
    });
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    this.getIndexData();
    
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  navToGoodsDetail: function (e) {
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + e.currentTarget.dataset.id
    });
  },
  navToLiveRoom: function (e) {
    wx.navigateTo({
      url: `plugin-private://wx2b03c6e691cd7370/pages/live-player-plugin?room_id=` + e.currentTarget.dataset.id
    })
  },
  navToBrandDetail: function (e) {
    wx.navigateTo({
      url: '/pages/brandDetail/brandDetail?id=' + e.currentTarget.dataset.id
    });
  },
  toLink: function (e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url
    });
  },
  goHere: function (e) {
    wx.openLocation({
      name: e.currentTarget.dataset.name,
      address: e.currentTarget.dataset.address,
      latitude: parseFloat(e.currentTarget.dataset.latitude),
      longitude: parseFloat(e.currentTarget.dataset.longitude)
    })
  },
  // 获取用户的经纬度
  findXy: function () {
    let that = this
    wx.getLocation({
      type: 'gcj02',
      success(res) {
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude
        })
        let shopsList = that.data.shopsList;
        shopsList.forEach(function (item) {
          item.distant = util.getDistance(that.data.latitude, that.data.longitude, item.latitude, item.longitude)
        })
        that.setData({
          shopsList: shopsList
        });
      }
    })
  },
  //放大预览轮播图片
  previewImg: function (e) {
    let curImg = e.currentTarget.dataset.img;
    let banner = this.data.banner;
    if (!util.isEmpty(curImg) && banner.length > 0) {
      let imgsArr = [];
      for (let i = 0; i < banner.length; i++) {
        imgsArr[i] = banner[i].imageUrl;
      }
      wx.previewImage({
        current: curImg, // 当前显示图片的http链接
        urls: imgsArr, // 需要预览的图片http链接列表
      })
    }
  },
  shopsJump: function (e) {
    wx.navigateTo({
      url: '/pages/shops/shops?shopsSn=' + e.target.dataset.shopsSn,
    })
  },
  channel: function (e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url
    })
  },
  openCall: function (e) {
    wx.makePhoneCall({
      phoneNumber: e.target.dataset.phone
    })
  },
  search: function (e) {
    wx.navigateTo({
      url: '/pages/search/search?keyword='+ e.currentTarget.dataset.keyword
    })
  },
  newGoods: function () {
    wx.navigateTo({
      url: '/pages/newGoods/newGoods'
    })
  },
  hotGoods: function (e) {
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + e.currentTarget.dataset.id
    })
  },
  hotGoodss: function (e) {
    wx.navigateTo({
      url: '/pages/hotGoods/hotGoods'
    })
  },
  category: function () {
    wx.switchTab({
      url: '/pages/catalog/catalog'
    })
  }
})
