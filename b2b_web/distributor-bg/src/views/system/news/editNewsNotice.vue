<template>
  <div class="manage-add">
    <div class="pay-list">
      <header>
        <h4 v-if="!isEdit">添加消息</h4>
        <h4 v-else>查看消息</h4>
        <el-button
          class="mini-back-btn btn-home"
          icon="el-icon-d-arrow-left"
          @click="handleCancle"
        >
          返回消息通知列表
        </el-button>
      </header>
    </div>
    <div class="addfunction">
      <el-form
        label-width="150px"
        label-position="right"
        ref="formName"
        :model="tableData"
        :rules="rules"
      >
        <el-form-item label="消息渠道:" prop="channel">
          <el-radio-group v-model="tableData.channel" :disabled="isEdit">
            <el-radio :label="1">B2B</el-radio>
            <el-radio :label="2">兑换商城</el-radio>
            <el-radio :label="3">定制商城</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="消息标题:" prop="title">
          <el-input
            v-model="tableData.title"
            placeholder=""
            size="small"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="消息内容:" prop="content">
          <el-input
            v-model="tableData.content"
            placeholder=""
            type="textarea"
            :rows="2"
            size="small"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="小程序跳转链接" prop="miniLink">
          <el-input
            v-model="tableData.miniLink"
            placeholder=""
            size="small"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="PC端跳转链接" prop="pcLink">
          <el-input
            v-model="tableData.pcLink"
            placeholder=""
            size="small"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="是否推送:" prop="pushSwitch">
          <el-radio-group v-model="tableData.pushSwitch" :disabled="isEdit">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
          <div class="editNewsNotice-tip">
            如开启推送，则会通知小程序或公众号进行消息推送，B2B暂不支持（没有合适场景）
          </div>
        </el-form-item>

        <goodsDistributor
          ref="distributor"
          :isDefault="false"
          :label="'推送范围'"
          :type="tableData.distributorScope"
          :gIds="tableData.scalePriceIds"
          :dIds="tableData.distributorIds"
          :gdIds="tableData.distributorGroupIds"
          :departmentIds="tableData.departmentIds"
          :adminIds="tableData.adminIds"
          @change="getChange"
        ></goodsDistributor>
      </el-form>

      <el-button
        style="margin-top: 20px; margin-left: 47%"
        class="mini-search-btn"
        @click="handleSubmit('tableData')"
        v-if="!isEdit"
      >
        保存
      </el-button>
      <el-button
        :style="{ 'margin-left': isEdit ? '47%' : '' }"
        size="mini"
        @click="handleCancle"
        style="margin-top: 20px"
      >
        返回
      </el-button>
    </div>
  </div>
</template>

<script>
import goodsDistributor from "@/views/goods/components/goodsDistributor";

export default {
  name: "expressReadct",
  props: {
    pageState: String,
  },
  components: { goodsDistributor },
  data() {
    return {
      loading: false,
      isEdit: false,
      tableData: {
        channel: 1, //消息渠道 1.B2b 2.兑换商城 3.定制商城
        title: "", //消息标题
        content: "", //消息内容
        miniLink: "", //小程序跳转链接
        pcLink: "", //PC端跳转链接
        pushSwitch: 1, //推送开关 0关 1开
        distributorScope: 1, //分销商推送范围1全部 2分销商等级 3.指定分销商 4.指定销售部门 5.指定业务员 6.指定分销商分组
        scalePriceIds: [], // 指定分销商等级
        departmentIds: [], // 指定销售部门
        adminIds: [], // 指定业务员
        distributorGroupIds: [], // 指定分销商分组
        distributorIds: [], // 指定分销商
      },
      rules: {
        channel: [
          {
            required: true,
            message: "请选择标题",
            trigger: "change",
          },
        ],
        title: [
          {
            required: true,
            message: "请输入消息标题",
            trigger: "change",
          },
        ],
        content: [
          {
            required: true,
            message: "请输入消息内容",
            trigger: "change",
          },
        ],
        miniLink: [
          {
            required: true,
            message: "请输入小程序跳转链接",
            trigger: "change",
          },
        ],
        pcLink: [
          {
            required: true,
            message: "请输入PC端跳转链接",
            trigger: "change",
          },
        ],
        pushSwitch: [
          {
            required: true,
            message: "请选择是否推送",
            trigger: "change",
          },
        ],
        distributorScope: [
          {
            required: true,
            message: "请选择推送范围",
            trigger: "change",
          },
        ],
      },
      saveFlag: true, // 保存flag，防重复点击
    };
  },
  activated() {
    this.tableData = {
      channel: 1, //消息渠道 1.B2b 2.兑换商城 3.定制商城
      title: "", //消息标题
      content: "", //消息内容
      distributorScope: 1, //分销商推送范围1全部 2分销商等级 3.指定销售部门 4.指定业务员 5.指定分销商分组 6.指定分销商
      miniLink: "", //小程序跳转链接
      pcLink: "", //PC端跳转链接
      pushSwitch: 1, //推送开关 0关 1开
    };
    this.distribution();
  },
  created() {},
  mounted() {},
  methods: {
    // 详情数据distribution
    distribution() {
      let query = this.$route.query;
      console.log(query.id);
      this.isEdit = false;
      if (query.id != undefined) {
        this.isEdit = true;
        this.$http.msgcenterDetail(this, { id: query.id }).then((res) => {
          console.log("消息详情：", res.data);
          if (res.success) {
            this.tableData = res.data;
          }
        });
      }
    },
    //保存
    handleSubmit() {
      if (this.saveFlag) {
        this.saveFlag = false;
        this.$refs.formName.validate((valid) => {
          if (valid) {
            this.tableData.distributorScope = this.$refs.distributor.formData.distributorType; //..可视范围
            this.tableData.scalePriceIds = this.$refs.distributor.formData.gradeIds; //..指定分销商等级
            this.tableData.departmentIds = this.$refs.distributor.formData.DepartmentIds // 指定部门
            this.tableData.adminIds = this.$refs.distributor.formData.adminIds // 指定业务员
            let distributorIds = this.$refs.distributor.formData.distributorIds; //..指定分销商用户ID
            this.tableData.distributorIds = [];
            distributorIds.forEach((item) => {
              this.tableData.distributorIds.push(item.distributorId);
            });
            this.tableData.distributorGroupIds = this.$refs.distributor.formData.distributorGroupIds; //..指定分销商分组

            this.$http.addMsgcenter(this, this.tableData).then((res) => {
              if (res.success) {
                this.$message("添加成功");
                this.handleCancle();
              }
              this.saveFlag = true;
            });
          } else {
            this.saveFlag = true;
          }
        });
      }
    },

    // 返回上个页面
    handleCancle() {
      this.$router.push({ name: "newsNotice" });
    },

    // 推送范围
    getChange(val) {
      this.tableData.distributorScope = val.distributorType; //..可视范围
      this.tableData.scalePriceIds = val.gradeIds; //..指定分销商等级
      this.tableData.departmentIds = val.DepartmentIds; // 指定部门
      this.tableData.adminIds = val.adminIds; // 指定业务员
      this.tableData.distributorIds = val.distributorIds; //..指定分销商用户ID
      this.tableData.distributorGroupIds = val.distributorGroupIds; //..指定分销商分组用户ID
    },
  },
  watch: {},
};
</script>

<style rel="stylesheet/scss" lang="scss">
.editNewsNotice-tip {
  font-size: 12px;
  color: #a19e9e;
  margin-top: -10px;
}
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.main {
  background-color: #fff;
}

.manage-add {
  background-color: #fff;
  padding-bottom: 30px;
  .pay-list {
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
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
  .addfunction {
    border-top: 0px;
    background-color: #fff;
    padding: 50px;
    width: 80%;
    .span-header {
      margin-right: 20px;
    }
    .span-heavy {
      width: 140px;
      margin: 0 5px 0 10px;
    }
    .cost-item {
      background-color: $bg;
      border-radius: 10px;
      padding: 20px 20px;
    }
    .cost {
      background-color: $bg;
      border-radius: 10px;
      padding: 20px 20px;
    }

    .use-formula {
      background-color: $lakeBlue;
      color: white;
      border-radius: 5px;
    }
    .course {
      margin-bottom: 20px;
    }
    .designated-area {
      border-radius: 10px 10px 10px 10px;
      background-color: $bg !important;
      padding: 20px;
    }
    .formula {
      width: 290px;
      margin: 0 10px;
    }
  }
}
</style>
