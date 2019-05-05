<template>
  <view class="promotion-wrap" v-show="showProPromotion">
    <uni-icons
      type="close"
      size="26"
      color="#FFF"
      @click="handleClose"
    ></uni-icons>
    <view class="inner-content">
      <div
        class="img-wrap"
        v-for="(item, index) in promotionList"
        :key="index"
        @click="handleLink(item.promotionUrl)"
      >
        <img :src="item.promotionImg" :alt="item.extensionGoodsName" />
      </div>
    </view>
  </view>
</template>

<script>
import { proPromotionData } from "../../common/api.js";

export default {
  name: "proProduction",
  data() {
    return {
      showProPromotion: false,
      promotionList: [],
    };
  },
  created() {
		this.getProPromotionData();
  },
  methods: {
    // 获取产品推广
    getProPromotionData() {
      proPromotionData().then((res) => {
        if (res.success) {
          if (res.data && res.data.moCnExtensionImgUrl) {
            let promotionList = {
              extensionGoodsName: res.data.extensionGoodsName,
              promotionImg: res.data.moCnExtensionImgUrl,
              promotionUrl: res.data.moCnExtensionGoodsUrl,
            };
            this.promotionList.push(promotionList);

            this.showProPromotion = true;
          }
        }
      });
    },
    // 点击图片跳转
    handleLink(link) {
      if (link) {
        this.showProPromotion = false;
        uni.navigateTo({
          url: link,
        });
      }
    },
    // 关闭产品推广弹窗
    handleClose() {
      this.showProPromotion = false;
    },
  },
};
</script>

<style lang="scss">
.promotion-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 10000;

  uni-icons, .uni-icons {
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -328rpx;
    margin-left: 248rpx;
  }

  .inner-content {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 518rpx;
    height: 518rpx;
    background: #d8d8d8;
    border-radius: 16rpx;
    border: 1px solid #979797;
    transform: translate(-50%, -50%);
    overflow: hidden;

    .img-wrap {
      width: 100%;
      height: 100%;
      position: relative;
      border-radius: 16rpx;
      overflow: hidden;

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        max-width: 100%;
        max-height: 100%;
        transform: translate(-50%, -50%);
      }
    }
  }
}
</style>