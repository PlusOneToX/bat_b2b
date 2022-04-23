var util = require('../../utils/util.js')
const app = getApp()

Page({
  data: {
    // text:"这是一个页面"
    navList: [],
    goodsList: [],
    id: '',
    pId: '',
    currentCategory: {},
    scrollLeft: 0,
    scrollTop: 0,
    page: 1,
    size: 10000
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    var that = this
    if (options.id) {
      that.setData({
        id: options.id
      })
    }
    if (options.pId) {
      that.setData({
        pId: options.pId
      })
    }
    this.getCategoryInfo()
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  getCategoryInfo: function () {
    let that = this

    util.request('category/current', {
      id: that.data.id
    }).then(function (res) {
      if (res.code == 0) {
        that.setData({
          currentCategory: res.data
        })
        let list = res.data.subCategoryList
        if (list && list.length > 0) {
          that.setData({
            navList: list
          })
        } else {
          if (that.data.pId) {
            util.request('category/current', {
              id: that.data.pId
            }).then(function (resp) {
              if (resp.code == 0) {
                that.setData({
                  navList: resp.data.subCategoryList
                })
              }
            })
          }
        }
      }
    })
    that.getGoodsList()
  },
  getGoodsList: function () {
    var that = this
    util.request('goods/list', {
      categoryId: that.data.id,
      current: that.data.page,
      limit: that.data.size
    }).then(function (res) {
      that.setData({
        goodsList: res.data.records,
      })
    })
  },
  switchCate: function (event) {
    if (this.data.id == event.currentTarget.dataset.id) {
      return false
    }
    this.setData({
      id: event.currentTarget.dataset.id
    })
    this.getCategoryInfo()
  }
})
