<!--
 * @Author: yaowei
 * @Date: 2019-05-08 10:21:33
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-07 15:47:27
-->
<template>
  <div class="model-content">
    <template v-if="type !== 'bottom'">
      <h6 class="title">选择手机型号</h6>
      <p class="cur-model" v-if="curMobile">已选型号：{{ curMobile }}</p>
      <p class="cur-model tips" v-else>系统未匹配到您的机型，请手动选择！</p>
    </template>
    <template v-else>
      <h6 class="title">
        <span class="title-l" v-if="!isFirstEnter" @click="handleCancel()"
          >取消</span
        >
        <span class="title-c">选择手机型号</span>
        <span
          v-if="!isFirstEnter"
          class="title-r"
          :class="{ default: !curMobile }"
          @click="handleConfirm()"
          >确定</span
        >
        <span
          v-else
          class="title-r"
          :class="{ default: !hasClicked }"
          @click="handleNext()"
          >下一步</span
        >
      </h6>
    </template>

    <div class="model-list">
      <div class="model-l">
        <div
          v-for="(item, index) in modelList"
          :key="index"
          class="model-item"
          :class="{ active: curIndex === index }"
          @click="changeModel(index)"
        >
          <span class="model-item-name">
            <i>{{ item.name }}</i>
          </span>
        </div>
      </div>
      <div class="model-r">
        <template v-for="(model, pIndex) in modelList">
          <div
            v-show="curIndex === pIndex"
            :key="pIndex"
            class="model-list-wrap"
            :class="{ active: curIndex === pIndex }"
          >
            <div
              v-for="(item, index) in model.childrenList"
              :key="index"
              class="model-item"
              :class="{
                default: item.underStockFlag || (!isDetail && !item.validFlag),
                active:
                  (Number(distributorId) === 6175 && !isDetail && hasClicked && curMobile === item.name) ||
                  (Number(distributorId) !== 6175 && !isDetail && curMobile === item.name) ||
                  (isDetail && curMobile === item.name),
              }"
              @click="chooseModel(item)"
            >
              <span
                class="model-item-name"
                v-if="isDetail || (!isDetail && item.validFlag)"
              >
                <i
                  >{{ item.name }}{{ item.underStockFlag ? "（缺货）" : "" }}</i
                >
              </span>
              <span
                class="model-item-name"
                v-if="!item.validFlag && item.validType === 1"
              >
                <i>{{ item.name }}（材质不适用）</i>
              </span>
              <span
                class="model-item-name"
                v-if="!item.validFlag && item.validType === 2"
              >
                <i>{{ item.name }}（图片不适用）</i>
              </span>

              <!-- 气泡，爱思定制 -->
              <div tabindex="0" :id="mobile === item.name ? 'targetBox' : ''">
                <Bubble
                  class="bubble-wrap"
                  :class="index > 3 ? '' : 'up'"
                  :text="'您的机型可能为'"
                  :isDown="index > 3 ? true : false"
                  v-show="Number(distributorId) === 6175 && !hasClicked && !isDetail && mobile === item.name"
                ></Bubble>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="btn-wrap" v-show="type !== 'bottom'">
      <span class="btn grey-btn" @click="handleCancel()">跳过</span>
      <span class="btn confirm-btn default" v-show="!curMobile">确定</span>
      <span class="btn confirm-btn" v-show="curMobile" @click="handleConfirm()"
        >确定</span
      >
    </div>
  </div>
</template>

<script>
// 组件
import Bubble from "components/bubble/bubble";

export default {
  name: "Model",
  props: {
    type: {
      type: String,
      default: "",
    },
    modelList: {
      type: Array,
      default: [],
    },
    mobile: {
      type: String,
      default: "",
    },
    isFirstEnter: {
      type: Boolean,
      default: false,
    },
    isDetail: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      distributorId: "",
      curMobile: "",
      curIndex: 0, // 默认展示第一项
      curChooseModel: {}, // 当前选中机型
      hasClicked: false, // 是否点击
    };
  },
  mounted() {
    this.distributorId = localStorage.getItem("distributorId");
  },
  watch: {
    modelList(arr) {
      if (arr && arr.length > 0) {
        if (this.mobile) {
          arr.forEach((brand, pIndex) => {
            if (brand.childrenList.length > 0) {
              brand.childrenList.forEach((model) => {
                // 判断已选型号是否匹配
                if (
                  model.name.replace(/\s*/g, "").toLowerCase() ===
                    this.mobile.replace(/\s*/g, "").toLowerCase() &&
                  model.validFlag
                ) {
                  // 获取当前选中大类
                  this.curIndex = pIndex;
                  this.curMobile = model.name;
                }
              });
            }
          });
        }

        this.$nextTick(() => {
          // 延迟滚动到默认识别机型位置
          if (document.getElementById("targetBox")) {
            document.getElementById("targetBox").scrollIntoView({
              behavior: "instant",
              block: "end",
              inline: "nearest",
            });
          }
        });
      }
    },
  },
  methods: {
    // 切换型号
    changeModel(index) {
      this.curIndex = index;
    },
    // 选择机型
    chooseModel(item) {
      let distributorId = localStorage.getItem("distributorId");

      this.hasClicked = true;
      if (this.isDetail || (!this.isDetail && item.validFlag)) {
        // 适用
        if (!item.underStockFlag) {
          this.curMobile = item.name;
        }
      } else {
        // 不适用
        this.curMobile = "";
        if (item.validType === 1) {
          // 材质不适用
          this.$dialog
            .confirm({
              title: "温馨提示",
              message:
                Number(distributorId) !== 6175
                  ? "当前机型不适用于这个材质，点击确定后请重新选择材质"
                  : "当前机型不适用于这个材质，请选择其它机型",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              showConfirmButton: true,
              cancelButtonText: "取消",
              showCancelButton: Number(distributorId) !== 6175 ? true : false,
              confirmButtonColor: "#333333",
              cancelButtonColor: "#999999",
            })
            .then(() => {
              if (Number(distributorId) !== 6175) {
                // 非爱思，弹窗提示
                this.curMobile = item.name;
                this.handleConfirm("materialType");
              }
            })
            .catch((error) => {
              console.log(error);
            });
        } else if (item.validType === 2) {
          // 图片不适用
          this.$dialog
            .confirm({
              title: "温馨提示",
              message: "当前机型不适用于这个图片，点击确定后请重新选择图片",
              className: "confirm-v-dialog tl",
              confirmButtonText: "确定",
              showConfirmButton: true,
              cancelButtonText: "取消",
              showCancelButton: true,
              confirmButtonColor: "#333333",
              cancelButtonColor: "#999999",
            })
            .then(() => {
              this.curMobile = item.name;
              this.handleConfirm("pictureType");
            })
            .catch((error) => {
              console.log(error);
            });
        }
      }
    },
    // 跳过
    handleCancel() {
      this.$emit("cancel");
      this.curMobile = this.mobile;
    },
    // 确定
    handleConfirm(type) {
      this.modelList.forEach((brand, pIndex) => {
        if (brand.childrenList.length > 0) {
          brand.childrenList.forEach((model) => {
            if (
              model.name.replace(/\s*/g, "").toLowerCase() ===
              this.curMobile.replace(/\s*/g, "").toLowerCase()
            ) {
              this.curChooseModel = model;
              this.$set(
                this.curChooseModel,
                "parentId",
                this.modelList[pIndex].modelId
              );
              this.$set(
                this.curChooseModel,
                "parentName",
                this.modelList[pIndex].name
              );

              this.$emit("confirm", this.curChooseModel, type);
            }
          });
        }
      });
    },
    // 下一步
    handleNext() {
      if (this.hasClicked && this.curMobile) {
        this.handleConfirm("next");
      }
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

.model-content {
  padding: 24px 15px 0;
  font-size: $font-base;
  line-height: 20px;

  .title {
    position: relative;
    align(center);

    .title-c {
      font-size: $font-lg;
    }

    .title-l {
      position: absolute;
      left: 0;
      color: $color-font-grey;
    }

    .title-r {
      position: absolute;
      right: 0;
      font-weight: 500;

      &.default {
        color: $color-font-grey;
      }
    }
  }

  .cur-model {
    margin-top: 12px;
    font-size: $font-sm;
    font-weight: 600;
    line-height: 17px;
    align(center);

    &.tips {
      color: $color-font-base;
    }
  }

  .model-list {
    margin-top: 28px;
    height: 284px;
    display: flex;

    .model-item {
      position: relative;
      line-height: 25px;

      & + .model-item {
        margin-top: $spacing-sm;
      }

      &.default {
        color: $color-font-grey;
      }

      .model-item-name {
        padding: 3px;
      }

      &.active {
        .model-item-name {
          position: relative;
          background-image: radial-gradient($color-main 70%, transparent 0), linear-gradient($color-main, $color-main), radial-gradient($color-main 70%, transparent 0);
          background-repeat: no-repeat;
          background-size: 6px 6px, calc(100% - 6px) 6px, 6px 6px;
          background-position: left 12px, 3px 12px, right 12px;
        }
      }

      .bubble-wrap {
        position: absolute;
        top: -30px;
        left: 0;
        z-index: 10;

        &.up {
          top: auto;
          bottom: -30px;
        }
      }

      i {
        position: relative;
        font-style: normal;
        z-index: 2;
      }
    }

    .model-l {
      position: relative;
      overflow-y: scroll;
      flex: 3;
      align(center);

      &::-webkit-scrollbar {
        display: none;
      }
    }

    .model-r {
      position: relative;
      overflow: hidden;
      flex: 9;
      border-left: 1px solid $color-border;

      .model-list-wrap {
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        padding-left: 20px;
        height: 100%;
        overflow: hidden;
        overflow-y: scroll;
      }
    }
  }

  .btn-wrap {
    padding: 47px 0 32px;
    align(center);

    .btn {
      display: inline-block;
      width: 100px;
      btn-circle(40px, $radius-sm);

      & + .btn {
        margin-left: $spacing-lg;
      }

      &.grey-btn {
        border: 1px solid #B7B7B7;
      }

      &.default {
        color: $color-font-grey;
        background-color: rgbaMain($opacity-light);
      }
    }
  }
}
</style>
