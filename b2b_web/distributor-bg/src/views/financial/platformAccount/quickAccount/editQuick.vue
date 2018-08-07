<template>
  <div class="alipay-add-edit">
    <header>
      <h4 v-if="isEdit">查看快钱账户</h4>
      <h4 v-else>添加快钱账户</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="cancel"
      >
        返回快钱账户列表
      </el-button>
    </header>
    <el-form
      ref="form"
      :model="form"
      label-width="260px"
      class="el-form1"
      :rules="rules"
    >
      <el-form-item label="ERP银行账号：" prop="bankNo">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.bankNo"
        ></el-input>
      </el-form-item>
      <el-form-item label="ERP编码：" prop="erpAccountNo">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.erpAccountNo"
        ></el-input>
      </el-form-item>
      <el-form-item label="所属组织: " prop="organizationId">
        <el-select size="mini" v-model="form.organizationId">
          <el-option
            v-for="item in organizationList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="收款账户名称" prop="accountName">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.accountName"
        ></el-input>
      </el-form-item>
      <el-form-item label="快钱人民币网关账号：" prop="merchanAcctId">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.merchanAcctId"
        ></el-input>
      </el-form-item>
      <el-form-item label="签名密码：" prop="signPwd">
        <el-input
          size="mini"
          style="width: 600px"
          :rows="5"
          v-model.trim="form.signPwd"
          type="textarea"
        ></el-input>
      </el-form-item>
      <el-form-item label="私钥：" prop="signPrivateKey">
        <el-input
          size="mini"
          style="width: 600px"
          :rows="7"
          v-model.trim="form.signPrivateKey"
          type="textarea"
        ></el-input>
      </el-form-item>
      <el-form-item label="签名文件路径：" prop="signFileUrl">
        <template>
          <el-upload
            class="avatar-uploader"
            :action="action"
            :show-file-list="false"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
          >
            <canvas
              style="width: 120px; height: 120px"
              v-if="form.signFileUrl"
              id="canvas"
            ></canvas>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>
      <el-form-item
        label="快钱支付通知回调验证签名文件路径："
        prop="checkSignFileUrl"
      >
        <template>
          <el-upload
            class="avatar-uploader"
            :action="action"
            :show-file-list="false"
            :on-remove="handleRemove2"
            :before-upload="beforeUpload2"
          >
            <canvas
              style="width: 120px; height: 120px"
              v-if="form.checkSignFileUrl"
              id="canvas"
            ></canvas>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </template>
      </el-form-item>
      <el-form-item label="取消订单是否原路返回: " prop="backType">
        <el-radio-group v-model="form.backType">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="状态: " prop="openFlag">
        <el-radio-group v-model="form.openFlag">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div class="clearfix footbtn">
      <el-button style="margin-left: 25%" size="mini" @click="cancel"
        >返回</el-button
      >
      <el-button class="mini-search-btn" @click="submitForm('form')"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
import PDFJS from 'pdfjs-dist'
import {monthDay} from '@/utils/timeFormat.js'
export default {
  data() {
    return {
      organizationList: [], // 所属组织列表
      form: {
        accountName: '',
        erpAccountNo:'',
        merchanAcctId: '',
        signPwd:'',
        signPrivateKey: '',
        organizationId: '',
        signFileUrl: '',
        checkSignFileUrl: '',
        bankNo: '',
        backType: 1,
        openFlag: 1
      },
      rules: {
        accountName: [{
          required: true,
          message: '请输入收款账户名称',
          trigger: 'blur'
        }],
        erpAccountNo:[{
          required: true,
          message: '请输入ERP编码',
          trigger: 'blur'
        }],
        merchanAcctId: [{
          required: true,
          message: '请输入人民币网关账号',
          trigger: 'blur'
        }],
        signPwd: [{
          required: true,
          message: '请输入签名密码',
          trigger: 'blur'
        }],
        signPrivateKey: [{
          required: true,
          message: '请输入私钥',
          trigger: 'blur'
        }],
        signFileUrl: [{
          required: true,
          message: '请选择签名文件路径',
          trigger: 'blur'
        }],
        checkSignFileUrl: [{
          required: true,
          message: '请选择快钱支付通知回调验证签名文件路径',
          trigger: 'blur'
        }],
        organizationId: [{
          required: true,
          message: '请选择所属组织',
          trigger: 'blur'
        }],
        bankNo: [{
          required: true,
          message: '请输入erp银行账号',
          trigger: 'blur'
        }]
      },
      isEdit:false,
      action: process.env.BASE_API + 'system/v1/web/admin/oss/sts'
    }
  },
  created(){
    this.getOrganizationList().then(res => {
      if (res.success) {
          this.organizationList = res.data.list
          this.getParams();
      }
    })
  },
  methods: {
    // 获取所属组织列表
    getOrganizationList() {
    return new Promise((resolve, reject) => {
        this.$http.getOrganizationList(this, {page:1, size:1000}).then(res => {
        if (res.success) {
            resolve(res)
        } else {
            reject()
        }
        })
    })
    },
    // 验证
    submitForm(form){
      this.$refs[form].validate((valid) => {
        if (valid) {
            this.handleSave();
        } else {
            return false;
        }
      });
    },
    // 保存提交
    handleSave(){
      this.$refs['form'].validate(valid => {
        if(valid){
          if(this.isEdit){
            this.$http.editPlatAccountQuick(this, this.form).then(res => {        
              if(res.success){
                this.$message({
                  message: '保存成功',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
                })
                this.cancel()
              }
              this.loading = false
            })
          }else {
            this.$http.addPlatAccountQuick(this, this.form).then(res => {        
              if(res.success){
                this.$message({
                  message: '保存成功',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
                })
                this.cancel()
              }
              this.loading = false
            })
          }
        }
      })
    },
    // 返回
    cancel(){
        this.$router.push({name:'quickPayList'})
    },
    // 获取详情
    getParams(){
      this.isEdit = false
      if(this.$route.query.id != undefined){
        this.isEdit = true
        this.$http.platAccountQuickDetail(this, {id:this.$route.query.id}).then(res => {        
          if(res.success){
              this.form = res.data
              // this.form.organizationId = res.data.organizationId.toString()
          }
        })
      }else{
        this.form.accountName = '';
        this.form.erpAccountNo='';
        this.form.merchanAcctId = ''
        this.form.signPwd = ''
        this.form.signPrivateKey = ''
        this.form.signFileUrl = '',
        this.form.checkSignFileUrl = '',
        this.form.bankNo = ''
        this.form.organizationId = ''
        this.form.backType = 1,
        this.form.openFlag = 1
      }
    },
    // 上传签名文件url
    beforeUpload(file) {
			// if(file.type != 'application/pdf'){
			// 	this.$message.error('上传只能是pdf格式!')
			// 	return false
			// }
			const isLt2M = file.size / 1024 / 1024 < 2;
			if (!isLt2M) {
				this.$message.error('上传文件大小不能超过 2MB!');
				return isLt2M
			}
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
						this.form.signFileUrl = result.data.hostname + results.name
						// this.loadFile(this.form.signFileUrl)
						resolve(true)
					})
				}).catch((err) => {
					console.log(err)
				})
			})
		},
    loadFile (url) {
			PDFJS.getDocument(url).then((pdf) => {
				this.pdfDoc = pdf
				this.$nextTick(() => {
					this.renderPage()
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
			this.form.signFileUrl = ''
		},
     // 上传检查签名文件
    beforeUpload2(file) {
			// if(file.type != 'application/pdf'){
			// 	this.$message.error('上传只能是pdf格式!')
			// 	return false
			// }
			const isLt2M = file.size / 1024 / 1024 < 2;
			if (!isLt2M) {
				this.$message.error('上传文件大小不能超过 2MB!');
				return isLt2M
			}
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
						this.form.checkSignFileUrl = result.data.hostname + results.name
						// this.loadFile(this.form.signFileUrl)
						resolve(true)
					})
				}).catch((err) => {
					console.log(err)
				})
			})
		},
    handleRemove2(file) {
			this.form.checkSignFileUrl = ''
		}
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.alipay-add-edit {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    margin-bottom: 3%;
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
}
.el-form1 {
  width: 800px;
  margin-top: 30px;
  padding-left: 20px;
}
.el-form2 {
  width: 400px;
  margin-top: 30px;
  padding-left: 20px;
}

.file {
  position: absolute;
  width: 290px;
  height: 40px;
  top: 0;
  opacity: 0;
  padding: 0px;
  filter: alpha(opacity=0);
  cursor: pointer;
}
.file-name {
  top: 0;
  position: relative;
  display: inline-block;
  overflow: hidden;
}

.el-icon-document::before {
  content: url("/src/assets/images/attachment.png");
}
.footbtn {
  margin-top: 40px;
  padding-bottom: 20px;
}
.header-row {
  @include table-header-color;
}
</style>

