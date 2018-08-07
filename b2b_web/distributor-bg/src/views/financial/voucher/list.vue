<template>
  <div class="list-wrap">
    <header>
      <h4>代金券列表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleAdd()"
      >
        创建新券
      </el-button>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-upload2"
        @click="handleExport()"
      >
        批量导入
      </el-button>
    </header>
    <div class="search-header">
      <el-select
        size="mini"
        v-model="pageInfo.voucherStatus"
        placeholder="状态"
        style="width: 140px"
        @change="filter()"
        clearable
      >
        <el-option
          v-for="(item, index) in statusList"
          :key="index"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-date-picker
        v-model="pageInfo.validTime"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="timestamp"
        size="mini"
        @input="handleDate"
      ></el-date-picker>
      <div class="search-block">
        <el-select
          size="mini"
          v-model="pageInfo.contentType"
          style="width: 140px"
          clearable
        >
          <el-option label="分销商用户名" :value="4"> </el-option>
          <el-option label="券号" :value="2"> </el-option>
        </el-select>
        <el-input
          class="box-input"
          size="mini"
          clearable
          @change="contentChange"
          placeholder="请输入关键字搜索"
          v-model.trim="pageInfo.content"
        ></el-input>
        <button
          class="mini-search-btn box-btn"
          size="mini"
          @click.prevent="filter()"
        >
          搜索
        </button>
      </div>
    </div>
    <el-table
      :data="tableData"
      border
      header-row-class-name="header-row"
      class="tableCenter"
    >
      <el-table-column
        align="center"
        label="所属分销商"
        prop="distributorName"
        :min-width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="券号"
        prop="voucherNo"
        :min-width="180"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="券名"
        prop="name"
        :min-width="150"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        align="center"
        label="面值"
        prop="faceValue"
        :min-width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>¥{{ scope.row.faceValue }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="余额"
        prop="balance"
        :min-width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>¥{{ scope.row.balance }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="有效期"
        :min-width="300"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span
            >{{ timeFormatter(scope.row.startTime) }} ~
            {{ timeFormatter(scope.row.endTime) }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="备注"
        prop="remark"
        :min-width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="状态"
        :formatter="formatStatus"
        :min-width="100"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="申请人"
        prop="createUserName"
        :min-width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="申请时间"
        prop="createTime"
        :formatter="formatTime"
        :min-width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="审批人"
        prop="checkUserName"
        :min-width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="审批时间"
        prop="checkTime"
        :formatter="formatTime"
        :min-width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button
            class="mini-search-btn"
            @click="handleHistory(scope.row.id)"
            >使用记录</el-button
          >
          <el-button
            v-if="
              scope.row.voucherStatus === 4 || scope.row.voucherStatus === 5
            "
            class="mini-delete-btn"
            @click="handleStatus(scope.row.id, 11)"
            >冻结</el-button
          >
          <el-button
            v-if="scope.row.voucherStatus === 11"
            class="mini-delete-btn"
            @click="handleStatus(scope.row.id, 10)"
            >解冻</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <page
      :page="pageInfo.page"
      :total="total"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    ></page>

    <!-- 批量导入 -->
    <el-dialog
      title="批量导入"
      :visible.sync="exprotDialog"
      width="520px"
      center
    >
      <div class="dialog-content">
        <span class="lable">请选择文件：</span>
        <el-upload
          drag
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
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            <el-link type="primary" @click="getDownloadTemp">点击下载批量导入模板</el-link>
          </div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="exprotDialog = false">返 回</el-button>
        <el-button type="primary" size="small" @click="exprotDialog = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>

    <!-- 创建新券 -->
    <el-dialog
      title="创建代金券"
      :visible.sync="voucherDialog"
      width="80%"
      center
      :before-close="handleCloseVoucher"
    >
      <div class="add-voucher">
        <el-button type="primary" size="small" @click="handleAddVocher()"
          >新增</el-button
        >
        <el-form
          :model="ruleTable"
          :rules="rules"
          label-position="right"
          ref="ruleTable"
        >
          <el-table
            :data="ruleTable.tableData"
            header-row-class-name="header-row"
            border
            class="tableCenter"
          >
            <el-table-column align="center" :width="200">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>所属分销商</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  :prop="'tableData.' + scope.$index + '.distributor'"
                  :rules="rules.distributor"
                >
                  <el-autocomplete
                    size="mini"
                    v-model="scope.row.distributor"
                    :fetch-suggestions="querySearch"
                    :trigger-on-focus="false"
                    placeholder="请输入分销商名称搜索"
                    @focus="handleFocus(scope.$index)"
                    @select="handleSelect"
                  ></el-autocomplete>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column align="center" :min-width="150">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>券名</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  :prop="'tableData.' + scope.$index + '.name'"
                  :rules="rules.name"
                >
                  <el-input
                    size="mini"
                    v-model="scope.row.name"
                    placeholder="请输入"
                  ></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column align="center" :min-width="150">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>代金券面值</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  :prop="'tableData.' + scope.$index + '.faceValue'"
                  :rules="rules.faceValue"
                >
                  <el-input
                    size="mini"
                    v-model="scope.row.faceValue"
                    placeholder="请输入"
                    onkeyup="var p2 = parseFloat(value).toFixed(2);value = p2>=0?(/\.0?$/.test(value)?value:p2.replace(/0$/,'').replace(/\.0$/,'')):''"
                    onblur="value = value.replace(/\.0*$/,'')"
                  ></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column align="center" :min-width="420">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>有效期</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  :prop="'tableData.' + scope.$index + '.validTime'"
                  :rules="rules.validTime"
                >
                  <el-date-picker
                    size="mini"
                    v-model="scope.row.validTime"
                    type="datetimerange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="timestamp"
                    :picker-options="expireTimeOption"
                    @focus="handleFocus(scope.$index)"
                    @input="handleVoucherDate"
                  ></el-date-picker>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column align="center" :min-width="220">
              <template slot="header">
                <!-- <span class="table-slot">*</span> -->
                <span>备注</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  :prop="'tableData.' + scope.$index + '.remark'"
                  :rules="rules.remark"
                >
                  <el-input
                    size="mini"
                    v-model="scope.row.remark"
                    placeholder="请输入"
                  ></el-input>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleCloseVoucher">返 回</el-button>
        <el-button type="primary" size="small" @click="handleConfirmAddVoucher"
          >确 定</el-button
        >
      </span>
    </el-dialog>

    <!-- 使用记录 -->
    <el-dialog
      title="使用记录"
      :visible.sync="historyDialog"
      width="80%"
      center
    >
      <div class="history-wrap">
        <el-table
          :data="historyData"
          header-row-class-name="header-row"
          border
          class="tableCenter"
        >
          <el-table-column
            align="center"
            label="时间"
            prop="useTime"
            :formatter="formatTime"
            width="160"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="订单号"
            prop="orderNo"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="使用金额"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.amountFlag }} ¥{{ scope.row.useAmount }}</span>
              <span v-if="scope.row.amountFlag === '+'">(退还)</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="余额"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>¥{{ scope.row.balance }}</span>
            </template>
          </el-table-column>
        </el-table>
        <page
          :page="historyInfo.page"
          :total="historyTotal"
          @sizeChange="historySizeChange"
          @currentChange="historyCurrentChange"
        ></page>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="historyDialog = false">返 回</el-button>
        <el-button type="primary" size="small" @click="historyDialog = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { timeFormat } from "@/utils/timeFormat";
import { parseTime } from "@/utils/index";
import page from "@/components/pagination";

import { getToken } from "@/utils/auth";

import url from '@/api/allUrl'
export default {
  name: "voucherList",
  data() {
    return {
      tableData: [],
      total: 0,
      pageInfo: {
        page: 1,
        size: 10,
        voucherStatus: "",
        validTime: [],
        startTime: "",
        endTime: "",
        contentType: 4,
        content: "",
      },
      loading: false,
      // 状态
      statusList: [
        {
          label: "可用",
          value: 5,
        },
        {
          label: "已过期",
          value: 7,
        },
        {
          label: "已用完",
          value: 9,
        },
        {
          label: "待审核",
          value: 1,
        },
        {
          label: "审核拒绝",
          value: 3,
        },
        {
          label: "已冻结",
          value: 11,
        },
        {
          label: "待生效",
          value: 4,
        },
      ],
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      // 批量导入
      exprotDialog: false, // 批量导入弹窗
      importHeaders: {
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        Authorization: "",
        token: "",
      },
      action: process.env.BASE_API + url.voucherImport,
      // 创建新券
      voucherDialog: false, // 创建新券弹窗
      ruleTable: {
        tableData: [],
      },
      rules: {
        distributor: [
          {
            required: true,
            message: "请选择分销商",
            trigger: ["blur", "change"],
          },
        ],
        name: [
          {
            required: true,
            message: "请输入券名",
            trigger: ["blur", "change"],
          },
        ],
        faceValue: [
          {
            required: true,
            message: "请输入代金券面值",
            trigger: ["blur", "change"],
          },
        ],
        validTime: [
          {
            required: true,
            message: "请设置有效期",
            trigger: ["blur", "change"],
          },
        ],
      },
      curEditIndex: 0, // 当前编辑券索引
      // 使用记录
      historyDialog: false, // 使用记录弹窗
      historyData: [],
      historyTotal: 0,
      historyInfo: {
        rebateVoucherId: "",
        page: 1,
        size: 10,
      },
    };
  },
  components: {
    page,
  },
  created() {
    this.importHeaders.Authorization = getToken();
    this.importHeaders.token = getToken();

    this.init();
  },
  methods: {
    // 监听时间选择
    handleDate(value) {
      this.pageInfo.validTime = value;
      this.pageInfo.startTime = timeFormat(value[0]);
      this.pageInfo.endTime = timeFormat(value[1]);
      this.$forceUpdate();
    },
    // 表格时间格式化
    formatTime(row, col, val) {
      return timeFormat(val);
    },
    timeFormatter(cellValue) {
      return parseTime(cellValue);
    },
    // 状态格式化
    formatStatus(row) {
      switch (row.voucherStatus) {
        case 1:
          return "待审核";
        case 3:
          return "审核拒绝";
        case 4:
          return "待生效";
        case 5:
          return "可用";
        case 7:
          return "已过期";
        case 9:
          return "已用完";
        case 11:
          return "已冻结";
        default:
          return row.voucherStatus;
      }
    },
    // ======== 操作 ========
    filter() {
      // 搜索操作
      this.pageInfo.page = 1;
      this.init();
    },
    contentChange(val) {
      // 输入框输入
      if (val === undefined || val === "" || val === null) {
        this.init();
      }
    },

    // 批量导入
    handleExport() {
      this.exprotDialog = true;
    },
    getDownloadTemp() {
      // 下载模板
      this.$http
        .voucherDownloadTemp(this, {})
        .then((res) => {
          if (res.success) {
            let url = res.data;
            if ("download" in document.createElement("a")) {
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.download = "代金券-批量导入.xls";
              document.body.appendChild(link);
              link.click();
            }
          }
        });
    },
    beforeUpload(file) {
      // 上传前的配置
      let excelfileExtend = ".xls,.xlsx"; //设置文件格式
      let fileExtend = file.name
        .substring(file.name.lastIndexOf("."))
        .toLowerCase();
      if (excelfileExtend.indexOf(fileExtend) <= -1) {
        this.$message.error("只能上传.xls,.xlsx格式");
        return false;
      }
      this.loading = this.$loading({
        lock: true,
        text: "文件正在处理中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    onProgress(event, file, fileList) {
      // 上传时的钩子
      this.loading = this.$loading({
        lock: true,
        text: "文件上传中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    uploadFail(err, file, fileList) {
      //上传错误
      this.$message.error({
        message: err.errMessage,
        duration: 3 * 1000,
        onClose: () => {},
      });
      this.loading.close();
    },
    uploadSuccess(response, file, fileList) {
      //上传成功
      if (response.success) {
        this.$message.success({
          message: "上传成功",
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.init();
        this.exprotDialog = false;
        this.loading.close();
      } else {
        this.$message.error({
          message: response.errMessage,
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.loading.close();
      }
    },

    // 创建新券
    handleAdd() {
      this.voucherDialog = true;
    },
    handleAddVocher() {
      // 新增
      let voucherData = {
        distributor: "",
        name: "",
        faceValue: "",
        validTime: "",
        remark: "",
      };

      this.ruleTable.tableData.push(voucherData);
    },
    // 查询合作中分销商列表
    querySearch(queryString, cb) {
      if (queryString != "") {
        let callBackArr = [];
        // 多级分销商
        this.$http
          .getDistributorNPoList(this, {
            page: 1,
            size: 1000,
            contentType: 1,
            content: queryString,
            erpFlag: 1, // 信息是否同步到erp: 1 是， 0 否
          })
          .then((res) => {
            const ary = res.data.list;
            ary.forEach((item) => {
              this.$set(item, "value", item.name);
            });
            ary.forEach((item) => {
              if (
                item.name.indexOf(queryString) > -1
              ) {
                callBackArr.push(item);
              }
            });
            if (callBackArr.length == 0) {
              cb([{ value: "暂无数据" }]);
            } else {
              cb(callBackArr);
            }
          });
      }
    },
    // 当前编辑券
    handleFocus(index) {
      this.curEditIndex = index;
    },
    // 获取点击分销商信息
    handleSelect(item) {
      this.ruleTable.tableData[this.curEditIndex].distributorId = item.id;
      this.ruleTable.tableData[this.curEditIndex].distributorName = item.name;
    },
    // 代金券有效期
    handleVoucherDate(value) {
      this.ruleTable.tableData[this.curEditIndex].validTime = value;
      this.$forceUpdate();
      this.ruleTable.tableData[this.curEditIndex].startTime = value[0];
      this.ruleTable.tableData[this.curEditIndex].endTime = value[1];
      console.log(this.ruleTable.tableData);
    },
    // 新增代金券 - 返回/关闭
    handleCloseVoucher() {
      // 重置表单验证
      this.$refs["ruleTable"].resetFields();
      this.ruleTable.tableData = [];
      this.voucherDialog = false;
    },
    // 新增代金券 - 确定
    handleConfirmAddVoucher() {
      this.$refs["ruleTable"].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.$http
            .batchCreateVoucher(this, {
              rebateVoucherCmdList: this.ruleTable.tableData,
            })
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.init();
                this.voucherDialog = false;
              }
            });
        }
      });
    },

    // 使用记录
    handleHistory(id) {
      this.historyInfo.rebateVoucherId = id;
      this.handleHistoryData(true);
    },
    handleHistoryData(type) {
      this.$http.getVoucherUsedList(this, this.historyInfo).then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let historyData = res.data.list;
            historyData.forEach((item) => {
              this.$set(item, "amountFlag", "+")
              if (item.useAmount < 0) {
                this.$set(item, "amountFlag", "-")
                this.$set(item, "useAmount", -item.useAmount)
              }
            })

            this.historyData = historyData;
            this.historyTotal = res.data.total;
            if (type) {
              // 点击使用记录按钮时，弹窗显示
              this.historyDialog = true;
            }
          } else {
            this.$message({
              type: "info",
              message: "暂无使用记录~",
            });
          }
        }
      });
    },
    historySizeChange(size) {
      this.historyInfo.size = size;
      this.historyInfo.page = 1;
      this.handleHistoryData();
    },
    historyCurrentChange(page) {
      this.historyInfo.page = page;
      this.handleHistoryData();
    },

    // 冻结/解冻
    handleStatus(id, status) {
      let msg = "";
      if (status === 11) {
        // 冻结
        msg = "此操作将冻结该代金券, 是否继续?";
      } else if (status === 10) {
        // 解冻
        msg = "此操作将解冻该代金券, 是否继续?";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$http
            .handleVoucherStatus(this, {
              id: id,
              freezeStatus: status,
            })
            .then((res) => {
              if (res.success) {
                this.$message({
                  message: "操作成功",
                  type: "success",
                  duration: 3 * 1000,
                });
                this.pageInfo.page = 1;
                this.init();
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    // ======== 数据 ========
    init() {
      // 列表数据
      this.$http.getVoucherList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.init();
    },

    currentChange(page) {
      this.pageInfo.page = page;
      this.init();
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.list-wrap {
  background-color: white;
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
  .header {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #ccc;
    button {
      margin-left: 10px;
    }
  }
  .search-header {
    width: 100%;
    padding: 10px;
    overflow: hidden;
    .search-block {
      float: right;
      .box-input {
        width: 215px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
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

.search-block {
  display: flex;
  font-size: 0;
  /deep/ .el-select {
    .el-input__inner {
      border-right: 0;
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
    }
  }
  /deep/ .box-input {
    margin-right: 5px;
    .el-input__inner {
      border-top-left-radius: 0;
      border-bottom-left-radius: 0;
    }
  }
}

/deep/ .el-dialog {
  .el-dialog__header {
    text-align: left;
  }

  .el-dialog__footer {
    text-align: right;
  }

  .dialog-content {
    position: relative;
    padding-left: 100px;

    .lable {
      position: absolute;
      top: 0;
      left: 0;
    }
  }

  .el-upload__tip {
    margin-top: 15px;
  }

  .add-voucher {
    .el-button {
      margin-bottom: 15px;
    }
    .el-table {
      .cell div {
        padding-right: 0;
      }
      .el-form-item {
        margin: 0;
        &.is-error {
          margin-bottom: 22px;
        }
      }
    }

    .table-slot {
      color: #f00;
    }
  }
}
</style>