<!--
 * @Author: yaowei
 * @Date: 2019-03-24 16:11:00
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-25 09:53:57
-->
<template>
  <view class="distributorDetails">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view @click="toback">{{ agreementName }}</view>
      </view>
    </view>

    <view class="agreement-text">
      <view v-html="content" v-if="isH5"></view>
      <Parser :html="content" v-if="!isH5"></Parser>
    </view>
  </view>
</template>

<script>
import { agreementSettingId } from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
import { Parser } from "../../components/jyf-Parser";
export default {
  components: {
    Parser,
  },
  data() {
    return {
      agreementName: "",
      content: "",
      isH5: false,
    };
  },
  onLoad(option) {
    this.agreementName = option.name;
    this.agreementSettingIdFun(option.id);
    this.isH5 = isH5;
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    agreementSettingIdFun(id) {
      let that = this;
      agreementSettingId({ id: id }).then((res) => {
        if (res.success) {
          that.content = res.data.content;
        }
      });
    },
  },
};
</script>

<style lang="scss">
.agreement-text {
  font-size: 26rpx;
  color: #666;
  padding: calc(136rpx + var(--status-bar-height)) 30rpx 30rpx 30rpx;
  /* #ifdef H5 */
  padding: 124rpx 30rpx 30rpx 30rpx;
  /* #endif*/
}
</style>
