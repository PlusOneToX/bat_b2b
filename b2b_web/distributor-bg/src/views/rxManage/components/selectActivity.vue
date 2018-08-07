<!--
 * @Author: yaowei
 * @Date: 2018-05-19 11:36:59
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-16 15:47:00
-->
<template>
  <div class="check-activity">
    <div class="search-wrap">
      <el-button
        style="float: right; margin-left: 10px"
        class="mini-search-btn"
        @click="filter"
        >搜索</el-button
      >
      <el-input
        size="mini"
        style="width: 20%; float: right"
        v-model="pageInfo.codeName"
        placeholder="兑换活动名称"
      ></el-input>
    </div>
    <el-table
      ref="multipleSelect"
      :data="tableData"
      tooltip-effect="dark"
      @select="select"
      @select-all="selectAll"
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
        :selectable="selectable"
      ></el-table-column>
      <el-table-column
        align="center"
        label="兑换活动名称"
        prop="codeName"
      ></el-table-column>
      <!-- <el-table-column
        align="center"
        label="邮费设置"
        prop="id"
      ></el-table-column>
      <el-table-column
        align="center"
        label="兑换卡类型"
        prop="id"
      ></el-table-column>
      <el-table-column
        align="center"
        label="所属分销商"
        prop="id"
      ></el-table-column> -->
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
  props: ["activityData", "selectType"],
  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        type: 1,
        exchangeWay: 1,
        status: 2,
        codeName: "",
      },
      tableData: [],
      total: 0,
      grades: [],
      multipleSelect: [],
      contentType: "",
      content: "",
      isSelect: false,
      selected: [],
      loading: false,
    };
  },
  components: { page },
  created() {
    this.multipleSelect = [];
    this.multipleSelect = this.multipleSelect.concat(this.activityData);
    this.getTableData();
  },
  methods: {
    selectRow() {
      //分销商请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection();
      this.multipleSelect.forEach((row1) => {
        this.tableData.forEach((row2) => {
          if (row1.activityId === row2.activityId) {
            this.$refs.multipleSelect.toggleRowSelection(row2);
          }
        });
      });
    },
    getTableData() {
      this.loading = true;
      this.$http.exchangeCardPoList(this, this.pageInfo).then((res) => {
        this.isSelect = false;
        this.selected = [];
        if (res.success) {
          res.data.list.forEach((item) => {
            this.$set(item, "activityName", item.codeName);
            this.$set(item, "activityId", item.id);
            this.$set(
              item,
              "activityCount",
              item.codeQuantity - item.exchangeQuantity
            );
          });
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.multipleSelect.forEach((row1) => {
            //重新获取数据时，判断哪些选中了
            this.tableData.forEach((row2) => {
              if (row1.activityId == row2.activityId) {
                this.$refs.multipleSelect.toggleRowSelection(row2);
                this.selected.push(row2);
              }
            });
          });
        }
        res.success ? (this.loading = false) : (this.loading = false);
      });
    },
    handleSubmit() {
      this.$emit("disSubmit", this.multipleSelect);
    },
    handleCancel() {
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.activityData);
      this.$emit("disClose");
      this.selectRow();
    },
    selectable(row) {
      if (this.selectType === "electric") {
        if (row.isEntity === 1) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }
    },
    select(selection, row) {
      //单选时调用
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].activityId === row.activityId) {
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
    selectAll(selection) {
      //全选时调用
      this.isSelect = true;
      if (selection.length === 0) {
        this.tableData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].activityId === row.activityId) {
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
    handleSelectionChange(val) {
      //当切换页面时的作用
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.tableData.forEach((row2) => {
            if (row1.activityId === row2.activityId) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    //去重
    setArr(arr) {
      let obj = {};
      let temp = [];
      for (let i = 0; i < arr.length; i++) {
        let type = Object.prototype.toString.call(arr[i].activityId); //不加类型 分不清 1 '1'
        if (!obj[arr[i].activityId + type]) {
          temp.push(arr[i]);
          obj[arr[i].activityId + type] = true; //这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
    filter() {
      this.pageInfo.content = this.content;
      this.pageInfo.contentType = this.contentType;
      this.pageInfo.page = 1;
      this.getTableData();
    },
    // 分页页数
    sizeChange(size) {
      this.pageInfo.size = size;
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
    activityData: {
      //请求的分销商数据变化时
      handler() {
        this.multipleSelect = [];
        this.multipleSelect = this.multipleSelect.concat(this.activityData);
        this.selectRow();
      },
      deep: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.check-activity {
  background-color: white;
  min-height: 100%;
  .search-wrap {
    margin-bottom: 10px;
    height: 40px;
    .mini-search-btn {
      margin-top: 6px;
    }
  }
  .header-row {
    @include table-header-color;
  }
  .foot-btn {
    margin-top: 50px;
  }
}
</style>
