// pages/order_detail/order_detail.js

import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    order:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    const id = options.id;
    const orderList = wx.getStorageSync('orders')||[];
    const index = orderList.findIndex(v=>v.id==id);
    const order = orderList[index];

    this.setData({
      order
    })

  },

  cancleOrder() {

    Dialog.confirm({
      message:"您确定取消订单吗？",
      cancelButtonColor: "#4A4A4A",
      confirmButtonColor: "#F94021",
    }).then(() => {
      // on confirm

    }).catch(() => {
      // on cancel
    });

  },

  toPayNow() {

  },

  gotoLogistics() {
    wx.navigateTo({
      url: '/pages/goods_logistics/goods_logistics'
    })

  }

})