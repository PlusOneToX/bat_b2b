<template>
  <div class="user-edit">
    <el-form :model="formData" status-icon :rules="rules" label-width='20%' label-position="right" ref="formData"
             v-loading="loading">
      <el-row>
        <el-col :span="15">
          <el-col :span="24">
            <div class="tip-left">
              <p><code>权限信息</code></p>
            </div>
            <!-- <div v-if="(node===2&&formData.extendData.erpFlag===1) || node===1"> -->
            <div>
              <el-form-item label="业务员" prop="business.salesId">
                <el-select v-model="formData.business.salesId" size="mini" placeholder="请选择业务员" clearable
                           :disabled="exaShow" filterable>
                  <el-option v-for="item in admins" :key="item.id" :label="item.userName" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="分销商分组">
                <el-select v-model="distributorGroupIds" multiple size="mini" placeholder="请选择分销商分组" clearable
                           :disabled="exaShow" filterable style="width: 100%">
                  <el-option v-for="item in groups" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="分销商类别" prop="business.distributorCategoryId">
                <el-select v-model="formData.business.distributorCategoryId" size="mini" placeholder="请选择分销商类别"
                           clearable :disabled="exaShow || autoShow">
                  <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="收款条件" prop="financial.tradeId">
                <el-select v-model="formData.financial.tradeId" size="mini" placeholder="请选择收款条件" clearable
                           :disabled="exaShow" filterable>
                  <el-option v-for="item in settleAccounts" :key="item.id" :label="item.name" :value="item.id" @click.native="changeAccounts(item)">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="币种" prop="financial.coinType">
                <el-select v-model="formData.financial.coinType" size="mini" placeholder="请选择币种" clearable
                           :disabled="exaShow">
                  <el-option v-for="item in state" :key="item.id" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="是否直发">
                <el-tooltip content="勾选是，分销商下单成功后将自动下推出库" placement="right">
                  <el-radio-group v-model="formData.business.autoDelivery">
                    <el-radio :label="1" :disabled="exaShow">是</el-radio>
                    <el-radio :label="0" :disabled="exaShow">否</el-radio>
                  </el-radio-group>
                </el-tooltip>
              </el-form-item>
              <el-form-item label="商品导出">
                <el-tooltip content="勾选是，分销商在采购中心导出商品报价单列表" placement="right">
                  <el-radio-group v-model="formData.business.canExportPrice">
                    <el-radio :label="1" :disabled="exaShow">是</el-radio>
                    <el-radio :label="0" :disabled="exaShow">否</el-radio>
                  </el-radio-group>
                </el-tooltip>
              </el-form-item>
            </div>
            <el-form-item label="参与活动" v-if="node===1"
                          v-bind:class="formData.business.promotionScope === 1 ? 'promotionScope1':'promotionScope2'">
              <el-radio-group v-model="formData.business.promotionScope" :disabled="exaShow">
                <el-radio :label="0">不参与活动</el-radio>
                <el-radio :label="1">全部活动</el-radio>
                <el-radio :label="2">指定活动类型</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-if="formData.business.promotionScope === 2 && node===1">
              <el-checkbox-group v-model="formData.business.promotionTypes" :disabled="exaShow"
                                 style="margin-left: 80px;">
                <el-checkbox label="1">营销活动</el-checkbox>
                <el-checkbox label="2">阶梯活动</el-checkbox>
                <el-checkbox label="3">拼团活动</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="是否开启C端模式" prop="extendData.customerFlag">
              <el-radio-group v-model="formData.extendData.customerFlag" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否启用柔性店铺" prop="business.rxShopSwitch" v-if="formData.extendData.customerFlag===1">
              <el-radio-group v-model="formData.business.rxShopSwitch" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="C端结算模式" prop="extendData.customerMode" v-if="formData.extendData.customerFlag===1">
              <el-radio-group v-model="formData.extendData.customerMode" :disabled="exaShow">
                <el-radio :label="1">平台收款</el-radio>
                <el-radio :label="2" v-if="node===2">上级收款</el-radio>
                <el-radio :label="3">自己收款</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否开启分账" prop="extendData.subAccountFlag" v-if="formData.extendData.customerMode===3">
              <el-radio-group v-model="formData.extendData.subAccountFlag" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否短信提示物流信息" prop="business.logisticsSmsSwitch">
              <el-radio-group v-model="formData.business.logisticsSmsSwitch" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否支持在途" prop="business.onWayFlag">
              <el-radio-group v-model="formData.business.onWayFlag" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否开启分销" prop="extendData.distributionFlag">
              <el-radio-group v-model="formData.extendData.distributionFlag" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="分销模式" prop="extendData.distributionMode"
                          v-if="formData.extendData.distributionFlag===1">
              <el-radio-group v-model="formData.extendData.distributionMode" :disabled="exaShow">
                <el-radio :label="1">平台方收款</el-radio>
                <el-radio :label="2" v-if="node===2">上级收款</el-radio>
                <el-radio :label="3">自己收款</el-radio>
              </el-radio-group>
            </el-form-item>
<!--            <el-form-item label="上级付款方式" prop="extendData.distributionPayWay"
                          v-if="formData.extendData.distributionMode===3">
              <el-radio-group v-model="formData.extendData.distributionPayWay" :disabled="exaShow">
                <el-radio :label="1">支付宝</el-radio>
                <el-radio :label="2">微信</el-radio>
                <el-radio :label="6">银行收款（快钱）</el-radio>
              </el-radio-group>
            </el-form-item> -->
			
			<el-form-item label="上级付款方式" prop="extendData.distributionPayWay"
			                          v-if="formData.extendData.distributionMode === 3 ">
				<el-radio-group v-model="formData.extendData.distributionPayWay" :disabled="exaShow">
					<!-- 分销商付款方式：0默认继承上一个付款人 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付 -->
					<el-radio :label="0">默认</el-radio>
					<el-radio :label="1">支付宝</el-radio>
					<el-radio :label="2">微信</el-radio>
					<el-radio :label="3" v-if="payWay !== 1">区间结算</el-radio>
					<el-radio :label="4">线下转账</el-radio>
					<el-radio :label="5" v-if="payWay !== 2">余额支付</el-radio>
					<el-radio :label="6">快钱支付</el-radio>
					<el-radio :label="7">余额+支付宝</el-radio>
					<el-radio :label="8">余额+微信</el-radio>
					<el-radio :label="9">余额+快钱支付</el-radio>
				</el-radio-group>
			</el-form-item>
            <el-form-item label="分销订单审核方式" prop="extendData.distributionAutoFlag"
                          v-if="formData.extendData.distributionFlag===1">
              <el-radio-group v-model="formData.extendData.distributionAutoFlag" :disabled="exaShow">
                <el-radio :label="1">自动审核</el-radio>
                <el-radio :label="0">人工审核</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="分销活动是否同步" prop="extendData.distributionPromotionFlag">
              <el-radio-group v-model="formData.extendData.distributionPromotionFlag" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="erp余额是否同步" prop="extendData.erpBalanceFlag"
                          v-if="formData.extendData.erpFlag===1 || node===1">
              <el-radio-group v-model="formData.extendData.erpBalanceFlag" :disabled="exaShow">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <div v-if="node===1">
              <div class="tip-left">
                <p><code>价格等级</code></p>
              </div>
              <el-form-item label="默认购买价格等级" prop="business.scalePriceId">
                <el-tooltip content="分销商的默认可享有的商品价格等级" placement="right">
                  <el-select v-model="formData.business.scalePriceId" size="mini" placeholder="请选择默认价格等级" clearable
                             :disabled="exaShow">
                    <el-option v-for="item in grades" :key="item.id + 'default'" :label="item.name"
                               :value="item.id"></el-option>
                  </el-select>
                </el-tooltip>
              </el-form-item>
              <el-form-item label="默认分销价格等级" prop="business.distributionScalePriceId"
                            v-if="formData.extendData.distributionFlag===1">
                <el-tooltip content="分销商的默认可享有的商品价格等级" placement="right">
                  <el-select v-model="formData.business.distributionScalePriceId" size="mini" placeholder="请选择默认分销价格等级"
                             clearable :disabled="exaShow">
                    <el-option v-for="item in grades" :key="item.id + 'default'" :label="item.name"
                               :value="item.id"></el-option>
                  </el-select>
                </el-tooltip>
              </el-form-item>
              <el-form-item label="品牌价格等级">
                <el-button class="mini-search-btn" @click="addGoods" :disabled="exaShow">添加品牌</el-button>
                <span class="place_holder">(名称/等级都需选择才能保存成功)</span>
              </el-form-item>

              <el-form-item v-if="addBrand">
                <table class="productData">
                  <tr>
                    <th>品牌名称</th>
                    <th>购买价格等级</th>
                    <th v-show="formData.extendData.distributionFlag===1">分销价格等级</th>
                    <th>操作</th>
                  </tr>
                  <tr v-for="(item, index) in arr" :key="index" :vlaue="item.id">
                    <td prop="territory">
                      <el-form-item>
                        <el-select v-model="item.brandId" size="mini" placeholder="请选择" class="brand-select"
                                   @change="changes" :disabled="exaShow">
                          <el-option style="min-width:160px" v-for="(brand, index) in brands" :key="index"
                                     :label="brand.name" :value="brand.id" :disabled="brand.disabled"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td>
                      <el-form-item>
                        <el-select v-model="item.scalePriceId" size="mini" placeholder="请选择" class="brand-select"
                                   :disabled="exaShow">
                          <el-option v-for="(grade, index) in grades" :key="index" :label="grade.name"
                                     :value="grade.id"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td v-if="formData.extendData.distributionFlag===1">
                      <el-form-item>
                        <el-select v-model="item.distributionScalePriceId" size="mini" placeholder="请选择"
                                   class="brand-select" :disabled="exaShow">
                          <el-option v-for="(grade, index) in grades" :key="index" :label="grade.name"
                                     :value="grade.id"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td>
                      <el-button class="mini-delete-btn" @click="delectBrand(item)" :disabled="exaShow">删除</el-button>
                    </td>
                  </tr>
                </table>
              </el-form-item>

              <el-form-item v-show="false" label="品类价格等级">
                <el-button type="primary" size="mini" @click="product" :disabled="exaShow">添加品类</el-button>
                <span class="place_holder">(名称/产品线/等级都需选择才能保存成功)</span>
              </el-form-item>

              <el-form-item v-show="false" v-if="addShow">
                <table class="productData">
                  <tr>
                    <th>品牌名称</th>
                    <th>品类</th>
                    <th>价格等级</th>
                    <th>操作</th>
                  </tr>
                  <tr v-for="(item,index) in arr2" :key="item.id" :vlaue="item.id">
                    <td>
                      <el-select v-model="item.brandId" size="mini" class="brand-select" placeholder="请选择"
                                 @change="brandChange(item.brandId,index)" :disabled="exaShow">
                        <el-option v-for="item in brandsAss" :key="item.id" :label="item.title" :value="item.id"
                                   :disabled="item.disabled"></el-option>
                      </el-select>
                    </td>
                    <td>
                      <el-select v-model="item.productlineId" class="brand-select" size="mini" placeholder="请先选择品类"
                                 :disabled="exaShow">
                        <el-option v-for="item in productlinelists[index]" :key="item.id" :value="item.id"
                                   :label="item.name"></el-option>
                      </el-select>
                    </td>
                    <td>
                      <el-select v-model="item.scalePriceId" size="mini" placeholder="请选择" :disabled="exaShow">
                        <el-option v-for="item in grades" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                      </el-select>
                    </td>
                    <td>
                      <el-button class="mini-delete-btn" @click="delectProcuct(index)" :disabled="exaShow">删除
                      </el-button>
                    </td>
                  </tr>
                </table>
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
 * @Date: 2018-05-06 17:16:08
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-06-Th 05:12:43
 */
import eventBus from '@/views/order/eventBus'

export default {
  name: '',
  props: ['basicMessage', 'erpFlag', 'exaShow', 'checkMsg', 'node', 'checkTyps'],
  data() {
    return {
	  payWay: 1, //付款方式
      autoShow: false,
      addBrand: false,
      addShow: false,
      loading: true,
      count: 6,
      admins: [], // 业务员
      groups: [], // 分销商分组
      categories: [], // 分销商类别
      settleAccounts: [], // 收款条件
      grades: [], // 默认价格等级
      arr: [], // 添加品牌arr
      arr2: [], // 添加品牌价格等级
      delArr: [], //删除的品牌
      brands: [], // 产品线品牌下拉数据
      productlinelists: [[]], // 产品线选择请先选择品牌
      brandsAss: [], // 深拷贝的品牌名称数组
      formData: {
        business: {
          salesId: '', // 业务员
          distributorGroupIds: '', // 分销商分组
          distributorCategoryId: '', // 分销商类别
          autoDelivery: 0, // 是否直发
          canExportPrice: 0, // 商品导出
          promotionScope: 1, // 参与活动
          promotionTypes: [],// 活动类型选择
          rxShopSwitch: 0, // 是否启用柔性店铺
          logisticsSmsSwitch: 0, // 是否短信提示物流信息
          onWayFlag: 1, // 是否支持在途
          scalePriceId: '', // 默认价格等级
          distributionScalePriceId: '' // 默认分销模式价格等级
        },
        extendData: {
          erpFlag: 0,
          distributionFlag: 0,
          distributionMode: 1,
          distributionPayWay: 0,
          distributionAutoFlag: 1, // 分销订单是否自动审核
          distributionPromotionFlag: 0, // 分销活动是否同步
          erpBalanceFlag: 1, // ERP余额是否同步
          customerFlag: 0, // 是否开启C端模式
          customerMode: 1, // C端结算模式
          subAccountFlag: 0, // 是否开启分账
        },
        financial: {
          tradeId: '', // 收款条件
          coinType: '', // 币种
        },

      },
      state: [
        {value: 1, label: '人民币'},
        {value: 2, label: '美元'}
      ],
      rules: {
        business: {
          salesId: [
            {required: true, message: '请选择业务员名字', trigger: 'change'}
          ],
          distributorCategoryId: [
            {required: true, message: '请选择分销商类别', trigger: 'change'}
          ],
          onWayFlag: [
            {required: true, message: '请选择是否支持在途库存', trigger: 'change'}
          ],
          scalePriceId: [
            {required: true, message: '请选择默认购买价格等级', trigger: 'change'}
          ],
          distributionScalePriceId: [
            {required: true, message: '请选择默认分销价格等级', trigger: 'change'}
          ],
          autoDelivery: [
            {required: true, message: '请选择是否直发', trigger: 'change'}
          ],
          canExportPrice: [
            {required: true, message: '请选择商品导出', trigger: 'change'}
          ],
          promotionScope: [
            {required: true, message: '请选择参与活动', trigger: 'change'}
          ],

          rxShopSwitch: [
            {required: true, message: '是否启用柔性店铺', trigger: 'change'}
          ],
          logisticsSmsSwitch: [
            {required: true, message: '是否短信提示物流信息', trigger: 'change'}
          ]
        },
        extendData: {
          customerFlag: [
            {required: true, message: '是否开启C端模式', trigger: 'change'}
          ],
          customerMode: [
            {required: true, message: '请选择C端结算模式', trigger: 'change'}
          ],
          subAccountFlag: [
            {required: true, message: '请选择是否开启分账', trigger: 'change'}
          ],
          distributionFlag: [
            {required: true, message: '请选择是否开启分销', trigger: 'change'}
          ],
          distributionMode: [
            {required: true, message: '请选择分销模式', trigger: 'change'}
          ],
          distributionPayWay: [
            {required: true, message: '请选择上级分销商付款方式', trigger: 'change'}
          ],
          distributionAutoFlag: [
            {required: true, message: '分销订单审核方式', trigger: 'change'}
          ],
          distributionPromotionFlag: [
            {required: true, message: '分销活动是否同步', trigger: 'change'}
          ],
          erpBalanceFlag: [
            {required: true, message: 'ERP余额是否同步', trigger: 'change'}
          ]
        },
        financial: {
          tradeId: [
            {required: true, message: '请选择收款条件', trigger: 'change'}
          ]
          // coinType: [
          //   { required: true, message: '请选择币种', trigger: 'change' }
          // ]
        }
      },
      ids: [],
      distributorGroupIds: [], // 分销商分组
    }
  },
  created() {
    this.adminList() // 业务人员
    this.groupsD() // 分销商分组
    this.classes() // 分销商类别
    this.pattern() // 收款条件
    this.gradeList() // 默认价格等级 || 添加品牌 => 价格等级 || 添加品类 => 价格等级
    this.productBrand() // 添加品牌 => 品牌名称 || 添加品类 => 品牌名称
  },
  methods: {
    // ======== 操作 ========
    handleSubmit(formData) { // 必填验证
      this.$refs['formData'].validate(valid => {
        // eslint-disable-next-line no-empty
        if (valid) {
          if (this.arr !== undefined) {
            if (this.formData.business.promotionScope === 2 && this.formData.business.promotionTypes.length === 0) {
              this.$message({
                message: '请先选择要参与的活动类型',
                type: 'error',
                duration: 3 * 1000,
                onClose: () => {
                }
              })
              valid = false
            }
            for (let i = 0; i < this.arr.length; i++) {
              if (this.arr[i].brandId === '已删除或停用') {
                this.$message({
                  message: '请先删除已停用或已删除的品牌等级权限！',
                  type: 'error',
                  duration: 3 * 1000,
                  onClose: () => {
                  }
                })
                valid = false
              }
            }
          }
        } else {
          this.$message({
            message: '请先将分销商权限设置资料补充完整',
            type: 'error',
            duration: 3 * 1000,
            onClose: () => {
            }
          })
        }
        eventBus.$emit('perSurely', {valid: valid})
      })
    },

    addGoods() { // 添加品牌操作
      this.addBrand = true
      var addManObject = {}
      this.arr.push(addManObject)
    },

    delectBrand(item) { // 删除品牌操作
      let delItem = JSON.parse(JSON.stringify(item));
      if (item.brandId === '已删除或停用') {
        delItem.brandId = item.bId
      }
      this.arr.splice(this.arr.indexOf(item), 1)
      if (this.arr.length === 0) {
        this.addBrand = false
      }
      if (item.id) {
        this.delArr.push(delItem)
      }
    },

    product() { // 添加产品线操作
      this.addShow = true
      this.arr2.push({})
      this.productlinelists.push([])
    },

    delectProcuct(index) { // 删除产品线操作
      this.arr2.splice(index, 1)
      this.productlinelists.splice(index, 1)
      if (this.arr2.length === 0) {
        this.addShow = false
      }
    },
    // ======== 数据 ========
    adminList() { // 业务人员
      return this.$http.salesList(this, {page: 1, size: 10000, status: 1}).then(res => {
        this.admins = res.data.list
        this.count--
        if (this.count === 0) {
          this.loading = false
        }
      })
    },

    groupsD() { // 分销商分组
      return this.$http.getDistGroupPoList(this, {page: 1, size: 10000, openFlag: 1}).then(res => {
        this.groups = res.data.list
        this.count--
        if (this.count === 0) {
          this.loading = false
        }
      })
    },

    classes() { // 分销商类别
      this.$http.getDistCategoryPoList(this, {page: 1, size: 1000, openFlag: 1}).then(res => {
        this.categories = res.data.list
        this.count--
        if (this.count === 0) {
          this.loading = false
        }
      })
    },

    pattern() { // 收款条件
      this.$http.tradePoList(this, {page: 1, size: 1000, openFlag: 1}).then(res => {
        this.settleAccounts = res.data.list
        this.count--
        if (this.count === 0) {
          this.loading = false
        }
      })
    },

    gradeList() { // 默认价格等级 || 添加品牌 => 价格等级 || 添加品类 => 价格等级
      this.$http.getGradePoList(this, {page: 1, size: 10000, openFlag: 1}).then(res => {
        if (res.success) {
          this.grades = res.data.list
          this.count--
          if (this.count === 0) {
            this.loading = false
          }
        }
      })
    },

    productBrand() { // 添加品牌 => 品牌名称 || 添加品类 => 品牌名称
      this.$http.getBrandPoList(this, {page: 1, size: 10000, openFlag: '1'}).then(res => {
        if (res.success) {
          this.brandsAss = JSON.parse(JSON.stringify(res.data.list))
          for (var i = 0; i < res.data.list.length; i++) {
            res.data.list[i].disabled = false
          }

          this.brands = res.data.list
          for (let i = 0; i < this.arr.length; i++) { // 添加品牌默认禁用
            let b = false;
            for (var j = 0; j < this.brands.length; j++) {
              if (this.arr[i].brandId === this.brands[j].id) {
                this.brands[j].disabled = true
                b = true
                break
              }
            }
            if (!b) {
              if (this.arr[i].brandId !== '已删除或停用') {
                this.arr[i].bId = this.arr[i].brandId;
                this.arr[i].brandId = '已删除或停用'
              }
            }
          }
          // for (let i = 0; i < this.arr2.length; i++) { // 添加品类默认禁用
          //   let b = false;
          //   for (let j = 0; j < this.brandsAss.length; j++) {
          //     if (this.arr2[i].brandId === this.brandsAss[j].id) {
          //       this.brandsAss[j].disabled = true
          //       break
          //     }
          //   }
          //   if(!b){
          //     this.arr[i].brandId = '已删除或停用'
          //   }
          // }
          this.count--
          if (this.count === 0) {
            this.loading = false
          }
        }
        res.success ? this.loading = false : this.loading = false
      })
    },
    // 暂不使用
    // brandChange(val, index) { // 拼配价格等级 => 添加品类 => 品类
    //   this.$api.get(this, 'admin/u/po/productline/list', { page: 1, count: 10000, brandId: val }).then(res => {
    //     const ary = []
    //     res.productlines.forEach(item => {
    //       if (item.isOpen) {
    //         ary.push(item)
    //       }
    //     })
    //     this.productlinelists[index] = ary
    //     this.productlinelists = JSON.parse(JSON.stringify(this.productlinelists))
    //   })
    //   this.brandsAss.forEach(item => {
    //     item.disabled = false
    //   })
    //   for (var i = 0; i < this.arr2.length; i++) { // 重复不能选择
    //     let b = false;
    //     for (var j = 0; j < this.brandsAss.length; j++) {
    //       if (this.arr2[i].brandId === this.brandsAss[j].id) {
    //         this.brandsAss[j].disabled = true
    //         break
    //       }
    //     }
    //     if(!b){
    //       this.arr[i].brandId = '已删除或停用'
    //     }
    //   }
    // },

    changes(val) { // 添加品牌价格等级
      this.brands.forEach(item => { // 重置空可以选择
        item.disabled = false
      })
      for (let i = 0; i < this.arr.length; i++) { // 重复不能选择
        for (let j = 0; j < this.brands.length; j++) {
          if (this.arr[i].brandId === this.brands[j].id) {
            this.brands[j].disabled = true
            break
          }
        }
      }
    },
	
	//监听收款条件选择
	changeAccounts(item) {
		console.log('payWay = ' + item.payWay)
		this.payWay = item.payWay
		if(item.payWay == 1){
			console.log("立即支付");
		}else{
			console.log("期间结算");
		}
	}	
	
  },
  watch: {
    basicMessage(val) {
      if (val.business) {
        if (val.business.salesId === 0 || val.business.salesId === undefined) { // 业务员
          this.formData.business.salesId = ''
        } else {
          this.formData.business.salesId = val.business.salesId
        }

        this.formData.business.autoDelivery = val.business.autoDelivery ? val.business.autoDelivery : 0 // 分销商自动下推出库
        this.formData.business.canExportPrice = val.business.canExportPrice ? val.business.canExportPrice : 0 //分销商商品导出
        // 分销商分组
        if (val.business.distributorGroupIds) {
          let distributorGroupIds = val.business.distributorGroupIds.split(",");
          this.distributorGroupIds = distributorGroupIds.map(Number)
        }
        this.formData.business.distributorCategoryId = val.business.distributorCategoryId // 分销商类别
        this.formData.business.promotionScope = val.business.promotionScope !== undefined ? val.business.promotionScope : 1 // 参与活动
        this.formData.business.logisticsSmsSwitch = (val.business.logisticsSmsSwitch === 0 || val.business.logisticsSmsSwitch === 1) ? val.business.logisticsSmsSwitch : 0 // 是否短信提示物流信息
        this.formData.business.onWayFlag = val.business.onWayFlag ? val.business.onWayFlag : 0
        this.formData.business.rxShopSwitch = val.business.rxShopSwitch !== undefined ? val.business.rxShopSwitch : 0 // 是否启用柔性店铺
        if (val.business.scalePriceId === 0) { // 默认价格等级
          this.formData.business.scalePriceId = ''
        } else {
          this.formData.business.scalePriceId = val.business.scalePriceId
        }
        if (val.business.distributionScalePriceId === 0 || val.business.distributionScalePriceId === undefined) { // 默认分销价格等级
          this.formData.business.distributionScalePriceId = ''
        } else {
          this.formData.business.distributionScalePriceId = val.business.distributionScalePriceId
        }

        if (val.business.promotionScope === 2 && val.business.promotionTypes !== undefined && val.business.promotionTypes !== null) {
          this.formData.business.promotionTypes = val.business.promotionTypes.split(",")
        }
      }
      if (val.extendData) {
        this.formData.extendData.customerFlag = val.extendData.customerFlag !== undefined ? val.extendData.customerFlag : 0 // 是否开启C端模式
        this.formData.extendData.customerMode = val.extendData.customerMode ? val.extendData.customerMode : 1 // C端结算模式
        this.formData.extendData.subAccountFlag = val.extendData.subAccountFlag ? val.extendData.subAccountFlag : 0 // 是否开启分账
        this.formData.extendData.distributionFlag = val.extendData.distributionFlag !== undefined ? val.extendData.distributionFlag : 0 // 是否开启分销
        this.formData.extendData.distributionMode = val.extendData.distributionMode !== undefined ? val.extendData.distributionMode : 1 // 分销模式
        this.formData.extendData.distributionPayWay = val.extendData.distributionPayWay !== undefined ? val.extendData.distributionPayWay : 0 // 上级收款方式
        this.formData.extendData.distributionAutoFlag = val.extendData.distributionAutoFlag !== undefined ? val.extendData.distributionAutoFlag : 1 // 分销订单是否自动审核
        this.formData.extendData.distributionPromotionFlag = val.extendData.distributionPromotionFlag !== undefined ? val.extendData.distributionPromotionFlag : 1 // 分销活动是否同步
        this.formData.extendData.erpBalanceFlag = val.extendData.erpBalanceFlag !== undefined ? val.extendData.erpBalanceFlag : 1 // ERP余额是否同步
        this.formData.extendData.erpFlag = val.extendData.erpFlag // 信息是否同步到erp
        if (val.extendData.erpId) {
          this.formData.extendData.erpId = val.extendData.erpId
        } else {
          this.$set(this.formData.extendData, 'erpId', '')
        }
      }
      if (val.financial) {
        if (val.financial.tradeId === 0) { // 收款条件
          this.formData.financial.tradeId = ''
        } else {
          this.formData.financial.tradeId = val.financial.tradeId
        }
        if (val.financial.coinType === 0) { // 币种
          this.formData.financial.coinType = ''
        } else {
          this.formData.financial.coinType = val.financial.coinType
        }
      }
      if (val.scalePrices && val.scalePrices.length > 0) { // 品牌价格等级
        eventBus.$emit('scalePriceEvent', {
          tempArr: JSON.parse(JSON.stringify(val.scalePrices))
        })
        this.arr = val.scalePrices
        this.productBrand()
        this.addBrand = true
      }
    },
    'formData.business.autoDelivery': {
      handler(val) {
        if (val === 1) {
          this.categories.forEach(item => {
            if (item.name === '普通客户') {
              this.formData.business.distributorCategoryId = item.id
              this.autoShow = true
            }
          })
        } else {
          this.autoShow = false
        }
      },
      deep: true
    },
    'erpFlag': {
      handler(val) {
        this.formData.extendData.erpFlag = val
      },
      deep: true
    },
    'formData.extendData.subAccountFlag': {
      handler(val) {
        console.log('subAccountFlag==', val);
        this.$emit('account', val)
      },
      deep: true
      // immediate: true
    },
    'distributorGroupIds': {
      handler(val) {
        this.formData.business.distributorGroupIds = val.toString();
      },
      deep: true
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import '../../scss/permissions.scss';

.productData {
  .el-form-item--feedback {
    margin: 0 10px;

    .brand-select {
      .el-input--suffix {
        width: 200px;
      }
    }

    .el-button--default {
      margin: 0 10px;
    }
  }
}

</style>
