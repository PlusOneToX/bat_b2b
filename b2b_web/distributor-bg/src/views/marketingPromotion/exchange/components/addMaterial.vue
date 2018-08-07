<!--
 * @Author: yaowei
 * @Date: 2018-05-21 16:07:20
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-25 16:07:26
-->
<template>
  <div>
    <el-form-item label="兑换材质设置:" prop="exchangeMaterial">
      <el-button
        class="mini-search-btn"
        @click="handleInitMaterialData"
        v-if="!isDisabled"
        >添加材质</el-button
      >
      <el-button
        class="mini-delete-btn"
        @click="handleDeleteChoose"
        v-if="!isDisabled"
        >批量删除</el-button
      >

      <el-table
        v-if="materialItems.length > 0"
        :data="materialItems"
        ref="multipleGoodsTable"
        border
        header-row-class-name="header-row"
        class="tableCenter goods-table"
        style="width: 100%"
        max-height="400"
        @selection-change="handleSelectionItemChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="55"
        ></el-table-column>
        <el-table-column
          align="center"
          label="编号"
          prop="id"
        ></el-table-column>
        <el-table-column
          align="center"
          label="产品类型"
          prop="categoryName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="材质分类"
          prop="parentName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="材质名称"
          prop="name"
        ></el-table-column>
        <el-table-column align="center" label="状态">
          <template slot-scope="scope">
            <span v-if="Number(scope.row.openFlag) === 1">启用</span>
            <span v-if="Number(scope.row.openFlag) === 0">停用</span>
          </template>
        </el-table-column>
        <el-table-column
          width="80"
          align="center"
          label="操作"
          v-if="!isDisabled"
        >
          <template slot-scope="scope">
            <el-button
              class="mini-delete-btn"
              @click="handleDeleteGood(scope.$index)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-form-item>

    <!-- 添加材质 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="showDialog"
      width="80%"
      :before-close="closeItemsDialog"

    >
      <div class="check-goods">
        <div class="nav-bar">
          <el-select
            v-model="pageInfo.openFlag"
            placeholder="状态"
            clearable
            size="mini"
            style="width: 100px"
          >
            <el-option label="启用" :value="1"></el-option>
            <el-option label="停用" :value="0"></el-option>
          </el-select>
          <el-select
            v-model="pageInfo.categoryId"
            placeholder="产品类型"
            clearable
            size="mini"
            style="width: 100px"
          >
            <el-option
              v-for="item in categorys"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <el-select
            v-model="curCode"
            placeholder="材质分类"
            clearable
            size="mini"
            style="width: 150px"
          >
             <el-option
                v-for="item in materials"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
            </el-option>
          </el-select>

          <el-button
            class="mini-search-btn"
            size="mini"
            @click="filter()"
            style="float: right; margin-left: 5px"
            >搜索</el-button
          >
          <el-input
            size="mini"
            style="float: right; width: 250px"
            placeholder="请输入材质名称"
            v-model.trim="pageInfo.content"
          ></el-input>
        </div>

        <div>
          <el-table
            ref="multipleSelect"
            :data="tableData"
            tooltip-effect="dark"
            v-loading="loading"
            @select="select"
            @select-all="selectAll"
            border
            header-row-class-name="header-row"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              align="center"
              type="selection"
              width="55"
            ></el-table-column>
            <el-table-column
              align="center"
              label="编号"
              prop="id"
            ></el-table-column>
            <el-table-column
              align="center"
              label="产品类型"
              prop="categoryName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="材质分类"
              prop="parentName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="材质名称"
              prop="name"
            ></el-table-column>
            <el-table-column align="center" label="状态">
              <template slot-scope="scope">
                <span v-if="Number(scope.row.openFlag) === 1">启用</span>
                <span v-if="Number(scope.row.openFlag) === 0">停用</span>
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
        <div class="foot-btn">
          <el-button
            class="mini-search-btn"
            style="margin-left: 47%"
            @click="handleSubmit()"
            >确定</el-button
          >
          <el-button size="mini" @click="handleCancel">返回</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import page from "@/components/pagination/index";
export default {
  name: "addMaterial",
  props: [
    "exchangeMaterial",
    "isDisabled",
    "modelIdList",
    "pictureIdList",
    "initMaterialItems",
    "editStatus",
  ],
  components: { page },
  data() {
    return {
      loading: false,
      showDialog: false,
      pageInfo: {
        page: 1,
        size: 10,
        openFlag: 1,
        categoryId: undefined,
        parentId: undefined,
        content: undefined,
        atLastTrademark:1
      },
      categorys: [],
      materials: [],
      tableData: [],
      total: 0,
      multipleSelect: [],
      isSelect: false,
      materialItems: [],
      curCode: "",
    };
  },
  watch: {
    editStatus: {
      handler(val) {
        if (val) {
          this.materialItems = this.initMaterialItems;
          this.multipleSelect = this.initMaterialItems;
        }
      },
      deep: true,
    },
    "pageInfo.openFlag": {
      handler() {
        this.pageInfo.page = 1;
        this.dataForamt();
      },
      deep: true,
    },
    "pageInfo.categoryId": {
      handler() {
        this.pageInfo.page = 1;
        this.dataForamt();
      },
      deep: true,
    },
    curCode: {
      handler() {
        this.pageInfo.parentId = this.curCode;
        this.pageInfo.page = 1;
        this.dataForamt();
      },
      deep: true,
    },
  },
  methods: {
    // 搜索添加材质列表
    filter() {
      this.pageInfo.parentId = this.curCode;
      this.dataForamt();
    },
    // 添加材质
    handleInitMaterialData() {
      this.showDialog = true;
      this.getProCategory()
      this.getMaterial();
      this.dataForamt();
    },
    // 产品类型下拉列表
    getProCategory () {
      this.$http.productUsableList(this).then(res => {
        if (res.success) {
          this.categorys = res.data
        }
      })
    },
    // 获取材质下拉列表
    getMaterial() {
      this.$http.materialUnTree(this, {page:1, size:10000,openFlag:1, parentId:0}).then(res => {
        let arr = []
        if (res.success && res.data.length > 0) {
          res.data.forEach(item => {
            arr.push(item)
          })
          this.materials = arr
        }
      });
    },
    // 获取材质列表
    dataForamt() {
      this.loading = true
      this.$http.materialPageTree(this, this.pageInfo)  
        .then((res) => {
          if (res.success) {
            this.isSelect = false;
            this.tableData = res.data.list;
            this.total = res.data.total;
            this.multipleSelect.forEach((row1) => {
              // 重新获取数据时，判断哪些选中了
              this.tableData.forEach((row2) => {
                if (row1.id === row2.id) {
                    this.$refs.multipleSelect.toggleRowSelection(row2);
                }
              });
            });
          }
          res.success ? this.loading = false : this.loading = false
        });
    },
    // 删除兑换材质
    handleDeleteGood(index) {
      this.handleConfirm(() => {
        this.materialItems.splice(index, 1);
        this.multipleSelect = this.materialItems;
        this.$emit("getModelData", []);
        this.$emit("getPicData", []);
        this.$emit("getMaterialData", this.materialItems);
      });
    },
    // 兑换材质
    handleSelectionItemChange(val) {
      this.multipleSelect = val;
    },
    // 兑换材质 - 批量删除
    handleDeleteChoose() {
      this.handleConfirm(() => {
        if (this.multipleSelect) {
          this.multipleSelect.forEach((item1, index1) => {
            this.materialItems.forEach((item2, index2) => {
              if (item1 === item2) {
                this.materialItems.splice(index2, 1);
              }
            });
          });
        }
        this.multipleSelect = this.materialItems;
        this.$emit("getModelData", []);
        this.$emit("getPicData", []);
        this.$emit("getMaterialData", this.materialItems);
        this.$refs.multipleGoodsTable.clearSelection();
      });
    },
    handleConfirm(callback) {
      if (this.modelIdList.length > 0 || this.pictureIdList.length > 0) {
        this.$confirm(
          "删除材质后，设置的定制型号与定制图片将会被清空，是否继续？",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            center: true,
          }
        )
          .then((_) => {
            callback();
          })
          .catch((_) => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      } else {
        callback();
      }
    },
    closeItemsDialog() {
      this.showDialog = false;
    },
    select(selection, row) {
      // 单选时调用
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
      // 全选时调用
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
    sizeChange(size) {
      // 分页页数
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.dataForamt();
    },
    currentChange(page) {
      // 分页总数
      this.pageInfo.page = page;
      this.dataForamt();
    },
    handleSelectionChange(val) {
      // 当切换页面时的作用
      if (
        val.length === 0 &&
        this.multipleSelect.length !== 0 &&
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
    handleSubmit() {
      // 确定
      this.materialItems = [];
      if (this.multipleSelect) {
        this.materialItems = this.materialItems.concat(this.multipleSelect);
      }
      this.showDialog = false;
      this.multipleSelect = this.materialItems;
      this.$emit("getMaterialData", this.materialItems);
    },
    handleCancel() {
      // 取消
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.materialItems);
      this.showDialog = false;
      this.$emit("getMaterialData", this.multipleSelect);
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}
.check-goods {
  background-color: white;
  .nav-bar {
    padding: 10px 0;
  }
  .commodityTable {
    text-align: center;
  }
  .header-row {
    @include table-header-color;
  }
  .foot-btn {
    margin-top: 10px;
  }
}
</style>
