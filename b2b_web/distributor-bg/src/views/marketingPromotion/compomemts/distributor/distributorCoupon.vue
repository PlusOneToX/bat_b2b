<!--
 * @Author: litian
 * @Date: 2018-02-26 10:27:15
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-11 16:06:00
 * @Description: file content
 * @FilePath: \B2B_WEB_BACKGROUND\src\views\marketingPromotion\compomemts\distributor\distributorCoupon.vue
-->
<template>
  <div>
    <el-form-item label="适用范围：" style="margin-bottom: 0px;">
      <el-radio-group v-model="formData.distributorScope">
        <div class="dis-item1"> 
          <el-radio :label="1" :disabled="disabled">全部分销商</el-radio>
          <el-radio :label="3" :disabled="disabled">指定分销商</el-radio>
        </div>
      </el-radio-group>
    </el-form-item>
    <el-form-item style="margin-bottom: 0px;" >
        <div v-if="formData.distributorScope === 3">
          <el-button class="mini-search-btn" style="margin-bottom: 5px;" icon="el-icon-plus" @click="distributorShow=true" v-if="!disabled"> 添加分销商 </el-button>
          <el-table class="goods-table" :data="formData.distributorData" border header-row-class-name="header-row" max-height="300">
            <el-table-column align="center" label="分销商用户名" prop="name"></el-table-column>
            <el-table-column align="center" label="公司名" show-overflow-tooltip prop="companyName"></el-table-column>
            <el-table-column align="center" label="操作" width="100">
              <template slot-scope="scope" v-if="!disabled">
                <el-button style="margin-top:0px;margin-bottom:0px;"  class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index)"> 删除 </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
    </el-form-item>
    <el-dialog  :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%">
      <select-distributor :distributorData="formData.distributorData"  ref="selectDistributor" @cancel="cancel" @submit="disSubmit"> </select-distributor>
    </el-dialog>
  </div>
</template>
<script>
import selectDistributor from '@/views/goods/components/selectDistributorAll'
export default {
  props: ['distributorType', 'distributors', 'disabled'],
  data() {
    return {
      formData: {
        distributorScope: 1,
        distributorData: []
      },
      pageInfo: {
        page: 1,
        count: 10000
      },
      distributorShow: false,
      multipleSelection: [],
      isChange: false
    }
  },
  components: { selectDistributor },
  created() {
    this.formData.distributorData = []
    if(this.distributors !== undefined && this.distributors !== null && this.distributors.length>0){
      this.formData.distributorData = this.formData.distributorData.concat(this.distributors)
    }
    this.formData.distributorScope = this.distributorType;
  },
  methods: {
    handleDeleteDistributor(index) {
      this.formData.distributorData.splice(index, 1)
      this.$emit('change', this.formData)
    },
    add() {
      this.$emit('add')
    },

    disCancel() {
      this.$refs.selectDistributor.handleCancel()
    },
    cancel() {
      this.distributorShow = false
    },
    disSubmit(msg) {
      this.formData.distributorData = msg
      this.distributorShow = false
      this.$emit('change', this.formData)
    }
  },
  watch: {
    distributorType(val) {
      this.formData.distributorScope = val
    },
    distributors(val) {
      this.formData.distributorData = []
      if(this.distributors !== undefined && this.distributors !== null && this.distributors.length>0){
        this.formData.distributorData = this.formData.distributorData.concat(this.distributors)
      }
    },
    'formData.distributorScope': {
      handler(val) {
        this.formData.distributorScope = val
        this.$emit('change', this.formData)
      },
      deep: true
    }
  }
}

</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox:first-child {
    margin-left: 30px;
}

</style>
