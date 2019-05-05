<!--
 * @Author: yaowei
 * @Date: 2019-10-19 17:45:13
 * @LastEditors: yaowei
 * @LastEditTime: 2019-10-22 15:49:08
-->
<template>
  <div class="setting-wrap">
    <h6 class="detail-title">
      请配置以下信息，当均显示已配置时，租户才可使用B2B（定制信息除外）
    </h6>

    <div class="setting-list">
      <el-timeline>
        <el-timeline-item
          v-for="(setting, index) in settingList"
          :key="index"
          :type="setting.hasSet ? 'primary' : ''"
          size="large"
          :timestamp="setting.description"
        >
          <span>{{ setting.title }}</span>
          <el-link
            class="setting-link"
            :type="setting.hasSet ? 'primary' : 'info'"
            @click="handleSetting(setting)"
          >
            {{ setting.hasSet ? "已配置" : "去填写" }}
            <i class="el-icon-arrow-right" />
          </el-link>
        </el-timeline-item>
      </el-timeline>
    </div>
    <div class="clearfix footbtn">
      <el-button @click="handleBack">返回</el-button>
    </div>
  </div>
</template>

<script>
import { tenantSummary } from "@/api/tenant";

export default {
  name: "TenantSetting",
  data() {
    return {
      tenantId: this.$route.query.id || "",
      tenantNo: this.$route.query.tenantNo || "",
      settingList: [
        {
          title: "基础信息配置",
          description: "请填写基础配置信息",
          name: "CommonSetting",
          flag: "commonFlag",
          hasSet: false,
        },
        {
          title: "数据库配置",
          description: "请填写数据库配置信息",
          name: "DBSetting",
          flag: "dbFlag",
          hasSet: false,
        },
        {
          title: "定制信息配置",
          description: "请填写定制配置信息",
          name: "DiySetting",
          flag: "diyFlag",
          hasSet: false,
        },
        {
          title: "ERP信息配置",
          description: "请填写ERP配置信息",
          name: "ErpSetting",
          flag: "erpFlag",
          hasSet: false,
        },
        {
          title: "文件存储配置",
          description: "请填写文件存储OSS配置信息",
          name: "OssSetting",
          flag: "ossFlag",
          hasSet: false,
        },
        {
          title: "短信配置",
          description: "请填写短信配置及模板信息",
          name: "SmsSetting",
          flag: "smsFlag",
          hasSet: false,
        },
      ],
    };
  },
  created() {
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      tenantSummary({
        id: this.tenantId,
      }).then((res) => {
        if (res.success) {
          let data = res.data;

          // 将对象转数组
          let arrData = [];
          for (let i in data) {
            let o = {};
            o[i] = data[i];
            arrData.push(o);
          }

          // 将数组的每一项的key跟value作为值赋值给新数组
          let arr = [];
          arrData.forEach((item) => {
            for (let k in item) {
              arr.push({ flag: k, hasSet: item[k] });
            }
          });

          this.settingList.forEach((setting) => {
            arr.forEach((item) => {
              if (setting.flag === item.flag) {
                this.$set(setting, "hasSet", item.hasSet);
              }
            });
          });
        }
      });
    },
    // 已配置/去填写
    handleSetting(setting) {
      this.$router.push({
        name: setting.name,
        query: {
          tenantId: this.tenantId,
          tenantNo: this.tenantNo,
          checkMsg: setting.hasSet ? 2 : 1,
        },
      });
    },
    // 返回
    handleBack() {
      this.$router.push({
        name: "TenantList",
        query: {
          id: this.tenantId,
          tenantNo: this.tenantNo,
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/styles/variables.scss";

.setting-wrap {
  padding: 50px 100px;
  min-width: 750px;
}

.setting-list {
  padding-top: 40px;
  .el-timeline {
    padding: 0;

    ::v-deep .el-timeline-item {
      position: relative;
      padding-bottom: 50px;
    }

    ::v-deep .el-timeline-item::after {
      content: "";
      position: absolute;
      right: 0;
      bottom: 35px;
      left: 28px;
      height: 1px;
      background: #eeeeee;
    }

    ::v-deep .el-timeline-item__content,
    ::v-deep .el-timeline-item__timestamp {
      font-size: 16px;
    }

    ::v-deep .el-timeline-item__content {
      position: relative;
      top: -10px;
    }

    ::v-deep .el-timeline-item__timestamp {
      margin-top: 0;
      color: #999999;
    }
  }

  .setting-link {
    position: absolute;
    top: 10px;
    right: 0;
    font-size: 14px;
  }
}
</style>