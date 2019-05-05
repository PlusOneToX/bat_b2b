<!--
 * @Author: yaowei
 * @Date: 2019-10-18 09:00:41
 * @LastEditors: yaowei
 * @LastEditTime: 2019-10-19 16:41:16
-->
<template>
  <div class="block clearfix" v-if="total > 0">
    <el-pagination
      background
      :current-page.sync="currentPage"
      :page-sizes="[10, 20, 30, 40, 50]"
      :page-size="pageSize"
      @size-change="sizeChange"
      @current-change="currentChange"
      layout=" prev, pager, next, total, sizes, jumper"
      :total="total"
      class="page"
    >
    </el-pagination>
  </div>
</template>

<script type="text/javascript">
export default {
  name: "Pagination",
  props: {
    total: {
      required: true,
    },
    page: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 10,
    };
  },
  methods: {
    sizeChange(val) {
      this.$emit("sizeChange", val);
    },
    currentChange(val) {
      this.$emit("currentChange", val);
    },
  },
  watch: {
    brandId: {
      handler() {
        this.currentPage = this.p;
      },
      deep: true,
    },
    page: {
      handler() {
        this.currentPage = this.page;
      },
      deep: true,
    },
  },
};
</script>

<style lang="scss" scope>
.block {
  background-color: white;
  .page {
    float: right;
    margin: 20px 0 20px 0;
  }
}
</style>
