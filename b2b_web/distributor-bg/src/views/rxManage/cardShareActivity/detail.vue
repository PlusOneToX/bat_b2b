<!--
 * @Author: yaowei
 * @Date: 2018-05-16 14:46:43
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-06 14:39:54
-->
<template>
  <div class="add-wrap">
    <header>
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">创建转发活动</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 2">转发活动详情</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
        >返回转发活动配置</el-button
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
            <el-form-item label="减免优惠活动名称" prop="preferName">
              <el-input
                v-model="formData.preferName"
                placeholder="请输入减免优惠活动名称，不超30个字"
              />
            </el-form-item>
            <el-form-item label="活动所属平台" prop="activityPlatform">
              <el-select
                v-model="formData.activityPlatform"
                placeholder="活动平台"
              >
                <el-option label="兑换商城" :value="1"> </el-option>
                <el-option label="定制商城" :value="2"> </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="活动位置" prop="seat">
              <el-radio-group v-model="formData.seat" size="mini">
                <el-radio :label="1">确认订单页</el-radio>
                <el-radio :label="2">订单详情</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="转发按钮文案" prop="forwardButtonText">
              <el-input
                v-model="formData.forwardButtonText"
                placeholder="不超12个字，示例：分享减0.5元邮费"
              />
            </el-form-item>
            <template v-if="formData.seat === 1">
              <el-form-item label="是否减免优惠" prop="reduceFlag">
                <el-radio-group v-model="formData.reduceFlag" size="mini">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item
                v-if="formData.reduceFlag === 1"
                label="减免金额"
                prop="reduceAmount"
              >
                <el-input
                  v-model="formData.reduceAmount"
                  placeholder="请输入减免金额，最小精确到0.01元"
                  style="width: 300px"
                />
                元
              </el-form-item>
            </template>

            <!-- 关联营销专题活动（只能关联一个） -->
            <el-form-item label="关联营销专题活动" prop="activityData">
              <el-button
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="handleActivity"
                >关联营销专题活动</el-button
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
                  label="活动ID"
                  align="center"
                  prop="id"
                ></el-table-column>
                <el-table-column
                  label="专题活动标题"
                  align="center"
                  prop="title"
                ></el-table-column>
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
              <p class="tips red">只能关联一个营销专题活动</p>
            </el-form-item>

            <!-- 活动可视分销商 -->
            <el-form-item label="活动可视分销商" prop="applyDistriData">
              <el-radio-group
                v-model="formData.distributorVisualType"
                @change="clearValidate"
              >
                <el-radio :label="1">全部分销商</el-radio>
                <el-radio :label="2">指定分销商</el-radio>
              </el-radio-group>
              <div v-if="formData.distributorVisualType === 2">
                <el-button
                  class="mini-search-btn add-goods-btn"
                  icon="el-icon-plus"
                  @click="applyDistriShow = true"
                  >添加分销商</el-button
                >
                <el-table
                  v-if="applyDistriData.length > 0"
                  :data="applyDistriData"
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
                        @click="handleDeleteDistributor(scope.$index, 1)"
                        >移除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>

                <!-- 添加指定适用分销商 -->
                <el-dialog
                  :modal-append-to-body="false"
                  :visible="applyDistriShow"
                  :before-close="disApplyCancel"
                  width="80%"
                >
                  <select-distributor
                    :distributorData="applyDistriData"
                    ref="selectApplyDistributor"
                    @disClose="disApplyClose"
                    @disSubmit="disApplySubmit"
                  >
                  </select-distributor>
                </el-dialog>
              </div>
            </el-form-item>

            <!-- 活动不适用分销商 -->
            <el-form-item
              v-if="formData.distributorVisualType === 1"
              label="活动不适用分销商"
            >
              <el-button
                class="mini-search-btn add-goods-btn"
                icon="el-icon-plus"
                @click="noApplyDistriShow = true"
                >添加分销商</el-button
              >
              <el-table
                v-if="noApplyDistriData.length > 0"
                :data="noApplyDistriData"
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

              <!-- 添加不适用分销商 -->
              <el-dialog
                :modal-append-to-body="false"
                :visible="noApplyDistriShow"
                :before-close="disNoApplyCancel"
                width="80%"
              >
                <select-distributor
                  :distributorData="noApplyDistriData"
                  ref="selectNoApplyDistributor"
                  @disClose="disNoApplyClose"
                  @disSubmit="disNoApplySubmit"
                >
                </select-distributor>
              </el-dialog>
            </el-form-item>

            <el-form-item label="自动上下线时间" prop="validTime">
              <el-date-picker
                v-model="formData.validTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="自动上线时间"
                end-placeholder="自动下线时间"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @input="handleDate"
              ></el-date-picker>
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

    <!-- 关联营销专题活动 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="activityShow"
      :before-close="activityCancel"
      width="80%"
      class="activity-dialog"
    >
      <div class="search-wrap">
        <div class="Fleft">
          <el-select
            size="mini"
            v-model="activityInfo.type"
            placeholder="活动类型"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="转发活动" :value="1"></el-option>
          </el-select>
          <el-select
            size="mini"
            v-model="activityInfo.activityPlatform"
            placeholder="所属平台"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="兑换商城" :value="1"></el-option>
            <el-option label="定制商城" :value="2"></el-option>
          </el-select>
        </div>
        <div class="Fsearch">
          <button class="mini-search-btn box-btn" @click="Csearch()">
            搜索
          </button>
          <el-input
            v-model.trim="activityInfo.content"
            size="mini"
            clearable
            @change="contentChange"
            @keyup.enter.native="Csearch()"
            placeholder="请输入活动名称/分销商名称"
            class="box-input"
          ></el-input>
          <el-select
            size="mini"
            v-model="contentType"
            style="width: 100px"
            @change="Csearch()"
            clearable
          >
            <el-option label="活动名称" :value="1"></el-option>
            <el-option label="分销商名称" :value="2"></el-option>
          </el-select>
        </div>
      </div>
      <el-table
        ref="multipleSelect"
        :data="activityAllData"
        tooltip-effect="dark"
        @select="select"
        @select-all="selectAll"
        row-key="id"
        style="text-align: center"
        border
        header-row-class-name="header-row"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="50"
          reserve-selection
          :selectable="selectable"
        ></el-table-column>
        <el-table-column
          label="活动ID"
          align="center"
          prop="id"
          :min-width="100"
        ></el-table-column>
        <el-table-column
          label="专题活动标题"
          align="center"
          prop="title"
          show-overflow-tooltip
          :min-width="150"
        ></el-table-column>
        <el-table-column
          label="活动所属平台"
          align="center"
          show-overflow-tooltip
          :min-width="120"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.activityPlatform === 1">兑换商城</span>
            <span v-else-if="scope.row.activityPlatform === 2">定制商城</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column
          label="结束时间"
          align="center"
          prop="endTime"
          show-overflow-tooltip
          :min-width="180"
        >
        </el-table-column>
        <el-table-column label="状态" align="center" width="90">
          <template slot-scope="scope">
            <span v-if="scope.row.status === 1">进行中</span>
            <span v-else>已停用</span>
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="activityTotal"
        :page="activityInfo.page"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>

      <el-button
        style="margin-left: 46%; margin-top: 10px"
        class="mini-search-btn"
        @click="activitySubmit"
        >确定</el-button
      >
      <el-button size="mini" style="margin-top: 10px" @click="activityCancel"
        >返回</el-button
      >
    </el-dialog>
  </div>
</template>

<script>
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";

export default {
  name: "cardShareActivityDetail",
  components: {
    selectDistributor,
    page,
  },
  data() {
    // 关联营销专题活动
    var validateActivityData = (rule, value, callback) => {
      if (this.activityData && this.activityData.length <= 0) {
        callback(new Error("请关联营销专题活动"));
      } else {
        callback();
      }
    };

    // 活动可视分销商
    var validateApplyDistriData = (rule, value, callback) => {
      if (
        this.formData.distributorVisualType === 2 &&
        this.applyDistriData &&
        this.applyDistriData.length <= 0
      ) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };

    return {
      loading: false,
      id: this.$route.query.id,
      checkMsg: this.$route.query.checkMsg,
      isDisabled: false,
      formData: {
        preferName: "",
        activityPlatform: 1,
        seat: 1,
        forwardButtonText: "",
        reduceFlag: 1,
        reduceAmount: "",
        distributorVisualType: 1,
        validTime: [],
        startTime: "",
        endTime: "",
        exchangeSpecialId: "",
        distributorIds: [],
      },
      // 关联营销专题活动
      activityData: [],
      // 活动可视分销商
      applyDistriData: [],
      applyDistriShow: false,
      // 不适用分销商
      noApplyDistriData: [],
      noApplyDistriShow: false,
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      rules: {
        preferName: [
          {
            required: true,
            message: "请输入减免优惠活动名称",
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
        activityPlatform: [
          {
            required: true,
            message: "请选择所属平台",
            trigger: ["blur", "change"],
          },
        ],
        seat: [
          {
            required: true,
            message: "请选择活动位置",
            trigger: ["blur", "change"],
          },
        ],
        forwardButtonText: [
          {
            required: true,
            message: "请输入转发按钮文案",
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
                if (len > 24) {
                  callback(new Error("最多展示12个字，24个字符"));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
          },
        ],
        reduceFlag: [
          {
            required: true,
            message: "请选择是否减免优惠",
            trigger: ["blur", "change"],
          },
        ],
        reduceAmount: [
          {
            required: true,
            message: "请输入减免金额",
            trigger: ["blur", "change"],
          },
        ],
        activityData: [
          {
            required: true,
            validator: validateActivityData,
            trigger: "change",
          },
        ],
        applyDistriData: [
          {
            required: true,
            validator: validateApplyDistriData,
            trigger: "change",
          },
        ],
        validTime: [
          {
            required: true,
            message: "请选择自动上下线时间",
            trigger: "change",
          },
        ],
      },

      // 关联营销活动
      activityInfo: {
        page: 1,
        size: 10,
        type: 1, // 活动类型
        activityPlatform: 1, // 所属平台
        content: undefined,
      },
      activityAllData: [],
      contentType: 1,
      activityShow: false,
      activityTotal: 0, // 关联营销活动总数
      multipleSelect: [], // 已选择
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
      // 查看详情
      this.$http.exchangeShareDetail(this, { id: id }).then((res) => {
        if (res.success) {
          this.formData = res.data;
          // 自动上下线时间
          this.formData.validTime = [
            new Date(res.data.startTime).getTime(),
            new Date(res.data.endTime).getTime(),
          ];

          // 分销商详情
          let distributor = res.data.distributor;
          if (distributor && distributor.length > 0) {
            distributor.forEach((distri) => {
              this.getDistributorByIds([distri.distributorId], distri);
            });
          }
          if (Number(res.data.distributorVisualType) === 1) {
            // 全部
            if (distributor && distributor.length > 0) {
              // 活动不适用分销商
              this.noApplyDistriData = distributor;
            }
          } else {
            // 活动可视分销商 - 指定
            this.applyDistriData = distributor;
          }

          // 关联营销专题活动
          this.initActivityDetail(res.data.exchangeSpecialId);
        }
      });
    },
    // 分销商详情
    getDistributorByIds(ids, item) {
      this.$http.getDistributorByIds(this, { ids: ids }).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let distri = "";
            res.data.forEach((item) => {
              distri += item.name + ",";
            });

            this.$set(
              item,
              "distributorName",
              distri.substring(0, distri.length - 1)
            );
          }
        }
      });
    },
    // 关联营销活动详情
    initActivityDetail(id) {
      this.$http.exchangeSpecialDetail(this, { id: id }).then((res) => {
        if (res.success) {
          this.activityData = [];
          let activityData = {
            id: res.data.id,
            title: res.data.title,
          };
          this.activityData.push(activityData);
        }
      });
    },
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.formData.startTime = this.formData.validTime[0];
          this.formData.endTime = this.formData.validTime[1];
          delete this.formData.validTime;
          if (Number(this.checkMsg) === 1) {
            // 新增
            this.$http.addExchangeShare(this, this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "活动新增成功",
                });
                this.clickLeave();
              }
            });
          } else {
            let distributorIds = [];
            this.formData.distributor.forEach((item) => {
              distributorIds.push(item.distributorId);
            });
            this.formData.distributorIds = distributorIds;
            delete this.formData.distributor;
            // 修改
            this.$http.editExchangeShare(this, this.formData).then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "活动修改成功",
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

    // 监听时间选择
    handleDate(value) {
      this.formData.validTime = value;
      this.$forceUpdate();
    },

    // 清除验证提示
    clearValidate() {
      if (
        this.formData.distributorVisualType === 2 &&
        this.applyDistriData.length > 0
      ) {
        this.$refs["ruleForm"].clearValidate(["applyDistriData"]);
      }
    },
    // 活动可视分销商
    disApplySubmit(msg) {
      this.applyDistriData = msg;
      this.applyDistriShow = false;
      if (this.applyDistriData.length > 0) {
        let distributorIds = [];
        this.applyDistriData.forEach((distri) => {
          distributorIds.push(distri.distributorId);
        });

        this.formData.distributorIds = distributorIds;
      }
      this.clearValidate();
    },
    disApplyCancel() {
      this.$refs.selectApplyDistributor.handleCancel();
    },
    disApplyClose() {
      this.applyDistriShow = false;
    },

    // 不适用分销商
    disNoApplySubmit(msg) {
      this.noApplyDistriData = msg;
      this.noApplyDistriShow = false;
      if (this.noApplyDistriData.length > 0) {
        let distributorIds = [];
        this.noApplyDistriData.forEach((distri) => {
          distributorIds.push(distri.distributorId);
        });

        this.formData.distributorIds = distributorIds;
      }
    },
    disNoApplyCancel() {
      this.$refs.selectNoApplyDistributor.handleCancel();
    },
    disNoApplyClose() {
      this.noApplyDistriShow = false;
    },

    // 移除分销商
    handleDeleteDistributor(index, type) {
      if (Number(type) === 1) {
        // 活动可视分销商
        this.applyDistriData.splice(index, 1);
      } else {
        // 不适用分销商
        this.noApplyDistriData.splice(index, 1);
      }
    },

    /* 关联营销专题活动 */
    handleActivity() {
      this.initActivityData();
    },
    activityCancel() {
      this.activityShow = false;
    },
    activitySubmit() {
      if (this.multipleSelect && this.multipleSelect.length > 1) {
        this.$message({
          type: "warning",
          message: "只能关联一个营销专题活动！",
        });
      } else {
        this.activityData = this.multipleSelect;
        this.formData.exchangeSpecialId = this.multipleSelect[0].id;
        this.activityShow = false;
      }
    },
    handleDeleteActivity(index) {
      this.activityData.splice(index, 1);
    },
    // 初始化列表数据
    initActivityData() {
      this.$http.exchangeSpecialList(this, this.activityInfo).then((res) => {
        if (res.success) {
          this.activityAllData = res.data.list;
          this.activityTotal = res.data.total;
          for (var i = 0; i < res.data.list.length; i++) {
            this.activityAllData.forEach((item) => {
              if (item.id === res.data.list[i].id) {
                // 结束时间
                var endTime = timeFormat(res.data.list[i].endTime);
                item.endTime = endTime;
              }
            });
          }

          this.activityShow = true;
        }
      });
    },
    // 列表条数
    sizeChange(size) {
      this.activityInfo.size = size;
      this.activityInfo.page = 1;
      this.initActivityData();
    },
    // 列表页数
    currentChange(page) {
      this.activityInfo.page = page;
      this.initActivityData();
    },
    // 搜索列表
    Csearch() {
      this.activityInfo.page = 1;
      this.initActivityData();
    },
    // 输入框输入搜索列表
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.Csearch();
      }
    },
    selectable(row) {
      // 进行中
      if (row.status === 1) {
        return true;
      } else {
        return false;
      }
    },
    // 单选时调用
    select(selection, row) {
      this.isSelect = true;
      let d = false;
      for (let i = 0; i < this.multipleSelect.length; i++) {
        if (this.multipleSelect[i].id === row.id) {
          this.multipleSelect.splice(i, 1);
          d = true;
          break;
        }
      }
      if (!d) {
        this.multipleSelect.push(row);
        this.multipleSelect = this.setArr(this.multipleSelect);
      }
    },
    // 全选时调用
    selectAll(selection) {
      this.isSelect = true;
      if (selection.length === 0) {
        this.codeData.forEach((row) => {
          for (let i = 0; i < this.multipleSelect.length; i++) {
            if (this.multipleSelect[i].id === row.id) {
              this.multipleSelect.splice(i, 1);
              break;
            }
          }
        });
      } else {
        this.multipleSelect = this.setArr(
          this.multipleSelect.concat(selection)
        );
      }
    },
    // 当切换页面时的作用
    handleSelectionChange(val) {
      if (
        val.length === 0 &&
        this.multipleSelect.length != 0 &&
        !this.isSelect
      ) {
        this.multipleSelect.forEach((row1) => {
          this.activityAllData.forEach((row2) => {
            if (row1.id === row2.id) {
              this.$refs.multipleSelect.toggleRowSelection(row2);
            }
          });
        });
        this.isSelect = true;
      }
    },
    setArr(arr) {
      // 去重
      const obj = {};
      const temp = [];
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id); // 不加类型 分不清 1 '1'
        if (!obj[arr[i].id + type]) {
          temp.push(arr[i]);
          obj[arr[i].id + type] = true; // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp;
    },
    /* 关联营销专题活动 */
  },
};
</script>

<style lang="scss" scoped>
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

.tips {
  padding-bottom: 10px;
  color: #666666;
  line-height: 20px;
  &.red {
    color: #e8574f;
  }
}
</style>