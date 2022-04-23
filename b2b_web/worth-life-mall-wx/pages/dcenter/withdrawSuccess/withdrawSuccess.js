const util = require('../../../utils/util.js')

Page({
  data: {
    incomeTime: '',
  },
  finish () {
    wx.navigateBack({
      delta: 1,
    })
  },
  onLoad: function(options) {
    console.log(options.incomeTime)
    this.setData({
      incomeTime: options.incomeTime
    })
  }
})
