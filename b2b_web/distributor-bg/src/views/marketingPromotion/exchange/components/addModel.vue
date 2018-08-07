<!--
 * @Author: yaowei
 * @Date: 2018-05-21 16:32:03
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 13:39:28
-->
<template>
  <div>
    <el-form-item label prop="exchangeModel">
      <el-button
        class="mini-search-btn"
        @click="handleInitModelData"
        v-if="!isDisabled"
        >添加型号</el-button
      >
      <el-button
        class="mini-delete-btn"
        @click="handleDeleteChoose"
        v-if="!isDisabled"
        >批量删除</el-button
      >

      <div class="select-wrap" v-if="modelItems.length > 0">
        <el-select
          v-model="customType"
          placeholder="产品类型"
          clearable
          size="mini"
          style="width: 150px"
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
          v-model="customModel"
          placeholder="定制型号"
          clearable
          size="mini"
          style="width: 150px"
        >
          <el-option
            v-for="item in customModels"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </div>

      <el-table
        v-if="modelItems.length > 0"
        :data="filterModelItems"
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
          label="型号编码"
          prop="id"
        ></el-table-column>
        <el-table-column align="center" label="产品类型" prop="categoryName">
        </el-table-column>
        <el-table-column
          align="center"
          label="型号分类"
          prop="parentName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="型号名称"
          prop="name"
        ></el-table-column>
        <el-table-column align="center" label="状态">
          <template slot-scope="scope">
            <span v-if="scope.row.openFlag === 1">启用</span>
            <span v-if="scope.row.openFlag === 0">停用</span>
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

    <!-- 添加型号 -->
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
          <div class="category-input">
            <el-input
              size="mini"
              clearable
              @focus="categoryFocus"
              @blur="focus = false"
              placeholder="定制型号"
              v-model="modelName"
            ></el-input>
            <transition name="el-zoom-in-top">
              <div
                v-clickoutside="handleClose"
                class="category-box"
                v-if="categoryShow"
              >
                <!-- <el-tree
                  @node-click="categorySelect"
                  lazy
                  :props="props"
                  :load="getChildren"
                  ref="tree"
                  node-key="id"
                ></el-tree> -->
                <el-tree
                  :data="modelCategoryList"
                  @node-click="categorySelect"
                  :props="props"
                  ref="tree"
                >
                </el-tree>
              </div>
            </transition>
          </div>

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
            placeholder="型号名称"
            v-model.trim="pageInfo.content"
          ></el-input>
        </div>

        <div>
          <el-table
            ref="multipleSelect"
            :data="tableData"
            v-loading="loading"
            tooltip-effect="dark"
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
              label="型号ID"
              prop="id"
            ></el-table-column>
            <el-table-column
              align="center"
              label="型号分类"
              prop="parentName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="型号名称"
              prop="name"
            ></el-table-column>
            <el-table-column
              align="center"
              label="产品类型"
              prop="categoryName"
            >
            </el-table-column>
            <el-table-column align="center" label="状态">
              <template slot-scope="scope">
                <span v-if="scope.row.openFlag === 1">启用</span>
                <span v-if="scope.row.openFlag === 0">停用</span>
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
  name: "addModel",
  props: [
    "exchangeModel",
    "isDisabled",
    "materialIdList",
    "pictureIdList",
    "initModelItems",
    "editStatus",
  ],
  components: { page },
  data() {
    return {
      loading: true,
      showDialog: false,
      pageInfo: {
        page: 1,
        size: 10,
        openFlag: 1,
        categoryId: '',
        parentId: "",
        atLastTrademark: 1,
        materialIdList: [],
        content: ''
      },
      categorys: [],
      tableData: [],
      total: 0,
      multipleSelect: [],
      isSelect: false,
      modelItems: [],
      modelName: "",
      focus: false,
      categoryShow: false,
      props: {
        label: "name",
        children: "childrenList",
        isLeaf: "leaf",
      },
      // 筛选
      customType: '',
      customModel: "",
      customModels: [],
      modelCategoryList: []
    };
  },
  watch: {
    initModelItems: {
      handler(val) {
        if (val) {
          this.modelItems = val;
          this.multipleSelect = val;
          this.modelItems.forEach((item) => {
            this.$set(item, "visible", true);
          });
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
    "pageInfo.parentId": {
      handler() {
        this.pageInfo.page = 1;
        this.dataForamt();
      },
      deep: true,
    },
    customModel: {
      handler(val) {
        var value = "";
        this.customModels.forEach((item) => {
          if (item.id === val) {
            value = item.name;
          }
        });
        this.filterSelectModel(value);
      },
      deep: true,
    },
    modelItems: {
      handler(val) {
        val.forEach((item) => {
          var options = {
            id: item.id,
            name: item.parentName,
          };
          this.customModels.push(options);
        });
        let obj = {};
        this.customModels = this.customModels.reduce((cur, next) => {
          obj[next.name] ? "" : (obj[next.name] = true && cur.push(next));
          return cur;
        }, []);
      },
      deep: true,
    },
  },
  computed: {
    filterModelItems() {
      return this.modelItems.filter((item) => {
        if (item.visible) {
          return item;
        }
      });
      return this.modelItems;
    },
  },
  directives: {
    //..事件绑定
    clickoutside: {
      bind: function (el, binding, vnode) {
        function documentHandler(e) {
          if (el.contains(e.target)) {
            //..这里判断点击的元素是否是本身，是本身，则返回
            return false;
          }
          if (binding.expression) {
            //..判断指令中是否绑定了函数
            binding.value(e); //..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
          }
        }
        el._vueClickOutside_ = documentHandler; //..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
        document.addEventListener("click", documentHandler);
      },
      unbind: function (el, binding) {
        document.removeEventListener("click", el._vueClickOutside_);
        delete el._vueClickOutside_;
      },
    },
  },
  methods: {
    // 筛选选择型号
    filterSelectModel(val) {
      if (val !== "") {
        this.modelItems.forEach((item) => {
          if (item.parentName === val) {
            this.$set(item, "visible", true);
          } else {
            this.$set(item, "visible", false);
          }
        });
      } else {
        this.modelItems.forEach((item) => {
          this.$set(item, "visible", true);
        });
      }
      return this.modelItems;
    },
    // 搜索添加型号列表
    filter() {
      this.dataForamt();
    },
    // 添加型号
    handleInitModelData() {
      if (this.materialIdList && this.materialIdList.length <= 0) {
        // 未设置材质
        this.$message({
          type: "warning",
          message: "请先设置兑换材质",
        });
        return;
      } else {
        // 已设置材质
        this.pageInfo.materialIdList = this.materialIdList;
        this.showDialog = true;
        this.getProCategory()
        this.getModelCategoryList()
        this.dataForamt()
      }
    },
    // 产品类型下拉列表
    getProCategory () {
      this.$http.productUsableList(this).then(res => {
        if (res.success) {
          this.categorys = res.data
        }
      })
    },
     // 型号分类下拉树形列表
    getModelCategoryList() {
      this.$http.modelPoList(this, {
        size:1000,
        page:1,
        parentId: 0,
        atLastTrademark: 0,
        openFlag: 1
      }).then(res => {
        if (res.success) {
          this.modelCategoryList = res.data.list
        }
      })
    },
    // 选择定制型号 - 关闭
    handleClose() {
      if (this.categoryShow && !this.focus) {
        this.categoryShow = false;
      }
    },
    // 选择定制型号 - 获取焦点
    categoryFocus() {
      this.focus = true
      this.categoryShow = true
    },
    // 选择定制型号
    categorySelect(data, checked, indeterminate) {
      this.modelName = data.name;
      this.pageInfo.parentId = data.id;
      if (this.categoryShow && !this.focus) {
        this.categoryShow = false;
      }
    },
    // 获取型号
    dataForamt() {
      this.loading = true
      this.$http.modelList(this, this.pageInfo).then(res => {   
        if (res.success) {
          this.isSelect = false;
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.tableData.forEach((item) => {
            this.$set(item, "visible", true);
          });
          this.multipleSelect.forEach((row1) => {
            // 重新获取数据时，判断哪些选中了
            this.tableData.forEach((row2) => {
              if (row1.id === row2.id) {
                this.$nextTick(() => {
                  this.$refs.multipleSelect.toggleRowSelection(row2);
                });
              }
            });
          });
        } 
        res.success ? this.loading = false : this.loading = false
      });
    },
    // 删除定制型号
    handleDeleteGood(index) {
      this.handleConfirm(() => {
        this.modelItems.splice(index, 1);
        this.multipleSelect = this.modelItems;
        this.$emit("getPicData", []);
        this.$emit("getModelData", this.modelItems);
      });
    },
    // 定制型号
    handleSelectionItemChange(val) {
      this.multipleSelect = val;
    },
    // 定制型号 - 批量删除
    handleDeleteChoose() {
      this.handleConfirm(() => {
        if (this.multipleSelect) {
          this.multipleSelect.forEach((item1, index1) => {
            this.modelItems.forEach((item2, index2) => {
              if (item1 === item2) {
                this.modelItems.splice(index2, 1);
              }
            });
          });
        }
        this.multipleSelect = this.modelItems;
        this.$emit("getPicData", []);
        this.$emit("getModelData", this.modelItems);
        this.$refs.multipleGoodsTable.clearSelection();
        this.customModels = [];
      });
    },
    handleConfirm(callback) {
      console.log(this.pictureIdList);
      if (this.pictureIdList.length > 0) {
        this.$confirm(
          "删除定制型号后，设置的定制图片将会被清空，是否继续？",
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
      this.modelItems = [];
      if (this.multipleSelect) {
        this.modelItems = this.modelItems.concat(this.multipleSelect);
      }
      this.showDialog = false;
      this.multipleSelect = this.modelItems;
      this.$emit("getModelData", this.modelItems);
    },
    handleCancel() {
      // 取消
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.modelItems);
      this.showDialog = false;
      this.$emit("getModelData", this.multipleSelect);
    },
  },
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
  .category-input {
    display: inline-block;
    width: 150px;
    margin-right: 5px;
    position: relative;
  }
  .category-box {
    border: 1px solid #ccc;
    min-width: 150px;
    padding: 10px;
    background-color: white;
    position: absolute;
    left: 0;
    top: 45px;
    z-index: 99;
    border-radius: 10px;
  }
}
</style>