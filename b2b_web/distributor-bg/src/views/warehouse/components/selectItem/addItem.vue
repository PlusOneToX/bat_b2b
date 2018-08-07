<template>
	<div class="check-goods">
		<div class="nav-bar">
			<!-- <el-col :span="5" :offset="1">
				<el-select size="mini" v-model="categoryId" placeholder="请选择分类">
					<el-option v-for="item in categorylist" :key="item.id" :value="item.id" :label="item.name"></el-option>
				</el-select>
			</el-col> -->
			<!-- <el-col :span="5">
				<el-select size="mini" v-model="brandId" placeholder="请选择品牌">
					<el-option v-for="item in brandlist" :key="item.id" :value="item.id" :label="item.title"></el-option>
				</el-select>
			</el-col> -->
        <el-select style="width:100px" size="mini" v-model="pageInfo.saleStatus" clearable placeholder="选择状态">
            <el-option v-for="item in saleStatusList" :key="item.saleStatus" :value="item.saleStatus" :label="item.saleStatusName"></el-option>
        </el-select>
				<el-button type="info" size="mini" @click="filter()" style="float: right;margin-left:5px;">搜索</el-button>
				<el-input size="mini" style="float: right;width:250px;" placeholder="商品编号/商品名称/存货编码/存货名称" v-model.trim="pageInfo.content"></el-input>
		</div>

		<div>
			<el-table ref="multipleSelect" :data="tableData" tooltip-effect="dark" @select="select" @select-all="selectAll" border header-row-class-name="header-row" @selection-change="handleSelectionChange" class="tableCenter">
				<el-table-column align="center" type="selection" width="55"></el-table-column>
				<!-- <el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
				<el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column> -->
				<el-table-column align="center" label="货号" prop="itemCode"></el-table-column>
				<el-table-column align="center" label="货品名称" prop="itemName"></el-table-column>
				<el-table-column align="center" label="上架状态" prop="saleStatus" :formatter="formatStatus"></el-table-column>
			</el-table>
			<page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
		</div>
		<div class="foot-btn">
			<el-button class="mini-search-btn" style="margin-left:47%;" @click="handleSubmit()">确定</el-button>
			<el-button size="mini" @click="handleCancel">返回</el-button>
		</div>
	</div>
</template>
<script>
import page from '@/components/pagination/index'
export default {
  props: ['saleStatus', 'selectItemsData', 'goodsType'],
  data() {
    return {
      categoryId: '',
      brandId: '',
      categorylist: [],
      brandlist: [],
      pageInfo: {
        page: 1,
        size: 10,
        saleStatus: 3,
        content: ''
      },
      tableData: [],
      total: 0,
      page: 1,
      loading: false,
      multipleSelect: [],
      selected: [],
      isSelect: false,
      saleStatusList: [{ saleStatus: 3, saleStatusName: '已上架' },
        { saleStatus: 1, saleStatusName: '已下架' },
        { saleStatus: 2, saleStatusName: '上架审批中' },
        { saleStatus: 4, saleStatusName: '下架审批中' }]
    }
  },
  components: { page },

  created() {
    this.pageInfo.saleStatus = this.saleStatus
    if (this.goodsType !== undefined && this.goodsType !== '') {
      this.pageInfo.goodsType = this.goodsType
    }
    this.multipleSelect = []
    if(this.selectItemsData !== undefined && this.selectItemsData.length>0){
      this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)// 第一次进入时
    }
    this.dataForamt()
  },
  methods: {
    selectRow() { // 商品请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection()
      this.multipleSelect.forEach(row1 => {
        this.tableData.forEach(row2 => {
          if (row1.itemCode === row2.itemCode) {
            this.$refs.multipleSelect.toggleRowSelection(row2)
          }
        })
      })
    },
    dataForamt() {
      if (this.pageInfo.saleStatus === '') {
        this.pageInfo.saleStatus = undefined
      }
      this.$http.getGoodsItemPoList(this, this.pageInfo).then(res => {
        this.isSelect = false
        res.data.list.forEach(item => {
          item.numInWarehouse = 0
          item.numLock= 0
          item.reserved = 0
          item.ableReserved = 0
          item.numReserved = 0
          item.itemId = item.id
        })
        this.tableData = res.data.list
        this.total = res.data.total
        this.multipleSelect.forEach(row1 => { // 重新获取数据时，判断哪些选中了
          this.tableData.forEach(row2 => {
            if (row1.itemCode === row2.itemCode) {
              this.$refs.multipleSelect.toggleRowSelection(row2)
              this.selected.push(row2)
            }
          })
        })
      })
    },
    select(selection, row) { // 单选时调用
      this.isSelect = true
      let d = false
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].itemCode === row.itemCode) {
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
            if (this.multipleSelect[i].itemCode === row.itemCode) {
              this.multipleSelect.splice(i, 1)
              break
            }
          }
        })
      } else {
        this.multipleSelect = this.setArr(this.multipleSelect.concat(selection))
      }
    },

    setArr(arr) { // 去重
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].itemCode) // 不加类型 分不清 1 '1'
        if (!obj[ arr[i].itemCode + type]) {
          temp.push(arr[i])
          obj[ arr[i].itemCode + type ] = true // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
    },

    sizeChange(size) { // 分页页数
      this.pageInfo.count = size
      this.pageInfo.page = 1
      this.dataForamt()
    },

    currentChange(page) { // 分页总数
      this.pageInfo.page = page
      this.dataForamt()
    },

    handleSelectionChange(val) { // 当切换页面时的作用
      if (val.length === 0 && this.multipleSelect.length !== 0 && !this.isSelect) {
        this.multipleSelect.forEach(row1 => {
          this.tableData.forEach(row2 => {
            if (row1.itemCode === row2.itemCode) {
              this.$refs.multipleSelect.toggleRowSelection(row2)
            }
          })
        })
        this.isSelect = true
      }
    },

    formatStatus(row, col, val) { // 上架状态值
      switch (val) {
        case 1:
          return '已下架'
          // eslint-disable-next-line no-unreachable
          break
        case 2:
          return '上架审批中'
          // eslint-disable-next-line no-unreachable
          break
        case 3:
          return '已上架'
          // eslint-disable-next-line no-unreachable
          break
        case 4:
          return '下架审批中'
          // eslint-disable-next-line no-unreachable
          break
      }
    },

    filter() { // 搜索操作
      this.dataForamt()
    },

    handleSubmit() { // 确定操作，给父页面监听
      this.multipleSelect.reverse()
      this.$emit('submit', this.multipleSelect)
    },

    handleCancel() { // 返回操作，给父页面监听
      this.multipleSelect = []
      this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
      this.$emit('cancel')
    }
  },
  watch: {
    selectItemsData: {// 请求的分销商数据变化时
      handler() {
        this.multipleSelect = []
        this.multipleSelect = this.multipleSelect.concat(this.selectItemsData)
        this.selectRow()
      },
      deep: true
    },
    'pageInfo.saleStatus': {
      handler() {
        this.pageInfo.page = 1
        this.dataForamt()
      },
      deep: true
    }
  }
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space:nowrap;
}
.check-goods {
	background-color: white;
	.nav-bar {
		padding: 10px 0;
	}
	.commodityTable {
		text-align: center;
	}
	.header-row {
		@include table-header-color;
	}
	.foot-btn {
		margin-top:10px;
	}
}

</style>
