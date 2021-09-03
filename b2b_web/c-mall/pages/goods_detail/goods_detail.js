// pages/goods_detail/goods_detail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    minusEnable:true,
    plusEnable:true,
    isShowSkuMenu:false,
    isShowServiceMenu:false,
    isShowGoodsParamMenu:false,
    isClickGoodsCellSku:false,
    tags:["全场满200.00元包邮","会员折上折"],
    goodsObj:{
      pics:[
        {
          pics_id:1,
          pics_url:"../../assets/images/good_detail_main.png",
        },
        {
          pics_id:1,
          pics_url:"../../assets/images/good_info.png",
        },
        {
          pics_id:1,
          pics_url:"../../assets/images/good_detail_main.png",
        }
      ],
      goods_num:1,
      skuList:[
        {
          sku_id:0,
          sku_name:"尺码",
          list:["S","M","L"],
          activeIndex:0
        },
        {
          sku_id:1,
          sku_name:"颜色分类",
          list:["黄色短款","蓝色短款"],
          activeIndex:1
        }
      ]
    },

    serviceList:[
      "支持15天退换",
      "极速退款（满足相应条件，诚信退货寄出，享受极速退款）",
      "假一赔十（正品保障）",
    ],

    goodsParamList:[
      {
        id:1,
        title:"型号",
        desc:"华为nova2s",
      },
      {
        id:2,
        title:"材质",
        desc:"软壳",
      },
      {
        id:3,
        title:"颜色分类",
        desc:"简笔画绿色小清新",
      }
    ],

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  addToCart: function() {
    wx.showToast({
      title: '加入购物车成功',
      icon: 'success',
      mask: true
    })
  },

  /**
   * 立即购买
   */
  toBuy: function() {

    const { isShowSkuMenu,isClickGoodsCellSku } = this.data;
    this.setData({
      isShowSkuMenu: !isShowSkuMenu,
      isClickGoodsCellSku: false
    })

  },

  /**
   * 关闭规格选择弹出框
   */
  closePopupTap() {
    this.setData({
      isShowSkuMenu: false,
      isClickGoodsCellSku: false
    })
  },

  hanleConfirm() {
    console.log("------------");
  },

  handleNumEdit(e) {

    const operation = e.currentTarget.dataset.index;
    const goods_id = e.currentTarget.dataset.id;
    let goods = this.data.goodsObj;

    if (goods.goods_num===1 && operation===-1) {
      //do nothing
    } else {
      goods.goods_num += operation;
      this.setData({
        goodsObj:goods
      })
    }
  },

  //规格选择
  skuItemClick(e) {
    const { index, id } = e.currentTarget.dataset;
    var goodsObj = this.data.goodsObj;
    var skuList = goodsObj.skuList;
    var sku = skuList.find(v=>v.sku_id==id);
    if (sku != undefined) {
      sku.activeIndex = index;
    }

    this.setData({
      goodsObj
    })
  },

  hanleSkuConfirm() {
    wx.navigateTo({
      url: '/pages/order_confirm/order_confirm',
    })

  },

  paramItemClick(e) {
    const { id } = e.currentTarget.dataset;
    const { isShowSkuMenu,isShowServiceMenu, isShowGoodsParamMenu } = this.data;
    if (id == 0) {
      this.setData({
        isShowSkuMenu: !isShowSkuMenu,
        isClickGoodsCellSku: true
      }) 
    } else if (id == 1) {
      this.setData({
        isShowServiceMenu:!isShowServiceMenu,
      }) 

    } else if (id == 2) {
      this.setData({
        isShowGoodsParamMenu:!isShowGoodsParamMenu,
      }) 
    }
  },

  handlePrevewImage(e) {
    const current = e.currentTarget.dataset.url;
    const urls = this.data.goodsObj.pics.map(v=>v.pics_url);
    wx.previewImage({
      current:current,
      urls: urls,
    })
  },

  finishButtonClicked() {
    this.setData({
      isShowServiceMenu:false,
      isShowGoodsParamMenu:false,
    }) 
  }

})