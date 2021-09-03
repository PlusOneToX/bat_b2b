// pages/cart/cart.js
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
import util from '../../utils/util';

let app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLogin: false,
    isShowAddressPopup: false,
    isAllChecked: false,
    totalPrice:0,
    totalNum:0,
    isCanEdit:true,
    cart:[
      {
        cat_id:0,
        cat_name:"新春服饰",
        goodsList:[
          {
            goods_id:1,
            goods_name:"奥黛丽的春天~方领刺绣裙子女春装女领刺绣裙子女春装女",
            goods_price:"28.00",
            goods_param:"黄色，M码",
            goods_num:1,
            goods_checked:true,
            goods_icon:"../../assets/images/good_image.png"
          }
        ]
      },
      {
        cat_id:1,
        cat_name:"炫彩手机壳",
        goodsList:[
          {
            goods_id:2,
            goods_name:"适用于苹果12中国风腕带手机壳11华",
            goods_price:"28.99",
            goods_param:"华为pro，蓝色鲸鱼",
            goods_num:1,
            goods_checked:true,
            goods_icon:"../../assets/images/good_image.png"
          },
          {
            goods_id:3,
            goods_name:"适用于苹果12中国风腕带手机壳11华",
            goods_price:"28.00",
            goods_param:"华为pro，小清新",
            goods_num:2,
            goods_checked:true,
            goods_icon:"../../assets/images/good_image.png"
          }
        ]
      }
    ],
    startX:0,
    endX:0,
    curShortAddress:"",
    addressList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    let cart = this.data.cart;
    cart.forEach(v=>{
      let goodsList = v.goodsList.forEach(v1=>{
        v1.x=0
      })
    })

    this.setCart(this.data.cart);
  },

  onShow: function () {

    const isLogin = util.isLogin();
    const addressList = wx.getStorageSync('address')||[];
    let curAddress = addressList.find(v=>v.isChecked===true)
    console.log(curAddress);
    let curShortAddress = "";
    if (addressList.length > 0) {
      if (curAddress === undefined) {
        curAddress = addressList[0];
      }         
      curShortAddress = curAddress.city + curAddress.district;
    }

    this.setData({
      curShortAddress,
      addressList,
      isLogin
    });

    if (isLogin) {
      wx.setNavigationBarTitle({
        title: '购物车'
      });
    } else {
      wx.setNavigationBarTitle({
        title: '微信授权'
      });
    }
  },

  selectAddress: function() {

    const isShowAddressPopup = this.data.isShowAddressPopup;
    this.setData({
      isShowAddressPopup: !isShowAddressPopup
    })

  },

  cateTitleClick() {

  },

  handleEdit: function() {
    const isCanEdit = this.data.isCanEdit;
    this.setData({
      isCanEdit: !isCanEdit,
    })
    
  },
  
  handleGoodsCheck: function(e) {
    const cateid = e.currentTarget.dataset.cateid;
    const goodsid = e.currentTarget.dataset.goodsid;
    let { cart } = this.data;
    let index1 = cart.findIndex(v=>v.cat_id===cateid);
    let goodsList = cart[index1].goodsList;
    let index2 = goodsList.findIndex(v=>v.goods_id===goodsid);
    goodsList[index2].goods_checked = !goodsList[index2].goods_checked;
    this.setCart(cart);
  },

  handleAllCheck: function() {
    let { cart, isAllChecked }=this.data;
    isAllChecked = !isAllChecked;
    cart.forEach(v=>{
      const goodsList=v.goodsList
      goodsList.forEach(v1=>v1.goods_checked=isAllChecked);
      this.setCart(cart)
    });
  },

  setCart: function(cart){

    let isAllChecked = true;
    let totalPrice=0;
    let totalNum=0;
    cart.forEach(v=>{
      let goodsList=v.goodsList
      goodsList.forEach(v1=>{

         if (v1.goods_checked) {
           totalPrice+=v1.goods_num*v1.goods_price;
           totalNum+=v1.goods_num;
         } else {
          isAllChecked = false;
         }

         v1.minusEnable = true;
         v1.plusEnable = true;
         //商品数量小于等于1则不可操作
         if (v1.goods_num<=1){
           v1.minusEnable = false;
         }
         //商品数量大于等于99999则不可操作
         if (v1.goods_num>=99999){
           v1.plusEnable = false;
         }

      })
    });

    this.setData({
      cart,
      isAllChecked,
      totalPrice: totalPrice.toFixed(2),
      totalNum
    });

    wx.setStorageSync("cart", cart);
  },

  gotoPay: function(e) {

    if (this.data.totalNum <= 0) {
      Toast.fail("请选择商品");
      return;
    }

    wx.navigateTo({
      url: '/pages/order_confirm/order_confirm'
    })
  },

  handleDelete: function() {
    
    // 判断是否选择了要删除的商品
    var hasChecked = false;
    var cart = this.data.cart;

    cart.forEach(v => {
      const goodsList = v.goodsList.filter(v1=>v1.goods_checked===true);
      if (goodsList.length > 0 ) {
        hasChecked = true;
      }
    });

    if ( !hasChecked){
      Toast.fail("请选择要删除的商品！");
      return;
    }

    Dialog.confirm({
      message:"确认将已选中商品都删除码？",
      cancelButtonColor: "#4A4A4A",
      confirmButtonColor: "#F94021",
    }).then(() => {
      // on confirm
      this.deleteConfim();

    }).catch(() => {
      // on cancel
    });

  },

  deleteConfim(){
    const isCanEdit = this.data.isCanEdit;
    const cart = this.data.cart;

    //重置编辑状态
    this.setData({
      isCanEdit: !isCanEdit
    })

    //删除购物车的商品
    let newCart = [];
    cart.forEach(v=>{
      const goodsList = v.goodsList.filter(v1=>v1.goods_checked===false);
      if (goodsList.length > 0) {
        let item = v;
        newCart.push(item);
        item.goodsList = goodsList;
      }
    });
    this.setCart(newCart)
  },

  handleNumEdit(e) {

    const operation = e.currentTarget.dataset.index;
    const goods_id = e.currentTarget.dataset.id;
    let cart = this.data.cart;

    cart.forEach(v=>{
      let goods =v.goodsList.find(v=>v.goods_id==goods_id);
      if (goods) {
        if (goods.goods_num===1 && operation===-1) {
          //do nothing
        } else {
          goods.goods_num += operation;
          this.setCart(cart);
        }
      }
    });
  },


  onTouchStart(event) {
    if (event.touches[0]) {
      this.startX = event.touches[0].clientX;
    }
  },

  onTouchMove(event) {

    if (event.touches[0]) {
      this.endX = event.touches[0].clientX;
    }
  },

  onTouchEnd(event) {

    if (this.startX && this.endX) {
      // 滑动结束的时候增加x的距离值
      const { index,index1 } = event.currentTarget.dataset;
      var x = "cart[" + index + "].goodsList[" + index1 + "].x";

      const rightWidth = 75;
      const limitWidth = rightWidth/2;

      let delta = this.startX - this.endX;
      //left
      if (delta > 0) {
        if (delta > limitWidth) {
          this.setData({
            [x]: -rightWidth
          })
        } else {
          this.setData({
            [x]: 0
          })
        }
      } else {
        //right
        if (Math.abs(delta) > limitWidth) {
          this.setData({
            [x]: 0
          })
        } else {
          this.setData({
            [x]: -rightWidth
          })
        }
      }
      this.startX = null;
      this.endX = null;
    }
  },

  deleteButtonClick(e) {

    const { index,index1 } = e.currentTarget.dataset;
    let cart = this.data.cart;

    if (cart[index].goodsList.length <= 1) {
      cart.splice(index,1);
    } else {
      cart[index].goodsList.splice(index1,1);
    }
    
    this.setCart(cart);

  },

  //去逛逛
  toSee() {

  },

  handleAdderssCheck(e) {
    const { index } = e.currentTarget.dataset;
    let addressList = this.data.addressList;
    let curAddress = addressList[index];
    const curShortAddress = curAddress.city + curAddress.district;
    addressList.forEach(v=>v.isChecked=false);
    curAddress.isChecked = true;
    this.setData({
      addressList,
      curShortAddress,
      isShowAddressPopup: false
    })

    wx.setStorageSync('address', addressList)
  },

  closeAddressMenu() {

    this.setData({
      isShowAddressPopup: false
    })

  },

  authSuccess(e) {

    const isLogin = util.isLogin();
    this.setData({
      isLogin
    })
  },

  selectOtherAddress() {
    wx.navigateTo({
      url: '/pages/address_add/address_add'
    })

  }

})