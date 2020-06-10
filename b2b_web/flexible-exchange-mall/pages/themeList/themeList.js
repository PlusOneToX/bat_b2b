// pages/themeList/themeList.js
// Toast
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 主题类型
    themeTitle: [{
        name: "全部",
        id: "",
      },
      {
        name: "IP素材",
        id: 2,
      },
      {
        name: "原创素材",
        id: 1,
      },
      // {
      //   name: "模板",
      //   id: 3,
      // },
    ],
    distributorId: "", // 分销商id
    page: 1, // 主题列表分页
    size: 3, // 主题列表条数
    modelId: "", // 型号id
    modelName: "", // 型号名称
    materialId: "", // 材质id
    curType: "", // 当前主题类型
    themeList: [], // 主题列表
  },

  // 获取官方主题列表
  getThemeListData() {
    Toast.loading({
      duration: 0, // 持续展示 toast
      message: '加载中...',
      forbidClick: true,
      loadingType: 'spinner',
      selector: '#van-toast',
    });
    $request
      .get(api.getThemeList, {
        distributorId: this.data.distributorId,
        page: this.data.page,
        size: this.data.size,
        modelId: this.data.modelId,
        materialId: this.data.materialId,
        type: this.data.curType,
      })
      .then((res) => {
        if (res.success) {
          this.setData({
            themeList: (res.data ? res.data : [])
          })
        } else {
          Toast.fail(res.errMessage);
        }
        Toast.clear();
      });
  },

  // 点击主题名称
  handleClickTheme(e) {
    let curType = e.detail.name;
    this.setData({
      curType: (curType ? curType : '')
    })
    // 获取官方主题列表
    this.getThemeListData();
  },

  // 跳转图库详情
  goDetailList(e) {
    let theme = e.currentTarget.dataset.item;

    let enterParams = JSON.stringify({
      modelId: this.data.modelId,
      modelName: encodeURIComponent(encodeURIComponent(this.data.modelName)),
      materialId: this.data.materialId,
      type: theme.type,
      categoryId: theme.categoryId,
    });
    wx.navigateTo({
      url: '/pages/webview/webview?enterFlag=themeDetail&enterParams=' + enterParams,
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let modelId = wx.getStorageSync('modelId') ?
      wx.getStorageSync('modelId') :
      "";
    let modelName = wx.getStorageSync('modelName') ?
      wx.getStorageSync('modelName') :
      "";
    let materialId = wx.getStorageSync('materialId') ?
      wx.getStorageSync('materialId') :
      "";

    this.setData({
      modelId: modelId,
      modelName: modelName,
      materialId: materialId
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let distributorId = wx.getStorageSync('distributorId');
    this.setData({
      distributorId: distributorId
    })


    this.getThemeListData();
  },

  /**
   * 生命周期函数--当前是 tab 页时，点击 tab 时触发
   */
  onTabItemTap(e) {
    Toast.clear();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})