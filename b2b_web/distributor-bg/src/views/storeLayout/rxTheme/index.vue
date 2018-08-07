<!--
 * @Author: yaowei
 * @Date: 2018-05-25 14:50:06
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-09 01:08:46
-->
<template>
  <div class="rx-wrap">
    <header>
      <h4>官方主题配置</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleTheme(1)"
        >创建主题项目</el-button
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
            placeholder="请输入主题名称/适用分销商"
            class="box-input"
          ></el-input>
        </div>
      </div>
    </div>

    <el-row v-loading="loading">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        row-key="id"
        border
        class="tableCenter"
      >
        <el-table-column
          label="主题ID"
          align="center"
          prop="id"
        ></el-table-column>
        <el-table-column
          label="主题项目名称"
          align="center"
          prop="name"
        ></el-table-column>
        <el-table-column
          label="适用分销商"
          align="center"
          prop="distributorName"
        ></el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <span v-if="Number(scope.row.openFlag) === 0">停用</span>
            <span v-if="Number(scope.row.openFlag) === 1">启用</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="160" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-browse-btn"
              @click="handleTheme(3, scope.row.id)"
              >编辑</el-button
            >
            <el-button
              class="mini-search-btn"
              @click="handleTheme(2, scope.row.id)"
              >关联主题</el-button
            >
            <el-button
              v-show="Number(scope.row.distributorId) !== 2601"
              :class="
                Number(scope.row.openFlag) === 1
                  ? 'mini-freeze-btn'
                  : 'mini-browse-btn'
              "
              @click="handleThemeStatus(scope.row)"
              >{{ Number(scope.row.openFlag) === 1 ? "停用" : "启用" }}</el-button
            >
            <el-button
              v-show="Number(scope.row.distributorId) !== 2601"
              class="mini-delete-btn"
              @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
import page from "@/components/pagination";
import {
  getThemeList,
  editTheme,
  deleteCTheme,
} from "@/views/storeLayout/rxData";

export default {
  name: "rxTheme",
  components: { page },
  data() {
    return {
      pageInfo: {
        content: "",
        size: 10,
        page: 1,
      },
      tableData: [],
      total: 0,
      loading: false,
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      this.loading = true;
      this.$http.pictureThemeList(this, this.pageInfo).then(res => {  
        this.loading = false;
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    Csearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    handleTheme(type, id) {
      if (type === 1) {
        // 创建主题项目
        this.$router.push({
          name: "addTheme",
        });
      } else {
        this.$router.push({
          name: "bindTheme",
          query: {
            id: id,
          },
        });
      }
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
    // 停用/启用
    handleThemeStatus(item) {
      let msg = "";
      if (item.openFlag === 1) {
        msg = "此操作将停用该主题，是否继续？";
      } else {
        msg = "此操作将启用该主题，是否继续？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          // editTheme(this, {
          this.$http.editPictureTheme(this, {  
            id: item.id,
            name: item.name,
            distributorId: item.distributorId,
            distributorName: item.distributorName,
            openFlag: item.openFlag === 1 ? 0 : 1,
          }).then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: item.openFlag === 1 ? "停用成功" : "启用成功",
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
      this.$confirm("此操作将删除该主题，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          // deleteCTheme(this, id).then((res) => {
          this.$http.delPictureTheme(this, {id: id}).then((res) => {   
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

.rx-wrap {
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
  justify-content: flex-end;
  align-items: center !important;
  .Fsearch {
    overflow: hidden;
    float: right;
    .box-input {
      width: 300px;
      float: right;
    }
    .box-btn {
      float: right;
      margin-left: 5px;
    }
  }
}
</style>