<template>
  <div class="wx-add-edit">
      <header>
		<h4 v-if="isEdit">查看微信支付账户</h4>
		<h4 v-else>添加微信支付账户</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回微信支付账户列表
		</el-button>
	</header>
    <el-form ref="form" :model="form" label-width="180px" class="el-form1" :rules="rules">
        <el-form-item label="API版本: " prop="version">
          <el-select size="mini"  v-model="form.version" class="select-box">
            <el-option label="V2" value="V2"></el-option>
            <el-option label="V3" value="V3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="ERP银行账号：" prop="bankNo">
            <el-input size="mini" style="width:300px;" v-model.trim="form.bankNo"></el-input>
        </el-form-item>
        <el-form-item label="ERP编码：" prop="erpAccountNo">
            <el-input size="mini" style="width:300px;" v-model.trim="form.erpAccountNo"></el-input>
        </el-form-item>
        <el-form-item label="所属组织: " prop="organizationId">
          <el-select size="mini"  v-model="form.organizationId">
           <el-option
            v-for="item in organizationList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        </el-form-item>
        <el-form-item label="收款账户名称" prop="accountName">
            <el-input size="mini" style="width:300px;" v-model.trim="form.accountName"></el-input>
        </el-form-item>
        <el-form-item label="AppId：" prop="appId">
          <el-input size="mini" style="width:300px;" v-model.trim="form.appId"></el-input>
        </el-form-item>
        <el-form-item label="Appkey：" prop="appKey">
          <el-input size="mini" style="width:300px;" v-model.trim="form.appKey" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="商户号：" prop="accountNo">
            <el-input size="mini" style="width:300px;" v-model.trim="form.accountNo"></el-input>
        </el-form-item>
        <el-form-item label="商户证书序列号: " prop="serialNumber" v-if="form.version==='V3'">
            <el-input size="mini" style="width:300px;" v-model.trim="form.serialNumber"></el-input>
        </el-form-item>
        <el-form-item label="商户API证书私钥: " prop="apiclientKey" v-if="form.version==='V3'">
            <el-input size="mini" style="width:600px;" v-model.trim="form.apiclientKey" type="textarea" :rows="7"></el-input>
        </el-form-item>
        <el-form-item label="商户证书失效时间: " prop="certificateInvalidTime" v-if="form.version==='V3'">
          <el-date-picker
            style="width:300px;"
            v-model="form.certificateInvalidTime"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="应用类型:" prop="appType">
          <el-radio-group v-model="form.appType">
              <el-radio :label="1">微信公众号</el-radio>
              <el-radio :label="2">微信小程序</el-radio>
          </el-radio-group>
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
        <el-button style="margin-left:25%;"  size="mini" @click="cancel">返回</el-button>
        <el-button class="mini-search-btn" @click="submitForm('form')" >保存</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        version: '',
        bankNo: '',
        erpAccountNo:'',
        organizationId: '',
        accountName: '',
        accountNo:'',
        serialNumber: '',
        apiclientKey: '',
        certificateInvalidTime: '',
        appId: '',
        appKey: '',
        appType: 1,
        backType: 1,
        openFlag: 1
      },
      rules: {
        version: [{
            required: true,
            message: '请选择API版本',
            trigger: 'change'
        }],
        bankNo: [{
            required: true,
            message: '请输入erp银行账号',
            trigger: 'blur'
        }],
        erpAccountNo:[{
            required: true,
            message: '请输入erp编码',
            trigger: 'blur'
        }],
        organizationId: [{
            required: true,
            message: '请选择所属组织',
            trigger: 'blur'
        }],
        accountName: [{
            required: true,
            message: '请输入收款账户名称',
            trigger: 'blur'
        }],
        accountNo: [{
            required: true,
            message: '请输入商户号',
            trigger: 'blur'
        }],
        serialNumber: [{
            required: true,
            message: '请输入商户证书序列号',
            trigger: 'blur'
        }],
        apiclientKey: [{
            required: true,
            message: '请输入商户API证书私钥',
            trigger: 'blur'
        }],
        appId: [{
            required: true,
            message: '请输入AppId',
            trigger: 'blur'
        }],
        appKey: [{
            required: true,
            message: '请输入appKey',
            trigger: 'blur'
        }]
      },
      isEdit:false,
      organizationList: [], // 所属组织列表
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
    submitForm(form){
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.handleSave();
        } else {
          return false;
        }
      });
    },
    handleSave(){
      this.$refs['form'].validate(valid => {
        if(valid){
          if(this.isEdit){
              this.$http.editPlatAccountWx(this, this.form).then(res => {    
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
            this.$http.addPlatAccountWx(this, this.form).then(res => {    
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
    cancel(){
      this.$router.push({name:'wxPayList'})
    },
    getParams(){
        this.isEdit = false
        if(this.$route.query.id != undefined){
          this.isEdit = true
          this.$http.platAccountWxDetail(this, {id:this.$route.query.id}).then(res => {    
            if(res.success){
              this.form = res.data
            }
          })
        }else{
          this.form.accountName = ''
          this.form.accountNo = ''
          this.form.erpAccountNo='';
          this.form.apiclientKey = ''
          this.form.serialNumber = ''
          this.form.certificateInvalidTime = ''
          this.form.appId = ''
          this.form.appKey = ''
        }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.wx-add-edit {
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
  .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
  }
}
.el-form1{
    width: 800px;
    margin-top: 30px;
    padding-left: 20px;
}
.file{ 
    position: absolute;
    width: 290px;
    height: 40px;
    top: 0;
    opacity: 0;
    padding: 0px;
    filter: alpha(opacity=0);
    cursor: pointer
}
.file-name{
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
.select-box{
  width:300px;
  /deep/.el-input--suffix{
    width:300px !important;
  }
}
</style>

