<!--
 * @Author: yaowei
 * @Date: 2019-10-20 09:54:01
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-02 11:39:48
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
        <h6 class="detail-title">短信配置信息</h6>

        <el-form-item label="短信平台类型" prop="smsType">
          <el-radio-group v-model="formData.smsType" size="mini">
            <el-radio :label="1">阿里云</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="签名" prop="sign">
          <el-input v-model="formData.sign" />
        </el-form-item>

        <el-form-item label="Key" prop="accessKeyId">
          <el-input v-model="formData.accessKeyId" />
        </el-form-item>

        <el-form-item label="Secret" prop="accessKeySecret">
          <el-input v-model="formData.accessKeySecret" />
        </el-form-item>

        <el-form-item label="验证码长度" prop="verifyCodeLength">
          <el-input
            v-model="formData.verifyCodeLength"
            maxlength="1"
            placeholder="验证码长度限制4或6位"
            @input="
              formData.verifyCodeLength = formData.verifyCodeLength.replace(
                /^\.+|[^\d.]/g,
                ''
              )
            "
          >
            <template slot="append">位</template>
          </el-input>
        </el-form-item>

        <el-form-item label="验证码有效时间" prop="codeVerifyTime">
          <el-input
            v-model="formData.codeVerifyTime"
            @input="
              formData.codeVerifyTime = formData.codeVerifyTime.replace(
                /^\.+|[^\d.]/g,
                ''
              )
            "
          >
            <template slot="append">秒</template>
          </el-input>
        </el-form-item>

        <el-form-item label="前端验证码倒计时" prop="verifyCodeCountDown">
          <el-input
            v-model="formData.verifyCodeCountDown"
            @input="
              formData.verifyCodeCountDown =
                formData.verifyCodeCountDown.replace(/^\.+|[^\d.]/g, '')
            "
          >
            <template slot="append">秒</template>
          </el-input>
        </el-form-item>

        <el-form-item label="短信模板ID" prop="tenantSmsTemplates">
          <el-table
            :data="formData.tenantSmsTemplates"
            header-row-class-name="header-row"
            border
            class="tableCenter"
          >
            <el-table-column
              label="模板类型"
              align="center"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <span>{{ smsTempFormatter(scope.row.templateType) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="模板编码"
              align="center"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.templateCode"></el-input>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>

      <div class="clearfix footbtn">
        <el-button @click="handleBack">返回</el-button>
        <el-button
          type="primary"
          @click="handleSave('ruleForm')"
          :loading="loading"
          >保存</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import {
  addSmsSetting,
  smsSettingDetail,
  updateSmsSetting,
} from "@/api/tenant";

export default {
  name: "SmsSetting",
  data() {
    return {
      loading: false,
      tenantId: this.$route.query.tenantId || "",
      tenantNo: this.$route.query.tenantNo || "",
      checkMsg: this.$route.query.checkMsg || "",
      formData: {
        smsType: 1,
        sign: "",
        accessKeyId: "",
        accessKeySecret: "",
        verifyCodeLength: "",
        codeVerifyTime: "",
        verifyCodeCountDown: "",
        tenantSmsTemplates: [],
      },
      rules: {
        smsType: [
          {
            required: true,
            message: "请选择短信平台类型",
            trigger: ["blur", "change"],
          },
        ],
        sign: [
          {
            required: true,
            message: "请输入签名",
            trigger: ["blur", "change"],
          },
        ],
        accessKeyId: [
          {
            required: true,
            message: "请输入Key",
            trigger: ["blur", "change"],
          },
        ],
        accessKeySecret: [
          {
            required: true,
            message: "请输入Secret",
            trigger: ["blur", "change"],
          },
        ],
        verifyCodeLength: [
          {
            required: true,
            message: "请输入验证码长度",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                if (Number(value) === 4 || Number(value) === 6) {
                  callback();
                } else {
                  return callback(new Error("验证码长度限制4或6位"));
                }
              } else {
                callback();
              }
            },
          },
        ],
        codeVerifyTime: [
          {
            required: true,
            message: "请输入验证码有效时间",
            trigger: ["blur", "change"],
          },
        ],
        verifyCodeCountDown: [
          {
            required: true,
            message: "请输入前端验证码倒计时",
            trigger: ["blur", "change"],
          },
        ],
        tenantSmsTemplates: [
          {
            required: true,
            message: "请设置短信模板ID",
            trigger: ["blur", "change"],
          },
        ],
      },
      tenantSmsTemplates: [1, 2, 3, 4, 5, 6, 7],
    };
  },
  created() {
    // 初始化短信模板类型
    this.tenantSmsTemplates.forEach((temp) => {
      let data = {
        templateType: temp,
        templateCode: "",
      };
      this.formData.tenantSmsTemplates.push(data);
    });

    if (Number(this.checkMsg) === 2) {
      this.initData();
    }
  },
  methods: {
    // 初始化数据
    initData() {
      smsSettingDetail({
        tenantId: this.tenantId,
        smsType: 1, // 短信平台类型：1 阿里云
      }).then((res) => {
        if (res.success) {
          this.formData = res.data;
        }
      });
    },
    // 格式化短信模板ID
    smsTempFormatter(id) {
      switch (id) {
        case 1:
          return "注册申请";
          break;
        case 2:
          return "B端验证码修改密码";
          break;
        case 3:
          return "C端修改手机号";
          break;
        case 4:
          return "C端客户验证码登录";
          break;
        case 5:
          return "C端验证码修改密码";
          break;
        case 6:
          return "分账业务员绑定";
          break;
        case 7:
          return "B端客户验证码登录";
          break;
      }
    },
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.formData.tenantId = this.tenantId;
          this.formData.tenantNo = this.tenantNo;

          if (Number(this.checkMsg) !== 2) {
            // 新增
            addSmsSetting(this.formData).then((res) => {
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
            updateSmsSetting(this.formData).then((res) => {
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
      this.$router.push({
        name: "TenantSetting",
        query: {
          id: this.tenantId,
          tenantNo: this.tenantNo,
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.detail-wrap {
  padding: 20px 100px 50px;
  min-width: 750px;

  .detail-title {
    margin: 50px 0 30px 12%;
  }

  .form-wrap {
    padding-right: 22%;
  }
}
</style>