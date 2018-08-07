<template>
  <div class="promotion">
    <div class="sales-promotion">
      <header>
        <h4>统一政策审批单</h4>
        <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onCancel()"> 返回 </el-button>
      </header>
    </div>
    <!-- <div class="header">
      活动可协助代销商更好的推广，带来更多的销量，当客户一次性购满设定的金额，可享有折扣优惠。
    </div> -->
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

    <div style="border:1px solid #dcdfe6;margin-left:20px;margin-right:20px ">
      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini">
        <el-form-item label="活动名称:" class="name" prop="policyName">
          <el-input :disabled="disabled" v-model="formData.policyName" placeholder="不超过10个字" maxlength="10" />
        </el-form-item>

        <el-form-item label="规则描述:" prop="description">
          <el-input :disabled="disabled" type="textarea" v-model="formData.description"></el-input>
        </el-form-item>

        <el-form-item label=" 在途是否参与:">
          <el-radio-group :disabled="disabled" v-model="formData.onWayAttendEventFlag">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="启用状态:">
          <el-radio-group :disabled="disabled"  v-model="formData.enable">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="2">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="活动时间:" prop="time">
          <el-radio-group :disabled="disabled" v-model="formData.timeType">
            <el-radio :label="1">长期</el-radio>
            <el-radio :label="2">具体时间范围
              <template>
                  <el-date-picker :disabled="disabled" v-show="formData.timeType === 2" v-model="formData.time" type="datetimerange"
                    value-format="timestamp" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                  </el-date-picker>
              </template>
              </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini" >
        <el-form-item style="margin-bottom: 5px">
          <div class="operation">
            <button class="mini-search-btn" style="float: left;" @click.prevent="addDiscountRule(discountTotalCount)"  v-if="!disabled" >添加规则</button>
            <button class="mini-search-btn" style="float: right; margin-left: 10px;" @click.prevent="allDiscountSpread(true)">全部展开</button>
            <button class="mini-search-btn" style="float: right;" @click.prevent="allDiscountSpread(false)">全部收起</button>
          </div>
        </el-form-item>

        <el-form-item label="折扣设置:">
          <div class="add-promotion">
            <!-- 商品等级折扣规则详情 -->
            <div v-if="discountTotalCount > 0">
              <component :productlines="productlines" :is="discountRule" :totalCount="discountTotalCount" v-for="(detail,index) in formData.details" :key="detail.id" :currentCount="index+1" v-model="formData.details" :detail="detail" :disabled="disabled" ></component>
            </div>
            <div v-else class="no-image">暂时没有添加折扣规则，请先添加新折扣规则</div>
          </div>
        </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini">
        <el-form-item style="margin-bottom: 5px">
          <div class="operation">
            <button class="mini-search-btn" style="float: left;" @click.prevent="add(totalCount)" v-if="!disabled">添加政策</button>
            <button class="mini-search-btn" style="float: right; margin-left: 10px;" @click.prevent="allSpread(true)">全部展开</button>
            <button class="mini-search-btn" style="float: right;" @click.prevent="allSpread(false)">全部收起</button>
          </div>
        </el-form-item>

        <el-form-item label="统一政策:">
          <div class="add-promotion">
            <div v-if="totalCount > 0">
              <!-- 统一政策组件 -->
              <component :is="policyRule" :distributorScope="rule.distributorScope" :totalCount='totalCount' v-for="(rule,index) in formData.rules" :key="rule.id" :currentCount='index+1' v-model="formData.rules" :rule ="rule" :disabled="disabled"> </component>
            </div>
            <div v-else class="no-image">暂时没有添加规则，请先添加新规则</div>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <div style="margin-right: 20px;margin-left: 20px;margin-bottom: 30px;" >
      <check-procedure :flows="checkData.flows" v-if="checkData.flows.length>0"></check-procedure>
    </div>

    <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini">
      <el-form-item label="审批备注：" v-if="checkStatus">
        <el-input type="textarea" v-model="remark" :rows="5"/>
      </el-form-item>
    </el-form>

    <div v-if="checkStatus" style="margin-left: 45%; margin-top:30px;margin-bottom:30px">
      <el-button class="mini-search-btn" type="success" @click="submit(2)" size="mini">同意</el-button>
      <el-button  @click="submit(3)" size="mini">拒绝</el-button>
    </div>
    <div style="margin-left:20px;margin-right:20px;">
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
import policyRule from '@/views/marketingPromotion/compomemts/policyRule'
import checkProcedure from "@/components/checkProcedure"
import discountRule from '@/views/marketingPromotion/compomemts/discountRule'
import store from '@/store'
import { timeFormat } from "@/utils/timeFormat"
import { policyCheckGet,policyCheck } from '@/views/marketingPromotion/promotionData'
export default {
  name:'approvePolicyPromotion',
  components: {
    discountRule,
    policyRule,
    checkProcedure
  },
  data () {
    return {
      discountTotalCount: 1,
      disabled:true,
      totalCount: 0,
      policyRule: "policy-rule",
      discountRule: 'discount-rule',
      remark:'',
      loading: false,
      time: [new Date().getTime(), new Date().getTime()],
      productlines:[],
      formData: {
        name: '',
        describe: '',
        enable:1,
        timeType:1,
        startTime: new Date().getTime,
        endTime: new Date().getTime,
        rules: [],
        time: [new Date().getTime(), new Date().getTime()]
      },
      rules: {
        name: [{
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
  created(){
    this.$http.getCategoryList(this, {
      page: 1, size: 1000, openFlag: 1
    }).then(res => {
      if (res.sucess) {
         res.data.list.forEach(item =>{
          item.disabled = false
        })
        this.productlines = res.data.list
        this.getParams()
      }
    })
  },
  activated() {
    this.getParams();
  },
  computed: {
    checkApproveType() {
      return this.$route.params.approveType
    }
  },
  methods: {
    allDiscountSpread(isSpread) { // 全部展开收起操作
      this.formData.details.forEach(item => {
        item.isSpread = isSpread
      })
    },
    fun(attr) { // 数组去重
      let arr = []
      attr.forEach(item1 =>{
        let isExit = false
        arr.forEach(item2 =>{
          if(item1.categoryIds == item2.categoryIds){
            item2.detailsArr.push(item1)
            isExit = true
            return
          }
        })
        if(!isExit){
          arr.push({
            categoryIds:item1.categoryIds,
            isSpread:true,
            detailsArr:[item1]
          })
        }
      })
      arr.forEach(item3 =>{
        item3.categoryIds = (item3.categoryIds.split(',')).map(Number)
      })
      return arr
    },
    onCancel() { // 返回操作
      if(this.checkApproveType) {
        this.$router.push({name: 'policyGradePromotion'})
      }else {
        this.$router.go(-1)
      }
		},
    getParams(){
      this.loading = false
      this.disabled = true
      if(this.$route.params.id != undefined){
        policyCheckGet(this,{id:this.$route.params.id}).then(res => {
          if(res.code === 0 && res.policy != undefined){
            this.disabled = true
            res.policy.time = []
            res.policy.rules.forEach(item =>{
              item.isSpread = true
              // item.details = this.fun(item.details)
            })
            res.policy.details = this.fun(res.policy.details)
            res.checkDetail.flows.forEach(item =>{
              item.checkUserName = ''
            })
            this.checkData = res.checkDetail
            this.checkStatus = false
            if(this.checkData.status === 1){ // 是否轮到自己审批
              for(let i = 0;i<this.checkData.flows.length;i++){
                if(this.checkData.flows[i].checkStatus === 1 && this.checkData.flows[i].checkUser === store.getters.userinfo.id){
                  this.checkStatus = true
                }
              }
            }
            if(res.policy.startTime !== undefined || res.policy.startTime !== 0 || res.policy.endTime !== undefined || res.policy.endTime !== 0){
              res.policy.time = [res.policy.startTime,res.policy.endTime]
            }
            this.totalCount = res.policy.rules.length;
            this.discountTotalCount = res.policy.details.length
            res.policy.rules.forEach(item =>{ // 初始化分销商等级或分销商
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
            this.formData = res.policy;
            let ids = [];
            this.checkData.flows.forEach(item => {
              ids.push(item.checkUser);
            })
            this.$api.get(this, 'admin/u/po/admin/ids', { ids: ids.join(',') }).then(res => { // 审批人姓名
              if(res.code == 0) {
                this.checkData.flows.forEach(item => {
                  res.admins.forEach(val => {
                    if(val.id == item.checkUser) {
                      item.checkUserName = val.name;
                    }
                  })
                })
              }
            })
          }
        })
      }else{
        this.$message.error("统一政策审批不存在！");
      }
    },
    allSpread(isSpread){
      this.formData.rules.forEach(item =>{
        item.isSpread = isSpread
      })
    },
    submit(type) { // 同意/拒绝操作
      this.loading = true
      this.$confirm('确定审批通过/审批拒绝?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          policyCheck(this, { id: this.checkData.id, checkStatus: type,checkType:11, remark: this.remark }).then(res => {
            this.loading = false
              if(res.code == 0) {
                this.$message({
                    message: '提交成功',
                    type: 'success',
                    duration: 3 * 1000,
                    onClose: () => {
                    }
                })
                this.$router.go(-1)
              }
              if(res.code == 40012) { //..其他端已审批
                this.getParams();
              }
          })
        })
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
					return '商品分类折扣审批'
				case 12: 
					return '提货增长返利审批'
				default:
					return '-'
			}
		},
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
  },
}
</script>

<style lang="scss" scoped>
.promotion{
  background-color: white;
  min-height: 100%;
  .header {
    line-height: 40px;
    font-size: 12px;
    color: $lakeBlue;
    font-weight: 700;
    padding-left: 20px;
  }
}
.add-promotion{
   border-radius: 5px;
   background-color: #f8f8f8;
   .btn-delete{
      color: #fff;
      background-color: #e6a23c;
      border-radius: 6px;
    }
    .btn-add{
      color: #fff;
      background-color: #21b8cb;
      border-radius: 6px;
    }
    .no-image{
      margin-left: 16px;
    }
 }
.function{
  padding-left: 20px;
  margin-bottom: 10px;
  padding-bottom: 10px;
  padding-top: 10px;
  background-color: white;
  border-bottom: 1px solid #ccc;
  .btn-delete{
    color: #fff;
    background-color: #e6a23c;
    border-radius: 6px;
  }
  .btn-add{
    color: #fff;
    background-color: #21b8cb;
    border-radius: 6px;
  }
}
.title{
  font-size: 12px;
}
.el-form1{
  width: 90%;
  margin-top: 30px;
  padding-left: 20px;
}
.el-promotion-rule{
  width: 100%;
  margin-top: 30px;
  padding-left: 20px;
}
.line{
  text-align: center
}
.operation{
  margin-bottom: 10px;
  .btn-add{
    color: #fff;
    background-color: #21b8cb;
    border-radius: 6px;
  }
}
.footbtn {
  padding-top: 30px;
  padding-bottom: 30px;
}
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
</style>



