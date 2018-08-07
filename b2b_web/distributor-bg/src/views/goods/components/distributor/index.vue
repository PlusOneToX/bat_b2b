<template>
	<div>
		<el-form-item label="可视范围" >
			<el-radio-group v-model="formData.distributorScope">
				<div class="dis-item" v-show="!isBrand">
					<el-radio :label="0">默认分销商</el-radio>
					<span class="place-holder">可见该品牌品类的分销商可见</span>
				</div>
				<div class="dis-item">
					<el-radio :label="1">全部分销商</el-radio>
					<span class="place-holder">所有分销商都可见</span>
				</div>
				<div class="dis-item" v-show="!isBrand">
					<div>
						<el-radio :label="2">指定分销商等级</el-radio>
						<span class="place-holder">只有指定的分销商等级可见</span>
					<div  v-if="formData.distributorScope==2" style="margin-bottom:-20px;">
						<el-form-item>
							<el-checkbox-group v-model="formData.gradeIds">
								<el-checkbox v-for="item in distributorList" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
							</el-checkbox-group>
						</el-form-item>
					</div>
					</div>
				</div>
				<div class="dis-item">
					<el-radio :label="3">指定分销商</el-radio>
					<span class="place-holder">只有指定的分销商可见</span>
					<div class="distributor-content" v-if="formData.distributorScope==3">
						<el-button icon="el-icon-plus" class="mini-search-btn" @click="distributorShow=true">
							添加分销商
						</el-button>
						<el-table :data="formData.distributorData" border header-row-class-name="header-row" max-height="600" class="tableCenter">
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
	props: ['type', 'gIds', 'dIds','isBrand'],
	data() {
		return {
			distributorList: [],
			distributors:[],
			formData: {
				distributorScope: 0,
				gradeIds: [],
				distributorIds: [],
				distributorData: [],
			},
			pageInfo: {
				page: 1,
				count: 10000
			},
			distributorShow: false,
		}
	}, 
	components: { selectDistributor },
	created() {
		this.formData.distributorScope = this.type;
		this.formData.gradeIds = this.gIds instanceof Array ? this.gIds : this.gIds == undefined ? [] : this.gIds.splitnum(',')
		this.formData.distributorIds =  this.dIds instanceof Array ? this.dIds : this.dIds == undefined ? [] : this.dIds
		this.distributors = store.getters.distributors
		this.changeDistributorIds(this.dIds)
		if(this.formData.distributorIds !== undefined && this.formData.distributorIds !== null && this.formData.distributorIds.length>0){
			if(this.distributors === undefined || this.distributors.length === 0){
				this.$http.getDistributorPoList(this, { page:1, size:10,freezeStatus: 1,profileStatus: 2}).then(res => {
					if (res.success) {
						this.distributors = res.data.list
						this.$store.commit('GET_DISTRIBUTORS', res.distributors)
						this.initDistributor()
					}		
				})
			}else{
				this.initDistributor()
			}
		}
		this.$http.getGradePoList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => {  
			if(res.success) {
				this.distributorList = res.data.list
			}
		})
	},
	methods: {
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
		initDistributor(){
			this.formData.distributorIds.forEach(item => {
				this.distributors.forEach(val => {
					if(val.id == Number(item)) {
						this.formData.distributorData.push(val)
					}
				})
			})
			this.formData.distributorData = this.setArr(this.formData.distributorData)
		},
		changeDistributorType(type){
			this.formData.distributorScope = type;
		},
		changeGradeIds(gIds){
			this.formData.gradeIds = gIds;
		},
		changeDistributorIds(dIds){
			this.formData.distributorIds = dIds
			if(this.formData.distributorIds !== undefined && this.formData.distributorIds !== null && this.formData.distributorIds.length>0){
				if(this.distributors === undefined || this.distributors.length === 0){
					this.$http.getDistributorPoList(this, { page:1, size:10000,freezeStatus: 1,profileStatus: 2}).then(res => {
						if (res.success) {
							this.distributors = res.data.list
							this.$store.commit('GET_DISTRIBUTORS', res.data.list)
							this.initDistributor()
						}
					})
				}else{
					this.initDistributor()
				}
			}
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
			// if(this.formData.distributorData.length > 0) {
			//     let ary=[]
			//     this.formData.distributorData.forEach(item => {
			//         ary.push(item.id)
			//     })
			//     this.formData.distributorIds=ary;
			// }
			this.distributorShow = false;
		},
	},
	watch: {
		formData: {
			handler() {
				this.$emit('change', this.formData)
			},
			deep: true
		}
	}
}

</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox:first-child {
	margin-left: 30px;
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
