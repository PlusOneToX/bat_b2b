<!--
 * @Author: yaowei
 * @Date: 2018-05-27 10:32:59
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-03 16:29:25
-->
<template>
  <div class="check-pic">
    <div style="margin-bottom: 20px">
      <el-select
        v-show="comingFlag !== undefined && comingFlag !== 'rxCategory'"
        size="mini"
        v-model="pageInfo.type"
        placeholder="图库类型"
        style="width: 120px"
        clearable
        @change="changeSelect"
      >
        <el-option label="普通素材" :value="1"></el-option>
        <el-option label="IP素材" :value="2"></el-option>
        <el-option label="模板" :value="3"></el-option>
        <el-option label="贴图" :value="4"></el-option>
      </el-select>
      <el-select
        v-if="!categoryId"
        v-model="pageInfo.categoryId"
        placeholder="图片分类"
        filterable
        clearable
        size="mini"
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
        v-model="pageInfo.openFlag"
        placeholder="图片状态"
        style="width: 120px"
        clearable
      >
        <el-option label="启用" :value="1"></el-option>
        <el-option label="禁用" :value="0"></el-option>
      </el-select>
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
      <el-table-column
        align="center"
        label="图片ID"
        prop="pictureId"
      ></el-table-column>
      <el-table-column
        align="center"
        label="图片编码"
        prop="code"
      ></el-table-column>
      <el-table-column
        align="center"
        label="图片名称"
        prop="pictureName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="图片英文名称"
        prop="englishName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="产品图">
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
      <el-table-column align="center" label="图片状态" prop="openFlag">
        <template slot-scope="scope">
          <span v-if="Number(scope.row.openFlag) === 1">启用</span>
          <span v-else>禁用</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="适用货品"
        prop="itemCode"
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
  props: ["pictureData", "distributorId", "comingFlag", "categoryId"],
  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        distributorId: "",
        categoryId: "",
        type: "", // 图片类型
        // picTypeId: '', // 图片分类
        openFlag: 1,
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
    this.multipleSelect = this.pictureData ? this.pictureData : [];
    this.getTableData();
  },
  activated() {
    this.getTableData();
  },
  methods: {
    // 选择图片类型（小类）
    changeSelect(val) {
      this.pageInfo.categoryId = "";;
      this.$http.pictureCategoryPoList(this, {
        page:1,
        size:1000, 
        atLastTrademark: 1
      })
      .then((res) => {
        this.pictureCategoryList = res.data.list;
      });
    },
    selectRow() {
      //分销商请求数据变化时，重新选择行（如，删除、数据变化）
      this.$refs.multipleSelect.clearSelection();
      this.multipleSelect.forEach((row1) => {
        this.tableData.forEach((row2) => {
          if (row1.pictureId === row2.pictureId) {
            this.$refs.multipleSelect.toggleRowSelection(row2);
          }
        });
      });
    },
    getTableData() {
      this.pageInfo.distributorId = this.distributorId;
      this.pageInfo.categoryId = this.categoryId
        ? this.categoryId
        : this.pageInfo.categoryId;
      this.loading = true;
      this.$http.picListByDistributor(this, this.pageInfo).then((res) => {  
        this.isSelect = false;
        this.selected = [];

        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.loading = false;
          if (this.multipleSelect.length > 0) {
            this.multipleSelect.forEach((row1) => {
              //重新获取数据时，判断哪些选中了
              this.tableData.forEach((row2) => {
                if (row1.pictureId === row2.pictureId) {
                  this.$refs.multipleSelect.toggleRowSelection(row2);
                  this.selected.push(row2);
                }
              });
            });
          }
        }
      });
    },
    handleSubmit() {
      this.$emit("picSubmit", this.multipleSelect);
    },
    handleCancel() {
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.pictureData);
      this.$emit("picClose");
      this.selectRow();
    },
    select(selection, row) {
      //单选时调用
      this.isSelect = true;
      let d = false;
      if (this.multipleSelect.length > 0) {
        for (let i = 0; i < this.multipleSelect.length; i++) {
          if (this.multipleSelect[i].pictureId === row.pictureId) {
            this.multipleSelect.splice(i, 1);
            d = true;
            break;
          }
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
            if (this.multipleSelect[i].pictureId === row.pictureId) {
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
            if (row1.pictureId === row2.pictureId) {
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
        let type = Object.prototype.toString.call(arr[i].pictureId); //不加类型 分不清 1 '1'
        if (!obj[arr[i].pictureId + type]) {
          temp.push(arr[i]);
          obj[arr[i].pictureId + type] = true; //这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
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
    distributorId: {
      handler() {
        this.getTableData();
      },
      deep: true,
    },
    categoryId: {
      handler() {
        this.getTableData();
      },
      deep: true,
    },
    pictureData: {
      //请求的分销商数据变化时
      handler() {
        this.multipleSelect = [];
        this.multipleSelect = this.multipleSelect.concat(this.pictureData);
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
