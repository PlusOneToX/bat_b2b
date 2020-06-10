// pages/webview/webview.js
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({
  data: {
    url: '',
    path: '',
    config: {
      tipsshow1: true,
      tipsshow2: false
    },
    showWeb: false, // 显示 webview
    shareData: {}, // 分享数据
  },

  getMessage(e) {
    let shareData = e.detail.data[e.detail.data.length - 1];
    this.setData({
      shareData: JSON.parse(shareData)
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    // 设置调整路径
    if (appId === "wx5d6f4e2g6ef5ess2") {
      // BAT小程序
      this.setData({
        path: 'https://wx.bat.com/wechatProgram', // 正式
      });
    } else if (appId === "wx5f6s8v4f6f68dsrw") {
      // 其他
      this.setData({
        path: 'https://wx.bat.com/wechatProgram', // 正式
      });
    } else {
      this.setData({
        // path: 'http://test.bat.com/springcloud/wechatProgram', // 测试
        path: 'https://test.bat.com/springcloud/wechatProgram', // 测试
        // path: 'http://172.16.12.53:8085/springcloud/wechatProgram', // 本地
      });
    }


    let that = this;

    let params = options.enterParams ? JSON.parse(options.enterParams) : {};
    // 获取用户信息
    params.phone = wx.getStorageSync('phone');
    params.userId = wx.getStorageSync('userId');
    params.userNo = wx.getStorageSync('userNo');
    params.auth = wx.getStorageSync('auth');
    params.openid = wx.getStorageSync('openid');
    // 获取分销商信息
    params.distributorId = wx.getStorageSync('distributorId') || 2601;
    params.exchangeId = wx.getStorageSync('exchangeId');
    params.platform = 28;
    params.orderSource = 28;

    let enterParams = JSON.stringify(params)

    if (options.enterFlag === 'externalLink') {
      // banner 外链
      let enterParams = JSON.parse(options.enterParams);
      this.setData({
        showWeb: true,
        url: decodeURIComponent(enterParams.externalLink)
      })
    } else if (options.enterFlag === 'themeDetail') {
      // 主题列表/banner 点击跳转主题详情
      this.setData({
        showWeb: true,
        url: that.data.path + '/themeDetail?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'diyCustom') {
      // 传图定制
      this.setData({
        showWeb: true,
        url: that.data.path + '/custom?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'codeList') {
      // 卡包中心
      if (options.enterParams) {
        this.setData({
          showWeb: true,
          url: that.data.path + '/code?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
        })
      } else {
        this.setData({
          showWeb: true,
          url: that.data.path + '/code?enterFlag=' + options.enterFlag
        })
      }
    } else if (options.enterFlag === 'details') {
      // 点击某个图片跳转详情
      this.setData({
        showWeb: true,
        url: that.data.path + '/detail?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'orderList') {
      // 订单列表
      if (options.sid) {
        params.sid = options.sid;
      }
      let orderParams = JSON.stringify(params)
      this.setData({
        showWeb: true,
        url: that.data.path + '/orderList?enterFlag=' + options.enterFlag + '&enterParams=' + orderParams
      })
    } else if (options.enterFlag === 'shopcart') {
      // 预览加入购物车、点击购物车
      this.setData({
        showWeb: true,
        url: that.data.path + '/shopcart?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'address') {
      // 地址管理
      this.setData({
        showWeb: true,
        url: that.data.path + '/address?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'submitOrder') {
      // 提交订单
      this.setData({
        showWeb: true,
        url: that.data.path + '/order?enterFlag=' + options.enterFlag
      })
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function (res) {
    // 判断是否在详情页
    if (res.webViewUrl.indexOf("orderDetail") > 0) {
      // 判断是否有分享内容
      if (this.data.shareData && this.data.shareData.id) {
        let id = this.data.shareData.id;
        let distributorId = this.data.shareData.distributorId;
        return $request
          .post(api.exchangeShareSend, {
            distributorId: distributorId,
            id: id,
          })
          .then((res) => {
            if (res.success) {
              return {
                title: res.data.forwardText,
                path: "/pages/activity/activity?id=" + res.data.exchangeSpecialReleaseId + "&distributorId=" + res.data.distributorId,
                imageUrl: res.data.forwardImg,
              }
            } else {
              wx.showToast({
                title: res.errMessage,
              });
            }
          })
      }
    }
  }
})