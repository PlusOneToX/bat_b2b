<!--
 * @Author: yaowei
 * @Date: 2019-11-20 20:14:12
 * @LastEditors: yaowei
 * @LastEditTime: 2019-07-20 10:53:47
-->
<template>
  <div class="previewer">
    <div class="content">
      <c-header :title="title"></c-header>
      <div class="preview-wrap">
        <div class="preview-content">
          <img class="preview-img" :src="imgUrl" alt="" />
        </div>
      </div>

      <div class="preview-info">
        <div class="info-content">
          <div class="btn-wrap">
            <span @click="addCart('shopcart')">加入购物车</span>
            <span @click="buyNow('buyNow')">立即购买</span>
          </div>

          <h6 class="title">
            <span class="line line-l"></span>{{ tName
            }}<span class="line line-r"></span>
          </h6>
          <ul class="phone-info">
            <li>{{ modelName }}</li>
            <li>{{ pictureName }}</li>
            <li>{{ materialsName }}</li>
          </ul>
        </div>
      </div>
    </div>
    <Loading v-show="loading" :message="message"></Loading>
  </div>
</template>

<script>
import cHeader from "../components/cHeader.vue";
import Loading from "base/loading/loading";
import { getLocalStorageItem } from "common/js/common";
import api from "common/js/allApi.js";
var clientApi = "";
export default {
  name: "previewer",
  components: {
    cHeader,
    Loading,
  },
  data() {
    return {
      title: "预览",
      imgUrl: "",
      pdfUrl: "",
      materialId: 0,
      modelId: 0,
      brandId: 0,
      modelName: "",
      materialsName: "",
      manufactor: "",
      brandName: "",
      pictureId: 0,
      picture: "",
      price: 0,
      sku: "",
      skuName: "",
      loading: false,
      message: "",
      hostname: "",
      comingFlag: true,
      pdfFile: "", // pdf 文件
      isBackClick: false, // 返回按钮点击状态
      pictureName: "", // 图片名称
      tName: "",
      picType: "",
      isMake: "",
    };
  },
  mounted() {
    this.getSam();
    this.loading = false;
    var enterParams = this.$route.query.enterParams;
    let params = JSON.parse(enterParams);
    this.imgUrl = params.diyPic;
    this.pdfUrl = params.diyPdf;
    this.materialId = params.materialId;
    this.modelId = params.modelId;
    this.brandId = params.brandId;
    this.materialsName = decodeURI(params.materialsName);
    this.modelName = decodeURI(params.modelName);
    this.brandName = decodeURI(params.brandName);
    this.pictureId = params.pictureId;
    this.picture = decodeURI(params.picture);
    this.price = params.price;
    this.sku = params.sku;
    this.skuName = decodeURI(params.skuName);
    this.manufactor = params.manufactor;
    this.materialsType = params.materialsType;

    this.picType = this.$route.query.picType; // 图片类型1-普通图片 2-IP图
    this.isMake = this.$route.query.isMake;
    if (Number(this.picType === 2) && !this.isMake) {
      // 缓存IP图
      this.$api
        .post(this, api.handleCacheIP, {
          generateImage: generateImage,
          materialId: this.materialId,
          modelId: this.modelId,
          pictureId: this.picId,
          previewImage: image,
        })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
  methods: {
    getSam() {
      let platform = parseInt(localStorage.getItem("platform"));
      if (platform === 7) {
        this.tName = "定制信息";
      } else {
        this.tName = "BAT";
      }
    },
    // 加入购物车
    addCart(handleType) {
      this.message = "正在加入购物车";
      this.loading = true;
      this.submit(handleType);
    },
    // 立即购买
    buyNow(handleType) {
      // 结算提示
      this.$dialog
        .confirm({
          title: "温馨提示",
          message:
            "您购买的专属定制的商品，非质量问题不支持无理由退换喔，是否继续提交结算？",
          className: "confirm-v-dialog tl",
          confirmButtonText: "确认提交",
          confirmButtonColor: "#0076A5",
          cancelButtonText: "我再想想",
        })
        .then(() => {
          this.message = "正在提交";
          this.loading = true;
          this.submit(handleType);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    submit(handleType) {
	  console.log("正在提交中......");
      let userId = parseInt(getLocalStorageItem("userId"));

      let categoryId = 1;
      let categoryName = "手机壳";

      if (userId) {
        // 已登录
        let info = {
          categoryId: categoryId,
          categoryName: categoryName,
          brandId: this.brandId,
          brandName: this.brandName,
          generateImage: this.pdfUrl,
          materialId: this.materialId,
          materialName: this.materialsName,
          modelId: this.modelId,
          modelName: this.modelName,
          pictureId: this.pictureId,
          pictureName: this.picture,
          previewImage: this.imgUrl,
          manufactors: this.manufactor,
        };

        this.$api
          .post(this, api.addToShopcart, {
            diy: info,
            itemCode: this.sku,
            itemCount: 1,
            itemType: 1, // 是否赠品：1 非赠品，2 赠品
            salePrice: this.price, // 价格
          })
          .then((res) => {
            if (res.success) {
              this.message = "";
              this.loading = false;

              let pid = res.data.id;
              if (handleType === "shopcart") {
                // 加入购物车
                this.$router.replace({
                  path: "/shopcart",
                  query: { pid: pid },
                });
              } else {
                // 订单
                let goodsArr = [];
                // 获取购物车列表，拿到匹配数据（下单页需要）
                this.$api.get(this, api.getShopcartList).then((res) => {
                  if (res.success) {
                    if (res.data && res.data.length > 0) {
                      res.data.forEach((item) => {
                        // 未返回价格，默认设置为0
                        if (!item.salePrice) {
                          this.$set(item, "salePrice", 0);
                        }

                        // 获取当前匹配数据
                        if (item.id === pid) {
                          goodsArr.push(item);
                          localStorage.setItem(
                            "goods",
                            JSON.stringify(goodsArr)
                          );

                          this.$router.replace("/order");
                        }
                      });
                    }
                  }
                });
              }
            } else {
			  console.log("关闭加载中提示......");
			  this.message = "";
			  this.loading = false;
              this.$toast(res.errMessage);
            }
          });
      } else {
        // 未登录
        var enterParams = JSON.stringify({
          categoryId: categoryId,
          categoryName: encodeURI(categoryName),
          brandId: this.brandId,
          brandName: encodeURI(this.brandName),
          generateImage: this.pdfUrl,
          materialId: this.materialId,
          materialName: encodeURI(this.materialsName),
          modelId: this.modelId,
          modelName: encodeURI(this.modelName),
          pictureId: this.pictureId,
          pictureName: encodeURI(this.picture),
          previewImage: this.imgUrl,
          manufactor: this.manufactor,
          itemCode: this.sku,
          itemCount: 1,
          itemType: 1, // 是否赠品：1 非赠品，2 赠品
          salePrice: this.price, // 价格
        });

        // 跳转登录 传参
        this.$router.push({
          path: "/login",
          query: {
            comingFlag: "preview",
            comingType: handleType === "shopcart" ? "addShopCart" : "buyNow",
            enterParams: enterParams,
          },
        });
      }
    },
  },
};
</script>

<style lang="stylus" scoped>
$white = #fff;
$darkBlue = #009ddb;
$lightBlue = #1275d1;
$blue = #0076A5;
$dark = #000000;
$gray = #5A5A5A;

.previewer {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;

  .content {
    position: absolute;
    width: 100%;
    height: 100%;
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }
  }
}

.preview-wrap {
  position: absolute;
  top: 0;
  left: -50%;
  width: 200%;
  height: 70%;
  background: linear-gradient(150deg, $lightBlue, $darkBlue);
  border-radius: 0 0 60% 60%;

  .preview-content {
    position: relative;
    width: 50%;
    height: 100%;
    top: 0;
    left: 25%;
    background: url('../../assets/images/background/preview_bg.png') no-repeat;
    background-size: 100% auto;

    &:before {
      content: '';
      position: absolute;
      width: 100%;
      height: 20%;
      left: 0;
      bottom: 5%;
      background: url('../../assets/images/background/preview_pallet.png') no-repeat;
      background-size: auto 100%;
      background-position: center;
    }

    .preview-img {
      position: absolute;
      left: 50%;
      bottom: 18.5%;
      height: 60%;
      transform: translateX(-50%);
    }
  }
}

.preview-info {
  position: absolute;
  width: 100%;
  top: 63%;
  padding: 0 15px;
  box-sizing: border-box;

  .info-content {
    padding: 30px 0;
    background-color: $white;
    border-radius: 10px;
  }

  .btn-wrap {
    display: flex;
    padding: 0 25px;
    justify-content: space-between;

    span {
      display: inline-block;
      width: 45%;
      height: 40px;
      font-size: 14px;
      color: $white;
      text-align: center;
      line-height: 40px;
      background-color: $blue;
      border-radius: 5px;
    }
  }

  .title {
    padding: 35px 0 30px;
    font-size: 16px;
    color: $dark;
    text-align: center;

    .line {
      position: relative;
      top: -6px;
      display: inline-block;
      width: 20%;
      height: 2px;
      border-radius: 1px;

      &.line-l {
        margin-right: 10px;
        background: linear-gradient(-90deg, #87B3FE, rgba(205, 245, 242, 0.22), rgba(204, 243, 244, 0));
      }

      &.line-r {
        margin-left: 10px;
        background: linear-gradient(90deg, #87B3FE, rgba(205, 245, 242, 0.22), rgba(204, 243, 244, 0));
      }
    }
  }

  .phone-info {
    display: flex;
    justify-content: center;
    align-items: top;

    li {
      flex: 1;
      padding: 0 25px;
      font-size: 14px;
      color: $gray;
      text-align: center;
      line-height: 18px;
      opacity: 0.45;

      &+li {
        position: relative;

        &:before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          width: 1px;
          height: 100%;
          background: linear-gradient(0deg, rgba(244, 244, 244, 0), #F4F4F4, rgba(244, 244, 244, 0));
        }
      }
    }
  }
}
</style>
