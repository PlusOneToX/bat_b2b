<template>
  <div class="check-goods">
    <div class="nav-bar">
      <div class="nav-list-header">
        <div v-if="!exaShow">
          <el-button class="mini-search-btn" @click="openDialog" :disabled="exaShow">添加联系人</el-button>
        </div>
      </div>
		</div>
		<el-row style="margin-left: 15px;margin-right: 15px;">
      <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter">
        <!-- <el-table-column align="center" type="selection"></el-table-column> -->
        <!-- <el-table-column align="center" label="ID" prop="id" :min-width="120"></el-table-column> -->
        <el-table-column align="center" label="联系人名称" prop="name" :min-width="120"></el-table-column>
        <el-table-column align="center" label="手机号" prop="phone" :min-width="120" v-if="countryId===37"></el-table-column>
        <el-table-column align="center" label="邮箱" prop="email" :min-width="120" v-else></el-table-column>
        <el-table-column align="center" label="是否主帐号" prop="ownerFlag" :formatter="formatOwnerFlag" :min-width="120"></el-table-column>
        <el-table-column align="center" label="角色" prop="roleName" :min-width="120">
        </el-table-column>
        <el-table-column label="操作" :min-width="100">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="handleEdit(scope.$index, tableData[scope.$index])" :disabled="exaShow">查看</el-button>
            <el-button class="mini-delete-btn" @click="handleDelete(scope.$index, tableData[scope.$index])" :disabled="exaShow">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
		</el-row>

    <!-- 添加联系人弹框 -->
		<el-dialog :title="type===1?'添加联系人':'编辑联系人'" :modal-append-to-body="false" :visible="contactShow" width="50%" :before-close="closeDialog">
		  <!-- <add-role :type="type" :index="index" :roleData="roleData" @cancel="closeDialog" @submit="addContact"></add-role> -->
      <el-form :model="formData" :rules="rules" label-width="100px" ref="formData2">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" size="mini" maxlength="20" clearable/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="formData.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="0">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select size="mini" v-model="formData.roleId" placeholder="角色" style="width: 220px;" clearable>
            <el-option :key="item.id" :label="item.name" :value="item.id" v-for="item in roles"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否主帐号" prop="ownerFlag">
          <el-radio-group v-model="formData.ownerFlag">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phone" v-if="countryId===37">
          <el-input v-model="formData.phone" size="mini" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" v-else>
          <el-input v-model="formData.email" size="mini" maxlength="50" clearable/>
        </el-form-item>
        <el-form-item label="" v-if="type===2">
          <el-button size="mini" type="text" v-if="!amendShow" @click="amend" >修改密码</el-button>
          <el-button size="mini" type="text" v-if="amendShow" @click="CoamendShow">取消修改密码</el-button>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="amendShow">
          <el-input type="password" v-model="formData.password" size="mini" auto-complete="off" maxlength="16" clearable/>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass" v-if="amendShow">
          <el-input type="password" v-model="formData.checkPass" size="mini" auto-complete="off" maxlength="16" clearable/>
        </el-form-item>
      </el-form>
      <el-button class="mini-search-btn check_btn" @click="handleSubmit()">确定</el-button>
      <el-button class="check_back_btn" size="mini" @click="contactShow=false">返回</el-button>
		</el-dialog>
  </div>
</template>

<script>
import page from '@/components/pagination'
import md5 from 'js-md5'
import eventBus from '@/views/order/eventBus'
export default {
  props: ['basicMessage', 'exaShow'],
  data() {
    // 用户名限制
    var checkNameReg = (rule, value, callback) => {
      if (!value) {
        return callback()
      }
      if (value) {
        setTimeout(() => {
          var reg = /^[\u4e00-\u9fa5_a-zA-Z0-9]{2,20}$/ // 中英文、数字、下划线!
          if (!reg.test(value)) {
            callback(new Error('2--20个字符，仅可为中英文、数字结合、下划线、区分大小写!'))
          } else {
            callback()
          }
        }, 500)
      }
    }
    // 验证手机号
    var checkPhoneReg = (rule, value, callback) => {
      if (/^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/.test(value) !== false) {
          callback();
        } else {
          callback(new Error('请输入正确的手机号'));
        }
    }
    // 验证邮箱
    var checkEmailReg = (rule, value, callback) => {
      if (/^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z._%+-]+\.[a-zA-Z]{2,4}$/.test(value) !== false) {
        callback();
      } else {
        callback(new Error('请输入正确的邮箱'));
      }
    }
    // 输入密码
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value) {
        setTimeout(() => {
          var reg = /^.{6,16}$/
          if (!reg.test(value)) {
            callback(new Error('密码为6-16个字符'))
          } else {
            callback()
          }
        }, 500)
      } else if (!value) {
        return callback()
      } else {
        if (this.formData.checkPass !== '') {
          this.$refs.formData.validateField('checkPass')
        }
        callback()
      }
    }
    // 确认密码
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.formData.password) {
        this.passConfir = true
        callback(new Error('两次输入密码不一致!'))
      } else {
        this.passConfir = false
        callback()
      }
    }
    return {
      tableData: [],
      contactShow: false,
      roleData: null,
      index: null,
      type: 0,
      passConfir: false,
      roles: [],
      amendShow: false,
      delArr: [],
      editArr: [],
      formData: {
        name: '',
        sex: 1,
        roleId: '',
        phone: undefined,
        email: undefined,
        password: '',
        checkPass: '',
        ownerFlag: 0
      },
      rules: { // 必填输入提示
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { validator: checkNameReg }
        ],
        sex: [
          { required: true, message: '请输入性别', trigger: 'blur' },
        ],
        roleId: [
          { required: true, message: '请输入角色', trigger: ['blur']}
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { validator: checkPhoneReg, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { validator: checkEmailReg, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: ['blur']},
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '请输入确认密码', trigger: ['blur']},
          { validator: validatePass2, trigger: 'blur' }
        ],
        ownerFlag: [
          { required: true, message: '请确认是否为主帐号', trigger: ['blur']},
        ]
      },
      checkPass: '',
      countryId: ''
    }
  },
  components: { page },
  created () {
    this.initData()
    eventBus.$on('countryId', (v) => {
      this.countryId = v.countryId
    })
  },
  methods: {
    formatOwnerFlag(row, col, val){
      switch(val) {
        case 1:
          return "是";
          break;
        case 0:
          return "否";
          break;
      }
    },
    // 角色列表
    initData () {
      return new Promise((resolve, reject) => {
        this.$http.getDisRoleList(this, {page:1, size:1000}).then(res => {
          if (res.success) {
            this.roles = res.data.list
            resolve(true)
          } else {
            reject()
          }
        })
      })
    },
    // 查看
    handleEdit (index, row) {
      this.contactShow = true  // 弹框显示
      this.type = 2 // 编辑
      this.index = index  // 当前选中行
      this.amendShow = false // 密码隐藏
      this.formData = row
    },
    // 删除
    handleDelete(index, row) {
      this.tableData.splice(index, 1)
      if (row.id) {
        row.operationType = 3
        this.delArr.push(row)
      }
      // if (this.tableData.length > 1) {
      //   this.tableData.splice(index, 1)
      //   if (row.id) {
      //     row.operationType = 3
      //     this.delArr.push(row)
      //   }
      // } else {
      //    this.$message.error('联系人至少有一个')
      //    this.tableData[0].ownerFlag = 1
      // }
    },
    // 提交保存
    handleSubmit () {
      this.$refs['formData2'].validate(valid => {
        if (valid) {
          this.contactShow = false
          // 获取角色名称
          this.roles.forEach(item => {
            if(this.formData.roleId === item.id) {
              this.formData.roleName = item.name
            }
          })
          
           // 判断是否为主帐号
          if (!this.formData.id) {
            // 添加
            if (this.type === 2) {
              this.tableData.splice(this.tableData.length-1, 1)
            }
            this.formData.password = md5(this.formData.password)
            this.tableData.push(this.formData)
            this.index = this.tableData.length - 1
          } else {
            // 修改
            this.editArr.forEach((item, index) => {
              if (item.id === this.formData.id ) {
                this.editArr.splice(index, 1)
              }
            })

            if (this.amendShow) {
              this.formData.password = md5(this.formData.password)
            }
            this.editArr.push(this.formData)
            this.tableData[this.index] = this.formData
          }
          if (this.formData.ownerFlag===1) {
            this.tableData.map((data, idx) => {
              if (this.index === idx) {
                data.ownerFlag = 1
              } else {
                if (data.ownerFlag === 1) {
                  data.ownerFlag = 0
                  this.editArr.push(data)
                } else {
                   data.ownerFlag = 0
                }
              }
            })
          }
          if (this.countryId === 37) {
            this.tableData.forEach(item => {
              delete item.email
            })
          } else if(this.countryId && this.countryId !== 37) {
            this.tableData.forEach(item => {
              delete item.phone
            })
          }
          this.tableData =  JSON.parse(JSON.stringify(this.tableData))
        } else {
          if (this.passConfir) {
            this.$message.error('两次输入密码不一致！')
            return false
          }
        }
      })
    },
    setArr(arr) { // 去重
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].id)// 不加类型 分不清 1 '1'
        if (!obj[ arr[i].id + type]) {
          temp.push(arr[i])
          obj[ arr[i].id + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
    },
    amend() { // 修改密码
      // this.formData.password = ''
      // this.formData.checkPass = ''
      this.amendShow = true
    },
    CoamendShow() { // 取消修改密码
      this.amendShow = false
    },
    openDialog () {
      if (this.countryId === '') {
        this.$message.error('请先选择国家！')
      } else {
        this.type = 1
        this.index = null
        this.formData.name = ''
        this.formData.sex = 1
        this.formData.password = ''
        this.formData.checkPass = ''
        this.formData.phone = ''
        this.formData.email = ''
        this.formData.roleId = ''
        this.formData.operationType = 1
        if (this.tableData.length > 0) {
          this.formData.ownerFlag = 0
        } else {
          this.formData.ownerFlag = 1
        }
        
        this.amendShow = true
        this.contactShow = true
      }
    },
    closeDialog () { // 关闭dialog的
      this.contactShow = false
    }
  },
  watch: {
    basicMessage(val) {
      let arr = []
      if (val.contacts && val.contacts.length > 0) {
        if (this.roles && this.roles.length > 0) {
          val.contacts.forEach(item => {
            item.sex = (item.sex === undefined || item.sex === '') ? 0 : item.sex
            this.roles.forEach(role => {
              if (item.roleId === role.id) {
                item.roleName = role.name
              }
            })
            arr.push(item)
          });   
          this.tableData = arr
        } else {
          this.initData().then(res => {
            if (res) {
              val.contacts.forEach(item => {
                item.sex = (item.sex === undefined || item.sex === '') ? 0 : item.sex
                this.roles.forEach(role => {
                  if (item.roleId === role.id) {
                    item.roleName = role.name
                  }
                })
                arr.push(item)
              });   
              this.tableData = arr
            }
          })
        }
      }
      
      // 国家
      if (val.address.countryId) {
        this.countryId = val.address.countryId
      }
    },
    'formData.ownerFlag': {
      handler(val) {
        this.formData.ownerFlag = val
      },
      deep: true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import '../../scss/specialcomponents.scss';
  .el-dialog__wrapper{
    .el-dialog__body{
      padding: 20px 40px;
      .el-form{
        margin-bottom:20px;
        .el-form-item{
          margin-bottom:15px;
          /deep/.el-form-item__content{
            display: inline-block;
            width:60%;
            margin-left:10px !important;
          }
        }
      }
    }
    .check_btn {
      margin-left: 46%;
      margin-top: 10px;
    }
    .check_back_btn {
      margin-top: 10px;
    }
  }
  
</style>