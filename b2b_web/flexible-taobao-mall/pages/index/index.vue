<template>
  <div class="index-wrap">
    <image
      src="../../static/common/image/loading.gif"
      mode="aspectFill"
    ></image>
  </div>
</template>
/**
* 入口文件，主要是设置分销商ID 平台ID 并进行调转
*/
<script type="text/ecmascript-6">
import {
  getTenantInfo as _getTenantInfo,
  getTConfig as _getTConfig,
  listSeller as _listSeller,
} from "../api/allApi";

import store from "../../store";
export default {
  name: "index",
  data() {
    return {};
  },
  created() {
    //淘宝小程序真实入口文件
    this.init();
  },
  methods: {
    init() {
      //设置分销商ID
      let _self = this;

      //获取 系统信息
      uni.getSystemInfo({
        success: (res) => {
          //Mutation commit 存储系统信息到vuex 并持久化
          _self.$store.commit("SET_SYSTEMINFO", res);

          let onLaunchParams = store.state.taobao.onLaunchParams;

          let tenantNo = 101;
          // if (onLaunchParams.sellerNick === "bat旗舰店") {
          //   tenantNo = 101;
          // }
          _getTenantInfo({
            gainUrlType: 6, // 需获取的主机类型（默认6）：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端 9兑换商城H5端
            tenantNo: tenantNo,
          }).then((res) => {
            if (res.success) {
              _self.$store.commit("SET_TENANTNO", res.data.tenantNo);
              _self.$store.commit("SET_TENANTURL", res.data.url);
							
              //distributorId 接口审核前 备用参数
              _getTConfig({
                sellerNick: onLaunchParams.sellerNick,
                distributorId: onLaunchParams.sellerNick,
              }).then((res) => {
                if (res.success) {
                  _self.$store.commit(
                    "SET_DISTRIBUTOR_ID",
                    res.data.distributorId
                  );
                  _self.$store.commit("SET_PLATFORM", res.data.ePlatfrom);
                  _self.$store.commit(
                    "SET_ORDERSOURCE",
                    res.data.orderSourceId
                  );
                } else {
                  _self.$store.commit("SET_DISTRIBUTOR_ID", 2775);
                  _self.$store.commit("SET_PLATFORM", 42);
                  _self.$store.commit("SET_ORDERSOURCE", 42);
                }

                if (onLaunchParams && onLaunchParams.tradeToken) {
                  //distributorId 接口审核前 备用参数
                  //seriesNum 备用参数替换为 真实意义参数 skuId
                  _listSeller({
                    distributorId: res.data.distributorId,
                    skuId: onLaunchParams.itemId,
                    sellerNick: res.data.sellerNick,
                  }).then((res) => {
                    let data = res.data.body;
                    if (typeof data === "string") {
                      data = JSON.parse(data);
                    }
                    if (data.item_seller_get_response) {
                      let sku = data.item_seller_get_response.item.skus.sku;
                      let _sku = sku.filter((val) => {
                        return val.sku_id == onLaunchParams.skuId;
                      })[0];
                      store.state.outerId = _sku.outer_id;
                    }
                    uni.redirectTo({
                      url: "/pages/custom/custom",
                    });
                  });
                } else {
                  uni.redirectTo({
                    url: "/pages/custom/custom",
                  });
                }
              });
            } else {
              if (res.errCode === "B_PLATFORM_QRY_URL_ERROR") {
                uni.showModal({
                  title: "温馨提示",
                  content: "网址解析异常，请输入正确的网址访问",
                  showConfirm: "好的",
                  showCancel: false,
                });
              } else if (res.errCode === "B_PLATFORM_WX_PROGRAM_APP_ID_NULL") {
                uni.showModal({
                  title: "温馨提示",
                  content: "服务器连接异常，请输入正确的网址重新访问",
                  confirmText: "好的",
                  showCancel: false,
                });
              } else if (res.errCode === "B_PLATFORM_GAIN_URL_NULL") {
                uni.showModal({
                  title: "温馨提示",
                  content: "获取服务器地址失败，请联系客户反馈",
                  confirmText: "好的",
                  showCancel: false,
                });
              } else {
                uni.showToast({
                  title: res.errMessage,
                  icon: "none",
                });
              }
            }
          });
        },
        fail: () => {
          _self.$toast.fail("获取系统信息失败");
        },
      });
    },
  },
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
.index-wrap {
  image {
    position: fixed;
    top: 50%;
    left: 50%;
    width: 80px;
    height: 80px;
    transform: translate(-50%, -50%);
  }
}
</style>
