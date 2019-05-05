<!--
 * @Author: yaowei
 * @Date: 2019-10-18 10:22:40
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-01 09:22:00
-->
<template>
  <div class="list-wrap">
    <div class="header-search">
      <div class="header-l">
        <el-button type="primary" @click="handleList(1)">新增管理员</el-button>
      </div>
      <div class="header-r">
        <el-select
          class="search-select"
          v-model="pageInfo.contentType"
          @change="Csearch()"
        >
          <el-option label="账号" :value="1"></el-option>
          <el-option label="姓名" :value="2"></el-option>
          <el-option label="手机号" :value="3"></el-option>
          <!-- <el-option label="邮箱" :value="4"></el-option> -->
        </el-select>
        <el-input
          class="search-input"
          v-model.trim="pageInfo.content"
          clearable
          @change="contentChange"
          @keyup.enter.native="Csearch()"
          placeholder="请输入"
        ></el-input>
        <el-button type="primary" @click="Csearch()">搜索</el-button>
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
          label="账号"
          align="center"
          prop="userName"
          show-overflow-tooltip
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="姓名"
          align="center"
          prop="realName"
          show-overflow-tooltip
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="手机号"
          align="center"
          prop="mobile"
          show-overflow-tooltip
          :min-width="150"
        >
        </el-table-column>
        <!-- <el-table-column
          label="邮箱"
          align="center"
          prop="email"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column> -->
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
            <span v-if="scope.row.openFlag === 1">启用</span>
            <span v-else>禁用</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="200" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleList(2, scope.row.id)"
              >编辑</el-link
            >
            <el-link type="danger" @click="handleDelete(scope.row.id)"
              >删除</el-link
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
import page from "@/components/Pagination";
import { userList, deleteUser } from "@/api/system";

export default {
  name: "AdminList",
  components: {
    page,
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        contentType: 1, // 搜索类型：1 公司名
        content: "",
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
      userList(this.pageInfo).then((res) => {
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
    // 输入框输入搜索列表
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    // 新增/编辑：1 新增，2 编辑
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
        name: "AdminDetail",
        query: query,
      });
    },
    // 删除
    handleDelete(id) {
      this.$confirm("此操作将删除该管理员，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          deleteUser({ id: id }).then((res) => {
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

<style lang="scss" scoped>
.list-wrap {
  padding: 20px 15px;
}

.header-search {
  margin-bottom: 20px;
  overflow: hidden;

  .header-l {
    float: left;
  }

  .header-r {
    float: right;
    overflow: hidden;

    .search-select {
      float: left;
      width: 100px;

      ::v-deep .el-input__inner {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
        border-right: 0;
      }
    }

    .search-input {
      float: left;
      margin-right: 10px;
      width: 220px;

      ::v-deep .el-input__inner {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
      }
    }

    .search-btn {
      float: right;
    }
  }
}
</style>