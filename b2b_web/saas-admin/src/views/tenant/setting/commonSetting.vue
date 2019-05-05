<!--
 * @Author: yaowei
 * @Date: 2019-10-20 09:38:45
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-16 10:02:57
-->
<template>
  <div class="detail-wrap">
    <div class="content">
      <el-form
        class="form-wrap"
        :model="formData"
        :rules="rules"
        label-width="180px"
        label-position="right"
        ref="ruleForm"
      >
        <h6 class="detail-title">分销小程序配置</h6>
        <el-form-item label="分销微信小程序APPID" prop="wxProgramAppId">
          <el-input v-model="formData.wxProgramAppId" />
        </el-form-item>

        <el-form-item label="分销微信小程序授权密钥" prop="wxProgramAppSecret">
          <el-input v-model="formData.wxProgramAppSecret" />
        </el-form-item>

        <el-form-item label="主色调" prop="colour">
          <el-color-picker v-model="formData.colour"></el-color-picker>
        </el-form-item>

        <h6 class="detail-title">兑换卡配置</h6>
        <el-form-item label="兑换卡默认分销商ID" prop="exchangeDistributorId">
          <el-input
            v-model="formData.exchangeDistributorId"
            @input="
              formData.exchangeDistributorId =
                formData.exchangeDistributorId.replace(/^\.+|[^\d.]/g, '')
            "
          />
        </el-form-item>
      </el-form>

      <h6 class="detail-title">URL配置</h6>
      <div class="setting-table">
        <el-button type="primary" @click="handleAddURL()">新增</el-button>
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
            <el-table-column align="center" :width="260">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>URL类型</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isEdit"
                  :prop="'tableData.' + scope.$index + '.urlType'"
                  :rules="rules.urlType"
                >
                  <!-- <el-select v-model="scope.row.urlType" @change="checkUrlType"> -->
                  <el-select v-model="scope.row.urlType">
                    <el-option
                      v-for="item in urlTypeList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      :disabled="item.disabled"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <span v-else>{{ urlTypeFormatter(scope.row.urlType) }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" :min-width="300">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>主机</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isEdit"
                  :prop="'tableData.' + scope.$index + '.host'"
                  :rules="rules.host"
                >
                  <el-input v-model="scope.row.host"></el-input>
                </el-form-item>
                <span v-else>{{ scope.row.host }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" :min-width="300">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>URL地址</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isEdit"
                  :prop="'tableData.' + scope.$index + '.url'"
                  :rules="rules.url"
                >
                  <el-input v-model="scope.row.url"></el-input>
                </el-form-item>
                <span v-else>{{ scope.row.url }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" :min-width="100" align="center">
              <template slot-scope="scope">
                <el-link
                  type="primary"
                  v-if="scope.row.isEdit"
                  @click="handleSaveURL(scope.row, scope.$index)"
                  >保存</el-link
                >
                <el-link
                  type="primary"
                  v-else
                  @click="handleEditURL(scope.row.id)"
                  >编辑</el-link
                >
                <el-link
                  type="danger"
                  @click="handleDeleteURL(scope.row.id, scope.$index)"
                  >删除</el-link
                >
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>

      <div class="clearfix footbtn">
        <el-button @click="handleBack">返回</el-button>
        <el-button type="primary" @click="handleSave()" :loading="loading"
          >保存</el-button
        >
        <el-button type="primary" plain @click="handleNext()">下一步</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  addCommonSetting,
  commonSettingDetail,
  updateCommonSetting,
  addUrlSetting,
  deleteUrlSetting,
  urlSettingList,
  updateUrlSetting,
  tenantSummary,
} from "@/api/tenant";

export default {
  name: "CommonSetting",
  data() {
    return {
      loading: false,
      tenantId: this.$route.query.tenantId || "",
      tenantNo: this.$route.query.tenantNo || "",
      checkMsg: this.$route.query.checkMsg || "",
      formData: {
        wxProgramAppId: "",
        wxProgramAppSecret: "",
        colour: "#5B2401",
        exchangeDistributorId: "",
      },
      ruleTable: {
        tableData: [],
      },
      rules: {
        wxProgramAppSecret: [
          {
            validator: (rule, value, callback) => {
              if (value) {
                if (value.length <= 32) {
                  callback();
                } else {
                  return callback(new Error("限制输入32个字符"));
                }
              } else {
                callback();
              }
            },
          },
        ],
        urlType: [
          {
            required: true,
            message: "请选择URL类型",
            trigger: ["blur", "change"],
          },
        ],
        host: [
          {
            required: true,
            message: "请输入主机",
            trigger: ["blur", "change"],
          },
        ],
        url: [
          {
            required: true,
            message: "请输入URL地址",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                if (value.length <= 128) {
                  callback();
                } else {
                  return callback(new Error("限制输入128个字符"));
                }
              } else {
                callback();
              }
            },
          },
        ],
      },
      urlTypeList: [
        {
          label: "分销后台PC端",
          value: 1,
          disabled: false,
        },
        {
          label: "分销前台PC端",
          value: 2,
          disabled: false,
        },
        {
          label: "分销前台H5端",
          value: 3,
          disabled: false,
        },
        {
          label: "店铺二维码",
          value: 4,
          disabled: false,
        },
        {
          label: "分销商申请二维码",
          value: 5,
          disabled: false,
        },
        {
          label: "后端接口",
          value: 6,
          disabled: false,
        },
        {
          label: "柔性H5端",
          value: 7,
          disabled: false,
        },
        {
          label: "兑换商城H5端",
          value: 9,
          disabled: false,
        },
      ],
      hasNextSet: false, // 下一步是否已配置
    };
  },
  created() {
    this.initData(Number(this.tenantId));
  },
  methods: {
    // 初始化数据
    initData(id) {
      // 分销小程序配置
      this.initCommonSetting(id);
      // URL配置
      this.initUrlSetting(id);
    },
    // 获取分销小程序配置
    initCommonSetting(id) {
      commonSettingDetail({ id: id }).then((res) => {
        if (res.success && res.data) {
          this.formData = res.data;
        }
      });
    },
    // 获取URL配置列表
    initUrlSetting(id) {
      urlSettingList({ id: id }).then((res) => {
        if (res.success) {
          this.ruleTable.tableData = res.data;

          // 检测URL类型（已选，不让重复选）
          // this.checkUrlType();
        }
      });
    },
    // 格式化URL类型
    urlTypeFormatter(type) {
      switch (type) {
        case 1:
          return "分销后台PC端";
          break;
        case 2:
          return "分销前台PC端";
          break;
        case 3:
          return "分销前台H5端";
          break;
        case 4:
          return "店铺二维码";
          break;
        case 5:
          return "分销商申请二维码";
          break;
        case 6:
          return "后端接口";
          break;
        case 7:
          return "柔性H5端";
          break;
        case 9:
          return "兑换商城H5端";
          break;
      }
    },
    // 检测URL类型
    checkUrlType() {
      // 重置已选URL类型
      this.urlTypeList.forEach((type) => {
        this.$set(type, "disabled", false);
      });
      // 设置已选URL类型不可重复选择
      this.ruleTable.tableData.forEach((item) => {
        this.urlTypeList.forEach((type) => {
          if (item.urlType === type.value) {
            this.$set(type, "disabled", true);
          }
        });
      });
    },
    /** URL配置 */
    // 新增
    handleAddURL() {
      let urlData = {
        tenantId: this.tenantId,
        tenantNo: this.tenantNo,
        urlType: "",
        host: "",
        url: "",
        isEdit: true, // 是否可编辑
      };

      this.ruleTable.tableData.push(urlData);
    },
    // 表格表单 - 单行验证
    validate(form, index) {
      let result = true;
      for (const item of this.$refs[form].fields) {
        if (item.prop.split(".")[1] === index.toString()) {
          this.$refs[form].validateField(item.prop, (error) => {
            if (error !== "") {
              result = false;
            }
          });
        }
      }
      return result;
    },
    // 保存
    handleSaveURL(row, index) {
      let isValid = this.validate("ruleTable", index);
      if (!isValid) return false;

      if (row.id) {
        // 修改
        updateUrlSetting({
          id: row.id,
          tenantId: this.tenantId,
          tenantNo: this.tenantNo,
          host: row.host,
          urlType: row.urlType,
          url: row.url,
        }).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "修改成功",
            });

            this.ruleTable.tableData.forEach((item) => {
              if (item.id === row.id) {
                this.$set(item, "isEdit", false);
              }
            });
          }
        });
      } else {
        // 新增
        addUrlSetting({
          tenantId: this.tenantId,
          tenantNo: this.tenantNo,
          host: row.host,
          urlType: row.urlType,
          url: row.url,
        }).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.ruleTable.tableData.forEach((item) => {
              if (item.id === row.id) {
                this.$set(item, "isEdit", false);
              }
            });
          }
        });
      }
    },
    // 编辑
    handleEditURL(id) {
      this.ruleTable.tableData.forEach((item) => {
        if (item.id === id) {
          this.$set(item, "isEdit", true);
        }
      });
    },
    // 删除
    handleDeleteURL(id, index) {
      if (id) {
        this.$confirm("此操作将删除该URL配置，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then((_) => {
            deleteUrlSetting({ id: id }).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
                this.ruleTable.tableData.splice(index, 1);
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
        this.ruleTable.tableData.splice(index, 1);
      }

      // 检测URL类型（已选，不让重复选）
      // this.checkUrlType();
    },
    /** URL配置 */
    // 保存提交
    handleSave() {
      if (!this.formData.id) {
        // 新增分销小程序配置
        addCommonSetting({
          tenantId: this.tenantId,
          tenantNo: this.tenantNo,
          wxProgramAppId: this.formData.wxProgramAppId,
          wxProgramAppSecret: this.formData.wxProgramAppSecret,
          colour: this.formData.colour,
          exchangeDistributorId: this.formData.exchangeDistributorId,
        }).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "新增成功",
            });
            this.handleBack();
          }
        });
      } else {
        // 修改分销小程序配置
        updateCommonSetting({
          id: this.formData.id,
          tenantId: this.tenantId,
          tenantNo: this.tenantNo,
          wxProgramAppId: this.formData.wxProgramAppId,
          wxProgramAppSecret: this.formData.wxProgramAppSecret,
          colour: this.formData.colour,
          exchangeDistributorId: this.formData.exchangeDistributorId,
        }).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.handleBack();
          }
        });
      }
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    // 返回
    handleBack() {
      this.resetValidate("ruleForm");
      this.clickLeave();
    },
    // 页面跳转
    clickLeave() {
      this.$router.push({
        name: "TenantSetting",
        query: {
          id: this.tenantId,
          tenantNo: this.tenantNo,
        },
      });
    },
    // 租户配置汇总
    tenantSummary() {
      tenantSummary({
        id: this.tenantId,
      }).then((res) => {
        if (res.success) {
          let data = res.data;

          // 是否配置数据库配置
          this.hasNextSet = data.dbFlag;

          // 跳转 数据库配置
          this.$router.push({
            name: "DBSetting",
            query: {
              tenantId: this.tenantId,
              tenantNo: this.tenantNo,
              checkMsg: this.hasNextSet ? 2 : 1,
            },
          });
        }
      });
    },
    // 下一步
    handleNext() {
      this.tenantSummary();
    },
  },
};
</script>

<style lang="scss" scoped>
.detail-wrap {
  padding: 0 100px;
  min-width: 750px;

  .detail-title {
    margin: 80px 0 30px;
  }

  .form-wrap {
    padding-right: 25%;
  }

  .el-table {
    margin-top: 10px;

    .el-form-item {
      margin-bottom: 0;

      &.is-error {
        margin-bottom: 22px;
      }
    }

    .el-select {
      width: 100%;
    }
  }
}
</style>