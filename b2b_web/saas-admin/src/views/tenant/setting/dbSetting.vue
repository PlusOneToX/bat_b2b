<!--
 * @Author: yaowei
 * @Date: 2019-10-20 09:38:45
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-10 10:38:22
-->
<template>
  <div class="detail-wrap">
    <div class="content">
      <h6 class="detail-title">
        数据库配置信息
        <span class="tips">
          （如选择自动生成需要手工点击创建数据库和数据表，数据库源名称和连接URL会创建成功后自动填写，用户名和密码需人工输入）
        </span>
      </h6>
      <el-form
        :model="ruleTable"
        :rules="rules"
        label-position="right"
        ref="ruleTable"
      >
        <div class="setting-table">
          <el-button type="primary" @click="handleAdd()">新增</el-button>
          <el-table
            :data="ruleTable.tableData"
            header-row-class-name="header-row"
            border
            class="tableCenter"
          >
            <el-table-column align="center" :min-width="160">
              <template slot="header">
                <span class="table-slot">*</span>
                <span>服务模板</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isAdd"
                  :prop="'tableData.' + scope.$index + '.modelType'"
                  :rules="rules.modelType"
                >
                  <el-select
                    v-model="scope.row.modelType"
                    @change="checkModelType"
                  >
                    <el-option
                      v-for="item in modelTypeList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      :disabled="item.disabled"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <span v-else>{{
                  modelTypeFormatter(scope.row.modelType)
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="110"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>类型</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isAdd"
                  :prop="'tableData.' + scope.$index + '.dbType'"
                  :rules="rules.dbType"
                >
                  <el-select v-model="scope.row.dbType">
                    <el-option label="独立库" :value="1"></el-option>
                    <el-option label="共享库" :value="2"></el-option>
                  </el-select>
                </el-form-item>
                <span v-else>{{ dbTypeFormatter(scope.row.dbType) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="130"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>来源</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isAdd"
                  :prop="'tableData.' + scope.$index + '.sourceType'"
                  :rules="rules.sourceType"
                >
                  <el-select v-model="scope.row.sourceType">
                    <!-- Mongodb 暂不支持自动生成 -->
                    <el-option
                      label="自动生成"
                      :value="1"
                      :disabled="
                        scope.row.modelType === 10 || scope.row.modelType === 11
                      "
                    ></el-option>
                    <el-option label="手工填写" :value="2"></el-option>
                  </el-select>
                </el-form-item>
                <span v-else>{{
                  sourceTypeFormatter(scope.row.sourceType)
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="180"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>数据库源名称</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="
                    (scope.row.isAdd || scope.row.isEdit) &&
                    scope.row.sourceType !== 1 &&
                    !(scope.row.modelType === 10 || scope.row.modelType === 11)
                  "
                  :prop="'tableData.' + scope.$index + '.dbName'"
                  :rules="rules.dbName"
                >
                  <el-input v-model="scope.row.dbName"></el-input>
                </el-form-item>
                <span v-else>{{
                  (scope.row.isAdd && scope.row.sourceType === 1) ||
                  (scope.row.isEdit &&
                    scope.row.sourceType === 1 &&
                    !scope.row.dbName)
                    ? ""
                    : scope.row.dbName
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="180"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>数据库连接URL</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="
                    (scope.row.isAdd || scope.row.isEdit) &&
                    scope.row.sourceType !== 1 &&
                    !(scope.row.modelType === 10 || scope.row.modelType === 11)
                  "
                  :prop="'tableData.' + scope.$index + '.dbUrl'"
                  :rules="rules.dbUrl"
                >
                  <el-input v-model="scope.row.dbUrl"></el-input>
                </el-form-item>
                <span v-else>{{
                  (scope.row.isAdd && scope.row.sourceType === 1) ||
                  (scope.row.isEdit &&
                    scope.row.sourceType === 1 &&
                    !scope.row.dbName)
                    ? ""
                    : scope.row.dbUrl
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="180"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>数据库连接基础URL</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="
                    (scope.row.isEdit &&
                      scope.row.sourceType !== 1 &&
                      !(
                        scope.row.modelType === 10 || scope.row.modelType === 11
                      )) ||
                    (scope.row.isAdd &&
                      !(
                        scope.row.modelType === 10 || scope.row.modelType === 11
                      ))
                  "
                  :prop="'tableData.' + scope.$index + '.dbBaseUrl'"
                  :rules="rules.dbBaseUrl"
                >
                  <el-input v-model="scope.row.dbBaseUrl"></el-input>
                </el-form-item>
                <span v-else>{{ scope.row.dbBaseUrl }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="130"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>数据库用户名</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isAdd || scope.row.isEdit"
                  :prop="'tableData.' + scope.$index + '.userName'"
                  :rules="rules.userName"
                >
                  <el-input v-model="scope.row.userName"></el-input>
                </el-form-item>
                <span v-else>{{ scope.row.userName }}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              show-overflow-tooltip
              :min-width="130"
            >
              <template slot="header">
                <span class="table-slot">*</span>
                <span>数据库密码</span>
              </template>
              <template slot-scope="scope">
                <el-form-item
                  v-if="scope.row.isAdd || scope.row.isEdit"
                  :prop="'tableData.' + scope.$index + '.password'"
                  :rules="rules.password"
                >
                  <el-input v-model="scope.row.password"></el-input>
                </el-form-item>
                <span v-else>********</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" :min-width="180" align="center">
              <template slot-scope="scope">
                <el-link
                  type="primary"
                  v-if="scope.row.sourceType === 1 && scope.row.tableFlag !== 1"
                  @click="handleCreateDB(scope.row.id)"
                  >创建数据库</el-link
                >
                <!-- <el-link
                  type="primary"
                  v-if="scope.row.isEdit"
                  @click="handleCancel(scope.row.id, scope.row)"
                  >取消</el-link
                > -->
                <el-link
                  type="primary"
                  v-if="!scope.row.isAdd && !scope.row.isEdit"
                  @click="handleEdit(scope.row.id)"
                  >编辑</el-link
                >
                <el-link
                  type="danger"
                  @click="handleDelete(scope.row, scope.$index)"
                  >删除</el-link
                >
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- Mongodb -->
        <template v-if="hasMongo">
          <h6 class="detail-title">数据库配置信息 - 非关系型数据库</h6>
          <div class="select-table">
            <el-table
              :data="ruleTable.mongoData"
              header-row-class-name="header-row"
              border
              class="tableCenter"
            >
              <el-table-column
                label="服务模板"
                align="center"
                show-overflow-tooltip
                :min-width="120"
              >
                <template slot-scope="scope">
                  <span>{{ modelTypeFormatter(scope.row.modelType) }}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :min-width="160"
              >
                <template slot="header">
                  <span class="table-slot">*</span>
                  <span>IP或域名</span>
                </template>
                <template slot-scope="scope">
                  <el-form-item
                    :prop="'mongoData.' + scope.$index + '.host'"
                    :rules="rules.password"
                  >
                    <el-input v-model="scope.row.host"></el-input>
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :min-width="120"
              >
                <template slot="header">
                  <span class="table-slot">*</span>
                  <span>端口</span>
                </template>
                <template slot-scope="scope">
                  <el-form-item
                    :prop="'mongoData.' + scope.$index + '.port'"
                    :rules="rules.password"
                  >
                    <el-input v-model="scope.row.port"></el-input>
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                show-overflow-tooltip
                :min-width="120"
              >
                <template slot="header">
                  <span class="table-slot">*</span>
                  <span>数据库</span>
                </template>
                <template slot-scope="scope">
                  <el-form-item
                    :prop="'mongoData.' + scope.$index + '.nosqlDatabase'"
                    :rules="rules.password"
                  >
                    <el-input v-model="scope.row.nosqlDatabase"></el-input>
                  </el-form-item>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-form>

      <div class="clearfix footbtn">
        <el-button @click="handleBack">返回</el-button>
        <el-button
          type="primary"
          @click="handleSave('ruleTable')"
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
  addDBSetting,
  editDBSetting,
  dbSettingDetail,
  deleteDBSetting,
  dbTableSetting,
  tenantSummary,
} from "@/api/tenant";

export default {
  name: "DBSetting",
  data() {
    return {
      loading: false,
      tenantId: this.$route.query.tenantId || "",
      tenantNo: this.$route.query.tenantNo || "",
      checkMsg: this.$route.query.checkMsg || "",
      ruleTable: {
        tableData: [],
        mongoData: [],
      },
      rules: {
        modelType: [
          {
            required: true,
            message: "请选择服务模板",
            trigger: ["blur", "change"],
          },
        ],
        dbType: [
          {
            required: true,
            message: "请选择类型",
            trigger: ["blur", "change"],
          },
        ],
        sourceType: [
          {
            required: true,
            message: "请选择来源",
            trigger: ["blur", "change"],
          },
        ],
        dbName: [
          {
            required: true,
            message: "请输入数据库源名称",
            trigger: ["blur", "change"],
          },
        ],
        dbUrl: [
          {
            required: true,
            message: "请输入数据库连接URL",
            trigger: ["blur", "change"],
          },
        ],
        dbBaseUrl: [
          {
            required: true,
            message: "请输入数据库连接基础URL",
            trigger: ["blur", "change"],
          },
        ],
        userName: [
          {
            required: true,
            message: "请输入数据库用户名",
            trigger: ["blur", "change"],
          },
        ],
        password: [
          {
            required: true,
            message: "请输入数据库密码",
            trigger: ["blur", "change"],
          },
        ],
        // Mongodb
        host: [
          {
            required: true,
            message: "请输入IP或域名",
            trigger: ["blur", "change"],
          },
        ],
        port: [
          {
            required: true,
            message: "请输入端口",
            trigger: ["blur", "change"],
          },
        ],
        nosqlDatabase: [
          {
            required: true,
            message: "请输入数据库",
            trigger: ["blur", "change"],
          },
        ],
      },
      modelTypeList: [
        {
          label: "商品服务",
          value: 1,
          disabled: false,
        },
        {
          label: "客户服务",
          value: 2,
          disabled: false,
        },
        {
          label: "仓库服务",
          value: 3,
          disabled: false,
        },
        {
          label: "系统服务",
          value: 4,
          disabled: false,
        },
        {
          label: "柔性定制服务",
          value: 5,
          disabled: false,
        },
        {
          label: "营销推广服务",
          value: 6,
          disabled: false,
        },
        {
          label: "订单服务",
          value: 7,
          disabled: false,
        },
        {
          label: "财务服务",
          value: 8,
          disabled: false,
        },
        {
          label: "第三方接口服务",
          value: 9,
          disabled: false,
        },
        {
          label: "Mongodb",
          value: 10,
          disabled: false,
        },
        {
          label: "Redis",
          value: 11,
          disabled: false,
        },
      ],
      hasMongo: false, // 是否选择mangodb
      hasNextSet: false, // 下一步是否已配置
    };
  },
  created() {
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      dbSettingDetail({ tenantNo: this.tenantNo }).then((res) => {
        if (res.success) {
          this.ruleTable.tableData = res.data;

          this.ruleTable.mongoData = [];
          this.ruleTable.tableData.forEach((item) => {
            if (item.modelType === 10) {
              // Mongodb
              let mongoData = {
                modelType: 10,
                host: item.host,
                port: item.port,
                nosqlDatabase: item.nosqlDatabase,
              };
              this.ruleTable.mongoData.push(mongoData);
            } else if (item.modelType === 11) {
              // Redis
              let mongoData = {
                modelType: 11,
                host: item.host,
                port: item.port,
                nosqlDatabase: item.nosqlDatabase,
              };
              this.ruleTable.mongoData.push(mongoData);
            }
          });

          // 检测服务模板（已选，不让重复选）
          this.checkModelType();
        }
      });
    },
    // 格式化服务模板
    modelTypeFormatter(type) {
      switch (type) {
        case 1:
          return "商品服务";
          break;
        case 2:
          return "客户服务";
          break;
        case 3:
          return "仓库服务";
          break;
        case 4:
          return "系统服务";
          break;
        case 5:
          return "柔性定制服务";
          break;
        case 6:
          return "营销推广服务";
          break;
        case 7:
          return "订单服务";
          break;
        case 8:
          return "财务服务";
          break;
        case 9:
          return "第三方接口服务";
          break;
        case 10:
          return "Mongodb";
          break;
        case 11:
          return "Redis";
          break;
      }
    },
    // 格式化数据库类型
    dbTypeFormatter(type) {
      switch (type) {
        case 1:
          return "独立库";
          break;
        case 2:
          return "共享库";
          break;
      }
    },
    // 格式化数据库来源
    sourceTypeFormatter(type) {
      switch (type) {
        case 1:
          return "自动生成";
          break;
        case 2:
          return "手工填写";
          break;
      }
    },
    // 检测服务模板
    checkModelType(value) {
      if (Number(value) === 10) {
        // Mongodb
        let mongoData = {
          modelType: 10,
          host: "",
          port: "",
          nosqlDatabase: "",
        };

        this.ruleTable.mongoData.push(mongoData);
      } else if (Number(value) === 11) {
        // Redis
        let mongoData = {
          modelType: 11,
          host: "",
          port: "",
          nosqlDatabase: "",
        };
        this.ruleTable.mongoData.push(mongoData);
      }

      // 重置已选服务模板
      this.modelTypeList.forEach((type) => {
        this.$set(type, "disabled", false);
      });
      // 设置已选服务模板不可重复选择
      this.ruleTable.tableData.forEach((item) => {
        this.modelTypeList.forEach((type) => {
          if (item.modelType === type.value) {
            this.$set(type, "disabled", true);
          }
        });
      });
    },
    // 新增
    handleAdd() {
      let dbData = {
        tenantId: this.tenantId,
        tenantNo: this.tenantNo,
        isAdd: true, // 是否新增
      };

      this.ruleTable.tableData.push(dbData);
    },
    // 创建数据库
    handleCreateDB(id) {
      dbTableSetting({ id: id }).then((res) => {
        if (res.success) {
          this.$message({
            type: "success",
            message: "创建成功",
          });
        }
      });
    },
    // 编辑
    handleEdit(id) {
      this.ruleTable.tableData.forEach((item) => {
        if (item.id === id) {
          this.$set(item, "isEdit", true);
        }
      });
    },
    // 取消
    handleCancel(id) {},
    // 删除
    handleDelete(row, index) {
      if (row.id) {
        this.$confirm("此操作将删除该服务模板，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then((_) => {
            deleteDBSetting({ id: row.id }).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
                this.ruleTable.tableData.splice(index, 1);

                // Mongodb/Redis
                if (row.modelType === 10 || row.modelType === 11) {
                  this.ruleTable.mongoData.forEach((item, index) => {
                    if (item.modelType === 10 || item.modelType === 11) {
                      this.ruleTable.mongoData.splice(index, 1);
                    }
                  });
                }

                // 检测服务模板（已选，不让重复选）
                this.checkModelType();
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

        // Mongodb/Redis
        if (row.modelType === 10 || row.modelType === 11) {
          this.ruleTable.mongoData.forEach((item, index) => {
            if (item.modelType === 10 || item.modelType === 11) {
              this.ruleTable.mongoData.splice(index, 1);
            }
          });
        }

        // 检测服务模板（已选，不让重复选）
        this.checkModelType();
      }
    },
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.ruleTable.tableData.forEach((item) => {
            this.ruleTable.mongoData.forEach((mongo) => {
              if (item.modelType === 10 && item.modelType === mongo.modelType) {
                // Mongodb 设置
                this.$set(item, "host", mongo.host);
                this.$set(item, "port", mongo.port);
                this.$set(item, "nosqlDatabase", mongo.nosqlDatabase);
              }
              if (item.modelType === 11 && item.modelType === mongo.modelType) {
                // Redis 设置
                this.$set(item, "host", mongo.host);
                this.$set(item, "port", mongo.port);
                this.$set(item, "nosqlDatabase", mongo.nosqlDatabase);
              }
            });
          });
          if (Number(this.checkMsg) !== 2) {
            // 新增
            addDBSetting(this.ruleTable.tableData).then((res) => {
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
            editDBSetting(this.ruleTable.tableData).then((res) => {
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

          // 是否配置定制信息配置
          this.hasNextSet = data.diyFlag;

          // 跳转 定制信息配置
          this.$router.push({
            name: "DiySetting",
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
  watch: {
    "ruleTable.tableData": {
      handler() {
        // 判断是否选择mangodb
        this.hasMongo = false;
        this.ruleTable.tableData.forEach((item) => {
          if (item.modelType === 10 || item.modelType === 11) {
            this.hasMongo = true;
          }
        });
      },
      deep: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.detail-wrap {
  padding: 20px 100px 50px;
  min-width: 750px;

  .detail-title {
    margin: 50px 0 30px;
  }

  .el-button + .el-table {
    margin-top: 10px;
  }

  .el-table {
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