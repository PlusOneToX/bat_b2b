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
          <div class="content">
            <h6 class="user-right-title">{{$t('UserCenter.Address')}}</h6>
            <div class="addAddress">
              <!-- <div class="add">
                <span
                  @click="showAdd"
                  class="rl-bg-blue-xs rl-text-white rl-tc rl-text-xxss cursor-pointer"
                >{{$t('P.AddShippingAddress')}}</span>
              </div>
              <div
                class="add-info rl-padding-left-mid rl-padding-top-mid rl-bd-black-sm rl-margin-top-xxxs rl-padding-bottom-xxxs"
                v-show="showAddAddress === true"
              >-->
              <div class="add-info rl-padding-top-default">
                <div class="items rl-clear">
                  <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                    <em class="red">*</em>
                    {{$t('UserCenter.RecipientName')}}
                  </span>
                  <div class="search-input rl-fl">
                    <input type="text" v-model="userName" :placeholder="$t('P.PleaseEnter')" />
                  </div>
                </div>
                <div class="items rl-clear">
                  <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                    <em class="red">*</em>
                    {{$t('UserCenter.Phone')}}
                  </span>
                  <div class="search-input rl-fl">
                    <input
                      type="text"
                      v-model="mobile"
                      :placeholder="$t('P.PleaseEnter')"
                      onkeyup="this.value=this.value.replace(/\D/g,'')"
                    />
                  </div>
                </div>
                <!--<div class="items rl-clear">
                  <span
                    class="rl-fl item-left"
                    :class="{'en': ($i18n.locale === 'en')}"
                  >{{$t('UserCenter.Tel')}}</span>
                  <div class="search-input rl-fl">
                    <input
                      type="text"
                      v-model="phone"
                      :placeholder="$t('P.PleaseEnter')"
                      onkeyup="this.value=this.value.replace(/\D/g,'')"
                    />
                  </div>
                </div>-->
                <!--收货地址-->
                <div class="items rl-clear">
                <!--<div class="disNone" v-on:click="hideAll"></div>-->
                <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                  <em class="red">*</em>
                  {{$t('Register.Country')}}
                </span>
                <!--国家-->
                <div class="item rl-fl rl-relative rl-margin-right-mid">
                  <input
                    class="cursor-pointer trade-input"
                    type="text"
                    readonly="readonly"
                    :placeholder="$t('P.pleaseSelect')"
                    v-model="countryName"
                    @click="getCountry()"
                  />
                  <ul class="selectUl rl-bg-white" v-show="countryType ===true ">
                    <li
                      @click="getCountryId(country.id,($i18n.locale === 'zh' || !country.regionNameEn == true) ? country.regionName:country.regionNameEn)"
                      v-for="country in countryList"
                      :key="country.id"
                    >{{($i18n.locale === 'zh' || !country.regionNameEn == true) ? country.regionName:country.regionNameEn}}</li>
                  </ul>
                </div>
              </div>
                <div class="items rl-clear" v-if="countryId==37">
                  <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                    <em class="red">*</em>
                    {{$t('UserCenter.Address')}}
                  </span>
                  <div class="item rl-fl rl-relative rl-margin-right-mid">
                    <input
                      class="cursor-pointer trade-input"
                      type="text"
                      readonly="readonly"
                      :placeholder="$t('P.pleaseSelect')"
                      v-model="provinceName"
                      @click="getProvince()"
                    />
                    <ul class="selectUl rl-bg-white" v-if="provinceType ===true ">
                      <li
                        @click="getProvinceId(province.id,province)"
                        v-for="province in provinceList"
                        :key="province.id"
                      >
                        <i
                          v-show="$i18n.locale === 'zh' || !province.regionNameEn == true"
                        >{{province.regionName}}</i>
                        <i
                          v-show="$i18n.locale === 'en'"
                          class="rl-text-xxs"
                        >{{province.regionNameEn}}</i>
                      </li>
                    </ul>
                  </div>
                  <div
                    class="item rl-fl rl-relative rl-margin-right-mid"
                    v-show="showCity === true"
                  >
                    <input
                      class="cursor-pointer trade-input"
                      type="text"
                      readonly="readonly"
                      :placeholder="$t('P.pleaseSelect')"
                      v-model="cityName"
                      @click="getCity()"
                    />
                    <ul class="selectUl rl-bg-white" v-if="cityType ===true">
                      <li
                        @click="getCityId(city.id,city,city.haveNext)"
                        v-for="city in cityList"
                        :key="city.id"
                      >
                        <i
                          v-show="$i18n.locale === 'zh' || !city.regionNameEn == true"
                        >{{city.regionName}}</i>
                        <i v-show="$i18n.locale === 'en'" class="rl-text-xxs">{{city.regionNameEn}}</i>
                      </li>
                    </ul> 
                  </div>
                  <div class="item rl-fl rl-relative" v-show="showArea === true">
                    <input
                      class="cursor-pointer trade-input"
                      type="text"
                      readonly="readonly"
                      :placeholder="$t('P.pleaseSelect')"
                      v-model="areaName"
                      @click="getArea()"
                    />
                    <ul class="selectUl rl-bg-white" v-if="areaType ===true ">
                      <li
                        @click="getAreaId(area.id,area.regionName)"
                        v-for="area in areaList"
                        :key="area.id"
                      >{{area.regionName}}</li>
                    </ul>
                  </div>
                </div>
                <div class="items rl-clear">
                  <span class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}">
                    <template v-if="countryId!=37">
                    <em class="red">*</em>
                    <!-- {{$t('UserCenter.Address')}} -->
                    详情地址
                    </template>
                  </span>
                  
                  <div class="search-input large rl-fl">
                    <!-- <input type="text" v-model="address" :placeholder="$t('P.PleaseEnter')" /> -->
                    
                    <textarea :placeholder="$t('P.PleaseEnter')" v-model="address"></textarea>
                  </div>
                </div>
                <div class="items rl-clear">
                  <span
                    class="rl-fl item-left"
                    :class="{'en': ($i18n.locale === 'en')}"
                  >{{$t('UserCenter.ZipCode')}}</span>
                  <div class="search-input rl-fl">
                    <input
                      type="text"
                      v-model="zipCode"
                      :placeholder="$t('P.PleaseEnter')"
                      onkeyup="this.value=this.value.replace(/\D/g,'')"
                    />
                  </div>
                </div>
                <div class="items rl-clear">
                   <span  class="rl-fl item-left" :class="{'en': ($i18n.locale === 'en')}"><em class="red">*</em>是否默认</span>
                   <el-radio-group  v-model:trim="defaultFlag" class="adress-el-radio">
                      <el-radio :label="0" value="0">否</el-radio>
                      <el-radio :label="1" value="1">是</el-radio>
                      
                    </el-radio-group>
                </div>

                <div class="button rl-clear">
                  <span
                    @click="addAddress('POST')"
                    class="cursor-pointer rl-fl rl-bg-blue-xs"
                  >{{$t('P.Save')}}{{$t('UserCenter.Address')}}</span>
                  <span
                    @click="clearAddress"
                    class="cursor-pointer rl-fl rl-bg-gray-sm rl-margin-left-default"
                  >{{$t('UserCenter.Empty')}}</span>
                </div>
              </div>
            </div>
            <div class="table rl-margin-top-default">
              <table>
                <tr>
                  <th>{{$t('UserCenter.RecipientName')}}</th>
                  <th>{{$t('UserCenter.Address')}}</th>
                  <!--<th>{{$t('UserCenter.Tel')}}</th>-->
                  <th>{{$t('UserCenter.Phone')}}</th>
                  <th>{{$t('UserCenter.ZipCode')}}</th>
                  <th>{{$t('P.Default')}}</th>
                  <th>{{$t('UserCenter.Operation')}}</th>
                </tr>
                <tr v-for="address in addressLists" :key="address.id">
                  <td>{{address.userName}}</td>
                  <td>
                    <div
                      class="rl-text-xxs"
                    >{{address.provinceName}}{{address.cityName}}{{address.areaName}}</div>
                    <div class="rl-text-xxs">{{address.address}}</div>
                  </td>
                  <!-- <td>{{address.phone}}</td>-->
                  <td>{{address.phone}}</td>
                  <td>{{address.zipCode}}</td>
                  <td>{{getDefaultStatus(address.defaultFlag)}}</td>
                  <td>
                    <div class="state">
                      <span @click="modifyAddress(address)" class="rl-tc cursor-pointer">
                        <i class="iconfont icon-edit"></i>
                      </span>
                      <span @click="deleteAddress(address.id)" class="rl-tc cursor-pointer">
                        <i class="iconfont icon-wrong"></i>
                      </span>
                      <span
                        v-if="address.defaultFlag === 0"
                        @click="setDefault(address.id,address.defaultFlag)"
                        class="rl-tc cursor-pointer"
                      >{{$t('UserCenter.MakeDefault')}}</span>
                    </div>
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { region } from "@/assets/js/common.js";
import Vue from "vue";
import GD from "@/assets/js/globalData";
// liu--
import { addressList,addressApi,addressDefault} from '@/apiService/api'
export default {
  name: "Address",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      userState: 2,
      userId: "",
      pageNo: 1,
      pageSize: 1000,
      defaultFlag:0,  //是否默认
      countryId:'', 
      provinceId: "", // 省市区上级id
      cityId: "",
      townId: "",
      countryName:'',
      countryType:false, 
      provinceName: "",
      provinceType: false,
      cityName: "",
      cityType: false,
      showCity: false,
      areaName: "",
      areaType: false,
      showArea: false,
      countryList:{},
      provinceList: {},
      cityList: {},
      areaList: {},
      address: "",
      zipCode: "",
      userName: "",
      mobile: "",
      phone: "",
      showAddAddress: false,
      addressLists: [], // 地址列表
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种

    };
  },
   created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    var id = window.localStorage.getItem("userId");
    this.userId = id;
   
    
  },
  mounted(){
     this.getAddressList();
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    getDefaultStatus(row) {
      // 默认地址状态
      switch (row) {
        case 0:
          return this.$t("P.No");
        case 1:
          return this.$t("P.Yes");
      }
    },
    // 地址保存
    showAdd() {
      this.showAddAddress = !this.showAddAddress;
    },
    // 清空
    clearAddress() {
      this.countryName = "";
      this.provinceName = "";
      this.cityName = "";
      this.areaName = "";
      this.address = "";
      this.zipCode = "";
      this.mobile = "";
      this.phone = "";
      this.userName = "";
    },
    // 点击空白处隐藏下拉列表
    hideAll() {
      this.provinceType = false;
      this.cityType = false;
      this.areaType = false;
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
                console.log('省：',res.data.list);  
            }else if(type==3){
                this.cityList = list;
                console.log('市：',res.data.list);  
            }else if(type==4){
                this.areaList =list;
                console.log('区：',res.data.list);  
            }
        })
    },

   // 获取国家名称
      getCountryId (id, name) {
        this.countryName = name;
        this.countryId = id;
        this.countryType = false;
        this.regionListFun(id,-1,2)
        this.areaType = false;
        this.showCity=false;
        this.showArea=false;
        this.provinceName ='';
        this.provinceId='';
        this.cityList = "";
        this.areaList = "";
        this.cityName = "";
        this.areaName = "";
        this.cityId = "";
        this.townId = "";
      },
      getCountry(){
          this.regionListFun(0,-1,1);
          this.countryType = !this.countryType;
          this.provinceType = false;
          this.cityType = false;
          
      },
      

    // 获取省级列表
    getProvince() {
      this.provinceList = [];
      this.regionListFun(this.countryId,-1,2)
      this.provinceType = !this.provinceType;
      this.cityType = false;
      this.areaType = false;
      
    },
    // 获取省级ID
    getProvinceId(cityCode, province) {
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
    getCity() {
      if (this.provinceName) {
        this.cityType = !this.cityType;
        this.provinceType = false;
        this.areaType = false;
      } else {
        if (this.$i18n.locale === "zh") {
          this.$message.error("请选择省市区");
        } else {
          this.$message.error("Please choose province, city and district.");
        }
      }
    },
    // 获取市级ID
    getCityId(cityCode, city, haveNext) {
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
    getArea() {
      if (this.cityName) {
        this.areaType = !this.areaType;
        this.provinceType = false;
        this.cityType = false;
      } else {
        if (this.$i18n.locale === "zh") {
          this.$message.error("请选择省市区");
        } else {
          this.$message.error("Please choose province, city and district.");
        }
      }
    },
    // 获取区级ID
    getAreaId(cityCode, name) {
      this.areaType = false;
      this.areaName = name;
      this.townId = cityCode;
    },
    // 添加收货地址
    addAddress() {
      let that=this;
      var pattern = /^1(3|4|5|6|7|8|9)\d{9}$/; // 手机号
      if (this.countryId==37&&this.cityId === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请选择省市区");
        } else {
          this.$message.warning("Please choose province, city and district.");
        }
        return false;
      }
      if (this.countryId==37&&this.address === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入详细地址");
        } else {
          this.$message.warning("Please enter detailed address.");
        }
        return false;
      }
      if (this.userName === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入收货人姓名");
        } else {
          this.$message.warning("Please enter recipient name.");
        }
        return false;
      }
      if (this.mobile === "") {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("请输入手机号");
        } else {
          this.$message.warning("Please enter mobile phone number.");
        }
        return false;
      }
      this.mobile = this.mobile.replace(/\D/g, "");
      if (this.$i18n.locale === "zh") {
        if (!pattern.test(this.mobile)) {
          this.$message.warning("请输入正确的手机号");
          return false;
        }
      }
     
      var json ={
        address: this.address,
        addressType: 2,   //地址类型 1.公司地址 2.收货地址（固定传2
        cityId: this.cityId,
        countryId: this.countryId,  //国家id
        defaultFlag: this.defaultFlag,
        distributorId: this.userId,
        districtId: this.townId,
        phone:this.mobile,
        provinceId: this.provinceId,
        userName: this.userName,
        zipCode: this.zipCode
      }
     addressApi('POST', json).then((res) => {
        if (res.success) {
          if (this.$i18n.locale === "zh") {
            this.$message.success("添加收货地址成功");
          } else {
            this.$message.success("Add shipping address successfully.");
          }
          this.getAddressList();
          this.clearAddress(); // 清空
        } else  {
        
          that.$message(res.errMessage);
        }
      });
    },
    // 新收货地址列表
    getAddressList(){
        let that=this;
        let params={
          id:this.userId,  //分销商id
          page: this.pageNo, 
          size: this.pageSize,
        };
        addressList(params).then(res=>{
           console.log('新收货地址：',res.data);
           if(res.success){
             that.addressLists =res.data.list;
             that.addressLists.forEach((item) => {
              Vue.set(item, "provinceName", "");
              Vue.set(item, "cityName", "");
              Vue.set(item, "areaName", "");
              let countryId='37';  //item.countryId
              region(countryId).then((res) => {
                  if (res.success) {
                    let provinceList = res.data.list;
                    provinceList.forEach((goods) => {
                      if (goods.id == item.provinceId) {
                        item.provinceName = goods.regionName;
                         region(item.provinceId).then((res) => {
                            if (res.success) {
                              this.cityList = res.data.list;
                              this.cityList.forEach((goods) => {
                                if (goods.id == item.cityId) {
                                  item.cityName = goods.regionName;
                                  region(item.cityId).then((res) => {
                                      if (res.success) {
                                        this.areaList = res.data.list;
                                        this.areaList.forEach((goods) => {
                                          if (goods.id == item.districtId) {
                                            item.areaName = goods.regionName;
                                          }
                                        });
                                      }
                                    });
                                }
                              });
                            }
                          });
                      }
                    });
                  }
                });
            });
            console.log(that.addressLists)
           }else{
            this.$message(res.errMessage);
           }
        }).catch(ERR=>{

        })
    },
   
    // 删除地址
    deleteAddress(id) {
      let that=this;
      let info = "";
      if (this.$i18n.locale === "zh") {
        info = "此操作将删除该地址, 是否继续?";
      } else {
        info = "The address will be deleted. Do you want to continue?";
      }
      this.$confirm(info, this.$t("P.Prompt"), {
        confirmButtonText: this.$t("P.OK"),
        cancelButtonText: this.$t("P.Cancel"),
        type: "warning",
      })
        .then(() => {
            addressApi('DELETE', { id: id }).then((res) => {
              if (res.success) {
                that.$message('删除成功！');
                this.getAddressList();
              } else {
              
                that.$message(res.errMessage);
              }
            });
        })
        .catch(() => {
          if (this.$i18n.locale === "zh") {
            this.$message.info("已取消退出");
          } else {
            this.$message.info("Withdrawal canceled");
          }
        });
    },
    // 设置默认地址
    setDefault(id, fault) {
      let that=this;
      if (fault === 1) {
        if (this.$i18n.locale === "zh") {
          this.$message.warning("该地址为默认地址");
        } else {
          this.$message.warning("The address is by default.");
        }
      } else {
          addressDefault({ id: id }).then((res) => {
            if (res.success) {
              if (this.$i18n.locale === "zh") {
                this.$message.success("设置默认地址成功");
              } else {
                this.$message.success("Set default address successfully.");
              }
              this.getAddressList();
            } else {
             
              that.$message(res.errMessage);
            }
          });
      }
    },
    // 修改地址
    modifyAddress(address) {
      let data = JSON.stringify(address);
      this.$router.push({ name: "AddressModify", query: { address: data } });
    },
  },
 
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.adress-el-radio{
  margin-top:8px;
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
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    .addAddress {
      .add {
        span {
          display: block;
          width: 100px;
          height: 40px;
          line-height: 40px;
          border-radius: 5px;
        }
      }
      .add-info {
        .items {
          margin-bottom: 15px;
          .item-left {
            margin-right: 15px;
            display: block;
            width: 60px;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            &.en {
              width: 115px;
            }
          }
          .red {
            color: @red;
          }
          .search-input {
            width: 130px;
            height: 28px;
            line-height: 28px;
            border: 1px solid @bdLightColor;
            input {
              padding-left: 10px;
              width: 100%;
              border: 0;
              box-sizing: border-box;
            }
            &.large {
              width: 420px;
              height: 66px;
              border: none;
              textarea {
                padding: 5px 10px;
                width: 100%;
                height: 100%;
                border: 1px solid @bdLightColor;
                box-sizing: border-box;
              }
            }
          }
          .item {
            input.trade-input {
              padding-left: 10px;
              width: 115px;
              height: 30px;
              line-height: 30px;
              box-sizing: border-box;
              font-size: 12px;
              color: @lighterBlack;
              background: url("../../assets/images/selectUl.png") no-repeat
                100px center;
              box-sizing: border-box;
              overflow: hidden;
              text-overflow: ellipsis;
              border: 1px solid @bdLightColor;
            }
            .selectUl {
              position: absolute;
              top: 30px;
              left: 0;
              z-index: 11;
              width: 85px;
              box-sizing: border-box;
              max-height: 220px;
              overflow-y: auto;
              border: 1px solid @bdLightColor;
              li {
                padding-left: 10px;
                line-height: 30px;
                cursor: pointer;
                box-sizing: border-box;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              li:hover {
                background: @blue;
                color: @white;
              }
            }
          }
        }
        .button {
          margin-left: 130px;
          span {
            display: block;
            padding: 0 20px;
            min-width: 60px;
            height: 30px;
            line-height: 30px;
            text-align: center;
            color: @white;
            border-radius: 4px;
            box-sizing: border-box;
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
          &:hover {
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
          }
        }
        .state {
          span {
            padding: 0 10px;
            font-size: 12px;
            color: @blue;
            .iconfont {
              font-size: 12px;
              color: @lighterBlack;
            }
            & + span {
              border-left: 1px solid @bdLighterColor;
            }
            &:hover {
              opacity: 0.6;
            }
          }
        }
      }
    }
  }
}
</style>
