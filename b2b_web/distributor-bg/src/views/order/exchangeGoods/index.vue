<template>
<div>
  <div class="add-goods" v-if="pageState == 'exchangeGoods'">
    <div class="warehouse-list-wrap">
      <div>
        <header>
          <div style="margin-left:30px;">换货单列表</div>
        </header>
      </div>
    </div>

    <div class="function">
      <!-- <el-button size="mini" @click="changetoggle">确认</el-button> -->
      <!-- <el-button size="mini">取消</el-button> -->
      <!-- <el-button size="mini">打印订单</el-button> -->
      <!-- <el-button size="mini" style="margin-right:30px;">导出</el-button> -->
      <el-select v-model="batch"  size="mini" @change="handleBatchChange" style="margin-right:30px;">
        <el-option label="处理中" :value="1"></el-option>
        <el-option label="已完成" :value="2"></el-option>
      </el-select>
      <el-input placeholder="请输入内容"  v-model="content" size="mini" class="box-search"></el-input>
      <el-button size="mini" class="btn-search" @click="search">搜索</el-button>
    </div>


    <el-table :data="tableData" header-row-class-name="header-row"  border ref="multipleTable" @selection-change="handleSelectionChange" class="table_size">
        <el-table-column align="center" label="退货订单号" prop="id" >
        </el-table-column>

        <el-table-column align="center" label="退换货申请单号" prop="distributorId">

        </el-table-column>

        <el-table-column align="center" label="下单时间" :formatter="timeFormatter" prop="createTime">
        </el-table-column>

        <el-table-column align="center" label="订单状态" :formatter="forstatus" prop="status">
        </el-table-column>

        <el-table-column align="center" label="发货状态" :formatter="shipments" prop="status">
        </el-table-column>

        <el-table-column align="center" label="配送方式" :formatter="delivery">
        </el-table-column>

        <el-table-column align="center" label="操作" min-width="150">
          <template slot-scope="scope">
            <el-button type="success" size="mini" @click="onEdit(scope.row,scope.$index)">查看</el-button>
          </template>
        </el-table-column>
    </el-table>
    <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>

  </div>
  <!-- @redactPage="onRedactPage" -->
  <change  v-if="pageState !== 'exchangeGoods'"
  @up="upData"
  :entryIndex="detailEntry"
  :pageState="pageState"
  :tableData="tableData"></change>
</div>
</template>

<script>
import eventBus from '@/views/order/eventBus'
import pagination from "@/components/pagination/index";
import { parseTime } from "@/utils/index";
import change from '@/views/order/exchangeGoods/exchangeReadct';
import {Orderlist} from '@/views/order/orderData'

export default {
  name: 'exchangeGoods',
  components: {
    change, pagination
  } ,
  mounted() {
    window.addEventListener('keydown',this.onEscPress);
    window.addEventListener('keydown',this.show);
    // 详情页面返回监听
    eventBus.$on('downBcak',payLoad => {
      this.pageState = payLoad
    })
    // 生成发货单返回监听
    eventBus.$on('backChange',payLoad => {
      this.pageState = payLoad
    })

    // 发货成功监听返回
    eventBus.$on('shipments',payLoad => {
      this.pageState = payLoad
    })

    this.updata();
  },
  data() {
    return {
      radio: 1,
      batch: 1,
      state: "",
      total: 1,
      tableData: [],
      pageState: 'exchangeGoods',
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

    // onRedactPage(payLoad){
    //   this.pageState = payLoad || 'exchangeGoods'
    // },
    upData(payLoad) {
      this.pageState = payLoad || 'createDelivery'
    },
    updata(statu) {
      // 退换货申请列表
      this.pageInfo.status = statu;
      this.$api.get(this,'admin/u/p/swapOrder/list',this.pageInfo).then(res => {
        this.tableData = res.swapOrders
      })
      // 数据条数
      // this.pageInfo.count = counts
      this.$api.get(this,'admin/u/p/swapOrder/list/count',this.pageInfo).then(res => {
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
      this.pageState = 'exchangeReadct';
      this.detailEntry = index;
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
          return '处理中';
        case 1:
          return '已完成';
      }
    },
    // 发货状态
    shipments(row) {
      switch(row.status) {
        case 0:
          return '未发货';
        case 1:
          return '部分发货';
        case 2:
          return
      }
    },
    //配送方式
    delivery(row) {
      switch(row.deliverName) {
        case 1:
          return "顺丰快递";
        case 2:
          return "申通快递";
        case 3:
          return "圆通快递";
        case 4:
          return "中通快递";
        case 5:
          return "百世快递";
        case 6:
          return "韵达快递";
        case 7:
          return "天天快递";
        case 8:
          return "中国邮政";
        case 9:
          return "EMS";
        case 10:
          return "宅急送";
        case 11:
          return "德邦物流";
        case 12:
          return "全峰快递";
        case 13:
          return "优速快递";
        case 14:
          return "汇通快递";
        case 15:
          return "速尔快递";
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
.main {
  background-color: #fff;
}
.add-goods {
  background-color: white;
  min-height: 100%;
  padding-bottom: 30px;
  margin-right: 0 !important;
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
  .table_size {
    text-align: center;
  }
  div.el-tabs__nav{
      margin-left: 30px;
  }
}
</style>
