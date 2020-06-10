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
          >
            <div
              class="pic-list"
              v-if="child.pictureList && child.pictureList.length > 0"
            >
              <div
                v-for="pic in child.pictureList"
                :key="pic.id"
                class="pic-item"
                @click="selectPic(pic.originImage, pic)"
              >
                <img
                  class="pic-item"
                  v-lazy="
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
              @click="selectPic(pic.originImage, pic)"
            >
              <img
                v-lazy="
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
    <div class="infinite-list-wrapper">
      <p v-if="loading">加载中...</p>
      <p v-if="noMore">我是有底线的哦～</p>
    </div>
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
    selectPic(url, pic) {
      this.$emit("change", url, pic);
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
<style scoped lang="stylus" rel="stylesheet/stylus">
.picture-list {
  text-align: left;

  .tag {
    position: absolute;
    top: 2px;
    right: -18px;
    display: inline-block;
    width: 66px;
    height: 18px;
    line-height: 18px;
    text-align: center;
    font-size: 12px;
    color: #ffffff;
    background-color: #F36902;
    transform: scale(0.8) rotate(38deg);
  }

  >>>.el-tabs {
    .el-tabs__header {
      padding: 0 18px;
      margin: 0;
      box-sizing: border-box;
      background-color: #ffffff;

      .el-tabs__nav-wrap {
        padding: 0;

        .el-tabs__nav-prev, .el-tabs__nav-next {
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
            background-color: transparent;
          }

          .el-tabs__item {
            padding: 0 10px;
            font-size: 14px;
            color: #4A4A4A;

            &:nth-child(2) {
              /* margin-left:0 */
            }

            &.is-active, &:hover {
              color: #F21E1C;
            }
          }
        }

        &::after {
          height: 1px;
        }
      }
    }

    .el-tabs__content {
      position: absolute;
      width: 100%;
      height: calc(100% - 60px);

      .el-tabs {
        .el-tabs__content {
          overflow-y: auto;

          &::-webkit-scrollbar {
            display: none;
          }
        }
      }

      .el-tabs__header {
        padding: 0 15px;

        .el-tabs__nav-scroll {
          overflow: scroll;
          -webkit-overflow-scrolling: touch;
          border-bottom: 1px solid #F1F1F1;

          .el-tabs__active-bar {
            background-color: transparent;
          }

          .el-tabs__item {
            font-size: 12px;
          }
        }
      }

      .el-tabs__content {
        .pic-list {
          display: flex;
          flex-wrap: wrap;
          justify-content: flex-start;
          padding: 20px 10px 10px;
          box-sizing: border-box;

          .pic-item {
            position: relative;
            flex: 0 0 29.3%;
            height: 140px;
            margin: 0 2% 15px;
            /* padding: 8px */
            box-sizing: border-box;
            border-radius: 8px;
            -webkit-appearance: none;
            -webkit-backface-visibility: hidden;
            -webkit-transform: translate3d(0, 0, 0);
            overflow: hidden;
            background-color: #ffffff;

            img {
              position: absolute;
              display: inline-block;
              top: 50%;
              left: 50%;
              width: 100%;
              height: auto;
              transform: translate3d(-50%, -50%, 0);
            }

            .name {
              margin-top: 5px;
              font-size: 14px;
              color: #4A4A4A;
              text-align: center;
            }
          }
        }
      }

      .infinite-list-wrapper {
        padding: 0 15px;
        text-align: center;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        padding-bottom: 20px;

        &::-webkit-scrollbar {
          display: none;
        }
      }
    }
  }

  .pic-box {
    position: relative;
    width: 100%;
    height: calc(100vh - 240px);
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    padding-right: 20px;

    &::-webkit-scrollbar {
      width: 0;
      height: 0;
      display: none;
    }

    .pic-list {
      display: flex;
      flex-wrap: wrap;
      justify-content: flex-start;
      margin: 20px 10px 10px;

      .pic-item {
        position: relative;
        flex: 0 0 29.3%;
        height: 140px;
        margin: 0 2% 15px;
        /* padding: 8px */
        box-sizing: border-box;
        border-radius: 8px;
        -webkit-appearance: none;
        -webkit-backface-visibility: hidden;
        -webkit-transform: translate3d(0, 0, 0);
        overflow: hidden;
        background-color: #ffffff;

        img {
          position: absolute;
          display: inline-block;
          top: 50%;
          left: 50%;
          width: 100%;
          height: auto;
          transform: translate3d(-50%, -50%, 0);
        }

        .name {
          margin-top: 5px;
          font-size: 14px;
          color: #4A4A4A;
          text-align: center;
        }
      }
    }
  }

  .pic-tab {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    margin: 20px 10px 10px;
    max-height: calc(100vh - 271px);
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    padding-bottom: 20px;

    &::-webkit-scrollbar {
      width: 0;
      height: 0;
      display: none;
    }

    .pic-item {
      position: relative;
      flex: 0 0 29.3%;
      height: 140px;
      margin: 0 2% 15px;
      box-sizing: border-box;
      border-radius: 8px;
      -webkit-appearance: none;
      -webkit-backface-visibility: hidden;
      -webkit-transform: translate3d(0, 0, 0);
      overflow: hidden;
      background-color: #ffffff;

      img {
        position: absolute;
        display: inline-block;
        top: 50%;
        left: 50%;
        width: 100%;
        height: auto;
        transform: translate3d(-50%, -50%, 0);
      }

      .name {
        margin-top: 5px;
        font-size: 14px;
        color: #4A4A4A;
        text-align: center;
      }
    }
  }

  >>>.van-tabs {
    .van-tabs__wrap {
      padding: 0;

      .van-tab {
        flex: 0 0 22%;

        &.van-tab--active {
          .van-tab__text {
            color: #1CB8CE;
          }
        }
      }

      &.van-hairline--top-bottom, &.hairline-unset--top-bottom {
        &:after {
          border-width: 0;
        }
      }

      .van-tabs__line {
        height: 2px;
        background-color: #f21e1c;
      }
    }

    .van-tabs__content {
      .pic-list {
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        margin: 20px 10px 10px;
        max-height: calc(100vh - 271px);
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        padding-bottom: 20px;

        &::-webkit-scrollbar {
          width: 0;
          height: 0;
          display: none;
        }

        .pic-item {
          position: relative;
          flex: 0 0 29.3%;
          height: 140px;
          margin: 0 2% 15px;
          box-sizing: border-box;
          border-radius: 8px;
          -webkit-appearance: none;
          -webkit-backface-visibility: hidden;
          -webkit-transform: translate3d(0, 0, 0);
          overflow: hidden;
          background-color: #ffffff;

          img {
            position: absolute;
            display: inline-block;
            top: 50%;
            left: 50%;
            width: 100%;
            height: auto;
            transform: translate3d(-50%, -50%, 0);
          }

          .name {
            margin-top: 5px;
            font-size: 14px;
            color: #4A4A4A;
            text-align: center;
          }
        }
      }

      .van-tab {
        font-size: 12px;
        flex: 0 0 20%;
      }
    }
  }
}
</style>
