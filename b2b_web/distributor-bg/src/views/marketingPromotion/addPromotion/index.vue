<template>
  <div class="promotion">
    <div class="sales-promotion">
       <header v-if="!checkMsg">
        <h4>促销活动</h4>
        <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="cancel()"> 返回列表 </el-button>
      </header>
    </div>
    <approve v-if="checkMsg === 4" :approveData="approveData"></approve>
    <div class="promotion-content" v-loading="loading2">
      <div class="body-header" v-if="!checkMsg">
        活动可协助代销商更好的推广，带来更多的销量，当代销商一次性购满设定的金额，可享有优惠（打折，减价，送赠品）。
      </div>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini">
        <el-form-item label="活动名称:" class="name" prop="name">
            <el-input style="width: 360px;" :disabled="isDisabled" v-model="formData.name" placeholder="不超过10个字" maxlength="10" />
            <el-button class="mini-search-btn" icon="el-icon-plus" @click="copyPromotion()" v-if="isDisabled && !checkMsg"> 复制添加 </el-button>
        </el-form-item>
        <el-form-item label="活动英文名称:" class="name" prop="nameEn">
          <el-input style="width: 360px;" :disabled="isDisabled" v-model="formData.nameEn" placeholder="请输入活动英文名称"/>
        </el-form-item>

        <el-form-item label="活动描述:" prop="promoDesc">
          <el-input :disabled="isDisabled"  type="textarea" v-model="formData.promoDesc" />
        </el-form-item>
        <el-form-item label="活动类型:" prop="promoType">
          <el-radio-group v-model="formData.promoType" :disabled="isDisabled" >
            <el-radio :label="1">营销活动</el-radio>
            <el-radio :label="2">阶梯活动</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="在途是否参与:" prop="onWayFlag">
          <el-radio-group v-model="formData.onWayFlag" :disabled="isDisabled" >
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="活动时间:" prop="time">
          <el-date-picker v-model="formData.time" type="datetimerange" value-format="timestamp"
            range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :disabled="isDisabled" >
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-radio-group v-model="formData.advanceFlag" :disabled="isDisabled" >
            <el-radio :label="1">准时展示</el-radio>
            <el-radio :label="2">提前
              <template>
                <span>提前</span>
                <el-input v-model="formData.advanceDay" :disabled="isDisabled" style="width: 80px;" @keyup.native="proving" /><span > 天展示</span>
              </template>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-promotion-rule" :rules="rules" size="mini">
        <el-form-item label="规则:">
          <div class="add-promotion">
            <div v-if="totalCount > 0">
              <component @deleteRules = "deleteRules" v-model="formData.rules" :is="pomotionRule" :totalCount='totalCount' :rule ='rule' :disabled="isDisabled"
              v-for="(rule,index) in formData.rules" :key="rule.id" :currentCount='index+1' @changeRule="changeRule" >
              </component>
            </div>
            <div v-else class="no-image">暂时没有添加规则，请先添加新规则</div>
          </div>
        </el-form-item>

        <el-form-item v-show="!isDisabled">
          <div class="operation">
            <el-tooltip content="最多添加6条规则" placement="right">
              <button class="mini-search-btn" @click.prevent="add(totalCount)">新增规则</button>
            </el-tooltip>
          </div>
        </el-form-item>
        <distributor ref="distributor" v-if="disShow" :type="formData.distributorScope" :gIds="formData.scalePriceIds" 
        :businessIds="formData.businessIds" :departmentIds="formData.departmentIds" :adminIds="formData.adminIds" 
        :dIds="formData.distributors"  @change="getChange" :disabled="isDisabled"></distributor>
      </el-form>

      <div class="clearfix footbtn" v-if="!type">
        <div>
          <el-button v-if="!isDisabled" size="mini" @click="handleSave(1)" :loading="loading">保存草稿</el-button>
          <el-button v-if="!isDisabled" class="mini-search-btn box-btn" @click="handleSave(0)" :loading="loading">保存提交</el-button>
          <el-button  size="mini" @click="cancel()">返回</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import pomotionRule from '@/views/marketingPromotion/compomemts/pomotionRule'
import distributor from '@/views/marketingPromotion/compomemts/distributor/discountPromotion'
import { parseTime } from '@/utils/index'
import approve from '@/views/marketingPromotion/compomemts/approve'
export default {
  name: 'editPromotion',
  components: {
    pomotionRule,
    distributor,
    approve
  },
  created(){
    this.checkMsg = this.$route.query.checkMsg;
    if (this.checkMsg === 4) {
      this.isDisabled = true
      this.getDetail()
    } else {
      this.getParams()
    }
  },
  activated() {
    if (this.checkMsg === 4) {
      this.isDisabled = true
      this.getDetail()
    } else {
      this.getParams()
    }
  },
  data () {
    return {
      loading2: false,
      disShow: true,
      isDisabled:false,
      orderRule:false,
      orderName:'',
      isSave:false,
      distributorData: [],
      totalCount: 0,
      count:0,
      pomotionRule: "pomotion-rule",
      loading: false,
      noCache:false,
      time: [new Date().getTime(), new Date().getTime()],
      formData: {
        id :undefined,
        name : '',
        nameEn : '',
        promoDesc :'',
        time : [new Date().getTime(),new Date().getTime()],
        advanceFlag : 1,
        distributorScope : 1,
        distributorScopeNo: 0,
        distributors:[],
        businessIds:[],
        departmentIds:[],
        adminIds:[],
        scalePriceIds:[],
        // advanceDay :0,
        promoType:1,
        onWayFlag:0,
        rules : [{
          ruleName:"",
          ruleNameEn:"",
          ruleType:1,
          moneyOrCount:1,
          isEnjoy:0,
          isCompose:0,
          addUpFlag:1,
          groups:[{
            id:"1",
            isAdd:true,
            label:"分组1",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[]
          },{
            id:'2',
            isAdd:true,
            label:"分组2",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[]
          }],
          goodss:[],
          goods:[],
          conditions:[{
            conditionName:'',
            specialFlag:0,
            isEnjoy:0,
            goodsItems:[],
            oneBuyCount:'',
            oneBuyMoney:'',
            reduceOrPresent:1,
            reductionPresentAddFlag:1,
            reduceType:1,
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
        }]
      },
      rules: {
        name: [{
            required: true,
            message: '请输入活动名称',
            trigger: 'blur'
        }],
        promoDesc: [{
            required: true,
            message: '请输入活动规则描述',
            trigger: 'blur'
        }],
        time: [{
            required: true,
            message: '活动时间不能为空',
            trigger: 'blur'
        }]
      },
      needCheckFlag:0,
      type: this.$route.query.type,
      submitLoading: false,
      isCheck: false, // 审批权限
      checkActive: 1,
      checkData: {
        remark: ''
      },
      approveData: {} // 审批数据
    }
  },
  methods: {
    copyPromotion(){
      this.formData.id = undefined
      this.formData.rules.forEach(rule =>{
        rule.id = undefined
        if(rule.ruleType !== 1 && rule.isCompose === 1){
          rule.groups.forEach(group =>{
            group.id = undefined
          })
        }
        rule.conditions.forEach(condition =>{
          condition.id = undefined
          if(rule.ruleType !== 1 && rule.isCompose === 1){
            condition.groupConditions.forEach(groupCondition =>{
              groupCondition.id = undefined
            })
          }
        })
      })
      this.isDisabled = false
    },
    // proving(){ // 天数展示验证
    //   this.formData.advanceDay=this.formData.advanceDay.replace(/[^\.\d]/g,'');
    //   this.formData.advanceDay=this.formData.advanceDay.replace('.','');
    //   if(this.formData.advanceDay > 99){
    //     this.formData.advanceDay = 0
    //     this.$message.error("1-99（正整数）!")
    //   }
    // },
    getParams(){
      this.isDisabled = false;
      this.orderRule = false;
      if(this.$route.params.id != undefined){
        this.loading2 = true;
        this.$http.promotionDetail(this, {id: this.$route.params.id}).then(res => { 
          this.initData(res.data)
          res.success ? this.loading2 = false : this.loading2 = false
        })
      }else{
        this.count = 0
        this.formData.id = undefined;
        this.formData.name = '';
        this.formData.promoDesc = '';
        this.formData.startTime = new Date().getTime();
        this.formData.endTime = new Date().getTime();
        this.formData.advanceFlag = 1;
        this.formData.distributorScope = 1;
        this.formData.distributorScopeNo = 0;
        this.formData.businessIds = []
        this.formData.departmentIds = []
        this.formData.adminIds = []
        this.formData.scalePriceIds = []
        this.formData.advanceDay = 0
        this.formData.promoType = 1
        this.formData.onWayFlag = 0
        this.formData.rules = [{
          ruleName:"",
          ruleNameEn:"",
          ruleType:1,
          isEnjoy:0,
          isCompose:0,
          addUpFlag:1,
          moneyOrCount:1,
          groups:[{
            id:"1",
            isAdd:true,
            label:"分组1",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[]
          },{
            id:'2',
            isAdd:true,
            label:"分组2",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[]
          }],
          goodss:[],
          goodsItems:[],
          conditions:[{
            conditionName:'',
            conditionNameEn:'',
            specialFlag:0,
            moneyOrCount:1,
            isEnjoy:0,
            goodss:[],
            goodsItems:[],
            oneBuyCount:'',
            oneBuyMoney:'',
            reduceOrPresent:1,
            reductionPresentAddFlag:1,
            reduceType:1,
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
        }]
        this.totalCount = this.formData.rules.length;
        this.disShow = true;
        this.formData.time = [this.formData.startTime, this.formData.endTime]
      }
    },
    initData (data) {
      data.rules.forEach(rule =>{
        rule.isCompose = 0
        if(rule.ruleType !== 1 && rule.isCompose === 1){//组合
          rule.conditions.forEach(condition =>{
            condition.groupConditions.forEach(groupCondition => {
              for(let i = 0;i<rule.groups.length;i++){
                if(rule.groups[i].id === groupCondition.promotionGroupId){
                  if(rule.ruleType === 2){
                    groupCondition.goodss = JSON.parse(JSON.stringify(rule.groups[i].goodss))
                  }else if(rule.ruleType === 3){
                    groupCondition.goods = JSON.parse(JSON.stringify(rule.groups[i].goodss))
                  }
                  if(rule.ruleType === 3 && condition.specialFlag === 1){
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
            if(rule.ruleType === 3){
              item.goodsItems = item.goodss
            }
          })
        } else if(rule.isCompose === 0 && rule.ruleType === 3){//非组合
          // rule.conditions.forEach(condition =>{
            // if (rule.goods) {
            //   condition.goodsItems = JSON.parse(JSON.stringify(rule.goods))
            // }
            // if(condition.specialFlag === 1){
            //   if (condition.goodsItems && condition.goodsItems.length>0) {
            //     condition.goodsItems.forEach(goods =>{//特价
            //       if(condition.specials !== undefined && condition.specials.length>0){
            //         for(let j = 0;j<condition.specials.length;j++){
            //           if(goods.itemId === condition.specials[j].itemId){
            //             goods.specialPrice = condition.specials[j].specialPrice
            //             condition.specials.splice(j,1)
            //             j--
            //             break
            //           }
            //         }
            //       }
            //     })
            //   }
            // }
          // })
          // rule.goodsItems = rule.goodss
        }
        rule.conditions.forEach(condition =>{
          if(condition.presents != undefined && condition.presents.length>0){
            condition.presents.forEach(present =>{
              present.totalNum = ''
            })
          }
        })
      })
      this.formData = data;
      // this.formData.promoStatus == 5 ? this.isDisabled = false : this.isDisabled = true //..是否可编辑
      this.isDisabled = this.formData.promoStatus === 0&&this.formData.applyStatus===0?false:true
      this.totalCount = this.formData.rules.length;
      // this.formData.scalePriceIds = res.promotion.gradeIds == undefined ? [] : res.promotion.gradeIds.splitnum(',')
      this.$set(this.formData, 'time', [new Date(this.formData.startTime).getTime(),new Date(this.formData.endTime).getTime()])
      if(this.formData.distributorScope === 1){
        this.count = 5
      }else{
        this.count = 6
      }
    },
    cancel(){ //..返回操作
      this.$router.go(-1);
    },

    handleSave(isDraft){ //..保存操作
      // isDraft 1 草稿  0 保存
      this.loading = true;
      this.formData.isDraft = isDraft
      if(this.formData.name == undefined || this.formData.name.replace(/^\s+|\s+$/g,"") == ""){
        this.$message.error('活动名称不能为空！');
        this.loading = false;
        return
      }
      if(this.formData.isDraft === 0 && (this.formData.promoDesc == undefined || this.formData.promoDesc.replace(/^\s+|\s+$/g,"") == "")){
        this.$message.error('活动规则描述不能为空！');
        this.loading = false;
        return
      }
      this.formData.startTime = this.formData.time[0]
      this.formData.endTime = this.formData.time[1]
      if(this.formData.isDraft === 0 && (this.formData.startTime === 0 || this.formData.endTime === 0)){
        this.$message.error('活动开始时间或结束时间不能为空！');
        this.loading = false;
        return
      }
      this.count = 5
      if(this.formData.distributorScope === 0 || this.formData.distributorScope === 1){
        this.formData.scalePriceIds = []
        this.formData.businessIds = []
        this.formData.departmentIds = []
        this.formData.adminIds = []
        this.formData.distributors = []
      }else if(this.formData.distributorScope === 2){
        if(this.formData.isDraft === 0 && (this.formData.scalePriceIds === undefined || this.formData.scalePriceIds.length === 0)){
          this.$message.error('活动范围，请至少指定一个分销商等级！');
          this.loading = false;
          return
        }else{
          // if(this.formData.scalePriceIds instanceof Array){
          //   this.formData.scalePriceIds = this.formData.scalePriceIds.join(",");
          // }
          this.formData.businessIds = []
          this.formData.departmentIds = []
          this.formData.adminIds = []
          this.formData.distributors = []
        }
      }else if(this.formData.distributorScope === 6){
        if(this.formData.isDraft === 0 && (this.formData.businessIds === undefined || this.formData.businessIds.length === 0)){
          this.$message.error('活动范围，请至少指定一个事业部！');
          this.loading = false;
          return
        }else{
          // if(this.formData.businessIds instanceof Array){
          //   this.formData.businessIds = this.formData.businessIds.join(",");
          // }
          this.formData.scalePriceIds = []
          this.formData.departmentIds = []
          this.formData.adminIds = []
          this.formData.distributors = []
        }
      }else if(this.formData.distributorScope === 4){
        if(this.formData.isDraft === 0 && (this.formData.departmentIds === undefined || this.formData.departmentIds.length === 0)){
          this.$message.error('活动范围，请至少指定一个销售部门！');
          this.loading = false;
          return
        }else{
          // if(this.formData.departmentIds instanceof Array){
          //   this.formData.departmentIds = this.formData.departmentIds.join(",");
          // }
          this.formData.scalePriceIds = []
          this.formData.businessIds = []
          this.formData.adminIds = []
          this.formData.distributors = []
        }
      }else if(this.formData.distributorScope === 5){
        if(this.formData.isDraft === 0 && (this.formData.adminIds === undefined || this.formData.adminIds.length === 0)){
          this.$message.error('活动范围，请至少指定一个业务员！');
          this.loading = false;
          return
        }else{
          // if(this.formData.adminIds instanceof Array){
          //   this.formData.adminIds = this.formData.adminIds.join(",");
          // }
          this.formData.scalePriceIds = []
          this.formData.businessIds = []
          this.formData.departmentIds = []
          this.formData.distributors = []
        }
      }else if(this.formData.distributorScope === 3){
        this.formData.distributors = []
        if(this.formData.isDraft === 0 && (this.distributorData.length === 0)){
          this.$message.error('活动范围，请至少指定一个分销商！');
          this.loading = false;
          return
        }else{
          this.formData.scalePriceIds = [];
          this.formData.distributors = []
          for(let i = 0; i<this.distributorData.length;i++){
            this.formData.distributors.push({
              distributorId: this.distributorData[i].id,
              name: this.distributorData[i].name,
              companyName: this.distributorData[i].companyName
            })
          }
          this.formData.scalePriceIds = []
          this.formData.businessIds = []
          this.formData.departmentIds = []
          this.formData.adminIds = []
        }
      }
      
    
      // if(this.formData.isDraft === 0 && (this.formData.advanceFlag === 1)){
      //   if(this.formData.advanceDay === 0){
      //     this.$message.error('提前天数不能为空或零！');
      //     this.loading = false;
      //     return
      //   }
      // }else{
      //   this.formData.advanceDay === 0;
      // }
      if(this.formData.isDraft === 0 && (this.formData.rules.length === 0)){
        this.$message.error('至少有一个规则！');
        this.loading = false;
        return
      }
      for(let i = 0;i<this.formData.rules.length;i++){
        let rule = this.formData.rules[i]
        if(this.formData.isDraft === 0 && (rule.ruleType === 1 && this.orderRule)){
          this.$message.error('订单规则'+this.orderName+'与订单规则'+rule.ruleName+'重复了，订单规则在同一时间段同一范围只能存在一个订单规则！');
          this.loading = false
          return
        }else{
          this.orderRule = true
        }
        if(this.formData.isDraft === 0 && (rule.ruleName == undefined || rule.ruleName.replace(/^\s+|\s+$/g,"") == "")){
          this.$message.error('规则标签不能为空！');
          this.loading = false;
          this.orderRule = false
          return
        }
        // 分组
        if(rule.isCompose === 1){
          for(let i= 0;i<rule.groups.length;i++){
            if(rule.groups[i].label == undefined || rule.groups[i].label.replace(/^\s+|\s+$/g,"") == ""){
              this.$message.error('分组标签不能为空！');
              this.loading = false;
              this.orderRule = false
              return
            }
          }
        }
        if(rule.ruleType === 2 && rule.isCompose === 0){ // 单个商品非组合
          if(this.formData.isDraft === 0 && rule.goods.length === 0){
            this.$message.error('指定商品不能为空！');
            this.loading = false;
            this.orderRule = false
            return
          }
        }else if(rule.ruleType === 2 && rule.isCompose === 1){ // 单个商品组合
          for(let i = 0;i<rule.groups.length;i++){
            let group = rule.groups[i]
            if(this.formData.isDraft === 0 && group.goodss.length === 0){
              this.$message.error("'"+group.label+"'组商品不能为空！");
              this.loading = false;
              this.orderRule = false
              return
            }else{
              group.goodsItemIds=''
              group.goodss.forEach((goods,index) =>{
                if(index === 0 && group.goodss.length === 1){
                  group.goodsItemIds = group.goodsItemIds+goods.goodsId
                }else if(index === group.goodss.length-1){
                  group.goodsItemIds = group.goodsItemIds+goods.goodsId
                }else{
                  group.goodsItemIds = group.goodsItemIds+goods.goodsId+","
                }
              })
            }
          }
        }else if(rule.ruleType === 3 && rule.isCompose === 0){ // 单个货品非组合
          if(this.formData.isDraft === 0 && rule.goods && rule.goods.length === 0){
            this.$message.error('指定货品不能为空！');
            this.loading = false;
            this.orderRule = false
            return
          }
        }else if(rule.ruleType === 3 && rule.isCompose === 1){ // 单个货品组合
          for(let i = 0;i<rule.groups.length;i++){
            let group = rule.groups[i]
            if(this.formData.isDraft === 0 && group.goodsItems.length === 0){
              this.$message.error("'"+group.label+"'组货品不能为空！");
              this.loading = false;
              this.orderRule = false
              return
            }else{
              group.goodsItemIds=''
              group.goodsItems.forEach((goodsItems,index) =>{
                if(index === 0 && group.goodsItems.length === 1){
                  group.goodsItemIds = group.goodsItemIds+goodsItems.itemId
                }else if(index === group.goodsItems.length-1){
                  group.goodsItemIds = group.goodsItemIds+goodsItems.itemId
                }else{
                  group.goodsItemIds = group.goodsItemIds+goodsItems.itemId+","
                }
              })
            }
          }
        }
        if(this.formData.isDraft === 0 && rule.conditions.length === 0){
          this.$message.error("'"+rule.ruleName+"'规则"+"至少有一个条件")
          this.loading = false
          this.orderRule = false
          return
        }
        for(let i = 0;i<rule.conditions.length;i++){
          let condition = rule.conditions[i]
          if(this.formData.isDraft === 0 && (condition.conditionName == undefined || condition.conditionName.replace(/^\s+|\s+$/g,"") == "")){
            this.$message.error("'"+rule.ruleName+"'规则中的"+'条件标签不能为空！');
            this.loading = false;
            this.orderRule = false
            return
          }
          if(rule.moneyOrCount === 1 && rule.isCompose === 0){
            if(this.formData.isDraft === 0 && (condition.oneBuyMoney === undefined || condition.oneBuyMoney === '')){
              this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件一次性购买满金额不能为空！");
              this.loading = false;
              this.orderRule = false
              return
            }
          }else if(rule.moneyOrCount === 1 && rule.isCompose === 1){
            for(let j = 0;j<condition.groupConditions.length;j++){
              let groupCondition = condition.groupConditions[j]
              if(this.formData.isDraft === 0 && (groupCondition.oneBuyMoney === undefined || groupCondition.oneBuyMoney === '')){
                this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件中的"+"'"+rule.groups[j].label+"'分组一次性购买满金额不能为空！");
                this.loading = false;
                this.orderRule = false
                return
              }
              groupCondition.goodsItemIds = rule.groups[j].goodsItemIds
            }
          }else if(rule.moneyOrCount === 2 && rule.isCompose === 0){
            if(this.formData.isDraft === 0 && (condition.oneBuyCount === undefined || condition.oneBuyCount === '')){
              this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'一次性购买满数量不能为空！");
              this.loading = false;
              this.orderRule = false
              return
            }
          }else if(rule.moneyOrCount === 2 && rule.isCompose === 1){
            for(let j = 0;j<condition.groupConditions.length;j++){
              let groupCondition = condition.groupConditions[j]
              if(this.formData.isDraft === 0 && (groupCondition.oneBuyCount === undefined || groupCondition.oneBuyCount === '')){
                this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件中的"+"'"+rule.groups[j].label+"'分组一次性购买满数量不能为空！");
                this.loading = false;
                this.orderRule = false
                return
              }
              groupCondition.goodsItemIds = rule.groups[j].goodsItemIds
            }
          }
          if(rule.ruleType === 3 && condition.specialFlag === 1){ // 特价商品处理
            // if(rule.isCompose === 0){
              // condition.goodsItems && condition.goodsItems.forEach(goodsItem =>{
              //   condition.specials = []
              //   if(goodsItem.specialPrice !== undefined && goodsItem.specialPrice !== ''){
              //     condition.specials.push(goodsItem)
              //   }
              // })
            // }else{
            //   condition.groupConditions && condition.groupConditions.forEach(groupCondition =>{
            //     groupCondition.goodsItems.forEach(goodsItem =>{
            //       if(goodsItem.specialPrice !== undefined && goodsItem.specialPrice !== ''){
            //         condition.specials.push(goodsItem)
            //       }
            //     })
            //   })
            // }
            if(this.formData.isDraft === 0 && (condition.specials.length === 0)){
              this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件中的"+'未设置特价商品，请设置特价商品！');
              this.loading = false;
              this.orderRule = false
              return
            }
          }
          if((rule.ruleType === 3 && condition.specialFlag === 0) || rule.ruleType !==3){
            condition.specials = []
            if(condition.reduceOrPresent === 1){// 满减
              if(condition.reduceType === 1){ // 减免
                condition.discount = undefined
                condition.count = undefined
                if(this.formData.isDraft === 0 && (condition.reduction === undefined || condition.reduction === '' || condition.reduction === 0 || condition.reduction == '0')){
                  this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件减免金额不能为空或零！");
                  this.loading = false;
                  this.orderRule = false
                  return
                }
              }else{ // 折扣
                condition.reduction = undefined
                condition.count = undefined
                if(this.formData.isDraft === 0 && (condition.discount === undefined || condition.discount === '' || condition.discount === 0  || condition.discount == '0')){
                  this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件折扣不能为空或零！");
                  this.loading = false;
                  this.orderRule = false
                  return
                }
              }
            }else if(condition.reduceOrPresent === 2){ // 满赠
              if(this.formData.isDraft === 0 && (condition.presentCount === undefined || condition.presentCount == '0' || condition.presentCount == '' || condition.presentCount === 0)){
                this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件赠送数量不能为空或零！");
                this.loading = false;
                this.orderRule = false
                return
              }
              if(this.formData.isDraft === 0 && (condition.presents == undefined || condition.presents.length === 0)){
                this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+"'条件赠送的商品不能为空！");
                this.loading = false;
                this.orderRule = false
                return
              }else{
                for(let j = 0;j<condition.presents.length;j++){//判断赠送货品总数量
                  if(this.formData.isDraft === 0 && (condition.presents[j].totalCount === undefined || condition.presents[j].totalCount === 0 || condition.presents[j].totalCount === '0' || condition.presents[j].totalCount == '')){
                    this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+'条件赠送的货品'+condition.presents[j].itemCode+'总数量不能为空或零！');
                    this.loading = false;
                    this.orderRule = false
                    return
                  }else{
                    if(condition.presents[j].totalCount > condition.presents[j].totalNum){
                      this.$message.error("'"+rule.ruleName+"'规则中的"+"'"+condition.conditionName+'条件赠送的货品'+condition.presents[j].itemCode+'总数量不能大于库存数量！');
                      this.loading = false;
                      this.orderRule = false
                      return
                    }
                  }
                }
              }
            }
          }
        }
      }
      let hintStr = '保存成功'
      if(this.formData.isDraft === 0){
        hintStr = '保存提交成功'
        // if(this.needCheckFlag === 1){
        //   hintStr = '提交成功，待审批通过后方可生效'
        // }
        this.formData.applyStatus = 1 // 申请中
      }else{
        this.formData.applyStatus = 0 // 草稿
        hintStr = "保存草稿成功"
      }

      // this.formData.rules.forEach(rule => {
      //   let goods = []  
      //   if (rule.ruleType === 2) { // 商品
      //     rule.goodss.forEach(val => {
      //       goods.push({
      //         goodsId: val.goodsId,
      //         goodsNo: val.goodsNo
      //       })
      //     })
      //   } else if (rule.ruleType === 3) { // 货品
      //       // rule.goodsItems.forEach(val => {
      //     rule.goods.forEach(val => {  
      //       goods.push({
      //         goodsId: val.goodsId,
      //         goodsNo: val.goodsNo,
      //         itemId: val.itemId,
      //         itemCode: val.itemCode,
      //       })
      //     })
      //   }
      //   rule.goods = goods
      // })
      // this.formData.rules.forEach(rule => {
      //   delete rule.goods
      //   delete rule.goodss
      //   delete rule.groups
      // })
      if (!this.formData.distributorScopeNo) {
        this.formData.distributorScopeNo = 0;
      }

      if(this.formData.id !== undefined && this.formData.promoStatus === 0 && this.formData.applyStatus===0){ // 草稿修改
        //..活动促销修改
        
        this.formData.rules.forEach(rule => {
          if (rule.conditions && rule.conditions.length>0) {
            rule.conditions.forEach(con => {
              con.operationType = 2 // 修改
            })
          }
        })
        this.$http.editPromotion(this, this.formData).then(res => {
          if(res.success) {
            this.$message({
              message: hintStr,
              type: 'success',
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.cancel()
          }
        this.loading = false
        this.orderRule = false
        })
      }else{
        //..活动促销添加
        this.formData.rules.forEach(rule => {
          if (rule.conditions && rule.conditions.length>0) {
            rule.conditions.forEach(con => {
              con.operationType = 1  // 添加
            })
          }
        })
        this.$http.addPromotion(this, this.formData).then(res => {
          if(res.success) {
            this.$message({
              message: hintStr,
              type: 'success',
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.loading = false
            this.cancel()
          }
          this.loading = false
          this.orderRule = false
        })
      }
    },

    add (count) { //..新增规则操作
      if(this.formData.rules.length > 5){
        this.$message('最多只能添加6个规则！');
      } else {
        this.formData.rules.push({
          ruleName:"",
          ruleNameEn:"",
          ruleType:1,
          isEnjoy:0,
          isCompose:0,
          addUpFlag:1,
          moneyOrCount:1,
          groups:[{
            id:'1',
            isAdd:true,
            label:"分组1",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[],
          },{
            id:'2',
            isAdd:true,
            label:"分组2",
            goodsItemIds:'',
            goodss:[],
            goodsItems:[],
          }],
          goodss:[],
          goods:[],
          conditions:[{
            conditionName:'',
            specialFlag:0,
            moneyOrCount:1,
            isEnjoy:0,
            goodsItems:[],
            oneBuyCount:'',
            oneBuyMoney:'',
            reduceOrPresent:1,
            reductionPresentAddFlag:1,
            reduceType:1,
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
        })
        this.totalCount = count+1
      }
    },

    deleteRules(index){
      this.formData.rules.splice(index-1,1);
      this.totalCount = this.totalCount -1;
    },

    getChange(val) {
      if(this.count === 0){
        this.formData.distributorScope = val.distributorType
        this.formData.scalePriceIds = val.distributorGradeIds
        this.formData.businessIds  = val.distributorBusinessIds
        this.formData.departmentIds = val.distributorDepartmentIds
        this.formData.adminIds = val.distributorAdminIds
        this.formData.distributors = val.distributorIds
        this.distributorData = val.distributorData
      }else{
        this.count--
        if (!this.isDisabled) {
          this.formData.adminIds = val.distributorAdminIds
        }
      }
    },

    changeRule(rule,currentCount){
      this.formData.rules[currentCount-1] = rule;
    },
    // 活动审核详情
    getDetail () {
      this.$http.promotionCheckDetail(this, {id: this.$route.query.id}).then(res => {
        if (res.success) {
          this.approveData = res.data
          this.initData(res.data.promotion)
        }
      })
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
  .el-form1{
    width: 600px;
    margin-top: 20px;
    padding-left: 20px;
  }
  .el-promotion-rule{
    margin-top: 20px;
    padding-left: 20px;
    .add-promotion{
      margin-right: 100px;
      border-radius: 5px;
      background-color: #f8f8f8;
      .no-image{
        margin-left: 16px;
      }
    }
    .operation{
      margin-bottom: 10px;
      .btn-add{
        color: #fff;
        background-color: #21b8cb;
        border-radius: 6px;
      }
    }
  }
  .footbtn {
    padding-top: 30px;
    padding-bottom: 30px;
    text-align: center;
    .box-btn {
      // margin-left:40%
      margin-left: 10px;
    }
  }
  .box-has-border{
      overflow: hidden;
      &.step{
        margin-top:20px;
        background-color: #e7fafb;
      }
      .half-width{
        width: 50%;
        box-sizing: border-box;
        float: left;
        .el-form{
          /deep/.el-form-item{
            margin-bottom: 0;
          }
        }
      }
      .title {
				margin: 20px 0;
        text-align: center;
				font-size: 18px;
        border-top: 1px solid #dcdcdc;
			}
      .s-title{
        padding-top: 40px;
        text-align: center;
        font-size: 18px;
        border-top: 1px solid #dcdcdc;
      }
      .l-title{
        padding: 50px 0 20px;
        text-align: center;
        font-size: 18px;
        border-top: 1px solid #dcdcdc;
      }
      div.form{
        margin-top: 30px;
        margin-bottom: 40px;
        form.el-form{
          margin-right: 0;
          width: 80%;
          min-width: 800px;
          max-width: 1000px;
        }
      }
      .step-box{
        margin:40px auto;
        width:80%;
      }
  }
  .btn-box{
    margin: 30px;
    text-align: right;
    .el-steps{
      color:#21b8cb;
    }
    /deep/.el-step__title{
      &.is-success{
        color:#21b8cb;
      }
    }
  }
  .el-table{
    /deep/.cell{
      white-space: pre-line;
    }
  }
  .promotion-content{
    padding-bottom:40px;
  }
}
</style>
