// pages/home/home.js

import httpRequest from '../../utils/request';
import api from '../../utils/allApi'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    hasNetwork: true, //判断是否有网络
    swiperCurrent:0,
    bannerList1:[],
    bannerList: [
      {
        id:"0",
        image_url:"../../assets/images/banner.png",
        link:"",
      },
      {
        id:"1",
        image_url:"../../assets/images/banner1.png",
        link:"",
      },
      {
        id:"2",
        image_url:"../../assets/images/banner2.png",
        link:"",
      },
      {
        id:"3",
        image_url:"../../assets/images/banner3.png",
        link:"",
      }
    ],
    cateList:[
      {
        id:"0",
        imageSrc:"../../assets/images/img-erji.png",
        title:"高档服饰",
      },
      {
        id:"1",
        imageSrc:"../../assets/images/img-erji.png",
        title:"手机背膜",
      },
      {
        id:"2",
        imageSrc:"../../assets/images/img-erji.png",
        title:"iPad背膜",
      },
      {
        id:"3",
        imageSrc:"../../assets/images/img-erji.png",
        title:"耳机",
      }
    ],
    seckillList:[
      {
        id:"0",
        imageSrc:"../../assets/images/shoujike.png",
        curPrice:"39.90",
        oriPrice:"59.90"
      },
      {
        id:"1",
        imageSrc:"../../assets/images/shoujike.png",
        curPrice:"29.90",
        oriPrice:"49.90"
      },
      {
        id:"2",
        imageSrc:"../../assets/images/shoujike.png",
        curPrice:"59.90",
        oriPrice:"89.90"
      }
    ],
    seckillTimeList:[16,18,30],
    recommendSwiperList:[
      {
        id:"0",
        image_url:"../../assets/images/img-fenlei.png",
        link:"",
      },
      {
        id:"1",
        image_url:"../../assets/images/img-fenlei.png",
        link:"",
      },
      {
        id:"2",
        image_url:"../../assets/images/img-fenlei.png",
        link:"",
      }
    ],
    recommendGoodsList:[
      {
        id:"0",
        imageSrc:"../../assets/images/img-bankuai.png",
        title:"服装",
        curPrice:"59.90",
        oriPrice:"89.90"
      },
    ],
    likeList1:[
      {
        id:"0",
        imageSrc:"../../assets/images/like_image.png",
        title:"服装",
        curPrice:"59.90",
        oriPrice:"89.90",
        tags:["满500减100","秒杀"]
      }
    ],
    likeList:[],

    pagenum:1,
    pagesize:6,
    totalPages: 3,
    isLoadFinished: false,

    seckillInterval:8888,
    isTimerOut:false,
    countdown:{
      hour: '00',
      minute: '00',
      second: '00'
    }
  },

  toastLoading:null,

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getLikeList();
    this.startCountDown(28800000);
  },

  onReady: function () {
    this.toastLoading = this.selectComponent("#toastLoading");
    this.onNetworkStatusChange();
  },

  onHide: function () {
    this.clearinterval();
  },
  
  onUnload: function () {
    this.clearinterval();
  },

  onPullDownRefresh: function () {

    if (this.data.pagenum>=this.data.totalPages){
      setTimeout(() => {
        wx.stopPullDownRefresh();
        this.setData({
          isLoadFinished:true
        })
      }, 1000);
      
    } else {
       this.data.pagenum++;
       this.getLikeList();
    }
  },

  onReachBottom: function () {

    if (this.data.pagenum>=this.data.totalPages){
      this.setData({
        isLoadFinished:true
      })
      
    } else {
       this.data.pagenum++;
       this.getLikeList();
    }

  },

  getLikeList: function (){

    if (this.data.pagenum === 1) {

      this.setData({
        likeList: this.data.likeList1
      })

    } else {
      const list = this.data.likeList1;
      const newList = [...this.data.likeList,...list];

      this.toastLoading.showLoading("加载中...");
      setTimeout(() => {
        wx.stopPullDownRefresh();
        this.toastLoading.hideLoading();
        this.setData({
          likeList: newList
        })
      }, 1000);
    }
  },

  handleSwiperChange: function(e) {
    const current=e.detail.current
    this.setData({
      swiperCurrent: current
    })
  },

  handleSearchTap(){
    wx.navigateTo({
      url: '/pages/search/search'
    })


  },

  goCateDetail: function(e) {
    
    wx.navigateTo({
      url: '/pages/apparel/apparel',
    })
  },

  startCountDown: function(millisecond) {
    var that = this;
    if (millisecond > 0) {
      that.seckillInterval = setInterval(function() {
        millisecond -=1000;
        if (millisecond <=0) {
          that.setData({
            countdown:{
              hour:'00',
              minute:'00',
              second:'00'
            },
            isTimerOut:true
          });
        }
        that.transformRemainTime(millisecond)    
      }, 1000);
    } else {
      that.setData({
        isTimerOut: true
      })
    }
  },

  transformRemainTime: function(millisecond) {
    var that = this;
    var countdownObj = that.data.countdown;
    var seconds = Math.floor(millisecond / 1000)
    // 小时
    countdownObj.hour = that.formatTime(Math.floor(seconds / 3600 % 24));
    // 分钟
    countdownObj.minute = that.formatTime(Math.floor(seconds / 60 % 60));
    // 秒
    countdownObj.second = that.formatTime(Math.floor(seconds % 60));
    // 毫秒
    countdownObj.millisecond = that.formatTime(Math.floor((millisecond % 1000) / 10));
    that.setData({
      countdown: countdownObj
    });
  },

   //格式化时间为2位
   formatTime: function (time) {
    if (time < 10)
      return '0' + time;
    return time;
  },

   //清除计时器
   clearinterval: function () {
    if (this.seckillinterval) {
      clearInterval(this.seckillinterval);
      this.seckillinterval = null;
    }
  },

  gotoGoodDetail: function() {
    wx.navigateTo({
      url: '/pages/goods_detail/goods_detail',
    })

  },

  //网络检测
  onNetworkStatusChange() {
    var that = this;
    wx.getNetworkType({
      success: (res) => {
        console.log(res);
        const networkType = res.networkType;
        var hasNetwork = false;
        if (networkType == 'none') {
          hasNetwork = false;
        } else {
          hasNetwork = true;
        }

        that.setData({
          hasNetwork
        });

        wx.onNetworkStatusChange((result) => {
          console.log(result);
          var hasNetwork = false;
          if (res.isConnected) {
            //网络变为有网
            hasNetwork = true
          } else {
            //网络变为无网
            hasNetwork = false
          }

          that.setData({
            hasNetwork
          });

        })
      },
    })
  },
})