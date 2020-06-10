// Api
import $request from '/assets/api/request'
import api from '/assets/api/allApi'

Component({
  data: {
    showExplain: false, // 是否显示优惠券说明
  },
  props: {
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
      this.props.onTabClick(curTab);
    },
    // 优惠券说明
    toggleExplain(e) {
      let index = e.currentTarget.dataset.index;
      this.props.onExplainClick(index);
    },
    // 加载更多
    loadMore() {
      this.props.onLoadMore();
    },
    // 优惠券 - 立即领取
    handleReceiveCoupon(e) {
      let item = e.currentTarget.dataset.item;
      // status: 1 待领取
      if (this.props.curTab === 1) {
        $request
          .post(api.receiveCoupon, {
            ids: [item.couponId]
          })
          .then((res) => {
            if (res.success) {
              my.showToast({
                content: "领取成功",
                type: "success",
                duration: 2000,
              });
              // 刷新
              this.props.onTabClick(this.props.curTab);
            } else {
              my.showToast({
                content: res.errMessage,
                type: "none",
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
      if (this.props.couponFlag) {
        // 订单页 - 选中
        if (item.goodsEnable === 1 && item.amountEnable === 1 && item.couponStatus === 2) {
          this.props.onHandleItem(item);
        }
      } else {
        // 优惠券页面 - 跳转定制页
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
      }
    },
    // 优惠券 - 不使用优惠券
    cancelCoupon() {
      this.props.onCancelItem();
    },
  }
})