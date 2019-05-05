<!--
 * @Author: yaowei
 * @Date: 2019-10-20 09:53:48
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-01 09:17:01
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
        <h6 class="detail-title">文件存储配置信息</h6>

        <el-form-item label="OSS平台类型" prop="ossType">
          <el-radio-group v-model="formData.ossType" size="mini">
            <el-radio :label="1">阿里云</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Endpoint" prop="endpoint">
          <el-input v-model="formData.endpoint" />
        </el-form-item>

        <el-form-item label="Key" prop="accessKeyId">
          <el-input v-model="formData.accessKeyId" />
        </el-form-item>

        <el-form-item label="Secret" prop="accessKeySecret">
          <el-input v-model="formData.accessKeySecret" />
        </el-form-item>

        <el-form-item label="Bucket" prop="bucket">
          <el-input v-model="formData.bucket" />
        </el-form-item>

        <el-form-item label="BaseHttp" prop="baseHttp">
          <el-input v-model="formData.baseHttp" />
        </el-form-item>

        <el-form-item label="RoleArn" prop="roleArn">
          <el-input v-model="formData.roleArn" />
        </el-form-item>

        <el-form-item label="RegionID" prop="regionId">
          <el-input v-model="formData.regionId" />
        </el-form-item>

        <el-form-item label="Region" prop="region">
          <el-input v-model="formData.region"> </el-input>
        </el-form-item>

        <el-form-item label="Policy" prop="policy">
          <el-input v-model="formData.policy" />
        </el-form-item>

        <el-form-item
          label="分销商申请二维码图片存放路径"
          prop="distributorOssFolder"
        >
          <el-input v-model="formData.distributorOssFolder" />
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
        <el-button type="primary" plain @click="handleNext()">下一步</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  addOssSetting,
  ossSettingDetail,
  updateOssSetting,
  tenantSummary,
} from "@/api/tenant";

export default {
  name: "OssSetting",
  data() {
    return {
      loading: false,
      tenantId: this.$route.query.tenantId || "",
      tenantNo: this.$route.query.tenantNo || "",
      checkMsg: this.$route.query.checkMsg || "",
      formData: {
        ossType: 1,
        endpoint: "",
        accessKeyId: "",
        accessKeySecret: "",
        bucket: "",
        baseHttp: "",
        roleArn: "",
        regionId: "",
        region: "",
        policy: "",
        distributorOssFolder: "",
      },
      rules: {
        ossType: [
          {
            required: true,
            message: "请选择OSS平台类型",
            trigger: ["blur", "change"],
          },
        ],
        endpoint: [
          {
            required: true,
            message: "请输入Endpoint",
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
        bucket: [
          {
            required: true,
            message: "请输入Bucket",
            trigger: ["blur", "change"],
          },
        ],
        baseHttp: [
          {
            required: true,
            message: "请输入BaseHttp",
            trigger: ["blur", "change"],
          },
        ],
        roleArn: [
          {
            required: true,
            message: "请输入RoleArn",
            trigger: ["blur", "change"],
          },
        ],
        regionId: [
          {
            required: true,
            message: "请输入RegionID",
            trigger: ["blur", "change"],
          },
        ],
        region: [
          {
            required: true,
            message: "请输入Region",
            trigger: ["blur", "change"],
          },
        ],
        policy: [
          {
            required: true,
            message: "请输入Policy",
            trigger: ["blur", "change"],
          },
        ],
      },
      hasNextSet: false, // 下一步是否已配置
    };
  },
  created() {
    if (Number(this.checkMsg) === 2) {
      this.initData();
    }
  },
  methods: {
    // 初始化数据
    initData() {
      ossSettingDetail({
        tenantId: this.tenantId,
        ossType: 1, // OSS平台类型：1 阿里云
      }).then((res) => {
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
          this.formData.tenantId = this.tenantId;
          this.formData.tenantNo = this.tenantNo;

          if (Number(this.checkMsg) !== 2) {
            // 新增
            addOssSetting(this.formData).then((res) => {
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
            updateOssSetting(this.formData).then((res) => {
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
    // 租户配置汇总
    tenantSummary() {
      tenantSummary({
        id: this.tenantId,
      }).then((res) => {
        if (res.success) {
          let data = res.data;

          // 是否配置短信配置
          this.hasNextSet = data.smsFlag;

          // 跳转 短信配置
          this.$router.push({
            name: "SmsSetting",
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