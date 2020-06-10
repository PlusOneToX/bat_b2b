<template>
  <div class="index">
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
      <div class="btn btn-submit" @click="next">提交</div>
    </div>
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/ecmascript-6">
import Loading from "components/loading/loading";

import api from "api/allApi.js";

import wx from "weixin-js-sdk";

export default {
  name: "preview",
  data() {
    return {
      title: "预览",
      picType: "",
      isMake: "",
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
      skuId: "",
      skuName: "",
      isLoading: false,
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
    };
  },
  beforeCreate() {
    this.isLoading = true;
    this.message = "载入中";
  },
  created() {
    this.picType = this.$route.query.picType; // 图片类型1-普通图片 2-IP图
    this.isMake = this.$route.query.isMake;
    this.imgUrl = this.$route.query.diyPic;
    this.pdfUrl = this.$route.query.diyPdf;
    this.materialId = this.$route.query.materialId;
    this.modelId = this.$route.query.modelId;
    this.brandId = this.$route.query.brandId;
    this.materialsName = this.$route.query.materialsName;
    this.modelName = this.$route.query.modelName;
    this.brandName = this.$route.query.brandName;
    this.pictureId = this.$route.query.pictureId;
    this.picture = this.$route.query.picture;
    this.price = this.$route.query.price;
    this.sku = this.$route.query.sku;
    this.skuName = this.$route.query.skuName;
    this.skuId = this.$route.query.skuId;
    this.manufactor = this.$route.query.manufactor;
    this.isLoading = false;
  },
  activated() {
    this.picType = this.$route.query.picType; // 图片类型1-普通图片 2-IP图
    this.isMake = this.$route.query.isMake;
    this.imgUrl = this.$route.query.diyPic;
    this.pdfUrl = this.$route.query.diyPdf;
    this.materialId = this.$route.query.materialId;
    this.modelId = this.$route.query.modelId;
    this.brandId = this.$route.query.brandId;
    this.materialsName = this.$route.query.materialsName;
    this.modelName = this.$route.query.modelName;
    this.brandName = this.$route.query.brandName;
    this.pictureId = this.$route.query.pictureId;
    this.picture = this.$route.query.picture;
    this.price = this.$route.query.price;
    this.sku = this.$route.query.sku;
    this.skuName = this.$route.query.skuName;
    this.skuId = this.$route.query.skuId;
    this.manufactor = this.$route.query.manufactor;
    this.isLoading = false;
  },
  methods: {
    next() {
      this.$confirm(
        "您购买的是专属定制商品，下单后，不支持取消订单，且非质量问题不支持无理由退换，请您确认无误后再提交。是否继续提交结算？",
        "温馨提示",
        {
          customClass: "confirm-v-dialog-upload",
          confirmButtonText: "确认提交",
          confirmButtonColor: "#f21e1c",
          cancelButtonText: "我再想想",
          type: "warning",
          iconClass: "el-warning",
          center: true,
        }
      )
        .then(() => {
          this.message = "作品提交中";
          this.isLoading = true;

          let count = 0;
          this.timer = setInterval(() => {
            if (this.pdfUrl === null) {
              if (count > 60) {
                clearInterval(this.timer);
                this.$message.warning("作品提交失败，请重新定制下单！");
                this.$router.push({ path: "/phone" });
              } else {
                if (Number(sessionStorage.getItem("pdfFlag")) === 1) {
                  this.pdfUrl = sessionStorage.getItem("pdfUrl");
                }
              }
              count++;
            } else {
              clearInterval(this.timer);
              if (Number(this.picType) === 2 && !this.isMake) {
                // 添加IP图定制信息
                this.$api
                  .post(this, api.handleCacheIP, {
                    generateImage: this.pdfUrl,
                    materialId: this.materialId,
                    modelId: this.modelId,
                    pictureId: this.pictureId,
                    previewImage: this.imgUrl,
                  })
                  .then((res) => {
                    this.message = "";
                    this.loading = false;
                    if (res.success) {
                      this.submit();
                    }
                  })
                  .catch((err) => {
                    console.log(err);
                  });
              } else {
                this.message = "";
                this.loading = false;
                this.submit();
              }
            }
          }, 1000);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    submit() {
      this.message = "作品提交中";
      this.isLoading = true;

      let info = {
        image: this.imgUrl,
        generateImage: this.pdfUrl,
        manufactor: this.manufactor,
        materialId: this.materialId,
        material: this.materialsName,
        brandId: this.brandId,
        brandName: this.brandName,
        modelId: this.modelId,
        modelName: this.modelName,
        pictureId: this.pictureId,
        price: this.price,
        skuNo: this.sku,
        skuId: this.skuId,
        count: 1,
        totalPrice: this.price,
      };

      let distributorId = localStorage.getItem("distributorId");
      let extendUserId = sessionStorage.getItem("extendUserId");
      let blOrderType = sessionStorage.getItem("blOrderType");
      let ipUserId = sessionStorage.getItem("ipUserId");
      let ipUserProductId = sessionStorage.getItem("ipUserProductId");
      let ipOrderId = sessionStorage.getItem("ipOrderId");
      let ipBackUrl = sessionStorage.getItem("ipBackUrl");
      let aAttach = sessionStorage.getItem("aAttach");
      let vQuantity = Number(sessionStorage.getItem("vQuantity"));
      let vSbomCode = sessionStorage.getItem("vSbomCode");
      if (distributorId === "1410") {
        let wholeUrl = sessionStorage.getItem("wholeUrl");
        info.extendField = { urlParam: wholeUrl }; //扩展字段
      } else if (extendUserId) {
        // 是否有传 userId
        info.extendField = { userId: extendUserId };
      } else if (blOrderType) {
        // 是否有传 orderType
        info.extendField = { orderType: blOrderType };
      } else if (ipUserId || ipBackUrl || ipOrderId || ipUserProductId) {
        // 是否有传 pId、jsonStr
        info.extendField = {
          userId: ipUserId,
          userProductId: ipUserProductId,
          orderId: ipOrderId,
          backUrl: ipBackUrl,
        };
      } else if (aAttach) {
        // 是否有传 attach
        info.extendField = {
          attach: aAttach,
        };
      } else if (vQuantity) {
        // 是否有传 quantity/sbomCode（
        info.extendField = {
          skuCodeAndQtys: vSbomCode + ":" + vQuantity,
        };
      }

      this.$api
        .post(this, api.handleOrder, {
          orderDetail: info,
        })
        .then((res) => {
          if (res.success) {
            this.message = "提交成功";
            this.isLoading = true;
            let url = res.data.url;

            // 判断当前环境
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
              // 微信浏览器
              wx.miniProgram.getEnv((res) => {
                if (res.miniprogram) {
                  // 小程序
                  if (distributorId === "3696") {
                    // IPTV
                    wx.miniProgram.navigateTo({
                      url: url,
                    });
                  } else {
                    window.location.href = url;
                  }
                } else {
                  window.location.href = url;
                }
              });
            } else {
              // 其他环境
              window.location.href = url;
            }
          } else {
            this.isLoading = false;
            this.$message.warning(res.errMessage);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  components: {
    Loading,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
.index {
  position: fixed;
  width: 100%;
  top: 0;
  bottom: 0;
  font-size: 12px;
  background-color: #F6F6F6;

  .container {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .pic-list {
      font-size: 0;

      .pic-item {
        position: relative;
        width: 100%;

        img {
          display: inline-block;
          width: 100%;
          height: 100%;
        }

        .customize {
          position: absolute;
          display: inline-block;
          max-width: 46%;
          height: auto;

          &.img1 {
            top: 8%;
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
        width: 242px;
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        border-radius: 70px;
        background: linear-gradient(90deg, #ff6200 0%, #f21e1c 100%);
      }
    }
  }
}
</style>
<style>
.confirm-v-dialog-upload {
  width: 90%;
  border-radius: 8px;
  padding: 0;
}
.confirm-v-dialog-upload .el-message-box__header {
  padding-top: 18px;
  font-size: 18px;
  color: #4a4a4a;
}
.confirm-v-dialog-upload .el-message-box__header .el-warning {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 2px;
  background: #ffffff url("../../common/images/remind@2x.png") center no-repeat;
  background-size: 20px 20px;
}
.confirm-v-dialog-upload .el-message-box__content {
  font-size: 14px;
  color: #666666;
  text-align: left;
  line-height: 24px;
}

.confirm-v-dialog-upload .el-message-box__btns {
  display: flex;
  padding: 0;
}
.confirm-v-dialog-upload .el-message-box__btns .el-button {
  flex: 1;
  margin: 0;
  padding: 0;
  height: 50px;
  line-height: 50px;
  font-size: 18px;
  color: #4a4a4a;
  text-align: center;
  background-color: #fff;
  border-color: transparent;
  border-radius: 0;
  border-top: 1px solid #ebedf0;
}

.confirm-v-dialog-upload .el-message-box__btns .el-button--primary {
  color: #f21e1c;
  border-left: 1px solid #ebedf0;
}

.el-message {
  min-width: 320px;
  padding: 15px 8px;
}
.el-message__icon {
  margin-right: 5px;
}
.el-message--warning {
  color: #ffffff;
  border: none;
  background-color: rgba(0, 0, 0, 0.9);
}
</style>
