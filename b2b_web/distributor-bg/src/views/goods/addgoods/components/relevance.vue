<template>
	<div class="relevance">
		<el-button @click="goodsShow=true" class="mini-search-btn" style="margin: 10px;">添加关联商品</el-button>
		<el-table :data='goods' border header-row-class-name="header-row">
			<el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
			<el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
			<el-table-column align="center" lable="操作">
				<template slot-scope="scope">
					<el-button class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeDialog">
			<select-goods @submit="submit" @cancel="goodsShow=false"></select-goods>
		</el-dialog>
	</div>
</template>
<script>
import selectGoods from "@/views/goods/components/selectGoods"
export default {
	props: ['relationGoods'],
	data() {
		return {
			goodsShow: false,
			goods: [],
			relationGoodsIds: ''
		}
	},
	components: {
		selectGoods
	},
	watch: {
		goods: {
			handler() {
				if(this.goods) {
					let ary = [];
					this.goods.forEach(item => {
					ary.push(item.id)
				})
				this.relationGoodsIds = ary.join(',')
				}
			},
			deep: true,
		},
		relationGoods(val) {
				let ids = val.map(v => { return v.relationGoodsId })
				ids = ids.join(',')
				if(ids) {
					this.$api.get(this, 'admin/u/po/goods/ids', { ids: ids }).then(res => {
						this.goods = res.goods
					})
				}
		}
	},
	methods: {
		closeDialog() {
			this.goodsShow = false;
		},
		submit(val) {
			this.goods = val;
			this.goodsShow = false;
		},
		handleDelete(index) {
			this.goods.splice(index, 1)
		}
	}
}

</script>
<style lang="scss" scoped>


</style>
