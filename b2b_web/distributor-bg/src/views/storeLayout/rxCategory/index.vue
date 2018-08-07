<!--
 * @Author: yaowei
 * @Date: 2018-05-19 10:02:48
 * @LastEditors: litian
 * @LastEditTime: 2018-06-28 10:35:07
-->
<template>
  <div class="rx-wrap">
    <header>
      <h4>系列展示管理</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleCategory(1)"
        >新增系列展示</el-button
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
            v-model="pageInfo.pictureCategoryId"
            placeholder="图片分类"
            style="width: 120px"
            @change="Csearch()"
            clearable
          >
            <el-option
              v-for="item in pictureCategoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="pageInfo.themeId"
            placeholder="主题分类"
            style="width: 120px"
            @change="Csearch()"
            clearable
          >
            <el-option
              v-for="(item, idx) in themeNameList"
              :key="idx"
              :label="item.themeName"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <!-- <el-select
            v-model="pageInfo.sortNoType"
            size="mini"
            placeholder="排序"
            style="width: 200px"
            @change="Csearch()"
            clearable
          >
            <el-option label="A级 - 置顶" :value="1"></el-option>
            <el-option label="B级 - 第二位" :value="2"></el-option>
            <el-option label="C级 - 随机" :value="3"></el-option>
          </el-select> -->
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
          label="主题名称"
          align="center"
          prop="themeName"
          width="100"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="图片分类名称"
          align="center"
          prop="categoryName"
          width="100"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="图片ID"
          align="center"
          prop="pictureId"
          width="100"
        ></el-table-column>
        <el-table-column
          label="图片编码"
          align="center"
          prop="code"
          width="100"
          show-overflow-tooltip
        >
        <template slot-scope="scope">
          <div v-if="scope.row.code">{{scope.row.code}}</div>
          <div v-else>--</div>
        </template>
        </el-table-column>
        <el-table-column
          label="图片名称"
          align="center"
          prop="pictureName"
          width="100"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="图片英文名称"
          align="center"
          prop="englishName"
          :min-width="170"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="分销商名称"
          align="center"
          prop="distributorName"
          :min-width="170"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="公司名称"
          align="center"
          prop="companyName"
          :min-width="170"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="产品图" align="center" :min-width="170">
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
              :src="
                scope.row.thumbnail !== undefined &&
                scope.row.thumbnail !== null &&
                scope.row.thumbnail !== ''
                  ? scope.row.thumbnail
                  : scope.row.originImage +
                    '?x-oss-process=image/resize,h_60,l_60'
              "
              :preview-src-list="[
                scope.row.thumbnail !== undefined &&
                scope.row.thumbnail !== null &&
                scope.row.thumbnail !== ''
                  ? scope.row.thumbnail
                  : scope.row.originImage,
              ]"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column
          label="适用货品"
          align="center"
          prop="itemCode"
          width="150"
        >
        </el-table-column>
        <el-table-column label="操作" :min-width="270" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-browse-btn"
              @click="handleMove(scope.row.id, true)"
              >上移</el-button
            >
            <el-button
              class="mini-pulloff-btn"
              @click="handleMove(scope.row.id, false)"
              >下移</el-button
            >
            <el-button
              class="mini-search-btn"
              @click="handleCategory(3, scope.row.seriesId)"
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
import {
  themeName,
  categoryList,
  moveCategory,
  deleteCategory,
  batchDeleteCategory,
} from "@/views/storeLayout/rxData";
export default {
  name: "rxCategory",
  components: { page },
  data() {
    return {
      pageInfo: {
        pictureCategoryId: "",
        themeId: "",
        // sortNoType: 1,
        content: "",
        size: 10,
        page: 1,
      },
      tableData: [],
      total: 0,
      pictureCategoryList: [], // 图片分类
      themeNameList: [], // 主题名称
      multipleSelect: [],
      loading: false,
    };
  },
  mounted() {
    this.getPictureCategory();
    this.getThemeName();
    this.initData();
  },
  methods: {
    // 图片分类
    getPictureCategory() {
      //pictureCategoryPoList
      this.$http.pictureCategoryPoList(this, {
        page:1,
        size:1000, 
        atLastTrademark: 1
        // parentId: 0
      })
      .then((res) => {
        this.pictureCategoryList = res.data.list;
      });
    },
    // 主题名称
    getThemeName() {
      // themeName(this, {
      this.$http.disThemeListByCondition(this).then((res) => {
        if (res.success) {
          this.themeNameList = res.data;
        }
      });
    },
    initData() {
      this.loading = true;
      // categoryList(this, this.pageInfo).then((res) => {
      this.$http.disThemeList(this, this.pageInfo).then((res) => {  
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
    handleCategory(type, id) {
      var query = {};
      if (id) {
        query = { checkMsg: type, id: id };
      } else {
        query = { checkMsg: type };
      }
      this.$router.push({
        name: "categoryDetail",
        query: query,
      });
    },
    handleMove(id, upFlag) {
      // upFlag： true 上移，false 下移
      // moveCategory(this, { id: id, upFlag: upFlag }).then((res) => {
      this.$http.picRelevanceMove(this, { id: id, flag: upFlag }).then((res) => { 
        if (res.success) {
          this.$message({
            type: "success",
            message: upFlag ? "上移成功" : "下移成功",
          });
          this.initData();
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
    handleDelete(id) {
      this.$confirm("此操作将删除该系列展示图片，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          // deleteCategory(this, id).then((res) => {
          this.$http.delPicRelevance(this, {id:id}).then((res) => {  
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
        this.$confirm("此操作将删除已选系列展示图片，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then((_) => {
            // batchDeleteCategory(this, { idList: idList }).then((res) => {
            this.$http.batchPicRelevance(this, { idList: idList }).then((res) => {
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