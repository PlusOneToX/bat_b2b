<template>
  <div>
    <div class="manage-add">
      <div class="pay-list">
        <header>
          <h4>公式验算对话框</h4>
          <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancle">
            返回配送方式
          </el-button>
        </header>
      </div>
      <div class="addfunction">
        <el-form label-width='10%' label-position="left">
                  <el-form-item>
                    <span style="color:#bfbfbf">您可以再这里测试配送公式</span>
                  </el-form-item>
                  <el-form-item label="配送公式:">
                    <el-input v-model="formula" placeholder="单行输入" size="small" ></el-input>
                  </el-form-item>
                  <el-form-item label="商品重量:" v-if="billingMethod===1">
                    <el-input v-model="weight" placeholder="单行输入" size="small" class="span-heavy"></el-input>克
                  </el-form-item>
                  <el-form-item label="商品体积:" v-else>
                    <el-input v-model="volume" placeholder="单行输入" size="small" class="span-heavy"></el-input>立方米
                  </el-form-item>
                   <el-form-item label="订单价格:">
                    <el-input v-model="price" placeholder="单行输入" size="small" class="span-heavy"></el-input>元
                    <el-button class="mini-search-btn" @click="verifyformula">计算</el-button>
                  </el-form-item>
                  <el-form-item label="配送费用:">
                    <span v-show="cost!==''" size="small" class="span-heavy">{{cost}}</span>元
                  </el-form-item>
        </el-form>
        <el-button style="margin-top: 20px;margin-left:47%;" size="mini" @click="handleCancle">
            返回
        </el-button>
      </div>
    </div>
    
   

  </div>
</template>

<script>
  export default {
    name: 'verify',
    activated() {
      this.formula = this.$route.params.formula
      this.billingMethod = this.$route.params.billingMethod
      this.cost = ''
      this.weight = ''
      this.volume = '', // 体积
      this.price = ''
    },
    created() {
      this.formula = this.$route.params.formula
      this.billingMethod = this.$route.params.billingMethod
      this.cost = ''
      this.weight = ''
      this.volume = '', // 体积
      this.price = ''
    },
    data() {
      return {
        formula: '', // 公式
        billingMethod: '', // 计算方式
        weight: '', // 重量
        volume: '', // 体积
        price: '', // 价格
        cost:''//计算费用
      }
    },
    methods: {
      // 计算公式
      verifyformula() {
        let formData = {}
        if (this.billingMethod === 1) {
           formData = {
             formula: this.formula, // 公式
             weight: this.weight, // 重量
             price: this.price, // 价格
           }
        } else {
          formData = {
             formula: this.formula, // 公式
             volume: this.volume, // 体积
             price: this.price, // 价格
           }
        }
        this.$http.formula(this, formData).then(res => {
          if(res.success) {
            this.$message.success({
              message: '公式验证通过',
              duration: 3 * 1000,
              onClose: () => {
                // this.$router.push({ name: 'managelist'})
              }
            })
            this.cost = res.data.cost
          }
        })
      },
      // 返回上个页面
      handleCancle() {
        this.$router.push({name : 'addEditShipping',query:{type: "verify"}})
      },
    },
    watch: {
      
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

.manage-add {
  background-color: #Fff;
  min-height: 100%;
  padding-bottom: 30px;
  .pay-list {
    header {
      color: white;
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
  .addfunction {
    border-top: 0px;
    background-color: #fff;
    padding: 2% 3% ;
    .span-heavy {
      width:140px;
      margin:0 15px 0 0;
    }
    .use-formula {
      background-color: $lakeBlue;
      color: white;
      border-radius: 5px;
    }
  }
}
</style>