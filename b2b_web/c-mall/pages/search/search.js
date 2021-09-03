// pages/search/search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    searchHistory: []

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  search: function(e) {
		this.setData({
			searchHistory: [...this.data.searchHistory,e.detail.value]
    })
    
    wx.navigateTo({
      url: '/pages/search_detail/search_detail'
    })
  },
  
  clearHistory: function() {
    this.setData({
      searchHistory:[]
    })
  },

  cancelSearch: function() {
    wx.navigateBack({
      delta: 1
    })

  }

})