<template>
  <!-- <div id="app" v-if="!hasUpdate"> -->
  <div id="app" v-if="hasTenantInfo">
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view>
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
      hasUpdate: false, // 是否启动升级
      hasTenantInfo: false, // 是否有租户信息
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
        qryUrlType: 8, // 传参主机类型(传参主机host不为空时填，默认3)：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端 9兑换商城H5端
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
            this.$confirm("网址解析异常，请输入正确的网址访问", "温馨提示", {
              customClass: "confirm-v-dialog-upload",
              showConfirmButton: false,
              showCancelButton: false,
              showClose: false,
              closeOnClickModal: false,
              type: "warning",
              iconClass: "el-warning",
              center: true,
            });
          } else if (res.errCode === "B_PLATFORM_WX_PROGRAM_APP_ID_NULL") {
            this.$confirm(
              "服务器连接异常，请输入正确的网址重新访问",
              "温馨提示",
              {
                customClass: "confirm-v-dialog-upload",
                showConfirmButton: false,
                showCancelButton: false,
                showClose: false,
                closeOnClickModal: false,
                type: "warning",
                iconClass: "el-warning",
                center: true,
              }
            );
          } else if (res.errCode === "B_PLATFORM_GAIN_URL_NULL") {
            this.$confirm("获取服务器地址失败，请联系客户反馈", "温馨提示", {
              customClass: "confirm-v-dialog-upload",
              showConfirmButton: false,
              showCancelButton: false,
              showClose: false,
              closeOnClickModal: false,
              type: "warning",
              iconClass: "el-warning",
              center: true,
            });
          } else {
            this.$message.warning(res.errMessage);
          }
        }
      });
    },
    getUrlParams(name) {
      // hash 模式
      const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      if (!window.location.hash.split("?")[1]) {
        return null;
      }
      const re = window.location.hash.split("?")[1].match(reg);
      if (re != null) return unescape(re[2]);
      return null;
    },
    // 系统升级
    sysUpdate() {
      let d1 = "2019-11-22 22:00:00"; // 开始时间
      let d2 = "2019-11-23 00:00:00"; // 结束时间
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
  },
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
#app {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  font-size: 0;
  overflow-x: hidden;
  overflow-y: auto;
  background-color: #ffffff; /* font-family: 'PingFangSC-Medium', 'PingFang SC', 'STHeitiSC-Light', 'Helvetica-Light', arial, sans-serif, 'Droid Sans Fallback' */
  user-select: none;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
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
<style>
.confirm-v-dialog-upload {
  width: 90%;
  border-radius: 8px;
  padding: 0;
}
.confirm-v-dialog-upload .el-message-box__header {
  padding-top: 18px;
  font-size: 18px;
  color: #4a4a4a;
}
.confirm-v-dialog-upload .el-message-box__header .el-warning {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 2px;
  background: #ffffff url("common/images/remind@2x.png") center no-repeat;
  background-size: 20px 20px;
}
.confirm-v-dialog-upload .el-message-box__content {
  font-size: 14px;
  color: #666666;
  text-align: left;
  line-height: 24px;
}

.confirm-v-dialog-upload .el-message-box__btns {
  display: flex;
  padding: 0;
}
.confirm-v-dialog-upload .el-message-box__btns .el-button {
  flex: 1;
  margin: 0;
  padding: 0;
  height: 50px;
  line-height: 50px;
  font-size: 18px;
  color: #4a4a4a;
  text-align: center;
  background-color: #fff;
  border-color: transparent;
  border-radius: 0;
  border-top: 1px solid #ebedf0;
}

.confirm-v-dialog-upload .el-message-box__btns .el-button--primary {
  color: #f21e1c;
  border-left: 1px solid #ebedf0;
}

.el-message {
  min-width: 320px;
  padding: 15px 8px;
}
.el-message__icon {
  margin-right: 5px;
}
.el-message--warning {
  color: #ffffff;
  border: none;
  background-color: rgba(0, 0, 0, 0.9);
}
</style>
