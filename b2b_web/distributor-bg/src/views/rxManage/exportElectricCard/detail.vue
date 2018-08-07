<!--
 * @Author: yaowei
 * @Date: 2018-05-16 14:42:16
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-07 10:47:44
-->
<template>
  <div class="add-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">创建导出记录单</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 2">导出记录单详情</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回电子兑换卡导出记录表</el-button
      >
    </header>

    <div class="content">
      <el-form
        :model="formData"
        :rules="rules"
        label-width="21%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <!-- 指定分销商（只能指定一个分销商） -->
            <el-form-item label="指定分销商" prop="distriData">
              <el-button
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="distriShow = true"
                :disabled="isDisabled"
                >添加分销商</el-button
              >
              <el-table
                v-if="distriData.length > 0"
                :data="distriData"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="B2B编码"
                  align="center"
                  prop="distributorId"
                ></el-table-column>
                <el-table-column
                  label="分销名称"
                  align="center"
                  prop="distributorName"
                ></el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-delete-btn"
                      @click="handleDeleteDistributor(scope.$index, 2)"
                      >移除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 添加分销商 -->
              <el-dialog
                :modal-append-to-body="false"
                :visible="distriShow"
                :before-close="distriCancel"
                width="80%"
              >
                <select-distributor
                  :distributorData="distriData"
                  ref="selectDistributor"
                  @disClose="distriClose"
                  @disSubmit="distriSubmit"
                >
                </select-distributor>
              </el-dialog>
            </el-form-item>
            <el-form-item label="导出记录表名称" prop="exportName">
              <el-input
                v-model="formData.exportName"
                placeholder="不超30字"
                :disabled="isDisabled"
              />
            </el-form-item>

            <!-- 关联兑换卡活动（只能关联一个） -->
            <el-form-item label="关联兑换卡活动" prop="activityData">
              <el-button
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="activityShow = true"
                :disabled="isDisabled"
                >关联兑换卡活动</el-button
              >
              <el-table
                v-if="activityData.length > 0"
                :data="activityData"
                header-row-class-name="header-row"
                border
                row-key="id"
                class="tableCenter"
              >
                <el-table-column
                  label="兑换卡活动名称"
                  align="center"
                  prop="activityName"
                ></el-table-column>
                <el-table-column label="兑换卡类型" align="center">
                  <template slot-scope="scope">
                    <span v-if="scope.row.isEntity === 1">实体卡</span>
                    <span v-else>电子卡</span>
                  </template>
                </el-table-column>
                <el-table-column label="包邮设置" align="center">
                  <template slot-scope="scope">
                    <span v-if="scope.row.mailSetting === 1">包邮</span>
                    <span v-if="scope.row.mailSetting === 2"
                      >收运费（赠卡模式）</span
                    >
                    <!-- <span v-if="scope.row.mailSetting === 3"
                      >收运费（卖卡模式）</span
                    > -->
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-delete-btn"
                      @click="handleDeleteActivity(scope.$index)"
                      >移除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 添加兑换卡活动 -->
              <el-dialog
                :modal-append-to-body="false"
                :visible="activityShow"
                :before-close="activityCancel"
                width="80%"
              >
                <select-activity
                  :activityData="activityData"
                  :selectType="'electric'"
                  ref="selectActivity"
                  @disClose="activityClose"
                  @disSubmit="activitySubmit"
                >
                </select-activity>
              </el-dialog>
            </el-form-item>

            <el-form-item label="出库数量" prop="outNum">
              <el-input
                v-model="formData.outNum"
                placeholder="请输入出库数量"
                :disabled="isDisabled"
              />
            </el-form-item>

            <el-form-item label="备注" prop="remark">
              <el-input
                type="textarea"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="不超60字"
                v-model="formData.remark"
                :disabled="isDisabled"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="clearfix footbtn">
        <div>
          <el-button
            class="mini-search-btn box-btn"
            @click="handleSave('ruleForm')"
            :loading="loading"
            v-if="!isDisabled"
            >保存提交</el-button
          >
          <el-button size="mini" @click="handleBack">返回</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import selectActivity from "@/views/rxManage/components/selectActivity";
export default {
  name: "exportElectricCardDetail",
  components: {
    selectDistributor,
    selectActivity,
  },
  data() {
    // 指定分销商
    var validateDistriData = (rule, value, callback) => {
      if (this.distriData && this.distriData.length <= 0) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };

    // 关联兑换卡活动
    var validateActivityData = (rule, value, callback) => {
      if (this.activityData && this.activityData.length <= 0) {
        callback(new Error("请关联兑换卡活动"));
      } else {
        callback();
      }
    };

    return {
      actionImg: process.env.BASE_API + "system/v1/web/admin/oss/sts",
      loading: false,
      checkMsg: this.$route.query.checkMsg,
      isDisabled: false,
      formData: {
        exportName: "",
        outNum: "",
        remark: "",
        exchangeId: "",
        distributorId: "",
      },
      // 关联兑换卡活动
      activityData: [],
      activityShow: false,
      // 发卡分销商
      distriData: [],
      distriShow: false,
      // 结束时间
      endTime: "",
      expireTimeOption: {
        // 限制可选日期，不小于当天
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      rules: {
        distriData: [
          {
            required: true,
            validator: validateDistriData,
            trigger: "change",
          },
        ],
        exportName: [
          {
            required: true,
            message: "请输入活动标题",
            trigger: ["blur", "change"],
          },
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 60) {
                  callback(new Error("最多展示30个字，60个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        activityData: [
          {
            required: true,
            validator: validateActivityData,
            trigger: "change",
          },
        ],
        outNum: [
          {
            required: true,
            message: "请输入出库数量",
            trigger: ["blur", "change"],
          },
        ],
        remark: [
          {
            validator: (rule, value, callback) => {
              if (value) {
                var str = value + "";
                var len = 0;
                for (var i = 0; i < str.length; i++) {
                  var c = str.charCodeAt(i);
                  // 单字节加1
                  if (
                    (c >= 0x0001 && c <= 0x007e) ||
                    (0xff60 <= c && c <= 0xff9f)
                  ) {
                    len++;
                  } else {
                    len += 2;
                  }
                }
                if (len > 120) {
                  callback(new Error("最多展示60个字，120个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
      },
    };
  },
  created() {
    if (Number(this.checkMsg) === 2) {
      let row = JSON.parse(this.$route.query.row);
      if (Number(row.examineFlag) === 1) {
        // 已审核，不允许编辑
        this.isDisabled = true;
      }
      this.formData = row;

      // 分销商详情
      let ids = [row.distributorId];
      this.getDistributorByIds(ids);

      // 兑换卡活动详情
      this.getExchangeCardDetail(row.exchangeId);
    }
  },
  methods: {
    // 分销商详情
    getDistributorByIds(ids) {
      this.$http.getDistributorByIds(this, { ids: ids }).then((res) => {
        if (res.success) {
          this.distriData = [];
          if (res.data && res.data.length > 0) {
            res.data.forEach((item) => {
              let distri = {
                distributorId: item.id,
                distributorName: item.name,
              };
              this.distriData.push(distri);
            });
          }
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
            this.$http.addExchangeExport(this, this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "导出记录单新增成功",
                });
                this.clickLeave();
              }
            });
          } else {
            // 修改
            this.$http.editExchangeExport(this, this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "导出记录单修改成功",
                });
                this.clickLeave();
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

    // 清除验证提示
    clearValidate() {
      if (this.distriData.length > 0) {
        this.$refs["ruleForm"].clearValidate(["distriData"]);
      }
    },

    // 指定分销商
    distriSubmit(msg) {
      if (msg.length > 1) {
        this.$message({
          type: "warning",
          message: "只能选择一个分销商！",
        });
      } else {
        this.distriData = msg;
        this.distriShow = false;
        this.formData.distributorId = this.distriData[0].distributorId;
        this.clearValidate();
      }
    },
    distriCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    distriClose() {
      this.distriShow = false;
    },
    // 移除分销商
    handleDeleteDistributor(index, type) {
      this.distriData.splice(index, 1);
    },

    // 兑换卡活动详情
    getExchangeCardDetail(id) {
      this.$http.exchangeCardDetail(this, { id: id }).then((res) => {
        if (res.success) {
          this.activityData = [];
          let activityData = {
            activityName: res.data.codeName,
            activityId: res.data.id,
            isEntity: res.data.isEntity,
            mailSetting: res.data.mailSetting,
          };
          this.activityData.push(activityData);
        }
      });
    },
    // 关联兑换卡活动
    activitySubmit(msg) {
      if (msg.length > 1) {
        this.$message({
          type: "warning",
          message: "只能关联一个兑换卡活动！",
        });
      } else {
        this.activityData = msg;
        this.activityShow = false;
        if (this.activityData.length > 0) {
          this.formData.exchangeId = this.activityData[0].id;
          this.$refs["ruleForm"].clearValidate(["activityData"]);
        }
      }
    },
    activityCancel() {
      this.$refs.selectActivity.handleCancel();
    },
    activityClose() {
      this.activityShow = false;
    },
    // 移除关联兑换卡活动
    handleDeleteActivity(index) {
      this.activityData.splice(index, 1);
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.add-wrap {
  background-color: #fff;
  position: relative;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    .header_h4 {
      margin: 0 0 0 30px;
    }
    h4 {
      margin-left: 30px;
      display: inline-block;
      font-weight: 400;
    }
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
      margin-left: 0;
    }
  }
  .content {
    padding-top: 30px;
    min-width: 900px;
    .footbtn {
      padding-top: 30px;
      padding-bottom: 30px;
      text-align: center;
      .box-btn + .box-btn {
        margin-left: 10px;
      }
    }
  }
}
</style>