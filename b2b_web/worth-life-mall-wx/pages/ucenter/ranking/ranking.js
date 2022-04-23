// pages/ucenter/ranking/ranking.js
var util = require('../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    amountTotal:0,
    pageSize:20,
    pageIndex:1,
    total:0,
    triggered:false,
    hasMoreData:false,
    dataSource:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const that=this;
    that.queryRanking();
    that.queryBonus()
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
  // 上拉刷新
  refresherrefresh:function(){
     const that=this;
     that.data.pageSize=20
     that.data.pageIndex=1
     that.data.total=0
     that.data.dataSource=[]
     that.queryRanking()

  },
  // 上拉分页
  scrolltolower:function(){
    const that=this;
    let {dataSource,total}=that.data
    if(!dataSource ||  dataSource.length>=total) return;
    that.data.pageIndex += 1;
    that.queryRanking()
  },
  // 分页
  queryRanking:function(){
    const that=this;
    let {pageSize,pageIndex}=that.data
    util.request("user/getIntegralOrder",{pageSize:pageSize,pageNo:pageIndex},"post").then(function(res){
      that.setData({
        triggered:false,
        dataSource:that.data.dataSource.concat(res.data.records),
        total:res.data.total,
        hasMoreData:that.data.dataSource.concat(res.data.records).length >= res.data.total
      })
    })
  },
  // 奖金池
  queryBonus:function(){
    const that=this;
    util.request("user/getBonusActivity").then(function(res){
       that.setData({
        amountTotal:res.data.amountTotal
       })
    })
  }
})