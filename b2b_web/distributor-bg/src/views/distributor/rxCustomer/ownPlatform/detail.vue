<!--
 * @Author: yaowei
 * @Date: 2018-06-11 10:36:43
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-11 16:01:30
-->

<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">添加自有平台</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">查看自有平台</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回自有平台列表</el-button
      >
    </header>

    <el-form
      ref="form"
      :model="form"
      label-width="150px"
      class="el-form1"
      :rules="rules"
    >
      <el-form-item label="系统平台编码：" prop="platform">
        <el-select
          class="custom_select"
          filterable
          placeholder="系统平台编码"
          size="mini"
          v-model="form.platform"
          clearable
          @change="changePlatform"
          style="width: 300px"
        >
          <el-option
            :key="item.id"
            :label="item.platformNo"
            :value="item.platformNo"
            v-for="item in sysNoData"
          ></el-option>
        </el-select>
        <p class="tips">
          请先在系统平台列表中新增平台编码，把添加好的编码在这里填入，全平台唯一
        </p>
      </el-form-item>
      <el-form-item label="域名或IP：" prop="hostName">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.hostName"
        ></el-input>
      </el-form-item>
      <template v-if="Number(checkMsg) === 3">
        <el-form-item label="appId：">
          <el-input
            size="mini"
            style="width: 300px"
            v-model.trim="form.appId"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="appKey：">
          <el-input
            size="mini"
            style="width: 300px"
            v-model.trim="form.appKey"
            type="textarea"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="appSecret：">
          <el-input
            size="mini"
            style="width: 300px"
            v-model.trim="form.appSecret"
            type="textarea"
            disabled
          ></el-input>
        </el-form-item>
      </template>
      <el-form-item label="接口列表：" prop="apis">
        <el-button
          icon="el-icon-plus"
          class="mini-search-btn"
          @click="apiDialogShow()"
        >
          添加接口
        </el-button>
        <el-table
          class="tableCenter"
          :data="form.apis"
          border
          header-row-class-name="header-row"
          max-height="600"
        >
          <el-table-column
            align="center"
            label="事务类型"
            prop="apiType"
            :formatter="apiTypeFormatter"
          ></el-table-column>
          <el-table-column
            align="center"
            label="开发源头类型"
            prop="developSource"
            :formatter="developSourceFormatter"
          ></el-table-column>
          <el-table-column
            align="center"
            label="请求方式"
            prop="method"
          ></el-table-column>
          <el-table-column
            align="center"
            label="URL"
            prop="uri"
          ></el-table-column>
          <el-table-column align="center" label="操作" width="100">
            <template slot-scope="scope">
              <el-button
                style="margin-top: 0px; margin-bottom: 0px"
                class="mini-delete-btn"
                @click="handleDeleteAPI(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="适用分销商：" prop="distributorIds">
        <el-button
          icon="el-icon-plus"
          class="mini-search-btn"
          @click="distributorShow = true"
        >
          添加分销商
        </el-button>
        <el-table
          class="tableCenter"
          :data="distributorData"
          border
          header-row-class-name="header-row"
          max-height="600"
        >
          <el-table-column
            align="center"
            label="分销商用户名"
            prop="name"
          ></el-table-column>
          <el-table-column
            align="center"
            label="公司名"
            prop="companyName"
          ></el-table-column>
          <el-table-column align="center" label="操作" width="100">
            <template slot-scope="scope">
              <el-button
                style="margin-top: 0px; margin-bottom: 0px"
                class="mini-delete-btn"
                @click="handleDeleteDistributor(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-form>

    <!-- 接口列表 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="apiShow"
      :before-close="apiCancel"
      class="apiDialog"
      width="600"
    >
      <el-form
        ref="apiForm"
        :model="apiForm"
        label-width="120px"
        :rules="apiRules"
      >
        <el-form-item label="事务类型：" prop="apiType">
          <el-select
            size="mini"
            v-model="apiForm.apiType"
            style="width: 140px"
            placeholder="请选择"
            clearable
          >
            <el-option label="物流信息推送" :value="1"></el-option>
            <el-option label="订单信息推送" :value="2"></el-option>
            <el-option label="定制信息推送" :value="3"></el-option>
            <el-option label="获取定制价格" :value="4"></el-option>
            <el-option label="订单编号回传" :value="5"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开发源头类型：" prop="developSource">
          <el-select
            size="mini"
            v-model="apiForm.developSource"
            style="width: 140px"
            placeholder="请选择"
            clearable
          >
          </el-select>
        </el-form-item>
        <el-form-item label="请求方式：" prop="method">
          <el-select
            size="mini"
            v-model="apiForm.method"
            style="width: 140px"
            placeholder="请选择"
            clearable
          >
            <el-option label="GET" value="GET"></el-option>
            <el-option label="POST" value="POST"></el-option>
            <el-option label="PUT" value="PUT"></el-option>
            <el-option label="DELETE" value="DELETE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="URL：" prop="uri">
          <el-input
            size="mini"
            style="width: 300px"
            v-model.trim="apiForm.uri"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="btn-wrap">
        <el-button size="mini" @click="apiShow = false">返回</el-button>
        <el-button class="mini-search-btn" @click="handleApiConfirm"
          >确定</el-button
        >
      </div>
    </el-dialog>

    <!-- 分销商列表 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="distributorData"
        ref="selectDistributor"
        @cancel="cancelDialog"
        @submit="disSubmitDialog"
      >
      </select-distributor>
    </el-dialog>

    <div class="clearfix footbtn">
      <el-button style="margin-left: 25%" size="mini" @click="handleBack"
        >返回</el-button
      >
      <el-button class="mini-search-btn" @click="submitForm('form')"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
import selectDistributor from "@/views/goods/components/selectDistributorAll";
import { apiTypeFormatter, developSourceFormatter } from "./formateData"; // 格式化引入

export default {
  components: { selectDistributor },
  name: "ownPlatformDetail",
  data() {
    return {
      form: {
        appId: "",
        appKey: "",
        appSecret: "",
        platform: "",
        distributorIds: [],
        apis: [],
      },
      distributorData: [], // 分销商列表
      distributorShow: false,
      rules: {
        platform: [
          {
            required: true,
            message: "请输入系统平台编码",
            trigger: "blur",
          },
        ],
        hostName: [
          {
            required: true,
            message: "请输入域名或IP",
            trigger: "blur",
          },
        ],
        distributorIds: [
          {
            type: "array",
            required: true,
            message: "请至少选择一个分销商",
            trigger: "blur",
          },
        ],
        apis: [
          {
            required: true,
            message: "请至少添加一个接口列表",
            trigger: "blur",
          },
        ],
      },
      checkMsg: this.$route.query.checkMsg,
      isEdit: false,
      apiShow: false,
      apiForm: {
        apiType: "",
        developSource: "",
        method: "",
        uri: "",
      },
      apiRules: {
        apiType: [
          {
            required: true,
            message: "请选择事务类型",
            trigger: ["blur", "change"],
          },
        ],
        developSource: [
          {
            required: true,
            message: "请选择开发源头类型",
            trigger: ["blur", "change"],
          },
        ],
        method: [
          {
            required: true,
            message: "请选择请求方式",
            trigger: ["blur", "change"],
          },
        ],
        uri: [
          {
            required: true,
            message: "请输入URL",
            trigger: ["blur", "change"],
          },
        ],
      },
      sysNoData: [], // 系统平台编码
    };
  },
  created() {
    this.getParams();
    this.getSysNo("");
  },
  methods: {
    // 获取系统平台列表（不受权限控制）
    getSysNo(content) {
      this.$http
        .getSysPlatformPoList(this, {
          page: 1,
          size: 200,
          contentType: 1,
          openFlag: 1,
          content: content,
        })
        .then((res) => {
          if (res.success) {
            this.sysNoData = res.data.list;
          }
        });
    },
    changePlatform(val) {
      this.form.platform = val;
    },
    // 接口列表
    apiTypeFormatter(row) {
      // 事务类型
      return apiTypeFormatter(row.apiType);
    },
    developSourceFormatter(row) {
      // 开发源头类型
      return developSourceFormatter(row.developSource);
    },
    apiCancel() {
      // 关闭接口弹窗
      this.apiShow = false;
    },
    apiDialogShow() {
      // 清空已输入接口数据
      this.apiForm.apiType = "";
      this.apiForm.developSource = "";
      this.apiForm.method = "";
      this.apiForm.uri = "";
      this.apiShow = true;
      this.$nextTick(() => {
        this.$refs.apiForm.clearValidate();
      });
    },
    handleDeleteAPI(index) {
      this.form.apis.splice(index, 1);
    },
    handleApiConfirm() {
      this.$refs["apiForm"].validate((valid) => {
        if (valid) {
          let option = {
            apiType: this.apiForm.apiType,
            developSource: this.apiForm.developSource,
            method: this.apiForm.method,
            uri: this.apiForm.uri,
          };
          this.form.apis.push(option);
          // 关闭接口弹窗
          this.apiShow = false;
        }
      });
    },

    // 适用分销商
    handleDeleteDistributor(index) {
      this.distributorData.splice(index, 1);
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    cancelDialog() {
      this.distributorShow = false;
    },
    disSubmitDialog(msg) {
      this.form.distributorIds = [];
      this.distributorData = msg;
      if (
        this.distributorData !== undefined &&
        this.distributorData !== null &&
        this.distributorData.length > 0
      ) {
        this.distributorData.forEach((item) => {
          this.$set(item, "distributorId", item.id);
          this.form.distributorIds.push(item.id);
        });
      }
      this.distributorShow = false;
    },
    submitForm(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.handleSave();
        } else {
          return false;
        }
      });
    },
    handleSave() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          delete this.form.distributorIds;
          let distributors = [];
          this.distributorData.forEach((item) => {
            let options = {
              companyName: item.companyName,
              distributorId: item.distributorId,
              name: item.name,
            };
            distributors.push(options);
          });
          this.form.distributors = distributors;

          if (this.isEdit) {
            // 更新
            this.form.id = this.$route.query.id;
            this.$http.updateOwnPlatform(this, this.form).then((res) => {
              if (res.success) {
                this.$message({
                  message: "保存成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.handleBack();
              }
              this.loading = false;
            });
          } else {
            // 新增
            this.$http.addOwnPlatform(this, this.form).then((res) => {
              if (res.success) {
                this.$message({
                  message: "保存成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.handleBack();
              }
              this.loading = false;
            });
          }
        }
      });
    },
    handleBack() {
      this.$router.push({ name: "ownPlatformList" });
    },
    getParams() {
      this.isEdit = false;
      if (this.$route.query.id != undefined) {
        this.isEdit = true;
        // 微信公众平台详情
        this.$http
          .getOwnPlatformDetail(this, {
            id: this.$route.query.id,
          })
          .then((res) => {
            if (res.success) {
              this.form = res.data;
              this.distributorData = res.data.distributors;
              this.form.apis = res.data.apis;
              this.form.distributorIds = [];
              if (
                this.distributorData !== undefined &&
                this.distributorData !== null &&
                this.distributorData.length > 0
              ) {
                this.distributorData.forEach((item) => {
                  this.$set(item, "id", item.distributorId);
                  this.form.distributorIds.push(item.distributorId);
                });
              }
            }
          });
      } else {
        this.form.appId = "";
        this.form.appKey = "";
        this.form.appSecret = "";
        this.form.distributorIds = [];
      }
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.detail-wrap {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    margin-bottom: 3%;
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
}
.el-form1 {
  width: 800px !important;
  margin-top: 30px;
  padding-left: 20px;
}
.tips {
  line-height: 18px;
}
.footbtn {
  margin-top: 40px;
  padding-bottom: 20px;
}
.header-row {
  @include table-header-color;
}

.apiDialog {
  .btn-wrap {
    text-align: center;
  }
}
</style>
