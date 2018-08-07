<template>
  <div class="promotion">
    <div class="sales-promotion">
      <header>
        <h4>统一政策审批单</h4>
        <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onCancel()"> 返回 </el-button>
      </header>
    </div>
    <!-- <el-row class="header">
      <el-col class="title">
        活动可协助代销商更好的推广，带来更多的销量，当客户一次性购满设定的金额，可享有折扣优惠。
      </el-col>
    </el-row> -->
    <div class="box-has-info">
			<div class="half-width">
				<el-form ref="recordDetail">
					<el-form-item label="审批单号:"> {{checkData.id}} </el-form-item>
					<el-form-item label="审批类型:"> {{extFormatter(checkData.ext)}} </el-form-item>
				</el-form>
			</div>

			<div class="half-width">
				<el-form ref="recordDetail" label-width="190px">
					<el-form-item label="发起时间:"> {{timeFormatter(checkData.createTime)}} </el-form-item>
					<el-form-item label="审批状态:"> {{formatStatus(0,0,checkData.status)}} </el-form-item>
				</el-form>
			</div>
		</div>

    <div class="promotion_container">
      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini">
        <el-form-item label="活动名称:" class="name" prop="policyName">
          <el-input disabled v-model="formData.policyName" placeholder="不超过10个字" maxlength="10" />
        </el-form-item>

        <el-form-item label="规则描述:" prop="description">
          <el-input disabled type="textarea" v-model="formData.description" />
        </el-form-item>

        <el-form-item label="启用状态:">
          <el-radio-group disabled  v-model="formData.enable">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="2">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="活动时间:" prop="time">
          <el-radio-group disabled v-model="formData.timeType">
            <el-radio :label="1">长期</el-radio>
            <el-radio :label="2">具体时间范围
              <template>
                  <el-date-picker disabled v-show="formData.timeType === 2" v-model="formData.time" type="datetimerange" 
                  value-format="timestamp" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
              </template>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form2" :rules="rules" size="mini">
        <el-form-item label="统一政策:">
          <div class="pick_rule">
            <div class="pick_details">当月所有下单实际总额满足活动条件时，下月每笔订单可享有设定的折扣政策</div>
            <div class="pick_details">下单总额是否包含在途订单
              <el-radio-group disabled  v-model="formData.includeOrderOnWay" class="pick_if">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="2">否</el-radio>
              </el-radio-group>
            </div>

            <div class="add_rule">
              <div v-if="totalCount>0" class="add_particulars">
                <div class="box_has_info" v-for="(rule,index) in formData.rules" :key="rule.id">
                  <div class="half_left">

                    <div class="left_header">
                      <div class="left_content_one">当月下单总额</div>
                      <div class="left_content_two"> > </div>
                      <div class="left_content_three">前 
                        <el-input class="left_content_input" disabled v-model="rule.monthNum" @input="rule.monthNum=/^\d+\.?\d{0,0}$/.test(rule.monthNum)||rule.monthNum == '' ? rule.monthNum:Number(rule.monthNum.toString().match(/^\d+(?:\.\d{0,0})?/))" size="mini" /> 个月平均下单总额
                      </div>

                      <div class="left_content_type1">折扣类型
                        <el-tooltip content="还没有提示" effect="light" placement="top" >
                          <span class = "el-icon-question question-color"></span>
                        </el-tooltip> 
                      </div>
                      
                      <div class="left_content_type2">
                        <el-radio-group v-model="rule.isDiscount" disabled>
                          <el-radio :label="1" >打折</el-radio>
                          <el-radio :label="2" >减免</el-radio>
                        </el-radio-group>
                        <el-input class="left_content_discount1" v-show="rule.isDiscount === 1" v-model="rule.discount"  @keyup.native="proving(index)" :disabled="disabled" />
                        <span v-show="rule.isDiscount === 1"> %</span>
                        <el-input class="left_content_discount2" v-show="rule.isDiscount === 2" v-model="rule.reduction" @input="rule.reduction=/^\d+\.?\d{0,2}$/.test(rule.reduction)||rule.reduction == '' ? rule.reduction:Number(rule.reduction.toString().match(/^\d+(?:\.\d{0,1})?/))" :disabled="disabled" type="number" min="0" step="0.01" />
                        <span class="left_content_span" v-show="rule.isDiscount === 2"> 元</span>
                      </div>
                    </div>

                    <!-- 折扣范围，选择分销商 -->
                    <div class="left_content_assign">
                      <distributor ref="distributor" :index="index" :type="rule.distributorScope" :gIds="rule.distributorGradeIds" :dIds="rule.distributorIds" @change="getChange" disabled></distributor>
                    </div>
                  </div>
                  <div class="half-right">
                    <!-- <div style="float: left; background-color :#ccc; width:1px; height: 100%;"></div> -->
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <div class="promotion_flow" >
      <check-procedure :flows="checkData.flows" v-if="checkData.flows.length>0"></check-procedure>
    </div>

    <el-form ref="formData" :model="formData" label-width="100px" class="el-form3" :rules="rules" size="mini">
      <el-form-item label="审批备注：" v-if="checkStatus">
          <el-input type="textarea" v-model="remark" :rows="3"></el-input>
      </el-form-item>
    </el-form>

    <div class="promotion_footer" v-if="checkStatus">
      <el-button class="mini-search-btn" type="success" @click="submit(2)" size="mini">同意</el-button>
      <el-button  @click="submit(3)" size="mini">拒绝</el-button>
    </div>

    <div class="promotion_column">
      <el-table :data="checkData.flows" border header-row-class-name="header-row" class="flows-table">
        <el-table-column align="center" label="审批人" prop="checkUserName"></el-table-column>
        <el-table-column align="center" label="审批时间" prop="checkTime" :formatter="timeFormat"></el-table-column>
        <el-table-column align="center" label="审批状态" prop="checkStatus" :formatter="formatStatus"></el-table-column>
        <el-table-column align="center" label="备注" prop="remark"></el-table-column>
      </el-table>
    </div>
  </div>
  
</template>

<script>
import Vue from 'vue'
import { addPickupRate,policyCheckGet,policyCheck } from '@/views/marketingPromotion/promotionData'
import checkProcedure from "@/components/checkProcedure"
import store from '@/store'
import { timeFormat } from "@/utils/timeFormat"
import distributor from '@/views/marketingPromotion/compomemts/distributor/discountRange.vue'
export default {
  name: 'approvePickUpRatePromotion',
   components: {
    distributor,
    checkProcedure
   },
   data () {
    return {
      totalCount: 1,
      disabled:false,
      loading: false,
      time: [new Date().getTime(), new Date().getTime()],
      distributorData: [],
      formData: {
        policyName: '',
        description: '',
        enable:1,
        timeType:1,
        includeOrderOnWay:2,
        startTime: new Date().getTime,
        endTime: new Date().getTime,
        rules: [{
          monthNum:'',
          isDiscount:1,
          moneyOrCount:1,
          discount:'',
          reduction:'',
          distributorScope:1,
          distributorGradeIds: [],
          distributorIds: [],
          distributorData:[],
        }],
        time: [new Date().getTime(), new Date().getTime()]
      },
      rules: {
        policyName: [{
            required: true,
            message: '请输入活动名称',
            trigger: 'blur'
        }],
        ruleDescribe: [{
            required: true,
            message: '请输入活动规则描述',
            trigger: 'blur'
        }],
      },
      checkData: {
				flows: []
			},
      flows: [],
			remark: '',
      checkStatus:false,
    }
   },
   activated() {
     this.getParams();
   },
   methods: {
		onCancel() { // 返回操作
			this.$router.go(-1)
    },

    submit(type) { // 同意/拒绝操作
      this.loading = true
      policyCheck(this, { id: this.checkData.id, checkStatus: type,checkType:12, remark: this.remark }).then(res => {
        this.loading = false
          if(res.code == 0) {
            this.$message({
              message: '提交成功',
              type: 'success',
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.$router.go(-1)
          }
          if(res.code == 40012) { //..其他端已审批
            this.getParams();
          }
      })
    },

    getParams(){ // 统一政策审批详情
      this.loading = false
      this.disabled = false
      if(this.$route.params.id != undefined){
        policyCheckGet(this,{id:this.$route.params.id}).then(res => {
          if(res.code === 0 && res.policy != undefined){
            this.disabled = true
            res.policy.time = []
            res.checkDetail.flows.forEach(item =>{
              item.checkUserName = ''
            })
            this.formData = res.pickupRate;
            this.checkData = res.checkDetail
            this.checkStatus = false
            if(this.checkData.status === 1){//是否轮到自己审批
              for(let i = 0;i<this.checkData.flows.length;i++){
                if(this.checkData.flows[i].checkStatus === 1 && this.checkData.flows[i].checkUser === store.getters.userinfo.id){
                  this.checkStatus = true
                }
              }
            }
            if(this.formData.startTime !== undefined || this.formData.startTime !== 0 || this.formData.endTime !== undefined || this.formData.endTime !== 0){
              this.formData.time = [this.formData.startTime,this.formData.endTime]
            }
            this.totalCount = this.formData.rules.length;
            this.formData.rules.forEach(item =>{//初始化分销商等级或分销商
              if(item.discount == null) { // 打折为空则为减免
                item.isDiscount = 2
              }else if(item.reduction == null) { // 减免为空则为打折
                item.isDiscount = 1
              }
              item.distributorData = []
              if(item.distributorIds != undefined && item.distributorIds != ""){
                item.distributorIds = item.distributorIds.split(",")
              }else{
                item.distributorIds = []
              }
              if(item.distributorGradeIds != undefined && item.distributorGradeIds != ""){
                let arr = item.distributorGradeIds.split(",")
                item.distributorGradeIds = []
                for(let i = 0;i<arr.length;i++){
                  item.distributorGradeIds.push(Number(arr[i]))
                }
              }else{
                item.distributorGradeIds = []
              }
            })

          }
        })
      }else{
        this.$message.error("统一政策审批不存在！");
      }
    },

    getChange(val,index) {
      this.formData.rules[index].distributorScope = val.distributorType;
      this.formData.rules[index].distributorGradeIds = val.distributorGradeIds;
      this.formData.rules[index].distributorIds = val.distributorIds;
      this.formData.rules[index].distributorData = val.distributorData;
    },

    timeFormat(row, col, val) {
      return timeFormat(val)
    },

    timeFormatter(createTime) {
      return timeFormat(createTime);
    },

    extFormatter(ext){
			switch(ext) {
				case 11: 
					return '商品分类折扣审批单'
				case 12: 
					return '提货增长返利审批单'
				default:
					return '-'
			}
    },

    // submit(type) {
    //   this.loading = true
    //   this.$confirm('确定审批通过/审批拒绝?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning',
    //     center: true
    //   }).then(() => {
    //     policyCheck(this, { id: this.checkData.id, checkStatus: type,checkType:12, remark: this.remark }).then(res => {
    //       this.loading = false
    //         if(res.code == 0) {
    //             this.$message({
    //                 message: '提交成功',
    //                 type: 'success',
    //                 duration: 3 * 1000,
    //                 onClose: () => {
    //                   this.$router.go(-1)
    //                 }
    //             })
    //         }
    //     })
    //   })
    // },
    formatStatus(row, col, type) {
      switch(type) {
        case 0:
          return "待审批";
          break;
        case 1:
          return '审批中';
          break;
        case 2:
          return '审批通过';
          break;
        case 3:
          return '审批拒绝';
          break;
        case 5:
          return '发起审批';
          break;
        default:
          return '-'
      }
    }
   }
}
</script>

<style lang="scss" scoped>
.promotion{
  background-color: white;
  min-height: 100%;
  .sales-promotion {
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
  .box-has-info{
    margin-left: 20px;
    margin-top: 20px;
    overflow: hidden;
    .half-width{
      width: 40%;
      box-sizing: border-box;
      float: left;
    }
  }
  .promotion_container {
    border:1px solid #dcdfe6;
    margin-left:20px;
    margin-right:20px;
    .el-form1{
      width: 90%;
      margin-top: 30px;
      padding-left: 20px;
    }
    .el-form2{
      width: 90%;
      padding-left: 20px;
      .pick_rule{
        border-radius: 5px;
        margin-top: 8px;
        padding: 16px;
        margin-bottom: 5px;
        border: 1px solid #ccc;
        background-color: #f8f8f8;
        .pick_details {
          color: #606266;
          font-weight: 500;
          .pick_if {
            margin-left: 10px;
          }
        }
        .add_rule{
          margin-bottom: 10px;
          margin-top: 10px;
          .add_particulars {
            margin-top: 10px;width:100%
          }
          .box_has_info{
            overflow: hidden;
            width: 100%;
            border-radius: 5px;
            margin-bottom: 5px;
            border: 1px solid #ccc;
            background-color: #fff;
            .half_left{
              line-height:50px;
              width: 90%;
              float: left;
              .left_header {
                width:1000px;
                .left_content_one {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left: 10px;
                }
                .left_content_two {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left: 20px;
                  margin-right: 20px;
                }
                .left_content_three {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  .left_content_input {
                    color: #606266;
                    font-weight: 500;
                    width: 50px;
                    font-size: 12px;
                    text-align: center;
                  }
                }
                .left_content_type1 {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left: 30px;
                }
                .left_content_type2 {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left:10px;
                  display:inline;
                  .left_content_discount1 {
                    color: #606266;
                    font-weight: 500;
                    width: 100px;
                    margin-left:10px;
                  }
                  .left_content_discount2 {
                    color: #606266;
                    font-weight: 500;
                    width: 100px;
                    margin-left:10px;
                  }
                  .left_content_span {
                    color: #606266;
                    font-weight: 500;
                  }
                }
              }
              .left_content_assign {
                overflow: hidden;
                float: left;
              }
            }
            .half-right{
              height: 100%;
              width: 10%;
              float: right;
            }
            .el-radio+.el-radio{
              margin-left: 10px;
            }
          }
        }
      }
    }
  }
  .promotion_flow {
    margin-right: 20px;
    margin-left: 20px;
    margin-bottom: 30px;
  }
  .el-form3{
    width: 90%;
    padding-left: 20px;
  }
  .promotion_footer {
    margin-left: 45%;
    margin-top:30px;
    margin-bottom:30px
  }
  .promotion_column {
    margin-left:20px;
    margin-right:20px;
  }
}
</style>
