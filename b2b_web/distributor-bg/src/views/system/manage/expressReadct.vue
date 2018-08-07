<template>
  <div>
    <div class="manage-add" >
      <div class="pay-list">
        <header>
          <h4>编辑配送方式</h4>
          <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="handleCancle">
            返回配送方式列表
          </el-button>
        </header>
      </div>
      <div class="addfunction">
        <el-form label-width='20%' label-position="left">
          <el-row>
              <el-col :span="18">
                <el-col>
                  <el-form-item label="配送方式名称:">
                      <el-input v-model="tableData.name" placeholder="单行输入" size="small" ></el-input>
                  </el-form-item>
                  <el-form-item label="重量设置:">
                    <span class="span-header">首重重量:<el-input v-model="tableData.firstWeight" placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                    <span>续重单位:<el-input v-model="tableData.additionalWeight" placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                  </el-form-item>
                </el-col >

                <el-col :span="24">
                  <el-form-item label="地区费用类型:" prop="">
                    <el-radio-group v-model="tableData.isAppointArea">
                      <el-radio :label="0">统一设置</el-radio>
                      <el-radio :label="1">指定配送地区和费用</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>

                <el-col :span="24" v-if="accign">
                  <div>
                    <el-form-item  v-if="useshow" label="配送费用:">
                      <div class="cost-item">
                          <!-- v-for="item in brandlist" :key="item.firstWeightCost" :value="item.firstWeightCost" -->
                        <span class="span-header">首重费用:<el-input v-model="tableData.areasCost[0].firstWeightCost" placeholder="单行输入" size="small" class="span-heavy"></el-input></span>
                        <!-- v-for="item in brandlist" :key="item.additionalWeightCost" :value="item.additionalWeightCost" -->
                        <span>续重单位:<el-input v-model="tableData.areasCost[0].additionalWeightCost"   placeholder="单行输入" size="small" class="span-heavy"></el-input></span>
                        <el-button size="small" class="use-formula" @click="useFormula">使用公式</el-button>
                      </div>
                      
                    </el-form-item>
                    <!-- 使用公式的UI -->
                    <el-form-item  v-if="addshow" label="配送费用:">
                      <div class="cost-item">
                        <span class="span-header">配送公式:<el-input v-for="item in brandlist" :key="item.formula" :value="item.formula"  placeholder="单行输入" size="small" class="span-heavy"></el-input></span>
                        <el-button size="small" class="use-formula" @click="verifyFormula">验证公式</el-button>
                        <el-button size="small" class="use-formula" @click="cancelFormula">取消公式</el-button>
                      </div>
                    </el-form-item>
                  </div>
                </el-col>


                <!-- 指定配送地区和费用  == 块 == -->
                <el-col v-if="assign">
                  <el-col >
                  <el-form-item>
                    <el-radio-group v-model="startType" style="width:100%">
                      <el-radio :label="1">启用默认费用</el-radio>
                      <el-radio :label="0">取消启用默认费用</el-radio>                  
                    </el-radio-group>
                    <span class="place-holder">注意: 未启用默认费用时，不在指定配送地区的顾客不能使用本配送方式下单</span>
                  </el-form-item>
                </el-col>

                <el-col :span="24" v-if="startUsing">
                  <div>
                    <el-form-item  v-if="ashow" label="配送费用:">
                      <div class="cost-item">
                        <span class="span-header">首重费用:<el-input v-model="startfirstWeight"  placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                      <span>续重费用:<el-input v-model="startadditionalWeightCost" placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                      <el-button size="small" class="use-formula" @click="aFormula">使用公式</el-button>
                      </div>
                    </el-form-item>

                    <el-form-item  v-if="bshow" label="配送费用:">
                      <div class="cost-item">
                        <span class="span-header">配送公式:<el-input v-model="startTransforms" placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                        <el-button size="small" class="use-formula" @click="verifyFormula">验证公式</el-button>
                        <el-button size="small" class="use-formula" @click="bFormula">取消公式</el-button>
                      </div>
                    </el-form-item>
                  </div>
                </el-col>


                <el-col :span="24">
                  <el-form-item label="支持的配送地区"></el-form-item>
                </el-col>

                <!-- 添加为指定地区设置运费组件 -->
                <el-col :span="24">
                    <support  v-if="show" v-for="item in arr" :key="item.id" @deletes="deletess(item)"></support>
                </el-col>

                <el-col >
                  <el-button size="small" icon="el-icon-plus" @click="appoint">为指定区域设置运费</el-button>
                </el-col>
                </el-col>

                <!-- ============ -->

                <el-col :span="24" style="margin-top:25px;">
                  <el-form-item label="排序:">
                      <el-input v-model="tableData.distributionOrder" placeholder="单行输入" size="small" ></el-input>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="状态:" prop="">
                    <el-radio-group v-model="tableData.isOpen">
                      <el-radio :label="1">启用</el-radio>
                      <el-radio :label="0">关闭</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="详细介绍">
                    <el-input v-model="tableData.description" type="textarea" :rows="5"></el-input>
                  </el-form-item>
                </el-col>

                <el-col style="margin-top: 20px;">
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
                
                 
                 

              </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
    
    <!-- 引用组件 -->
    <verify v-if="pageState == 'addVerify'"
    @goback="goback"
    :pageState="pageState"
    ></verify>

  </div>
</template>

<script>
  import support from "@/views/system/manage/support";
  import verify from "@/views/system/manage/verify";
  import eventBus from "@/views/order/eventBus";
  export default {
    name: 'expressReadct',
    props: {
      pageState: String,
    },
    components: {
      support,
      verify
    },
    data() {
      return {
        tableData: [],
        brandlist: [],
        rowData: "",
        accign: true,
        isFormula: 0,
        show: false,
        addshow: false,
        useshow: true,
        ashow: true,
        bshow: false,
        cshow: true,
        dshow: false,
        assign: false, // 指定配送地区和费用部分
        wayname: '', // 配送方式名称
        mytime: '',
        payTime: '',
        isAppointArea: "",
        startType: "",
        is_open: '',
        arr: [],
        startTransforms: '', // 使用配送公式的公式
        startUsing: false,
        description: '',  // 描述,说明(String)
        firstWeight: '', // 首重(Number)
        additionalWeight: '', // 续重(Number)
        distributionOrder: '', // 排序(Number)
        areasCost: [], // 配送地区费用(Array)
        firstWeightCost: '', // 首重费用(Number)
        additionalWeightCost: '', // 续重费用(Number)
        formula: '', // 公式(String)
        startfirstWeight: '', // 启用默认费用时的首重费用
        startadditionalWeightCost: '', // 启用默认费用时的续重费用
      }
    },
    activated() {
      this.distribution()
    },
    // mounted() {
    //     this.distribution()
    // },
    created() {
      this.rowData = this.$route.query.row
    },
    methods: {
      // 使用公式
      useFormula() {
        this.addshow = true;
        this.useshow = false;
        this.isFormula = 1;
      },
      // 取消公式
      cancelFormula() {
        this.addshow = false;
        this.useshow = true;
        this.isFormula = 0;
      },
      // 验证公式
      verifyFormula() {
        this.$router.push({name: 'verify'})
      },
      goback() {
        this.pageState = 'addpaymanage'
      },
      aFormula() {
        this.ashow = false;
        this.bshow = true;
        this.startfirstWeight = "";
        this.startadditionalWeightCost = "";
      },
      bFormula() {
        this.ashow = true
        this.bshow = false
      },
      cFormula() {
        this.dshow = true
        this.cshow = false
      },
      dFormula() {
        this.dshow = false
        this.cshow = true
      },
      appoint() {
        this.show = true;
        var addManObject = {};
        this.arr.push(addManObject)
      },
      deletess(item) {
        this.arr.splice(this.arr.indexOf(item), 1);
      },
      // 详情数据distribution
      distribution() {
          let query = this.$route.query;
          this.$http.logisticsDetail(this, {id: query.id}).then(res => {
              this.tableData = res.data
              let ary = [];
              this.tableData.areasCost.forEach(item => {
                ary.push(item)
              })
              this.brandlist = ary;
              
          })
      },
      // 添加配送方式
      handleSubmit() {
        var product = {
            firstWeightCost: this.brandlist.map(val => val.firstWeightCost),  // 首重费用,未使用公式时必填
            firstWeightCost: parseInt(this.tableData.areasCost[0].firstWeightCost),
            additionalWeightCost: parseInt(this.tableData.areasCost[0].additionalWeightCost),
        //   firstWeightCost: parseInt(this.firstWeightCost), 
        //   additionalWeightCost: parseInt(this.additionalWeightCost), // 续重费用,未使用公式时必填
        //   isDefault: this.startType, // 是否默认费用 0否, 1是
        //   isFormulathis: this.isFormula, // 是否使用公式 0否, 1是
        //   formula: this.formula,// 公式,使用公式必填
        //   formula: this. startTransforms, // 使用配送公式
        //   // provinceId: , // 省份ID isAppointArea=1且isDefault=0必填
        //   // cityId: , // 城市ID
        //   // districtId: , // 地区ID
        //   firstWeightCost: this.startfirstWeight,  // 启用默认费用时的首重费用
        //   additionalWeightCost: this.startadditionalWeightCost, // 启用默认费用时的续重费用

        }
        this.areasCost.push(product)
        let query = this.$route.query.row;
        this.$http.editLogistics(this, { 
          id: this.tableData.id,  // 配送id(Number)
          name: this.tableData.name, // 名称(String)
          firstWeight: this.tableData.firstWeight, // 首重(Number)
          additionalWeight: this.tableData.additionalWeight, // 续重(Number)
          distributionOrder: this.tableData.distributionOrder, // 排序(Number)
          description: this.tableData.description, //描述,说明(String)
          areasCost: this.areasCost, // 配送地区费用
          isAppointArea: this.tableData.isAppointArea, // 是否指定地区 values: 0统一设置, 1指定地区
          is_open: this.tableData.isOpen, // 是否启用 values: 0未启用, 1启用
        }).then(res => {
          if(res.success) {
            this.$message.success({
                message: '修改成功',
                duration: 3 * 1000,duration:1*1000,
                onClose: () => { }
            })
            this.$router.push({ name: 'managelist'})
          } else {
              // this.$message.success({
              //     // message: '退款失败，请重新退款',
              //     duration: 3 * 1000,
              //     onClose: () => {
              //       // this.$router.push({ name: 'refundOrderDetail'})
              //     }
              // })
          }
        })
      },
      // 返回上个页面
      handleCancle() {
        this.$router.go(-1);
      },
    },
    watch: {
      // 是否启用默认费用监听
      startType: function () {
        if(this.startType == 1) {
          this.startUsing = true
        }else {
          this.startUsing = false
          this.startfirstWeight = "",
          this.startadditionalWeightCost = ""
        }
      },
      // 是否统一设置监听，指定配送地区和费用
      "tableData.isAppointArea": function () {
        if(this.tableData.isAppointArea == 1) {
          this.assign = true
          this.accign = false
          this.startType = 1
        }else {
          this.assign = false
          this.accign = true
        }
      }
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
    padding: 3%;
    .span-header {
      margin-right: 20px;
    }
    .span-heavy {
      width:140px;
      margin:0 15px;
    }
    .cost-item {
        background-color: $bg;
        border-radius: 10px;
        padding: 20px 20px;
      }
    .cost {
      background-color: $bg;
      border-radius: 10px;
      padding: 20px 20px;
      .cost-verify {
        margin-left: 19%;
        margin-top: 10px;
        background-color:#fff;
        border-radius: 5px;
        width:650px;
        padding:10px;
        font-size: 14px;
        line-height: 30px;
      }
    }
    
    .use-formula {
      background-color: $lakeBlue;
      color: white;
      border-radius: 5px;
    }
    .course {
      margin-bottom: 20px;
    }
    .place-holder {
      color: #bfbfbf;
      font-size: 12px;
      margin-left: 4%;
      width: 100%;
    }
  }
}
</style>