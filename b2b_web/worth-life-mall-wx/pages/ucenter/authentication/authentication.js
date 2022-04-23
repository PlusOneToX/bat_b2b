// pages/ucenter/authentication/authentication.js
var util = require('../../../utils/util');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    form:"",
    shopStatus:-1
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const that=this;
    that.shopInfo()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
  shopInfo:function(){
    const that=this;
    util.request("user/getSubMerchantInfo").then(function(res){
        that.setData({
          form:res.data,
          shopStatus:res.data?res.data.status:-1
        })
    })
  },
  handleInput:function(event){
      let that=this;
      let name=event.target.dataset.name;
      let value=event.detail.value
      that.setData({
        [`form.${name}`]:value
      })
  },
  uploaderImg: function (event) {
    const that = this;
    if(that.data.shopStatus==0 || that.data.shopStatus==1) return;
    let name=event.currentTarget.dataset.name;

    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(imgRes) {
        if (imgRes.tempFiles[0].size > 2097152) {
          wx.showToast({
            title: '文件超出2m',
            icon: 'error',
            duration: 2000
          })
          return
        }

        wx.uploadFile({
          url: util.API_BASE_URL + 'upload/upload',
          filePath: imgRes.tempFilePaths[0],
          header: {
            'content-type': 'multipart/form-data',
            'token': wx.getStorageSync('token')
          },
          name: 'file',
          success(res) {
            const result = JSON.parse(res.data)
            that.setData({
              [`form.${name}`]: result.url
            })
          },
        });
      }
    })
  },
  handleSubmit:function(){
    let that=this;
    let {form} =that.data
    if(!form || !form.shopName){
      util.showMsg("请输入店铺名称")
       return
    }
    if(!form || !form.phone){
      util.showMsg("请输入联系方式")
      return
    }

    util.request("user/subMerchantInfo",form,"POST").then(function(res){
      util.showMsg("信息提交成功",true,2000,"success")
      that.shopInfo()
    })
  }
})