<template>
  <div class="promotion">
    <div class="sales-promotion">
      <header>
        <h4>活动审批</h4>
        <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="cancel">
          返回列表
        </el-button>
      </header>
    </div>
    <!-- <div class="body-header">
      活动可协助代销商更好的推广，带来更多的销量，当代销商一次性购满设定的金额，可享有优惠（打折，减价，送赠品，包邮）。
    </div> -->
    <div class="box-has-info">
			<div class="half-width">
				<el-form ref="recordDetail">
					<el-form-item label="审批单号:"> {{formData.check.id}} </el-form-item>
					<el-form-item label="审批类型:"> {{extFormatter(formData.check.ext)}} </el-form-item>
				</el-form>
			</div>

			<div class="half-width">
				<el-form ref="recordDetail" label-width="190px">
					<el-form-item label="发起时间:"> {{timeFormatter(formData.check.createTime)}} </el-form-item>
					<el-form-item label="审批状态:"> {{formatStatus(0,0,formData.check.status)}} </el-form-item>
				</el-form>
			</div>
		</div>

    <div class="half-left">
      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" size="mini">
        <el-form-item label="活动名称:" prop="name">
          <el-input v-model="formData.name" placeholder="不超过10个字" maxlength="10" disabled></el-input>
        </el-form-item>
        <el-form-item label="活动英文名称:" prop="nameEn">
          <el-input v-model="formData.nameEn" placeholder="不超过10个字" maxlength="10" disabled></el-input>
        </el-form-item>

        <el-form-item label="活动描述:" prop="ruleDescribe">
          <el-input type="textarea" v-model="formData.ruleDescribe" disabled></el-input>
        </el-form-item>

        <el-form-item label="活动类型:" prop="promotionType">
          <el-radio-group v-model="formData.promotionType" :disabled="isDisabled" >
            <el-radio :label="1">营销活动</el-radio>
            <el-radio :label="2">阶梯活动</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="在途是否参与:" prop="onWayAttendEventFlag">
          <el-radio-group v-model="formData.onWayAttendEventFlag" :disabled="isDisabled" >
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="活动时间:" prop="time">
          <el-date-picker v-model="formData.time" type="datetimerange" value-format="timestamp"
            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" disabled>
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-radio-group v-model="formData.isAdvance">
            <el-radio :label="false" disabled>准时展示</el-radio>
            <el-radio :label="true" disabled>提前
              <template>
                <span>提前</span>
                <el-input v-model="formData.advanceDay" @keyup.native="proving" disabled style="width: 80px;"></el-input><span > 天展示</span>
              </template>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form2" size="mini">
        <el-form-item label="规则:">
          <div class="add-promotion">
            <div v-if="totalCount > 0">
              <component :is="pomotionRule" :totalCount='totalCount' v-for="(rule,index) in formData.rules"
              :key="rule.id" :currentCount='index+1' v-model="formData.rules" :rule = 'rule' :disabled="true">
              </component>
            </div>
            <div v-else class="no-image">暂时没有添加规则，请先添加新规则</div>
          </div>
        </el-form-item>
        <distributor ref="distributor" v-if="disShow" :type="formData.distributorScope" :gIds="formData.gradeIds" :businessIds="formData.businessIds" :departmentIds="formData.departmentIds" :adminIds="formData.adminIds" :dIds="formData.distributorIds"  :disabled="true"></distributor>
      </el-form>
    </div>

    <div class="half-one">
      <check-procedure :flows="formData.check.flows" v-if="formData.check.flows.length>0"></check-procedure>
    </div>

    <el-form ref="formData" :model="formData" style="margin-right: 20px;" label-width="100px" class="el-form3" size="mini">
      <el-form-item label="审批备注：" v-if="checkStatus">
        <el-input type="textarea" v-model="remark"  :rows="5" />
      </el-form-item>
    </el-form>

    <div class="half-two" v-if="checkStatus">
      <el-button class="mini-search-btn" type="success" @click="submit(2)" size="mini">同意</el-button>
      <el-button  @click="submit(3)" size="mini">拒绝</el-button>
    </div>

    <div class="half-three">
      <el-table :data="formData.check.flows" border header-row-class-name="header-row" class="flows-table">
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
import pomotionRule from '@/views/marketingPromotion/compomemts/pomotionRule'
import { timeFormat } from "@/utils/timeFormat"
import distributor from '@/views/marketingPromotion/compomemts/distributor/discountPromotion'
import { promotionCheckGet,promotionPut,promotionPost,promotionCheck,promotionCheckPut } from '@/views/marketingPromotion/promotionData'
import checkProcedure from "@/components/checkProcedure"
import store from '@/store'
export default {
  components: {
    pomotionRule,
    distributor,
    checkProcedure
  },

  activated() {
    this.getParams();
  },
  mounted() {
    this.getParams();
  },
  data () {
    return {
      disShow: true,
      distributorData: [],
      items: [{}],
      totalCount: 0,
      pomotionRule: "pomotionRule",
      remark:'',
      formData: {
        formData: {
        id :undefined,
        name : '',
        nameEn : '',
        ruleDescribe :'',
        time : [new Date().getTime(),new Date().getTime()],
        isAdvance : 0,
        distributorScope : 1,
        distributorIds:'',
        businessIds:'',
        departmentIds:'',
        adminIds:'',
        gradeIds:'',
        advanceDay :0,
        rules : [{
          label:"",
          labelEn:"",
          target:1,
          moneyOrCount:1,
          isEnjoy:0,
          isCompose:0,
          isAddUp:0,
          groups:[{
            id:"1",
            isAdd:true,
            label:"分组1",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[],
          }],
          goodss:[],
          goodsItems:[],
          conditions:[{
            label:'',
            labelEn:'',
            isSpecial:0,
            isEnjoy:0,
            goodsItems:[],
            oneBuyCount:'',
            oneBuyMoney:'',
            reduceOrPresent:1,
            reductionPresentIsAdd:1,
            discountOrReduction:1,
            discount:'',
            reduction:'',
            presents:[],
            groupConditions:[{
              promotionGroupId:1,
              oneBuyCount:'',
              oneBuyMoney:'',
              goodss:[],
              goodsItems:[],
            },{
              promotionGroupId:2,
              oneBuyCount:'',
              oneBuyMoney:'',
              goodss:[],
              goodsItems:[],
            }],
            specials:[]
          }]
        }]},
        check: {
            flows: []
        }
      },
      checkStatus:false
    }
  },
  methods: {
    getParams(){
      this.loading = false;
      if(this.$route.params.id != undefined){
          // promotionCheckGet(this,{id: this.$route.params.id}).then(res => {//获取活动详情
          this.$http.promotionCheckDetail(this, {id: this.$route.params.id}).then(res => {//获取活动详情
            res.promotion.time = []
            res.promotion.rules.forEach(rule =>{
              if(rule.target !== 1 && rule.isCompose === 1){//组合
                rule.conditions.forEach(condition =>{
                  condition.groupConditions.forEach(groupCondition => {
                    for(let i = 0;i<rule.groups.length;i++){
                      if(rule.groups[i].id === groupCondition.promotionGroupId){
                        if(rule.target === 2){
                          groupCondition.goodss = JSON.parse(JSON.stringify(rule.groups[i].goodss))
                        }else if(rule.target === 3){
                          groupCondition.goodsItems = JSON.parse(JSON.stringify(rule.groups[i].goodss))
                        }
                        if(rule.target === 3 && condition.isSpecial === 1){
                          groupCondition.goodsItems.forEach(goods =>{//特价
                            if(condition.specials !== undefined && condition.specials.length>0){
                              for(let j = 0;j<condition.specials.length;j++){
                                if(goods.itemId === condition.specials[j].itemId){
                                  goods.specialPrice = condition.specials[j].specialPrice
                                  condition.specials.splice(j,1)
                                  j--
                                  break
                                }
                              }
                            }
                          })
                        }
                        break
                      }
                    }
                  })
                })
                rule.groups.forEach(item =>{
                  item.id = item.id+''
                  if(rule.target === 3){
                    item.goodsItems = item.goodss
                  }
                })
              } else if(rule.isCompose === 0 && rule.target === 3){//非组合
                rule.conditions.forEach(condition =>{
                  condition.goodsItems = JSON.parse(JSON.stringify(rule.goodsItems))
                  if(condition.isSpecial === 1){
                    condition.goodsItems.forEach(goods =>{//特价
                      if(condition.specials !== undefined && condition.specials.length>0){
                        for(let j = 0;j<condition.specials.length;j++){
                          if(goods.itemId === condition.specials[j].itemId){
                            goods.specialPrice = condition.specials[j].specialPrice
                            condition.specials.splice(j,1)
                            j--
                            break
                          }
                        }
                      }
                    })
                  }
                })
              }
              rule.conditions.forEach(condition =>{
                if(condition.presents != undefined && condition.presents.length>0){
                  condition.presents.forEach(present =>{
                    present.totalNum = ''
                  })
                }
              })
            })
            res.promotion.check.flows.forEach(item => {
              item.checkUserName = "";
            })
            this.formData = res.promotion;
            this.formData.promoStatus == 5 ? this.isDisabled = false : this.isDisabled = true //..是否可编辑
            this.totalCount = this.formData.rules.length;
            // this.formData.gradeIds = res.promotion.gradeIds == undefined ? [] : res.promotion.gradeIds.splitnum(',')
            this.formData.time = [this.formData.startTime,this.formData.endTime]
            if(this.formData.distributorScope === 1){
              this.count = 5
            }else{
              this.count = 6
            }
            this.checkStatus = false
            if(this.formData.checkStatus === 1){//是否轮到自己审批
              for(let i = 0;i<this.formData.check.flows.length;i++){
                if(this.formData.check.flows[i].checkStatus === 1 && this.formData.check.flows[i].checkUser === store.getters.userinfo.id){
                  this.checkStatus = true
                }
              }
            }
            this.formData.time = [this.formData.startTime,this.formData.endTime]
            let ids = [];
            this.formData.check.flows.forEach(item => {
              ids.push(item.checkUser);
            })
            this.$api.get(this, 'admin/u/po/admin/ids', { ids: ids.join(',') }).then(res => {//审批人姓名
              if(res.code == 0) {
                this.formData.check.flows.forEach(item => {
                  res.admins.forEach(val => {
                    if(val.id == item.checkUser) {
                      item.checkUserName = val.name;
                    }
                  })
                })
              }
            })
            res.code == 0 ? this.loading2 = false : this.loading2 = false
          })
      }else{
        this.$message.error("促销活动审批不存在！");
      }
  },
  getItemIds(index){
    for(let i = 0; i<this.formData.rules[index].items.length;i++){
      if(i === 0){
          this.formData.rules[index].itemIds = String(this.formData.rules[index].items[i].id);
      }else{
          this.formData.rules[index].itemIds =this.formData.rules[index].itemIds  +","+ String(this.formData.rules[index].items[i].id);
      }
    }
  },
  gitGoodIds(index){
    for(let i = 0; i<this.formData.rules[index].goods.length;i++){
      if(i === 0){
          this.formData.rules[index].goodsIds = String(this.formData.rules[index].goods[i].id);
      }else{
          this.formData.rules[index].goodsIds =this.formData.rules[index].goodsIds  +","+ String(this.formData.rules[index].goods[i].id);
      }
    }
  },
  cancel(){
    this.$router.push({name:'checkSalesPromotion'});
  },
  submit(type) {
    promotionCheck(this, { id: this.formData.id, checkStatus: type,checkType: this.formData.check.ext, remark: this.remark }).then(res => {
      if(res.code == 0) {
        this.$message({
            message: '提交成功',
            type: 'success',
            duration: 3 * 1000,
            onClose: () => { }
        })
        this.$router.push({name:'checkSalesPromotion'})
      }else if(res.code != 0) {
        this.getParams();
      }
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
      case 7:
        return '促销活动';
        break;
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
  .body-header {
    line-height: 40px;
    font-size: 12px;
    color: $lakeBlue;
    font-weight: 700;
    padding-left: 20px;
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
  .half-left {
    border:1px solid #dcdfe6;
    margin-left:20px;
    margin-right:20px;
    .el-form1{
      width: 600px;
      margin-top: 30px;
      padding-left: 20px;
    }
    .el-form2{
      margin-top: 30px;
      width: 100%;
      padding-left: 20px;
      .add-promotion{
        margin-right: 100px;
        border-radius: 5px;
        background-color: #f8f8f8;
        .no-image{
          margin-left: 16px;
        }
      }
    }
  }
  .half-one {
    margin-right: 20px;
    margin-left: 20px;
    margin-bottom: 30px;
  }
  .el-form3{
    margin-top: 30px;
    padding-left: 20px;
  }
  .half-two {
    margin-left: 45%;
    margin-top:30px;
    margin-bottom:30px
  }
  .half-three {
    margin-left:20px;
    margin-right:20px;
    .flows-table {
      margin-top: 20px;
      padding-bottom: 20px;
      text-align: center;
    }
  }
}
</style>
