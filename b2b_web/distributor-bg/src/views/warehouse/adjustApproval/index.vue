<template>
  <transition name="fade" mode="out-in">
    <div class="adjust-approval">
      <header>
        <span>调整审批</span>
      </header>
      <section v-if="!coverSwitch">
        <div class="function">
          <div class="choose-stock">
            <el-button-group>
              <el-button @click="changeCheckType(1)" :type="checkTypeToBtnType(1)" size="mini">我发起的</el-button>
              <el-button @click="changeCheckType(2)" :type="checkTypeToBtnType(2)" size="mini">待我审批</el-button>
              <el-button @click="changeCheckType(3)" :type="checkTypeToBtnType(3)" size="mini">我已审批</el-button>
            </el-button-group>
          </div>

        </div>
        <el-table border :data="adjusts" style="width: 100%" header-row-class-name="header-row">
          <el-table-column align="center" type="index">
          </el-table-column>
          <el-table-column align="center" prop="id" label="申请单号" > </el-table-column>
          <el-table-column align="center" :formatter="formatTime" label="申请时间" > </el-table-column>
          <el-table-column align="center" prop="checkStatus" label="审批状态" > </el-table-column>
          <el-table-column align="center" prop="warehouseId" label="仓库编号" > </el-table-column>
          <el-table-column align="center" prop="warehouseName" label="仓库" > </el-table-column>
          <el-table-column align="center" fixed="right" label="操作" width="150" >
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleCheck(scope.$index, scope.row)">查看</el-button>
              <!-- <el-button size="mini" type="danger" @click="handleAdjust(scope.$index, scope.row, scope)">调整</el-button>
              <el-button size="mini" type="danger" @click="handleReserve(scope.$index, scope.row, scope)">预留</el-button> -->
            </template>
          </el-table-column>

        </el-table>
        <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      </section>
      <section class="pop" v-if="coverSwitch">
        <div class="half-width">
          <el-form ref="form" label-width="180px">
            <el-form-item label="申请单号"> {{checkedItem.id}} </el-form-item>
            <el-form-item label="仓库编号"> {{checkedItem.warehouseId}} </el-form-item>
            <el-form-item label="仓库名称"> {{checkedItemWhName}} </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="form" >
            <el-form-item label="申请时间"> {{timeFormat(checkedItem.createTime)}} </el-form-item>
            <el-form-item label="审批状态"> {{checkedItem.checkStatus}} </el-form-item>
          </el-form>
        </div>
        <el-table :data="checkedItem.details"  style="width:100%;" border header-row-class-name="header-row">
          <el-table-column align="center" type="index" fixed> </el-table-column>
          <el-table-column align="center" prop="goodsId" label="商品编号"> </el-table-column>
          <el-table-column align="center" prop="goodsName" label="商品名称"> </el-table-column>
          <el-table-column align="center" :formatter="formatCategory" label="商品分类"> </el-table-column>
          <el-table-column align="center" :formatter="formatBrand" label="品牌"> </el-table-column>
          <el-table-column align="center" prop="itemId" label="存货编码"> </el-table-column>
          <el-table-column align="center" prop="itemName" label="存货名称"> </el-table-column>
          <el-table-column align="center" prop="numOnWay" label="在途数量"> </el-table-column>
          <el-table-column align="center" prop="numInWarehouse" label="在库数量"> </el-table-column>
          <el-table-column align="center" prop="numAdjust" label="在途调整数量"> </el-table-column>
          <el-table-column align="center" prop="numAdjust" label="在库调整数量"> </el-table-column>
        </el-table>
        <div class="precedure">
          <procedure :flows="flows" :statusFormatter="statusFormatter"></procedure>
        </div>
        <div class="remark">
          <h3>操作</h3>
          <div class="form">
            <el-form label-width="180px">
              <el-form-item label="审批备注">
                <el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="remark" />
              </el-form-item>
            </el-form>
          </div>
          <el-button type="primary" @click="onSubmit(2)">同意</el-button>
          <el-button type="warning" @click="onSubmit(3)">拒绝</el-button>
        </div>
        <el-table :data="flows"  style="width:100%;" border header-row-class-name="header-row">
          <el-table-column align="center" type="index" fixed>
          </el-table-column>
          <el-table-column align="center" prop="checkUserName" label="审批人"> </el-table-column>
          <el-table-column align="center" prop="checkTime" label="审批时间"> </el-table-column>
          <el-table-column align="center" prop="checkStatus" label="审批状态"> </el-table-column>
          <el-table-column align="center" :formatter="row => {return statusFormatter(row.checkStatus)}" label="备注"> </el-table-column>
        </el-table>
        <p class="revert">
          <el-button type="primary" @click="onRevert">返回</el-button>
        </p>
        <div class="esc-hint">
          或按下ESC键返回库存列表
        </div>
      </section>
    </div>
  </transition>
</template>

<script>
import { isvalidUsername } from '@/utils/validate'
import md5 from 'js-md5'
import Cookies from 'js-cookie'
import store from '@/store'
import pagination from '@/components/pagination/index'
import procedure from '@/components/Procedure/index'

import {timeFormat} from '@/utils/timeFormat'
import {getDetailByItemIds, getAdjustChecklist, getAdjustChecklistCount, getAdjustCheckDetail, getStockNumByItemIds, getCategoryList, getBrandList, getAdmins, adjustCheck } from '@/views/warehouse/warehousesData'
import {getGoodsList} from '@/views/goods/goodslist/data/goodslistManage'

import {
  getToken,
  getId
} from '@/utils/auth'
export default {
  name: 'stocklist',
  components: {
    pagination,
    procedure
  } ,
  mounted(){
    window.addEventListener('keydown',this.onEscPress);

    this.getTotal();
    this.getAdjustChecklist();
    this.getWarehousesData();

    getCategoryList(this, {page: 1, count: 10000}).then(categoryList => {
      this.categoryList = categoryList;
    });

    getBrandList(this, {page: 1, count: 10000}).then(brandList => {
      this.brandList = brandList;
    });

  } ,
  methods: {
    
    timeFormat,
    onSubmit(status){ // 检查参数是否合格
      if(status != 2 && status != 3){
        throw new Error('checkStatus参数应为2或3')
      }

      adjustCheck(this, {
        id: this.checkedItem.id,
        checkStatus: status,
        remark: this.remark
      }).then(res => console.log(res)).catch(
        e => console.log(e)
      )
    },
    statusFormatter(statusCode){
      switch(statusCode){
        case 0:
          return '未审批'
        case 1:
          return '审批中'
        case 2:
          return '审批通过'
        case 3:
          return '审批未通过'
        default:
          return '加载中...'
      }
    },
    formatCategory(row, column, cellValue){
      const
        categorys = [],
        ids = row.categoryIds.split(',') ;
      for(let i = 0, len = this.categoryList.length; i < len; i++){
        for(let j = 0, len = ids.length; j < len; j++){
          if(this.categoryList[i].id == ids[j]){
            categorys.push(this.categoryList[i].name)
          }
        }
      }
      if(categorys.length == 0){
        return '未找到对应的种类'
      }else{
        return categorys.toString();
      }
    },
    formatBrand(row, column, cellValue){
      for(let i = 0, len = this.brandList.length; i < len; i++){
        if(row.brandId == this.brandList[i].id){
          return this.brandList[i].title;
        }
      }
      return '未找到对应的品牌'
    },
    formatTime(row, column, cellValue){
      switch(column.label){
        case '申请时间':
          return timeFormat(row.createTime)
        case '预留开始时间':
          return timeFormat(row.startTime)
        case '预留结束时间':
          return timeFormat(row.endTime)
        default:
          return null;
      }
    },
    onRevert(){
      this.coverSwitch = false;
    },
    warehouseBtnClicked(warehouse){
      this.warehouseId = warehouse.id;
      this.getStockList();
    },
    getWarehousesData(){
      return this.$http.warehouseList(this, {
        page: 1, 
        size: 1000,
        openFlag: 1
      }).then(res => {
        if (res.success) {
          this.$data.warehouses = res.data.list
        }
      })
    },
    getAdjustChecklist(){
      const {page, count} = this.pageInfo;
      return getAdjustChecklist(this, {checkType: this.checkType, page, count})
      .then(res => {
        this.adjusts = res.adjusts;
      })
    },

    
    onSizeCHange(val){
      this.pageInfo.count = val;
      this.getAdjustChecklist();
    },
    onCurrentChange(val){
      this.pageInfo.page = val;
      this.getAdjustChecklist();
    },
    getTotal() {
      getAdjustChecklistCount(this, {checkType: this.checkType}).then(res => {
          this.total = res.count;
        })
    },
    handleCheck(index, row){
      this.coverSwitch = true;
      this.checkedItem = row;
    },
    handleAdjust(index, row){
      this.$router.push('stockAdjust');
    },
    handleReserve(index, row){
      this.$router.push('stockReserved');
    },
    onEscPress(event){
      if(event.keyCode == 27){
        this.coverSwitch = false;
      }
    },
    checkTypeToBtnType(checkType){ //checkType是button对应的type:1or2or3
      switch(checkType == this.checkType){
        case true:
          return 'primary';
        case false:
          return null;  
        default:
          return null;
      }
    },
    changeCheckType(checkType){
      this.checkType = checkType;
    }
  },
  computed: {
    checkedItemWhName(){
      const warehouses =  this.warehouses;

      for(let i = 0, len = warehouses.length; i < len; i++ ){
        if(warehouses[i].id == this.checkedItem.warehouseId){
          return warehouses[i].name
        }
      }
      return '请尝试刷新页面'
    },
  },
  data(){
    return {
      remark: '', // 审批备注
      flows: [], // 审批流程
      categoryList: [],
      brandList: [],
      reserveds: [], // 预留审批*2
      adjusts: [],
      checkType: 1,
      stocks: [],
      warehouses: [],
      warehouseId: 1,
      pageInfo: {
        page: 1,
        count: 10
      },
      total: 1,
      coverSwitch: false,
      checkedItem: null,
    }
  },
  watch: {
    checkType(){
      this.getAdjustChecklist();
      this.getTotal();
    },
    // 点击查看时，请求表格需要的数据并拼接
    checkedItem(){
      // 1.首先根据checkedItem的ID发出get审批详情请求，取出审批流程数据
      getAdjustCheckDetail(this, {id: this.checkedItem.id}).then(res => {
        const flows = res.adjust.check.flows; // 初始化checkUserName
        for(let i = 0, len = flows.length; i < len; i++){
          flows[i].checkUserName = '加载中...';
        }
        this.flows = flows;

        // flow.checkUser是用户id，getAdmins将checkUserName拼入flow
        getAdmins(this, this.flows)
        /**
         * 需要拼接的字段
         * @type {Array}
         */
        const
          itemAttrs = ['goodsName', 'categoryIds', 'brandId', 'goodsId', 'itemName', 'boxNum'],
          stockAttrs = ['numLock' ,'numReserved', 'numInWarehouse', 'numOnWay'],
          details = res.adjust.details,
          warehouseId = this.checkedItem.warehouseId;
        // 初始化要拼接的字段，使之参与绑定
        for(let i = 0, len = itemAttrs.length; i < len; i++){
          for(let j = 0, len = details.length; j < len; j++){
            details[j][itemAttrs[i]] = '读取中...';
          }
        }
        for(let i = 0, len = stockAttrs.length; i < len; i++){
          for(let j = 0, len = details.length; j < len; j++){
            if(stockAttrs[i] == 'numReserved'){
              details[j].numWillReserved = details[j].numReserved;
            }else{
              details[j][stockAttrs[i]] = '读取中...';
            }
          }
        }
        // 2.请求表格数据 -> 请求表格中缺漏的数据
        this.checkedItem.details = details;
        const detailsProm = getDetailByItemIds(this, this.checkedItem.details, itemAttrs);

        detailsProm.then(_ => {
          getStockNumByItemIds(this, this.checkedItem.details, stockAttrs)
        })

      });
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .adjust-approval{
    background-color: white;
    height:100%;
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
    .choose-stock{
      width: 100%;
      padding-left: 20px;
    }
    .btn-add{
      float: right;
      padding:5px;
      margin-top:7px;
      margin-right:8px;
    }


    .wraper{
      height: 100%;
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    div.remark{
      text-align: center;
      margin-bottom: 40px;
      div.form{
        margin-right: 200px;
        margin-bottom: 40px;
      }

    }
    .header-row{
      background-color: $table-header-bg;
      th {
        padding: 5px;
        text-align: center;
      }
    }
    td {
      text-align: center;
    }

    .pop{
      padding-top: 50px;
      padding-bottom: 10px;
      background-color: white;
      height: 100%;
      .half-width{
        width: 50%;
        box-sizing: border-box;
        float: left;
      }
      p.revert{
        margin-top: 150px;
        color:grey;
        text-align:center;
      }
      div.esc-hint{
        color:grey;
        text-align:center;
      }
    }
  }
  

</style>
