var util = require('./utils/util.js');

App({
  globalData: {
    // todo 申请 https://lbs.qq.com/qqmap_wx_jssdk/index.html，详细接入配置请查看产品文档
    qqMapKey: '',
    share: false, // 分享默认为false
    // todo 快递鸟注册 http://www.kdniao.com/reg
    kdnBusinessId: '',
    kdnAppKey: '',
    referrerUserId: '', //分享的用户Id
    customBar: {
      width: 0,
      height: 0,
      top: 0,
      scrollH: 0
    },
    userInfo: {
      nickname: 'Hi,游客,点击头像登录',
      headImgUrl: '/static/images/150547696d798c.png'
    },
    token: '',
    rechargeStatus: 2
  },
  onLaunch: function (options) {
    let that = this;
    // 判断是否由分享进入小程序
    if (options.scene == 1007 || options.scene == 1008) {
      this.globalData.share = true
    } else {
      this.globalData.share = false
    }

    //获取设备顶部窗口的高度（不同设备窗口高度不一样，根据这个来设置自定义导航栏的高度）
    let obj = wx.getMenuButtonBoundingClientRect();
    wx.getSystemInfo({
      success: (res) => {
        that.globalData.customBar.width = obj.left || res.windowWidth
        that.globalData.customBar.height = obj.top ? (obj.top + obj.height + 8) : (res.statusBarHeight + 44)
        that.globalData.customBar.top = obj.top ? (obj.top + (obj.height - 32) / 2) : (res.statusBarHeight + 6)
        that.globalData.customBar.scrollH = res.windowWidth * 0.6
      }
    })
    //获取小程序更新机制兼容
    if (wx.canIUse('getUpdateManager')) {
      const updateManager = wx.getUpdateManager()
      updateManager.onCheckForUpdate(function (res) {
        // 请求完新版本信息的回调
        if (res.hasUpdate) {
          updateManager.onUpdateReady(function () {
            wx.showModal({
              title: '更新提示',
              content: '新版本已经准备好，是否重启应用？',
              success: function (res) {
                if (res.confirm) {
                  // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                  updateManager.applyUpdate()
                }
              }
            })
          })
          updateManager.onUpdateFailed(function () {
            // 新的版本下载失败
            wx.showModal({
              title: '已经有新版本了哟~',
              content: '新版本已经上线啦~，请您删除当前小程序，重新搜索打开哟~',
            })
          })
        }
      })
    } else {
      // 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
      wx.showModal({
        title: '提示',
        content: '当前微信版本过低，无法更好体验程序，请升级到最新微信版本后重试。'
      })
    }
    util.request('index/getRechargeStatus').then(res => {
      if (res.code === 0) {
        this.globalData.rechargeStatus = res.rechargeStatus
      }
    });
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  // 更新小程序
  updateManager: function () {
    //获取系统信息 客户端基础库
    wx.getSystemInfo({
      success: function (res) {
        //基础库版本比较，版本更新必须是1.9.90以上
        const v = util.compareVersion(res.SDKVersion, '1.9.90');
        if (v > 0) {
          const manager = wx.getUpdateManager();
          manager.onCheckForUpdate(function (res) {
            // 请求完新版本信息的回调
          });
          manager.onUpdateReady(function () {
            wx.showModal({
              title: '更新提示',
              content: '新版本已经准备好，是否重启应用？',
              success: function (res) {
                if (res.confirm) {
                  // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
                  manager.applyUpdate();
                }
              }
            })
          });
          manager.onUpdateFailed(function () {
            // 新的版本下载失败
          });
        } else {
          wx.showModal({
            title: '温馨提示',
            content: '当前微信版本过低，无法更好体验程序，请升级到最新微信版本后重试。'
          })
        }
      },
    })
  }
})
