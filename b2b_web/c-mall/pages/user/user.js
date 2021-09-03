import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
import util from '../../utils/util';

let app = getApp()

// pages/user/user.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLogin: false,
    navBarHeight: getApp().globalData.navBarHeight,
    menuTop: getApp().globalData.menuTop,
    menuHeight: getApp().globalData.menuHeight,
    isShowServiceMenu: false,
    phoneNumber:"400-500-2356",
    wechatNum:"batkeji",
    recommendList:[
      {
        id:"0",
        imageSrc:"../../assets/images/like_image.png",
        title:"服装",
        curPrice:"59.90",
        oriPrice:"89.90",
        tags:["满500减100","秒杀"]
      }
    ],

  },

  orderState: {
    "all": 0,
    "unPay": 0,
    "": 0,
    "all": 0,
    "all": 0,
  },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    wx.getSystemInfo({
      success: (result) => {

      },
    })
    
  },

  onShow: function () {

    const isLogin = util.isLogin();
    this.setData({
      isLogin
    })
    
  },

  handleAddress() {
    wx.navigateTo({
      url: '/pages/address/address'
    })
  },

  handleCoupon() {
    wx.navigateTo({
      url: '/pages/coupon/coupon',
    })

  },
  
  handleService() {
    const { isShowServiceMenu } = this.data;
    this.setData({
      isShowServiceMenu: !isShowServiceMenu
    })
  },

  handleSetting() {
    wx.navigateTo({
      url: '/pages/setting/setting',
    })
  },
  
  closeServiceMenu() {
    this.setData({
      isShowServiceMenu: false
    })
  },

  handleHotline() {
    Dialog.confirm({
      title:"客服热线",
      message:this.data.phoneNumber,
      cancelButtonColor: "#4A4A4A",
      confirmButtonColor: "#F94021",
    }).then(() => {
      // on confirm
      wx.makePhoneCall({
        phoneNumber: this.data.phoneNumber
      })

      this.setData({
        isShowServiceMenu: false
      })

    }).catch(() => {
      // on cancel
    });
  },

  handleWechat() {
    
    Dialog.confirm({
      title:"已复制微信号:" + this.data.wechatNum,
      message:"是否打开微信？",
      confirmButtonText:"复制到微信",
      cancelButtonColor: "#4A4A4A",
      confirmButtonColor: "#F94021",
    }).then(() => {
      // on confirm
      wx.setClipboardData({
        data: this.data.wechatNum,
        success(res) {
          wx.showToast({
            title: '复制成功'
          })
        }
      })

      this.setData({
        isShowServiceMenu: false
      })

    }).catch(() => {
      // on cancel
    });

  },

  authSuccess(e) {

    const  isLogin = util.isLogin();
    this.setData({
      isLogin
    })

  },

})