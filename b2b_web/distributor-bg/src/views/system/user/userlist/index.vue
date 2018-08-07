<template>
  <div class="user-list">
    <header>
		  <h4>用户列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleAdd()"> 添加用户 </el-button>
		</header>
    <div class="user_header">
      <el-select class="user_star" @change="filter()"  placeholder="启用状态" size="mini" v-model="status" clearable>
        <el-option :key="item.status" :label="item.statusName" :value="item.status" v-for="item in statuss">
        </el-option>
      </el-select>
      <!-- <el-select class="user_type" @change="filter()" placeholder="角色" size="mini" v-model="adminType" clearable>
        <el-option :key="item.adminType" :label="item.typeName" :value="item.adminType" v-for="item in userTypes">
        </el-option>
      </el-select> -->
      
      <el-button type="info" class="mini-search-btn user_search" size="mini" @click.prevent="filter()">搜索</el-button>
      <el-input class="user_input" @change="contentChange" clearable size="mini" @keyup.enter.native="filter()" placeholder="请输入关键词搜索" v-model.trim="content"></el-input>
      <el-select
            class="content_select"
            placeholder="选择类型"
            size="mini"
            v-model="contentType"
            clearable
          >
        <el-option
          v-for="item in contentTypes"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </div>

    <el-table :data="tableData" border class="tableCenter" style="width:100%" v-loading="loading" header-row-class-name="header-row">
      <el-table-column align="center" label="用户名" prop="userName" show-overflow-tooltip fixed :min-width="110"></el-table-column>
      <el-table-column align="center" label="用户编号" prop="erpUserNo" show-overflow-tooltip :min-width="120"></el-table-column>
      <el-table-column align="center" label="账号中心ID" prop="rockAccountId" show-overflow-tooltip :formatter="teturnAccountId" :min-width="110"></el-table-column>
      <el-table-column align="center" label="姓名" prop="realName" show-overflow-tooltip :min-width="120"></el-table-column>
      <el-table-column align="center" label="部门" prop="departmentName" show-overflow-tooltip :min-width="120"></el-table-column>
      <el-table-column align="center" label="登录次数" prop="times" show-overflow-tooltip :min-width="100"></el-table-column>
      <el-table-column  align="center"  label="角色" prop="roleName" :formatter="returnAdminType" show-overflow-tooltip :min-width="120"></el-table-column>
      <el-table-column align="center" label="最后登录时间" prop="lastOnlineTime" :formatter="timeFormat" show-overflow-tooltip :min-width="160"></el-table-column>
      <el-table-column align="center" label="状态" prop="status" :formatter="userStatus" show-overflow-tooltip :min-width="80"></el-table-column>
      <el-table-column align="center" label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          <el-button class="mini-freeze-btn" v-show="scope.row.status" @click="handleEnable(scope.$index, scope.row)">停用</el-button>
          <el-button class="mini-tableadd-btn" v-show="!scope.row.status" @click="handleEnable(scope.$index, scope.row)">启用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
  </div>
</template>
<script>
import { timeFormat } from '@/utils/timeFormat'
import page from '@/components/pagination'
import store from '@/store'
export default {
  name: 'userlist',
  data() {
    return {
      // tableHead: ['number', 'userName', 'name', 'status', 'department', 'lastOnlineTime', 'times', 'adminType', 'remark'],
      tableData: [],
      tableHeadData: [],
      userTypes:[{
        adminType:1,
        typeName:'超级管理员'
      },{
        adminType:2,
        typeName:'普通用户'
      }],
      statuss:[{
        status:1,
        statusName:'启用'
      },{
        status:0,
        statusName:'停用'
      }],
      contentTypes: [
        { value: 1, label: '用户名' },
        { value: 2, label: '用户编号' },
        { value: 3, label: '姓名' },
        { value: 4, label: '权限角色' }
      ],
      total: 0,
      pageInfo: {
        page: 1,
        size: 10,
        contentType: '',
        content:'',
        status:1
      },
      loading: false,
      contentType:1,
      content:'',
      status:1
    }
  },
  components: {
    page,
  },
  activated() {
    // this.tableHeadInit()
    this.getAllRoles()
    this.getAllBrands()
    this.init()
  },
  computed: {
    /** 是否超级管理员 */
    // isSuperAdmin() {
    //   return this.$store.getters.userinfo.adminType === 1
    // },
  },
  methods: {
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.filter()
      }
    },
    // ======== 操作 ========
    filter(){ // 搜索操作
      this.pageInfo.content = this.content
      if(this.contentType === ''){ 
        this.pageInfo.contentType = undefined
      }else{
        this.pageInfo.contentType = this.contentType
      }
      if(this.status === ''){ // 启用状态
        this.pageInfo.status = undefined
      }else{
        this.pageInfo.status = this.status
      }
      this.pageInfo.page = 1
      this.init()
    },
    
    // ======== 数据 ========
    init() { // 列表详情
      this.loading = true;
      this.$http.getUserList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
        this.loading = false
      })
    },

    sizeChange(size) {
      this.pageInfo.page = 1
      this.pageInfo.size = size;
      this.init()
    },
    
    currentChange(page) {
      this.pageInfo.page = page;
      this.init()
    },

    getAllRoles() {
      this.$http.getRoleList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.$store.commit('GET_ALL_ROLES', res.data.list)
        }
      })
    },
    getAllBrands() {
      this.$http.getBrandPoList(this, {
        page: 1,
        size: 10,
        adminType:'',
        content:'',
        status:1
      }).then(res => {
        if (res.success) {
          this.$store.commit('GET_ALL_BRANDS', res.brands)
        }
      })
    },

    handleEnable(index, row) { // 停用操作
      if(row.status){
        this.$confirm('是否要停用此用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
        }).then(() => {
          this.$http.userStatus(this, { id: row.id,status: 0 }).then(res => { 
            if (res.success) {
              this.$message({
                message: '停用成功',
                type: 'success',
                duration: 3 * 1000,
              })
              this.init()
            }
          })
        })
      }else{
        this.$confirm('是否要启用此用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
        }).then(() => {
          this.$http.userStatus(this, { id: row.id,status: 1 }).then(res => {
            if (res.success) {
              this.$message({
                message: '启用成功',
                type: 'success',
                duration: 3 * 1000,
              })
              this.init()
            }   
          })
        })
      }
    },

    // ======== 转换 ========
    returnAdminType(row, col, val) { //..用户类型
      if(row.adminType == 1) {
        return '超级管理员'
      }else {
        return val
      }
    },

    teturnAccountId(row, col, val) { //..账号中心ID
      if(val == 0) {
        return ''
      }else {
        return val
      }
    },

    userStatus(row, col, val) { //..状态
      if(val) {
        return '启用'
      } else {
        return '停用'
      }
    },

    timeFormat(row, col, val) { // 时间戳转换
      return timeFormat(val)
    },

    tableHeadInit() {
      this.tableHeadData = map.filter(item => {
        return this.tableHead.indexOf(item.label) >= 0
      })
    },
    handleAdd() { //..添加用户操作
      this.$store.commit('GET_USERLIST_INFO', '');
      this.$router.push({ name: 'useredit',query:{addPage: 1}});
    },

    handleEdit(index, row) { // 查看操作
      // this.$store.commit('GET_USERLIST_INFO', row)
      this.$router.push({ name: 'useredit',query:{id:row.id} })
    },
  },
  watch: {
    // 'pageInfo.content': {
    //   handler() {
    //     this.pageInfo.content = content
    //     this.init()
    //   },
    //   deep: true
    // },
    tableHead() {
      this.tableHeadInit()
    }

  }
}

</script>
<style rel="stylesheet/scss" lang="scss" >
.user-list {
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
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .user_header {
    .user_star {
      width: 130px;
      margin: 10px ;
    }
    .user_type {
      width: 130px;
      margin-top: 10px;
      margin-bottom: 10px;
    }
    .user_search {
      float: right;
      margin: 10px 10px 10px 5px;
    }
    .user_input {
      float: right;
      width:250px;
      margin: 10px 0;
      .el-input__inner{
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
        border-left: none;
      }
    }
    .content_select{
      float:right;
      width: 120px;
      margin: 10px 0;
      /deep/.el-input{
        .el-input__inner{
          border-top-right-radius: 0;
          border-bottom-right-radius: 0;
        }
      }
    }
  }
}
</style>
