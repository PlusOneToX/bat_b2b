<template>
	<div class="check-goods">
		<h4 style="margin-left: 15px;margin-bottom: 15px;">
			注意：此页面保存只保存分销商定制价格的数据且无需审批，不保存其他页签编辑的数据，如需保存其他页签编辑数据，请切换到其他页签保存！</h4>
		<div class="nav-bar">
			<div class="nav-list-header">
				<div>
					<el-button class="mini-search-btn" @click="addGoodsShow">添加定制商品</el-button>
					<el-button class="mini-search-btn" @click="batchDelete()">批量删除</el-button>
				</div>
			</div>
		</div>
		<el-row style="margin-left: 15px;margin-right: 15px;">
			<!-- 修改前的展示 -->
			<el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter"
				@select-all="selectAll" @selection-change="handleSelectionChange" max-height="600">
				<el-table-column align="center" type="selection" with="55"></el-table-column>
				<el-table-column align="center" label="商品编号" prop="goodsNo" :min-width="120"></el-table-column>
				<el-table-column align="center" label="商品名称" prop="goodsName" :min-width="120"></el-table-column>
				<el-table-column align="center" label="货品编号" prop="itemCode" :min-width="120"></el-table-column>
				<el-table-column align="center" label="货品名称" prop="itemName" :min-width="120"></el-table-column>
				<!-- <el-table-column align="center" label="材质" prop="materialName" :min-width="120"></el-table-column> -->
				<el-table-column align="center" label="C端价格" :min-width="120">
					<template slot-scope="scope">
						<el-input type="number" step="1" min="0.01" max="1000000" size="mini"
							v-model.trim="scope.row.price" @keyup.native="proving" />
					</template>
				</el-table-column>
				<el-table-column label="操作" :min-width="80">
					<template slot-scope="scope">
						<el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-row>

		<!-- 引用组件 -->
		<el-dialog :modal-append-to-body="false" :visible="selectShow" width="80%" :before-close="closeDialog">
			<addGoods :speciaData="tableData" :goodsType="2" :relevanceMaterialFlag="1" ref="addGoods" @cancel="cancel"
				@submit="disSubmit"></addGoods>
		</el-dialog>

		<!-- v-if="node===1" -->
		<div class="footbtn">
			<el-button class="mini-search-btn foot-btn" @click="savePrice()"> 保存 </el-button>
		</div>
	</div>
</template>

<script>
	/*
	 * @Author: lijiemin
	 * @Date: 2018-05-06 17:16:17
	 * @Last Modified by: li.tian
	 * @Last Modified time: 2018-07-We 03:34:02
	 */
	import page from '@/components/pagination'
	// 引用组件
	import addGoods from '@/views/distributor/cooperating/cooperatingadd/components/addGoods'

	export default {
		name: 'diyPrice',
		props: ['distributorId', 'node'],
		data() {
			return {
				tableData: [],
				tmpTableData: [],
				deleteArray: [],
				multipleSelect: [],
				ruleForm: {},
				rules: {
					price: [{
						required: true,
						message: '请输入特价商品',
						trigger: 'blur'
					}]
				},
				selectShow: false
			}
		},
		components: {
			page,
			addGoods
		},
		created() {
			this.getTableData()
		},
		methods: {
			proving() {
				this.tableData.forEach(item => {
					item.price = item.price.toString().replace(/[^\.\d]/g, ''); //清除"数字"和"."以外的字符
					item.price = item.price.toString().replace(/^\./g, ''); //验证第一个字符是数字而不是.
					item.price = item.price.toString().replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的.
					item.price = item.price.toString().replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
				})
			},
			savePrice() {
				var b = true
				let itemPriceList = []
				if (this.tableData.length > 0) {
					this.tableData.forEach(item => {
						if (item.price === undefined || item.price === null || item.price === '') {
							b = false
						}
						itemPriceList.push({
							itemId: item.id,
							price: Number(item.price)
						})
					})
				}
				if (b) {
					this.$http.distributorCustomPrice(this, {
						distributorId: this.distributorId,
						itemPriceList: itemPriceList,
						source: 1
					}).then(res => {
						if (res.success) {
							this.$message({
								message: '保存成功',
								type: 'success',
								duration: 3 * 1000
							})
							if (this.node === 1) {
								this.$router.push({
									name: 'distributorcooperating'
								})
							} else {
								this.$router.push({
									name: 'distributorcooperatingn'
								})
							}
						}
					})
				} else {
					this.$message({
						message: '选择的定制商品必须维护价格',
						type: 'error',
						duration: 3 * 1000
					})
				}
			},
			getTableData() {
				// 分销商柔性定制价格
				if (this.distributorId !== undefined && this.distributorId !== null && this.distributorId !== '') {
					this.$http.disCustomPriceById(this, {
						id: this.distributorId
					}).then(res => {
						if (res.success) {
							this.tableData = []
							if (res.data && res.data.length > 0) {
								res.data.forEach(item => {
									this.tableData.push({
										id: item.itemId,
										itemId: item.itemId,
										goodsNo: item.goodsNo,
										goodsName: item.goodsName,
										itemCode: item.itemCode,
										itemName: item.itemName,
										materialName: item.materialName,
										price: item.price
									})
								})
							}
						}
					})
				}
			},
			disSubmit(msg) { // dialog确定操作
				msg.forEach(item => {
					if (!item.price) {
						this.$set(item, 'price', 0.01)
					}
				})
				this.tableData = msg;
				this.selectShow = false
			},

			cancel() { // dialog返回操作
				this.selectShow = false
			},

			addGoodsShow() { // 添加特价商品dialog
				this.selectShow = true
			},

			closeDialog() { // 关闭dialog的X
				this.selectShow = false
			},

			handleDelete(index, row) { // 定制商品定价删除
				this.tableData.splice(index, 1)
			},

			batchDelete() { // 批量删除
				if (this.deleteArray.length === 0) {
					this.$message({
						message: '请先选择需要删除的记录',
						type: 'warning',
						duration: 3 * 1000
					})
				}
				this.deleteArray.forEach((item, e) => {
					this.tableData.forEach((i, index) => {
						if (item.itemId === i.itemId) {
							this.tableData.splice(index, 1)
						}
					})
				})
			},

			selectAll(selection) { // 全选操作，传入给批量删除
				this.deleteArray = selection
			},

			handleSelectionChange(val) { // 选项改变时传入的数据
				this.deleteArray = val
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" sceopd>
	@import '../../scss/specialcomponents.scss';
</style>
