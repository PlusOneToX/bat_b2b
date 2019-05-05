<template>
  <view class="classify">
    <view class="classify-top">
      <view class="search-view">
        <image src="../../static/imgs/icon_search.png"></image>
        <input placeholder="磁吸手机壳" disabled="true" @click="toSearch" />
      </view>
    </view>

    <view class="cf-module" id="cfWrap"> 
      <!-- 左边菜单 -->
      <view class="cf-lf">
        <view class="cf-lfList">
          <view
            v-for="(item, index) in classifyData"
            :key="item.id" 
            @click="choiceClassify(item, index)"
            class="cf-classifyView"
            :class="classifyIndex == index ? 'cf-lfHover' : ''" 
            :style="{
              'border-radius':
                classifyIndex + 1 == index ? '0rpx 16rpx 0rpx 0rpx' : '0',
            }"
          >
            <text
              class="iconfont"
              :class="classifyIndex == index ? 'icon-icon_zy' : ''"
              :style="{
                color: themeColor,
                'font-weight': classifyIndex == index ? 'bold' : '',
              }"
            ></text>
            <text
              :style="{ color: classifyIndex == index ? themeColor : '' }"
              >{{ item.appletName }}</text
            >
          </view>
        </view>
      </view>
      <!-- 右边 -->
      <view class="cf-rg">
        <scroll-view
          scroll-y="true"
          :scroll-into-view="scrollIntoView"
          class="cf-rg-scroll"
          @scroll="scrolltoupperFun"
        >
          <view
            v-for="item in classifyData"
            :key="item.id"
            class="cf-rg-line"
            :class="'classifyS' + item.id"
            :id="'classifyS' + item.id"
          >
            <!-- 轮播图-start &&classifyIndex==index -->
            <view
              class="classify-swiperView"
              v-if="item.userClassifyBannerDTOS.length > 0"
              id="bannerss"
            >
              <swiper
                class="classify-swiper"
                :indicator-dots="indicatorDots"
                :autoplay="autoplay"
                :interval="interval"
                :indicator-active-color="dotActiveColor"
                :indicator-color="dotColor"
                circular
              >
                <swiper-item
                  v-for="(items, index) in item.userClassifyBannerDTOS"
                  :key="index"
                >
                  <image
                    :src="items.imgUrl"
                    class="classify-swiperImg"
                    @click="toThreePageFun(items.jumpUrl)"
                  ></image>
                </swiper-item>
              </swiper>
            </view>
            <!-- 轮播图-end -->
            <view class="cf-rg-lineName">{{ item.appletName }}</view>
            <view
              class="cf-rg-lineGoofList"
              v-for="items in item.subClassifies"
              :key="items.id"
              @click="toTwoPage(items)"
            >
              <image :src="items.imageUrl" mode="scaleToFill"></image>
              <view>{{ items.appletName }}</view>
            </view>
            <view style="clear: both"></view>
          </view>
          <view
            class="cf-rg-pullTip"
            v-if="classifyData && classifyData.length > 0"
          >
            <image src="../../static/imgs/icon_slidingtop.png"></image>
            <view>向上拉继续浏览</view>
          </view>
        </scroll-view>
      </view>
    </view>

    <!-- 提示框 -->
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { classifyList } from "../../common/api.js";
import goodsItem from "../../components/myComponents/goodsItem.vue";
export default {
  components: {
    goodsItem,
  },
  data() {
    return {
      classifyIndex: 0,
      classifyData: [], //分类列表
      page: 1,
      size: 10,
      totalPage: 0,
      classifyId: "", //分类id
      userId: "",
      tipTextShow: false,
      tipText: "",
      themeColor: "",
      scrollIntoView: "",
      // 轮播图
      indicatorDots: true,
      autoplay: true,
      interval: 3000,
      dotColor: "none",
      dotActiveColor: "",
      curClickedIndex: 0, // 记录当前点击菜单索引
    };
  },

  onLoad() {
    this.themeColor = uni.getStorageSync("themeColor");
    this.dotActiveColor = uni.getStorageSync("themeColor");
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
    }
    this.classifyListFun();
  },

  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // 进入搜索
    toSearch() {
      uni.navigateTo({ url: "/pages/index/search" });
    },

    // 右边滚动
    scrolltoupperFun(e) {
      let top = e.detail.scrollTop;
      if (this.classifyData && this.classifyData.length > 0) {
        let curTop = this.classifyData[0].offsetTop;
        for (let index = 0; index < this.classifyData.length; index++) {
          if (top > this.classifyData[index].offsetTop - curTop) {
            this.classifyIndex = index;
          }
        }
      }

      // 判断当前点击菜单索引与滚动索引，如果大于或等于则取当前点击菜单索引
      if (
        this.curClickedIndex &&
        this.classifyData.length - this.curClickedIndex <=
          this.classifyData.length - this.classifyIndex
      ) {
        this.classifyIndex = this.curClickedIndex;
        this.curClickedIndex = 0; // 重置当前点击菜单索引（避免点击后，滑动右侧内容左侧菜单不跟随）
      }
    },

    // 轮播图跳转
    toThreePageFun(url) {
      uni.navigateTo({ url: url });
    },

    // 选择左右分类
    choiceClassify(item, index) {
      // 右侧内容滚动
      this.$nextTick(() => {
        this.scrollIntoView = "classifyS" + item.id;
      });

      const query = uni.createSelectorQuery().in(this);
      query
        .select("#cfWrap")
        .boundingClientRect((data) => {
          let curViewH = data.height; // 获取滚动可视区域高

          let H = 0;
          let vIndex = 0;
          for (let i = this.classifyData.length - 1; i >= 0; i--) {
            H += this.classifyData[i].height;
            // 判断右侧内容最后几项内容高与滚动可视区域高（高度不够，左侧菜单切换失败）
            if (H >= curViewH) {
              vIndex = i; // 获取高度不足索引值
              break;
            }
          }

          this.classifyIndex = index; // 设置当前点击菜单索引
          this.curClickedIndex = 0; // 重置当前点击菜单索引（避免点击后，滑动右侧内容左侧菜单不跟随）
          // 判断当前点击菜单索引与高度不足索引值
          if (index >= vIndex) {
            // 大于或等于
            this.curClickedIndex = index; // 记录当前点击菜单索引
          }
        })
        .exec();
    },

    // 获取分类
    classifyListFun() {
      classifyList().then((res) => {
        if (res.success) {
          if (res.data.classify && res.data.classify.length > 0) {
            let dataList = [];
            res.data.classify.forEach((item) => {
              // 设置默认值
              if (!item.subClassifies) {
                item.subClassifies = [];
              }
              if (!item.userClassifyBannerDTOS) {
                item.userClassifyBannerDTOS = [];
              }
              dataList.push(item);
            });
            this.classifyData = [...dataList, ...this.classifyData];
          }

          if (
            res.data.recommend &&
            res.data.recommend.classifies &&
            res.data.recommend.classifies.length > 0
          ) {
            let oBJg = [
              {
                id: 0,
                appletName: "推荐分类",
                name: "推荐分类",
                subClassifies: res.data.recommend.classifies
                  ? res.data.recommend.classifies
                  : [],
                userClassifyBannerDTOS: res.data.recommend.classifyBanners
                  ? res.data.recommend.classifyBanners
                  : [],
              },
            ];
            this.classifyData = [...oBJg, ...this.classifyData];
          }

          let bannerHeight = 0;
          setTimeout(() => {
            this.classifyData.forEach((item, index) => {
              const query = uni.createSelectorQuery().in(this);
              query
                .select("#bannerss")
                .boundingClientRect((data) => {
                  bannerHeight = data.height;
                })
                .exec();
              query
                .select("#classifyS" + item.id)
                .boundingClientRect((data) => {
                  this.$set(item, "offsetTop", data.top);
                  this.$set(
                    item,
                    "height",
                    index != 0 ? data.height + bannerHeight : data.height
                  );
                })
                .exec();
            });
          }, 1000);

          this.classifyId = this.classifyData[0].id;
        }
      });
    },

    // 进入二级分类列表
    toTwoPage(item) {
      uni.navigateTo({
        url: "classifyList?title=" + item.name + "&id=" + item.id,
      });
    },
  },
};
</script>

<style lang="scss">

//
.classify-swiperView wx-swiper .wx-swiper-dot {
  background: none !important;
  border: 2rpx solid #0076A5 !important;
}
.classify-swiperView wx-swiper .wx-swiper-dot-active {
  background: #0076A5 !important;
}
.classify {
  font-size: 26rpx;
  .classify-top {
    height: 124rpx;
    border-top: 1rpx solid #f3f4f8;
    .search-view {
      display: flex;
      background: #fff;
      padding: 22rpx 30rpx;
      position: relative;
      image {
        width: 44rpx;
        height: 44rpx;
        position: absolute;
        left: 55rpx;
        top: 40rpx;
      }
      input {
        background: #f3f4f8;
        width: 100%;
        height: 80rpx;
        background: #f3f4f8;
        border-radius: 40rpx;
        font-size: 28rpx;
        padding-left: 78rpx;
      }
    }
  }
  .cf-module {
    display: flex;
    position: fixed;
    width: 100%;
    top: 124rpx;
    bottom: 0;
    // #ifdef H5
    top: calc(124rpx + 44px);
    bottom: var(--window-bottom);
    // #endif
    left: 0;
    z-index: 999;
  }
  .cf-lf {
    width: 232rpx;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
    background: #f3f4f8;
    &::-webkit-scrollbar {
      display: none;
    }
    .cf-lfList {
      background: #fff;
      .cf-classifyView {
        position: relative;
        display: flex;
        align-items: center;
        width: 232rpx;
        padding: 30rpx 0 30rpx 0;
        background: #f3f4f8;
        text:nth-child(1) {
          position: absolute;
          top: 50%;
          left: -6rpx;
          font-size: 34rpx;
          transform: translateY(-50%);
        }
        text:nth-child(2) {
          font-size: 30rpx;
          font-weight: 400;
          color: #333333;
          width: 100%;
          padding: 0 24rpx;
          text-align: center;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
        }
      }

      .cf-lfHover {
        background: #fff;
        font-weight: 500;
      }
    }
  }
  // rg
  .cf-rg {
    width: calc(100% - 232rpx);
    background: #fff;
    // overflow-y: auto;
    .cf-rg-scroll {
      height: 100%;
    }
    .classify-swiperView {
      // padding:30rpx;
      padding: 0 30rpx 30rpx 30rpx;
      .classify-swiper {
        width: 462rpx;
        height: 180rpx;

        .classify-swiperImg {
          width: 462rpx;
          height: 180rpx;
          border-radius: 16rpx;
        }
      }
    }
    .cf-rg-line {
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #333333;
      margin-top: 30rpx;
      .cf-rg-lineName {
        font-size: 32rpx;
        font-weight: 600;
        margin-left: 30rpx;
      }
      .cf-rg-lineGoofList {
        float: left;
        width: 134rpx;
        margin-left: 30rpx;
        margin-top: 30rpx;
        image {
          width: 134rpx;
          height: 134rpx;
          border-radius: 8rpx;
          background: #f3f4f8;
        }
        view {
          text-align: center;
          width: 134rpx;
          font-size: 24rpx;
          margin-top: 18rpx;
          height: 50rpx;
        }
      }
    }
    .cf-rg-pullTip {
      font-size: 20rpx;
      font-family: PingFangSC-Regular, PingFang SC;
      color: #5f5f5f;
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 50rpx 0 30rpx;
      image {
        width: 28rpx;
        height: 28rpx;
        margin-left: 5rpx;
      }
    }
  }
}
.classifyPopup-btm {
  bottom: 100rpx !important;
}
</style>
