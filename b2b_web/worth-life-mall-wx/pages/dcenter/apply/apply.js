const util = require('../../../utils/util.js')

Page({
  data: {
    form: {
      name: '',
      mobile: null,
      mobileCode: null,
      code: null
    },
    inviter: null,
    isShare: false,
    codeDisabled: false,
    timeoutId: null,
    captcha: {
      cooldown: 0,
      intervalId: null
    }
  },
  bindNameInput(event) {
    const form = this.data.form
    form.name = event.detail.value
    this.setData({
      form
    })
  },
  bindMobileInput(event) {
    const form = this.data.form
    form.mobile = event.detail.value
    this.setData({
      form
    })
  },
  bindMobileCodeInput(event) {
    const form = this.data.form
    form.mobileCode = event.detail.value
    this.setData({
      form
    })
  },
  bindCodeInput(event) {
    const form = this.data.form
    form.code = event.detail.value
    this.setData({
      form,
      inviter: null
    })

    this.getInviter()
  },
  getInviter() {
    const form = this.data.form

    clearTimeout(this.data.timeoutId)
    this.data.timeoutId = setTimeout(() => {
      util.request('distributor/getInviter', {
        code: form.code,
        isShare: this.data.isShare,
      }).then(({
        data
      }) => {
        const codeDisabled = data.disabled
        const inviter = data.inviter

        if (codeDisabled) {
          form.code = data.code
        }

        this.setData({
          form,
          codeDisabled,
          inviter
        })
      })
    }, 1200)
  },
  sendSms() {
    let that = this;
    if (!util.isMobile(that.data.form.mobile)) {
      return
    }
    util.request('index/smsCode', {
        phone: this.data.form.mobile
      }, 'POST')
      .then(() => {
        // 再次发送倒计时
        const intervalId = setInterval(() => {
          const captcha = this.data.captcha
          captcha.cooldown--
          if (captcha.cooldown <= 0) {
            clearInterval(captcha.intervalId)
            this.setData({
              captcha: {
                cooldown: 0,
                intervalId: null
              }
            })
          } else {
            this.setData({
              captcha
            })
          }
        }, 1000)

        this.setData({
          captcha: {
            cooldown: 60,
            intervalId
          }
        })
      })
  },
  submitApply() {
    util.request('distributor/apply', this.data.form, 'POST')
      .then(() => {
        wx.redirectTo({
          url: '/pages/dcenter/index/index'
        })
      })
  },
  // 扫小程序码接收参数
  onLoad(options) {
    console.log("二维码中携带的参数，需注意上线的参数格式是否与测试参数格式不一致:" + JSON.stringify(options))
    const form = this.data.form
    form.code = options.scene
    this.setData({
      isShare: true,
      form,
    })
    wx.setStorageSync('navUrl', '/pages/dcenter/apply/apply?scene=' + options.scene);
    this.getInviter()
  },
})
