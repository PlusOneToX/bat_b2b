<!--
 * @Author: yaowei
 * @Date: 2018-06-11 09:48:32
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-18 09:10:53
-->

<template>
  <div class="platform-list">
    <header>
      <h4>微信公众平台列表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleAdd()"
      >
        添加微信公众平台
      </el-button>
    </header>
    <div class="platform-header">
      <el-select
        size="mini"
        v-model="pageInfo.type"
        placeholder="平台类型"
        style="width: 140px"
        @change="filter()"
        clearable
      >
        <el-option label="公众号" :value="1"> </el-option>
        <el-option label="小程序" :value="2"> </el-option>
      </el-select>
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
          class="box-input"
          size="mini"
          clearable
          @change="contentChange"
          placeholder="请输入关键字搜索"
          v-model.trim="pageInfo.content"
        ></el-input>
        <button
          class="mini-search-btn box-btn"
          size="mini"
          @click.prevent="filter()"
        >
          搜索
        </button>
      </div>
    </div>
    <el-table
      :data="tableData"
      border
      v-loading="loading"
      header-row-class-name="header-row"
      class="tableCenter"
    >
      <el-table-column
        align="center"
        label="平台编码"
        prop="platform"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="类型"
        prop="type"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="scope.row.type === 1">公众号</span>
          <span v-else-if="scope.row.type === 2">小程序</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="公众平台名称"
        prop="name"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="Appid"
        prop="appId"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="更新时间"
        prop="updateTime"
        :formatter="formatTime"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button
            class="mini-search-btn"
            @click="handleEdit(scope.$index, scope.row)"
            >查看</el-button
          >
          <el-button
            class="mini-delete-btn"
            @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button
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

<script>
import { timeFormat } from "@/utils/timeFormat";
import page from "@/components/pagination";
export default {
  name: "rolelist",
  data() {
    return {
      tableData: [],
      total: 0,
      pageInfo: {
        page: 1,
        size: 10,
        contentType: 1,
        type: 1,
        content: "",
      },
      loading: false,
    };
  },
  components: {
    page,
  },
  created() {
    this.init();
  },
  methods: {
    formatTime(row, col, val) {
      // 表格时间格式化
      return timeFormat(val);
    },
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.init();
      }
    },
    // ======== 操作 ========
    filter() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.init();
    },

    handleEdit(index, row) {
      // 查看操作
      this.$router.push({
        name: "editAddWxPlatform",
        query: { id: row.id },
      });
    },

    handleAdd() {
      // 添加操作
      this.$router.push({ name: "editAddWxPlatform" });
    },

    handleDelete(index, row) {
      // 删除操作
      this.$confirm("此操作将永久删除该微信账户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$http
            .deleteWxPlatform(this, {
              id: row.id,
            })
            .then((res) => {
              if (res.success) {
                this.$message({
                  message: "删除成功",
                  type: "success",
                  duration: 3 * 1000,
                });
                this.pageInfo.page = 1;
                this.init();
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    // ======== 数据 ========
    init() {
      // 列表数据
      this.loading = true;
      this.$http.getWxPlatformList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading = false;
        }
      });
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.init();
    },

    currentChange(page) {
      this.pageInfo.page = page;
      this.init();
    },
  },
  watch: {},
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