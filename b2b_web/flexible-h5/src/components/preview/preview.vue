<template>
  <div class="preview" :class="{ 'ry-style': Number(distributorId) === 4378 }">
    <v-header :back="true" :title="title" @back="toback"></v-header>
    <div class="container">
      <div class="top-text">预览看看效果吧</div>
      <div class="pic-list">
        <div class="pic-item" v-for="item in imgs" :key="item.id">
          <img v-lazy="item.img" alt="" />
          <img
            class="customize"
            :class="'img' + item.id"
            :src="imgUrl"
            alt=""
          />
        </div>
      </div>
      <div
        class="btn btn-submit"
        @click="submit"
        v-if="Number(distributorId) === 4378"
      >
        提交
      </div>
      <div class="btn btn-submit" @click="addToCart" v-else>加入购物车</div>
    </div>
    <Loading v-show="loading" :message="message"></Loading>
  </div>
</template>
<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import Loading from "base/loading/loading";
import api from "common/js/allApi.js";
import { getLocalStorageItem } from "common/js/common";
let clientApi = "";
export default {
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
      message: "",
      imgs: [
        {
          id: 1,
          img: require("../../common/images/1.png"),
        },
        {
          id: 2,
          img: require("../../common/images/2.png"),
        },
        {
          id: 3,
          img: require("../../common/images/3.png"),
        },
        {
          id: 4,
          img: require("../../common/images/4.png"),
        },
      ],
      timer: null,
      hostname: "",
      comingFlag: true,
      pdfFile: "", // pdf 文件
      isBackClick: false, // 返回按钮点击状态
      picType: "",
      isMake: "",
      distributorId: "", // 分销商ID
    };
  },
  mounted() {
    this.distributorId = localStorage.getItem("distributorId");

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
    // 提交
    submit() {
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

          let categoryId = 1;
          let categoryName = "手机壳";

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
              } else {
                this.$toast(res.errMessage);
              }
            });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 加入购物车
    addToCart() {
      let userId = parseInt(getLocalStorageItem("userId"));

      let categoryId = 1;
      let categoryName = "手机壳";

      if (userId) {
        // 已登录
        this.message = "正在加入购物车~";
        this.loading = true;
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
              this.$router.replace({ path: "/shopcart", query: { pid: pid } });
            } else {
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
        this.$router.replace({
          path: "/login",
          query: {
            comingFlag: "preview",
            enterParams: enterParams,
          },
        });
      }
    },
    toback() {
      this.isBackClick = true; // 设置返回按钮点击状态
      this.$router.push({ path: "/phone" });
    },
  },
  beforeRouteLeave(to, from, next) {
    next();
  },
  components: {
    VHeader,
    Loading,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
.preview {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 0;
  font-size: 12px;
  background-color: #F6F6F6;

  .container {
    position: relative;
    height: 100%;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .pic-list {
      font-size: 0;
      overflow-y: scroll;

      .pic-item {
        position: relative;
        width: 100%;

        img {
          display: inline-block;
          width: 100%;
          height: 480px;
        }

        .customize {
          position: absolute;
          display: inline-block;
          width: 46%;
          height: auto;

          &.img1 {
            top: 5%;
            left: 30%;
            transform: rotate(-30deg);
            -ms-transform: rotate(-30deg);
            -moz-transform: rotate(-30deg);
            -webkit-transform: rotate(-30deg);
            -o-transform: rotate(-30deg);
          }

          &.img2 {
            width: 40%;
            top: 20%;
            left: 40%;
          }

          &.img3 {
            top: 20%;
            left: 20%;
            transform: rotate(-30deg);
            -ms-transform: rotate(-30deg);
            -moz-transform: rotate(-30deg);
            -webkit-transform: rotate(-30deg);
            -o-transform: rotate(-30deg);
          }

          &.img4 {
            width: 40%;
            top: 8%;
            left: 10%;
          }
        }
      }
    }

    .top-text {
      margin: 20px auto;
      font-size: 14px;
      text-align: center;
      color: #333333;
    }

    .btn {
      position: fixed;
      left: 50%;
      display: inline-block;
      font-size: 18px;
      color: #ffffff;
      text-align: center;
      transform: translateX(-50%);

      &.title {
        top: 80px;
        width: 140px;
        height: 32px;
        line-height: 32px;
        border-radius: 8px;
        background: linear-gradient(270deg, rgba(163, 46, 63, 1) 0%, rgba(255, 133, 0, 1) 100%);
      }

      &.btn-submit {
        bottom: 30px;
        width: 250px;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        border-radius: 70px;
        background: #0076A5;
      }
    }
  }
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;

// 荣耀
.ry-style {
  background-color: $color-bg;

  .container {
    .btn {
      &.btn-submit {
        background: $color-main;
      }
    }
  }
}
</style>