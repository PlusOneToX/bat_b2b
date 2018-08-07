<template>
	<div class="add_restriction">
		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="loading2">
			<el-form-item label="规则名称" prop="ruleName" style="margin-bottom: 22px">
				<el-input class="add_res_input" v-model="formData.ruleName"></el-input>
			</el-form-item>

			<el-form-item label="规则描述"> 
				<el-input class="add_res_input1" type="textarea" v-model="formData.ruleDesc" :autosize="{ minRows: 5, maxRows: 8}" maxlength="200" ></el-input>
			</el-form-item>

			<el-form-item label="启用状态" style="margin-bottom:0">
				<el-radio-group v-model="formData.ruleStatus">
					<el-radio :label="1">启用</el-radio>
					<el-radio :label="2">停用</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item label="限购对象">
				<el-radio-group v-model="formData.ruleTarget">
					<el-radio :label="1">商品限购</el-radio>
					<el-radio :label="2">订单限购</el-radio>
				</el-radio-group>
			</el-form-item>

      <div class="add_res_body">
        <el-form-item label="限购方式" prop="ruleType" v-if="ruleTypeShow" class="add_res_itemN">
          <el-radio-group v-model="formData.ruleType">
            <el-radio :label="1">按订单</el-radio>
            <el-radio :label="2">按时间</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="下单条件" prop="orderCondition" class="add_res_itemN">
          <el-radio-group v-model="formData.orderCondition" >
            <!-- 限购对象为：商品限购ruleTypeShow    限购对象为：订单限购orderPurchasing-->
            <el-radio :label="1" v-if="ruleTypeShow">金额</el-radio>
            <el-radio :label="2" v-if="ruleTypeShow">数量</el-radio>
            <el-radio :label="1" v-if="orderPurchasing">订单金额
							<el-tooltip content="订单优惠后的订单总金额" placement="top" effect="light" >
								<span class = "el-icon-question " ></span>
							</el-tooltip>
						</el-radio>
            <el-radio :label="3" v-if="orderPurchasing">商品总价
							<el-tooltip content="订单优惠前的订单总金额" placement="top" effect="light" >
								<span class = "el-icon-question " ></span>
							</el-tooltip>
						</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="leastBuy" v-if="formData.ruleType === 1 || orderPurchasing">
          <span class="add_res_span">单笔订单最少购买 </span>
          <el-input class="add_res_input2" placeholder="留空默认不限制" v-if="formData.orderCondition === 1" v-model="formData.leastBuy" @input="formData.leastBuy=/^\d+\.?\d{0,2}$/.test(formData.leastBuy)||formData.leastBuy == '' ? formData.leastBuy:Number(formData.leastBuy.toString().match(/^\d+(?:\.\d{0,1})?/))" type="number" min="0" step="0.01"></el-input>
          <el-input class="add_res_input2" placeholder="留空默认不限制" v-else v-model="formData.leastBuy" @input="formData.leastBuy=/^\d+\.?\d{0,0}$/.test(formData.leastBuy)||formData.leastBuy == '' ? formData.leastBuy:Number(formData.leastBuy.toString().match(/^\d+(?:\.\d{0,0})?/))" type="number" min="0"></el-input>
          <span class="add_res_span"> 元(会员价)/数量</span>
        </el-form-item>

        <el-form-item prop="mostBuy" v-if="formData.ruleType === 1 && ruleTypeShow">
          <span class="add_res_span">单笔订单最多购买 </span>
          <el-input class="add_res_input2" placeholder="留空默认不限制" v-if="formData.orderCondition === 1" v-model="formData.mostBuy" @input="formData.mostBuy=/^\d+\.?\d{0,2}$/.test(formData.mostBuy)||formData.mostBuy == '' ? formData.mostBuy:Number(formData.mostBuy.toString().match(/^\d+(?:\.\d{0,1})?/))"  type="number" min="0" step="0.01"></el-input>
          <el-input class="add_res_input2" placeholder="留空默认不限制" v-else v-model="formData.mostBuy" @input="formData.mostBuy=/^\d+\.?\d{0,0}$/.test(formData.mostBuy)||formData.mostBuy == '' ? formData.mostBuy:Number(formData.mostBuy.toString().match(/^\d+(?:\.\d{0,0})?/))" type="number" min="0"></el-input>
          <span class="add_res_span"> 元(会员价)/数量</span>
        </el-form-item>

        <el-form-item prop="mostBuy" v-if="formData.ruleType === 2 && ruleTypeShow">
          <span class="add_res_span">每日最多购买满 </span>
          <el-input class="add_res_input2" placeholder="留空默认不限制" v-if="formData.orderCondition === 1" v-model="formData.dayLimit" @input="formData.dayLimit=/^\d+\.?\d{0,2}$/.test(formData.dayLimit)||formData.dayLimit == '' ? formData.dayLimit:Number(formData.dayLimit.toString().match(/^\d+(?:\.\d{0,1})?/))" type="number" min="0" step="0.01"></el-input>
          <el-input class="add_res_input2" placeholder="留空默认不限制" v-else v-model="formData.dayLimit" @input="formData.dayLimit=/^\d+\.?\d{0,0}$/.test(formData.dayLimit)||formData.dayLimit == '' ? formData.dayLimit:Number(formData.dayLimit.toString().match(/^\d+(?:\.\d{0,0})?/))" type="number" min="0"></el-input>
          <span class="add_res_span"> 元(会员价)/数量</span>
        </el-form-item>

        <!-- <el-form-item label="当天购买量" prop="dayLimit" v-show="formData.ruleType === 2">
          <el-input style="width:15%;color: #606266;font-weight: 500;" v-model="formData.dayLimit" type="number" min="0" @input="formData.dayLimit=/^\d+\.?\d{0,0}$/.test(formData.dayLimit)||formData.dayLimit == '' ? formData.dayLimit:Number(formData.dayLimit.toString().match(/^\d+(?:\.\d{0,0})?/))"></el-input>
        </el-form-item> -->

        <el-form-item label="限购商品" prop="goodsIds" class="add_res_item1" v-if="ruleTypeShow">
          <el-button class="mini-tableadd-btn" @click="addGoods()">添加限购商品</el-button>
          <el-table :data="goods" border v-if="goods.length" header-row-class-name="header-row" class="goods-table tableCenter">
            <el-table-column align="center" label="商品编号" prop="goodsNo" show-overflow-tooltip></el-table-column>
            <el-table-column align="center" label="商品名称" prop="goodsName" show-overflow-tooltip></el-table-column>
            <el-table-column align="center" label="操作" width="80">
              <template slot-scope="scope">
                <!-- <el-button size="mini" type="warning" @click="handleUp(scope.$index,scope.row)">上移</el-button>
                <el-button size="mini" type="success" @click="handleDown(scope.$index,scope.row)">下移</el-button>
                <el-button size="mini" type="primary" @click="handleEdit(scope.$index,scope.row)">编辑</el-button> -->
                <el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      
      </div>

			<!-- 可视范围 -->
			<distributor ref="distributor" :type="formData.distributorType" :gIds="formData.gradeIds" :dIds="formData.distributorIds" @change="getChange" :disabled="false"></distributor>
			<el-button class="mini-search-btn add_res_button" :loading="loading" @click="handleSubmit()">保存</el-button>
			<el-button size="mini" @click="handleCancel()">返回</el-button>
		</el-form>

		<!-- 限购商品选择 -->
		<el-dialog :modal-append-to-body="false" :visible="selectShow" width="80%" :before-close="closeDialog">
			<select-goods :selectGoodsData="goods" ref="selectGoods"  @cancel="selectShow=false" @submit="getGoodsData"></select-goods>
		</el-dialog>
	</div>
</template>
<script>
import { isNumber } from "@/utils/validate";
import distributor from '@/views/marketingPromotion/compomemts/distributor/discountRest'
import selectGoods from "@/views/goods/components/selectGoods/index"
import { isString } from "@/utils/index"
export default {
	name: 'editrestriction',
	components: { distributor, selectGoods },
  mounted() {
    this.getParams()
  },
  data() {
		const validateNumber = (rule, value, callback) => {
			if(!isNumber(value)) {
				callback(new Error('只能输入数字'))
			} else {
				callback()
			}
		}
		return {
			loading2: false,
			loading: false,
			append: true, // 添加标题
			looking: false, // 查看标题
			formData: {
				ruleName: '', // 规则名称
				ruleDesc: '', // 规则描述
				ruleStatus: 1, // 启用状态
				ruleTarget: 1, // 限购对象
				ruleType:1, // 限购方式
				orderCondition:1, // 下单条件
				dayLimit: 0, // 每天最大县限购量
				distributorType: 1, // 可视分销商范围
				gradeIds: [],
				goodsIds: "",
				distributorIds: [],
			},
			pageInfo: {
				page: 1,
				count: 10000
			},
			rules: {
				ruleName: [{
					required: true,
					message: '请输入规则名称',
					trigger: 'blur'
				}],
				goodsIds: [{
					required: true,
					message: '请选择限购商品',
					trigger: 'blur'
				}],
				dayLimit: [{
					validator: validateNumber,
				}]

			},
			distributorData: [],
			goods: [],
      selectShow: false,
      ruleTypeShow: true,
      orderPurchasing: false
		}
	},
	methods: {
    // ======== 操作 ========
		handleSubmit() { // 保存操作
			if(this.distributorData.length > 0) {
				let ary=[]
				this.distributorData.forEach(item => {
					ary.push(item.id)
				})
				this.formData.distributorIds=ary;
			}
			this.$refs['formData'].validate(valid => {
				if(valid) {
					this.loading = true;
					if(this.formData.distributorType === 1){ //..可视分销商范围 1 全部分销商
						this.formData.distributorIds = ''
						this.formData.gradeIds = ''
					}else if(this.formData.distributorType === 2){ //..可视分销商范围 2 指定分销商等级
						this.formData.gradeIds = this.formData.gradeIds === '' ? this.formData.gradeIds : (isString(this.formData.gradeIds) ? this.formData.gradeIds : this.formData.gradeIds.join(','));
						this.formData.distributorIds = ''
					}else if(this.formData.distributorType === 3){ //..可视分销商范围 3 指定分销商
						this.formData.distributorIds = this.formData.distributorIds === '' ? this.formData.distributorIds : (isString(this.formData.distributorIds) ? this.formData.distributorIds : this.formData.distributorIds.join(','))
						this.formData.gradeIds = ''
					}
					if(this.formData.ruleTarget == 1) { //..限购对象： 1 商品限购 2 订单限购
						
					}else if(this.formData.ruleTarget == 2) {
						this.formData.ruleType = '' //..限购方式
						this.formData.mostBuy = '' //..单笔订单最多购买
						this.goods = [] //..限购商品清空
					}
					if(this.$route.query.id) {
						if(this.formData.ruleTarget == 1) { //..限购对象的购买数量判断
							if(parseInt(this.formData.leastBuy) > parseInt(this.formData.mostBuy)) {
								this.$message.error('最少购买数量、金额应小于最大购买数量、金额');
								this.loading = false
							}else if(parseInt(this.formData.leastBuy) < parseInt(this.formData.mostBuy)) {
								this.putData() //..修改
							}else if(this.formData.leastBuy != '' && this.formData.mostBuy == undefined) {
								this.putData()
							}else if(this.formData.leastBuy != '' && this.formData.mostBuy == '') {
								this.putData()
							}else if(this.formData.leastBuy == undefined && this.formData.mostBuy != '') {
								this.putData()
							}else if(this.formData.leastBuy == '' && this.formData.mostBuy != '') {
								this.putData()
							}
						}else if(this.formData.ruleTarget == 2) { //..限购对象： 1 商品限购 2 订单限购
							this.putData() //..修改
						}
					} else {
						if(this.formData.ruleTarget == 1) { //..限购对象： 1 商品限购 2 订单限购
							if(parseInt(this.formData.leastBuy) > parseInt(this.formData.mostBuy)) {
								this.$message.error('最少购买数量、金额应小于最大购买数量、金额');
								this.loading = false
							}
							if(parseInt(this.formData.leastBuy) < parseInt(this.formData.mostBuy))  {
								this.postData() //..添加
							}else if(this.formData.leastBuy != '' && this.formData.mostBuy == undefined) {
								this.postData()
							}else if(this.formData.leastBuy !='' && this.formData.mostBuy == '') {
								this.postData()
							}else if(this.formData.leastBuy == undefined && this.formData.mostBuy != '') {
								this.postData()
							}else if(this.formData.leastBuy == '' && this.formData.mostBuy != '') {
								this.postData()
							}
						}else if(this.formData.ruleTarget == 2) { //..限购对象： 1 商品限购 2 订单限购
							this.postData()
						}
					}
				} else {
					return false
				}
			})
		},
		postData() { //..限购规则添加请求
			this.$api.post(this, 'admin/u/p/goods/restriction', this.formData).then(res => {
				if(res.code == 0) {
					this.$message({
						message: '添加成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.loading = false
					this.$router.push({ name: 'restrictionlist' })
				} else {
					this.loading = false;
				}
			})
		},

		putData() { //..限购规则修改请求
			this.$api.put(this, 'admin/u/p/goods/restriction', this.formData).then(res => {
				if(res.code == 0) {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 3 * 1000,
						onClose: () => { }
					})
					this.loading = false
					this.$router.push({ name: 'restrictionlist' })
				} else {
					this.loading = false;
				}
			})
		},
    
    handleDelete(index) { // 删除限购商品
			this.goods.splice(index, 1);
			if(this.goods.length > 0) {  // 限购商品的值
				this.formData.goodsIds = this.goods.map(item => item.id).reduce((sum, cur, index) => {
					return sum + (index == 0? '' : ',') + cur
					}, '')
			}else{
				this.formData.goodsIds = ''
			}
    },
    
    handleCancel() { // 返回操作
			this.$router.go(-1)
		},

    addGoods() { // 添加限购商品操作
			this.selectShow = true;
    },

    closeDialog() { // 关闭限购商品窗口
			this.$refs.selectGoods.handleCancel()
		},
    
    setArr(arr){ // 去重
			let obj ={};  
			let temp=[];  
			for( let i = 0; i < arr.length; i++ ) {  
				let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'    
				if( !obj[ arr[i].id +type] ) {  
				temp.push( arr[i] );  
				obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读  
				}
				this.goods = temp;
			}  
    },
    
    // ======== 数据 ========
    getParams(){
			if(this.$route.query.id) {
				this.loading2 = true;
				this.$api.get(this, 'admin/u/p/goods/restriction', { id: this.$route.query.id }).then(res => {
					res.code == 0 ? this.loading2 = false : this.loading2 = false
					this.formData = res.rule
					if(res.rule.mostBuy == 0) {
						this.formData.mostBuy = ''
					}
					if(res.rule.leastBuy == 0) {
						this.formData.leastBuy = ''
					}
					// this.formData.gradeIds = res.rule.grades
					// this.formData.distributorIds = []
					if(this.formData.distributorIds != undefined && this.formData.distributorIds != ""){
						this.formData.distributorIds = this.formData.distributorIds.split(",")
					}else{
						this.formData.distributorIds = []
					}
					let ary = [];
					res.rule.goods.forEach(item => {
						ary.push(item.goodsId)
					})
					this.formData.goodsIds = ary.join(',')
					if(this.formData.ruleTarget == 1) {
						this.$api.get(this, 'admin/u/po/goods/ids', { ids: this.formData.goodsIds }).then(res => {
							if(res.code == 0) {
								this.goods = res.goods
							}
						})
					}
					if(this.formData.gradeIds !== undefined && this.formData.gradeIds !== ""){
						let arr = this.formData.gradeIds.split(",")
						this.formData.gradeIds = []
						for(let i = 0;i<arr.length;i++){
							this.formData.gradeIds.push(Number(arr[i]))
						}
					}else{
						this.formData.gradeIds = []
					}
					this.$refs.distributor.getParams(this.formData.distributorType,this.formData.gradeIds,this.formData.distributorIds)
				})
			} else {
				this.formData.ruleName = ''
				this.formData.ruleType = 1
				this.formData.orderCondition = 1
				this.formData.dayLimit = 0
				this.formData.distributorType = 1
				this.formData.gradeIds = []
				this.formData.goodsIds = ""
				this.formData.distributorIds = []
				this.distributorData = [];
				this.$refs.distributor.getParams(this.formData.distributorType,this.formData.gradeIds,this.formData.distributorIds)
			}
    },
    
    getGoodsData(val) { // 选择的限购商品的值
			this.goods = this.goods.concat(val);
			this.setArr(this.goods);
			this.selectShow = false;
			if(this.goods.length > 0) {  // 限购商品的值
				this.formData.goodsIds = this.goods.map(item => item.id).reduce((sum, cur, index) => {
					return sum + (index == 0? '' : ',') + cur
					}, '')
			}else{
				this.formData.goodsIds = ''
			}
    },
    
		getChange(val) { // 可视范围分销商
			if(this.loading){

			}else{
				this.formData.distributorType = val.distributorType;
				this.formData.gradeIds = val.distributorGradeIds;
				this.formData.distributorIds = val.distributorIds;
				this.distributorData = val.distributorData;
			}
    },
    
    // ======== 验证 ========
		verify1(formData) {  // 验证只能输入数字（每天最大县限购量）
			this.formData.dayLimit = this.formData.dayLimit.replace(/\D/g,'')
		},
  },
  watch: {
    'formData.ruleTarget': function() { // 限购对象: 1 商品限购 2 订单限购
      if(this.formData.ruleTarget == 1) {
        this.ruleTypeShow = true
				this.orderPurchasing = false
				this.formData.ruleType = 1 //..限购方式
				this.formData.orderCondition = 1 //..下单条件
      }else if(this.formData.ruleTarget == 2) {
        this.orderPurchasing = true
				this.ruleTypeShow = false
      }
		},
		'formData.leastBuy': function() {
			if(this.formData.ruleTarget == 1 && this.formData.ruleType == 1) {
				if(parseInt(this.formData.leastBuy) > parseInt(this.formData.mostBuy)) {
					this.$message.error('最少购买数量、金额应小于最大购买数量、金额');
				}
			}
		},
		'formData.mostBuy': function() {
			if(this.formData.ruleTarget == 1 && this.formData.ruleType == 1) {
				if(parseInt(this.formData.mostBuy) < parseInt(this.formData.leastBuy)) {
					this.$message.error('最大购买数量、金额应大于最少购买数量、金额');
				}
			}
		}
  }
}
</script>

<style lang="scss" scoped>
.el-form-item {
	margin-bottom: 8px;
}
.add_restriction {
	background-color: white;
  min-height: 100%;
	.add_res_body {
		margin-left: 65px;
		border: 1px solid #dcdcdc;
		width: 80%;
		padding: 10px 0;
		.add_res_itemN {
			margin-bottom:0
		}
	}
	.add_res_input {
		width:30%
	}
	.add_res_input1 {
		width:50%
	}
	.add_res_span {
		color: #606266;
		font-weight: 500;
	}
	.add_res_input2 {
		width: 15%;
		color: #606266;
		font-weight: 500;
	}
  .add_res_item1 {
    margin-right: 20%;
  }
  .add_res_button {
    margin-left: 45%;
    margin-top: 20px;
    margin-bottom: 30px;
  }
	.el-input__inner,
	.el-textarea__inner {
		background-color: white;
	}
}
</style>