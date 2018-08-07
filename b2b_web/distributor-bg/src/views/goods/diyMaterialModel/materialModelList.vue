<template>
  <div class="material-list">
    <header>
      <h4>材料型号关联列表</h4>
    </header>
    <div class="search">
      <div class="search-right">
        <el-input
          v-model.trim="pageInfo.content"
          clearable
          @change="changeContent"
          placeholder="材料名称/型号名称"
          size="mini"
          class="box-search"
        ></el-input>
        <button class="mini-search-btn box-btn" @click="onSearch()">
          搜索
        </button>
      </div>
      <el-select
        v-model="pageInfo.productCategoryId"
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
        v-model="pageInfo.materialId"
        placeholder="材料"
        clearable
        size="mini"
        style="width: 100px"
      >
        <el-option
          v-for="item in materials"
          :key="item.materialId"
          :label="item.name"
          :value="item.materialId"
        >
        <div v-if="item.parentId === 0">{{item.name}}</div>
        <div v-else>&nbsp;&nbsp;&nbsp;├{{item.name}}</div>
        </el-option>
      </el-select>
      <!-- <div class="category-input">
        <el-input
          size="mini"
          clearable
          @focus="categoryFocus"
          @blur="focus = false"
          placeholder="定制型号"
          v-model="pageInfo.modelName"
        ></el-input>
        <transition name="el-zoom-in-top">
          <div
            v-clickoutside="handleClose"
            class="category-box"
            v-if="categoryShow"
          >
            <el-tree
              @node-click="categorySelect"
              lazy
              :props="props"
              :load="getChildren"
              ref="tree"
              node-key="id"
            ></el-tree>
          </div>
        </transition>
      </div> -->
      <el-select
        v-model="pageInfo.openFlag"
        placeholder="状态"
        clearable
        size="mini"
        style="width: 100px"
      >
        <el-option
          v-for="item in statuss"
          :key="item.status"
          :label="item.label"
          :value="item.status"
        >
        </el-option>
      </el-select>

      <!-- <el-upload
        class="upload-demo"
        :headers="importHeaders"
        :action="action"
        :auto-upload="true"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-success="uploadSuccess"
        :on-error="uploadFail"
        :on-progress="onProgress"
      >
        <el-button class="mini-search-btn">修改材质型号</el-button>
      </el-upload> -->
    </div>

    <el-table
      :data="tableData"
      header-row-class-name="header-row"
      border
      style="text-align: center"
      v-loading="loading"
    >
      <el-table-column
        align="center"
        label="ID"
        prop="id"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="SKU编码" show-overflow-tooltip>
        <template slot-scope="scope">
          <input
            class="input-name"
            v-model="scope.row.thirdSku"
            placeholder="SKU编码"
            v-show="scope.row.isEdit"
          />
          <span v-show="!scope.row.isEdit">{{ scope.row.thirdSku }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="BOM编码" show-overflow-tooltip>
        <template slot-scope="scope">
          <input
            class="input-name"
            v-model="scope.row.bomCode"
            placeholder="BOM编码"
            v-show="scope.row.isEdit"
          />
          <span v-show="!scope.row.isEdit">{{ scope.row.bomCode }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="产品类型"
        prop="type"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="型号"
        prop="modelName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="材料"
        prop="materialName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="外框图" :width="100">
        <template
          slot-scope="scope"
          style="text-align: center; width: 60px; height: 60px"
        >
          <el-image
            v-if="scope.row.outImage"
            style="
              text-align: center;
              width: 60px;
              height: 60px;
              padding-right: 0px;
            "
            :src="scope.row.outImage"
            fit="contain"
            :preview-src-list="[scope.row.outImage]"
          >
          </el-image>
          <div
            v-else
            style="text-align: center; line-height: 60px; padding-right: 0px"
          >
            -
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="底图" :width="100">
        <template
          slot-scope="scope"
          style="text-align: center; width: 60px; height: 60px"
        >
          <el-image
            v-if="scope.row.floorImage"
            style="
              text-align: center;
              width: 60px;
              height: 60px;
              padding-right: 0px;
            "
            :src="scope.row.floorImage"
            fit="contain"
            :preview-src-list="[scope.row.floorImage]"
          >
          </el-image>
          <div
            v-else
            style="text-align: center; line-height: 60px; padding-right: 0px"
          >
            -
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="长*宽*高(mm)">
        <template
          slot-scope="scope"
          style="text-align: center; width: 60px; height: 60px"
        >
          {{
            scope.row.length + "*" + scope.row.width + "*"
          }}{{fomatFloat(scope.row.height,4)| NumFormat}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="重量(g)" prop="weight">
      </el-table-column>
      <el-table-column
        align="center"
        label="状态"
        prop="openFlag"
        :formatter="formatStatus"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="操作" :min-width="120">
        <template slot-scope="scope">
          <el-button
            class="mini-search-btn"
            @click="handleSave(scope.row)"
            style="background-color: #21b8cb"
            v-if="scope.row.isEdit"
            >保存</el-button
          >
          <el-button
            class="mini-search-btn"
            @click="handleTEdit(scope.row)"
            v-else
            >编辑</el-button
          >
          <el-button
            size="mini"
            @click="handleCancel(scope.row)"
            v-if="scope.row.isEdit"
            >取消</el-button
          >
          <el-button
            class="mini-tableadd-btn"
            v-if="scope.row.openFlag !== 1"
            @click="handleChangeStart(scope.row, 1)"
            >启用</el-button
          >
          <el-button
            class="mini-freeze-btn"
            v-else
            @click="handleChangeStart(scope.row, 0)"
            >停用</el-button
          >
          <el-button
            class="mini-delete-btn"
            v-if="scope.row.openFlag === 0"
            @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <page
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>
  </div>
</template>
<script>
import page from "@/components/pagination";
import { getToken } from "@/utils/auth";
import { fomatFloat } from '@/utils/common'
export default {
  data() {
    return {
      tableHeight: 600,
      loading: false,
      tableData: [],
      pageInfo: {
        page: 1,
        size: 10,
        productCategoryId: undefined, // 产品类型ID
        materialId: undefined, // 材质ID
        modelName: undefined,
        model: undefined,
        openFlag: undefined,
        content: undefined,
      },
      total: "",
      statuss: [
        {
          status: 1,
          label: "启用",
        },
        {
          status: 0,
          label: "停用",
        },
      ],
      categorys: [],
      materials: [],
      focus: false,
      categoryShow: false,
      props: {
        label: "name",
        children: "pictureList",
        isLeaf: "leaf",
      },
      // 上传
      processLoading: "",
      importHeaders: {
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        Authorization: "",
      },
      action: process.env.BASE_API + "admin/u/p/goods/custom/import",
    };
  },
  created() {
    this.importHeaders.Authorization = getToken();

    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight * 0.8 - 100); // 计算表高度，固定表头
  },
  mounted() {
    this.initData()
  },
  components: {
    page,
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
    fomatFloat(num,n){
      return fomatFloat(num,n)
    },
    initData () {
      this.getProCategory()
      this.getMaterial();
      this.getTableData();
    },
    handleClose() {
      if (this.categoryShow && !this.focus) {
        this.categoryShow = false;
      }
    },
    // 产品材料类型下拉列表
    getProCategory () {
      this.$http.productUsableList(this).then(res => {
        if (res.success) {
          this.categorys = res.data
        }
      })
    },
    categoryFocus() {
      this.focus = true;
      this.categoryShow = true;
    },
    getChildren(node, resolve) {
      if (node.level === 0) {
        this.$http.modelPoList(this, {    
            categoryId: this.pageInfo.productCategoryId,
            parentId: 0
          })
          .then((res) => {
            if (res.success) {
              if (
                res.data.list !== undefined &&
                res.data.list !== null &&
                res.data.list.length > 0
              ) {
                for (let i = 0; i < res.data.list.length; i++) {
                  if (res.data.list[i].trademark === 0) {
                    res.data.list[i].leaf = false;
                  } else {
                    res.data.list[i].leaf = true;
                  }
                }
              }
              resolve(res.data.list);
            }
            this.loading = false;
          });
      } else {
        this.$http.modelPoList(this, {     
            categoryId: this.pageInfo.productCategoryId,
            parentId: node.data.id,
          })
          .then((res) => {
            if (res.success) {
              if (
                res.data.list !== undefined &&
                res.data.list !== null &&
                res.data.list.length > 0
              ) {
                for (let i = 0; i < res.data.list.length; i++) {
                  if (res.data.list[i].trademark === 0) {
                    res.data.list[i].leaf = true;
                  } else {
                    res.data.list[i].leaf = true;
                  }
                }
              }
              resolve(res.data.list);
            }
            this.loading = false;
          });
      }
    },
    categorySelect(data, checked, indeterminate) {
      this.pageInfo.modelName = data.name;
      this.pageInfo.model = data.id;
      if (this.categoryShow && !this.focus) {
        this.categoryShow = false;
      }
    },
    changeContent(val) {
      if (val === undefined || val === "" || val === null) {
        this.filter();
      }
    },
    onSearch() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.getTableData();
    },
    filter() {
      this.pageInfo.page = 1;
      this.getTableData();
    },
    sizeChange(size) {
      this.pageInfo.page = 1;
      this.pageInfo.size = size;
      this.getTableData();
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.getTableData();
    },
    getMaterial() {
      this.$http.materialTreeList(this).then(res => {
        let arr = []
        if (res.success && res.data.length > 0) {
          res.data.forEach(item => {
            arr.push(item)
             if (item.childrenList.length > 0) {
              item.childrenList.forEach(val => {
                arr.push(val)
              })
            }
          })
          this.materials = arr
        }
      });
    },
    getTableData() {
      this.loading = true;
      this.$http.modelAmaterialList(this, this.pageInfo).then(res => {  
          if (res.success) {
            this.tableData = [];
            let dataArr = res.data.list;
            this.total = res.data.total;
            if (dataArr && dataArr.length > 0) {
              dataArr.forEach((item) => {
                this.tableData.push({
                  id: item.id,
                  thirdSku: item.thirdSku,
                  bomCode: item.bomCode,
                  floorImage: item.floorImage,
                  outImage: item.outImage,
                  width: item.width,
                  weight: item.weight,
                  height: item.height,
                  length: item.length,
                  materialName: item.materialName,
                  modelId: item.modelId,
                  modelName: item.modelName,
                  openFlag: item.openFlag,
                  type: item.type,
                  isEdit: false,
                });
              });
              if (
                (this.tableData === undefined ||
                  this.tableData === null ||
                  this.tableData.length === 0) &&
                this.pageInfo.page > 1
              ) {
                this.pageInfo.page = this.pageInfo.page - 1;
                this.getTableData();
              }
            } else {
              this.tableData = [];
            }
          }
          this.loading = false;
        });
    },
    formatStatus(row, col, val) {
      switch (val) {
        case 1:
          return "启用";
          break;
        case 0:
          return "停用";
          break;
      }
    },
    handleEdit(index, row) {
      this.$router.push({ name: "materialEdit", params: { id: row.id } });
    },
    handleDelete(index, row) {
      this.$confirm("确定删除此材料和型号之间的关系吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        this.$http.delModelAmaterial(this, {id:row.id}).then(res => {
          if (res.success) {
            this.$message({
              message: "删除成功",
              type: "success",
              duration: 3 * 1000,
            });
            this.getTableData();
          } 
        });
      });
    },
    handleChangeStart(row, status) {
      let msg = "";
      if (status === 1) {
        msg = "确定启用此材料与型号的关系吗？";
      } else if (status === 0) {
        msg = "确定停用此材料与型号的关系吗？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        this.$http.modelAmaterialStatus(this, {id:row.id,openFlag:status}).then(res => {    
          if (res.success) {
            row.openFlag = status;
          }
        });
      });
    },
    // 编辑
    handleTEdit(row) {
      let isFlag = false;
      this.tableData.forEach((item) => {
        if (item.isEdit) {
          isFlag = true;
        }
      });
      if (isFlag) {
        this.$message.error("请先保存编辑中的数据！");
      } else {
        row.isEdit = true;
      }
    },
    // 保存
    handleSave(row) {
      if (row.bomCode === "") {
        this.$message.error("请先输入BOM编码！");
      } else {
        const params = {
          id: row.id,
          thirdSku: row.thirdSku,
          bomCode: row.bomCode,
        };
        this.$http.editModelAmaterial(this, params).then(res => {     
            if (res.success) {
              this.$message.success("更新成功");
              this.getTableData();
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    // 取消
    handleCancel(row) {
      row.isEdit = false;
      this.getTableData();
    },
    // 修改材质型号
    beforeUpload(file) {
      //上传前配置
      let excelfileExtend = ".xls,.xlsx"; //设置文件格式
      let fileExtend = file.name
        .substring(file.name.lastIndexOf("."))
        .toLowerCase();
      if (excelfileExtend.indexOf(fileExtend) <= -1) {
        this.$message.error("只能上传.xls,.xlsx格式");
        return false;
      }
    },
    onProgress(event, file, fileList) {
      // 上传
      this.processLoading = this.$loading({
        lock: true,
        text: "文件上传中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    uploadFail(err, file, fileList) {
      //上传错误
      this.$message.error({
        message: err.msg,
        duration: 3 * 1000,
        onClose: () => {},
      });
      this.processLoading.close();
    },
    uploadSuccess(response, file, fileList) {
      //上传成功
      if (response.code == 0) {
        this.$message.success({
          message: "修改成功~",
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.processLoading.close();
      } else {
        this.$message.error({
          message: response.msg,
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.processLoading.close();
      }
    },
  },
  watch: {
    "pageInfo.productCategoryId": {
      handler() {
        this.pageInfo.page = 1;
        this.getTableData();
      },
      deep: true,
    },
    "pageInfo.materialId": {
      handler() {
        this.pageInfo.page = 1;
        this.getTableData();
      },
      deep: true,
    },
    "pageInfo.model": {
      handler() {
        this.pageInfo.page = 1;
        this.getTableData();
      },
      deep: true,
    },
    "pageInfo.openFlag": {
      handler() {
        this.pageInfo.page = 1;
        this.getTableData();
      },
      deep: true,
    },
    "pageInfo.modelName": {
      handler() {
        if (
          this.pageInfo.modelName === undefined ||
          this.pageInfo.modelName === ""
        ) {
          this.pageInfo.model = undefined;
        }
      },
      deep: true,
    },
  },
};
</script>
<style lang="scss" scoped>
.material-list {
  background-color: white;
  min-height: 100%;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
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
  .action {
    margin: 20px 0;
  }
  .search {
    width: 70%;
  }
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    // position: absolute;
    // bottom: auto;
    // top: expression(eval(document.documentElement.scrollTop));
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .btn-home {
    float: right;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
  }

  .search {
    width: 100%;
    border-bottom: 1px solid #dcdcdc;
    padding: 10px;
    // overflow: hidden;
    .search-right {
      float: right;
      overflow: hidden;
      .box-search {
        width: 215px;
        // float: right;
      }
      .box-btn {
        float: right;
        margin-left: 5px;
      }
    }
  }
  .category-input {
    width: 150px;
    float: left;
    margin-right: 5px;
    position: relative;
  }
  .category-box {
    border: 1px solid #ccc;
    width: 150px;
    padding: 10px;
    background-color: white;
    position: absolute;
    left: 0;
    top: 45px;
    z-index: 99;
    border-radius: 10px;
  }
  .el-table {
    color: red;
    .input-name {
      height: 30px;
      font-size: 14px;
      border-radius: 5px;
      border: 1px solid #ccc;
      font-weight: bold;
      width: 100%;
      text-align: center;
    }
  }
}

.upload-demo {
  display: inline-block;
}
</style>
