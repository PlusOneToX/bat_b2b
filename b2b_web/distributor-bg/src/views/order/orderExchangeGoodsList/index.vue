<template>
<div>
  <div class="add-goods" v-if="pageState == 'orderGoodslist'">
    <div class="warehouse-list-wrap">
      <div>
        <header>
          <div style="margin-left:30px;">退换货申请列表</div>
        </header>
      </div>
    </div>

    <div class="function">
      <!-- <el-button size="mini" @click="changetoggle">确认</el-button> -->
      <!-- <el-button size="mini">取消</el-button> -->
      <!-- <el-button size="mini">移除</el-button> -->
      <!-- <el-button size="mini" style="border-radius: 5px;">导出</el-button> -->
        <el-select v-model="batch"  size="mini" @change="handleBatchChange">
          <el-option label="全部" :value="1"></el-option>
          <el-option label="申请中" :value="2"></el-option>
          <el-option label="待到货" :value="3"></el-option>
          <el-option label="部分到货" :value="4"></el-option>
          <el-option label="处理中" :value="5"></el-option>
          <el-option label="已完成" :value="6"></el-option>
          <el-option label="已拒绝" :value="7"></el-option>
          <el-option label="退款中" :value="8"></el-option>
        </el-select>
      <el-input placeholder="请输入内容"  v-model="content" size="mini" class="box-search"></el-input>
      <el-button size="mini" style="border-radius: 5px;" class="btn-search" @click="search">搜索</el-button>
    </div>
    

    <el-table :data="tableData" header-row-class-name="header-row"  border ref="multipleTable" @selection-change="handleSelectionChange" class="table_size">
        <!-- <el-table-column type="selection" align="center" width="55"> </el-table-column> -->
         <!-- <el-table-column label="操作" fixed="left" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="onEdit(scope.row,scope.$index)">
              编辑
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column label="退货单号" prop="userId" align="center">
        </el-table-column>

        <el-table-column align="center" label="申请日期" :formatter="timeFormatter" prop="updateTime" >
        </el-table-column>

        <el-table-column align="center" label="分销商用户名" prop="userName" >
        </el-table-column>

        <el-table-column align="center" label="申请状态" :formatter="forstatus" prop="status" :width="100" :max-height="100" > 
        </el-table-column>

        <el-table-column align="center" label="包裹登记时间" :formatter="timeFormatter" prop="courierTime">
        </el-table-column>

        <el-table-column align="center" label="到货时间" :formatter="timeFormatter" prop="arriveTime">
        </el-table-column>

        <el-table-column label="备注" prop="remark" align="center"></el-table-column>
        <el-table-column label="操作" align="center" min-width="80">
          <template slot-scope="scope">
            <el-button type="success" size="mini" style="border-radius:5px;" @click="onEdit(scope.row,scope.$index)">查看</el-button>
              <!-- <el-button type="danger" size="mini" @click="handleDelete(scope.row)" >移除</el-button> -->
            </template>
        </el-table-column>
    </el-table>
    <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    
  </div>

  <change  v-if="pageState !== 'orderGoodslist'" @redactPage="onRedactPage" :entryIndex="detailEntry" :rowData="rowData" :pageState="pageState" :tableData="tableData"></change>
</div>  
</template>

<script>
import eventBus from '@/views/order/eventBus'
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import change from '@/views/order/orderExchangeGoodsList/orderRedact';
import {Orderlist} from '@/views/order/orderData'
import orderRedact from '@/views/order/orderExchangeGoodsList/orderRedact.vue'

export default {
  name: 'orderExchangeGoodsList',
  components: {
    change, pagination
  } ,
  mounted() {
    window.addEventListener('keydown',this.onEscPress);
    window.addEventListener('keydown',this.show);
    eventBus.$on('callBack',payLoad => {
      this.pageState = payLoad
    })
    this.updata();
  },
  data() {
    return {
      rowData: "",
      radio: 1,
      batch: 1,
      state: "",
      total: 1,
      tableData: [],
      pageState: 'orderGoodslist',
      detailEntry: 0, // 被点击编辑的条目在tableData数组中的index pass to 子组件 
      chosenOrders : [], // 表格中被勾选的订单
      pageInfo:{
        page: 1,
        count: 10,
      },
      params: '',
      multipleSelection: [],
      content: '',
    }
  }, 
  computed: {
    checkedData(){
      return this.tableData[this.detailEntry]
    },
  },
  methods:{
    
    onRedactPage(payLoad){
      this.pageState = payLoad || 'orderGoodslist'
    },
    updata(statu) {
      // 退换货申请列表
      this.pageInfo.status = statu;
      this.$api.get(this,'admin/u/afterservice/list',this.pageInfo).then(res => {
        this.tableData = res.list
      })
      // 数据条数
      // this.pageInfo.count = counts
      this.$api.get(this,'admin/u/afterservice/count',this.pageInfo).then(res => {
        this.total = res.count;
      })
    },

    // 搜索键盘事件
    show(ev) {
      if(ev.keyCode==13){
        this.search()
      }
    },

    // 搜索按钮
    search() {
      switch(this.batch) {
        case 1:
          this.pageInfo.searchContent = this.content;
          this.updata()
        break;
        case 2:
          this.pageInfo.searchContent = this.content;
          this.updata(0)
        break;
        case 3:
          this.pageInfo.searchContent = this.content;
          this.updata(1)
        break;
        case 4:
          this.pageInfo.searchContent = this.content;
          this.updata(2)
        break;
        case 5:
          this.pageInfo.searchContent = this.content;
          this.updata(3)
        break;
        case 6:
          this.pageInfo.searchContent = this.content;
          this.updata(4)
        break;
        case 7:
          this.pageInfo.searchContent = this.content;
          this.updata(5)
        break;
        case 8:
          this.pageInfo.searchContent = this.content;
          this.updata(6)
        break;
      }
    },

    // 选中框的值
    handleSelectionChange(val){
      this.multipleSelection = val
    },

    // 确认选中按钮
    changetoggle() {
      let ids = [];
      this.multipleSelection.forEach(item => {
        ids.push(item.id)
      })
      // ids = ids.join(',')
      // ids = ids.valueOf();
      this.$api.put(this,'admin/u/afterservice',{id: ids,operateId: 1}).then(res => {
        if(res.code == 0) {
          this.updata()
        }
      })
    },   
    
    // 时间格式化
    timeFormatter(row, column, cellValue){ 
      return parseTime(row.createTime)
    },

    // 查看按钮
    onEdit(row,index) {
      this.pageState = 'details';
      this.detailEntry = index;
      this.rowData = row;
    },

    /**
     * 列表数据，状态值，譬如申请中，以已通过
     * 加上状态值的情况下，status，总数跟列表的接口需要变更
     * "status": 0, //0申请中,1待到货,2部分到货,3处理中,4已完成,5已拒绝,6退款中
     */
    handleBatchChange(){
      //0申请中,1待到货,2部分到货,3处理中,4已完成,5已拒绝,6退款中
      switch(this.batch)
      {
      case 1:
        this.updata()
      break;
      case 2:
        this.updata(0)
      break;
      case 3:
        this.updata(1)
      break;
      case 4:
        this.updata(2)
      break;
      case 5:
        this.updata(3)
      break;
      case 6:
        this.updata(4)
      break;
      case 7:
        this.updata(5)
      break;
      case 8:
        this.updata(6)
      break;
      }
    },
    // 申请状态
    forstatus(row) {
      switch(row.status) {
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
    
    // 分页
    onSizeCHange(val){ // 分页方法
      this.pageInfo.count = val;
      this.updata();
    },
     onCurrentChange(val){ // 分页方法
      this.pageInfo.page = val;
      this.updata();
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.add-goods {
  background-color: white;
  min-height: 100%;
  padding-bottom: 30px;
  .warehouse-list-wrap {
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
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
    padding: 16px 16px 16px 16px;
    border-top: 0px;
    background-color: #fff;
    .box-search {
      width: 140px;
      .btn-search {
        background-color: $lakeBlue;
          color: #fff;
      }
    }
  }
  // .header-row {
  //   th{
  //     text-align: center;
  //   }
  // }
  div.el-tabs__nav{
      margin-left: 30px;
  }
}
</style>
