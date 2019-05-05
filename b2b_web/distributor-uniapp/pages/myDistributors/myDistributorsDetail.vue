<template>
  <view class="myDistributors-details">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>分销商详情</view>
      </view>
    </view>

    <view class="dsb-details-box">
      <view class="dsb-details-line">
        <view>
          <text>商户名称</text><text>{{ info.companyName }}</text>
        </view>
        <view v-if="info.applyStatus == 2">
          <text>审核时间</text>
          <text>{{ info.applyTime }}</text>
        </view>
      </view>
      <view class="dsb-details-line">
        <view>
          <text>商户联系人</text><text>{{ info.userName }}</text>
        </view>
        <view>
          <text>商户手机号</text><text>{{ info.phone }}</text>
        </view>
        <view>
          <text>登录账号</text><text>{{ info.name }}</text>
        </view>
      </view>
      <view class="dsb-details-line">
        <view>
          <text>账号状态</text>
          <text v-if="info.applyStatus == 0">未提交</text>
          <text v-if="info.applyStatus == 1">申请中</text>
          <text v-if="info.applyStatus == 2">申请通过</text>
          <text v-if="info.applyStatus == 3">申请失败</text>
        </view>
      </view>
	  
	  <view class="dsb-details-line">
	    <view  @click="setFreezeStatus(info)">
	      <text>冻结状态</text>
	      <text v-if="info.freezeStatus == 1">未冻结（设置）</text>
	      <text v-if="info.freezeStatus == 2">已冻结（设置）</text>
	    </view>
	  </view>

      <view class="dsb-details-line2" v-if="info.applyStatus == 2">
        <picker
          @change="bindPickerChange($event, levelList)"
          :value="index"
          :range="levelList"
          :range-key="'name'"
        >
          <view class="dsb-details-toLine">
            <text>价格等级</text>
            <view>
              <text>{{ info.scalePriceName }}</text>
              <text class="iconfont icon-more"></text>
            </view>
          </view>
        </picker>
        <view class="dsb-details-toLine" @click="toPage('brandVisual')">
          <text>品牌可视范围</text>
          <view>
            <text>设置</text>
            <text class="iconfont icon-more"></text>
          </view>
        </view>
        <view class="dsb-details-toLine" @click="toPage('commodityVisual')">
          <text>商品可视范围（品牌外的商品）</text>
          <view>
            <text>设置</text>
            <text class="iconfont icon-more"></text>
          </view>
        </view>
      </view>
      <!-- 待审核才展示按钮 -->
      <view class="dsb-details-btn" v-if="info.applyStatus == 1">
        <text @click="checkFun(3)">审核拒绝</text>
        <text @click="checkFun(2)">审核通过</text>
      </view>
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  distributorNextDetails,
  distributorNextCheck,
  scalepriceList,
  adjustScaleprice,
  twoDistributorFreeze
} from "../../common/api.js";
export default {
  data() {
    return {
      id: "",
      info: {},
      index: 0,
      levelList: [
        { id: 0, name: "默认等级价" },
        // {id:1,name:'一级等级价'},
      ], //等级价格
      scalePriceId: "", //价格id
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.id = option.id;
    this.getDetails(option.id);
    this.scalepriceListFun();
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
    // 获取详情
    getDetails(id) {
      let that = this;
      distributorNextDetails({ id: id }).then((res) => {
		console.log("查看下级分销商详情");
		console.log(res);
        if (res.success) {
          that.info = res.data;
        }
      });
    },

    // 审核
    checkFun(status) {
      let params = {
        applyStatus: status,
        id: this.id,
        scalePriceId: this.info.scalePriceId,
      };
      let that = this;
      let title = "";
      if (status == 2) {
        title = "审核通过";
      } else {
        title = "审核拒绝成功！";
      }
      distributorNextCheck(params).then((res) => {
        if (res.success) {
          that.tipFun(title);
          that.getDetails(that.id);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 获取等级价格
    scalepriceListFun() {
      let that = this;
      let id = uni.getStorageSync("userId");
      scalepriceList({ distributorId: id }).then((res) => {
        if (res.success) {
          that.levelList = [...that.levelList, ...res.data];
        }
      });
    },

    // 调整价格价格
    bindPickerChange: function (e, storage) {
      let that = this;
      this.info.scalePriceName = storage[e.target.value].name;
      let parames = {
        applyStatus: 2,
        id: this.info.id,
        scalePriceId: storage[e.target.value].id,
      };
      if (storage[e.target.value].name == "") {
        that.tipFun("请选择价格等级");
        return;
      }
      adjustScaleprice(parames).then((res) => {
        if (res.success) {
          that.tipFun("价格等级调整成功！");
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 调整品牌可视和商品可视
    toPage(page) {
      uni.navigateTo({
        url: page + "?id=" + this.info.id,
      });
    },
	
	// 二级分销冻结
	setFreezeStatus(info) {
	  let that = this;
	  console.log("二级分销冻结");
	  let freezeStatus = info.freezeStatus;
	  if(freezeStatus == 1){
		freezeStatus = 2
	  }else{
		freezeStatus = 1
	  }
	  twoDistributorFreeze({ id: info.id,freezeStatus: freezeStatus}).then((res) => {
		console.log(res);
	    if (res.success) {
	       that.tipFun("分销商冻结状态调整成功！");
		   that.getDetails(info.id);
	    }
	  });
	},
  },
};
</script>

<style lang="scss">
.myDistributors-details {
  font-size: 28rpx;
  .dsb-details-box {
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
  }
  .dsb-details-line {
    background: #fff;
    margin-top: 15rpx;
    view {
      padding: 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      display: flex;
      align-items: center;
      justify-content: space-between;
      text:nth-child(2) {
        color: #999;
      }
    }
  }
  .dsb-details-line2 {
    background: #fff;
    margin-top: 15rpx;
    .dsb-details-toLine {
      padding: 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      display: flex;
      align-items: center;
      justify-content: space-between;
      view {
        display: flex;
        align-items: center;
        font-size: 26rpx;
        color: #999;
        icon {
          margin-left: 10rpx;
          font-size: 20rpx;
        }
      }
    }
  }
  .dsb-details-btn {
    font-size: 32rpx;
    display: flex;
    align-items: center;
    justify-content: center;

    position: fixed;
    bottom: 30rpx;
    width: 100%;
    text {
      display: block;
      width: 330rpx;
      height: 100rpx;
      line-height: 100rpx;
      border-radius: 50rpx;
      text-align: center;
    }
    text:nth-child(1) {
      background: #d7d7d7;
      color: #666666;
    }
    text:nth-child(2) {
      background: $bg-gradient-btn;
      margin-left: 30rpx;
      color: #fff;
    }
  }
}
</style>
