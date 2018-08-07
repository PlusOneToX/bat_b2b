<template>
  <div class="role-edit">
    <header v-if="append">
      <h4>添加角色</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
        返回角色列表
      </el-button>
    </header>
    <header v-if="looking">
      <h4>查看角色</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
        返回角色列表
      </el-button>
    </header>

    <el-form class="role-edit-form" label-width="100px" :model="formData" :rules="rules" ref="formData" label-position="right" v-loading="loading2">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="formData.roleName" maxlength="20" placeholder="不超过20个字">
        </el-input>
      </el-form-item>

      <el-form-item label="角色描述">
        <el-input type="textarea" v-model="formData.roleDescription" maxlength="100" placeholder="不超过100个字"></el-input>
      </el-form-item>

      <el-form-item label="权限选择">
        <div class="auth-box">
          <!-- <div  v-for="item in authMap" :key="item.title" v-if="loading">
            <div class="auth-item">
                <div style="font-weight: 500;">{{item.title}}</div>
                <div>
                  <div class="module module-left">
                    <auth-list :authData="item.auth.actions" :checkedNode="componentPermissions" title="功能" :name="item.title" @checkedChange="getActionCheckedChange" :key="1">
                    </auth-list>
                  </div>
                  <div class="module module-right">
                    <auth-list :authData="item.auth.menus" :checkedNode="componentMenus" :name="item.title" title="菜单" @checkedChange="getMenuCheckedChange" :key="2">
                    </auth-list>
                  </div>
                </div>
            </div>
          </div> -->
          <div v-if="loading" class="list-box left">
            <div v-for="item in mapList" :key="item.title" class="auth-left">
              <div class="auth-item">
                  <div style="font-weight: 500;">{{item.title}}</div>
                  <div>
                    <div class="module module-left">
                      <auth-list :authData="item.auth.actions" :checkedNode="componentPermissions" title="功能" :name="item.title" @checkedChange="getActionCheckedChange" :key="1">
                      </auth-list>
                    </div>
                  </div>
              </div>
            </div>
          </div>
          <div v-if="loading" class="list-box right">
             <div v-for="item in menuList" :key="item.title" class="auth-right">
              <div class="auth-item">
                  <div style="font-weight: 500;">{{item.title}}</div>
                  <div>
                    <div class="module module-right">
                      <auth-list :authData="item.auth.menus" :checkedNode="componentMenus" :name="item.title" title="菜单" @checkedChange="getMenuCheckedChange" :key="2">
                      </auth-list>
                    </div>
                  </div>
              </div>
            </div>
          </div>
        </div>
      </el-form-item>
      <div class="foot-btn">
        <el-button style="margin-left: 49%;" class="mini-search-btn" @click="handleSubmit('formData')" :loading="btnloading">保存</el-button>
        <el-button size="mini" @click="handleCancle">取消</el-button>
      </div>
    </el-form>
  </div>
</template>
<script type="text/javascript">
import authList from "../../components/authList";
import { auth as authMap } from './authMap';
import { sortBy, setArrByKey } from '@/utils/common.js'
export default {
  name: 'roleedit',
  data() {
    return {
      append: true,
      looking: false,
      id: 0,
      formData: {
        roleName: '',
        roleDescription: ''
      },
      rules: {
        roleName: [{
          required: true,
          message: '请输入角色名称',
          trigger: 'blur'
        }],
      },
      mapList: [],
      menuList: [],
      loading: false,
      loading2: false,
      btnloading: false,

      // 以下2*3是权限相关数据 [{..},{..},{..},{..}]
      permissions: [], // array made by a lot of {id,module,roleId}
      menus: [],
      /** ------------- 上方-用于submit的数据，只是从checkedActions到submit的中间层 ------------- */
      /** ------------- 下方-核心数据 被勾选的功能module  ------------- */
      checkedActions: [], // checkedActions.module: string[] 结构化数据
      checkedMenus: [],
      /** ------------- 下方-传给组件的数据  ------------- */
      componentPermissions: [], // 全局被选中的权限 把permissions扁平化为string[]
      componentMenus: [],
    }

  },
  created() {
    this.id = this.$route.params.id;
    // 获取权限列表
    this.$http.permissionPoList(this).then(res => {
      if (res.success) {
        this.mapList = res.data.sort(sortBy('sort'))
      }
    })
    // 获取菜单列表
    this.$http.menuPoList(this).then(res2 => {
      if (res2.success) {
        this.menuList = res2.data.sort(sortBy('sort'))
      }
    })
    if(this.id != undefined && this.id != 0) {
      this.loading2 = true;
      this.append = false;
      this.looking = true;
      this.$http.roleDetail(this, { id: this.id }).then(res => {
        if (res.success) {
          this.formData.roleName = res.data.roleName;
          this.formData.roleDescription = res.data.roleDescription;

          this.permissions = res.data.permissions;
          this.menus = res.data.menus;

          if (this.permissions.length>0) {
            this.permissions.forEach(item => {
              this.componentPermissions.push(item.permissionModule) // push a string
            })
          }
          if (this.menus.length>0) {
            this.menus.forEach(item => {
              this.componentMenus.push(item.menuEn)
            })
          }
         
          this.loading = true;
          if(res.success) {
            this.loading2 = false
          }else {
            this.loading2 = false
          }
        }
      })
    } else {
      this.loading = true;
    }
  },
  computed: {
    // authMap 结构化的权限数据 没有是否选择的数据 树形结构
    // 它的意义在于 列出可选项
    authMap() {
      return authMap
    },
  },
  components: {
    authList
  },
  methods: {
    // 返回
    backpuls() {
      this.$router.push({ name: 'rolelist'})
    },
    /**
     * 处理auth-list组件中的勾选改变事件
     * val 是 功能module组成的数组
     * val.module : {title: string, modules: string[]}
     * val.title : string 
     */
    getActionCheckedChange(val) {
      if(this.checkedActions.length != 0) {
        this.checkedActions.forEach((item, index) => {
          if(item.title == val.title) {
            // 如果该module已存在 在数组checkedActions中把item替换为val
            // 如果该module不存在 直接将val push到数组在数组checkedActions中把item替换为val
            this.checkedActions.splice(index, 1);
          }
        })
      }
      this.checkedActions.push(val);
    },
    
    getMenuCheckedChange(val) { // 与getActionCheckedChange非常相似,一个对应action,一个对应menu
      if(this.checkedMenus.length != 0) {
        this.checkedMenus.forEach((item, index) => {
          if(item.title == val.title) {
            this.checkedMenus.splice(index, 1);
          }
        })
      }
      this.checkedMenus.push(val);
    },
    handleCancle() { // 取消操作
      this.$router.go(-1)
    },
    handleSubmit(data) { // 保存操作
      this.$refs[data].validate(valid => {
        if(valid) {
          this.btnloading=true;
          this.permissions = [];
          this.menus = [];
          // 分别将actions和menus转化为合适的格式,并写入准备发出的数据
          // 格式化后准备发出的数据： permissions, menus
          this.checkedActions.forEach(item => {
            item.modules.forEach(val => {
              let obj = {
                module: val
              }
              this.permissions.push(obj)
            })
          })

          this.checkedMenus.forEach(item => {
            item.modules.forEach(val => {
              let obj = {
                module: val
              }
              this.menus.push(obj)
            })
          })

          // 去重
          this.permissions = setArrByKey(this.permissions, 'module')
          this.menus = setArrByKey(this.menus, 'module')
          
          if(this.id === undefined || this.id === 0) { // 新建
            // 拼凑即将发出的params
            let obj = {
              roleName: this.formData.roleName,
              roleDescription: this.formData.roleDescription,
              permissions: this.permissions, // 核心数据,权限信息
              menus: this.menus // 核心数据,权限信息
            }
            if(obj.permissions == undefined || obj.permissions.length === 0 || obj.menus == undefined || obj.menus.length === 0 ){
              this.$message('功能和菜单至少设置一个权限！')
              this.btnloading=false;
              return
            }else{
              // obj.permissions.push({module:"role"})
            }
            this.$http.addRole(this, obj).then(res => {
              if(res.success) {
                this.$message({
                  message: '添加成功',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
                })
                this.$router.push({ name: 'rolelist' })
              }
              this.btnloading=false;
            })
          }else { // 编辑
            let obj = {
              id: this.id,
              roleName: this.formData.roleName,
              roleDescription: this.formData.roleDescription,
              permissions: this.permissions,
              menus: this.menus
            }
            if(obj.permissions == undefined || obj.permissions.length === 0 || obj.menus == undefined || obj.menus.length === 0 ){
              this.$message('功能和菜单至少设置一个权限！')
              this.btnloading=false;
              return
            }else{
              obj.permissions.push({module:"role"})
            }
            this.$http.editRole(this, obj).then(res => {  
              if(res.success) {
                this.$message({
                  message: '修改成功',
                  type: 'success',
                  duration: 3 * 1000,
                  onClose: () => { }
                })
                this.$router.push({ name: 'rolelist' })
              }
              this.btnloading=false;
            })
          }
        } else {
          this.btnloading=false;
          return false;
        }
      })
    }
  }
}

</script>
<style lang="scss">
.role-edit {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    margin-bottom: 20px;
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    } 
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .role-edit-form {
    padding-left: 40px;
    padding-right: 40px;
    padding-top: 20px;
  }
  .el-input__inner,
  .auth-box,
  .el-textarea__inner {
    background-color: $bg;
  }
  .el-form-item__content {
    line-height: 30px;
  }
  .auth-box {
    width: 100%;
    height: 100%;
    
    padding-bottom: 5%;
    border-radius: 10px;
    display:inline-block;
    .list-box{
      display: inline-block;
      width:100%;
      &.left{
        float:left;
        width:48%;
      }
      &.right{
        float:right;
        width:48%;
      }
    }
    
    .auth-item {
      padding-left: 2%;
      padding-right: 2%;
      padding-top: 10px;
      width: 100%;
      display:inline-block;
    }
    .module {
      background-color: white;
      width: 100%;
      border-radius: 10px;
    }
    .module-left {
      float: left;
    }
    .module-right {
      float: right;
    }
  }
  .foot-btn {
    padding-bottom: 100px;
  }
}

</style>
