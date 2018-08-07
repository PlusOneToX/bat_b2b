<template>
  <div class="warehouse-list-wrap">
    <header>
      <span>{{pageStateFormatter()}}</span>
      <el-button @click="onSwitchPageState()" v-show="pageState == 'orderDetail'" class="btn-home">
        返回价格申请
      </el-button>
      <el-button @click="onSwitchPageState('orderDetail')" v-show="pageState != 'rootPage' && pageState != 'orderDetail'" class="btn-home">
        返回订单详情
      </el-button>
    </header>
    <div class="warehouse-list" v-show="pageState == 'rootPage'">
      <el-tabs v-model="checkType" @tab-click="updateMainData">
        <el-tab-pane label="我发起的" name="1"></el-tab-pane>
        <el-tab-pane label="待我审批" name="2"></el-tab-pane>
        <el-tab-pane label="我已审批" name="3"></el-tab-pane>
      </el-tabs>
      <div class="function clearfix">
        <!-- <el-button size="mini">导出</el-button> -->
        <div class="search">
          <el-select size="mini" v-model="orderFilter.deliverStatus" placeholder="请选择" style="width:120px">
            <el-option label="全部" value="0"> </el-option>
            <el-option label="未发货" value="1"> </el-option>
            <el-option label="部分发货" value="2"> </el-option>
            <el-option label="已发货" value="3"> </el-option>
          </el-select>
          <el-input placeholder="请输入内容" v-model="search.content" size="mini" class="box-search"></el-input>
          <el-button size="mini" class="btn-search" @click="onSearch">搜索</el-button>
        </div>
      </div>

      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row">
        <el-table-column align="center" type="index" fixed>
        </el-table-column>
        <el-table-column  align="center" prop="orderId" label="订单号"> </el-table-column>
        <el-table-column align="center" :formatter="timeFormatter" prop="createTime" label="申请日期"> </el-table-column>
        <el-table-column align="center" :formatter="capitalStatusFormatter" prop="status" label="审核状态"> </el-table-column>
        <el-table-column align="center" :formatter="timeFormatter" prop="lastCheckTime" label="审核时间">
        </el-table-column>
        <el-table-column align="center" label="操作" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="onCheck(scope.row, scope.$index)" >
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </div>
    <detail @switchPageState="onSwitchPageState" @checkNextOrder="changeCheckedOrder(0)" @checkPreOrder="changeCheckedOrder(1)"
    :pageState="pageState" :entryIndex="detailEntry" :tableData="tableData" v-if="pageState != 'rootPage'">
    </detail>
  </div>
</template>

<script>
import eventBus from '@/views/order/applyForPrice/eventBus'
import pagination from '@/components/pagination/index'
import detail from '@/views/order/orderList/orderDetail'

import {parseTime} from '@/utils/index'
import {getDiscountChecks} from '@/views/order/orderData'
import {capitalStatusFormatter} from '@/views/order/orderUtils'
const
  name = 'applyForPrice';

export default {
  name: 'applyForPrice',
  mounted(){
    eventBus.$on('switchPageState', payLoad => {
      this.onSwitchPageState(payLoad);
    })
    
    this.updateMainData();
  },
  components: { pagination, detail } ,
  data(){
    return {
      pageState: 'rootPage', //页面状态 0 -> 价格申请 1 -> 订单详情 2 -> 编辑
      detailEntry: 0,
      chosenOrders : [], // 表格中被勾选的订单
      orderFilter:{
        orderStatus: null, //订单状态
        payStatus: null, //付款状态
        deliverStatus: null, //发货状态
        orderType: '0',
        orderStatus: null,
      },
      search:{
        contentType: null, // 搜索用关键词类型
        content: null, // 搜索用关键词
      },
      checkType: "1",
      pageInfo: {
        page: 1,
        count: 10
      },
      total: 1,
      tableData: [], // 表格信息 @main Array of orders!
    }
  },
  computed: {
    checkedData(){
      return this.tableData[this.detailEntry]
    },
  },
  methods:{
    onSwitchPageState(payLoad){
      this.pageState = payLoad || 'rootPage' ;
    },
    onSearch(){
      this.updateMainData([this.search]);
    },
    onCheck(chosenOne, index){
      this.$router.push({
        name: 'applyForPriceDetail',
        params: {orderId: chosenOne.orderId},
        query: {
          orderIdList: JSON.stringify(this.tableData.map(data => data.orderId)),
          currentIndex: index,
          applyId: chosenOne.id
        }
      })
      // this.$router.push({name:'applyFor  PriceDetail',params:{id:row.id}})
      this.detailEntry = index;
    },
    /**
     * 发出请求(主要数据)，并更新本地数据
     * @param  {array of obj} paramsToJoin 要拼装的对象们
     * orderFilter的数据默认需要
     */
    updateMainData(paramsToJoin = []){ 
      getDiscountChecks(this, {checkType: this.checkType}) // 请求主要数据
      .then(res => {
        this.tableData = res.checks;
      })

      // get数据条数
    },
    // chosenOrdersIds :: [order] -> String
    // 本函数供批量操作(确认、取消等)获取ids
    chosenOrdersIds(){
      const chosenOrders = this.chosenOrders ;
      let ids = chosenOrders[0].id;
      // 拼凑需要确认的订单ids
      for(let i = 1, len = chosenOrders.length; i < len; i++){
        ids = ids + ',' + chosenOrders[i].id;
      }

      return ids;
    },
    // 子组件翻页行为，类似于iterator
    changeCheckedOrder(actionType){
      switch(actionType){
        case 0:
          this.detailEntry = this.detailEntry + 1;
          break;
        case 1:
          this.detailEntry = this.detailEntry - 1;
          break;
      }
    },
    /**
     * 高阶函数-修饰器，可return批量操作函数
     * operateOrderBy :: Function -> Function
     * @param  {Function} operateCallback confirmOrder、cancelOrder之类
     * @return {Function}                 批量操作函数
     */
    operateOrderBy(operateCallback){
      return _ => {
        // 检查是否有选中的订单
        if(!this.chosenOrders[0]){
          this.$message({message: '请选择要操作的订单', type:'warning'})
          return 0;
        }
        // 获取ids
        const ids = this.chosenOrdersIds();
        operateCallback(this, {ids}) // 操作数据库中的订单
        .then(res => { // 显示响应的message
          this.$message({message: res.message, type:'success'})
          this.updateMainData();
        })
        .catch(e => console.log(e))
      }
    },
    onSizeCHange(val){ // 分页方法
      this.pageInfo.count = val;
      this.updateMainData();
    },
    onCurrentChange(val){ // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },
    timeFormatter(row, column, cellValue){ // 时间格式化
      return parseTime(cellValue)
    },
    capitalStatusFormatter(row, column, cellValue){
      return capitalStatusFormatter(cellValue)
    },
    pageStateFormatter(){
      switch(this.pageState){
        case 'rootPage':
          return '价格申请'
        case 'orderDetail':
          return '订单详情'
        case 'editCostInfo':
          return '费用信息'
        case 'editDeliveryInfo':
          return '收货人信息'
        case 'editDistributionMode':
          return '配送方式'
        case 'editGoodsInfo':
          return '商品信息'
        case 'editOtherInfo':
          return '其他信息'
        case 'editOperationInfo':
          return '操作信息'
        default:
          return '订单'
      }
    },
  },
  watch: {
    // 'orderFilter.orderType': {
    //   handler(){
    //     this.updateMainData();
    //   },
    //   deep: true
    // }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .warehouse-list-wrap{
    height: 100%;
    // min-width: 1050px;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-home{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .warehouse-list{
      background-color: white;
      height: 100%;
      .function{
        padding: 0px 16px 16px 16px;
        // background-color: white;
        .btn-export{
          background-color: lighten(grey, 40%);
        }
        .search{
          float: right;
        }
        .box-search{
          width: 140px;
        }
        .btn-search{
          background-color: $lakeBlue;
          color: white;
        }
      }

      /*background-color: blue;*/
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
      .table-cell{
        text-align: center;
      }
      .el-form{
        width: 700px;
        margin-top: 30px;
      }
      div.el-tabs__nav{
        margin-left: 30px;
      }
    }
  }
</style>
