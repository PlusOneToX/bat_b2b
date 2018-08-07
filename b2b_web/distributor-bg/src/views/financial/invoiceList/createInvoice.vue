<template>
  <main class="create-invoice">
    <header>
      <span>发票管理</span>
    </header>
    <div class="box-has-border">
      <div class="text-center title">
        <span>基本信息</span>
      </div>
      <el-form ref="chosenData" label-width="280px">
        <el-form-item label="发票类型">
          <el-radio :label="1" v-model="paramsToFill.invoiceType">普通发票</el-radio>
          <el-radio :label="2" v-model="paramsToFill.invoiceType">增值发票</el-radio>
        </el-form-item>
        <el-form-item label="发票抬头"></el-form-item>
        <el-form-item label="纳税人识别码">{{distrDetail.taxpayerNumber}}</el-form-item>
        <el-form-item label="发票注册地址">{{distrDetail.address}}</el-form-item>
        <el-form-item label="发票注册电话">{{distrDetail.phone}}</el-form-item>
        <el-form-item label="发票开户银行">{{distrDetail.bankDepositName}}</el-form-item>
        <el-form-item label="发票银行账户">{{distrDetail.bankAccountName}}</el-form-item>
        <el-form-item label="开票区域">
          <el-radio :label="1" v-model="paramsToFill.invoiceArea">国内</el-radio>
          <el-radio :label="2" v-model="paramsToFill.invoiceArea">国外</el-radio>
        </el-form-item>
        <el-form-item label="发票号码">
          <el-input v-model="paramsToFill.invoiceNo" style="width: 300px" size="mini"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="box-has-border">
      <div class="text-center title">
        <span>开票账单</span>
      </div>
      <div class="function">
        对账时间
        <el-date-picker size="mini" type="datetime" placeholder="请选择时间" v-model="billSpan.startTime"></el-date-picker>到
        <el-date-picker size="mini" type="datetime" placeholder="请选择时间" v-model="billSpan.endTime"></el-date-picker>
      </div>
      <el-table border :data="billTableData" style="width: 100%" header-row-class-name="header-row" @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column align="center" prop="billAmount" label="对账单号"></el-table-column>
        <el-table-column align="center" prop="billTime" label="对账时间"></el-table-column>
        <el-table-column align="center" prop="payType" :formatter="billPayTpFormatter" label="支付方式"></el-table-column>
        <el-table-column align="center" prop="payState" :formatter="billPayStFormatter" label="付款状态"></el-table-column>
        <el-table-column align="center" prop="billAmount" label="对账金额"></el-table-column>
      </el-table>
    </div>
    
    <div class="box-has-border">
      <div class="text-center title">
        <span>收票信息</span>
      </div>

      <div class="half-width">
        <el-form ref="chosenData" label-width="190px">
          <el-form-item label="收件人">{{distrDetail.registerName}}</el-form-item>
          <el-form-item label="邮编">{{distrDetail.zipCode}}</el-form-item>
          <el-form-item label="配送方式">
            {{distributionName}}
            <el-button size="mini" @click="distributionsVisible = true">编辑</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="half-width">
        <el-form ref="chosenData" label-width="190px">
          <el-form-item label="收件地址">{{distrDetail.address}}</el-form-item>
          <el-form-item label="联系电话">{{distrDetail.phone}}</el-form-item>
          <el-form-item label="物流单号">
            <el-input size="mini" style="width:180px" v-model="paramsToFill.logisticsNo"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!-- <div class="box-has-border">
      <div class="text-center title">
        <span>收款信息</span>
      </div>
      <el-form ref="chosenData" label-width="280px">
        <el-form-item label="发票金额"></el-form-item>
        <el-form-item label="已收金额"></el-form-item>
        <el-form-item label="未收金额"></el-form-item>
      </el-form>
    </div>-->
    <div class="box-has-border">
      <console parent="createInvoice" :params="mergeParams"></console>
    </div>

    <el-dialog :modal-append-to-body="false" title="选择快递" center :visible.sync="distributionsVisible" width="600px">
      <choseDistribution @chosen="handleChoseDis" :disList="distributions"></choseDistribution>
    </el-dialog>
  </main>
</template>

<script>
import pagination from "@/components/pagination/index";
import console from "@/views/financial/invoiceList/components/console";
import choseDistribution from "@/views/financial/invoiceList/components/choseDistribution";
import { dateToNum } from "@/views/warehouse/warehousesUtils";
import { parseTime } from "@/utils/index";
import {
  createInvoice,
  getbillsToInvoice,
  getDistrDetail
} from "@/views/financial/financialData";
import { confirmCreator } from "@/views/order/orderUtils";
import {
  billPayTpFormatter,
  billPayStFormatter
} from "@/views/financial/financialUtils";

export default {
  name: "createInvoice",
  created() {
    this.paramsToFill.distributorId = this.$route.query.distrId;
    this.updateMainData();
  },
  components: { pagination, console, choseDistribution },
  data() {
    return {
      distributionsVisible: false,
      distributionName: null,
      distrDetail: {},
      // paramsToFill: {
      //   },
      billSpan: {
        startTime: new Date(),
        endTime: new Date()
      },
      billTableData: [],
      billsChosen: [],
      paramsToFill: {
        distributorId: null,
        invoiceType: 1, // 发票类型
        invoiceArea: 1, // 开票区域
        logisticsNo: null, // 物流单号
        distributionId: null,
        invoiceNo: null
      },
      distributions: [] // 配送模式
    };
  },
  methods: {
    // 开票账单
    updateMainData() {
      // 对账单信息
      getbillsToInvoice(this, {
        distributorId: this.paramsToFill.distributorId
      }).then(res => (this.billTableData = res.bills));
      // 分销商信息
      getDistrDetail(this, { id: this.paramsToFill.distributorId }).then(
        res => (this.distrDetail = res.distributor)
      );
    },
    getbillsByTime() {
      getbillsToInvoice(this, {
        distributorId: this.paramsToFill.distributorId,
        startTime: this.billSpan.startTime.getTime(),
        endTime: this.billSpan.endTime.getTime()
      }).then(res => (this.billTableData = res.bills));
    },
    checkTimes() {
      const startTime = dateToNum(this.billSpan.startTime),
        endTime = dateToNum(this.billSpan.endTime),
        now = dateToNum(new Date());
      if (endTime > now) {
        this.$message({ message: "请选择过去的时间", type: "warning" });
        this.billSpan.endTime = new Date();
      } else {
        if (startTime > endTime) {
          this.$message({
            message: "终点时间必须在起点时间之后",
            type: "warning"
          });
          this.billSpan.endTime = this.billSpan.startTime;
        }
      }
    },
    handleChoseDis(dis) {
      this.paramsToFill.distributionId = dis.id;
      this.distributionName = dis.name;
      this.distributionsVisible = false;
    },
    handleSelectionChange(val) {
      this.billsChosen = val;
    },
    billPayTpFormatter(row, column, cellValue) {
      return billPayTpFormatter(cellValue);
    },
    billPayStFormatter(row, column, cellValue) {
      return billPayStFormatter(cellValue);
    }
  },
  watch: {
    "billSpan.startTime": function(val, oldVal) {
      if (val) {
        this.checkTimes();
        this.getbillsByTime();
      }
    },
    "billSpan.endTime": function(val, oldVal) {
      if (val) {
        this.checkTimes();
        this.getbillsByTime();
      }
    }
  },
  computed: {
    mergeParams() {
      const billIds = this.billsChosen
        .map(bill => bill.id)
        .reduce((sum, cur, index) => {
          return sum + (index == 0 ? "" : ",") + cur;
        }, "");
      const params = Object.assign({}, this.paramsToFill, { billIds });

      params.distributorId = this.paramsToFill.distributorId;

      return params;
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.create-invoice {
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    span {
      margin-left: 30px;
    }
    .btn-add {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
    }
  }
  background-color: white;
  padding-bottom: 30px;
  // height: 100%;
  form.el-form {
    margin-top: 0;
  }
  .el-form div.el-form-item {
    margin-bottom: 5px;
  }
  .half-width {
    width: 50%;
    box-sizing: border-box;
    float: left;
  }
  .text-center {
    text-align: center;
  }
  .text-center.title {
    // margin-bottom: 25px;
    background-color: $bg;
    padding-top: 10px;
    padding-bottom: 10px;
    border-bottom: 1px solid $tableColor;
    border-top: 1px solid $tableColor;
  }
  .box-btn-top {
    padding: 20px;
  }
  .box-has-border {
    overflow: hidden;
    .cost-line {
      padding-bottom: 10px;
      padding-top: 10px;
      border-bottom: 1px solid $tableColor;
      padding-left: 30px;
      span.cost-info {
        margin-left: 5px;
      }
      span.cost-info:last-child {
        margin-right: 35px;
      }
    }
    .cost-line:last-child {
      border-bottom: none;
    }

    .align-right {
      text-align: right;
    }
    div.form {
      margin-top: 30px;
      margin-bottom: 40px;
      form.el-form {
        margin-right: 0;
        width: 80%;
        min-width: 800px;
        // display: inline-block;
        max-width: 1000px;
      }
    }
    // .operation{
    //   padding-bottom: 50px;
    //   .operation-part:first-child{
    //     margin-left: 100px;
    //   }
    //   .operation-part:last-child{
    //     float: right;
    //     margin-right: 80px;
    //   }
    // }
  }
  .function {
    padding: 16px;
    background-color: white;
    .btn-export {
      background-color: lighten(grey, 40%);
    }
    .search {
      float: right;
    }
    .box-search {
      width: 140px;
    }
    .btn-search {
      background-color: $lakeBlue;
      color: white;
    }
  }

  /*background-color: blue;*/
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
  .table-cell {
    text-align: center;
  }
  div.el-tabs__nav {
    margin-left: 30px;
  }
}
</style>
