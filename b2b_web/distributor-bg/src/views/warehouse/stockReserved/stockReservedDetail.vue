<template>
    <div class="stock-adjust">
      <header>
          <span>库存预留</span>
          <el-button @click="backStep()" class="mini-add-btn btn-add">返回</el-button>
      </header>
      <el-form ref="form" :model="formData" label-width="150px" class="el-form">
        <el-form-item label="仓库">
          <el-select :disabled="!addDisabled" size="mini" v-model="formData.warehouseId" placeholder="请选择">
            <el-option v-for="warehouse in warehouselist" :key="warehouse.id" :label="warehouse.name" :value="warehouse.id" >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="预留状态" v-if="!addDisabled">
          {{formData.status === 0?"预留中":"已释放"}}
        </el-form-item>
        <el-form-item label="备注">
          <el-input :disabled="!addDisabled" type="textarea" :rows="3" style="width:50%" placeholder="请输入内容" v-model="formData.remark" />
        </el-form-item>
        <el-form-item v-if="addDisabled" >
          <el-button @click="selectGoodsItems" class="mini-search-btn box-btn">选择商品</el-button>
        </el-form-item>
        <el-form-item>
          <template>
            <el-table border header-row-class-name="header-row" :data="formData.detailCmdList" style="width: 100%">
                <el-table-column align="center" prop="itemCode" label="存货编码"></el-table-column>
                <el-table-column align="center" prop="itemName" label="存货名称"></el-table-column>
                <el-table-column v-if="addDisabled" align="center" prop="numInWarehouseSum" label="在库数量" width="120"></el-table-column>
                <el-table-column v-if="addDisabled" align="center" prop="numLockSum" label="锁定数量" width="120"></el-table-column>
                <el-table-column v-if="addDisabled" align="center" prop="reserved" label="已预留数量" width="120"></el-table-column>
                <el-table-column v-if="addDisabled" align="center" prop="ableReserved" label="可预留数量" width="120"></el-table-column>
                <el-table-column v-if="!addDisabled" align="center" prop="numReservedSum" label="预留数量" width="120"></el-table-column>
                <el-table-column v-if="addDisabled" align="center" label="预留数量" width="120">
                  <template slot-scope="scope">
                    <el-input size="mini" :disabled="!addDisabled" type="number" step="1" min="0" @input="checkReserveNum(scope.row)" v-model="scope.row.numReserved" />
                  </template>
                </el-table-column>
                <el-table-column v-if="addDisabled" align="center" label="操作" width="80">
                  <template slot-scope="scope">
                    <el-button class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
            </el-table>
          </template>
        </el-form-item>
      </el-form>
      <div style="text-align:center;margin-top:30px">
        <el-button v-if="addDisabled" size="mini" @click="postReserveData()" :loading="buttonLoading" type="primary">确定</el-button>
        <el-button v-if="!addDisabled && this.formData.status === 0" :loading="buttonLoading" size="mini" @click="releaseReserveData()" type="primary">释放</el-button>
        <el-button size="mini" :loading="buttonLoading" @click="backStep()">返回</el-button>
      </div>

      <el-dialog :modal-append-to-body="false" :visible="itemsShow" width="80%" :before-close="closeItemsDialog">
        <add-item :saleStatus="3" :goodsType="goodsType" :selectItemsData="formData.detailCmdList" ref="selectGoodItems" @cancel="itemsShow=false" @submit="getItemsData"></add-item>
      </el-dialog>
    </div>
</template>

<script>
  import addItem from '@/views/warehouse/components/selectItem/addItem'
  export default {
    name: 'stockReservedDetail',
    components: {
      addItem
    },
    data(){
      return {
        formData:{
          warehouseId:undefined,
          detailCmdList:[],
          remark:'',
        },
        addDisabled:true,
        warehouselist:[],
        itemsShow: false,
        goodsType:1,
        buttonLoading:false
      }
    },
    created() {
      this.getWarehouses();
      // this.getParamData();
    },
    activated(){
      this.getParamData();
    },
    methods: {
      checkReserveNum(row){
        if(this.addDisabled && row.numReserved > row.ableReserved){
          this.$message.info('预留数量不能超可预留数量！');
          row.numReserved = row.ableReserved
        }
      },
      handleDelete(index){
        this.formData.detailCmdList.splice(index,1);
      },
      getItemsData(val) {
        val.forEach(item =>{
          if(item.itemId === undefined){
            item.itemId = item.id
            item.goodsId = item.goodsId
          }
        })
        this.formData.detailCmdList = []
        this.formData.detailCmdList = this.formData.detailCmdList.concat(val)
        this.getWarehouseStock()
        this.itemsShow = false
      },
      getWarehouseStock(){
        if(this.formData.detailCmdList !== undefined && this.formData.detailCmdList.length>0){
          let ids = []
          this.formData.detailCmdList.forEach(item =>{
            ids.push(item.itemId)
          })
          this.$http.stockListByitemId(this, { warehouseId:this.formData.warehouseId,itemIdList:ids }).then(res => {  
            if(res.success && res.data !== undefined && res.data.length>0){
              this.formData.detailCmdList.forEach(item =>{
                  for(let i = 0;i<res.data.length;i++){
                    if(item.itemId === res.data[i].itemId){
                      item.numInWarehouseSum = res.data[i].numInWarehouseSum
                      item.numLockSum=res.data[i].numLockSum
                      item.numReservedSum = res.data[i].numReservedSum
                      let ableReserved = res.data[i].numInWarehouseSum - res.data[i].numLockSum - res.data[i].numReservedSum
                      if(ableReserved >0){
                        item.ableReserved = ableReserved
                      }else{
                        item.ableReserved = 0
                      }
                    }
                  }
              })
            }
          })
        }
      },
      closeItemsDialog() {
        this.$refs.selectGoodItems.handleCancel()
      },
      backStep(){
        this.$router.go(-1);
      },
      getWarehouses(){
        this.$http.warehousePoList(this, { page:1, size: 1000, openFlag: 1 }).then(res => {
          if (res.success) {
            this.warehouselist = res.data.list
          }
        })
      },
      getParamData(){
        this.addDisabled = true
        this.itemsShow = false
        if(this.$route.query.id) {
          this.addDisabled = false
          this.$http.stockReservedDetail(this, { id: this.$route.query.id }).then(res => {
            if (res.success) {
              if(res.data.detailCmdList !== undefined && res.data.detailCmdList.length>0){
                res.data.detailCmdList.forEach(item =>{
                  item.id= item.itemId 
                })
              }
              this.formData = res.data
            }
          })
        }else if( !this.$route.query.id){
          this.formData.warehouseId = undefined
          this.formData.detailCmdList = []
          this.formData.remark = ''
          this.formData.status = 0
        }
      },
      selectGoodsItems(){
        if(this.formData.warehouseId === undefined){
          this.$message.error('请先选择需要预留的仓库！');
          return
        }
        this.itemsShow = true
      },
      // 保存提交
      postReserveData(){
        this.buttonLoading = true
        if(this.formData.warehouseId  == null || this.formData.warehouseId == undefined || this.formData.warehouseId == ''){
          this.$message.error('选择的预留仓库不能空！');
          this.buttonLoading = false
          return
        }
        if(this.formData.detailCmdList  == null || this.formData.detailCmdList == undefined || this.formData.detailCmdList.length == 0){
          this.$message.error('选择预留商品不能为空！');
          this.buttonLoading = false
          return
        }
        let b = true
        this.formData.detailCmdList.forEach(item =>{
          if(item.numReserved === null || item.numReserved === undefined || item.numReserved <= 0){
            this.$message.error('物料'+item.itemCode+'预留数量不能为空或小于等于零！');
            b = false
            this.buttonLoading = false
          }
        })
        if(b){
          this.$http.addStockReserved(this, this.formData).then(res => {  
            if(res.success){
              this.$message({
                message: "添加预留成功",
                type: "success",
                duration: 1 * 1000,
                onClose: () => {
                  this.$router.go(-1);
                  this.buttonLoading = false
                }
              });
            }
          })
        }
      },
      releaseReserveData(){
        this.buttonLoading = true
        this.$http.stockRelease(this, {id:this.formData.id}).then(res => { 
          if(res.success){
            this.$message({
              message: "预留释放成功",
              type: "success",
              duration: 1 * 1000,
              onClose: () => {
                this.$router.go(-1);
                this.buttonLoading = false
              }
            });
            
          }
        })
      }
    },
    watch: {
      'formData.warehouseId': {
        handler() {
          if(this.addDisabled){
            this.getWarehouseStock();
            this.formData.detailCmdList.forEach(item =>{
                item.numReserved = 0
            })
          }
        },
        deep: true
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
        width:90%;
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
