<template>
    <div class="warehouse-list">
      <header>
        <h4>退款单列表</h4>
      </header>

      <div class="function">
        <div class="Fheader">
          <div class="f-left">
            <el-select size="mini" v-model="pageInfo.refundStatus" placeholder="请选择" style="width:120px;" clearable>
              <el-option label="待确认" :value="1"></el-option>
              <el-option label="已确认" :value="2"></el-option>
              <el-option label="已取消" :value="3"></el-option>
            </el-select>
            <el-select size="mini" clearable  v-model="pageInfo.refundWay" placeholder="退款方式" style="width: 120px;">
              <el-option label="支付宝" :value="1"> </el-option>
              <el-option label="微信" :value="2"> </el-option>
              <el-option label="区间结算" :value="3"> </el-option>
              <el-option label="线下转账" :value="4"> </el-option>
              <el-option label="余额支付" :value="5"> </el-option>
              <el-option label="快钱支付" :value="6"> </el-option>
            </el-select>
          </div>
          <div class="f-search">
            <button class="mini-search-btn box-btn" @click.prevent="filterBill()">搜索</button>
            <el-input placeholder="请输入搜索关键词" @keyup.enter.native="filterBill()"  size="mini" class="box-search" v-model.trim="pageInfo.content" clearable></el-input>
            <el-select
              class="content_select"
              placeholder="选择类型"
              size="mini"
              style="width:140px;float:right;"
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
      
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" type="index" width="50" fixed :min-width="50"></el-table-column>
        <el-table-column align="center" label="退款单号" prop="id" :min-width="120"></el-table-column>
        <el-table-column align="center" label="EPR退款单号" prop="refundErpNo" :min-width="120"></el-table-column>
        <el-table-column align="center" label="业务单号" prop="businessId" :min-width="120"></el-table-column>
        <el-table-column align="center" label="退款时间" prop="updateTime" :formatter="timeFormatter" :min-width="180"></el-table-column>
        <el-table-column align="center" label="分销商用户名" prop="distributorName" :min-width="120"></el-table-column>
        <el-table-column align="center" label="退款方式" prop="refundWay" :formatter="statusType" :min-width="120"></el-table-column>
        <el-table-column align="center" label="退款金额" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ scope.row.amount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="退款状态" prop="refundStatus" :min-width="120" :formatter="statusFormatter"></el-table-column>
        <el-table-column align="center" label="备注" prop="remark" :min-width="120"></el-table-column>
        <el-table-column align="center" label="操作" :min-width="80">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="preview(scope.$index,scope.row)" >查看</el-button>
            <!-- <el-button @click="turnDown(scope.row)">拒绝</el-button>
            <el-button @click="reimburse(scope.row)">退款</el-button> -->
            <!-- <el-button @click="cancel(scope.row)"v-if="scope.row.status==1">取消对账</el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" :page="pageInfo.page" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </div>
</template>
<script>
import pagination from '@/components/pagination/index'
import { parseTime } from '@/utils/index'

export default {
  name: 'refundOrder',

  components: { pagination },

  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        userId: 10000,
        refundStatus: undefined,
        refundWay: undefined,
        contentType: undefined,
        content: undefined
      },
      contentTypes: [
        { value: 1, label: '分销商用户名' }
      ],
      endTime: '',
      total: 1,
      tableData: [],
      action: 0,
      loading: false,
    }
  },

  activated() {
    this.getTableData()
  },

  methods: {
    // ======== 操作 ========
    getTableData() { // 退款单数据
      this.loading = true;
      this.$http.refundList(this, this.pageInfo).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
        res.success ? this.loading = false : this.loading = false
      })
    },
    
    filterBill() { // 搜索操作
      this.pageInfo.page = 1
      this.pageInfo.content = this.pageInfo.content
      this.getTableData()
    },

     preview(index, row) { // 查看操作
      this.$router.push({ name: 'refundOrderDetail', query: { id: row.id }})
    },
    
    reimburse(row) { // 退款操作
      this.$router.push({ name: 'refundOrderHandle', query: { id: row.id }})
    },
    // 暂不使用
    // turnDown(row) { // 拒绝操作
    //   this.$api.put(this, 'admin/u/p/refund/status', { id: row.id, status: 4 }).then(res => {
    //     if(res.code == 0) {
    //       this.$message.success({
    //         message: '成功拒绝',
    //         duration: 3 * 1000,
    //         onClose: () => { }
    //       })
    //       this.getTableData()
    //     } else {
    //       this.$message.error({
    //         message: '拒绝失败，请刷新重新删除',
    //         duration: 3 * 1000,
    //         onClose: () => { }
    //       })
    //       this.getTableData()
    //     }
    //   })
    // },

    // ======== 数据 ========
    onSizeCHange(val) { // 条数
      this.pageInfo.size = val
      this.getTableData()
    },

    onCurrentChange(val) { // 页数
      this.pageInfo.page = val
      this.getTableData()
    },

    // ======== 转换 ========
    statusFormatter(r, c, v) { // 退款状态
      switch (v) {
        case 1:
          return '待确认'
          break
        case 2:
          return '已确认'
          break
        case 3:
          return '已取消'
          break
        default:
          return '-'
      }
    },

    statusType(r, c, v) { // 退款方式
      switch (v) {
        case 1:
          return '支付宝'
          break
        case 2:
          return '微信'
          break
        case 3:
          return '区间结算'
          break
        case 4:
          return '线下转账'
          break
        case 5:
          return '余额支付'
          break
        case 6:
          return '快钱支付'
          break  
        default:
          return '-'
      }
    },

    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    }
  },
  watch: {
    'pageInfo.refundStatus': function() {
      this.pageInfo.page = 1
      this.getTableData()
    },
    'pageInfo.refundWay': function() {
      this.pageInfo.page = 1
      this.getTableData()
    }
  }
}

</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.warehouse-list {
  background-color: white;
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
      background-color: white;
      overflow: hidden;
      .search {
        float: right;
        overflow: hidden;
        .box-btn {
          float: right;
          margin-left: 5px;
        }
        .box-search {
          width: 190px;
        }
      }
      
      .Fheader {
        padding: 10px;
        display: inline-block;
        width:100%;
        .f-left{
          display: inline-block;
        }
        .f-search {
          display: inline-block;
          .box-search {
            width: 180px;
            float:right;
          }
          .box-btn {
            float: right;
            margin-left: 5px;
          }
        }
      }
    }
}
</style>
