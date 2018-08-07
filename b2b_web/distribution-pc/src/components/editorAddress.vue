<template>
<div>
  <div @click.stop="" class="cover" v-if="this.addressInfos.type === 1"></div>
  <div @click.stop="" class="pro-cover cover-box rl-padding-bottom-lllg rl-padding-top-default rl-relative" v-if="this.addressInfos.type === 1">
    <div class="rl-margin-bottom-default rl-bdb-gray-sm rl-padding-left-default rl-padding-bottom-xxxs">{{$t('P.EditShippingAddress')}}</div>
    <span @click.stop="shutLog" class="shut cursor-pointer"></span>
    <div class="address-info rl-bg-white rl-padding-left-default">
      <div class="items rl-clear rl-margin-bottom-xxxs">
        <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double"><em class="rl-text-blue-xs">*</em>{{$i18n.locale === 'zh'?$t('UserCenter.Location'):$t('Register.Country')}}</span>
        <div v-show="$i18n.locale === 'zh'" class="item rl-fl rl-relative rl-margin-right-mid">
          <el-select  v-model="provinceId" @change="getProvinceId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width:100px;">
            <el-option
              v-for="item in provinceList"
              :key="item.id"
              :label="$i18n.locale === 'zh' || !item.regionNameEn == true?item.regionName:item.regionNameEn"
              :value="item.id">
            </el-option>
          </el-select>
         
        </div>
        <div class="item rl-fl rl-relative rl-margin-right-mid" v-show="showCity === true">
          <el-select v-model="cityId" @change="getCityId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width:100px;">
            <el-option
              v-for="item in cityList"
              :key="item.id"
              :label="$i18n.locale === 'zh' || !item.regionNameEn == true?item.regionName:item.regionNameEn"
              :value="item.id">
            </el-option>
          </el-select>
         
        </div>
        <div class="item rl-fl rl-relative" v-show="showArea === true">
          <el-select v-model="districtId" :placeholder="$t('P.pleaseSelect')" size="mini" style="width:100px;">
            <el-option
              v-for="item in areaList"
              :key="item.id"
              :label="$i18n.locale === 'zh' || !item.regionNameEn == true?item.regionName:item.regionNameEn"
              :value="item.id">
            </el-option>
          </el-select>
         
        </div>
      </div>
      <div v-if="this.provinceId !== 2000000" class="items rl-clear rl-margin-bottom-xxxs">
        <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double"><em class="rl-text-blue-xs">*</em>{{$t('UserCenter.Address')}}</span>
        <div class="search-input rl-fl"><el-input size="mini" type="text"  v-model="addressInfos.address" :placeholder="$t('P.PleaseEnter')" /></div>
      </div>
      <div v-if="this.provinceId !== 2000000" class="items rl-clear rl-margin-bottom-xxxs">
        <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">{{$t('UserCenter.ZipCode')}}</span>
        <div class="search-input rl-fl"><el-input size="mini" type="text" v-model="addressInfos.zipCode" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')"/></div>
      </div>
      <div v-if="this.provinceId !== 2000000" class="items rl-clear rl-margin-bottom-xxxs">
        <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double"><em class="rl-text-blue-xs">*</em>{{$t('UserCenter.RecipientName')}}</span>
        <div class="search-input rl-fl"><el-input size="mini" type="text" v-model="addressInfos.userName" :placeholder="$t('P.PleaseEnter')" /></div>
      </div>
      <div v-if="this.provinceId !== 2000000" class="items rl-clear rl-margin-bottom-xxxs">
        <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double">{{$t('UserCenter.Tel')}}</span>
        <div class="search-input rl-fl"><el-input size="mini" type="text"  v-model="addressInfos.phone" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')" /></div>
      </div>
      <div v-if="this.provinceId !== 2000000" class="items rl-clear rl-margin-bottom-xxxs">
        <span class="rl-fl rl-text-xxs rl-tr rl-margin-right-double"><em class="rl-text-blue-xs">*</em>{{$t('UserCenter.Phone')}}</span>
        <div class="search-input rl-fl"><el-input size="mini" type="text" v-model="addressInfos.mobile" :placeholder="$t('P.PleaseEnter')" onkeyup="this.value=this.value.replace(/\D/g,'')" /></div>
      </div>

      <div v-if="this.provinceId === 2000000" class="items rl-clear rl-margin-bottom-xxxs" style="margin-top:15px; margin-bottom:15px;">
        <div class="rl-fl"><span style="width: 340px;color: #606266;font-size: 12px;word-wrap: break-word;text-align: left;margin-left:160px">{{$t('P.OverseasDeliveryTips')}}</span></div>
      </div>
    </div>
    <el-button class="mini-search-btn" style="width: 276px;margin-left:80px;" v-loading="loading" @click="modifyAddress">{{$t('P.Save')}}</el-button>
  </div>
</div>
</template>

<script>

import GD from '@/assets/js/globalData'
export default {
  name: 'editorAddress',
  props: {
    addressInfos: {
      type: Object
    }
  },
  data () {
    return {
      showcov: false, // 是否显示编辑地址信息
      address: '', // 添加地址
      zipCode: '',
      userName: '',
      mobile: '',
      phone: '',
      provinceId: '', // 省市区上级id
      cityId: '',
      districtId: '',
      provinceName: '',
      provinceType: false,
      cityName: '',
      cityType: false,
      showCity: false,
      areaName: '',
      areaType: false,
      showArea: false,
      provinceList: {},
      cityList: {},
      areaList: {},
      pageNo: 1,
      pageSize: 10000,
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB', // 语种
      loading: false
    }
  },
  watch: {
    cityId (val) {
      if (this.provinceId === 2000000) {
        for (let i = 0; i < this.cityList.length; i++) {
          if (val === this.cityList[i].id) {
            this.addressInfos.address = this.$i18n.locale === 'zh' || !this.cityList[i].regionNameEn == true ? this.cityList[i].regionName : this.cityList[i].regionNameEn
            this.addressInfos.userName === ''
            this.addressInfos.mobile === ''
            this.addressInfos.provinceId === ''
            this.addressInfos.zipCode === ''
            break
          }
        }
      }
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    stop () {
      // console.log('stop')
    },
    shutLog () {
      this.addressInfos.type = 0
      // this.$emit('bdd', this.addressInfos.type)
    },
    // 获取省级列表
    getProvince () {
      this.provinceList = []
      this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize}).then(res => {
        if (res.code === 0) {
          if (this.$i18n.locale === 'zh') {
            this.provinceList = res.regions
          } else {
            this.provinceList.push(res.regions[res.regions.length - 1])
            this.provinceId = this.provinceList[0].id
            this.getProvinceId(this.provinceId)
          }
          this.provinceType = !this.provinceType
          this.cityType = false
          this.areaType = false
        }
      })
    },
    // 获取省级ID
    getProvinceId (val) {
      // console.log(cityCode)
      this.cityList = ''
      this.areaList = ''
      this.cityName = ''
      this.areaName = ''
      this.cityId = ''
      this.districtId = ''
      this.showCity = true
      this.showArea = false
      this.provinceType = false
      // if (this.$i18n.locale === 'zh' || !province.regionNameEn === true) {
      //   this.provinceName = province.regionName
      // } else { this.provinceName = province.regionNameEn }
      this.provinceId = val
      // this.provinceId = cityCode
      this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize, parentId: this.provinceId}).then(res => {
        if (res.code === 0) {
          this.cityList = res.regions
          this.cityId = this.cityList[0].id
          if (this.$i18n.locale === 'zh' || !this.cityList[0].regionNameEn === true) {
            this.cityName = this.cityList[0].regionName
          } else { this.cityName = this.cityList[0].regionNameEn }
          if (this.cityList[0].haveNext === 1) {
            this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize, parentId: this.cityId}).then(res => {
              if (res.code === 0) {
                this.showArea = true
                this.areaList = res.regions
                this.districtId = this.areaList[1].id
                this.areaName = this.areaList[1].regionName
              }
            })
          } else {
            this.districtId = 0
          }
        }
      })
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
    getCityId (val) {
      this.areaList = ''
      this.districtId = ''
      this.cityType = false
      // if (this.$i18n.locale === 'zh' || !city.regionNameEn === true) {
      //   this.cityName = city.regionName
      // } else { this.cityName = city.regionNameEn }
      this.cityId = val
      this.areaName = ''
      // if (haveNext === 1) {
        this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize, parentId: this.cityId}).then(res => {
          if (res.code === 0) {
            this.areaList = res.regions
            if (res.regions.length > 0) {
              this.showArea = true
              this.districtId = this.areaList[1].id
              this.areaName = this.areaList[1].regionName
            } else {
              this.showArea = false
              this.districtId = 0
            }
          }
        })
      // } else {
      //   this.showArea = false
      //   this.districtId = 0
      // }
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
      this.districtId = cityCode
    },
    // 显示省市区地址
    addressShow () {
      if (this.addressInfos.districtId === 0) {
        this.showArea = false
      }
      this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize}).then(res => {
        if (res.code === 0) {
          this.provinceList = res.regions
          this.provinceList.forEach((goods) => {
            if (goods.id === this.addressInfos.provinceId) {
              this.provinceId = goods.id
              if (this.$i18n.locale === 'zh' || !goods.regionNameEn === true) {
                this.provinceName = goods.regionName
              } else { this.provinceName = goods.regionNameEn }
              this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize, parentId: this.addressInfos.provinceId}).then(res => {
                if (res.code === 0) {
                  this.cityList = res.regions
                  this.cityList.forEach((goods) => {
                    if (goods.id === this.addressInfos.cityId) {
                      this.cityId = goods.id
                      if (this.$i18n.locale === 'zh' || !goods.regionNameEn === true) {
                        this.cityName = goods.regionName
                      } else { this.cityName = goods.regionNameEn }
                      this.showCity = true
                      this.$api.get(this, 'region', {page: this.pageNo, count: this.pageSize, parentId: this.addressInfos.cityId}).then(res => {
                        if (res.code === 0) {
                          this.areaList = res.regions
                          this.areaList.forEach((goods) => {
                            if (goods.id === this.addressInfos.districtId) {
                              this.districtId = goods.id
                              this.areaName = goods.regionName
                              this.showArea = true
                            }
                          })
                        }
                      })
                    }
                  })
                }
              })
            }
          })
        }
      })
    },
    // 编辑收货地址
    modifyAddress () {
      this.loading = true
      var myDate = new Date()
      var pattern = /^1[3456789]\d{9}$/ // 手机号
      if (this.addressInfos.cityId === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请选择省市区')
        } else { this.$message.warning('Please choose province, city and district.') }
        return false
      }
      if (this.provinceId !== 2000000 && this.addressInfos.address === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入详细地址')
        } else { this.$message.warning('Please enter detailed address.') }
        return false
      }
      if (this.provinceId !== 2000000 && this.addressInfos.userName === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入收货人姓名')
        } else { this.$message.warning('Please enter recipient name.') }
        return false
      }
      if (this.provinceId !== 2000000 && this.addressInfos.mobile === '') {
        if (this.$i18n.locale === 'zh') {
          this.$message.warning('请输入手机号')
        } else { this.$message.warning('Please enter mobile phone number.') }
        return false
      }
      this.addressInfos.mobile = this.addressInfos.mobile.replace(/\D/g, '');
      if (this.provinceId !== 2000000 && this.$i18n.locale === 'zh') {
        if (this.addressInfos.mobile.length !== 11) {
          this.$message({
            type: 'warning',
            message: '请输入正确的手机号！'
          })
          return false
        }
        if (!pattern.test(this.addressInfos.mobile)) {
          this.$message({
            type: 'warning',
            message: '请输入正确的手机号！'
          })
          return false
        }
      }
      this.addressInfos.provinceId = this.provinceId
      this.addressInfos.cityId = this.cityId
      this.addressInfos.districtId = this.districtId
      var json = {id: this.addressInfos.id, provinceId: this.addressInfos.provinceId, cityId: this.addressInfos.cityId, districtId: this.addressInfos.districtId, address: this.addressInfos.address, zipCode: this.addressInfos.zipCode, userName: this.addressInfos.userName, mobile: this.addressInfos.mobile, phone: this.addressInfos.phone}
      this.$api.put(this, 'user/u/deliveryaddress?' + myDate.getMinutes() + myDate.getSeconds(), json).then(res => {
        this.loading = false
        if (res.code === 0) {
          this.$message.success(this.$t('P.ModifiedSuccess'))
          this.addressInfos.type = 0
          this.$emit('add', this.addressInfos.id)
        } else if (res.code === 3) {
         
        }
      })
    }
  },
  mounted () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    this.$forceUpdate()
    this.addressShow()
  }
}
</script>

<style scoped="scoped" lang='less'>
  /*弹框*/
  .cover{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #000;
    z-index: 99;
    opacity: 0.5;
  }
  .pro-cover{
    width: 555px;
    // height: 380px;
    border: 1px solid #ccc;
    border-radius: 5px;
    z-index: 99;
    background: #fefefe;
    text-align: center;
    display: table;
  }
  .cover-box{
    box-sizing: border-box;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    margin: auto;
    z-index: 99;
    .shut{
      position: absolute;
      top:-8px;
      right:-8px;
      display: block;
      width: 18px;
      height: 18px;
      background: url("../../src/assets/images/shut.png") no-repeat center center;
    }
    .address-info{
      .items{
        span{
          display: block;
          width: 120px;
          line-height: 30px;
        }
        .search-input{
          width: 328px;
          height: 28px;
          // border:1px solid #ccc;
          input{
            padding-top: 5px;
            padding-left: 20px;
            width: 308px;
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
            font-size: 12px;
            background: url("../../src/assets/images/selectUl.png") no-repeat 88px center;
            border-radius: 5px;
          }
          .selectUl{
            position: absolute;
            top:30px;
            left:0;
            z-index: 11;
            width: 85px;
            max-height: 250px;
            box-sizing: border-box;
            border: 1px solid #ccc;
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
        .search-choose{
          .yuan{
            margin-top: 4px;
            width: 14px;
            height: 14px;
          }
          .yuan-nogou{
            background: url("../../src/assets/images/yuan3.png") no-repeat center center;
          }
          .yuan-gou{
            background: url("../../src/assets/images/yuan4.png") no-repeat center center;
          }
          .save{
            line-height: 22px;
          }
        }
      }
    }
    .button{
      margin: 0 auto;
      width: 276px;
      span{
        margin-top: 20px;
        display: block;
        width: 330px;
        height: 35px;

        line-height: 35px;
        text-align: center;
        border: 1px solid #ccc;
        background-color: #00c9dc;
        color: #fff;

        border-radius: 5px;
      }
    }
  }
</style>
