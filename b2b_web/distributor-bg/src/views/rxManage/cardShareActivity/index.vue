<!--
 * @Author: yaowei
 * @Date: 2018-05-16 14:42:10
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-07 10:46:59
-->
<template>
  <div class="list-wrap">
    <header>
      <h4>转发活动配置</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleList(1)"
        >创建转发活动</el-button
      >
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <el-select
            size="mini"
            v-model="pageInfo.activityPlatform"
            placeholder="活动平台"
            style="width: 120px"
            @change="Csearch()"
            clearable
          >
            <el-option label="兑换商城" :value="1"></el-option>
            <el-option label="定制商城" :value="2"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.seat"
            placeholder="活动位置"
            style="width: 120px"
            @change="Csearch()"
            clearable
          >
            <el-option label="确认订单页" :value="1"></el-option>
            <el-option label="订单详情" :value="2"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="pageInfo.preferName"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入活动名称"
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
          label="转发活动ID"
          align="center"
          prop="id"
          :min-width="110"
        ></el-table-column>
        <el-table-column
          label="转发活动名称"
          align="center"
          prop="preferName"
          show-overflow-tooltip
          :min-width="150"
        ></el-table-column>
        <el-table-column
          label="活动所属平台"
          align="center"
          show-overflow-tooltip
          :min-width="120"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.activityPlatform === 1">兑换商城</span>
            <span v-else-if="scope.row.activityPlatform === 2">定制商城</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="活动位置"
          align="center"
          show-overflow-tooltip
          :min-width="110"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.seat === 1">确认订单页</span>
            <span v-else-if="scope.row.seat === 2">订单详情</span>
          </template>
        </el-table-column>
        <el-table-column
          label="转发次数"
          align="center"
          prop="forwardNum"
          show-overflow-tooltip
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="自动上线时间"
          align="center"
          prop="startTime"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column
          label="自动下线时间"
          align="center"
          prop="endTime"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column label="活动时效状态" align="center" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.timeStatus === 1">待上线</span>
            <span v-else-if="scope.row.timeStatus === 2">已上线</span>
            <span v-else-if="scope.row.timeStatus === 3">已下线</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.status === 1">启用</span>
            <span v-else>停用</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="260" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="handleList(2, scope.row.id)"
              >查看</el-button
            >
            <el-button
              v-if="Number(scope.row.status) === 1"
              class="mini-freeze-btn"
              @click="handleStatus(scope.row.id, 0)"
              >停用</el-button
            >
            <el-button
              v-else
              class="mini-tableadd-btn"
              @click="handleStatus(scope.row.id, 1)"
              >启用</el-button
            >
            <el-button
              v-if="Number(scope.row.status) !== 1"
              class="mini-delete-btn"
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
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  name: "exchangeShare",
  components: {
    page,
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        activityPlatform: 1, // 活动平台
        seat: "", // 活动位置
        preferName: undefined,
      },
      tableData: [], // 列表
      total: "", // 列表总数
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
      this.$http.exchangeShareList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          let now = new Date();
          for (var i = 0; i < res.data.list.length; i++) {
            this.tableData.forEach((item) => {
              if (item.id === res.data.list[i].id) {
                // 活动时效状态
                let start = new Date(
                  Date.parse(item.startTime.replace(/-/g, "/"))
                );
                let end = new Date(Date.parse(item.endTime.replace(/-/g, "/")));
                if (now < start) {
                  // 待上线
                  this.$set(item, "timeStatus", 1);
                } else if (now > end) {
                  // 已下线
                  this.$set(item, "timeStatus", 3);
                } else {
                  // 已上线
                  this.$set(item, "timeStatus", 2);
                }
              }
            });
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
    handleList(checkMsg, id) {
      var query = {};
      if (id) {
        query = {
          checkMsg: checkMsg,
          id: id,
        };
      } else {
        query = {
          checkMsg: checkMsg,
        };
      }
      this.$router.push({
        name: "cardShareActivityDetail",
        query: query,
      });
    },
    // 启用/停用 （status：1 启用/0 停用）
    handleStatus(id, status) {
      let msg = "";
      if (status === 1) {
        msg = "此操作将启用该转发活动，是否继续？";
      } else if (status === 0) {
        msg = "此操作将暂停该转发活动，是否继续？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http
            .editExchangeShare(this, {
              id: id,
              status: status,
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
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 删除
    handleDelete(id) {
      this.$confirm("此操作将删除该转发活动，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http.deleteExchangeShare(this, { id: id }).then((res) => {
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
