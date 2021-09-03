// pages/address/address.js

import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';


Page({

  /**
   * 页面的初始数据
   */
  data: {
    addressList:[],
    // addressList:[
    //   {
    //     id:0,
    //     x:0,
    //     province: "",
    //     city: "",
    //     district: "",
    //     address: "",
    //     full_region: "",
    //     userName: "",
    //     telNumber: "",
    //     isDefault: true
    //   },
    // ],
    startX:0,
    endX:0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

   /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let addressList = wx.getStorageSync('address')||[];
    addressList.forEach(v=>v.itemHeight=50);
    if (addressList.length > 0){
      this.setData({
        addressList,
      })
    }

    this.getAddressCellHeight();

  },

  onTouchStart(event) {
    if (event.touches[0]) {
      this.startX = event.touches[0].clientX;
    }
  },

  onTouchMove(event) {

    if (event.touches[0]) {
      this.endX = event.touches[0].clientX;
    }
  },

  onTouchEnd(event) {

    if (this.startX && this.endX) {
      // 滑动结束的时候增加x的距离值
      const index = event.currentTarget.dataset.index;
      var x = "addressList[" + index + "].moveX";

      const isDefault = this.data.addressList[index].isDefault;
      const rightWidth = isDefault? 75 : 150;
      const limitWidth = rightWidth/2;

      let delta = this.startX - this.endX;
      //left
      if (delta > 0) {
        if (delta > limitWidth) {
          this.setData({
            [x]: -rightWidth
          })
        } else {
          this.setData({
            [x]: 0
          })
        }
      } else {
        //right
        if (Math.abs(delta) > limitWidth) {
          this.setData({
            [x]: 0
          })
        } else {
          this.setData({
            [x]: -rightWidth
          })
        }
      }
      this.startX = null;
      this.endX = null;
    }
  },

  handleEdit(e) {

    const { index } = e.currentTarget.dataset;
    var addressList = this.data.addressList;
    var item = this.data.addressList[index];

    wx.navigateTo({
      url: '/pages/address_edit/address_edit?id=' + item.id
    })
  },

  handleDefault(e) {
    const { index } = e.currentTarget.dataset;
    var addressList = this.data.addressList;

    //重置默认地址，全设为false
    addressList.forEach(v=>v.isDefault=false);

    //设为默认地址
    var item = this.data.addressList[index];
    item.isDefault = true;
    item.moveX = 0;

    this.setData({
      addressList
    })

  },

  handleDelete(e) {
    const { index } = e.currentTarget.dataset;
    var addressList = this.data.addressList;

    Dialog.confirm({
      message:"确定删除该地址吗",
      cancelButtonColor: "#4A4A4A",
      confirmButtonColor: "#F94021",
    }).then(() => {
      // on confirm
      addressList.splice(index, 1);
      wx.setStorageSync('address', addressList)
      this.setData({
        addressList
      })
      

    }).catch(() => {
      // on cancel
    });

  },

  addAddress() {

    wx.navigateTo({
      url: '/pages/address_add/address_add'
    })

  },

  //获取每个地址单元格高度
  getAddressCellHeight() {
    var that = this;
    let addressList = that.data.addressList;
    wx.createSelectorQuery().selectAll('#address-cell').boundingClientRect(function(rects){
      console.log(rects);
      rects.forEach((v,i)=>{
        addressList[i].itemHeight = v.height;
      })
      that.setData({
        addressList
      })
      console.log(addressList);
    }).exec()
  }

})