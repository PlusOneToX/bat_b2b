<template>
  <view class="myDistributors">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>我的分销商</view>
      </view>
    </view>

    <view class="myDistributors-tab">
      <view
        v-for="item in statusList"
        :key="item.id"
        :class="tabIndex == item.id ? 'hoverTab' : ''"
        @click="tabClick(item)"
      >
        {{ item.name }}
      </view>
    </view>

    <!--  list-->
    <scroll-view
      scroll-y="true"
      class="myDistributors-scrollList"
      @scrolltolower="pageScrollClick"
    >
      <view class="myDistributors-list" v-if="listData.length > 0">
        <view class="myDistributors-listLine">
          <!-- <view class="listLine-time">注册时间：2015-23-32</view> -->
          <view
            class="listLine-list"
            v-for="item in listData"
            :key="item.id"
            @click="toDetailClick(item.id)"
          >
            <image
              src="../../static/img/heahericon.png"
              class="headerImg"
            ></image>
            <view class="listLine-center">
              <view>{{ item.name }}</view>
              <view>{{ item.phone }}</view>
            </view>
            <view class="listLine-rg">
              <image
                src="../../static/img/toAudit.png"
                class="toAudit"
                v-if="item.applyStatus == 1"
              ></image>
              <view class="rg-priceTip" v-if="item.applyStatus == 2">{{
                item.scalePriceName
              }}</view>
              <image
                src="../../static/img/refuseStatus.png"
                class="toAudit"
                v-if="item.applyStatus == 3"
              ></image>
              <image
                src="../../static/img/auditTo_icon.png"
                class="auditTo"
              ></image>
            </view>
          </view>
          <!-- <view class="listLine-list" @click="toDetailClick">
					   <image src="../../static/img/heahericon.png" class="headerImg"></image>
					   <view class="listLine-center">
						   <view>商户名称</view>
						   <view>135****8000</view>
					   </view>
					   <view class="listLine-rg">
						   <view class="rg-priceTip" >一级价</view>
						   <image src="../../static/img/auditTo_icon.png" class="auditTo"></image>
					   </view>
				   </view> -->
        </view>
      </view>
      <view class="no-more" v-if="page == total">没有更多啦~</view>
    </scroll-view>

    <view class="myDistributors-no-dataList" v-if="listData.length == 0">
      <image src="../../static/img/no-dataIcon.png"></image>
      <view>暂无数据</view>
    </view>

    <view class="brandVisual-save" @click="toPriceLevel"
      ><text>价格等级设置</text></view
    >
  </view>
</template>

<script>
import { distributorNextList } from "../../common/api.js";
export default {
  data() {
    return {
      statusList: [
        { id: 1, name: "待审核" },
        { id: 2, name: "合作中" },
        { id: 3, name: "已拒绝" },
      ],
      tabIndex: 1,
      page: 1,
      total: 0,
      listData: [],
    };
  },
  onLoad() {},
  onShow() {
    this.getDataList(this.tabIndex);
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // tab选择
    tabClick(item) {
      this.tabIndex = item.id;
      this.getDataList(item.id);
    },
    // 跳转详情
    toDetailClick(id) {
      uni.navigateTo({
        url: "myDistributorsDetail?id=" + id,
      });
    },
    // 上拉分页
    pageScrollClick() {
      console.log("分页");
      let status = this.tabIndex;
      if (this.page < this.total) {
        this.page += 1;
        this.getDataList(status);
      }
    },

    // 获取分销商列表
    getDataList(status) {
      let that = this;
      let id = uni.getStorageSync("userId");
      let params = {
        applyStatus: status,
        distributorId: id,
        page: that.page,
        size: 10,
      };
      distributorNextList(params).then((res) => {
        if (res.success) {
          console.log(res.data);
          that.total = res.data.pages;

          that.listData =
            that.page == 1
              ? res.data.list
              : [...that.listData, ...res.data.list];
        }
      });
    },

    // 价格等级设置
    toPriceLevel() {
      uni.navigateTo({
        url: "priceLevel",
      });
    },
  },
};
</script>

<style lang="scss">
.myDistributors {
  font-size: 26rpx;
}
.myDistributors-tab {
  display: flex;
  align-items: center;
  justify-content: space-around;
  background: #fff;
  padding-top: calc(116rpx + var(--status-bar-height));
  // #ifdef H5
  padding-top: 104rpx;
  // #endif
  view {
    padding: 30rpx 20rpx;
    color: #999999;
  }
  .hoverTab {
    color: $base-color1;
    border-bottom: 4rpx solid $base-color1;
  }
}
.myDistributors-scrollList {
  padding-bottom: 150rpx;
  .myDistributors-list {
    .myDistributors-listLine {
      margin-top: 20rpx;
      .listLine-time {
        font-size: 24rpx;
        color: #999;
        padding: 35rpx 30rpx;
      }
      .listLine-list {
        background: #fff;
        display: flex;
        align-items: center;
        padding: 30rpx;
        border-bottom: 1px solid $opacity-border;
        .headerImg {
          width: 100rpx;
          height: 100rpx;
        }
        .listLine-center {
          width: 440rpx;
          margin-left: 20rpx;
          view:nth-child(1) {
            font-size: 28rpx;
          }
          view:nth-child(2) {
            font-size: 24rpx;
            color: #999;
            margin-top: 15rpx;
          }
        }
        .listLine-rg {
          text-align: right;
          display: flex;
          align-items: center;
          justify-content: flex-end;
          .toAudit {
            width: 71rpx;
            height: 62rpx;
          }
          .auditTo {
            width: 12rpx;
            height: 21rpx;
            margin-left: 20rpx;
          }
          .rg-priceTip {
            background: #ffe880;
            border-radius: 10rpx;
            text-align: center;
            width: 170rpx;
            padding: 10rpx 0;
            font-size: 24rpx;
          }
        }
      }
    }
  }
}
.myDistributors-no-dataList {
  text-align: center;
  font-size: 30rpx;
  color: #666;
  margin-top: 150rpx;
  image {
    width: 311rpx;
    height: 300rpx;
  }
  view {
    margin-top: 30rpx;
  }
}
.brandVisual-save {
  position: fixed;
  bottom: 0;
  background: #fff;
  width: 100%;
  padding: 25rpx 0;
  text {
    display: block;
    width: 600rpx;
    text-align: center;
    background: $bg-gradient-btn;
    font-size: 32rpx;
    color: #fff;
    height: 80rpx;
    line-height: 80rpx;
    border-radius: 100rpx;
    margin: 0 auto;
  }
}
</style>
