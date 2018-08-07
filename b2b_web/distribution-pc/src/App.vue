<template>
  <div id="app" v-if="!hasUpdate">
    <router-view v-if="isRouterAlive"></router-view>
  </div>

  <div id="sysUpdate" v-else>
    <div class="sys-wrap">
      <img src="./assets/images/sys-update.gif" alt="系统升级" />
      <h6>系统升级通知</h6>
      <p>
        为提升系统支持服务和业务能力，BAT系统正在进行升级，<br />暂不支持登录和使用，敬请谅解，如有疑问请联系客服。
      </p>
    </div>
  </div>
</template>

<script>
import { loginOut, loginOutEn } from "@/assets/js/common.js";
import axios from "axios";
import { tenantUrl } from "@/apiService/api";
export default {
  name: "app",
  provide() {
    return {
      reload: this.reload,
      hasUpdate: false,
    };
  },
  data() {
    return {
      isRouterAlive: true,
    };
  },
  methods: {
    //获取地址栏方法
    getUrlCode() {
      let url = location.href;
      if (url.split("?")[1]) {
        url = url.split("?")[1];
      } else {
        return;
      }

      /* eslint-disable */
      let theRequest = new Object();

      let str = url;
      let strs = str.split("&");
      for (let i = 0; i < strs.length; i++) {
        theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
      }
      return theRequest;
    },
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
        window.location.reload();
      });
    },
   
  },
  created() {
    // 修改 title，keyword，description（切换为英文版）
    let title = "";
    let keywords = "";
    let description = "";

    if (this.$i18n.locale === "en") {
      document.title = title;
      document
        .querySelector('meta[name="keywords"]')
        .setAttribute("content", keywords);
      document
        .querySelector('meta[name="description"]')
        .setAttribute("content", description);
    }
  
    // 系统升级通知
    let d1 = "2018-05-22 10:00:00"; // 开始时间
    let d2 = "2018-05-23 02:00:00"; // 结束时间
    let startTime = new Date(Date.parse(d1.replace(/-/g, "/")));
    let startTime2 = new Date(Date.parse(d2.replace(/-/g, "/")));
    let now = new Date(); // 当前时间
    if (now >= startTime && now <= startTime2) {
      // 判断是否在有效时间内
      this.hasUpdate = true;
    } else {
      this.hasUpdate = false;
    }

    let host = window.location.host.split(":")[0];
	console.log("process.env.NODE_ENV = " + process.env.NODE_ENV);
	console.log("输入host = " + host);  
    if (process.env.NODE_ENV == "development") {
      host = "www.bat.com";
    }
    let tenantNo = this.getUrlCode() ? this.getUrlCode().tenantNo : "";
    let params = {
      gainUrlType: 6,
      host: host,
      qryUrlType: 2,
    };
    if (tenantNo && tenantNo != "") {
      params = { ...params, ...{ tenantNo: this.getUrlCode().tenantNo } };
    }
	
	let hostUrl = "https://api.bat.com/";  //正式服
	//let hostUrl = "https://test.bat.com/";  //测试服
	if(process.env.NODE_ENV=='development'){
	  hostUrl='https://api.bat.com/';
	}
	
    axios
      .get(hostUrl + "platform/v1/web/tenant/url", {
        params: params,
      })
      .then((res) => {
        console.log("后台配置接口地址：", res);
        if (res.data.success) {
          let data = res.data.data;
          localStorage.setItem("tenantId", data.tenantId);
          localStorage.setItem("tenantNo", data.tenantNo);
          localStorage.setItem("tenantHost", data.host);
          localStorage.setItem("tenantUrl", data.url);
        } else {
          localStorage.removeItem("tenantId");
          localStorage.removeItem("tenantNo");
          localStorage.removeItem("tenantHost");
        }
      });
  },
};
</script>

<style lang='less'>
@import "../src/assets/css/common.less";
@import "../src/assets/css/iconfont.css";
@import url("../src/assets/less/variable.less");
#app {
  min-width: 1260px;
  height: 100%;
}
html {
  background-color: @white;
}
body {
  padding-right: 0 !important;
}
.el-step__description.is-finish {
  color: @blue;
}
.el-step__head.is-finish {
  color: @blue;
  border-color: @blue;
}
.el-step__head.is-process {
  color: @lighterGray;
}
.el-step.is-vertical .el-step__line {
  width: 0;
  left: 12px;
  border-left: 1px dashed @lighterGray;
  background-color: @white;
}
.el-step__description {
  padding-right: 5%;
}
.el-step__description.is-process {
  color: @lighterBlack;
}
.el-step__description.is-wait {
  color: @lighterBlack;
}
.el-step__icon-inner[class*="el-icon"]:not(.is-status) {
  font-size: 18px;
}
/*.el-loading-spinner{
    margin-top: -20px;
    background: url("../src/assets/images/star.png") center center no-repeat;
    .el-loading-text{
      padding-top: 20px;
      color: #ffa800;
    }
  }*/
.el-popper {
  .el-cascader-panel {
    .el-cascader-menu {
      min-width: auto;
      .el-cascader-menu__wrap {
        width: 100%;
        height: auto;
        margin-bottom: 10px !important;
        margin-right: 0 !important;
        -ms-overflow-style: none;
        scrollbar-width: none;
        &::-webkit-scrollbar {
          width: 0;
          height: 0;
          background: none;
          display: none;
        }
        &.-o-scrollbar {
          -moz-appearance: none !important;
          background: rgba(0, 255, 0, 0) !important;
        }
        .el-cascader-node {
          font-size: 12px;
          &.is-active {
            color: @blue;
          }
          .el-cascader-node__label {
            .color {
              display: inline-block;
              width: 12px;
              height: 12px;
              margin-right: 10px;
              border-radius: 4px;
              vertical-align: middle;
            }
            .text {
              vertical-align: middle;
            }
          }
        }
      }
    }
  }
}
.el-scrollbar__wrap {
  scrollbar-width: none;
}

::-webkit-input-placeholder {
  color: @placeholderClor;
}
::-moz-placeholder {
  color: @placeholderClor;
}
:-ms-input-placeholder {
  color: @placeholderClor;
}
.orderHint {
  /deep/.el-message-box__message {
    p {
      font-size: 20px;
    }
  }
}

#sysUpdate {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #ffffff;
  z-index: 10000;

  .sys-wrap {
    position: absolute;
    top: 48%;
    left: 50%;
    text-align: center;
    transform: translate(-50%, -50%);
  }

  img {
    width: 480px;
  }

  h6 {
    margin-top: 20px;
    font-size: 24px;
    color: #333333;
  }

  p {
    margin-top: 20px;
    font-size: 18px;
    color: #666666;
    line-height: 30px;
  }
}
</style>
