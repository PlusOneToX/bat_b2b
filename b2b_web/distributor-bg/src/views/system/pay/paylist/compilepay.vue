<template>
  <div>
  <div class="add" v-show="pageState == 'paycompile'">
    <div class="pay-list">
      <header>
        <h4>查看收款条件</h4>
        <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancle">
            返回收款条件列表
          </el-button>
      </header>
    </div>
    <div class="compileFunction">
    <el-form label-width='20%' label-position="left">
      <el-row>
        <el-col :span="18">
        <el-col :span="18">
          <el-form-item label="支付方式名称:">
            <el-input v-model="particulars.name"></el-input>
          </el-form-item>
           <el-form-item label="结算时长:">
            <el-radio-group v-model="particulars.payWay">
              <el-radio :label="1">立即支付</el-radio>
              <el-radio :label="2">期间结算</el-radio>
            </el-radio-group>
            <el-input
              size="mini"
              class="couseInput"
              v-if="particulars.payWay==2"
              v-model="particulars.settlingTime"
            />
            <span class="place-holder" v-if="particulars.payWay==2">数字单位为: 天</span>
          </el-form-item>
        </el-col>
         <el-col :span="2" :offset="8">
          <el-button type="primary" @click="handleSubmit()" >
            保存
          </el-button>
        </el-col>
        <!-- <el-col :span="2" :offset="1">
          <el-button type="info" @click="handleCancle">
            返回
          </el-button>
        </el-col> -->
        </el-col>
      </el-row>
    </el-form>
    </div>

  </div>
  </div>
</template>

<script>
  export default {
  name: 'compilepay',
  props: {
    pageState: String,
    tableData: Array,
    // entryIndex: Number, // 本组件主数据在父组件数组中的index
    particulars: String, // 支付详情
  },
  data() {
    return {
      payTime: '',
      pageInfo: {
        page: 1,
        count: 10
      },
    }
  },
  computed: {
    isFormLegal(){
      if(this.particulars.payWay == 2){
        return !!this.particulars.settlingTime
      }else{
        return true
      }
    }
  },
  methods: {
    handleSubmit() {
      if(!this.isFormLegal){
        this.$message({
          message: '请输入结算时长',
          type: 'warning'
        })
        return 0
      }
      this.$api.put(this,'admin/u/p/settleAccount',{
        id: this.particulars.id,
        name: this.particulars.name,
        payWay: this.particulars.payWay,
        settlingTime: this.particulars.settlingTime
      }).then(res => {
        if(res.code == 0) {
        this.$message.success({
          message: '修改成功',
          duration: 3 * 1000,
          onClose: () => { }
        })
        this.$emit('updatepay')
        }
      })
    },
    handleCancle() {
      this.$emit('cancel')
    },
  },
  watch: {
    'particulars.payWay': function () {
      if(this.particulars.payWay == 1) {
        this.particulars.settlingTime = undefined
      }else{
        this.particulars.settlingTime = "1"
      }
    },
  }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.main {
  background-color: #fff
}
.add {
  background-color: #Fff;
  min-height: 100%;
  padding-bottom: 30px;
  .pay-list {
    header{
      color: #fff;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
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
  .compileFunction {
  border-top: 0px;
  background-color: #fff;
  padding: 3%;
  .course {
    margin-bottom: 20px;
  }
  .place-holder {
    color: #bfbfbf;
    font-size: 12px;
  }
  }
}
</style>