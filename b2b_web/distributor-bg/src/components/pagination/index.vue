<template>
  <div class="block clearfix" v-if="total>0">
    <el-pagination background :current-page.sync="currentPage" :page-sizes="[10, 20, 30, 40, 50]" :page-size="pageSize" @size-change="sizeChange" @current-change="currentChange" layout=" prev, pager, next , total , sizes , jumper" :total="total" class="page">
      <!-- :current-page.sync="currentPage"   // 此处应注释，如果修改搜索分页bug -->
    </el-pagination>
  </div>
</template>
<script type="text/javascript">
export default {
  name: 'pagination',
  props: {
    total: {
      // type:Number,
      required: true
    },
    page:{
      type:Number,
      default:1
    }
    // currentPage:{
    //     type:'Number',
    //     required:true
    // },
    // pageSize:{
    //     type:'Number',
    //     required:true
    // }

  },
  data() {
    return {
      currentPage: 1, // 此处应注释，如果修改搜索分页bug
      pageSize: 10,
    }
  },
  methods: {
    sizeChange(val) {
      this.$emit('sizeChange', val)
    },
    currentChange(val) {
      this.$emit('currentChange', val)
    },
  },
  watch: {
    brandId: {
      handler() {
          this.currentPage = this.p;
      },
      deep: true
    },
    page:{
      handler() {
        this.currentPage = this.page
      },
      deep: true
    }
  }
}

</script>
<style lang="scss" scope>
.block {
  background-color:white;
  .page{
  float: right;
  margin: 20px 20px 20px 0;
  }
}

</style>
