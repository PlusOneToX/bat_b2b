<!--
 * @Author: yaowei
 * @Date: 2018-06-11 15:40:41
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-12 09:45:16
-->

<template>
  <div class="sys-add-edit">
    <header>
      <h4 v-if="isEdit">查看系统平台</h4>
      <h4 v-else>添加系统平台</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="cancel"
      >
        返回系统平台列表
      </el-button>
    </header>
    <el-form
      ref="form"
      :model="form"
      label-width="150px"
      class="el-form1"
      :rules="rules"
    >
      <el-form-item label="平台编码：" prop="platformNo">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.platformNo"
        ></el-input>
        <p class="tips">全平台唯一，请谨慎输入</p>
      </el-form-item>
      <el-form-item label="分销商平台名称：" prop="name">
        <el-input
          size="mini"
          style="width: 300px"
          v-model.trim="form.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态：" prop="openFlag">
        <el-radio-group v-model="form.openFlag" size="mini">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <div class="clearfix footbtn">
      <el-button style="margin-left: 25%" size="mini" @click="cancel"
        >返回</el-button
      >
      <el-button class="mini-search-btn" @click="submitForm('form')"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        name: "",
        openFlag: 1,
        platformNo: "",
      },
      rules: {
        platformNo: [
          {
            required: true,
            message: "请输入平台编码",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "分销商平台名称",
            trigger: "blur",
          },
        ],
        openFlag: [
          {
            required: true,
            message: "请选择状态",
            trigger: "blur",
          },
        ],
      },
      isEdit: false,
    };
  },
  created() {
    this.getParams();
  },
  methods: {
    submitForm(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.handleSave();
        } else {
          return false;
        }
      });
    },
    handleSave() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 更新
            this.form.id = this.$route.query.id;
            this.$http.updateSysPlatform(this, this.form).then((res) => {
              if (res.success) {
                this.$message({
                  message: "保存成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.cancel();
              }
              this.loading = false;
            });
          } else {
            // 新增
            this.$http.addSysPlatform(this, this.form).then((res) => {
              if (res.success) {
                this.$message({
                  message: "保存成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.cancel();
              }
              this.loading = false;
            });
          }
        }
      });
    },
    cancel() {
      this.$router.push({ name: "sysPlatformList" });
    },
    getParams() {
      this.isEdit = false;
      if (this.$route.query.id != undefined) {
        this.isEdit = true;
        // 微信公众平台详情
        this.$http
          .getSysPlatformDetail(this, {
            id: this.$route.query.id,
          })
          .then((res) => {
            if (res.success) {
              this.form = res.data;
            }
          });
      } else {
        this.form.name = "";
        this.form.platformNo = "";
        this.form.openFlag = 1;
      }
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.sys-add-edit {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    margin-bottom: 3%;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .btn-home {
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
}
.el-form1 {
  width: 800px !important;
  margin-top: 30px;
  padding-left: 20px;
}
.tips {
  line-height: 18px;
}
.footbtn {
  margin-top: 40px;
  padding-bottom: 20px;
}
.header-row {
  @include table-header-color;
}
</style>

