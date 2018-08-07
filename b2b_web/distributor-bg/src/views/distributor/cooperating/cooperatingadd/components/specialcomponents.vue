<template>
	<div class="check-goods">
		<div class="nav-bar">
      <div class="nav-list-header">
        <div>
          <el-button class="mini-search-btn" @click="addGoodsShow" :disabled="exaShow">添加特价商品</el-button>
          <el-button class="mini-search-btn" @click="batchDelete()" :disabled="exaShow">批量删除</el-button>
        </div>
        <div class="nav-list-sear">
          <el-input placeholder="请输入货品编号自动检索" v-model.trim="content" size="mini" class="box-search box-btn" :disabled="exaShow" clearable=""></el-input>
        </div>
      </div>
		</div>
		<el-row>
      <!-- 修改前的展示 -->
      <el-table :data="stocks" v-if="goodsShow" border header-row-class-name="header-row" class="tableCenter" @select-all="selectAll" @selection-change="handleSelectionChange" max-height="600">
        <el-table-column align="center" type="selection" with="55"></el-table-column>
        <el-table-column align="center" label="商品编号" prop="goodsNo" :min-width="120"></el-table-column>
        <el-table-column align="center" label="商品名称" prop="goodsName" :min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" label="货品编号" prop="itemCode" :min-width="120"></el-table-column>
        <el-table-column align="center" label="货品名称" prop="itemName" :min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" label="分销商特价" :min-width="120">
          <template slot-scope="scope">
            <el-input type="number" step="1" min="0" max="1000000" size="mini" v-model.trim="scope.row.distributorPrice" :disabled="exaShow" 
            @input="if(isNaN(scope.row.distributorPrice)) { scope.row.distributorPrice = parseFloat(scope.row.distributorPrice) } if(scope.row.distributorPrice.indexOf('.')>0){ scope.row.distributorPrice=scope.row.distributorPrice.slice(0,scope.row.distributorPrice.indexOf('.')+3)}"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="80">
          <template slot-scope="scope">
          <el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)" :disabled="exaShow">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page> -->
      <!-- 审批页面修改后的展示 -->
      <div class="title" v-if="examinationShow">修改后的信息</div>
      <el-table :data="beforeAmendList" v-if="examinationShow" border header-row-class-name="header-row" class="tableCenter" max-height="600">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="商品编号" prop="goodsNo" :min-width="120"></el-table-column>
        <el-table-column align="center" label="商品名称" prop="goodsName" :min-width="120"></el-table-column>
        <el-table-column align="center" label="货品编号" prop="itemCode" :min-width="120"></el-table-column>
        <el-table-column align="center" label="货品名称" prop="itemName" :min-width="120"></el-table-column>
        <el-table-column align="center" label="分销商特价" :min-width="120">
          <template slot-scope="scope">
            <el-input type="number" step="1" min="0" max="1000000" size="mini" v-model.trim="scope.row.distributorPrice" :disabled="exaShow"
            @input="if(isNaN(scope.row.distributorPrice)) { scope.row.distributorPrice = parseFloat(scope.row.distributorPrice) } if(scope.row.distributorPrice.indexOf('.')>0){ scope.row.distributorPrice=scope.row.distributorPrice.slice(0,scope.row.distributorPrice.indexOf('.')+3)}" />
          </template>
        </el-table-column>
        <el-table-column label="操作2" :min-width="80">
          <template slot-scope="scope">
          <el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)" :disabled="exaShow">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
		</el-row>

		<!-- 引用组件 -->
		<el-dialog :modal-append-to-body="false" :visible="selectShow" width="80%" :before-close="closeDialog">
		  <addGoods :speciaData="tmpStocks" ref="addGoods" @cancel="cancel" @submit="disSubmit"></addGoods>
		</el-dialog>
	</div>
</template>

<script>
/*
 * @Author: lijiemin
 * @Date: 2018-05-06 17:16:17
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-07-We 06:04:35
 */
import eventBus from '@/views/order/eventBus'
import page from '@/components/pagination'
// 引用组件
import addGoods from '@/views/distributor/cooperating/cooperatingadd/components/addGoods'

export default {
  name: 'special',
  props: ['basicMessage', 'checkQuqry', 'checkDistributorId', 'exaShow', 'checkMsg', 'checkTyps'],
  data() {
    return {
      distributorPrice: [],
      pageInfo: {
        page: 1,
        count: 10000
      },
      total: 0,
      content: '',
      stocks: [], // 选中的特价商品数据
      tmpStocks: [], // 临时特价商品数据
      tableData: [],
      multipleSelect: [],
      commodities: [], // 特价商品列表,
      temData: [],
      ruleForm: {},
      rules: {
        distributorPrice: [
          { required: true, message: '请输入特价商品', trigger: 'blur' }
        ]
      },
      goodsShow: false,
      selectShow: false,
      stocksJudgeData: false,
      deleteArray: [],
      examinationShow: false,
      beforeAmendList: [] // ..修改前特价商品
    }
  },
  components: { page, addGoods },
  created() {
    this.stocksDataFlag()
  },
  methods: {
    onSearch() { // 搜索操作
      this.pageInfo.page = 1
      this.pageInfo.content = this.content
      this.stocksDataFlag()
    },
    stocksDataFlag() {
      if (this.checkMsg == 0 || this.checkMsg == 1 || this.checkMsg == 4) { // 添加 || 合作中
        this.pageInfo.distributorId = this.checkQuqry
      } else if (this.checkMsg == 2) { // 审批
        this.pageInfo.distributorId = this.checkDistributorId
        this.examinationShow = true
      }
    },

    sizeChange(size) {
      this.pageInfo.count = size
      this.stocksDataFlag()
    },

    currentChange(page) {
      this.pageInfo.page = page
      this.stocksDataFlag()
    },

    disSubmit(msg) { // dialog确定操作
      this.tmpStocks = msg
      if (this.content !== '') {
        this.content = ''
      } else {
        this.stocks = this.tmpStocks.concat()
      }
      this.goodsShow = true
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
    handleDelete(index, row) { // 特价商品删除
      this.stocks.splice(index, 1)
      for (let i = 0; i < this.tmpStocks.length; i++) {
        if (row.goodsItemId === this.tmpStocks[i].goodsItemId) {
          this.tmpStocks.splice(i, 1)
          break
        }
      }
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
        this.stocks.forEach((i, index) => {
          if (item.goodsItemId === i.goodsItemId) {
            this.stocks.splice(index, 1)
          }
        })
        for (let i = 0; i < this.tmpStocks.length; i++) {
          if (item.goodsItemId === this.tmpStocks[i].goodsItemId) {
            this.tmpStocks.splice(i, 1)
            break
          }
        }
      })
    },

    selectAll(selection) { // 全选操作，传入给批量删除
      this.deleteArray = selection
    },

    handleSelectionChange(val) { // 选项改变时传入的数据
      this.deleteArray = val
    }
  },
  watch: {
    basicMessage(val) {
      if (val.modifyList !== undefined) {
        val.modifyList.forEach(item => {
          if (item.mdField === '特价商品') {
            if (item.newCommodities != null) {
              this.beforeAmendList = item.newCommodities
            }
          }
        })
      }
      if (this.checkMsg == 1 || this.checkMsg == 2 || this.checkMsg == 4) {
        if (val.specialGoods &&　val.specialGoods.length>0) {
          this.goodsShow = true
          eventBus.$emit('stocksJudgeEvent', {
            stocksData: JSON.parse(JSON.stringify(val.specialGoods))
          })
          this.stocks= []
          val.specialGoods.forEach(item => {
            this.stocks.push({
              id: item.id,
              goodsItemId: item.goodsItemId,
              distributorPrice: item.distributorPrice,
              goodsId: item.goodsId,
              goodsName: item.goodsName,
              goodsNo: item.goodsNo,
              itemCode: item.itemCode,
              itemName: item.itemName,
              createTime: item.createTime,
              updateTime: item.updateTime
            })
          })
          // this.stocks = val.specialGoods
          this.total = val.specialGoods.length
        } else {
          eventBus.$emit('stocksJudgeData', {
            stocksJudgeData: true
          })
        }
        this.tmpStocks = this.stocks.concat()
      }
    },
    content(val) {
      this.stocks = this.tmpStocks.filter(function(currentValue) {
        return currentValue.itemCode.indexOf(val) !== -1
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" sceopd>
  @import '../../scss/specialcomponents.scss';
</style>
