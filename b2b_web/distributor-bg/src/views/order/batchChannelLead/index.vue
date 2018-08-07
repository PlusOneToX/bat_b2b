<template>
  <div class="batch-lead">
    <header>
      <h4>批量导入订单</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="cancel"
      >
        返回批量导入订单列表
      </el-button>
    </header>

    <el-tabs v-model="importOrderType">
      <el-tab-pane label="标准订单导入" name="1"></el-tab-pane>
      <!-- <el-tab-pane label="定制订单导入" name="2"></el-tab-pane> -->
    </el-tabs>

    <div class="batch-body">
      <div class="btach-one">
        <h4>第一步：导出XLS文件模板</h4>
      </div>
      <div class="btach-channel">
        <button class="mini-search-btn" @click="exportXLS()">
          点击此处导出XLS文件模板
        </button>
      </div>
      <div class="btach-one">
        <h4>第二步：填写XLS文件</h4>
      </div>
      <div class="btach-step">
        <div style="margin-bottom: 15px">
          <h4>
            1、打开刚导出的XLS文件模板，在这里写入对应的订单信息。请注意填写的格式，如果格式不正确将不能导入订单
          </h4>
        </div>
        <div>
          <h4>2、请参考文件进行填写。</h4>
        </div>
        <div class="batch-explain">
          <button class="mini-search-btn" @click="writeState">填写说明</button>
        </div>
        <!-- <div class="batch-citing">
          <button class="mini-search-btn" @click="writeCiting()">
            填写举例
          </button>
        </div> -->
      </div>
      <div class="btach-one">
        <h4>第三步：上传填好的XLS文件</h4>
      </div>
      <div class="btach-step">
        <div>
          <h4>1、上传XLS文件</h4>
          <el-upload
            style="margin-top: 15px; margin-left: 41px"
            drag
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
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              上传格式类型只能为xls,xlsx格式，且不超过500kb
            </div>
          </el-upload>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/javascript">
import { getToken } from "@/utils/auth";
import axios from "axios";

export default {
  name: "batchChannelLead",
  data() {
    return {
      importOrderType: "1",
      isShow: false,
      fileupView: true,
      fileList: [],
      Authorization: "",
      loading: "",
      fileList: [],
      importHeaders: {
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        token: "",
      },
      uploadTip: "点击上传",
      importFlag: 1,
      action: "",
      uploadFile: new FormData(),
    };
  },
  created() {
    this.importHeaders.token = getToken();
  },
  methods: {
    exportXLS() {
      // 导出文件模板
      this.$http
        .getTempDownload(this, {
          fileType: parseInt(this.importOrderType),
        })
        .then((res) => {
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
    writeCiting() {
      // 填写举例
      this.$api
        .exportData(this, "admin/u/p/importOrder/tempDownLoad", {
          type: 1,
          importOrderType: this.importOrderType,
        })
        .then((res) => {
          const content = res;
          let blob = new Blob([content], {
            type: "application/ms-excel",
          });
          let url = window.URL.createObjectURL(blob);
          if ("download" in document.createElement("a")) {
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.setAttribute("download", "填写举例.xls");
            document.body.appendChild(link);
            link.click();
          } else {
            navigator.msSaveBlob(blob, "填写举例.xls");
          }
        });
    },
    writeState() {
      // 填写说明操作
      this.$router.push({ name: "declare" });
    },
    cancel() {
      // 返回操作
      this.$router.push({ name: "batchChannel" });
    },

    importData() {
      this.fileList = [];
      this.dialogImportVisible = true;
    },
    handlePreview(file) {
      //可以通过 file.response 拿到服务端返回数据
    },
    handleRemove(file, fileList) {
      //文件移除
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
      //上传错误
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
      //上传成功
      if (response.code == 0) {
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
      this.$http.importOrder(this, formData).then((res) => {
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
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.batch-lead {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .btn-home {
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
}
.batch-body {
  width: 95%;
  margin: 50px auto;
  border: 1px solid #dcdcdc;
  border-radius: 6px;
  .btach-one {
    padding: 15px;
    background-color: #f7f7f7;
    border: 1px solid #dcdcdc;
  }
  .btach-channel {
    padding: 15px 0 15px 33px;
    line-height: 50px;
    border: 1px solid #dcdcdc;
  }
  .btach-step {
    padding: 17px;
    .batch-explain {
      margin: 15px 0 0 41px;
    }
    .batch-citing {
      margin: 15px 0 0 41px;
    }
  }
}
</style>
