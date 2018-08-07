<template>
  <transition name="fade" mode="out-in">
    <div class="stock-adjust">
      <header>
        <span>库存调拨</span>
        <router-link :to="{ path: '/warehouse/stockAllocationList'}">
          <el-button class="btn-add">商品调拨列表</el-button>
        </router-link>
      </header>
      <el-form ref="form" :rules="rules" :model="form" label-width="180px" class="el-form">
        <el-form-item label="调出仓库" prop="warehouseInId">
          <el-select v-model="warehouseInId" placeholder="请选择">
            <el-option v-for="warehouse in warehouses" :key="warehouse.id" :label="warehouse.name" :value="warehouse.id" >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="调入仓库" prop="warehouseOutId">
          <el-select v-model="warehouseOutId" placeholder="请选择">
            <el-option v-for="warehouse in warehouses" :key="warehouse.id" :label="warehouse.name" :value="warehouse.id" >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button @click="toggleAddPage" type="info" round :disabled="addDisabled">{{addBtnWord}}</el-button>
        </el-form-item>
      </el-form>
      <el-table border :data="addedStocks" style="width: 100%">
          <el-table-column align="center" prop="goodsId" label="商品编号"></el-table-column>
          <el-table-column align="center" prop="goodsName" label="商品名称"></el-table-column>
          <el-table-column align="center" prop="itemCode" label="存货编号"></el-table-column>
          <el-table-column align="center" prop="itemName" label="存货名称"></el-table-column>
          <el-table-column align="center" prop="numInWarehouse" label="在库数量"></el-table-column>
          <el-table-column align="center" prop="numLock" label="在库锁定数量"></el-table-column>
          <el-table-column align="center" prop="numReserved" label="已预留数量"></el-table-column>
          <el-table-column  align="center" :formatter="canBeAllocated" label="在库可调数量"></el-table-column>
          <el-table-column align="center" label="调拨数量">
            <template slot-scope="scope">
              <el-input size="mini" @change="checkReserveNum(scope.row)" v-model="scope.row.addAllocate"></el-input>
            </template>
          </el-table-column>
      </el-table>

      <div style="text-align:center;margin-top:30px">
        <el-button @click="checkForm('form')" type="primary">确定</el-button>
        <el-button @click="onRevert">返回</el-button>
      </div>
      <section class="pop" v-if="coverSwitch">
        <el-table border :data="stocks" max-height="600" >
          <el-table-column align="center" prop="goodsId" label="商品编号"></el-table-column>
          <el-table-column align="center" prop="goodsName" label="商品名称"></el-table-column>
          <el-table-column align="center" prop="itemId" label="货品编号"></el-table-column>
          <el-table-column align="center" prop="itemName" label="货品名称"></el-table-column>
          <el-table-column align="center" label="操作" width="100">
            <template slot-scope="scope">
              <el-button @click="addStock(scope.row)" type="primary">添加</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align:center;margin-top:30px">
          <el-button @click="onAddConfirm">确定</el-button>
          <el-button @click="toggleAddPage">返回</el-button>
        </div>
      </section>
    </div>
  </transition>
</template>

<script>
  import {dateToNum} from '@/views/warehouse/warehousesUtils'
  import {postReserveData, addAllocate} from '@/views/warehouse/warehousesData'

  export default {
    name: 'stocklist',
    mounted(){
      window.onkeydown = this.onEscPress;
      // get仓库数据
      this.getWarehouseList()
      // 有了仓库数据->有了默认选中仓库->get默认仓库的商品数据
      .then(_ => {
        this.$refs.form.validateField('warehouseOutId');
        this.getStocksByWHid();
      })

    } ,
    methods: {
      dateToNum,
      addAllocate,
      canBeAllocated(row, column, cellValue){
        return row.numInWarehouse - row.numLock - row.numReserved;
      },
      checkReserveNum(row){
        const {numInWarehouse, numLock, numReserved} = row;
        if(row.addAllocate > numInWarehouse - numLock - numReserved){
          row.addAllocate = 0;
          this.$message({message: '调拨数过大', type:'warning'})
        }else{
          this.dataCouldbeSend = true;
        }
      },
      checkTimes(){
        const
          startTime = this.dateToNum(this.startTime),
          endTime = this.dateToNum(this.endTime),
          now = this.dateToNum(new Date());
        if(startTime < now){
          this.$message({message: '请选择未来的时间', type:'warning'});
          this.startTime = new Date();
        }else{
          if(startTime > endTime){
            this.$message({message: '终点时间必须在起点时间之后', type:'warning'})
            this.endTime = this.startTime;
          }
        }
      },
      calculateAdjust(row, column, cellValue){
        return row.numInWarehouse - row.numLock - row.numReserved;
      },
      getWarehouseList(){
        return this.$http.warehouseList(this, {
          page: 1, 
          size: 1000,
          openFlag: 1
        }).then(res => {
          if (res.success) {
             this.warehouses = data.data.list;
            this.warehouseInId = this.warehouses[0].id;
            this.warehouseOutId = this.warehouses[1].id;
          }
        })
      },
      getStocksByWHid(){
        this.$api.get(this, 'admin/u/p/warehouse/stock/item/list', {warehouseId: this.$data.warehouseInId})
        .then(data => {
          // 初始化预留数量
          for(let stock of data.stocks){
            stock.allocateCount = 0;
          }
          this.$data.stocks = data.stocks;
        })
        .catch(e => console.log(e))
      },
      onRevert(){
        this.addedStocks = [];
      },
      toggleAddPage(){
        this.coverSwitch = !this.coverSwitch;
      },
      onAddConfirm(){
        this.toggleAddPage();
      },
      // 按下esc,关闭弹窗
      onEscPress(event){
        if(event.keyCode == 27){
          this.coverSwitch = false;
        }
      },
      /**
       * 添加仓库中的商品到面板中
       * @param {obj} row 一类商品
       */
      addStock(row){
        const addedStocks = this.addedStocks;
        let shouldBeAdded = true;
        // 为防止重复添加同一个商品，遍历已添加的，如果发现重复则不添加该商品
        for(let stock of addedStocks){
          if(row == stock){
            shouldBeAdded = false;
          }
        }
        if(shouldBeAdded){
          row.adjust = 0;
          this.addedStocks.push(row);
        }
      },
      checkForm(formName){
        
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.postAllocateData();
          } else {
            return false;
          }
        });
      },
      postAllocateData(){
        this.$confirm('即将发出调拨请求，是否继续？','提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'waring',
          center: true
        }).then(_ => {
          const
            params = {},
            allocateDetails = [],
            addedStocks = this.addedStocks;
          // 从本地采集调整需要的数据，写入params
          params.warehouseInId = this.warehouseInId;
          params.warehouseOutId = this.warehouseOutId;
          for(let i = 0, len = addedStocks.length; i < len; i++){
            allocateDetails.push({
              goodsId: addedStocks[i].goodsId,
              itemId: addedStocks[i].itemId,
              allocateCount: addedStocks[i].allocateCount,
              warehouseId: addedStocks[i].warehouseId,
            })
          }
          params.allocateDetails = allocateDetails;
          // 将采集好的数据发出
          this.addAllocate(this, params)
          .then(res => {
              this.$message({
                message: '预留成功',
                type: 'success',
                duration: 3 * 1000,
              })
            })
          // 捕获error
          .catch(e => {
            this.$message({
              type: 'info',
              message: '已取消预留'
            })
          })
        })
      }
    },
    data(){
      const checkInId = (rule, value, callback) => {
        this.$refs.form.validateField('warehouseOutId');
        callback()
      }
      const checkBothWh = (rule, value, callback) => {
        if(this.warehouseInId === this.warehouseOutId){
          return callback(new Error('调入、调出仓库是不同的两个仓库'));
        }
        let
          isPlatformIn = '',
          isPlatformOut = '',
          warehouses = this.warehouses;
        // 查询出入两个仓库的isPlatform
        for(let i = 0, len = warehouses.length; i < len; i++ ){
          if(warehouses[i].id == this.warehouseInId){
            isPlatformIn = warehouses[i].isPlatform;
          }else if(warehouses[i].id == this.warehouseOutId){
            isPlatformOut = warehouses[i].isPlatform;
          }
        }
        // 得出是否有平台仓 isPlatformIn || isPlatformOut
        if (!(isPlatformIn || isPlatformOut)) {
          return callback(new Error('调入、调出仓库中至少有一个平台仓'));
        }else{
          callback()
        }
      }
      return {
        form: {},
        rules: {
          warehouseInId: [
            { validator: checkInId, trigger: 'change' }
          ],
          warehouseOutId: [
            { validator: checkBothWh, trigger: 'change' }
          ],
        },
        stocks: [],
        addedStocks: [],
        warehouses: [],
        warehouseInId: undefined,
        warehouseOutId: undefined,
        coverSwitch: false,
        dataCouldbeSend: true,
      }
    },
    computed: {
      addDisabled(){
        return !this.stocks[0]
      },
      addBtnWord(){
        if(this.addDisabled){
          return '无可选择商品'
        }else{
          return '选择商品'
        }
      },
      getWarehouseName(){
        const warehouses = this.warehouses;
        for(let i = 0,len = warehouses.length; i < len ; i++){
          if(warehouses[i].id === this.warehouseInId){
            return warehouses[i].name
          }
        }
      }
    },
    watch: {
      warehouseInId: function(){
        this.getStocksByWHid();
      },
      coverSwitch(){
        console.log(`coverSwitch = ${this.coverSwitch}!`);
      },
      startTime(){
        this.checkTimes()
      },
      endTime(){
        this.checkTimes()
      },
    }

  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  $lineHight: 40px;
  $lakeBlue: rgb(33,184,203);
  .stock-adjust{
    background-color: white;
    padding-bottom: 100px;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
    }
    .el-form{
        width: 700px;
        margin-top: 30px;
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
    // 弹出添加商品窗口
    section.pop{
      // text-align: center;
      padding: 100px;
      position: fixed;
      background-color: rgba(black, .5);
      top:0;
      bottom:0;
      left:0;
      right:0;
      z-index: 8;
      table{
        width: 90%;
        margin-left: auto;
        margin-right: auto;
        maring-top:100px;
      }
    }
    table{
      width: 100%;
      border: 1px solid $tableColor;
      border-radius: 5px;
      color: darken($tableColor,30%);
      border-collapse: separate;
      th{
        border-bottom: 1px solid $tableColor;
        padding-top: 8px;
        padding-bottom: 8px;
      }
      td{
        border-top: 1px solid $tableColor;
        padding-top: 8px;
        padding-bottom: 8px;
        text-align: center;
      }

    }
  }

</style>
