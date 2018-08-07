<!--
 * @Author: yaowei
 * @Date: 2018-05-25 15:19:59
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 18:07:01
-->
<template>
  <div class="detail-wrap">
    <header>
      <h4 class="header_h4">创建主题项目</h4>
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
            <el-form-item label="主题项目名称:" prop="name">
              <el-input
                style="width: 300px"
                size="small"
                placeholder="不超过32个字"
                v-model="detailData.name"
              ></el-input>
            </el-form-item>
            <el-form-item label="指定分销商:" prop="distriData">
              <el-button
                class="mini-search-btn add-goods-btn"
                @click="distributorShow = true"
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
                  label="分销商用户名"
                  align="center"
                  prop="distributorName"
                ></el-table-column>
                <el-table-column
                  label="公司名"
                  align="center"
                  prop="companyName"
                ></el-table-column>
                <el-table-column label="操作" align="center">
                  <template slot-scope="scope">
                    <el-button
                      class="mini-delete-btn"
                      @click="handleDeleteDistributor(scope.$index)"
                      >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
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

    <!-- 添加分销商 -->
    <el-dialog
      :modal-append-to-body="false"
      :visible="distributorShow"
      :before-close="disCancel"
      width="80%"
    >
      <select-distributor
        :distributorData="distriData"
        ref="selectDistributor"
        @disClose="disClose"
        @disSubmit="disSubmit"
      >
      </select-distributor>
    </el-dialog>
  </div>
</template>

<script>
import { addTheme } from "@/views/storeLayout/rxData";
import selectDistributor from "@/views/storeLayout/rxComponents/selectDistributor";

export default {
  name: "addTheme",
  components: { selectDistributor },
  data() {
    // 主题项目名称
    var validateName = (rule, value, callback) => {
      let cnReg = /([\u4e00-\u9fa5]|[\u3000-\u303F]|[\uFF00-\uFF60])/g;
      let mat = value.match(cnReg);
      let length;
      if (mat) {
        length = mat.length + (value.length - mat.length) * 0.5;
      } else {
        length = value.length * 0.5;
      }

      if (length <= 0) {
        callback(new Error("请设置主题项目名称"));
      } else {
        if (length >= 32) {
          callback(new Error("主题项目名称不能超过32个字"));
        } else {
          callback();
        }
      }
    };

    // 添加分销商
    var validateDistriData = (rule, value, callback) => {
      if (this.distriData && this.distriData.length <= 0) {
        callback(new Error("请选择分销商"));
      } else {
        callback();
      }
    };

    return {
      detailData: {
        name: "",
        distributorId: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: validateName,
            trigger: "blur",
          },
        ],
        distriData: [
          {
            required: true,
            validator: validateDistriData,
            trigger: "change",
          },
        ],
      },
      distributorShow: false,
      distriData: [],
      loadingStatus: false,
    };
  },
  mounted() {},
  methods: {
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
          // 新增
          // addTheme(this, this.detailData).then((res) => {
            this.detailData.openFlag = 1  // 状态 开启
          this.$http.addPictureTheme(this, this.detailData).then((res) => {     
            if (res.success) {
              this.$message({
                type: "success",
                message: "新增成功",
              });
              this.clickLeave();
            }
            this.loadingStatus = false;
          });
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
        this.detailData.distributorName = this.distriData[0].distributorName;
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