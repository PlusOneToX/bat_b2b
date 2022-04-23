const util = require('../../../utils/util.js');

Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    navUrl: '',
    code: '',
    userInfo: {},
  },

  onLoad: function (options) {
    if (wx.getUserProfile) {
      this.setData({
        canIUseGetUserProfile: true
      })
    }

    let that = this;
    if(options && options.scene){
      that.data.inviteCode=options.scene
    }
    if (wx.getStorageSync("navUrl")) {
      that.setData({
        navUrl: wx.getStorageSync("navUrl")
      })
    } else {
      that.setData({
        navUrl: '/pages/index/index'
      })
    }
    wx.removeStorageSync('navUrl');

    //不在onLoad时候调用wx.login获取code，用户第一次登录会失败，暂时没查到原因
    wx.login({
      success: function (res) {
        if (res.code) {
          that.setData({
            code: res.code
          })
        }
      }
    });
  },
  noLogin: function () {
    wx.switchTab({
      url: '/pages/index/index'
    })
  },
  goLogin: function () {
    wx.navigateTo({
      url: '/pages/auth/login/login',
    })
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    let that = this;
    wx.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        this.setData({
          userInfo: res,
          
        })
         //登录远程服务器
      if (that.data.code) {
        that.LoginByMa(that.data.userInfo)
      } else {
        wx.login({
          success: function (res) {
            if (res.code) {
              that.setData({
                code: res.code
              })
              that.LoginByMa(that.data.userInfo)
            }
          }
        });
      }
      },
      fail:(err)=>{
        wx.showModal({
          title: '提示',
          content: '用户拒绝',
          showCancel: false,
          success(res) {
            if (res.confirm) {
              wx.switchTab({
                url: '/pages/index/index',
              })
            }
          }
        });
      }
    })
  },
  bindGetUserInfo: function (e) {
    console.log(e);
    let that = this;
    if (e.detail.errMsg.indexOf("fail auth deny") > -1) {
      wx.showModal({
        title: '提示',
        content: '用户拒绝',
        showCancel: false,
        success(res) {
          if (res.confirm) {
            wx.switchTab({
              url: '/pages/index/index',
            })
          }
        }
      });
    } else {
      //登录远程服务器
      if (that.data.code) {
        that.LoginByMa(e)
      } else {
        wx.login({
          success: function (res) {
            if (res.code) {
              that.setData({
                code: res.code
              })
              that.LoginByMa(e)
            }
          }
        });
      }
    }
  },
  LoginByMa(e) {
    let that = this;
    util.request('auth/LoginByMa', {
      code: that.data.code,
      userInfo: e,
      inviteCode:that.data.inviteCode || ""
    }, 'POST', 'application/json').then(res => {
      if (res.code === 0) {
        //存储用户信息
        wx.setStorageSync('userInfo', res.userInfo);
        wx.setStorageSync('token', res.token);
        wx.setStorageSync('userId', res.userId);

      } else {
        util.showMsg(res.msg)
      }

      if (that.data.navUrl && (that.data.navUrl == '/pages/index/index' || that.data.navUrl == '/pages/ucenter/index/index' || that.data.navUrl == '/pages/cart/cart')) {
        wx.switchTab({
          url: that.data.navUrl,
        })
      } else if (that.data.navUrl) {
        wx.redirectTo({
          url: that.data.navUrl,
        })
      }
    });
    //每个code只能使用一次
    that.setData({
      code: ""
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
  }
})
