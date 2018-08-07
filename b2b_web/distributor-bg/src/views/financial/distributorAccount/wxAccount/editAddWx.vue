<template>
  <div class="wx-add-edit">
      <header>
		<h4 v-if="isEdit">查看微信账户</h4>
		<h4 v-else>添加微信账户</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回微信账户列表
		</el-button>
	</header>
    <el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item label="账户类型：" prop="accountType">
          <el-radio-group v-model="form.accountType">
              <el-radio :label="1">自有账户</el-radio>
              <el-radio :label="2">服务商子商户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公众号名称：" prop="accountName">
            <el-input size="mini" style="width:300px;" v-model.trim="form.accountName"></el-input>
        </el-form-item>
        <el-form-item label="商户号：" prop="accountNo" v-if="form.accountType===1">
            <el-input size="mini" style="width:300px;" v-model.trim="form.accountNo"></el-input>
        </el-form-item>
        <el-form-item label="服务商商户号：" prop="accountNo" v-if="form.accountType===2">
            <el-input size="mini" style="width:300px;" v-model.trim="form.accountNo"></el-input>
        </el-form-item>
        <el-form-item label="子商户号：" prop="subMchid" v-if="form.accountType===2">
            <el-input size="mini" style="width:300px;" v-model.trim="form.subMchid"></el-input>
        </el-form-item>
        <el-form-item label="最大分账比例：" prop="subAccountRatio" v-if="form.accountType===2">
            <el-input size="mini" style="width:300px;" v-model.trim="form.subAccountRatio"></el-input>
        </el-form-item>
        <el-form-item label="AppId：" prop="appId">
            <el-input size="mini" style="width:300px;" v-model.trim="form.appId"></el-input>
        </el-form-item>
        <el-form-item label="Appkey：" prop="appKey">
            <el-input size="mini" style="width:300px;" v-model.trim="form.appKey" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="API版本: " prop="version">
          <el-select size="mini"  v-model="form.version" class="select-box">
            <el-option label="V2" value="V2"></el-option>
            <el-option label="V3" value="V3"></el-option>
          </el-select>
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
        <el-form-item label="适用分销商：" prop="distributorInfos">
            <el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
                添加分销商
            </el-button>
            <el-table class="tableCenter" :data="distributorData" border header-row-class-name="header-row" max-height="600">
                <el-table-column align="center" label="分销商用户名" width = "150" prop="name"></el-table-column>
                <el-table-column align="center" label="公司名" prop="companyName"></el-table-column>
                <el-table-column  align="center" label="操作" width="80">
                    <template slot-scope="scope">
                        <el-button style="margin-top:0px;margin-bottom:0px;" class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index)">
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-form-item>
    </el-form>
    <el-dialog :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%" >
        <select-distributor  :appType="form.appType" :isFinancial="1" :distributorData="distributorData" ref="selectDistributor" @cancel="cancelDialog" @submit="disSubmitDialog"> </select-distributor>
    </el-dialog>
    <div class="clearfix footbtn">
        <el-button style="margin-left:25%;" size="mini" @click="cancel">返回</el-button>
        <el-button class="mini-search-btn" @click="submitForm('form')" >保存</el-button>
    </div>
  </div>
</template>

<script>
import selectDistributor from "@/views/goods/components/selectDistributorAll"
export default {
    components: { selectDistributor },
    data() {
        return {
            form: {
                accountType: 1,
                version: '',
                accountName: '',
                accountNo:'',
                subMchid: '',
                appId: '',
                appType: 1,
                serialNumber: '',
                apiclientKey: '',
                certificateInvalidTime: '',
                subAccountRatio: '',
                distributorInfos:[]
            },
            distributorData:[],
            distributorShow:false,
            rules: {
                version: [{
                    required: true,
                    message: '请选择API版本',
                    trigger: 'change'
                }],
                accountNo: [{
                    required: true,
                    message: '请输入商户号',
                    trigger: 'blur'
                }],
                subMchid: [{
                    required: true,
                    message: '请输入子商户号',
                    trigger: 'blur'
                }],
                subAccountRatio: [{
                    required: true,
                    message: '请输入最大分账比例',
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
                }],
                distributorInfos: [{
                    type: 'array',
                    required: true,
                    message: '请至少选择一个分销商',
                    trigger: 'blur'
                }],
                certificateInvalidTime:[{
                    // type: 'date',
                    required: true,
                    message: '请选择证书失效时间',
                    trigger: 'change'
                }]
            },
            isEdit:false,
        }
    },
    created(){
        this.getParams();
    },
    methods: {
        handleDeleteDistributor(index) {
            this.distributorData.splice(index, 1)
            this.form.distributorInfos.splice(index, 1)
		},
        disCancel() {
            this.$refs.selectDistributor.handleCancel()
        },
        cancelDialog(){
            this.distributorShow = false;
        },
        disSubmitDialog(msg) {
            this.form.distributorInfos = []
            this.distributorData = msg;
            if(this.distributorData !==undefined && this.distributorData !== null && this.distributorData.length > 0){
                this.distributorData.forEach(item =>{
                    this.form.distributorInfos.push({
                        distributorId: item.id,
                        distributorName: item.name,
                        distributorCompanyName: item.companyName
                    })
                })
            }
            this.distributorShow = false;
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
                         this.$http.editAccountWx(this, this.form).then(res => {    
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
                        this.$http.addAccountWx(this, this.form).then(res => {    
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
            this.$router.push({name:'wxAccountList'})
        },
        getParams(){
            this.isEdit = false
            if(this.$route.query.id != undefined){
                this.isEdit = true
                this.$http.accountWxDetail(this, {appId:this.$route.query.id,subMchid:this.$route.query.subMchid}).then(res => {    
                    if(res.success){
                        this.form = res.data
                        if(res.data.distributorInfos !==undefined && res.data.distributorInfos !== null && res.data.distributorInfos.length > 0){
                            res.data.distributorInfos.forEach(item =>{
                                this.distributorData.push({
                                    id: item.distributorId,
                                    name: item.distributorName,
                                    companyName: item.distributorCompanyName
                                })
                            })
                        }
                    }
                })
            }else{
                this.form.accountName = ''
                this.form.accountNo = ''
                this.form.apiclientKey = ''
                this.form.serialNumber = ''
                this.form.certificateInvalidTime = ''
                this.form.appId = ''
                this.form.distributorInfos = []
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

