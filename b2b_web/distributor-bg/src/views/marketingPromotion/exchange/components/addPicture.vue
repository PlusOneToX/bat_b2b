<!--
 * @Author: yaowei
 * @Date: 2018-05-21 16:43:27
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 13:40:49
-->
<template>
  <div>
    <el-form-item label prop="exchangePicture">
      <el-button
        class="mini-search-btn"
        @click="handleInitPicData"
        v-if="!isDisabled"
        >添加图片</el-button
      >
      <el-button
        class="mini-delete-btn"
        @click="handleDeleteChoose"
        v-if="!isDisabled"
        >批量删除</el-button
      >

      <div class="select-wrap" v-if="picItems.length > 0">
        <el-select
          v-model="customPic"
          placeholder="图片类型"
          clearable
          size="mini"
          style="width: 150px"
        >
          <el-option
            v-for="item in customPics"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
        <el-select
          v-model="customCategory"
          placeholder="图片分类"
          clearable
          size="mini"
          style="width: 150px"
        >
          <el-option
            v-for="item in customCategorys"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </div>

      <el-table
        v-if="picItems.length > 0"
        :data="filterPicItems"
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
          label="图片编码"
          prop="id"
        ></el-table-column>
        <el-table-column align="center" label="素材类型" prop="type">
          <template slot-scope="scope">
            <span v-if="scope.row.type === 1">普通素材</span>
            <span v-if="scope.row.type === 2">IP素材</span>
            <span v-if="scope.row.type === 3">模板</span>
            <span v-if="scope.row.type === 4">贴图</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="图片分类"
          prop="categoryName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="图片名称"
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

    <!-- 添加图片 -->
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
            v-model="pageInfo.type"
            placeholder="图片类型"
            clearable
            size="mini"
            style="width: 100px"
          >
            <el-option
              v-for="item in picType"
              :key="item.type"
              :label="item.label"
              :value="item.type"
            >
            </el-option>
          </el-select>
          <el-select
            v-model="pageInfo.categoryId"
            placeholder="图片分类"
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
            placeholder="图片名称"
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
              label="图片编码"
              prop="id"
            ></el-table-column>
            <el-table-column align="center" label="素材类型" prop="type">
              <template slot-scope="scope">
                <span v-if="scope.row.type === 1">普通素材</span>
                <span v-if="scope.row.type === 2">IP素材</span>
                <span v-if="scope.row.type === 3">模板</span>
                <span v-if="scope.row.type === 4">贴图</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="图片分类"
              prop="categoryName"
            ></el-table-column>
            <el-table-column
              align="center"
              label="图片名称"
              prop="name"
            ></el-table-column>
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
  name: "addPicture",
  props: [
    "exchangePicture",
    "isDisabled",
    "materialIdList",
    "modelIdList",
    "initPicItems",
    "editStatus",
    "modelUseType",
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
        content: "",
        type: 2,
        categoryId: "",
        modelIdList: [],
        materialIdList: [],
      },
      picType: [
        {
          type: 1,
          label: "普通素材",
        },
        {
          type: 2,
          label: "IP素材",
        },
        {
          type: 3,
          label: "模板",
        },
        {
          type: 4,
          label: "贴图",
        },
      ],
      tableData: [],
      total: 0,
      multipleSelect: [],
      isSelect: false,
      picItems: [],
      pictureCategoryList: [], // 图片分类
      // 筛选
      customPic: "",
      customPics: [],
      customCategory: [],
      customCategorys: [],
    };
  },
  watch: {
    initPicItems: {
      handler(val) {
        if (val) {
          this.picItems = val;
          this.multipleSelect = val;
          this.picItems.forEach((item) => {
            this.$set(item, "visible", true);
            if (item.type === 1) {
              this.$set(item, "mName", "普通素材");
            } else if (item.type === 2) {
              this.$set(item, "mName", "IP素材");
            } else if (item.type === 3) {
              this.$set(item, "mName", "模板");
            } else if (item.type === 4) {
              this.$set(item, "mName", "贴图");
            }
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
    "pageInfo.type": {
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
    customPic: {
      handler(val) {
        this.filterSelectPic(val, this.customCategory);
      },
      deep: true,
    },
    customCategory: {
      handler(val) {
        var value = "";
        this.customCategorys.forEach((item) => {
          if (item.id == val) {
            value = item.name;
          }
        });
        this.filterSelectPic(this.customPic, value);
      },
      deep: true,
    },
    initPicItems(arr) {
      this.picItems = arr;
    },
    picItems: {
      handler(val) {
        val.forEach((item) => {
          var options1 = {
            id: item.type,
            name: item.mName,
          };
          this.customPics.push(options1);
          var options2 = {
            id: item.categoryName,
            name: item.categoryName,
          };
          this.customCategorys.push(options2);
        });
        let obj1 = {};
        this.customPics = this.customPics.reduce((cur, next) => {
          obj1[next.id] ? "" : (obj1[next.id] = true && cur.push(next));
          return cur;
        }, []);
        let obj2 = {};
        this.customCategorys = this.customCategorys.reduce((cur, next) => {
          obj2[next.name] ? "" : (obj2[next.name] = true && cur.push(next));
          return cur;
        }, []);
      },
      deep: true,
    },
  },
  computed: {
    filterPicItems() {
      return this.picItems.filter((item) => {
        if (item.visible) {
          return item;
        }
      });
    },
  },
  methods: {
    // 筛选选择型号
    filterSelectPic(val1, val2) {
      if (val1 !== "") {
        this.picItems.forEach((item) => {
          if (item.type === val1) {
            this.$set(item, "visible", true);
          } else {
            this.$set(item, "visible", false);
          }
        });
      }
      if (val2 !== "") {
        this.picItems.forEach((item) => {
          if (item.categoryName === val2) {
            this.$set(item, "visible", true);
          } else {
            this.$set(item, "visible", false);
          }
        });
      }
      if (val1 !== "" && val2 !== "") {
        this.picItems.forEach((item) => {
          if (item.type === val1 && item.categoryName === val2) {
            this.$set(item, "visible", true);
          } else {
            this.$set(item, "visible", false);
          }
        });
      }
      if (val1 === "" && val2 === "") {
        this.picItems.forEach((item) => {
          this.$set(item, "visible", true);
        });
      }
      return this.picItems;
    },
    // 获取图片分类
    getPictureCategory() {
      this.$http
        .pictureCategoryPoList(this, {
          page: 1,
          size: 10000,
          atLastTrademark: 1,
        })
        .then((res) => {
          if (res.success) {
            this.pictureCategoryList = res.data.list;
          }
        });
    },
    // 搜索添加图片列表
    filter() {
      this.dataForamt();
    },
    // 添加图片
    handleInitPicData() {
      if (this.materialIdList && this.materialIdList.length <= 0) {
        // 未设置材质
        this.$message({
          type: "warning",
          message: "请先设置兑换材质",
        });
        return;
      } else if (
        Number(this.modelUseType) === 2 &&
        this.modelIdList &&
        this.modelIdList.length <= 0
      ) {
        // 未设置型号
        this.$message({
          type: "warning",
          message: "请先设置定制型号",
        });
        return;
      } else {
        // 已设置材质、型号
        this.getPictureCategory();
        this.pageInfo.materialIdList = this.materialIdList;
        this.pageInfo.modelIdList = this.modelIdList;
        this.showDialog = true;
        this.dataForamt();
      }
    },
    // 获取图片
    dataForamt() {
      this.loading = true;
      this.$http.pictureList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.isSelect = false;
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.tableData.forEach((item) => {
            this.$set(item, "visible", true);
            if (item.type === 1) {
              this.$set(item, "mName", "普通素材");
            } else {
              this.$set(item, "mName", "IP素材");
            }
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
        res.success ? (this.loading = false) : (this.loading = false);
      });
    },
    // 删除定制图片
    handleDeleteGood(index) {
      this.picItems.splice(index, 1);
      this.multipleSelect = this.picItems;
      this.$emit("getPicData", this.picItems);
    },
    // 定制图片
    handleSelectionItemChange(val) {
      this.multipleSelect = val;
    },
    // 定制图片 - 批量删除
    handleDeleteChoose() {
      if (this.multipleSelect) {
        this.multipleSelect.forEach((item1, index2) => {
          this.picItems.forEach((item2, index2) => {
            if (item1 === item2) {
              this.picItems.splice(index2, 1);
            }
          });
        });
      }
      this.multipleSelect = this.picItems;
      this.$emit("getPicData", this.picItems);
      this.$refs.multipleGoodsTable.clearSelection();
      this.customCategorys = [];
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
      this.pageInfo.count = size;
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
      this.picItems = [];
      if (this.multipleSelect) {
        this.picItems = this.picItems.concat(this.multipleSelect);
      }
      this.showDialog = false;
      this.multipleSelect = this.picItems;
      this.$emit("getPicData", this.picItems);
    },
    handleCancel() {
      // 取消
      this.multipleSelect = [];
      this.multipleSelect = this.multipleSelect.concat(this.picItems);
      this.showDialog = false;
      this.$emit("getPicData", this.multipleSelect);
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
}
</style>