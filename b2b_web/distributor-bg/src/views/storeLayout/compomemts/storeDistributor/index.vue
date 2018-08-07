<template>
	<div>
		<el-form-item label="可视范围" >
			<el-radio-group v-model="formData.distributorScope">
				<div class="dis-item">
					<el-radio :label="1">全部分销商</el-radio>
					<span class="place-holder">所有分销商都可见</span>
				</div>
				<div class="dis-item" >
					<div>
						<el-radio :label="2">指定分销商等级</el-radio>
						<span class="place-holder">只有指定的分销商等级可见</span>
					</div>
					<el-col  v-if="formData.distributorScope==2">
						<el-form-item>
							<el-checkbox-group v-model="formData.gradeIds">
								<el-checkbox style="line-height:28px" v-for="item in distributorList" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
							</el-checkbox-group>
						</el-form-item>
					</el-col>
				</div>
				<div class="dis-item">
					<div>
						<el-radio  :label="4" >指定销售部门</el-radio>
						<span class="place-holder">只有指定的销售部门对应的分销商可见</span>
					</div>

					<!-- 指定销售部门 -->
					<el-col v-if="formData.distributorScope === 4">
						<el-form-item>
						<el-checkbox-group v-model="formData.departmentIds">
							<el-checkbox style="height:28px" v-for="item in departments" :label="item.id" :key="item.id">{{item.departmentName}}</el-checkbox>
						</el-checkbox-group>
						</el-form-item>
					</el-col>
				</div>
				<div class="dis-item">
					<div>
						<el-radio  :label="5">指定业务员</el-radio>
						<span class="place-holder">只有指定的业务员对应的分销商可见</span>
					</div>
					<!-- 指定业务员 -->
					<el-col v-if="formData.distributorScope === 5">
						<transition mode="out-in" name="fade">
						<div class="sales" >
							<el-transfer v-model="formData.distributorAdminIds" :data="admins" :titles="['业务员列表','选中列表']" :button-texts="['取消','选中']"></el-transfer>
						</div>
						</transition>
					</el-col>
				</div>
				<div class="dis-item">
					<el-radio :label="6">指定分销商</el-radio>
					<span class="place-holder">只有指定的分销商可见</span>
					<div class="distributor-content" v-if="formData.distributorScope == 6">
						<el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
							添加分销商
						</el-button>
						<el-table class="goods-table" :data="formData.distributorData" border header-row-class-name="header-row" max-height="600">
							<el-table-column align="center" label="分销商用户名" width="150" prop="name"></el-table-column>
							<el-table-column align="center" label="公司名" width="300" prop="companyName"></el-table-column>
							<el-table-column  align="center" label="操作" width="80">
								<template slot-scope="scope">
									<el-button style="margin-top:0px;margin-bottom:0px;" class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index)">
										删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
					</div>
				</div>
			</el-radio-group>
		</el-form-item>
		<el-dialog :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%" >
			<select-distributor :distributorData="formData.distributorData" ref="selectDistributor" @cancel="cancel" @submit="disSubmit"> </select-distributor>
		</el-dialog>
	</div>
</template>
<script>
import selectDistributor from "@/views/goods/components/selectDistributor"
import store from '@/store'
export default {
	props: ['type', 'gIds', 'dIds','isBrand','departmentIds','adminIds'],
	data() {
		return {
			distributorList: [],
			distributors:[],
			departments:[],
			admins:[],
			formData: {
				distributorScope: 1,
				gradeIds: [],
				departmentIds:[],
				distributorAdminIds:[],
				distributorIds: [],
				distributorData: [],
			},
			distributorShow: false,
		}
	}, 
	components: { selectDistributor },
	mounted() {
		this.getDepartments()
		this.getAllSales()
		this.getGrades()
		this.formData.distributorData = []
		// this.formData.distributorScope = this.type;
		// this.formData.gradeIds = this.gIds instanceof Array ? this.gIds : this.gIds == undefined ? [] : this.gIds
		// this.formData.departmentIds = this.departmentIds instanceof Array ? this.departmentIds : this.departmentIds == undefined ? [] : this.departmentIds
		// this.formData.distributorAdminIds = this.adminIds instanceof Array ? this.adminIds : this.adminIds == undefined ? [] : this.adminIds
		// this.formData.distributorIds =  this.dIds instanceof Array ? this.dIds : this.dIds == undefined ? [] : this.dIds
		// this.distributors = store.getters.distributors
		// this.initDistributor()
	},
	methods: {
		initDistributor() {
			if (this.formData.distributorIds !== undefined && this.formData.distributorIds !== null && this.formData.distributorIds.length > 0) {
				if (this.distributors === undefined || this.distributors.length === 0) {
					this.$http.getDistributorPoList(this, { page:1, size:10000,freezeStatus: 1,profileStatus: 2}).then(res => {			
						this.distributors = res.data.list
						this.$store.commit('GET_DISTRIBUTORS', res.data.list)
						this.formData.distributorIds.forEach(item => {
							this.distributors.forEach(val => {
								if (val.id === Number(item)) {
									this.formData.distributorData.push(val)
								}
							})
						})
						this.formData.distributorData = this.setArr(this.formData.distributorData)
					})
				} else {
					this.formData.distributorIds.forEach(item => {
						this.distributors.forEach(val => {
							if (val.id === Number(item)) {
								this.formData.distributorData.push(val)
							}
						})
					})
					this.formData.distributorData = this.setArr(this.formData.distributorData)
				}
			}
		},
		getGrades(){
			this.$http.getGradePoList(this, { page:1, size:10000, openFlag: 1}).then(res => {
				if(res.success) {
					this.distributorList = res.data.list
				}
			})
		},
		getAllSales() {  //..用户列表
			this.$http.salesList(this, {page:1,size:10000,saleFlag:1}).then(res => {		
				res.data.list.forEach(item => {
				let obj = {
					key: item.id,
					label: item.realName,
					disabled: false,
				}
				this.admins.push(obj)
				})
			})
		},
		getDepartments(){ //销售部门
			this.$http.getDepartmentPoList(this, {page:1,size:10000,saleType:1}).then(res => {	
				if (res.success) {
				this.departments = res.data.list
				}
			})
		},
		setArr(arr) { // 去重
			const obj = {}
			// eslint-disable-next-line prefer-const
			let temp = []
			for (let i = 0; i < arr.length; i++) {
				const type = Object.prototype.toString.call(arr[i].id) // 不加类型 分不清 1 '1'
				if (!obj[arr[i].id + type]) {
				temp.push(arr[i])
				obj[arr[i].id + type] = true // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
				}
			}
			return temp
		},
		handleDeleteDistributor(index) {
			this.formData.distributorData.splice(index, 1)
		},
		add() {
			this.$emit('add')
		},
		disCancel() {
			this.$refs.selectDistributor.handleCancel()
		},
		cancel(){
			this.distributorShow = false;
		},
		disSubmit(msg) {
			this.formData.distributorData = msg;
			this.formData.distributorIds = []
			this.formData.distributorData.forEach(item => {
				this.formData.distributorIds.push(item.id)
			})
			this.distributorShow = false;
		},
	},
	watch: {
		formData: {
			handler(val) {
				this.$emit('change', this.formData)
			},
			deep: true
		},
		type: {
			handler(val) {
				this.formData.distributorScope = val
			},
			deep:true,
			immediate: true
		},
		dIds: {
			handler(val, oldVal) {
				this.formData.distributorIds = val instanceof Array ? val : ((val == undefined || val === null)  ? [] : val)
				this.initDistributor()
			},
			deep: true,
			immediate: true
		},
		gIds: {
			handler(val, oldVal) {
				this.formData.gradeIds = val instanceof Array ? val : ((val === undefined || val === null)  ? [] : val)
			},
			deep: true,
			immediate: true
		},
		departmentIds: {
			handler(val, oldVal) {
				this.formData.departmentIds = val instanceof Array ? val : ((val === undefined || val === null) ? [] : val)
			},
			deep: true,
			immediate: true
		},
		adminIds: {
			handler(val, oldVal) {
				this.formData.distributorAdminIds = val instanceof Array ? val : ((val === undefined || val === null) ? [] : val)
			},
			deep: true,
			immediate: true
		}
	}
}

</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox {
	margin-left: 30px;
    margin-right: 0px;
}
.sales {
	margin-left: 25px;
	margin-bottom: 10px;
	margin-top: 10px;
	.el-button--primary{
		color: #fff;
		background-color: $lakeBlue !important;
		border-color: $lakeBlue !important;
	}
	.el-transfer__button.is-disabled, .el-transfer__button.is-disabled:hover{
		background-color: #CCCCCC !important;
		border-color: #CCCCCC !important;
	}
}

.dis-item {
	margin-top: 13px;
	.place-holder {
		color: #ccc;
		font-size: 12px;
		margin-left: 10px !important
	}
	.distributor-content button {
		margin-top: 10px !important;
		margin-bottom: 10px !important;
	}
}



</style>
