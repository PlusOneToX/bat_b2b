<template>
  <transition name="fade" mode="out-in">
    <div class="warehouse-list">
      <header>
        <span>订单列表</span>
      </header>
      <div class="function clearfix">
        <el-col :span="2">
          <el-button size="mini" @click="selectTime=true">发起对账</el-button>
        </el-col>
        <!-- <el-col :span="3" :offset="1">
          <el-select v-model="action" size="mini">
            <el-option label="导入" :value="1"></el-option>
            <el-option label="导出" :value="2"></el-option>
          </el-select>
        </el-col> -->
        <el-col :span="3" :offset="1">
          <el-select v-model="pageInfo.status" size="mini">
            <el-option label="对账中" :value="1"></el-option>
            <el-option label="已拒绝" :value="2"></el-option>
            <el-option label="折扣申请" :value="3"></el-option>
            <el-option label="已取消" :value="4"></el-option>
            <el-option label="已完成" :value="5"></el-option>
          </el-select>
        </el-col>
        <el-col :span="3" :offset="1">
          <el-select v-model="pageInfo.searchFiled" size="mini">
            <el-option label="分销商用户名" value="name"></el-option>
            <el-option label="公司名" value="company_name"></el-option>
            <el-option label="姓名" value="register_name"></el-option>
            <el-option label="手机" value="mobile"></el-option>
            <el-option label="电话" value="phone"></el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-input size="mini" v-model="pageInfo.searchContent" placeholder="请输入搜索内容"></el-input>
        </el-col>
        <el-col :span="2" :offset="1">
          <el-button type="primary" size="mini" @click="filterBill">搜索</el-button>
        </el-col>
      </div>
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row">
        <el-table-column align="center" label="对账单号" prop="id"></el-table-column>
        <el-table-column align="center" label="分销商用户名" prop="name"></el-table-column>
        <el-table-column align="center" label="支付方式" prop="payType" :formatter="payTypeFormatter"></el-table-column>
        <el-table-column align="center" label="对账开始时间" prop="startTime" :formatter="timeFormatter" width="150px"></el-table-column>
        <el-table-column align="center" label="对账结束时间" prop="updateTime" :formatter="endTimeFormatter" width="150px"></el-table-column>
        <el-table-column align="center" label="总金额(元)" prop="totalAmount"></el-table-column>
        <el-table-column align="center" label="折扣金额(元)" prop="discountAmount"></el-table-column>
        <el-table-column align="center" label="对账金额(元)" prop="billAmount"></el-table-column>
        <el-table-column align="center" label="对账状态" prop="status" :formatter="statusFormatter"></el-table-column>
        <el-table-column align="center" label="操作" fixed="right" min-width="180">
          <template slot-scope="scope">
            <el-button size="mini" @click="preview(scope.row)" type='primary'>查看</el-button>
            <el-button size="mini" @click="cancel(scope.row)" type='primary' v-if="scope.row.status==1">取消对账</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      <el-dialog :modal-append-to-body="false" :visible.sync="selectTime" center>
        <el-form label-width="150px">
          <el-form-item label="对账截止时间">
            <el-date-picker type='datetime' placeholder="请选择时间" v-model="endTime"></el-date-picker>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="success" @click="checkEndTime">确定</el-button>
        </div>
      </el-dialog>
      <el-dialog :modal-append-to-body="false" :visible.sync="selectDistributor" center>
        <el-row style="margin-bottom: 20px;">
          <el-col :span="10">
            <el-input v-model="pInfo.content" size="mini"></el-input>
          </el-col>
          <el-col :span='5' :offset="2">
            <el-button type="primary" size="mini" @click="filterDistributor" >搜索</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-table :data="distributorList" border header-row-class-name="header-row">
            <el-table-column align="center" label="分销商用户名" prop="name"></el-table-column>
            <!-- <el-table-column label="分销商等级"></el-table-column> -->
            <!-- <el-table-column label="销售区域"></el-table-column> -->
            <!-- <el-table-column label="公司名"></el-table-column> -->
            <el-table-column align="center" label="上次对账时间" prop="lastBillTime" :formatter="timeFormatter"></el-table-column>
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="primary" @click="checkDistributor(scope.row)" size="mini">选择</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
      </el-dialog>
    </div>
  </transition>
</template>
<script>
import pagination from '@/components/pagination/index'
import { parseTime } from '@/utils/index'

export default {
  components: { pagination },
  data() {
    return {
      pageInfo: { //分页信息
        page: 1,
        count: 10,
        status: 1,
        searchFiled: 'name',
        searchContent: ''
      },
      pInfo: {
        page: 1,
        count: 10,
        endTime: '',
        content: ''
      },
      endTime: '',
      total: 1, //分页信息
      tableData: [], // 表格信息 important
      action: 1,
      selectTime: false,
      selectDistributor: false,
      distributorList: [],
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    getTableData() {
      this.$api.get(this, 'admin/u/p/bill/list', this.pageInfo).then(res => {
        if(res.code == 0) {
          this.tableData = res.bills;
        }
      })
    },
    filterBill(){
      this.getTableData()
    },
    filterDistributor(){
      this.getDistributorList()
    },
    getDistributorList(){
        this.$api.get(this, 'admin/u/p/bill/distributor/list', this.pInfo).then(res => {
        let ids = res.distributors.map(v => { return v.distributorId })
        this.$api.get(this, 'admin/u/po/distributor/common/list', { ids: ids.join(',') }).then(result => {
          result.distributors.forEach(item => {
            res.distributors.forEach(val => {
              if(val.distributorId == item.id)
                item.lastBillTime = val.lastBillTime
            })
          })
          this.distributorList = result.distributors.filter(item => {
            return item.freezeStatus == 0 && item.capitalStatus == 2
          })
          this.selectTime = false;
          this.selectDistributor = true;

        })
      })
    },
    checkEndTime() {
      let time = Date.parse(this.endTime)
      this.pInfo.endTime = time;
      this.getDistributorList()
    },
    payTypeFormatter(r, c, v) {
      switch(v) {
        case 1:
          return '支付宝';
          break;
        case 2:
          return '微信';
          break;
        case 3:
          return '期间结算';
          break;
        case 4:
          return '线下转账';
          break;
        case 5:
          return '余额支付';
          break;
        default:
          return '-'
      }
    },
    statusFormatter(r, c, v) {
      switch(v) {
        case 1:
          return '对账中';
          break;
        case 2:
          return '已拒绝';
          break;
        case 3:
          return '折扣申请';
          break;
        case 4:
          return '已取消';
          break;
        case 5:
          return '已完成';
          break;
        default:
          return '-'
      }
    },
    endTimeFormatter(r, c, v) {
      if(r.status == 5) {
        return parseTime(v)
      } else {
        return '未完成'
      }
    },
    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    },
    preview(row) {
      this.$router.push({name:'accountdetail',query:{id:row.id}})
    },
    cancel(row) {

    },
    checkDistributor(row) {
      this.$router.push({ name: 'addaccount', query: { id: row.id,endTime:this.pInfo.endTime } })
    },
    getTotal() { // 分页方法
      return 0;
    },
    onSizeCHange(val) { // 分页方法
      this.pageInfo.count = val;
    },
    onCurrentChange(val) { // 分页方法
      this.pageInfo.page = val;
    },

  },
  watch: {
    pageInfo: {
      handler() {
        this.getTableData()
      },
      deep: true
    }
  },
}

</script>
<style rel="stylesheet/scss" lang="scss">
.warehouse-list {
  background-color: white;
  height: 100%;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    span {
      margin-left: 30px;
    }
    .btn-add {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
    }
  }
  .function {
    padding: 16px;
    background-color: white;
    .btn-export {
      background-color: lighten(grey, 40%);
    }
    .search {
      float: right;
    }
    .box-search {
      width: 140px;
    }
    .btn-search {
      background-color: $lakeBlue;
      color: white;
    }
  }
  .header-row {
    background-color: $table-header-bg;
    th {
      padding: 5px;
      text-align: center;
    }
  }
  td {
    text-align: center;
  }
  .el-form {
    width: 700px;
    margin-top: 30px;
  }
}

</style>
