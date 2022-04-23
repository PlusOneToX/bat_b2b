// pages/ucenter/grade/grade.js
var util = require('../../../utils/util.js');

function createRpx2px() {
  var _wx$getSystemInfoSync = wx.getSystemInfoSync(), windowWidth = _wx$getSystemInfoSync.windowWidth;
  return function(rpx) {
      return rpx / 2;
      // return windowWidth / 750 * rpx
      };
}

var rpx2px = createRpx2px();


Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: "",
    canvasWidth:rpx2px(240),
    canvasHeight:rpx2px(240)
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this;
    that.queryUserInfo()
    this.queryGrade()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
  queryGrade:function(){
    let that=this;
    util.request('user/myLevel',"",'POST').then(function(res){
      that.setData(res.data,function(){
        that.integralInit()
        that.priceInit()
      })
       
    })
  },
  queryUserInfo: function () {
    let that = this;
    let userInfo = wx.getStorageSync('userInfo');
    that.setData({userInfo})
  },
  integralInit: function () {
    var that=this;
    var ctx = wx.createCanvasContext("integral", this);
    var {amount,nextIntegral}=that.data
    var cirWidth=that.data.canvasWidth,cirHight=that.data.canvasHeight;
    // 背景圆
    ctx.arc(cirWidth/2, cirHight/2 - 10, 36, 0, 2 * Math.PI)
    ctx.setLineWidth(rpx2px(10))
    ctx.setStrokeStyle('#F2D1C7')
    ctx.setLineCap("round")
    ctx.stroke()
     
    
    // 进度圆
    ctx.beginPath()
    ctx.arc(cirWidth/2, cirHight/2 - 10, 36, 0 * Math.PI, (2*(amount/nextIntegral)) * Math.PI)
    ctx.setLineWidth(rpx2px(10))
    ctx.setStrokeStyle('#F96319')
    ctx.setLineCap("round")
    ctx.stroke()

    // 文字
    ctx.save()
    ctx.setFillStyle('#F96319')
    ctx.setTextBaseline("middle")
    ctx.setFontSize(rpx2px(18))
    ctx.fillText(`${amount}/${nextIntegral}`, cirWidth/2 - ctx.measureText(`${amount}/${nextIntegral}`).width/2 , cirHight/2 -10 ,rpx2px(108))
    
    ctx.save()
    ctx.setFillStyle('#999999')
    ctx.setTextBaseline("middle")
    ctx.setFontSize(rpx2px(18))
    ctx.fillText('累计积分', cirWidth/2 - ctx.measureText('累计积分').width/2 , cirHight/2 + 36 + 5 ,rpx2px(108))
   
    ctx.draw()
  },
  priceInit: function () {

    var ctx = wx.createCanvasContext("price", this);
    var that=this;
    var cirWidth=that.data.canvasWidth,cirHight=that.data.canvasHeight;
    // 背景圆
    ctx.arc(cirWidth/2, cirHight/2 - 10, 36, 0, 2 * Math.PI)
    ctx.setLineWidth(rpx2px(10))
    ctx.setStrokeStyle('#F2D1C7')
    ctx.setLineCap("round")
    ctx.stroke()

    // 进度圆
    ctx.beginPath()
    ctx.arc(cirWidth/2, cirHight/2 - 10, 36, -0.5 * Math.PI, 0.5 * Math.PI)
    ctx.setLineWidth(rpx2px(10))
    ctx.setStrokeStyle('#F96319')
    ctx.setLineCap("round")
    ctx.stroke()

    // 文字
    ctx.save()
    ctx.setFillStyle('#F96319')
    ctx.setTextBaseline("middle")
    ctx.setFontSize(rpx2px(18))
    ctx.fillText('480/1000', cirWidth/2 - ctx.measureText('480/1000').width/2 , cirHight/2 - 10 ,rpx2px(108))
    
    ctx.save()
    ctx.setFillStyle('#999999')
    ctx.setTextBaseline("middle")
    ctx.setFontSize(rpx2px(18))
    ctx.fillText('累计购买金额 (￥)', cirWidth/2 - ctx.measureText('累计购买金额 (￥)').width/2 , cirHight/2 + 36 + 5  ,rpx2px(198))
   
    ctx.draw()
  }
})