var util = require('../../../utils/util.js');

//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    height: 64, //header高度
    top: 0, //标题图标距离顶部距离
    scrollH: 0, //滚动总高度
    shopStatus:-1,//店铺认证状态
    opcity: 0,
    iconOpcity: 0.5,
    unPayNum: 0,
    unTakeNum: 0,
    unEvalNum: 0,
    unSendNum: 0,
    couponCount: 0,
    newGoods: [],
    curUser: {
      balance: 0,
      signAllIntegral: 0,
      signUsedIntegral: 0,
      isDistributor: false,
    },
    userInfo: {},
    bindPhoneText: '绑定手机',
    faceShow: false,
    money: '',
    shopsId: '',
    shopsList: []
  },
  onShareAppMessage: function () {
    return {
      title: '个人中心',
      desc: '个人中心',
      path: '/pages/ucenter/index/index'
    }
  },

  onLoad: function (options) {
    this.setData({
      width: app.globalData.customBar.width,
      height: app.globalData.customBar.height,
      top: app.globalData.customBar.top,
      scrollH: app.globalData.customBar.scrollH
    })
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
  onReady: function () {},
  closePayFace: function() {
    this.setData({
      money: '',
      faceShow: false
    })
  },
  payFace: function() {
    this.setData({
      faceShow: true
    })
  },
  getShopsList: function() {
    let that = this;
    util.request('shops/shopsList').then(function(res) {
      let shopsList = res.data;
      shopsList.forEach(function(item) {
        item.distant = util.getDistance(that.latitude, that.longitude, item.latitude, item.longitude)
      })
      that.setData({
        shopsList: shopsList
      })
    });
  },
  radioChange: function(evt) {
    for (let i = 0; i < this.data.shopsList.length; i++) {
      if (this.data.shopsList[i].id === evt.detail.value) {
        this.setData({
          shopsId: evt.detail.value
        })
        break;
      }
    }
  },
  pay: function() {
    let that = this;
    if (!wx.getStorageSync('token')) {
      util.showMsg('您还没登录哦');
      return;
    }
    if (!util.isMoney(that.data.money)) {
      util.showMsg('请输入正确的金额')
      return;
    }
    if (!that.data.shopsId) {
      util.showMsg('请选择门店')
      return;
    }
    util.faceToface(that.data.money, that.data.shopsId).then(res => {
      util.showMsg('付款成功')
    }).catch(res => {
      util.showMsg('付款失败')
    });
    this.setData({
      faceShow: false
    })
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

    that.setData({
      userInfo: app.globalData.userInfo,
    });

    util.request('user/userInfo').then(function (res) {
      that.setData({
        curUser: res.data
      });
      if (res.data.mobile) {
        that.setData({
          bindPhoneText: '修改密码'
        });
      }
      if (res.code === 0) {
        util.request('index/userCount', {}).then(function (res) {
          if (res.code === 0) {
            that.setData({
              unPayNum: res.countMap.unPayNum ? res.countMap.unPayNum : 0,
              unSendNum: res.countMap.unSendNum ? res.countMap.unSendNum : 0,
              unTakeNum: res.countMap.unTakeNum ? res.countMap.unTakeNum : 0,
              unEvalNum: res.countMap.unEvalNum ? res.countMap.unEvalNum : 0
            });
          }
          // that.getFootprintList()
        });
        util.request('coupon/userCount', {}).then(function (res) {
          if (res.code === 0) {
            that.setData({
              couponCount: res.data ? res.data : 0
            });
          }
        });
      }
    });
  },
  onShow: function () {
    this.initial();
    this.getShopsList();
    this.getSubMerchantInfo();
    wx.setStorageSync("navUrl", "/pages/ucenter/index/index");
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
    var that = this;
    this.getSubMerchantInfo();
    that.initial();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  // 获取店铺认证状态
  getSubMerchantInfo:function(){
    const that=this;
    util.request('user/getSubMerchantInfo').then(function (res) {
      that.setData({
        shopStatus: res.data ? res.data.status : -1
      })
    })
  },
  goLogin() {
    if (wx.getStorageSync('token')) {
      return;
    }
    wx.navigateTo({
      url: '/pages/auth/btnAuth/btnAuth',
    })
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  orderList(e) {
    if (!wx.getStorageSync('token')) {
      util.showMsg('您还没登录哦');
      return;
    }
    wx.navigateTo({
      url: '/pages/ucenter/order/order?currentTab=' + Number(e.currentTarget.dataset.type)
    })
  },
  setting() {
    if (!wx.getStorageSync('token')) {
      util.showMsg('您还没登录哦');
      return;
    }
    wx.navigateTo({
      url: '/pages/ucenter/set/set'
    })
  },
  goAsset(e) {
  
    let type = Number(e.currentTarget.dataset.type)
    if (type === 10) {
      wx.navigateTo({
        url: '/pages/auth/company/company'
      })
    }
    if (!wx.getStorageSync('token')) {
      util.showMsg('您还没登录哦');
      return;
    }

   if(type==14 && this.data.shopStatus!=1 ){
     let tetx;
      switch (this.data.shopStatus){
          case -1:
            tetx="店铺未认证！"
          break;
          case 0:
            tetx="店铺认证中！"
          break;
          case 2:
            tetx="店铺认证失败！"
          break;
      }     
      util.showMsg(tetx);
      return;
   }
    let url;
    switch (type) {
      case 1:
        url = '/pages/ucenter/yue/yue';
        break;
      case 2:
        url = '/pages/ucenter/sign/sign';
        break;
      case 3:
        url = '/pages/ucenter/coupon/coupon';
        break;
      case 4:
        url = '/pages/ucenter/skill/skill';
        break;
      case 5:
        url = '/pages/ucenter/collect/collect';
        break;
      case 6:
        url = '/pages/ucenter/footprint/footprint';
        break;
      case 7:
        url = '/pages/ucenter/address/address';
        break;
      case 8:
        url = '/pages/ucenter/feedback/feedback';
        break;
      case 9:
        url = '/pages/ucenter/kefu/kefu';
        break;
      case 11:
        url = this.data.curUser.isDistributor ? '/pages/dcenter/index/index' :'/pages/dcenter/apply/apply';
        break;
      case 12:
          url = '/pages/ucenter/extension/extension';
         break;
      case 13:
         url = '/pages/ucenter/grade/grade';
      break;
       case 14:
        url = '/pages/ucenter/shop/shop';
      break;
        case 15:
          url = '/pages/ucenter/ranking/ranking';
      break;
      case 16:
        url = '/pages/ucenter/authentication/authentication';
    break;
    }
    if (url) {
      wx.navigateTo({url});
    }
  },
  getFootprintList() {
    let that = this;

    util.request('goods/list', {
      type: 'IS_NEW'
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          newGoods: res.data.records
        });
      }
    });
  },
  downContract: function () {
    const downloadTask = wx.downloadFile({
      url: 'https://platform-wxmall.oss-cn-beijing.aliyuncs.com/contract-template.docx',
      success(res) {
        wx.hideLoading();

        const filePath = res.tempFilePath;

        // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
        if (res.statusCode === 200) {
          wx.showModal({
            title: '提示',
            content: "是否预览文档？",
            success: function (res) {
              if (res.confirm) {
                wx.openDocument({
                  filePath,
                  success(res) {}
                })
              }
            }
          })
        }
      }
    })
    downloadTask.onProgressUpdate((res) => {
      wx.showLoading({
        title: '下载进度：' + res.progress + '%'
      })
    })
  },
  exitLogin: function () {
    wx.showModal({
      title: '',
      confirmColor: '#b4282d',
      content: '退出登录？',
      success: function (res) {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.switchTab({
            url: '/pages/index/index'
          });
        }
      }
    })

  }
})
