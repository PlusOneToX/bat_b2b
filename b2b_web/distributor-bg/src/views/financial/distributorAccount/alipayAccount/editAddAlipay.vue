<template>
  <div class="alipay-add-edit">
      <header>
		<h4 v-if="isEdit">查看支付宝账户</h4>
		<h4 v-else>添加支付宝账户</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回支付宝账户列表
		</el-button>
	    </header>
        <el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item label="应用名称：" prop="accountName">
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
        <select-distributor :isFinancial="2" :distributorData="distributorData" ref="selectDistributor" @cancel="cancelDialog" @submit="disSubmitDialog"> </select-distributor>
    </el-dialog>

    <div class="clearfix footbtn">
        <el-button style="margin-left:25%;"  size="mini" @click="cancel">返回</el-button>
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
                accountName: '',
                appId: '',
                appPublicSecret:'',
                appPrivateSecret: '',
                distributorInfos:[],
            },
            distributorData:[],
            distributorShow:false,
            rules: {
                accountName: [{
                    required: true,
                    message: '请输入应用名称',
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
                distributorInfos: [{
                    type: 'array',
                    required: true,
                    message: '请至少选择一个分销商',
                    trigger: 'blur'
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
                         this.$http.editAccountAlipay(this, this.form).then(res => {        
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
                        this.$http.addAccountAlipay(this, this.form).then(res => {        
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
            this.$router.push({name:'alipayAccountList'})
        },
        getParams(){
            this.isEdit = false
            if(this.$route.query.id != undefined){
                this.isEdit = true
                this.$http.accountAlipayDetail(this, {appId:this.$route.query.id}).then(res => {        
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
                this.form.appId = ''
                this.form.appPublicSecret = ''
                this.form.appPrivateSecret = ''
                this.form.distributorInfos = []
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

