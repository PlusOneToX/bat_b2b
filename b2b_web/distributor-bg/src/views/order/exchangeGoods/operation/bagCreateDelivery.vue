<template>
  <div>
    <div class="warehouse-list-wrap" v-if="pageState == 'bagCreateDelivery' ">
      <div>
        <header>
          <h4>生成发货单</h4>
          <!-- <el-button class="btn-home" @click="backTrun"> 返回换货单详情 </el-button>-->
        </header>
      </div>
      <!-- 基本信息 -->
      <div class="box-has-border">
        <div class="title">
          <span>基本信息</span>
        </div>

        <div class="half-width">
          <el-form ref="chosen" label-width="280px">
            <el-form-item label="订单号:">{{formData.id}}</el-form-item>
            <el-form-item label="购买者:">{{formData.userName}}</el-form-item>
          </el-form>
        </div>

        <div class="half-width">
          <el-form ref="chosen" label-width="280px">
            <el-form-item label="下单时间:">{{timeFormatter(formData.createTime)}}</el-form-item>
            <el-form-item label="配送方式:">{{orderStatusFormatter(formData.userId)}}</el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 物流信息 -->
      <div class="box-has-border">
        <div class="title">
          <span>收货人信息</span>
        </div>

        <div class="half-width">
          <el-form ref="chosen" label-width="280px">
            <el-form-item label="收货人:">{{formData.userName || '该字段为空'}}</el-form-item>
            <el-form-item label="地址:"></el-form-item>
            <el-form-item label="电话:"></el-form-item>
            <el-form-item label="最近送货日:"></el-form-item>
          </el-form>
        </div>

        <div class="half-width">
          <el-form ref="chosen" label-width="280px">
            <el-form-item label="电子邮件:">{{formData.courierNo || "空字符串"}}</el-form-item>
            <el-form-item label="邮编:">{{timeFormatter(formData.createTime) || "空字符串"}}</el-form-item>
            <el-form-item label="手机:">{{timeFormatter(formData.arriveTime) || "空字符串"}}</el-form-item>
            <el-form-item label="分销商留言"></el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 退换商品 -->
      <div class="box-has-border">
        <div class="title">
          <span>商品信息</span>
        </div>
        <el-table border :data="formData.list" class="commodityTable" header-row-class-name="header-row" >
          <el-table-column align="center" prop="goodsName" label="商品名称"></el-table-column>
          <el-table-column align="center" prop="goodsNo" label="商品编号"></el-table-column>
          <el-table-column align="center" prop="orderId" label="货号"></el-table-column>
          <el-table-column align="center" prop label="货品号"></el-table-column>
          <el-table-column align="center" prop="applyCount" label="仓库"></el-table-column>
          <el-table-column align="center" prop="againstCount" label="库存"></el-table-column>
          <el-table-column align="center" prop="arrivalCount" label="规格"></el-table-column>
          <el-table-column align="center" prop="returnCount" label="数量"></el-table-column>
          <el-table-column align="center" prop="exchangeCount" label="已发数量"></el-table-column>
          <el-table-column align="center" label="此单发货数量">
            <el-input></el-input>
          </el-table-column>
        </el-table>
      </div>

      <!-- 操作信息 -->
      <div class="box-has-border" style="margin:0;">
        <div class="title">
          <span>操作信息</span>
        </div>
        <div class="form">
          <el-form label-width="180px" class="fomrm_remark">
            <el-form-item label="操作备注">
              <el-input type="textarea" :rows="5" v-model="remark"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="operation">
        <span class="instruction">
          <span class="instruction_left">当前可执行操作：</span>
          <el-button size="mini" @click="sure()">确认生成发货单</el-button>
          <el-button size="mini" @click="cancel">取消</el-button>
        </span>
      </div>
    </div>

    <!-- 引用组件 -->
    <!-- <createDelivery :chosenData="chosenData" :distributions="distributions" ></createDelivery> -->
  </div>
</template>


<script>
import eventBus from "@/views/order/eventBus";
import { parseTime } from "@/utils/index";
import { timeFormat } from "@/utils/timeFormat";
// 引用组件
import createDelivery from "@/views/order/orderList/operation/createDelivery";
export default {
  name: "bagCreateDelivery",
  created() {
    this.formData = JSON.parse(JSON.stringify(this.chosen));
  },
  components: {},
  props: {
    pageState: String,
    tableData: Array,
    chosen: Object
  },
  data() {
    return {
      remark: "",
      formData: {}
    };
  },
  methods: {
    
    sure() { // 确认生成发货单
      this.$api .put(this, "admin/u/p/order/operate", {
          id: this.formData.id,
          operateType: 4,
          remark: this.remark
        }).then(res => {
          if (code == 0) {
            this.$message.success({
              message: "发货成功",
              duration: 3 * 1000,
              onClose: () => {}
            });
            this.$emit("shipments", "exchangeReadct");
          } else {
            this.$message.error({
              message: "发货失败",
              duration: 3 * 1000,
              onClose: () => {  }
            });
          }
        });
    },
    
    cancel() { // 取消按钮
      eventBus.$emit("backChange", "exchangeReadct");
    },
    
    timeFormatter(cellValue) { // 时间格式化
      return parseTime(cellValue);
    },
    
    formatTime(row, col, val) { // element表格时间格式化
      return timeFormat(val);
    },
    orderStatusFormatter(stateCode) { // 订单状态
      switch (stateCode) {
        case 1:
          return "待确认";
        case 2:
          return "已确认";
        case 3:
          return "已完成";
        case 4:
          return "退换货";
        case 5:
          return "无效";
        default:
          return stateCode;
      }
    },
    
    forstatus(row) { // 申请状态
      switch (row.status) {
        case 0:
          return "处理中";
        case 1:
          return "已完成";
      }
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
    .form {
      padding: 20px 0 0 0;
      .fomrm_remark {
        width: 80%;
      }
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
