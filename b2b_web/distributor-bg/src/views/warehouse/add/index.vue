<template>
    <div class="wraper">
      <header v-if="!compile">
        <h4>添加仓库</h4>
        <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onRevert()"> 返回仓库列表 </el-button>
      </header>

      <header v-if="compile">
        <h4>编辑仓库</h4>
        <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onRevert()"> 返回仓库列表 </el-button>
      </header>

      <el-form ref="form" :rules="rules" :model="form" label-width="180px" class="el-form" v-loading="loading">
        <el-tooltip content="仓库编号需要同ERP中对应仓库一致，如果不一致，无法同步ERP中库存信息" placement="right">
          <el-form-item label="仓库编码" prop="warehouseNo">
            <el-input v-model="form.warehouseNo" maxlength="30"></el-input>
          </el-form-item>
        </el-tooltip>

        <el-form-item label="仓库名称" prop="name">
          <el-input v-model="form.name" maxlength="20"></el-input>
        </el-form-item>

        <el-form-item label="所属区域">
          <el-select  placeholder="请选择活动区域" value="11" v-model="form.areaId" :clearable="true">
            <el-option v-for="salesArea in salesAreas" :label="salesArea.name" :value="salesArea.id" :key="salesArea.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态"  prop="openFlag">
          <el-radio-group v-model="form.openFlag">
            <el-radio :label="1">开启</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否集成"  prop="syncType">
          <el-tooltip content="集成仓为与ERP关联的仓库，非集成仓为同ERP不关联的仓库" placement="right">
            <el-radio-group v-model="form.syncType">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="参与存销比计算"  prop="calculationType">
          <el-tooltip content="计算存销比数据时，是否获取该仓库的数量" placement="right">
            <el-radio-group v-model="form.calculationType">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="仓库描述" prop="remark">
          <el-input type="textarea" v-model="form.remark" :autosize="{ minRows: 5, maxRows: 8}" maxlength="100"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="mini-search-btn" @click="onSubmit('form')">保存</el-button>
        </el-form-item>
      </el-form>

    </div>
</template>

<script>
export default {
  name: 'warehouseAdd',
  data(){
    return {
      loading: false,
      compile: false,
      append: true,
			looking: false,
      form: {
        name: '',
        warehouseNo: '',
        areaId: '',
        isPlatform: 1,
        syncType: 1,
        calculationType:1,
        sortNo:0,
        remark: '',
        openFlag: 1
      },
      salesAreas: [],
      rules:{
        name: [ { required: true, message: '请输入仓库名称', trigger: 'blur' } ],
        warehouseNo: [ { required: true, message: '请输入仓库编码', trigger: 'blur' } ],
        // areaId: [ { required: true, message: '请选择所属区域', trigger: 'change' } ],
        isPlatform: [ { required: true, message: '请输入是否平台', trigger: 'blur' } ],
        syncType: [ { required: true, message: '请选择是否集成', trigger: 'blur' } ], 
        inMoreThan: [ { required: true, message: '请选择是否参与存销比', trigger: 'blur' } ], 
      },
    }
  },
  activated() {
    this.getSaleareas() // 所属区域
    if(this.$route.query.id) {
      this.details()
      this.compile = true;
		}else if( !this.$route.query.id){
      this.form.id = undefined
      this.form.warehouseNo = ''
			this.form.name = ''
			this.form.areaId = ''
      this.form.syncType = 1
      this.form.remark = ''
      this.form.sortNo = 0
      this.form.openFlag = 1
      this.compile = false;
		}
  },
  methods: {
    proving(){
			this.form.sortNo=this.form.sortNo.replace(/[^\.\d]/g,'');
			this.form.sortNo=this.form.sortNo.replace('.','');
			if(Number(this.form.sortNo) >100000){
				this.form.sortNo = 100000
			}
		},
    // ======== 操作 ========
    onRevert(){ // 返回仓库列表操作
      this.$router.push({ name: 'warehouseList'});
    },

    onSubmit(formName) { // 添加仓库操作
      this.$refs[formName].validate((valid) => { // 必填验证
        if (valid) {
          this.submitForm();
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    submitForm(){ // 确认操作
      this.loading = true;
      if(this.compile == true) {
          this.form.id = this.$route.query.id
          this.$http.editWarehouse(this, this.form).then(res => {  
            if(res.success) {
              this.loading = false;
              this.$message({
                message: '修改成功',
                type: 'success',
                duration: 3 * 1000,
                onClose: () => {}
              })
              this.$router.push({ name: 'warehouseList'})
            }else {
               this.loading = false;
              this.$message({ 
                message: res.msg,
                type: 'warning',
                duration: 3 * 1000,
              })
            }
          })
      }else {
          this.$http.addWarehouse(this, this.form).then(res => {  
            if(res.success) {
              this.$message({
                message: "添加成功",
                type: "success",
                duration: 3 * 1000,
                onClose: () => {}
              });
              this.$router.push({ name: 'warehouseList'})
            }
            this.loading = false;
          })
      }
    },

    // ======== 数据 ========
    details() { // 仓库详情
      this.loading = true;
      this.$http.warehouseDetail(this, { id: this.$route.query.id }).then(res => {
        if (res.success) {
          this.form = res.data
          if(this.form.areaId === 0){
            this.form.areaId =''
          } else {
            this.form.areaId = parseInt(res.data.areaId)
          }
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },
    
    getSaleareas(){  // 销售区域列表，所属区域下拉框数据来源
      this.$http.getSalesareaPoList(this, { page:1, size:10000, openFlag: 1}).then(res => {
        if (res.success) {
          this.salesAreas = res.data.list
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  $lineHight: 40px;
  $lakeBlue: rgb(33,184,203);
  .wraper{
    background-color: white;
    padding-bottom: 100px;
    position: relative;
     header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
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
    .el-form{
      max-width: 600px;
      margin-top: 30px;
    }
  }
</style>
