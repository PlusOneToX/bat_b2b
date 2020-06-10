// // Api
// import $request from '../../assets/api/request'
// import api from '../../assets/api/allApi'

const app = getApp()

Page({
  data: {
    url: '',
    path: '',
    shareData: {}, // 分享数据
  },

  // getMessage(e) {
  //   let shareData = e.detail.data[e.detail.data.length - 1];
  //   this.setData({
  //     shareData: JSON.parse(shareData)
  //   })
  // },

  onLoad: function (options) {
    // 设置调整路径
    if (app.globalData.host === 'https://api.bat.com/') {
      this.setData({
        path: 'https://wx.bat.com/wechatProgram', // 正式
      });

    } else {
      this.setData({
        path: 'https://test.bat.com/springcloud/wechatProgram', // 测试
        // path: 'http://172.16.12.157:8085/springcloud/wechatProgram', // 本地
      });
    }

    let that = this;

    let params = options.enterParams ? JSON.parse(options.enterParams) : {};
    // 获取用户信息
    params.phone = tt.getStorageSync('phone');
    params.userId = tt.getStorageSync('userId');
    params.userNo = tt.getStorageSync('userNo');
    params.auth = tt.getStorageSync('auth');
    params.openid = tt.getStorageSync('openid');
    // 获取分销商信息
    params.distributorId = tt.getStorageSync('distributorId') || 2601;
    params.exchangeId = tt.getStorageSync('exchangeId');
    params.platform = 'GF60006';
    params.orderSource = 'GF60006';

    let enterParams = JSON.stringify(params)

    if (options.enterFlag === 'externalLink') {
      // banner 外链
      let enterParams = JSON.parse(options.enterParams);
      this.setData({
        url: decodeURIComponent(enterParams.externalLink)
      })
    } else if (options.enterFlag === 'themeDetail') {
      // 主题列表/banner 点击跳转主题详情
      this.setData({
        url: that.data.path + '/themeDetail?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'diyCustom') {
      // 传图定制
      this.setData({
        url: that.data.path + '/custom?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'codeList') {
      // 卡包中心
      if (options.enterParams) {
        this.setData({
          url: that.data.path + '/code?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
        })
      } else {
        this.setData({
          url: that.data.path + '/code?enterFlag=' + options.enterFlag
        })
      }
    } else if (options.enterFlag === 'details') {
      // 点击某个图片跳转详情
      this.setData({
        url: that.data.path + '/detail?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'orderList') {
      // 订单列表
      if (options.sid) {
        params.sid = options.sid;
      }
      let orderParams = JSON.stringify(params)
      this.setData({
        url: that.data.path + '/orderList?enterFlag=' + options.enterFlag + '&enterParams=' + orderParams
      })
    } else if (options.enterFlag === 'shopcart') {
      // 预览加入购物车、点击购物车
      this.setData({
        url: that.data.path + '/shopcart?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'address') {
      // 地址管理
      this.setData({
        url: that.data.path + '/address?enterFlag=' + options.enterFlag + '&enterParams=' + enterParams
      })
    } else if (options.enterFlag === 'submitOrder') {
      // 提交订单
      this.setData({
        url: that.data.path + '/order?enterFlag=' + options.enterFlag
      })
    }
  },


  onShareAppMessage: function (res) {
    // 判断是否在详情页
    // if (res.webViewUrl.indexOf("orderDetail") > 0) {
    //   // 判断是否有分享内容
    //   if (this.data.shareData && this.data.shareData.id) {
    //     let id = this.data.shareData.id;
    //     let distributorId = this.data.shareData.distributorId;
    //     return $request
    //       .post(api.exchangeShareSend, {
    //         distributorId: distributorId,
    //         id: id,
    //       })
    //       .then((res) => {
    //         if (res.success) {
    //           return {
    //             title: res.data.forwardText,
    //             path: "/pages/activity/activity?id=" + res.data.exchangeSpecialReleaseId + "&distributorId=" + res.data.distributorId,
    //             imageUrl: res.data.forwardImg,
    //           }
    //         } else {
    //           tt.showToast({
    //             title: res.errMessage,
    //           });
    //         }
    //       })
    //   }
    // }
  }
})