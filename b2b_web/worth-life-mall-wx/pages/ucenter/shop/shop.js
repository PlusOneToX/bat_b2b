// pages/ucenter/shop/shop.js
var util = require('../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shopInfo:"",//店铺信息
    active: 0,
    scrollTop:0,
    triggered:false,
    hasMoreData:false,
    pageSize:20,
    pageIndex:1,
    recordDataSource:[],//邀请记录
    recordTotal:0,// 邀请记录总数
    rewardDataSource:[],//推广收入
    rewardTotal:0,//推广收入总数
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.queryMyShop()
    this.data.active==0 && this.invitedRecord()
    this.data.active==1 && this.extension()
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
  // 下拉刷新
  refresherrefresh:function(){
    const that=this;
     that.data.pageSize=20;
     that.data.pageIndex=1;
     if(that.data.active==0){
       that.setData({
        scrollTop:0,
        recordTotal:0,
        recordDataSource:[]
       },()=>{that.invitedRecord()})
     }else if(that.data.active==1){
       that.setData({
        scrollTop:0,
        rewardTotal:0,
        rewardDataSource:[]
       },()=>{that.extension()})     
     }
    
  },
  // 上拉分页
  scrolltolower:function(){
    const that=this;
      if(that.data.active==0 && that.data.recordDataSource.length >= that.data.recordTotal )return;
      if(that.data.active==1 && that.data.rewardDataSource.length >= that.data.rewardTotal )return;
      that.data.pageSize=20;
      that.data.pageIndex += 1;
      that.data.active==0 && that.invitedRecord()
      that.data.active==1 && that.extension()
  },
  // tab切换
  checkTab: function (event) {
    const that=this;
    let active = event.currentTarget.dataset.active;
    if (that.data.active == active) return;
    that.setData({
      active: event.currentTarget.dataset.active
    },function(){
      that.refresherrefresh();
    })
  },
  // 邀请记录分页
  invitedRecord:function(){
    const that=this;
    let {pageSize,pageIndex}=that.data
    util.request("user/invitedRecord",{pageSize:pageSize,pageNo:pageIndex},"post").then(function(res){
      that.setData({
        triggered:false,
        recordDataSource:that.data.recordDataSource.concat(res.data.records),
        recordTotal:res.data.total,
        hasMoreData:that.data.recordDataSource.concat(res.data.records).length >= res.data.total
      })
    }).catch(function(){
      that.setData({
        triggered:false
      })
    })
  },
  // 推广收入分页
  extension:function(){
    const that=this;
    let {pageSize,pageIndex}=that.data
    util.request("user/getDistributionRecord",{pageSize:pageSize,pageNo:pageIndex},"post").then(function(res){
      that.setData({
        triggered:false,
        rewardDataSource:that.data.rewardDataSource.concat(res.data.records),
        recordTotal:res.data.total,
        hasMoreData:that.data.rewardDataSource.concat(res.data.records).length >= res.data.total
      })
    }).catch(function(){
      that.setData({
        triggered:false
      })
    })
  },
  // 店铺详情
  queryMyShop:function(){
    const that=this;
    util.request("user/shopDetail").then(function(res){
      that.setData({
        shopInfo:res.data
      })
    })
  }

})