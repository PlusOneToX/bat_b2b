<template>
  <div>
    <div class="warehouse-list-wrap" v-show="pageState == 'exchangeReadct'" >
      <div>
        <header>
          <h4>换货单详情</h4>
            <el-button class="btn-home" @click="backTrun">
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
              <el-form-item label="退换货申请单号:">
                {{chosen.userName}}
              </el-form-item>
              <el-form-item label="创建人:">
                {{chosen.userName}}
              </el-form-item>
              <el-form-item label="配送方式:">
                {{chosen.phone || '却少字段或者空字符串'}}
              </el-form-item>
            </el-form>
          </div>
          <!-- 右侧 -->
          <div class="half-width">
            <el-form ref="chosen" label-width="280px">
              <el-form-item label="订单状态:">
                {{forstatus(chosen.status)}}
              </el-form-item>
              <el-form-item label="申请时间:">
                <span v-if="chosen.list">
                  {{timeFormatter(chosen.list[entryIndex].createTime)}}
                </span>
              </el-form-item>
              <el-form-item label="下单时间:">
                <span v-if="chosen.list">
                  {{timeFormatter(chosen.list[entryIndex].createTime)}}
                </span>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <!-- 物流信息 -->
        <div class="box-has-border">
          <div class="text-center title">
            <span>收货人信息</span>
            <!-- <el-button size="mini" @click="_ => {{$emit('redactPage','logistics')}}">编辑</el-button> -->
          </div>
          <!-- 左侧 -->
          <div class="half-width">
            <el-form ref="chosen" label-width="280px">
              <el-form-item label="收货人:">
                  {{chosen.courierNo  || "空字符串"}}
              </el-form-item>
              <el-form-item label="地址:">
                  {{chosen.courierNo  || "空字符串"}}
              </el-form-item>
              <el-form-item label="电话:">
                  {{chosen.courierNo  || "空字符串"}}
              </el-form-item>
              <el-form-item label="最近收获日:">
                  {{chosen.courierNo  || "空字符串"}}
              </el-form-item>
            </el-form>
          </div>
          <!-- 右侧 -->
          <div class="half-width">
            <el-form ref="chosen" label-width="280px">
              <el-form-item label="电子邮件:">
                  {{chosen.courierNo  || "空字符串"}}
              </el-form-item>
              <el-form-item label="邮编:">
                  {{timeFormatter(chosen.createTime)  || "空字符串"}}
              </el-form-item>
              <el-form-item label="手机:">
                  {{timeFormatter(chosen.arriveTime)  || "空字符串"}}
              </el-form-item>
            </el-form>
          </div>

        </div>

        <!-- 退换商品 -->
        <div class="box-has-border">
          <div class="text-center title">
            <span>商品信息</span>
          </div>

          <el-table border :data="chosen.list" class="commodityTable"   header-row-class-name="header-row">
            <el-table-column align="center" prop="goodsName" label="商品名称"> </el-table-column>
            <el-table-column align="center" prop="goodsNo" label="商品编号"> </el-table-column>
            <el-table-column align="center" prop="orderId" label="订单号"> </el-table-column>
            <el-table-column align="center" prop="" label="货号"> </el-table-column>
            <el-table-column align="center" prop="applyCount" label="货品号"> </el-table-column>
            <el-table-column align="center" prop="againstCount" label="数量"> </el-table-column>
            <el-table-column align="center" prop="arrivalCount" label="规格"> </el-table-column>
            <el-table-column align="center" prop="returnCount" label="仓库"> </el-table-column>
            <el-table-column align="center" prop="exchangeCount" label="库存"> </el-table-column>
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
            <el-table :data="chosen.list" border style="width: 100%" header-row-class-name="header-row" class="sheetDada">
              <el-table-column align="center" prop="goodsName"  label="操作者"> </el-table-column>
              <el-table-column align="center" prop="createTime"  :formatter="formatTime" label="操作时间"> </el-table-column>
              <el-table-column align="center" prop="status"  :formatter="forstatus" label="订单状态" > </el-table-column>
              <el-table-column align="center" prop="status"  :formatter="forstatus" label="发货状态" > </el-table-column>
              <el-table-column align="center" prop="remark"  label="备注"> </el-table-column>
            </el-table>
          </div>
        </div>

        <div class="operation">
          <span class="instruction">
            <span class="instruction_left"> 当前可执行操作： </span>
            <el-button size="mini" @click="sure('bagCreateDelivery')">生成发货单</el-button>
            <el-button size="mini" @click="AOG()">取消发货单</el-button>
          </span>
        </div>

      </div>

      <!-- 编辑页面的子组件页面 -->
      <!-- <logistics v-if="chosen.list && pageState== 'logistics'  "  :chosen="chosen"></logistics> -->
      <!-- <editsure v-if="pageState == 'editsure'"  :chosen="chosen"></editsure> -->
    </div>
    <!-- 发货单的子组件页面 -->
    <bagCreateDelivery
    v-if="pageState == 'bagCreateDelivery'"
    :chosen="chosen"
    :tableData="tableData"
    :pageState="pageState"
    >
<!-- :distributions="distributions" -->
    </bagCreateDelivery>
  </div>

</template>

<script>
// import procedure from "@/components/Procedure/index";
import { parseTime } from "@/utils/index";
import eventBus from "@/views/order/eventBus";
import { timeFormat } from "@/utils/timeFormat";
// ======引用组件======
import bagCreateDelivery from "@/views/order/exchangeGoods/operation/bagCreateDelivery";
import createDelivery from "@/views/order/orderList/operation/createDelivery";

export default {
  name: "exchangeReadct",
  components: {
    bagCreateDelivery,
    createDelivery
  },
  mounted() {
    this.updateMainData();
  },
  data() {
    return {
      chosen: {},
      operationData: [],
      operateBean: [], // 申请数量
      productOne: [], // 到货数量
      productTwo: [], // 退货数量
      productThree: [], // 换货数量
      productFour: [], // 违规数量
      payAmount: [] // 未处理数量
    };
  },
  props: {
    pageState: String,
    entryIndex: Number, // 本组件主数据在父组件数组中的index
    tableData: Array,
    // distributions: Array,
  },
  computed: {
    checkedData() {
      return this.tableData[this.entryIndex].status;
    },
    // 未处理货品数量 payAmount
    payAmountData() {
      return this.payAmount;
    }
  },
  methods: {
    backTrun() {
      eventBus.$emit("downBcak", "exchangeGoods");
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
      this.$api.get(this, "admin/u/afterservice", {
          id: this.tableData[this.entryIndex].id
        }).then(res => {
          this.chosen = res.afterServiceBean;
          // 未处理数量操作
          for (let i = 0, len = this.chosen.list.length; i < len; i++) {
            var product = { applyCount: this.chosen.list[i].applyCount }; // 申请数量
            var productOne = { arrivalCount: this.chosen.list[i].arrivalCount }; // 到货数量
            var productTwo = { returnCount: this.chosen.list[i].returnCount }; // 退货数量
            var productThree = { exchangeCount: this.chosen.list[i].exchangeCount }; // 换货数量
            var productFour = { againstCount: this.chosen.list[i].againstCount }; // 违规数量
            this.operateBean.push(product); // 申请数量
            this.productOne.push(productOne); // 到货数量
            this.productTwo.push(productTwo); // 退货数量
            this.productThree.push(productThree); // 换货数量
            this.productFour.push(productFour); // 违规数量
            // 未处理数量
            this.payAmount = this.chosen.list[i].returnCount - this.chosen.list[i].exchangeCount - this.chosen.list[i].againstCount;
            const payfor = this.chosen.list.map(val => val.returnCount);
            var payArray = 0;
            for (let i = 0, leng = payfor.length; i < len; i++) {
              payArray = payArray + payfor[i];
              this.chosen.list[i].payAmountB = this.payAmount;
            }
          }
        }),
        // 操作人主要信息详情
        this.$api.get(this, "admin/u/afterservice/log/list", {
            id: this.tableData[this.entryIndex].id
          }).then(res => {
            this.operationData = res.list;
          });
    },
    // 生成发货单
    sure(state) {
      this.$emit("up", state);
    },
    // 取消发货单
    AOG() {
      this.$router.push({
        name: "editAOG",
        query: { id: this.tableData[this.entryIndex].id }
      });
    }
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
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
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
      text-align: center;
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
  }
  .box_small {
    text-align: center;
    margin-top: 20px;
  }
}
</style>
