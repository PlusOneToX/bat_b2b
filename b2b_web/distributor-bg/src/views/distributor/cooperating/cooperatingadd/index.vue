
<template>
  <div class="distributor-list-wrap" >
    <header v-if="append">
      <h4 class="header_h4">添加合作分销商</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="clickLeave()">
        返回合作中的分销商列表
      </el-button>
    </header>

    <header v-if="clickLeaveShow">
      <h4 class="header_h4">查看合作分销商</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="clickLeave()">
        返回合作中的分销商列表
      </el-button>
    </header>

    <header v-if="backpulsShow">
      <h4 class="header_h4">分销商审批单</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="backpuls">
        返回分销商审批列表
      </el-button>
    </header>

    <header v-if="addpulsShow">
      <h4 class="header_h4">查看申请中的分销商</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="addpuls">
        返回申请中的分销商
      </el-button>
    </header>

    <header v-if="addForessShow">
      <h4 class="header_h4">查看已冻结的分销商</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="addForess">
        返回已冻结分销商列表
      </el-button>
    </header>

    <!-- 审批头部信息 -->
    <div class="halfs" v-if="backpulsShow">
      <div class="halfs-width">
        <el-form ref="chosenData" label-width="180px">
          <el-form-item label="审批单号:"> {{response.checkId}} </el-form-item>
          <el-form-item label="审批类型:"> {{forstatus(response.checkType)}} </el-form-item>
        </el-form>
      </div>
      <div class="halfs-width">
        <el-form ref="chosenData" label-width="180px">
          <el-form-item label="发起时间:"> {{timeFormatter(response.checkApplyTime)}} </el-form-item>
          <el-form-item label="审批状态:"> {{cStatus(response.checkStatus)}} </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="distributor-list" v-loading="loading">
      <el-tabs v-model="distributorTab" >
        <el-tab-pane label="基本信息" name="0" ref="DisFirst"></el-tab-pane>
        <el-tab-pane label="账户信息" name="1" v-if="checkMsg === 0 || (node===2 && checkIsErp===1) || node===1"></el-tab-pane>
        <el-tab-pane label="权限设置" name="2"></el-tab-pane>
        <el-tab-pane label="特价商品" name="3" v-if="(checkMsg == 1 || checkMsg == 2 || checkMsg == 4) && node === 1"></el-tab-pane>
        <el-tab-pane label="联系人信息" name="4" v-if="checkMsg === 0 || checkMsg == 1 || checkMsg == 3 || checkMsg == 4"></el-tab-pane>
        <el-tab-pane label="店铺分账" name="7" v-if="checkIsAccount==1"></el-tab-pane>
        <el-tab-pane label="定制设置" name="5"  v-if="checkMsg == 1 || checkMsg == 2 || checkMsg == 4"></el-tab-pane>
        <el-tab-pane label="操作日志" name="6"  v-if="checkMsg == 1 || checkMsg == 3 || checkMsg == 4"></el-tab-pane>
      </el-tabs>
    </div>
    <div class="user-edit-TB">
			<el-button class="mini-search-btn" @click="syncDisERP" :loading="loadingErp" v-if="checkIsErp == 1 && checkMsg != 2">同步到ERP</el-button>
		</div>
    <div class="content" v-loading="loading" >
      <!-- 基本信息 -->
      <addcomponents ref="addcomponents" v-show="distributorTab == 0" :passShow="passShow" :basicMessage="responseData" :nameShow="nameShow" :syncShow="syncShow" :exaShow="exaShow" :checkMsg="checkMsg" :node="node" :flows="flows" :checkTyps="checkTyps" @erp="changeErp"></addcomponents>
      <!-- 账户信息 -->
      <accountInformation v-if="checkMsg === 0 || (node===2 && checkIsErp===1) || node===1"  v-show="distributorTab == 1" ref="accountInformation" :basicMessage="responseData" :disableIsNameShow="disableIsNameShow" :exaShow="exaShow" :checkMsg="checkMsg" :checkTyps="checkTyps"></accountInformation>
      <!-- 权限设置 -->
      <permissions ref="permissions" v-show="distributorTab == 2" :basicMessage="responseData" :erpFlag="erpFlag" :exaShow="exaShow" :checkMsg="checkMsg" :node="node" :checkTyps="checkTyps"  @account="changeAccount"></permissions>
      <!-- 特价商品 -->
      <specialcomponents ref="specialcomponents" v-show="distributorTab == 3" :basicMessage="responseData" :checkQuqry="checkQuqry" :checkDistributorId="checkDistributorId" :stocks="stocks" :exaShow="exaShow" :checkMsg="checkMsg" :checkTyps="checkTyps"></specialcomponents>
      <!-- 联系人信息 -->
      <roleList ref="roleList" v-show="distributorTab == 4" :basicMessage="responseData" :exaShow="exaShow" ></roleList>
      <!-- 店铺分账 -->
      <sub-account ref="subAccount" v-show="distributorTab == 7" :basicMessage="responseData" :exaShow="exaShow" :checkMsg="checkMsg" :node="node"></sub-account>
      <!-- 定制商品价格设置 -->
      <diyPrice ref="diyPrice" v-show="distributorTab == 5" :distributorId="checkQuqry" :node="node"></diyPrice>
      <!-- 操作日志 -->
      <cooperatingLog ref="cooperatingLog" v-show="distributorTab == 6" :basicMessage="responseData" :node="node"></cooperatingLog>
    </div>

    <el-row class="dis_Modify" v-if="checkMsg == 2 && approvalType == 2">
				<div class="title">修改信息</div>
				<el-table :data="responseData.modifyList" border header-row-class-name="header-row" class="tableCenter" :span-method="arraySpanMethod">
					<el-table-column align="center" label="修改字段" prop="mdField"> </el-table-column>
					<el-table-column align="center" label="修改前内容" class="dis_Modify_before">
						<template slot-scope="scope" >
              <el-button size="mini" type="text" @click="handlePreviewStock(scope.row)" v-if="scope.row.mdField == '价格等级-品牌价格等级' || scope.row.mdField == '价格等级-品类价格等级' || scope.row.mdField == '特价商品'">
                查看
              </el-button>
              <span size="mini" type="text" v-else >
                {{originValueStatus(scope.row)}}
              </span>
						</template>
					</el-table-column>
					<el-table-column label="修改后内容" prop="mdValue">
						<template slot-scope="scope">
							<span size="mini" type="text">
								{{mdValueStatus(scope.row)}}
							</span>
						</template>
					</el-table-column>
				</el-table>
    </el-row>

    <!-- ========================= -->
		<!-- 审批详情需改信息弹窗 -->
		<!-- 价格等级-品牌价格等级 -->
		<el-dialog :visible="warehouseShow" :before-close="closeWarehouseShow">
			<h4 class="beHeader">品牌价格等级</h4>
			<h4 class="beAmend">查看修改前的内容</h4>
			<table class="modifierStyle" cellspacing="0" cellpadding="0">
				<tr>
					<th>品牌名称</th>
					<th>价格等级</th>
				</tr>
				<tr v-for="(item) in arr" :key="item.id" :vlaue="item.id">
					<td>
						<el-select v-model="item.brandId" size="mini" placeholder="请选择" :disabled="exaShow" class="modifierSelect">
							<el-option v-for="item in brands" :key="item.id" :label="item.title" :value="item.id" ></el-option>
						</el-select>
					</td>
					<td>
						<el-select v-model="item.scalePriceId" size="mini" placeholder="请选择" :disabled="exaShow" class="modifierSelect">
							<el-option v-for="item in grades" :key="item.id" :label="item.name" :value="item.id"></el-option>
						</el-select>
					</td>
				</tr>
			</table>
			<h4 class="beAmend">查看修改后的内容</h4>
			<table class="modifierStyle" cellspacing="0" cellpadding="0">
				<tr>
					<th>品牌名称</th>
					<th>价格等级</th>
				</tr>
				<tr v-for="(item) in arr2" :key="item.id" :vlaue="item.id">
					<td>
						<el-select v-model="item.brandId" size="mini" placeholder="请选择" :disabled="exaShow"  class="modifierSelect">
							<el-option v-for="item in brands" :key="item.id" :label="item.title" :value="item.id" ></el-option>
						</el-select>
					</td>
					<td>
						<el-select v-model="item.scalePriceId" size="mini" placeholder="请选择" :disabled="exaShow"  class="modifierSelect">
							<el-option v-for="item in grades" :key="item.id" :label="item.name" :value="item.id"></el-option>
						</el-select>
					</td>
				</tr>
			</table>
		</el-dialog>
		<!-- 价格等级-品牌价格等级 -->
		<el-dialog :visible="productlinesShow" :before-close="closeProductlinesShow">
			<h4 class="beHeader">品类价格等级</h4>
			<h4 class="beAmend">查看修改前的内容</h4>
      <table class="modifierStyle" cellspacing="0" cellpadding="0">
				<tr>
					<th>品牌名称</th>
					<th>品类</th>
					<th>价格等级</th>
				</tr>
				<tr v-for="(item) in arr3" :key="item.id" :vlaue="item.id">
					<td>
						<el-select v-model="item.brandId" size="mini" placeholder="请选择" :disabled="exaShow" class="modifierSelect">
							<el-option v-for="item in brands" :key="item.id" :label="item.title" :value="item.id" ></el-option>
						</el-select>
					</td>
					<td>
						<el-select v-model="item.productlineId" size="mini"  placeholder="请先选择品类" :disabled="exaShow" class="modifierSelect">
							<el-option v-for="item in productlinelist" :key="item.id" :value="item.id" :label="item.name"></el-option>
						</el-select>
					</td>
					<td>
						<el-select v-model="item.scalePriceId" size="mini" placeholder="请选择" :disabled="exaShow" class="modifierSelect">
							<el-option v-for="item in grades" :key="item.id" :label="item.name" :value="item.id"></el-option>
						</el-select>
					</td>
				</tr>
			</table>
			<h4 class="beAmend">查看修改后的内容</h4>
      <table class="modifierStyle" cellspacing="0" cellpadding="0">
				<tr>
					<th>品牌名称</th>
					<th>品类</th>
					<th>价格等级</th>
				</tr>
				<tr v-for="(item) in arr4" :key="item.id" :vlaue="item.id">
						<td>
							<el-select v-model="item.brandId" size="mini" placeholder="请选择" :disabled="exaShow" class="modifierSelect">
								<el-option v-for="item in brands" :key="item.id" :label="item.title" :value="item.id" ></el-option>
							</el-select>
						</td>
						<td>
							<el-select v-model="item.productlineId" size="mini"  placeholder="请先选择品类" :disabled="exaShow" class="modifierSelect">
								<el-option v-for="item in productlinelist" :key="item.id" :value="item.id" :label="item.name"></el-option>
							</el-select>
						</td>
						<td>
							<el-select v-model="item.scalePriceId" size="mini" placeholder="请选择" :disabled="exaShow" class="modifierSelect">
								<el-option v-for="item in grades" :key="item.id" :label="item.name" :value="item.id"></el-option>
							</el-select>
						</td>
					</tr>
      </table>
		</el-dialog>

		<el-dialog :visible="specialShow" :before-close="closeSpecialShow">
			<h4 style="text-align: center;">特价商品</h4>
			<el-table :data="tableData" border header-row-class-name="headerRow" class="tableCenter" ref='multipleSelect'>
				<el-table-column align="center" label="修改类型" prop="operationType" :formatter="amendStatus"></el-table-column>
				<el-table-column align="center" label="商品编号" prop="goodsNo"></el-table-column>
				<el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
				<el-table-column align="center" label="货品编号" prop="itemCode"></el-table-column>
				<el-table-column align="center" label="货品名称" prop="itemName"></el-table-column>
				<el-table-column align="center" label="默认销售价格" prop="salePrice"></el-table-column>
				<el-table-column align="center" label="分销商特价">
          <template slot-scope="scope">
						<p style="color: red;text-decoration: line-through;" v-if="scope.row.operationType == 3">¥:{{scope.row.soDistributorPrice}}</p>
            <p>¥:{{scope.row.distributorPrice}}</p>
					</template>
        </el-table-column>
			</el-table>
		</el-dialog>
		<!-- ========================= -->


		<!-- ===== 审批流程及备注 ===== -->
    <el-row v-if="checkMsg == 2">
      <check-procedure :flows="flows"></check-procedure>
    </el-row>

    <el-row v-if="checkMsg == 2 && disCheckType == 2">
      <div class="title">审批备注</div>
      <el-row>
        <el-input type="textarea" :rows="5" v-model="comment" clearable/>
      </el-row>
    </el-row>
    <!-- ===== 审批流程及备注 ===== -->


    <div class="footbtn" >
      <el-button class="mini-search-btn foot-btn" @click="checkSyncs()"  v-if="saveShow == 0"> 保存  </el-button>
      <el-button class="mini-search-btn foot-btn" @click="checkSyncs(2)" v-if="AgreedShow == 0"> 同意 </el-button>
      <el-button class="mini-delete-btn foot-btn" @click="checkSyncs(3)" v-if="AgreedShow == 0"> 拒绝 </el-button>
      <el-button class="mini-search-btn foot-btn" @click="handleEdit()" v-if="IsEditShow == 0"> 编辑 </el-button> <!-- 分销商审核拒绝的可以进行重新编辑 -->
    </div>
  </div>
</template>

<script>
/*
 * @Author: lijiemin
 * @Date: 2018-05-06 17:11:50
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-04-Th 02:19:22
 */
import md5 from 'js-md5'
import eventBus from '@/views/order/eventBus'
import { parseTime } from '@/utils/index'
import { applyData } from '@/views/distributor/distributorData' // 申请
import checkProcedure from '@/views/distributor/cooperating/cooperatingadd/components/checkProcedure' // 审批流程

// ======引用组件======
import addcomponents from '@/views/distributor/cooperating/cooperatingadd/components/addcomponents' // 基本信息
import accountInformation from '@/views/distributor/cooperating/cooperatingadd/components/accountInformation' // 账户信息
import permissions from '@/views/distributor/cooperating/cooperatingadd/components/permissions' // 权限设置
import specialcomponents from '@/views/distributor/cooperating/cooperatingadd/components/specialcomponents' // 特价商品
import roleList from '@/views/distributor/cooperating/cooperatingadd/components/roleList' // 联系人信息
import subAccount from '@/views/distributor/cooperating/cooperatingadd/components/subAccount' // 店铺分账
import diyPrice from '@/views/distributor/cooperating/cooperatingadd/components/diyPrice' // 柔性定制商品
import cooperatingLog from '@/views/distributor/cooperating/cooperatingadd/components/cooperatingLog' // 操作日志

export default {
  name: 'distributorcooperatingadd',
  components: {
    addcomponents, // 基本信息
    accountInformation, // 账户信息
    permissions, // 权限设置
    specialcomponents, // 特价商品
    roleList, // 联系人信息
    subAccount, // 店铺分账
    diyPrice, // 柔性定制商品
    cooperatingLog, // 操作日志
    checkProcedure // 审批流程
  },
  data() {
    return {
      checkSync: false, // 是否开启审批
      disableIsShow: false,
      disableIsNameShow: true, // 账户基本信息，公司名不可输入判断
      loading: false, // 加载效果: 合作中分销商查看
      loadingErp: false, // 同步到ERP

      append: false, // 标题: 添加合作中分销商
      clickLeaveShow: false, // 标题: 查看合作中分销商
      backpulsShow: false, // 标题: 分销商审批
      addpulsShow: false, // 标题: 申请中的分销商
      addForessShow: false, // 标题: 已冻结的分销商
      accountShow: true, // 账户信息

      addSurely: false, // 合作中分销商必填，基本信息
      accSurely: false, // 合作中分销商必填，账户信息
      perSurely: false, // 合作中分销商必填，权限设置
      subSurely: false, // 合作中分销商必填，店铺分账设置

      erpFlag: false, // 是否同步ERP显示隐藏权限设置模块信息
      subAccountFlag: false, // 是否开启分账显示隐藏店铺分账模块信息
      checkTyps: '', // 合作中分销商（申请）拒绝
      distributorTab: '0',
      activeName: 'first',
      formData: {},
      defaultData: {
        address: {},
        business: {},
        contacts: {},
        extendData: {},
        financial: {},
        salesAreaIds: [],
        scalePrices: [],
        specialGoods: undefined,
        subAccountAdminConfigCmd: {}
      },
      responseData: {}, // 详情数据的对象
      modiData: {},
      specialAdd: [],
      response: {},
      tableDataLog: {}, // 操作日志
      flows: {}, // 审批流程
      stocks: [], // 选中的特价商品数据
      stocksJudge: false, // 是否获取特价商品数据进行传值
      stocksJudgeData: false, // 没有任何特价商品时候的取值
      stocksData: [], // 选中的特价商品数据临时数组
      tempArr: [], // 品牌价格等级初始数组
      passShow: false, // 密码显示
      syncShow: true, // 分销商同步到ERP
      exaShow: true, // 分销商审批输入框不可编辑
      nameShow: false, // 分销商用户名输入禁用
      // selectShow: false, // 特价商品dialog
      hezuo: false, //  合作中分销商（编辑）（接口请求标识）
      tianjia: false, // 合作中分销商（添加）（接口请求标识）
      shenpi: false, // 合作中分销商（审批）（接口请求标识）
      shenqing: false, // 合作中分销商（申请）（接口请求标识）
      dongjie: false, // 合作中分销商（冻结）（接口请求标识）
      loading2: '', // 编辑合作中的分销商修改
      loading3: '', // 添加合作中的分销商
      loading4: '', // 审批合作中的分销商
      loading5: '', // 申请合作中的分销商
      loading6: '', // 冻结合作中的分销商
      counts: 0,
      hintStr: '',
      checkTypeSumbit: '', // 同意拒绝的值
      isCheckStatus: false,
      checkStock: [],
      comment: '',
      addOperation: false, // 为新增的特价商品
      deeOperation: false, // 为删除的特价商品
      amendOperation: false, // 为修改的特价商品

      // ===== 审批修改信息部分 =====
      warehouseShow: false, // 价格等级-品牌价格等级弹窗
      productlinesShow: false, // 价格等级-品类价格等级弹窗
      specialShow: false, // 特价商品弹窗
      arr: [], // 价格等级-品牌价格等级,修改前内容
      arr2: [], // 价格等级-品牌价格等级,修改后内容
      arr3: [], // 价格等级-品类价格等级,修改前内容
      arr4: [], // 价格等级-品类价格等级,修改后内容
      productlinelist: [], // 产品线选择请先选择品牌
      brands: [], // 产品线品牌下拉数据
      grades: [], // 默认价格等级
      tableData: [], // 特价商品
      salesAreas: [], // 分销商区域
      // ===== 审批修改信息部分 =====
      checkIsAccount: 0
    }
  },
  created() {
    this.isEventBus() // 验证必填项是否完整 (组件页面的必填项验证事件发布)
    this.isInitialize()

    // 修改信息
    this.salearea() // 分销商区域
    this.brandList() // 品牌名称
    this.gradeList() // 品类列表价格等级
  },
  beforeDestroy() { // 实例销毁之前执行的钩子 (此处用来销毁eventbus的创建)
    eventBus.$off('addSurely')
    eventBus.$off('accSurely')
    eventBus.$off('perSurely')
    eventBus.$off('subSurely')
    eventBus.$off('amendEmit')
    eventBus.$off('CoamendEmit')
    eventBus.$off('FirstReqShow')
  },
  computed: {
    checkIsErp() { // 是否同步到ERP 1 是  0 否
      if (this.responseData && this.responseData.extendData && this.responseData.extendData.erpFlag) {
        return this.responseData.extendData.erpFlag
      } else {
        return ''
      }
    },
    checkQuqry() {
      return this.$route.query.id
    },
    checkDistributorId() {
      return this.$route.query.distributorId
    },

    checkMsg() { // 分销商页面判断 0 添加分销商 1 合作中分销商进来的页面 2 分销商审批 3 申请中的分销商 4 已冻结的分销商
      return this.$route.query.checkMsg
    },
    node() { // 分销商页面判断 1 一级分销商 2 多级分销
      return this.$route.query.node
    },

    /**
     * checkMsg: 0 添加分销商 1 合作中分销商进来的页面 2 分销商审批 3 申请中的分销商 4 已冻结的分销商
     * checkDistributorTab: 第几个子页面  0 基本信息  1 账户信息 2 权限设置 3 特价商品 4 联系人信息 5 定制设置 6 操作日志  7 店铺分账
     * isCheckStatus: 当前审批人是否自己 true:自己 false:不是自己
     * checkIsEdit: 是否可编辑是否可编辑 1.能编辑  2.不能编辑
     */
    saveShow() { // 保存是否显示
      if (this.checkMsg === '') {
        return 0 // 刷新后的页面
      } else if (this.checkMsg == 1 && this.checkDistributorTab == 0) {
        return 0 // ==== 合作中分销商 && 基本信息 ====
      } else if (this.checkMsg == 1 && this.checkDistributorTab == 1) {
        return 0 // ==== 合作中分销商 && 账户信息 ====
      } else if (this.checkMsg == 1 && this.checkDistributorTab == 2) {
        return 0 // ==== 合作中分销商 && 权限设置 ====
      } else if (this.checkMsg == 1 && this.checkDistributorTab == 3) {
        return 0 // ==== 合作中分销商 && 特价商品 ====
      } else if (this.checkMsg == 1 && this.checkDistributorTab == 4) {
        return 0 // ==== 合作中分销商 && 联系人信息 ====
      } else if (this.checkMsg == 1 && this.checkDistributorTab == 7) {
        return 0 // ==== 合作中分销商 && 店铺分账 ====
      } else if (this.checkMsg == undefined) {
        return 0
      } else if (this.checkMsg == 0) {
        return 0
      }
    },

    AgreedShow() { // 审批同意是否显示
      if (this.checkMsg == 2 && this.isCheckStatus) {
        return 0 // ..分销商审批,且轮到自己审批的时候
      } else if (this.checkMsg == 3 && this.isCheckStatus) {
        return 0 // ..分销商申请
      } else if (this.checkMsg == 2 && this.isCapitalStatus == 1 && this.isCheckStatus) {
        return 0 // ..分销商审批 & 为前台申请被拒绝的 & 轮到自己审批 (申请被拒绝后,跳转到审批页面从新编辑发起审批)
      } else if (this.checkMsg == 3 && this.isCapitalStatus == 1) {
        return 0 // ..分销商申请 & 申请中
      }
    },

    IsEditShow() { // 是否可编辑
      if (this.checkIsEdit == 1 && this.checkMsg == 2) {
        return 0 // ==== 可编辑 && 分销商审批页面 ====
      }
    },

    checkDistributorTab() { // 第几个子页面  0 基本信息  1 账户信息 2 权限设置 3 特价商品 4 特价商品 5 定制商品定价 6 操作日志 7 店铺分账
      return this.distributorTab
    },

    isCapitalStatus() { // 分销商的申请状态 0 未提交 1 申请中 2 审核通过 3 审核未通过 4 审核中
      return this.responseData.applyStatus
    },

    checkIsEdit() { // 是否能编辑 1能编辑  2 不能编辑
      return this.response.isEdit
    },

    checkType() { // 审批状态  1: 分销商添加审批 2: 分销商编辑审批
      return this.$route.query.checkType
    },

    disCheckType() { // 审批列表由谁审批的进来的页面
      return this.$route.query.checkType
    },

    approvalType() {
      return this.response.checkType
    }
  },
  methods: {
    // 是否开启分账P显示隐藏分账模块信息
    changeAccount(val) {
      console.log('mmm===', val)
      this.checkIsAccount = val
    },
    // 是否同步ERP显示隐藏权限设置模块信息
    changeErp (val) {
      this.erpFlag = val
    },
    distriData() { // ..详情页面所需数据
      this.isEventBus() // 验证必填项是否完整 (组件页面的必填项验证事件发布)
      this.isInitialize()
      this.salearea() // 分销商区域
      this.brandList() // 品牌名称
      this.gradeList() // 品类列表价格等级
    },
    // ========= 修改信息部分 =========
    /**
		 * 表格合并
		 * @param {*} param0
		 * row 表格每一行的数据
		 * column 表格每一列的数据
		 * rowIndex 表格的行索引,不包括表头,从0开始
		 * columnIndex 表格的列索引,从0开始
		 */
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (this.responseData.modifyList[rowIndex].mdField === '价格等级-品牌价格等级' || this.responseData.modifyList[rowIndex].mdField === '价格等级-品类价格等级' || this.responseData.modifyList[rowIndex].mdField === '特价商品') {
        if (columnIndex === 1) {
          return [1, 2]
        } else if (columnIndex === 2) {
          return [0, 0]
        }
      }
    },
    closeWarehouseShow() { // 价格等级-品牌价格等级(关闭弹窗)
      this.warehouseShow = false
    },
    closeProductlinesShow() { // 价格等级-品类价格等级(关闭弹窗)
      this.productlinesShow = false
    },
    closeSpecialShow() { // 特价商品(关闭弹窗)
      this.specialShow = false
    },
    handlePreviewStock(row) {
      if (row.mdField === '价格等级-品牌价格等级') {
        this.warehouseShow = true
      } else if (row.mdField === '价格等级-品类价格等级') {
        this.productlinesShow = true
      } else if (row.mdField === '特价商品') {
        this.specialShow = true
      }
    },
    amendStatus(row) { // 申请状态
      switch (row.operationType) {
        case 1:
          return '增加'
        case 2:
          return '删除'
        case 3:
          return '修改'
      }
    },
    originValueStatus(row) { // 修改前
      if (row.mdField === '基本信息-是否同步到ERP') {
        switch (row.originValue) {
          case '1':
            return '否'
          case '2':
            return '是'
        }
      } else if (row.mdField === '权限信息-币种') {
        switch (row.originValue) {
          case '0':
            return '-'
          case '1':
            return '人民币'
        }
      } else if (row.mdField === '基本信息-分销商区域') {
        let objName = ''
        for (let j = 0; j < this.salesAreas.length; j++) {
          for (let p = 0; p < row.salesAreaIds.length; p++) {
            if (row.salesAreaIds[p] === this.salesAreas[j].id) {
              objName = objName + this.salesAreas[j].name + ','
            }
          }
        }
        return objName.slice(0, objName.length - 2)
      } else if(row.mdField === '权限信息-参与活动'){
        if(row.fieldName === 'promotionScope'){
          return row.originValue === '1'?'全部活动':(row.originValue === '2'?'指定活动类型':'-')
        }else if(row.fieldName === 'promotionTypes'){
          let originName = ''
          if(row.originValue !== undefined && row.originValue !== null){
            let originValue = row.originValue.split(",")
            originValue.forEach(item => {
              if(item === '1'){
                originName = originName + '营销活动'+','
              }else if(item === '2'){
                originName = originName + '阶梯活动'+','
              }
            })
          }else {
            originName = '-'
          }
          return originName
        }
      }else {
        return row.originValue
      }
    },
    mdValueStatus(row) { // 修改后
      if (row.mdField === '基本信息-是否同步到ERP') {
        switch (row.mdValue) {
          case '1':
            return '否'
          case '2':
            return '是'
        }
      } else if (row.mdField === '权限信息-币种') {
        switch (row.mdValue) {
          case '0':
            return '-'
          case '1':
            return '人民币'
          case '2':
            return '美元'
        }
      } else if (row.mdField === '基本信息-分销商区域') {
        let objName = ''
        for (let j = 0; j < this.salesAreas.length; j++) {
          for (let p = 0; p < row.mdSalesAreaIds.length; p++) {
            if (row.mdSalesAreaIds[p] === this.salesAreas[j].id) {
              objName = objName + this.salesAreas[j].name + ','
            }
          }
        }
        return objName.slice(0, objName.length - 2)
      } else if(row.mdField === '权限信息-参与活动'){
        if(row.fieldName === 'promotionScope'){
          return row.mdValue === '1'?'全部活动':row.mdValue === '2'?'指定活动类型':row.mdValue === '0'?"不参与活动":'-'
        }else if(row.fieldName === 'promotionTypes'){
          let originName = ''
          if(row.mdValue !== undefined && row.mdValue !== null){
            let mdValue = row.mdValue.split(",")
            mdValue.forEach(item => {
              if(item === '1'){
                originName = originName + '营销活动'+','
              }else if(item === '2'){
                originName = originName + '阶梯活动'+','
              }
            })
          }else {
            originName = '-'
          }
          return originName
        }
      } else {
        return row.mdValue
      }
    },
    salearea() { // 分销商区域
      return this.$http.getSalesareaPoList(this, {page:1, size:10000, openFlag:1}).then(res => {this.salesAreas = res.data.list})
    },
    brandList() { // 品牌名称
      this.$http.getBrandPoList(this, { page: 1, size: 10000, openFlag: '1' }).then(res => {
        if (res.success) {
          this.brands = res.data.list
        }
      })
    },
    gradeList() { // 默认价格等级 || 添加品牌 => 价格等级 || 添加品类 => 价格等级
      this.$http.getGradeList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => {
        if (res.success) {
          this.grades = res.data.list
        }
      })
    },
    // ========= 修改信息部分 =========

    clickLeave() { // 返回合作中分销商列表操作
      if (this.node === 1) {
        this.$router.push({ name: 'distributorcooperating' })
      } else {
        this.$router.push({ name: 'distributorcooperatingn' })
      }
    },
    backpuls() { // 返回分销商审批列表
      this.$router.push({ name: 'distributorcheck' })
    },
    addpuls() { // 返回申请中的分销商列表
      this.$router.push({ name: 'Dapplication' })
    },
    addForess() { // 返回已冻结分销商列表
      this.$router.push({ name: 'Dcooperatingfreeze' })
    },

    isInitialize() { // 页面逻辑
      if (this.$route.query.id) {
        this.loading = true
        this.isCheckDetail() // 是否开启审批
        this.checkJudge() // 页面请求判断
      } else { // 添加合作分销商页面
        this.reusePage4()
        this.isCheckDetail() 
      }
    },
    isEventBus() { // 验证必填项是否完整 (组件页面的必填项验证事件发布)
      eventBus.$on('addSurely', payLoad => { // 合作中分销商必填,基本信息部分
        this.counts++
        this.addSurely = payLoad.valid
        this.checkOr() // 判断选择保存或是同意请求
      })
      eventBus.$on('accSurely', payLoad => { // 合作中分销商必填，账户信息部分
        this.counts++
        this.accSurely = payLoad.valid
        this.checkOr() // 判断选择保存或是同意请求
      })
      eventBus.$on('perSurely', payLoad => { // 合作中分销商必填，权限设置部分
        this.counts++
        this.perSurely = payLoad.valid
        this.checkOr() // 判断选择保存或是同意请求
      })
      eventBus.$on('subSurely', payLoad => { // 合作中分销商必填，店铺分账部分
        this.counts++
        this.subSurely = payLoad.valid
        this.checkOr() // 判断选择保存或是同意请求
      })
    },
    checkOr() { // 判断选择保存或是同意请求
      if (this.checkMsg == 2 || this.checkMsg == 3) { // 审批页面，申请页面 同意
        this.checkRequired(this.checkTypeSumbit)
      } else if (this.checkMsg == 0) {
        this.checkRequired()
      } else {
        // 修改
        this.checkRequired()
      }
    },
    checkJudge() { // 页面请求判断
      if (this.$route.query.checkMsg == 1) { // 合作中分销商进来的页面
        this.reusePage()
      } else if (this.$route.query.checkMsg == 2) { // 分销商审批
        this.reusePage1()
      } else if (this.$route.query.checkMsg == 3) { // 申请中的分销商
        this.reusePage2()
      } else if (this.$route.query.checkMsg == 4) { // 已冻结的分销商
        this.reusePage3()
      } // else if(this.$route.query.checkMsg == 0) { // 添加分销商
      // this.reusePage4()
      // }
    },
    checkRequired() { // 必填项是否完整判断
      if (this.counts === 2 && this.addSurely && this.perSurely && this.checkMsg == 1 && this.node === 2  && this.checkIsAccount == 0 ||
      this.counts ===3 && this.addSurely && this.perSurely && this.subSurely && this.node === 2 && this.checkMsg == 1 && this.checkIsAccount == 1 ||
      this.counts === 3 && this.addSurely && this.accSurely && this.perSurely && this.checkMsg == 1 && this.checkIsAccount == 0 ||
      this.counts === 3 && this.addSurely && this.accSurely && this.perSurely && this.checkMsg == 4 && this.checkIsAccount == 0 ||
      this.counts === 4 && this.addSurely && this.accSurely && this.perSurely && this.subSurely && this.checkMsg == 1 && this.checkIsAccount == 1 ||
      this.counts === 4 && this.addSurely && this.accSurely && this.perSurely && this.subSurely && this.checkMsg == 4 && this.checkIsAccount == 1 ) { // 分销商修改
        this.handleSubmitNet() // 发出判断请求
        this.counts = 0
      } else if (this.counts === 3 && this.checkMsg == 2 && this.checkTypeSumbit == 2) { // 分销商页面审批同意
        this.agreeApprove(this.checkTypeSumbit)
        this.counts = 0
      } else if (this.counts === 3 && this.checkMsg == 2 && this.checkTypeSumbit == 3) { // 分销商页面审批拒绝
        this.agreeApprove(this.checkTypeSumbit)
        this.counts = 0
      } else if (this.counts === 3 && this.checkMsg == 3 && this.checkTypeSumbit == 2 && this.accSurely && this.addSurely && this.perSurely) { // 申请中分销商同意
        this.agreeApprove(this.checkTypeSumbit)
        this.counts = 0
      } else if (this.counts === 3 && this.checkMsg == 3 && this.checkTypeSumbit == 3) { // 申请中分销商拒绝
        this.agreeApprove(this.checkTypeSumbit)
        this.counts = 0
      } else if (this.counts === 3 && this.addSurely && this.accSurely && this.perSurely && this.checkMsg == 0 || 
          this.counts === 4 && this.addSurely && this.accSurely && this.perSurely && this.subSurely && this.checkIsAccount == 1 && this.checkMsg == 0) { // 添加分销商
        this.handleSubmitNet() // 发出判断请求
        this.counts = 0
      } else if (this.counts === 4) {
        this.counts = 0
      }
    },
    reusePage() { // 编辑分销商页面
      this.tianjia = false // 合作中分销商（添加）（接口请求标识）
      this.hezuo = true // 合作中分销商（编辑）（接口请求标识）
      this.shenpi = false // 合作中分销商（审批）（接口请求标识）
      this.shenqing = false // 合作中分销商（申请）（接口请求标识）
      this.dongjie = false // 合作中分销商（冻结）（接口请求标识）

      this.clickLeaveShow = true // 标题: 查看合作中分销商
      this.append = false // 标题: 添加合作中分销商
      this.backpulsShow = false // 标题: 分销商审批列表（查看）
      this.addpulsShow = false // 标题: 申请中的分销商（查看）
      this.addForessShow = false // 标题: 已冻结的分销商（查看）

      this.syncShow = true
      this.nameShow = true
      this.exaShow = false
     
      this.$http.distributorDetail(this, { id: this.$route.query.id }).then(res => {
        if (res.success) {
          this.responseData = res.data
          this.erpFlag = this.responseData.extendData.erpFlag
          this.passShow = false // 密码显示
          this.loading = false // 分销商数据加载 查看加载效果
        }
      })
      eventBus.$on('stocksJudgeData', payLoad => { // 没有任何特价的时候取值特价商品
        this.stocksJudgeData = payLoad.stocksJudgeData
      })

      eventBus.$on('stocksJudgeEvent', payLoad => { // 有特价商品进行取值
        this.stocksJudge = true
        this.stocksData = payLoad.stocksData
      })

      eventBus.$on('scalePriceEvent', payLoad => { // 品牌价格等级
        this.tempArr = payLoad.tempArr
      })

      eventBus.$on('amendEmit', payLoad => { // 修改密码
        this.passShow = true
      })
      eventBus.$on('CoamendEmit', payLoad => { // 取消修改密码
        this.passShow = false
      })
      eventBus.$on('FirstReqShow', payLoad => { // 取消修改密码
      })
    },
    // 不走审批流程
    // reusePage1() { // 审批分销商页面
    //   this.tianjia = false // 合作中分销商（添加）（接口请求标识）
    //   this.hezuo = false // 合作中分销商（编辑）（接口请求标识）
    //   this.shenpi = true // 合作中分销商（审批）（接口请求标识）
    //   this.shenqing = false // 合作中分销商（申请）（接口请求标识）
    //   this.dongjie = false // 合作中分销商（冻结）（接口请求标识）

    //   this.append = false // 标题: 添加合作中分销商
    //   this.clickLeaveShow = false // 标题: 查看合作中分销商
    //   this.backpulsShow = true // 标题: 分销商审批列表（查看）
    //   this.addpulsShow = false // 标题: 申请中的分销商（查看）
    //   this.addForessShow = false // 标题: 已冻结的分销商（查看）
    //   this.nameShow = true
    //   this.syncShow = false
    //   this.exaShow = true

    //   this.$api.get(this, 'admin/u/p/distributor/cooperating/checkDetail', { id: this.$route.query.id }).then(res => { // 审批详情部分
    //     this.responseData = res.distributor
    //     this.response = res // 分销商审批顶部信息
    //     this.flows = res.checkFlows
    //     this.modiData = res.distributor.modifyList // 原特价商品
    //     if (res.checkStatus === 1) { // 此分销商审批是否已经完成 checkStatus: 1, 审批状态 0.未审批 1.审批中，2.审批通过 3.审批未通过'
    //       for (let i = 1; i < res.checkFlows.length; i++) { // 是否轮到自己审批 1: 自己审批 2: 不是自己审批
    //         if (res.checkFlows[i].checkStatus === 1 && res.checkFlows[i].checkUser === store.getters.userinfo.id) {
    //           this.isCheckStatus = true
    //         }
    //       }
    //     }
    //     this.loading = false // 分销商数据加载 查看加载效果

    //     // ==== 特价商品的展示部分 ====
    //     // operationType 1: 新增 2: 修改 3: 删除
    //     for (let u = 0; u < this.modiData.length; u++) {
    //       if (this.modiData[u].mdField == '特价商品') {
    //         this.specialAdd = this.modiData[0].mdCommodities
    //         for (let p = 0; p < this.specialAdd.length; p++) {
    //           if (this.specialAdd[p].operationType === 1) { // 新增的
    //             this.addOperation = true
    //           } else if (this.specialAdd[p].operationType === 3) { // 删除
    //             this.deeOperation = true
    //           } else if (this.specialAdd[p].operationType === 2) { // 修改
    //             this.amendOperation = true
    //           }
    //         }
    //       }
    //     }
    //     // ==== End ====
    //   })
    // },
    reusePage2() { // 申请分销商页面
      this.tianjia = false // 合作中分销商（添加）（接口请求标识）
      this.hezuo = false // 合作中分销商（编辑）（接口请求标识）
      this.shenpi = false // 合作中分销商（审批）（接口请求标识）
      this.shenqing = true // 合作中分销商（申请）（接口请求标识）
      this.dongjie = false // 合作中分销商（冻结）（接口请求标识）

      this.append = false // 标题: 添加合作中分销商
      this.clickLeaveShow = false // 标题: 查看合作中分销商
      this.backpulsShow = false // 标题: 分销商审批列表（查看）
      this.addpulsShow = true // 标题: 申请中的分销商（查看）
      this.addForessShow = false // 标题: 已冻结的分销商（查看）
      this.syncShow = false
      this.exaShow = false

      this.$http.distributorDetail(this, { id: this.$route.query.id }).then(res => {
        this.responseData = res.data
        this.erpFlag = this.responseData.extendData.erpFlag
        this.passShow = false // 密码显示
        this.loading = false // 分销商数据加载 查看加载效果
      })
    },
    reusePage3() { // 已冻结分销商页面
      this.tianjia = false // 合作中分销商（添加）（接口请求标识）
      this.hezuo = false // 合作中分销商（编辑）（接口请求标识）
      this.shenpi = false // 合作中分销商（审批）（接口请求标识）
      this.shenqing = false // 合作中分销商（申请）（接口请求标识）
      this.dongjie = true // 合作中分销商（冻结）（接口请求标识）

      this.append = false // 标题: 添加合作中分销商
      this.clickLeaveShow = false // 标题: 查看合作中分销商
      this.backpulsShow = false // 标题: 分销商审批列表（查看）
      this.addpulsShow = false // 标题: 申请中的分销商（查看）
      this.addForessShow = true // 标题: 已冻结的分销商（查看）
      this.syncShow = false
      this.exaShow = true
      this.nameShow = true

      // 冻结分销商详情部分
      this.$http.distributorDetail(this, { id: this.$route.query.id }).then(res => {
        if (res.success) {
          this.responseData = res.data
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },
    reusePage4() { // 添加分销商页面
      this.tianjia = true // 合作中分销商（添加）（接口请求标识）
      this.hezuo = false // 合作中分销商（编辑）（接口请求标识）
      this.shenpi = false // 合作中分销商（审批）（接口请求标识）
      this.shenqing = false // 合作中分销商（申请）（接口请求标识）
      this.dongjie = false // 合作中分销商（冻结）（接口请求标识）

      this.loading = false // 合作中分销商查看加载效果
      this.append = true // 标题: 添加合作中分销商
      this.clickLeaveShow = false // 标题: 查看合作中分销商
      this.backpulsShow = false // 标题: 分销商审批列表（查看）
      this.addpulsShow = false // 标题: 申请中的分销商（查看）
      this.addForessShow = false // 标题: 已冻结的分销商（查看）

      this.syncShow = false
      this.exaShow = false
      this.passShow = true // 密码显示
      this.checkRequired() // 必填项是否完整判断
    },
    syncDisERP() { // 分销商同步到ERP
      this.loadingErp = true
      this.$http.syncDistributorToERP(this, { id: this.$route.query.id }).then(res => {
        if (res.success) {
          this.$message({
            message: '成功同步到ERP',
            type: 'success',
            duration: 3 * 1000
          })
          this.checkJudge() // 页面请求判断
          this.loadingErp = false
        } else {
          this.loadingErp = false
        }
      })
      setTimeout(() => { // 无法收到反馈则6S结束动画
        this.loadingErp = false
      }, 6000)
    },
  
    isCheckDetail() { // 是否开启审批
      this.$http.checkDetail(this).then(res => {  
        let flag = false
        res.data.forEach(item => {
          if (item.ext === 2) { // ext:1 商品管理上下架审批, 2 分销商编辑审批, 3 仓库库存调整审批,4 仓库库存预留审批, 5 订单价格审批,6 订单对账折扣审批,7 促销活动新增审批 8 促销活动编辑审批
            flag = true
            if (item.openFlag === 1 && this.hezuo) { // openFlag 1. 审批开启, 2. 审批关闭
              this.hintStr = '分销商修改提交审批'
              this.checkSync = true
              console.log('分销商审批已开启')
            } else if (item.openFlag === 2 && this.hezuo) {
              this.hintStr = '提交成功'
              this.checkSync = false
              flag = false
            } else if (item.openFlag === 1 && this.tianjia) {
              this.hintStr = '分销商成功添加至审批流程'
            } else if (item.openFlag === 2 && this.tianjia) {
              this.hintStr = '添加成功'
              flag = false
            } else if (item.openFlag === 1 && this.shenqing) {
              this.hintStr = '操作成功，待审批通过后方可成为合作中的分销商'
            } else if (item.openFlag === 2 && this.shenqing) {
              this.hintStr = '操作成功'
              flag = false
            }
          }
        })
        if (flag === false) { // 未开启分销商审批配置
          if (this.hezuo) {
            this.hintStr = '提交成功'
          } else if (this.tianjia) {
            this.hintStr = '添加成功'
          }
        }
      })
    },
    checkSyncs(type) { // 保存操作
      // 联系人信息必填验证
      for(let i=0; i<this.$refs.roleList.tableData.length; i++) {
        if (!this.$refs.roleList.tableData[i].password) {
            this.$message.error('请完善联系人信息-联系人密码')
            return
        }
        if (!this.$refs.roleList.tableData[i].name || this.$refs.roleList.tableData[i].sex === undefined
          || !this.$refs.roleList.tableData[i].roleId
          || this.$refs.roleList.tableData[i].ownerFlag === undefined
          || (this.$refs.addcomponents.formData.address.countryId === 37 && !this.$refs.roleList.tableData[i].phone)
          || (this.$refs.addcomponents.formData.address.countryId && 
          this.$refs.addcomponents.formData.address.countryId !== 37 && (!this.$refs.roleList.tableData[i].email && !this.$refs.roleList.tableData[i].phone))
          ) {
            this.$message.error('请完善联系人信息')
            return
        }
      }
      // 状态
      this.defaultData.applyStatus = this.responseData.applyStatus
      // ..登录信息部分
      this.defaultData.name = this.$refs.addcomponents.formData.name // 用户名
      // this.defaultData.mobile = this.$refs.addcomponents.formData.mobile // 登录手机 (原“手机”)
      if (this.passShow) {
        this.defaultData.password = md5(this.$refs.addcomponents.formData.password) // 修改密码
      } else {
        this.defaultData.password = this.responseData.password
      }
     
      this.defaultData.address.addressType = 1 // 地址类型  1 公司地址
      this.defaultData.email = this.$refs.addcomponents.formData.email // 电子邮件

      // ..基本信息部分
      this.defaultData.address.countryId = this.$refs.addcomponents.formData.address.countryId // 国家ID
      this.defaultData.address.provinceId = this.$refs.addcomponents.formData.address.provinceId // 省份ID
      this.defaultData.address.cityId = this.$refs.addcomponents.formData.address.cityId // 城市ID
      this.defaultData.address.districtId = this.$refs.addcomponents.formData.address.districtId // 区ID

      this.defaultData.companyType = this.$refs.addcomponents.formData.companyType // 公司类型
      this.defaultData.extendData.erpId = this.$refs.addcomponents.formData.extendData.erpId // erp内码
      this.defaultData.extendData.erpFlag = this.$refs.addcomponents.formData.extendData.erpFlag // 是否同步到ERP
      this.defaultData.extendData.erpNo = this.$refs.addcomponents.formData.extendData.erpNo // 分销商ID
      this.defaultData.companyName = this.$refs.addcomponents.formData.companyName // 客户名称 (原“公司名/分销商名“)
      this.defaultData.address.address = this.$refs.addcomponents.formData.address.address // 客户联系地址 (原“联系地址”)
      this.defaultData.address.zipCode = this.$refs.addcomponents.formData.address.zipCode // 邮编
      this.defaultData.extendData.certNo = this.$refs.addcomponents.formData.extendData.certNo // 营业执照号/身份证号
      this.defaultData.salesAreaIds = this.$refs.addcomponents.formData.salesAreaIds && this.$refs.addcomponents.formData.salesAreaIds.length>0 ? this.$refs.addcomponents.formData.salesAreaIds : [] // 分销商区域
      
      // this.defaultData.registerName = this.$refs.addcomponents.formData.registerName // 客户联系人 (原“姓名/联系人名”)
      // this.defaultData.phone = this.$refs.addcomponents.formData.phone // 客户联系人电话 (原“固定电话”，改为必填，不限制输入格式)
       
      
     
      
      this.defaultData.taxType = this.$refs.addcomponents.formData.taxType // 税种类型
      this.defaultData.procureName = this.$refs.addcomponents.formData.procureName // 采购联系人-联系人姓名 (原“姓名/联系人名”)
      // this.defaultData.procureSex = this.$refs.addcomponents.formData.procureSex // 采购联系人-联系人性别  (原“姓名/联系人名”)
      this.defaultData.procurePhone = this.$refs.addcomponents.formData.procurePhone // 采购联系人-联系人电话  (原“姓名/联系人名”)
      this.defaultData.procureEmail = this.$refs.addcomponents.formData.procureEmail // 采购联系人-联系人邮箱  (原“姓名/联系人名”)
      this.defaultData.companyRepresent = this.$refs.addcomponents.formData.companyRepresent // 公司负责人-联系人电话 新添加
      this.defaultData.companyRepresentPhone = this.$refs.addcomponents.formData.companyRepresentPhone // 公司负责人联系电话  新添加

      // ..账户信息部分
      // if (this.node!==2 && this.defaultData.extendData.erpFlag !== 0) { // 多级分销商不同步erp时不显示
      if (this.checkMsg === 0 || (this.node===2 && this.checkIsErp===1) || this.node===1) {
        this.defaultData.financial.bankAccountName = this.$refs.accountInformation.formData.financial.bankAccountName // 银行账户名
        this.defaultData.financial.bankDepositName = this.$refs.accountInformation.formData.financial.bankDepositName // 开户行
        this.defaultData.financial.bankAccount = this.$refs.accountInformation.formData.financial.bankAccount // 银行账户 (原‘银行账号’)
        // this.defaultData.billCheckName = this.$refs.accountInformation.formData.billCheckName // 对账人姓名
        // this.defaultData.billCheckPhone = this.$refs.accountInformation.formData.billCheckPhone // 对账人联系电话
        // this.defaultData.billCheckEmail = this.$refs.accountInformation.formData.billCheckEmail // 对账人邮箱
        // this.defaultData.invoice = this.$refs.accountInformation.formData.invoice // 公司 (原‘发票抬头’)
        this.defaultData.financial.invoiceTitleName = this.$refs.accountInformation.formData.financial.invoiceTitleName // 开票公司名称
        if (this.$refs.addcomponents.formData.companyType == 1 || this.$refs.addcomponents.formData.companyType == 2) {
          this.defaultData.financial.taxpayerNumber = this.$refs.accountInformation.formData.financial.taxpayerNumber // 纳税人识别号
        }
        this.defaultData.financial.taxType = this.$refs.accountInformation.formData.financial.taxType // 税种类型
        this.defaultData.financial.registeredPhone = this.$refs.accountInformation.formData.financial.registeredPhone // 开票对应电话 (原‘增值税发票注册电话’)
        this.defaultData.financial.registeredAddress = this.$refs.accountInformation.formData.financial.registeredAddress // 开票对应地址( 原'增值税发票注册地址')
        this.defaultData.financial.registeredBankDepositName = this.$refs.accountInformation.formData.financial.registeredBankDepositName // 开票对应银行
        this.defaultData.financial.registeredBankAccount = this.$refs.accountInformation.formData.financial.registeredBankAccount // 开票对应账户
        this.defaultData.financial.tradeId = this.$refs.permissions.formData.financial.tradeId // 收款条件
        this.defaultData.financial.coinType = this.$refs.permissions.formData.financial.coinType // 币种
      } else {
        this.defaultData.financial = undefined
      }
      // ..权限设置部分
      this.defaultData.business.salesId = this.$refs.permissions.formData.business.salesId // 业务员

      // 分销商分组
      this.defaultData.business.distributorGroupIds = this.$refs.permissions.formData.business.distributorGroupIds;
      
      this.defaultData.business.distributorCategoryId = this.$refs.permissions.formData.business.distributorCategoryId // 分销商类别
      this.defaultData.business.autoDelivery = this.$refs.permissions.formData.business.autoDelivery // 是否直发
      this.defaultData.business.canExportPrice = this.$refs.permissions.formData.business.canExportPrice // 商品导出
      this.defaultData.business.promotionScope = this.$refs.permissions.formData.business.promotionScope // 参与活动
      this.defaultData.business.promotionTypes = this.$refs.permissions.formData.business.promotionTypes // 可参与活动类型
      this.defaultData.business.rxShopSwitch = this.$refs.permissions.formData.business.rxShopSwitch //是否开启柔性店铺
      this.defaultData.business.logisticsSmsSwitch = this.$refs.permissions.formData.business.logisticsSmsSwitch //是否短信提示物流信息
      this.defaultData.business.onWayFlag = this.$refs.permissions.formData.business.onWayFlag // 是否支持在途
      this.defaultData.extendData.customerFlag = this.$refs.permissions.formData.extendData.customerFlag // 是否开启C端模式
      this.defaultData.extendData.customerMode = this.$refs.permissions.formData.extendData.customerMode // C端结算方式
      this.defaultData.extendData.distributionFlag = this.$refs.permissions.formData.extendData.distributionFlag // 是否开启分销模式
      this.defaultData.extendData.distributionMode = this.$refs.permissions.formData.extendData.distributionMode // 分销模式
	  this.defaultData.extendData.distributionPayWay = this.$refs.permissions.formData.extendData.distributionPayWay // 上级付款方式
      this.defaultData.extendData.distributionAutoFlag = this.$refs.permissions.formData.extendData.distributionAutoFlag // 分销订单是否自动审核
      this.defaultData.extendData.distributionPromotionFlag = this.$refs.permissions.formData.extendData.distributionPromotionFlag // 分销活动是否同步
      this.defaultData.extendData.erpBalanceFlag = this.checkIsErp===1 || this.node===1 ? this.$refs.permissions.formData.extendData.erpBalanceFlag : undefined // ERP余额是否同步
      this.defaultData.business.scalePriceId = this.$refs.permissions.formData.business.scalePriceId // 默认价格等级
      this.defaultData.business.distributionScalePriceId = this.$refs.permissions.formData.business.distributionScalePriceId // 默认分销价格等级
      // this.defaultData.productlines = this.$refs.permissions.arr2 //  品类价格等级 {1.brandId，品牌编号，2.scalePriceId，等级编号，3.productlineId，产品线编号}

      // 是否开启分账
      if (this.defaultData.extendData.customerFlag === 1 && this.defaultData.extendData.customerMode === 3) {
        this.defaultData.extendData.subAccountFlag = this.$refs.permissions.formData.extendData.subAccountFlag
        // 店铺分账
        this.defaultData.subAccountAdminConfigCmd.agingType = this.$refs.subAccount.formData.subAccountAdminConfigCmd.agingType // 分账时效类型 1、实时 2、延迟
        // 延迟分账时间
        this.defaultData.subAccountAdminConfigCmd.delayTime = this.defaultData.subAccountAdminConfigCmd.agingType === 2 ? parseInt(this.$refs.subAccount.formData.subAccountAdminConfigCmd.delayTime) : undefined
        this.defaultData.subAccountAdminConfigCmd.levelNameList = this.$refs.subAccount.formData.subAccountAdminConfigCmd.levelNameList // 分账等级 
      } else {
        this.defaultData.extendData.subAccountFlag = undefined
      }

      
      if(this.defaultData.business.promotionScope === 2 && this.$refs.permissions.formData.business.promotionTypes.length > 0){
        this.defaultData.business.promotionTypes = this.$refs.permissions.formData.business.promotionTypes.join(',')
      }else {
        this.defaultData.business.promotionTypes = undefined
      }
      if(this.defaultData.business.distributorCategoryId === 'KHLB002_SYS' && this.defaultData.business.autoDelivery === 1){// 寄售客户不能为直发客户
        this.$message.error("抱歉，寄售客户不能同时为直发客户！")
        return
      }

      if (this.checkMsg == 2 && type === 3 || this.checkMsg == 3 && type === 3) { // 申请，审批，的拒绝不做必填验证，
        this.agreeApprove(type) // ..审批同意请求
      } else { // ..其余页面均需要验证必填
        this.checkTypeSumbit = type
        // this.isEventBus()
        this.$refs.addcomponents.handleSubmit() // ..基本信息,必填验证
        if ((this.node!==2 && this.defaultData.extendData.erpFlag !== 0) || this.node === 1) { // 多级分销商不同步erp时不显示
          this.$refs.accountInformation.handleSubmit() // ..账户信息,必填验证
        }
        this.$refs.permissions.handleSubmit() // ..权限设置,必填验证
        if (this.defaultData.extendData.subAccountFlag === 1) {
          this.$refs.subAccount.handleSubmit() // 店铺分账，必填验证
        }
        
        this.isEventBus()
      }
    },
    // 分销商信息更新
    handleValidate() {
      // 品牌价格等级
      let arr = this.$refs.permissions.arr // 更改后数据
      let delArr = this.$refs.permissions.delArr // 删除数据
      let scalePriceArr = []
      if (arr.length > 0) {
        arr.forEach(item2 => {
          if (!item2.id) {
            item2.operationType = 1
            scalePriceArr.push(item2)
          }
          if (this.tempArr.length > 0) {
            this.tempArr.forEach(item => {
                if (item.id === item2.id && (item.brandId !== item2.brandId || item.scalePriceId !== item2.scalePriceId || item.distributionScalePriceId !== item2.distributionScalePriceId)) {
                  item2.operationType = 2 // 修改
                  scalePriceArr.push(item2)
                }
            })
          }
        })
      }
      if (delArr.length>0) {
        delArr.forEach(item => {
          scalePriceArr.push({
            id:item.id,
            brandId: item.bId ? item.bId : item.brandId,
            scalePriceId: item.scalePriceId,
            distributionScalePriceId: item.distributionScalePriceId,
            operationType: 3 // 删除
          })
        })
      }
      this.defaultData.scalePrices = scalePriceArr // 品牌价格等级 {1.brandId,品牌编号，2.scalePriceId}
      // TODO特价商品取值部分
      if (this.stocksJudge || this.stocksJudgeData) { // ..如果有特价商品的修改才进行取值
        // ..operationType 1: 新增 2: 修改 3: 删除
        const isStocksData = this.stocksData.map(item => ({ // ..页面进来初始数组
          goodsId: item.goodsId, // 商品ID
          goodsName: item.goodsName, // 商品名称
          goodsNo: item.goodsNo, // 商品编号
          goodsItemId: item.goodsItemId, // 货品ID
          itemName: item.itemName, // 货品名称
          itemCode: item.itemCode, // 商品编号
          id: item.id,
          distributorPrice: Number(item.distributorPrice) // ..分销商特价
        }))
        // const isStocks = this.$refs.specialcomponents.tmpStocks.map(item => ({ // ..最后修改完成的数组
        this.checkStock = []
        const isStocks = this.$refs.specialcomponents.stocks.map(item => ({
          goodsId: item.goodsId, // 商品ID
          goodsName: item.goodsName, // 商品名称
          goodsNo: item.goodsNo, // 商品编号
          goodsItemId: item.goodsItemId?item.goodsItemId:item.id, // 货品ID
          itemName: item.itemName, // 货品名称
          itemCode: item.itemCode, // 商品编号
          id: item.id,
          distributorPrice: Number(item.distributorPrice) // ..分销商特价
        }))
        for (let k = 0; k < isStocksData.length; k++) { // 软删除
          let isDelete = true
          for (let p = 0; p < isStocks.length; p++) {
            if (isStocks[p].id === isStocksData[k].id) {
              isDelete = false
              break
            }
          }
          if (isDelete) {
            isStocksData[k].operationType = 3
            this.checkStock.push(isStocksData[k])
          }
        }
        for (let j = 0; j < isStocks.length; j++) {
          let isAdd = true
          for (let i = 0; i < isStocksData.length; i++) {
            if (isStocksData[i].id === isStocks[j].id) { // 修改特价商品
              isAdd = false
              if (isStocksData[i].distributorPrice !== isStocks[j].distributorPrice) {
                isStocks[j].operationType = 2
                this.checkStock.push(isStocks[j])
              }
              break
            }
          }
          if (isAdd && isNaN(isStocks[j].distributorPrice)) { // 添加新增特价商品
            this.$message({
              message: '分销商特价没有填写完整',
              type: 'warning',
              duration: 3 * 1000
            })
            return
          } else if (isAdd) {
            isStocks[j].id = undefined
            isStocks[j].operationType = 1
            this.checkStock.push(isStocks[j])
          }
        }

        // 分销商特价商品
        this.defaultData.specialGoods = this.checkStock.length>0?this.checkStock:undefined
      } else {
        this.defaultData.specialGoods = undefined
      }
      // 修改时联系人信息
      let contactData = this.$refs.roleList.tableData
     
      let contactArr = []
      contactData.forEach(item => {
        if (!item.id) {
          item.operationType = 1 // 新增
          contactArr.push(item)
        }
      })
      this.$refs.roleList.editArr.forEach(item => {
        item.operationType = 2 // 修改
        contactArr.push(item)
      })
      this.$refs.roleList.delArr.forEach(item => {
        item.operationType = 3 // 删除
        contactArr.push(item)
      })
      this.defaultData.contacts = []
      this.defaultData.contacts = contactArr
      this.defaultData.comment = this.comment // 审批备注
    },
    handleSubmitNet() { // ..分销商添加、编辑保存操作
      this.handleValidate()
      // 判断联系人信息是否有主帐号
      if (this.$refs.roleList.tableData.length > 0) {
        let count = []
        // 判断联系人信息是否有设置主帐号
        this.$refs.roleList.tableData.forEach(item => {
          count.push(item.ownerFlag)
        })
        let map = count.reduce((m, x) => m.set(x, (m.get(x) || 0) + 1), new Map())
        if (!map.get(1) || map.get(1) < 1) {
          this.$message.error('联系人信息请设置一个主帐号！')
          return
        }
      }
      
      // 状态
      this.defaultData.applyStatus = 2  // 后台添加分销商 3-申请通过
      // 是否开启分账
      console.log(this.defaultData.extendData.customerFlag)
      if (this.defaultData.extendData.subAccountFlag === 1) {
        if (this.$refs.subAccount.formData.subAccountAdminConfigCmd.levelNameList.length === 0) {
          this.$message.error('请添加分账等级！')
          return
        } else {
          this.defaultData.subAccountAdminConfigCmd.levelNameList = this.$refs.subAccount.formData.subAccountAdminConfigCmd.levelNameList // 分账等级 
        }
      }

      if (this.hezuo === true) { // 编辑合作中分销商
        this.defaultData.id = parseInt(this.$route.query.id)
        this.defaultData.business.id = this.responseData.business.id
        this.defaultData.subAccountAdminConfigCmd.distributorId = parseInt(this.$route.query.id)
        
        this.loading2 = this.$loading({
          lock: true,
          text: '拼命修改中...请稍等',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        setTimeout(() => { // 无法收到反馈则6S结束动画
          this.loading2.close()
        }, 6000)
        
        this.$http.editDistributor(this, this.defaultData).then(res => { 
          if (res.success) {
            this.$message({
              message: '提交成功',
              type: 'success',
              duration: 3 * 1000
            })
            this.loading2.close()
            this.$router.go(-1)
          } else if (res.code === 394) {
            this.loading2.close()
            this.$router.go(-1)
          } else if (res.code === 357) {
            this.loading2.close()
            this.$refs.addcomponents.formData.mobile = ''
          } else {
            this.loading2.close()
          }
        })
      }
      if (this.tianjia === true) { // 添加合作中分销商
        if (this.defaultData.contacts.length === 0) {
          this.$message.error('请至少添加一个联系人信息！')
          return false
        }
        this.loading3 = this.$loading({
          lock: true,
          text: '拼命添加中，请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        setTimeout(() => { // 无法收到反馈则6S结束动画
          this.loading3.close()
        }, 6000)
        this.$http.addDistributor(this, this.defaultData).then(res => {  
          if (res.success) {
            this.$message({
              message: '添加成功',
              type: 'success',
              duration: 3 * 1000
            })
            this.loading3.close()
            this.$store.dispatch('updateDistributors')
            this.$router.push({ name: 'distributorcooperating' })
          } else {
            this.loading3.close()
          }
        })
      }
    },
    agreeApprove(type) { // 审批保存部分: "type": 2.同意操作 3.拒绝
      this.handleValidate()
      // if (this.shenpi === true) { // 分销商审批同意，拒绝
        if (this.checkMsg == 2 && type === 2) { // checkMsg == 2（3） 分销商审批页面（申请中分销商）； type == 2（3） 同意操作（拒绝）
          this.$confirm('确定审批通过/审批拒绝？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            center: true
          }).then(_ => {
            this.loading4 = this.$loading({
              lock: true,
              text: '拼命通过中...请稍等',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            // setTimeout(() => { // 无法收到反馈则6S结束动画
            //   this.loading4.close()
            // }, 6000)
            this.$http.disNextCheck(this, {
              id: this.responseData.distributorId,
              // checkStatus: type,
              // remark: this.comment
              applyStatus:type
            }).then(res => {
              if (res.success) {
                this.$message({
                  message: '您同意该审批申请',
                  type: 'success',
                  duration: 3 * 1000
                })
                this.loading4.close()
                this.$router.push({ name: 'distributorcheck' })
              } else if (res.success) {
                // this.distriData()
                // this.$router.push({ name: 'distributorcheck' })
                this.loading4.close()
              } else {
                this.loading4.close()
              }
              this.loading4.close() // 超时关闭
            })
          })
        } else if (this.checkMsg == 2 && type === 3) {
          this.$confirm('确定审批通过/审批拒绝？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            center: true
          }).then(_ => {
            this.loading4 = this.$loading({
              lock: true,
              text: '拼命通过中...请稍等',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            setTimeout(() => { // 无法收到反馈则6S结束动画
              this.loading4.close()
            }, 6000)
            this.$http.disNextCheck(this, {  
              id: this.responseData.distributorId,
              // checkStatus: type,
              // remark: this.comment
              applyStatus:type
            }).then(res => {
              if (type === 3 && res.success) {
                this.$message({
                  message: '你已拒绝该审批申请',
                  type: 'success',
                  duration: 3 * 1000
                })
                this.loading4.close()
                this.$router.push({ name: 'distributorcheck' })
              } else if (res.code !== 0) {
                // this.distriData()
                this.$router.push({ name: 'distributorcheck' })
                this.loading4.close()
              } else {
                this.loading4.close()
              }
            })
          })
        }
      // }
      // if (this.shenqing === true) { // 申请分销商同意，拒绝
        if (this.checkMsg == 3 && type === 2) { // checkMsg == 2（3） 分销商审批页面（申请中分销商）； type == 2（3） 同意操作（拒绝）
          if(type === 2){
            this.hintSt = "你已同意申请中的分销商"
          }else if(type === 3){
            this.hintSt = "你已拒绝申请中的分销商"
          }
          this.$confirm('确定要同意该分销商申请？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            center: true
          }).then(_ => {
            this.loading5 = this.$loading({
              lock: true,
              text: '拼命同意中...请稍等',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            this.defaultData.id = this.$route.query.id
            // this.defaultData.capitalStatus = type
            this.defaultData.applyStatus = type
            // applyData(this, this.defaultData).then(res => {
            this.$http.editDistributor(this, this.defaultData).then(res => {  
              if (res.success) {
                this.$message({
                  message: "操作成功",
                  type: 'success',
                  duration: 3 * 1000
                })
                this.loading5.close()
                this.$store.dispatch('updateDistributors')
                if (this.checkSync === true) { // 开启审批跳转至审批列表
                  this.$router.push({ name: 'distributorcheck' })
                } else if (this.checkSync === false) { // 审批未开启跳转至合作中列表
                  this.$router.push({ name: 'distributorcooperating' })
                }
              } 
              // else if (res.code !== 0) { // ..给多端审批适用
              //   // this.distriData()
              //   if (this.checkSync === true) { // 开启审批跳转至审批列表
              //     this.$router.push({ name: 'distributorcheck' })
              //   } else if (this.checkSync === false) { // 审批未开启跳转至合作中列表
              //     this.$router.push({ name: 'distributorcooperating' })
              //   }
              //   this.loading4.close()
              // }
               else {
                this.loading5.close()
              }
            })
          }).catch(_ => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
        } else { // 分销商申请中拒绝不进行必填项验证
          this.$confirm('确定要拒绝该分销商申请？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            center: true
          }).then(_ => {
            this.loading5 = this.$loading({
              lock: true,
              text: '拼命拒绝中...请稍等',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            })
            // applyData(this, { id: this.$route.query.id, capitalStatus: type }).then(res => {
            this.$http.refuseDistributor(this, { id: this.$route.query.id }).then(res => {    
              if (res.success) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 3 * 1000
                })
                this.loading5.close()
                this.$router.push({ name: 'Dapplication' })
              } 
              // else if (res.code !== 0) {
              //   // this.distriData()
              //   this.$router.push({ name: 'Dapplication' })
              //   this.loading4.close()
              // } 
              else {
                this.loading5.close()
              }
            })
          }).catch(_ => {
            this.$message({
              type: 'info',
              message: '已取消操作'
            })
          })
        }
      // }
    },
    handleEdit() { // 编辑操作 "applyType": 1.后台添加 2.前台注册申请
      if (this.response.applyType == 2) {
        this.$router.push({ name: 'distributorcooperatingadd', query: { id: this.responseData.distributorId, checkMsg: 3 }})
      } else if (this.response.applyType == 1) {
        this.$router.push({ name: 'distributorcooperatingadd', query: { id: this.responseData.distributorId, checkMsg: 1 }})
      }
      this.isInitialize()
    },
    forstatus(row) { // 审批类型
      switch (row) {
        case 1:
          return '分销商新增审批'
        case 2:
          return '分销商编辑审批'
      }
    },
    cStatus(row) { // 审批状态
      switch (row) {
        case 0:
          return '未审批'
        case 1:
          return '审批中'
        case 2:
          return '审批通过'
        case 3:
          return '审批拒绝'
      }
    },
    timeFormatter(cellValue) { // 时间格式化
      return parseTime(cellValue)
    }
  },
  watch: {
    responseData(val) {
      /**
			 * 审批修改的信息
			 * brands 修改前的品牌等级
			 * mdBrands 修改的品牌等级
			 * productlines 修改前的品类等级
			 * mdProductlines 修改后的品类等级
			 */
      // 是否开启分账
      val.extendData.subAccountFlag = val.extendData.subAccountFlag ? val.extendData.subAccountFlag : 0
      if (val.extendData.customerFlag === 1 && val.extendData.customerMode === 3 && val.extendData.subAccountFlag === 1) { // 是否开启分账
        this.checkIsAccount = val.extendData.subAccountFlag
      } else {
        this.checkIsAccount = 0
      }
      // if(this.checkMsg == 2) {
      if (this.exaShow && val.modifyList !== undefined && val.modifyList !== null) {
        for (let k = 0; k < val.modifyList.length; k++) {
          if (val.modifyList[k].mdField === '价格等级-品牌价格等级') {
            this.arr = val.modifyList[k].brands // 修改前的品牌等级
            this.arr2 = val.modifyList[k].mdBrands // 修改后的品牌等级
          } else if (val.modifyList[k].mdField === '价格等级-品类价格等级') {
            this.arr3 = val.modifyList[k].productlines // 修改前的品类等级
            // 不使用
            // this.$api.get(this, 'admin/u/p/productline/list', { page: 1, count: 10000, brandId: val.modifyList[k].productlines.brandId }).then(res => {
            //   const ary = []
            //   res.productlines.forEach(item => {
            //     if (item.isOpen) {
            //       ary.push(item)
            //     }
            //   })
            //   this.productlinelist = ary
            // })
            this.arr4 = val.modifyList[k].mdProductlines // 修改后的品类等级
            // this.$api.get(this, 'admin/u/p/productline/list', { page: 1, count: 10000, brandId: val.modifyList[k].mdProductlines.brandId }).then(res => {
            //   const arys = []
            //   res.productlines.forEach(item => {
            //     if (item.isOpen) {
            //       arys.push(item)
            //     }
            //   })
            //   this.productlinelist = arys
            // })
          } else if (val.modifyList[k].mdField === '特价商品') {
            this.tableData = val.modifyList[k].mdCommodities // 特价商品
          }
        }
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">

  @import '../scss/cooperatingadd.scss';
</style>
