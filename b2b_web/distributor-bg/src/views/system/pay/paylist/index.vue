<template>
  <div>
    <div class="add">
      <div class="pay-list">
        <div>
          <header>
            <h4>收款条件列表</h4>
            <el-button class="mini-add-btn pay-btn-home" icon="el-icon-plus" @click="addPay">
              添加收款条件
            </el-button>
          </header>
        </div>
      </div>
      <div class="function" v-loading="loading">
          <el-table :data="tableData" header-row-class-name="header-row"  border ref="multipleTable" class="table_size">
            <el-table-column align="center" label="收款条件ID" prop="erpSettleAccountNo">
            </el-table-column>
            <el-table-column align="center" label="支付方式(中文)" prop="name"></el-table-column>
            <el-table-column align="center" label="支付方式(英文)" prop="nameEn"></el-table-column>
            <el-table-column align="center" label="结算时长">
              <template slot-scope="scope">
                <span>{{scope.row.payWay === 1 ? "立即支付":(scope.row.settlingTime+"天")}}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="状态">
              <template slot-scope="scope">
                <span>{{scope.row.openFlag === 1 ? "已启用":"已停用"}}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" min-width="100">
              <template slot-scope="scope">
                <el-button class="mini-search-btn" @click="onEdit(scope.row,scope.$index)">查看</el-button>
                <el-button class="mini-tableadd-btn" @click="handleStartStop(scope.row,scope.$index)" v-if="scope.row.openFlag === 0">启用</el-button>
                <el-button class="mini-freeze-btn" @click="handleStartStop(scope.row,scope.$index)" v-else>停用</el-button>
                <el-button class="mini-delete-btn" @click="handleDelete(scope.row,scope.$index)" v-show="scope.row.openFlag === 0">删除</el-button>
                </template>
            </el-table-column>
          </el-table>
        <pagination :page="pageInfo.page" :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      </div>

    </div>

    <!-- 引用组件 -->
    <!-- 添加支付方式 -->
    <!-- <addpay v-if="pageState !== 'payment'"
    :pageState="pageState"
    @cancel="gocancel"
    ></addpay> -->
    <!-- 编辑支付方式 -->
    <!-- :entryIndex="detailEntry" :tableData="tableData" -->
    <!-- <compilepay v-if="pageState !== 'payment'"
    :pageState="pageState"
    :particulars="particulars"
    @cancel="gocancel"
    @updatepay="updatepay"
    ></compilepay> -->

  </div>
</template>

<script>
import pagination from "@/components/pagination/index";
// ===== 引用组件 =====
  export default {
    name: 'paylist',
    components: {
      pagination
    },
    computed: {
    checkedData(){
      return this.tableData.payway
    },
  },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 1,
        etailEntry: 0, // 被点击编辑的条目在tableData数组中的index pass to 子组件
        pageInfo: {
          page: 1,
          size: 10
        },
        particulars: "",
      }
    },
    mounted() {
      this.payData();
    },
    activated() {
        this.payData();
    },
    methods: {
      // 编辑按钮
      onEdit(row,index) {
        this.$router.push({ name: 'editpay',params:{id: row.id}})
        // this.pageState = 'paycompile';
        // this.particulars = row;
      },

      addPay(){ //添加操作
        this.$router.push({ name: 'addpay'})
      },

      handleDelete(row) { // 删除操作
        this.$confirm('删除此条收款条件吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
        }).then(() => {
          this.$http.delTrade(this, {id: row.id}).then(res => {
            this.payData()
          });
        })
      },

      handleStartStop(row,index) { // 停用操作
        let msg = row.openFlag === 0 ? '启用此条收款条件吗?' : '停用此条收款条件吗?'
        this.$confirm(msg, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
        }).then(() => {
          this.$http.tradeStatus(this, {id: row.id,openFlag: row.openFlag === 0?1:0}).then(res => {
            if (res.success) {
              this.payData()
            }
          })
        })
      },

      payData() { // 列表
        this.loading = true;
        this.$http.tradeList(this, this.pageInfo).then(res => {
          if (res.success) {
            this.tableData = res.data.list
            this.total = res.data.total
            this.loading = false
          } else {
            this.looking = false
          }
        })
      },


      onSizeCHange(val){ // 分页方法
        this.pageInfo.size = val;
        this.payData();
      },

      onCurrentChange(val){ // 分页方法
        this.pageInfo.page = val;
        this.payData();
      },


      paywayStatus(row) { // 支付方式
        switch(row.name) {
          case 0:
            return '申请中';
          case 1:
            return '待到货';
          case 2:
            return '部分到货';
          case 3:
            return '处理中';
          case 4:
            return '已完成';
          case 5:
            return '已拒绝';
          case 6:
            return '退款中'
        }
      },


      paystatus(row) { // 申请状态
        switch(row) {
          case 0:
            return '启用';
          case 1:
            return '禁用';
          default:
            return '_'
        }
      },
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.main {
  background-color: #fff
}
.add {
  background-color: #Fff;
  min-height: 100%;
  padding-bottom: 30px;
  .pay-list {
    header {
    color: white;
    padding-right: 10px;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    .pay-btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .btn-add{
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    font-size: 12px;
  }
  }
  .nav {
    width: 100%;
    .nav_list {
      width: 100%;
      padding: 10px 0 0px 0;
      .nav_heder {
        width: 100%;
      }
    }
  }
  .el-radio-button__inner {
    border: 0;
  }
  .btn {
    float: right;
    width: 80px;
    margin-right: 5%;
  }
  .function {
    // padding: 16px 16px 16px 16px;
    border-top: 0px;
    background-color: #fff;
    .table_size {
      text-align: center;
    }
  }
  div.el-tabs__nav{
      margin-left: 30px;
  }
}
</style>
