<template>
	<div class="check-distributor">
		<el-row class="nav_bar">
      <div class="nav_bar_header">
        <el-input class="nva_bar_input" size="mini" placeholder="商品编号/商品名称/存货编码/存货名称" v-model.trim="content" />
        <el-button class="mini-search-btn nva_bar_btn" @click="filter">搜索</el-button>
      </div>
		</el-row>

    <el-table
      class="tableCenter"
      ref="multipleSelect"
      :data="tableData"
      tooltip-effect="dark"
      @select="select"
      @select-all="selectAll"
      border header-row-class-name="header-row" 
      @selection-change="handleSelectionChange"
      v-loading="loading">
      <el-table-column align="center" type="selection" with="55"></el-table-column>
      <el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
      <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
      <el-table-column align="center" label="货品编号" prop="itemCode"></el-table-column>
      <el-table-column align="center" label="货品名称" prop="itemName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="默认销售价格" prop="salePrice"></el-table-column>
    </el-table>
    <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    <el-button class="mini-search-btn check_btn" @click="handleSubmit()">确定</el-button>
    <el-button class="check_back_btn" size="mini" @click="handleCancel()">返回</el-button>
	</div>
</template>
<script>
/*
 * @Author: lijiemin
 * @Date: 2018-01-09 14:56:04
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-07-We 03:46:02
 */
import page from '@/components/pagination'
export default {
  props: ['speciaData','goodsType',"relevanceMaterialFlag"],
  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        goodsType: this.goodsType,
        relevanceMaterialFlag: this.relevanceMaterialFlag // 是否关联材质 1-是 2-否
      },
      tableData: [],
      total: 0,
      grades: [],
      multipleSelect: [],
      content: '',
      isSelect: false,
      selected: [],
      loading: false
    }
  },
  components: { page },
  created() {
    this.$nextTick(() => {
      this.multipleSelect = this.speciaData
      this.getTableData()
    })
  },
  methods: {
    selectRow() { // 分销商请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection()
      this.multipleSelect.forEach(row1 => {
        this.tableData.forEach(row2 => {
          if (row1.id === row2.id) {
            this.$refs.multipleSelect.toggleRowSelection(row2)
          }
        })
      })
    },
    getTableData() {
      this.loading = true
      this.$http.getGoodsItemPoList(this, this.pageInfo).then(res => {  
        this.isSelect = false
        this.selected = []
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
          if (this.multipleSelect && this.multipleSelect.length > 0) {
            this.multipleSelect.forEach(row1 => { // 重新获取数据时，判断哪些选中了
              this.tableData.forEach(row2 => {
                if (row1.id === row2.id) {
                  this.$refs.multipleSelect.toggleRowSelection(row2)
                  this.selected.push(row2)
                }
              })
            })
          }
        }
        res.success ? this.loading = false : this.loading = false
      })
    },
    sizeChange(size) { // 分页页数
      this.pageInfo.size = size
      this.getTableData()
    },

    currentChange(page) { // 分页总数
      this.pageInfo.page = page
      this.getTableData()
    },
    handleSubmit() { // 确定操作
      this.multipleSelect.reverse()
      this.$emit('submit', this.multipleSelect)
    },
    handleCancel() { // 返回操作
      this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.speciaData)
      this.$emit('cancel')
      this.selectRow()
    },
    select(selection, row) { // 单选时调用
      this.isSelect = true
      let d = false
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].id === row.id) {  
          this.multipleSelect.splice(i, 1)
          d = true
          break
        }
      }
      if (!d) {
        this.multipleSelect.push(row)
        this.multipleSelect = this.setArr(this.multipleSelect)
      }
    },
    selectAll(selection) { // 全选时调用
      this.isSelect = true
      if (selection.length === 0) {
        this.tableData.forEach(row => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1)
              break
            }
          }
        })
      } else {
        this.multipleSelect = this.setArr(this.multipleSelect.concat(selection))
      }
    },
    handleSelectionChange(val) { // 当切换页面时的作用
      if (val.length === 0 && this.multipleSelect.length !== 0 && !this.isSelect) {
        this.multipleSelect.forEach(row1 => {
          this.tableData.forEach(row2 => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2)
            }
          })
        })
        this.isSelect = true
      }
    },

    setArr(arr) { // 去重
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id)// 不加类型 分不清 1 '1'
        if (!obj[ arr[i].id + type]) {
          temp.push(arr[i])
          obj[ arr[i].id + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
    },
    filter() {
      this.pageInfo.page = 1
      this.pageInfo.content = this.content
      this.getTableData()
    }
  },
  watch: {
    pageInfo: {
      handler() {
        this.getTableData()
      },
      deep: true
    },
    speciaData: { // 请求数据变化时
      handler() {
        // this.multipleSelect = []
        // this.multipleSelect = this.multipleSelect.concat(this.speciaData)
        this.multipleSelect = this.speciaData
        this.selectRow()
      },
      deep: true
    }
  }
}
</script>
<style lang="scss" scoped>
.check-distributor {
	background-color: white;
	min-height: 100%;
	.nav_bar {
		.nav_bar_header {
			display: flex;
			justify-content: flex-end;
      align-items: center;
      margin: 10px 0 10px 10px;
			.nva_bar_input {
				width: 245px;
			}
			.nva_bar_btn {
				margin-left: 10px;
			}
		}
	}
  .check_btn {
    margin-left: 46%;
    margin-top: 10px;
  }
  .check_back_btn {
    margin-top: 10px;
  }
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
		margin-top: 50px;
	}
}

</style>
