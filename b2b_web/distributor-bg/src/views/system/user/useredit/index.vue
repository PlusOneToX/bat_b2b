<template>
  <div class="user-edit">
    <div class="user-header">
      <header>
        <h4 v-if="!isEdite">添加用户</h4>
        <h4 v-else>查看用户</h4>
        <el-button
          class="mini-back-btn btn-home"
          icon="el-icon-d-arrow-left"
          @click="backList()"
        >返回用户列表</el-button>
      </header>
    </div>

    <div v-loading="loading2">
      <el-form
        label-width="100px"
        label-position="right"
        :model="formData"
        :rules="rules"
        ref="formData"
        style="margin: 3%;"
      >
        <el-form-item label="账号密码">
          <el-col :span="16">
            <div class="grid-content bg-purple-dark">
              <el-form-item label="用户名" prop="userName">
                <el-tooltip content="用户创建成功后，用户名无法修改" placement="right">
                  <el-input
                    v-model="formData.userName"
                    style="width: 50%;"
                    :disabled="isEdite"
                    maxlength="20"
                    placeholder="不超过20个字"
                  />
                </el-tooltip>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model.trim="password"
                  type="password"
                  style="width: 50%;"
                  maxlength="16"
                />
              </el-form-item>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-col :span="16">
            <div class="grid-content bg-purple-dark">
              <el-radio-group v-model="status" style="margin: 2%;">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="用户资料">
          <el-col :span="16">
            <div class="grid-content bg-purple-dark">
              <el-form-item label="是否销售员" prop="saleFlag">
                <el-radio-group v-model="formData.saleFlag">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="虚拟销售员">
                <el-radio-group v-model="formData.fictitiousFlag">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="账号中心" prop="rockAccountId" v-if="formData.fictitiousFlag === 0">
                <el-select
                  class="select-width"
                  v-model="formData.rockAccountId"
                  placeholder="请选择关联的账号中心账号"
                  filterable
                  clearable
                  @change="accountAss"
                >
                  <el-option
                    v-for="item in rockAccountList"
                    :key="item.rockAccountId"
                    :label="item.selectTitle"
                    :value="item.rockAccountId"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="姓名" prop="realName">
                <el-input
                  maxlength="20"
                  placeholder="不超过20个字"
                  v-model="formData.realName"
                  style="width: 50%;"
                />
              </el-form-item>

              <el-form-item label="手机号">
                <el-input maxlength="20" v-model="formData.mobile" style="width: 50%;" />
              </el-form-item>

              <el-form-item label="用户ID" prop="erpUserNo">
                <el-tooltip content="用户ID需同ERP中的销售员ID一致，如果不一致无法同步信息" placement="right">
                  <el-input
                    v-model="formData.erpUserNo"
                    style="width: 50%;"
                    maxlength="30"
                    placeholder="不超过30个字"
                  />
                </el-tooltip>
              </el-form-item>

              <el-form-item label="销售组织" prop="organizationId" >
                <el-select
                  class="select-width"
                  v-model="formData.organizationId"
                  placeholder="请选择销售组织"
                  @change="getDepList"
                  :disabled="exaShow"
                >
                  <el-option
                    v-for="item in saleOrganizationList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <!--暂不用-->
              <!-- <el-form-item label="事业部" prop="businessUnitId">
                <el-select
                  class="select-width"
                  v-model="formData.businessUnitId"
                  placeholder="请选择事业部"
                >
                  <el-option
                    v-for="item in businessUnitList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item> -->

              <el-form-item label="销售部门" prop="departmentId">
                <el-select
                  v-model="formData.departmentId"
                  placeholder="请选择销售部门"
                  class="select-width"
                >
                  <el-option
                    v-for="item in saleDepartmentList"
                    :key="item.id"
                    :label="item.departmentName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <!-- <el-form-item label="BAT">
                <el-select v-model="formData.departmentId" placeholder="请选择BAT账号" filterable class="select-width" >
                  <el-option v-for="item in saleDepartmentList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
                </el-select>
              </el-form-item>-->
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="用户类型">
          <el-col :span="16">
            <div class="grid-content">
              <el-radio-group v-model="adminType">
                <el-radio :label="2">普通用户</el-radio>
                <el-radio :label="1">超级管理员</el-radio>
              </el-radio-group>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="权限角色" v-if="adminType == 2">
          <el-col :span="16">
            <div class="grid-content bg-purple-dark">
              <el-checkbox-group v-model="roleIds">
                <el-checkbox
                  :label="item.id"
                  :key="item.id"
                  name="roleIds"
                  v-for="item in allRoles"
                >{{item.roleName}}</el-checkbox>
              </el-checkbox-group>
            </div>
          </el-col>
        </el-form-item>
    
        <el-form-item label="品牌管理" v-if="adminType == 2">
          <el-col :span="16">
            <div class="grid-content bg-purple-dark">
              <div>
                <el-checkbox
                  label="全局"
                  v-model="checkall"
                  :indeterminate="isIndeterminate"
                  @change="handleCheckAll"
                ></el-checkbox>
              </div>
              <el-checkbox-group v-model="brandIds" @change="handleCheckBrand">
                <el-checkbox
                  :label="item.id"
                  :key="item.id"
                  name="brandIds"
                  v-for="item in allBrands"
                >{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </div>
          </el-col>
        </el-form-item>
    
        <el-form-item label="分销商和订单数据权限"  v-if="adminType == 2">
          <el-col :span="16">
            <div class="grid-content bg-purple-dark">
              <el-radio-group v-model="saleScope">
                <div class="global" style="margin: 30px;">
                  <el-tooltip content="可以看见所有的分销商和订单数据" placement="right">
                    <el-radio :label="1">全部业务</el-radio>
                  </el-tooltip>
                </div>
                <div class="single" style="margin: 30px;">
                  <el-tooltip content="只能看到自己负责的分销商和订单数据" placement="right">
                    <el-radio :label="2">单业务范围</el-radio>
                  </el-tooltip>
                </div>
                <div style="margin: 30px;">
                  <el-tooltip content="能看到自己和所选成员负责的分销商和订单数据" placement="right">
                    <el-radio :label="3">多业务范围</el-radio>
                  </el-tooltip>
                </div>
                <transition mode="out-in" name="fade">
                  <div class="sales" v-if="saleScope==3">
                    <el-transfer
                      v-model="saleIds"
                      :data="allSales"
                      :titles="['业务员列表','选中列表']"
                      :button-texts="['取消','选中']"
                    ></el-transfer>
                  </div>
                </transition>
              </el-radio-group>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" v-if="false">
          <el-col :span="16">
            <el-input type="textarea" :autosize="{minRows:5}" v-model="remark" />
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="16">
            <el-col :span="2" :offset="12">
              <el-button class="mini-search-btn" @click="handleSubmit('formData')">保存</el-button>
            </el-col>
          </el-col>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import md5 from "js-md5";
export default {
  name: "useredit",
  data() {
    return {
      rockPrinceId: "",
      append: true,
      looking: false,
      isFirst: true,
      formData: {
        userName: "", // 用户名
        realName: "", // 姓名
        password: "", // 密码
        erpUserNo: "", // 用户ID
        department: "",
        departmentId: "", // 销售部门
        organizationId: "", // 销售组织
        businessUnitId: "", // 事业部
        rockAccountId: "", // 关联账号中心
        mobile: "", // 手机号
        fictitiousFlag: 0, //是否虚拟销售员
        saleFlag: 1,
      },
      password: "",
      remark: "", // 备注
      adminType: 2,
      brandScope: 0,
      saleScope: 1,
      status: 1,
      roleIds: [],
      brandIds: [],
      saleIds: [],
      allRoles: [],
      allSales: [],
      allBrands: [],
      pageInfo: {
        page: 1,
        size: 1000,
      },
      id: "",
      isIndeterminate: false,
      checkall: false,
      rules: {
        userName: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        realName: [
          {
            required: true,
            message: "请输入姓名",
            trigger: "blur",
          },
        ],
        roleIds: [
          {
            required: true,
            message: "选择权限角色",
            trigger: "blur",
          },
        ],
        organizationId: [
          {
            required: true,
            message: "选择销售组织",
            trigger: "change",
          },
        ],
        // rockAccountId: [
        //   {
        //     required: true,
        //     message: "请选择关联的账号中心账号",
        //     trigger: "change",
        //   },
        // ],
      },
      loading: false,
      loading2: false,
      rockAccountList: [], // 账号中心
      saleOrganizationList: [], // 销售组织
      saleDepartmentList: [], // 销售部门
      businessUnitList: [], // 事业部
      isEdite: false,
      rockAccountIdStr: "",
      cancelAdd: false,
      exaShow: false, // 是否禁用销售组织
    };
  },
  computed: {
    isSuperAdmin() {
      //..登录的用户名
      return this.$store.getters.userinfo.userName;
    },
  },
  mounted() {
    this.getParams();
  },
  activated() {
    this.getParams();
  },
  methods: {
    getParams() {
      this.dataCall();
      this.isFirst = true;
      if (this.$route.query.id != undefined) {
        this.isEdite = true;
        this.append = false;
        this.looking = true;
        this.loading2 = true;
        this.$http.userDetail(this, { id: this.$route.query.id }).then(res => { 
          // 用户详情
          if (res.success) {
            if (
              res.data.departmentId === undefined ||
              res.data.departmentId === "0" ||
              res.data.departmentId === 0 ||
              res.data.departmentId === ""
            ) {
              // 销售部门
              res.data.departmentId = "";
            }
            if (
              res.data.organizationId === undefined ||
              res.data.organizationId === "0" ||
              res.data.organizationId === 0 ||
              res.data.organizationId === ""
            ) {
              // 销售组织
              res.data.organizationId = "";
            }
            if (
              res.data.businessUnitId === undefined ||
              res.data.businessUnitId === "0" ||
              res.data.businessUnitId === 0 ||
              res.data.businessUnitId === ""
            ) {
              // 事业部
              res.data.businessUnitId = "";
            }

            this.rockAccountIdStr = res.data.rockAccountId; // 获取账号中心取得b2bId进行判断是否已被使用对比
            this.formData = res.data;
            if (res.data.rockAccountId == 0) {
              this.formData.rockAccountId = "";
            }
            if (res.data.erpUserNo) {
              this.exaShow = true
            }
            this.password = "****************";
            this.remark = this.formData.remark; // 备注
            this.adminType = this.formData.adminType;
            this.id = this.formData.id;
            this.brandScope = this.formData.brandScope;
            this.saleScope = this.formData.saleScope; // 分销商和订单数据权限 1. 全部业务 2.单业务范围 3.多业务范围
            this.status = this.formData.status;
            this.fictitiousFlag = this.formData.fictitiousFlag; //是否虚拟销售员
            this.roleIds = this.formData.roleIds ? this.formData.roleIds : [];
            this.brandIds = this.formData.brandIds ? this.formData.brandIds : [];
             this.saleIds = this.formData.saleIds ? this.formData.saleIds : [];
            if (
              this.brandScope === 1 ||
              this.brandIds.length === this.allBrands.length
            ) {
              this.checkall = true;
              if (this.brandScope === 1) {
                this.allBrands.forEach((item) => {
                  this.brandIds.push(item.id);
                });
              }
            } else {
              this.isIndeterminate = this.brandIds.length > 0;
            }
            this.getDepList();
          } else {
            this.$message(res.msg);
            this.loading2 = false;
          }
          this.loading2 = false;
        });
      } else {
        this.isEdite = false;
        this.password = ""; // 密码
        this.formData.userName = ""; // 用户名
        this.formData.realName = ""; // 姓名
        this.formData.erpUserNo = ""; // 用户ID
        this.formData.department = "";
        this.formData.departmentId = ""; // 销售部门
        this.formData.organizationId = ""; // 销售组织
        this.formData.rockAccountId = ""; // 账号中心
        this.formData.mobile = ""; // 手机号
        this.formData.fictitiousFlag = 0; //是否虚拟销售员
        this.formData.saleFlag = 1;
        this.exaShow = false;
        (this.remark = ""),
          (this.adminType = 2),
          (this.brandScope = 0),
          (this.saleScope = 1),
          (this.tatus = true),
          (this.roleIds = []),
          (this.brandIds = []),
          (this.saleIds = []);
      }
    },
    // ======== 数据调用 ========
    dataCall() {
      this.getAllRoles(); //..销售组织
      this.getAllBrands(); //..品牌管理，全局
      this.getAllSales(); //..用户列表
      this.getOrgList(); //..组织销售
      this.getrockList(); //..账号中心
    },
    // ======== 操作 ========
    getrockList() {
      //..账号中心数据
      this.$http.rockAccountInfoList(this).then(res => {
        if (res.success) {
          this.rockAccountList = res.data
        }
      })
    },
    accountAss(event) {
      //..账号数据中心
      this.rockAccountList.forEach((item) => {
        if (item.rockAccountId == event) {
          if (this.$route.query.addPage == 1) {
            //..addPage为1时为添加用户页面
            this.cancelAdd = true;
            this.formData.realName = item.realName; //..姓名
            this.formData.mobile = item.mobile; //..用户ID
          } else {
            if (this.formData.realName == "" && this.formData.mobile == "") {
              this.formData.realName = item.realName;
              this.formData.mobile = item.mobile;
            } else if (this.formData.realName != "" && this.formData.mobile == "") {
              this.formData.mobile = item.mobile;
            } else if (this.formData.mobile != "" && this.formData.realName == "") {
              this.formData.realName = item.realName;
            }
          }
        }
      });
    },
    getDepList() {
      //..销售组织
      if (
        this.formData.organizationId !== undefined &&
        this.formData.organizationId !== ""
      ) {
        let count = 2;
          this.$http.getDepartmentPoById(this, {
            page:1,
            size:1000,  
            organizationId: this.formData.organizationId,
          })
          .then((res) => {
            if (res.success) {
              let ary = [];
              res.data.list.forEach((item) => {
                item.children = [];
                ary.push(item);
              });
              ary.sort((a, b) => {
                return a.sort - b.sort > 0;
              });
              this.saleDepartmentList = ary;
              count--;
              if (count == 0) {
                this.loading2 = false;
              }
            }
          });
      }
    },

    getAllRoles() {
      //..销售组织
      this.$http.getRoleList(this, this.pageInfo).then(res => {
        if (res.success) {
           this.allRoles = res.data.list;
        }
      })
    },

    getAllBrands() {
      //..品牌管理，全局，数据
        this.$http.getBrandPoList(this, {
          page: 1,
          size: 1000,
          openFlag: 1
        }).then(res => {
          if (res.success) {
            this.allBrands = res.data.list;
          if (this.brandIds.length === this.allBrands.length) {
            this.checkall = true;
          } else {
            this.isIndeterminate = this.brandIds.length > 0;
          }
          }
        })
    },

    getAllSales() {
      //..用户列表
      this.$http.salesList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.allSales = [];
          res.data.list.forEach((item) => {
            let obj = {
              key: item.id,
              label: item.userName,
              disabled: false,
            };
            this.allSales.push(obj);
          });
        }
      });
    },

    backList() {
      //..返回用户列表操作
      this.$router.push({ name: "userlist" });
    },

    getOrgList() {
      //..销售组织
      this.$http.getOrganizationPoList(this, { page: 1, size: 1000 }).then(res => {
          let ary = [];
          res.data.list.forEach((item) => {
            item.children = [];
            ary.push(item);
          });
          ary.sort((a, b) => {
            return a.sort - b.sort > 0;
          });
          this.saleOrganizationList = ary;
        });
    },

    handleCheckAll(val) {
      //..品牌管理，全局，全选
      let allBrandsIds = [];
      this.allBrands.forEach((item) => {
        allBrandsIds.push(item.id);
      });
      this.brandIds = val ? allBrandsIds : [];
      this.isIndeterminate = false;
      if (this.brandIds.length > 0) {
        this.brandScope = 1;
      } else if (this.brandIds.length === 0) {
        this.brandScope = 0;
      }
    },

    handleCheckBrand(val) {
      //..品牌管理，全局，单选
      this.checkall = val.length === this.allBrands.length;
      this.isIndeterminate =
        val.length > 0 && val.length < this.allBrands.length;
      if (val.length < this.allBrands.length) {
        this.brandScope = 2;
      }
    },

    handleCancle() {
      //..取消操作
      this.$router.push({ name: "userlist" });
    },

    handleSubmit(data) {
      //..保存操作
      this.loading = true;
      if (
        this.formData.organizationId === undefined ||
        this.formData.organizationId === ""
      ) {
        this.$message.error("请选择销售组织！");
        this.loading = false;
        return false;
      }
      if (this.formData.id != undefined) {
        //..修改
        if (
          this.password === undefined ||
          this.password === "" ||
          this.password === "****************"
        ) {
          //编辑用户密码可以为空
          this.formData.password = null;
        } else {
          if (this.password.length < 6 || this.password.length > 16) {
            this.$message.error("密码长度6-16个字符！");
            this.loading = false;
            return false;
          }
          this.formData.password = md5(this.password);
        }
        // if (
        //   this.formData.fictitiousFlag == 0 &&
        //   (this.formData.rockAccountId == "" ||
        //     this.formData.rockAccountId == null ||
        //     this.formData.rockAccountId == undefined)
        // ) {
        //   this.$message.error("请选择账号中心");
        //   this.loading = false;
        //   return false;
        // }
        this.formData.id = this.id;
        this.formData.remark = this.remark;
        this.formData.adminType = this.adminType;
        // this.formData.roleIds = this.roleIds.join(",");
        this.formData.roleIds = this.roleIds;
        if (this.formData.adminType === 2) {
          if (this.formData.roleIds == undefined || this.formData.roleIds == "") {
            this.$message.error("普通用户至少要有一个角色！");
            this.loading = false;
            return false;
          }
          if (this.saleScope === 3 && this.saleIds.length === 0) {
            this.$message.error("多业务范围时至少要有一个业务员！");
            this.loading = false;
            return false;
          }
        } else {
          this.saleScope = 1;
        }

        this.formData.brandScope = this.brandScope;
        this.formData.saleScope = this.saleScope;
        this.formData.status = this.status;
        this.formData.brandIds = this.brandIds;
        this.formData.saleIds = this.saleIds;

        this.$http.editUser(this, this.formData).then(res => {    
            if (res.success) {
              this.$message({
                message: "修改成功",
                type: "success",
                duration: 3 * 1000,
                onClose: () => {},
              });
              this.$router.push({ name: "userlist" });
            } else {
              this.loading = false;
            }
          })
          .catch((_) => {
            this.loading = false;
          });
      } else {
        //..添加
        this.$refs[data].validate((valid) => {
          if (valid) {
            this.loading = true;
            if (
              this.password === undefined ||
              this.password === "" ||
              this.password === "****************"
            ) {
              //..添加用户密码不能为空
              this.formData.password = null;
              this.$message.error("新建用户需设置密码！");
              this.loading = false;
              return false;
            } else {
              if (this.password.length < 6 || this.password.length > 16) {
                this.$message.error("密码长度6-16个字符！");
                this.loading = false;
                return false;
              }
              this.formData.password = md5(this.password);
            }
            // if (
            //   this.formData.fictitiousFlag == 0 &&
            //   (this.formData.rockAccountId == "" ||
            //     this.formData.rockAccountId == null ||
            //     this.formData.rockAccountId == undefined)
            // ) {
            //   this.$message.error("新建用户需选择账号中心");
            //   this.loading = false;
            //   return false;
            // }
            this.formData.remark = this.remark;
            this.formData.adminType = this.adminType;
            this.formData.brandScope = this.brandScope;
            this.formData.saleScope = this.saleScope;
            this.formData.status = this.status;
            this.formData.roleIds = this.roleIds;
            if (this.formData.adminType === 2) {
              if (
                this.formData.roleIds == undefined ||
                this.formData.roleIds == ""
              ) {
                this.$message.error("普通用户至少要有一个角色！");
                this.loading = false;
                return false;
              }
            }
             this.formData.brandIds = this.brandIds;
            this.formData.saleIds = this.saleIds;
            this.$http.addUser(this, this.formData).then(res => {    
                if (res.success) {
                  this.$message({
                    message: "添加成功",
                    type: "success",
                    duration: 3 * 1000,
                    onClose: () => {},
                  });
                  this.$router.push({ name: "userlist" });
                }
              });
          } else {
            this.loading = false;
            return false;
          }
        });
      }
    },
  },
  watch: {
    // "formData.rockAccountId": function (val) {
    //   this.formData.rockAccountId = this.rockAccountIdStr;
    //   for (let i = 0; i < this.rockAccountList.length; i++) {
    //     if (
    //       this.formData.rockAccountId == this.rockAccountList[i].rockAccountId
    //     ) {
    //       let b2bIdStr = this.rockAccountList[i].b2bId; // 获取选中的b2bId
    //       if (b2bIdStr != null) {
    //         if (this.formData.id != b2bIdStr) {
    //           const h = this.$createElement;
    //           this.$confirm("", "提示", {
    //             message: h("p", null, [
    //               h("span", null, "该账号已被"),
    //               h(
    //                 "i",
    //                 { style: "color: #21b8cb" },
    //                 this.rockAccountList[i].b2bName
    //               ),
    //               h("span", null, "用户所选择，若继续选择,"),
    //               h(
    //                 "i",
    //                 { style: "color: #21b8cb" },
    //                 this.rockAccountList[i].b2bName
    //               ),
    //               h("span", null, "的账号中心信息将被清除，是否继续？"),
    //             ]),
    //             confirmButtonText: "确定",
    //             cancelButtonText: "取消",
    //             type: "warning",
    //             center: true,
    //           })
    //             .then((_) => {})
    //             .catch((_) => {
    //               if (this.cancelAdd == 1) {
    //                 //..添加用户页面，有被选用的不进行填充名字手机号
    //                 this.formData.realName = "";
    //                 this.formData.mobile = "";
    //                 this.formData.rockAccountId = this.rockAccountIdStr;
    //               } else {
    //                 this.formData.rockAccountId = "";
    //               }
    //             });
    //         }
    //       }
    //     } else {
    //       if (this.cancelAdd == 1) {
    //         //..添加用户页面，有被选用的不进行填充名字手机号
    //         this.formData.realName = "";
    //         this.formData.mobile = "";
    //         this.formData.rockAccountId = this.rockAccountIdStr;
    //       } else {
    //         this.formData.rockAccountId = "";
    //       }
    //     }
    //   }
    // },
    checkall(val) {
      // 品牌管理下全局全选监听
      if (val) {
        this.brandScope = 1;
        let allBrandsIds = [];
        this.allBrands.forEach((item) => {
          allBrandsIds.push(item.id);
        });
        this.brandIds = val ? allBrandsIds : [];
      } else {
        this.brandScope = 0;
      }
    },

    brandIds(val) {
      // 品牌管理下全局单选反选监听
      this.isIndeterminate =
        val && val.length > 0 && val.length < this.allBrands.length;
    },

    // "formData.organizationId": {
    //   // 销售组织
    //   handler() {
    //     this.isFirst == true
    //       ? (this.isFirst = false)
    //       : (this.formData.departmentId = "");
    //   },
    //   deep: true,
    // },
    adminType(val) {
      // 用户类型
      if (val == 2) {
        this.checkall = false;
        this.indeterminate = false;
        this.brandIds = [];
      }
    },
  },
};
</script>
<style lang="scss">
.main[data-v-6beed8bc] {
  background-color: #fff;
}
.user-edit {
  background-color: white;
  .user-header {
    header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
    }
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
  .sale-scope,
  .brand,
  .auth {
    background-color: $bg;
    border-radius: 10px;
    padding: 10px 20px;
    margin: 10px 0;
    .el-radio {
      background-color: white;
    }
  }
  .el-input__inner,
  .el-textarea__inner {
    background-color: $bg;
  }
  .el-checkbox {
    margin-left: 30px;
  }
  .global,
  .single {
    margin-bottom: 20px;
  }
  .sales {
    margin: 30px;
    .el-button--primary {
      color: #fff;
      background-color: $lakeBlue !important;
      border-color: $lakeBlue !important;
    }
    .el-transfer__button.is-disabled,
    .el-transfer__button.is-disabled:hover {
      background-color: #cccccc !important;
      border-color: #cccccc !important;
    }
  }
  .grid-content {
    border-radius: 2px;
    min-height: 36px;
    .select-width {
      width: 200px;
    }
    .el-form-item{
      margin:18px 0;
    }
  }
  .bg-purple-dark {
    background: #f9fafc;
    border: 1px solid #ccc;
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
</style>
