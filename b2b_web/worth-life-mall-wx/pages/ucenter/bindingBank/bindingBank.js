var util = require('../../../utils/util.js');
var bindingUtil = require('../../../utils/bindingUtil.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    name: '',
    cardNumber: '',
    cardType: '请选择卡类型',
    bankName: '请选择银行',
    bankId: '',
    bankTypeList: [],
    cardTypeList: [
      "储蓄卡",
      "信用卡",
      "准贷记卡",
      "预付费卡"
    ],
    cardTypeShow:false,
    bankTypeShow:false,
  },
  //姓名
  getUserIdCardName: function (e) {
    this.setData({
      name: e.detail.value
    })
  },
  //银行卡号
  getUserIdCardNumber: function (e) {
    if (e.detail.value == null || e.detail.value == '') return 
    this.setData({
      cardNumber: e.detail.value
    })
    var temp = bindingUtil.bankCardAttribution(e.detail.value)
    console.log(temp)
    if (temp == Error || temp == 'error') {  
      this.setData({
        cardType: '请选择卡类型',
        bankName: '请选择银行',
        bankId: '',
      })
      wx.showToast({
        title: '查找不到相关银行信息',
        icon: 'none',
        image: '',
        duration: 1000
      })
    }
    else {
      this.setData({
        cardType: temp.cardTypeName,
      })

      for (let index in this.data.bankTypeList) {  
        let bankType = this.data.bankTypeList[index]
        if (bankType.bankName == temp.bankName) {
          this.setData({
            bankName: bankType.bankName,
            bankId: bankType.id,
          })
          return
        }
      };
      this.setData({
        bankName: '请选择银行',
        bankId: '',
      })
      wx.showToast({
        title: '银行名称查找不到',
        icon: 'none',
        image: '',
        duration: 1000
      })
      

    }
  },
  //银行支行名称
  getUserIdCardBankName: function (e) {
    this.setData({
      bankName: e.detail.value
    })
  },
  getUserIdCardType: function (e) {
    this.setData({
      cardType: e.detail.value
    })
  },

  //提交转账信息
  submitInfos: function () {
    var compare = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
    var that = this;
    if (that.data.name.trim().length == 0) {
      wx.showToast({
        title: '用户名不能为空',
        icon: 'none',
        image: '',
        duration: 1000
      })
    }
    else if (!that.data.cardNumber) {
      wx.showToast({
        title: '银行卡号不能为空',
        icon: 'none',
        image: '',
        duration: 1000
      })
    }
    else if (!that.data.cardType || that.data.cardType == '请选择卡类型') {
      wx.showToast({
        title: '卡类型不能为空',
        icon: 'none',
        image: '',
        duration: 1000
      })
    }
    else if (!that.data.bankName || that.data.bankName == '请选择银行') {
      wx.showToast({
        title: '支行名称不能为空',
        icon: 'none',
        image: '',
        duration: 1000
      })
    }
    else {
      //TODO post data to sever
      util.request('user/bindingCard', {
      cardName: that.data.name,
      cardNumber: that.data.cardNumber,
      cardType: that.data.cardType,
      bankTypeId: that.data.bankId,
    }, 'POST').then(function (res) {
      if (res.code === 0) {
        
      wx.showToast({
        title: '绑定成功',
        icon: 'success',
        image: '',
        duration: 1000
      })
      setTimeout(function () {
        wx.navigateBack({
          delta: 1,
        })
       }, 1000)
      } else {
        util.showMsg(res.msg);
      }
    });
    }
  },
  getBankTypeList: function () {
    let that = this
    util.request('user/bankTypeList').then(res => {
      if (res.code === 0) {
        that.setData({
          bankTypeList: res.data
        });
      }
    });
  },
  onLoad: function () {
    this.getBankTypeList()
  },
  // 点击下拉显示框
  cardTypeSelectTap(){
    this.setData({
      cardTypeShow: !this.data.cardTypeShow
    });
  },
  // 点击下拉列表
  cardTypeOptionTap(e){
    let Index=e.currentTarget.dataset.index;//获取点击的下拉列表的下标
    this.setData({
      cardTypeShow:!this.data.cardTypeShow,
      cardType: this.data.cardTypeList[Index]
    });
  },
  closeCardTypeSelect() {
    this.setData({
      cardTypeShow:!this.data.cardTypeShow,
    });
  },
  bankTypeSelectTap(){
    this.setData({
      bankTypeShow: !this.data.bankTypeShow
    });
  },
  bankTypeOptionTap(e){
    let Index=e.currentTarget.dataset.index;
    this.setData({
      bankTypeShow:!this.data.bankTypeShow,
      bankName: this.data.bankTypeList[Index].bankName,
      bankId: this.data.bankTypeList[Index].id,
    });
  },
})