<!--
 * @Author: yaowei
 * @Date: 2018-06-11 10:36:31
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-18 09:10:42
-->

<template>
  <div class="platform-list">
    <header>
      <h4>自有平台列表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handlePlatform(1)"
        >添加自有平台</el-button
      >
    </header>
    <div class="platform-header">
      <div class="platform-block">
        <el-select
          size="mini"
          v-model="pageInfo.contentType"
          style="width: 140px"
          clearable
        >
          <el-option label="平台名称" :value="1"> </el-option>
          <el-option label="分销商名称" :value="2"> </el-option>
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
        <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
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
          label="平台名称"
          align="center"
          prop="platformName"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="域名或IP"
          align="center"
          prop="hostName"
        ></el-table-column>
        <el-table-column
          label="appId"
          align="center"
          prop="appId"
        ></el-table-column>
        <el-table-column
          label="appKey"
          align="center"
          prop="appKey"
          show-overflow-tooltip
        ></el-table-column>
        <!-- <el-table-column label="更新时间" align="center" width="180">
          <template slot-scope="scope">
            <span v-if="scope.row.updateTime">{{
              formatTime(scope.row, scope.row, scope.row.updateTime)
            }}</span>
            <span v-else>{{
              formatTime(scope.row, scope.row, scope.row.createTime)
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          prop="status"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="scope.row.status">已冻结</span>
            <span v-else>未冻结</span>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" align="center" width="160">
          <template slot-scope="scope">
            <!-- 可编辑 -->
            <el-button
              class="mini-search-btn"
              @click="handlePlatform(3, scope.row.id)"
              >查看</el-button
            >
            <!-- <el-button
              class="mini-browse-btn"
              v-if="Number(scope.row.status) === 0"
              @click="handleStatus(scope.row.id, 1)"
              >解冻</el-button
            >
            <el-button
              class="mini-freeze-btn"
              v-if="Number(scope.row.status) === 1"
              @click="handleStatus(scope.row.id, 0)"
              >冻结</el-button
            > -->
            <el-button
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

<script>
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat";

export default {
  name: "ownPlatformList",
  components: {
    page,
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        // 搜索
        page: 1,
        size: 10,
        content: "",
        contentType: 1,
      },
      tableData: [], // 表格
      total: "", // 总数
    };
  },
  methods: {
    // 表格时间格式化
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    // 初始化数据
    initData() {
      this.$http.getOwnPlatformList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    // 添加/查看/编辑 - 合作平台（1，添加，3，查看/编辑）
    handlePlatform(checkMsg, id) {
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
        name: "ownPlatformDetail",
        query: query,
      });
    },
    // 搜索兑换活动
    Csearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    // 输入框输入搜索兑换活动
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    // 兑换活动列表条数
    sizeChange(size) {
      this.pageInfo.count = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 兑换活动列表页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 删除
    handleDelete(id) {
      this.$confirm("此操作将删除该自有平台，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http.deleteOwnPlatform(this, { id: id }).then((res) => {
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
    // 冻结/解冻（status：1 解冻/0 冻结）
    // handleStatus(id, status) {
    //   let msg = "";
    //   if (status) {
    //     msg = "此操作将解冻该自有平台，是否继续？";
    //   } else {
    //     msg = "此操作将冻结该自有平台，是否继续？";
    //   }
    //   var params = {
    //     id: id,
    //     status: status ? 0 : 1,
    //   };
    //   this.$confirm(msg, "提示", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning",
    //     center: true,
    //   })
    //     .then((_) => {
    //       handlePlatformStatus(this, params).then((res) => {
    //         if (Number(res.code) === 0) {
    //           this.$message({
    //             type: "success",
    //             message: status ? "解冻成功" : "冻结成功",
    //           });
    //           this.initData();
    //         }
    //       });
    //     })
    //     .catch((_) => {
    //       this.$message({
    //         type: "info",
    //         message: "已取消操作",
    //       });
    //     });
    // },
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.platform-list {
  background-color: white;
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
  .header {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #ccc;
    button {
      margin-left: 10px;
    }
  }
  .platform-header {
    width: 100%;
    padding: 10px;
    overflow: hidden;
    .platform-block {
      float: right;
      .box-input {
        width: 215px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
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

.platform-block {
  display: flex;
  font-size: 0;
  /deep/ .el-select {
    .el-input__inner {
      border-right: 0;
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
    }
  }
  /deep/ .box-input {
    margin-right: 5px;
    .el-input__inner {
      border-top-left-radius: 0;
      border-bottom-left-radius: 0;
    }
  }
}
</style>