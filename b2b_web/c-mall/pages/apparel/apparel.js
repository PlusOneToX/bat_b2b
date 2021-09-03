// pages/apparel/apparel.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
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
        image_url:"../../assets/images/banner.png",
        link:"",
      },
      {
        id:"2",
        image_url:"../../assets/images/banner.png",
        link:"",
      },
      {
        id:"3",
        image_url:"../../assets/images/banner.png",
        link:"",
      }
    ],
    seckillList:[
      {
        id:"0",
        imageSrc:"../../assets/images/img-seckill.png",
        curPrice:"39.90",
        oriPrice:"59.90"
      },
      {
        id:"1",
        imageSrc:"../../assets/images/img-seckill.png",
        curPrice:"29.90",
        oriPrice:"49.90"
      },
      {
        id:"2",
        imageSrc:"../../assets/images/img-seckill.png",
        curPrice:"59.90",
        oriPrice:"89.90"
      }
    ],
    likeList:[
      {
        id:"0",
        imageSrc:"../../assets/images/like_image.png",
        title:"服装",
        curPrice:"59.90",
        oriPrice:"89.90",
        tags:["满500减100","秒杀"]
      },
    ],
    tabList:[
      {
        id:0,
        value:"女装",
        isActive: true
      },
      {
        id:1,
        value:"男装",
        isActive:false
      },
      {
        id:2,
        value:"童装",
        isActive:false
      }
    ],

    seckillInterval:9999,
    isTimerOut:false,
    countdown:{
      hour: '00',
      minute: '00',
      second: '00'
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.startCountDown(28800000);

  },

  onHide: function () {
    this.clearinterval();
  },
  
  onUnload: function () {
    this.clearinterval();
  },

  handleTabsItemChange: function(e){
    const {index} = e.detail;
    const {tabList} = this.data;

    tabList.forEach((v,i)=>v.id===index?v.isActive=true:v.isActive=false);
    this.setData({
      tabList
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
  }

})