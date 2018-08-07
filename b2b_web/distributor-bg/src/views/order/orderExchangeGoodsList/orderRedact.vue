<template>
  <div class="warehouse-list-wrap" v-show="pageState == 'details'">
    <div>
      <header>
      <h4>退换货详情</h4>
       <el-button class="btn-home" @click="fanhui">
        返回订单列表
      </el-button>
    </header>
    
    
    <div class="box-has-border">
      <div class="text-center title">
        <!-- 售后详情的分销商姓名、电话、手机，是通过分销商接口获取的 2018/5/7 16:03 -->
        <span>基本信息</span>
      </div>
      <!-- 左侧 -->
      <div class="half-width">
        <el-form ref="chosen" label-width="280px">
          <el-form-item label="退换单号:">
            <span v-if="chosen.list">
              {{chosen.list[entryIndex].id}}
            </span>
          </el-form-item>
          <el-form-item label="分销商用户名:">
            {{chosen.userName}}
          </el-form-item>
          <el-form-item label="分销商姓名:">
            {{chosen.userName}}
          </el-form-item>
          <el-form-item label="分销商电话:">
            {{chosen.phone || '却少字段或者空字符串'}}
          </el-form-item>
          <el-form-item label="备注:">
            {{chosen.remark}}
          </el-form-item>
        </el-form>
      </div>
      <!-- 右侧 -->
      <div class="half-width">
        <el-form ref="chosen" label-width="280px">
          <el-form-item label="申请状态:">{{forstatus(chosen.status)}} </el-form-item>
          <el-form-item label="申请时间:">
            <span v-if="chosen.list">
              {{timeFormatter(chosen.list[entryIndex].createTime)}}
            </span>
          </el-form-item>
          <el-form-item label="分销商手机:">
            {{chosen.mobile || '却少字段或者空字符串'}}
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 物流信息 -->
    <div class="box-has-border">
      <div class="text-center title">
        <span>物流信息</span>
        <!-- <el-button size="mini" @click="_ => {{$emit('redactPage','logistics')}}">编辑</el-button> -->
      </div>
      <!-- 左侧 -->
      <div class="half-width">
        <el-form ref="chosenData" label-width="280px">
          <el-form-item label="包裹单号:">
            <!-- <el-input size="small" class="half_search"></el-input> -->
              {{chosen.courierNo  || "空字符串"}} 
          </el-form-item>
          <el-form-item label="配送方式:"> 
            {{chosen.courierName  || '缺少字段或者为空字符串'}} 
          </el-form-item>
        </el-form>
      </div>
       <!-- 右侧 -->
      <div class="half-width">
        <el-form ref="chosenData" label-width="280px">
          <el-form-item label="包裹状态:"> The lack of field </el-form-item>
          <el-form-item label="登记时间:"> {{timeFormatter(chosen.createTime)}} </el-form-item>
          <el-form-item label="到货时间:"> {{timeFormatter(chosen.arriveTime)}} </el-form-item>
        </el-form>
      </div>

    </div>

    <!-- 退换商品 -->
    <div class="box-has-border">
      <div class="text-center title">
        <span>退换商品</span>
      </div>

      <el-table border :data="chosen.list" class="commodityTable"   header-row-class-name="header-row">
        <el-table-column align="center" prop="goodsName" label="商品名称"> </el-table-column>
        <el-table-column align="center" prop="goodsNo" label="商品编号"> </el-table-column>
        <el-table-column align="center" prop="orderId" label="订单号"> </el-table-column>
        <el-table-column align="center" prop="" label="规格"> </el-table-column>
        <el-table-column align="center" prop="applyCount" label="申请数量"> </el-table-column>
        <el-table-column align="center" prop="againstCount" label="违规数量"> </el-table-column>
        <el-table-column align="center" prop="arrivalCount" label="到货数量"> </el-table-column>
        <el-table-column align="center" prop="returnCount" label="退货数量"> </el-table-column>
        <el-table-column align="center" prop="exchangeCount" label="换货数量"> </el-table-column>
        <el-table-column align="center" prop="" label="换货订单"> </el-table-column>
        <el-table-column align="center" prop="description" label="换货原因"> </el-table-column>
      </el-table>

    </div>

    <!-- 操作信息 -->
    <div class="box-has-border" style="margin:0;">
      <div class="text-center title">
        <span>操作信息</span>
      </div>
      <!-- <div class="form">
        <el-form label-width="180px">
          <el-form-item label="操作备注" >
            <el-input type="textarea" :rows="5" v-model="mytext"></el-input>
          </el-form-item>
        </el-form>
      </div> -->

      

      <div class="sheet">
        <!--   :data="chosen.list" -->
        <el-table :data="operationData" border style="width: 100%" header-row-class-name="header-row" class="sheetDada">
          <el-table-column align="center" prop="operationName"  label="操作者"> </el-table-column>
          <el-table-column align="center" prop="updateTime"  :formatter="formatTime" label="操作时间"> </el-table-column>
          <el-table-column align="center" prop="status"  :formatter="forstatus" label="申请状态" > </el-table-column>
          <el-table-column align="center" prop=""  label="操作"> </el-table-column>
          <el-table-column align="center" prop="remark"  label="备注"> </el-table-column>
        </el-table>
      </div>
    </div>

    <div class="operation">
        <span class="instruction">
          <span class="instruction_left"> 当前可执行操作： </span>
          <!-- 0申请中,1待到货,2部分到货,3处理中,4已完成,5已拒绝,6退款中 -->
          <el-button size="mini" v-if="checkedData == 0"  @click="sure()">确认</el-button>
          <el-button size="mini" v-if="checkedData == 1 || checkedData == 2 || payAmountData == 0" @click="AOG()">到货</el-button>
          <el-button size="mini" v-if="checkedData == 2" @click="abolish()" >取消到货</el-button>
          <el-button size="mini" v-if="checkedData == 2 || checkedData == 3" @click="salesReturn()">退货</el-button>
          <el-button size="mini" v-if="checkedData == 2 || checkedData == 3" @click="Exchange()">换货</el-button>
          <el-button size="mini" v-if="checkedData == 2 || checkedData == 3" @click="editGetout()" >违规</el-button>
          <el-button size="mini" v-if="checkedData == 0" @click="reject()">拒绝</el-button>
          <el-button size="mini" v-if="checkedData == 1 || checkedData == 3" @click="complete()">完成</el-button>
        </span>
      </div>

    <div class="box_small">
      <!-- <el-button @click="back()" size="small" >返回</el-button> -->
    </div>
    </div>



    <!-- 编辑页面的子组件页面 -->
    <logistics v-if="chosen.list && pageState== 'logistics'  "  :chosen="chosen"></logistics>
    <!-- <editsure v-if="pageState == 'editsure'"  :chosen="chosen"></editsure> -->
  </div>

</template>

<script>
import procedure from "@/components/Procedure/index";
import { parseTime } from "@/utils/index";
import eventBus from "@/views/order/eventBus";
import { timeFormat } from "@/utils/timeFormat";
// ======引用组件======
import logistics from "@/views/order/orderExchangeGoodsList/edit/editRedact";
import editsure from "@/views/order/orderExchangeGoodsList/edit/editSure";

export default {
  name: "orderRedact",
  components: {
    procedure,
    logistics,
    editsure
  },

  mounted() {
    this.updateMainData();
    // this.chief();
  },
  data() {
    return {
      chosen: {},
      operationData: [],  // 操作者日志
      operateBean: [], // 申请数量
      productOne: [], // 到货数量
      productTwo: [], // 退货数量
      productThree: [], // 换货数量
      productFour: [], // 违规数量
      payAmount: [], // 未处理数量
    };
  },
  props: {
    pageState: String,
    entryIndex: Number, // 本组件主数据在父组件数组中的index
    tableData: Array,
    rowData: Object
  },
  computed: {
    checkedData(){
      return this.tableData[this.entryIndex].status
    },
    // 未处理货品数量 payAmount
    payAmountData() {
      return this.payAmount
    },

    
  },
  methods: {
    // // 详情主要数据
    // chief() {
    //   this.$api.get(this,'admin/u/afterservice',{id: this.rowData.id}).then(res => {
    //   })
    // },
    fanhui() {
      // this.pageState = 'orderGoodslist'
      eventBus.$emit('callBack','orderGoodslist')
    },
    back() {
      this.$router.push({ name: "orderExchangeGoods" });
    },

    // 时间格式化一
    timeFormatter(cellValue) {
      return parseTime(cellValue);
    },
    // 时间格式化二
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    // 申请状态
    forstatus(row) {
      switch (row) {
        case 0:
          return "申请中";
        case 1:
          return "待到货";
        case 2:
          return "部分到货";
        case 3:
          return "处理中";
        case 4:
          return "已完成";
        case 5:
          return "已拒绝";
        case 6:
          return "退款中";
      }
    },

    // 售后商品详情主要数据
    updateMainData() {
      // 商品主要信息详情
      this.$api.get(this, "admin/u/afterservice", {id: this.rowData.id}).then(res => {
          this.chosen = res.afterServiceBean;
          // 未处理数量操作
          for(let i = 0, len = this.chosen.list.length; i < len; i++){   
            var product = {applyCount: this.chosen.list[i].applyCount} // 申请数量
            var productOne = {arrivalCount: this.chosen.list[i].arrivalCount} // 到货数量
            var productTwo = {returnCount: this.chosen.list[i].returnCount} // 退货数量
            var productThree = {exchangeCount: this.chosen.list[i].exchangeCount} // 换货数量
            var productFour = {againstCount: this.chosen.list[i].againstCount} // 违规数量
            this.operateBean.push(product)  // 申请数量
            this.productOne.push(productOne)  // 到货数量
            this.productTwo.push(productTwo)  // 退货数量
            this.productThree.push(productThree)  // 换货数量
            this.productFour.push(productFour)  // 违规数量
            // 未处理数量
            this.payAmount = this.chosen.list[i].returnCount - this.chosen.list[i].exchangeCount - this.chosen.list[i].againstCount;
            const payfor = this.chosen.list.map(val => val.returnCount);
            var payArray = 0;
            for(let i = 0, leng = payfor.length; i < len; i++){
                payArray = payArray + payfor[i]
                this.chosen.list[i].payAmountB = this.payAmount
            }
          }
        }).then( _ => {
          this.$api.get(this, "admin/u/afterservice/log/list", {id: this.chosen.afterServiceId}).then(res => {
            this.operationData = res.list;
          });
        })
        // 操作人主要信息详情  afterServiceId // 售后单号  {id: this.tableData[this.entryIndex].id}
        
   
    },
    // 确认到货
    sure() {
      this.$router.push({
        name: "editsure",
        query: { id: this.tableData[this.entryIndex].id }
      });
    },
    // 到货操作
    AOG() {
      this.$router.push({
        name: "editAOG",
        query: { id: this.tableData[this.entryIndex].id }
      });
    },
    // 取消到货: 取消到货，直接取消商家一整个到货
    abolish() {
      this.$api.put(this, "admin/u/afterservice", {
        id: this.tableData[this.entryIndex].id,
        operateId: 8
      }).then(res => {
        if(res.code == 0) {
          this.$message.success({ 
            message: '您已成功取消到货',
            duration: 3 * 1000,
            onClose: () => {
              // this.$router.go(-1)
            }
          })
          this.$router.push({ name: 'orderGoodslist'})
        }
      });
    },
    // 退货操作
    salesReturn() {
      this.$router.push({
        name: "editReturn",
        query: { id: this.tableData[this.entryIndex].id }
      });
    },
    // 换货操作
    Exchange() {
      this.$router.push({
        name: "editExchange",
        query: { 
          id: this.tableData[this.entryIndex].id,
          orderId: this.chosen.list[this.entryIndex].orderId,
          payAmount: this.payAmount
         }
      });
    },
    // 违规操作
    editGetout() {
      this.$router.push({
        name: "editGetout",
        query: { 
          id: this.tableData[this.entryIndex].id,
          orderId: this.chosen.list[this.entryIndex].orderId,
        },
      });
    },
    // 拒绝操作
    reject() {
      this.$api.put(this,'admin/u/afterservice',{
        id: this.tableData[this.entryIndex].id,
        operateId: 7
      }).then(res => {
        if(res == 0) {
          this.$message.success({
              message: '您已成功拒绝该订单申请',
              duration: 3 * 1000,
              onClose: () => { }
          })
          this.$router.go(-1)
        }
      })
    },
    // 完成操作
    complete() {
      this.$api.put(this,'admin/u/afterservice',{
        id: this.tableData[this.entryIndex].id,
        operateId: 6
      }).then(res => {
        if(res.code == 0) {
          this.$message.success({
              message: '您已成功完成该订单申请',
              duration: 3 * 1000,
              onClose: () => {
                  // this.$router.go(-1)
              }
          })
        }
      })
    },
  },
  watch: {
    entryIndex() {
      this.updateMainData();
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.warehouse-list-wrap {
  min-width: 1050px;
  background-color: #fff;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    h4 {
      margin-left: 30px;
      display: inline-block;
      font-weight: 400;
    }
    .btn-home{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
        margin-left: 0;
      }
  }
  .box-has-border {
    overflow: hidden;
    margin-top: 15px;
    background-color: #fff;
    .title {
      padding-bottom: 10px;
      padding-top: 10px;
      border-top: 1px solid $tableColor;
      border-bottom: 1px solid $tableColor;
      text-align: center;
      background-color: lighten(grey, 40%);
    }
    .commodityTable {
      text-align: center
    }
    .header-row {
      th {
        text-align: center;
      }
    }
    .sheet {
      margin-top: 15px;
      .sheetDada {
        text-align: center;
      }
    }
    .half-width {
      width: 50%;
      box-sizing: border-box;
      float: left;
      .half_search {
        width: 35%;
      }
      .el-form-item {
        margin-bottom: 10px;
      }
    }
  }
  .operation {
      margin-left: 45px;
      padding: 20px 0 0 0;
      margin-top: 40px;
      padding-bottom: 40px;
    }
  .box_small {
    text-align: center;
    margin-top: 20px;
  }
}
</style>
