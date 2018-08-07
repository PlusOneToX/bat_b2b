<!--
 * @Author: yaowei
 * @Date: 2018-05-25 14:03:37
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-06 09:46:20
-->
<template>
  <div
    class="code-list-wrap"
    v-loading="exportDownloadLoading"
    element-loading-text="正在导出..."
  >
    <div class="Fheader">
      <div class="Fleft">
        <el-button class="mini-search-btn" @click="handleExport"
          >导出券码</el-button
        >
        <el-button
          class="mini-freeze-btn"
          v-if="enterFlag !== 'exchangeCode'"
          @click="handleCode"
          >生成券码</el-button
        >
        <el-button
          size="mini"
          :class="{ 'mini-freeze-btn': enterFlag === 'exchangeCode' }"
          @click="handleInvalidCodeMore"
          >批量作废</el-button
        >
        <el-select
          v-if="enterFlag === 'exchangeCode'"
          size="mini"
          v-model="codeInfo.exchangeWay"
          placeholder="兑换形式"
          style="width: 100px"
          @change="searchCode()"
          clearable
        >
          <el-option label="兑换" :value="1"></el-option>
          <el-option label="权益" :value="2"></el-option>
        </el-select>
        <el-select
          size="mini"
          v-model="codeInfo.status"
          placeholder="券码状态"
          style="width: 100px"
          @change="searchCode()"
          clearable
        >
          <el-option label="未激活" :value="0"></el-option>
          <el-option label="未使用" :value="1"></el-option>
          <el-option label="已核销" :value="2"></el-option>
          <el-option label="已作废" :value="4"></el-option>
          <el-option label="已过期" :value="3"></el-option>
        </el-select>
        <button
          class="mini-browse-btn box-btn"
          @click="getEncode(curActivityId)"
        >
          查看暗码
        </button>
      </div>
      <div class="Fsearch">
        <button class="mini-search-btn box-btn" @click="searchCode()">
          搜索
        </button>
        <el-input
          v-model.trim="codeInfo.content"
          size="mini"
          clearable
          @change="codeChange"
          @keyup.enter.native="searchCode()"
          :placeholder="
            enterFlag === 'exchangeCode'
              ? '请输入明码/盒码/批次ID/核销人/核销订单ID'
              : '请输入明码/盒码/批次ID'
          "
          class="box-input"
        ></el-input>
      </div>
    </div>
    <el-table
      :data="codeData"
      header-row-class-name="header-row"
      border
      class="tableCenter"
      ref="multipleSelect"
      row-key="id"
      @select="select"
      @select-all="selectAll"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        align="center"
        type="selection"
        width="50"
        reserve-selection
        :selectable="selectable"
      ></el-table-column>
      <el-table-column
        label="兑换码(暗码)"
        align="center"
        prop="secretCode"
        :min-width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="卡片码(明码)"
        align="center"
        prop="plainCode"
        :min-width="140"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="包装码(盒码)"
        align="center"
        prop="boxCode"
        :min-width="140"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="批次ID"
        align="center"
        prop="exchangeFactoryId"
        :min-width="80"
      ></el-table-column>
      <el-table-column
        v-if="enterFlag === 'exchangeCode'"
        label="活动名称"
        align="center"
        prop="cardName"
        :min-width="140"
      ></el-table-column>
      <el-table-column
        v-if="enterFlag === 'exchangeCode'"
        label="兑换形式"
        align="center"
        :min-width="140"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.exchangeWay === 1">兑换</span>
          <span v-else-if="scope.row.exchangeWay === 2">权益</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="enterFlag !== 'exchangeCode'"
        label="生成时间"
        align="center"
        prop="createTime"
        :formatter="formatTime"
        :min-width="160"
      ></el-table-column>
      <el-table-column
        label="券码状态"
        align="center"
        :min-width="120"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">未激活</span>
          <span v-else-if="scope.row.status === 1">未使用</span>
          <span v-else-if="scope.row.status === 2">已核销</span>
          <span v-else-if="scope.row.status === 4">已作废</span>
          <span v-else-if="scope.row.status === 3">已过期</span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="enterFlag === 'exchangeCode'"
        label="核销人"
        align="center"
        prop="userName"
        :min-width="140"
      ></el-table-column>
      <el-table-column
        v-if="enterFlag === 'exchangeCode'"
        label="核销订单ID"
        align="center"
        prop="userOrderId"
        :min-width="140"
      ></el-table-column>
      <el-table-column
        v-if="enterFlag === 'exchangeCode'"
        label="最后操作时间"
        align="center"
        prop="updateTime"
        :formatter="formatTime"
        :min-width="140"
      ></el-table-column>
      <el-table-column label="操作" :min-width="140" align="center">
        <template slot-scope="scope">
          <el-button
            class="mini-search-btn"
            @click="handleInvalidCode(scope.row.id)"
            v-if="scope.row.status === 0 || scope.row.status === 1"
            >作废</el-button
          >
          <el-button
            class="mini-freeze-btn"
            @click="handleUnbound(scope.row)"
            v-if="scope.row.status === 1"
            >解绑</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <page
      :total="codeTotal"
      :page="codeInfo.page"
      @sizeChange="codeSizeChange"
      @currentChange="codeCurrentChange"
    ></page>
  </div>
</template>

<script>
import axios from "axios";
import url from "@/api/allUrl";
import { getToken } from "@/utils/auth";
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  name: "codeList",
  props: ["curActivityId", "enterFlag", "exchangeExportId", "exchangeWay"],
  components: { page },
  data() {
    return {
      codeData: [], // 码库列表
      codeInfo: {
        // 码库搜索
        page: 1,
        size: 10,
        type: 1,
        exchangeWay: 1,
        status: "",
        content: "",
        exchangeId: "",
        exchangeFactoryId: "",
        exchangeExportId: "", // 电子兑换码记录id
      },
      codeTotal: 0, // 码库总数
      multipleSelect: [], // 已选择码库
      curId: "",
      exportDownloadLoading: false,
    };
  },
  created() {
    if (this.curActivityId) {
      this.handleCodeList(this.curActivityId);
    } else if (this.exchangeExportId) {
      this.handleCodeList(this.exchangeExportId);
    } else {
      this.handleCodeList(this.curId);
    }
  },
  activated() {
    if (this.curActivityId) {
      this.handleCodeList(this.curActivityId);
    } else if (this.exchangeExportId) {
      this.handleCodeList(this.exchangeExportId);
    } else {
      this.handleCodeList(this.curId);
    }
  },
  methods: {
    // 码库列表
    handleCodeList(id) {
      this.curId = id;
      this.initCodeData(id);
    },
    // 码库数据初始化
    initCodeData(id) {
      this.loading = true;
      if (this.curActivityId) {
        this.codeInfo.exchangeId = id;
      }
      if (this.exchangeExportId) {
        this.codeInfo.exchangeExportId = id; // 电子兑换码记录id
      }
      if (this.enterFlag !== "exchangeCode") {
        this.codeInfo.exchangeWay = this.exchangeWay;
      }
      this.$http.exchangeCodeList(this, this.codeInfo).then((res) => {
        if (res.success) {
          this.loading = false;
          this.codeData = res.data.list;
          this.codeTotal = res.data.total;
        }
      });
    },
    // 搜索码库列表
    searchCode() {
      this.codeInfo.page = 1;
      this.initCodeData(this.curId);
    },
    // 输入框输入搜索码库列表
    codeChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.searchCode();
      }
    },
    // 表格时间格式化
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    // 码库条数
    codeSizeChange(size) {
      this.codeInfo.size = size;
      this.codeInfo.page = 1;
      if (this.curActivityId) {
        this.initCodeData(this.curActivityId);
      } else if (this.exchangeExportId) {
        this.initCodeData(this.exchangeExportId);
      } else {
        this.initCodeData(this.curId);
      }
    },
    // 码库页数
    codeCurrentChange(page) {
      this.codeInfo.page = page;
      if (this.curActivityId) {
        this.initCodeData(this.curActivityId);
      } else if (this.exchangeExportId) {
        this.initCodeData(this.exchangeExportId);
      } else {
        this.initCodeData(this.curId);
      }
    },
    // 码库 - 作废
    handleInvalidCode(id) {
      this.$prompt("请输入作废说明", "", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator(value) {
          if (value) {
            return true;
          } else {
            return false;
          }
        },
        inputErrorMessage: "请输入作废说明",
      })
        .then(({ value }) => {
          this.loading = true;
          this.$http
            .exchangeCodeInvalid(this, { id: id, reason: value })
            .then((res) => {
              if (res.success) {
                this.loading = false;
                this.$message({
                  type: "success",
                  message: "作废成功",
                });
                this.multipleSelect = [];
                this.$refs.multipleSelect.clearSelection();
                this.initCodeData(this.curId);
              } else {
                this.$message({
                  type: "error",
                  message: res.errMessage,
                });
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 码库 - 批量作废
    handleInvalidCodeMore() {
      let ids = [];
      for (let i = 0; i < this.multipleSelect.length; i++) {
        ids.push(this.multipleSelect[i].id);
      }
      if (ids.length > 0) {
        this.$prompt("请输入作废说明", "", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputValidator(value) {
            if (value) {
              return true;
            } else {
              return false;
            }
          },
          inputErrorMessage: "请输入作废说明",
        })
          .then(({ value }) => {
            this.loading = true;
            this.$http
              .exchangeCodeInvalidBatch(this, { idList: ids, reason: value })
              .then((res) => {
                if (res.success) {
                  this.loading = false;
                  this.$message({
                    type: "success",
                    message: "作废成功",
                  });
                  this.multipleSelect = [];
                  this.$refs.multipleSelect.clearSelection();
                  this.initCodeData(this.curId);
                } else {
                  this.$message({
                    type: "error",
                    message: res.errMessage,
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消操作",
            });
          });
      } else {
        this.$message({
          type: "error",
          message: "未勾选兑换码",
        });
      }
    },
    selectable(row) {
      // 已核销/已作废/已过期
      if (row.status === 0 || row.status === 1) {
        return true;
      } else {
        return false;
      }
    },
    // 码库 - 单选时调用
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
    // 码库 - 全选时调用
    selectAll(selection) {
      this.isSelect = true;
      if (selection.length === 0) {
        this.codeData.forEach((row) => {
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
    // 码库 - 当切换页面时的作用
    handleSelectionChange(val) {
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.codeData.forEach((row2) => {
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
    // 生成券码
    handleCode() {
      this.$prompt("请输入生成兑换码数量", "", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValidator(value) {
          var val = Number(value);
          if (val && Number.isInteger(val) && val <= 10000 && val >= 1) {
            return true;
          } else {
            return false;
          }
        },
        inputErrorMessage: "单次生成兑换码数量应大于1，小于等于10000个",
      })
        .then(({ value }) => {
          const loading = this.$loading({
            lock: true,
            text: "券码生成中，请稍后......",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          this.$http
            .createCode(this, {
              exchangeId: this.curActivityId
                ? this.curActivityId
                : this.exchangeExportId,
              count: value,
            })
            .then((res) => {
              if (res.success) {
                loading.close();
                this.$message({
                  type: "success",
                  message: "券码生成成功",
                });
                if (this.curActivityId) {
                  this.initCodeData(this.curActivityId);
                }
                if (this.exchangeExportId) {
                  this.initCodeData(this.exchangeExportId);
                }
                if (this.curActivityId || this.exchangeExportId) {
                  this.$emit("handleInit");
                }
              } else {
                loading.close();
                this.$message({
                  type: "success",
                  message: res.errMessage,
                });
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 导出券码
    handleExport() {
      var params = {
        exchangeId: this.codeInfo.exchangeId
          ? this.codeInfo.exchangeId
          : this.codeInfo.exchangeExportId,
        type: this.codeInfo.type,
        exchangeWay: this.codeInfo.exchangeWay,
        status: this.codeInfo.status,
        content: this.codeInfo.content,
        exchangeFactoryId: this.codeInfo.exchangeFactoryId,
      };
      this.exportDownloadLoading = true;
      let tenantUrl = localStorage.getItem("tenantUrl");
      axios({
        method: "post",
        url: tenantUrl + "/" + url.exchangeCodeExport,
        data: params,
        responseType: "arraybuffer",
        headers: {
          "Content-Type": "application/json",
          token: getToken(),
        },
      }).then((res) => {
        this.exportDownloadLoading = false;
        const content = res.data;
        let blob = new Blob([content], {
          type: "application/ms-excel",
        });
        let url = window.URL.createObjectURL(blob);
        if ("download" in document.createElement("a")) {
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", "兑换码列表.xls");
          document.body.appendChild(link);
          link.click();
        } else {
          navigator.msSaveBlob(blob, "兑换码列表.xls");
        }
      });
    },
    // 查看暗码
    getEncode(id) {
      this.loading = true;
      this.codeInfo.exchangeId = id;
      this.$http.exchangeEncodeList(this, this.codeInfo).then((res) => {
        if (res.success) {
          this.loading = false;
          this.codeData = res.data.list;
          this.codeTotal = res.data.total;
        }
      });
    },
    // 解绑
    handleUnbound(row) {
      this.$confirm("此操作将解绑该兑换码，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http
            .exchangeUnbound(this, { id: row.id, plainCode: row.plainCode })
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "解绑成功",
                });
                this.multipleSelect = [];
                this.$refs.multipleSelect.clearSelection();
                this.initCodeData(this.curId);
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.Fheader {
  padding: 10px 0;
  display: flex !important;
  justify-content: space-between;
  align-items: center !important;
  .Fleft {
    overflow: hidden;
    float: left;
    .el-button {
      margin: 0;
    }
  }
  .Fsearch {
    overflow: hidden;
    float: right;
    .box-input {
      width: 280px;
      float: right;
    }
    .box-btn {
      float: right;
      margin-left: 5px;
    }
  }
}
</style>
