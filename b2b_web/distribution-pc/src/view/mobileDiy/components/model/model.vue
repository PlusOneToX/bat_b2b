<!--
 * @Author: yaowei
 * @Date: 2018-05-08 10:21:33
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-13 12:08:38
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
        <span class="title-l" @click="handleCancel()">取消</span>
        <span class="title-c">选择手机型号</span>
        <span
          class="title-r"
          :class="{ default: !curMobile }"
          @click="handleConfirm()"
          >确定</span
        >
      </h6>
    </template>

    <div class="model-list">
      <div class="model-l">
        <p
          v-for="(item, index) in modelList"
          :key="index"
          class="model-item"
          :class="{ active: curIndex === index }"
          @click="changeModel(index)"
        >
          <span>
            <i>{{ item.name }}</i>
          </span>
        </p>
      </div>
      <div class="model-r">
        <div
          v-for="(model, pIndex) in modelList"
          :key="pIndex"
          class="model-list-wrap"
          :class="{ active: curIndex === pIndex }"
        >
          <p
            v-for="(item, index) in model.childrenList"
            :key="index"
            class="model-item"
            :class="{
              default: item.underStockFlag,
              active:
                (!hasClicked && mobile === item.name) ||
                (hasClicked && curMobile === item.name),
            }"
            @click="chooseModel(item)"
          >
            <span>
              <i>{{ item.name }}{{ item.underStockFlag ? "（缺货）" : "" }}</i>
            </span>
          </p>
        </div>
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
  },
  data() {
    return {
      curMobile: "",
      curIndex: 0, // 默认展示第一项
      curChooseModel: {}, // 当前选中机型
      hasClicked: false, // 是否点击
    };
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
                  this.mobile.replace(/\s*/g, "").toLowerCase()
                ) {
                  // 获取当前选中大类
                  this.curIndex = pIndex;
                  this.curMobile = model.name;
                }
              });
            }
          });
        }
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
      this.hasClicked = true;
      if (!item.underStockFlag) {
        this.curMobile = item.name;
      }
    },
    // 跳过
    handleCancel() {
      this.$emit("cancel");
    },
    // 确定
    handleConfirm() {
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

              this.$emit("confirm", this.curChooseModel);
            }
          });
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
@import url("../../../../assets/less/variable.less");
@import url("../../../../assets/less/mixin.less");

.model-content {
  padding: 2.4rem 2rem 0;
  font-size: @font-base;
  line-height: 2rem;

  .title {
    .align(center);

    .title-c {
      font-size: @font-lg;
    }

    .title-l {
      float: left;
      color: @color-font-grey;
    }

    .title-r {
      float: right;
      font-weight: 500;
    }
  }

  .cur-model {
    margin-top: 1.2rem;
    font-size: @font-sm;
    font-weight: 600;
    line-height: 1.7rem;
    .align(center);

    &.tips {
      color: @color-font-base;
    }
  }

  .model-list {
    margin-top: 2.8rem;
    height: 28.4rem;
    display: flex;

    .model-item {
      line-height: 2.5rem;

      & + .model-item {
        margin-top: @spacing-sm;
      }

      &.default {
        color: @color-font-grey;
      }

      &.active {
        span {
          position: relative;

          &::before {
            content: "";
            position: absolute;
            bottom: 0;
            left: -0.5rem;
            width: calc(100% + 1rem);
            height: 0.6rem;
            background-color: @color-main-lighter;
            border-radius: 0.6rem;
            z-index: 1;
          }
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
      .align(center);

      &::-webkit-scrollbar {
        display: none;
      }
    }

    .model-r {
      position: relative;
      overflow-y: scroll;
      flex: 7;
      padding-left: 2.8rem;
      border-left: 0.1rem solid @color-border;

      .model-list-wrap {
        position: absolute;
        display: none;

        &.active {
          display: block;
        }
      }

      .model-item {
        &.active {
          span {
            &::before {
              bottom: 0.2rem;
              left: -0.3rem;
              width: calc(100% + 0.5rem);
            }
          }
        }
      }
    }
  }

  .btn-wrap {
    padding: 4.7rem 0 3.2rem;
    .align(center);

    .btn {
      display: inline-block;
      width: 10rem;
      .btn-circle(4rem, @radius-sm);

      & + .btn {
        margin-left: @spacing-lg;
      }

      &.grey-btn {
        border: 0.1rem solid #b7b7b7;
      }

      &.default {
        color: @color-font-grey;
        background-color: rgba(255, 218, 1, @opacity-light);
      }
    }
  }
}
</style>
