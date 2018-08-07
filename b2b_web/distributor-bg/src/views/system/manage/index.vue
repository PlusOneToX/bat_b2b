<template>
  <div>
    <div class="add" ><!-- v-if="pageState == 'paymanage'" -->
      <div class="pay-list">
          <header>
          <h4>配送方式列表</h4>
          <el-button class="mini-add-btn manage-btn-home" icon="el-icon-plus" @click="addPay">
            添加配送方式
          </el-button>
        </header>
      </div>
      
      <div class="function" v-loading="loading">
        <el-table :data="tableData" header-row-class-name="header-row" border ref="multipleTable" class="table_size" @selection-change="handleSelectionChange">
          <el-table-column  align="center" label="ID" prop="id"> </el-table-column>
          <el-table-column  align="center" label="配送方式名称" prop="name"> </el-table-column>
          <el-table-column align="center" label="ERP配送方式编码" prop="logisticsErpId"> </el-table-column>
          <el-table-column align="center" label="计算方式" prop="billingMethod">
            <template slot-scope="scope">
              <div v-if="scope.row.billingMethod===1">按重量</div>
              <div v-else>按体积</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="排序"  prop="sort"> </el-table-column>
          <el-table-column align="center" label="状态" prop="enable">
            <template slot-scope="scope">
              <div v-if="scope.row.enable===0">关闭</div>
              <div v-else>启用</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="200">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="onEdit(scope.row,scope.$index)">查看</el-button>
              <el-button class="mini-delete-btn" @click="handleDelete(scope.row,scope.$index)">删除</el-button>
              </template>
          </el-table-column>
        </el-table>
        <pagination :page="pageInfo.page" :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      </div>
    </div>
    
    <!-- 引用组件 -->
    <!-- 添加配送方式 -->
    <addmanage v-if="pageState !== 'paymanage'"
      :pageState="pageState"
      @cancel="gocancel"
      @verifyChange="verifyChange"
    ></addmanage>
    

  </div>
</template>

<script>
import eventBus from "@/views/order/eventBus";
import pagination from "@/components/pagination/index";
// ===== 引用组件 =====
import addmanage from "@/views/system/manage/addmanage"
import compilepay from "@/views/system/pay/paylist/compilepay"
import { sortBy } from '@/utils/common'
import { getAllRegionList } from "@/views/system/systemData"
import store from '@/store'
  export default {
    name: 'managelist',
    components: {
      addmanage,compilepay,pagination
    },
    computed: {
      checkedData(){
        return this.tableData.payway
      },
    },
    mounted() {
      eventBus.$on('goback',payLoad => {
        this.pageState = payLoad
      })
    },
    data() {
      return {
        loading: false,
        multipleSelection: [],
        pageState: 'paymanage',
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
    created(){
      if(store.getters.areas === undefined || store.getters.areas.length === 0){
        // getAllRegionList(this).then(res =>{
        //   this.$store.commit('GET_ALL_AREAS', res.regionList)
        // })
        this.$http.regionList(this).then(res => {
          if (res.success) {
            this.$store.commit('GET_ALL_AREAS', res.data)
          }
        })
      }
    },
    activated() {
      this.payData();
    },
    methods: {
      // ======== 操作 ========
      addPay() { // 添加快递方式操作
        this.$router.push({name: 'addEditShipping'})
      },
      
      gocancel() { // 添加页面返回按钮
        this.pageState = 'paymanage';
        this.payData();
      },
      
      verifyChange() { // 验证公式返回
        this.pageState = "addVerify";
      },
      
      onEdit(row,index) { // 编辑操作
        this.$router.push({name : 'addEditShipping',query:{id: row.id}})
      },
      
      handleDelete(row) { // 删除操作
      this.$confirm('此操作将永久删除该配送方式, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        if(this.tableData.length === 1){
          this.pageInfo.page = this.pageInfo.page -1
        }
        this.$http.deleteLogistics(this, { id: row.id }).then(res => {  
          if(res.success) {
            this.$message.success({
              message: '删除成功',
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.payData()
          }
        })
      }).catch((err) => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
      },
      
      handleSelectionChange(val) { // 选中框的值 
        this.multipleSelection = val;
      },
      
      // ======== 数据 ========
      payData() { // 详情
        this.loading = true
        this.$http.logisticsList(this, this.pageInfo).then(res => {
          if (res.success) {
            let ary = [];
            ary = res.data.list;
            ary.sort((a, b) => {
              return a.sort - b.sort < 0;
            });
            ary.sort(sortBy('distributionOrder'))
            this.tableData = ary
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
      
      // ======== 转换 ========
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

      // 申请状态
      paystatus(row) {
        switch(row.payWay) {
          case 0:
            return '现款支付';
          case 1:
            return '货到付款';
          case 2:
            return '15天结算';
          case 3:
            return '30天结算';
          case 4:
            return '60天结算';
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
  .pay-list {
    header {
      padding-right: 10px;
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
    .manage-btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .nav {
    .nav_list {
      padding: 10px 0 0px 0;
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