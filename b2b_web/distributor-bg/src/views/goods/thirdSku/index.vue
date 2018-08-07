<!--
 * @Author: yaowei
 * @Date: 2018-05-17 13:40:02
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-17 17:45:01
-->
<template>
  <div class="table-wrap">
    <header>
      <h4>第三方材质型号关联</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleImport()">导入</el-button>
    </header>

    <div class="function">
      <div class="Fheader">
        <div class="Fleft">
          <button class="mini-delete-btn box-btn" @click="handleDelete()">
            删除
          </button>
          <button class="mini-search-btn box-btn" @click="handleExport()">
            导出全部
          </button>
          <el-select size="mini" v-model="pageInfo.openFlag" placeholder="启用状态" style="width: 120px" @change="Csearch()"
            clearable>
            <el-option label="启用" :value="1"></el-option>
            <el-option label="停用" :value="0"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="pageInfo.distributorId"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入分销商ID"
            class="box-input"
          ></el-input>
        </div>
      </div>
    </div>

    <el-row v-loading="loading">
      <el-table :data="tableData" header-row-class-name="header-row" row-key="id" border class="tableCenter"
        ref="multipleSelect" @select="select" @select-all="selectAll" @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="50" reserve-selection></el-table-column>
        <el-table-column label="分销商用户名" align="center" prop="distributorName" :min-width="120" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="分销商编码" align="center" prop="distributorId" :min-width="110" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="材料类型" align="center" prop="categoryName" :min-width="100" show-overflow-tooltip></el-table-column>
        <el-table-column label="材料类型编码" align="center" prop="categoryId" :min-width="120" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方材料类型" align="center" prop="materialCategoryName" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方材料类型编码" align="center" prop="materialCategoryNo" :min-width="160" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="材料名称分类" align="center" prop="parentMaterialName" :min-width="120" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="材料名称" align="center" prop="materialName" :min-width="100" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="材料编码" align="center" prop="materialNo" :min-width="100" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方材质名称" align="center" prop="thirdMaterialName" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方材质编码" align="center" prop="thirdMaterialNo" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方颜色名称" align="center" prop="colourName" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方颜色编码" align="center" prop="colourNo" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="型号名称" align="center" prop="modelName" :min-width="100" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="型号编码" align="center" prop="modelNo" :min-width="100" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方品牌名称" align="center" prop="brandName" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方系列" align="center" prop="series" :min-width="110" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方机型" align="center" prop="thirdModelName" :min-width="110" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方机型编码" align="center" prop="thirdModelNo" :min-width="130" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="第三方SKU" align="center" prop="thirdSkuNo" :min-width="120" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="状态" align="center" :min-width="80" prop="openFlag">
          <template slot-scope="scope">
            <span v-if="Number(scope.row.openFlag) === 1">启用</span>
            <span v-else-if="Number(scope.row.openFlag) === 0">停用</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="150" align="center">
          <template slot-scope="scope">
            <el-button class="mini-browse-btn" v-if="Number(scope.row.openFlag) === 0"
              @click="handleStatus(scope.row.id, 1)">启用
            </el-button>
            <el-button class="mini-pulloff-btn" v-else-if="Number(scope.row.openFlag) === 1"
              @click="handleStatus(scope.row.id, 0)">停用</el-button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
    </el-row>
  </div>
</template>

<script>
  import axios from "axios";
  import url from "@/api/allUrl";
  import { getToken } from "@/utils/auth";
  import page from "@/components/pagination";
  export default {
    name: "thirdSkuList",
    components: {
      page
    },
    data() {
      return {
        pageInfo: {
          distributorId: "",
          openFlag: 1,
          size: 10,
          page: 1,
        },
        tableData: [],
        total: 0,
        multipleSelect: [],
        loading: false,
      };
    },
    mounted() {
      this.initData();
    },
    methods: {
      initData() {
        this.loading = true;
        this.$http.thirdSkuList(this, this.pageInfo).then(res => {  
          this.loading = false;
          if (res.success) {
            this.tableData = res.data.list;
            this.total = res.data.total;
          }
        });
      },
      // 输入框输入搜索
      contentChange(val) {
        if (val === undefined || val === "" || val === null) {
          this.Csearch();
        }
      },
      Csearch() {
        this.pageInfo.page = 1;
        this.initData();
      },
      // 导入
      handleImport() {
        this.$router.push({
          name: "thirdSkuImport"
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
      // 启用/禁用
      handleStatus(id, status) {
        this.$http.thirdSkuStatus(this, {id:id, openFlag:status}).then(res => {  
          if (res.success) {
            this.$message({
              type: "success",
              message: status === 1 ? '启用成功' : '停用成功',
            });
            this.initData();
          }
        });
      },
      // 删除
      handleDelete(id) {
        let idList = [];
        if (id) {
          // 单个
          idList.push(id)
        } else {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            idList.push(this.multipleSelect[i].id);
          }
        }
        if (idList.length > 0) {
          this.$confirm("此操作将删除已选第三方材质型号关联，是否继续？", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
              center: true,
            })
            .then((_) => {
              this.$http.delThirdSku(this, { 
                idList: idList
              }).then((res) => {
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
            message: "未勾选数据",
          });
        }
      },
      // 导出全部
      handleExport() {
        let openFlag = this.pageInfo.openFlag;
        let distributorId = this.pageInfo.distributorId;
        let tenantUrl = localStorage.getItem('tenantUrl');
        axios({
          method: "post",
          url: tenantUrl + "/" + url.thirdSkuExport + "?openFlag=" + openFlag + "&distributorId=" + distributorId,
          responseType: "arraybuffer",
          headers: {
            "Content-Type": "application/json",
            token: getToken(),
          },
        }).then((res) => {
          const content = res.data;
          let blob = new Blob([content], {
            type: "application/ms-excel"
          })
          let url = window.URL.createObjectURL(blob)
          let fileName = distributorId ? "_" + distributorId : '';
          if ('download' in document.createElement('a')) {
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', '第三方材质型号关联' + fileName + '.xls')
            document.body.appendChild(link)
            link.click()
          } else {
            navigator.msSaveBlob(blob, '第三方材质型号关联' + fileName + '.xls')
          }
        })
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

  .table-wrap {
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
