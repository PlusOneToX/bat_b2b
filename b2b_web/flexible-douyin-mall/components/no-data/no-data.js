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
        materialId: tt.getStorageSync('materialId'),
        modelId: tt.getStorageSync('modelId'),
        modelName: tt.getStorageSync('modelName'),
      });

      tt.navigateTo({
        url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
      });
    },
  }
})