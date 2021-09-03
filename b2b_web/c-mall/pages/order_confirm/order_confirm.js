// pages/order_confirm/order_confirm.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    goodsList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    const cart = wx.getStorageSync('cart')||[];
    let checkedGoodsList = [];
    cart.forEach(v=>{
      v.goodsList.forEach(v1=>{
        if (v1.goods_checked) {
          checkedGoodsList.push(v1);
        }
      })
    });

    this.setData({
      goodsList: checkedGoodsList
    })
  },

  toPayNow() {

  }

})