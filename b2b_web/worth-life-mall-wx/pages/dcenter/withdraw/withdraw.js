const util = require('../../../utils/util.js')

Page({
  data: {
    availableAmount: '',
    form: {
      type: 'ENT_PAY',
      amount: '',
      cardId: '',
    },
    alertText:'',
    withdrawCard: '',
    withdrawShow: false,
    cardList: [],
  },
  onShow () {
    this.getDistributorInfo()
    this.getCardList()
  },
  getDistributorInfo() {
    let that = this
    util.request('distributor/getDistributorInfo').then(function (res) { 
      that.setData({
        availableAmount: res.data.mallDistEntity.amountAvailable
      });
    });
  },
  getCardList() {
    var that = this;
    util.request('user/getBankCard').then(function (res) {
      if (res.code === 0) {
        that.setData({
          cardList: res.data
        })
      }
    });
  },
  submit () {
    // 校验提现金额
    let legalAmount = this.checkAmount()
    if (!legalAmount) return
    if (this.data.form.amount == '') {
      this.setData({
        alertText: '请输入金额'
      })
      return 
    }
    console.log(this.data.form)
    util.request('distributor/getAmount', this.data.form, 'POST').then((res) => {
        wx.navigateTo({
          url: '/pages/dcenter/withdrawSuccess/withdrawSuccess?incomeTime='+res.data.incomeTime
        })
      })
  },
  modeChange (e) {
    const form = this.data.form
    form.type = e.detail.value
    this.setData({form})
  },
  bindAmountInput (e) {
    const form = this.data.form
    form.amount = e.detail.value
    this.setData({form})
  },
  checkAmount () {
    let amount = this.data.form.amount
    let alertText = ''
    var reg = /^\d+(\.\d{0,2})?$/;
    if (amount != '' && !reg.test(amount)){
      alertText = '请输入有效金额'
    }
    this.setData({
      alertText : alertText
    })
    return alertText == ''
  },
  withdrawAll() {
    const form = this.data.form
    form.amount = this.data.availableAmount
    this.setData({form})
  },
  bindingCard() {
    wx.navigateTo({
      url: '/pages/ucenter/bindingBank/bindingBank',
    })
  },
  withdrawSelectTap() {
    this.setData({
      withdrawShow: true
    })
  },
  closeWithdrawSelectTap() {
    this.setData({
      withdrawShow: false
    })
  },
  selectWithdraw(event) {
    let type = event.currentTarget.dataset.type
    let item = event.currentTarget.dataset.cardItem
    let form = this.data.form
    if (item == null) {
      item = ''
      form.cardId = ''
    } else {
      form.cardId = item.id
    }
    form.type = type
    this.setData({
      withdrawCard: item,
      withdrawShow: false,
      form: form
    })
  },
  lookRule(){
		wx.showModal({
			title: '手续费收费规则',
      content: '手续费收取提现金额的0.1%，最低1元，最高25元。',
      showCancel: false
		})
	}
})
