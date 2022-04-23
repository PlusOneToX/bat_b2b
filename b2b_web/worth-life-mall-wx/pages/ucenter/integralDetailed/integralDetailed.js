// pages/ucenter/detailed/detailed.js
var util = require('../../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
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
   this.queryIntegral()
  },
  queryIntegral:function(){
    const that=this;
    let {pageSize,pageIndex}=that.data
    let integralCount;
    util.request("user/pointsDetails",{pageSize:pageSize,pageNo:pageIndex},"post").then(function(res){
      res.data.records.forEach(element => {
        integralCount=element.integralCount
        element.json= (element.remark && JSON.parse(element.remark)) || ""
      });
      that.setData({
        triggered:false,
        dataSource:that.data.dataSource.concat(res.data.records),
        total:res.data.total,
        hasMoreData:that.data.dataSource.concat(res.data.records).length >= res.data.total,
        integralCount
      })
    })
  },
  refresherrefresh:function(){
    const that=this;
    that.data.pageSize=20
    that.data.pageIndex=1
    that.data.total=0
    that.data.dataSource=[]
    that.queryIntegral()
  },
  scrolltolower:function(){
    const that=this;
    let {dataSource,total}=that.data
    if(!dataSource ||  dataSource.length>=total) return;
    that.data.pageIndex += 1;
    that.queryIntegral()
  }
})