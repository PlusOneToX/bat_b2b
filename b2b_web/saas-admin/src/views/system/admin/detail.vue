<!--
 * @Author: yaowei
 * @Date: 2019-10-19 14:28:30
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-01 14:20:14
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
        <el-form-item label="用户名" prop="userName">
          <el-input
            v-model="formData.userName"
            :disabled="Number(checkMsg) === 2"
          />
        </el-form-item>

        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" />
        </el-form-item>

        <el-form-item label="手机号" prop="mobile">
          <el-input maxlength="11" v-model="formData.mobile" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-form-item label="密码" prop="pwd">
          <el-input maxlength="16" v-model="formData.pwd" @focus="clearPwd()" />
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
import md5 from "js-md5";
import { addUser, userDetail, updateUser } from "@/api/system";

export default {
  name: "AdminDetail",
  data() {
    return {
      loading: false,
      id: this.$route.query.id || "",
      checkMsg: this.$route.query.checkMsg || "",
      formData: {
        userName: "",
        realName: "",
        mobile: "",
        email: "",
        password: "",
        pwd: "",
        remark: "",
        openFlag: 1,
      },
      rules: {
        userName: [
          {
            required: true,
            message: "请输入用户名",
            trigger: ["blur", "change"],
          },
        ],
        realName: [
          {
            required: true,
            message: "请输入真实姓名",
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
        pwd: [
          {
            required: true,
            message: "请输入密码",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                if (value.length >= 6 && value.length <= 16) {
                  callback();
                } else {
                  return callback(new Error("密码长度请设置在6~16个字符!"));
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
      userDetail({ id: id }).then((res) => {
        if (res.success) {
          this.formData = res.data;
          this.$set(this.formData, "pwd", "********");
        }
      });
    },
    // 密码框聚焦，清除密码
    clearPwd() {
      if (Number(this.checkMsg) === 2) {
        // 编辑时清空，因为密码加密
        this.$set(this.formData, "pwd", "");
      }
    },
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.formData.password = md5(this.formData.pwd);

          if (Number(this.checkMsg) !== 2) {
            // 新增
            addUser(this.formData).then((res) => {
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
            updateUser(this.formData).then((res) => {
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