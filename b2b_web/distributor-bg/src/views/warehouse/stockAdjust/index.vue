<template>
  <transition name="fade" mode="out-in">
    <div class="stock-adjust">
      <header>
          <h4>库存调整</h4>
          <el-button v-if="onBack == 1" class="btn-home" icon="el-icon-d-arrow-left" @click="onClose">
            商品库存列表
          </el-button>
        </header>

      <el-form ref="form" :model="form" label-width="180px" class="el-form">
        <el-form-item label="仓库编号">
          <el-select v-model="warehouseId" placeholder="请选择" value="1">
            <el-option v-for="warehouse in warehouses" :key="warehouse.id" :label="warehouse.id" :value="warehouse.id" > </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="仓库名称">
          {{getWarehouseName}}
        </el-form-item>
        <el-form-item>
          <el-button @click="toggleAddPage" type="info" round :disabled="addDisabled">{{addBtnWord}}</el-button>
        </el-form-item>
      </el-form>
      <el-table border :data="addedStocks" style="width: 100%">
          <el-table-column align="center" prop="goodsNo" label="商品编号"></el-table-column>
          <el-table-column align="center" prop="goodsName" label="商品名称"></el-table-column>
          <el-table-column align="center" prop="itemCode" label="存货编码"></el-table-column>
          <el-table-column align="center" prop="itemName" label="存货名称"></el-table-column>
          <el-table-column align="center" prop="numInWarehouse" label="在库数量"></el-table-column>
          <el-table-column align="center" prop="numLock" label="已锁定数量"></el-table-column>
          <el-table-column align="center" prop="numReserved" label="已预留数量"></el-table-column>
          <!-- <el-table-column :formatter="calculateAdjust" label="可调整数量"></el-table-column> -->
          <el-table-column align="center" label="调整后数量">
            <template slot-scope="scope">
              <el-input size="mini" type="number" min="0" @change="checkAdjustNum(scope.row)" v-model.trim="scope.row.adjust"></el-input>
            </template>
          </el-table-column>
      </el-table>

      <div style="text-align:center;margin-top:30px">
        <el-button @click="adjustmentReq" type="primary">确定</el-button>
        <!-- <el-button @click="onRevert">返回</el-button> -->
      </div>
      <section class="pop" v-if="coverSwitch">
        <el-table border :data="stocks" max-height="600" >
          <el-table-column align="center" prop="goodsNo" label="商品编号"></el-table-column>
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

  export default {
    name: 'stocklist',
    data(){
      return {
        form: {},
        stocks: [],
        addedStocks: [],
        warehouses: [],
        warehouseId: NaN,
        coverSwitch: false,
        adjustCouldbeSend: true,
      }
    },
    computed: {
      // 返回操作按钮判断
      onBack() {
        return this.$route.query.states
      },
      addDisabled(){
        return !this.stocks[0]
      },
      addBtnWord(){
        if(this.addDisabled){
          return '无可添加商品'
        }else{
          return '添加商品'
        }
      },
      getWarehouseName(){
        const warehouses = this.warehouses;
        for(let i = 0,len = warehouses.length; i < len ; i++){
          if(warehouses[i].id === this.warehouseId){
            return warehouses[i].name
          }
        }
      }
    },
    mounted(){
      window.addEventListener('keydown',this.onEscPress )

      // get仓库数据
      this.getWarehouseList()
      // 有了仓库数据->有了默认选中仓库->get默认仓库的商品数据
      .then(_ => {
        this.getStocksByWHid();
      })
      

    } ,
    methods: {
      // 返回商品库存列表操作
      onClose() {
        this.$router.push({ name: 'stockList'});
      },
      checkAdjustNum(row){
        // const {numInWarehouse, numLock, numReserved} = row;
        // if(row.adjust <= 0){
        //   row.adjust = 1;
        //   this.$message({message: '调整数过小', type:'warning'})
        // }else{
        //   this.adjustCouldbeSend = true;
        // }
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
            this.$data.warehouses = res.data.list
            this.warehouseId = this.$data.warehouses[0].id;
          }
        })
      },
      getStocksByWHid(){
        return this.$api.get(this, 'admin/u/p/warehouse/stock/item/list', {warehouseId: this.$data.warehouseId}).then(data => {
          for(let stock of data.stocks){
            stock.adjust = 0;
          }
          this.stocks = data.stocks;
        })
      },
      onRevert(){
        this.addedStocks = [];
      },
      toggleAddPage(){ // 返回操作
        this.coverSwitch = !this.coverSwitch;
      },
      onAddConfirm(){ // 确定操作
        this.toggleAddPage();
      },
      onEscPress(event){ // 按下esc,关闭弹窗
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
      adjustmentReq(){
        this.$confirm('即将发出调整请求，是否继续？','提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'waring',
          center: true
        }).then(_ => {
          const
            self = this,
            adjusts = [],
            params = {};
          // 从本地采集调整需要的数据
          params.warehouseId = self.$data.warehouseId;
          params.adjusts = adjusts;
          for(let stock of self.addedStocks){
            adjusts.push({
              goodsId: stock.goodsId,
              itemId: stock.itemId,
              type: stock.adjust > 0 ? 1 : 2,
              count: Math.abs(stock.adjust),
            })
          }
          // 将采集好的数据发出
          this.$api.post(this, 'admin/u/p/warehouse/stock/adjust', params).then(res => {
              this.$message({
                message: '调整成功',
                type: 'success',
                duration: 3 * 1000,
              })
              this.getStocksByWHid().then(_ => {
                this.addedStocks = this.addedStocks
                  .map(obj => obj.id)
                  .map(id => this.stocks.filter(obj => obj.id == id)[0])
              })
            }).catch(e => {
            this.$message({
              type: 'info',
              message: '已取消调整'
            })
          })
        })
      }
    },
    watch: {
      warehouseId: function(){
        this.getStocksByWHid();
        this.addedStocks = [];
      },
      coverSwitch(){
        console.log(`coverSwitch = ${this.coverSwitch}!`);
      }
    }

  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  $lineHight: 40px;
  $lakeBlue: rgb(33,184,203);
  .stock-adjust{
    background-color: white;
    padding-bottom: 100px;
    header {
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
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
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
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    // 弹出添加商品窗口
    section.pop{
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
        margin-top:100px;
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
