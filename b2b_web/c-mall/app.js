// app.js

import httpRequest from './utils/request'
import api from './utils/allApi'

App({
  onLaunch() { 
    this.calcNavigationBarInfo();
  },

  globalData: {
    navBarHeight: 0,
    statusBarHeight:0,
    menuTop:0,
    menuHeight:0,
  },

  calcNavigationBarInfo () {

    // 获取系统信息
    const systemInfo = wx.getSystemInfoSync();
    this.globalData.statusBarHeight = systemInfo.statusBarHeight;
    console.log(systemInfo);

    this.globalData.navBarHeight = 44 + + systemInfo.statusBarHeight;
    const menuButtonInfo = wx.getMenuButtonBoundingClientRect();
    this.globalData.menuTop = menuButtonInfo.top;
    this.globalData.menuHeight= menuButtonInfo.height;

    // if (systemInfo.platform == "ios") {
    //   this.globalData.navBarHeight = 44 + + systemInfo.statusBarHeight;
    // } else {
    //   // 胶囊按钮位置信息
    //   const menuButtonInfo = wx.getMenuButtonBoundingClientRect();
    //   // 导航栏高度 = 状态栏到胶囊的间距（胶囊上坐标位置-状态栏高度） * 2 + 胶囊高度 + 状态栏高度
    //   this.globalData.navBarHeight = (menuButtonInfo.top - systemInfo.statusBarHeight) * 2 + menuButtonInfo.height + systemInfo.statusBarHeight;
    // }

  }

})
