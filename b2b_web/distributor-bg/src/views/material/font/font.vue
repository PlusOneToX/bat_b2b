<template>
	<div class="add-productline">
		 <header v-if="append">
		  <h4>添加字体</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回列表
		  </el-button>
		</header>
		 <header v-if="looking">
		  <h4>查看字体</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回列表
		  </el-button>
		</header>

		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="vloading">
			<el-form-item label="字体名称" prop="name" style="margin-bottom: 10px;">
				<el-input size="mini" style="width:300px;" v-model="formData.name" placeholder="不超过40个字" maxlength="40"></el-input>
			</el-form-item>
			<el-form-item label="字体英文名称" prop="englishName" style="margin-bottom: 10px;">
				<el-input size="mini" style="width:500px;" v-model="formData.englishName" placeholder="不超过80个字" maxlength="80"></el-input>
			</el-form-item>
			<el-form-item label="字体包附件" prop="fontFile" style="margin-bottom: 10px;">
				<template>
					<el-upload
						class="upload-demo"
						action="http://172.16.12.81:9997/file/upload"
						multiple
						:limit="1"
						:on-remove="handleRemove"
						style="width:150px;"
						:on-exceed="handleExceed"
						:on-success="handleSuccess"
						:before-upload="beforeUpload"
						:file-list="fileList">
						<el-button size="mini" type="primary">上传附件</el-button>
					</el-upload>
				 </template>
			</el-form-item>

			<el-form-item label="字体描述" prop="description" style="margin-bottom: 10px;">
				<el-input style="width:50%;" size="mini"  type="textarea" :rows="4" v-model="formData.description" placeholder="不超过200个字" maxlength="200"></el-input>
			</el-form-item>
			<el-form-item label="状态" prop="openFlag">
				<el-radio-group v-model="formData.openFlag" size="mini">
					<el-radio :label="1" >启用</el-radio>
					<el-radio :label="0" >停用</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-button style="margin-left: 25%; margin-top:30px;" class="mini-search-btn" :loading="loading" @click="handleSubmit('formData')">保存</el-button>
			<el-button style="margin-bottom:30px;" size="mini"  @click="handleCancel">返回</el-button>
		</el-form>
	</div>
</template>
<script>
import {monthDay} from '@/utils/timeFormat.js'
export default {
	data() {
		return {
			loading: false,
			vloading: false,
			append:false,
			formData: {
				name: '',
				englishName: '',
				description: '',
				openFlag:1,
				fontFile:'',
				fileName:''
			},
			rules: {
				name: [{
					required: true,
					message: '请输入字体名称',
					trigger: 'blur'
				}],
				fontFile:[{
					required: true,
					message: '请上传字体包文件',
					trigger: 'blur'
				}],
			},
			fileList:[],
			imageObj:'',
			focus:false,	
			file:''
		}
	},
	created() {
		this.getData()
	},
	methods: {
		handleExceed(files, fileList){
			if(fileList.length>0){
				this.$message.warning("已经存在字体包文件，请先删除历史字体包文件再上传")
			}
		},
		getData(){
			if(this.$route.params.id != undefined) {
				this.vloading = true
				this.looking = true
				this.append = false
				// 详情
				this.$http.fontDetail(this, {id:this.$route.params.id}).then(res => {
					if(res.success) {
						this.formData = res.data
						if(this.formData.fontFile !== undefined && this.formData.fontFile !== null && this.formData.fontFile !== ''){
							if(this.formData.fileName === undefined || this.formData.fileName === null || this.formData.fileName === ''){
								this.formData.fileName = this.formData.name
							}
							this.fileList.push({name:this.formData.fileName,url:this.formData.fontFile})
						}
					}
					this.vloading= false
				})
			}else{
				this.looking = false
				this.append = true
			}
		},
		handleSuccess(response, file) {
			console.log(this.fileList);
		},
		beforeUpload(file) {
			// 随机命名
			let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
			this.$http.getFileSts(this).then(result => { 	
				const client = new OSS.Wrapper({
				region: result.data.region,
				accessKeyId: result.data.accessKeyId,
				accessKeySecret: result.data.accessKeySecret,
				stsToken: result.data.securityToken,
				bucket: result.data.bucketName,
				endpoint: result.data.endpoint,
				secure:true
				})
				// 上传
				client.multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {
				}).then((results) => {
					return new Promise((resolve, reject) => {
						this.formData.fontFile = result.data.hostname + results.name
						this.formData.fileName = file.name
						resolve(true)
					})
				}).catch((err) => {
					console.log(err)
				})
			})
		},
		// 随机生成文件名
		random_string(len) {　　
			len = len || 32;　　
			var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';　　
			var maxPos = chars.length;　　
			var pwd = '';　　
			for(let i = 0; i < len; i++) {　　
				pwd += chars.charAt(Math.floor(Math.random() * maxPos));
			}
			return pwd;
		},
		handleRemove(file) {
			this.formData.fontFile = ''
			this.formData.fileName = ''
			this.fileList = []
		},
		handleSubmit(data) {
			this.$refs[data].validate(valid => {
				if(valid) {
					this.loading = true;
					if(this.formData.id !== undefined && this.formData.id !== null && this.formData.id !== ''){ // 编辑
						this.$http.ediFont(this, this.formData).then(res => {		
							this.loading = false
							if(res.success){
								this.$message.success("编辑成功")
								this.$router.go(-1)
							}
						})
					}else{ // 新增
						this.$http.addFont(this, this.formData).then(res => {			
							this.loading = false
							if(res.success){
								this.$message.success("新增成功")
								this.$router.go(-1)
							}
						})
					}
				}
			})
		},
		handleCancel() {  // 返回操作
			this.$router.go(-1)
		},
	},
	watch: {

	}
}

</script>
<style lang="scss">
.add-productline {
	background-color: white;
	min-height: 100%;
	.hint-msg {
		color: #ccc;
		font-size: 12px;
		line-height: 40px;
		margin-left: 10px;
	}
	.el-input__inner,
	.el-textarea__inner {
		background-color: white;
	}
	.header-row {
		@include table-header-color;
	}
	.distributor-table {
		margin-top: 20px;
	}
	.distributor-content {
		margin-top: 10px;
	}
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
		margin-bottom: 20px;
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
	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}
	.avatar-uploader .el-upload:hover {
		border-color: rgb(33, 184, 203);
	}
	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 120px;
		height: 120px;
		line-height: 120px;
		text-align: center;
	}
	.avatar {
		width: 120px;
		height: 120px;
		display: block;
	}
	.category-input {
		width: 193px;
	}
	.category-box {
		border: 1px solid #ccc;
		width: 193px;
		padding: 10px;
		background-color: white;
		position: absolute;
		left: 0;
		top: 45px;
		z-index: 99;
		border-radius: 10px;
	}
}

</style>
