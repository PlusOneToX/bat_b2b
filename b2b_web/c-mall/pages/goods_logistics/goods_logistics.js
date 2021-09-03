// pages/goods_logistics/goods_logistics.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    parameter: {
      'navbar': '1',
      'return': '1',
      'title': '物流信息'
    },
    orderId:'',
    product: { productInfo:{}},
    orderInfo:{
      delivery_name:"圆通速递",
      delivery_id:"21333444rff5666666"
    },
    expressList:[
      {
        status:"已签收",
        detail:"",
        time:"2018-03-03 00:18:56"
      },
    ],
    newExpressList:[],
    isShowMore: false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const expressList = this.data.expressList;
    var isShowMore = this.data.isShowMore;
    var newExpressList = expressList;
    if (expressList.length >= 2) {
      newExpressList = expressList.slice(0,2);
      isShowMore = true;
    } 

    this.setData({
      newExpressList,
      isShowMore
    })
  },

  seeMoreLogistics: function() {

    const expressList = this.data.expressList;
    var newExpressList = this.data.newExpressList;
    var newExpressList = this.data.newExpressList;
    if (expressList.length === newExpressList.length) {
      return;
    }
    
    newExpressList = expressList;
    this.setData({
      newExpressList,
      isShowMore: false
    })
  }
})