<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
       <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="order-list-wrap">
            <h6 class="user-right-title">{{$t('P.AccountManagement')}}</h6>
            <div class="query rl-padding-top-default">
              <div class="search rl-clear">
              <!--  <div class="item rl-fl rl-margin-right-xxs">
                  <input
                    type="text"
                    v-model.trim="accountForm.name"
                    :placeholder="$t('UserCenter.Username')"
                  />
                </div>
                <div class="item rl-fl rl-margin-right-xxs">
                  <input
                    type="text"
                    v-model.trim="accountForm.num"
                    onkeyup="this.value=this.value.replace(/\D/g,'')"
                    :placeholder="$t('UserCenter.LoginAccount')"
                  />
                </div>
                <div class="search-order rl-fl">
                  <el-button
                    @click="queryOrder"
                    class="search-btn rl-text-white"
                  >{{$t('UserCenter.Query')}}</el-button>
                </div>-->
                <!-- 新增账号 -->
                <div class="rl-fr export-btn" @click="openPopup('POST')"> +  {{$t('UserCenter.AddAcount')}}</div>
              </div>
            </div>
            <!--数据列表-->
            <div class="query-detail">
              
              <div class="table">
                <table>
                  <tr>
                    <th>{{$t('UserCenter.SerialNumber')}}</th>
                    <th>{{$t('UserCenter.Username')}}</th>
                    <th>{{$t('UserCenter.Account')}}</th>
                    <th>{{$t('UserCenter.Role')}}</th>
                    <th>{{$t('UserCenter.Operation')}}</th>
                  </tr>
                  <tr v-if="contactList.length === 0 || totalCount === 0">
                    <td class="empty" colspan="5">{{$t('P.NoData')}}</td>
                  </tr>
                  <tr v-else class="goods-list" v-for="(item,index) in contactList" :key="index">
                    <td>{{item.id}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.phone}}</td>
                    <td>{{item.roleName}}</td>
                    <td width="20%" class="rl-clear" >
                      <div style="display:flex;align-items: center;">
                        <div class="views rl-fl">
                          <span  @click="openPopup('PUT',item)"  class="log rl-tc cursor-pointer rl-fl">编辑</span>
                          <!-- {{$t('UserCenter.OrderAgain')}}-->
                        </div>
                        <div class="views rl-fl ">
                          <span @click="deleteFun(item.id)" class="log rl-tc cursor-pointer">删除</span>
                        </div>
                      </div>

                    </td>
                  </tr>
                </table>
              </div>
            </div>
          </div>
          <!--分页-->
          <div class="apply rl-clear">
            <div class="rl-tr rl-margin-top-default">
              <el-pagination
                v-if="paginationShow && this.totalCount > 0"
                background
                :current-page.sync="cur_page"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
                layout="prev, pager, next, jumper"
                :page-size="pagesize"
                :total="totalCount"
              ></el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--弹框-->
    <el-dialog class="alls" :title="isAdd==1?$t('OrderSuccess.AddUserTitle'):$t('OrderSuccess.EditUserTitle')" :visible.sync="dialogVisible">
      <div class="Am-dialog-box">
          <div class="Am-dialog-line" v-if="isAdd==2">
             <span>是否主账号</span>
             <el-radio-group  v-model:trim="roleForm.ownerFlag" class="Am-el-radio">
                <el-radio :label="0" value="0">否</el-radio>
                <el-radio :label="1" value="1">是</el-radio>
              </el-radio-group>
          </div>
          <div class="Am-dialog-line">
             <span>姓名</span>
             <input placeholder="" v-model:trim="roleForm.name"/>
          </div>
          <div class="Am-dialog-line">
             <span>手机号/登录账号</span>
             <input  :placeholder="$t('P.PleaseEnter')" size="11"  onkeyup="this.value=this.value.replace(/\D/g,'')" v-model:trim="roleForm.phone"/>
          </div>
          
          <div class="Am-dialog-line">
             <span>性别</span>
             <el-radio-group placeholder="请选择性别"  v-model:trim="roleForm.sex" class="Am-el-radio">
                <el-radio :label="1" value="1">男</el-radio>
                <el-radio :label="2" value="2">女</el-radio>
              </el-radio-group>
          </div>
          <div class="Am-dialog-line">
             <span>邮箱</span>
             <input  placeholder="输入邮箱"  @blur="schoolCheckEmail" v-model:trim="roleForm.email"/>
          </div>
          <div class="Am-dialog-line">
             <span>角色</span>
             <el-select class="Am-dialog-select" :placeholder="$t('P.Select')" v-model.trim="roleForm.roleId" clearable>
                <el-option
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                  v-for="item in roleListData"
                ></el-option>
              </el-select>
          </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmDialog">{{$t('P.OK')}}</el-button>
        <el-button @click="dialogVisible = false">{{$t('P.Cancel')}}</el-button>
      </div>
    </el-dialog>
   
    <!--加载动画-->
    <loading v-if="this.showLoading === true"></loading>

   
      

      
  </div>
</template>

<script>
import Vue from 'vue';
import Header from '@/components/Header.vue';
import Left from '@/components/Left.vue';
import onceAgain from '@/components/onceAgain.vue';
import GD from '@/assets/js/globalData';
import loading from '@/components/loading.vue';
import countDown from '@/components/countDown.vue';

// liu--
import { contactList,contactApi,roleList} from '@/apiService/api'
export default {
  name: 'OrderManage',
  components: { Header, Left, onceAgain, loading, countDown },
  data () {
    return {
      // 查询表单
      accountForm:{
          name:'',   //姓名
          num:'',  //账号
      },
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      contactList:[],  //联系人列表
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB', // 语种
      dialogVisible: false, // 新增编辑弹框
      isAdd:1,  //1 新增 2 修改
      roleListData:[
        {id:1,title:'jue1'},
        {id:2,title:'jue2'},
      ],  //角色列表
      roleForm:{
         name:'',
         phone:'',
         roleId:'',
         sex:1,
         email:'',
         ownerFlag:0,
      },
      

      // liu--end
      userState: 2,
      showLoading: false,
      orderStatus: 0, // 订单状态
      name: '',
      phone: '',
      paginationShow: true, // 分页控制
      
      dialogItemList: [], // 再来一单弹框显示货品
      editUser:{},  //要修改的用户数据
      
    };
  },
 
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang')
      ? window.localStorage.getItem('bLang')
      : 'zh-RMB';
  },
  mounted(){
      this.roleListFun();
      this.getDataList();
  },
  methods: {
    schoolCheckEmail () {
      var reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z]{2,5}$/
      if (!reg.test(this.roleForm.email)) {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入有效的对账人邮箱！')
        } else { this.$message.warning('Please enter valid Reconciliation personnel e-mail.') }
        this.roleForm.email = ''
        return false
      }
    },
    //  获取联系人列表
    getDataList(){
       let id=localStorage.getItem('userId');
       let that=this;
       console.log('联系人id',id)
       let params={
         id:id,
         page:that.cur_page,
         size:that.pagesize
       }
       contactList(params).then(res=>{
           console.log('联系人列表',res);  
           if(res.success){
              let dataList =res.data.list;
              dataList.forEach(item=>{
                that.roleListData.forEach(items=>{
                   if(item.roleId==items.id){
                      item.roleName=items.name;
                   }
                })
              })
               that.contactList=dataList;
               console.log(that.contactList);
               that.totalCount=res.data.total;
           }else{
              that.$message.error(res.errMessage);
           }
       })
       .catch(err=>{
          
       })
    },

    // 查询用户角色列表
    roleListFun(){
       let id=localStorage.getItem('userId');
       let that=this;
        roleList({id:id,page:1,size:6000}).then(res=>{
           console.log('角色列表',res); 
           if(res.success){
               that.roleListData=res.data.list;
               
           }else{
              this.$message(res.errMessage);
           }
            
        }).catch(err=>{

        })
    },

    // 打开新增和编辑弹框
    openPopup(methods,item){
        this.dialogVisible=true;
        this.isAdd=methods=='POST'?1:2;
        let roleForm=this.roleForm;
        console.log(item);
        if(methods=='PUT'){
          this.editUser=item;
          roleForm.name=item.name;
          roleForm.phone=item.phone;
          roleForm.roleId=item.roleId;
          roleForm.ownerFlag=item.ownerFlag;
          roleForm.sex=item.sex;
          roleForm.email=item.email;
        }else{
          this.editUser={};
          roleForm.name='';
          roleForm.phone='';
          roleForm.roleId='';
          roleForm.ownerFlag=0;
          roleForm.sex=1;
          roleForm.email='';
        }
        
    },

    // 新增or编辑
    addOrEditFun(){
        let item=this.editUser;
        let id=localStorage.getItem('userId');
        let that=this;
        let methods=this.isAdd==1?'POST':'PUT';
        let roleForm=this.roleForm; 
        let params={
          distributorId:id,  //分销商（客户）id     
          operationType: this.isAdd,  //操作类型 1 新增 2 修改
          ownerFlag:1,  //是否账号拥有: 0 否, 1 是
        }
        params={...params,...roleForm};
        if(this.isAdd==2){   //修改需传递联系人的id
          params={...params,...{id:item.id }};
        }
        if (!params.email) {
          params.email = undefined;
        }
        console.log(params);
        contactApi(methods,params).then(res=>{
            console.log('联系人列表',res);  
            if(res.success){
                that.dialogVisible=false;
                that.cur_page =1;
                that.getDataList();
                 that.$message.success(this.isAdd==1?'添加成功！':'编辑成功！');
            }else{
                that.$message.error(res.errMessage);
            }
        })
        .catch(err=>{
            
        })
    },

    // 删除
    deleteFun(id){
        let that=this;
        contactApi('DELETE',{id:id}).then(res=>{
            if(res.success){
                that.getDataList();
            }else{
                that.$message.error(res.errMessage);
            }
        })
        .catch(err=>{
            
        })
    },
  
    // 当前页码
    handleCurrentChange (val) {
      console.log(val);
      this.cur_page = val;
      this.getDataList();
      
    },
    // 每页条数
    handleSizeChange (val) {
      this.pagesize = val;
      this.getDataList();
    },
    
    // 订单查询
    queryOrder () {
      this.paginationShow = false;
      this.cur_page = 1;
      this.getDataList();


      this.$nextTick(function () {
        this.paginationShow = true;
      });
    },
    
    confirmDialog () {
      let pattern = /^1(3|4|5|6|7|8|9)\d{9}$/; // 手机号
      console.log(this.roleForm);
      let roleForm=this.roleForm;
      // 判断姓名
      if(roleForm.name==''){
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入姓名");
        } else {
          this.$message.warning("Please enter name.");
        }
         return
      }
      // 判断手机号
      if (roleForm.phone === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入手机号");
        } else {
          this.$message.warning("Please enter mobile phone number.");
        }
        return 
      }
      roleForm.phone = roleForm.phone.replace(/\D/g, "");
      if (this.$i18n.locale === "zh") {
        if (!pattern.test(roleForm.phone)) {
          this.$message.warning("请输入正确的手机号");
          return 
        }
      }
      // 判断角色
      if(roleForm.roleId==''){
          if (this.$i18n.locale === "zh") {
            this.$message.warning("请选择角色");
          } else {
            this.$message.warning("Please choice role.");
          }
         return
      }

      this.addOrEditFun();
      // this.dialogVisible = false;
      
    },
   
   
    
  
   
 
  },
 
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.Am-dialog-box{
  border-top:1px solid #F2F3F8;
  padding:30px 0;
   .Am-dialog-line{
      width:550px;
      display:flex;
      align-items:center;
      justify-content:center ;
      margin-top:20px;
      span{
        width:150px;
        text-align:right;
      }
      input{
        width:250px;
        border:1px solid  #DCDFE6;
        border-radius:5px;
        font-size:14px;
        padding:8px 10px;
        margin-left:12px;
      }
      .Am-dialog-select{
        width:270px;
        margin-left:12px;
      }
    }
}
.Am-el-radio{
  width:250px;
  margin-left:25px;
}

.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .order-list-wrap {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .query {
    margin-bottom: 25px;
    .search {
      .item {
        input[type="text"] {
          padding-left: 10px;
          width: 140px;
          height: 38px;
          font-size: 12px;
          box-sizing: border-box;
          border: 1px solid @bdLighterColor;
          border-radius: 4px;
        }
        input[type="button"] {
          padding: 0 12px;
          height: 38px;
          line-height: 38px;
          box-sizing: border-box;
          border-radius: 4px;
        }
      }
      .select {
        /deep/ .el-input__inner {
          width: 180px;
        }
      }
    }
    .search-info {
      .items {
        .common-input {
          input {
            padding-left: 10px;
            width: 140px;
            height: 38px;
            font-size: 12px;
            box-sizing: border-box;
            border: 1px solid @bdLighterColor;
            border-radius: 4px;
          }
          &.addr-input {
            input {
              width: 180px;
            }
          }
        }
        &.date-items {
          line-height: 40px;
        }
      }
      /deep/ .el-date-editor.el-input,
      .el-date-editor.el-input__inner {
        width: 200px;
      }
    }
    /deep/ .el-input__inner {
      height: 38px;
      font-size: 12px;
      border: 1px solid @bdLighterColor;
      border-radius: 4px;
    }
  }
  button {
    padding: 0 16px;
    height: 40px;
    line-height: 40px;
    border: none;
  }
  .search-btn {
    background-color: @blue;
    &:hover,
    &:active,
    &:focus {
      color: @white;
      opacity: 0.6;
    }
  }
  .query-detail {
    .nav {
      margin-bottom: 15px;
      width: 100%;
      height: 40px;
      line-height: 40px;
      ul {
        overflow: hidden;
        background-color: @bdLightColor;
        border-radius: 4px;
        &.navEn {
          li {
            width: auto;
            padding: 0 12px;
            font-size: 13px;
          }
        }
        li {
          float: left;
          width: 93px;
          cursor: pointer;
          font-size: 14px;
          color: @lighterBlack;
          text-align: center;
          &:hover,
          &.current {
            color: @white;
            background-color: @blue;
          }
        }
      }
    }
    
    .table {
      width: 100%;
      margin-bottom: 30px;
      table {
        width: 100%;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        tr {
          &.goods-list:hover {
            background-color: @lightGrayBg;
          }
          & + tr {
            border-top: 1px dashed @bdLighterColor;
          }
          th {
            height: 30px;
            line-height: 30px;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            height: 50px;
            text-align: center;
            font-size: 12px;
            color: @lightBlack;
            &.empty {
              width: 100%;
              font-size: 16px;
              text-align: center;
            }
            .views {
              min-width: 65px;
              span {
                display: block;
              }
              .log {
                width: 100%;
                font-size: 12px;
                color: @blue;
                &:hover {
                  opacity: 0.6;
                }
              }
            }
            .countdown-box {
              width: 100%;
              margin-top: 12px;
              text-align: left;
              box-sizing: border-box;
            }
          }
        }
      }
    }
  }
  .export-btn {
      padding-top: 24px;
      color: @blue;
      line-height: 1;
      cursor: pointer;
      .iconfont {
        margin-right: 2px;
        color: @lighterGray;
      }
      &:hover {
        opacity: 0.6;
      }
    }
  .apply {
    .after-sale {
      z-index: 11;
      width: 70px;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
    }
  }
}
/*弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.5;
}
.pro-cover {
  width: 555px;
  height: 350px;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 99;
  background: #fefefe;
}
.cover-box {
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  z-index: 99;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../assets/images/shut.png") no-repeat center center;
  }
}
/*再来一单*/
.max-height300 {
  max-height: 300px;
}
.shop-table {
  margin-bottom: 20px;
  overflow-y: scroll;
  table {
    tr {
      td {
        height: 45px;
        text-align: center;
        border-bottom: 1px solid #ccc;
        .buy-sum {
          width: 92px;
          height: 22px;
          line-height: 22px;
          border: 1px solid #ebeff5;
          div {
            height: 22px;
            box-sizing: border-box;
            background-color: #fff;
            input {
              width: 62px;
            }
          }
          .buyac {
            width: 22px;
            font-size: 22px;
            color: #9b9b9b;
            cursor: pointer;
            text-align: center;
          }
          .buyb {
            width: 48px;
            line-height: 22px;
            color: #3a3a3a;
            border-left: 1px solid #ebeff5;
            border-right: 1px solid #ebeff5;
            input {
              width: 46px;
            }
          }
        }
        .songImg {
          width: 50px;
          img {
            margin-top: 5px;
            height: 50px;
          }
        }
      }
    }
  }
}
.export-price {
  border: 1px solid transparent;
  background-color: #eef8fa;
  .all-file {
    border-bottom: 1px solid #d2d2d2;
    padding-left: 10px;
    padding-right: 10px;
  }
  .file {
    padding-left: 10px;
    padding-right: 10px;
  }
}
</style>
<style lang='less'>
.el-dialog__wrapper {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
