<template>
  <div class="promotion">
    <div class="sales-promotion">
      <header>
        <h4>提货增长返利</h4>
      </header>
    </div>

    <div v-loading="loading2">
      <el-row class="body-header">
        <el-col class="title">活动可协助代销商更好的推广，带来更多的销量，当客户一次性购满设定的金额，可享有折扣优惠。</el-col>
      </el-row>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form1" :rules="rules" size="mini">
        <el-form-item label="活动名称:" prop="policyName">
          <el-input :disabled="disabled" v-model="formData.policyName" placeholder="不超过10个字" maxlength="10" />
        </el-form-item>

        <el-form-item label="规则描述:">
          <el-input :disabled="disabled" type="textarea" v-model="formData.description" />
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
                <el-date-picker :disabled="disabled"  v-show="formData.timeType === 2" v-model="formData.time" type="datetimerange" 
                  value-format="timestamp"  range-separator="至" start-placeholder="开始日期"  end-placeholder="结束日期">
                </el-date-picker>
              </template>
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="活动限制:" prop="fullMonths">
          <span class="pro_span">分销商合作满</span>
          <el-input class="pro_input" v-model="formData.fullMonths" :disabled="disabled" type="number" step="1" size="mini" @keyup.native="proving1"></el-input>
          <span class="pro_span">个月，才可参与提货增长返利</span>
        </el-form-item>

      </el-form>

      <el-form ref="formData" :model="formData" label-width="100px" class="el-form2" :rules="rules" size="mini">
        <el-form-item label="统一政策:">
          <div class="pick-rule">
            <div class="pick-meet">当月所有下单实际总额满足活动条件时，下月每笔订单可享有设定的折扣政策</div>
            <div class="pick-meet">下单总额是否包含在途订单
            <el-radio-group class="pick-whether" :disabled="disabled"  v-model="formData.includeOrderOnWay">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="2">否</el-radio>
            </el-radio-group>
            </div>
            <div class="add-rule">
              <button v-show="!disabled" class="mini-search-btn" @click.prevent="addRule()">添加规则</button>
              <!-- 统一政策规则 -->
              <div v-if="totalCount>0" class="add-box-rule">
                <div class="box-has-info" v-for="(rule,index) in formData.rules" :key="rule.id">
                  <div class="half-left">
                    <div class="half-left-body">
                      <div class="half-left-body-one">当月下单总额</div>
                      <div class="half-left-body-two">></div>
                      <div class="half-left-body-three">前 
                        <el-input class="half-left-body-four" :disabled="disabled" v-model="rule.monthNum" @input="rule.monthNum=/^\d+\.?\d{0,0}$/.test(rule.monthNum)||rule.monthNum == '' ? rule.monthNum:Number(rule.monthNum.toString().match(/^\d+(?:\.\d{0,0})?/))" size="mini" /> 个月平均下单总额
                      </div>

                      <div class="half-left-body-five">折扣类型
                        <el-tooltip content="还没有提示" effect="light" placement="top" >
                          <span class = "el-icon-question "></span>
                        </el-tooltip> 
                      </div>

                      <div class="half-left-body-six">
                        <el-radio-group v-model="rule.isDiscount" :disabled="disabled" @change="discountChange">
                          <el-radio :label="1" >打折</el-radio>
                          <el-radio :label="2" >减免</el-radio>
                        </el-radio-group>
                        <el-input class="half-left-body-seven" v-show="rule.isDiscount == 1" v-model="rule.discount"  @keyup.native="proving(index)" :disabled="disabled"></el-input>
                        <span v-show="rule.isDiscount == 1"> %</span>
                        <el-input class="half-left-body-eight" v-show="rule.isDiscount == 2" v-model="rule.reduction" @input="rule.reduction=/^\d+\.?\d{0,2}$/.test(rule.reduction)||rule.reduction == '' ? rule.reduction:Number(rule.reduction.toString().match(/^\d+(?:\.\d{0,1})?/))"  :disabled="disabled" type="number" min="0" step="0.01" />
                        <span class="half-left-body-nine"  v-show="rule.isDiscount == 2"> 元</span>
                      </div>
                    </div>

                    <!-- 折扣范围 -->
                    <div class="half-left-com"> <!-- distributorScope: 分销商可视范围 1 全部分销商 2 分销商等级 3 指定分销商 -->
                      <distributor ref="distributor" :index="index" :type="rule.distributorScope" :gIds="rule.distributorGradeIds" :dIds="rule.distributorIds" @change="getChange" :disabled="disabled"></distributor>
                    </div>
                  </div>

                  <div class="half-right">
                    <div class="half-right-header" align="center">
                      <button class="mini-delete-btn" v-show="!disabled" @click.prevent="deleteRules(index)" size="mini">删除</button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="add-box-else" v-else> 暂无数据，请先添加返利规则 </div>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <div class="clearfix footbtn">
        <el-button class="mini-search-btn footbtn_btn" size="mini" type="success" @click="handleSave()" :loading="loading">{{disabled?'编辑':'保存'}}</el-button>
        <el-button size="mini" @click="cancel()" v-show="!disabled">取消</el-button>
      </div>
    </div>

  </div>
  
</template>

<script>
import { addPickupRate, getPickupRate } from '@/views/marketingPromotion/promotionData'
import distributor from '@/views/marketingPromotion/compomemts/distributor/discountRange.vue'
export default {
  name: 'pickUpRatePromotion',
  components: {
    distributor
  },

  created() {
    this.getParams()
    this.checkDetail()
  },

  data() {
    return {
      loading2: false,
      disabled: false,
      totalCount: 1,
      loading: false,
      needCheckFlag: 0,
      time: [new Date().getTime(), new Date().getTime()],
      distributorData: [],
      formData: {
        policyName: '',
        description: '',
        enable: 1,
        timeType: 1,
        fullMonths: '',
        includeOrderOnWay: 2,
        startTime: new Date().getTime,
        endTime: new Date().getTime,
        rules: [{
          monthNum: '',
          isDiscount: 1, // 折扣类型 1 打折 2 减免
          moneyOrCount: 1,
          discount: '',
          reduction: '',
          distributorScope: 1, // 分销商可视范围 1 全部分销商 2 分销商等级 3 指定分销商
          distributorGradeIds: [],
          distributorIds: [],
          distributorData: []
        }],
        time: [new Date().getTime(), new Date().getTime()]
      },
      rules: {
        policyName: [{
          required: true,
          message: '请输入活动名称',
          trigger: 'blur'
        }],
        fullMonths: [{
          required: true,
          message: '请输入活动限制时间',
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
    discountChange(event) {
      this.$forceUpdate() // 手动更新视图
    },
    proving1() { // 输入验证
      this.formData.fullMonths = this.formData.fullMonths.replace(/[^\.\d]/g, '')
      this.formData.fullMonths = this.formData.fullMonths.replace('.', '')
      if (Number(this.formData.fullMonths) > 100) {
        this.formData.fullMonths = 100
        this.$message.warning('合作时间最长不能超过100个月')
      } else if (Number(this.formData.fullMonths < 2)) {
        this.formData.fullMonths = 2
        this.$message.warning('合作时间最少为满两个月')
      }
    },
    // ======== 操作 ========
    handleSave() { // 编辑/保存操作
      this.loading = true
      if (this.disabled) { // 编辑
        this.disabled = false
        this.loading = false
      } else { // 保存
        if (this.formData.policyName === undefined || this.formData.policyName.replace(/^\s+|\s+$/g, '') === '') {
          this.$message.error('活动名称不能为空！')
          this.loading = false
          return
        }
        // if(this.formData.description == undefined || this.formData.description.replace(/^\s+|\s+$/g,"") == ""){
        //     this.$message.error('活动规则描述不能为空！');
        //     this.loading = false;
        //     return
        // }
        if (this.formData.timeType === 1) {
          this.formData.startTime = undefined
          this.formData.endTime = undefined
        } else if (this.formData.timeType === 2) {
          this.formData.startTime = this.formData.time[0]
          this.formData.endTime = this.formData.time[1]
          if (this.formData.startTime === undefined || this.formData.startTime === 0 || this.formData.endTime === undefined || this.formData.endTime === 0) {
            this.$message.error('活动开始时间或结束时间不能为零或空！')
            this.loading = false
            return
          }
        }
        let b = true
        if (this.formData.rules.length > 0) { // 返利规则不能为空
          this.loading = false
        } else {
          this.$message.warning('至少有一个返利规则！')
          this.loading = false
          b = false
          return
        }
        this.formData.rules.forEach((item, index) => {
          if (item.monthNum === undefined || item.monthNum === 0 || item.monthNum === '') {
            this.$message.error('前多少个月不能为空或零！')
            this.loading = false
            b = false
            return
          }
          if (item.isDiscount === 1) { // 折扣类型 1 打折 2 减免
            if (item.discount === undefined || item.discount === 0 || item.discount === '' || item.discount == null) {
              this.$message.error('折扣数值不能为空或零！')
              this.loading = false
              b = false
              return
            }
            item.reduction = undefined
          } else {
            // eslint-disable-next-line no-cond-assign
            if (item.reduction = undefined || item.reduction === 0 || item.reduction === '' || item.reduction == null) {
              this.$message.error('减免金额不能为空或零！')
              this.loading = false
              b = false
              return
            }
            item.discount = undefined
          }
          if (item.distributorScope === 0 || item.distributorScope === 1) { // 分销商可视范围 1 全部分销商
            item.distributorGradeIds = ''
            item.distributorIds = ''
          } else if (item.distributorScope === 2) { // 分销商可视范围 2 分销商等级
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
          } else if (item.distributorScope === 3) { // 分销商可视范围 3 指定分销商
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
        if (!b) {
          return
        }
        let hintStr = '保存成功'
        if (this.needCheckFlag === 1) {
          hintStr = '提交成功，需审批通过后方可生效'
        }
        this.formData.fullMonths = Number(this.formData.fullMonths)
        addPickupRate(this, this.formData).then(res => {
          if (res.code === 0) {
            this.$message({
              message: hintStr,
              type: 'success',
              duration: 3 * 1000,
              onClose: () => { }
            })
            if (res.id) { // 有返回ID进行跳转至审批详情
              this.$router.push({ name: 'approvePickUpRatePromotion', params: { id: res.id }})
            } else {
              this.cancel()
              this.disabled = true
            }
          }
          this.loading = false
        })
      }
    },

    cancel() { // 取消操作
      this.disabled = true
      this.getParams()
    },

    addRule() { // 添加规则
      this.formData.rules.push({
        monthNum: '',
        moneyOrCount: 1,
        isDiscount: 1,
        discount: '',
        reduction: '',
        distributorScope: 1,
        distributorGradeIds: [],
        distributorIds: [],
        distributorData: []
      })
      this.totalCount = this.totalCount + 1
    },

    deleteRules(index) { // 删除操作
      if (this.formData.rules.length > 1) {
        this.formData.rules.splice(index - 1, 1)
        this.totalCount = this.totalCount - 1
      } else {
        this.$message.warning('至少有一个返利规则！')
      }
    },

    // ======== 数据 ========
    getParams() { // 提货增长规则详情
      this.loading2 = true
      this.loading = false
      this.disabled = false
      getPickupRate(this).then(res => {
        res.code === 0 ? this.loading2 = false : this.loading2 = false
        if (res.code === 0 && res.policy !== undefined) {
          this.disabled = true
          res.policy.time = []
          this.formData = res.policy
          if (this.formData.startTime !== undefined && this.formData.startTime !== 0 && this.formData.startTime !== null && this.formData.endTime !== undefined && this.formData.endTime !== 0 && this.formData.endTime !== null) {
            this.formData.time = [this.formData.startTime, this.formData.endTime]
          } else {
            this.formData.time = [new Date().getTime(), new Date().getTime()]
          }
          this.totalCount = this.formData.rules.length
          this.formData.rules.forEach(item => { // 初始化分销商等级或分销商
            if (item.discount == null) { // 打折为空则为减免
              item.isDiscount = 2
            } else if (item.reduction == null) { // 减免为空则为打折
              item.isDiscount = 1
            }
            item.distributorData = []
            if (item.distributorIds !== undefined && item.distributorIds !== '') {
              item.distributorIds = item.distributorIds.split(',')
            } else {
              item.distributorIds = []
            }
            if (item.distributorGradeIds !== undefined && item.distributorGradeIds !== '') {
              const arr = item.distributorGradeIds.split(',')
              item.distributorGradeIds = []
              for (let i = 0; i < arr.length; i++) {
                item.distributorGradeIds.push(Number(arr[i]))
              }
            } else {
              item.distributorGradeIds = []
            }
          })
        }
      })
    },

    checkDetail() { // 是否开启审批
      this.$api.get(this, 'admin/u/po/checkDetail').then(res => {
        for (let i = 0; i < res.checkConfigs.length; i++) {
          if (res.checkConfigs[i].ext === 12) {
            this.needCheckFlag = res.checkConfigs[i].isOpen
          }
        }
      })
    },

    proving(index) { // 正则验证
      this.formData.rules[index].discount = this.formData.rules[index].discount.replace(/[^\.\d]/g, '')
      this.formData.rules[index].discount = this.formData.rules[index].discount.replace('.', '')
      if (this.formData.rules[index].discount > 100) {
        this.formData.rules[index].discount = 0
        this.$message.error('1-100(正整数）!')
      }
    },

    getChange(val, index) { // 统一政策折扣范围子组件传上来的值
      this.formData.rules[index].distributorScope = val.distributorType // 折扣范围
      this.formData.rules[index].distributorGradeIds = val.distributorGradeIds // 指定分销商ID
      this.formData.rules[index].distributorIds = val.distributorIds
      this.formData.rules[index].distributorData = val.distributorData
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
  }
  .body-header {
    line-height: 40px;
    color: $lakeBlue;
    font-weight: 700;
    padding-left: 20px;
    .title{
      font-size: 12px;
    }
  }
  .el-form1{
    width: 90%;
    padding-left: 20px;
    .pro_span {
      font-size: 14px;
      color:#606266;
    }
    .pro_input {
      width: 100px;
    }
  }
  .el-form2{
    width: 90%;
    padding-left: 20px;
    .pick-rule{
      border-radius: 5px;
      margin-top: 8px;
      padding: 16px;
      margin-bottom: 5px;
      border: 1px solid #ccc;
      background-color: #f8f8f8;
      .pick-meet {
        color: #606266;
        font-weight: 500;
      }
      .pick-whether {
        margin-left: 10px;
      }
      .add-rule{
        margin-bottom: 10px;
        margin-top: 10px;
        .add-box-rule {
          margin-top: 10px;
          width:100%;
          .box-has-info{
            width: 100%;
            border-radius: 5px;
            margin-bottom: 5px;
            border: 1px solid #ccc;
            background-color: #fff;
            overflow: hidden;
            .half-left{
              line-height:50px;
              width: 90%;
              float: left;
              .half-left-body {
                width:1000px;
                .half-left-body-one {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left: 10px;
                }
                .half-left-body-two {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left: 20px;
                  margin-right: 20px;
                }
                .half-left-body-three {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  .half-left-body-four {
                    color: #606266;
                    font-weight: 500;
                    width: 50px;
                    font-size: 12px;
                    text-align: center;
                  }
                }
                .half-left-body-five {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left: 50px;
                }
                .half-left-body-six {
                  color: #606266;
                  font-weight: 500;
                  float: left;
                  margin-left:20px;
                  display:inline;
                  .half-left-body-seven {
                    color: #606266;
                    font-weight: 500;
                    width: 100px;
                    margin-left:10px;
                  }
                  .half-left-body-eight {
                    color: #606266;
                    font-weight: 500;
                    width: 100px;
                    margin-left:10px;
                  }
                  .half-left-body-nine {
                    color: #606266;
                    font-weight: 500;
                  }
                }
              }
              .half-left-com {
                float: left;
                overflow: hidden;
                padding-top: 10px;
              }
            }
            .half-right{
              height: 100%;
              width: 10%;
              float: right;
              .half-right-header {
                text-align:center;
                height:50px;
                line-height:50px
              }
            }
            .el-radio+.el-radio{
              margin-left: 10px;
            }
          }
        }
        .add-box-else {
          padding:10px;
          color: #606266;
        }
      }
    }
  }
  .footbtn {
    padding-top: 30px;
    padding-bottom: 30px;
    .footbtn_btn {
      margin-left: 45%;
    }
  }
}
.el-promotion-rule{
  width: 100%;
  margin-top: 30px;
  padding-left: 20px;
}
</style>
