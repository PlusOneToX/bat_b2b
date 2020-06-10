// components/protocol/protocol.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {},

  /**
   * 组件的初始数据
   */
  data: {
    diyTitle: "BAT小程序", // 小程序名
    wechatName: "BAT公众号", // 微信公众号
    companyName: "", // 公司名
    creditCode: "", // 信用代码
  },

  /**
   * 组件的方法列表
   */
  methods: {
    handleClose() {
      this.triggerEvent('agree', {});
    }
  },

  /**
   * 组件生命周期函数--在组件实例进入页面节点树时执行
   */
  attached: function () {
    // 获取 appId
    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    // 根据 appId 判断当前环境
    if (appId === "") {
      // BAT小程序
      this.setData({
        diyTitle: "BAT小程序", // 小程序名
        wechatName: "BAT公众号", // 微信公众号
        companyName: "", // 公司名
        creditCode: "", // 信用代码
      })
    }
  }
})