<!--
 * @Author: yaowei
 * @Date: 2018-05-19 10:02:25
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-09 09:29:09
-->
<template>
  <div class="rx-wrap">
    <header>
      <h4>轮播图管理</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleBanner(1)"
        >新增轮播图</el-button
      >
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <button class="mini-delete-btn box-btn" @click="handleBatchDelete()">
            删除
          </button>
          <el-select
            size="mini"
            v-model="pageInfo.showLocation"
            placeholder="运营位"
            style="width: 150px"
            @change="Csearch()"
            clearable
          >
            <el-option label="首页主banner位" :value="1"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.status"
            placeholder="状态"
            style="width: 120px"
            @change="Csearch()"
            clearable
          >
            <el-option label="未开始" :value="0"></el-option>
            <el-option label="展示中" :value="1"></el-option>
            <el-option label="已结束" :value="2"></el-option>
            <!-- <el-option label="长期展示" :value="3"></el-option> -->
          </el-select>
        </div>
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
            placeholder="请输入分销商名称/公司名称"
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
        ref="multipleSelect"
        @select="select"
        @select-all="selectAll"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="50"
          reserve-selection
        ></el-table-column>
        <el-table-column
          label="分销商名称"
          align="center"
          prop="distributorName"
          width="80"
        ></el-table-column>
        <el-table-column
          label="公司名称"
          align="center"
          prop="companyName"
          width="80"
        ></el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template slot-scope="scope">
            <span v-if="Number(scope.row.status) === 0">未开始</span>
            <span v-if="Number(scope.row.status) === 1">展示中</span>
            <span v-if="Number(scope.row.status) === 2">已结束</span>
          </template>
        </el-table-column>
        <el-table-column
          label="所属位置"
          align="center"
          prop="showLocation"
          width="100"
        >
        </el-table-column>
        <el-table-column
          label="展示开始时间"
          align="center"
          prop="startTime"
          :min-width="170"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="展示结束时间"
          align="center"
          prop="endTime"
          :min-width="170"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="图片"
          align="center"
          :min-width="170"
          show-overflow-tooltip
        >
          <template
            slot-scope="scope"
            style="text-align: center; width: 60px; height: 60px"
          >
            <el-image
              style="
                text-align: center;
                width: 60px;
                height: 60px;
                padding-right: 0px;
              "
              fit="contain"
              :src="scope.row.bannerUrl"
              :preview-src-list="[scope.row.bannerUrl]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column
          label="备注"
          align="center"
          prop="remark"
          width="150"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="操作" :min-width="160" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="handleBanner(3, scope.row.id)"
              >编辑</el-button
            >
            <el-button
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
import { timeFormat } from "@/utils/timeFormat.js";
import {
  bannerList,
  deleteBanner,
  batchDeleteBanner,
} from "@/views/storeLayout/rxData";
export default {
  name: "rxBanner",
  components: { page },
  data() {
    return {
      pageInfo: {
        showLocation: 1,
        status: 1,
        content: "",
        size: 10,
        page: 1,
      },
      tableData: [],
      total: 0,
      multipleSelect: [],
      loading: false,
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      this.loading = true;
      // bannerList(this, this.pageInfo).then((res) => {
      this.$http.disBannerList(this, this.pageInfo).then(res => {  
        this.loading = false;
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          res.data.list.forEach((item) => {
            item.startTime = timeFormat(item.startTime);
            item.endTime = timeFormat(item.endTime);
          });
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
    handleBanner(type, id) {
      var query = {};
      if (id) {
        query = { checkMsg: type, id: id };
      } else {
        query = { checkMsg: type };
      }
      this.$router.push({
        name: "bannerDetail",
        query: query,
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
    handleDelete(id) {
      this.$confirm("此操作将删除该轮播图，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http.delDisBanner(this, {id: id}).then(res => {  
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
    handleBatchDelete() {
      let idList = [];
      for (let i = 0; i < this.multipleSelect.length; i++) {
        idList.push(this.multipleSelect[i].id);
      }
      if (idList.length > 0) {
        this.$confirm("此操作将删除已选轮播图，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then((_) => {
            // batchDeleteBanner(this, { idList: idList }).then((res) => {
            this.$http.batchDelDisBanner(this, { idList: idList }).then(res => {    
              if (res.success) {
                this.multipleSelect = [];
                this.$refs.multipleSelect.clearSelection();
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
      } else {
        this.$message({
          type: "error",
          message: "未勾选图片",
        });
      }
    },
    // 单选时调用
    select(selection, row) {
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].id === row.id) {
          this.multipleSelect.splice(i, 1);
          d = true;
          break;
        }
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
    },
    // 全选时调用
    selectAll(selection) {
      this.isSelect = true;
      if (selection.length === 0) {
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
    // 当切换页面时的作用
    handleSelectionChange(val) {
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    setArr(arr) {
      // 去重
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
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