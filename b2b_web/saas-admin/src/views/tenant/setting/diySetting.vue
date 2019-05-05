<!--
 * @Author: yaowei
 * @Date: 2019-10-20 09:52:18
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-11 14:28:46
-->
<template>
  <div class="detail-wrap">
    <div class="content">
      <h6 class="detail-title">
        定制配置信息
        <span class="tips"
          >（工厂信息非必填，如未填写则在添加材质时无法选择工厂）</span
        >
      </h6>
      <el-button type="primary" @click="handleAdd()">新增</el-button>
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        class="tableCenter"
      >
        <el-table-column
          label="工厂名称"
          align="center"
          prop="factoryName"
          show-overflow-tooltip
          :min-width="100"
        >
          <template slot-scope="scope">
            <span>{{ factoryFormatter(scope.row.factoryNo) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="工厂类型"
          align="center"
          show-overflow-tooltip
          :min-width="100"
        >
          <template slot-scope="scope">
            <span>{{ factoryFormatter(scope.row.factoryNo) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="AppID"
          align="center"
          prop="appId"
          show-overflow-tooltip
          :min-width="100"
        >
        </el-table-column>
        <el-table-column
          label="AppKey"
          align="center"
          prop="appKey"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="AppSecret"
          align="center"
          prop="appSecret"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="添加订单URL"
          align="center"
          prop="orderAddUrl"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="取消订单URL"
          align="center"
          prop="orderCancelUrl"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="店铺名称"
          align="center"
          prop="shopName"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="供应商编码"
          align="center"
          prop="supplierNo"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="仓库编码"
          align="center"
          prop="warehouseNo"
          show-overflow-tooltip
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="其它配置内容"
          align="center"
          prop="configOther"
          show-overflow-tooltip
          :min-width="150"
        >
        </el-table-column>
        <el-table-column label="操作" :min-width="100" align="center">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleEditDiy(scope.row.id)"
              >编辑</el-link
            >
            <el-link type="danger" @click="handleDelete(scope.row.id)"
              >删除</el-link
            >
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="clearfix footbtn">
      <el-button @click="handleBack">返回</el-button>
      <el-button type="primary" plain @click="handleNext()">下一步</el-button>
    </div>

    <el-dialog title="新增工厂" :width="'600px'" :visible.sync="dialogVisible">
      <el-form
        :model="formData"
        :rules="rules"
        label-width="160px"
        ref="ruleForm"
      >
        <el-form-item label="工厂名称" prop="factoryName">
          <el-input v-model="formData.factoryName" clearable />
        </el-form-item>
        <el-form-item label="工厂类型" prop="factoryNo">
          <el-select v-model="formData.factoryNo">
            <el-option
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-for="item in factoryTypeList"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工厂AppID" prop="appId">
          <el-input v-model="formData.appId" clearable />
        </el-form-item>
        <el-form-item label="工厂AppKey" prop="appKey">
          <el-input v-model="formData.appKey" clearable />
        </el-form-item>
        <el-form-item label="工厂AppSecret" prop="appSecret">
          <el-input v-model="formData.appSecret" clearable />
        </el-form-item>
        <el-form-item label="工厂添加订单URL" prop="orderAddUrl">
          <el-input v-model="formData.orderAddUrl" clearable />
        </el-form-item>
        <el-form-item label="工厂取消订单URL" prop="orderCancelUrl">
          <el-input v-model="formData.orderCancelUrl" clearable />
        </el-form-item>
        <el-form-item label="店铺名称" prop="shopName">
          <el-input v-model="formData.shopName" clearable />
        </el-form-item>
        <el-form-item label="工厂供应商编码" prop="supplierNo">
          <el-input v-model="formData.supplierNo" clearable />
        </el-form-item>
        <el-form-item label="工厂仓库编码" prop="warehouseNo">
          <el-input v-model="formData.warehouseNo" clearable />
        </el-form-item>
        <el-form-item label="工厂其它配置内容" prop="configOther">
          <el-input type="textarea" v-model="formData.configOther" clearable />
        </el-form-item>
      </el-form>
      <div class="btn-wrap">
        <el-button @click="handleCancel()">取消</el-button>
        <el-button type="primary" @click="handleConfirm('ruleForm')"
          >确定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addDiySetting,
  deleteDiySetting,
  diySettingList,
  updateDiySetting,
  tenantSummary,
} from "@/api/tenant";

export default {
  name: "DiySetting",
  data() {
    return {
      tenantId: this.$route.query.tenantId || "",
      tenantNo: this.$route.query.tenantNo || "",
      checkMsg: this.$route.query.checkMsg || "",
      tableData: [],
      dialogVisible: false,
      // 工厂类型
      factoryTypeList: [
		{
		  label: "壳大师(飞快)",
		  value: "KDS_FK",
		},
        {
          label: "麦克",
          value: "MK",
        },
        {
          label: "烙印",
          value: "LY",
        },
        {
          label: "联辉王",
          value: "LHW",
        },
        {
          label: "多鸿欧丽科",
          value: "DH_OLK",
        },
      ],
      formData: {
        factoryName: "",
        factoryNo: "",
        appId: "",
        appKey: "",
        appSecret: "",
        orderAddUrl: "",
        orderCancelUrl: "",
        shopName: "",
        supplierNo: "",
        warehouseNo: "",
        configOther: "",
      },
      rules: {
        factoryName: [
          {
            required: true,
            message: "请输入工厂名称",
            trigger: ["blur", "change"],
          },
        ],
        factoryNo: [
          {
            required: true,
            message: "请选择工厂类型",
            trigger: ["blur", "change"],
          },
        ],
        appId: [
          {
            required: true,
            message: "请输入工厂AppId",
            trigger: ["blur", "change"],
          },
        ],
        appKey: [
          {
            required: true,
            message: "请输入工厂AppKey",
            trigger: ["blur", "change"],
          },
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
        appSecret: [
          {
            required: true,
            message: "请输入工厂AppSecret",
            trigger: ["blur", "change"],
          },
        ],
        orderAddUrl: [
          {
            required: true,
            message: "请输入工厂添加订单URL",
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
        orderCancelUrl: [
          {
            required: true,
            message: "请输入工厂取消订单URL",
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
        shopName: [
          {
            required: true,
            message: "请输入店铺名称",
            trigger: ["blur", "change"],
          },
        ],
        supplierNo: [
          {
            required: true,
            message: "请输入工厂供应商编码",
            trigger: ["blur", "change"],
          },
        ],
        warehouseNo: [
          {
            required: true,
            message: "请输入工厂仓库编码",
            trigger: ["blur", "change"],
          },
        ],
      },
      hasNextSet: false, // 下一步是否已配置
    };
  },
  created() {
    this.initData();
  },
  activated() {
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      diySettingList({ id: this.tenantId }).then((res) => {
        if (res.success) {
          this.tableData = res.data;
        }
      });
    },
    // 格式化工厂类型
    factoryFormatter(type) {
      switch (type) {
		case "KDS_FK":
		  return "壳大师(飞快)";
		  break;
        case "MK":
          return "麦克";
          break;
        case "LY":
          return "烙印";
          break;
        case "LHW":
          return "联辉王";
          break;
        case "DH_OLK":
          return "多鸿欧丽科";
          break;
      }
    },
    // 删除
    handleDelete(id) {
      this.$confirm("此操作将删除该定制配置信息，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          deleteDiySetting({ id: id }).then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.initData();
            }
          });
        })
        .catch((_) => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 新增
    handleAdd() {
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.$refs["ruleForm"].resetFields();
      });
    },
    // 确定
    handleConfirm(formName) {
      console.log(this.formData);
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          if (this.formData.isEdit) {
            // 编辑
            updateDiySetting(this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "修改成功",
                });
                this.dialogVisible = false;
                this.resetValidate("ruleForm");
                this.initData();
              }
            });
          } else {
            // 新增
            this.formData.tenantId = this.tenantId;
            this.formData.tenantNo = this.tenantNo;
            addDiySetting(this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.dialogVisible = false;
                this.resetValidate("ruleForm");
                this.initData();
              }
            });
          }
        }
      });
    },
    // 取消
    handleCancel() {
      this.dialogVisible = false;
      this.$nextTick(() => {
        this.formData = {
          factoryName: "",
          factoryNo: "",
          appId: "",
          appKey: "",
          appSecret: "",
          orderAddUrl: "",
          orderCancelUrl: "",
          shopName: "",
          supplierNo: "",
          warehouseNo: "",
          configOther: "",
        };

        this.$refs["ruleForm"].clearValidate();
      });
    },
    // 编辑
    handleEditDiy(id) {
      this.tableData.forEach((item) => {
        if (item.id === id) {
          this.dialogVisible = true;
          this.formData = item;
          this.$set(this.formData, "isEdit", true);
        }
      });
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    // 返回
    handleBack() {
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

          // 是否配置ERP信息配置
          this.hasNextSet = data.erpFlag;

          // 跳转 ERP信息配置
          this.$router.push({
            name: "ErpSetting",
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

  .el-table {
    margin-top: 10px;
  }

  .el-form {
    padding-right: 60px;
  }

  .btn-wrap {
    padding-top: 10px;
    text-align: center;
  }
}
</style>