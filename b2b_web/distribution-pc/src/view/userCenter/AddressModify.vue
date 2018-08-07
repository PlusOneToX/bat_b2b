<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-padding-horizontal-lllg rl-bg-white rl-padding-top-default rl-padding-bottom-double">
          <div class="content">
            <div class="addAddress">
              <h6 class="user-right-title">{{$t('P.ModifyShippingAddress')}}</h6>
           
              <div class="add-info rl-padding-left-mid rl-padding-top-mid rl-margin-top-xxxs rl-padding-bottom-xxxs">
                <div class="items rl-clear rl-margin-bottom-xxxs">
                  <span class="rl-fl rl-text-xxss rl-tr rl-margin-right-double">{{$t('UserCenter.Location')}}</span>
                  <div class="item rl-fl rl-relative rl-margin-right-mid">
                    <input class="cursor-pointer trade-input" type="text" readonly="readonly" :placeholder="$t('P.pleaseSelect')" v-model="countryName" @click= "getCountry()">
                    <ul class="selectUl rl-bg-white"  v-show="countryType ===true ">
                      <li @click="getCountryId(item.id,item)" v-for="item in countryList" :key="item.id">
                        <i v-show="$i18n.locale === 'zh' || !item.regionNameEn == true">{{item.regionName}}</i>
                        <i v-show="$i18n.locale === 'en'" class="rl-text-xxs">{{item.regionNameEn}}</i>
                      </li>
                    </ul>
                  </div>
                  <div class="item rl-fl rl-relative rl-margin-right-mid">
                    <input class="cursor-pointer trade-input" type="text" readonly="readonly" :placeholder="$t('P.pleaseSelect')" v-model="provinceName" @click= "getProvince()">
                    <ul class="selectUl rl-bg-white"  v-show="provinceType ===true ">
                      <li @click="getProvinceId(province.id,province)" v-for="province in provinceList" :key="province.id">
                        <i v-show="$i18n.locale === 'zh' || !province.regionNameEn == true">{{province.regionName}}</i>
                        <i v-show="$i18n.locale === 'en'" class="rl-text-xxs">{{province.regionNameEn}}</i>
                      </li>
                    </ul>
                  </div>
                  <div class="item rl-fl rl-relative rl-margin-right-mid" v-show="showCity === true">
                    <input class="cursor-pointer trade-input" type="text" readonly="readonly" :placeholder="$t('P.pleaseSelect')" v-model="cityName" @click= "getCity()">
                    <ul class="selectUl rl-bg-white"  v-show="cityType ===true ">
                      <li @click="getCityId(city.id,city,city.haveNext)" v-for="city in cityList" :key="city.id">
                        <i v-show="$i18n.locale === 'zh' || !city.regionNameEn == true">{{city.regionName}}</i>
                        <i v-show="$i18n.locale === 'en'" class="rl-text-xxs">{{city.regionNameEn}}</i>
                      </li>
                    </ul>
                  </div>
                  <div class="item rl-fl rl-relative" v-show="showArea === true">
                    <input class="cursor-pointer trade-input" type="text" readonly="readonly" :placeholder="$t('P.pleaseSelect')" v-model="areaName" @click= "getArea()">
                    <ul class="selectUl rl-bg-white"  v-show="areaType ===true ">
                      <li @click="getAreaId(area.id,area.regionName)" v-for="area in areaList" :key="area.id">{{area.regionName}}</li>
                    </ul>
                  </div>
                </div>
                <div class="items rl-clear rl-margin-bottom-xxxs">
                  <span class="rl-fl rl-text-xxss rl-tr rl-margin-right-double">{{$t('UserCenter.Address')}}</span>
                  <div class=" rl-fl"><textarea type="text" v-model="address.address" :placeholder="$t('P.PleaseEnter')" class="editAddress-textarea"></textarea></div>
                </div>
                <div class="items rl-clear rl-margin-bottom-xxxs">
                  <span class="rl-fl rl-text-xxss rl-tr rl-margin-right-double">{{$t('UserCenter.ZipCode')}}</span>
                  <div class="search-input rl-fl"><input type="text" v-model="address.zipCode" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
                </div>
                <div class="items rl-clear rl-margin-bottom-xxxs">
                  <span class="rl-fl rl-text-xxss rl-tr rl-margin-right-double">{{$t('UserCenter.RecipientName')}}</span>
                  <div class="search-input rl-fl"><input type="text"  v-model="address.userName" :placeholder="$t('P.PleaseEnter')"></div>
                </div>
                <!--<div class="items rl-clear rl-margin-bottom-xxxs">
                  <span class="rl-fl rl-text-xxss rl-tr rl-margin-right-double">{{$t('UserCenter.Tel')}}</span>
                  <div class="search-input rl-fl"><input type="text" v-model="address.phone" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
                </div>-->
                <div class="items rl-clear rl-margin-bottom-xxxs">
                  <span class="rl-fl rl-text-xxss rl-tr rl-margin-right-double">{{$t('UserCenter.Phone')}}</span>
                  <div class="search-input rl-fl"><input type="text" v-model="address.phone" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
                </div>
                <div class="items rl-clear rl-margin-bottom-xxxs">
                   <span  class="rl-fl rl-text-xxss rl-tr rl-margin-right-double" :class="{'en': ($i18n.locale === 'en')}">是否默认</span>
                   <el-radio-group  v-model:trim="address.defaultFlag" class="adress-el-radio">
                      <el-radio :label="0" value="0">否</el-radio>
                      <el-radio :label="1" value="1">是</el-radio>
                      
                    </el-radio-group>
                </div>
                <div class="button">
                  <span @click="modifyAddress" class="cursor-pointer rl-bg-blue-xs rl-text-white">{{$t('P.Save')}}</span>
                  <span @click="goBack" class="cursor-pointer rl-bg-gray-xx rl-text-white rl-margin-left-xxxxs">{{$t('P.Back')}}</span>
                </div>
              </div>
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
import {region} from '@/assets/js/common.js'
import GD from '@/assets/js/globalData'
// liu--
import { addressApi,addressDefault} from '@/apiService/api'
export default {
  name: 'AddressModify',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      value: this.$route.query.value,
      address: {
        address: '',
        zipCode: '',
        userName: '',
        phone: '',
        phone: '',
        provinceId: '',
        cityId: '',
        districtId: '',
        defaultFlag:0,
      },
      pageNo: 1,
      pageSize: 1000,
      provinceId: '', // 省市区上级id
      cityId: '',
      townId: '',
      countryName:'',
      countryType:false,
      provinceName: '',
      provinceType: false,
      cityName: '',
      cityType: false,
      showCity: true,
      areaName: '',
      areaType: false,
      showArea: true,
      countryList:{},
      provinceList: {},
      cityList: {},
      areaList: {},
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB', // 语种
      userId:'',
    }
  },
   created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    var list = JSON.parse(this.$route.query.address)
    if (list instanceof Object) {
      this.address = list
      this.countryId=list.countryId;
      this.provinceId=list.provinceId;
      this.townId=list.districtId;
      this.cityId=list.cityId;
    }
    var id = window.localStorage.getItem("userId");
    this.userId = id;
    this.addressShow()
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    // 点击空白处隐藏下拉列表
    hideAll () {
      this.provinceType = false
      this.cityType = false
      this.areaType = false
    },
    // 返回上一页
    goBack () {
      this.$router.go(-1)
    },
     // liu--获取区域列表type:(1:国家，2：省，3：市，4：区)
    regionListFun(parentId,id,type){
        region(parentId).then(res=>{
           console.log('====',res);
           let list=res.data.list;
           if(type==1){
                this.countryList = list;
            }else if(type==2){
                this.provinceList =list;
            }else if(type==3){
                this.cityList = list;
            }else if(type==4){
                this.areaList =list;
            }
           list.forEach((item) => {
                if (item.id === id) {
                    if(type==1){
                        this.countryName = item.regionName;   
                    }else if(type==2){
                        this.provinceName = item.regionName;
                        console.log('省：',res.data.list);  
                    }else if(type==3){
                        this.cityName  = item.regionName;
                        console.log('市：',res.data.list);  
                    }else if(type==4){
                        this.townId = item.id
                        this.areaName= item.regionName;
                        console.log('区：',res.data.list);  
                    } 
                  
                }
              });
        })
    },
     // 获取国家名称
      getCountryId (id, item) { 
        this.countryId = id;
        this.countryType = false;
        this.provinceList = [];
        this.cityList = [];
        this.areaList = [];
        this.provinceName ='';
        this.provinceId='';
        this.cityName = "";
        this.areaName = "";
        this.cityId = "";
        this.townId = "";
        this.showCity = true;
        this.showArea = false;
        this.provinceType = false;
        if (this.$i18n.locale === "zh" ) {
        this.countryName = item.regionName;
      } else {
        this.countryName = item.regionNameEn;
      }
        // this.regionListFun(37,-1,2)
      },
      getCountry(){
          // this.regionListFun(0,-1,1);
          this.countryType = !this.countryType;
          this.provinceType = false;
          this.cityType = false;
          this.areaType = false;
      },
      
    // 获取省级列表
    getProvince () {
      this.provinceList = [];
      this.regionListFun(this.countryId,-1,2)
      this.provinceType = !this.provinceType;
      this.cityType = false;
      this.areaType = false;
    },
    // 获取省级ID
    getProvinceId (cityCode, province) {
      this.cityList = "";
      this.areaList = "";
      this.cityName = "";
      this.areaName = "";
      this.cityId = "";
      this.townId = "";
      this.showCity = true;
      this.showArea = false;
      this.provinceType = false;
      if (this.$i18n.locale === "zh" ) {
        this.provinceName = province.regionName;
      } else {
        this.provinceName = province.regionNameEn;
      }
      this.provinceId = cityCode;
      if (province.haveNext == 1) {
            this.regionListFun(cityCode,-1,3) 
            this.cityType = true;
            // this.townId = this.areaList[1].id;
      }else {
        this.townId = 0;
      }
    },
    // 获取市级列表
    getCity () {
      if (this.provinceName) {
        this.cityType = !this.cityType
        this.provinceType = false
        this.areaType = false
      } else {
        if (this.$i18n.locale === 'zh') {
          this.$message.error('请选择省市区')
        } else { this.$message.error('Please choose province, city and district.') }
      }
      
    },
    // 获取市级ID
    getCityId (cityCode, city, haveNext) {     
      this.areaList = "";
      this.cityType = false;
      if (this.$i18n.locale === "zh" || !city.regionNameEn === true) {
        this.cityName = city.regionName;
      } else {
        this.cityName = city.regionNameEn;
      }
      this.cityId = cityCode;
      this.areaName = "";
      if (haveNext == 1) {
        
         this.regionListFun(cityCode,-1,4) 
         this.showArea = true;
         this.areaType = true;
        
      }else {
        this.showArea = false;
        this.townId = 0;
      }
        



    },
    // 获取区级列表
    getArea () {
      if (this.cityName) {
        this.areaType = !this.areaType
        this.provinceType = false
        this.cityType = false
      } else {
        if (this.$i18n.locale === 'zh') {
          this.$message.error('请选择省市区')
        } else { this.$message.error('Please choose province, city and district.') }
      }
    },
    // 获取区级ID
    getAreaId (cityCode, name) {
      this.areaType = false
      this.areaName = name
      this.townId = cityCode
    },
    // 提交修改地址
    modifyAddress () {
      if (this.countryId==37&&this.cityId === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请选择省市区')
        } else { this.$message.warning('Please choose province, city and district.') }
        return false
      }
      if (this.address.address === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入详细地址')
        } else { this.$message.warning('Please enter detailed address.') }
        return false
      }
      if (this.address.userName === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入收货人姓名')
        } else { this.$message.warning('Please enter recipient name.') }
        return false
      }
      if (this.address.phone === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入手机号')
        } else { this.$message.warning('Please enter mobile phone number.') }
        return false
      }
       var json ={
          distributorId: this.userId,
          addressType: 2,   //地址类型 1.公司地址 2.收货地址（固定传2
          cityId: this.cityId,
          districtId: this.townId,
          countryId:this.countryId,  //国家id
          provinceId: this.provinceId,
          defaultFlag: this.address.defaultFlag,
          id: this.address.id,
          address: this.address.address,
          phone:this.address.phone,
          userName: this.address.userName,
          zipCode: this.address.zipCode
      }
      
      addressApi('PUT', json).then(res => {
        if (res.success) {
          if (this.$i18n.locale === 'zh') {
            this.$message.success('修改收货地址成功')
          } else { this.$message.success('Modify shipping address successfully.') }
          if (this.value === 0) {
            this.$router.go(-1)
          } else {
            this.$router.push({name: 'Address'})
          }
        } else {
  
          this.$message(res.errMessage);
        }
      })
    },
    // 显示省市区地址
    addressShow () {
      if (this.address.districtId === 0) {
        this.showArea = false
      }
      
      this.regionListFun(0,this.address.countryId,1);
      if (this.address.provinceId) {
        this.regionListFun(37,this.address.provinceId,2);
      }
      if (this.address.cityId) {
        this.regionListFun(this.address.provinceId,this.address.cityId,3);
      }
      if (this.address.districtId) {
        this.regionListFun(this.address.cityId,this.address.districtId,4);
      }
    }
  },
 
}
</script>

<style scoped="scoped" lang='less'>
.adress-el-radio{
  margin-top:8px;
}
.editAddress-textarea{
     width: 298px;
     height:60px;
     border: 1px solid #ccc;
     border-radius:5px;
     padding:10px 15px;
}
.user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid #ccc;
    font-size: 20px;
  }
  .user{width: 100%;}
  .user-main{width: 1190px}
  .user-right {
    width: 960px;
    .content{
      .addAddress{
        .add{
          span{
            display: block;
            width: 180px;
            height: 40px;
            line-height: 40px;
            border-radius: 5px;
          }
        }
        .add-info{
          .items{
            span{
              display: block;
              width: 120px;
              line-height: 30px;
            }
            .search-input{
              width: 328px;
              height: 28px;
              border:1px solid #ccc;
              input{
                padding-left: 20px;
                padding-top: 5px;
                width: 215px;
                border:0;
              }
            }
            .item{
              input.trade-input{
                padding-left: 10px;
                width: 100px;
                height: 30px;
                line-height: 30px;
                box-sizing: border-box;
                border: 1px solid #ccc;
                font-size: 13px;
                background: url("../../assets/images/selectUl.png") no-repeat 88px center;
                border-radius: 5px;
                box-sizing: border-box;
                overflow: hidden;
                text-overflow: ellipsis;
              }
              .selectUl{
                position: absolute;
                top:30px;
                left:0;
                z-index: 11;
                width: 85px;
                box-sizing: border-box;
                border: 1px solid #ccc;
                max-height: 220px;
                overflow-y: auto;
                li{
                  padding-left: 10px;
                  line-height: 30px;
                  cursor: pointer;
                  box-sizing: border-box;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                }
                li:hover{
                  background-color: #00c9dc;
                  color: #fff;
                }
              }
            }
          }
          .button{
            margin-left: 115px;
            width: 276px;
            span{
              display: inline-block;
              width: 110px;
              height: 35px;
              line-height: 35px;
              text-align: center;
              border: 1px solid #ccc;
              border-radius: 5px;
            }
          }
        }
      }
    }
  }
</style>
