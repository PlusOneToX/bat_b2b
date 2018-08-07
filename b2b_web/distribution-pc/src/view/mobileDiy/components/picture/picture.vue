<!--
 * @Author: yaowei
 * @Date: 2018-05-11 18:04:13
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-13 12:10:41
-->
<template>
  <div class="picture-list">
    <el-tabs v-model="pictab">
      <el-tab-pane
        v-for="item in picData"
        :key="item.id"
        :label="item.name"
        :name="item.name"
      >
        <el-tabs v-model="itemtab" v-if="item.childrenList.length > 0">
          <el-tab-pane
            v-for="child in item.childrenList"
            :key="child.id"
            :label="child.name"
            :name="child.name"
            class="pic-box has-child"
          >
            <div
              class="pic-list"
              v-if="child.pictureList && child.pictureList.length > 0"
            >
              <div
                v-for="pic in child.pictureList"
                :key="pic.id"
                class="pic-item"
                @click="selectPic(pic)"
              >
                <img
                  class="pic-item"
                  :src="
                    pic.originImage
                      ? pic.originImage +
                        '?x-oss-process=image/resize,h_400,l_400'
                      : pic.thumbnail
                  "
                  alt=""
                />
                <div v-show="picname" class="name">{{ pic.name }}</div>
                <div class="tag" v-if="pic.type === 2">版权</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
        <div v-else class="pic-box">
          <ul class="pic-list" v-if="item.pictureList.length > 0">
            <li
              v-for="pic in item.pictureList"
              :key="pic.id"
              class="pic-item list-item"
              @click="selectPic(pic)"
            >
              <img
                :src="
                  pic.originImage
                    ? pic.originImage +
                      '?x-oss-process=image/resize,h_400,l_400'
                    : pic.thumbnail
                "
                alt
              />
              <div v-show="picname" class="name">{{ pic.name }}</div>
              <div class="tag" v-if="pic.type === 2">版权</div>
            </li>
          </ul>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div class="toast"></div>
  </div>
</template>
<script type="text/ecmascript-6">
export default {
  name: "picture-list",
  props: {
    picData: {
      type: Array,
      default: null,
    },
    picname: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      pictab: "",
      itemtab: "",
      count: 30,
      loading: false,
      distributorId: "", // 分销商ID
    };
  },
  created() {
    this.distributorId = parseInt(localStorage.getItem("distributorId"));
    this.pictab = this.picData[0].name;
  },
  methods: {
    selectPic(pic) {
      this.$emit("changeSelect", pic);
    },
    load() {
      this.loading = true;
      setTimeout(() => {
        this.count += 2;
        this.loading = false;
      }, 2000);
    },
  },
  watch: {
    picData(data) {
      this.picData = data;
      if (this.picData.length > 0) {
        if (this.pictab !== "") {
          this.pictab = this.picData[0].name;
        }
        if (
          this.picData[0].childrenList &&
          this.picData[0].childrenList.length > 0
        ) {
          this.itemtab = this.picData[0].childrenList[0].name;
        } else {
          if (
            this.picData[0].pictureList &&
            this.picData[0].pictureList.length > 0
          ) {
            this.itemtab = this.picData[0].pictureList[0].name;
          }
        }
      }
    },
    pictab(value) {
      if (value !== "") {
        this.pictab = value;
        this.picData.forEach((item, index) => {
          if (item.name === value) {
            if (item.childrenList && item.childrenList.length > 0) {
              this.itemtab = item.childrenList[0].name;
            } else {
              if (item.pictureList && item.pictureList.length > 0) {
                this.itemtab = item.pictureList[0].name;
              }
            }
          }
        });
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import url("../../../../assets/less/variable.less");
@import url("../../../../assets/less/mixin.less");

.picture-list {
  text-align: left;

  .tag {
    position: absolute;
    top: 0.2rem;
    right: -1.8rem;
    display: inline-block;
    width: 6.6rem;
    height: 1.8rem;
    line-height: 1.8rem;
    text-align: center;
    font-size: 1.2rem;
    color: @color-font-white;
    background-color: @color-orange;
    transform: scale(0.8) rotate(38deg);
  }

  /deep/ .el-tabs {
    .el-tabs__header {
      position: relative;
      margin: 0;
      box-sizing: border-box;
      background-color: @color-bg-white;

      &::before,
      &::after {
        content: "";
        display: inline-block;
        position: absolute;
        top: 0;
        width: 2rem;
        height: 100%;
        background-color: @color-bg-white;
        z-index: 10;
      }

      &::before {
        left: 0;
      }

      &::after {
        right: 0;
      }

      .el-tabs__nav-wrap {
        padding: 0;
        &::after {
          background-color: transparent;
        }

        .el-tabs__nav-prev,
        .el-tabs__nav-next {
          display: none;
        }

        .el-tabs__nav-scroll {
          overflow: scroll;
          -webkit-overflow-scrolling: touch;
          opacity: 1 !important;

          .el-tabs__nav {
            transform: translateX(0) !important;
          }

          &::-webkit-scrollbar {
            width: 0;
            height: 0;
            display: none;
          }

          .el-tabs__active-bar {
            width: 0 !important;
            background-color: transparent;
          }

          .el-tabs__item {
            padding: 0 2rem;
            font-size: 1.4rem;
            color: @color-font-darkGrey;

            &.is-active,
            &:hover {
              position: relative;
              font-size: @font-xl;
              color: @color-font-base !important;

              &::after {
                content: "";
                position: absolute;
                bottom: 0;
                left: 50%;
                width: 2.9rem;
                height: 0.2rem;
                background-color: @color-font-base;
                transform: translateX(-50%);
              }
            }
          }
        }
      }
    }

    .el-tabs__content {
      position: relative;
      width: 100%;
      height: calc(100% - 6rem);

      .el-tabs__header {
        .el-tabs__nav-scroll {
          .el-tabs__item {
            font-size: 1.2rem;

            &.is-active,
            &:hover {
              font-size: @font-base;

              &::after {
                width: 2rem;
              }
            }
          }
        }
      }
    }
  }

  .pic-box {
    position: relative;
    top: 1.5rem;
    width: 100%;
    height: calc(70vh - 9rem);
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    padding-right: 2rem;

    &.has-child {
      height: calc(70vh - 13rem);
    }

    &::-webkit-scrollbar {
      width: 0;
      height: 0;
      display: none;
    }
  }

  .pic-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    padding: 0 1rem;

    .pic-item {
      position: relative;
      flex: 0 0 29.3%;
      height: 14rem;
      margin: 0 2% 1.5rem;
      box-sizing: border-box;
      border-radius: 0.8rem;
      -webkit-appearance: none;
      backface-visibility: hidden;
      transform: translate3d(0, 0, 0);
      overflow: hidden;
      background-color: @color-bg-white;
      box-shadow: 0px 1px 5px 0px rgba(213, 222, 229, 0.3);

      img {
        position: absolute;
        display: inline-block;
        top: 50%;
        left: 50%;
        width: 100%;
        height: auto;
        transform: translate3d(-50%, -50%, 0);
      }
    }
  }
}
</style>
