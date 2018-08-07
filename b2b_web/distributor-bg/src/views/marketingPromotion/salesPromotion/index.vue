<template>
    <div class="promotion-list" v-loading="exportDownloadLoading" element-loading-text="正在导出...">
      <div class="sales-list">
        <header>
          <h4>促销活动列表</h4>
          <el-button class="btn-home" icon="el-icon-plus" @click="addPlateData()"> 添加促销活动 </el-button>
          <el-button class="btn-home" icon="el-icon-download" @click.prevent="exportDownload()">导出活动</el-button>
        </header>
      </div>

      <div class="promotion_header" >
        <div class="promotion_he_1">
          <div class="promoStatus">
            <div>
              <el-select size="mini" clearable placeholder="促销状态"  v-model="pageInfo.promoStatus" class="promo-status">
                <el-option :value="0" label="未开始"></el-option>
                <el-option :value="1" label="促销中"></el-option>
                <el-option :value="2" label="已过期"></el-option>
                <el-option :value="3" label="提前结束"></el-option>
              </el-select>
              <el-select size="mini" clearable placeholder="申请状态"  v-model="pageInfo.applyStatus" class="promo-status">
                <el-option :value="0" label="草稿"></el-option>
                <el-option :value="1" label="申请中"></el-option>
                <el-option :value="2" label="申请通过"></el-option>
                <el-option :value="3" label="申请失败"></el-option>
              </el-select>
              <el-select size="mini" clearable placeholder="选择活动类型" v-model="pageInfo.promoType" class="promo-status">
                <el-option :value="1" label="普通活动"></el-option>
                <el-option :value="2" label="阶梯活动"></el-option>
              </el-select>
              
              <el-date-picker
                v-model="time"
                size="mini"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="timestamp">
              </el-date-picker>
              <el-button class="mini-search-btn btn-box" style="float:right;margin-left:10px;" @click.prevent="onSearch()">搜索</el-button>
              <el-input
                class="box_input"
                size="mini"
                style="width:160px;float:right;"
                placeholder="请输入关键词搜索"
                clearable
                @change="changeContent"
                @keyup.enter.native="filter()"
                v-model.trim="content"
              ></el-input>
              <el-select
                class="content_select"
                placeholder="选择类型"
                size="mini"
                style="width:120px;float:right;"
                v-model="pageInfo.contentType"
                clearable
              >
                <el-option
                  v-for="item in contentTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
          </div>
        </div>
        
      </div>
      <div class="sales-function">
        <el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter" max-height="550" v-loading="loading">
          <el-table-column align="center" label="活动编号" prop="id" :width="90" ></el-table-column>
          <el-table-column align="center" label="活动名称" prop="name" ></el-table-column>
          <el-table-column align="center" label="活动类型" prop="promoType" :width="100" :formatter="promoType" ></el-table-column>
          <el-table-column align="center" label="开始时间" prop="startTime" :formatter="formatTime" ></el-table-column>
          <el-table-column align="center" label="结束时间" prop="endTime" :formatter="formatTime" ></el-table-column>
          <el-table-column align="center" label="活动范围" :width="120" prop="distributorScope" :formatter="formatScope"></el-table-column>
          <el-table-column align="center" label="促销状态" :width="80" prop="promoStatus" :formatter="promoStatus"></el-table-column>
          <el-table-column align="center" label="申请状态" :width="80" prop="applyStatus" :formatter="applyStatus"></el-table-column>
          <el-table-column align="center" label="规则" :width="80" >
            <template slot-scope="scope" >
              <el-button @click="show(scope.row)" type="text" size="small">查看</el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" :width="200">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleSee(scope.$index,scope.row)">查看</el-button>
              <el-button v-show="scope.row.promoStatus === 1" class="mini-browse-btn" @click="handleStop(scope.row)">提前结束</el-button>
              <el-button v-show="scope.row.promoStatus === 0 || scope.row.applyStatus === 0" class="mini-delete-btn" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </div>
        <el-dialog :modal-append-to-body="false" title="规则" :visible.sync="centerDialogVisible" width="30%" center>
          <span >{{promotionRuleDescribe}}</span>
        </el-dialog>
      </div>
</template>

<script type="text/javascript">
import treeTable from '@/components/TreeTable'
import page from '@/components/pagination'
import { timeFormat } from '@/utils/timeFormat.js'
import api from  '@/api/allUrl'
export default {
  name: 'frontPagePlate',
  components: { treeTable, page },
  data() {
    return {
      loading: false,
      exportDownloadLoading: false,
      editDistributor: null,
      centerDialogVisible: false,
      promotionRuleDescribe: '',
      batch: '',
      pageInfo: {
        page: 1,
        size: 10,
        applyStatus: 2,
        promoStatus: 1,
        promoType:undefined,
        contentType: '',
        content: '',
        promoSource: ''
      },
      contentTypes: [
        { value: 1, label: '活动名称' },
        { value: 2, label: '商品编号' },
        { value: 3, label: '货品编号' },
        { value: 4, label: '分销商' },
        { value: 5, label: '规则标签' },
        { value: 6, label: '条件标签' }
      ],
      time:[],
      total: 0,
      addShow: false,
      tableData: [],
      parentId: 1,
      editId: '',
      mytext: '',
      scope: [],
      forEach: [],
      content: '',
      formData: {
        id: '',
        name: '',
        ruleDescribe: '',
        isAdvance: 0,
        distributorScope: 0,
        distributorScopeNo: 0,
        advanceTime: 0,
        startTime: 0,
        endTime: 0,
        createTime: 0,
        updateTime: 0
      },
      salesAreas: []
    }
  },
  activated() {
    this.dataFot()
  },
  methods: {
    // 暂不对接
    exportDownload(){
      if(this.pageInfo.promoType !== 2){
        this.$message.error("暂时只支持'阶梯活动'导出，请先刷选阶梯活动再导出")
        return
      }
      this.exportDownloadLoading = true
      this.$api
          .exportData(this, api.promotionExport, this.pageInfo).then(res =>{
          this.exportDownloadLoading = false
          const content = res
          debugger
          let blob = new Blob([content],{
            type: "application/ms-excel"
          })
          let url = window.URL.createObjectURL(blob)
          if ('download' in document.createElement('a')) {
            let link = document.createElement('a')
            link.style.display ='none'
            link.href = url
            link.setAttribute('download','阶梯活动导出列表.xls')
            document.body.appendChild(link)
            link.click()
          }else{
            navigator.msSaveBlob(blob, '阶梯活动导出列表.xls')
          }
        })
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.onSearch()
      }
    },
    // ======== 操作 ========
    onSearch() { // 搜索操作
      this.pageInfo.content = this.content
      this.pageInfo.page = 1
      this.dataFot()
    },
    addPlateData() { // 添加促销活动操作
      this.$router.push({ name: 'addPromotion' })
    },
    handleSee(index, row) { // 查看详情操作
      this.$router.push({ name: 'editPromotion', params: { id: row.id }})
    },
    handleDelete(row) { // 删除操作
      this.$confirm('删除此条促销活动?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.delPromotion(this, {id:row.id}).then(res => {
          if (res.success) {
            this.dataFot()
          }
        })
      })
    },

    handleStop(row) { // 提前结束操作
      this.$confirm('提前结束此条促销活动?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.promotionStatus(this, {id: row.id, promoStatus: 3}).then(res => {
          if (res.success) {
            this.dataFot()
          }
        })
      })
    },

    show(row) { // 规则查看操作
      this.centerDialogVisible = true
      this.promotionRuleDescribe = row.promoDesc
    },
    // ======== 转换 ========
    formatScope(row, col, val) { // 活动范围
      switch (val) {
        case 1:
          return '全部分销商'
          break
        case 2:
          return '指定分销商等级'
          break
        case 6:
          return '指定事业部'
          break
        case 4:
          return '指定销售部门'
          break
        case 5:
          return '指定业务员'
          break
        case 3:
          return '指定分销商'
          break
      }
    },

    promoType(row, col, val){
      switch (val) {
        case 1:
          return '普通活动'
          break
        case 2:
          return '阶梯活动'
          break
      }
    },

    promoStatus(row, col, val) { // 促销状态
      switch (val) {
        case 0:
          return '未开始'
          break
        case 1:
          return '促销中'
          break
        case 2:
          return '已过期'
          break
        case 3:
          return '提前结束'
          break
      }
    },
    
    applyStatus(row, col, val) { // 申请状态
      switch (val) {
        case 0:
          return '草稿'
          break
        case 1:
          return '申请中'
          break
        case 2:
          return '申请通过'
          break
        case 3:
          return '申请失败'
          break
      }
    },
    formatTime(row, col, val) { // 表格时间转换
      return timeFormat(val)
    },
    changeContent(val) {
      if (val === undefined || val === "" || val === null) {
        this.filter();
      }
    },
    filter() {
      // 搜索操作
      if (this.pageInfo.promoStatus === -1 || this.pageInfo.promoStatus === "") {
        this.pageInfo.promoStatus = undefined;
      }
      if (this.pageInfo.promoType === -1 || this.pageInfo.promoType === "") {
        this.pageInfo.promoType = undefined;
      }
      this.pageInfo.contentType = this.contentType;
      this.pageInfo.content = this.content;
      this.page = this.pageInfo.page;
      this.dataFot();
    },
    // ======== 数据 ========
    dataFot() { // 列表数据
      this.loading = true
      if (this.pageInfo.promoStatus === '') {
        this.pageInfo.promoStatus = undefined
      }
      if (this.pageInfo.promoType === '') {
        this.pageInfo.promoType = undefined
      }
      this.$http.promotionList(this, this.pageInfo).then(res => {  
        const ary = []
        res.data.list.forEach(item => {
          item.children = []
          if (item.distributorType === 1) {
            item.promotionRuleDescribe = '全部'
          } else if (item.distributorType === 2) {
            item.promotionRuleDescribe = '分销商等级'
          } else if (item.distributorType === 3) {
            item.promotionRuleDescribe = '指定分销商'
          }
          ary.push(item)
        })
        ary.sort((a, b) => {
          return a.sort - b.sort > 0
        })
        this.tableData = ary
        this.total = res.data.total
        if(this.tableData.length === 0 && this.pageInfo.page > 1){
            this.pageInfo.page = this.pageInfo.page - 1
            this.dataFot()
        }
        res.success ? this.loading = false : this.loading = false
      })
    },

    sizeChange(size) {
      this.pageInfo.page= 1
      this.pageInfo.size = size
      this.dataFot()
    },

    currentChange(page) {
      this.pageInfo.page = page
      this.dataFot()
    }
  },
  watch: {
    'pageInfo.promoStatus': {
      handler() {
        this.pageInfo.page = 1
        this.dataFot()
      },
      deep: true
    },
    'pageInfo.applyStatus': {
      handler() {
        this.pageInfo.page = 1
        this.dataFot()
      },
      deep: true
    },
    'pageInfo.promoType': {
      handler() {
        this.pageInfo.page = 1
        this.dataFot()
      },
      deep: true
    },
    time(val){
      if(this.time != null && this.time instanceof Array && this.time.length>0){
        this.pageInfo.startTime = this.time[0]
        this.pageInfo.endTime = this.time[1]
      }else {
        this.pageInfo.startTime = undefined
        this.pageInfo.endTime = undefined
        this.pageInfo.page = 1
        this.dataFot()
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden; 
  white-space:nowrap; 
}
.promotion-list {
  background-color: white;
  height: 100%;
  .sales-list {
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
    }
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    } 
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .promotion_header {
    overflow: hidden;
    .promotion_he_1 {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .rpomotion-list-sear {
        .box-search{
          width: 400px;
          margin-right: 10px;
        }
        .btn-box {
          position: relative;
          top: -1px;
          right: 10px;
        }
      }
    }
  }
  .promoStatus{
    width:100%;
    padding: 10px;
    .promo-status {
      width: 120px;
    }
    .box-name{
      width: 150px;
      // margin-right: 10px;
    }
    .box-code{
      width: 130px;
      // margin-right: 10px;
    }
    .box-lable{
      width: 120px;
    }
    .box-discount{
      width: 80px;
    }
  }
  .sales-function {
    background-color: white;
  }
}
</style>
