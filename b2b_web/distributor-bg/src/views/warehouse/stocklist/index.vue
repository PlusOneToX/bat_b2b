<template>
    <div class="stock-list">
      <header v-if="append">
        <h4>商品库存列表</h4>
      </header>
      <header v-if="looking">
        <h4>商品库存列表</h4>
         <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="onRevert">
            返回商品库存列表
          </el-button>
      </header>

        <div class="search">
          <div class="search-right">
            <el-input v-model.trim="searchContent" clearable @change="contentChange" @keyup.enter.native="onSearch()" placeholder="存货编码/存货名称" size="mini" class="box-search" ></el-input>
            <button class="mini-search-btn box-btn" @click="onSearch()" > 搜索 </button>
          </div>
          <el-button :loading="syncStockLoading" class="mini-search-btn"  @click="syncStock()">同步库存</el-button>
        </div>

        <el-button-group class="nva-bar-btn-gro">
          <el-button size="mini" :key="warehouse.id" @click="warehouseBtnClicked(warehouse)" v-for="warehouse in warehouses" :type="isCurrentWarehouse(warehouse)">{{warehouse.name}}</el-button>
        </el-button-group>

      <el-table border :data="stocks" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" prop="itemCode" label="存货编码" :min-width="100"> </el-table-column>
        <el-table-column align="center" prop="itemName" label="存货名称" :min-width="100" show-overflow-tooltip> </el-table-column>
        <el-table-column align="center" prop="numInWarehouse" label="ERP预计可发量" :min-width="90"> </el-table-column>
        <el-table-column align="center" prop="numOnWay" label="在途数量" :min-width="80"> </el-table-column>
        <el-table-column align="center" prop="numVmi" label="VMI库存数量" :min-width="90"> </el-table-column>
        <el-table-column align="center" label="在库可下单量" :render-header="renderHeader" :min-width="120">
          <template slot-scope="scope">
            <div>
              <span style="font-size: 14px;" >{{scope.row.inStockUsableCount}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="在途在库可下单量" :render-header="renderHeader" :min-width="120">
          <template slot-scope="scope">
            <div>
              <span style="font-size: 14px;" >{{scope.row.onWayUsableCount}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="numReserved" label="预留数量" :render-header="renderHeader" :min-width="100">
          <template slot-scope="scope">
            <div>
              <span style="font-size: 14px;" >{{scope.row.numReserved}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="numLock" label="B2B锁定数量" :render-header="renderHeader" :min-width="100">
          <template slot-scope="scope">
            <div>
              <el-button size="mini" type="text" style="width:100px" v-if="scope.row.numLock !== 0" @click="showLockOrders(scope.row.itemId)">
                {{scope.row.numLock}}
              </el-button>
              <span v-else style="font-size: 14px;" >{{scope.row.numLock}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="240" align="center">
          <template slot-scope="scope">
            <el-button class="mini-search-btn"  @click="handleSync(scope.row)">同步库存</el-button>
            <el-button class="mini-search-btn"  @click="handleSet(scope.row)">校正锁库数量</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>

      <!-- 锁定订单列表 -->
      <el-dialog title="锁定数量清单" :visible="lockordersShow" width="70%" :before-close="closeOrders">
        <el-table show-summary :summary-method="getSummaries" border :data="lockOrders" max-height="600"  style="width: 100%" header-row-class-name="header-row" class="lockTable" v-loading="lockOrdersLoading">
          <el-table-column align="center" prop="orderNo" label="订单号">
            <template slot-scope="scope">
              <div>
                <el-button size="mini" type="text" style="width:100px" v-if="scope.row.numLock !== 0 && scope.row.orderNo" @click="goOrder(scope.row)">
                  {{scope.row.orderNo}}
                </el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="orderErpNo" label="ERP订单号" > </el-table-column>
          <el-table-column align="center" prop="orderStatus" label="订单状态" :formatter="orderStatusFormatter"> </el-table-column>
          <el-table-column align="center" prop="inStockLockNum" label="在库锁定数量" > </el-table-column>
          <el-table-column align="center" prop="onWayLockNum" label="在途锁定数量" > </el-table-column>
          <el-table-column align="center" prop="vmiLockNum" label="VMI锁定数量" > </el-table-column>
          <!-- <el-table-column align="center" prop="lockNum" label="锁定数量" >
            <template slot-scope="scope">
              <input type="number" min="1" v-if="scope.row.isEdit" class="input-num" v-model='scope.row.lockNum' 
              @keyup="if(scope.row.lockNum.length==1){scope.row.lockNum=scope.row.lockNum.replace(/[^0-9]/g,'')}else{scope.row.lockNum=scope.row.lockNum.replace(/\D/g,'')}" />
              <span v-else>{{scope.row.lockNum}}</span>
          </template>
          </el-table-column> -->
          <el-table-column align="center" prop="createTime" label="锁定时间" sortable :formatter="timeFormat"> </el-table-column>
          <!-- <el-table-column label="操作" :width="240" align="center">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleSave(scope.$index)" style="background-color:#21b8cb" v-if="scope.row.isEdit">保存</el-button>
              <button class="mini-search-btn" @click="handleEdit(lockOrders[scope.$index])" v-else >编辑</button>
              <el-button  size="mini" @click="handleCancel(scope.$index)" v-if="scope.row.isEdit">取消</el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-dialog>
    </div>
</template>

<script>
import pagination from '@/components/pagination/index'
import { timeFormat } from "@/utils/timeFormat"
import {orderStatusFormatter} from '@/views/order/orderUtils' // 格式化引入
export default {
  name: 'stocklist',
  components: {
    pagination
  },
  activated(){
    if(this.goOrdersShow){
      this.goOrdersShow = false
      this.lockordersShow = true
    }
    this.getWarehousesData().then(res => {
      this.warehouseId = this.warehouses[0].id
      this.warehouseNO = this.warehouses[0].no
    }).then(_ => {
      this.getStockList();
    })
    window.addEventListener('keydown',this.keyboard)
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  130); // 计算表高度，固定表头
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
    isSuperAdmin() { /** 是否超级管理员 */
      return this.$store.getters.userinfo.adminType === 1
    },
  },
  data(){
    return {
      tableHeight: 600,
      loading: false,
      syncStockLoading:false,
      syncResttLoading:false,
      append: true,
      looking: false,
      stocks: [],
      warehouses: [],
      warehouseId: 1,
      warehouseNO: '',
      pageInfo: {
        page: 1,
        size: 10
      },
      total: 1,
      searchContent: '',
      checkedItem: null,
      lockOrders:[],
      lockOrdersLoading:false,
      lockordersShow:false,
      goOrdersShow:false,
      itemId: 0 // B2B锁定数量ID
    }
  },
  methods: {
    // 校正锁库数量
    handleSet (row) {
      this.$http.resetLockStock(this, {itemId: row.itemId}).then(res => {
        if (res.success) {
          this.$message.success('校正锁库数量成功！')
        } else {
          this.$message.error(res.errMessage)
        }
      }).catch(error => {
        this.$message.error(error)
      })
    },
    // 同步库存
    handleSync (row) {
      this.$http.syncStockByItemErpId(this, {itemErpId: row.itemErpId}).then(res => {
        if (res.success) {
          this.$message.success('同步库存成功！')
        } else {
          this.$message.error(res.errMessage)
        }
      }).catch(error => {
        this.$message.error(error)
      })
    },
    handleSave(index){
        if (this.lockOrders[index].lockNum.replace(/(^s*)|(s*$)/g, "").length === 0){
          this.$message.error('请先输入锁定数量！');
        }else{
          const params = {
            orderGoodsStockId: this.lockOrders[index].id,
            correctNum: parseInt(this.lockOrders[index].lockNum)
          }
          // 暂无此功能
          // this.$api.post(this, 'admin/u/po/warehouse/stock/item/lock/orders/correct', params).then(res => {
          //    if(res.code === 0){
          //     this.$message.success('保存成功！');
          //     this.getLockOrders();
          //   }
          // })
        }
    },
    handleEdit (obj) {
      let editLen = 0;
      this.lockOrders.forEach(item => {
        if (item.isEdit) {
          editLen++;
        }
      })
      if (editLen <= 0) {
        obj.isEdit = true
      } else {
        this.$message.error("请先保存编辑中的锁定数量！");
      }
    },
    handleCancel (index) {
      this.lockOrders[index].isEdit = false;
      if(this.lockOrders[index].id == undefined){
        this.lockOrders.splice(index,1);
      }else{
        this.getLockOrders();
      }
    },
    getLockOrders () {
        // lockType 1- 在库
       this.$http.orderGoodsStockList(this, {lockType:1, warehouseId:this.warehouseId,itemId:this.itemId}).then(res => {  
        if(res.success){
           this.lockOrders = []
          if (res.data && res.data.length >0) {
            res.data.forEach(item => {
              this.lockOrders.push({
                id: item.id,
                lockNum: item.lockNum,
                createTime: item.createTime,
                orderId: item.orderId,
                orderNo: item.orderNo,
                orderErpNo: item.orderErpNo,
                inStockLockNum: item.inStockLockNum,
                onWayLockNum: item.onWayLockNum,
                vmiLockNum: item.vmiLockNum,
                orderStatus: item.orderStatus,
                isEdit: false
              })
            })
          }
        }
      })
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.onSearch()
      }
    },
    getSummaries(param){
      const { columns, data } = param;
      const sums = []
      columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计';
            return;
          }
          if(index === 3 || index === 4 || index === 5){
            const values = data.map(item => Number(item[column.property]));
            if (!values.every(value => isNaN(value))) {
              sums[index] = values.reduce((prev, curr) => {
                const value = Number(curr);
                if (!isNaN(value)) {
                  return prev + curr;
                } else {
                  return prev;
                }
              }, 0);
              sums[index] += '';
            }
          }
        });
        return sums;
    },
    orderStatusFormatter(row, col, val){ // 订单状态
      return orderStatusFormatter(row.orderStatus);
    },
    goOrder(row){ // 弹窗订单号查看
      this.lockordersShow = false
      this.goOrdersShow = true

      this.$http.getOrderStatus(this, {id: row.orderId}).then(res => {  
        if (res.success) {
          if (res.data.erpOrderFlag) {
            // 订单详情
            this.$router.push({name: 'orderDetail',query: { orderId: row.orderId, stockG: true, type:1 }})
          } else if (res.data.distributorOrderFlag) {
            // 分销订单详情
            this.$router.push({name: 'orderDetail',query: { orderId: row.orderId, orderDistrG: true, type:2 }})
          } else if (res.data.customerOrderFlag) {
            // 柔性订单详情
            this.$router.push({name: 'orderDetail',query: { orderId: row.orderId, orderCustG: true, type:3 }})
          }
        } else {
          this.$message.error(res.errMessage)
        }
      });
    },
    closeOrders(){
      this.lockordersShow = false
    },
    // 时间戳转换
    timeFormat(row, col, val) {
      if(val === 0){
        return '-'
      }else{
        return timeFormat(val)
      }
    },
    // 显示锁定订单列表
    showLockOrders(itemId){
      this.itemId = itemId
      this.lockOrdersLoading = true
      this.lockordersShow = true
      this.lockOrders = []
      this.getLockOrders()
      this.lockOrdersLoading = false
    },
    // ======== 操作 ========
    keyboard(ev) { // 搜索键盘事件
      if(ev.keyCode == 13){
        this.getStockList()
      }
    },

    syncStock() { // 商品库存erp同步
      this.syncStockLoading = true
      this.$http.syncStock(this).then(res => {  
        this.syncStockLoading = false
        if(res.success) {
          this.$message.success({
            message: "同步成功，请稍后刷新页面查看",
            duration: 3 * 1000,
            onClose: () => { }
          });
          // this.dataForm();
        }
      })
    },

    onSearch(){ // 搜索操作
    this.pageInfo.page = 1;
      this.getStockList()
    },

    onRevert(){ // 返回操作
      this.append = true;
      this.looking = false;
    },

    warehouseBtnClicked(warehouse){ // 仓库列表请求
      this.warehouseId = warehouse.id;
      this.warehouseNO = warehouse.no;
      this.getWarehousesData();
      this.getStockList();
    },

    handleAdjust(index, row){ // 调整操作
      this.$router.push({ name: 'stockAdjust',query:{states: 1}});
    },

    // ======== 数据 ========
    getWarehousesData(){ // 仓库数据
      this.loading = true
      return this.$http.warehouseList(this, { page:1, size:10, openFlag:1 }).then(res => {
        if (res.success) {
          this.$data.warehouses = res.data.list;
          this.loading = false
        } else {
          this.loading = false
        }
      })
    },

    getStockList(){ // 库存列表数据
      let {page, size} = this.pageInfo
      this.$http.warehouseStockList(this, { page, size, content: this.searchContent, warehouseId: this.warehouseId }).then(res => {
        if (res.success) {
          this.stocks = res.data.list
          this.total = res.data.total
        }
      })
    },

    renderHeader(h, col) { // 页面右上角的规格值
      switch(col.$index) {
        case 5:
          return h("div", [h("span","在库可下单量"),
          h("el-tooltip",{props:{content:"B2B在库可下单量=ERP预计可发量+VMI库存数量-B2B锁定数量-预留数量（计算结果小于零取零）",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
          break;
        case 6:
          return h("div", [h("span","在途在库可下单量"),
          h("el-tooltip",{props:{content:"B2B在途可下单量=在途数量-(B2B锁定数量-ERP预计可发量-VMI库存数量)-预留数量（计算结果大于在途数量取在途数量）",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
          break;
        case 7:
          return h("div", [h("span","预留数量"),
          h("el-tooltip",{props:{content:"预留数量包含手工预留和收单系统预留",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
          break;
        case 8:
          return h("div", [h("span","B2B锁定数量"),
          h("el-tooltip",{props:{content:"锁定数量为未同步到ERP和已同步ERP且未审核订单的数量",effect:"light",placement:"top"}},[h("span",{"class":"el-icon-question question-color"})])])
          break;
      }
    },

    onSizeCHange(val){  // 分页
      this.pageInfo.size = val;
      this.getStockList();
    },
    onCurrentChange(val){ // 分页
      this.pageInfo.page = val;
      this.getStockList();
    },
    
    isCurrentWarehouse(warehouse){
      if(warehouse.id == this.warehouseId){
        return 'primary';
      }else{
        return null;
      }
    },
    
    handleReserve(index, row){ // 预留调整
      this.$router.push('stockReserved');
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .stock-list{
    background-color: white;
	  min-height: 100%;
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
      .search{
        width: 100%;
        border-bottom: 1px solid #dcdcdc;
        padding: 10px;
        overflow: hidden;
        .search-right {
          float: right;
          overflow: hidden;
          .box-search{
            width: 215px;
            // float: right;
          }
          .box-btn {
            float: right;
            margin-left:5px;
            margin-right:10px;
          }
        }
      }
      .nva-bar-btn-gro {
        margin-top: 10px;
        margin-left: 10px;
        margin-bottom: 10px;
      }
    .pop{
      padding-top: 50px;
      padding-bottom: 10px;
      background-color: white;
      height: 100%;
      
      p{
        margin-top: 150px;
        color:grey;
        text-align:center;
      }
      div.esc-hint{
        color:grey;
        text-align:center;
      }
    }
    .input-num{
      height: 30px;
      font-size: 14px;
      border-radius: 5px;
      border: 1px solid #ccc;
      font-weight: bold;
      width: 100%;
      text-align: center;
    }
  }
</style>