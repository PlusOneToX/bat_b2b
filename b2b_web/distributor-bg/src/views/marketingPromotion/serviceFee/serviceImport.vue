<template>
	<div class="service-import">
		<header>
			<h4>批量服务活动导入</h4>
			<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
				返回
			</el-button>
		</header>

		<div class="upload-box">
			<h4 class="upload">上传XLS文件</h4>
			<el-upload
							class="upload-demo"
							:headers="importHeaders"
							:action="action"
							:auto-upload="true"
							:show-file-list="true"
							:before-upload="beforeUpload"
							:on-success="uploadSuccess"
							:on-error="uploadFail"
							:on-progress="onProgress">
			<el-button type="primary"><i class="el-icon-upload"></i>选择文件并上传</el-button>
			<div class="el-upload__tip" slot="tip">上传格式类型只能为xls,xlsx格式，且不超过10MB</div>
			</el-upload>
		</div>
		<div class="help-box">
			<h4>如何获取XLS文件模板？<em @click="exportXLS">去下载</em></h4>
			<ul class="step-list">
			<li class="step-item">
				<div class="title">第一步：导出XLS文件模板</div>
				<img src="../promotionImport/step1.png" alt="">
			</li>
			<li class="step-item">
				<div class="title">第二步：填写XLS文件</div>
				<img src="../promotionImport/step2.png" alt="">
			</li>
			<li class="step-item">
				<div class="title">第三步：上传填好的XLS文件</div>
				<img src="../promotionImport/step3.png" alt="">
			</li>
			</ul>
		</div>
	</div>
</template>

<script type="text/javascript">
import { getToken } from '@/utils/auth'
export default {
	data() {
		return {
			isShow: false,
			fileupView: true,
			fileList: [],
			Authorization:'',
			loading: '',
			fileList: [],
			importHeaders: {
				Accept: 'application/json',
				enctype: 'multipart/form-data',
				Platform: 'web',
				Version: '1.0.0',
				Authorization: ''
			},
			uploadTip: "点击上传",
			importFlag: 1,
			action: process.env.BASE_API + 'admin/u/p/appoint/import'
		}
	},
	created(){
		this.importHeaders.Authorization = getToken();
	},
	methods: {
		exportXLS() { // 导出文件模板
			this.$api.exportData(this,'admin/u/p/appoint/tempDownLoad').then(res =>{
				const content = res
				let blob = new Blob([content],{
					type: "application/ms-excel"
				})
				let url = window.URL.createObjectURL(blob)
				if ('download' in document.createElement('a')) {
					let link = document.createElement('a')
					link.style.display ='none'
					link.href = url
					link.setAttribute('download','服务费活动导入模板.xls')
					document.body.appendChild(link)
					link.click()
				}else{
					navigator.msSaveBlob(blob, '服务费活动导入模板.xls')
				}
			})
		},
		cancel() { // 返回操作
			this.$router.go(-1)
		},
		importData() {
			this.fileList = [];
			this.dialogImportVisible = true;
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
				message: err.msg,
				duration: 3 * 1000,
				onClose: () => { }
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
			if(response.code == 0) {
				this.$message.success({
					message: response.msg,
					duration: 3 * 1000,
					onClose: () => { }
				})
				this.loading.close()
			}else {
				this.$message.error({
					message: response.msg,
					duration: 3 * 1000,
					onClose: () => { }
				})
				this.loading.close()
			}
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.service-import {
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
	.btn-home{
		float: right;
		padding: 5px;
		margin-top: 8px;
		margin-right: 8px;
		margin-left: 0;
		font-size: 12px;
	}
}
.upload-box {
	width: 100%;
	display:inline-block;
	margin-bottom:52px;
	text-align: center;
	h4 {
		margin:100px 0 10px;
		font-size:14px;
		color:#333333;
		font-weight: bold;
	}
	.upload-demo {
		.el-button {
			padding:10px 20px;
			border-color:#00CADD;
			background-color:#00CADD;
			.el-icon-upload {
				margin-right: 10px;
				font-size:16px;
				vertical-align: text-bottom;
			}
		}
		.el-upload__tip {
			margin-top:8px;
			font-size:12px;
			color:#666666;
		}
	}
}
.help-box {
	padding-top:16px;
	margin-left: 100px;
	margin-right: 100px;
	border-top:1px solid #EBEBEB;
	text-align: center;
	h4 {
		margin-left:20px;
		text-align: left;
		font-size:12px;
		width: 100%;
		margin-left: 100px;
		color:#333333;
		font-weight: bold;
		em {
			margin-left:5px;
			font-size:14px;
			color:#00CADD;
			font-weight: bold;
			cursor: pointer;
		}
	}
	.step-list {
		margin-top:42px;
		display: flex;
		align-items: center;
		justify-content: space-around;
		.step-item {
			flex: 1;
			list-style:none;
			.title {
				margin-bottom:18px;
				font-size:12px;
				color:#666666;
			}
		}
	}
}
</style>

  .service-import{
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