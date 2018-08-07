<template>
  <div class="list-wrap">
    <header>
      <h4>权益兑换列表</h4>
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <el-select
            size="mini"
            v-model="pageInfo.status"
            style="width: 140px"
            clearable
          >
            <el-option label="未验证" :value="1"> </el-option>
            <el-option label="未兑换" :value="2"> </el-option>
            <el-option label="已兑换" :value="3"> </el-option>
            <el-option label="已作废" :value="4"> </el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <el-select
            size="mini"
            v-model="pageInfo.contentType"
            style="width: 140px"
            clearable
          >
            <el-option label="分销商ID" :value="1"> </el-option>
            <el-option label="分销商用户名" :value="2"> </el-option>
            <el-option label="第三方手机号" :value="3"> </el-option>
            <el-option label="第三方验证码" :value="4"> </el-option>
            <el-option label="兑换码" :value="5"> </el-option>
          </el-select>
          <el-input
            v-model.trim="pageInfo.content"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入关键字搜索"
            class="box-input"
          ></el-input>
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
        </div>
      </div>
    </div>

    <el-row v-loading="loading">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        class="tableCenter"
      >
        <el-table-column
          label="分销商ID"
          align="center"
          prop="distributorId"
          :min-width="90"
        ></el-table-column>
        <el-table-column
          label="分销商用户名"
          align="center"
          prop="distributorName"
          show-overflow-tooltip
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="第三方手机号"
          align="center"
          prop="thirdPhone"
          show-overflow-tooltip
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="第三方验证码（苏宁是订单号）"
          align="center"
          prop="thirdCode"
          show-overflow-tooltip
          :min-width="230"
        ></el-table-column>
        <el-table-column
          label="材质ID"
          align="center"
          prop="materialId"
          show-overflow-tooltip
          :min-width="80"
        ></el-table-column>
        <el-table-column
          label="材质名称"
          align="center"
          prop="materialName"
          show-overflow-tooltip
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          show-overflow-tooltip
          :min-width="80"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.status === 1">未验证</span>
            <span v-else-if="scope.row.status === 2">未兑换</span>
            <span v-else-if="scope.row.status === 3">已兑换</span>
            <span v-else-if="scope.row.status === 4">已作废</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="用户编码"
          align="center"
          prop="customerNo"
          show-overflow-tooltip
          :min-width="130"
        >
        </el-table-column>
        <el-table-column
          label="兑换活动名称"
          align="center"
          prop="exchangeName"
          show-overflow-tooltip
          :min-width="130"
        >
        </el-table-column>
        <el-table-column
          label="兑换码（暗码）"
          align="center"
          prop="secretCode"
          show-overflow-tooltip
          :min-width="130"
        >
        </el-table-column>
        <el-table-column
          label="卡片码（明码）"
          align="center"
          prop="plainCode"
          show-overflow-tooltip
          :min-width="130"
        >
        </el-table-column>
        <el-table-column label="操作" :min-width="180" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="
                Number(scope.row.status) === 1 || Number(scope.row.status) === 2
              "
              class="mini-tableadd-btn"
              @click="handleStatus(scope.row, 1)"
              >作废</el-button
            >
            <el-button
              class="mini-search-btn"
              v-if="Number(scope.row.status) === 3"
              @click="handleCheck(scope.row.orderId)"
              >查看订单</el-button
            >
            <el-button
              class="mini-delete-btn"
              v-if="Number(scope.row.status) === 4"
              @click="handleStatus(scope.row, 2)"
              >恢复</el-button
            >
            <el-button class="mini-search-btn" @click="handleLog(scope.row.id)"
              >日志</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="total"
        :page="pageInfo.page"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </el-row>

    <!-- 日志 -->
    <el-dialog title="日志" :visible.sync="logDialogVisible" width="60%" center>
      <el-table
        :data="logData"
        header-row-class-name="header-row"
        border
        style="text-align: center"
      >
        <el-table-column
          align="center"
          label="操作人"
          prop="operatorName"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.operatorUserType === 1">苏宁</span>
            <span v-else-if="scope.row.operatorUserType === 2">B2B系统</span>
            <span v-else>{{scope.row.operatorName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="时间"
          prop="createTime"
        ></el-table-column>
        <el-table-column
          align="center"
          label="操作内容"
          prop="operateType"
          :formatter="formatOperateType"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="备注"
          prop="remark"
          show-overflow-tooltip
        >
        </el-table-column>
      </el-table>
      <page
        :total="logTotal"
        :page="logInfo.page"
        @sizeChange="logSizeChange"
        @currentChange="logCurrentChange"
      ></page>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
export default {
  name: "exchangeExport",
  components: {
    page,
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        status: "",
        contentType: 1,
        content: undefined,
      },
      tableData: [], // 列表
      total: "", // 列表总数
      // 日志
      logDialogVisible: false,
      logData: [],
      logTotal: "",
      logInfo: {
        page: 1,
        size: 10,
        thirdQuanyiId: "",
      },
    };
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
  methods: {
    // 操作内容
    formatOperateType(row) {
      switch (row.operateType) {
        case 1:
          return "同步订单"; // 苏宁传输订单
        case 2:
          return "系统对苏宁订单进行派工";
        case 3:
          return "验证权益"; // C端客户领取权益码
        case 4:
          return "系统对苏宁订单进行签到";
        case 5:
          return "使用兑换码下单"; // 系统对苏宁订单进行核销
        case 6:
          return "苏宁端取消订单";
        case 7:
          return "作废权益"; // 管理员对权益进行取消
        case 8:
          return "恢复权益"; // 管理员撤销权益取消
        case 9:
          return "客户重新下单";
      }
    },
    // 初始化列表数据
    initData() {
      // 清空默认数据
      this.pageInfo.distributorId = "";
      this.pageInfo.distributorName = "";
      this.pageInfo.thirdPhone = "";
      this.pageInfo.thirdCode = "";
      this.pageInfo.secretCode = "";

      switch (this.pageInfo.contentType) {
        // 分销商ID
        case 1:
          this.pageInfo.distributorId = this.pageInfo.content;
          break;
        // 分销商用户名
        case 2:
          this.pageInfo.distributorName = this.pageInfo.content;
          break;
        // 第三方手机号
        case 3:
          this.pageInfo.thirdPhone = this.pageInfo.content;
          break;
        // 第三方验证码
        case 4:
          this.pageInfo.thirdCode = this.pageInfo.content;
          break;
        // 兑换码
        case 5:
          this.pageInfo.secretCode = this.pageInfo.content;
          break;
        default:
          break;
      }
      this.$http.getEquityList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    // 列表条数
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 列表页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 搜索列表
    Csearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    // 输入框输入搜索优惠券列表
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    // 查看订单（柔性订单）
    handleCheck(id) {
      this.$router.push({
        name: "orderDetail",
        query: {
          orderId: id,
          orderCustG: true,
          type: 3,
        },
      });
    },
    // 作废/恢复
    handleStatus(row, type) {
      let msg = "";
      if (type === 1) {
        msg = "此操作将作废该权益，是否继续？";
      } else {
        msg = "此操作将恢复该权益，是否继续？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          if (type === 1) {
            // 作废
            this.$http
              .handleEquityStatus(this, {
                id: row.id,
                distributorId: row.distributorId,
                distributorName: row.distributorName,
              })
              .then((res) => {
                if (res.success) {
                  this.$message({
                    type: "success",
                    message: "操作成功",
                  });
                  this.initData();
                }
              });
          } else {
            // 恢复
            this.$http
              .handleEquityStatusCancel(this, {
                id: row.id,
                distributorId: row.distributorId,
                distributorName: row.distributorName,
              })
              .then((res) => {
                if (res.success) {
                  this.$message({
                    type: "success",
                    message: "操作成功",
                  });
                  this.initData();
                }
              });
          }
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 日志
    initLogData() {
      this.$http.getEquityLogList(this, this.logInfo).then((res) => {
        if (res.success) {
          this.logData = res.data.list;
          this.logTotal = res.data.total;
        }
      });
    },
    handleLog(id) {
      this.logDialogVisible = true;
      this.logInfo.thirdQuanyiId = id;
      this.initLogData();
    },
    logSizeChange(size) {
      this.logInfo.size = size;
      this.logInfo.page = 1;
      this.initLogData();
    },
    logCurrentChange(page) {
      this.logInfo.page = page;
      this.initLogData();
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}

.list-wrap {
  background-color: white;

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

    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
}

.function {
  background-color: white;
}

.Fheader {
  margin: 10px;
  display: flex !important;
  justify-content: space-between;
  align-items: center !important;
  .Fleft {
    overflow: hidden;
    float: left;
  }
  .Fsearch {
    overflow: hidden;
    float: right;
    .box-input {
      width: 215px;
    }
    .box-btn {
      float: right;
      margin-left: 5px;
    }
  }
}
</style>
