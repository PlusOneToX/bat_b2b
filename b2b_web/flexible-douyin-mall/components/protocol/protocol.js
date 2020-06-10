Component({
  data: {
    diyTitle: "BAT小程序", // 小程序名
    wechatName: "BAT公众号", // 微信公众号
    companyName: "", // 公司名
    creditCode: "", // 信用代码
  },
  properties: {
    
  },
  methods: {
    handleClose() {
      this.triggerEvent('agree', {});
    }
  }
})