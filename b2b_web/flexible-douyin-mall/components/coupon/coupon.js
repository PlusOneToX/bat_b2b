// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Component({
  data: {
    showExplain: false, // 是否显示优惠券说明
  },
  properties: {
    couponFlag: [{
      type: Boolean,
      value: false
    }],
    couponTotal: [{
      type: Number,
      value: 0
    }],
    curTab: [{
      type: Number,
      value: 1
    }],
    couponTabs: [{
      type: Array,
      value: []
    }],
    couponList: [{
      type: Array,
      value: []
    }]
  },
  methods: {
    // 优惠券状态tab切换
    handleClickTab(e) {
      let curTab = e.currentTarget.dataset.id;
      this.triggerEvent("tabClick", curTab);
    },
    // 优惠券说明
    toggleExplain(e) {
      let index = e.currentTarget.dataset.index;
      this.triggerEvent("explainClick", index);
    },
    // 加载更多
    loadMore() {
      this.triggerEvent("loadMore");
    },
    // 优惠券 - 立即领取
    handleReceiveCoupon(e) {
      let item = e.currentTarget.dataset.item;
      // status: 1 待领取
      if (this.data.curTab === 1) {
        $request
          .post(api.receiveCoupon, { ids: [item.couponId] })
          .then((res) => {
            if (res.success) {
              tt.showToast({
                title: "领取成功",
                icon: "success",
                duration: 2000,
              });
              // 刷新
              this.triggerEvent("tabClick", this.data.curTab);
            } else {
              tt.showToast({
                title: res.errMessage,
                icon: "none",
                duration: 2000,
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    // 优惠券 - 立即使用
    handleUseCoupon(e) {
      let item = e.currentTarget.dataset.item;
      if (this.data.couponFlag) {
        // 订单页 - 选中
        if (item.goodsEnable === 1 && item.amountEnable === 1 && item.couponStatus === 2) {
          this.triggerEvent("handleItem", item);
        }
      } else {
        // 优惠券页面 - 跳转定制页
        let enterParams = JSON.stringify({
          materialId: tt.getStorageSync('materialId'),
          modelId: tt.getStorageSync('modelId'),
          modelName: tt.getStorageSync('modelName'),
        });

        tt.navigateTo({
          url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
        });
      }
    },
    // 优惠券 - 不使用优惠券
    cancelCoupon() {
      this.triggerEvent("cancelItem");
    },
  }
})