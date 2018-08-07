<template>
  <div class="store-order">
    <div class="store-manage">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="store-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div
          class="
            store-right
            rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double
          "
        >
          <div class="content">
            <h6 class="store-right-title">
              {{ $t("UserCenter.SubSalesman") }}
            </h6>
            <div class="top-box">
              <!-- 新增 -->
              <el-button
                class="mini-search-btn"
                type="info"
                @click="handleAdd"
                size="mini"
                >{{ $t("UserCenter.Add") }}</el-button
              >
              <!-- 下载模板 -->
              <el-button
                class="mini-search-btn"
                type="info"
                @click="handleTemp"
                size="mini"
                >{{ $t("P.DownloadTemplate") }}</el-button
              >
              <!-- 批量导入 -->
              <el-upload
                class="upload-excel"
                :headers="importHeaders"
                :action="action"
                :auto-upload="true"
                :show-file-list="true"
                :before-upload="beforeUpload"
                :on-success="uploadSuccess"
                :on-error="uploadFail"
                :on-progress="onProgress"
              >
                <el-button class="mini-search-btn" type="info">
                  {{ $t("UserCenter.BatchImport") }}
                </el-button>
              </el-upload>
              <!-- 绑定二维码 -->
              <el-button
                class="mini-search-btn"
                type="info"
                @click="handleQrcode"
                size="mini"
                >绑定二维码</el-button
              >
            </div>
            <div class="search-box">
              <el-select
                size="mini"
                v-model="pageInfo.searchType"
                placeholder="业务员"
                style="width: 140px; height: 38px"
                clearable
              >
                <el-option label="业务员" value="2"> </el-option>
                <el-option label="手机号" value="3"> </el-option>
              </el-select>
              <el-input
                class="ml-15"
                placeholder="请输入搜索关键字"
                type="text"
                clearable
                v-model.trim="pageInfo.content"
              ></el-input>
              <el-button
                class="mini-search-btn ml-15"
                type="info"
                @click="searchOrder"
                size="mini"
                >{{ $t("P.Search") }}</el-button
              >
              <!-- 导出业务员 -->
              <!-- <div class="handle-wrap rl-tr"  @click="handleExport">
                <i class="iconfont icon-export"></i> 导出业务员
              </div> -->
            </div>
            <div class="query-detail">
              <div class="nav rl-clear">
                <ul class="rl-fl" :class="$i18n.locale === 'en' ? 'navEn' : ''">
                  <li
                    :class="orderStatus === 0 ? 'current' : ''"
                    @click="changeState(0)"
                  >
                    全部
                  </li>
                  <!--级别等级--->
                  <li
                    :class="orderStatus === item.id ? 'current' : ''"
                    v-for="item in levelList"
                    :key="item.id"
                    @click="changeState(item.id)"
                  >
                    {{ item.levelName }}
                  </li>
                </ul>
              </div>
              <div class="table-box">
                <el-table
                  ref="tableRef"
                  :data="orderList"
                  header-row-class-name="header-row"
                  border
                  style="text-align: center"
                  :row-key="getRowKeys"
                  v-loading="loading"
                >
                  <el-table-column
                    align="center"
                    label="ID"
                    prop="id"
                    width="100"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="业务员姓名"
                    prop="salemanName"
                    :min-width="90"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="类型"
                    prop="type"
                    :min-width="70"
                    :formatter="formatType"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('P.Mobile')"
                    prop="mobile"
                    :min-width="120"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="级别"
                    prop="levelName"
                    :min-width="100"
                    show-overflow-tooltip
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="上级业务员名称"
                    prop="parentSalemanName"
                    :min-width="120"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <span>{{
                        scope.row.parentSalemanName
                          ? scope.row.parentSalemanName
                          : "--"
                      }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="openid/商户号"
                    prop="merchantNumber"
                    :min-width="120"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <span>{{
                        scope.row.merchantNumber || scope.row.openId
                          ? scope.row.type == 2
                            ? "已绑定"
                            : scope.row.merchantNumber
                          : "未绑定无法分账"
                      }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('UserCenter.CreationTime')"
                    prop="createTime"
                    :min-width="140"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div style="font-size: 12px">
                        {{ scope.row.createTime }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    :label="$t('OrderSuccess.Status')"
                    prop="openFlag"
                    width="60"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div>{{ getStatus(scope.row.openFlag) }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    key="12"
                    :label="$t('ShopCarts.Operation')"
                    :min-width="140"
                  >
                    <template slot-scope="scope">
                      <span
                        class="log rl-tc cursor-pointer"
                        @click="handleEdit(scope.row)"
                        >{{ $t("ConfirmOrder.Edit") }}</span
                      >
                      <span
                        class="log rl-tc cursor-pointer"
                        @click="onChangeStatus(scope.row)"
                        >{{ getfStatus(scope.row.openFlag) }}</span
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default" v-if="totalCount > 0">
            <el-pagination
              background
              :current-page="pageInfo.page"
              :page-size="pageInfo.count"
              @current-change="onCurrentChange"
              layout=" prev, pager, next , total , jumper"
              :total="totalCount"
              class="page"
            ></el-pagination>
          </div>
        </div>
      </div>
      <!----绑定二维码弹框----->
      <el-dialog
        title="绑定二维码"
        :visible.sync="qrDialogVisible"
        width="30%"
        center
      >
        <div class="qr-content">
          <div class="text">
            个人的微信账号，请引导用户扫码，使用录入系统的手机号验证后，获取到了微信openID才可以参与分账
          </div>
          <div class="img">
            <img :src="codeUrl" alt="" />
          </div>
        </div>
      </el-dialog>
      <!----新增、编辑弹框----->
      <el-dialog
        :title="title"
        :visible.sync="DialogVisible"
        width="50%"
        center
      >
        <div class="add-info rl-padding-top-default">
          <el-form
            :model="formData"
            :rules="rules"
            label-width="25%"
            label-position="right"
            ref="ruleForm"
          >
            <el-row>
              <el-col :span="20">
                <!-- 业务等级 -->
                <el-form-item label="业务等级" prop="levelId">
                  <el-select
                    class="app_select ml-15"
                    placeholder="等级名称"
                    size="mini"
                    v-model="formData.levelId"
                    :disabled="disabled"
                    @change="handleChange"
                  >
                    <el-option
                      v-for="item in levelList"
                      :key="item.id"
                      :label="item.levelName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <!-- 上级业务员 -->
                <el-form-item label="上级业务员" prop="parentId">
                  <el-select
                    class="app_select ml-15"
                    placeholder="上级业务员"
                    size="mini"
                    v-model="formData.parentId"
                    clearable
                  >
                    <el-option
                      v-for="item in salesman"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                  <div>上级业务员请谨慎修改，调整后下级分账会跟着一起变</div>
                </el-form-item>
                <!-- 业务员类型 -->
                <el-form-item label="业务员类型" prop="type">
                  <el-radio-group
                    v-model="formData.type"
                    class="adress-el-radio"
                  >
                    <el-radio :label="1" value="1">企业</el-radio>
                    <el-radio :label="2" value="2">个人</el-radio>
                  </el-radio-group>
                </el-form-item>
                <!-- 姓名 -->
                <el-form-item label="姓名" prop="name">
                  <el-input
                    type="text"
                    v-model="formData.name"
                    placeholder="请输入姓名"
                    maxlength="30"
                  ></el-input>
                </el-form-item>
                <!-- 手机号 -->
                <el-form-item label="手机号" prop="mobile">
                  <el-input
                    type="text"
                    v-model="formData.mobile"
                    placeholder="请输入手机号"
                    maxlength="11"
                  ></el-input>
                </el-form-item>
                <!-- 商户号 -->
                <el-form-item
                  label="商户号"
                  prop="merchantNumber"
                  v-if="formData.type === 1"
                >
                  <el-input
                    type="text"
                    v-model="formData.merchantNumber"
                    placeholder="请输入企业的微信商户号"
                    maxlength="30"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="handleSave">保 存</el-button>
          <el-button @click="DialogVisible = false">取 消</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate } from "@/assets/js/common.js";
import {
  subAccountSaleman,
  subLevelListByCondition,
  subSalemanListByCondition,
  subAccountSalemanAdd,
  subAccountSalemanUpdate,
  subAccountSalemanStatus,
  getWechatProgramCode,
  getSubAccountSalesmanTemp,
} from "@/apiService/api";
import { getToken } from "@/utils/auth";
export default {
  components: {
    Header,
    Left,
  },
  data() {
    /**
     * 验证手机号
     */
    const validatePhone = (rule, value, callback) => {
      if (
        /^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/.test(
          value
        ) !== false
      ) {
        callback();
      } else {
        callback(new Error("请输入正确的手机号"));
      }
    };
    return {
      loading: false,
      title: "",
      sId: undefined,
      userState: 2,
      orderStatus: 0,
      radioType: "2",
      appArr: [],
      appId: null,
      pageInfo: {
        page: 1,
        size: 10,
      },
      totalCount: 0,
      orderList: [],
      orderListImp: [],
      multipleSelect: [],
      levelList: [], // 等级列表
      salesman: [], // 上级业务员列表
      isSelect: false,
      senior: false,
      qrDialogVisible: false, // 绑定二维码弹框
      ExportDialogVisible: false,
      DialogVisible: false, // 编辑弹框
      disabled: false,
      formData: {
        levelId: undefined,
        parentId: undefined,
        type: 1,
        name: "",
        mobile: "",
        merchantNumber: "",
      },
      rules: {
        levelId: [
          {
            required: true,
            message: "请选择业务等级",
            trigger: "blur",
          },
        ],
        parentId: [
          {
            required: true,
            message: "请上级业务员",
            trigger: "change",
          },
        ],
        type: [
          {
            required: true,
            message: "请选择业务员类型",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "请填写姓名",
            trigger: "blur",
          },
        ],
        mobile: [
          {
            required: true,
            message: "请填写手机号",
            trigger: "blur",
            validator: validatePhone,
          },
        ],
        merchantNumber: [
          {
            required: true,
            message: "请填写商户号",
            trigger: "blur",
          },
        ],
      },
      filters: {
        column: {
          starDate: "",
          endDate: "",
        },
      },
      pickerStarDate: {
        disabledDate: (time) => {
          let beginDateVal = this.pageInfo.endTime;
          if (beginDateVal) {
            return time.getTime() > beginDateVal;
          }
        },
      },
      pickerEndDate: {
        disabledDate: (time) => {
          let beginDateVal = this.filters.column.starDate;
          if (beginDateVal) {
            return time.getTime() < beginDateVal;
          }
        },
      },
      platform: "",
      importHeaders: {
        Accept: "application/json",
        enctype: "multipart/form-data",
        Platform: "web",
        Version: "1.0.0",
        token: getToken(),
      },
      codeUrl: "",
      action:
        this.locationUrl_L + "distributor/v1/web/user/subAccountSaleman/import",
      saveFlag: true, // 保存，防重点
    };
  },
  mounted() {
    this.getLevel();
    this.getList();
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  methods: {
    // 保存
    handleSave() {
      if (this.saveFlag) {
        this.saveFlag = false;
        this.$refs["ruleForm"].validate((valid) => {
          if (valid) {
            this.formData.parentId = !this.formData.parentId
              ? 0
              : this.formData.parentId;
            if (this.sId) {
              // 编辑
              this.formData.merchantNumber =
                this.formData.type == 2 ? "" : this.formData.merchantNumber;
              subAccountSalemanUpdate(this.formData)
                .then((res) => {
                  if (res.success) {
                    this.$message.success("更新成功！");
                    this.getList();
                    this.DialogVisible = false;
                  }
                  this.saveFlag = true;
                })
                .catch((err) => {
                  console.log(err);
                });
            } else {
              // 新增
              subAccountSalemanAdd(this.formData)
                .then((res) => {
                  if (res.success) {
                    this.$message.success("新增成功！");
                    this.getList();
                    this.DialogVisible = false;
                  }
                  this.saveFlag = true;
                })
                .catch((err) => {
                  console.log(err);
                });
            }
          }
        });
      }
    },
    // 获取分账业务员列表
    getList() {
      subAccountSaleman(this.pageInfo).then((res) => {
        if (res.success) {
          this.orderList = res.data.list;
          this.totalCount = res.data.total;
        }
      });
    },
    // 获取等级下拉列表
    getLevel() {
      subLevelListByCondition().then((res) => {
        if (res.success) {
          console.log(res.data);
          this.levelList = res.data;
        }
      });
    },
    // 获取上级业务员下拉列表
    getSalemanList(dId, sId) {
      subSalemanListByCondition({
        distributorId: dId,
        sonLevelId: sId,
        openFlag: 1,
      }).then((res) => {
        if (res.success) {
          if (res.data) {
            this.salesman = res.data;
            this.formData.parentId = this.formData.parentId
              ? this.formData.parentId
              : undefined;
          } else {
            this.salesman = [{ name: "顶级业务员", id: 0 }];
            this.formData.parentId = 0;
          }
        }
      });
    },
    // 更新状态
    onChangeStatus(row) {
      let openFlag = row.openFlag === 1 ? 0 : 1;
      subAccountSalemanStatus({ id: row.id, openFlag: openFlag }).then(
        (res) => {
          if (res.success) {
            this.$message.success("更新成功！");
            this.getList();
          } else {
            this.$message.error(res.errMessage);
          }
        }
      );
    },
    // 变更业务等级
    handleChange(val) {
      let dId;
      this.levelList.forEach((item) => {
        if (item.id === val) {
          dId = item.distributorId;
        }
      });
      this.getSalemanList(dId, val);
    },
    // 获取导入订单
    getImpOrderList() {
      var myDate = new Date();
      let pageInfo = {
        page: 1,
        count: this.totalCount,
      };
      this.$api
        .get(
          this,
          "user/u/order/list?" + myDate.getMinutes() + myDate.getSeconds(),
          pageInfo
        )
        .then((res) => {
          if (
            res.code === 0 &&
            res.orders !== undefined &&
            res.orders !== null
          ) {
            this.orderListImp = res.orders;
          } else if (res.code === 3) {
          } else {
            this.orderListImp = [];
          }
        });
    },
    // 搜索
    searchOrder() {
      this.getList();
    },
    // 新增
    handleAdd() {
      this.title = "新增业务员";
      this.sId = undefined;
      this.formData.id = undefined;
      this.formData.levelId = "";
      this.formData.parentId = undefined;
      this.formData.type = 1;
      this.formData.name = "";
      this.formData.mobile = "";
      this.formData.merchantNumber = "";
      this.DialogVisible = true;
    },
    handleEdit(row) {
      // 编辑
      this.title = "编辑业务员";
      this.sId = row.id;
      this.formData.id = row.id;
      this.formData.levelId = row.levelId;
      this.formData.parentId = row.parentId;
      this.formData.type = row.type;
      this.formData.name = row.salemanName;
      this.formData.mobile = row.mobile;
      this.formData.merchantNumber = row.merchantNumber;
      this.DialogVisible = true;
      this.levelList.forEach((item) => {
        if (item.id === row.levelId) {
          this.getSalemanList(item.distributorId, row.levelId);
        }
      });
    },
    // 绑定二维码弹框
    handleQrcode() {
      getWechatProgramCode().then((res) => {
        if (res.success) {
          this.codeUrl = res.data;
          this.qrDialogVisible = true;
        }
      });
    },
    // 上传前校验文件类型
    beforeUpload(file) {
      //
      let excelfileExtend = ".xls,.xlsx"; // 设置文件格式
      let fileExtend = file.name
        .substring(file.name.lastIndexOf("."))
        .toLowerCase();
      if (excelfileExtend.indexOf(fileExtend) <= -1) {
        this.$message.error("只能上传.xls,.xlsx格式");
        return false;
      }
    },
    // 上传中
    onProgress(event, file, fileList) {
      // 上传时的钩子
      this.loading2 = this.$loading({
        lock: true,
        text: "文件上传中....",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    // 批量上传失败
    uploadFail(err, file, fileList) {
      alert();
      // 上传错误
      this.$message.error({
        message: err.msg,
        duration: 3 * 1000,
        onClose: () => {},
      });
      this.loading2.close();
    },
    // 批量上传成功
    uploadSuccess(response, file, fileList) {
      // 上传成功
      if (response.success) {
        this.$message.success({
          message: "上传成功",
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.loading2.close();
        this._initData();
      } else {
        this.$message.error({
          message: response.errMessage,
          duration: 3 * 1000,
          onClose: () => {},
        });
        this.loading2.close();
      }
    },
    formatNumber(n) {
      n = n.toString();
      return n[1] ? n : "0" + n;
    },

    formatTime(number) {
      let date = new Date(number);
      let year = date.getFullYear();
      let month = this.formatNumber(date.getMonth() + 1);
      let day = this.formatNumber(date.getDate());
      let hours = this.formatNumber(date.getHours());
      let Minutes = this.formatNumber(date.getMinutes());
      let Seconds = this.formatNumber(date.getSeconds());
      let format =
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        Minutes +
        ":" +
        Seconds;
      return format;
    },
    changeState(type) {
      this.orderStatus = type;
      this.pageInfo.levelId = type ? type : undefined;
      this.pageInfo.page = 1;
      this.getList();
    },
    formatType(row, col, val) {
      switch (val) {
        case 1:
          return "企业";
        case 2:
          return "个人";
      }
    },
    // 付款状态
    confirmPayStatus(row) {
      switch (row) {
        case 1:
          return this.$t("UserCenter.PendingPayment");
        case 2:
          return this.$t("UserCenter.ToBeShipped");
        case 3:
          return this.$t("UserCenter.PartOfPayment");
        case 4:
          return this.$t("UserCenter.ToBeConfirmed");
        case 5:
          return this.$t("UserCenter.Confirmed");
        case 6:
          return this.$t("UserCenter.PartOfShipped");
        case 7:
          return this.$t("UserCenter.Shipped");
        case 8:
          return this.$t("UserCenter.Closed");
        case 9:
          return this.$t("UserCenter.Completed");
      }
    },
    // 支付状态
    confirmPayWays(val) {
      switch (val) {
        case 1:
          return this.$t("P.Alipay");
        case 2:
          return this.$t("P.WechatPay");
        case 3:
          return this.$t("P.RangeCheckout");
        case 4:
          return this.$t("P.BankTransfer");
        case 5:
          return this.$t("ConfirmOrder.BalancePaid");
        case 6:
          return this.$t("P.EcurrencyPayment");
      }
    },
    // 获取状态
    getStatus(val) {
      if (Number(val) === 1) {
        return "启用";
      } else if (Number(val) === 0) {
        return "禁用";
      }
    },
    // 获取相反状态
    getfStatus(val) {
      if (Number(val) === 0) {
        return "启用";
      } else if (Number(val) === 1) {
        return "禁用";
      }
    },
    // 当切换页面时的作用
    handleSelectionChange(val) {
      this.multipleSelect = val;
    },
    getRowKeys(row) {
      return row.id;
    },
    // 高级搜索显隐
    showSenior() {
      this.senior = !this.senior;
    },
    onCurrentChange(val) {
      // 分页方法
      this.pageInfo.page = val;
      this.getList();
    },

    openDialog() {
      if (this.orderList.length > 0) {
        this.getImpOrderList();
        this.ExportDialogVisible = true;
      } else {
        this.$message({
          type: "error",
          message: "暂无订单数据",
        });
      }
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map((v) =>
        filterVal.map((j) => {
          if (j === "timestamp") {
            return "parseTime(v[j])";
          } else {
            return v[j];
          }
        })
      );
    },
    // 下载模板
    handleTemp() {
      getSubAccountSalesmanTemp().then((res) => {
        if (res.success) {
          let url = res.data;
          if ("download" in document.createElement("a")) {
            let link = document.createElement("a");
            link.style.display = "none";
            link.href = url;
            link.download = "分账业务员.xls";
            document.body.appendChild(link);
            link.click();
          }
        }
      });
    },
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.store-order {
  width: 100%;
}
.store-manage {
  .store-main {
    width: 1200px;
    .store-right {
      width: 1030px;
      .content {
        padding: 24px 40px 0;
        border: 2px solid @bdLightColor;
        border-radius: 8px;
      }
      .store-right-title {
        padding-bottom: 10px;
        border-bottom: 1px solid @bdLightColor;
        font-size: 20px;
      }
      .top-box {
        margin-top: 25px;
        .el-button + .el-button {
          margin-left: 2px;
        }
        .upload-excel {
          margin: 0 2px;
          display: inline-block;
          /deep/.el-upload-list {
            display: none;
          }
        }
      }
      .search-box {
        margin-top: 20px;
        .el-select {
          /deep/.el-input__inner {
            height: 38px;
            line-height: 38px;
          }
        }
        .el-input {
          display: inline-block;
          width: 160px;
          /deep/.el-input__inner {
            height: 38px;
            line-height: 38px;
          }
        }
        .el-select {
          display: inline-block;
          width: 120px;
        }
        .el-button {
          height: 38px;
          vertical-align: middle;
        }
        .ml-15 {
          margin-left: 15px;
        }
        .handle-wrap {
          position: absolute;
          right: 0;
          bottom: 15px;
          font-size: 14px;
          color: @blue;
          line-height: 1;
          cursor: pointer;
          &:hover {
            opacity: 0.6;
          }
          .mini-export {
            color: @blue;
          }
          .iconfont {
            margin-right: 2px;
            color: @lighterGray;
          }
        }
      }
      .search-info {
        .date-items {
          line-height: 38px;
        }
        /deep/.el-select {
          margin-left: 20px;
          line-height: 38px;
          .el-input__inner {
            height: 38px;
            line-height: 38px;
          }
        }
      }
      .query-detail {
        margin-top: 30px;
        .nav {
          margin-bottom: 15px;
          width: 100%;
          height: 40px;
          line-height: 40px;
          ul {
            overflow: hidden;
            background-color: @bdLightColor;
            border-radius: 4px;
            &.navEn {
              li {
                width: auto;
                padding: 0 12px;
                font-size: 13px;
              }
            }
            li {
              float: left;
              width: 93px;
              cursor: pointer;
              font-size: 14px;
              color: @lighterBlack;
              text-align: center;
              &:hover,
              &.current {
                color: @white;
                background-color: @blue;
              }
            }
          }
        }
        .export-btn {
          padding-top: 24px;
          color: @blue;
          line-height: 1;
          cursor: pointer;
          .iconfont {
            margin-right: 2px;
            color: @lighterGray;
          }
          &:hover {
            opacity: 0.6;
          }
        }
        .table-box {
          width: 100%;
          margin-top: 20px;
          margin-bottom: 40px;
          /deep/ .el-table {
            width: 100%;
            word-wrap: break-word;
            word-break: break-all;
            border-collapse: collapse;
            tr {
              &:hover {
                background-color: @lightGrayBg;
              }
              th {
                padding: 0;
                height: 32px;
                line-height: 32px;
                text-align: center;
                background-color: @bdLightColor;
                font-size: 12px;
                color: @gray;
                font-weight: normal;
              }
              td {
                height: 53px;
                text-align: center;
                font-size: 12px;
                color: @lightBlack;
                border: none;
                border-top: 1px dashed @bdLighterColor;
                .log {
                  margin: 0 10px;
                  color: @blue;
                  &:hover {
                    opacity: 0.6;
                  }
                }
                .cell {
                  canvas {
                    width: 40px;
                    height: 40px;
                    vertical-align: middle;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  /deep/.el-dialog__wrapper {
    .el-dialog__header {
      padding-top: 30px;
    }
    .el-dialog__body {
      margin: 0 auto;
      padding: 0;
      width: 80%;
      text-align: center;
      .qr-content {
        .text {
          font-size: 12px;
        }
        .img {
          display: inline-block;
          text-align: center;
          margin: 20px auto;
          width: 200px;
          height: 200px;
          img {
            display: inline-block;
            width: 100%;
            height: 100%;
          }
        }
      }
      .el-form {
        .el-form-item__content {
          text-align: left;
        }
      }
      .raido-group {
        .el-radio {
          display: inline-block;
          margin: 0 30px;
          text-align: center;
          .el-radio__input {
            display: none;
          }
          .el-radio__label {
            padding: 0;
            .icon {
              display: inline-block;
              width: 64px;
              height: 64px;
              background-size: 64px 64px;
              background-repeat: no-repeat;
              &.screen {
                background-image: url("../../assets/images/store/icon_screen@2x.png");
              }
              &.all {
                background-image: url("../../assets/images/store/icon_all@2x.png");
              }
            }
            .text {
              margin-top: 10px;
              color: #555555;
            }
          }
          &.is-checked {
            .el-radio__label {
              .screen {
                background-image: url("../../assets/images/store/icon_screen_pre@2x.png");
              }
              .all {
                background-image: url("../../assets/images/store/icon_all_pre@2x.png");
              }
            }
            .text {
              color: #0db8cf;
            }
          }
        }
      }
    }
    .el-dialog__footer {
      padding-bottom: 40px;
      .el-button {
        margin: 0 30px;
        &.el-button--primary {
          background-color: #0db8cf;
          border-color: #0db8cf;
        }
      }
    }
  }
}
</style>
