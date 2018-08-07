<template>
  <!-- <div id="app" v-if="!hasUpdate"> -->
  <div id="app">
    <!-- v-if="isRouterAlive" -->
    <router-view v-if="isRouterAlive"></router-view>
  </div>

  <!-- <div id="sysUpdate" v-else>
    <div class="sys-wrap">
      <img src="./assets/images/sys-update.gif" alt="系统升级" />
      <h6>系统升级通知</h6>
      <p>
        为提升系统支持服务和业务能力，公司系统正在进行升级，<br />暂不支持登录和使用，敬请谅解，如有疑问请联系客服。
      </p>
    </div>
  </div> -->
</template>

<script>
import axios from "axios";
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
  mounted() {
    // 系统升级通知
    let d1 = "2018-05-22 10:00:00"; // 开始时间
    let d2 = "2018-05-23 00:00:00"; // 结束时间
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
    if (process.env.NODE_ENV === "development") {
      host = "api.bat.com";
    }

    let tenantNo = this.getUrlCode() ? this.getUrlCode().tenantNo : "";
    let params = {
      gainUrlType: 6,
      host: host,
      qryUrlType: 1,
    };
    if (tenantNo && tenantNo != "") {
      params = { ...params, ...{ tenantNo: this.getUrlCode().tenantNo } };
    }

    let platApi = "https://api.bat.com/";
	//let platApi = "https://test.bat.com/";
    if (process.env.NODE_ENV === "development") {
      platApi = "https://test.bat.com/";
    }
    axios
      .get(platApi + "platform/v1/web/tenant/url", {
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
  methods: {
    //获取地址栏方法
    getUrlCode() {
      let url = location.href;
      if (url.split("?")[1]) {
        url = url.split("?")[1];
      } else {
        return;
      }

      console.log(url);
      /* eslint-disable */
      let theRequest = new Object();

      // if (url.indexOf("?") !== -1) {
      let str = url;
      console.log(str);
      let strs = str.split("&");
      for (let i = 0; i < strs.length; i++) {
        theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
      }
      return theRequest;
      // }
    },
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
  },
};
</script>

<style>
#sysUpdate {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #ffffff;
  z-index: 10000;
}
.sys-wrap {
  position: absolute;
  top: 48%;
  left: 50%;
  text-align: center;
  transform: translate(-50%, -50%);
}

.sys-wrap img {
  width: 480px;
}

.sys-wrap h6 {
  margin-top: 20px;
  font-size: 24px;
  color: #333333;
}

.sys-wrap p {
  margin-top: 20px;
  font-size: 18px;
  color: #666666;
  line-height: 30px;
}
</style>