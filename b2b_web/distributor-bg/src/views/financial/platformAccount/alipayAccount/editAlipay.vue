<template>
  <div class="alipay-add-edit">
      <header>
		<h4 v-if="isEdit">查看支付宝账户</h4>
		<h4 v-else>添加支付宝账户</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回支付宝账户列表
		</el-button>
	    </header>
        <el-form ref="form" :model="form" label-width="180px" class="el-form1" :rules="rules">
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
            <el-form-item label="支付宝公钥：" prop="appPublicSecret">
                <el-input size="mini" style="width:600px;" :rows="5" v-model.trim="form.appPublicSecret" type="textarea"></el-input>
            </el-form-item>
            <el-form-item label="应用私钥：" prop="appPrivateSecret">
                <el-input size="mini" style="width:600px;" :rows="7"  v-model.trim="form.appPrivateSecret" type="textarea"></el-input>
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
            organizationList: [], // 所属组织列表
            form: {
                accountName: '',
                erpAccountNo:'',
                appId: '',
                appPublicSecret:'',
                appPrivateSecret: '',
                organizationId: '',
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
                appId: [{
                    required: true,
                    message: '请输入AppId',
                    trigger: 'blur'
                }],
                appPublicSecret: [{
                    required: true,
                    message: '请输入支付宝公钥',
                    trigger: 'blur'
                }],
                appPrivateSecret: [{
                    required: true,
                    message: '请输入应用私钥',
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
                         this.$http.editPlatAccountAlipay(this, this.form).then(res => {        
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
                        this.$http.addPlatAccountAlipay(this, this.form).then(res => {        
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
            this.$router.push({name:'alipayList'})
        },
        getParams(){
            this.isEdit = false
            if(this.$route.query.id != undefined){
                this.isEdit = true
                this.$http.platAccountAlipayDetail(this, {id:this.$route.query.id}).then(res => {        
                    if(res.success){
                        this.form = res.data
                        // this.form.organizationId = res.data.organizationId.toString()
                    }
                })
            }else{
                this.form.accountName = '';
                this.form.erpAccountNo='';
                this.form.appId = ''
                this.form.appPublicSecret = ''
                this.form.appPrivateSecret = ''
                this.form.bankNo = ''
                this.form.organizationId = ''
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
.el-form2{
    width: 400px;
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
    // font-size: 200px;
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
</style>

