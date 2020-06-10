<template>
  <div
    class="picture-list"
    :class="{ 'ry-style': Number(distributorId) === 4378 }"
  >
    <van-tabs :ellipsis="false">
      <van-tab
        v-for="item in picData"
        :key="item.id"
        :title="item.name"
        :name="item.id"
        :swipe-threshold="3"
      >
        <van-tabs
          v-if="item.childrenList && item.childrenList.length > 0"
          :ellipsis="false"
        >
          <van-tab
            v-for="child in item.childrenList"
            :key="child.id"
            :title="child.name"
            :name="child.id"
            :swipe-threshold="3"
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
                <div
                  v-show="distributorId === 2529 || distributorId === 1217"
                  class="pic-code"
                >
                  {{ pic.id }}
                </div>
              </div>
            </div>
          </van-tab>
        </van-tabs>
        <div v-else class="pic-box">
          <ul
            class="pic-list"
            v-if="item.pictureList && item.pictureList.length > 0"
          >
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
              <div
                v-show="distributorId === 2529 || distributorId === 1217"
                class="pic-code"
              >
                {{ pic.id }}
              </div>
            </li>
          </ul>
        </div>
      </van-tab>
    </van-tabs>
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
      count: 30,
      loading: false,
      distributorId: "", // 分销商ID
    };
  },
  created() {
    this.distributorId = parseInt(localStorage.getItem("distributorId"));
  },
  methods: {
    selectPic(url, pic) {
      sessionStorage.removeItem("imageInfo"); // 移除推荐页传递数据
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
};
</script>
<style scoped lang="stylus" rel="stylesheet/stylus">
.picture-list {
  text-align: left;

  >>>.van-tabs {
    .van-tabs__wrap {
      padding: 0 10px 5px 0;
      height: 32px;

      .van-tab {
        display: inline-block;
        flex: 0 0 auto;
        flex-basis: auto !important;
        margin: 0 6px;

        span {
          overflow: visible !important;
        }

        &.van-tab--active {
          .van-tab__text {
            color: #0076A5;
          }
        }
      }

      &.van-hairline--top-bottom, &.hairline-unset--top-bottom {
        &:after {
          border-width: 0;
        }
      }

      .van-tabs__nav--line {
        padding-bottom: 10px;
      }

      .van-tabs__line {
        height: 2px;
        background-color: #0076A5;
      }
    }

    .van-tabs__content {
      .pic-list {
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        margin: 20px 10px 10px;
        max-height: calc(100vh - 300px);
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

          .pic-code {
            position: absolute;
            bottom: 4px;
            right: 4px;
            display: inline-block;
            width: 48px;
            height: 16px;
            line-height: 16px;
            text-align: center;
            font-size: 12px;
            color: #ffffff;
            border-radius: 8px;
            background-color: rgba(56, 56, 56, 0.4);
            transform: translateZ(1000px) scale(0.9);
            z-index: 999;
          }
        }
      }

      .van-tab {
        font-size: 12px;

        .van-ellipsis {
          display: visible !important;
        }
      }
    }
  }
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-main = #256FFF;

// 荣耀
.ry-style {
  >>>.van-tabs {
    .van-tabs__wrap {
      .van-tab {
        &.van-tab--active {
          .van-tab__text {
            color: $color-main;
          }
        }
      }

      .van-tabs__line {
        background-color: $color-main;
      }
    }
  }
}
</style>