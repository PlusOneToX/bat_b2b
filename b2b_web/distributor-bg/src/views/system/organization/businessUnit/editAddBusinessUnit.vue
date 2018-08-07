<template>
  <div class="department-add-edit">
      <header>
		<h4 v-if="isEdit">查看事业部</h4>
		<h4 v-else>添加事业部</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回事业部列表
		</el-button>
	</header>
        <!-- <el-row class="header">
            <el-col :span="2">
                销售部门
            </el-col>
        </el-row> -->
        <el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
            <el-form-item label="事业部ID：" prop="erpBusinessUnitId">
                <el-tooltip content="事业部ID需同ERP中的事业部ID一致，如果不一致无法同步信息" placement="right">
                    <el-input v-model.trim="form.erpBusinessUnitId" maxlength="25" placeholder="不超过25个字"></el-input>
                </el-tooltip>
            </el-form-item>
            <el-form-item label="事业部名称：" prop="name">
                <el-input v-model.trim="form.name" maxlength="50" placeholder="不超过50个字"></el-input>
            </el-form-item>
            <el-form-item label="所属销售组织：" prop="organizationId">
                <el-select v-model="form.organizationId" placeholder="请选择销售组织">
                    <el-option
                        v-for="item in saleOrganizationList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="事业部描述：" prop="description">
                <el-input :rows="4" v-model.trim="form.description" type="textarea" maxlength="100" placeholder="不超过100个字"></el-input>
            </el-form-item>
        </el-form>

    <div class="clearfix footbtn">
        <el-button style="margin-left:25%;"  class="mini-search-btn" @click="submitForm('form')" >保存</el-button>
        <el-button size="mini" @click="cancel">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getBusinessUnit,saveBusinessUnit,editBusinessUnit,getOrganizationList } from '@/views/system/organization/organizationData'

  export default {
    data() {
      return {
        form: {
          erpBusinessUnitId: '',
          organizationId:'',
          name: '',
          description: '',
        },
        rules: {
            erpBusinessUnitId: [{
                required: true,
                message: '请输入事业部ID',
                trigger: 'blur'
            }],
            organizationId: [{
                required: true,
                message: '请选择销售组织',
                trigger: 'blur'
            }],
            name: [{
                required: true,
                message: '请输入事业部名称',
                trigger: 'blur'
            }]
        },
        rowData: {},
        saleOrganizationList:[],
        isEdit:false
      }
    },
    created(){
        getOrganizationList(this,{page:1,count:100}).then(res => {
          let ary = [];
          res.organizations.forEach(item => {
              item.children = [];
              ary.push(item);
          });
          ary.sort((a, b) => {
            return a.sort - b.sort < 0;
          });
          this.saleOrganizationList = ary;
      })
    },
    mounted() {
        this.getParams();
    },
    activated() {
        this.getParams();
    },
    methods: {
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
        if(this.form != undefined){
            this.rowData.erpBusinessUnitId = this.form.erpBusinessUnitId
            this.rowData.name = this.form.name
            this.rowData.organizationId = this.form.organizationId
            this.rowData.description = this.form.description
        }
        if(this.isEdit){
            editBusinessUnit(this,this.rowData).then(res => {
                if(res.code == 0) {
                    this.$message({
                        message: '保存成功',
                        type: 'success',
                        duration: 3 * 1000,
                        onClose: () => { }
                    })
                    this.loading = false
                    this.cancel()
                }
            })
        }else{
            saveBusinessUnit(this,this.rowData).then(res => {
                if(res.code == 0) {
                    this.$message({
                        message: '保存成功',
                        type: 'success',
                        duration: 3 * 1000,
                        onClose: () => { }
                    })
                    this.loading = false
                    this.cancel()
                }
            })
        }
      },
      cancel(){
        this.$router.push({name:'businessUnit'})
      },
      getParams(){
        this.isEdit = false
        if(this.$route.params.id != undefined){
            getBusinessUnit(this,{ids:this.$route.params.id}).then(res =>{
                if(res.code === 0){
                    this.rowData = res.businessUnits[0]
                    if(this.rowData !== undefined){
                        this.isEdit = true
                        this.form.erpBusinessUnitId = this.rowData.erpBusinessUnitId;
                        this.form.name = this.rowData.name;
                        this.form.description = this.rowData.description;
                        this.form.organizationId = this.rowData.organizationId;
                    }else{
                        this.rowData.id = undefined;
                        this.form.erpBusinessUnitId = "";
                        this.form.name = "";
                        this.form.description = "";
                        this.form.organizationId ="";
                    }
                }
            })
        }else{
            this.rowData.id = undefined;
            this.form.erpBusinessUnitId = "";
            this.form.name = "";
            this.form.description = "";
            this.form.organizationId ="";
        }
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
.department-add-edit {
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
    width: 700px;
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

