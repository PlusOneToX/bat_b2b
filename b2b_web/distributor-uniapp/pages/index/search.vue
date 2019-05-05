<template>
  <view class="search">
    <!-- <view class="top-moudle search-top">
			<view class="status_bar"> </view>
				<view class="top-title">
					<image src="../../static/img/back_icon.png" @click="toback"></image>
					<view>搜索</view>
				</view> 
		</view> -->

    <!-- @click="searchClick" -->
    <view class="search-input">
      <input
        confirm-type="search"
        placeholder="请输入搜索内容"
        v-model="seatchContent"
        @confirm="searchClick"
      />
      <image
        src="../../static/imgs/icon_search.png"
        @click="searchClick"
      ></image>
    </view>
    <!-- 暂时不做 -->
    <!-- <view class="search-box">
			<view class="search-box-title">热门搜索</view>
			<view class="search-box-list">
				<view class="search-box-line"><text>硅胶耳机套</text><icon class="deleteIcon"></icon></view>
				<view style="clear: both;"></view>
			</view>
		</view> -->
    <view class="search-box" v-if="historyList.length > 0">
      <view class="search-box-title">
        <text>搜索历史</text>
        <image
          src="../../static/imgs/icon_delete.png"
          @click="clearData"
        ></image>
      </view>
      <view class="search-box-list">
        <view
          class="search-box-line"
          v-for="(item, index) in historyList"
          :key="index"
        >
          <text @click="historyClick(item)">{{ item }}</text>
          <text class="deleteIcon" @click="deleteHistory(index)"></text>
        </view>
        <view style="clear: both"></view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      seatchContent: "",
      historyList: [], //历史搜索
    };
  },
  onShow() {
    this.seatchContent = "";
  },
  onLoad() {},
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    // 搜索
    searchClick() {
      uni.navigateTo({
        url: "searchResult?seatchContent=" + this.seatchContent,
      });
      if (this.seatchContent) {
        this.historyList.push(this.seatchContent);
      }
    },
    // 历史记录点击
    historyClick(value) {
      uni.navigateTo({
        url: "searchResult?seatchContent=" + value,
      });
    },
    // 删除单个历史记录
    deleteHistory(index) {
      this.historyList.splice(index, 1);
    },

    // 清空历史记录
    clearData() {
      this.historyList = [];
    },
  },
};
</script>

<style lang="scss">
.search {
  font-size: 26rpx;
  background: #fff;
  height: 100vh;
}
.search-top {
  border: none;
}
.search-input {
  border-top: 1rpx solid #f3f4f8;
  background: #fff;
  padding-top: 22rpx;
  padding-left: 30rpx;
  position: relative;
  input {
    width: 608rpx;
    height: 80rpx;
    line-height: 80rpx;
    padding-left: 82rpx;
    border-radius: 40rpx;
    font-size: 28rpx;
    background: #f3f4f8;
  }
  image {
    width: 44rpx;
    height: 44rpx;
    position: absolute;
    top: 40rpx;
    left: 58rpx;
    z-index: 999;
  }
}
.deleteIcon {
  width: 28rpx;
  height: 29rpx;
  background: url(../../static/img/delete_icon.png);
  background-size: 28rpx 29rpx;
}
.search-box {
  background: #fff;
  padding: 30rpx;
  margin-top: 20rpx;
  .search-box-title {
    display: flex;
    justify-content: space-between;
    font-size: 32rpx;
    image {
      width: 44rpx;
      height: 44rpx;
    }
  }
  .search-box-list {
    .search-box-line {
      padding: 0 30rpx;
      height: 62rpx;
      line-height: 62rpx;
      background: #f3f4f8;
      border-radius: 36rpx;
      text-align: center;
      position: relative;
      color: #424242;
      margin-right: 30rpx;
      margin-top: 30rpx;
      float: left;
      font-size: 28rpx;
      max-width: 600rpx;
      .deleteIcon {
        position: absolute;
        top: -8rpx;
        right: -8rpx;
      }
    }
  }
}
</style>
