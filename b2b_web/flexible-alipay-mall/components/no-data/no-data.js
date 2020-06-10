Component({
  data: {

  },
  properties: {
    flagType: {
      type: String,
      value: ''
    }
  },
  methods: {
    // 立即定制
    goCustom() {
      let enterParams = JSON.stringify({
        materialId: my.getStorageSync({
          key: 'materialId'
        }).data || '',
        modelId: my.getStorageSync({
          key: 'modelId'
        }).data || '',
        modelName: my.getStorageSync({
          key: 'modelName'
        }).data,
      });

      my.navigateTo({
        url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
      });
    },
  }
})