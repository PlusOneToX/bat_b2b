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
        <h6 class="detail-title">ERP基础信息配置</h6>

        <el-form-item label="ERP类型" prop="erpType">
          <el-radio-group v-model="formData.erpType" size="mini">
            <el-radio :label="1">金蝶</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="基础URL" prop="baseUrl">
          <el-input v-model="formData.baseUrl" />
        </el-form-item>

        <el-form-item label="基础扩展URL" prop="baseExtUrl">
          <el-input v-model="formData.baseExtUrl" />
        </el-form-item>

        <el-form-item label="ERP登录用户名" prop="userName">
          <el-input v-model="formData.userName" />
        </el-form-item>

        <el-form-item label="ERP登录密码" prop="password">
          <el-input v-model="formData.password" />
        </el-form-item>

        <el-form-item label="ERP数据库ID" prop="dbId">
          <el-input v-model="formData.dbId" />
        </el-form-item>

        <el-form-item label="ERP语言" prop="lang">
          <el-input v-model="formData.lang" />
        </el-form-item>

        <el-form-item label="ERP平台" prop="platform">
          <el-input v-model="formData.platform" />
        </el-form-item>

        <el-form-item label="Token有效时间" prop="tokenValidTime">
          <el-input
            v-model="formData.tokenValidTime"
            @input="
              formData.tokenValidTime = formData.tokenValidTime.replace(
                /^\.+|[^\d.]/g,
                ''
              )
            "
          >
            <template slot="append">秒</template>
          </el-input>
        </el-form-item>

        <h6 class="detail-title">ERP业务信息配置</h6>

        <el-form-item label="物流费用编码" prop="wlfItemNo">
          <el-input v-model="formData.wlfItemNo" />
        </el-form-item>

        <el-form-item label="VMI仓库编码" prop="vmiWarehouse">
          <el-input v-model="formData.vmiWarehouse" />
        </el-form-item>

        <el-form-item label="订单缺省结算方式编码" prop="settleDefault">
          <el-input v-model="formData.settleDefault" />
        </el-form-item>

        <el-form-item label="订单线上结算方式编码" prop="settleCashOnline">
          <el-input v-model="formData.settleCashOnline" />
        </el-form-item>

        <el-form-item label="订单线下结算方式编码" prop="settleCashOffline">
          <el-input v-model="formData.settleCashOffline" />
        </el-form-item>

        <el-form-item label="订单月结结算方式编码" prop="settleMonth">
          <el-input v-model="formData.settleMonth" />
        </el-form-item>
				
		<el-form-item label="采购入库单据类型" prop="poInboundType">
		  <el-input v-model="formData.poInboundType" />
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
  addErpSetting,
  erpSettingDetail,
  updateErpSetting,
  tenantSummary,
} from "@/api/tenant";

export default {
  name: "ErpSetting",
  data() {
    return {
      loading: false,
      tenantId: this.$route.query.tenantId || "",
      tenantNo: this.$route.query.tenantNo || "",
      checkMsg: this.$route.query.checkMsg || "",
      formData: {
        erpType: 1,
        baseUrl: "",
        baseExtUrl: "",
        userName: "",
        password: "",
        dbId: "",
        lang: "",
        platform: "",
        tokenValidTime: "",
        wlfItemNo: "",
        vmiWarehouse: "",
        settleDefault: "",
        settleCashOnline: "",
        settleCashOffline: "",
        settleMonth: "",
		poInboundType: ""
      },
      rules: {
        erpType: [
          {
            required: true,
            message: "请选择ERP类型",
            trigger: ["blur", "change"],
          },
        ],
        baseUrl: [
          {
            required: true,
            message: "请输入基础URL",
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
        baseExtUrl: [
          {
            required: true,
            message: "请输入基础扩展URL",
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
        userName: [
          {
            required: true,
            message: "请输入ERP登录用户名",
            trigger: ["blur", "change"],
          },
        ],
        password: [
          {
            required: true,
            message: "请输入ERP登录密码",
            trigger: ["blur", "change"],
          },
        ],
        dbId: [
          {
            required: true,
            message: "请输入ERP数据库ID",
            trigger: ["blur", "change"],
          },
        ],
        lang: [
          {
            required: true,
            message: "请输入ERP语言",
            trigger: ["blur", "change"],
          },
        ],
        platform: [
          {
            required: true,
            message: "请输入ERP平台",
            trigger: ["blur", "change"],
          },
        ],
        tokenValidTime: [
          {
            required: true,
            message: "请输入Token有效时间",
            trigger: ["blur", "change"],
          },
        ],
        wlfItemNo: [
          {
            required: true,
            message: "请输入物流费用编码",
            trigger: ["blur", "change"],
          },
        ],
        vmiWarehouse: [
          {
            required: true,
            message: "请输入VMI仓库编码",
            trigger: ["blur", "change"],
          },
        ],
        settleDefault: [
          {
            required: true,
            message: "请输入订单缺省结算方式编码",
            trigger: ["blur", "change"],
          },
        ],
        settleCashOnline: [
          {
            required: true,
            message: "请输入订单线上结算方式编码",
            trigger: ["blur", "change"],
          },
        ],
        settleCashOffline: [
          {
            required: true,
            message: "请输入订单线下结算方式编码",
            trigger: ["blur", "change"],
          },
        ],
        settleMonth: [
          {
            required: true,
            message: "请输入订单月结结算方式编码",
            trigger: ["blur", "change"],
          },
        ],
        poInboundType: [
          {
            required: true,
            message: "请输入采购入库单据类型",
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
      erpSettingDetail({ id: this.tenantId }).then((res) => {
        if (res.success) {
		  console.log(res);
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
            addErpSetting(this.formData).then((res) => {
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
            updateErpSetting(this.formData).then((res) => {
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

          // 是否配置文件存储配置
          this.hasNextSet = data.ossFlag;

          // 跳转 文件存储配置
          this.$router.push({
            name: "OssSetting",
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