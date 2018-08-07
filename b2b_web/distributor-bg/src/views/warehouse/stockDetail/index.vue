<template>
  <transition name="fade" mode="out-in">
    <div class="stock-detail">
      <header>
          <span>库存明细列表</span>
      </header>
      <div class="function">
        <el-select v-model="warehouseId" placeholder="-选择仓库-" size="mini">
          <el-option v-for="warehouse in warehouses" :value="warehouse.id" :key="warehouse.id">
          </el-option>
        </el-select>

        <el-select v-model="orderType"  placeholder="-类型-" size="mini">
          <el-option value="" label="无类型">
          </el-option>
          <el-option value="1" label="销售发货">
          </el-option>
          <el-option value="2" label="采购入库">
          </el-option>
          <el-option value="3" label="调入">
          </el-option>
          <el-option value="4" label="调出">
          </el-option>
          <el-option value="5" label="调整">
          </el-option>
        </el-select>
        <!-- <div class="search">
          <el-button class="btn-export">商品编号</el-button>
          <el-input placeholder="请输入内容" size="medium" class="box-search"></el-input>
          <el-button class="btn-search">搜索</el-button>
        </div> -->
      </div>
      <el-table :data="stockDetails" border header-row-class-name="header-row">
        <el-table-column align="center" type="index" fixed>
        </el-table-column>
        <el-table-column align="center" prop="id" label="编号" > </el-table-column>
        <el-table-column align="center" prop="warehouseId" label="仓库" > </el-table-column>
        <el-table-column align="center" prop="orderType" label="业务类型" > </el-table-column>
        <el-table-column align="center" prop="goodsId" label="商品编号" > </el-table-column>
        <el-table-column align="center" prop="goodsName" label="商品名称" > </el-table-column>
        <el-table-column align="center" prop="itemId" label="存货编号" > </el-table-column>
        <el-table-column align="center" prop="itemName" label="存货名称" > </el-table-column>
        <el-table-column align="center" prop="orderNum" label="业务单号" > </el-table-column>
        <el-table-column align="center" prop="boxNum" label="数量" > </el-table-column>
        <el-table-column align="center" prop="createTime" label="创建时间" > </el-table-column>
        <!-- <el-table-column prop="remark" label="备注" > </el-table-column> -->
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </div>
  </transition>
</template>

<script>
import { isvalidUsername } from '@/utils/validate'
import md5 from 'js-md5'
import Cookies from 'js-cookie'
import store from '@/store'
import {timeFormat} from '@/utils/timeFormat'
import pagination from '@/components/pagination/index'
import {getWarehousesData} from '@/views/warehouse/warehousesData'

import {
  getToken,
  getId
} from '@/utils/auth'
export default {
  name: 'stockDetails',
  mounted(){
    this.getWarehousesData()
    // 有了WarehouseId,才能根据仓库请求货物数据
    .then(_ => {
      this.warehouseId = this.warehouses[0].id; //设置默认warehouseId

      this.getItemList()
      .then(stockDetails => {
        if(stockDetails){
          this.getDetailByItemIds(stockDetails);
        }
      });
    })
    .catch(e => console.log(e))
  } ,
  watch: {
    warehouseId(){
      this.getItemList()
      .then(ids => {
        this.getDetailByItemIds(ids);
      });
    },
    orderType(){
      this.getItemList()
      .then(ids => {
        this.getDetailByItemIds(ids);
      });
    }
  },
  components: {
    pagination
  } ,
  methods: {
    getItemList(){
      return this.$api.get(this, 'admin/u/p/warehouse/stock/detail/list', {page: 1, count: 10000, warehouseId: this.warehouseId, orderType: this.orderType})
      .then(data => {
        // 已获取的那部分数据先注入data渲染
        const stockDetails = data.stockDetails;
        // 给暂时未获得的数据设置占位，使之参与绑定
        for(let stockDetail of stockDetails){
          stockDetail.createTime = timeFormat(stockDetail.createTime);
          stockDetail.goodsName = '';
          stockDetail.itemName = '';
          stockDetail.boxNum = ''; 
        }
        this.$data.stockDetails = stockDetails;
        this.getTotal();
        // 把已获得数据传递给getDetailByItemIds
        return this.$data.stockDetails;
      })
    },
    getWarehousesData: function(){
      return getWarehousesData(this)
      .then(data => {
        this.warehouses = data.warehouses;
      })
    },
    /**
     * @param  {array}
     * @return {[type]}
     */
    getDetailByItemIds(stockDetails){
      // 若仓库中没有货物则立即停止
      if(stockDetails.length === 0){
        return
      }

      // 组装param
      // idToItemObj为id到data中item的map
      const 
        ids = [],
        idToItemObj = {};

      for(let stockDetail of stockDetails){
        const id = stockDetail.itemId;
        ids.push(id);
        idToItemObj[id] = stockDetail;
      }
      const param = ids.join(',');
      
      this.$api.get(this, 'admin/u/po/goods/item/ids', {ids: param})
      .then(data => {
        console.log(data);
        const items = data.items;
        for(let item of items){
          // 获取itemId -> 获取itemId映射的item对象引用
          const
            itemId = item.item.id,
            itemObj = idToItemObj[itemId] ;
          // Vue.set(itemObj, 'goodsName', item.goods.goodsName);
          itemObj.goodsName = item.goods.goodsName;
          itemObj.itemName = item.item.itemName;
          itemObj.boxNum = item.item.boxNum;
        }
      } )
      .catch(e => console.log(e))

    },
    getTotal() {
      this.$api.get(this, 'admin/u/p/warehouse/stock/detail/count', {warehouseId: this.warehouseId})
      .then(res => {this.total = res.count; }
      )
    },
    onSizeCHange(val){
      this.pageInfo.count = val;
      this.getWarehousesData();
    },
    onCurrentChange(val){
      this.pageInfo.page = val;
      this.getWarehousesData();
    },
  },
  data(){
    return {
      stockDetails: [],
      warehouses: [],
      warehouseId: null,
      pageInfo: {
        page: 1,
        count: 10
      },
      total: 1,
      orderType: '',
    }
  },
  computed: {

  }
}
</script>

<style rel="stylesheet/scss" lang="scss">

  .stock-detail{

    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
    }
    .function{
      padding: 16px;
      background-color: white;
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
    .btn-add{
      float: right;
      padding:5px;
      margin-top:7px;
      margin-right:8px;
    }
    .wraper{
      height: 100%;
      /*background-color: blue;*/
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
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
    
  }

</style>
