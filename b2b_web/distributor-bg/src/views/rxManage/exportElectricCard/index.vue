<!--
 * @Author: yaowei
 * @Date: 2018-05-16 14:42:10
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-24 15:22:14
-->
<template>
  <div class="list-wrap">
    <header>
      <h4>电子兑换卡导出记录表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleList(1)"
        >创建导出记录单</el-button
      >
    </header>

    <div class="function">
      <div class="Fheader">
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
            placeholder="请输入兑换活动名称"
            class="box-input"
          ></el-input>
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
          label="记录单号"
          align="center"
          prop="id"
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="记录单名称"
          align="center"
          prop="exportName"
          show-overflow-tooltip
          :min-width="150"
        ></el-table-column>
        <el-table-column
          label="分销商"
          align="center"
          prop="distributorName"
          show-overflow-tooltip
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="使用/出库数量"
          align="center"
          show-overflow-tooltip
          :min-width="150"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.hasUseNum + "/" + scope.row.outNum }}</span>
          </template></el-table-column
        >
        <el-table-column
          label="邮费设置"
          align="center"
          show-overflow-tooltip
          :min-width="90"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.mailSetting === 1">包邮</span>
            <span v-if="scope.row.mailSetting === 2">收运费（赠卡模式）</span>
            <!-- <span v-if="scope.row.mailSetting === 3">收运费（卖卡模式）</span> -->
          </template>
        </el-table-column>
        <el-table-column
          label="卡片属性"
          align="center"
          prop="groupSeckillDesc"
          show-overflow-tooltip
          :min-width="90"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.isEntity === 1">实体卡</span>
            <span v-else>电子卡</span>
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column label="状态" align="center" width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.examineFlag === 1">已审核</span>
            <span v-else>待审核</span>
          </template>
        </el-table-column>
        <el-table-column
          label="备注"
          align="center"
          prop="remark"
          show-overflow-tooltip
          :min-width="200"
        >
        </el-table-column>
        <el-table-column label="操作" :min-width="260" align="center">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleList(2, scope.row)"
              >查看</el-button
            >
            <el-button
              v-if="Number(scope.row.examineFlag) === 1"
              class="mini-freeze-btn"
              @click="handleExport(scope.row.id, scope.row.exportName)"
              >导出</el-button
            >
            <el-button
              v-else
              class="mini-tableadd-btn"
              @click="handleCheck(scope.row.id)"
              >审核</el-button
            >
            <el-button
              class="mini-search-btn"
              v-if="Number(scope.row.examineFlag) === 1"
              @click="handleCodeShow(scope.row.id)"
              >码库</el-button
            >
            <el-button
              class="mini-delete-btn"
              v-else
              @click="handleDelete(scope.row.id)"
              >删除</el-button
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

    <!-- 码库 -->
    <el-dialog
      class="code-wrap"
      width="70%"
      :visible.sync="codeShow"
      v-if="codeShow"
    >
      <codeList
        :exchangeExportId="curActivityId"
        @handleInit="initData"
      ></codeList>
    </el-dialog>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import codeList from "@/views/marketingPromotion/exchange/components/codeList";
export default {
  name: "exchangeExport",
  components: {
    page,
    codeList,
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        content: undefined,
      },
      tableData: [], // 列表
      total: "", // 列表总数
      codeShow: false, // 码库弹窗
      curActivityId: "", // 当前记录表单号
    };
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
  methods: {
    // 初始化列表数据
    initData() {
      this.$http.exchangeExportList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;

          // 分销商详情
          if (res.data.list && res.data.list.length > 0) {
            res.data.list.forEach((item) => {
              let ids = [item.distributorId];
              this.getDistributorByIds(ids, item);
            });
          }
        }
      });
    },
    // 分销商详情
    getDistributorByIds(ids, item) {
      this.$http.getDistributorByIds(this, { ids: ids }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let distri = "";
            res.data.forEach((item) => {
              distri += item.name + ",";
            });

            this.$set(
              item,
              "distributorName",
              distri.substring(0, distri.length - 1)
            );
          }
        }
      });
    },
    // 列表条数
    sizeChange(size) {
      this.pageInfo.count = size;
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
    // 添加/查看/编辑 - （1，添加，2，查看/编辑）
    handleList(checkMsg, row) {
      var query = {};
      if (row) {
        query = {
          checkMsg: checkMsg,
          row: JSON.stringify(row),
        };
      } else {
        query = {
          checkMsg: checkMsg,
        };
      }
      this.$router.push({
        name: "exportElectricCardDetail",
        query: query,
      });
    },
    // 导出
    handleExport(id, name) {
      this.exportDownloadLoading = true;
      this.$http.exchangeExportOut(this, { id: id }).then((res) => {
        if (res.success) {
          let url = res.data;
          if ("download" in document.createElement("a")) {
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.download = name + ".xls";
            document.body.appendChild(link);
            link.click();
          }
        }
        this.exportDownloadLoading = false;
      });
    },
    // 审核
    handleCheck(id) {
      this.$http
        .editExchangeExport(this, {
          id: id,
          examineFlag: 1,
        })
        .then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "审核成功",
            });
            this.initData();
          }
        });
    },
    // 删除
    handleDelete(id) {
      this.$confirm("此操作将删除该电子兑换卡导出记录表，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http.deleteExchangeExport(this, { id: id }).then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.initData();
            }
          });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 码库
    handleCodeShow(id) {
      this.codeShow = true;
      this.curActivityId = id;
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
      float: right;
    }
    .box-btn {
      float: right;
      margin-left: 5px;
    }
  }
}
</style>
