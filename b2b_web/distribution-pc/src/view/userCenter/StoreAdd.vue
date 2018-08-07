<template>
  <div class="store-add">
    <!--公共头部-->
    <Header :userState="userState"></Header>
     <!--主内容-->
    <div class="store-main rl-clear rl-margin-zero">
      <!--公共左边-->
      <Left></Left>
       <div class="store-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
        <div class="content">
          <div class="store-right-title">
            <h6 class="title">{{$t('UserCenter.StoreManage')}}-{{isEdit?$t('UserCenter.EditStore'):$t('UserCenter.AddStore')}}</h6>
            <div class="back" @click="handleBack">
              <i class="iconfont icon-left"></i>
              <span>{{$t('P.Back')}}</span>
            </div>
          </div>
          <div class="store-form">
            <el-form :model="detailData" :rules="rules" label-width="20%" label-position="right" ref="ruleForm">
            <el-row>
              <el-col :span="18">
                <!-- 公众号名称 -->
                <el-form-item :label="$t('UserCenter.AppName')" prop="appId" v-if="appArr && appArr.length>0">
                  <el-select class="app_select ml-15" :placeholder="$t('UserCenter.AppName')" size="mini" v-model="detailData.appName" @change="handleSelect">
                  <el-option v-for="item in appArr" :key="item.id" :label="item.name" :value="item.id" ></el-option>
                </el-select>
                </el-form-item>
                <!-- 门店名称 -->
                <el-form-item :label="$t('UserCenter.StoreName')" prop="shopName">
                  <el-input v-model.trim="detailData.shopName"></el-input>
                </el-form-item>
                <!-- 门店编码 -->
                <el-form-item :label="$t('UserCenter.StoreCode')" prop="shopCode">
                  <el-input v-model.trim="detailData.shopCode" :disabled="disabled"></el-input>
                </el-form-item>
                 <!-- 业务员 -->
                <el-form-item :label="$t('UserCenter.Salesman')" prop="salemanName" v-if="subAccountFlag==1">
                  <el-select value-key="id" filterable size="mini" v-model="detailData.salemanName" @change="handleSelectS">
                    <el-option v-for="item in salesmanList" :key="item.id" :label="item.name" :value="item"></el-option>
                  </el-select>
                  
                </el-form-item>
                 <!-- 分账配置 -->
                <el-form-item :label="$t('UserCenter.SubAccountConfig')" prop="userConfigName" v-if="subAccountFlag==1">
                  <el-select value-key="id" :placeholder="$t('UserCenter.SubAccountConfig')" size="mini" v-model="detailData.userConfigName" @change="handleSelectA">
                  <el-option v-for="item in subList" :key="item.id" :label="item.name" :value="item" ></el-option>
                </el-select>
                </el-form-item>
                <!-- 备注 -->
                <el-form-item :label="$t('UserCenter.Remark')" prop="remarks">
                  <el-input type="textarea" v-model="detailData.remark"  placeholder="不超过200个字" maxlength="200"></el-input>
                </el-form-item>
                <!-- 状态 -->
                <el-form-item :label="$t('OrderSuccess.Status')" prop="shopStatus">
                  <el-radio-group v-model="detailData.openFlag">
                    <el-radio :label="1">开启</el-radio>
                    <el-radio :label="0">停用</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            </el-form>
            <div class="clearfix footbtn">
              <el-button
                class="mini-search-btn box-btn"
                @click="handleSubmit('ruleForm')"
                >{{$t('P.Save')}}</el-button
              >
              <el-button size="mini" @click="handleBack">{{$t('P.Cancel')}}</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
// liu--
import { addStore,modifyStore,queryStore,platformList,subAccountUserConfigBy,subAccountSalemanBy} from '@/apiService/api'

export default {
  components: {
    Header,
    Left
  },
  data () {
    return {
      sid: undefined,
      userId: localStorage.getItem('userId'),
      userState: 2,
      appArr: [],
      salesmanList: [],
      subList: [],
      detailData: {
        distributorId: '',
        appId: '',
        appName: '',
        shopName: '',
        platform:'',
        shopCode: '',
        remark: '',
        openFlag: 1,
        distributorCompanyName:localStorage.getItem('companyName'),
        distributorName:localStorage.getItem('userName'),
        wxPlatformId:'',  //微信平台id
        type:1,  //关联的平台类型 1、微信公众号 2、微信小程序
        salemanId: undefined,
        salemanName: undefined,
        userConfigId: undefined,
        userConfigName: undefined
      },
      rules: {
        appId: [
          {
            required: true,
            message: '请选择公众号名称',
            trigger: 'blur'
          }
        ],
        shopName: [
          {
            required: true,
            message: '请填写门店名称',
            trigger: 'blur'
          }
        ],
        shopCode: [
          {
            required: true,
            message: '请填写门店编码',
            trigger: 'blur'
          }
        ],

        openFlag: [
          {
            required: true,
            trigger: 'blur'
          }
        ]
      },
      disabled: false,
      subAccountFlag: localStorage.getItem('subAccountFlag'),  // 是否开启分账
      isEdit:false,
      subAccountFlag:0,  //是否开启分账
    }
  },
  mounted () {
    this.subAccountFlag = localStorage.getItem('subAccountFlag');
    this.sid = this.$route.query.id
    console.log('是否是编辑：',this.sid);
    if(this.sid!=undefined){
       this.isEdit=true;
    }
    if (this.subAccountFlag == 1) {
      subAccountUserConfigBy().then(res => {
        if (res.success) {
          this.subList = res.data
        }
      })
      subAccountSalemanBy({distributorId: this.userId, openFlag: 1}).then(res => {
        if (res.success) {
          this.salesmanList = res.data
        }
      })
    }
    if (this.sid) {
       // 编辑
      this.initData()
    } else {
      // 公众号列表显示
      this.getAppList()
    }
  },
  methods: {
    // 获取公众号
    getAppList () {
      // type 平台类型：1 公众号 2 小程序,不传查询全部
      platformList({page:1,size:50,type:1 }).then(res => {
        if (res.success) {
          this.appArr = res.data
        }
      })
    },
     // 获取数据
    initData () { 
      queryStore({id: this.sid}).then(res => {
        if (res.success) {
          this.detailData = res.data
          this.detailData.openFlag = Number(res.data.openFlag);
          this.disabled = true
        }
      })
    },

    // 选择公众号平台
    handleSelect (val) {
      this.appArr.forEach(item => {
        if (item.id === val) {
          this.detailData.appId = item.appId
          this.detailData.appName = item.name;
          this.detailData.platform=item.platform;
          this.detailData.wxPlatformId=item.id;
        }
      })
    },
    // 选择分账业务员
    handleSelectS(row) {
      this.detailData.salemanId = row.id
      this.detailData.salemanName = row.name  
    },
    // 选择分账配置
    handleSelectA(row) {
      this.detailData.userConfigId = row.id
      this.detailData.userConfigName = row.name
    },
    // 更换状态
    // ChangeRadio (val) {
    //   this.detailData.status = val
    // },
    // 保存
    handleSubmit (formName) {
       this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          this.detailData.distributorId = this.userId
          if(this.detailData.salemanName!=''&&this.detailData.salemanName!=undefined&&this.detailData.userConfigName==undefined){
             this.$message('请选择分账配置!');
             return
          } 
          
          if(this.detailData.userConfigName==undefined&&this.detailData.userConfigName!=''&&this.detailData.userConfigName!=undefined){
             this.$message('请选择业务员!');
             return
          }
          if (this.sid) {
            // 编辑
            modifyStore(this.detailData).then((res) => {
              if (res.success) {
                this.$message({
                  type: 'success',
                  message: '编辑成功'
                });
                this.$router.push({ name: 'StoreManage' });
              }
            })
          } else { // 新增
            if (this.appArr.length === 0) {
              this.$message({
                type: 'error',
                message: '没有关联的公众号，不可添加！'
              })
              this.$router.push({ name: 'StoreManage' })
            } else {   
              addStore(this.detailData).then(res=>{
                  if (res.success) {
                    this.$message({
                      type: 'success',
                      message: '新增成功'
                    });
                    this.$router.push({ name: 'StoreManage' });
                  }else{
                    this.$message(res.errMessage);
                  }
              })
            }
          }
        }
      });
    },
    // 返回
    handleBack () {
      this.$router.push({ name: 'StoreManage' })
    }
  }
}
</script>

<style scoped="scoped" lang='less'>
   @import url("../../assets/less/variable.less");
  .store-add {
    width: 100%;
  }
  .store-main {
    width: 1200px;
    .store-right {
      width: 1030px;
      .content{
        padding: 24px 40px 0;
        border: 2px solid @bdLightColor;
        border-radius: 8px;
        .store-right-title{
          font-size: 20px;
          padding-bottom: 10px;
          border-bottom: 1px solid @bdLightColor;
          .title{
            display: inline-block;
          }
          .back{
            float:right;
            padding: 10px;
            margin-top: -10px;
            cursor: pointer;
            .iconfont{
              display: inline-block;
              font-size:14px;
            }
          }
        }
        .store-form{
          width:900px;
          margin:40px 0 60px;
          /deep/.el-form{
            .el-form-item__label{
              width:15% !important;
            }
            .el-form-item__content{
              margin-left:15% !important;
              .el-input__inner{
                height: 40px;
                line-height:40px;
              }
              .el-textarea__inner{
                height: 80px;
              }
            }
          }
          .footbtn{
            text-align: center;
            .el-button{
              margin:0 20px;
            }
          }
        }
      }
    }
  }
</style>
