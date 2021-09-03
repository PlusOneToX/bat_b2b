// pages/order/order.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tabList:[
      {
        id:0,
        type:0,
        value:"全部订单",
        isActive: true
      },
      {
        id:1,
        type:1,
        value:"待付款",
        isActive:false
      },
      {
        id:2,
        type:2,
        value:"待发货",
        isActive:false
      },
      {
        id:3,
        type:3,
        value:"待收货",
        isActive:false
      },
      {
        id:4,
        type:4,
        value:"已完成",
        isActive:false
      },
    ],

    orderList:[
      {
        id:"265432562363",
        type:1,
        tag:"待付款",
        tagIcon:"../../assets/images/unpaid.png",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          },
          {
            goods_id:2,
            goods_name:"适用于苹果12中国风腕带手机壳11华",
            goods_price:"28.99",
            goods_param:"华为pro，蓝色鲸鱼",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          },
        ]
      },
      {
        id:"265432562364",
        type:1,
        tag:"待付款",
        tagIcon:"../../assets/images/unpaid.png",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          }
        ]
      },
      {
        id:"265432562367",
        type:2,
        tag:"待发货",
        tagIcon:"../../assets/images/unshipped.png",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          },
          {
            goods_id:2,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          },
        ]
      },
      {
        id:"265432562368",
        type:2,
        tag:"待发货",
        tagIcon:"../../assets/images/unshipped.png",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          }
        ]
      },
      {
        id:"375432562368",
        type:3,
        tag:"待收货",
        tagIcon:"../../assets/images/unreceived.png",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          }
        ]
      },
      {
        id:"475432562368",
        type:4,
        tag:"交易成功",
        tagIcon:"../../assets/images/unreceived.png",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          },
          {
            goods_id:2,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          },
          {
            goods_id:3,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_icon:"../../assets/images/good_image.png"
          }
        ]
      }
    ],
    newOrderList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    const type = Number(options.type);

    const {orderList} = this.data;
    wx.setStorageSync('orders', orderList);

    this.changeTabsTitle(type);
    this.getOrders(type);

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
    this.getOrders(type);
  },

  goOrderDetail: function(e) {
    const { id } = e.currentTarget.dataset;

    wx.navigateTo({
      url: '/pages/order_detail/order_detail?id=' + id
    })
  },

  getOrders(type) {

    const {orderList} = this.data;
    let newOrderList = orderList.filter(v=>v.type===type)
    if (type===0){
      newOrderList = orderList;
    }

    this.setData({
      newOrderList
    });

  },

  toPay(e) {
    const { id } = e.currentTarget.dataset;

    wx.navigateTo({
      url: '/pages/order_detail/order_detail?id=' + id
    })

  },

  seeLogistics() {

  },

  confirmReceive() {

  },

  seeLogistics() {
    wx.navigateTo({
      url: '/pages/goods_logistics/goods_logistics'
    })

  }

})