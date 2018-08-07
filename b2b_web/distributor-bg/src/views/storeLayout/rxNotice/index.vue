<!--
 * @Author: yaowei
 * @Date: 2018-05-25 14:54:56
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 08:54:46
-->
<template>
  <div class="rx-wrap">
    <header>
      <h4>公告列表</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleNotice(1)"
        >添加公告</el-button
      >
    </header>

    <el-row v-loading="loading">
      <el-table
        :data="tableData"
        header-row-class-name="header-row"
        row-key="id"
        border
        class="tableCenter"
      >
        <el-table-column
          label="序号"
          align="center"
          type="index"
          width="60"
        ></el-table-column>
        <el-table-column
          label="公告内容"
          align="center"
          prop="content"
        ></el-table-column>
        <el-table-column
          label="自动上线日期"
          align="center"
          prop="startTimeVal"
        ></el-table-column>
        <el-table-column
          label="自动下线日期"
          align="center"
          prop="endTimeVal"
        ></el-table-column>
        <el-table-column label="效期" align="center">
          <template slot-scope="scope">
            <span v-if="Number(scope.row.validStatus) === 1">待生效</span>
            <span v-if="Number(scope.row.validStatus) === 2">生效中</span>
            <span v-if="Number(scope.row.validStatus) === 3">已过期</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <span v-if="Number(scope.row.status) === 0">停用</span>
            <span v-if="Number(scope.row.status) === 1">启用</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="160" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-browse-btn"
              @click="handleNotice(3, scope.row.id)"
              >编辑</el-button
            >
            <el-button
              v-if="Number(scope.row.validStatus) !== 3"
              :class="
                Number(scope.row.status) === 1
                  ? 'mini-freeze-btn'
                  : 'mini-browse-btn'
              "
              @click="handleNoticeStatus(scope.row)"
              >{{ Number(scope.row.status) === 1 ? "停用" : "启用" }}</el-button
            >
            <el-button
              class="mini-delete-btn"
              @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
import page from "@/components/pagination";
import {
  getNoticeList,
  editNotice,
  deleteNotice,
} from "@/views/storeLayout/rxData";
import { timeFormat } from "@/utils/timeFormat.js";

export default {
  name: "rxNotice",
  components: { page },
  data() {
    return {
      pageInfo: {
        size: 10,
        page: 1,
      },
      tableData: [],
      total: 0,
      loading: false,
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      this.loading = true;
      // getNoticeList(this, this.pageInfo).then((res) => {
      this.$http.exchangeNoticeList(this, this.pageInfo).then(res => {  
        this.loading = false;
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;

          // 获取当前时间戳
          let now = new Date().getTime();

          this.tableData.forEach((item) => {
            // 判断当前时间与开始/结束时间
            if (now < item.startTime) {
              // 小于开始时间：待生效
              this.$set(item, "validStatus", 1);
            } else if (now >= item.startTime && now <= item.endTime) {
              // 大于等于开始时间，小于等于结束时间：生效中
              this.$set(item, "validStatus", 2);
            } else {
              // 大于结束时间：已过期
              this.$set(item, "validStatus", 3);
            }
            item.startTimeVal = timeFormat(item.startTime);
            item.endTimeVal = timeFormat(item.endTime);
          });
        }
      });
    },
    handleNotice(type, id) {
      if (type === 1) {
        // 创建主题项目
        this.$router.push({
          name: "noticeDetail",
          query: {
            enterFlag: "add",
          },
        });
      } else {
        this.$router.push({
          name: "noticeDetail",
          query: {
            enterFlag: "edit",
            id: id,
          },
        });
      }
    },
    // 列表条数
    sizeChange(size) {
      this.pageInfo.size = size;
      this.pageInfo.page = 1;
      this.initData();
    },
    // 列表页数
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 停用/启用
    handleNoticeStatus(item) {
      let msg = "";
      if (item.status === 1) {
        msg = "此操作将停用该公告，是否继续？";
      } else {
        msg = "此操作将启用该公告，是否继续？";
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          // editNotice(this, {
          this.$http.editExchangeNotice(this, {    
            id: item.id,
            content: item.content,
            startTime: item.startTime,
            endTime: item.endTime,
            status: item.status === 1 ? 0 : 1,
          }).then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: item.status === 1 ? "停用成功" : "启用成功",
              });
              this.initData();
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
    // 删除
    handleDelete(id) {
      this.$confirm("此操作将删除该公告，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          // deleteNotice(this, id).then((res) => {
          this.$http.delExchangeNotice(this, {id: id}).then(res => {    
            if (res.success) {
              this.$message({
                type: "success",
                message: "删除成功",
              });
              this.initData();
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
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
}

.rx-wrap {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
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
}

.function {
  background-color: white;
}
.Fheader {
  margin: 10px;
  display: flex !important;
  justify-content: flex-end;
  align-items: center !important;
  .Fsearch {
    overflow: hidden;
    float: right;
    .box-input {
      width: 300px;
      float: right;
    }
    .box-btn {
      float: right;
      margin-left: 5px;
    }
  }
}
</style>