<!--
 * @Author: yaowei
 * @Date: 2019-05-11 09:39:25
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-29 14:46:41
-->
<template>
  <div class="material-content">
    <h6 class="sub-title">选择材质</h6>
    <ul class="list-wrap">
      <li
        v-for="(item, index) in materialList"
        :key="index"
        :class="{
          active: item.name === materialName,
          hasCard: curMaterialIndex
            ? item.childrenList[curMaterialIndex].cardNum
            : item.childrenList[0].cardNum,
          default: (!isDetail && !item.validFlag) || item.underStockFlag,
        }"
        @click="handleChangeMaterial(item)"
      >
        <span v-if="(isDetail || (!isDetail && item.validFlag))">{{ item.name }}<i class="tips">{{ item.underStockFlag ? "（缺货）" : "" }}</i></span>
        <span v-if="!item.validFlag && item.validType === 1">{{ item.name }}<i class="tips">（型号不适用）</i></span>
        <span v-if="!item.validFlag && item.validType === 2">{{ item.name }}<i class="tips">（图片不适用）</i></span>

        <!-- 气泡 -->
        <!-- 兑换卡 -->
        <Bubble
          class="bubble-wrap"
          :text="item.name === scanMaterial ? '当前扫码材质' : ''"
          v-show="Number(platform) === 28 && item.name === materialName && item.name === scanMaterial"
        ></Bubble>

        <Bubble
          class="bubble-wrap"
          :text="
            (curMaterialIndex
              ? item.childrenList[curMaterialIndex].cardNum
              : item.childrenList[0].cardNum) && item.name !== scanMaterial
              ? '你有' +
                (curMaterialIndex
                  ? item.childrenList[curMaterialIndex].cardNum
                  : item.childrenList[0].cardNum) +
                '张兑换卡可用'
              : ''
          "
          v-show="
            Number(platform) === 28 && 
            item.name !== scanMaterial &&
            item.name === materialName &&
            (curMaterialIndex
              ? item.childrenList[curMaterialIndex].cardNum
              : item.childrenList[0].cardNum)
          "
        ></Bubble>
        <!-- 兑换卡 -->
      </li>
    </ul>
    <h6 class="sub-title">材质颜色</h6>
    <ul class="list-wrap">
      <li
        v-for="(item, index) in colorList"
        :key="index"
        :class="{
          active: item.materialId === Number(materialId),
          default: !item.validFlag || item.underStockFlag,
        }"
        @click="handleChangeColor(item)"
      >
        <span v-if="item.validFlag">{{ item.name }}<i class="tips">{{ item.underStockFlag ? "（缺货）" : "" }}</i></span>
        <span v-if="!item.validFlag && item.validType === 1">{{ item.name }}<i class="tips">（型号不适用）</i></span>
        <span v-if="!item.validFlag && item.validType === 2">{{ item.name }}<i class="tips">（图片不适用）</i></span>
      </li>
    </ul>
  </div>
</template>

<script>
// 组件
import Bubble from "components/bubble/bubble";

export default {
  name: "Material",
  props: {
    scanMaterial: {
      type: String,
      default: "",
    },
    materialName: {
      type: String,
      default: "",
    },
    materialId: {
      type: String,
      default: "",
    },
    materialList: {
      type: Array,
      default: [],
    },
    colorList: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      curMaterialIndex: 0,
      platform: 0, // 平台
    };
  },
  mounted() {
    this.platform = localStorage.getItem("platform") || 28;
  },
  methods: {
    // 选择材质
    handleChangeMaterial(item) {
      let materialName = item.name;
      if (this.isDetail || (!this.isDetail && item.validFlag)) {
        // 适用
        this.$emit("changeMaterial", materialName);
      } else {
        // 不适用
        if (item.validType === 1) {
          // 型号不适用
          this.$dialog
            .confirm({
              title: "温馨提示",
              message:
                "该材质不适用于当前选择机型，点击确定后请重新选择机型",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              showConfirmButton: true,
              cancelButtonText: "取消",
              showCancelButton: true,
              confirmButtonColor: "#333333",
              cancelButtonColor: "#999999",
            })
            .then(() => {
              this.$emit("changeMaterial", materialName, "modelType");
            })
            .catch((error) => {
              console.log(error);
            });
        } else if (item.validType === 2) {
          // 图片不适用
          this.$dialog
            .confirm({
              title: "温馨提示",
              message:
                "该材质不适用于当前选择图片，点击确定后请重新选择图片",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              showConfirmButton: true,
              cancelButtonText: "取消",
              showCancelButton: true,
              confirmButtonColor: "#333333",
              cancelButtonColor: "#999999",
            })
            .then(() => {
              this.$emit("changeMaterial", materialName, "pictureType");
            })
            .catch((error) => {
              console.log(error);
            });
        }
      }
    },
    // 材质颜色
    handleChangeColor(item, index) {
      if (this.isDetail || (!this.isDetail && item.validFlag)) {
        // 适用
        this.$emit("changeColor", item, index);
      } else {
        // 不适用
        if (item.validType === 1) {
          this.$dialog
            .confirm({
              title: "温馨提示",
              message:
                "该材质不适用于当前选择机型，点击确定后请重新选择机型",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              showConfirmButton: true,
              cancelButtonText: "取消",
              showCancelButton: true,
              confirmButtonColor: "#333333",
              cancelButtonColor: "#999999",
            })
            .then(() => {
              this.$emit("changeColor", item, index, "modelType");
            })
            .catch((error) => {
              console.log(error);
            });
        } else if (item.validType === 2) {
          // 图片不适用
          this.$dialog
            .confirm({
              title: "温馨提示",
              message:
                "该材质不适用于当前选择图片，点击确定后请重新选择图片",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              showConfirmButton: true,
              cancelButtonText: "取消",
              showCancelButton: true,
              confirmButtonColor: "#333333",
              cancelButtonColor: "#999999",
            })
            .then(() => {
              this.$emit("changeColor", item, index, "pictureType");
            })
            .catch((error) => {
              console.log(error);
            });
        }
      }
    },
  },
  watch: {
    curMaterialIndex() {
      let val = this.$parent.curMaterialIndex;
      return val;
    },
    materialId(val) {
      return val;
    },
    materialName(val) {
      return val;
    },
    materialList(arr) {
      return arr;
    },
    colorList(arr) {
      return arr;
    },
  },
  components: {
    Bubble,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.sub-title {
  font-size: $font-base;
  color: $color-font-base;
}

.list-wrap {
  display: flex;
  flex-wrap: wrap;
  padding: 0 $spacing-sm;

  & + .sub-title {
    margin-top: 43px;
  }

  li {
    position: relative;
    width: 92px;
    margin-top: $spacing-base;
    margin-right: $spacing-base;
    lineHeight(32px);
    font-size: $font-sm;
    align(center);
    border: 1px solid $color-btn-border;
    border-radius: $radius-xs;

    .bubble-wrap {
      position: absolute;
      bottom: -35px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 10;
    }

    &.active {
      border: 1px solid $color-main;
      background-color: $color-main;
    }

    &.hasCard {
      border: 1px solid $color-main;
    }

    &.default {
      line-height: 1.5;
      color: $color-font-grey;
      background-color: $color-bg;
      border: 1px solid $color-bg;
    }

    .tips {
      position: relative;
      top: -5px;
      display: block;
      font-size: 12px;
      font-style: normal;
      transform: scale(0.8);
    }
  }
}
</style>