<template>
  <div class="store-manage">
    <header class="header">
      <h4 class="title">店铺管理</h4>
      <el-button
        class="mini-add-btn btn-right"
        icon="el-icon-plus"
        @click="handleAdd"
        >新增店铺</el-button
      >
      <el-button class="mini-add-btn btn-right" @click="handleImport"
        >导入店铺</el-button
      >
    </header>
    <div class="search">
      <!-- <el-button class="mini-delete-btn box-btn" @click="handleBatchDelete()"
        >批量删除</el-button
      > -->
      <!-- <el-button class="mini-search-btn box-btn" @click="handleExportData(1)"
        >导出筛选后信息</el-button
      > -->
      <el-button class="mini-search-btn box-btn" @click="handleExportData(2)"
        >导出信息</el-button
      >
      <el-button
        class="mini-search-btn box-btn box-search"
        @click="handleSearch()"
        >搜索</el-button
      >
      <el-input
        v-model.trim="pageInfo.content"
        size="mini"
        clearable
        placeholder="请输入分销商名称/门店名称/门店编码/公众号名称"
        class="box-input"
        @clear="handleClear"
      ></el-input>
      <el-select
        clearable
        v-model="status"
        placeholder="状态"
        size="mini"
        @change="handleSelect"
      >
        <el-option label="开启" :value="1"> </el-option>
        <el-option label="停用" :value="0"> </el-option>
      </el-select>
    </div>
    <!----数据列表----->
    <el-row v-loading="loading2">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        style="text-align: center"
        row-key="id"
        ref="multipleSelect"
        @select="select"
        @select-all="selectAll"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="50"
          reserve-selection=""
        ></el-table-column>
        <el-table-column
          align="center"
          label="分销商名称"
          :min-width="120"
          prop="distributorName"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="分销商公司"
          :min-width="120"
          prop="distributorCompanyName"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="门店名称"
          :min-width="120"
          prop="shopName"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="门店编码"
          :min-width="100"
          prop="shopCode"
          show-overflow-tooltip
        >
        </el-table-column>
        <!-- <el-table-column align="center" label="公众号名称" prop="appName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="APPID" prop="appId" show-overflow-tooltip>
        </el-table-column> -->
        <el-table-column align="center" label="备注" prop="remark" show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="状态" prop="openFlag" show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="a">{{getStatus(scope.row.openFlag)}}</div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="微信访问URL"
          :min-width="200"
          prop="url"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column align="center" label="店铺二维码" width="150">
          <template
            slot-scope="scope"
            style="text-align: center; width: 140px; height: 140px"
          >
            <el-image
              v-if="scope.row.qrUrl"
              style="
                text-align: center;
                width: 120px;
                height: 120px;
                padding-right: 0px;
              "
              fit="contain"
              :src="scope.row.qrUrl + '?x-oss-process=image/resize,h_120,l_120'"
              :preview-src-list="[scope.row.qrUrl]"
            >
            </el-image>
            <span v-else>/</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="店铺二维码（三方规则）" width="185">
          <template
            slot-scope="scope"
            style="text-align: center; width: 140px; height: 140px"
          >
            <el-image
              v-if="scope.row.thirdQrUrl"
              style="
                text-align: center;
                width: 120px;
                height: 120px;
                padding-right: 0px;
              "
              fit="contain"
              :src="scope.row.thirdQrUrl + '?x-oss-process=image/resize,h_120,l_120'"
              :preview-src-list="[scope.row.thirdQrUrl]"
            >
            </el-image>
            <span v-else>/</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="260" align="center">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleEdit(scope.row)" :disabled="scope.row.openFlag===0">编辑</el-button>
            <el-button class="mini-search-btn" @click="handleChangeStatus(scope.row)">{{getfStatus(scope.row.openFlag)}}</el-button>
            <el-button class="mini-search-btn" @click="handleDownload(scope.row)" :disabled="scope.row.openFlag===0">下载</el-button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.row)" :disabled="scope.row.openFlag===0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <page
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>
  </div>
</template>

<script>
import axios from 'axios'
import url from '@/api/allUrl'
import { getToken } from '@/utils/auth'
import page from "@/components/pagination"
import {
  getStoreList,
  deleteStore,
  batchDeleteStore,
  exportShop,
  editStore,
} from "@/views/storeLayout/rxData";
export default {
  data() {
    return {
      loading2: false,
      loading: "",
      tableData: [],
      multipleSelect: [],
      status: "",
      isSelect: false,
      pageInfo: {
        openFlag: null,
        content: null,
        page: 1,
        size: 10,
      },
      total: 0,
    };
  },
  components: {
    page,
  },
  mounted() {
    this.initData();
  },
  methods: {
    // 获取数据列表
    initData() {
      this.loading2 = true;
      if (this.pageInfo.content === '') {
        this.pageInfo.content = null
      }
      this.multipleSelect = []
      // getStoreList(this, this.pageInfo).then((res) => {
      this.$http.shopList(this, this.pageInfo).then(res => {  
        this.loading2 = false;
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
      });
    },
    // 获取状态
    getStatus(val) {
      if (Number(val) === 1) {
        return "开启";
      } else if (Number(val) === 0) {
        return "停用";
      }
    },
    // 获取相反状态
    getfStatus(val) {
      if (Number(val) === 0) {
        return "开启";
      } else if (Number(val) === 1) {
        return "停用";
      }
    },
    // 导入店铺
    handleImport() {
      this.$router.push({ name: "storeImport" });
    },
    // 切换状态搜索
    handleSelect (val) {
      this.pageInfo.openFlag = val === '' ? null : val
      this.initData()
    },
    // 清空搜索关键字
    handleClear () {
      this.pageInfo.content = null
       this.initData()
    },
    // 搜索
    handleSearch() {
      this.pageInfo.page = 1;
      this.initData();
    },
    // 新增店铺
    handleAdd() {
      this.$router.push({ name: "storeDetail" });
    },
    // 批量删除
    handleBatchDelete() {
      let idList = [];
      for (let i = 0; i < this.multipleSelect.length; i++) {
        idList.push(this.multipleSelect[i].id);
      }
      if (idList.length > 0) {
        this.$confirm("此操作将删除已选数据，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then((_) => {
            batchDeleteStore(this, {
              ids: idList,
            }).then((res) => {
              if (Number(res.code) === 0) {
                this.$message({
                  type: "success",
                  message: "批量删除成功",
                });
                this.initData();
              } else {
                this.$message({
                  type: "error",
                  message: "批量删除失败",
                });
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
          message: "未勾选数据",
        });
      }
    },
    // 导出信息
    handleExportData(type) {
      let downIds = [];
      let isExport = false;
      if (type === 1) {
        // 导出筛选后信息
        for (let i = 0; i < this.multipleSelect.length; i++) {
          this.multipleSelect[i].status = this.multipleSelect[i].openFlag === 1 ? '开启' : '停用'
          downIds.push(this.multipleSelect[i].id);
        }
        isExport = false;
      } else {
        // 导出全部信息
        for (let i = 0; i < this.tableData.length; i++) {
          this.tableData[i].status = this.tableData[i].openFlag === 1 ? '开启' : '停用'
          downIds.push(this.tableData[i].id);
        }
        isExport = true;
      }
      if (downIds.length > 0) {
        this.loading = this.$loading({
          lock: true,
          text: "文件导出中....",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
        });
        let params = {
          content:this.pageInfo.content,
          openFlag: this.pageInfo.openFlag
        }

        let tenantUrl = localStorage.getItem('tenantUrl');
        axios({
          method: 'post',
          url: tenantUrl + "/" + url.shopExport,
          data: params,
          responseType: 'arraybuffer',
          headers: {
            'Content-Type': 'application/json',
            token: getToken()
          }
        }).then((res) => {
          if (res) {
            const content = res.data;
            let blob = new Blob([content], {
              type: "application/ms-excel",
            });1
            let url = window.URL.createObjectURL(blob);
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "店铺信息.xls");
              document.body.appendChild(link);
              link.click();
            } else {
              navigator.msSaveBlob(blob, "店铺信息.xls");
            }
          } else {
            this.$messag.error("导出失败！");
          }
        })
        .finally(() => {
          this.loading.close();
        });
      } else {
        this.$message({
          type: "error",
          message: "未勾选数据",
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
    // 去重
    setArr(arr) {
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
    // 下载二维码
    handleDownload(row) {
      var userAgent = navigator.userAgent; // 取得浏览器的 userAgent 字符串
      var isOpera = userAgent.indexOf("Opera") > -1;
      let image = new Image();
      // 解决跨域 Canvas 污染问题
      image.setAttribute("crossOrigin", "anonymous");
      image.src = row.qrUrl
      image.onload = function () {
        let url = row.qrUrl
        if (
          userAgent.indexOf("Trident") > -1 ||
          (userAgent.indexOf("compatible") > -1 &&
            userAgent.indexOf("MSIE") > -1 &&
            !isOpera)
        ) {
          // 判断是否 Edge/IE 浏览器
          var bstr = atob(url.split(",")[1]);
          var n = bstr.length;
          var u8arr = new Uint8Array(n);
          while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
          }
          // 创建blob对象
          var blob = new Blob([u8arr]);
          // 调用浏览器的方法，调起 IE 的下载流程
          window.navigator.msSaveOrOpenBlob(blob, row.pictureName + ".png");
        } else {
          let a = document.createElement("a"); // 生成一个 a 元素
          let event = new MouseEvent("click"); // 创建一个单击事件
          a.download = row.pictureName || "photo"; // 设置图片名称
          a.href = url; // 将生成的 URL 设置为 a.href 属性
          a.dispatchEvent(event); // 触发 a 的单击事件
        }
      };
    },
    // 更改状态
    handleChangeStatus (row) {
      let status = row.openFlag === 1 ? 0 : 1
      // editStore(this, {
      this.$http.shopStatus(this, {  
        id: row.id,
        openFlag: status
      }).then(res => {
        if (res.success) {
          this.$message.success('更新成功！')
          this.initData()
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 编辑
    handleEdit(row) {
      this.$router.push({ name: "storeDetail", query: { id: row.id } });
    },
    // 删除
    handleDelete(row) {
      if (row.id) {
        this.$confirm("此操作将删除已选数据，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then((_) => {
          // deleteStore(this, {
          this.$http.delShop(this, {  
            id: row.id
          }).then(res => {
            if (res.success) {
              this.$message({
                type: 'success',
                message: '删除成功'
              })
              this.initData()
            } else {
              this.$message({
                type: 'error',
                message: '删除失败'
              })
              .catch((err) => {
                console.log(err);
              });
            }
          })
          .catch((_) => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      })
      }
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
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.store-manage {
  .header {
    .upload-import {
      float: right;
    }
  }
  /deep/.el-table {
    .cell {
      canvas {
        width: 80px;
        height: 80px;
      }
    }
  }
  /deep/.el-select {
    float: right;
  }
  /deep/.box-search {
    margin-left: 20px;
    float: right;
  }
  /deep/.box-input {
    width: 300px;
    margin-left: 20px;
    float: right;
  }
}
</style>