<template>
  <div>
    <div class="manage-add" > <!-- v-show="pageState == 'addpaymanage'" -->
      <div class="pay-list">
        <div>
          <header>
            <div style="margin-left:30px;">新增配送方式</div>
          </header>
        </div>
      </div>
      <div class="addfunction">
        <el-form label-width='20%' label-position="left">
          <el-row>
              <el-col :span="18">
                <el-col> <!-- :span="20" -->
                  <el-form-item label="配送方式名称:">
                      <el-input v-model="wayname" placeholder="单行输入" size="small" ></el-input>
                  </el-form-item>
                  <el-form-item label="重量设置:">
                    <span class="span-header">首重重量:<el-input v-model="firstWeight" placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                    <span>续重单位:<el-input v-model="additionalWeight" placeholder="单行输入" size="small" class="span-heavy"></el-input>公斤</span>
                  </el-form-item>
                </el-col >

                <el-col :span="24">
                  <el-form-item label="地区费用类型:" prop="">
                    <el-radio-group v-model="isAppointArea">
                      <el-radio :label="0">统一设置</el-radio>
                      <el-radio :label="1">指定配送地区和费用</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>

                <el-col :span="24" v-if="accign">
                  <div>
                    <el-form-item  v-if="useshow" label="配送费用:">
                      <div class="cost-item">
                        <span class="span-header">首重费用:<el-input v-model="firstWeightCost" placeholder="单行输入" size="small" class="span-heavy"></el-input></span>
                        <span>续重单位:<el-input v-model="additionalWeightCost" placeholder="单行输入" size="small" class="span-heavy"></el-input></span>
                        <el-button size="small" class="use-formula" @click="useFormula">使用公式</el-button>
                      </div>
                      
                    </el-form-item>
                    <!-- 使用公式的UI -->
                    <el-form-item  v-if="addshow" label="配送费用:">
                      <div class="cost-item">
                        <span class="span-header">配送公式:<el-input v-model="formula" placeholder="单行输入" size="small" class="span-heavy"></el-input></span>
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
                    <support  v-if="show" v-for="(item, index) in arr" :count = 'index' :key="item.id" @deletes="deletess(index)" ></support>
                </el-col>

                <el-col >
                  <el-button size="small" icon="el-icon-plus" @click="appoint">为指定区域设置运费</el-button>
                </el-col>
              </el-col>

                <!-- ============ -->

                <el-col :span="24" style="margin-top:25px;">
                  <el-form-item label="排序:">
                      <el-input type="number" v-model="distributionOrder" placeholder="单行输入" size="small" onkeyup.native="this.value=this.value.replace(/\D/g,'')"></el-input>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="状态:" prop="">
                    <el-radio-group v-model="is_open">
                      <el-radio :label="1">启用</el-radio>
                      <el-radio :label="0">关闭</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="详细介绍">
                    <el-input v-model="description" type="textarea" :rows="5"></el-input>
                  </el-form-item>
                </el-col>
                <el-form ref="formData" label-width="150px">
                  <!-- 可视范围 -->
                  <managedistributor ref="distributor" :type="distributorScope" :gIds="gradeIds" :dIds="distributorIds" :departmentIds="departmentIds" :adminIds="adminIds" @change="getChange"></managedistributor>
                </el-form>

                <el-col style="margin-top: 20px;">
                  <el-col :span="2" :offset="8">
                    <el-button type="primary" @click="handleSubmit()" >
                        确定添加
                    </el-button>
                </el-col>
                <el-col :span="2" :offset="1">
                    <el-button type="info" @click="handleCancle">
                        返回
                    </el-button>
                </el-col>
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
  import managedistributor from "@/views/system/components/manageDistributor"
  export default {
    name: 'addmanage',
    props: {
      pageState: String,
    },
    components: {
      support,
      verify,
      managedistributor
    },
    data() {
      return {
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
        isAppointArea: 0,
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
        distributorScope: 1,
        gradeIds: [],
        distributorIds: [],
        departmentIds: [],
        adminIds: [],
        distributorData: [],
        count:0,
      }
    },
    methods: {
      getChange(val) { //..可视范围选中数据
        if(this.count === 0){
          this.formData.distributorScope = val.distributorType; //..可视范围
          this.formData.gradeIds = val.distributorGradeIds; //..指定分销商等级
          this.formData.departmentIds = val.distributorDepartmentIds // 指定部门
          this.formData.adminIds = val.distributorAdminIds // 指定业务员
          this.formData.distributorIds = val.distributorIds; //..指定分销商用户ID
          this.distributorData = val.distributorData;
        }else {
          this.count--
        }
      },
      // 使用公式
      useFormula() {
        this.addshow = true;
        this.useshow = false;
        // this.firstWeightCost = ""; // 首重费用
        // this.additionalWeightCost = ""; // 续重费用
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
        // this.$emit('verifyChange')
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
      deletess(index) {
        this.arr.splice(index, 1);
        // productGroup
      },
      // 添加配送方式
      handleSubmit() {
        this.count = 4
        var product = {
          firstWeightCost: parseInt(this.firstWeightCost), // 首重费用,未使用公式时必填
          additionalWeightCost: parseInt(this.additionalWeightCost), // 续重费用,未使用公式时必填
          isDefault: this.startType, // 是否默认费用 0否, 1是
          isFormulathis: this.isFormula, // 是否使用公式 0否, 1是
          formula: this.formula,// 公式,使用公式必填
          formula: this. startTransforms, // 使用配送公式
          // provinceId: , // 省份ID isAppointArea=1且isDefault=0必填
          // cityId: , // 城市ID
          // districtId: , // 地区ID
          firstWeightCost: this.startfirstWeight,  // 启用默认费用时的首重费用
          additionalWeightCost: this.startadditionalWeightCost // 启用默认费用时的续重费用
        }
        this.areasCost.push(product)
        this.$http.addLogistics(this, {  
          name: this.wayname, // 名称(String)
          firstWeight: this.firstWeight, // 首重(Number)
          additionalWeight: this.additionalWeight, // 续重(Number)
          distributionOrder: this.distributionOrder, // 排序(Number)
          description: this.description, //描述,说明(String)
          areasCost: this.areasCost, // 配送地区费用
          isAppointArea: this.isAppointArea, // 是否指定地区 values: 0统一设置, 1指定地区
          is_open: this.is_open, // 是否启用 values: 0未启用, 1启用
        }).then(res => {
          if(res.success) {
            this.$message.success({
                message: '成功添加快递配送方式',
                duration: 3 * 1000,
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
      isAppointArea: function () {
        if(this.isAppointArea == 1) {
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
    header{
      color: #fff;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
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