<template>
  <div class="order-list-wrap">
    <header>
      <h4>批量活动导入列表</h4>
      <el-button class="mini-add-btn btn-home el-icon-upload2" @click="addpuls">导入活动</el-button>
    </header>
    <div class="order-list" >
      <div>
        <el-tabs v-model="importPromotionStatus">
          <el-tab-pane label="未提交" name="1"></el-tab-pane>
          <el-tab-pane label="已提交" name="2"></el-tab-pane>
        </el-tabs>
      </div>
      <el-table :data="tableData" @selection-change="handleSelectionChange" header-row-class-name="header-row" border class="tableCenter" max-height="550" v-loading="loading">
        <el-table-column align="center" type="selection" width="55" key="1" v-if="importPromotionStatus == 1"></el-table-column>
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
        <el-table-column align="center" label="操作" :width="210">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleSee(scope.row)">查看</el-button>
            <el-button v-show="scope.row.applyStatus === 0" class="mini-browse-btn" @click="handleSubmit(scope.row)">提交</el-button>
            <el-button v-show="scope.row.applyStatus === 0" class="mini-delete-btn" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div>
        <div class="order-list-fun" v-show="importPromotionStatus === '1' && tableData.length > 0">
          <el-button class="mini-search-btn btn-box" :loading="submitLoading"  @click.prevent="handleSubmits()">批量提交</el-button>
          <el-button class="mini-delete-btn btn-box" :loading="submitLoading"  @click.prevent="handleDeletes()">批量删除</el-button>
        </div>
        <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      </div>
    </div>
    <el-dialog :modal-append-to-body="false" title="规则" :visible.sync="centerDialogVisible" width="30%" center>
      <span >{{promotionRuleDescribe}}</span>
    </el-dialog>
  </div>
</template>

<script>
import pagination from '@/components/pagination/index'
import { timeFormat } from '@/utils/timeFormat.js'
import { getPromotionList, promotionData } from '@/views/marketingPromotion/promotionData'

export default {
  name: 'promotionImportList',
  components: { pagination  } ,
  activated(){
    this.updateMainData();
  },
  created(){
    this.updateMainData();
  },
  data(){
    return {
      loading: false,
      submitLoading:false,
      importPromotionStatus: "1",
      centerDialogVisible: false,
      promotionRuleDescribe: '',
      search:{
        content: '', // 搜索用关键词
      },
      pageInfo: {
        page: 1,
        size: 10,
        promotionSource:2,// 批量导入
        applyStatus:0
      },
      total: 0,
      tableData: [],
      ids:[]
    }
  },
  methods:{
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
    show(row) { // 规则查看操作
      this.centerDialogVisible = true
      this.promotionRuleDescribe = row.promoDesc
    },
    handleSelectionChange(val) {
      this.ids = []
      val.forEach(item =>{
        this.ids.push(item.id)
      })
    },
    handleSee(row) { // 查看详情操作
      this.$router.push({ name: 'editPromotion', params: { id: row.id }})
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
    promoType(row, col, val){
      switch (val) {
        case 1:
          return '营销活动'
          break
        case 2:
          return '阶梯活动'
          break
      }
    },
    formatTime(row, col, val) { // 表格时间转换
      return timeFormat(val)
    },
    addpuls(){
      this.$router.push({ name: 'promotionImport'})
    },
    handleDeletes(){
      if(this.ids != undefined && this.ids.length >0){
        this.$confirm('是否批量删除活动?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.$http.deletePromotions(this, { ids: this.ids }).then(res => {
            if(res.success){
              this.$message.success({
                message: '批量删除成功',
                duration: 3 * 1000,
              })
            }
            if(this.pageInfo.page > 1){
              this.pageInfo.page = this.pageInfo.page - 1
            }
            this.updateMainData()
          })
        })
      }
    },
    handleSubmits(){
      if(this.ids != undefined && this.ids.length >0){
          this.$confirm('是否批量提交活动?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.$http.promotionSubmits(this, {applyStatus:1, ids: this.ids }).then(res => {
            if(res.success){
              this.$message.success({
                message: '批量提交成功',
                duration: 3 * 1000,
              })
              this.updateMainData()
            }
          })
        })
      }
    },
    handleSubmit(row){// 提交
    this.$confirm('提交此条促销活动?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.promotionSubmits(this, {applyStatus:1, ids: [row.id] }).then(res => {
          if(res.success){
            this.$message.success({
              message: '提交成功',
              duration: 3 * 1000,
            })
          }
          if(this.pageInfo.page > 1){
            this.pageInfo.page = this.pageInfo.page - 1
          }
          this.updateMainData()
        })
      })
    },
    handleDelete(row) { // 删除操作
      this.$confirm('删除此条促销活动?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.deletePromotions(this, { ids: [row.id] }).then(res => {
          if(res.success){
            this.$message.success({
              message: '删除成功',
              duration: 3 * 1000,
            })
            this.updateMainData()
          }
        })
      })
    },
    updateMainData(){
      if(this.importPromotionStatus === '1'){// 未提交
        this.pageInfo.applyStatus = 0
      }else if(this.importPromotionStatus === '2'){// 已提交
        this.pageInfo.applyStatus = undefined
      }
      this.loading = true
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
    onSizeCHange(val){ // 分页方法
      this.pageInfo.size = val;
      this.updateMainData();
    },
    onCurrentChange(val){ // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    }
  },
  watch: {
    importPromotionStatus(val) {
      this.tableData = [] 
      this.updateMainData();
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .order-list-wrap{
    height: 100%;
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
      .btn-home{
        float: right;
        padding: 5px;
        margin-top: 8px;
        margin-right: 8px;
        margin-left: 0;
        font-size: 12px;
      }
    }
    .order-list{
      background-color: white;
      height: 100%;
    }
    .order-list-fun{
      padding: 0px 16px 14px 16px;
      overflow: hidden;
      float: left;
      margin-top: 20px;
    }
  }
</style>
