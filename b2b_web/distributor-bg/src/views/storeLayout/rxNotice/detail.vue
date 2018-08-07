<!--
 * @Author: yaowei
 * @Date: 2018-05-25 19:36:59
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-03 16:30:41
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4">
        {{ enterFlag === "add" ? "添加公告" : "公告详情" }}
      </h4>
    </header>

    <div class="content">
      <el-form
        :model="detailData"
        :rules="rules"
        label-width="20%"
        label-position="right"
        ref="ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <el-form-item label="公告内容:" prop="content">
              <el-input
                style="width: 300px"
                type="textarea"
                :rows="8"
                size="small"
                placeholder="请输入公告内容"
                v-model="detailData.content"
              ></el-input>
            </el-form-item>
            <el-form-item label="公告展示时间:" prop="validTime">
              <el-date-picker
                v-model="detailData.validTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @input="handleDate"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="状态">
              <el-radio-group v-model="detailData.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="clearfix footbtn">
        <el-button
          class="mini-search-btn box-btn"
          @click="handleSubmit('ruleForm')"
          :loading="loadingStatus"
          >确定</el-button
        >
        <el-button size="mini" @click="handleBack">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getNoticeDetail,
  addNotice,
  editNotice,
} from "@/views/storeLayout/rxData";

export default {
  name: "noticeDetail",
  data() {
    return {
      enterFlag: this.$route.query.enterFlag,
      id: this.$route.query.id,
      detailData: {
        content: "",
        validTime: [],
        startTime: "",
        endTime: "",
        status: 1,
      },
      rules: {
        content: [
          {
            required: true,
            message: "请输入公告内容",
            trigger: "blur",
          },
        ],
        validTime: [
          {
            required: true,
            message: "请选择有效时间",
            trigger: "change",
          },
        ],
        status: [
          {
            required: true,
            message: "请选择状态",
            trigger: "change",
          },
        ],
      },
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      loadingStatus: false,
    };
  },
  mounted() {
    if (this.enterFlag && this.enterFlag !== "add") {
      this.initDetail(Number(this.id));
    }
  },
  methods: {
    initDetail(id) {
      // getNoticeDetail(this, id).then((res) => {
      this.$http.exchangeNoticeDetail(this, {id: id}).then(res => {  
        if (res.success) {
          this.detailData = res.data;

          // 有效时间
          this.detailData.validTime = [
            new Date(res.data.startTime).getTime(),
            new Date(res.data.endTime).getTime(),
          ];
        }
      });
    },
    // 监听时间选择
    handleDate(value) {
      this.detailData.validTime = value;
      this.$forceUpdate();
    },
    // 返回列表
    handleBack() {
      this.resetValidate("ruleForm");
      this.clickLeave();
    },
    // 页面跳转
    clickLeave() {
      this.$router.go(-1);
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    handleSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
        this.loadingStatus = true;
        this.detailData.startTime = this.detailData.validTime[0];
        this.detailData.endTime = this.detailData.validTime[1];
         delete this.detailData.validTime;
          if (this.enterFlag === "add") {
            // 新增
            // addNotice(this, this.detailData).then((res) => {
            this.$http.addExchangeNotice(this, this.detailData).then(res => {   
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "新增成功",
                });
                this.clickLeave();
              }
              this.loadingStatus = false;
            });
          } else {
            // 编辑
            this.detailData.id = Number(this.id);
            // editNotice(this, this.detailData).then((res) => {
            this.$http.editExchangeNotice(this, this.detailData).then(res => {  
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "编辑成功",
                });
                this.clickLeave();
              }
              this.loadingStatus = false;
            });
          }
        }
      });
    },
    // 选择分销商
    disSubmit(msg) {
      if (msg.length > 1) {
        this.$message({
          type: "warning",
          message: "只能选择一个分销商！",
        });
      } else {
        this.distriData = msg;
        this.detailData.distributorId = this.distriData[0].distributorId;
        this.distributorShow = false;
      }
    },
    disCancel() {
      this.$refs.selectDistributor.handleCancel();
    },
    disClose() {
      this.distributorShow = false;
    },
    handleDeleteDistributor(index) {
      this.distriData.splice(index, 1);
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-wrap {
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
  }
  .content {
    padding-top: 30px;
    min-width: 1200px;
  }

  .footbtn {
    padding-bottom: 30px;
    text-align: center;
    .box-btn + .box-btn {
      margin-left: 10px;
    }
  }
}
</style>