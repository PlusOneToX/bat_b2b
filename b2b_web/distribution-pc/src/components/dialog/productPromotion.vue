<template>
  <div class="promotion-wrap" v-show="showProPromotion">
    <i class="el-icon-circle-close" @click="handleClose"></i>
    <div class="inner-content">
      <div class="content">
        <div
          class="img-wrap"
          v-for="(item, index) in promotionList"
          :key="index"
          @click="handleLink(item.promotionUrl)"
        >
          <img :src="item.promotionImg" :alt="item.extensionGoodsName" />
        </div>
      </div>
      <img
        class="up-img"
        src="../../assets/images/index/pro-promotion.png"
        alt="产品推广"
      />
    </div>
  </div>
</template>

<script>
import { proPromotionData } from "@/apiService/api";
export default {
  name: "proProduction",
  data() {
    return {
      showProPromotion: false,
      promotionList: [],
    };
  },
  mounted() {
    if (!sessionStorage.getItem("hasShowPromotion")) {
      // 判断是否有显示产品推广（默认只显示一次）
      this.getProPromotionData();
    }
  },
  methods: {
    // 获取产品推广
    getProPromotionData() {
      proPromotionData().then((res) => {
        if (res.success) {
          sessionStorage.setItem("hasShowPromotion", true);
        }
        if (res.success && res.data) {
          if (this.$i18n.locale === "zh" && res.data.pcCnExtensionImgUrl) {
            let promotionList = {
              extensionGoodsName: res.data.extensionGoodsName,
              promotionImg: res.data.pcCnExtensionImgUrl,
              promotionUrl: res.data.pcCnExtensionGoodsUrl,
            };
            this.promotionList.push(promotionList);
            this.showProPromotion = true;
          }
          if (this.$i18n.locale === "en" && res.data.pcEnExtensionImgUrl) {
            let promotionList = {
              extensionGoodsName: res.data.extensionGoodsName,
              promotionImg: res.data.pcEnExtensionImgUrl,
              promotionUrl: res.data.pcEnExtensionGoodsUrl,
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
        window.location.href = link;
      }
    },
    // 关闭产品推广弹窗
    handleClose() {
      this.showProPromotion = false;
    },
  },
};
</script>

<style scoped="scoped" lang='less'>
.promotion-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 10000;

  .el-icon-circle-close {
    position: absolute;
    top: 50%;
    left: 50%;
    font-size: 28px;
    color: #ffffff;
    margin-top: -305px;
    margin-left: 460px;
  }

  .inner-content {
    position: absolute;
    top: 50%;
    left: 50%;
    padding: 15px;
    width: 930px;
    height: 530px;
    background: #ffffff;
    border-radius: 8px;
    transform: translate(-50%, -50%);
    box-sizing: border-box;
    overflow: hidden;

    .content {
      width: 100%;
      height: 100%;
      background: #d8d8d8;
    }

    .img-wrap {
      width: 100%;
      height: 100%;
      position: relative;

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        max-width: 100%;
        max-height: 100%;
        transform: translate(-50%, -50%);
      }
    }

    .up-img {
      position: absolute;
      bottom: 0;
      left: 0;
    }
  }
}
</style>