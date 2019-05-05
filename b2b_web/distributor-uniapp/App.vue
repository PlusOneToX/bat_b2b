
<script>
import { mapMutations } from "vuex";

import { commonConfig } from "common/api.js";
export default {
  onLaunch: function () {
    let baseUrl = "";
    let params = {};
    // #ifdef MP-WEIXIN
    const accountInfo = uni.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    console.log("小程序appid：", appId);
    // wx5dwx5g8r4g6ew2fd  正式
	// wx5wf1fw26twer65fg 测试
    uni.setStorageSync("appId", appId);
	if(appId == 'wx5dwx5g8r4g6ew2fd'){
		// 正式
		baseUrl = "https://api.bat.com/"; 
	}else{
		// 测试
		baseUrl = "https://test.bat.com/"; 
	}

    params = {
      gainUrlType: 6,
      wxProgramAppId: appId,
    };
    // #endif

    // #ifdef H5
    let host = window.location.host.split(":")[0];
    // 测试
    //host = "test.bat.com";
    //baseUrl = "https://test.bat.com/";
	
	// 正式
	host = "www.bat.com";
	baseUrl = "https://api.bat.com/";

    params = {
      gainUrlType: 6,
      host: host,
      qryUrlType: 3,
    };
    // #endif

    uni
      .request({
        method: "get",
        url: baseUrl + "platform/v1/web/tenant/url",
        data: params,
        dataType: "json",
      })
      .then((res) => {
		console.log(res[1]);
        uni.removeStorageSync("tenantId");
        uni.removeStorageSync("tenantNo");
        uni.removeStorageSync("tenantHost");
        if (res[1].data.success) {
          let data = res[1].data.data;
          uni.setStorageSync("tenantId", data.tenantId);
          uni.setStorageSync("tenantNo", data.tenantNo);
          uni.setStorageSync("tenantHost", data.host);
          // 获取平台配置
          commonConfig({ id: data.tenantId }).then((res) => {
            if (res.success) {
			  if(res.data != undefined){
				 uni.setStorageSync("themeColor", res.data.colour);
			  }
            }
          });
        }
      });
  },
  onShow: function () {},
  onHide: function () {},
  globalData: {
    test: "",
  },
  methods: {
    ...mapMutations(["setUniverifyErrorMsg", "setUniverifyLogin"]),
  },
};
</script>

<style lang="scss">






/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
@import "./common/uni.css";
@import url("static/iconfont/iconfont.css");
// @import url('static/iconfont/iconfont_base2.css');

/* H5 兼容 pc 所需 */
/* #ifdef H5 */
@media screen and (min-width: 768px) {
  body {
    overflow-x: hidden;
  }
}
/* #endif*/
body::-webkit-scrollbar {
  display: none !important;
}

// 加载中
.B2B-loading {
  top: 0;
  left: 0;
  position: fixed;
  width: 100%;
  height: 100vh;
  z-index: 99999;
  background: rgba($color: #000000, $alpha: 0.3);
  .B2B-loading-content {
    position: absolute;
    width: 282rpx;
    height: 282rpx;
    background: #4c4a4d;
    border-radius: 16rpx;
    left: 50%;
    margin-left: -141rpx;
    margin-top: -141rpx;
    top: 50%;
    image {
      width: 118rpx;
      height: 118rpx;
      margin-top: 52rpx;
      margin-left: 82rpx;
      -webkit-animation: haha1 1.8s linear infinite;
      animation: haha1 1.8s linear infinite;
    }
    @keyframes haha1 {
      from {
        transform: rotate(0);
      }
      to {
        transform: rotate(360deg);
      }
    }
    view {
      color: #fff;
      font-size: 30rpx;
      font-family: PingFangSC-Regular, PingFang SC;
      text-align: center;
      margin-top: 28rpx;
    }
  }
}

// 搜索框样式
.classify-top {
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
      width: 690rpx;
      height: 80rpx;
      background: #f3f4f8;
      border-radius: 40rpx;
      font-size: 28rpx;
      padding-left: 78rpx;
    }
  }
}

/* liu--3/29 */
.tipText {
  position: fixed;
  z-index: 9999;
  left: 50%;
  top: 50%;
  background-color: rgba(17, 17, 17, 0.6);
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 13px;
  color: #fff;
  line-height: 18px;
  max-width: 60%;
  text-align: center;
  transform: translate(-50%, -50%);
}
uni-toast {
  z-index: 99999 !important;
}
// 标签
.label-new {
  position: absolute;
  z-index: 99;
  background: #ed5307;
  color: #fff;
  font-size: 20rpx;
  padding: 5rpx 5rpx;
  border-top-left-radius: 10rpx;
  border-bottom-right-radius: 10rpx;
  top: 0;
}
.loading-more {
  margin-top: 100rpx;
}

// 顶部head
uni-page-head {
  // #ifdef H5
  .uni-page-head {
    background-color: #fff !important;
  }
  // #endif
}

// 底部tabbar
uni-tabbar {
  z-index: 1001 !important;

  // #ifdef H5
  position: fixed;
  width: 100%;
  // var(--window-bottom);
  bottom: 0;
  left: 0;
  z-index: 998!important;
  .uni-tabbar__icon {
    margin-top: 0 !important;
    width: 58rpx !important;
    height: 58rpx !important;
  }
  .uni-tabbar__label {
    margin-top: 0 !important;
    font-size: 20rpx !important;
    line-height: 28rpx !important;
  }
  // #endif
}

.font-colorHoverL {
  color: $base-color1 !important;
}

.cartIconColor {
  font-size: 52rpx !important;
  color: $base-color1;
}
.moreIcon {
  font-size: 21rpx;
}
page {
  background-color: rgba(242, 243, 248, 0.35) !important;
  min-height: 100% !important;
  height: auto !important;
}
uni-page-body {
  background-color: rgba(242, 243, 248, 0.35) !important;
  min-height: 100% !important;
  height: auto !important;
}
.jktLogo {
  width: 176rpx;
  height: 30rpx;
}

// 距离顶部的距离
.mgTop {
  padding:  calc(146rpx + var(--status-bar-height)) 30rpx 30rpx 30rpx;
  /* #ifdef H5 */
  padding: 134rpx 30rpx 30rpx 30rpx;
  /* #endif*/
}
.no-more {
  color: #999999;
  font-size: 20rpx;
  text-align: center;
  padding-top: 56rpx;
  padding-bottom: 30rpx;
}
.status_bar {
  height: var(--status-bar-height);
  width: 100%;
  background: #fff;
}
.locationIcon {
  width: 49rpx;
  height: 49rpx;
}
.toIcon {
  width: 12rpx;
  height: 22rpx;
}
.toImg {
  width: 12rpx;
  height: 22rpx;
  background: url(static/img/auditTo_icon.png);
  background-size: 12rpx 22rpx;
}
.downIcon {
  width: 22rpx;
  height: 11rpx;
}
.downIconImg {
  width: 22rpx;
  height: 11rpx;
  background: url(static/img/down_icon.png);
  background-size: 22rpx 11rpx;
}
.upIcon {
  width: 21rpx;
  height: 12rpx;
}
.upIconImg {
  width: 21rpx;
  height: 12rpx;
  background: url(static/img/up_icon.png);
  background-size: 21rpx 12rpx;
}
.success_iconL {
  width: 99rpx;
  height: 99rpx;
}
.iconLogo {
  width: 40rpx;
  height: 36rpx;
}

.top-moudle {
  background: #fff;
  border-bottom: 1px solid $opacity-border;
  position: fixed;
  left: 0;
  width: 100%;
  z-index: 9999;
  padding-top: 36rpx;
  // #ifdef H5
  padding-top: 10rpx;
  height: 104rpx;
  // #endif
  box-sizing: border-box;

  .top-title {
    position: relative;
    padding: 0 30rpx;
    background: #fff;
    image {
      position: absolute;
      top: 50%;
      left: 30rpx;
      width: 55rpx;
      height: 55rpx; 
      transform: translateY(-50%);
    }
    view {
      font-size: 32rpx;
      font-weight: 400;
      width: 100%;
      height: 80rpx;
      line-height: 80rpx;
      text-align: center;
    }
    /* #ifdef H5 */
    view {
      height: 88rpx;
      line-height: 88rpx;
    }
    /*#endif*/
  }
  .top-NoBackTitle {
    font-size: 32rpx;
    font-weight: 400;
    height: 85rpx;
    line-height: 85rpx;
    text-align: center;
  }
}
.myCollect-popup-title {
  display: flex;
  justify-content: space-between;
  padding: 30rpx;
  text {
    width: 650rpx;
    text-align: center;
    font-size: 28rpx;
  }
  .popup-colose {
    width: 38rpx;
    height: 39rpx;
    // margin-top: 30rpx;
    // margin-left: 680rpx;
  }
}
.uni-numbox {
  height: 50rpx !important;
  width: 210rpx !important;
}
.uni-numbox__minus,
.uni-numbox__value,
.uni-numbox__plus {
  width: 50rpx !important;
  height: 50rpx !important;
  margin-left: 10rpx;
  background: #fafafc !important;
  border: none !important;
  border-radius: 10rpx;
}
.uni-numbox--text {
  color: #999999 !important;
}
.uni-numbox__value {
  background: #f2f3f8 !important;
  font-size: 26rpx !important;
  width: 80rpx !important;
}
.uni-picker-toggle,
.uni-picker-header {
  border-top-left-radius: 15rpx !important;
  border-top-right-radius: 15rpx !important;
}

// 带三角的提示框
.Message-boxUp {
  font-size: 20rpx !important;
  margin-left: 10rpx;
}
.self-Message-box {
  position: absolute;
  right: 0rpx;
  top: 60rpx;
  z-index: 998;
  .Message-box-content {
    transition: opacity 0.3s, visibility 0.3s;
    box-shadow: 0 0 15rpx 2rpx rgba(0, 0, 0, 0.05);
    border-radius: 5px;
    display: block;
    width: 200rpx;
    max-height: 200rpx;
    background-color: #fff;
    overflow: auto;
    .Message-box-list {
      margin-top: 10rpx;
      cursor: pointer;
      text {
        display: block;
        text-align: center;
        border-bottom: 1rpx solid $opacity-border;
        padding: 12rpx 0;
      }
    }
  }
  .Message-box-sanjao {
    position: absolute;
    width: 0px;
    height: 0px;
    border-style: solid;
    left: 130rpx;
    top: -12rpx;
    border-width: 0px 6px 6px;
    border-color: transparent transparent #fff;
    z-index: 999;
  }
}
.myCollect-scroll-Y {
  height: 800rpx;
}
.myCollect-popup {
  font-size: 22rpx;
  // ifdef H5
  z-index: 999 !important;
  // endif

  .uni-popup {
    z-index: 1000 !important;
  }

  .myCollect-popup-content {
    background: #fff;

    height: 75vh;

    border-radius: 30rpx 30rpx 0px 0px;
    .myCollect-popup-inform {
      display: flex;
      padding: 0 30rpx;
      image {
        width: 120rpx;
        height: 120rpx;
      }
      view {
        margin-left: 30rpx;
        text {
          display: block;
          margin-bottom: 8rpx;
        }
      }
    }
    .myCollect-popup-label {
      color: #999;
      padding: 0 30rpx;
      margin-top: -20rpx;
      .myCollect-fuwufei {
        display: flex;
        justify-content: flex-end;
      }
      .myCollect-label-btm {
        margin-top: 40rpx;
        display: flex;
        align-items: center;
        justify-content: space-between;
        position: relative;
        .myCollect-zaitu {
          display: flex;
          align-items: center;
          .iconfont {
            position: relative;
            top: 3rpx;
            font-size: 26rpx;
            margin-right: 8rpx;
          }
          image {
            width: 30rpx;
            height: 30rpx;
            margin-right: 10rpx;
          }
          icon {
            font-size: 30rpx;
            margin-right: 10rpx;
          }
          .tuTypeHover {
            color: $base-color1 !important;
          }
        }
      }
    }
    .myCollect-categoryList {
      padding: 30rpx;
      margin-top: 20rpx;
      .myCollect-categoryList-line {
        position: relative;
        padding: 30rpx;
        box-shadow: 0rpx 0rpx 20rpx 0px rgba(0, 0, 0, 0.05);
        border-radius: 10rpx;
        display: flex;
        align-items: center;
        margin-bottom: 20rpx;
        .myCollect-categoryLine-Lf {
          width: 260rpx;
          text {
            display: block;
            margin-bottom: 10rpx;
          }
        }
        .myCollect-categoryLine-center {
          width: 120rpx;
          color: #ed5407;
          font-size: 28rpx;
          text {
            font-size: 24rpx;
          }
        }
        .myCollect-categoryLine-RG {
          width: 250rpx;
          text-align: right;
          .uni-numbox {
            margin-left: 40rpx;
            height: 50rpx;
            line-height: 50rpx;
          }
          .categoryLine-RG-kucun {
            color: #999;
            margin-top: 10rpx;
          }
        }
      }
      // .categoryList-Hover{
      // 	box-shadow: 0px 0px 20px 0px rgba(9, 213, 238, 0.05);
      // 	border: 1px solid #09D5EE;
      // 	border-left: 8rpx solid  #09D5EE;
      // }
    }
    .myCollect-popup-btm {
      position: fixed;
      bottom: 0;
      width: 100%;
      background: #fff;
      padding-bottom: 25rpx;
      z-index: 1000;
      .myCollect-popup-total {
        color: #999;
        text-align: right;
        padding: 0 30rpx;
        text {
          color: $base-color2;
        }
      }
      .myCollect-popup-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-top: 15rpx;
        text {
          display: block;
          height: 80rpx;
          line-height: 80rpx;
          text-align: center;
          border-radius: 40rpx;
          width: 330rpx;
          font-size: 28rpx;
        }
        .add-shoppingCart {
          color: $base-color1;
          border: 2rpx solid $base-color1;
        }
        .popup-buy {
          background: $base-color1;
          color: #fff;
          margin-left: 30rpx;
        }
        .popup-tijiao {
          width: 690rpx;
          background: $base-color1;
          color: #fff;
        }
      }
    }
  }
}
.myCollect-activityTip {
  position: absolute;
  top: 30rpx;
  font-size: 20rpx;
  color: #fff;
  width: 58rpx;
  height: 28rpx;
  line-height: 28rpx;
  text-align: center;
  background: url(static/img/activityBg.png);
  background-size: 58rpx 28rpx;
}
.popup-activityTip {
  top: 0;
  left: 0;
}
.sc-swipe-delete {
  background: #ed5307;
  color: #fff;
  font-size: 26rpx;
  width: 86rpx;
  align-items: center;
  display: -webkit-flex;
  text {
    display: block;
    width: 26rpx;
    margin-left: 30rpx;
  }
}

// 筛选
.qo-screen-module {
  background: #fff;
  padding-top: calc(116rpx + var(--status-bar-height));
  // #ifdef H5
  padding-top: 104rpx;
  // #endif
  display: flex;
  align-items: center;
  justify-content: space-around;
  .qo-screen-line {
    display: flex;
    font-size: 32rpx;
    align-items: center;
    line-height: 50rpx;
    padding: 20rpx 10rpx;
    .sortIcon {
      width: 32rpx;
      height: 32rpx;
      background: url(static/imgs/icon_sorting.png) no-repeat center center;
      background-size: 100% 100%;
      &.sort_down {
        background: url(static/imgs/icon_sorting_down.png) no-repeat center
          center;
        background-size: 100% 100%;
      }
      &.sort_up {
        background: url(static/imgs/icon_sorting_up.png) no-repeat center center;
        background-size: 100% 100%;
      }
    }
    .rowIcon {
      font-size: 34rpx;
      color: #999;
    }
    .fontColor {
      font-size: 36rpx;
      color: $uni-text-color;
      font-weight: 500;
    }
    .row-line-icon {
      position: relative;
      width: 40rpx;
      height: 40rpx;
      &::before {
        content: "";
        position: absolute;
        top: 50%;
        left: -34rpx;
        width: 5rpx;
        height: 32rpx;
        background-color: #f2f3f8;
        transform: translateY(-50%);
      }
      &.icon-line-list {
        background: url(static/imgs/c_line_icon.png) no-repeat center center;
        background-size: 100% 100%;
      }
      &.icon-row-list {
        background: url(static/imgs/c_row_icon.png) no-repeat center center;
        background-size: 100% 100%;
      }
    }
    .qo-screenBorder {
      width: 50rpx;
      height: 40rpx;
      border-left: 5rpx solid #f2f3f8;
    }
    .screenIcon {
      font-size: 32rpx;
      color: #333;
    }
  }
}

// 标签组
.label-module {
  display: flex;
  align-items: center;
  margin-top: 15rpx;
  text {
    height: 32rpx;
    line-height: 32rpx;
    font-size: 20rpx;
    border-radius: 5rpx;
    text-align: center;
    display: block;
    padding: 0 10rpx;
    margin-right: 20rpx;
  }
  .orangeLabel {
    color: #f29c2b;
    border: 1rpx solid rgba(242, 156, 43, 0.3);
  }
  .redLabel {
    color: #ff585d;
    border: 1rpx solid #ff585d;
  }
  .greenLabel {
    color: #8bc14c;
    border: 1rpx solid #8bc14c;
  }
}

.nodata-style {
  text-align: center;
  font-size: 28rpx;
  color: #999;
  margin-top: 150rpx;
  image {
    width: 268rpx;
    height: 258rpx;
  }
  view {
    margin-top: 30rpx;
  }
}

// 隐藏滚动条
.uni-scroll-view {
  -webkit-overflow-scrolling: touch;
  &::-webkit-scrollbar {
    display: none;
  }
}

// picker 主题色
.uni-picker-container .uni-picker-action.uni-picker-action-confirm {
  color: $base-color1 !important;
}

// modal
uni-modal {
  .uni-modal__bd {
    font-size: 32rpx;
    color: #333 !important;
  }
  .uni-modal__btn_primary {
    font-size: 32rpx;
    color: $base-color1 !important;
  }
  .uni-modal__btn_default {
    font-size: 32rpx;
    color: #999 !important;
  }
}
</style>
