<!--
 * @Author: yaowei
 * @Date: 2018-06-10 13:38:05
 * @LastEditors: yaowei
 * @LastEditTime: 2018-07-06 11:16:36
-->

<template>
  <div class="customer-list">
    <header>
      <h4 class="header_h4">柔性用户列表</h4>
    </header>
    <div class="customer-header">
      <el-select
        v-model="sex"
        placeholder="性别"
        clearable
        size="mini"
        style="width: 100px"
        @change="handleSearch()"
      >
        <el-option
          v-for="item in sexList"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="platform"
        placeholder="注册渠道"
        clearable
        size="mini"
        style="width: 120px; margin-left: 5px"
        @change="handleSearch()"
      >
        <el-option
          v-for="item in channelList"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <el-select
        v-model="pageInfo.customerType"
        placeholder="用户类型"
        clearable
        size="mini"
        style="width: 120px; margin-left: 5px"
        @change="handleSearch()"
      >
        <el-option label="有效用户" :value="1"> </el-option>
        <el-option label="游客" :value="2"> </el-option>
      </el-select>
      <div class="customer-block">
        <el-select
          size="mini"
          v-model="pageInfo.contentType"
          style="width: 140px"
          clearable
        >
          <el-option label="客户手机号" :value="1"> </el-option>
          <el-option label="客户名称" :value="2"> </el-option>
          <el-option label="客户昵称" :value="3"> </el-option>
        </el-select>
        <el-input
          v-model.trim="pageInfo.content"
          placeholder="请输入关键字搜索"
          size="mini"
          class="box-input"
          @keyup.enter.native="handleSearch()"
        ></el-input>
        <el-button
          class="mini-search-btn box-btn"
          @click.prevent="handleSearch()"
          >搜索</el-button
        >
      </div>
    </div>

    <el-row>
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        ref="multipleTable"
        class="tableCenter"
        v-loading="loading"
        :height="tableHeight"
      >
        <el-table-column
          label="用户编码"
          align="center"
          prop="no"
          :min-width="120"
        ></el-table-column>
        <el-table-column label="用户头像" align="center" :min-width="100">
          <template slot-scope="scope">
            <el-avatar :size="60" :src="scope.row.headPortrait">
              <img src="../../../../assets/images/add_img_default.png" />
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名"
          align="center"
          prop="nikeName"
          :min-width="120"
        ></el-table-column>
        <el-table-column
          label="手机号"
          align="center"
          prop="phone"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column label="性别" align="center" :min-width="60">
          <template slot-scope="scope">
            <span v-if="scope.row.sex == 2">女</span>
            <span v-if="scope.row.sex == 1">男</span>
          </template>
        </el-table-column>
        <el-table-column
          label="生日"
          align="center"
          prop="birthday"
          :formatter="formatTime"
          :min-width="120"
        >
        </el-table-column>
        <el-table-column
          label="注册渠道"
          align="center"
          prop="platformName"
          :min-width="100"
        >
        </el-table-column>
        <el-table-column
          label="注册时间"
          align="center"
          prop="createTime"
          :formatter="formatTime"
          :min-width="150"
        >
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          prop="status"
          :min-width="80"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.status === 2">已冻结</span>
            <span v-else>未冻结</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" :min-width="100">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status == 1"
              @click="handleStatus(scope.row)"
              class="mini-freeze-btn"
            >
              冻结
            </el-button>
            <el-button
              v-if="scope.row.status == 2"
              @click="handleStatus(scope.row)"
              class="mini-search-btn"
            >
              解冻
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page
        :total="total"
        :page="pageInfo.page"
        @sizeChange="sizeChange"
        @currentChange="currentchange"
      ></page>
    </el-row>
  </div>
</template>

<script type="text/javascript">
import { timeFormat } from "@/utils/timeFormat";

export default {
  name: "customerlist",
  data() {
    return {
      sex: "",
      platform: "",
      pageInfo: {
        page: 1,
        size: 10,
        sex: "",
        platform: "",
        content: "",
        contentType: 2,
        customerType: 1,
      },
      sexList: [
        {
          id: 2,
          value: "女",
        },
        {
          id: 1,
          value: "男",
        },
      ],
      channelList: [
        {
          id: 1,
          value: "微信",
        },
        {
          id: 2,
          value: "支付宝",
        },
        {
          id: 6,
          value: "定制商城",
        },
      ],
      loading: false,
      tableHeight: 600,
      tableData: [],
      total: 0,
    };
  },
  components: {
    page: (resolve) => require(["@/components/pagination"], resolve),
  },
  created() {
    this.pageInfo.platform = "";
    this.initData();
  },
  methods: {
    // 初始化数据
    initData() {
      this.$http.getCutomerList(this, this.pageInfo).then((res) => {
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    // 搜索
    handleSearch() {
      this.pageInfo.sex = this.sex;
      this.pageInfo.platform = this.platform;
      this.initData();
    },
    // 冻结/解冻
    handleStatus(row) {
      let msg = "";
      if (row.status) {
        msg = "此操作将解冻该用户, 是否继续?";
      } else {
        msg = "此操作将冻结该用户, 是否继续?";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.loading = true;

          this.$http
            .updataCustomerStatus(this, {
              id: row.id,
              status: row.status ? 0 : 1,
            })
            .then((res) => {
              if (res.success) {
                this.$message({
                  message: row.status ? "解冻成功" : "冻结成功",
                  type: "success",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.pageInfo.sex = this.sex;
                this.pageInfo.platform = this.platform;

                this.initData();
                this.loading = false;
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
    // 分页
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    currentchange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 表格时间格式化
    formatTime(row, col, val) {
      return timeFormat(val);
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" >
.customer-list {
  background-color: white;
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
  .header {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #ccc;
    button {
      margin-left: 10px;
    }
  }
  .customer-header {
    width: 100%;
    padding: 10px;
    overflow: hidden;
    .customer-block {
      float: right;
      .box-input {
        width: 215px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
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

.el-avatar {
  img {
    width: 100%;
    height: 100%;
  }
}
</style>
<style lang="scss" scoped>
.customer-block {
  display: flex;
  font-size: 0;
  /deep/ .el-select {
    .el-input__inner {
      border-right: 0;
      border-top-right-radius: 0;
      border-bottom-right-radius: 0;
    }
  }
  /deep/ .box-input {
    margin-right: 5px;
    .el-input__inner {
      border-top-left-radius: 0;
      border-bottom-left-radius: 0;
    }
  }
}
</style>
