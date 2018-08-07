<template>
  <div>
    <el-form-item :label="labelTitle ? labelTitle : '活动范围:'">
      <el-radio-group v-model="formData.distributorType">
        <div class="dis-item1"> 
          <el-radio :label="1" :disabled="disabled">全部分销商</el-radio>
        </div>

        <div class="dis-item2">
          <div>
            <el-radio  :label="2" :disabled="disabled">指定分销商等级</el-radio>
          </div>

          <!-- 指定分销商等级 -->
          <el-col v-if="formData.distributorType === 2">
            <el-form-item>
              <el-checkbox-group v-model="formData.distributorGradeIds" :disabled="disabled">
                <el-checkbox v-for="item in distributorGradesList" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div>

        <!-- 指定事业部 -->
        <!-- <div class="dis-item2">
          <div>
            <el-radio  :label="3" :disabled="disabled">指定事业部</el-radio>
          </div>
          <el-col v-if="formData.distributorType === 6">
            <el-form-item>
              <el-checkbox-group v-model="formData.distributorBusinessIds" :disabled="disabled">
                <el-checkbox v-for="item in businesses" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div> -->

        <div class="dis-item2">
          <div>
            <el-radio  :label="4" :disabled="disabled">指定销售部门</el-radio>
          </div>

          <!-- 指定销售部门 -->
          <el-col v-if="formData.distributorType === 4">
            <el-form-item>
              <el-checkbox-group v-model="formData.distributorDepartmentIds" :disabled="disabled">
                <el-checkbox v-for="item in departments" :label="item.id" :key="item.id">{{item.departmentName}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </div>

        <div class="dis-item2">
          <div>
            <el-radio  :label="5" :disabled="disabled">指定业务员</el-radio>
          </div>
          <!-- 指定业务员-->
          <el-col v-if="formData.distributorType === 5">
            <transition mode="out-in" name="fade">
              <div class="sales" >
                <el-transfer v-model="formData.distributorAdminIds" :data="admins" :titles="['业务员列表','选中列表']" 
                :button-texts="['取消','选中']"></el-transfer>
              </div>
            </transition>
          </el-col>
        </div>

        <div class="dis-item3">
          <el-radio :label="3" :disabled="disabled">指定分销商</el-radio>
          <div class="distributor-content" v-if="formData.distributorType ===3">
            <el-button class="mini-search-btn" icon="el-icon-plus" @click="distributorShow=true" v-if="!disabled"> 添加分销商 </el-button>
            <el-table class="goods-table" :data="formData.distributorData" border header-row-class-name="header-row" style="width: 100%" max-height="300">
              <el-table-column align="center" label="分销商用户名" width="150" prop="name"></el-table-column>
              <el-table-column align="center" label="公司名" show-overflow-tooltip width="300" prop="companyName"></el-table-column>
              <el-table-column align="center" label="操作" width="80" v-if="!disabled">
                <template slot-scope="scope">
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
  props: ['ruleType', 'type', 'gIds', 'dIds', 'disabled','businessIds','departmentIds','adminIds', 'labelTitle'],
  data() {
    return {
      distributorGradesList: [],
      departments:[],
      businesses:[],
      distributors: [],
      admins:[],
      formData: {
        distributorType: 1,
        distributorGradeIds: [],
        distributorBusinessIds:[],
        distributorDepartmentIds:[],
        distributorAdminIds:[],
        distributorIds: [],
        distributorData: []
      },
      distributorShow: false,
      multipleSelection: [],
      isChange: false
    }
  },
  components: { selectDistributor },
  created() {
    this.getGrades()
    // this.getBusinesses()
    this.getDepartments()
    this.getAllSales()
    this.formData.distributorData = []
    this.formData.distributorType = this.type;
    this.formData.distributorGradeIds = this.gIds instanceof Array ? this.gIds : this.gIds == undefined ? [] : this.gIds.splitnum(',')
    this.formData.distributorBusinessIds = this.businessIds instanceof Array ? this.businessIds : this.businessIds == undefined ? [] : this.businessIds.splitnum(',')
    this.formData.distributorDepartmentIds = this.departmentIds instanceof Array ? this.departmentIds : this.departmentIds == undefined ? [] : this.departmentIds.splitnum(',')
    this.formData.distributorAdminIds = this.adminIds instanceof Array ? this.adminIds : this.adminIds == undefined ? [] : this.adminIds.splitnum(',')
    this.formData.distributorIds =  this.dIds instanceof Array ? this.dIds : this.dIds == undefined ? [] : this.dIds.splitnum(',')
    this.distributors = this.$store.getters.distributors
    this.initDistributor()
  },
  methods: {
    getAllSales() {  //..用户列表
      this.$http.salesList(this, {page:1, size:10000, saleFlag: 1}).then(res => {  
        if (res.success) {
           res.data.list.forEach(item => {
            let obj = {
              key: item.id,
              label: item.realName,
              disabled: false,
            }
            this.admins.push(obj)
          })
        }
      })
    },
    // getBusinesses(){ // 事业部
    //   this.$api.get(this, 'admin/u/po/businessUnit/list').then(res => {
    //     if (res.code === 0) {
    //       this.businesses = res.businessUnits
    //     }
    //   })
    // },
    getGrades(){
      this.$http.getGradePoList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => {  		  
        if (res.success) {
          this.distributorGradesList = res.data.list
        }
      })
    },
    getDepartments(){ //销售部门
      this.$http.getDepartmentPoList(this, {page:1, size: 1000, saleType: 1}).then(res => {  
        if (res.success) {
          this.departments = res.data.list
        }
      })
    },

    handleDeleteDistributor(index) {
      this.formData.distributorData.splice(index, 1)
    },
    initDistributor() {
      if (this.formData.distributorIds !== undefined && this.formData.distributorIds !== null && this.formData.distributorIds.length > 0) {
        if (this.distributors === undefined || this.distributors.length === 0) {
          this.$http.getDistributorPoList(this, { page:1, size:10000,freezeStatus: 1,profileStatus: 2}).then(res => {	  
            this.distributors = res.data.list
            this.$store.commit('GET_DISTRIBUTORS', res.data.list)
            this.formData.distributorIds.forEach(item => {
              this.distributors.forEach(val => {
                if (val.id === Number(item.distributorId)) {
                  this.formData.distributorData.push(val)
                }
              })
            })
            this.formData.distributorData = this.setArr(this.formData.distributorData)
          })
        } else {
          this.formData.distributorIds.forEach(item => {
            this.distributors.forEach(val => {
              if (val.id === Number(item.distributorId)) {
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
      this.formData.distributorIds = val instanceof Array ? val : ((val == undefined || val === null)  ? [] : val.splitnum(','))
      this.initDistributor()
    },
    gIds(val) {
      this.formData.distributorGradeIds = val instanceof Array ? val : ((val === undefined || val === null)  ? [] : val.splitnum(','))
    },
    businessIds(val) {
      this.formData.distributorBusinessIds = val instanceof Array ? val : ((val == undefined || val === null) ? [] : val.splitnum(','))
    },
    departmentIds(val) {
      this.formData.distributorDepartmentIds = val instanceof Array ? val : ((val === undefined || val === null) ? [] : val.splitnum(','))
    },
    adminIds(val) {
      this.formData.distributorAdminIds = val instanceof Array ? val : ((val === undefined || val === null) ? [] : val.splitnum(','))
    }
  }
}

</script>
<style lang="scss" scoped>
.el-radio-group .el-checkbox {
    margin-left: 30px;
    margin-right: 0px;
}

.sales {
    margin: 30px;
    .el-button--primary{
      color: #fff;
      background-color: $lakeBlue !important;
      border-color: $lakeBlue !important;
    }
    .el-transfer__button.is-disabled, .el-transfer__button.is-disabled:hover{
      background-color: #CCCCCC !important;
      border-color: #CCCCCC !important;
    }
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

.distributor-content {
    margin-left: 26px;
    margin-top: 10px;
}
.mini-search-btn{
  margin-bottom: 10px;
}

</style>
