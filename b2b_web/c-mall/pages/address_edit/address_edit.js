// pages/address_edit/address_edit.js

import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    regions: [
      {
        values: ['山东省','湖南省','广东省','海南省','四川省','浙江省','福建省','河南省'],
        className: 'column1',
        defaultIndex: 2,
      },
      {
        values: ['广州市','韶关市','深圳市','珠海市','江门市','清远市'],
        className: 'column2',
        defaultIndex: 2,
      },
      {
        values: ['龙华新区','宝安区','南山区','福田区','坪山区','盐田区','光明新区','龙岗区'],
        className: 'column3',
        defaultIndex: 4,
      }
    ],
    isOpenRegionMenu:false,
    currentRegion:"",
    currentId:0,
    addressInfo:{},
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const id = options.id;

    const addressList = wx.getStorageSync('address')||[];
    const addressInfo = addressList.find(item=>item.id==id);
    if (addressInfo.userName.length > 0) {
      const currentRegion = addressInfo.province + addressInfo.city + addressInfo.district;
      this.setData({
        addressInfo,
        currentRegion,
        currentId: id
      })
    }

  },

  chooseRegion(){
    const { isOpenRegionMenu } = this.data;
    this.setData({
      isOpenRegionMenu: !isOpenRegionMenu
    })
  },

  onRegionCancel() {

    //关闭菜单
    this.setData({
      isOpenRegionMenu: false
    })

  },

  onRegionConfirm(event) {
    const { picker, value, index } = event.detail;
    let { regions } = this.data;
    let { currentRegion } = this.data;

    regions[0].defaultIndex = index[0];
    regions[1].defaultIndex = index[1];
    regions[2].defaultIndex = index[2];

    currentRegion = value.join("");

    var addressInfo = this.data.addressInfo;
    addressInfo.province = value[0];
    addressInfo.city = value[1];
    addressInfo.district = value[2];

    this.setData({
      addressInfo,
      regions,
      currentRegion,
      isOpenRegionMenu:false
    });

  },

  bindinputUserName(e){
    var addressInfo = this.data.addressInfo;
    const userName =  e.detail.value;
    addressInfo.userName = userName
    this.setData({
      addressInfo
    })
  },

  bindinputMobile(e) {
    var addressInfo = this.data.addressInfo;
    const mobile =  e.detail.value;
    addressInfo.telNumber = mobile
    this.setData({
      addressInfo
    })
  },

  bindinputDetail(e) {

    var addressInfo = this.data.addressInfo;
    const detail = e.detail.value;
    addressInfo.detail = detail;
    addressInfo.fullRegion = addressInfo.province + addressInfo.city + addressInfo.district + detail;
    this.setData({
      addressInfo
    })
  },

  handleCheck() {

    var addressInfo = this.data.addressInfo;
    const isDefault = addressInfo.isDefault;
    addressInfo.isDefault = !isDefault;
    this.setData({
      addressInfo
    })

  },

  saveAddress() {
    const addressInfo = this.data.addressInfo;
    if (!addressInfo.userName){
      Toast("请填写收货人姓名");
      return;
    }

    if (!addressInfo.telNumber){
      Toast("请填写手机号码");
      return;
    }

    if (!this.data.currentRegion){
      Toast("请选择所在地区");
      return;
    }

    if (!addressInfo.detail){
      Toast("请填写详细地址");
      return;
    }

    const id = this.data.currentId;
    var addressList = wx.getStorageSync('address')||[];
    const index = addressList.findIndex(v=>v.id==id);
    addressList[index] = addressInfo;
  
    if(addressInfo.isDefault){
      //重置默认地址，全设为false
      addressList.forEach(v=>v.isDefault=false);
      addressList[index].isDefault = true;
    } 

    wx.setStorageSync('address', addressList)
    
    wx.navigateBack({
      delta: 1
    })
  },

  deleteAddress() {

    Dialog.confirm({
      message:"确认删除该地址么？",
      cancelButtonColor: "#4A4A4A",
      confirmButtonColor: "#F94021",
    }).then(() => {
      // on confirm

      const id = this.data.currentId;
      var addressList = wx.getStorageSync('address')||[];
      const index = addressList.findIndex(v=>v.id==id);
      addressList.splice(index, 1);

      wx.setStorageSync('address', addressList)
      
      wx.navigateBack({
        delta: 1
      })

    }).catch(() => {
      // on cancel
    });

  }

})