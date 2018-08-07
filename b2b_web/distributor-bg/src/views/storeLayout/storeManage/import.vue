<template>
  <div class="store-import">
    <header class="header">
      <h4 class="title">批量导入店铺</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-back" @click="handleBack">返回</el-button>
    </header>
    	<div class="batch-body">
			<div class="btach-one">
				<h4>第一步：导出XLS文件模板</h4>
			</div>
			<div class="btach-channel">
				<button class="mini-search-btn" @click="exportXLS()"> 点击此处导出XLS文件模板 </button>
			</div>
			<div class="btach-one">
				<h4>第二步：填写XLS文件说明</h4>
			</div>
			<div class="btach-step">
				<div style="margin-bottom: 15px;">
					<h4>1、打开刚导出的XLS文件模板，在这里写入对应的信息。请注意填写的格式，如果格式不正确将不能导入</h4>
				</div>
				<!-- <div class="batch-explain">
          <div class="bg_td"></div>
				</div> -->
			</div>
			<div class="btach-one">
				<h4>第三步：上传填好的XLS文件</h4>
			</div>
			<div class="btach-step">
				<div>
					<h4>1、上传XLS文件</h4>
						<el-upload style="margin-top: 15px;margin-left: 41px;"
								drag
								class="upload-demo"
								:headers="importHeaders"
								:action="action"
								:auto-upload="true"
								:show-file-list="true"
								:before-upload="beforeUpload"
								:on-success="uploadSuccess"
								:on-error="uploadFail"
								:on-progress="onProgress">
							<i class="el-icon-upload"></i>
							<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
							<div class="el-upload__tip" slot="tip">上传格式类型只能为xls,xlsx格式，且不超过500kb</div>
						</el-upload>
				</div>
			</div>
		</div>
  </div>
</template>

<script type="text/javascript">
import axios from 'axios'
import url from '@/api/allUrl'
import { getToken } from '@/utils/auth'
export default {
  data() {
    return {
      loading: '',
      importHeaders: {
        Accept: 'application/json',
        enctype: 'multipart/form-data',
        Platform: 'web',
        Version: '1.0.0',
        Authorization: getToken(),
        token: getToken()
      },
      action: ""
    }
  },
  created() {
    let tenantUrl = localStorage.getItem('tenantUrl');
    this.action = tenantUrl + "/" + url.shopImport;
  },
  methods: {
    //导出XLS文件模板
    exportXLS() {
      let tenantUrl = localStorage.getItem('tenantUrl');
      axios({
        method: 'post',
        url: tenantUrl + "/" + url.shopTempDownLoad,
        data: "",
        responseType: 'arraybuffer',
        headers: {
          'Content-Type': 'application/json',
          token: getToken()
        }
      }).then(res => {
        const content = res.data
        let blob = new Blob([content],{
          type: "application/ms-excel"
        })
        let url = window.URL.createObjectURL(blob)
        if ('download' in document.createElement('a')) {
          let link = document.createElement('a')
          link.style.display ='none'
          link.href = url
          link.setAttribute('download','XLS文件模板.xls')
          document.body.appendChild(link)
          link.click()
        }else{
          navigator.msSaveBlob(blob, 'XLS文件模板.xls')
        }
      })
    },
    // 判断上传文件格式
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
    // 上传时的钩子
    onProgress(event, file, fileList) {
      this.loading = this.$loading({
      	lock: true,
      	text: '文件上传中....',
      	spinner: 'el-icon-loading',
      	background: 'rgba(0, 0, 0, 0.7)'
      });
    },
    //上传成功
    uploadSuccess(response, file, fileList) {
      if(response.success) {
        this.$message.success({
          message: '上传成功',
          duration: 3 * 1000,
          onClose: () => { }
        })
        this.loading.close()
        console.log('upload success')
      } else {
        this.$message.error({
          message: response.msg,
          duration: 3 * 1000,
          onClose: () => { }
        })
        this.loading.close()
      }
    },
    //上传错误
    uploadFail(err, file, fileList) {
      this.$message.error({
        message: err.msg,
        duration: 3 * 1000,
        onClose: () => { }
      });
      this.loading.close();
    },
    // 返回列表
    handleBack() {
      this.$router.push({name: 'storeManage'})
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .store-import{
    background-color: white;
    padding-bottom:10px;
    .batch-body {
      width: 95%;
      margin: 50px auto 60px;
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
          margin: 15px 0 0 0;
          .bg_td {
            background-image: url('../../../assets/images/import.png');
            background-position: center;
            background-repeat: no-repeat;
            background-position: 20px 20px;
            width: 810px;
            height: 500px;
          }
        }
        .batch-citing {
          margin: 15px 0 0 41px;
        }
      }
    }
  }
</style>