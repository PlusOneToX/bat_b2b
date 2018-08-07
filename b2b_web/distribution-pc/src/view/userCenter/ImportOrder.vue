<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div
          class="
            user-right
            rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double
          "
        >
          <div class="content rl-tc">
            <h6 class="user-right-title rl-tl">
              {{ $t("UserCenter.BatchImport") }}
            </h6>
            <el-tabs v-model="importOrderType" class="rl-margin-top-default">
              <el-tab-pane
                :label="$t('versionsps.StandardOrder')"
                name="1"
              ></el-tab-pane>
              <!-- <el-tab-pane
                :label="$t('versionsps.DiyOrder')"
                name="2"
              ></el-tab-pane> -->
            </el-tabs>
            <div class="upload-box">
              <h4 class="upload">{{ $t("versionsps.UploadFile") }}</h4>
              <el-upload
                class="upload-demo"
                :headers="importHeaders"
                :action="action"
                :auto-upload="true"
                :show-file-list="true"
                :before-upload="beforeUpload"
                :on-success="uploadSuccess"
                :on-error="uploadFail"
                :on-progress="onProgress"
                :http-request="handleUpload"
              >
                <el-button type="primary">
                  <i class="el-icon-upload"></i>
                  {{ $t("versionsps.SelectFileUpload") }}
                </el-button>
                <div class="el-upload__tip" slot="tip">
                  {{ $t("versionsps.FileFormatMsg") }}
                </div>
                <p class="el-upload__tip red" slot="tip">
                  （{{ $t("versionsps.FileUploadTips") }}）
                </p>
              </el-upload>
            </div>
            <div class="help-box">
              <h4>
                {{ $t("versionsps.GetTemplate") }}
                <em @click="exportXLS">{{ $t("versionsps.Download") }}</em>
              </h4>
              <ul class="step-list">
                <li class="step-item">
                  <div class="title">{{ $t("versionsps.Step1") }}</div>
                  <img src="../../../src/assets/images/step1.png" alt />
                </li>
                <li class="step-item">
                  <div class="title">{{ $t("versionsps.Step2") }}</div>
                  <img src="../../../src/assets/images/step2.png" alt />
                </li>
                <li class="step-item">
                  <div class="title">{{ $t("versionsps.Step3") }}</div>
                  <img src="../../../src/assets/images/step3.png" alt />
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";

import { getToken } from "@/utils/auth";
import GD from "@/assets/js/globalData";

import { getTempDownload, importOrder } from "@/apiService/api";

export default {
  name: "WantBook",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      importOrderType: "1",
      userState: 2,
      canExportPrice: "2",
      loading: false,
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      importHeaders: {
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        Authorization: "",
      },
      action: "",
    };
  },
  methods: {
    exportXLS() {
      // 导出文件模板
      getTempDownload({
        fileType: parseInt(this.importOrderType),
      }).then((res) => {
        if (res.success) {
          let url = res.data;
          if ("download" in document.createElement("a")) {
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", "XLS文件模板.xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, "XLS文件模板.xls");
          }
        }
      });
    },
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    onProgress(event, file, fileList) {
      // 上传时的钩子
      this.loading = this.$loading({
        lock: true,
        text: "文件上传中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    uploadFail(err, file, fileList) {
      // 上传错误
      this.$message.error({
        message: err.msg,
        duration: 3 * 1000,
        onClose: () => {},
      });
      this.loading.close();
    },
    beforeUpload() {
      // 上传前的配置
      this.loading = this.$loading({
        lock: true,
        text: "文件正在处理中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    uploadSuccess(response, file, fileList) {
      // 上传成功
      if (response.code === 0) {
        this.$message.success({
          message: response.msg,
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.loading.close();
      } else {
        this.$message.error({
          message: response.msg,
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.loading.close();
      }
    },
    handleUpload(file) {
      let formData = new FormData();
      formData.append("file", file.file);
      importOrder(formData).then((res) => {
        if (res.success) {
          this.$message.success({
            message: "上传成功",
            duration: 3 * 1000,
            onClose: () => {},
          });
        } else {
          this.$message.error({
            message: res.errMessage,
            duration: 3 * 1000,
            onClose: () => {},
          });
        }

        this.loading.close();
      });
    },
  },
  created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    if (
      window.localStorage.getItem("canExportPrice") !== undefined &&
      window.localStorage.getItem("canExportPrice") !== null
    ) {
      this.canExportPrice = window.localStorage.getItem("canExportPrice");
    }
    this.importHeaders.Authorization = getToken();
  },
};
</script>
<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    /deep/.el-tabs {
      .el-tabs__nav {
        overflow: hidden;
        background-color: @bdLightColor;
        border-radius: 4px;
      }
      .el-tabs__nav-wrap::after {
        background-color: transparent;
      }
      .el-tabs__item {
        min-width: 93px;
        padding: 0 12px;
        text-align: center;
        &:hover,
        &.is-active {
          color: @white !important;
          background-color: @blue;
        }
      }
    }
    .upload-box {
      display: inline-block;
      margin-bottom: 52px;
      text-align: center;
      h4 {
        margin: 100px 0 10px;
        font-size: 14px;
        color: @lightBlack;
        font-weight: bold;
      }
      .upload-demo {
        .el-button {
          border-color: @blue;
          background-color: @blue;
          .el-icon-upload {
            margin-right: 10px;
            font-size: 16px;
            vertical-align: text-bottom;
          }
          &:hover {
            opacity: 0.6;
          }
        }
        .el-upload__tip {
          margin-top: 8px;
          font-size: 12px;
          color: @darkGray;
          &.red {
            color: @red;
          }
        }
      }
    }
    .help-box {
      padding-top: 16px;
      border-top: 1px solid @bdLightColor;
      text-align: center;
      h4 {
        margin-left: 20px;
        text-align: left;
        font-size: 12px;
        color: @lightBlack;
        em {
          margin-left: 5px;
          font-size: 14px;
          color: @blue;
          font-weight: bold;
          cursor: pointer;
        }
      }
      .step-list {
        margin-top: 42px;
        display: flex;
        align-items: center;
        justify-content: space-around;
        .step-item {
          flex: 1;
          .title {
            margin-bottom: 18px;
            font-size: 12px;
            color: @darkGray;
          }
        }
      }
    }
  }
}
</style>
