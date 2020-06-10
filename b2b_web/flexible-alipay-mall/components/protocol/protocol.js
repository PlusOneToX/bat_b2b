Component({
  data: {
    diyTitle: "BAT小程序", // 小程序名
    wechatName: "BAT公众号", // 微信公众号
    phone: "", // 热线
    companyName: "", // 公司名
    creditCode: "", // 信用代码
  },
  props: {

  },
  methods: {
    handleClose() {
      this.props.onAgree();
    }
  }
})