<template>
  <div id="app" v-if="hasTenantInfo">
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive && isReloadAlive"></router-view>
  </div>

  <!-- <div id="update" v-else>
    <div class="system-wrap">
      <img src="./common/images/system-update.gif" alt="系统升级中..." />
      <p>我们正在升级系统服务</p>
      <p>请稍后再来</p>
    </div>
  </div> -->
</template>

<script>
import api from "api/allApi.js";

export default {
  name: "App",
  data() {
    return {
      isReloadAlive: true,
      hasUpdate: false, // 是否启动升级
      hasTenantInfo: false, // 是否有租户信息
    };
  },
  provide() {
    return {
      reload: this.reload,
    };
  },
  mounted() {
    // 系统升级
    // this.sysUpdate();

    // 初始化租户信息
    this.initTenantInfo();
  },
  methods: {
    // 初始化租户信息
    initTenantInfo() {
      let host = window.location.host.split(":")[0];
      if (process.env.NODE_ENV === "development") {
        host = "test.bat.com";
      }
      let params = {
        gainUrlType: 6, // 需获取的主机类型（默认6）：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端  9兑换商城H5端
        qryUrlType: 9, // 传参主机类型(传参主机host不为空时填，默认3)：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端 9兑换商城H5端
        host: host, // 主机
      };

      // 获取链接 tenantNo
      let tenantNo = this.getUrlParams("tenantNo") || "";
      if (tenantNo) {
        // 链接有 tenantNo，获取缓存 tenantNo，获取缓存
        let localTenantNo = localStorage.getItem("tenantNo") || "";
        // 链接与缓存不匹配，重新获取 url 配置
        params = {
          ...params,
          ...{ tenantNo: tenantNo },
        };
        if (localTenantNo) {
          if (localTenantNo !== tenantNo) {
            this.getTenantInfo(params);
          }
        } else {
          this.getTenantInfo(params);
        }
      } else {
        // 链接无 tenantNo，获取 url 配置
        this.getTenantInfo(params);
      }
    },
    // 获取租户地址
    getTenantInfo(params) {
      this.$api.get(this, api.getTenant, params).then((res) => {
		console.log(res);
        if (res.success) {
          localStorage.setItem("tenantUrl", res.data.url + "/");
          localStorage.setItem("tenantNo", res.data.tenantNo);
          this.hasTenantInfo = true;
        } else {
          if (res.errCode === "B_PLATFORM_QRY_URL_ERROR") {
            this.$dialog.confirm({
              title: "温馨提示",
              message: "网址解析异常，请输入正确的网址访问",
              showConfirmButton: false,
              showCancelButton: false,
            });
          } else if (res.errCode === "B_PLATFORM_WX_PROGRAM_APP_ID_NULL") {
            this.$dialog.confirm({
              title: "温馨提示",
              message: "服务器连接异常，请输入正确的网址重新访问",
              showConfirmButton: false,
              showCancelButton: false,
            });
          } else if (res.errCode === "B_PLATFORM_GAIN_URL_NULL") {
            this.$dialog.confirm({
              title: "温馨提示",
              message: "获取服务器地址失败，请联系客户反馈",
              showConfirmButton: false,
              showCancelButton: false,
            });
          } else {
            this.$toast({
              message: res.errMessage,
            });
          }
        }
      });
    },
    getUrlParams(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      var r = window.location.search.substr(1).match(reg);
      this.code = r;
      if (r != null) {
        const result = unescape(r[2]);
        return result;
      }
      return null;
    },
    // 系统升级
    sysUpdate() {
      let d1 = "2019-08-07 23:00:00"; // 开始时间
      let d2 = "2019-08-08 08:00:00"; // 结束时间
      let startTime = new Date(Date.parse(d1.replace(/-/g, "/")));
      let endTime = new Date(Date.parse(d2.replace(/-/g, "/")));
      let now = new Date(); // 当前时间
      if (now >= startTime && now <= endTime) {
        // 判断是否在有效时间内
        this.hasUpdate = true;
      } else {
        this.hasUpdate = false;
      }
    },
    reload() {
      this.isReloadAlive = false;
      this.$nextTick(function () {
        this.isReloadAlive = true;
      });
    },
  },
};
</script>

<style lang="stylus" scoped>
#app {
  width: 100%;
  min-height: 100%;
}

// 系统升级
#update {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #FFFFFF;
}

.system-wrap {
  position: absolute;
  top: 48%;
  left: 50%;
  text-align: center;
  transform: translate(-50%, -50%);

  img {
    width: 260px;
  }

  p {
    font-size: 15px;
    color: #333333;
    line-height: 20px;
  }
}
</style>