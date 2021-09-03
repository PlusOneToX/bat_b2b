// component/toast-loading/index.js
Component({
  properties: {
    // 这里定义了innerText属性，属性值可以在组件使用时指定
    innerText: {
      type: String,
      value: 'default value',
    }
  },

  data: {
    dataText: '',
    isShow:false
  },

  methods: {
    //隐藏弹框
    hideLoading() {
      this.setData({
        isShow: !this.data.isShow
      })
    },

    //显示弹框
    showLoading(data) {

      this.setData({
        isShow: !this.data.isShow,
        dataText: data
      })
    }
  }
})