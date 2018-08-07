<template>
  <div class="add-goods" @click="cancelBC(false)">
    <header v-if="append">
      <h4>添加商品</h4>
      <el-button @click="goReturn"  class="mini-back-btn btn-home" icon="el-icon-d-arrow-left">返回商品列表</el-button>
    </header>
    <header v-if="redact">
      <h4>编辑商品</h4>
      <el-button @click="goReturn"  class="mini-back-btn btn-home" icon="el-icon-d-arrow-left">返回商品列表</el-button>
    </header>
    <header v-if="looking">
      <h4>查看商品</h4>
      <el-button @click="goReturn"  class="mini-back-btn btn-home" icon="el-icon-d-arrow-left">返回商品列表</el-button>
    </header>
    <div class="nav">

      <el-tabs v-model="radio" style="radio">
        <el-tab-pane label="通用信息" name="1" ref="DisFirst"></el-tab-pane>
        <el-tab-pane label="扩展数据" name="2"></el-tab-pane>
        <el-tab-pane label="属性参数" name="3"></el-tab-pane>
        <el-tab-pane label="商品图片" name="4"></el-tab-pane>
        <el-tab-pane label="详细信息" name="5"></el-tab-pane>
        <!-- <el-tab-pane label="配件" name="5"></el-tab-pane>
        <el-tab-pane label="关联商品" name="6"></el-tab-pane> -->
        <!-- <el-tab-pane label="标签" name="7"></el-tab-pane> -->
      </el-tabs>
    </div>
    <div class="content" v-loading="loading">
       <!-- 通用信息 -->
      <default-info  
        v-show="radio==1"
        ref="default" 
        :compile="compile" 
        :updownSaleStatus="responseData.updownSaleStatus" 
        :productlinelist="productlinelist" 
        :categorys="responseData.classifyIds"
        :distributorGroups="responseData.distributorGroupIds"
        :distributors="responseData.distributorIds" 
        :departments="responseData.departmentIds" 
        :admins="responseData.adminIds" 
        :goods="responseData" 
        :productlines="responseData.productlines" 
        :grades="responseData.scalePriceIds" 
        :warehouses="responseData.warehouseIds"></default-info>
      <!-- 扩展数据 -->
      <extend :productlinelist="productlinelist" :isAdd="isAdd" :redact="redact" v-show="radio==2" ref="extend" :customizedItems="responseData.customizedItems" :diysType="responseData.diyType" :goodsType="responseData.goodsType" :materialsType="responseData.materialType" :items="responseData.goodsItems" :gradePrices="responseData.gradePrices"></extend>
      <!-- 属性参数 -->
      <attribute-parameters v-show="radio==3" ref="attribute" :goodsLabelRefs="responseData.tagIds" :goodsParam="responseData.paramIds"></attribute-parameters>
      <!-- 商品图片 -->
      <upload-img v-show="radio==4" ref="uploadImg" :goods="responseData"></upload-img>
      <!-- 详细信息 -->
      <detail v-show="radio==5" ref="detail" :goods="responseData"></detail>
      <!-- 配件 -->
      <parts v-show="radio==6" ref="parts" :partGroup="responseData.partGroups" :partGroupGoods="responseData.partGorupGoods"></parts>
      <!-- 关联商品 -->
      <relevance v-show="radio==7" ref='relevance' :relationGoods="responseData.relationGoods" ></relevance>
    </div>
    <div class="clearfix footbtn">
        <el-button class="mini-search-btn" :loading="loading" style="margin-left: 46.5%;" @click="handleSubmit(1)" v-if="checkFreeze !== 2">保存</el-button>
        <el-button v-if="checkFreeze !== 2" size="mini" @click="cancel()">返回</el-button>
        <el-button v-else size="mini" style="margin-left: 46.5%;" @click="cancel()">返回</el-button>
    </div>
  </div>
</template>
<script>
import defaultInfo from "./components/default"
import extend from './components/extend'
import attributeParameters from './components/attributeParameters'
import detail from './components/detail'
import parts from './components/parts'
import uploadImg from "./components/uploadImg"
import relevance from "./components/relevance"
import { isString } from "@/utils/index"
import eventBus from '@/views/order/eventBus'
export default {
  name: 'addgoods',
  data() {
    return {
      loading:true,
      append: true, // 添加商品标题
      redact: false, // 编辑商品标题
      looking: false, // 查看商品标题
      isAdd:true,
      radio: '1',
      defaultData: {
        distributorScopeNo: 0
      },
      extendData: [],
      tempData: [], // 临时扩展数据
      tempDatas:[],
      imgUrl: [],
      contentUrl: '',
      contentUrlEn: '', // 详细信息 - 英文
      partGroups: [],
      productlinelist:[],
      relationGoodsIds: '',
      responseData:{
        gradePrices:[],
        goodsType: 1
        // goods:{
        //   goodsType:1
        // }
      },
      compile: false,
      updownSaleStatus: '',
      needCheckFlag:0,
    }
  },
  components: { defaultInfo, extend, detail, parts, uploadImg, relevance,attributeParameters },
  created() {
    // 编辑商品时数据来源（如果是编辑进来的页面，就带参数进来请求数据赋值到页面上）
    this.isEdit = false
    if(this.$route.query.id){
      this.redact = true;
      this.append = false;
      this.isAdd = false;
      this.$http.getGoodsDetail(this, { id:this.$route.query.id }).then(res => {
        if (res.success) {
          if(res.data.goodsType === 1 || res.data.goodsType === 3 || res.data.goodsType === 2){// 在赋值之前用来初始化specificationName的值（规格 + 颜色）
            res.data.goodsItems.forEach(item =>{
              // item.specificationName = ''
              // item.colorName = ''
            })
          }
          if(res.data !== null) {
            if(res.data.imageUrl1 !== undefined && res.data.imageUrl1 !== ''){
              res.data.imageUrl11 = res.data.imageUrl1+'?x-oss-process=image/resize,h_292,l_292'
            }
            if(res.data.imageUrl2 !== undefined && res.data.imageUrl2 !== ''){
              res.data.imageUrl22 = res.data.imageUrl2+'?x-oss-process=image/resize,h_292,l_292'
            }
            if(res.data.imageUrl3 !== undefined && res.data.imageUrl3 !== ''){
              res.data.imageUrl33 = res.data.imageUrl3+'?x-oss-process=image/resize,h_292,l_292'
            }
            if(res.data.imageUrl4 !== undefined && res.data.imageUrl4 !== ''){
              res.data.imageUrl44 = res.data.imageUrl4+'?x-oss-process=image/resize,h_292,l_292'
            }
            if(res.data.imageUrl5 !== undefined && res.data.imageUrl5 !== ''){
              res.data.imageUrl55 = res.data.imageUrl5+'?x-oss-process=image/resize,h_292,l_292'
            }
            if(res.data.imageUrl6 !== undefined && res.data.imageUrl6 !== ''){
              res.data.imageUrl66 = res.data.imageUrl6+'?x-oss-process=image/resize,h_292,l_292'
            }
          }
        }
        this.responseData = res.data
        // 指定分销商分组
				if (res.data.distributors) {
					this.responseData.distributorIds = []
					res.data.distributors.forEach(item => {
						this.responseData.distributorIds.push({
              id: item.distributorId,
							companyName: item.companyName,
							name: item.name
						})
					})
          this.responseData.distributors = undefined
				}
        // 指定分销商分组
				if (res.data.distributorGroups) {
					this.responseData.distributorGroupIds = []
					res.data.distributorGroups.forEach(item => {
						this.responseData.distributorGroupIds.push({
							id: item.distributorGroupId,
							erpGroupNo: item.erpGroupNo,
							name: item.name
						})
					})
          this.responseData.distributorGroups = undefined
				}
         this.responseData.updownSaleStatus = this.$route.query.saleStatus
          if(this.responseData !== null) {
            this.compile = true
          }
          setTimeout(()=>{
            this.loading = false;
          },1000)
      })
    }else {
      this.loading=false;
      this.isAdd = true;
    }
    //????
    // this.$api.get(this,api.getCheckDetail).then(res=>{
    //   for(let i = 0;i<res.checkConfigs.length;i++){
    //     if(res.checkConfigs[i].ext === 1){
    //       this.needCheckFlag = res.checkConfigs[i].isOpen
    //     }
    //   }
    // })
    // 获取品类列表（不受权限控制）
    this.$http.getCategoryPoList(this, { page: 1, size: 10000, openFlag: 1 }).then(res => {
      if (res.success) {
        this.productlinelist = res.data.list
      }
    })
    this.isEventBus()
  },
  computed: {
    upDowm() { //..上下架状态 1 上架 3 下架
      return this.updownSaleStatus = this.$route.queyr.saleStatus
    },
    check() {
      return this.responseData !== null
    },
    checkFreeze() {
      return this.$route.query.freezeStatus
    }
  },
  beforeDestroy() { // 实例销毁之前执行的钩子 (此处用来销毁eventbus的创建)
    eventBus.$off('saveOn')
    eventBus.$off('handleShelveOff')
    eventBus.$off('handleShelveSuccess')
    eventBus.$off('refreshData')
  },
  methods: {
    refreshData(){
	   console.log("同步货品信息......");
       this.$http.getGoodsDetail(this, { id:this.$route.query.id }).then(res => {  
        if(res.data.goodsType === 1 || res.data.goodsType === 3 || res.data.goodsType === 2 ){// 在赋值之前用来初始化specificationName的值（规格 + 颜色）
          res.data.goodsItems.forEach(item =>{
            item.specificationName = ''
            item.colorName = ''
          })
        }
	    if(res !== null) {
			if(res.data.imageUrl1 !== undefined && res.data.imageUrl1 !== ''){
			  res.data.imageUrl11 = res.data.imageUrl1+'?x-oss-process=image/resize,h_292,l_292'
			}
			if(res.data.imageUrl2 !== undefined && res.data.imageUrl2 !== ''){
			  res.data.imageUrl22 = res.data.imageUrl2+'?x-oss-process=image/resize,h_292,l_292'
			}
			if(res.data.imageUrl3 !== undefined && res.data.imageUrl3 !== ''){
			  res.data.imageUrl33 = res.data.imageUrl3+'?x-oss-process=image/resize,h_292,l_292'
			}
			if(res.data.imageUrl4 !== undefined && res.data.imageUrl4 !== ''){
			  res.data.imageUrl44 = res.data.imageUrl4+'?x-oss-process=image/resize,h_292,l_292'
			}
			if(res.data.imageUrl5 !== undefined && res.data.imageUrl5 !== ''){
			  res.data.imageUrl55 = res.data.imageUrl5+'?x-oss-process=image/resize,h_292,l_292'
			}
			if(res.data.imageUrl6 !== undefined && res.data.imageUrl6 !== ''){
			  res.data.imageUrl66 = res.data.imageUrl6+'?x-oss-process=image/resize,h_292,l_292'
			}
	    }
		this.responseData = res
		if(this.responseData !== null) {
			this.compile = true
		}
		setTimeout(()=>{
			this.loading = false;
		},1000)
      })
    },
    isEventBus(){
        eventBus.$on('saveOn', payLoad => { // 货品上架同时商品也上架情况先保存商品信息
          if(this.responseData.data.needSale === 1){
            this.handleSubmit(2)
          }else{
            eventBus.$emit('handleShelveOn')
          }
        })
        eventBus.$on('handleShelveOff', payLoad => { // 货品下架同时商品也下架情况
          if(this.responseData.data.needSale === 2){
            this.responseData.data.needSale = 1
            this.$refs.default.formData.needSale = 1
          }
        })
        eventBus.$on('handleShelveSuccess',payLoad => { // 货品下架同时商品也下架情况更新商品上下架状态
          if(this.responseData.data.needSale === 1){
            this.responseData.data.needSale = 2
            this.$refs.default.formData.needSale = 2
          }
        })
        eventBus.$on('refreshData',payLoad =>{ // 工厂同步成功刷新数据
          this.refreshData()
        })
    },
    cancelBC(b){
      this.$refs.extend.cancelBC(b)
    },

    goReturn() { // 返回商品列表
      this.$router.go(-1)
    },

    cancel(){ // 返回操作
      this.$router.go(-1)
    },

    handleSubmit(type) { // 点击保存 -> 提交新增商品数据&POST
      // 触发通用信息、商品图片组件的必填项
      this.loading = true
      this.$refs.default.handleSubmit()
      // this.$refs.uploadImg.handleSubmit()
      // <!-- 通用信息，子组件传上来的值 -->
      this.defaultData = this.$refs.default.formData;
      this.defaultData.distributorScope = this.defaultData.distributorScope ? this.defaultData.distributorScope : 0
      this.defaultData.distributorScopeNo = this.defaultData.distributorScopeNo ? this.defaultData.distributorScopeNo : 0
      
      this.defaultData.id = this.$route.query.id
      this.defaultData.classifyIds = this.defaultData.classifyIds
      this.defaultData.productlineIds = this.$refs.default.formData.productlineId
      this.tempData = []
      this.extendData = []
      this.tempDatas = []
      // 通用信息，仓库ids（warehouseIds）
      if(this.$refs.default.warehouseIds.length === undefined || this.$refs.default.warehouseIds === 0 ){
        this.$message.error("至少选择一个仓库！")
        this.loading = false
        return
      }
      this.defaultData.warehouseIds = this.$refs.default.warehouseIds.join(',')
      if (this.defaultData.distributorScope === 0 || this.defaultData.distributorScope === 1){
        this.defaultData.scalePriceIds = []
        this.defaultData.distributorIds = []
        this.defaultData.departmentIds = []
        this.defaultData.adminIds = []
        this.defaultData.distributorGroupIds = []
      }
      if(this.defaultData.distributorScope === 2 && (this.defaultData.scalePriceIds === undefined || this.defaultData.scalePriceIds === '' || this.defaultData.scalePriceIds.length === 0)){
        this.$message.error("请先指定分销商等级范围")
        this.loading = false
        return
      } else if (this.defaultData.distributorScope === 2 && this.defaultData.scalePriceIds.length > 0) {
        this.defaultData.distributorIds = []
        this.defaultData.departmentIds = []
        this.defaultData.adminIds = []
        this.defaultData.distributorGroupIds = []
      }
      if(this.defaultData.distributorScope === 3 && (this.defaultData.distributorIds === undefined || this.defaultData.distributorIds === '' || this.defaultData.distributorIds.length === 0)){
        this.$message.error("请先指定分销商范围")
        this.loading = false
        return
      } else if (this.defaultData.distributorScope === 3 && this.defaultData.distributorIds.length > 0) {
        let disArr = []
        this.defaultData.distributorIds.forEach(item => {
          if (!item.id) {
            disArr.push(item)
          } else {
            disArr.push(item.id)
          }
        })
        this.defaultData.distributorIds = disArr
        this.defaultData.scalePriceIds = []
        this.defaultData.departmentIds = []
        this.defaultData.adminIds = []
        this.defaultData.distributorGroupIds = []
      }
      
      if(this.defaultData.distributorScope === 4 && (this.defaultData.departmentIds === undefined || this.defaultData.departmentIds === '' || this.defaultData.departmentIds.length === 0)){
        this.$message.error("请先指定销售部门范围")
        this.loading = false
        return
      } else if (this.defaultData.distributorScope === 4 && this.defaultData.departmentIds.length > 0) {
        this.defaultData.scalePriceIds = []
        this.defaultData.adminIds = []
        this.defaultData.distributorIds = []
        this.defaultData.distributorGroupIds = []
      }
      if(this.defaultData.distributorScope === 5 && (this.defaultData.adminIds === undefined || this.defaultData.adminIds === '' || this.defaultData.adminIds.length === 0)){
        this.$message.error("请先指定业务员范围")
        this.loading = false
        return
      } else if (this.defaultData.distributorScope === 5 && this.defaultData.adminIds.length > 0) {
        this.defaultData.scalePriceIds = []
        this.defaultData.departmentIds = []
        this.defaultData.distributorIds = []
        this.defaultData.distributorGroupIds = []
      }
       if(this.defaultData.distributorScope === 6 && (this.defaultData.distributorGroupIds === undefined || this.defaultData.distributorGroupIds === '' || this.defaultData.distributorGroupIds.length === 0)){
        this.$message.error("请先指定分销商分组范围")
        this.loading = false
        return
      } else if (this.defaultData.distributorScope === 6 && this.defaultData.distributorGroupIds.length > 0) {
        let groupArr = []
        this.defaultData.distributorGroupIds.forEach(item => {
          groupArr.push(item.id)
        })
        this.defaultData.distributorGroupIds = groupArr
        this.defaultData.scalePriceIds = []
        this.defaultData.distributorIds = []
        this.defaultData.departmentIds = []
        this.defaultData.adminIds = []
      }
      this.defaultData.goodsType = this.$refs.extend.goodType
      if(this.defaultData.goodsType === 2 || this.defaultData.goodsType=== 3){
        this.defaultData.diyType = this.$refs.extend.diyType // 定制类型
      } else {
        this.defaultData.diyType = 0; // 普通商品
      }
      if(this.defaultData.goodsType=== 3){// 定制材料类型
        this.defaultData.materialType = this.$refs.extend.materialType
      }
      
      
      if(this.defaultData.goodsType === 1 || this.defaultData.goodsType === 2){ // 普通商品 新定制商品
        this.tempData = this.tempData.concat(this.$refs.extend.tableData); // 从子组件中抓取扩展数据 <!-- 扩展数据，子组件传上来的值 --> === 临时数组
        this.extendData = this.extendData.concat(this.$refs.extend.extendData); // 从子组件中抓取扩展数据 <!-- 扩展数据，子组件传上来的值 -->
        for(var i = 0; i< this.tempData.length; i++) {
          let isExit = false
          for(var j = 0; j < this.extendData.length; j++) {
            if(this.extendData[j].itemErpId == this.tempData[i].itemErpId) {
              this.tempData[i].id = this.extendData[j].id
              this.extendData[j] = this.tempData[i]
              isExit = true
              break
            }
          }
          if(!isExit) {
            this.extendData.push(this.tempData[i])
          }
        }
        if(this.extendData != undefined && this.extendData.length>0){
          let b = false
          for(let i = 0;i<this.extendData.length;i++){
            if(this.extendData[i].operationType != 3 && (this.extendData[i].weight === undefined || this.extendData[i].weight=== 0 || this.extendData[i].weight=== '0' || this.extendData[i].weight=== '')){
              this.$message.error("请先确保货品重量不能为零或空！")
              this.loading = false
              return
            }
            if(this.extendData[i].operationType != 3){
              b = true
              break
            }
          }

          if(this.defaultData.goodsType === 1) { //..MOQ不能为空
            for(let i = 0; i<this.extendData.length; i++) {
              if(this.extendData[i].operationType != 3 && this.extendData[i].advanceSaleFlag == 1 && (this.extendData[i].moq == undefined || this.extendData[i].moq == '')) {
                this.$message.error('支持预售的商品，MOQ不能为空');
                this.loading = false;
                return
              }
            }
          }

          if(!b){
            this.$message.error("至少添加一个货品！")
            this.loading = false
            return
          }
          if(this.defaultData.goodsType=== 3){// 新定制材料类型
            for(let i = 0;i<this.extendData.length;i++){
              if(this.extendData[i].operationType != 3 && (this.extendData[i].materialId === undefined || this.extendData[i].materialId === null || this.extendData[i].materialId === '')){
                this.$message.error('货品'+this.extendData[i].itemCode+'材质不能为空')
                this.loading = false
                return
              }
            }
          }
          

        }else{
          this.$message.error("至少添加一个货品！")
          this.loading = false
          return
        }
      }else { // 定制商品图片判断
        this.$refs.extend.tableDatas.forEach(item =>{
          let obj = {
            itemGroup:item.itemGroup,
            itemGroupRemark:item.itemGroupRemark,
            groupSort:item.groupSort,
            itemId:item.itemId,
            items:item.items.concat(),
            userItems:item.userItems
          }
          this.tempDatas.push(obj)
        })
        this.extendData = this.extendData.concat(this.$refs.extend.extendData);
        this.extendData.forEach(items =>{
          items.items.forEach(item =>{
            if(item.operationType === 2){
              let b = false
              for(let i = 0;i<this.tempDatas.length;i++){
                if(items.itemId == this.tempDatas[i].itemId){
                  this.tempDatas[i].items.push(item)
                  b = true
                  break
                }
              }
              if(!b){
                let obj = {
                  itemGroup:items.itemGroup,
                  itemGroupRemark:items.itemGroupRemark,
                  groupSort:items.groupSort,
                  itemId:items.itemId,
                  items:[],
                  userItems:items.userItems
                }
                obj.items.push(item)
                this.tempDatas.push(obj)
              }
            }
          })
        })
        let b = true
        this.extendData = []
        if(this.tempDatas.length === 0){
          this.$message.error("定制商品至少添加一个分组！")
          b = false
          this.loading = false
          return
        }else{
          for(let i = 0;i<this.tempDatas.length;i++){
            if(this.tempDatas[i].itemGroup === undefined || this.tempDatas[i].itemGroup === null || this.tempDatas[i].itemGroup.trim() === ''){
              this.$message.error("定制商品分组名称不能为空！")
              b = false
              this.loading = false
              return
            }
          }
        }
        this.tempDatas.forEach(items =>{ // 判断条件是否满足
          let isExit = false
          if(items.itemGroup !== '' && items.items.length === 0){
            this.$message.error("分组至少添加一个货品！")
            b = false
            this.loading = false
            return
          }
          items.items.forEach(item =>{
            if(item.operationType != 3){
              isExit = true
            }
            if(item.operationType != 3 && (item.weight === undefined || item.weight=== 0 || item.weight=== '0' || item.weight=== '')){
              this.$message.error("请先确保所有货品重量不能为零或空！")
              b = false
              this.loading = false
              return
            }
            if(item.operationType != 3 && item.specificationValueId === undefined || item.specificationValueId === ''){
              this.$message.error("请先确保所有货品规格不能为空！")
              b = false
              this.loading = false
              return
            }
            if(item.operationType != 3 && item.imageUrl === undefined || item.imageUrl === null || item.imageUrl.trim() === ''){
              this.$message.error("请先确保定制货品对应的图片不能为空！")
              b = false
              this.loading = false
              return
            }
            if(item.operationType != 3 && item.barCode === undefined || item.barCode === ''){
              this.$message.error("请先确保定制货品69码不能为空！")
              b = false
              this.loading = false
              return
            }
            item.groupName = items.itemGroup
            item.groupRemark = items.itemGroupRemark
            item.groupSort = items.groupSort
            this.extendData.push(item)
          })
          if(!isExit){
            this.$message.error("分组至少添加一个货品！")
            this.loading = false
            return
          }
        })
        if(!b){//定制商品条件不满足
          this.loading = false
          return
        }
      }
      this.imgUrl = []
      this.imgUrl = this.$refs.uploadImg.url.concat(Object.keys(this.$refs.uploadImg.listObj).map(v => this.$refs.uploadImg.listObj[v])) // 商品图片的URL
      if(this.imgUrl == undefined || this.imgUrl.length === 0){
        this.$message.error("商品图片不能为空！")
        this.loading = false
        return
      }
      this.contentUrl = this.$refs.detail.content; // 详细信息组件的值
      this.contentUrlEn = this.$refs.detail.contentEn; // 详细信息组件的值 - 英文
      this.partGroups = this.$refs.parts.partGroups; // 配件
      this.relationGoodsIds = this.$refs.relevance.relationGoodsIds; // 关联商品
      this.defaultData.goodsItems = this.extendData; // 把扩展数据写入post的body
      this.defaultData.specificationId = this.$refs.extend.specificationId;
      this.defaultData.contentUrl = this.contentUrl;
      this.defaultData.contentUrlEn = this.contentUrlEn; // 详细信息组件的值 - 英文
      this.defaultData.goodsType = this.$refs.extend.goodType; // 商品类型
      this.defaultData.diyType = this.$refs.extend.diyType // 定制类型
      this.defaultData.materialType = this.$refs.extend.materialType // 材质类型
      this.defaultData.partGroups = this.partGroups;
      this.defaultData.relationGoodsIds = this.relationGoodsIds;
      this.imgUrl.forEach((item, index) => {
        let n = 'imageUrl' + (index + 1)
        this.defaultData[n] = item.url1;
      })
      // 英文图片处理
      let imgUrlEn = [];
      imgUrlEn = this.$refs.uploadImg.urlEn.concat(Object.keys(this.$refs.uploadImg.listObjEn).map(v => this.$refs.uploadImg.listObjEn[v]));
      imgUrlEn.forEach((item, index) => {
        let n = 'imageUrl' + (index + 1) + 'En';
        this.defaultData[n] = item.url1;
      })
      if(this.$refs.attribute.labels !== undefined && this.$refs.attribute.labels.length>0){// 商品标签
        this.defaultData.tagIds = []
        this.$refs.attribute.labels.forEach((item, index) =>{
          this.defaultData.tagIds.push(item.id)
        })
      }
      if(this.$refs.attribute.params !== undefined && this.$refs.attribute.params.length>0){// 商品参数
        this.defaultData.paramIds = []
        this.$refs.attribute.params.forEach((item, index) =>{
          this.defaultData.paramIds.push(item.valueId)
        })
      }
      if(this.compile == true) {  // 判断如果是查看按钮进来的使用修改接口
        let hintStr = '保存成功'
        if(this.needCheckFlag === 1){
          if(this.defaultData.needSale === 2){
            hintStr = '修改成功，上架需审批通过后方可生效'
          }
        }
        if(type == 2){
          hintStr = "商品保存成功，货品准备上架"
        }
        this.$http.editGoods(this, this.defaultData).then(res => {
          this.loading = false
          if(res.success) {
            this.$message.success({
              message: hintStr,
              duration: 3 * 1000,
              onClose: () => {}
            })
            if(type === 1){
              this.$router.go(-1)
            }else if(type == 2){
              eventBus.$emit('handleShelveOn')
            }
          } else {
           this.defaultData.classifyIds = this.defaultData.classifyIds
           if(this.defaultData.productlineIds == '') {
              this.defaultData.productlineIds = [];
            } else {
              this.defaultData.productlineIds = !isString(this.defaultData.productlineIds) ? this.defaultData.productlineIds : this.defaultData.productlineIds.split(',')
            }
            this.defaultData.warehouseIds = !isString(this.defaultData.warehouseIds) ? this.defaultData.warehouseIds : this.defaultData.warehouseIds.split(',')
            this.defaultData.scalePriceIds = isString(this.defaultData.scalePriceIds) ? this.defaultData.scalePriceIds : this.defaultData.scalePriceIds.join(',')
            this.defaultData.distributorIds = !isString(this.defaultData.distributorIds) ? this.defaultData.distributorIds : this.defaultData.distributorIds.split(',')
            this.defaultData.departmentIds = !isString(this.defaultData.departmentIds) ? this.defaultData.departmentIds : this.defaultData.departmentIds.split(',')
            this.defaultData.adminIds = !isString(this.defaultData.adminIds) ? this.defaultData.adminIds : this.defaultData.adminIds.split(',')
          }
        })
      }else {
        let hintStr = '添加成功'
        if(this.needCheckFlag === 1){
          if(this.defaultData.needSale === 2){
            hintStr = '添加成功，上架需审批通过后方可生效'
          }
        }
        // addGoods
        this.$http.addGoods(this, this.defaultData).then(res => {
          this.loading = false
          if(res.success) {
            this.$message.success({
              message: hintStr,
              duration: 3 * 1000,
              onClose: () => { }
            })
            this.$router.go(-1)
            // this.$router.push({ name: 'goodslist'})
          } else {
            this.loading = false
            // this.defaultData.classifyIds = !isString(this.defaultData.classifyIds) ? this.defaultData.classifyIds : this.defaultData.classifyIds.split(',')
             this.defaultData.classifyIds = this.defaultData.classifyIds
            
            if(this.defaultData.productlineIds == '') {
              this.defaultData.productlineIds = [];
            } else {
              this.defaultData.productlineIds = !isString(this.defaultData.productlineIds) ? this.defaultData.productlineIds : this.defaultData.productlineIds.split(',')
            }
            this.defaultData.warehouseIds = !isString(this.defaultData.warehouseIds) ? this.defaultData.warehouseIds : this.defaultData.warehouseIds.split(',')
            this.defaultData.scalePriceIds = !isString(this.defaultData.scalePriceIds) ? this.defaultData.scalePriceIds : this.defaultData.scalePriceIds.join(',')
            this.defaultData.distributorIds = !isString(this.defaultData.distributorIds) ? this.defaultData.distributorIds : this.defaultData.distributorIds.split(',')
            this.defaultData.departmentIds = !isString(this.defaultData.departmentIds) ? this.defaultData.departmentIds : this.defaultData.departmentIds.split(',')
            this.defaultData.adminIds = !isString(this.defaultData.adminIds) ? this.defaultData.adminIds : this.defaultData.adminIds.split(',')
          }
        })
      }
    }
  }
}

</script>
<style rel="stylesheet/scss" lang="scss">
.add-goods {
  background-color: white;
  min-height: 100%;
  padding-bottom: 30px;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  }
  .nav {
    width: 100%;
  }
  .radio {
    white-space: nowrap;
    position: fixed;
    .el-tabs__nav-scroll{
      position: relative;
    }
  }
  .btn-home{
    text-align: center;
    float: right;
    padding:5px;
    margin-top:7px;
    margin-right:8px;
  }

  .el-radio-button__inner {
    border: 0;
  }
  .btn {
    float: right;
    margin-right: 5px;
    margin-top: 5px;
    color: $lakeBlue;
    border: 1px solid #fff;
  }
  .btn:hover{
    background-color: #fff;
    color: $lakeBlue;
    border: 1px solid $lakeBlue;
  }
  .btn:active {
    border: #fff;
    color: $lakeBlue;
    border: 1px solid #fff;
  }
  .footbtn {
    margin-top: 30px;
  }
}

</style>
