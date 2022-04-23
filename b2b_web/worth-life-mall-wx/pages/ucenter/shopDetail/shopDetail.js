// pages/ucenter/shop/shop.js
var util = require('../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
     id:"",
     pageSize:20,
     pageIndex:1,
     rewardTotal:0,
     rewardDataSource:[],
     triggered:false,
     hasMoreData:false,
     shopData:"",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const that=this;
   if(options && options.id){
     that.data.id=options.id
     that.shopInfo()
      that.rewardData()
   }
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
  rewardData:function(){
    const that=this;
    if(!that.data.id)return;
    util.request("user/getDistributionRecord",{shopUserId:that.data.id,pageNo:that.data.pageIndex,pageSize:that.data.pageSize},"post").then(function(res){
      that.setData({
        triggered:false,
        rewardDataSource:that.data.rewardDataSource.concat(res.data.records),
        rewardTotal:res.data.total,
        hasMoreData:that.data.rewardDataSource.concat(res.data.records).length >= res.data.total
      })
    }).catch(function(){
      that.setData({
        triggered:false
      })
    })
  },
  refresherrefresh:function(){
    const that=this;
     that.data.pageSize=20;
     that.data.pageIndex=1
     that.data.rewardTotal=0
     that.data.rewardDataSource=[]
     that.rewardData()
  },
  scrolltolower:function(){
    const that=this;
    if(that.data.rewardDataSource.length >= that.data.rewardTotal )return;
    that.data.pageSize=20;
    that.data.pageIndex += 1;
    that.rewardData()
  },
  shopInfo:function(){
    const that=this;
    if(!that.data.id)return;
    util.request("user/shopDistributionDetail",{shopUserId:that.data.id},"post").then(function(res){
      that.setData({
        shopData:res.data
      })
    })
  }
})