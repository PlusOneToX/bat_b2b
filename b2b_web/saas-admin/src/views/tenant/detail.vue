<!--
 * @Author: yaowei
 * @Date: 2019-10-18 11:50:37
 * @LastEditors: yaowei
 * @LastEditTime: 2019-10-22 11:08:15
-->
<template>
  <div class="detail-wrap">
    <div class="content">
      <el-form
        class="form-wrap"
        :model="formData"
        :rules="rules"
        label-width="35%"
        label-position="right"
        ref="ruleForm"
      >
        <el-form-item label="平台租户编码" prop="tenantNo">
          <el-input
            v-model="formData.tenantNo"
            :disabled="Number(checkMsg) === 2"
          />
        </el-form-item>

        <el-form-item label="公司名" prop="companyName">
          <el-input v-model="formData.companyName" />
        </el-form-item>

        <el-form-item label="公司类型" prop="companyType">
          <el-radio-group v-model="formData.companyType" size="mini">
            <el-radio :label="1">公司</el-radio>
            <el-radio :label="2">个体商户</el-radio>
            <el-radio :label="3">个人</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="联系人姓名" prop="realName">
          <el-input v-model="formData.realName" />
        </el-form-item>

        <el-form-item label="联系人性别" prop="sex">
          <el-radio-group v-model="formData.sex" size="mini">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">未知</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="手机号" prop="mobile">
          <el-input maxlength="11" v-model="formData.mobile" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="formData.remark" />
        </el-form-item>

        <el-form-item label="状态" prop="openFlag">
          <el-radio-group v-model="formData.openFlag" size="mini">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div class="clearfix footbtn">
        <el-button
          type="primary"
          @click="handleSave('ruleForm')"
          :loading="loading"
          >保存</el-button
        >
        <el-button @click="handleBack">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { addTenant, tenantDetail, updateTenant } from "@/api/tenant";

export default {
  name: "TenantDetail",
  data() {
    return {
      loading: false,
      id: this.$route.query.id || "",
      checkMsg: this.$route.query.checkMsg || "",
      formData: {
        tenantNo: "",
        companyName: "",
        companyType: 1,
        realName: "",
        sex: 1,
        mobile: "",
        email: "",
        remark: "",
        openFlag: 1,
      },
      rules: {
        tenantNo: [
          {
            required: true,
            message: "请输入平台租户编码",
            trigger: ["blur", "change"],
          },
        ],
        companyName: [
          {
            required: true,
            message: "请输入公司名",
            trigger: ["blur", "change"],
          },
        ],
        companyType: [
          {
            required: true,
            message: "请选择公司类型",
            trigger: ["blur", "change"],
          },
        ],
        realName: [
          {
            required: true,
            message: "请输入联系人姓名",
            trigger: ["blur", "change"],
          },
        ],
        mobile: [
          {
            required: true,
            message: "请输入手机号",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                const reg = /^1[3|4|5|6|7|8|9][0-9]\d{8}$/;
                if (reg.test(value)) {
                  callback();
                } else {
                  return callback(new Error("请输入正确的手机号"));
                }
              } else {
                callback();
              }
            },
          },
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  created() {
    if (Number(this.checkMsg) === 2) {
      this.initData(Number(this.id));
    }
  },
  methods: {
    // 初始化数据
    initData(id) {
      tenantDetail({ id: id }).then((res) => {
        if (res.success) {
          this.formData = res.data;
        }
      });
    },
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          if (Number(this.checkMsg) === 1) {
            // 新增
            addTenant(this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.handleBack();
              }
            });
          } else {
            // 修改
            updateTenant(this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "修改成功",
                });
                this.handleBack();
              }
            });
          }
        }
      });
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
      this.$router.go(-1);
    },
  },
};
</script>

<style lang="scss" scoped>
.detail-wrap {
  padding: 20px 15px;

  .content {
    padding-top: 20px;
    min-width: 750px;
    margin: 0 auto;

    .form-wrap {
      padding-right: 25%;
    }
  }
}
</style>