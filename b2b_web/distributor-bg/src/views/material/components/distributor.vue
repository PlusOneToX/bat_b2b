<template>
  <div>
    <el-form-item label="适用范围" style="margin-bottom: 0px;">
      <el-radio-group v-model="formData.distributorScope">
        <div class="dis-item1"> 
          <el-radio :label="1" :disabled="disabled">全部分销商</el-radio>
          <el-radio :label="2" :disabled="disabled">国内分销商</el-radio>
          <el-radio :label="3" :disabled="disabled">国外分销商</el-radio>
          <el-radio :label="4" :disabled="disabled">指定分销商</el-radio>
        </div>
      </el-radio-group>
    </el-form-item>
    <el-form-item style="margin-bottom: 0px;" >
        <div v-if="formData.distributorScope === 4">
          <el-button class="mini-search-btn" style="margin-bottom: 5px;" icon="el-icon-plus" @click="distributorShow=true" v-if="!disabled"> 添加分销商 </el-button>
          <el-table class="goods-table" :data="formData.distributorData" border header-row-class-name="header-row" style="width: 60%" max-height="300">
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
    },
    setArr(arr) { // 去重
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id) // 不加类型 分不清 1 '1'
        if (!obj[ arr[i].id + type]) {
          temp.push(arr[i])
          obj[ arr[i].id + type ] = true // 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
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
    }
  }
}

</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox:first-child {
    margin-left: 30px;
}

</style>
