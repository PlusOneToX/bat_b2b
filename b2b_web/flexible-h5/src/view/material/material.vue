<!--
 * @Author: yaowei
 * @Date: 2019-11-23 10:24:26
 * @LastEditors: yaowei
 * @LastEditTime: 2019-06-25 16:17:11
-->
<template>
  <div
    class="material-wrap"
    v-swipeleft="{ fn: swipeleft }"
    v-swiperight="{ fn: swiperight }"
  >
    <c-header :title="title"></c-header>

    <div class="material-info">
      <!-- 飞出元素 -->
      <template v-if="materialList.length > 1">
        <div
          class="material-detail fly-item"
          :class="{ 'fly-left': isFlyLeft, 'fly-right': isFlyRight }"
        >
          <div class="content">
            <h6 class="title">{{ materialList[curItem].name }}</h6>
            <p class="intro">{{ materialList[curItem].subtitle }}</p>
            <p class="detail">{{ materialList[curItem].description }}</p>
            <div class="img-wrap">
              <img :src="materialList[curItem].image" alt="" />
              <div
                class="img-r"
                :class="{ 'has-stock': materialList[curItem].underStockFlag }"
              >
                <template v-if="materialList[curItem].childrenList">
                  <p class="price">
                    价格：<span class="blue"
                      ><i>¥</i
                      >{{ materialList[curItem].childrenList[0].price }}</span
                    >
                  </p>
                  <p
                    class="btn"
                    v-for="(subItem, index) in materialList[curItem]
                      .childrenList"
                    :key="index"
                  >
                    {{
                      materialList[curItem].underStockFlag
                        ? subItem.name + "(缺货)"
                        : subItem.name
                    }}
                  </p>
                </template>
                <template v-else>
                  <p class="btn">选择</p>
                </template>
              </div>
            </div>
          </div>
        </div>
      </template>
      <!-- 材质信息 -->
      <div
        class="material-detail"
        v-for="(material, index) in materialList"
        :key="index"
        :class="{ active: curIndex === index }"
      >
        <div
          :class="[
            'bg' + index,
            { active: index + 1 === materialList.length && !hasComing },
          ]"
          v-for="(item, index) in materialList"
          :key="index"
        ></div>
        <div class="content">
          <h6 class="title">{{ material.name }}</h6>
          <p class="intro">{{ material.subtitle }}</p>
          <p class="detail">{{ material.description }}</p>
          <div class="img-wrap">
            <img :src="material.image" alt="" />
            <div
              class="img-r"
              :class="{ 'has-stock': material.underStockFlag }"
            >
              <template v-if="material.childrenList">
                <p class="price">
                  价格：<span class="blue"
                    ><i>¥</i>{{ material.childrenList[curChoose].price }}</span
                  >
                </p>
                <p
                  v-for="(subItem, subIndex) in material.childrenList"
                  :key="subIndex"
                  @click="
                    goPath(
                      material.name,
                      subItem,
                      subIndex,
                      material.underStockFlag
                    )
                  "
                  class="btn"
                >
                  {{
                    material.underStockFlag
                      ? subItem.name + "(缺货)"
                      : subItem.name
                  }}
                </p>
              </template>
              <template v-else>
                <p class="btn" @click="goPath">选择</p>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 转盘 -->
    <div class="bottom-wrap" :class="{ 'only-one': materialList.length === 1 }">
      <span class="hight-light"></span>
      <ul
        class="material-list"
        :class="[
          { active: hasComing },
          materialList.length ? 'has_' + materialList.length : '',
        ]"
        ref="rotateWrap"
      >
        <li
          v-for="(material, index) in materialList"
          :key="index"
          :style="
            'transform: rotate(' +
            (360 / materialList.length) * index +
            'deg) skewY(' +
            (materialList.length === 3 ? 30 : 0) +
            'deg)'
          "
        >
          <span class="name">{{ material.name }}</span>
        </li>
      </ul>
      <div class="choose-picker"></div>
    </div>
  </div>
</template>

<script>
import cHeader from "../components/cHeader.vue";

import "../../common/js/vue-touch.js";
import api from "common/js/allApi.js";
export default {
  components: {
    cHeader,
  },
  name: "material",
  data() {
    return {
      title: "选择材质",
      materialList: [], // 材质列表
      hasComing: false,
      curIndex: 0, // 当前材质
      curentDeg: 0, // 当前旋转次数
      curItem: 0, // 当前飞出材质
      isFlyLeft: false, // 左滑
      isFlyRight: false, // 右滑
      leftFlag: true, // 左滑防重标记
      rightFlag: true, // 右滑防重标记
      curChoose: 0, // 当前选择材质
      pictureId: "", // 图片ID（推荐页/分类页 去定制所选图片）
      modelId: "", // 型号ID（选择型号）
    };
  },
  mounted() {
    this.hasComing = true;

    // 获取distributorId/platform
    this.distributorId = localStorage.getItem("distributorId");
    this.platform = localStorage.getItem("platform");

    // 获取图片id
    if (sessionStorage.getItem("imageInfo")) {
      let imageInfo = JSON.parse(sessionStorage.getItem("imageInfo"));
      this.pictureId = imageInfo.pictureId;
    }

    // 获取型号id
    if (sessionStorage.getItem("modelInfo")) {
      let modelInfo = JSON.parse(sessionStorage.getItem("modelInfo"));
      this.modelId = modelInfo.modelId;
    }

    this.getMaterialList();
  },
  methods: {
    // 获取材质列表
    getMaterialList() {
      this.$api
        .get(this, api.getMaterialList, {
          pictureId: this.pictureId,
          categoryId: 1, // 产品类型id：1 手机壳
          distributorId: this.distributorId,
          modelId: this.modelId,
          platform: this.platform, // 平台
        })
        .then((res) => {
          if (res.success) {
            if (res.data && res.data.length > 0) {
              this.materialList = res.data;

              // 获取价格
              this.materialList.forEach((item) => {
                if (item.childrenList && item.childrenList.length > 0) {
                  item.childrenList.forEach((material) => {
                    this.getPrice(material, material.materialId);
                  });
                }
              });
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          this.$toast.fail(error);
        });
    },
    // 获取价格
    getPrice(item, materialId) {
      this.$api
        .get(this, api.getPrice, {
          distributorId: this.distributorId,
          orderSource: this.platform,
          materialId: materialId,
        })
        .then((res) => {
          if (res.success) {
            this.$set(item, "price", res.data || 0);
          } else {
            this.$toast.fail(error);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 左滑
    swipeleft(e) {
      this.hasComing = false;
      if (this.leftFlag) {
        this.leftFlag = false;
        this.rightFlag = false;

        this.curItem = this.curIndex;
        let len = this.materialList.length;
        if (len > 1) {
          this.isFlyLeft = true;
          this.isFlyRight = false;
        }
        this.curIndex++;
        if (this.curIndex >= len) {
          // 最后一个时，设为第一个
          this.curIndex = 0;
        }

        this.curentDeg++;
        this.rotateDeg(len, this.curentDeg);

        // 动画完成才能再次左滑
        setTimeout(() => {
          this.leftFlag = true;
          this.rightFlag = true;
        }, 1000);
      }
    },
    // 右滑
    swiperight(e) {
      this.hasComing = false;
      if (this.rightFlag) {
        this.rightFlag = false;
        this.leftFlag = false;

        this.curItem = this.curIndex;
        let len = this.materialList.length;
        if (len > 1) {
          this.isFlyLeft = false;
          this.isFlyRight = true;
        }
        if (this.curIndex <= 0) {
          // 第一个时，设为最后一个
          this.curIndex = len;
        }
        this.curIndex--;

        this.curentDeg--;
        this.rotateDeg(len, this.curentDeg);

        // 动画完成才能再次右滑
        setTimeout(() => {
          this.rightFlag = true;
          this.leftFlag = true;
        }, 1000);
      }
    },
    // 转盘旋转角度
    rotateDeg(len, curentDeg) {
      setTimeout(() => {
        this.isFlyLeft = false;
        this.isFlyRight = false;
      }, 800);

      let deg = 0;
      switch (len) {
        // len 材质种类
        case 1:
          break;
        case 2:
          deg = -(360 / len) * curentDeg - 90;
          break;
        case 3:
          deg = -(360 / len) * curentDeg - 60;
          break;
        case 4:
          deg = -(360 / len) * curentDeg - 45;
          break;
        default:
          break;
      }
      this.$refs.rotateWrap.style.transform = "rotate(" + deg + "deg)";
    },
    // 页面跳转
    goPath(material, item, index, underStockFlag) {
      // 判断是否缺货
      if (!underStockFlag) {
        // 否
        this.curChoose = index;
        let materialInfo = {
          parentName: material,
          item: item,
        };
        sessionStorage.setItem("materialInfo", JSON.stringify(materialInfo));
        this.$router.push({
          path: "/custom",
          query: {
            comingFlag: "mmPage",
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
$yellow = #FBA322;
$light-yellow = #fdc079;
$lighter-yellow = #ffcf71;
$dark-yellow = #ffc258;
$orange = #ff7613;
$dark = #000;
$gray = #5A5A5A;
$light-gray = #959595;
$blue = #0076A5;
$btn-bg = #81DBE9;
$btn-color = #F9FDFE;
$disable-color = #333;
$disable-bg = #f4f4f4;

.material-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(0deg, $lightBlue, $darkBlue);

  .material-info {
    position: fixed;
    top: 50%;
    left: 0;
    width: 100%;
    margin-top: -270px;

    .material-detail {
      position: absolute;
      left: 13%;
      width: 74%;
      z-index: 100;
      display: none;

      &.fly-item {
        z-index: 101;

        &.fly-left {
          display: block;
          transform: translateX(0) scale(1);
          animation: flyLeft 0.8s;
        }

        @keyframes flyLeft {
          0% {
            transform: translateX(0) scale(1);
            opacity: 1;
          }

          30% {
            transform: translateX(0) scale(1) rotate(-10deg);
            transform-origin: 0 100%;
            opacity: 1;
          }

          100% {
            display: none;
            transform: translate(-100%, -50%) scale(0.5) rotate(-120deg);
            opacity: 0;
          }
        }

        &.fly-right {
          display: block;
          transform: translateX(0) scale(1);
          animation: flyRight 0.8s;
        }

        @keyframes flyRight {
          0% {
            transform: translateX(0) scale(1);
            opacity: 1;
          }

          30% {
            transform: translateX(0) scale(1) rotate(10deg);
            transform-origin: 100% 100%;
            opacity: 1;
          }

          100% {
            display: none;
            transform: translate(100%, -50%) scale(0.5) rotate(120deg);
            opacity: 0;
          }
        }
      }

      &.active {
        display: block;
        z-index: 5;
        animation: showItem 1s;
      }

      @keyframes showItem {
        0% {
          transform: scale(0.9);
        }

        30% {
          transform: scale(0.9);
        }

        100% {
          transform: scale(1);
        }
      }

      .bg1 {
        position: absolute;
        bottom: -20px;
        left: 4%;
        width: 92%;
        height: 120px;
        background-color: #A3DFF1;
        border-radius: 30px;
        z-index: 3;

        &.active {
          animation: showBg 1.5s;
        }
      }

      .bg2 {
        position: absolute;
        bottom: -40px;
        left: 10%;
        width: 80%;
        height: 240px;
        background-color: #6BCBE7;
        border-radius: 30px;
        z-index: 2;

        &.active {
          animation: showBg 1.5s;
        }
      }

      .bg3 {
        position: absolute;
        bottom: -60px;
        left: 15%;
        width: 70%;
        height: 240px;
        background-color: #5ac7e6;
        border-radius: 30px;
        z-index: 1;

        &.active {
          animation: showBg 1.5s;
        }
      }

      @keyframes showBg {
        0% {
          opacity: 0;
        }

        30% {
          opacity: 0;
        }

        100% {
          opacity: 1;
        }
      }

      .content {
        position: relative;
        padding: 20px;
        background-color: $white;
        box-shadow: 0px 1px 44px 0px rgba(0, 138, 194, 0.54);
        border-radius: 35px;
        z-index: 3;
      }

      .title {
        position: relative;
        padding: 15px 10px;
        font-size: 16px;
        color: $dark;

        &:before {
          display: block;
          content: '';
          position: absolute;
          top: 17px;
          left: 0;
          width: 4px;
          height: 12px;
          background-color: $blue;
          border-radius: 2px;
        }
      }

      .intro {
        font-size: 15px;
        color: $gray;
      }

      .detail {
        margin-top: 10px;
        font-size: 13px;
        color: $light-gray;
        line-height: 18px;
      }

      .img-wrap {
        position: relative;
        margin-top: 30px;
        min-height: 105px;
        overflow: hidden;

        img {
          float: left;
          width: auto;
          height: 100%;
          max-width: 40%;
          max-height: 190px;
        }

        .img-r {
          position: absolute;
          right: 0;
          bottom: 0;
          width: 106px;
          text-align: center;

          &.has-stock {
            .btn {
              color: $disable-color;
              background-color: $disable-bg;
            }
          }
        }

        .model-info {
          display: none;

          &.current-model {
            display: blocl;
          }
        }

        .btn {
          margin-top: 20px;
          width: 100%;
          height: 36px;
          font-size: 13px;
          color: $btn-color;
          line-height: 36px;
          background-color: $btn-bg;
          border-radius: 36px;
        }
      }

      .price {
        font-size: 15px;
        color: $gray;

        .blue {
          color: $blue;

          i {
            font-size: 12px;
            font-style: normal;
          }
        }
      }
    }
  }

  .bottom-wrap {
    position: fixed;
    left: 50%;
    bottom: -152px;
    margin-left: -152px;
    width: 304px;
    height: 304px;
    background: rgba(255, 255, 255, 0.2);
    border: 1px solid rgba(255, 255, 255, 0.52);
    box-shadow: 0px 0px 13px 0px rgba(10, 151, 176, 0.26);
    box-sizing: border-box;
    border-radius: 50%;

    &:before {
      content: '';
      display: block;
      position: fixed;
      left: 50%;
      bottom: -140px;
      margin-left: -140px;
      width: 280px;
      height: 280px;
      background: $yellow;
      border-radius: 50%;
    }

    &:after {
      content: '';
      display: block;
      position: fixed;
      bottom: 0;
      left: 50%;
      margin-left: -160px;
      width: 320px;
      height: 160px;
      background: url('../../assets/images/picker-arrow.png') no-repeat;
      background-size: 100%;
    }

    &.only-one {
      &:after {
        background: none;
      }
    }

    .hight-light {
      display: block;
      position: fixed;
      bottom: 0;
      left: 50%;
      margin-left: -120px;
      width: 240px;
      height: 120px;
      background: url('../../assets/images/picker-highlight.png') no-repeat;
      background-size: 100% 100%;
      opacity: 0.65;
      z-index: 1;
    }

    .choose-picker {
      position: fixed;
      bottom: 0;
      left: 50%;
      margin-left: -120px;
      width: 240px;
      height: 120px;
      background: url('../../assets/images/choose-picker.png') no-repeat;
      background-size: 100% 100%;
      z-index: 3;
    }

    .material-list {
      position: fixed;
      bottom: -128px;
      left: 50%;
      margin-left: -128px;
      width: 256px;
      height: 256px;
      background-color: $light-yellow;
      border-radius: 50%;
      transition: all 1s;
      overflow: hidden;

      li {
        position: absolute;
        top: 0;
        right: 0;
        width: 50%;
        height: 50%;
        font-size: 17px;
        color: $orange;
        text-align: center;
        transform-origin: 0 100%;

        .name {
          display: inline-block;
          position: relative;
          top: 50%;
          transform: translateY(-50%);
          z-index: 2;
        }
      }

      li:nth-child(2n) {
        background-color: $lighter-yellow;
      }

      li:nth-child(3n) {
        background-color: $dark-yellow;
      }

      &.has_1 {
        li {
          width: 100%;
          height: 100%;

          .name {
            top: 25%;
          }
        }

        &.active {
          animation: rotate1 0.8s;
        }

        @keyframes rotate1 {
          0% {
            transform: rotate(0deg);
          }

          100% {
            transform: rotate(360deg);
          }
        }
      }

      &.has_2 {
        transform: rotate(-90deg);

        li {
          top: -50%;
          height: 100%;
          transform-origin: 0 75%;

          &:first-child {
            top: 0;
            transform-origin: 0 0;
          }

          span {
            top: 50%;
            transform: translateY(-50%) rotate(90deg);
          }
        }

        &.active {
          animation: rotate2 0.8s;
        }

        @keyframes rotate2 {
          0% {
            transform: rotate(-90deg);
          }

          100% {
            transform: rotate(270deg);
          }
        }
      }

      &.has_3 {
        transform: rotate(-60deg);

        li {
          top: -25%;
          height: 75%;

          .name {
            top: 60%;
            transform: skewY(-30deg) rotate(60deg);
          }
        }

        &.active {
          animation: rotate3 0.8s;
        }

        @keyframes rotate3 {
          0% {
            transform: rotate(-60deg);
          }

          100% {
            transform: rotate(300deg);
          }
        }
      }

      &.has_4 {
        transform: rotate(-45deg);

        .name {
          transform: rotate(45deg);
        }

        &.active {
          animation: rotate4 0.8s;
        }

        @keyframes rotate4 {
          0% {
            transform: rotate(-45deg);
          }

          100% {
            transform: rotate(315deg);
          }
        }
      }
    }
  }
}
</style>
