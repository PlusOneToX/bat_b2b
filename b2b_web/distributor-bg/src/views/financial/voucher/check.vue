<template>
  <div class="check-list">
    <div class="sales-promotion-check">
      <header>
        <h4>代金券审批列表</h4>
      </header>
    </div>
    <el-row class="nav-bar">
      <el-col :span="8">
        <el-radio-group v-model="pageInfo.labelType" size="mini">
          <el-radio-button :label="1">我发起的</el-radio-button>
          <el-radio-button :label="2">待我审批</el-radio-button>
          <el-radio-button :label="3">我审批的</el-radio-button>
        </el-radio-group>
      </el-col>
      <el-col :span="16" justify="right">
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="pageInfo.content"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入搜索内容"
            class="box-input"
          ></el-input>
          <el-select
            class="content_select"
            placeholder="选择类型"
            size="mini"
            style="width: 140px; float: right"
            v-model="pageInfo.contentType"
            clearable
          >
            <el-option
              v-for="item in contentTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </div>
      </el-col>
    </el-row>

    <el-table
      :data="tableData"
      border
      header-row-class-name="header-row"
      class="tr-header tableCenter"
      v-loading="loading"
    >
      <el-table-column
        align="center"
        label="审批单号"
        prop="id"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="审批状态"
        prop="checkStatus"
        :formatter="checkStatus"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="审批类型"
        prop="promotionType"
        :formatter="formatType"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="发起人"
        prop="userName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="发起时间"
        prop="createTime"
        :formatter="formatTime"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="操作" width="80" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleSee(scope.row)"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <page
      :page="pageInfo.page"
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
import PageHeader from "@/components/PageHeader";
export default {
  name: "voucherCheck",
  data() {
    return {
      loading: false,
      promotionRuleDescribe: "",
      pageInfo: {
        page: 1,
        size: 10,
        labelType: 1,
        promotionType: 4, // 代金券
        contentType: 4,
        content: "",
      },
      total: 0,
      tableData: [],
      contentTypes: [
        { value: 4, label: "审批单号" },
        { value: 1, label: "代金券券名" },
        { value: 3, label: "发起人" },
      ],
    };
  },
  components: { page, PageHeader },
  activated() {
    this.dataFot();
  },
  created() {
    this.dataFot();
  },
  methods: {
    formatTime(row, col, val) {
      // 表格时间格式化
      return timeFormat(val);
    },
    checkStatus(row, col, val) {
      switch (val) {
        case 0:
          return "审批中";
        case 1:
          return "审批通过";
        case 2:
          return "审批未通过";
      }
    },
    formatType(row, col, val) {
      // 申请类型
      switch (val) {
        case 1:
          return "促销活动";
          break;
        case 2:
          return "拼团秒杀活动";
          break;
        case 3:
          return "优惠券";
          break;
        case 4:
          return "代金券创建审批";
          break;
        default:
          return "-";
      }
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.dataFot();
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot();
    },

    dataFot() {
      // 数据列表
      this.loading = true;
      this.$http.promotionCheckList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.loading = false;
        } else {
          this.loading = false;
        }
      });
    },
    // 搜索列表
    Csearch() {
      this.pageInfo.page = 1;
      this.dataFot();
    },
    // 输入框输入搜索列表
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    handleSee(row) {
      // 查看按钮
      this.$router.push({
        name: "voucherCheckDetail",
        query: { id: row.id, checkMsg: 4 },
      });
    },
  },
  watch: {
    "pageInfo.labelType": {
      handler(val) {
        if (val === 2) {
          this.pageInfo.checkStatus = 0;
        } else {
          this.pageInfo.checkStatus = undefined;
        }
        this.dataFot();
      },
      deep: true,
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}
.check-list {
  background-color: white;
  min-height: 100%;
  .sales-promotion-check {
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
  }
  .nav-bar {
    padding: 10px;
  }
  .Fsearch {
    overflow: hidden;
    float: right;
    .box-input {
      width: 215px;
      float: right;
      /deep/ .el-input__inner {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
      }
    }
    .content_select {
      /deep/ .el-input__inner {
        border-right: 0;
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
    }
    .box-btn {
      float: right;
      margin-left: 5px;
    }
  }
}
</style>