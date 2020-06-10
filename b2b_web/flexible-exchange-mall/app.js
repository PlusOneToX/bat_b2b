/*
 * @Author: yaowei
 * @Date: 2019-11-04 17:14:48
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-22 14:00:05
 */
//app.js
// Api
import $request from './assets/api/request'
import api from './assets/api/allApi'

App({
  onLaunch: function () {
    wx.cloud.init({
      traceUser: true,
    })

    this.getTenantInfo();
  },
  getTenantInfo() {
    let that = this;

    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    console.log("appId = " + appId);
    let tenantNo = 100;
    let hostName = that.globalData.proHost;
    // if (appId === "wx5d6f4e2g6ef5ess2") {
    //   // BAT小程序
    //   tenantNo = 101;
    //   hostName = that.globalData.testHost;
    // } else if (appId === "wx5f6s8v4f6f68dsrw") {
    //   // 其他
    //   tenantNo = 100;
    //   hostName = that.globalData.testHost;
    // }
    return new Promise(function (resolve, reject) {
      $request
        .get(hostName + api.getTenant, {
          gainUrlType: 6, // 需获取的主机类型（默认6）：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端 9兑换商城H5端
          tenantNo: tenantNo
        })
        .then((res) => {
          console.log(res);
          if (res.success) {
            wx.setStorageSync('tenantNo', res.data.tenantNo);
            wx.setStorageSync('tenantUrl', res.data.url);

            resolve(res.data);
          } else {
            if (res.errCode === "B_PLATFORM_QRY_URL_ERROR") {
              wx.showModal({
                title: "温馨提示",
                content: "网址解析异常，请输入正确的网址访问",
                showConfirm: "好的",
                showCancel: false,
              })
            } else if (res.errCode === "B_PLATFORM_WX_PROGRAM_APP_ID_NULL") {
              wx.showModal({
                title: "温馨提示",
                content: "服务器连接异常，请输入正确的网址重新访问",
                confirmText: "好的",
                showCancel: false,
              })
            } else if (res.errCode === "B_PLATFORM_GAIN_URL_NULL") {
              wx.showModal({
                title: "温馨提示",
                content: "获取服务器地址失败，请联系客户反馈",
                confirmText: "好的",
                showCancel: false,
              })
            } else {
              wx.showToast({
                title: res.errMessage,
                icon: 'none'
              });
            }
            reject('Error');
          }
        })
    })
  },
  globalData: {
    hasClickDialog: false, // 是否点击首页弹窗
    proHost: "https://api.bat.com/", // 正式
    // testHost: "https://test.bat.com/", // 测试
    testHost: "http://120.78.221.134:8083/" // 测试
  }
})