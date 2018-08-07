<template>
  <div class="ulog-list">
    <div>
      <header class="newsNotice-top">
        <h4>消息通知列表</h4>
        <el-button
          class="mini-add-btn manage-btn-home"
          icon="el-icon-plus"
          @click="addMessage"
          >添加消息</el-button
        >
      </header>
    </div>
    <div class="log-header">
      <div class="log-left">
        <el-select
          size="mini"
          v-model="pageInfo.channel"
          placeholder="消息渠道"
          style="width: 100px"
          clearable
        >
          <el-option label="B2B" value="1"></el-option>
          <el-option label="定制商城" value="2"></el-option>
          <el-option label="兑换商城" value="3"></el-option>
        </el-select>
        <el-date-picker
          size="mini"
          v-model="time"
          style="width: 330px"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="至"
          start-placeholder="下单开始日期"
          end-placeholder="下单结束日期"
        ></el-date-picker>
      </div>
      <div class="log-block">
        <el-select
          size="mini"
          v-model="contentType"
          placeholder="类型"
          style="width: 120px"
          clearable
        >
          <el-option label="消息编号" value="1"></el-option>
          <el-option label="消息标题" value="2"></el-option>
          <el-option label="消息内容" value="3"></el-option>
        </el-select>
        <el-input
          class="box-input"
          size="mini"
          @change="contentChange"
          @keyup.enter.native="filter()"
          placeholder="请输入搜索关键字"
          v-model.trim="content"
          clearable
        ></el-input>
        <button
          class="mini-search-btn box-btn"
          size="mini"
          @click.prevent="filter()"
        >
          搜索
        </button>
      </div>
    </div>
    <div class="function">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        border
        class="tr-header"
        max-height="550"
        v-loading="loading"
      >
        <!-- <el-table-column align="center" label="ID" :width="80" prop="operateId" ></el-table-column> -->
        <el-table-column
          align="center"
          label="编号"
          :width="120"
          prop="id"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="消息渠道"
          :width="140"
          prop="channel"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="scope.row.channel == 1">B2B</span>
            <span v-if="scope.row.channel == 2">兑换商城</span>
            <span v-if="scope.row.channel == 3">定制商城</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="消息标题"
          prop="title"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="消息内容"
          prop="content"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="发布时间"
          prop="createTime"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="是否推送"
          prop="pushSwitch"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="scope.row.pushSwitch == 1">是</span>
            <span v-if="scope.row.pushSwitch == 0">否</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" :width="200">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleSee(scope.row)"
              >查看</el-button
            >
            <el-button
              class="mini-delete-btn"
              @click="deleveFun(scope.row)"
              v-if="scope.row.pushSwitch == 0"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <page
        :page="pageInfo.page"
        :total="total"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </div>
  </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { parseTime } from "@/utils/index";
export default {
  components: { page },
  data() {
    return {
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        channel: undefined, //消息渠道 1.B2b 2.兑换商城 3.定制商城
        id: undefined,
        title: undefined, //标题
        content: undefined, //内容
        startTime: undefined,
        endTime: undefined,
      },
      time: "",
      contentType: "", //类型 1：消息编号 2：消息标题  3：消息内容
      content: "",
      total: 0,
      tableData: [],
      formData: {},
      dialogShow: false,
    };
  },
  mounted() {
    this.dataFot();
  },
  activated() {
    this.dataFot();
  },
  methods: {
    filter() {
      // 搜索操作
      this.pageInfo.page = 1;
      if (this.content == 1) {
        this.pageInfo.id = this.content;
        console.log("this.pageInfo.id:", this.pageInfo.id);
      } else if (this.content == 2) {
        this.pageInfo.title = this.content;
      } else if (this.content == 3) {
        this.pageInfo.content = this.content;
      }

      this.dataFot();
    },
    contentChange(val) {
      if (val === undefined || val === "" || val === null) {
        this.filter();
      }
    },
    // ======== 查看 ========
    handleSee(row) {
      this.$router.push({ name: "editNewsNotice", query: { id: row.id } });
    },
    // 删除
    deleveFun(row) {
      this.$confirm("此操作将删除该消息通知，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
      .then((_) => {
        this.$http.deleteMsgcenter(this, { id: row.id }).then((res) => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "操作成功",
            });
            this.dataFot();
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

    // ======== 数据 ========
    dataFot() {
      // 数据列表
      this.loading = true;
      this.$http.msgcenterList(this, this.pageInfo).then((res) => {
        if (res.success && res.data.list && res.data.list.length > 0) {
          this.tableData = res.data.list;
          this.loading = false;
        } else {
          this.tableData = [];
          this.loading = false;
        }
        this.total = res.data.total;
      });
    },

    sizeChange(size) {
      this.pageInfo.size = size;
      this.dataFot();
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.dataFot();
    },
    closeDialog() {
      // 关闭dialog的
      this.dialogShow = false;
    },

    // 添加消息
    addMessage() {
      this.$router.push({ name: "editNewsNotice" });
    },
  },
  watch: {
    contentType: {
      // 操作结果
      handler(val) {
        console.log(val);
        if (val == 1) {
          this.pageInfo.id = this.content;
          console.log("this.pageInfo.id:", this.pageInfo.id);
        } else if (val == 2) {
          this.pageInfo.title = this.content;
        } else if (val == 3) {
          this.pageInfo.content = this.content;
        }

        this.dataFot();
      },
      deep: true,
    },
    "pageInfo.channel": {
      handler(val) {
        console.log(val);
        if (val == "") {
          this.pageInfo.channel = undefined;
        }
        this.dataFot();
      },
      deep: true,
    },
    content: {
      // 操作结果
      handler(val) {
        console.log(val);

        if (val == "") {
          this.pageInfo.id = undefined;
          this.pageInfo.title = undefined;
          this.pageInfo.content = undefined;
        }

        this.dataFot();
      },
      deep: true,
    },

    time: {
      handler(val) {
        if (val) {
          this.pageInfo.startTime = parseTime(val[0]);
          this.pageInfo.endTime = parseTime(val[1]);
        } else {
          this.pageInfo.startTime = undefined;
          this.pageInfo.endTime = undefined;
        }
        this.pageInfo.page = 1;
        this.dataFot();
      },
      deep: true,
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.manage-btn-home {
  float: right;
  padding: 5px;
  margin-top: 3px;
  margin-left: 0;
  font-size: 12px;
}
.newsNotice-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}
.ulog-list {
  background-color: white;
  height: 100%;
  header {
    color: white;
    padding-right: 10px;
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
  .log-header {
    width: 100%;
    overflow: hidden;
    .log-left {
      display: inline-block;
      margin: 10px 10px;
    }
    .log-block {
      float: right;
      margin: 10px 10px;
      .box-input {
        width: 200px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
  }
}
.tr-header {
  text-align: center;
}
.function {
  padding: 16px 0;
  background-color: white;
  .btn-export {
    background-color: lighten(grey, 40%);
  }
  .search {
    float: right;
    margin-bottom: 10px;
  }
  .box-search {
    width: 140px;
  }
  .btn-search {
    background-color: $lakeBlue;
    color: white;
  }
  .Fheader {
    width: 96%;
    margin: 10px auto;
  }
}
.choose-stock {
  width: 100%;
  padding: 10px;
  padding-left: 20px;
}
.btn-add {
  float: right;
  padding: 5px;
  margin-top: 7px;
  font-size: 12px;
}

.el-table__row {
  text-align: center !important;
}
.btn-home {
  float: right;
  padding: 5px;
  margin-top: 8px;
  margin-left: 0;
  font-size: 12px;
}
.el-dialog__wrapper {
  /deep/.el-dialog__body {
    .el-form-item {
      margin-bottom: 0;
      .el-form-item__content {
        display: inline-block;
        width: 70% !important;
      }
      &.content {
        .el-form-item__content {
          width: 80% !important;
          max-height: 200px;
          overflow-y: scroll;
          -ms-overflow-style: none;
          overflow: -moz-scrollbars-none;
          &::-webkit-scrollbar {
            display: none;
          }
        }
      }
    }
  }
}
</style>