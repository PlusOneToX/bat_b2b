<template>
  <div class="list-wrap">
    <header>
      <h4>产品推广</h4>
      <el-button
        class="mini-add-btn btn-home"
        icon="el-icon-plus"
        @click="handleDetail(1)"
        >添加</el-button
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
          label="产品推广名称"
          align="center"
          prop="extensionGoodsName"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="有效期" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <span
              >{{ scope.row.startTimeVal }} - {{ scope.row.endTimeVal }}</span
            >
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.goodsPromotionStatus === 0">未开始</span>
            <span v-else-if="scope.row.goodsPromotionStatus === 1">进行中</span>
            <span v-else-if="scope.row.goodsPromotionStatus === 2">已结束</span>
            <span v-else-if="scope.row.goodsPromotionStatus === 3">已作废</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.goodsPromotionStatus !== 3"
              class="mini-browse-btn"
              @click="handleDetail(2, scope.row.id)"
            >
              编辑
            </el-button>
            <el-button
              class="mini-delete-btn"
              @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
            <el-button
              v-if="scope.row.goodsPromotionStatus === 1"
              class="mini-freeze-btn"
              @click="handleStatus(scope.row)"
            >
              作废
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
import page from "@/components/pagination";
import { monthDay } from "@/utils/timeFormat.js";

export default {
  name: "productPromotion",
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
      this.$http.proPromotionList(this, this.pageInfo).then((res) => {
        this.loading = false;
        if (res.success) {
          this.tableData = res.data.list;
          this.total = res.data.total;

          this.tableData.forEach((item) => {
            // 格式化时间
            item.startTimeVal = monthDay(item.extensionStartTime);
            item.endTimeVal = monthDay(item.extensionEndTime);
          });
        }
      });
    },
    handleDetail(type, id) {
      if (type === 1) {
        // 添加
        this.$router.push({
          name: "productPromotionDetail",
          query: {
            enterFlag: "add",
          },
        });
      } else {
        // 编辑
        this.$router.push({
          name: "productPromotionDetail",
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
    // 作废
    handleStatus(item) {
      let msg = "此操作将作废该产品推广，是否继续？";

      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http
            .invalidProPromotion(this, {
              id: item.id,
            })
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "操作成功",
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
      this.$confirm("此操作将删除该产品推广，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then((_) => {
          this.$http.deleteProPromotion(this, { id: id }).then((res) => {
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

.list-wrap {
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