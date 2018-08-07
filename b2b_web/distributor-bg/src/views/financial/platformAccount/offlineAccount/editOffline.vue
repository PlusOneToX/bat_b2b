<template>
  <div class="alipay-add-edit">
    <header>
      <h4 v-if="isEdit">查看线下账户</h4>
      <h4 v-else>添加线下账户</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="cancel"
      >
        返回线下账户列表
      </el-button>
    </header>
    <el-form
      ref="form"
      :model="form"
      label-width="260px"
      class="el-form1"
      :rules="rules">
      <el-form-item label="ERP编码：" prop="erpAccountNo">
        <el-input size="mini" style="width: 300px"
          v-model.trim="form.erpAccountNo"></el-input>
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
      <el-form-item label="开户行：" prop="bankName">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.bankName"
        ></el-input>
      </el-form-item>
      <el-form-item label="开户人姓名" prop="accountName">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.accountName"
        ></el-input>
      </el-form-item>
      <el-form-item label="账号：" prop="cardNo">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.cardNo"
        ></el-input>
      </el-form-item>
      <el-form-item label="银行地址：" prop="bankAddr">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.bankAddr"
        ></el-input>
      </el-form-item>
       <el-form-item label="支持币种: " prop="currencyCode">
        <el-select size="mini" v-model="form.currencyCode">
          <el-option
            v-for="item in currencyTypes"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否显示为前台转账账户: " prop="openFlag">
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
export default {
  data() {
    return {
      organizationList: [], // 所属组织列表
      currencyTypes: [], // 币种列表
      form: {
        erpAccountNo:'',
        accountName: '',
        organizationId: '',
        bankName: '',
        cardNo: '',
        bankAddr: '',
        currencyCode: '',
        openFlag: 1
      },
      rules: {
        erpAccountNo:[{
          required: true,
          message: '请输入ERP编码',
          trigger: 'blur'
        }],
        accountName: [{
          required: true,
          message: '请输入收款账户名称',
          trigger: 'blur'
        }],
        cardNo: [{
          required: true,
          message: '请输入账号',
          trigger: 'blur'
        }],
        organizationId: [{
          required: true,
          message: '请选择所属组织',
          trigger: 'blur'
        }],
        bankName: [{
          required: true,
          message: '请输入开户行',
          trigger: 'blur'
        }],
        bankAddr: [{
          required: true,
          message: '请输入银行地址',
          trigger: 'blur'
        }],
        currencyCode: [{
          required: true,
          message: '请选择支持币种',
          trigger: 'blur'
        }]
      },
      isEdit:false,
      action: process.env.BASE_API + 'file/upload'
    }
  },
  created(){
    this.initData().then(res => {
      if (res.currency && res.organization) {
        this.getParams();
      }
    })
  },
  methods: {
    initData() {
      // 币别列表
      let a = new Promise((resolve, reject) => {
        this.$http.currencyList(this, {page:1, size:1000}).then(res1 => { 
          if(res1.success) {
            this.currencyTypes = res1.data.list
            resolve(true)
          } else {
            reject()
          }
        })  
      })
      // 所属组织列表
      let b = new Promise((resolve, reject) => {
        this.$http.getOrganizationList(this, {page:1, size:1000}).then(res2 => {
          if (res2.success) {
            this.organizationList = res2.data.list
            resolve(true)
          } else {
            reject()
          }
        })
      })
      return Promise.all([a, b]).then(res => {
        return {
          currency: res[0],
          organization: res[1]
        }
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
          this.form.bankNo = this.form.cardNo
          if(this.isEdit){
            this.$http.editPlatAccountOffline(this, this.form).then(res => {        
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
            this.$http.addPlatAccountOffline(this, this.form).then(res => {        
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
        this.$router.push({name:'offlineList'})
    },
    // 获取详情
    getParams(){
      this.isEdit = false
      if(this.$route.query.id != undefined){
        this.isEdit = true
        this.$http.platAccountOfflineDetail(this, {id:this.$route.query.id}).then(res => {        
          if(res.success){
            this.form = res.data
          }
        })
      }else{
        this.form.erpAccountNo='';
        this.form.accountName = ''
        this.form.cardNo = ''
        this.form.bankAddr = ''
        this.form.bankName = ''
        this.form.currencyCode = ''
        this.form.organizationId = ''
        this.form.openFlag = 1
      }
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

