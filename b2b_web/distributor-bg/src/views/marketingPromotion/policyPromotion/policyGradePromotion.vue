<template>
  <div class="promotion">
    <div class="sales-promotion">
      <header>
        <h4>商品等级折扣</h4>
      </header>
    </div>

    <div v-loading="loading2">
      <el-row class="body-header">
        <el-col class="title">活动可协助代销商更好的推广，带来更多的销量，当客户一次性购满设定的金额，可享有折扣优惠。</el-col>
      </el-row>

      <el-form ref="formData" :model="formData" label-width="120px" class="el-form1" :rules="rules" size="mini" >
        <el-form-item label="活动名称:" prop="policyName">
          <el-input :disabled="disabled" v-model="formData.policyName" placeholder="不超过10个字" maxlength="10" />
        </el-form-item>

        <el-form-item label="英文名称:" prop="policyNameEn">
          <el-input :disabled="disabled" v-model="formData.policyNameEn" placeholder="不超过10个字" maxlength="10" />
        </el-form-item>

        <el-form-item label="规则描述:">
          <el-input :disabled="disabled" type="textarea" v-model="formData.description"/>
        </el-form-item>

        <el-form-item label=" 在途是否参与:">
          <el-radio-group :disabled="disabled" v-model="formData.onWayAttendEventFlag">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="启用状态:">
          <el-radio-group :disabled="disabled" v-model="formData.enable">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="2">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        

        <el-form-item label="活动时间:" prop="time">
          <el-radio-group :disabled="disabled" v-model="formData.timeType">
            <el-radio :label="1">长期</el-radio>
            <el-radio :label="2">
              具体时间范围
              <template>
                <el-date-picker
                  :disabled="disabled"
                  v-show="formData.timeType === 2"
                  v-model="formData.time"
                  type="datetimerange"
                  value-format="timestamp"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                ></el-date-picker>
              </template>
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini" >
        <el-form-item class="add-policy">
          <div class="operation">
            <button class="mini-search-btn add-policy-one" @click.prevent="addDiscountRule(discountTotalCount)"  v-if="!disabled" >添加规则</button>
            <button class="mini-search-btn policy-open-an" @click.prevent="allDiscountSpread(true)">全部展开</button>
            <button class="mini-search-btn policy-down-an" @click.prevent="allDiscountSpread(false)">全部收起</button>
          </div>
        </el-form-item>

        <el-form-item label="折扣设置:">
            <div class="add-promotion">
              <!-- 商品等级折扣规则详情 -->
              <div v-if="discountTotalCount > 0">
                <component ref="component1" :productlines="productlines" @deleteRules="deleteDiscountRules" :is="discountRule" :totalCount="discountTotalCount" v-for="(detail,index) in formData.details" :key="detail.id" :currentCount="index+1" v-model="formData.details" :detail="detail" @changeRule="changeDiscountRule" @changeCategory="changeCategory" :disabled="disabled" ></component>
              </div>
              <div v-else class="no-image">暂时没有添加折扣规则，请先添加新折扣规则</div>
            </div>
          </el-form-item>
      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini" >
        <el-form-item class="add-policy">
          <div class="operation">
            <button class="mini-search-btn add-policy-one" @click.prevent="addPolicyRule(policyTotalCount)"  v-if="!disabled" >添加政策</button>
            <button class="mini-search-btn policy-open-an" @click.prevent="allPolicySpread(true)">全部展开</button>
            <button class="mini-search-btn policy-down-an" @click.prevent="allPolicySpread(false)">全部收起</button>
          </div>
        </el-form-item>

        <el-form-item label="统一政策:">
          <div class="add-promotion">
            <!-- 统一政策详情 -->
            <div v-if="policyTotalCount > 0">
              <component ref="component2" :distributorScope="rule.distributorScope" @deleteRules="deletePolicyRules" :is="policyRule" :totalCount="policyTotalCount" v-for="(rule,index) in formData.rules" :key="rule.id" :currentCount="index+1" v-model="formData.rules" :rule="rule" @changeRule="changePolicyRule" :disabled="disabled" ></component>
            </div>
            <div v-else class="no-image">暂时没有添加政策，请先添加新政策</div>
          </div>
        </el-form-item>
      </el-form>

      <div class="clearfix footbtn">
        <el-button class="mini-search-btn" size="mini" style="margin-left: 45%;" type="success" @click="handleSave()" :loading="loading" >{{disabled?'编辑':'保存'}}</el-button>
        <el-button size="mini" @click="cancel()" v-show="!disabled">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import Vue from 'vue'
import discountRule from '@/views/marketingPromotion/compomemts/discountRule'
import policyRule from '@/views/marketingPromotion/compomemts/policyRule'
import { addGradeDiscount, getradeDiscount } from '@/views/marketingPromotion/promotionData'
export default {
  name: 'policyGradePromotion',
  components: {
    discountRule,
    policyRule
  },
  created() {
    this.$http.getCategoryList(this, { page: 1, size: 1000, openFlag: 1 }).then(res => {
      if (res.success) {
        res.data.list.forEach(item =>{
          item.disabled = false
        })
        this.productlines = res.data.list
        this.getParams()
      }
    })
    this.checkDetail()
  },
  data() {
    return {
      ArrCopy1: [],
      loading2: false,
      disabled: true,
      productlines:[],
      policyTotalCount: 1,
      discountTotalCount: 1,
      needCheckFlag: 2,
      policyRule: 'policy-rule',
      discountRule: 'discount-rule',
      loading: false,
      time: [new Date().getTime(), new Date().getTime()],
      formData: {
        policyName: '',
        description: '',
        enable: 1,
        timeType: 1,
        onWayAttendEventFlag:0,
        startTime: new Date().getTime,
        endTime: new Date().getTime,
        abdc: '',
        rules: [{
          description: '',
          moneyOrCount: 1,
          oneBuyMoney: 0,
          oneBuyCount: 0,
          distributorType: 1,
          distributorScope: 1,
          distributorGradeIds: [],
          distributorIds: [],
          distributorData: [],
          discountBeforeAfter:2,
          isSpread: true,
        }],
        details: [{
          isSpread: true,
          categoryIds:[],
          detailsArr:[{
              gradeId: '',
              discount: 0
            }]
        }],
        time: [new Date().getTime(), new Date().getTime()],
      },
      rules: {
        policyName: [{
          required: true,
          message: '请输入活动名称',
          trigger: 'blur'
        }],
        description: [{
          required: true,
          message: '请输入活动规则描述',
          trigger: 'blur'
        }]
      }
    }
  },
  methods: {
    // ======== 操作 ========
    allPolicySpread(isSpread) { // 全部展开收起操作
      this.formData.rules.forEach(item => {
        item.isSpread = isSpread
      })
    },
    changeCategory(){
      this.productlines.forEach(res =>{
        let b = false
        this.formData.details.forEach(item =>{
          item.categoryIds.forEach(value =>{
              if(value === res.id){
                b = true
              }
          })
        })
        res.disabled = b
      })
    },
    allDiscountSpread(isSpread) { // 全部展开收起操作
      this.formData.details.forEach(item => {
        item.isSpread = isSpread
      })
    },

    handleSave() { // 编辑/保存操作
      this.loading = true
      if (this.disabled) { // 编辑
        this.disabled = false
        this.loading = false
      } else { // 保存
        let u = true
        this.formData.rules.forEach(item => {
          if (item.details === 0) {
            this.$message.error('等级折扣规则不能为空！请先添加等级折扣规则')
            this.loading = false
            u = false
            return
          }
        })

        if (this.formData.policyName === undefined || this.formData.policyName.replace(/^\s+|\s+$/g, '') === '') {
          this.$message.error('活动名称不能为空！')
          this.loading = false
          return
        }
        if (this.formData.description === undefined || this.formData.description.replace(/^\s+|\s+$/g, '') === '') {
          this.$message.error('活动规则描述不能为空！')
          this.loading = false
          return
        }
        if (this.formData.timeType === 1) { // 活动时间 1.长期 2.提前
          this.formData.startTime = undefined
          this.formData.endTime = undefined
        } else {
          this.formData.startTime = this.formData.time[0]
          this.formData.endTime = this.formData.time[1]
          if (this.formData.startTime === undefined || this.formData.startTime === 0 || this.formData.endTime === undefined || this.formData.endTime === 0) {
            this.$message.error('活动开始时间或结束时间不能为零或空！')
            this.loading = false
            return
          }
        }
        let b = true
        this.formData.rules.forEach((item, index) => {
          if (item.moneyOrCount === 1 && (item.oneBuyMoney === undefined || item.oneBuyMoney === '')) {
            this.$message.error('统一政策' + (index + 1) + '一次性购买满金额不能为空！')
            this.loading = false
            b = false
            return
          } else if (item.moneyOrCount === 2 && (item.oneBuyCount === undefined || item.oneBuyCount === '')) {
            this.$message.error('统一政策' + (index + 1) + '一次性购买满数量不能为空！')
            this.loading = false
            b = false
            return
          }
          if (item.distributorScope === 0 || item.distributorScope === 1) {
            item.distributorGradeIds = ''
            item.distributorIds = ''
          } else if (item.distributorScope === 2) {
            if (item.distributorGradeIds === undefined || item.distributorGradeIds === '') {
              this.$message.error('指定会员，请至少指定一个分销商等级！')
              this.loading = false
              b = false
              return
            } else {
              if (item.distributorGradeIds instanceof Array) {
                item.distributorGradeIds = item.distributorGradeIds.join(',')
              }
              item.distributorIds = ''
            }
          } else if (item.distributorScope === 3) {
            if (item.distributorData.length === 0) {
              this.$message.error('指定会员，请至少指定一个分销商！')
              this.loading = false
              b = false
              return
            } else {
              item.distributorGradeIds = ''
              for (let i = 0; i < item.distributorData.length; i++) {
                if (i === 0) {
                  item.distributorIds = String(item.distributorData[i].id)
                } else {
                  item.distributorIds = item.distributorIds + ',' + String(item.distributorData[i].id)
                }
              }
            }
          }
        })
        this.formData.details.forEach((item1, index)=> {
          if (item1.id === 0) {
            this.$message.error('折扣规则' + (index + 1) + '商品等级不能为空！')
            this.loading = false
            b = false
            return
          }
          item1.detailsArr.forEach(item3 => {
            if (item3.discount === undefined || item3.discount === '' || item3.discount === 0) {
              this.$message.error('折扣规则' + (index + 1) + '商品等级折扣不能为零或空！')
              this.loading = false
              this.$forceUpdate()
              b = false
              return
            }
          })
        })
        if (!b) {
          return
        }
        if (!u) {
          return
        }
        let hintStr = ''
        if (this.needCheckFlag === 1) {
          hintStr = '提交成功，需审批通过后方可生效'
        } else if (this.needCheckFlag === 2) {
          hintStr = '保存成功'
        }
        /**
         * 获取选择类目的IDS拼接并赋值
         * begin
         */
        const formDataCopy = JSON.parse(JSON.stringify(this.formData))
        const ary = []
        formDataCopy.details.forEach((item, index) => {
          item.categoryIds = item.categoryIds.join(',')
          item.detailsArr.forEach(item1 => {
            ary.push({
              categoryIds:item.categoryIds,
              gradeId:item1.gradeId,
              discount:item1.discount
            })
          })
        })
        formDataCopy.details = ary
        // ..the end
        return Promise.all([
          addGradeDiscount(this, formDataCopy).then(res => { // 统一政策(等级折扣)添加
            if (res.code === 0) {
              this.$message({
                message: hintStr,
                type: 'success',
                duration: 3 * 1000,
                onClose: () => { }
              })
              this.getParams()
              if (res.id) { // 有返回ID进行跳转至审批详情
                this.$router.push({ name: 'approvePolicyPromotion', params: { id: res.id, approveType: true }})
              } else {
                this.cancel()
                this.disabled = true
              }
            }
            this.loading = false
          })
        ])
      }
    },

    addDiscountRule(count){// 添加折扣规则
      this.formData.details.push({
        categoryIds:[],
        isSpread: true,
        detailsArr:[{
          gradeId: '',
          discount: 0
        }]
      })
      this.discountTotalCount = count + 1
    },

    addPolicyRule(count) { // 添加政策操作
      this.formData.rules.push({
        description: '',
        moneyOrCount: 1,
        oneBuyMoney: 0,
        oneBuyCount: 0,
        distributorType: 1,
        distributorScope: 1,
        distributorGradeIds: [],
        distributorIds: [],
        distributorData: [],
        isSpread: true
      })
      this.policyTotalCount = count + 1
    },

    cancel() { // 取消操作
      // this.disabled = true
      this.getParams()
    },
    // ======== 数据 ========

    getParams() { // 统一政策(等级折扣)详情
      this.loading = false
      this.disabled = false
      this.loading2 = true
      getradeDiscount(this).then(res => {
        res.code === 0 ? this.loading2 = false : this.loading2 = false
        if (res.code === 0 && res.policy !== undefined) {
          this.disabled = true
          res.policy.time = []
          res.policy.rules.forEach(item => {
            item.isSpread = true
          })
          res.policy.details = this.fun(res.policy.details)
          if (res.policy.startTime !== undefined && res.policy.startTime !== 0 && res.policy.startTime !== null && res.policy.endTime !== undefined && res.policy.endTime !== 0 && res.policy.endTime !== null) {
            res.policy.time = [res.policy.startTime, res.policy.endTime]
          } else {
            res.policy.time = [new Date().getTime(), new Date().getTime()]
          }
          res.policy.rules.forEach(item => { // 初始化分销商等级或分销商
            item.distributorData = []
            if (item.distributorIds !== undefined && item.distributorIds !== '' && item && item.distributorIds) {
              const arr = item.distributorIds.split(',')
              item.distributorIds = []
              for(let i = 0; i < arr.length; i++){
                item.distributorIds.push(Number(arr[i]))
              }
            } else {
              item.distributorIds = []
            }
            if (item.distributorGradeIds !== undefined && item.distributorGradeIds !== '' && item && item.distributorGradeIds) {
              const arr = item.distributorGradeIds.split(',')
              item.distributorGradeIds = []
              for (let i = 0; i < arr.length; i++) {
                item.distributorGradeIds.push(Number(arr[i]))
              }
            } else {
              item.distributorGradeIds = []
            }
          })
          this.formData = res.policy
          this.policyTotalCount = res.policy.rules.length
          this.discountTotalCount = res.policy.details.length
        }else{
          formData = {
            policyName: '',
            description: '',
            enable: 1,
            timeType: 1,
            onWayAttendEventFlag:0,
            startTime: new Date().getTime,
            endTime: new Date().getTime,
            abdc: '',
            rules: [{
              description: '',
              moneyOrCount: 1,
              oneBuyMoney: 0,
              oneBuyCount: 0,
              distributorType: 1,
              distributorScope: 1,
              distributorGradeIds: [],
              distributorIds: [],
              distributorData: [],
              discountBeforeAfter:2,
              isSpread: true,
            }],
            details: [{
              isSpread: true,
              categoryIds:[],
              detailsArr:[{
                  gradeId: '',
                  discount: 0
                }]
            }],
            time: [new Date().getTime(), new Date().getTime()],
          }
        }
      })
    },

    deletePolicyRules(index) { // ..删除统一政策数目
      this.formData.rules.splice(index - 1, 1)
      this.policyTotalCount = this.policyTotalCount - 1
    },

    deleteDiscountRules(index) { // ..删除折扣规则数目
      this.formData.details.splice(index - 1, 1)
      this.discountTotalCount = this.discountTotalCount - 1
    },

    changePolicyRule(rule, currentCount) {
      this.formData.rules[currentCount - 1] = rule
    },
    changeDiscountRule(rule, currentCount) {
      this.formData.details[currentCount - 1] = rule
    },

    checkDetail() { // 是否开启等级审批
      this.$api.get(this, 'admin/u/po/checkDetail').then(res => {
        for (let i = 0; i < res.checkConfigs.length; i++) {
          if (res.checkConfigs[i].ext === 11) {
            this.needCheckFlag = res.checkConfigs[i].isOpen
          }
        }
      })
    },

    groupBy(arr, property) {
      return arr.reduce(function(memo, x) {
        if (!memo[x[property]]) {
          memo[x[property]] = []
        }
        memo[x[property]].push(x)
        return memo
      }, {})
    },

    arrayUnique2(attr, name) { // 对象数组根据某个属性去重
      var hash = {}
      return attr.reduce(function(item, next) {
        hash[next[name]] ? '' : hash[next[name]] = true && item.push(next)
        return item
      }, [])
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
    }
  }
}
</script>

<style lang="scss" scoped>
.promotion {
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
  }
  .body-header {
    line-height: 40px;
    color: $lakeBlue;
    font-weight: 700;
    padding-left: 20px;
    .title {
      font-size: 12px;
    }
  }
  .el-form1 {
    width: 90%;
    margin-top: 30px;
    padding-left: 20px;
    .add-policy {
      margin-bottom: 5px;
      .operation {
        margin-bottom: 10px;
        margin-top: 5px;
        overflow: hidden;
        .add-policy-one {
          float: left;
        }
        .policy-open-an {
          float: right;
          margin-left: 10px;
        }
        .policy-down-an {
          float: right;
        }
      }
    }
    .add-promotion {
      border-radius: 5px;
      // background-color: #f8f8f8;
      .no-image {
        margin-left: 16px;
      }
    }
  }
  .footbtn {
    padding-top: 30px;
    padding-bottom: 30px;
  }
}
</style>
