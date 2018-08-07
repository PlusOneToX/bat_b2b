<template>
	<div class="user-edit">
		<el-form :model="formData" status-icon :rules="rules" label-width='20%' label-position="right" ref="formData">
			<el-row>
				<el-col :span="15">
					<el-col :span="24">
						<div class="tip-left">
							<p><code>银行账户信息</code></p>
						</div>

						<el-form-item label="银行账户名" prop="financial.bankAccountName">
							<el-input v-model="formData.financial.bankAccountName" size="mini" maxlength="100" :disabled="exaShow" />
						</el-form-item>

						<el-form-item label="开户行" prop="financial.bankDepositName">
							<el-input v-model="formData.financial.bankDepositName" size="mini" maxlength="100" :disabled="exaShow" />
						</el-form-item>

						<el-form-item label="银行账户" prop="financial.bankAccount">
							<el-input v-model="formData.financial.bankAccount" type="number" onKeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"  size="mini" maxlength="30" :disabled="exaShow" />  <!-- 原‘银行账号’ -->
						</el-form-item>

						<!-- <div class="tip-left">
							<p><code>财务联系人</code></p>
						</div>

						<el-form-item label="联系人姓名" prop="billCheckName">
							<el-input size="mini" v-model="formData.billCheckName" maxlength="100" :disabled="exaShow" />
						</el-form-item>

						<el-form-item label="联系人电话" prop="billCheckPhone">
							<el-input size="mini" v-model="formData.billCheckPhone" maxlength="50" :disabled="exaShow" />
						</el-form-item>

						<el-form-item label="联系人邮箱" prop="billCheckEmail">
							<el-input size="mini" v-model="formData.billCheckEmail" maxlength="50" :disabled="exaShow" />
						</el-form-item> -->

						<div class="tip-left">
							<p><code>开票信息</code></p>
						</div>
            <el-form-item label="税种类型">
              <el-radio-group v-model="formData.financial.taxType">
                <el-radio :label="1" :disabled="exaShow">一般纳税人</el-radio>
                <el-radio :label="2" :disabled="exaShow">小规模纳税人</el-radio>
                <el-radio :label="3" :disabled="exaShow">个人</el-radio>
              </el-radio-group>
						</el-form-item>
						<el-form-item label="开票公司名称">
              <!-- 原‘发票抬头’ -->
							<!-- <el-input v-model="formData.invoiceTitleName" size="mini" :disabled="disableIsNameShow" />  -->
              <el-input v-model="formData.financial.invoiceTitleName" size="mini" :disabled="exaShow" />
						</el-form-item>

						<el-form-item label="纳税人识别码" v-if="companyTypeNoShow">
							<el-input v-model="formData.financial.taxpayerNumber" size="mini" :disabled="exaShow" />
						</el-form-item>

						<el-tooltip content="开票对应地址" placement="right">
							<el-form-item label="开票地址">
								<el-input v-model="formData.financial.registeredAddress" size="mini" maxlength="200" :disabled="exaShow" /> <!-- 原“增值税发票注册地址” -->
							</el-form-item>
						</el-tooltip>

						<el-tooltip content="开票对应电话" placement="right">
							<el-form-item label="电话"> <!-- 原“增值税发票注册电话” -->
								<el-input v-model="formData.financial.registeredPhone" size="mini" maxlength="13" :disabled="exaShow" />
							</el-form-item>
						</el-tooltip>

						<el-tooltip content="开户行名称" placement="right">
							<el-form-item label="开户行">
								<el-input size="mini" v-model="formData.financial.registeredBankDepositName" maxlength="100" :disabled="exaShow" />
							</el-form-item>
						</el-tooltip>

						<el-tooltip content="开票银行账户" placement="right">
							<el-form-item label="账户">
								<el-input size="mini" v-model="formData.financial.registeredBankAccount" maxlength="30" :disabled="exaShow" />
							</el-form-item>
						</el-tooltip>

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
 * @Last Modified time: 2018-05-Mo 02:23:01
 */
import eventBus from '@/views/order/eventBus'
export default {
  props: ['basicMessage', 'disableIsNameShow', 'exaShow', 'checkMsg'],
  mounted() {
    eventBus.$on('blurCompanyName', payLoad => {
      // this.formData.companyName = payLoad.companyName // 公司
    })
    eventBus.$on('blurCertNo', payLoad => { // companyType公司类型 1：公司 2：个体工商户3：个人
      if (payLoad.companyType === 1 || payLoad.companyType === 2) {
        this.formData.taxpayerNumber = payLoad.taxpayerNumber // 纳税人识别号
        this.companyTypeNoShow = true
      } else if (payLoad.companyType === 3) {
        this.formData.taxpayerNumber = ''
        this.companyTypeNoShow = false
      }
    })
    eventBus.$on('companyTypeNo', payLoad => { // 根据公司类型进行纳税人识别号的显示
      if (payLoad.companyType === 1 || payLoad.companyType === 2) {
        this.companyTypeNoShow = true
        this.formData.taxpayerNumber = payLoad.taxpayerNumber
      } else if (payLoad.companyType === 3) {
        this.companyTypeNoShow = false
        this.formData.taxpayerNumber = ''
      }
    })
  },
  data() {
    return {
      FirstReq: true, // 生命周期加载初始数据时，invoice不会被watcher清洗
      companyTypeNoShow: true,
      formData: {
        financial:{
          bankAccountName: '', // 银行账户名
          bankDepositName: '', // 开户行
          bankAccount: '', // 银行账户 (原‘银行账号’)
          // billCheckName: '', // 对账人姓名
          // billCheckPhone: '', // 对账人联系电话
          // billCheckEmail: '', // 对账人邮箱
          taxType: 1, // 税种类型
          invoiceTitleName: '', // 开票公司名称
          taxpayerNumber: '', // 纳税人识别号
          registeredPhone: '', // 固定电话 (原‘增值税发票注册电话’)
          registeredAddress: '', // 地址(原'增值税发票注册地址')
          registeredBankDepositName: '', // 发票开户银行
          registeredBankAccount: '' // 发票银行账户
        }
      },
      rules: { // 必填输入提示
        financial: {
          bankDeposit: [
            { required: true, message: '请输入开户银行', trigger: 'blur' }
          ],
          bankAccountName: [
            { required: true, message: '请输入银行账户名', trigger: 'blur' }
          ],
          bankDepositName: [
            { required: true, message: '请输入开户行全称', trigger: 'blur' }
          ],
          bankAccount: [
            { required: true, message: '请输入银行账号', trigger: 'blur' }
            // { validator: validateCard }
          ],
          taxpayerNumber: [
            { required: true, message: '请输入纳税人识别号', trigger: 'blur' }
          ]
        },
        phone: [
          { required: true, message: '请输入客户联系人电话', trigger: 'blur' }
        ],
       
        billCheckName: [
          { required: true, message: '请输入对账人姓名', trigger: 'blur' }
        ],
        billCheckPhone: [
          { required: true, message: '请输入对账人联系电话', trigger: 'blur' }
        ],
        billCheckEmail: [
          { required: true, message: '请输入对账人邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ]
      }
    }
  },
  methods: {
    // ======== 操作 ========
    handleSubmit(formData) { // 必填验证操作
      this.$refs['formData'].validate(valid => {
        // eslint-disable-next-line no-empty
        if (!valid) {
           this.$message({
            message: '请先将分销商账户信息资料补充完整',
            type: 'error',
            duration: 3 * 1000,
            onClose: () => {}
          })
        }
        eventBus.$emit('accSurely', { valid: valid })
      })
    }
  },
  watch: {
    basicMessage: { // 账户信息详情
      handler(val) {
        if (val.financial) {
          this.formData.financial.invoiceTitleName = val.financial.invoiceTitleName  // 开票公司名称
          this.formData.financial.bankAccountName = val.financial.bankAccountName // 银行账户名
          this.formData.financial.bankDepositName = val.financial.bankDepositName // 开户行
          this.formData.financial.bankAccount = val.financial.bankAccount // 银行账户 (原‘银行账号’)
          if (val.companyType === 3) { // 公司类型 1：公司 2：个体工商户3：个人
            this.formData.financial.taxpayerNumber = '' // 纳税人识别号
            this.companyTypeNoShow = false
          } else {
            this.formData.financial.taxpayerNumber = val.financial.taxpayerNumber // 纳税人识别号
            this.companyTypeNoShow = true
          }
          this.formData.financial.registeredPhone = val.financial.registeredPhone // 开票对应电话 (原‘增值税发票注册电话’)
          this.formData.financial.registeredAddress = val.financial.registeredAddress // 开票对应地址( 原'增值税发票注册地址')
          this.formData.financial.registeredBankDepositName = val.financial.registeredBankDepositName // 开票对应银行
          this.formData.financial.registeredBankAccount = val.financial.registeredBankAccount // 开票对应账户
        }
      },
      // deep: true,
      immediate: true
    },
    disableIsNameShow(val) { // 是否禁止输入
      this.disabled = val
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" sceopd>
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  }
  input[type="number"]{
    -moz-appearance: textfield;
  }
</style>
