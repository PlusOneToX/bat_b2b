<template>
  <div>
    <div class="register-head rl-bg-white rl-margin-bottom-xxxs">
      <div class="login-head rl-padding-horizontal-lllg rl-padding-top-lllg rl-padding-bottom-default rl-clear">
        <div class="logo rl-fl">
          <router-link to="/Index">
            <img class="logo-img" src="../../assets/images/index/logo.png" alt="BAT商城" />
          </router-link>
        </div>
        <div class="lang-box" v-if="useLang">
          <el-select v-model="lang"
                     @change="fLangChange"
                     :placeholder="$t('P.Select')">
            <el-option
              v-for="item in langList"
              :key="item.id"
              :label="item.name"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <!--        <div class="rl-fl words rl-text-lg rl-margin-left-lllg rl-text-gray rl-margin-top-xxxs" v-if="this.times !== 1">欢迎注册</div>-->
        <!--        <div class="rl-fl word-spe rl-text-lg rl-margin-top-xxxs" v-if="this.times === 1">首次登录需进行信息修改</div>-->
        <div class="rl-fr logo-right rl-text-default rl-margin-right-default rl-margin-top-xxxs" v-if="this.times !== 1">
          <span class="rl-text-gray">{{$t('Register.AlreadyAccount')}}</span>
          <span class="rl-text-orange-sm cursor-pointer" @click="$router.push({name: 'Login', params: {key: '-1'}})">{{$t('P.Login')}}》</span>
        </div>
      </div>
    </div>
    <div class="register rl-padding-horizontal-lllg rl-bg-white rl-margin-zero">
      <div class="yuan rl-clear rl-margin-zero">
        <div class="all rl-relative rl-fl">
          <div class="num-spe">1</div>
          <div class="word-spe rl-tc" v-if="this.times === 1">{{$t('Register.Step.OneOther')}}</div>
          <div class="word-spe rl-tc" v-else>{{$t('Register.Step.One')}}</div>
          <div class="img-spe"></div>
        </div>
        <div class="all rl-relative rl-fl">
          <div class="num-spe">2</div>
          <div class="word-spe rl-tc">{{$t('Register.Step.Two')}}</div>
          <div class="img" v-if="this.step === 1"></div>
          <div class="img-spe" v-if="this.step === 2"></div>
        </div>
        <!-- <div class="all rl-relative rl-fl" v-if="this.step === 2">
           <div class="num-spe">3</div>
           <div class="word-spe rl-tc">{{$t('Register.Step.Three')}}</div>
           <div class="img"></div>
         </div>
         <div class="all rl-relative rl-fl" v-if="this.step === 1">
           <div class="num">3</div>
           <div class="word rl-tc">{{$t('Register.Step.Three')}}</div>
           <div class="img"></div>
         </div>-->
        <div class="all rl-relative rl-fl">
          <div class="num">3</div>
          <div class="word rl-tc" v-if="this.times === 1">{{$t('Register.Step.FourOther')}}</div>
          <div class="word rl-tc" v-else>{{$t('Register.Step.Four')}}</div>
        </div>
      </div>
      <div class="register-content rl-margin-zero">
        <!--<div class="head rl-tc">详细资料补充</div>-->
        <div class="info rl-margin-zero">
          <div class="info-base rl-margin-bottom-double rl-padding-top-default rl-relative" v-show="step === 1">
            <!--<el-form  status-icon label-width='20%'  :rules="rules" label-position="right" ref="basic">-->
            <!--<el-form-item label="用户名:" prop="name">-->
            <!--<el-input size="mini"  v-model="basic.name" placeholder="请输入" maxlength="20"/>-->
            <!--</el-form-item>-->

            <!--<el-form-item label="密码" prop="password">-->
            <!--<el-input type="password" v-model="basic.password" size="mini" auto-complete="off" maxlength="16"/>-->
            <!--</el-form-item>-->
            <!--</el-form>-->
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.CompanyType')}}</span>
              <div class="checkedInput rl-fl">
                <div class="rl-fl rl-margin-left-default" v-for="item in companyTypeList" :key="item.id">
                  <span @click="getIsTaxpayer(item.index)" name="showpri" checked="checked" class="chenked" :class="{'haschecked':item.index === companyType}"></span>
                  <span class="rl-margin-right-mid">{{item.text}}</span></div>
              </div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.Username')}}</span>
              <div class="enterInput rl-fl" v-if="this.times === 1"><input class="common-input" type="text" v-model="basic.name" readonly></div>
              <div class="enterInput rl-fl" v-else><input class="common-input" type="text" @blur="inputName" v-model="basic.name"></div>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'zh'">用户名仅可为2-20个字符，可为中文、英文、数字结合，区分大小写</p>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'en'">The username is composed of 2-20 characters, and is case-sensitive.</p>
            </div>
            <div class="item rl-clear z-index rl-relative" v-if="this.times === 1">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.LoginMobile')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text"  v-model="basic.mobile"></div>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'zh'">用于登录、找回密码</p>
            </div>
            <div class="item rl-clear z-index rl-relative" v-if="this.times !== 1">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.Password')}}</span>
              <div class="enterInput rl-fl"><input @blur="schoolOne" class="common-input" type="text" onfocus="this.type='password'"  maxlength="16" v-model="basic.password"></div>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'zh'">密码仅可为6-16个字符，可为英文、数字组合，区分大小写</p>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'en'">The password is composed of 6-16 characters, and is case-sensitive.</p>
            </div>
            <div class="item rl-clear z-index rl-relative" v-if="this.times !== 1">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.ComfirmPassword')}}</span>
              <div class="enterInput rl-fl"><input @blur="schoolTwo" class="common-input" type="text" onfocus="this.type='password'" maxlength="16" v-model="basic.confirmPassword"></div>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'zh'">密码仅可为6-16个字符，可为英文、数字组合，区分大小写</p>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'en'">The password is composed of 6-16 characters, and is case-sensitive.</p>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.CustomerName')}}</span>
              <div class="enterInput rl-fl">
                <input class="common-input" type="text" v-model="basic.companyName">
              </div>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'zh'">客户为公司、个体户，填营业执照上的公司全称；客户为个人，填身份证客户全名</p>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'en'">Enter the company on the business license or the full name of ID card.</p>
            </div>
            <div class="item rl-clear z-index">
              <span v-show="companyType === 3" class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.IDNo')}}</span>
              <span v-show="companyType !== 3" class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.BusinessLicense')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="basic.idCard"></div>
              <p class="prompt rl-text-xxs" v-show="$i18n.locale === 'zh'">客户为公司、个体户，则填写营业执照号码；客户为个人，则填写身份证号</p>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.Contact')}}</span>
              <div class="enterInput rl-fl">
                <input class="common-input" type="text" v-model="basic.registerName">
              </div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.ContactPhone')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="basic.phone" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
            </div>
            <div class="item rl-clear">
              <div class="disNone" v-on:click="hideAll"></div>
              <span class="rl-fl enterLeft rl-tr rl-text-xxss rl-margin-right-double"><em class="rl-text-blue-xs">*</em>{{$t('Register.Country')}}</span>
              <!-- <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="basic.country" placeholder="请输入"></div>-->
              <div class="items rl-fl rl-relative rl-margin-right-mid z-index">
                <input class="cursor-pointer trade-input" type="text" readonly="readonly" v-model="basic.country" :placeholder="$t('P.pleaseSelect')" @click= "getCountry()">
                <ul class="selectUl rl-bg-white"  v-show="countryType ===true ">
                  <li @click="getCountryId(country.id,($i18n.locale === 'zh' || !country.regionNameEn == true) ? country.regionName:country.regionNameEn)" v-for="country in countryList" :key="country.id">{{($i18n.locale === 'zh' || !country.regionNameEn == true) ? country.regionName:country.regionNameEn}}</li>
                </ul>
              </div>
            </div>
            <div class="item rl-clear rl-margin-bottom-xxxs" v-if="this.countryId === 37">
              <div class="disNone" v-on:click="hideAll"></div>
              <span class="enterLeft rl-fl rl-text-xxss rl-tr rl-margin-right-double"><em class="rl-text-blue-xs">*</em>{{$t('Register.ProvincesCity')}}</span>
              <div class="items rl-fl rl-relative rl-margin-right-mid">
                <input class="cursor-pointer trade-input" type="text" readonly="readonly" v-model="provinceName" :placeholder="$t('P.pleaseSelect')" @click= "getProvince()">
                <ul class="selectUl rl-bg-white"  v-show="provinceType ===true ">
                  <li @click="getProvinceId(province.id,province.regionName)" v-for="province in provinceList" :key="province.id">{{province.regionName}}</li>
                </ul>
              </div>
              <div class="items rl-fl rl-relative rl-margin-right-mid" v-show="showCity === true">
                <input class="cursor-pointer trade-input" type="text" readonly="readonly" v-model="cityName" :placeholder="$t('P.pleaseSelect')" @click= "getCity()">
                <ul class="selectUl rl-bg-white"  v-show="cityType ===true ">
                  <li @click="getCityId(city.id,city.regionName,city.haveNext)" v-for="city in cityList" :key="city.id">{{city.regionName}}</li>
                </ul>
              </div>
              <div class="items rl-fl rl-relative" v-show="showArea === true">
                <input class="cursor-pointer trade-input" type="text" readonly="readonly" v-model="areaName" :placeholder="$t('P.pleaseSelect')" @click= "getArea()">
                <ul class="selectUl rl-bg-white"  v-show="areaType ===true ">
                  <li @click="getAreaId(area.id,area.regionName)" v-for="area in areaList" :key="area.id">{{area.regionName}}</li>
                </ul>
              </div>
            </div>

            <div class="item rl-clear">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.ContactAddress')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="basic.address"></div>
            </div>
            <div class="item rl-clear">
              <span class="rl-fl enterLeft rl-tr"><em v-if="this.lang !== 'zh_CNY'" class="rl-text-blue-xs">*</em>{{$t('Register.EmailAddress')}}</span>
              <div class="enterInput rl-fl"><input @blur="schoolEmail" class="common-input" type="text" v-model="basic.procureEmail"></div>
            </div>
            <div class="item rl-clear">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.ZipCode')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="basic.zipCode" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
            </div>
            <div class="item rl-clear z-index" v-if="this.countryId === 1">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.TaxType')}}</span>
              <div class="checkedInput rl-fl">
                <div class="rl-fl rl-margin-left-default" v-for="item in ticketTypeList" :key="item.id"><span @click="getIsIndustrial(item.index)" name="showpri" checked="checked" class="chenked" :class="{'haschecked':item.index === ticketType}"></span><span class="rl-margin-right-mid">{{item.text}}</span></div>
              </div>
              <p class="prompt rl-text-xxs rl-text-orange-sm rl-margin-left-default" v-show="$i18n.locale === 'zh'">海外客户可不填写</p>
            </div>
            <div class="item rl-clear remark">
              <span class="rl-fl enterLeft rl-tr">{{$t('UserCenter.Remark')}}</span>
              <div class="enterInput rl-fl">
                <textarea class="remark" rows="4" cols="41" v-model="basic.remarkes" :placeholder="$t('UserCenter.Notes')"></textarea>
              </div>
            </div>
          </div>




          <div class="info-base rl-margin-bottom-xxxs rl-padding-top-default" v-show="step === 2">
            <div class="rl-text-blue-xs rl-text-mid rl-margin-top-default rl-margin-bottom-default">{{$t('Register.BankInformation')}}</div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.BankAccountName')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="bank.bankAccountName"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.BankDeposit')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="bank.bankDepositName"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.BankAccount')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="bank.bankAccount" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
            </div>
            <div class="rl-text-blue-xs rl-text-mid rl-margin-top-default rl-margin-bottom-default">{{$t('Register.ReconciliationInformation')}}</div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.ReconciliationName')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="check.checkName"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.ReconciliationPhone')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="check.checkPhone" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr"><em class="rl-text-blue-xs">*</em>{{$t('Register.ReconciliationEmail')}}</span>
              <div class="enterInput rl-fl"><input @blur="schoolCheckEmail" class="common-input" type="text" v-model="check.checkEmail"></div>
            </div>
            <div class="rl-text-blue-xs rl-text-mid rl-margin-top-default rl-margin-bottom-default">{{$t('Register.BillingInformation')}}</div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.Company')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" v-model="basic.companyName" type="text" readonly></div>
            </div>
            <div class="item rl-clear z-index" v-if="this.companyType === 1 || this.companyType === 2">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.IdentificationNumber')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" v-model="basic.idCard" type="text" readonly></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.Telephone')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="invoice.phone" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.Address')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="invoice.address"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.Bank')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="invoice.bankAccountName"></div>
            </div>
            <div class="item rl-clear z-index">
              <span class="rl-fl enterLeft rl-tr">{{$t('Register.BankAccount')}}</span>
              <div class="enterInput rl-fl"><input class="common-input" type="text" v-model="invoice.bankName"  onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer rl-margin-zero rl-clear">
        <div @click="preaStep" class="footer-btn cancel rl-tc rl-fl rl-bg-gray-mm rl-text-white rl-margin-top-default rl-margin-right-xxxs" v-if="this.step === 1">{{$t('P.Back')}}</div>
        <div @click="preaStep" class="footer-btn cancel rl-tc rl-fl rl-bg-blue-xs rl-text-white rl-margin-top-default rl-margin-right-xxxs" v-else>{{$t('P.Back')}}</div>
        <!--<div @click="nextStep" class="footer-btn rl-tc rl-fl rl-bg-blue-xs rl-text-white rl-margin-top-default" v-if="this.step === 1">{{$t('P.Next')}}</div>-->
        <div @click="submitRegister" class="footer-btn rl-tc rl-fl rl-bg-blue-xs rl-text-white rl-margin-top-default" v-if="this.step === 1  && this.times !== 1">{{$t('P.Register')}}</div>
        <div @click="submitRegister" class="footer-btn rl-tc rl-fl rl-bg-blue-xs rl-text-white rl-margin-top-default" v-if="this.step === 1 && this.times === 1">{{$t('P.Save')}}</div>
      </div>
    </div>
  </div>
</template>

<script>
  import md5 from 'js-md5'
  import GD from '@/assets/js/globalData'
  import {region } from "@/assets/js/common.js";
  import { oneLevelApply,userInfo} from '@/apiService/api'
  export default {
    name: 'Register',
    data () {
      return {
        step: 1, // 步骤
        times: 0,
        pageNo: 1,
        pageSize: 1000,
        basic: {
          mobile: '',
          name: '', // 基本信息
          password: '',
          confirmPassword: '',
          companyName: '',
          idCard: '',
          registerName: '',
          phone: '',
          address: '',
          email: '',
          procureEmail: '', // 公司邮箱
          zipCode: '',
          country: '', // 国家
          remarkes: ''
        },
        bank: {
          bankAccountName: '',
          bankDepositName: '',
          bankAccount: ''
        },
        check: {
          checkName: '',
          checkPhone: '',
          checkEmail: ''
        },
        invoice: {
          phone: '',
          address: '',
          bankName: '',
          bankAccountName: ''
        },
        companyType: 1, // 公司类型
        companyTypeList: [ // 公司类型选择
          {index: 1, text: this.$t('Register.Company')},
          {index: 2, text: this.$t('Register.IndividualBusiness')},
          {index: 3, text: this.$t('Register.Individual')}
        ],
        ticketType: 1, // 税票类型
        ticketTypeList: [ // 税票类型选择
          {index: 1, text: this.$t('Register.GeneralTaxpayer')},
          {index: 2, text: this.$t('Register.SmallTaxpayer')},
          {index: 3, text: this.$t('Register.Individual')}
        ],
        sex: 1, // 性别
        sexList: [ // 选择性别
          {index: 1, text: this.$t('Register.Male')},
          {index: 2, text: this.$t('Register.Female')}
        ],
        countryId: 0, // 国家id
        provinceId: 0, // 省市区上级id
        cityId: 0,
        townId: 0,
        countryType: false,
        provinceName: '',
        provinceType: false,
        cityName: '',
        cityType: false,
        showCity: false,
        areaName: '',
        areaType: false,
        showArea: false,
        countryList: [],
        provinceList: [],
        cityList: [],
        areaList: [],
        mobile: this.$route.params.mobile,
        taxpayerNumber: '',
        idCard: '',
        isSmallTaxpayer: 1, // 是否小规模纳税人
        isFit: 1, // 是否散客
        recommendMan: '',
        btnSendCodeTitle: '获取验证码',
        count: 0, // 记录输入用户名次数
        useLang: false, // 是否启用多语种
        langList: GD.langList, // 语种列表
        lang: 'zh-CNY', // 语种
        rules: { // 必填输入提示
          name: [
            {required: true, message: '请输入用户名', trigger: 'blur'}
          ]
        }
      }
    },
    created () {
      this.useLang = this.$b2bConfig.lang;
      this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh_CNY';
    },
    mounted () {
      var time = window.localStorage.getItem('times')
      this.times = Number(time)
      if (this.times === 1) {
        var pas = window.localStorage.getItem('password')
        this.basic.password = pas
        this.getUserInfo()
      } else {
        var biles = window.localStorage.getItem('mobile')
        if (this.lang === 'zh_CNY') {
          this.basic.mobile = biles
          this.basic.phone = biles
        } else {
          this.basic.email = biles
          this.basic.procureEmail = biles
        }
      }
    },
    methods: {
      fLangChange (value) {
        window.localStorage.setItem('bLang', value);
        this.$i18n.locale = value.split('_')[0];
        this.companyTypeList.forEach(item => {
          if (item.index === 1) {
            item.text = this.$t('Register.Company')
          } else if (item.index === 2) {
            item.text = this.$t('Register.IndividualBusiness')
          } else {
            item.text = this.$t('Register.Individual')
          }
        })
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
      inputName () {
        if (this.basic.name.length < 2 || this.basic.name.length > 20) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('用户名仅可为2-20个字符，可为中文、英文、数字结合，区分大小写！')
          } else { this.$message.warning('The username is composed of 2-20 characters, and is case-sensitive.') }
          this.basic.name = ''
        }
      },
      schoolOne () {
        var reg = /^[A-Za-z0-9]+$/
        if (!reg.test(this.basic.password)) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
          } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
          this.basic.password = ''
        } else if (this.basic.password.length < 6 || this.basic.password.length > 16) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
          } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
          this.basic.password = ''
        }
      },
      schoolTwo () {
        var reg = /^[A-Za-z0-9]+$/
        if (!reg.test(this.basic.confirmPassword)) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
          } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
          this.basic.confirmPassword = ''
        } else if (this.basic.confirmPassword.length < 6 || this.basic.confirmPassword.length > 16) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('密码长度为6-16个字符，可为英文、数字组合，区分大小写！')
          } else { this.$message.warning('The password is composed of 6-16 characters, and is case-sensitive.') }
          this.basic.confirmPassword = ''
        } else if (this.basic.password !== this.basic.confirmPassword) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('两次输入的密码不一致，请重新输入！')
          } else { this.$message.warning('The password entered in the first time is inconsistent with that entered in the second time.') }
          this.basic.confirmPassword = ''
        }
      },
      schoolEmail () {
        var reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z]{2,5}$/
        if (!reg.test(this.basic.procureEmail)) {
          if (this.$i18n.locale === 'zh') {
            if (this.basic.procureEmail !== '') {
              this.$message.warning('请输入有效的电子邮件！')
            }
          } else {
            this.$message.warning('Please enter valid e-mail.')
          }
            this.basic.procureEmail = ''
            return false
        }
      },
      schoolCheckEmail () {
        var reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z]{2,5}$/
        if (!reg.test(this.check.checkEmail)) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入有效的对账人邮箱！')
          } else { this.$message.warning('Please enter valid Reconciliation personnel e-mail.') }
          this.check.checkEmail = ''
          return false
        }
      },
      // 信息选择按钮框
      getIsTaxpayer (index) {
        this.companyType = index
      },
      getIsIndustrial (index) {
        this.ticketType = index
      },
      getSex (index) {
        this.sex = index
      },
      // 点击空白处隐藏下拉列表
      hideAll () {
        this.countryType = false
        this.provinceType = false
        this.cityType = false
        this.areaType = false
      },




      // liu--获取区域列表type:(1:国家，2：省，3：市，4：区)
      regionListFun(parentId,id,type){
          region(parentId).then(res=>{
            let list=res.data.list;
            if(type==1){
                  this.countryList = list;
                  console.log('国际--',this.countryList);
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
                      this.basic.country = item.regionName;   
                  }else if(type==2){
                      this.provinceName = item.regionName;
                      console.log('省：',res.data.list);  
                  }else if(type==3){
                      this.cityName  = item.regionName;
                      this.cityId = this.cityList[0].id
                      this.cityName = this.cityList[0].regionName
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
      // 获取国家
      getCountry () {
        this.countryType = !this.countryType
        this.regionListFun(0,-1,1)
        console.log(this.countryType);
      },
      // 获取国家名称
      getCountryId (id, name) {
        this.basic.country = name
        this.countryId = id
        this.countryType = false;
      
      },
      // 获取省级列表
      getProvince () {
        this.regionListFun(this.countryId,this.provinceId,2)  //获取省
        this.provinceType = !this.provinceType
        this.cityType = false
        this.areaType = false
       
       
      },
      // 获取省级ID
      getProvinceId (cityCode, name) {
        this.cityList = ''
        this.areaList = ''
        this.cityName = ''
        this.areaName = ''
        this.cityId = ''
        this.townId = ''
        this.showCity = true
        this.showArea = false
        this.provinceType = false
        this.provinceName = name
        this.provinceId = cityCode
        this.regionListFun(this.provinceId,-1,3)  //获取市
      },
      // 获取市级列表
      getCity () {
        if (this.provinceName) {
          this.cityType = !this.cityType
          this.provinceType = false
          this.areaType = false
        } else {
          if (this.$i18n.locale === 'zh') {
            this.$message.error('请选择省级城市')
          } else { this.$message.error('Please choose province, city and district.') }
        }
      },
      // 获取市级ID
      getCityId (cityCode, name, haveNext) {
        this.areaList = ''
        this.cityType = false
        this.cityName = name
        this.cityId = cityCode
        this.areaName = ''
        if (haveNext == 1) {
          this.regionListFun(this.cityId,-1,4)  //获取区
          this.showArea = true
          this.townId = this.areaList[1].id
          this.areaName = this.areaList[1].regionName
        } else {
          this.showArea = false
          this.townId = 0
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
            this.$message.error('请选择省级城市')
          } else { this.$message.error('Please choose province, city and district.') }
        }
      },
      // 获取区级ID
      getAreaId (cityCode, name) {
        this.areaType = false
        this.areaName = name
        this.townId = cityCode
      },
      // 上一步
      preaStep () {
        this.$router.push('VerifyMobile')
      },
      // 下一步
      nextStep () {
        var reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z]{2,5}$/
        if (this.basic.name === '') {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入用户名')
          } else { this.$message.warning('Please enter user name.') }
          return false
        }
        if (this.basic.password === '' && this.times !== 1) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入密码')
          } else { this.$message.warning('Please enter password.') }
          return false
        }
        if (this.basic.confirmPassword === '' && this.times !== 1) {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请重复输入密码')
          } else { this.$message.warning('Please enter password again.') }
          return false
        }
        if (this.basic.companyName === '') {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入客户名称')
          } else { this.$message.warning('Please enter customer name.') }
          return false
        }
        if (this.basic.idCard === '' && this.times !== 1) {
          let msg = this.companyType === 3 ? '请输入身份证号' : '请输入营业执照号'
          let msgEn = this.companyType === 3 ? 'ID card' : 'BusinessLicense'
          if (this.$i18n.locale === 'zh') {
            this.$message.warning(msg)
          } else { this.$message.warning(msgEn) }
          return false
        }
        if (this.basic.registerName === '') {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入账户操作人')
          } else { this.$message.warning('Please enter Account operator') }
          return false
        }
        if (this.basic.phone === '') {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入账户操作人联系方式')
          } else { this.$message.warning('Please enter contact number') }
          return false
        } else {
          if (/^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[8-9])[0-9]{8}$/.test(this.basic.phone) === false) {
            if (this.$i18n.locale === 'zh') {
              this.$message.warning('请输入正确的手机')
            } else {
              this.$message.warning('Please enter correct contact number')
            }
            return false
          }
        }
        if (this.basic.country === '') {
          if (this.$i18n.locale === 'zh') {
            this.$message.warning('请输入国家')
          } else { this.$message.warning('Please choose country.') }
          return false
        }
        if (this.cityName === '' && this.countryId === 1) {
          if (this.$i18n.locale === 'zh') {
            this.$message.error('请选择省市')
          } else { this.$message.error('Please choose province, city and district.') }
          return false
        }
        if (this.basic.address === '') {
          if (this.$i18n.locale === 'zh') {
            this.$message.error('请输入客户联系地址')
          } else { this.$message.error('Please enter customer contact address.') }
          return false
        }
        if (this.$i18n.locale === 'zh') {
          if (this.basic.procureEmail !== '') {
            if (!reg.test(this.basic.procureEmail)) {
              this.$message.error('请输入有效的电子邮件')
              this.basic.procureEmail = ''
              return false
            }
          }
        } else {
          if (this.basic.procureEmail !== '') {
            if (!reg.test(this.basic.procureEmail)) {
              this.$message.error('Please enter valid e-mail.')
              this.basic.procureEmail = ''
              return false
            }
          } else {
            this.$message.error('Please enter e-mail.')
            return false
          }
        }
        this.judge()
        // this.step = this.step + 1
      },
      // 提交注册--判断
      submitRegister () {
        this.nextStep()
      },
      // 提交注册--请求
      judge () {
        var jsonOne = {
          companyType: this.companyType, 
          companyName: this.basic.companyName,
          password: md5(this.basic.password), 
          name: this.basic.name, 
          certNo: this.basic.idCard, 
          accountName:this.basic.registerName,  //账户拥有联系人名称
          
          phone: this.basic.phone,
          countryId: this.countryId,
          provinceId: this.provinceId, 
          cityId: this.cityId, 
          districtId:this.townId,  //区id
          address: this.basic.address, 
          email: this.basic.procureEmail,
          zipCode: this.basic.zipCode,  //邮编
          comment: this.basic.remarkes,
          sex: this.sex, 
          // mobile: this.basic.mobile, 
          // email: this.basic.email, 
          // procureEmail: this.basic.procureEmail, 
          // taxType: this.ticketType
        }
        oneLevelApply(jsonOne).then(res=>{
            if (res.success) {
              if (this.$i18n.locale === 'zh') {
                this.$message.success('注册成功')
              } else { this.$message.success('Register successfully') }
              window.localStorage.removeItem('mobile')
              this.$router.push({name: 'RegisterSuccess'})
            }else{
              this.$message(res.errMessage);
            }
        })
        
      },
      // 获取用户信息
      getUserInfo () {
        let id=localStorage.getItem('userId');
        userInfo({id:id}).then(res=>{
           let data=res.data;
           if (res.success) {
              this.countryId = data.countryId
              this.provinceId = data.provinceId
              this.cityId = data.cityId
              this.townId = data.districtId
              this.basic.name = data.name
              this.basic.mobile = data.phone
              this.basic.companyName = data.companyName
              this.basic.idCard = data.certNo
              this.basic.registerName = data.userName
              this.sex = data.sex
              this.taxType=data.financial.taxType
              this.basic.phone = data.phone
              this.basic.address =data.address
              this.basic.email = data.email
              this.basic.zipCode = data.zipCode
              this.companyType = data.companyType
              this.bank.bankAccountName = data.financial.bankAccountName
              this.bank.bankDepositName = data.financial.bankDepositName
              this.bank.bankAccount =data.financial.bankAccount
              this.invoice.phone = data.financial.registeredPhone
              this.invoice.address = data.financial.registeredAddress
              this.invoice.bankName = data.financial.registeredBankAccount
              this.invoice.bankAccountName =data.financial.registeredBankDepositName
              this.areaInfo()
          }
        })
      },
      areaInfo () {
        this.showCity = true
        this.showArea = true
        if (this.townId === 0) {
          this.showArea = false
        }
        
        this.regionListFun(0,-1,1)
        this.regionListFun(this.countryId,this.provinceId,1)  //获取省
        this.regionListFun(this.provinceId,this.cityId,1)  //获取市
        this.regionListFun(this.cityId,this.townId,1)  //获取区
       
      }
    },
    
  }
</script>

<style scoped="scoped" lang='less'>
  .yuan{
    padding-left: 250px;
    padding-top: 40px;
    padding-bottom: 20px;
    width: 600px;
    .all{
      width: 120px;
      margin-right: 30px;
      .num{
        margin: 0 auto;
        width: 20px;
        height: 20px;
        line-height: 20px;
        text-align: center;
        border: 1px solid #C9C9C9;
        border-radius: 50%;
        color: #C9C9C9;
        font-size: 14px;
      }
      .word{
        margin-top: 5px;
        font-size: 14px;
        color: #C9C9C9;
      }
      .img{
        position: absolute;
        top: 8px;
        left: 85px;
        width: 95px;
        height: 6px;
        background: url("../../assets/images/step/bg-yuan.png") center center no-repeat;
      }
      .num-spe{
        margin: 0 auto;
        width: 20px;
        height: 20px;
        line-height: 20px;
        text-align: center;
        background-color: #00c9dc;
        border-radius: 50%;
        color: #fff;
        font-size: 14px;
      }
      .word-spe{
        margin-top: 5px;
        font-size: 14px;
        color: #00c9dc;
      }
      .img-spe{
        position: absolute;
        top: 8px;
        left: 85px;
        width: 95px;
        height: 6px;
        background: url("../../assets/images/step/bg-yuan1.png") center center no-repeat;
      }
    }
  }
  .register-head{
    width: 100%;
    .login-head{
      width: 1190px;
      margin: 0 auto;
      .logo{
        display:flex;
        align-items: center;
        z-index: 11;
      }
      .logo-right{
        z-index: 11;
      }
      .word-spe{
        margin-left: 220px;
      }
    }
  }
  .register{
    padding-bottom: 60px;
    width: 100%;
    .register-logo{
      width: 328px;
      img{width: 100%}
    }
    .register-content{
      .head{
        padding-top: 20px;
        margin-bottom: 30px ;
        font-size: 24px;
      }
      .info{
        width: 760px;
        .info-base{
          .item{
            margin-left: 80px;
            margin-bottom: 30px;
            height: 30px;
            line-height: 30px;
            &.remark{
              height: 80px;
            }
            .prompt{
              position: absolute;
              left: 190px;
              bottom:-30px;
              color: #C9C9C9;
            }
            .authCode{
              width: 80px;
              height: 30px;
              line-height: 30px;
              border-radius: 5px;
            }
            .enterLeft{
              width: 180px;
              display: block;
              font-weight: bold;
            }
            .enterInput{
              margin-left: 40px;
              width: 328px;
              height: 30px;
              .common-input{
                padding-left: 20px;
                border: 0;
                width: 308px;
                height: 30px;
                line-height: 30px;
                border: 1px solid #ccc;
              }
              .remark{
                padding:10px;
                border-color:#F3F3F3;
                background-color: #F3F3F3;
              }
            }
            .checkedInput{
              margin-left: 20px;
              /* width: 255px;*/
              height: 30px;
              .chenked{
                cursor: pointer;
                width: 19px;
                height: 19px;
                display: inline-block;
                vertical-align: -5px;
                -webkit-appearance: none;
                appearance: none;
                outline: none;
                margin-right: 5px;
                font-size: 14px;
                color: #333;
                background: url(../../assets/images/un-selected.png) no-repeat center center;
              }
              .haschecked{
                background: url(../../assets/images/selected.png) no-repeat center center;
              }
            }
            .items{
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
                  background: #00c9dc;
                  color: #fff;
                }
              }
            }
          }
        }
      }
    }
  }
  .footer{
    padding-left: 100px;
    width: 368px;
    cursor: pointer;
    .footer-btn{
      width: 170px;
      height: 35px;
      line-height: 35px;
      &.cancel {
        color:#333;
      }
    }
  }
  /*点击空白处隐藏下拉列表*/
  .disNone{
    position: fixed; left:0; top: 0; width: 100%; height: 100%;
    z-index: 0;
  }
  .z-index{
    z-index: 1;
  }
  .lang-box{
    display: inline-block;
    width: 140px;
    margin-left: 20px;
    z-index: 11;
  }

  // logo
  .logo-img {
    width: 177px;
  }
</style>
