<!--
 * @Author: yaowei
 * @Date: 2019-05-11 18:04:13
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-08 16:37:49
-->
<template>
  <div class="picture-con">
    <van-tabs
      class="picture-category"
      @click="chooseTab"
      v-model="curIndex"
      :ellipsis="false"
    >
      <van-tab
        v-for="(category, index) in picCategory"
        :key="index"
        :swipe-threshold="3"
        :class="{
          hasChild:
            category.twoPictureCategory &&
            category.twoPictureCategory.length > 0,
        }"
      >
        <template #title
          >{{ category.name }}
          <p class="tag" v-if="category.type === 2 && curIndex === index">
            <span>版权</span>
          </p></template
        >

        <!-- 二级分类 -->
        <div
          v-if="
            category.twoPictureCategory &&
            category.twoPictureCategory.length > 0
          "
        >
          <van-tabs
            class="picture-category child"
            @click="chooseTabS"
            v-model="nextIndex"
            :ellipsis="false"
          >
            <van-tab
              v-for="(cate, cateIndex) in category.twoPictureCategory"
              :key="cateIndex"
              :swipe-threshold="3"
            >
              <template #title>{{ cate.name }} </template>
            </van-tab>
          </van-tabs>
        </div>

        <!-- 图片列表 -->
        <div
          class="pic-list"
          v-show="category.picList && category.picList.length > 0"
        >
          <div class="img-list clearfix">
            <div
              class="img-wrap"
              v-if="index === 0 && uploadValid"
              @click.stop="chooseImg()"
            >
              <div class="img upload">
                <span class="sprite-icon icon-add_picture"></span>
                <p class="upload-text">上传图片</p>
              </div>
            </div>
            <div
              class="img-wrap"
              v-for="(picture, picIndex) in category.picList"
              :key="picIndex"
              @click.stop="selectPic(picture)"
            >
              <div class="img">
                <img
                  v-lazy="
                    picture.originImage +
                    '?x-oss-process=image/resize,h_400,l_400'
                  "
                  :alt="picture.pictureName"
                />
              </div>
            </div>
          </div>
        </div>
        <div
          class="no-data-wrap"
          v-show="category.picList && category.picList.length <= 0"
        >
          <NoData></NoData>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
// 组件
import NoData from "components/noData/noData";

export default {
  name: "Picture",
  props: {
    picCategory: {
      type: Array,
      default: [],
    },
    isFirstEnter: {
      type: Boolean,
      default: false,
    },
    uploadValid: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      curIndex: 0, // 当前图库分类
      nextIndex: 0, // 二级图库分类
    };
  },
  methods: {
    // 选择分类
    chooseTab(index) {
      this.curIndex = index;
      this.nextIndex = 0;
      let curCategory = this.picCategory[index].id;
      this.$emit("changeCategory", curCategory, 6);
    },
    // 选择二级分类
    chooseTabS(index) {
      this.nextIndex = index;
      let curCategory =
        this.picCategory[this.curIndex].twoPictureCategory[index].id;
      this.$emit("changeCategoryMore", curCategory, 6);
    },
    // 选择图片
    selectPic(item) {
      this.$emit("changeSelect", item);
    },
    // 上传本地图片
    chooseImg() {
      this.$emit("chooseImg", "picture");
    },
  },
  watch: {
    picCategory(data) {
      this.picCategory = data;
    },
  },
  components: {
    NoData,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.picture-con {
  position: absolute;
  padding: 0 $spacing-base;
  width: 100%;
  height: 100%;
  background-color: $color-bg-white;

  .picture-category {
    position: relative;
    width: 100%;
    height: 100%;

    &.child {
      >>>.van-tabs__wrap {
        height: 40px;

        .van-tabs__nav {
          padding: 5px 0 $spacing-sm;
          height: 24px;
        }

        .van-tab {
          font-size: $font-sm;

          & + .van-tab {
            margin-left: 25px;
          }
        }

        .van-tab--active {
          font-size: $font-xm;
        }
      }
    }
  }

  >>>.van-tabs__wrap {
    height: 50px;

    .van-tabs__nav {
      padding: $spacing-base 0 $spacing-sm;
      height: 24px;
    }

    .van-tab {
      position: relative;
      padding: 0;
      font-size: $font-xm;
      color: $color-font-grey;

      & + .van-tab {
        margin-left: 30px;
      }

      .tag {
        position: absolute;
        top: -15px;
        left: calc(50% + 24px);
        width: 32px;
        height: 18px;
        font-size: $font-sm;
        align(center);
        bg-image('banquan');
        background-size: 100% 100%;

        span {
          position: relative;
          top: -1px;
          display: block;
          color: $color-font-base;
          transform: scale(0.8);
        }
      }
    }

    .van-tab--active {
      font-size: $font-lg;
      color: $color-font-base;
    }

    .van-tabs__line {
      position: absolute;
      bottom: 5px;
      width: 20px;
      height: 10px;
      bg-image('home-tab');
      background-color: transparent;
      background-size: 100% 100%;
    }
  }

  >>>.van-tab__pane {
    position: absolute;
    top: 50px;
    bottom: 60px;
    width: 100%;

    &.hasChild {
      bottom: 100px;
    }
  }

  .pic-list {
    position: relative;
    padding: $spacing-sm 7px;
    width: 100%;
    height: 100%;

    .img-list {
      position: absolute;
      top: 0;
      right: 7px;
      bottom: 0;
      left: 7px;
      padding-top: $spacing-base;

      &::-webkit-scrollbar {
        display: none;
      }
    }

    .img-wrap {
      float: left;
      width: calc((100% / 3));
      padding: 7px;

      .img {
        width: 100%;
        height: 100%;
        border-radius: $radius-base;
        box-shadow: 0px 1px 5px 0px rgba(213, 222, 229, 0.3);
        overflow: hidden;

        &.upload {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 180px;
          background-color: $color-bg;

          .upload-text {
            margin-top: 10px;
            font-size: $font-sm;
            color: $color-font-grey;
          }
        }
      }

      img {
        position: relative;
        left: 50%;
        min-width: 100%;
        height: 180px;
        transform: translateX(-50%);
      }
    }
  }
}
</style>