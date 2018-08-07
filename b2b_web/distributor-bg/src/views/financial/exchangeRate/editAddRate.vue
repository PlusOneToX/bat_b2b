<template>
  <div class="department-add-edit">
      <header>
		<h4 v-if="isEdit">查看汇率</h4>
		<h4 v-else>添加汇率</h4>
		<el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回汇率列表
		</el-button>
	</header>
        <el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
            <el-form-item label="原币：" prop="cyForid">
                <el-select :disabled="isEdit" v-model="form.cyForid" placeholder="请选择">
                    <el-option
                    v-for="item in currencyList"
                    :key="item.currencyCode"
                    :label="item.currencyCode"
                    :value="item.currencyCode">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="目标币：" prop="cyToid">
                <el-select :disabled="isEdit" v-model="form.cyToid" placeholder="请选择">
                    <el-option
                    v-for="item in currencyList"
                    :key="item.currencyCode"
                    :label="item.currencyCode"
                    :value="item.currencyCode">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="直接汇率：" prop="exchangeRate">
                <el-tooltip content="注意:最大支持4位小数位" placement="right">
                    <el-input :disabled="isEdit" style="width:150px;" @input="form.exchangeRate=/^\d+\.?\d{0,4}$/.test(form.exchangeRate)||form.exchangeRate == '' ? form.exchangeRate:Number(form.exchangeRate.toString().match(/^\d+(?:\.\d{0,4})?/))" v-model.trim="form.exchangeRate" type="number"></el-input>
                </el-tooltip>
            </el-form-item>
            <el-form-item label="间接汇率：" prop="reverseExRate">
                <el-tooltip content="注意:最大支持4位小数位" placement="right">
                    <el-input :disabled="isEdit" style="width:150px;" @input="form.reverseExRate=/^\d+\.?\d{0,4}$/.test(form.reverseExRate)||form.reverseExRate == '' ? form.reverseExRate:Number(form.reverseExRate.toString().match(/^\d+(?:\.\d{0,4})?/))" v-model.trim="form.reverseExRate" type="number"></el-input>
                </el-tooltip>
            </el-form-item>
            <el-form-item label="开始生效时间：" prop="begDate">
                <el-date-picker
                    :disabled="isEdit"
                    v-model="form.begDate"
                    type="datetime"
                    placeholder="选择日期"
                    value-format="timestamp">
                </el-date-picker>
            </el-form-item>
        </el-form>

    <div class="clearfix footbtn">
        <el-button style="margin-left:25%;"  size="mini" @click="cancel">返回</el-button>
        <el-button  v-show="!isEdit"   class="mini-search-btn" @click="submitForm('form')" >保存</el-button>
    </div>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        form: {
          cyForid: '',
          cyToid:'',
          exchangeRate: '',
          reverseExRate:'',
          begDate: new Date().getTime(),
        },
        rules: {
            cyForid: [{
                required: true,
                message: '请输入原币',
                trigger: 'blur'
            }],
            cyToid: [{
                required: true,
                message: '目标币',
                trigger: 'blur'
            }],
            exchangeRate: [{
                required: true,
                message: '请输入直接汇率',
                trigger: 'blur'
            }],
            reverseExRate: [{
                required: true,
                message: '请输入间接汇率',
                trigger: 'blur'
            }],
            begDate: [{
                required: true,
                message: '请输入开始生效时间',
                trigger: 'blur'
            }]
        },
        isEdit:false,
        currencyList: [] // 币种列表
      }
    },
    created(){
        this.$http.currencyList(this, {page:1,size:1000}).then(res => {      
            if (res.success) {
                this.currencyList = res.data.list;
            }
        })
        this.getParams();
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
        this.$refs['form'].validate(valid => {
            if(valid){
                this.$http.addCurrencyRate(this, this.form).then(res => { 
                    if (res.success) {
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
        })
      },
      cancel(){
        this.$router.push({name:'exchangeRate'})
      },
      getParams(){
        this.isEdit = false
        if(this.$route.query.id != undefined){
            this.isEdit = true
            this.$http.currencyRateDetail(this, {id:this.$route.query.id}).then(res => {   
                if(res.success){
                    this.form = res.data
                    this.form.begDate = new Date(this.form.begDate).getTime()
                }
            })
        }else{
            this.form.cyForid = ''
            this.form.cyToid = ''
            this.form.exchangeRate = ''
            this.form.reverseExRate = ''
            this.form.begDate = new Date().getTime()
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

