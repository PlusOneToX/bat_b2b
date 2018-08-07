<template>
	<div class="user-edit" v-loading="loading">
		<el-form :model="formData" :rules="rules" label-width='20%' label-position="right" ref="formData">
			<el-row>
				<el-col :span="16">
					<el-col :span="24">
            <div>
              <div class="tip-left">
                <p><code>登录信息</code></p>
              </div>
              <el-tooltip content="用于账号登录，一旦创建成功，用户名无法修改" placement="right">
                <el-form-item label="用户名" prop="name">
                  <el-input v-model.trim="formData.name" size="mini" maxlength="20" clearable/>
                  <!-- <el-input v-model.trim="formData.name" size="mini" :disabled="nameShow" maxlength="20" clearable/> -->
                </el-form-item>
              </el-tooltip>
              <!-- <el-form-item label="性别" prop="contacts.sex">
                <el-radio-group v-model="formData.contacts.sex">
                  <el-radio :label="1" :disabled="exaShow">男</el-radio>
                  <el-radio :label="2" :disabled="exaShow">女</el-radio>
                  <el-radio :label="0" :disabled="exaShow">未知</el-radio>
                </el-radio-group>
              </el-form-item> -->
              <!-- <el-tooltip content="用于账号登录和找回密码" placement="right" v-if="formData.address.countryId===37">
                <el-form-item  label="登录手机" prop="contacts.phone">
                  <el-input  v-model="formData.contacts.phone" size="mini" :disabled="exaShow" clearable />
                </el-form-item>
              </el-tooltip> -->
              <!-- <el-tooltip content="用于账号登录和找回密码" placement="right" v-if="formData.address.countryId &&　formData.address.countryId!==37">
                <el-form-item label="邮箱" prop="contacts.email">
                  <el-input v-model="formData.contacts.email" size="mini" :disabled="exaShow" clearable />
                </el-form-item>
              </el-tooltip> -->
              <el-form-item label="" v-if="checkMsg == 1">
                <el-button size="mini" type="text" v-if="!amendShow" @click="amend" >修改密码</el-button>
                <el-button size="mini" type="text" v-if="amendShow" @click="CoamendShow">取消修改密码</el-button>
              </el-form-item>
              
              <el-form-item label="密码" prop="password" v-if="passShow">
                <el-input type="password" v-model="formData.password" size="mini" auto-complete="off" maxlength="16" :disabled="exaShow" clearable/>
              </el-form-item>

              <el-form-item label="确认密码" prop="checkPass" v-if="passShow">
                <el-input type="password" v-model="formData.checkPass" size="mini" auto-complete="off" maxlength="16" :disabled="exaShow" clearable/>
              </el-form-item>
            </div>
            <!----多级分销商详情---->
            <div v-if="node===2">
              <div class="tip-left">
							  <p><code>上级信息</code></p>
              </div>
              <el-form-item label="上级分销商">
                <el-input v-model="formData.upName"  size="mini" maxlength="100" :disabled="nameShow"  clearable/>
              </el-form-item>
              <el-form-item label="当前分销商级数">
                <el-input v-model="formData.treeNode"  size="mini" maxlength="30" :disabled="nameShow" clearable/>
              </el-form-item>
              <div class="tip-left">
                <p><code>基本信息</code></p>
              </div>
              <el-tooltip content="用于账号登录，一旦创建成功，用户名无法修改" placement="right">
                <el-form-item label="用户名" prop="name">
                  <el-input v-model.trim="formData.name" size="mini" :disabled="nameShow" maxlength="20" clearable/>
                </el-form-item>
              </el-tooltip>
              <el-tooltip content="客户为公司、个体户，必须是营业执照上的公司全称；客户为个人，必须是身份证上客户全名" placement="right">
                <el-form-item label="客户名称" prop="companyName"> <!-- 原“公司名/分销商名“ -->
                  <el-input v-model="formData.companyName" size="mini"  @change="blurCompanyName" maxlength="100" :disabled="exaShow" clearable/>
                </el-form-item>
              </el-tooltip>
              <el-form-item label="是否同步到ERP">
                <el-tooltip content="勾选是，创建/编辑分销商信息可自动同步到ERP；如果勾选否，创建/编辑分销商信息将不会同步到ERP" placement="right">
                  <el-radio-group v-model="formData.extendData.erpFlag">
                    <el-radio :label="1" :disabled="exaShow">是</el-radio>
                    <el-radio :label="0" :disabled="exaShow">否</el-radio>
                  </el-radio-group>
                </el-tooltip>
              </el-form-item>
            </div>
            
						<div class="tip-left" v-if="node===1">
							<p><code>基本信息</code></p>
						</div>
            <div class="tip-left" v-if="(node==2 && formData.extendData.erpFlag===1)">
							<p><code>ERP信息</code></p>
						</div>
            <el-form-item label="国家" prop="address.countryId">
              <el-select v-model="formData.address.countryId" placeholder="请选择" size="mini" :disabled="exaShow" filterable @change="countryChange($event)">
                <el-option v-for="country in this.RegionForChose.id" :key="country.id" :label="country.regionName" :value="country.id"> </el-option>
              </el-select>
              <el-select v-if="provinceShow" v-model="formData.address.provinceId" placeholder="请选择" size="mini" :disabled="exaShow" filterable @change="provinceIdChange($event)">
                <el-option v-for="province in this.RegionForChose.provinceId" :key="province.id" :label="province.regionName" :value="province.id"> </el-option>
              </el-select>
              <el-select  v-model="formData.address.cityId" v-if="cityShow" placeholder="请选择" size="mini" :disabled="exaShow" filterable @change="cityIdChange($event)">
                <el-option v-for="city in this.RegionForChose.cityId" :key="city.id" :label="city.regionName" :value="city.id"> </el-option>
              </el-select>
              <el-select  v-model="formData.address.districtId" v-if="townShow" placeholder="请选择" size="mini" :disabled="exaShow" filterable @change="districtIdChange($event)">
                <el-option v-for="town in this.RegionForChose.districtId" :key="town.id" :label="town.regionName" :value="town.id"> </el-option>
              </el-select>
            </el-form-item>
            <div v-if="(node===2 && formData.extendData.erpFlag===1) || node ===1">
              <el-form-item label="公司类型" prop="companyType">
                <el-radio-group v-model="formData.companyType" @change="companyTypeNo">
                  <el-radio :label="1" :disabled="exaShow">公司</el-radio>
                  <el-radio :label="2" :disabled="exaShow">个体工商户</el-radio>
                  <el-radio :label="3" :disabled="exaShow">个人</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="分销商内码">
                <el-tooltip content="如果ERP中已存在该分销商，则需填写ERP中的客户内码。如果不存在可不填写，创建成功会自动同步。" placement="right">
                  <el-input v-model="formData.extendData.erpId" onKeyUp="value=value.replace(/[\W]/g,'')" size="mini" maxlength="30" :disabled="exaShow" id="inputBlur" clearable/>
                </el-tooltip>
              </el-form-item>
              
              <el-form-item label="是否同步到ERP" v-if="node===1">
                <el-tooltip content="勾选是，创建/编辑分销商信息可自动同步到ERP；如果勾选否，创建/编辑分销商信息将不会同步到ERP" placement="right">
                  <el-radio-group v-model="formData.extendData.erpFlag" >
                    <el-radio :label="1" :disabled="exaShow">是</el-radio>
                    <el-radio :label="0" :disabled="exaShow">否</el-radio>
                  </el-radio-group>
                </el-tooltip>
              </el-form-item>
              
              <el-form-item label="分销商ID">
                <el-tooltip content="如果ERP中已存在该分销商，则需填写ERP中的分销商ID。如果不存在可不填写，创建成功会自动同步。" placement="right">
                  <el-input v-model="formData.extendData.erpNo" onKeyUp="value=value.replace(/[\W]/g,'')" size="mini" maxlength="30" :disabled="exaShow" clearable/>
                </el-tooltip>
              </el-form-item>
              
              <el-form-item label="客户名称" prop="companyName"  v-if="node===1">
                <el-tooltip content="客户为公司、个体户，必须是营业执照上的公司全称；客户为个人，必须是身份证上客户全名" placement="right">
                  <el-input v-model="formData.companyName" size="mini"  @change="blurCompanyName" maxlength="100" :disabled="exaShow" clearable/>
                </el-tooltip>
              </el-form-item>
              
              <el-form-item label="客户联系地址" prop="address.address">
                <!-- <el-tooltip content="客户联系地址" placement="right"> -->
                <el-input v-model="formData.address.address" size="mini" @change="blurAddress" :disabled="exaShow" clearable />	<!-- 原“联系地址” -->
                 <!-- </el-tooltip> -->
              </el-form-item>

              <el-form-item label="邮编"  prop="address.zipCode">
                <el-input v-model="formData.address.zipCode" size="mini" @change="blurZipCode"  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="20" :disabled="exaShow" clearable/>
              </el-form-item>

              <el-form-item :label="formData.companyType === 3 ? '身份证号' : '营业执照号'" prop="extendData.certNo"> <!-- 新增 -->
                <el-tooltip content="客户为公司、个体户，则填写营业执照号码；客户为个人，则填写身份证号码" placement="right">
                  <el-input v-model="formData.extendData.certNo" onKeyUp="value=value.replace(/[\W]/g,'')" size="mini" @change="blurCertNo" maxlength="30" :disabled="exaShow" clearable/>
                </el-tooltip>
              </el-form-item>
              

              <el-form-item label="分销商区域" prop="salesAreaIds" v-if="node===1">
                <el-select v-model="formData.salesAreaIds" multiple size="mini" placeholder="请选择分销商区域" :disabled="exaShow" style="width:200px;">
                  <el-option v-for="item in salesAreas" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="(checkMsg===3 || checkMsg===2) && (formData.extendData.comment!=='' || formData.extendData.comment!==null)"  label="备注" prop="extendData.comment">
                <div class="" else>{{formData.extendData.comment}}</div>
              </el-form-item>
            </div>
					</el-col>
				</el-col>
			</el-row>
		</el-form>
		</div>
</template>

<script>
/*
 * @Author: lijiemin
 * @Date: 2018-05-06 17:12:57
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-04-Th 01:42:52
 */
import eventBus from '@/views/order/eventBus'
import checkProcedure from '@/views/distributor/cooperating/cooperatingadd/components/checkProcedure'
import { setArr2 } from "@/utils/common.js";
export default {
  name: '',
  props: ['basicMessage', 'passShow', 'syncShow', 'exaShow', 'nameShow', 'checkMsg', 'node', 'flows', 'FirstReq', 'checkTyps'],
  components: {
    checkProcedure
  },
  computed: {
    disCheckType() { // 审批列表由谁审批的进来的页面
      return this.$route.query.checkType
    }
  },
  created() {
    this.loading = true
    let count = 2
    this.$http.regionListById(this, {
      page:1,
      size:200,
      parentId: 0
    }).then(res => {
      if (res.success) {
        this.RegionForChose.id = res.data.list
        count--
        if (count === 0) {
          this.loading = false
        }
        this.$http.regionListById(this, {
          page:1,
          size:1000,
          parentId: 37
        }).then(res => {
          this.RegionForChose.provinceId = res.data.list
          count--
          if (count === 0) {
            this.loading = false
          }
        })
      }
    })
  },
  mounted() {
    this.salearea() // 分销商区域
  },
  data() {
    var checkNameReg = (rule, value, callback) => { // 用户名限制
      if (!value) {
        return callback()
      }
      if (value) {
        setTimeout(() => {
          var reg = /^[\u4e00-\u9fa5_a-zA-Z0-9]{2,20}$/ // 中英文、数字、下划线!
          if (!reg.test(value)) {
            callback(new Error('2--20个字符，仅可为中英文、数字结合、下划线、区分大小写!'))
          } else {
            callback()
          }
        }, 500)
      }
    }

    var validatePass = (rule, value, callback) => { // 输入密码
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value) {
        setTimeout(() => {
          var reg = /^.{4,20}$/
          if (!reg.test(value)) {
            callback(new Error('密码为6-16个字符'))
          } else {
            callback()
          }
        }, 500)
      } else if (!value) {
        return callback()
      } else {
        if (this.formData.checkPass !== '') {
          this.$refs.formData.validateField('checkPass')
        }
        callback()
      }
    }

    var validatePass2 = (rule, value, callback) => { // 确认密码
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.formData.password) {
        this.passConfir = true
        callback(new Error('两次输入密码不一致!'))
      } else {
        this.passConfir = false
        callback()
      }
    }


    /**
     * 验证手机号
     * 施义煌
     * @param rule
     * @param value
     * @param callback
     */
    const validatePhone = (rule, value, callback) => {
        if (/^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$/.test(value) !== false) {
          callback();
        } else {
          callback(new Error('请输入正确的手机号'));
        }
    }

    /**
     * 验证邮箱
     * @param rule
     * @param value
     * @param callback
     */
    const validateEmail = (rule, value, callback) => {
          if (/^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z._]+\.[a-zA-Z]{2,4}$/.test(value) !== false || value === '' || value === null) {
            callback();
          } else {
            callback(new Error('请输入正确的邮箱'));
          }
    }
   
    /**
     * 验证国内客户省市县必填
     * @param rule
     * @param value
     * @param callback
     */
    const validateChinaProvince = (rule, value, callback) => {
      if (this.formData.address.countryId === 37) {
          if (!this.formData.address.provinceId) {
          callback(new Error('中国地区，请选择省市'));
        } else {
          callback();
        }
      } else {
        callback();
      }
    }

    return {
      passConfir: false,
      rest: false,
      loading: false,
      provinceShow: false,
      cityShow: false,
      townShow: false,
      taxTypeShow: false, // 税种类型
      amendShow: false,
      regionParams: { // 用来请求region数据  地区信息，省份
        parentId: 0,
        count: 300,
        page: 1
      },
      RegionForChose: { // 被选region
        countryId: [],
        provinceId: [],
        cityId: [],
        districtId: []
      },
      pageInfo: {
        page: 1,
        count: 10,
        freezeStatus: 0
      },
      erpId: '',
      FirstChina: true, // 生命周期加载初始数据时，cityId不会被watcher清洗
      salesAreas: [], // 分销商区域
      formData: {
        name: '', // 用户名
        password: '', // 密码
        checkPass: '', // 确认密码
        companyName: '', // 客户名称
        registerName: '', // 账户操作人
        procureName: '', // 采购联系人
        procurePhone: '', // 采购联系电话
        companyRepresent: '', // 公司负责人
        companyRepresentPhone: '', // 公司负责人联系电话
        phone: '', // 账户操作人电话
        procureEmail: '', // 公司邮箱
        address: {
          countryId: '', // 国家id
          provinceId: '', // 省份id
          cityId: '', // 城市id
          districtId: '', // 区id
          addressType: 1,
          address: '', // 客户联系地址
          zipCode: '' // 邮编
        },
        contacts: {
          phone: '', // 登录手机
          email: '', // 电子邮件
          sex: 1  // 性别
        },
        extendData: {
          erpFlag: 1, // 是否同步到ERP 1是 2否
          erpId: '', // ERP内码
          certNo: '', // 营业执照号/身份证号
        },
        salesAreaIds: [], // 分销商区域
        companyType: 1, // 公司类型
        taxType: 1, // 税种类型
        comment: '', // 审批备注
        capitalStatus: 0 // 审批状态
      },
      rules: { // 必填输入提示
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { validator: checkNameReg.trim }
        ],
        password: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
        procureEmail: [
          { validator: validateEmail }
        ],
        companyName: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        extendData: {
          certNo: [
            { required: true, message: '请输入营业执照号/身份证号', trigger: 'blur' }
          ]
        },
        registerName: [
          { required: true, message: '请输入账户操作人姓名', trigger: 'blur' }
        ],
        procureName: [
          { required: true, message: '请输入采购人姓名', trigger: 'blur' }
        ],
        procurePhone: [
          { required: true, message: '请输入采购联系电话', trigger: 'blur' },
        ],
        phone: [
          { required: true, message: '请输入账户操作人电话', trigger: 'blur' }
        ],
        address: {
          countryId: [
            { required: true, message: '请选择国家', trigger: 'change' }
          ],
          provinceId: [
            { required: true, message: '请选择省市', trigger: 'change' },
            { validator: validateChinaProvince}
          ]
          // address: [
          //   { required: true, message: '请输入客户联系地址', trigger: 'blur' }
          // ],
          // zipCode: [
          //   { required: true, message: '请输入邮编', trigger: 'blur' }
          // ]
        },
        contacts: {
          phone: [
            { required: true, message: '请输入登录手机号', trigger: ['blur']},
            { validator: validatePhone }
          ],
          email: [
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
            { validator: validateEmail }
          ],
          sex: [
            { required: true, message: '请选择性别', trigger: 'change' }
          ]
        },
        salesAreaIds: [
          { required: true, message: '请选择分销商区域', trigger: 'change' }
        ],
        companyType: [
          { required: true, message: '请选择公司类型', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    // ======== 操作 ========
    countryChange(event) { // 国家
      this.formData.address.countryId = event
      if (event !== 37) { // 国外
        this.provinceShow = false
        this.cityShow = false
        this.townShow = false
        this.formData.address.provinceId = ''
        this.formData.address.cityId = ''
        this.formData.address.districtId = ''
        // this.formData.contacts.email = ''
        this.taxTypeShow = false // 不是中国隐藏税种类型
      } else if (event === 37) { // 37中国
        this.provinceShow = true // 中国显示省份
        this.formData.address.provinceId = ''
        this.taxTypeShow = true // 中国显示税种类型
      }
      eventBus.$emit('countryId', {countryId: this.formData.address.countryId})
    },
    provinceIdChange(event) { // 省
      this.$http.regionListById(this, {
        page:1,
        size:200,
        parentId: event
      }).then(res => {     
        if (res.success) {
          this.cityShow = true
          this.RegionForChose.cityId = res.data.list
          this.formData.address.cityId = res.data.list[0].id
          if (res.data.list[0].haveNext === 1) {
            this.$http.regionListById(this, {
              page:1,
              size:200,
              parentId: this.formData.address.cityId
            }).then(res => {    
              this.RegionForChose.districtId = res.data.list
              this.formData.address.districtId = this.RegionForChose.districtId[1].id
            })
            this.townShow = true
          } else {
            this.townShow = false
            this.formData.address.districtId = 0
          }
        }
      })
    },
    cityIdChange(event) { // 市
      this.$http.regionListById(this, {
        page:1,
        size:200,
        parentId: event
      }).then(res => {      
        if (res.success) {
          if (res.data.list.length === 0) {
            this.townShow = false
            this.formData.address.districtId = 0
          } else {
            this.RegionForChose.districtId = res.data.list
            this.formData.address.districtId = this.RegionForChose.districtId[1].id
            this.townShow = true
          }
        }
      })
    },
    districtIdChange (event) {
      this.formData.address.districtId = event
    },
    handleSubmit(formData) { // 必填验证
      this.$refs['formData'].validate(valid => {
        // eslint-disable-next-line no-empty
        if (!valid) {
          this.$message({
            message: '请先将分销商基本信息资料补充完整',
            type: 'error',
            duration: 3 * 1000,
            onClose: () => {}
          })
        }
        eventBus.$emit('addSurely', { valid: valid })
      })
    },
    // ======== 数据 ========
    amend() { // 修改密码
      eventBus.$emit('amendEmit')
      this.amendShow = true
    },

    CoamendShow() { // 取消修改密码
      eventBus.$emit('CoamendEmit')
      this.amendShow = false
    },

    blurCompanyName() {
      eventBus.$emit('blurCompanyName', {
        companyName: this.formData.companyName // 客户名称
      })
    },

    blurAddress() {
      eventBus.$emit('blurAddress', {
        address: this.formData.address.address // 客户联系地址
      })
    },

    blurZipCode() {
      eventBus.$emit('blurZipCode', {
        zipCode: this.formData.address.zipCode // 邮编
      })
    },

    blurCertNo() {
      eventBus.$emit('blurCertNo', {
        certNo: this.formData.extendData.certNo, // 营业执照号/身份证号
        companyType: this.formData.companyType // 公司类型 1：公司 2：个体工商户3：个人
      })
    },

    companyTypeNo() {
      eventBus.$emit('companyTypeNo', {
        companyType: this.formData.companyType, // 公司类型
        certNo: this.formData.extendData.certNo // 营业执照号/身份证号
      })
    },

    salearea() { // 分销商区域
       this.$http.getSalesareaPoList(this, { page:1, size:10000, openFlag: 1}).then(res => {
        if (res.success) {
          this.salesAreas = res.data.list
        }
      })
    }
  },
  watch: {
    basicMessage(val) {
      console.log('----shuju数据：',val);
      this.formData = val
      // this.formData.salesAreaIds = this.formData.salesAreaIds ? setArr2(this.formData.salesAreaIds) :[] // 去重
       this.formData.salesAreaIds =  setArr2(this.formData.salesAreaIds)// 去重
      this.formData.companyType = this.formData.companyType ? this.formData.companyType : 1
      if (this.formData.address) {
        if (!this.formData.address.districtId) {
          this.$set(this.formData.address, 'districtId', 0)
        }
        if (this.formData.address.countryId === 37) { // 国家
          this.provinceShow = true
          this.taxTypeShow = true // 中国显示税种类型
          if (this.formData.address.provinceId) {
             this.$http.regionListById(this, {
              page:1,
              size:200,
              parentId: this.formData.address.provinceId
            }).then(res => {  
              res.data.list.forEach(item => {
                if (this.formData.address.cityId === item.id) {
                  this.cityShow = true
                  this.RegionForChose.cityId = res.data.list
                  this.$http.regionListById(this, {
                    page:1,
                    size:200,
                    parentId: this.formData.address.cityId
                  }).then(res => {    
                    if (this.formData.districtId === 0) {
                      this.townShow = false
                      this.formData.address.districtId = 0
                    } else {
                      res.data.list.forEach(item => {
                        if (this.formData.address.districtId === item.id) {
                          this.townShow = true
                          this.RegionForChose.districtId = res.data.list
                        }
                      })
                    }
                  })
                }
              })
            })
          }
         
        } else if (this.formData.countryId !== 1) {
          // this.taxTypeShow = false // 不是中国隐藏税种类型
          this.formData.procureEmail = (this.formData.procureEmail==='' || this.formData.procureEmail===null) ? this.formData.contacts.email : this.formData.procureEmail
        }
      } else {
        this.$set(this.formData, 'address', {
          countryId: '',
          provinceId: '',
          cityId: '', 
          districtId: 0,
          addressType: 1,
          address: '',
          zipCode: ''
        })
      }
      if (this.formData.extendData.erpId) {
        this.formData.extendData.erpId = this.formData.extendData.erpId === 0 ? '' : this.formData.extendData.erpId
      } else {
        this.$set(this.formData.extendData, 'erpId', '')
      }
      console.log(this.formData);
    },
    nameShow(val) {
      this.disabled = val
    },
    'formData.extendData.erpFlag': { // 是否同步到ERP
      handler(val) {
        this.$emit('erp', val)
      },
      deep: true
    }
  }
}
</script>
