<template>
  <div class="department-add-edit">
      <header>
		<h4 v-if="isEdit">查看销售部门</h4>
		<h4 v-else>添加销售部门</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回销售部门列表
		</el-button>
	</header>
        <el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
            <el-form-item label="销售部门ID：" prop="erpDepartmentId">
                <el-tooltip content="销售部门ID需同ERP中的销售部门ID一致，如果不一致无法同步信息" placement="right">
                    <el-input v-model.trim="form.erpDepartmentId" maxlength="25" placeholder="不超过25个字"></el-input>
                </el-tooltip>
            </el-form-item>
            <el-form-item label="销售部门名称：" prop="departmentName">
                <el-input v-model.trim="form.departmentName" maxlength="50" placeholder="不超过50个字"></el-input>
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
            <el-form-item label="是否销售部门：" prop="saleType">
                <el-radio-group v-model="form.saleType" >
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="销售部门描述：" prop="description">
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
  export default {
    data() {
      return {
        form: {
          erpDepartmentId: '',
          organizationId:'',
          departmentName: '',
          saleType:1,
          description: '',
        },
        rules: {
            erpDepartmentId: [{
                required: true,
                message: '请输入销售部门ID',
                trigger: 'blur'
            }],
            organizationId: [{
                required: true,
                message: '请选择销售组织',
                trigger: 'blur'
            }],
            name: [{
                required: true,
                message: '请输入销售部门名称',
                trigger: 'blur'
            }]
        },
        rowData: {},
        saleOrganizationList:[],
        isEdit:false
      }
    },
    created(){
        this.$http.getOrganizationList(this, {page:1,size:1000}).then(res => {
          let ary = [];
          res.data.list.forEach(item => {
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
            this.rowData.erpDepartmentId = this.form.erpDepartmentId
            this.rowData.departmentName = this.form.departmentName
            this.rowData.saleType = this.form.saleType
            this.rowData.organizationId = this.form.organizationId
            this.rowData.description = this.form.description
        }
        if(this.isEdit){
            this.$http.editDepartment(this, this.rowData).then(res => {    
                if(res.success) {
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
            this.$http.addDepartment(this, this.rowData).then(res => {    
                if(res.success) {
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
        this.$router.push({name:'saleDepartment'})
      },
      getParams(){
        this.isEdit = false
        if(this.$route.params.id != undefined){
            this.$http.departmentDetail(this, { id: this.$route.params.id }).then(res => {
                if(res.success){
                    this.rowData = res.data
                    if(this.rowData !== undefined){
                        this.isEdit = true
                        this.form.erpDepartmentId = this.rowData.erpDepartmentId;
                        this.form.departmentName = this.rowData.departmentName;
                        this.form.saleType = this.rowData.saleType
                        this.form.description = this.rowData.description;
                        this.form.organizationId = this.rowData.organizationId;
                    }else{
                        this.rowData.id = undefined;
                        this.form.erpDepartmentId = "";
                        this.form.departmentName = "";
                        this.form.saleType = 1
                        this.form.description = "";
                        this.form.organizationId ="";
                    }
                }
            })
        }else{
            this.rowData.id = undefined;
            this.form.erpDepartmentId = "";
            this.form.departmentName = "";
            this.form.saleType = 1
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

