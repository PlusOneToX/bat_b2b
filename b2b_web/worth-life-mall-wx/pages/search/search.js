var util = require('../../utils/util.js');

Page({
  data: {
    keyword: '',
    searchStatus: false,
    goodsList: [],
    helpKeyword: [],
    historyKeyword: [],
    categoryFilter: false,
    filterCategory: [],
    defaultKeyword: {},
    hotKeyword: [],
    current: 1,
    limit: 20,
    sortType: 'desc',
    order: 'id',
    categoryId: '',
  },
  //事件处理函数
  closeSearch: function () {
    wx.navigateBack({
      fail() {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    })
  },
  clearKeyword: function () {
    this.setData({
      keyword: '',
      searchStatus: false
    });
  },
  onLoad: function (option) {
    wx.setStorageSync("navUrl", "/pages/search/search");

    let that = this;
    if (option.keyword) {
      // that.setData({
      //   keyword: option.keyword
      // });
      that.getGoodsList()
    }
    this.getSearchKeyword();
  },
  goodsDetail: function (event) {
    let goodsId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/goods/goods?id=' + goodsId,
    })
  },
  getSearchKeyword() {
    let that = this;
    util.request('search/history').then(function (res) {
      if (res.code === 0) {
        that.setData({
          historyKeyword: res.historyKeywordList,
          defaultKeyword: res.defaultKeyword,
          hotKeyword: res.hotKeywordList
        });
      }
    });
  },

  inputChange: function (e) {

    this.setData({
      keyword: e.detail.value,
      searchStatus: false
    });
    this.getHelpKeyword();
  },
  getHelpKeyword: function () {
    let that = this;
    util.request('search/helper', {
      keyword: that.data.keyword
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          helpKeyword: res.data
        });
      }
    });
  },
  inputFocus: function () {
    this.setData({
      searchStatus: false,
      goodsList: []
    });

    if (this.data.keyword) {
      this.getHelpKeyword();
    }
  },
  clearHistory: function () {
    this.setData({
      historyKeyword: []
    })

    util.request('search/clearHistory', {}, 'POST')
      .then(function (res) {});
  },
  getGoodsList: function () {
    let that = this;
    if (!that.data.keyword) {
      return;
    }
    util.request('search/searchGoods', {
      keyword: that.data.keyword,
      searchFrom: 1,
      current: that.data.current,
      limit: that.data.limit,
      sortType: that.data.sortType,
      order: that.data.order,
      categoryId: that.data.categoryId
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          searchStatus: true,
          categoryFilter: false,
          goodsList: res.data.records,
          filterCategory: res.filterCategory,
          current: res.data.current,
          limit: res.data.size
        });
      }

      //重新获取关键词
      // that.getSearchKeyword();
    });
  },
  onKeywordTap: function (event) {
    this.getSearchResult(event.currentTarget.dataset.keyword);
  },
  getSearchResult(keyword) {
    this.setData({
      keyword: keyword,
      current: 1,
      categoryId: '',
      goodsList: []
    });

    this.getGoodsList();
  },
  openSortFilter: function (event) {
    let currentId = event.currentTarget.id;
    switch (currentId) {
      case 'categoryFilter':
        this.setData({
          'categoryFilter': !this.data.categoryFilter,
          'sortType': 'asc'
        });
        break;
      case 'priceSort':
        let tmpSortOrder = 'asc';
        if (this.data.sortType == 'asc') {
          tmpSortOrder = 'desc';
        }
        this.setData({
          'order': 'retail_price',
          'sortType': tmpSortOrder,
          'categoryFilter': false
        });

        this.getGoodsList();
        break;
      default:
        this.setData({
          'order': '',
          'sortType': '',
        });
        this.getGoodsList();
    }
  },
  selectCategory: function (event) {
    let currentIndex = event.target.dataset.categoryIndex;
    let filterCategory = this.data.filterCategory;
    let currentCategory = null;
    for (let key in filterCategory) {
      if (key == currentIndex) {
        filterCategory[key].selected = true;
        currentCategory = filterCategory[key];
      } else {
        filterCategory[key].selected = false;
      }
    }
    this.setData({
      'filterCategory': filterCategory,
      'categoryFilter': false,
      categoryId: currentCategory.id || '',
      current: 1,
      goodsList: []
    });
    this.getGoodsList();
  },
  onKeywordConfirm(event) {
    this.getSearchResult(event.detail.value);
  }
})
