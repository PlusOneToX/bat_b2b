<!--
 * @Author: yaowei
 * @Date: 2018-06-11 09:48:32
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-11 16:01:54
-->

<template>
  <div class="wx-add-edit">
    <header>
      <h4 v-if="isEdit">查看微信公众平台</h4>
      <h4 v-else>添加微信公众平台</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="cancel"
      >
        返回微信公众平台列表
      </el-button>
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
      <el-form-item label="公众平台名称：" prop="name">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="应用类型：" prop="type">
        <el-radio-group v-model="form.type" size="mini">
          <el-radio :label="1">微信公众号</el-radio>
          <el-radio :label="2">微信小程序</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="AppId：" prop="appId">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.appId"
        ></el-input>
      </el-form-item>
      <el-form-item label="AppSecret" prop="appSecret">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.appSecret"
          type="textarea"
        ></el-input>
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
      <el-button style="margin-left: 25%" size="mini" @click="cancel"
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
export default {
  components: { selectDistributor },
  data() {
    const validateNumber = (rule, value, callback) => {
      if (value === undefined || value === null || value.lenght === 0) {
        callback(new Error("至少选择一个分销商"));
      }
    };
    return {
      form: {
        name: "",
        type: 1,
        appId: "",
        appSecret: "",
        platform: "",
        distributorIds: [],
      },
      distributorData: [],
      distributorShow: false,
      rules: {
        name: [
          {
            required: true,
            message: "请输入公众平台名称",
            trigger: "blur",
          },
        ],
        type: [
          {
            required: true,
            message: "请选择应用类型",
            trigger: "blur",
          },
        ],
        appId: [
          {
            required: true,
            message: "请输入AppId",
            trigger: "blur",
          },
        ],
        appSecret: [
          {
            required: true,
            message: "请输入AppKey",
            trigger: "blur",
          },
        ],
        platform: [
          {
            required: true,
            message: "请输入平台编码",
            trigger: "blur",
          },
        ],
        distributorIds: [
          {
            type: "array",
            required: true,
            message: "请至少选择一个分销商",
            trigger: "blur",
            // validator: validateNumber,
          },
        ],
      },
      isEdit: false,
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
          size: 100,
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
            this.$http.updateWxPlatform(this, this.form).then((res) => {
              if (res.success) {
                this.$message({
                  message: "保存成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.cancel();
              }
              this.loading = false;
            });
          } else {
            // 新增
            this.$http.addWxPlatform(this, this.form).then((res) => {
              if (res.success) {
                this.$message({
                  message: "保存成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.cancel();
              }
              this.loading = false;
            });
          }
        }
      });
    },
    cancel() {
      this.$router.push({ name: "wxPlatformList" });
    },
    getParams() {
      this.isEdit = false;
      if (this.$route.query.id != undefined) {
        this.isEdit = true;
        // 微信公众平台详情
        this.$http
          .getWxPlatformDetail(this, {
            id: this.$route.query.id,
          })
          .then((res) => {
            if (res.success) {
              this.form = res.data;
              this.distributorData = res.data.distributors;
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
        this.form.name = "";
        this.form.appId = "";
        this.form.appSecret = "";
        this.form.distributorIds = [];
      }
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.wx-add-edit {
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
</style>

