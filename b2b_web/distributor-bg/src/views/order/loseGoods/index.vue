<template>
  <div class="lose-goods-wrap">
    <header>
      <h4>缺货登记列表</h4>
    </header>
    <div class="warehouse-list" >
      <!-- <el-tabs v-model="isProcess" @tab-click="updateMainData">
        <el-tab-pane label="全部" name="0"></el-tab-pane>
        <el-tab-pane label="未到货" name="1"></el-tab-pane>
        <el-tab-pane label="已到货" name="2"></el-tab-pane>
      </el-tabs> -->
      <div class="function clearfix">
        <button class="mini-batch-btn" style="margin:10px;" @click="onDeleteBatch">批量删除</button>
        <div class="search">
          <el-input placeholder="商品名称/商品编号" @keyup.enter.native="onSearch()" v-model.trim="search.content" size="mini" class="box-search"></el-input>
          <button class="mini-search-btn" @click="onSearch()">搜索</button>
        </div>
      </div>

      <el-table :data="tableData" border :max-height="700" style="width:100%" header-row-class-name="header-row" @selection-change="onSelectionChange" class="tableCenter" v-loading="loading" :height="tableHeight">
        <el-table-column  align="center" type="selection" :min-width="40"> </el-table-column>
        <el-table-column align="center" type="index" fixed :min-width="40"> </el-table-column>
        <el-table-column align="center" prop="goodsName" label="缺货商品名称" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="goodsNo" label="商品编号" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="goodsLoseErpNo" label="ERP预测单号" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="userName" label="分销商用户名" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="needNumber" label="数量" :min-width="120"> </el-table-column>
        <el-table-column align="center" prop="createTime" :formatter="timeFormatter" label="登记时间" :min-width="150"> </el-table-column>
        <!-- <el-table-column prop="arrival" :formatter="booleanFormatter" label="是否到货" :min-width="150"> </el-table-column>
        <el-table-column prop="isProcess" :formatter="booleanProcess" label="是否处理" :min-width="150"> </el-table-column> -->
        <el-table-column align="center" label="操作" :min-width="150">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="onCheck(scope.row, scope.$index)"> 查看 </el-button>
            <el-button class="mini-delete-btn" @click="onDelete(scope.row, scope.$index)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </div>
  </div>
</template>

<script>
import eventBus from '@/views/order/applyForPrice/eventBus'
import page from "@/components/pagination";

import {parseTime} from '@/utils/index'
import {getlosegoodsList, getlosegoodsListCount, getDistributorByIds, getGoodsByIds, handleDelete} from '@/views/order/orderData'
import {booleanFormatter, booleanProcess, asyncPatch, confirmCreator} from '@/views/order/orderUtils'
const
  name = 'applyForPrice',
  _ = require('ramda');

export default {
  name: 'loseGoods',
  components: { page } ,
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.82  -  100); // 计算表高度，固定表头
  },
  mounted(){
    eventBus.$on('switchPageState', payLoad => {
      this.onSwitchPageState(payLoad);
    })
    this.updateMainData();
  },
  data(){
    return {
      tableHeight:600,
      loading: false,
      detailEntry: 0,
      chosenData: {}, // 表格中被勾选的订单
      chosenLoses : [], 
      tableData: [],
      orderFilter:{
        orderStatus: null, //订单状态
        payStatus: null, //付款状态
        deliverStatus: null, //发货状态
        orderType: '0',
      },
      search:{
        contentType: null, // 搜索用关键词类型
        content: null, // 搜索用关键词
      },
      isProcess: "0",
      pageInfo: {
        page: 1,
        count: 10
      },
      total: 1,
    }
  },
  methods:{
    // ======== 操作 ========
    onSearch(){ // 搜索操作
      this.updateMainData([this.search]);
    },

    onCheck(row, index){ // 查看操作
      this.$router.push({ name: 'loseDetail',query: {id: row.id}})
    },

    onDelete(chosenOne, index){ // 删除操作
      const req = _ => handleDelete(this, {ids: chosenOne.id});
      confirmCreator(this)('删除缺货', req).then(_ => this.updateMainData())
    },

    onDeleteBatch(){ // 批量删除操作
      const
        ids = this.chosenLoses
        .map(lose => lose.id)
        .reduce((prev, cur, index) => prev + (index != 0? ',': '') + cur , '');
      
      if(!ids){ //若没有勾选项目,则终止操作
        this.$message({message: '请勾选要操作的项目', type:'warning'});
        return
      }

      const req = _ => handleDelete(this,{ids});

      confirmCreator(this)('批量删除缺货', req).then(_ => this.updateMainData());
    },

    // ======== 数据 ========
    /**
     * 发出请求(主要数据)，并更新本地数据
     * @param  {array of obj} paramsToJoin 要拼装的对象们
     * orderFilter的数据默认需要
     */
    updateMainData(){ // 详情数据
      this.loading = true;
      getlosegoodsListCount(this,{isProcess: this.isProcess}).then(res => { // get数据条数
        this.total = res.count;
      })
      // const patchTable = _.curry(asyncPatch)(this); // 柯里化,第0次局部传参,

      // let
        // patchUserName = patchTable('userId')('id'),
        // patchGoodsName = patchTable('userId')('id'); // 第1次局部传参, 对暗号

      this.pageInfo.isProcess = this.isProcess // 是否处理
      this.pageInfo.content= this.search.content // 搜索内容
      const mainData = getlosegoodsList(this,this.pageInfo).then(res => { // 请求主要数据第1步
        this.tableData = res.list;
        // return new Promise((resolve, reject) => {
        //   if(res.list.length){
        //     resolve(res.list)
        //   }else{
        //     reject('无数据')
        //   }
        // })
        res.code == 0 ? this.loading = false : this.looking = false
      });

      // mainData.then(loseGoodsList => { // 请求联系人名
      //   // 拼装ids
      //   let ids = loseGoodsList
      //     .map(lose => lose.userId)
      //     .reduce((prev, cur, index) => prev + (index != 0? ',': '') + cur , '');
      //   // 根据flows的ids请求用户信息
      //   return getDistributorByIds(this,{ids})
      // }).then(res => {
      //   patchUserName = patchUserName(this.tableData)(res.distributors); // 第2次局部传参，要拼接的数据
      //   patchUserName('userName')('name');
      // });

      // mainData.then(loseGoodsList => { // 请求商品名
      //   // 拼装ids
      //   let ids = loseGoodsList
      //     .map(lose => lose.itemId)
      //     .reduce((prev, cur, index) => prev + (index != 0? ',': '') + cur , '');
      //   // 根据flows的ids请求用户信息
      //   return getGoodsByIds(this,{ids})
      // }).then(res => {
      //   patchGoodsName = patchGoodsName(this.tableData)(res.goods); // 第2次局部传参，要拼接的数据
      //   patchGoodsName('goodsName')('goodsName');
      // });

    },

    sizeChange(size) { // 分页页数
      this.pageInfo.count = size;
      this.updateMainData();
    },
    
    currentChange(page) { // 分页总数
      this.pageInfo.page = page;
      this.updateMainData();
    },

    onSelectionChange(val){ // 勾选中触发时选中的值
      this.chosenLoses = val;
    },
    
    // 本函数供批量操作(确认、取消等)获取ids
    // chosenOrdersIds(){
    //   const chosenOrders = this.chosenOrders ;
    //   let ids = chosenOrders[0].id;
    //   // 拼凑需要确认的订单ids
    //   for(let i = 1, len = chosenOrders.length; i < len; i++){
    //     ids = ids + ',' + chosenOrders[i].id;
    //   }
    //   return ids;
    // },
   
    // changeCheckedOrder(actionType){ // 子组件翻页行为，类似于iterator
    //   switch(actionType){
    //     case 0:
    //       this.detailEntry = this.detailEntry + 1;
    //       break;
    //     case 1:
    //       this.detailEntry = this.detailEntry - 1;
    //       break;
    //   }
    // },
    /**
     * 高阶函数-修饰器，可return批量操作函数
     * operateOrderBy :: Function -> Function
     * @param  {Function} operateCallback confirmOrder、cancelOrder之类
     * @return {Function}                 批量操作函数
     */
    // operateOrderBy(operateCallback){
    //   return _ => {
    //     // 检查是否有选中的订单
    //     if(!this.chosenOrders[0]){
    //       this.$message({message: '请选择要操作的订单', type:'warning'})
    //       return 0;
    //     }
    //     // 获取ids
    //     const ids = this.chosenOrdersIds();
    //     operateCallback(this, {ids}).then(res => { // 操作数据库中的订单
    //       this.$message({message: res.message, type:'success'})
    //       this.updateMainData();
    //     })
    //     .catch(e => console.log(e))
    //   }
    // },
    
    // ======== 转换 ========
    timeFormatter(row, column, cellValue){ // 时间格式化
      return parseTime(cellValue)
    },
    
    booleanFormatter(row, column, cellValue){
      return booleanFormatter(cellValue)
    },

    booleanProcess(row, column, cellValue){
      return booleanProcess(cellValue)
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .lose-goods-wrap{
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
    }
    .warehouse-list{
      background-color: white;
      height: 100%;
      .function{
        text-align: left;
        padding: 0px 0px 0px 0px;
        overflow: hidden;
        .btn-export{
          background-color: lighten(grey, 40%);
        }
        .search {
          text-align: right;
          overflow: hidden;
          width: 440px;
          .box-search{
            
            width: 140px;
          }
          .btn-search{
            float: right;
            background-color: $lakeBlue;
            color: white;
          }
        }
      }
      .table-cell{
        text-align: center;
      }
    }
  }
</style>
