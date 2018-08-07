<!--
 * @Author: yaowei
 * @Date: 2018-05-25 16:55:36
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-27 10:52:14
-->
<template>
  <div class="check-pic">
    <div style="margin-bottom: 20px">
      <el-select
        size="mini"
        v-model="pageInfo.type"
        placeholder="图库类型"
        style="width: 120px"
        clearable
      >
        <el-option
          v-for="(item, index) in themeTitle"
          :key="index"
          :label="item.name"
          :value="item.id"
        ></el-option>
      </el-select>

      <el-button
        style="float: right; margin-bottom: 10px; margin-left: 10px"
        class="mini-search-btn"
        @click="filter"
        >搜索</el-button
      >
      <el-input
        size="mini"
        style="width: 20%; float: right; margin-bottom: 10px; line-height: 0px"
        v-model="pageInfo.content"
        placeholder="图片分类名称/编码"
      ></el-input>
    </div>
    <el-table
      ref="multipleSelect"
      :data="tableData"
      tooltip-effect="dark"
      row-key="id"
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
      ></el-table-column>
      <el-table-column align="center" label="编码" prop="id"></el-table-column>
      <el-table-column
        align="center"
        label="图片分类名称"
        prop="name"
      ></el-table-column>
      <el-table-column align="center" label="分类图片">
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
            :src="scope.row.image"
            :preview-src-list="[scope.row.image]"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column align="center" label="图片类型" prop="type">
        <template slot-scope="scope">
          <span v-if="Number(scope.row.type) === 1">普通素材</span>
          <span v-else-if="Number(scope.row.type) === 2">IP素材</span>
          <span v-else-if="Number(scope.row.type) === 3">模板</span>
          <span v-else-if="Number(scope.row.type) === 4">贴图</span>
        </template>
      </el-table-column>
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
import { getCategory } from "@/views/storeLayout/rxData";

export default {
  props: ["categoryData"],
  data() {
    return {
      // 主题类型
      themeTitle: [
        {
          name: "全部",
          id: "",
        },
        {
          name: "普通素材",
          id: 1,
        },
        {
          name: "IP素材",
          id: 2,
        },
        {
          name: "模板",
          id: 3,
        },
        {
          name: "贴图",
          id: 4,
        }
      ],
      pageInfo: {
        type: "",
        content: "",
        page: 1,
        size: 10,
        parentId: 0
      },
      tableData: [],
      total: 0,
      multipleSelect: [],
      isSelect: false,
      selected: [],
      loading: false,
      pictureCategoryList: [],
    };
  },
  components: { page },
  created() {
    this.multipleSelect = [];
    this.multipleSelect = this.multipleSelect.concat(this.categoryData);
    this.getTableData();
  },
  activated() {
    this.getTableData();
  },
  methods: {
    selectRow() {
      //分销商请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection();
      this.multipleSelect.forEach((row1) => {
        this.tableData.forEach((row2) => {
          if (row1.id === row2.id) {
            this.$refs.multipleSelect.toggleRowSelection(row2);
          }
        });
      });
    },
    getTableData() {
      this.loading = true;
      // getCategory(this, this.pageInfo).then((res) => {
      this.$http.pictureCategoryPoList(this, this.pageInfo).then(res => {  
        this.isSelect = false;
        this.selected = [];

        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading = false;

          this.multipleSelect.forEach((row1) => {
            //重新获取数据时，判断哪些选中了
            this.tableData.forEach((row2) => {
              if (row1.id === row2.id) {
                this.$refs.multipleSelect.toggleRowSelection(row2);
                this.selected.push(row2);
              }
            });
          });
        }
      });
    },
    handleSubmit() {
      this.$emit("picSubmit", this.multipleSelect);
    },
    handleCancel() {
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.categoryData);
      this.$emit("picClose");
      this.selectRow();
    },
    select(selection, row) {
      //单选时调用
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
    selectAll(selection) {
      //全选时调用
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
    handleSelectionChange(val) {
      //当切换页面时的作用
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
    categoryData: {
      //请求的分销商数据变化时
      handler() {
        this.multipleSelect = [];
        this.multipleSelect = this.multipleSelect.concat(this.categoryData);
        this.selectRow();
      },
      deep: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.check-pic {
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
</style>
