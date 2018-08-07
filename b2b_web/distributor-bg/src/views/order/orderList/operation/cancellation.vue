<template>
  <div class="order-cancel">
    <header>
      <h4>作废订单</h4>
      <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onCancel()">
        返回订单详情
      </el-button>
    </header>
    
    <el-form :model="formData" status-icon :rules="rules" label-width='20%' label-position="right" ref="formData" class="el-form">
      <el-form-item label="作废原因" prop="remark">
        <el-input type="textarea" v-model="formData.remark" :autosize="{ minRows: 5, maxRows: 8}" maxlength="1000" ></el-input>
      </el-form-item>

      <el-form-item>
        <el-col :span="2" :offset="8">
          <el-button class="mini-search-btn" @click="onConfirm('formData')" :loading="loading">确定</el-button>
        </el-col>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
export default {
  name: 'cancellation',
  data() {
    return {
      loading: false,
      formData: {
        remark: ''
      },
      rules: {
        remark:[{required: true, message: '请输入作废原因', trigger: "blur"}]
      }
    }
  },
  methods: {
    onCancel() {
      this.$router.go(-1)
    },
    onConfirm(formData) { // 确定操作
      this.$refs[formData].validate((valid) => {
        if(valid) {
          this.$confirm('确定作废订单，是否继续？','提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }).then(_ => {
            this.loading = true;
            this.$api.put(this,'admin/u/p/order/invalid', {id: this.$route.query.id,remark: this.formData.remark}).then(res => {
              if(res.code) {
                this.loading = false;
              }
              if(res.code == 0) {
                this.$message({
                  message: '成功作废订单',
                  type: 'success',
                  duration: 3 * 1000,
                })
                let orderid = this.$route.query.id
                this.$router.go(-1)
              }
            })
          }).catch(_ => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .order-cancel {
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
    .el-form-item{
      padding: 15px 0;
    }
    .el-form{
      width: 900px;
      margin-top: 30px;
    }
  }
</style>
