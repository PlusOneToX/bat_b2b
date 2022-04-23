// pages/ucenter/extension/extension.js
var util = require('../../../utils/util');


var _extends = Object.assign || function(target) {
  for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];
      console.log(source);
      for (var key in source) {
          if (Object.prototype.hasOwnProperty.call(source, key)) {
              target[key] = source[key];
          }
      }
  }
  return target;
};


function saveImageToPhotosAlbum(option) {
  return new Promise(function(resolve, reject) {
      wx.saveImageToPhotosAlbum(_extends({}, option, {
          success: resolve,
          fail: reject
      }));
  });
}

function createRpx2px() {
  var _wx$getSystemInfoSync = wx.getSystemInfoSync(), windowWidth = _wx$getSystemInfoSync.windowWidth;
  return function(rpx) {
      return rpx / 2;
      // return windowWidth / 750 * rpx
      };
}

function canvasToTempFilePath(option, context) {
  return new Promise(function(resolve, reject) {
      wx.canvasToTempFilePath(_extends({}, option, {
          success: resolve,
          fail: reject
      }), context);
  });
}


function getImageInfoPath(url) {
  return new Promise(function (resolve, reject) {
    wx.getImageInfo(_extends({}, {src:url}, {
      success: resolve,
      fail: reject
    }))
  })

}

var rpx2px = createRpx2px();


Page({

  /**
   * 页面的初始数据
   */
  data: {
    header:0,//顶部切换
    tabActive: 0,//团队切换
    faceShow:false,//分享
    inviteCode:"",
    imageFile:'',//下载图片
    qrImg:"",//二维码
    scrollTop:0,//设置滚动位置
    canvasWidth: rpx2px(750),
    canvasHeight: rpx2px(1330),
    teamData:"",//我的团队
    shopData:"",//我的店铺
    pageSize:20,
    pageIndex:1,
    triggered:false,
    hasMoreData:false,
    recordDataSource:[],//邀请记录数据
    recordTotal:0,//邀请记录总数
    rewardDataSource:[],//推广收入数据
    rewardTotal:0,//推广收入总数
    shopDataSource:[],//店铺记录
    shopTotal:0,//店铺记录总数
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
   var that=this
   that.getUserinfo();
   that.data.header==0 && that.myTeam();
   that.data.header==1 && that.myShop();
  },
  // 我的团队
  myTeam:function(){
   const that=this;
    that.teamInfo();
    that.refresherrefresh()
  },
  // 我的店铺
  myShop:function(){
    const that=this;
    that.refresherrefresh();
    util.request('user/shopDistributionVo').then(function(res){
      that.setData({
        shopData:res.data
      })
    })
  },
  // 团队统计
  teamInfo:function(){
    const that=this;
    util.request('user/teamDistributionVo').then(function(res){
      that.setData({
        teamData:res.data
      })
    })
  },
  // 获取邀请码
  getUserinfo:function(){
    var that=this
    util.request('user/userInfo').then(function (res) {
     if(res.code==0){
       that.setData({
        inviteCode:res.data.inviteCode
       })
     }
    })
  },
  // 店铺详情
  shopDetail:function(event){
    wx.navigateTo({
      url:`/pages/ucenter/shopDetail/shopDetail?id=${event.currentTarget.dataset.id}`
    })
  },
  // 团队，店铺切换
  checkHeade:function(event){
    var that = this,
    type = event.currentTarget.dataset.type;
    if (type == that.data.header) return
    that.setData({
      header:type
    },function(){
      that.data.header==0 && that.myTeam();
      that.data.header==1 && that.myShop();
    })
  },
  // tab切换
  checkTab: function (event) {
    var that = this,
    type = event.currentTarget.dataset.type;
    if (type == that.data.tabActive) return
      that.setData({
        tabActive:type
      },function(){
        that.refresherrefresh()
    }) 
  },
  // 邀请记录
  getRecordData:function(){
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
  // 推广收入
  getRewardData:function(){
    const that=this;
    let {pageSize,pageIndex}=that.data
    util.request('user/getDistributionRecord',{pageSize:pageSize,pageNo:pageIndex},"POST").then(function (res) {
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
  // 店铺记录
  getShopData:function(){
    const that=this;
    let {pageSize,pageIndex}=that.data
    util.request('user/shopRecord',{pageSize:pageSize,pageNo:pageIndex},"POST").then(function (res) {
      that.setData({
        triggered:false,
        shopDataSource:that.data.shopDataSource.concat(res.data.records),
        shopTotal:res.data.total,
        hasMoreData:that.data.shopDataSource.concat(res.data.records).length >= res.data.total
      })
    }).catch(function(){
      that.setData({
        triggered:false
      })
    })
  },
  // 下拉刷新
  refresherrefresh:function(){
    var that = this;
    that.data.pageSize=20;
    that.data.pageIndex=1;
    if(that.data.header==0){
      if(that.data.tabActive==0){
        that.setData({
          scrollTop:0,
          recordTotal: 0,
          recordDataSource: []
        }, () => {
          that.getRecordData()
        })
      }else if(that.data.tabActive==1){
        that.setData({
          scrollTop:0,
          rewardTotal: 0,
          rewardDataSource: []
        }, () => {
          that.getRewardData()
        })
      }
    }else if(that.data.header==1){
      that.setData({
        scrollTop:0,
        shopTotal: 0,
        shopDataSource: []
      }, () => {
        that.getShopData()
      })
    }
  },
  // 上拉加载更多
  scrolltolower:function(){
    const that=this;
    if(that.data.header==0){
      if(that.data.tabActive==0 && that.data.recordDataSource.length >= that.data.recordTotal )return;
      if(that.data.tabActive==1 && that.data.rewardDataSource.length >= that.data.rewardTotal )return;
    }else if(that.data.header==1){
      if(that.data.shopDataSource.length >= that.data.shopTotal)return;
    }
    that.data.pageSize=20;
    that.data.pageIndex += 1;
    that.data.tabActive == 0 && that.data.tabActive==0 && that.getRecordData()
    that.data.tabActive == 0 && that.data.tabActive==1 && that.getRewardData()
    that.data.tabActive == 1 && that.getShopData()
  },
  // 获取二维码
  faceShow:function(){
   var that=this;
    util.request('reward/getInviteQrCode').then(function (res) {
      if (res.code == 0) {
        that.setData({
          faceShow:true,
          qrImg:res.data.qrCodeUrl
        },()=>{
          that.getAllImg()
        })
      }
    })
  },
  // 保存海报
  preservation: function () {
    var imageFile = this.data.imageFile;
    if (imageFile) {
      saveImageToPhotosAlbum({
        filePath: imageFile
      }).then(function () {
        wx.showToast({
          title: "保存存成功，快去分享吧！",
          icon: "none",
          mask: true,
          duration: 2000
        })
      }).catch(err=>{
        wx.showToast({
          title: "海报保存失败！",
          icon: "none",
          mask: true,
          duration: 2000
        })
      })
    }else{
      wx.showToast({
        title: "海报生成失败",
        icon: "none",
        mask: true,
        duration: 2000
      })
    }
  },
  // 获取所有图片
  getAllImg:function(){

    var that=this
    if(!that.data.qrImg)return;

    // var InviteImg=getImageInfoPath('https://excellent-1304180178.cos.ap-guangzhou.myqcloud.com/20201119/b1955be372a349ee87da43dc90c6846c.png');
    var InviteImg=getImageInfoPath('https://excellent-1304180178.cos.ap-guangzhou.myqcloud.com/20210201/cbdc216a5e574ed2915a591e6cf98a54.png');
    var qrImg=getImageInfoPath(that.data.qrImg);
    Promise.all([InviteImg,qrImg]).then(result => {
       that.darw(result[0].path,result[1].path)
    })

  },
  darw:function(InviteImg,qrImg){
    var _this2 = this;
    var self = this;
    var _self$data = self.data, canvasWidth = _self$data.canvasWidth, canvasHeight = _self$data.canvasHeight;
    var ctx = wx.createCanvasContext("share",this);

    
        // 画分享背景
    ctx.drawImage(InviteImg, 0,0,canvasWidth,canvasHeight);


    // 二维码盒子
    ctx.rect(rpx2px(197), rpx2px(850), rpx2px(357), rpx2px(340))
    self.roundRect(ctx,rpx2px(197), rpx2px(850), rpx2px(357), rpx2px(340), rpx2px(10))
    ctx.setFillStyle('#FFFFFF')
    ctx.fillRect(rpx2px(197), rpx2px(850), rpx2px(357), rpx2px(340));
    ctx.setShadow(0,rpx2px(3),rpx2px(13),'rgba(221, 0, 24, 0.36)')

    ctx.drawImage(qrImg, rpx2px(197),rpx2px(850),rpx2px(357),rpx2px(340),rpx2px(405),rpx2px(800));


    ctx.draw(false, function () {
      canvasToTempFilePath({
          canvasId: "share"
      }, _this2).then(function(_ref) {
          var tempFilePath = _ref.tempFilePath;
          _this2.setData({
              imageFile: tempFilePath
          });
      });
    })

  },
  faceHiden:function(){
    var that=this;
    that.setData({
      faceShow:false
    })
  },
    roundRect: function(ctx, x, y, w, h, r) {
      // 开始绘制
      ctx.beginPath();
      // 因为边缘描边存在锯齿，最好指定使用 transparent 填充
                  ctx.setFillStyle("transparent");
      // ctx.setStrokeStyle('transparent')
      // 绘制左上角圆弧
                  ctx.arc(x + r, y + r, r, Math.PI, Math.PI * 1.5);
      // 绘制border-top
                  ctx.moveTo(x + r, y);
      ctx.lineTo(x + w - r, y);
      ctx.lineTo(x + w, y + r);
      // 绘制右上角圆弧
                  ctx.arc(x + w - r, y + r, r, Math.PI * 1.5, Math.PI * 2);
      // 绘制border-right
                  ctx.lineTo(x + w, y + h - r);
      ctx.lineTo(x + w - r, y + h);
      // 绘制右下角圆弧
                  ctx.arc(x + w - r, y + h - r, r, 0, Math.PI * .5);
      // 绘制border-bottom
                  ctx.lineTo(x + r, y + h);
      ctx.lineTo(x, y + h - r);
      // 绘制左下角圆弧
                  ctx.arc(x + r, y + h - r, r, Math.PI * .5, Math.PI);
      // 绘制border-left
                  ctx.lineTo(x, y + r);
      ctx.lineTo(x + r, y);
      ctx.fill();
      // ctx.stroke()
      // 剪切
                  ctx.clip();
      ctx.closePath();
  },
  onShareAppMessage:function(){
    var that=this;
    return {
      title: '从传递一款好酒开始。',
      path: `/pages/auth/btnAuth/btnAuth?scene=${that.data.inviteCode}`,
      imageUrl:"https://excellent-1304180178.cos.ap-guangzhou.myqcloud.com/image/invite.png"
    }
  }
})