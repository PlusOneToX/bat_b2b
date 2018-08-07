<template>
  <div class="check-detail">
    <header>
      <h4 class="header_h4">代金券审批详情</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="back()"
      >
        返回代金券审批列表
      </el-button>
    </header>
    <!-- 审批头部信息 -->
    <div class="box-has-border">
      <div class="text-center title" style="margin-top: 0px">
        <span>基本信息</span>
      </div>
      <div class="half-width">
        <el-form ref="formData" label-width="280px">
          <el-form-item label="审批单号:">{{ formData.id }}</el-form-item>
          <el-form-item label="发起人:">{{ formData.userName }}</el-form-item>
          <el-form-item label="发起时间:">{{
            timeFormatter(formData.createTime)
          }}</el-form-item>
        </el-form>
      </div>
      <div class="half-width">
        <el-form ref="formData" label-width="280px">
          <el-form-item label="审批类型:">
            {{ forstatus(formData.checkType) }}
          </el-form-item>
          <el-form-item label="审批状态:">{{
            cStatus(formData.checkStatus)
          }}</el-form-item>
        </el-form>
      </div>
    </div>
    <!-- 流程进度 -->
    <div class="box-has-border step">
      <div class="s-title">流程进度</div>
      <div class="step-box">
        <el-steps :active="checkActive" finish-status="success">
          <el-step
            :title="'发起审批：' + formData.userName"
            :description="formData.createTime"
          ></el-step>
          <el-step
            v-for="item in formData.checkFlows"
            :key="item.id"
            :title="'审批人：' + item.userName"
            :description="item.checkTime"
          >
            <template slot-scope="scope">
              <div>{{ scope.userName }}</div>
            </template>
          </el-step>
          <el-step
            v-if="formData.checkFlows"
            title="完成"
            description=""
          ></el-step>
        </el-steps>
      </div>
    </div>
    <div class="btn-box" v-if="isCheck">
      <el-form :model="checkData" label-width="160px">
        <el-form-item prop="remark" label="审批备注:" style="width: 80%">
          <el-input
            v-model="checkData.remark"
            type="textarea"
            :rows="5"
            placeholder="请输入内容"
          >
          </el-input>
        </el-form-item>
        <el-form-item style="text-align: left">
          <span>审批操作：</span>
          <el-button
            :loading="submitLoading"
            class="mini-search-btn"
            type="success"
            @click="submit(1)"
            size="mini"
            >同意</el-button
          >
          <el-button :loading="submitLoading" @click="submit(2)" size="mini"
            >拒绝</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!-- 代金券清单 -->
    <div class="box-has-border">
      <div class="box-has-border">
        <div class="l-title">
          <span>代金券清单</span>
        </div>
        <el-table
          :data="formData.rebateVouchers"
          border
          style="width: 100%"
          header-row-class-name="header-row"
        >
          <el-table-column
            align="center"
            label="所属分销商"
            prop="distributorName"
            :min-width="120"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            align="center"
            label="券号"
            prop="voucherNo"
            :min-width="180"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            align="center"
            label="券名"
            prop="name"
            :min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
            align="center"
            label="面值"
            prop="faceValue"
            :min-width="100"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>¥{{ scope.row.faceValue }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="有效期"
            :min-width="300"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span
                >{{ timeFormatter(scope.row.startTime) }} ~
                {{ timeFormatter(scope.row.endTime) }}</span
              >
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="remark"
            :min-width="150"
            show-overflow-tooltip
          >
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { parseTime } from "@/utils/index";
export default {
  name: "voucherCheckDetail",
  data() {
    return {
      submitLoading: false,
      isCheck: false, // 审批权限
      checkActive: 1,
      formData: {},
      checkData: {
        remark: "",
      },
    };
  },
  mounted() {
    this.getDetail();
  },
  methods: {
    // 审核详情
    getDetail() {
      this.$http
        .promotionCheckDetail(this, { id: this.$route.query.id })
        .then((res) => {
          if (res.success) {
            this.formData = res.data;
            if (
              this.formData.checkStatus === 1 ||
              this.formData.checkStatus === 2
            ) {
              // 审批通过或不通过
              this.checkActive = this.formData.checkFlows.length + 2;
              this.isCheck = false;
            } else {
              this.formData.checkFlows.forEach((item) => {
                if (item.userId === this.formData.checkUserId) {
                  this.checkActive = item.checkSort;
                }
              });
              let userId = this.$store.getters.userinfo.id;
              if (userId === this.formData.checkUserId) {
                this.isCheck = true;
              }
            }
          }
        });
    },
    // 审批操作
    submit(type) {
      this.checkData.id = this.$route.query.id;
      this.checkData.checkStatus = type;
      this.$http
        .promotionCheck(this, this.checkData)
        .then((res) => {
          if (res.success) {
            this.$message.success({
              message: "操作成功！",
              duration: 3 * 1000,
            });
            this.$router.push({ name: "voucherCheck" });
          } else {
            this.$message.error(res.errMessage);
          }
        })
        .catch((err) => {
          this.$message.error(err);
        });
    },
    back() {
      this.$router.push({ name: "voucherCheck" });
    },
    forstatus(row) {
      // 审批类型
      switch (row) {
        case 1:
          return "新增";
        case 2:
          return "修改";
      }
    },
    cStatus(row) {
      // 审批状态
      switch (row) {
        case 0:
          return "审批中";
        case 1:
          return "审批通过";
        case 2:
          return "审批未通过";
      }
    },
    timeFormatter(cellValue) {
      // 时间格式化
      return parseTime(cellValue);
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.check-detail {
  height: 100%;
  min-width: 1050px;
  background-color: white;
  padding-bottom: 30px;
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
  .box-has-border {
    overflow: hidden;
    &.step {
      margin-top: 20px;
      background-color: #e7fafb;
    }
    .half-width {
      width: 50%;
      box-sizing: border-box;
      float: left;
    }
    .title {
      margin: 20px 0;
      text-align: center;
      font-size: 18px;
      border-top: 1px solid #dcdcdc;
    }
    .s-title {
      padding-top: 20px;
      text-align: center;
      font-size: 18px;
      border-top: 1px solid #dcdcdc;
    }
    .l-title {
      padding: 50px 0 20px;
      text-align: center;
      font-size: 18px;
      border-top: 1px solid #dcdcdc;
    }
    div.form {
      margin-top: 30px;
      margin-bottom: 40px;
      form.el-form {
        margin-right: 0;
        width: 80%;
        min-width: 800px;
        max-width: 1000px;
      }
    }
    .step-box {
      margin: 40px auto;
      width: 80%;
    }
  }
  /deep/ .el-table .cell span {
    white-space: normal;
  }
  .btn-box {
    margin: 30px;
    text-align: right;
    .el-steps {
      color: #21b8cb;
    }
    /deep/.el-step__title {
      &.is-success {
        color: #21b8cb;
      }
    }
  }
  .el-table {
    /deep/.cell {
      white-space: pre-line;
    }
  }
}
</style>