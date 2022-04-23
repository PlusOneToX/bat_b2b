// pages/ucenter/carry/carry.js
var util = require('../../../utils/util.js');
const utils = require('../../../components/utils/utils');
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    nowMoney:0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getUserInfo()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
   
  },
  getUserInfo:function(){
    var that=this;
    util.request('user/userInfo').then(function (res) {
      if (res.code === 0) {
        that.setData({
          nowMoney: res.data.balance
        })
      }
    });
  },
  submitSub: function (e) {
    var that = this;
    if (!e.detail.value.number) {
      util.showMsg('请输入提现金额');
      return false;
    }
    if (!util.isNumber(e.detail.value.number)) {
      util.showMsg('请输入正确的提现金额');
      return false;
    }
    if(e.detail.value.number > that.data.nowMoney){
      util.showMsg('余额不足');
      return false;
    }

    util.request('distributor/getAmount', {type:"ENT_PAY",amount:e.detail.value.number}, 'POST').then(function (res) {
      util.showMsg('提现成功');
      setTimeout(function(){
        wx.navigateBack()
      },1000)
    })
  }
})