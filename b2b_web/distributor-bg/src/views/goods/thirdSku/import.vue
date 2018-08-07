<!--
 * @Author: yaowei
 * @Date: 2018-05-17 14:08:29
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-17 18:10:33
-->
<template>
  <div class="batch-lead">
    <header>
      <h4>批量导入第三方数据</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="goBack">
        返回列表
      </el-button>
    </header>

    <div class="batch-body">
      <div class="btach-one">
        <h4>第一步：导出XLS文件模板</h4>
      </div>
      <div class="btach-channel">
        <button class="mini-search-btn" @click="exportXLS()"> 点击此处导出XLS文件模板 </button>
      </div>
      <div class="btach-one">
        <h4>第二步：模板说明</h4>
      </div>
      <div class="btach-step">
        <div style="margin-bottom: 15px;">
          <h4>1、打开刚导出的XLS文件模板，在这里写入对应的订单信息。请注意填写的格式，如果格式不正确将不能导入订单</h4>
        </div>
        <div>
          <h4>2、请参考以下截图进行填写。</h4>
          <p class="temp-wrap"><img src="../../../assets/images/thirdSkuTemp.png" alt=""></p>
        </div>
      </div>
      <div class="btach-one">
        <h4>第三步：上传填好的XLS文件</h4>
      </div>
      <div class="btach-step">
        <div style="margin-bottom: 15px;">
          <h4>1、请输入分销商ID</h4>
          <p>
            <el-input
            style="width: 200px; margin-top: 15px; margin-left: 41px;"
            v-model.trim="distributorId"
            clearable
            placeholder="请输入分销商ID"
            class="box-input"
          ></el-input>
          </p>
        </div>
        <div>
          <h4>2、上传XLS文件<span class="tips">（上传完成后会将该分销商之前的数据清空）</span></h4>
          <el-upload style="margin-top: 15px;margin-left: 41px;" drag class="upload-demo" :headers="importHeaders"
            :action="action + '?distributorId=' + distributorId" :auto-upload="true" :show-file-list="true"
            :before-upload="beforeUpload" :on-success="uploadSuccess" :on-error="uploadFail" :on-progress="onProgress">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">上传格式类型只能为xls,xlsx格式</div>
          </el-upload>
        </div>
      </div>
    </div>

  </div>
</template>

<script type="text/javascript">
  import axios from "axios";
  import url from "@/api/allUrl";
  import { getToken } from "@/utils/auth";
  export default {
    name: 'batchChannelLead',
    data() {
      return {
        fileList: [],
        Authorization: '',
        loading: '',
        importHeaders: {
          Accept: "application/json",
          enctype: "multipart/form-data",
          Platform: "web",
          Version: "1.0.0",
          Authorization: "",
          token: "",
        },
        action: "",
        distributorId: "",
      }
    },
    created() {
      this.importHeaders.Authorization = getToken();
      this.importHeaders.token = getToken();
      let tenantUrl = localStorage.getItem('tenantUrl');
      this.action = tenantUrl + "/" + url.thirdSkuImport;
    },
    methods: {
      exportXLS() { // 导出文件模板
        let tenantUrl = localStorage.getItem('tenantUrl');
        axios({
          method: "post",
          url: tenantUrl + "/" + url.thirdSkutempDownload,
          responseType: "arraybuffer",
          headers: {
            "Content-Type": "application/json",
            token: getToken(),
          },
        }).then((res) => {
          const content = res.data
          let blob = new Blob([content], {
            type: "application/ms-excel"
          })
          let url = window.URL.createObjectURL(blob)
          if ('download' in document.createElement('a')) {
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', 'XLS文件模板.xls')
            document.body.appendChild(link)
            link.click()
          } else {
            navigator.msSaveBlob(blob, 'XLS文件模板.xls')
          }
        })
      },
      goBack() { // 返回操作
        this.$router.back(-1)
      },
      beforeUpload(file) { //上传前配置
        let excelfileExtend = ".xls,.xlsx"; //设置文件格式
        let fileExtend = file.name
          .substring(file.name.lastIndexOf("."))
          .toLowerCase();
        if (excelfileExtend.indexOf(fileExtend) <= -1) {
          this.$message.error("只能上传.xls,.xlsx格式");
          return false;
        }
      },
      onProgress(event, file, fileList) { // 上传时的钩子
        this.loading = this.$loading({
          lock: true,
          text: '文件上传中....',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      },
      uploadFail(err, file, fileList) { //上传错误
        this.$message.error({
          message: err.errMessage,
          duration: 3 * 1000,
          onClose: () => {}
        });
        this.loading.close();
      },
      beforeUpload() { // 上传前的配置
        this.loading = this.$loading({
          lock: true,
          text: '文件正在处理中....',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      },
      uploadSuccess(response, file, fileList) { //上传成功
        if (response.success) {
          this.$message.success({
            message: "上传成功",
            duration: 3 * 1000,
            onClose: () => {}
          })
          this.loading.close()
        } else {
          this.$message.error({
            message: response.errMessage,
            duration: 3 * 1000,
            onClose: () => {}
          })
          this.loading.close()
        }
      },
    },
  }

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

    .temp-wrap {
      padding-top: 10px;
      padding-left: 20px;

      img {
        width: 100%;
      }
    }

    .tips {
      color: #f00;
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
    }
  }

</style>
