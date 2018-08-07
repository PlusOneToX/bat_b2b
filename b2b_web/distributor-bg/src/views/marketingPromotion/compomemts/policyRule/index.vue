<template>
  <div class="policy-rule">
    <el-form label-width="80px" class ="pomotion" ref="rule" :model="rule">
      <el-form-item :label="'统一政策 '+currentCount" class="pom_head">
        <button v-show="!disabled" class="rule-delete mini-delete-btn rule_dle" size="mini" @click.prevent="handleDeleteRule(currentCount)">删除</button>
        <button v-show="rule.isSpread" class="rule-delete mini-search-btn" size="mini" @click.prevent="isSpread()"><span class="el-icon-arrow-up"></span></button> <!-- 收起 -->
        <button v-show="!rule.isSpread" class="rule-delete mini-search-btn" size="mini" @click.prevent="isSpread()"><span class="el-icon-arrow-down"></span></button><!-- 展开 -->
      </el-form-item>

      <el-form-item class="rule_describe" v-if="rule.isSpread" label="政策描述:" prop="label">
        <el-input v-model="rule.description" class="rule_des_input" type="textarea" :disabled ="disabled" size="mini" maxlength="20"></el-input>
      </el-form-item>

      <el-form-item class="rule_describe" v-if="rule.isSpread" label-width="10px">
        <el-form ref="rule" :model="rule" >
          <el-form-item label="政策条件:" class="rule_conditions" >
            <el-radio-group v-model="rule.moneyOrCount" :disabled ="disabled" @change="conditions">
              <el-radio :label="2">按数量</el-radio>
              <el-radio :label="1">按金额</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item v-if="discountBeAfShow" label="折扣前后:" class="rule_describe1" prop="moneyOrCount">
            <el-radio-group :disabled ="disabled" v-model="rule.discountBeforeAfter">
              <el-radio :label="1">折扣前</el-radio>
              <el-radio :label="2">折扣后</el-radio>
            </el-radio-group>
            <el-input class="rule_desc_input" v-if="rule.moneyOrCount == 1" size="mini" v-model="rule.oneBuyMoney" @input="rule.oneBuyMoney=/^\d+\.?\d{0,2}$/.test(rule.oneBuyMoney)||rule.oneBuyMoney == '' ? rule.oneBuyMoney:Number(rule.oneBuyMoney.toString().match(/^\d+(?:\.\d{0,1})?/))" :disabled ="disabled" type="number" min="0" step="0.01"></el-input>
            <el-input class="rule_desc_input" v-else size="mini" v-model="rule.oneBuyCount" @input="rule.oneBuyCount=/^\d+\.?\d{0,0}$/.test(rule.oneBuyCount)||rule.oneBuyCount == '' ? rule.oneBuyCount:Number(rule.oneBuyCount.toString().match(/^\d+(?:\.\d{0,0})?/))" :disabled ="disabled" type="number" min="0"></el-input>
            <span class="rule_desc_span"> 元/数量</span>
          </el-form-item>

          <el-form-item v-else label="一次性购买：" class="rule_describe1" prop="moneyOrCount">
            <el-input class="rule_desc_input" v-if="rule.moneyOrCount == 1" size="mini" v-model="rule.oneBuyMoney" @input="rule.oneBuyMoney=/^\d+\.?\d{0,2}$/.test(rule.oneBuyMoney)||rule.oneBuyMoney == '' ? rule.oneBuyMoney:Number(rule.oneBuyMoney.toString().match(/^\d+(?:\.\d{0,1})?/))" :disabled ="disabled" type="number" min="0" step="0.01"></el-input>
            <el-input class="rule_desc_input" v-else size="mini" v-model="rule.oneBuyCount" @input="rule.oneBuyCount=/^\d+\.?\d{0,0}$/.test(rule.oneBuyCount)||rule.oneBuyCount == '' ? rule.oneBuyCount:Number(rule.oneBuyCount.toString().match(/^\d+(?:\.\d{0,0})?/))" :disabled ="disabled" type="number" min="0"></el-input>
            <span class="rule_desc_span">数量</span>
          </el-form-item>
        </el-form>
      </el-form-item>
    </el-form>
		<el-form label-width="100px" ref="rule" :model="rule" v-if="rule.isSpread">
			<el-form-item label="分销商类型:" prop="moneyOrCount" >
				<el-radio-group v-model="rule.distributorType" :disabled ="disabled">
					<el-radio :label="1">国内外分销商</el-radio>
					<el-radio :label="2">国内分销商</el-radio>
					<el-radio :label="3">国外分销商</el-radio>
				</el-radio-group>
			</el-form-item>
		</el-form>
		<!-- 活动范围 -->
		<distributor v-if="rule.isSpread" ref="distributor" :type="rule.distributorScope" :gIds="rule.distributorGradeIds" :dIds="rule.distributorIds" @change="getChange" :disabled="disabled" :ruleType="rule.distributorType" :isUse="true"></distributor>
	</div>
</template>


<script type="text/javascript">
import distributor from '@/views/marketingPromotion/compomemts/distributor'
import { getPoGradeDetail } from '@/views/goods/goodGrade/goodGradeData.js'
export default {
  props: {
    totalCount: {
      type: Number,
      default: 0
    },
    currentCount: {
      type: Number,
      default: 0
    },
    rule: {
      description: '',
      moneyOrCount: 1,
      discountBeforeAfter: 2,
      oneBuyMoney: 0,
      oneBuyCount: 0,
      distributorType: 1,
      distributorScope: 1,
      distributorData: [],
      isSpread: true
    },
    disabled: true
    
    // detailsArr: []
  },
  components: {
    distributor
  },
  data() {
    return {
      pageInfo: {
        originalGradeId: ''
      },
      count: 3,
      discountBeAfShow: true,
      selectIndex:0
    }
  },
  created() {
    this.conditions(this.rule.moneyOrCount)
  },
  mounted() {
    if (this.$refs && this.$refs.distributor && this.$refs.distributor.setDistributor()) {
      this.$refs.distributor.setDistributor(this.rule)
    }
  },
  methods: {
    changeIndex(index){
      this.selectIndex = index
    },

    isSpread() { // 收起/展开统一政策详情操作
      this.rule.isSpread = !this.rule.isSpread
    },

    handleDeleteRule(index) { // 删除统一政策数目
      this.$emit('deleteRules', index)
    },

    getChange(val) { // ..活动范围分销商的值
      this.rule.distributorScope = val.distributorType
      this.rule.distributorGradeIds = val.distributorGradeIds
      this.rule.distributorIds = val.distributorIds
      this.rule.distributorData = val.distributorData
    },

    conditions(value) { // ..政策调整变化，折扣前后的跟踪变化
      if (value === 1) {
        this.discountBeAfShow = true
      } else {
        this.discountBeAfShow = false
      }
    }
  },
  watch: {
    rule: {
      handler() {
        this.$emit('changeRule', this.rule, this.currentCount)
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>
.policy-rule{
	border-radius: 5px;
	margin-bottom: 5px;
	border-top: 1px solid #ccc;
  border-right: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  border-left: 1px solid #ccc;
  background-color: #fff;
  .pomotion{
    width: 100%;
    .pom_head {
      background-color: #f8f8f8;
      border-top-left-radius: 5px;
      border-top-right-radius: 5px;
      padding: 10px;
      border-bottom: 1px solid #ccc;
      font-weight: bold;
      .rule_dle {
        margin-left: 2px
      }
    }
    .rule_describe {
      margin-bottom:10px;
      margin-top:15px;
      padding: 0 16px;
      .rule_conditions {
        margin-bottom:10px;
      }
      .rule_des_input {
        width: 100%;
        background-color: #fff;
      }
    }
    .rule_describe1 {
      margin-bottom:10px;
      margin-top:15px;
      .rule_desc_input {
        width: 100px;
      }
      .rule_desc_span {
        font-weight:bold;
        color: #606266;
      }
    }
  .el-input__inner, .el-textarea__inner {
      background-color: #fff;
    }
  }
	.rule-delete{
		float: right;
		margin-right: 8px;
	}
	.el-input__inner{
		font-weight:bold;
		color: #606266;
	}
	.grade_rule{
		border-radius: 5px;
		margin-bottom: 5px;
		border-top: 1px solid #ccc;
    border-right: 1px solid #ccc;
    border-bottom: 1px solid #ccc;
    border-left: 1px solid #ccc;
		background-color: #fff;
		margin: 16px;
    position: relative;
		.half_two {
			padding:10px;
			color: #606266;
		}
	}
	.box_has_info{
		overflow: hidden;
    border-bottom: 1px solid #ccc;
    position: relative;
    .grade_category {
      padding: 10px;
      .grade_cat_item {
        margin-bottom: 0;
        .grade_cat_selectOne {
          width: 300px;
        }
      }
    }
		.half_left{
			padding: 10px;
			width: 30%;
			float: left;
			.half_left_selectOne {
				width:130px;
			}
		}
		.half-center{
			padding: 10px;
			width: 30%;
			float: left;
			.half_center_input {
				width: 100px;
			}
			.half_center_span {
				font-weight:bold;
				color: #606266;
			}
		}
		.half_right{
      position: absolute;
      top: 10px;
      right: 4px;
			.half_right_button {
				margin-left: 2px;
			}
		}
		.half_one {
      position: absolute;
      // color:#ccc;
      float: bottom;
      // border-top: 1px solid #ccc;
			width: 100%;
		}
    .grade_add {
      margin-left: 20px;
      font-size: 20px;
      position: absolute;
      top: 5px;
      cursor:pointer;
    }
    .grade_reduction {
      margin-left: 60px;
      font-size: 20px;
      position: absolute;
      top: 5px;
      cursor:pointer;
    }
	}
  .box_has_info1{
		overflow: hidden;
    // border-bottom: 1px solid #ccc;
    position: relative;
    .grade_category {
      padding: 10px;
      .grade_cat_item {
        margin-bottom: 0;
        .grade_cat_selectOne {
          width: 300px;
        }
      }
    }
		.half_left{
			padding: 10px;
			width: 30%;
			float: left;
			.half_left_selectOne {
				width:130px;
			}
		}
		.half-center{
			padding: 10px;
			width: 30%;
			float: left;
			.half_center_input {
				width: 100px;
			}
			.half_center_span {
				font-weight:bold;
				color: #606266;
			}
		}
		.half_right{
      position: absolute;
      top: 10px;
      right: 4px;
			.half_right_button {
				margin-left: 2px;
			}
		}
		.half_one {
      position: absolute;
      // color:#ccc;
      float: bottom;
      border-top: 1px solid #ccc;
			width: 100%;
		}
    .grade_add {
      margin-left: 20px;
      font-size: 20px;
      position: absolute;
      top: 5px;
      cursor:pointer;
    }
    .grade_reduction {
      margin-left: 60px;
      font-size: 20px;
      position: absolute;
      top: 5px;
      cursor:pointer;
    }
	}
}
.operation{
	margin-bottom: 10px;
	margin-top: 10px;
	.btn_add{
		margin-left: 16px;
	}
}


</style>
