<!--
 * @Author: yaowei
 * @Date: 2019-11-25 14:26:49
 * @LastEditors: yaowei
 * @LastEditTime: 2019-06-24 15:13:04
-->
<template>
  <div class="picture-wrap">
    <van-tabs
      class="picture-category"
      @click="chooseTab"
      :class="{ 'current-category': hasChildren }"
      v-model="curCategory"
      :ellipsis="false"
    >
      <van-tab
        v-for="(category, index) in picData"
        :key="index"
        :swipe-threshold="3"
      >
        <template #title>
          <span class="sprite-icon icon_fanhui"></span>{{ category.name }}
        </template>
        <van-tabs
          class="sub-category"
          v-show="
            curCategory === index &&
            category.childrenList &&
            category.childrenList.length > 0
          "
          :ellipsis="false"
        >
          <van-tab
            v-for="(subCategory, subIndex) in category.childrenList"
            :title="subCategory.name"
            :key="subIndex"
            :swipe-threshold="3"
          >
            <div
              class="pic-list"
              :class="{ 'more-pic-list': showMore }"
              v-show="
                subCategory.pictureList && subCategory.pictureList.length > 0
              "
            >
              <!-- 图库箭头 -->
              <span
                class="more-arrow"
                @click.stop="showMorePic()"
                v-show="!showMore"
              >
                <van-icon name="arrow" />
              </span>
              <span
                class="more-arrow"
                @click.stop="showMorePic()"
                v-show="showMore"
              >
                <van-icon name="arrow-left" />
              </span>
              <div class="img-list">
                <div
                  class="img-wrap"
                  v-for="(picture, picIndex) in subCategory.pictureList"
                  :key="picIndex"
                  @click.stop="selectPic(picture)"
                >
                  <img
                    v-lazy="
                      picture.originImage
                        ? picture.originImage +
                          '?x-oss-process=image/resize,h_400,l_400'
                        : picture.thumbnail
                    "
                    :alt="picture.name"
                  />
                </div>
              </div>
            </div>
          </van-tab>
        </van-tabs>
        <div
          class="pic-list"
          :class="{ 'more-pic-list': showMore }"
          v-show="category.pictureList && category.pictureList.length > 0"
        >
          <!-- 图库箭头 -->
          <span
            class="more-arrow"
            @click.stop="showMorePic()"
            v-show="!showMore"
          >
            <van-icon name="arrow" />
          </span>
          <span
            class="more-arrow"
            @click.stop="showMorePic()"
            v-show="showMore"
          >
            <van-icon name="arrow-left" />
          </span>
          <div class="img-list">
            <div
              class="img-wrap"
              v-for="(picture, picIndex) in category.pictureList"
              :key="picIndex"
              @click.stop="selectPic(picture)"
            >
              <img
                v-lazy="
                  picture.originImage
                    ? picture.originImage +
                      '?x-oss-process=image/resize,h_400,l_400'
                    : picture.thumbnail
                "
                :alt="picture.name"
              />
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
export default {
  name: "picture",
  props: {
    picData: {
      type: Array,
      default: null,
    },
    topValue: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      curCategory: 0, // 当前图库分类
      hasChildren: true, // 是否有子类
      showMore: false, // 是否展示更多图库
    };
  },
  methods: {
    // 选择分类
    chooseTab(index) {
      this.curCategory = index;

      if (this.hasChildren) {
        this.hasChildren = false;
      } else {
        if (
          this.picData[index] &&
          this.picData[index].childrenList.length > 0
        ) {
          this.hasChildren = true;
        }
      }
    },
    // 选择图片
    selectPic(item) {
      sessionStorage.removeItem("imageInfo"); // 移除推荐页传递数据
      this.$emit("change", item);
    },
    // 展示更多图库
    showMorePic() {
      if (this.showMore) {
        this.showMore = false;
      } else {
        this.showMore = true;
      }
    },
  },
  watch: {
    picData(data) {
      this.picData = data;
      this.hasChildren = false;
      if (this.picData.length > 0) {
        if (this.picData[0] && this.picData[0].childrenList.length > 0) {
          this.hasChildren = true;
        }
      }
    },
  },
};
</script>

<style lang="stylus" scoped>
$dark = #000;
$white = #fff;
$blue = #0076A5;
$gray = #959595;
$light-dark = #1D1D1D;

.picture-wrap {
  position: fixed;
  top: 44px;
  left: 0;
  right: 0;

  .picture-category {
    position: absolute;
    top: 0;
    right: 0;
    left: 0;
    font-size: 15px;
    color: $dark;
    height: 44px;
    line-height: 44px;

    >>>.van-tabs__nav--line {
      padding-bottom: 0;
      z-index: 10;
    }

    .icon_fanhui {
      display: none;
    }

    &.current-category {
      >>>.van-tab--active {
        position: fixed;
        top: 44px;
        left: -1px;
        width: 88px;
        padding: 0;
        padding-right: 6px;
        height: 46px;
        font-size: 15px !important;
        text-align: center;
        background: $white url('../../assets/images/background/customized_var_open.png') no-repeat;
        background-size: auto 100%;
        background-position: top right;
        z-index: 11;

        .icon_fanhui {
          display: inline-block;
          margin-right: 3px;
        }
      }

      .sub-category {
        >>>.van-tabs__wrap {
          z-index: 10;
        }

        >>>.van-tab--active {
          position: relative;
          top: 0;
          left: 0;
          padding: 0 8px;
          font-size: 13px !important;
          color: $dark;
        }
      }
    }
  }

  .sub-category {
    top: -46px;

    >>>.van-tabs__wrap {
      position: absolute;
      top: 0;
      right: 0;
      left: 86px;
      padding-bottom: 0;
      height: 46px;
      z-index: 0;
    }

    >>>.van-tab {
      font-size: 12px;
      color: $gray;

      &.van-tab--active {
        position: relative;
        width: 33.3%;
        padding-bottom: 2px;
        background: $white;

        &:after {
          content: '';
          position: absolute;
          bottom: 10px;
          width: 0;
          height: 0;
          border-right: 6px solid transparent;
          border-left: 6px solid transparent;
          border-bottom: 6px solid $light-dark;
          transform: scaleX(0.8);
        }
      }
    }
  }

  >>>.van-tabs {
    .van-tabs__wrap--scrollable {
      .van-tabs__nav--complete {
        padding-left: 0;
        padding-right: 0;
      }
    }

    .van-tabs__line {
      display: none;
    }

    .van-tab {
      width: 33.3%;
      padding: 0 5px;
      background: $white;

      &.van-tab--active {
        font-size: 16px;
        color: $blue;
      }
    }

    .van-tabs__wrap {
      height: 46px;
    }
  }
}

.pic-list {
  position: fixed;
  top: 46px;
  bottom: 0;
  left: 0;
  width: 70px;
  padding: 0 10px 0 0;
  background-color: $white;
  box-shadow: 0px 35px 21px 0px rgba(212, 212, 212, 0.32);
  transition: width 0.6s;

  &.more-pic-list {
    width: 140px;
  }

  .img-list {
    position: absolute;
    padding-top: 56px;
    height: 100%;
    overflow-y: scroll;
    box-sizing: border-box;

    &::-webkit-scrollbar {
      display: none;
    }
  }

  .more-arrow {
    position: absolute;
    top: 50%;
    right: -18px;
    width: 18px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    background-color: $white;
    transform: perspective(0.5em) rotateY(10deg);
    border-radius: 0 3px 3px 0;

    .van-icon {
      top: 3px;
      color: $gray;
    }
  }

  .img-wrap {
    float: left;
    margin: 0 0 10px 10px;
    width: 60px;
    height: 90px;
    overflow: hidden;
    border-radius: 3px;

    img {
      width: 100%;
      height: auto;
      vertical-align: middle;
    }
  }
}
</style>
