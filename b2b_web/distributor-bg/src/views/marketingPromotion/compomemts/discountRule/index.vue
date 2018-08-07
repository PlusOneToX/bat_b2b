<template>
  <div class="policy-rule">
    <el-form class ="pomotion" ref="rule" :model="detail">
      <el-form-item :label="'规则 '+currentCount" class="pom_head">
        <button v-show="!disabled" class="rule-delete mini-delete-btn rule_dle" size="mini" @click.prevent="handleDeleteRule(currentCount)">删除</button>
        <button v-show="detail.isSpread" class="rule-delete mini-search-btn" size="mini" @click.prevent="isSpread()"><span class="el-icon-arrow-up"></span></button> <!-- 收起 -->
        <button v-show="!detail.isSpread" class="rule-delete mini-search-btn" size="mini" @click.prevent="isSpread()"><span class="el-icon-arrow-down"></span></button><!-- 展开 -->
      </el-form-item>
    </el-form>
		<div class="grade_rule" v-if="detail.isSpread">
      <div class="box_has_info">
        <div class="grade_category">
          <el-form label-width="90px">
            <el-form-item label="选择类目：" class="grade_cat_item">
              <el-select class="grade_cat_selectOne" placeholder="请选择类目"  size="mini" @change="changes" @remove-tag="removeTag" v-model="detail.categoryIds" :disabled="disabled" multiple filterable>
                <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in productlines" :disabled="item.disabled">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <!-- 设置等级 -->
        <div class="half_cycle" v-for="(gra,index) in detail.detailsArr" :key="gra.id">
          <div class="half_left" style="width: 30%">
            <el-form label-width="90px">
              <el-form-item label="设置等级：" prop="gra.gradeId" >
                <el-select @focus="selectIndex=index" class="half_left_selectOne" placeholder="请选择" size="mini" v-model="gra.gradeId" :disabled ="disabled" @change="gradeIdChange" >
                  <el-option :key="item.id" :label="item.gradeName" :value="item.id" v-for="item in goodGrades" :disabled="item.disabled">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>

          <div class="half-center" style="width: 70%">
            <el-form label-width="80px">
              <el-form-item label="折扣" prop="gra.discount" >
                <el-input class="half_center_input" size="mini" v-model="gra.discount" @input="gra.discount=/^\d+\.?\d{0,0}$/.test(gra.discount)||gra.discount == '' ? gra.discount:Number(gra.discount.toString().match(/^\d+(?:\.\d{0,0})?/))" :disabled ="disabled" type="number" min="0"></el-input>
                <span class="half_center_span"> %</span>
                <i v-show="!disabled" class="el-icon-circle-plus grade_add" @click="gradeAdd(detail.detailsArr)" :disabled="disabled"></i>
                <i v-show="!disabled" class="el-icon-remove grade_reduction" @click="gradeReduction(detail.detailsArr,index)" :disabled="disabled"></i>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
		</div>
	</div>
</template>

<script type="text/javascript">
// import { getGoods, getItems, getItemsStock } from '@/views/marketingPromotion/promotionData'
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
    detail: {
      categoryIds:[],
      detailsArr:[{
        gradeId: '',
        discount: 0
      }],
      isSpread: true
    },
    disabled: true,
    productlines:{
      type:Array,
      default: []
    }
    // detailsArr: []
  },
  data() {
    return {
      pageInfo: {
        originalGradeId: ''
      },
      goodGrades:[],
      selectIndex:0
    }
  },
  created() {
    getPoGradeDetail(this, { status: 1 }).then(res => {
      if(res.code === 0){
        this.goodGrades = res.goodGrades
        this.initGrade()
      }
    })
    this.initCategory()
    
  },
  mounted() {
    
  },
  methods: {
    changeIndex(index){
      this.selectIndex = index
    },
    initGrade(){//货品等级初始化
      if(this.goodGrades != undefined && this.goodGrades.length>0){
        this.detail.detailsArr.forEach(value =>{
          this.goodGrades.forEach(res =>{
            if(res.id === value.gradeId){
              res.disabled = true
            }
          })
        })
      }
    },
    initCategory(){//品类初始化
      if(this.productlines != undefined && this.productlines.length>0){
        this.$emit('changeCategory')
      }
    },
    isSpread() { // 收起/展开统一政策详情操作
      this.detail.isSpread = !this.detail.isSpread
    },

    handleDeleteRule(index) { // 删除统一政策数目
      this.$emit('deleteRules', index)
    },


    gradeAdd(ruleTemp) { // ..设置等级加号
      ruleTemp.push({
          gradeId: '',
          discount: 0
        })
    },

    gradeReduction(ruleTemp,index) { // ..设置等级减号
      if(ruleTemp[index].gradeId != undefined && this.goodGrades != undefined){
        this.goodGrades.forEach(item =>{
          if(item.id === ruleTemp[index].gradeId){
            item.disabled = false
          }
        })
      }
      if (ruleTemp.length > 1) {
        ruleTemp.splice(index, 1)
      }else{
        this.$message.warning('至少有一个等级折扣规则！')
      }
    },

    gradeIdChange(val) { // ..设置等级的页面数据渲染更新
      this.goodGrades.forEach(item =>{
        let b = false
        this.detail.detailsArr.forEach(item1 =>{
          if(item1.gradeId === item.id){
            b = true;
            return
          }
        })
        item.disabled = b
      })
      this.$forceUpdate()
    },

    changes(val) { // ..选择类目选中事件
      this.$emit('changeCategory')
      // this.productlines.forEach((item) => {
      //   val.forEach(item3 => {
      //     if (item3 === item.id) {
      //       item.disabled = true
      //       return
      //     }
      //   })
      // })
    },

    removeTag(val) { // ..选择类目移除事件
      this.$emit('changeCategory')
      // this.productlines.forEach((item) => {
      //   if (val === item.id) {
      //     item.disabled = false
      //     return
      //   }
      // })
    }
  },
  watch: {
    detail: {
      handler() {
        this.$emit('changeRule', this.detail, this.currentCount)
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
    margin-top: 5px;
		background-color: #fff;
    position: relative;
		.half_two {
			padding:10px;
			color: #606266;
		}
	}
	.box_has_info{
		overflow: hidden;
    position: relative;
    .grade_category {
      padding: 10px;
      .grade_cat_item {
        margin-bottom: 0;
        .grade_cat_selectOne {
          width: 80%;
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
}
.operation{
	margin-bottom: 10px;
	margin-top: 10px;
	.btn_add{
		margin-left: 16px;
	}
}


</style>
