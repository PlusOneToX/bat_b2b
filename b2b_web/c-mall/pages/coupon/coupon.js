// pages/coupon/coupon.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tabList:[
      {
        id:0,
        type:1,
        value:"未领取",
        isActive: true
      },
      {
        id:1,
        value:"已领取",
        type:2,
        isActive:false
      },
      {
        id:2,
        value:"已使用",
        type:3,
        isActive:false
      },
      {
        id:3,
        value:"已过期",
        type:4,
        isActive:false
      }
    ],

    couponList:[
      {
        id:0,
        vaule:6,
        threshold:"",
        type:1,
        thresholdType:1,
        subCate:"",
        range:"",
        time:"",
        coupontype:"",
        isShowDetail:false,
        tips:[
        ]
      }
    ],
    newCouponList:[],
    couponDisable: false, //优惠券已使用或已过期

  },

   /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {

    const type = 1;
    this.changeTabsTitle(type);
    this.getCoupons(type);

  },

  changeTabsTitle(type) {

    const {tabList} = this.data;
    tabList.forEach(v=>v.type===type? v.isActive=true : v.isActive=false);
    this.setData({
      tabList
    })
  },

  tabsItemClick: function(e) {

    const {index} = e.currentTarget.dataset;
    const {tabList} = this.data;
    const type = tabList[index].type;

    this.changeTabsTitle(type);
    this.getCoupons(type);
  },

  getCoupons(type) {

    const {couponList} = this.data;
    let newCouponList = couponList.filter(v=>v.type===type)
    newCouponList.forEach(v=>{
      v.couponDisable = false;
      v.couponDisable=(v.type===3 || v.type===4)
    })
    this.setData({
      newCouponList
    });

  },

  seeCouponDetail(e) {
    const {index} = e.currentTarget.dataset;
    let newCouponList = this.data.newCouponList;
    newCouponList[index].isShowDetail = !newCouponList[index].isShowDetail;

    this.setData({
      newCouponList
    })
  }

})