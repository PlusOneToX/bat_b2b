<template>
	<div class="category-list">
		<header>
			<h4>订单增长率</h4>
		</header>

		<div class="function">
			<div class="Fheader">
				<el-button class="mini-search-btn" @click="exportM()">导出</el-button>
				<div class="block" style="display: inline-block">
          <el-date-picker v-model="createTime" value-format="timestamp" type="month" placeholder="选择月" size="mini"> </el-date-picker>
				</div>
				<div class="search">
					<el-input v-model.trim="content" placeholder="分销商用户名/客户名称/分销商ID 输入自动检索" size="mini" class="box-search"></el-input>
				</div>
			</div>

			<el-row>
				<el-table :data="increaseData" border header-row-class-name="header-row" class="tableCenter" v-loading="loading">
					<el-table-column label="分销商用户名" align="center" prop="distributorName"></el-table-column>
					<el-table-column label="客户名称" align="center" prop="companyName"></el-table-column>
					<el-table-column label="分销商ID" align="center" prop="distributorNo"></el-table-column>
					<el-table-column label="时间" align="center" prop="createTime" :formatter="formatTime"></el-table-column>
					<el-table-column label="当月销售出库单总额" align="center" prop="currentMonthMoney"></el-table-column>
					<el-table-column label="上月销售出库单总额" align="center" prop="beforeMonthsMoney"></el-table-column>
					<el-table-column label="销售出库单增长率" align="center" prop="orderGrowRate"></el-table-column>
				</el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
			</el-row>
		</div>
	</div>
</template>

<script type="text/javascript">
  import page from "@/components/pagination";
  import { timeMonth } from "@/utils/timeFormat.js"
	export default {
    name: 'orderIncreaseList',
    components: { page },
		activated() {
			this.raOfIncrease()
		},
		data() {
			return {
				loading: false,
        createTime: '',
				content: '', // 搜索值
				increaseData: [],
				pageInfo: {
					page: 1,
          count: 10,
				},
				total: 0,
			}
		},
		methods: {
      Csearch() { // 搜索操作
        this.pageInfo.createTime = this.createTime
        this.pageInfo.content = this.content
				this.raOfIncrease()
			},
			exportM() { // 导出操作
				this.$api.exportData(this,'admin/u/p/order/grow/rate/export',{
					createTime: this.createTime,
				  content: this.content
				}).then(res =>{
					if(res === null){
						return
					}
					const content = res
					let blob = new Blob([content],{
						type: "application/ms-excel"
					})
					let url = window.URL.createObjectURL(blob)
					if ('download' in document.createElement('a')) {
						let link = document.createElement('a')
						link.style.display ='none'
						link.href = url
						link.setAttribute('download','订单增长率.xls')
						document.body.appendChild(link)
						link.click()
					}else{
						navigator.msSaveBlob(blob, 'excel.xls')
					}
				})
			},
			raOfIncrease() { // 增长率数据
				this.loading = true;
				this.$api.get(this,'admin/u/p/order/grow/rate',this.pageInfo).then(res => {
					this.increaseData = res.growRates
					res.code == 0 ? this.loading = false : this.looking = false
        })
        this.$api.get(this,'admin/u/p/order/grow/rate/count',this.pageInfo).then(res => {
          this.total = res.count
        })
      },
      sizeChange(size) { // 条数
        this.pageInfo.count = size;
        this.raOfIncrease()
      },

      currentChange(page) { // 页数
        this.pageInfo.page = page;
        this.raOfIncrease()
      },
      formatTime(row, col, val) { // 表格时间格式化
        return timeMonth(val);
			},
		},
		watch: {
			createTime: { //..月份
				handler() {
					this.pageInfo.page = 1;
					this.Csearch()
				},
				deep: true
			},
			// content(newVal, oldVal) { //..搜索框 数据框防抖
			// 	if(this.content) {
			// 		clearTimeout(this.setConterTime)
			// 	}
			// 	this.setConterTime = setTimeout(() => {
			// 		this.Csearch()
			// 	},500)
			// },
			content: {
				handler() {
					setTimeout(() => {
						this.pageInfo.page = 1;
						this.Csearch()
					},500) 
				},
				deep: true
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss">
	.category-list {
		background-color: #fff;
		min-width: 1050px;
		header {
			color: white;
			height: $lineHight;
			line-height: $lineHight;
			background-color: $lakeBlue;
				h4 {
				display: inline-block;
				font-weight: 350;
				font-size: 16px;
				margin: 0 20px;
			}
		}
		.function {
			.Fheader{
				width: 96%;
				margin: 10px auto;
				overflow: hidden;
				.search {
					float: right;
					.box-search {
						width: 288px;
					}
          .btn-box {
            position: relative;
            top: -1px;
          }
				}
			}
		}
	}
</style>