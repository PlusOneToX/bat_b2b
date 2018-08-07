<template>
  <div>
    <el-form-item label="活动范围:">
      <el-radio-group v-model="formData.distributorType">
        <div class="dis-item1"> 
          <el-radio v-if="!isUse" :label="1" :disabled="disabled">全部分销商</el-radio>
          <el-radio v-if="this.ruleType == 1 && isUse" :label="1" :disabled="disabled">全部分销商</el-radio>
          <el-radio v-if="this.ruleType == 2 && isUse " :label="1" :disabled="disabled">全部国内分销商</el-radio>
          <el-radio v-if="this.ruleType == 3 && isUse" :label="1" :disabled="disabled">全部国外分销商</el-radio>
        </div>

        <div class="dis-item2">
          <div>
            <el-radio v-if="!isUse" :label="2" :disabled="disabled">指定分销商等级</el-radio>
            <el-radio v-if="this.ruleType == 1 && isUse" :label="2" :disabled="disabled">指定分销商等级</el-radio>
            <el-radio v-if="this.ruleType == 2 && isUse " :label="2" :disabled="disabled">指定国内分销商等级</el-radio>
            <el-radio v-if="this.ruleType == 3 && isUse " :label="2" :disabled="disabled">指定国外分销商等级</el-radio>
          </div>

          <!-- 指定分销商等级 -->
          <el-col v-if="formData.distributorType === 2">
            <el-form-item>
              <el-checkbox-group v-model="formData.distributorGradeIds" :disabled="disabled">
                <el-checkbox v-for="item in distributorList" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div>

        <div class="dis-item3">
          <el-radio v-if="!isUse" :label="3" :disabled="disabled">指定分销商</el-radio>
          <el-radio v-if="this.ruleType == 1 && isUse" :label="3" :disabled="disabled">指定分销商</el-radio>
          <el-radio v-if="this.ruleType == 2 && isUse" :label="3" :disabled="disabled">指定国内分销商</el-radio>
          <el-radio v-if="this.ruleType == 3 && isUse" :label="3" :disabled="disabled">指定国外分销商</el-radio>
          <div class="distributor-content" v-if="formData.distributorType ===3">
            <el-button class="mini-search-btn" icon="el-icon-plus" @click="distributorShow=true" v-if="!disabled"> 添加分销商 </el-button>
            <el-table class="goods-table" :data="formData.distributorData" border header-row-class-name="header-row" style="width: 100%" max-height="300">
              <el-table-column align="center" label="分销商用户名" width="150" prop="name"></el-table-column>
              <el-table-column align="center" label="公司名" show-overflow-tooltip width="300" prop="companyName"></el-table-column>
              <el-table-column align="center" label="操作" width="80">
                <template slot-scope="scope" v-if="!disabled">
                  <el-button style="margin-top:0px;margin-bottom:0px;"  class="mini-delete-btn" @click="handleDeleteDistributor(scope.$index)"> 删除 </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-radio-group>
    </el-form-item>
    <el-dialog  :modal-append-to-body="false" :visible="distributorShow" :before-close="disCancel" width="80%">
      <select-distributor :distributorData="formData.distributorData"  ref="selectDistributor" @cancel="cancel" @submit="disSubmit"> </select-distributor>
    </el-dialog>
  </div>
</template>
<script>
import selectDistributor from '@/views/goods/components/selectDistributor'
export default {
  props: ['ruleType', 'type', 'gIds', 'dIds', 'disabled', 'isUse'],
  data() {
    return {
      distributorList: [],
      distributors: [],
      formData: {
        distributorType: 1,
        distributorGradeIds: [],
        distributorIds: [],
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
    this.formData.distributorType = this.type;
    this.formData.distributorGradeIds = this.gIds instanceof Array ? this.gIds : this.gIds == undefined ? [] : this.gIds.splitnum(',')
    this.formData.distributorIds =  this.dIds instanceof Array ? this.dIds : this.dIds == undefined ? [] : this.dIds.splitnum(',')
    this.initDistributor()
    this.$http.getGradePoList(this, { page:1, size:10000, openFlag: 1 }).then(res => {  
      if (res.success) {
        this.distributorList = res.data.list
      }
    })
  },
  methods: {
    handleDeleteDistributor(index) {
      this.formData.distributorData.splice(index, 1)
      // this.$refs.selectDistributor.selectRow()
    },
    initDistributor() {
      if (this.formData.distributorIds !== undefined && this.formData.distributorIds !== null && this.formData.distributorIds.length > 0) {
        if (this.distributors === undefined || this.distributors.length === 0) {
          this.$http.getDistributorPoList(this, { page:1, size:10000,freezeStatus: 1,profileStatus: 2}).then(res => {			  
            this.distributors = res.data.list
            this.$store.commit('GET_DISTRIBUTORS', res.data.list)
            this.formData.distributorIds.forEach(item => {
              this.distributors.forEach(val => {
                if (val.id === Number(item)) {
                  this.formData.distributorData.push(val)
                }
              })
            })
            this.formData.distributorData = this.setArr(this.formData.distributorData)
          })
        } else {
          this.formData.distributorIds.forEach(item => {
            this.distributors.forEach(val => {
              if (val.id === Number(item)) {
                this.formData.distributorData.push(val)
              }
            })
          })
          this.formData.distributorData = this.setArr(this.formData.distributorData)
        }
      }
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
    setDistributor(val) {
      if (val && val.distributorScope) {
        this.formData.distributorType = val.distributorScope
        this.formData.distributorGradeIds = val.distributorGradeIds instanceof Array ? val.distributorGradeIds : val.distributorGradeIds === undefined ? [] : val.distributorGradeIds.splitnum(',')
        this.formData.distributorIds = val.distributorIds instanceof Array ? val.distributorIds : val.distributorIds === undefined ? [] : val.distributorIds.splitnum(',')
        this.formData.distributorData = val.distributorData
        this.initDistributor()
      }
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
    formData: {
      handler() {
        this.$emit('change', this.formData)
      },
      deep: true
    },
    type(val) {
      this.formData.distributorType = val
    },
    dIds(val) {
      this.formData.distributorIds = val instanceof Array ? val : val == undefined ? [] : val.splitnum(',')
      this.initDistributor()
    },
    gIds(val) {
      this.formData.distributorGradeIds = val instanceof Array ? val : val === undefined ? [] : val.splitnum(',')
    }
  }
}

</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox:first-child {
    margin-left: 30px;
}

.dis-item2 {
    margin-top: 16px;
}
.dis-item1 {
    margin-top: 8px;
}
.dis-item3 {
    margin-top: 16px;
}

.distributor-content button {
    margin: 10px 0;
}

</style>
