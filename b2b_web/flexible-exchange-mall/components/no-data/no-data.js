// components/no-data/no-data.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    flagType: {
      type: String,
      value: ''
    }
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 立即定制
    goCustom() {
      let enterParams = JSON.stringify({
        materialId: wx.getStorageSync('materialId'),
        modelId: wx.getStorageSync('modelId'),
        modelName: wx.getStorageSync('modelName'),
      });

      wx.navigateTo({
        url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
      });
    },
  }
})