<!--
 * @Author: yaowei
 * @Date: 2018-05-25 15:38:11
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-25 15:59:22
-->
<template>
  <div class="check-distributor">
    <div>
      <el-button
        style="float: right; margin-bottom: 10px; margin-left: 10px"
        class="mini-search-btn"
        @click="filter"
        >搜索</el-button
      >
      <el-input
        size="mini"
        style="width: 20%; float: right; margin-bottom: 10px; line-height: 0px"
        v-model="content"
        placeholder="分销商用户名/公司名/分销商等级"
      ></el-input>
    </div>
    <el-table
      ref="singleSelect"
      :data="tableData"
      tooltip-effect="dark"
      style="text-align: center"
      border
      header-row-class-name="header-row"
      @selection-change="handleSelectionChange"
      v-loading="loading"
    >
      <el-table-column
        align="center"
        type="selection"
        with="100"
      ></el-table-column>
      <el-table-column
        align="center"
        label="分销商用户名"
        prop="name"
      ></el-table-column>
      <el-table-column
        align="center"
        label="分销商等级"
        prop="gradeName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="公司名"
        prop="companyName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="手机号"
        prop="mobile"
      ></el-table-column>
    </el-table>
    <page
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>
    <el-button
      style="margin-left: 46%; margin-top: 10px"
      class="mini-search-btn"
      @click="handleSubmit"
      >确定</el-button
    >
    <el-button size="mini" style="margin-top: 10px" @click="handleCancel"
      >返回</el-button
    >
  </div>
</template>
<script>
import page from "@/components/pagination";
export default {
  props: ["distributorData"],
  data() {
    return {
      pageInfo: {
        page: 1,
        count: 10,
        freezeStatus: 2,
        capitalStatus: 2,
      },
      tableData: [],
      total: 0,
      grades: [],
      singleSelect: [],
      content: "",
      isSelect: false,
      selected: [],
      loading: false,
    };
  },
  components: { page },
  created() {
    this.singleSelect = [];
    this.singleSelect = this.singleSelect.concat(this.distributorData);
    this.getTableData();
  },
  methods: {
    selectRow() {
      //分销商请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.singleSelect.clearSelection();
      this.singleSelect.forEach((row1) => {
        this.tableData.forEach((row2) => {
          if (row1.id === row2.id) {
            this.$refs.singleSelect.toggleRowSelection(row2);
          }
        });
      });
    },
    getTableData() {
      this.loading = true;
      if (this.grades.length == 0) {
        this.$http.getGradePoList(this, { page:1, size:10000, openFlag:1 }).then(res => {  
            if (res.success) {
              this.grades = res.data.list;
            }
          });
      }
      this.$api
        .get(this, "admin/u/po/distributor/cooperating/list", this.pageInfo)
        .then((res) => {
          this.isSelect = false;
          this.selected = [];
          if (res.code == 0) {
            res.distributors.forEach((item) => {
              this.$set(item, "distributorName", item.name);
              this.$set(item, "distributorId", item.id);
              this.grades.forEach((val) => {
                if (val.id == item.gradeId) {
                  item.gradeName = val.name;
                }
              });
            });
            this.tableData = res.distributors;
            this.singleSelect.forEach((row1) => {
              //重新获取数据时，判断哪些选中了
              this.tableData.forEach((row2) => {
                if (row1.id === row2.id) {
                  this.$refs.singleSelect.toggleRowSelection(row2);
                  this.selected.push(row2);
                }
              });
            });
          }
          res.code == 0 ? (this.loading = false) : (this.loading = false);
        });
      this.$api
        .get(this, "admin/u/po/distributor/cooperating/count", this.pageInfo)
        .then((res) => {
          if (res.code == 0) {
            this.total = res.count;
          }
        });
    },
    handleSubmit() {
      this.$emit("submit", this.singleSelect);
    },
    handleCancel() {
      this.singleSelect = [];
      this.singleSelect = this.singleSelect.concat(this.distributorData);
      this.$emit("cancel");
      this.selectRow();
    },
    handleSelectionChange(selection) {
      //当切换页面时的作用
      this.singleSelect = selection[0];
      if (selection.length > 1) {
        this.$refs.singleSelect.clearSelection();
        this.$refs.singleSelect.toggleRowSelection(selection.pop());
      }
    },
    //去重
    setArr(arr) {
      let obj = {};
      let temp = [];
      for (let i = 0; i < arr.length; i++) {
        let type = Object.prototype.toString.call(arr[i].id); //不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; //这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
    filter() {
      this.pageInfo.content = this.content;
      this.pageInfo.page = 1;
      this.getTableData();
    },
    // 分页页数
    sizeChange(size) {
      this.pageInfo.count = size;
    },
    // 分页总数
    currentChange(page) {
      this.pageInfo.page = page;
    },
  },
  watch: {
    pageInfo: {
      handler() {
        this.getTableData();
      },
      deep: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.check-distributor {
  background-color: white;
  min-height: 100%;
  .header-row {
    @include table-header-color;
  }
  .foot-btn {
    margin-top: 50px;
    /* margin-bottom:20px; */
  }
}
/deep/ .el-table {
  .el-table__header-wrapper .el-checkbox {
    //找到表头那一行，然后把里面的复选框隐藏掉
    display: none;
  }
}
</style>
