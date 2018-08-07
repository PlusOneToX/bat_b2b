<template>
  <div class="user rl-margin-zero">
    <!--公共头部-->
    <Header :userState="userState" @reloadFLang="fLangChange"></Header>
    <!--主内容-->
    <div class="user-main rl-clear rl-margin-zero">
      <!--公共左边-->
      <Left></Left>
      <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
        <div class="content register-content">
          <div class="info-tab rl-clear">
            <span
              @click="toBasic"
              class="rl-fl rl-tc cursor-pointer"
              :class="{'active': tabs === 'basic'}"
            >{{$t('Register.BasicInformation')}}</span>
            <span
              @click="toAccount"
              class="rl-fl rl-tc cursor-pointer"
              :class="{'active': tabs === 'account'}"
            >{{$t('Register.AccountInformation')}}</span>
            <span
              @click="toProtocol"
              class="rl-fl rl-tc cursor-pointer"
              :class="{'active': tabs === 'protocol'}"
            >{{$t('UserCenter.ContractText')}}</span>
          </div>
          <div class="info rl-margin-zero">
            <div class="info-base rl-margin-bottom-double" v-show="tabs=='basic'">
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.Username')}}
                </span>
                <div class="enterInput mini rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.name"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.LoginMobile')}}
                </span>
                <div class="enterInput mini rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.phone"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.CustomerName')}}
                </span>
                <div class="enterInput mini rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.companyName"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.BusinessLicense')}}
                </span>
                <div class="enterInput mini rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.certNo"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <!--账户操作人-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.Contact')}}
                </span>
                <div class="enterInput mini rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.userName"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <!-- <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr"><em class="red">*</em>{{$t('Register.Gerder')}}</span>
                <div class="checkedInput rl-fl">
                  <div class="rl-fl rl-margin-left-default" v-for="item in sexList" :key="item.id"><span class="rl-margin-right-mid">{{item.text}}</span><span @click="getSex(item.index)" name="showpri" checked="checked" class="chenked" :class="{'haschecked':(item.index+1) === userInfos.sex}"></span></div>
                </div>
              </div>-->
              <!--账户操作人联系方式--> 
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.ContactPhone')}}
                </span>
                <div class="enterInput mini rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.phone"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              
              <div class="item rl-clear">
                <!--<div class="disNone" v-on:click="hideAll"></div>-->
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.Country')}}
                </span>
                <!--国家-->
                <div class="enterInput items rl-fl rl-relative">
                  <input
                    class="cursor-pointer trade-input"
                    type="text"
                    readonly="readonly"
                    :placeholder="$t('P.pleaseSelect')"
                    v-model="userInfos.countryName"
                    @click="getCountry()"
                  />
                  <ul class="selectUl rl-bg-white" v-show="countryType ===true ">
                    <li
                      @click="getCountryId(country.id,($i18n.locale === 'zh' || !country.countryNameEn == true) ? country.countryName:country.countryNameEn)"
                      v-for="country in countryList"
                      :key="country.id"
                    >{{($i18n.locale === 'zh' || !country.countryNameEn == true) ? country.countryName:country.countryNameEn}}</li>
                  </ul>
                </div>
              </div>
              <div
                class="item rl-clear rl-margin-bottom-xxxs"
                v-if="this.userInfos.countryId === 37"
              ><!-- 37: 国内 -->
                <!--<div class="disNone" v-on:click="hideAll"></div>-->
                <span class="enterLeft rl-fl rl-text-xxss rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.ProvincesCity')}}
                </span>
                <!--省-->
                <div class="items rl-fl rl-relative rl-margin-right-mid" style="display: flex;align-items: center; margin-left:15px;">
                    <div class="items rl-fl rl-relative rl-margin-right-mid">
                      <input
                        class="cursor-pointer trade-input"
                        type="text"
                        readonly="readonly"
                        :placeholder="$t('P.pleaseSelect')"
                        v-model="userInfos.provinceName"
                      />
                      <ul class="selectUl rl-bg-white" v-show="provinceType ===true">
                        <li
                          @click="getProvinceId(province.id,province.regionName)"
                          v-for="province in provinceList"
                          :key="province.id"
                        >{{province.regionName}}</li>
                      </ul>
                    </div>
                    <!--市-->
                    <div class="items rl-fl rl-relative rl-margin-right-mid" v-show="showCity === true">
                      <input
                        class="cursor-pointer trade-input"
                        type="text"
                        readonly="readonly"
                        :placeholder="$t('P.pleaseSelect')"
                        v-model="userInfos.cityName"
                      />
                      <ul class="selectUl rl-bg-white" v-show="cityType ===true ">
                        <li
                          @click="getCityId(city.id,city.regionName,city.haveNext)"
                          v-for="city in cityList"
                          :key="city.id"
                        >{{city.regionName}}</li>
                      </ul>
                    </div>
                    <!--区-->
                    <div class="items rl-fl rl-relative" v-show="showArea === true">
                      <input
                        class="cursor-pointer trade-input"
                        type="text"
                        readonly="readonly"
                        :placeholder="$t('P.pleaseSelect')"
                        v-model="userInfos.areaName"
                      />
                      <ul class="selectUl rl-bg-white" v-show="areaType ===true">
                        <li
                          @click="getAreaId(area.id,area.regionName)"
                          v-for="area in areaList"
                          :key="area.id"
                        >{{area.regionName}}</li>
                      </ul>
                    </div>
                    </div>
                 </div>
              <!--客户联系地址-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.ContactAddress')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.address"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <!--邮箱-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">{{$t('Register.EmailAddress')}}</span>
                <div class="enterInput rl-fl">
                  <input class="common-input" type="text" v-model="userInfos.email" readonly />
                </div>
              </div>
               <!--邮编-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">{{$t('Register.ZipCode')}}</span>
                <div class="enterInput rl-fl">
                  <input class="common-input" type="text" v-model="userInfos.zipCode" readonly />
                </div>
              </div>
              <!--公司类型-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.CompanyType')}}
                </span>
                <div class="checkedInput rl-fl">
                  <div class="rl-fl" v-for="item in companyTypeList" :key="item.id">
                    <!--                    <span @click="getIsTaxpayer(item.index)" name="showpri" checked="checked" class="chenked" :class="{'haschecked':item.index === userInfos.companyType}"></span>-->
                    <span
                      name="showpri"
                      checked="checked"
                      class="chenked"
                      :class="{'haschecked':item.index === userInfos.companyType}"
                    ></span>
                    <span class="rl-margin-right-double">{{item.text}}</span>
                  </div>
                </div>
              </div>
              <!--税票类型-->
              <div class="item rl-clear z-index" v-if="this.userInfos.countryId === 1">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('Register.TaxType')}}
                </span>
                <div class="checkedInput rl-fl">
                  <div class="rl-fl" v-for="item in ticketTypeList" :key="item.id">
                    <!--                    <span @click="getIsIndustrial(item.index)" name="showpri" checked="checked" class="chenked" :class="{'haschecked':item.index === userInfos.taxType}"></span>-->
                    <span
                      name="showpri"
                      checked="checked"
                      class="chenked"
                      :class="{'haschecked':item.index === financial.taxType}"
                    ></span>
                    <span class="rl-margin-right-mid">{{item.text}}</span>
                  </div>
                </div>
              </div>
              <!--付款期-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="red">*</em>
                  {{$t('UserCenter.PaymentTerms')}}
                </span>
                <div class="enterInput rl-fl">
                  <input class="common-input" type="text" v-model="financial.trade" readonly />
                </div>
              </div>
            </div>



            <div class="info-base rl-margin-bottom-double" v-show="tabs=='account'">
              <div class="info-title rl-padding-top-xxxs">1.{{$t('Register.BankInformation')}}</div>
              <!--银行账户名-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="rl-text-red-sm rl-margin-right-xxxxs">*</em>
                  {{$t('Register.BankAccountName')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.bankAccountName"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <!--开户行-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="rl-text-red-sm rl-margin-right-xxxxs">*</em>
                  {{$t('Register.BankDeposit')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.bankDepositName"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <!--银行账户-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="rl-text-red-sm rl-margin-right-xxxxs">*</em>
                  {{$t('Register.BankAccount')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.bankAccount"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>
              <!--<div
                class="info-title rl-padding-top-lllg"
              >2.{{$t('Register.ReconciliationInformation')}}</div>-->
              <!--对账人姓名-->
             <!-- <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="rl-text-red-sm rl-margin-right-xxxxs">*</em>
                  {{$t('Register.ReconciliationName')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.billCheckName"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>-->
              <!--对账人联系电话-->
             <!-- <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="rl-text-red-sm rl-margin-right-xxxxs">*</em>
                  {{$t('Register.ReconciliationPhone')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.billCheckPhone"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>-->
              <!--对账人邮箱-->
              <!--<div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">
                  <em class="rl-text-red-sm rl-margin-right-xxxxs">*</em>
                  {{$t('Register.ReconciliationEmail')}}
                </span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="userInfos.billCheckEmail"
                    :placeholder="$t('P.PleaseEnter')"
                    readonly
                  />
                </div>
              </div>-->
              <div class="info-title rl-padding-top-lllg">2.{{$t('Register.BillingInformation')}}</div>
              <!--开票公司名称-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">{{$t('Invoice.CompanyName')}}</span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    v-model="financial.invoiceTitleName"
                    type="text"
                   
                    readonly
                  />
                </div>
              </div>
              <!-- <div class="item rl-clear z-index" v-if="(this.userInfos.companyType === 1 || this.userInfos.companyType === 2) && this.userInfos.countryId === 1"> -->
              <!--纳税人识别码-->
              <div class="item rl-clear z-index">
                <span class="rl-fl enterLeft rl-tr">{{$t('Invoice.IdentificationNumber')}}</span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    v-model="financial.taxpayerNumber"
                    type="text"
                    readonly
                  />
                </div>
              </div>
              <!--开票地址-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">{{$t('Invoice.Address')}}</span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.registeredAddress"
                    readonly
                  />
                </div>
              </div>
              <!--电话-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">{{$t('Invoice.Telephone')}}</span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.registeredPhone"
                    readonly
                  />
                </div>
              </div>
              <!--开户行-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">{{$t('Invoice.BankDeposit')}}</span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.registeredBankDepositName"
                    readonly
                  />
                </div>
              </div>
              <!--开票账户-->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft rl-tr">{{$t('Invoice.BankAccount')}}</span>
                <div class="enterInput rl-fl">
                  <input
                    class="common-input"
                    type="text"
                    v-model="financial.registeredBankAccount"
                    readonly
                  />
                </div>
              </div>
            </div>
            <!--协议信息-->
            <div class="info-base rl-margin-bottom-xxxs" v-show="tabs=='protocol'">
              <!-- 协议列表 -->
              <p class="rl-margin-top-lllg rl-margin-bottom-mid cursor-pointer" v-for="protocol in protocolList" :key="protocol.id" @click="openProtocol(protocol)">《{{protocol.name}}》</p>
              
              <!-- 协议弹窗 -->
              <protocolDialog :showOpen="showProtocol" :select="true" :protocolId="curProtocolId" @close="closeProtocol"></protocolDialog>
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
  import {loginOut,region} from '@/assets/js/common.js'
  import GD from '@/assets/js/globalData'
  import protocolDialog from '@/components/protocolDialog'
  // import RegisterDialog from '@/components/RegisterDialog'
  // import cutFilmMachineDialog from '@/components/cutFilmMachineDialog'
  // import proCustomizeDialog from '@/components/proCustomizeDialog'
  // liu--
import { userInfo,regionList,agreementSignList,agreementSignId,userAgreementSign} from '@/apiService/api'
  export default {
    name: 'UserInfos',
    components: {
      Header,
      Left,
      protocolDialog,
      // RegisterDialog,
      // cutFilmMachineDialog,
      // proCustomizeDialog
    },
    data () {
      return {
        tabs: 'basic',
        userState: 2,
        userInfos: {
          countryId: 1, // 国家id
          provinceId: '', // 省市区上级id
          cityId: '',
          townId: '',
          countryName: '',
          provinceName: '',
          cityName: '',
          areaName: '',
          name: '', // 基本信息
          mobile: '',
          authCode: '',
          email: '',
          password: '',
          confirmPassword: '',
          companyName: '',
          
          taxpayerNumber: '',
          address: '',
          zipCode: '',
          
          isTaxpayer: 0, // 是否一般纳税人
          isSmallTaxpayer: 1, // 是否小规模纳税人
          isIndustrial: 1, // 是否个体工商户
          isFit: 0, // 是否散客
          recommendMan: '',
          phone: '',
          sex: 0, // 性别
          companyType: 1, // 公司类型
          taxType: 1 // 税票类型
        }, // 用户信息
        financial:{},  //账户信息
        oldPassword: '', // 旧密码
        newPassword: '', // 新密码
        countryType: false,
        provinceType: false,
        cityType: false,
        showCity: true,
        areaType: false,
        showArea: true,
        countryList: {},
        provinceList: {},
        cityList: {},
        areaList: {},
        companyTypeList: [ // 公司类型选择
          {index: 1, text: this.$t('Register.Company')},
          {index: 2, text: this.$t('Register.IndividualBusiness')},
          {index: 3, text: this.$t('Register.Individual')}
        ],
        ticketTypeList: [ // 税票类型选择
          {index: 1, text: this.$t('Register.GeneralTaxpayer')},
          {index: 2, text: this.$t('Register.SmallTaxpayer')},
          {index: 3, text: this.$t('Register.Individual')}
        ],
        sexList: [ // 选择性别
          {index: 0, text: this.$t('Register.Male')},
          {index: 1, text: this.$t('Register.Female')}
        ],
        useLang: false, // 是否启用多语种
        langList: GD.langList, // 语种列表
        lang: 'zh-CNY', // 语种
        pageNo: 1,
        pageSize: 1000,
        loading: false,
        showOpenReg: false, // 用户协议弹框
        showOpenCut: false, // 膜切机协议弹框
        showOpenPro: false, // 定制协议弹框
        showCut: false,
        showPro: false,
        protocolList: [], // 协议
        showProtocol: false, // 协议弹窗
        curProtocolId: 0, // 协议Id
      }
    },
    methods: {
      fLangChange (value) {
        this.companyTypeList.forEach(item => {
          if (item.index === 1) {
            item.text = this.$t('Register.Company')
          } else if (item.index === 2) {
            item.text = this.$t('Register.IndividualBusiness')
          } else {
            item.text = this.$t('Register.Individual')
          }
        })
        // 税票类型
        this.ticketTypeList.forEach(item => {
          if (item.index === 1) {
            item.text = this.$t('Register.GeneralTaxpayer')
          } else if (item.index === 2) {
            item.text = this.$t('Register.SmallTaxpayer')
          } else {
            item.text = this.$t('Register.Individual')
          }
        })
        this.sexList.forEach(item => {
          if (item.index === 1) {
            item.text = this.$t('Register.Male')
          } else {
            item.text = this.$t('Register.Female')
          }
        })
      },
      // tab切换
      toBasic () {
        this.tabs = 'basic'
      },
      toAccount () {
        this.tabs = 'account'
      },
      toProtocol () {
        this.tabs = 'protocol'
      },
      showUserProtocol (value) {
        if (value === 1) {
          this.showOpenReg = true
        } else if (value === 2) {
          this.showOpenCut = true
        }
      },
      closeUserProtocol (value) {
        // 1-用户协议  2-膜切机协议  3-定制协议
        if (value === 1) {
          this.showOpenReg = false
        } else if (value === 2) {
          this.showOpenCut = false
        }
      },
      
      // 信息选择按钮框
      getIsTaxpayer (index) {
        this.userInfos.companyType = index;
      },
      getIsIndustrial (index) {
        this.userInfos.taxType = index;
      },
      getSex (index) {
        this.userInfos.sex = index + 1;
      },
      // 点击空白处隐藏下拉列表
      hideAll () {
        this.countryType = false;
        this.provinceType = false;
        this.cityType = false;
        this.areaType = false;
      },
      getCountry(){

      },
      // 获取国家名称
      getCountryId (id, name) {
        this.userInfos.countryName = name;
        this.userInfos.countryId = id;
        this.countryType = false;
      },
      // 获取省级列表
      getProvince () {
        region.then((res) => {
            if (res.code === 0) {
              this.provinceList = res.regions;
              this.provinceType = !this.provinceType;
              this.cityType = false;
              this.areaType = false;
            }
          });
      },
      // 获取省级ID
      getProvinceId (cityCode, name) {
        this.cityList = '';
        this.areaList = '';
        this.userInfos.cityName = '';
        this.userInfos.areaName = '';
        this.userInfos.cityId = '';
        this.userInfos.districtId = '';
        this.showCity = false;
        this.showArea = false;
        this.provinceType = false;
        this.showCity = true;
        this.userInfos.provinceName = name;
        this.userInfos.provinceId = cityCode;
        this.$api
          .get(this, 'region', {
            page: this.pageNo,
            count: this.pageSize,
            parentId: this.userInfos.provinceId
          })
          .then((res) => {
            if (res.code === 0) {
              this.cityList = res.regions;
              this.userInfos.cityId = this.cityList[0].id;
              this.userInfos.cityName = this.cityList[0].regionName;
              if (this.cityList[0].haveNext === 1) {
                this.$api
                  .get(this, 'region', { parentId: this.userInfos.cityId })
                  .then((res) => {
                    if (res.code === 0) {
                      this.showArea = true;
                      this.areaList = res.regions;
                      this.userInfos.townId = this.areaList[1].id;
                      this.userInfos.areaName = this.areaList[1].regionName;
                    }
                  });
              } else {
                this.userInfos.districtId = 0;
              }
            }
          });
      },
      // 获取市级列表
      getCity () {
        if (this.userInfos.provinceName) {
          this.cityType = !this.cityType;
          this.userInfos.provinceType = false;
          this.areaType = false;
        } else {
          this.$message.error('请选择省级城市');
        }
      },
      // 获取市级ID
      getCityId (cityCode, name, haveNext) {
        this.areaList = '';
        this.cityType = false;
        this.userInfos.cityName = name;
        this.userInfos.cityId = cityCode;
        this.userInfos.areaName = '';
        if (haveNext === 1) {
          this.$api
            .get(this, 'region', { parentId: this.userInfos.cityId })
            .then((res) => {
              if (res.code === 0) {
                this.areaList = res.regions;
                if (res.regions.length > 0) {
                  this.showArea = true;
                  this.userInfos.districtId = this.areaList[1].id;
                  this.userInfos.areaName = this.areaList[1].regionName;
                } else {
                  this.showArea = false;
                  this.userInfos.districtId = 0;
                }
              }
            });
        } else {
          this.showArea = false;
          this.districtId = 0;
        }
      },
      // 获取区级列表
      getArea () {
        if (this.userInfos.cityName) {
          this.areaType = !this.areaType;
          this.userInfos.provinceType = false;
          this.cityType = false;
        } else {
          this.$message.error('请选择市级城市');
        }
      },
      // 获取区级ID
      getAreaId (cityCode, name) {
        this.areaType = false;
        this.userInfos.areaName = name;
        this.userInfos.townId = cityCode;
      },
      // 获取用户信息
      getUserInfos () {
         let that=this;
         let id=localStorage.getItem('userId');
          userInfo({id:id}).then((res) => {
            if (res.success) {
              this.userInfos = res.data;
              let data=res.data;
              this.financial=res.data.financial;  //账户信息
              if (this.userInfos.townId === 0) {
                this.showArea = false;
              }
              
              that.regionListFun(0,data.countryId,1);  //获取国家
              if (data.countryId) {
                that.regionListFun(data.countryId,data.provinceId,2);  //获取省
              }
              if (data.provinceId) {
                that.regionListFun(data.provinceId,data.cityId,3);  //获取市
              }
              if (data.cityId) {
                that.regionListFun(data.cityId,data.districtId,4);  //获取区
              }
              
            } else {
              this.$message(res.errMessage);
            }
          });
      },

    // liu--获取区域列表type:(1:国家，2：省，3：市，4：区)
    regionListFun(parentId,id,type){
        region(parentId).then(res=>{ 
           let list=res.data.list;
           list.forEach((item) => {

                if (item!=null&&item.id === id) {
                  if(type==1){
                      this.countryList = list;
                      this.userInfos.countryName = item.regionName; 
                      // console.log('国家：',res.data.list);   
                  }else if(type==2){
                     this.provinceList =list;
                     this.userInfos.provinceName = item.regionName;
                     console.log('省：',res.data.list);  
                  }else if(type==3){
                     this.cityList = list;
                     this.userInfos.cityName  = item.regionName;
                     console.log('市：',res.data.list);  
                  }else if(type==4){
                     this.areaList =list;
                     this.userInfos.areaName= item.regionName;
                     console.log('区：',res.data.list);  
                  }
                  
                }
              });
        })
    },
    


      // 确认修改
      confirmModify () {
        this.loading = true;
        var reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z]{2,5}$/;
        if (this.userInfos.cityId === 0 && this.userInfos.countryId === 1) {
          this.$message.warning('请选择省市');
          return false;
        }
        if (this.userInfos.cityName === '' && this.userInfos.countryId === 1) {
          this.$message.warning('请选择省市');
          return false;
        }
        if (this.userInfos.email !== '') {
          if (!reg.test(this.userInfos.email)) {
            this.$message.warning('请输入有效的电子邮件！');
            this.userInfos.email = '';
            return false;
          }
        }
        var jsonOne = {
          countryId: this.userInfos.countryId,
          provinceId: this.userInfos.provinceId,
          cityId: this.userInfos.cityId,
          townId: this.userInfos.townId
        };
        var jsonTwo = {
          bankAccountName: this.userInfos.bankAccountName,  
          bankDepositName: this.userInfos.bankDepositName,
          bankAccount: this.userInfos.bankAccount,
          // billCheckName: this.userInfos.billCheckName,
          // billCheckPhone: this.userInfos.billCheckPhone,
          // billCheckEmail: this.userInfos.billCheckEmail,
          registeredPhone: this.userInfos.registeredPhone,
          registeredAddress: this.userInfos.registeredAddress,
          registeredBankAccount: this.userInfos.registeredBankAccount,
          registeredBankDepositName: this.userInfos.registeredBankDepositName
        };
        var jsonThr = {
          name: this.userInfos.name,
          mobile: this.userInfos.mobile,
          companyName: this.userInfos.companyName,
          certNo: this.userInfos.certNo,
          userName: this.userInfos.userName,
          sex: this.userInfos.sex,
          phone: this.userInfos.phone,
          address: this.userInfos.address,
          email: this.userInfos.email,
          zipCode: this.userInfos.zipCode,
          companyType: this.userInfos.companyType,
          taxType: this.userInfos.taxType
        };
        var jsonFor = {};
        jsonFor = Object.assign(jsonOne, jsonTwo, jsonThr);
        this.$api.put(this, 'user/u/user', jsonFor).then((res) => {
          this.loading = false;
          if (res.code === 0) {
            this.tabs = 'basic';
            this.$message({
              type: 'success',
              message: this.$t('Register.Step.FourOther'),
              duration: 5000
            });
            this.getUserInfos();
          } else if (res.code === 3) {
           
          }
        });
      },
      // 获取用户已签署协议
      getUserAgreementSign() {
        let id=localStorage.getItem('userId');
        userAgreementSign({distributorId: id}).then((res) => {
          if (res.success) {
            let protocolList = [];
            res.data.forEach((protocol) => {
              if (this.$i18n.locale === 'zh' && protocol.agreementArea == 0) {
                protocolList.push(protocol);
              } else if (this.$i18n.locale !== 'zh' && protocol.agreementArea == 1) {
                protocolList.push(protocol);
              } 
            });
            this.protocolList = protocolList;
          } else {
            this.$message(res.errMessage);
          }
        });
      },
      openProtocol(protocol) {
        this.curProtocolId = protocol.id;
        this.showProtocol = true;
      },
      closeProtocol() {
        this.showProtocol = false;
      },
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang')
      ? window.localStorage.getItem('bLang')
      : 'zh-RMB';
  },
  mounted () {
    this.getUserInfos();
    this.regionListFun(0);
    this.getUserAgreementSign(); // 获取用户已签署协议
  }
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .content {
    padding: 24px 40px 20px;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
}
.register-content {
  .info-tab {
    border-bottom: 1px solid @bdLightColor;
    span {
      padding: 0 3px 18px;
      margin-right: 20px;
      height: 22px;
      line-height: 22px;
      font-size: 16px;
      color: @lightBlack;
      border-bottom: 2px solid @white;
      &.active {
        color: @blue;
        border-bottom: 2px solid @blue;
      }
    }
  }
  .info {
    width: 650px;
    .info-base {
      padding-top: 20px;
      .item {
        margin-bottom: 15px;
        height: 30px;
        line-height: 30px;
        .red {
          font-size: 12px;
          color: @red;
        }
        .enterLeft {
          width: 220px;
          display: block;
          font-size: 12px;
          color: @lighterBlack;
        }
        .enterInput {
          margin-left: 15px;
          width: 280px;
          height: 30px;
          &.mini {
            width: 190px;
          }
          .common-input {
            padding-left: 10px;
            width: 100%;
            height: 30px;
            line-height: 30px;
            border: 1px solid @bdLightColor;
            cursor: not-allowed;
          }
        }
        .checkedInput {
          margin-left: 20px;
          height: 30px;
          cursor: not-allowed;
          .chenked {
            width: 17px;
            height: 19px;
            display: inline-block;
            vertical-align: -5px;
            -webkit-appearance: none;
            appearance: none;
            outline: none;
            margin-right: 2px;
            font-size: 12px;
            color: @lighterBlack;
            background: url(../../assets/images/un-selected.png) no-repeat
              center center;
            background-size: 100%;
          }
          .haschecked {
            background: url(../../assets/images/selected.png) no-repeat center
              center;
            background-size: 100%;
          }
          span {
            font-size: 12px;
            color: @lighterBlack;
          }
        }
        .items {
          input.trade-input {
            padding-left: 10px;
            width: 115px;
            height: 30px;
            line-height: 30px;
            box-sizing: border-box;
            font-size: 12px;
            color: @lighterBlack;
            background: url("../../assets/images/selectUl.png") no-repeat 100px
              center;
            box-sizing: border-box;
            overflow: hidden;
            text-overflow: ellipsis;
            cursor: not-allowed;
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
      .info-title {
        padding-left: 30px;
        padding-bottom: 15px;
        font-size: 12px;
        color: @lighterBlack;
      }
    }
  }
  .protocol-wrap {
    .protocol-title {
      font-size: 16px;
      color: @lightBlack;
      text-align: center;
    }
    .user-protocol {
      padding-top: 30px;
      max-height: 480px;
      overflow: hidden;
      &.show-all {
        max-height: inherit;
      }
      h1 {
        margin: 30px 0 10px;
        line-height: 2;
      }
      p {
        margin-bottom: 15px;
        line-height: 1.8;
        text-align: justify;
      }
    }
    .show-more {
      margin-top: 10px;
      color: @blue;
      &:hover {
        opacity: 0.6;
      }
    }
  }
  input {
    font-size: 12px;
    color: @lighterBlack;
  }
}
.footer {
  width: 368px;
  cursor: pointer;
  .footer-btn {
    width: 425px;
    height: 35px;
    font-size: 14px;
    // line-height: 35px;
  }
}
/*点击空白处隐藏下拉列表*/
.disNone {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}
.z-index {
  z-index: 1;
}
</style>
