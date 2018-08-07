<template>
  <div class="add-coupon-wrap">
    <header v-if="Number(checkMsg) < 4">
      <h4 class="header_h4" v-if="Number(checkMsg) === 1">添加优惠券</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 2">查看优惠券</h4>
      <h4 class="header_h4" v-if="Number(checkMsg) === 3">编辑优惠券</h4>
      <el-button
        class="mini-back-btn btn-home"
        icon="el-icon-d-arrow-left"
        @click="handleBack"
      >返回优惠券列表</el-button>
    </header>
    <approve v-if="Number(checkMsg) === 4" :approveData="approveData"></approve>
    <div class="content">
      <el-form
        :model="formData"
        :rules="rules"
        label-width="20%"
        label-position="right"
        ref="ruleForm"
        :disabled="isDisabled"
      >
        <el-row>
          <el-col :span="18">
            <div class="tip-left">
              <p>
                <code>基本信息</code>
              </p>
            </div>
            <el-form-item label="优惠券名称：" prop="name">
              <el-input v-model="formData.name" placeholder="最多展示100个字" maxlength="100" />
            </el-form-item>
            <el-form-item label="描述：">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="仅内部可见，不展示到前台"
                v-model="formData.couponDesc"
              />
            </el-form-item>
            <el-form-item label="使用说明：" prop="couponExplain">
              <el-input
                type="textarea"
                :rows="6"
                placeholder="内容前台可见，最多输入200文字"
                maxlength="200"
                v-model="formData.couponExplain"
              />
            </el-form-item>
            <el-form-item label="生成数量：" prop="generateCount">
              <el-input placeholder="请输入优惠券发放数量，默认为无限制" v-model="formData.generateCount" />
            </el-form-item>
            <el-form-item label="限领数量：" prop="limitCount">
              <el-input placeholder="请输入单个用户限领的数量上限，默认为不限制" v-model="formData.limitCount" />
            </el-form-item>
            <el-form-item label="有效时间：" prop="validTime">
              <el-date-picker
                v-model="formData.validTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :picker-options="expireTimeOption"
                @input="handleDate"
              ></el-date-picker>
            </el-form-item>

            <div class="tip-left">
              <p>
                <code>优惠规则</code>
              </p>
            </div>
            <el-form-item class="couponTypeRadio" label="优惠类型：" prop="couponType">
              <el-radio-group v-model="formData.couponType">
                <el-radio :label="1">普通</el-radio>
                <el-radio :label="4">新人</el-radio>
                <el-radio :label="2">Note 20专题</el-radio>
                <el-radio :label="3">S20 FE专题</el-radio>
                <el-radio :label="5">哆啦A梦兑换专题</el-radio>
                <el-radio :label="6">个性定制专题</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="优惠券编号：" prop="couponCode">
              <el-input v-model="formData.couponCode" placeholder="最多展示32个字" maxlength="100" />
            </el-form-item>
            <el-form-item label="领券方式：" prop="receivedType">
              <el-radio-group v-model="formData.receivedType" :disabled="isEdit">
                <el-radio :label="1">自主领取</el-radio>
                <el-radio :label="2">人工发放</el-radio>
                <el-radio :label="3">自动发放</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="兑换形式：" prop="couponMethod">
              <el-radio-group v-model="formData.couponMethod" @change="clearValidate">
                <el-radio :label="1">满减</el-radio>
                <el-radio :label="2">满折</el-radio>
                <el-radio :label="3">兑换</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item
              class="pl-con"
              label="减免金额"
              prop="reduction"
              ref="reduction"
              v-show="formData.couponMethod === 1"
            >
              <el-input style="width: 200px;" size="mini" placeholder v-model="formData.reduction" />
            </el-form-item>
            <el-form-item
              class="pl-con"
              label="减免折扣"
              prop="discount"
              ref="discount"
              v-show="formData.couponMethod === 2"
            >
              <el-input style="width: 200px;" size="mini" placeholder="示例：5折，请输入50" v-model="formData.discount" />&nbsp;%
            </el-form-item>
            <el-form-item label="是否启用物流收费" prop="deliveryFeeFlag" v-show="formData.couponMethod===3">
              <el-radio-group v-model="formData.deliveryFeeFlag" :disabled="isEdit" @change="changeLogistics">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="物流费金额：" prop="deliveryFee" v-if="formData.couponMethod===3 &&　formData.deliveryFeeFlag===1">
              <el-input style="width: 200px;" size="mini" onkeyup="value=value.replace(/[^\d]/g,'')"  placeholder="请输入数字" v-model="formData.deliveryFee" >元</el-input>
            </el-form-item>
            <el-form-item label="使用条件：" prop="orderMoney" v-show="!(formData.couponMethod===3&&formData.deliveryFeeFlag===1)">
              <p class="order-amount">
                <span class="title">订单满额</span>
                <el-input style="width: 200px;" size="mini"  placeholder v-model="formData.orderMoney" >元</el-input>
              </p>
            </el-form-item>
             <el-form-item label="适用材质：" style="margin-bottom: 0px;">
              <el-radio-group v-model="formData.materialScope">
                <el-radio :label="1">全部材质可用</el-radio>
                <el-radio :label="2">指定材质可用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-if="formData.materialScope === 2">
              <el-table :data="materialTable" border header-row-class-name="header-row" class="tableCenter goods-table"
                 max-height="400" ref="multipleSelectMaterial" @select="selectMaterial" @select-all="selectMaterialAll" 
                @selection-change="handleSelecMaterialChange">
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column align="center" label="材质编码" prop="materialNo"></el-table-column>
                <el-table-column align="center" label="材质名称">
                  <template slot-scope="scope">
                    <div>{{scope.row.parentName}}-{{scope.row.name}}</div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus"> </el-table-column>
              </el-table>
              <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
            </el-form-item>
            <el-form-item label="适用型号：" prop="modelScope" style="margin-bottom: 0px;">
              <el-radio-group v-model="formData.modelScope" size="mini">
                <el-radio :label="1" >全部型号可用</el-radio>
                <el-radio :label="2" >指定型号可用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-show="formData.modelScope === 2">
              <el-select size="mini" class="modelSelect" v-model="category" placeholder="请选择" clearable>
                <el-option
                v-for="item in categoryTypes"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                </el-option>
              </el-select>
              <el-input size="mini" clearable  @focus="categoryFocus" @blur="focus=false"  placeholder="请选择型号分类筛选" class="category-input" v-model="searchModel.parentName"></el-input>
              <transition name="el-zoom-in-top">
                <div v-clickoutside="handleClose" class="category-box" v-if="categoryShow">
                  <el-tree @node-click="categorySelect" lazy :props='props' :load="getChildren" ref="tree" node-key="id"></el-tree>
                </div>
              </transition>
              <el-table :data="modelList" tooltip-effect="dark" ref="multipleSelectMode" @select="select" @select-all="selectAll"  
              @selection-change="handleSelectionChange" border header-row-class-name="header-row" class="tableCenter goods-table" max-height="400">
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column align="center" label="型号编码" prop="id"> </el-table-column>
                <el-table-column align="center" label="型号名称" prop="name"> </el-table-column>
                <el-table-column align="center" label="型号类型" prop="categoryName"> </el-table-column>
                <el-table-column align="center" label="状态" prop="openFlag" :formatter="formatStatus" show-overflow-tooltip></el-table-column>
              </el-table>
              <page :page="searchModel.page" :total="mtotal" @sizeChange="sizeModelChange" @currentChange="currentModelChange"></page>
            </el-form-item>
            <distributor ref="distributor" :distributorType="formData.couponScope" :distributors="distributors"  @change="getChange" :disabled="false"></distributor>
            <el-form-item
              class="use-scope"
              label="作废说明："
              v-if="(Number(checkMsg) === 2) && formData.invalidExplain">
              <p class="invalid-explain">{{formData.invalidExplain}}</p>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="clearfix footbtn" v-if="Number(checkMsg) < 4">
        <div>
          <el-button
            class="mini-search-btn box-btn"
            @click="handleSave('ruleForm')"
            :loading="loading"
            v-if="!isDisabled"
          >保存提交</el-button>
          <el-button size="mini" @click="handleBack">返回</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import distributor from '@/views/marketingPromotion/compomemts/distributor/distributorCoupon'
import page from "@/components/pagination"
import approve from '@/views/marketingPromotion/compomemts/approve'
export default {
  name: "addCoupon",
  components: { distributor, page, approve},
  directives:{ //..事件绑定
		clickoutside:{
			bind:function(el,binding,vnode){
				function documentHandler(e){
				if(el.contains(e.target)){ //..这里判断点击的元素是否是本身，是本身，则返回
					return false;
				}
				if(binding.expression){ //..判断指令中是否绑定了函数
					binding.value(e) //..如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
				}
				}
				el._vueClickOutside_ = documentHandler; //..给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
				document.addEventListener('click',documentHandler);
			},
			unbind:function(el,binding){
				document.removeEventListener('click',el._vueClickOutside_);
				delete el._vueClickOutside_;
			}
		}
	},
  data() {
    var validateReduction = (rule, value, callback) => {
      if (this.formData.couponMethod === 1 && value === "") {
        callback(new Error("请输入减免金额"));
      } else {
        callback();
      }
    };
    var validateDiscount = (rule, value, callback) => {
      if (this.formData.couponMethod === 2 && value === "") {
        callback(new Error("请输入减免折扣"));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      id: this.$route.query.id,
      checkMsg: this.$route.query.checkMsg,
      isEdit: false,
      formData: {
        name: "",
        couponDesc: "",
        couponExplain: "",
        generateCount: "",
        limitCount: "",
        validTime: [],
        couponType: 1,
        receivedType: 3,
        couponMethod: 1,
        reduction: "",
        discount: "",
        orderMoney: "",
        ruleScope: 1,
        modelScope: 1,
        couponScope: 1,
        materialScope: 1,
        distributors: [],
        models: [],
        materials: [],
        deliveryFeeFlag: 0
      },
      rules: {
        name: [
          { required: true, message: "请输入优惠券名称", trigger: "blur" },
          { max: 100, message: "最多展示100个字", trigger: "blur" },
        ],
        couponExplain: [
          { required: true, message: "请输入使用说明", trigger: "blur" },
          { max: 200, message: "最多输入200个字", trigger: "blur" },
        ],
        validTime: [
          { required: true, message: "请选择有效时间", trigger: "change" },
        ],
        receivedType: [
          { required: true, message: "请选择领券方式", trigger: "change" },
        ],
        couponMethod: [
          { required: true, message: "请选择兑换方式", trigger: "change" },
        ],
        reduction: [
          { required: true, validator: validateReduction, trigger: "blur" },
        ],
        discount: [
          { required: true, validator: validateDiscount, trigger: "blur" },
        ],
        orderMoney: [
          { required: true, message: "请输入使用条件", trigger: "blur" },
        ],
        deliveryFee: [
          { required: true, message: "请输入物流费金额", trigger: "blur" },
        ],
      },
      isDisabled: false, // 是否可编辑
      expireTimeOption: {
        // 限制可选日期
        disabledDate(date) {
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
      },
      pageInfo: {
        page: 1,
        size: 10
      },
      total:0,
      distributors:[],
      materialTable: [], // 材质列表
      materialIds: [], // 选中材质列表
      focus:false,
			categoryShow: false,
			props: {
				label: 'name',
				children: 'childrenList',
				isLeaf: 'leaf'
			},
			addLabel:false,
      modelList:[],
			searchModel:{
        page:1,
        size:10,
				parentName:'',
				parentId:undefined,
        categoryId: undefined,
				atLastTrademark:1
			},
      mtotal: 0,
			isSelect:false,
			distributors:[],
			models:[],
      category:null,
      categoryTypes:[],
      approveData: {} // 审批数据
    }
  },
  created() {
    this.checkMsg = this.$route.query.checkMsg;
    this.id = this.$route.query.id;
    if (Number(this.checkMsg) === 2 || Number(this.checkMsg) === 3 || Number(this.checkMsg) === 4) {
      this.initCounponDetail(Number(this.id));
    }
    this.initData()
  },
  methods: {
    initData() {
      // 获取产品类型
      this.$http.productUsableList(this).then(res => {
        if (res.success) {
          this.categoryTypes = res.data
        } else {
        }
      })
      // 材质列表
      this.$http.materialLowestList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.materialTable = res.data.list
          this.total = res.data.total <= 10 ? undefined : res.data.total
          this.selectMaterialList()
        }
      })
      this.getModel() // 型号列表
    },
    selectMaterialList() {
      if (this.materialTable.length > 0) {
        this.materialIds.forEach(row1 => {
          this.materialTable.forEach(row2 => {
            if(row1.materialId === row2.id){
              this.$nextTick( ()=> {
                this.$refs.multipleSelectMaterial.toggleRowSelection(row2);
              })
            }
          })
        })
      }
    },
    selectMaterial(selection, row){ // 材质单选时调用
        this.isMaterialSelect = true
        let d = false
        for(let i = 0;i<this.materialIds.length;i++){
          if(this.materialIds[i].materialId === row.id){
            this.materialIds.splice(i,1)
            d = true
            break
          }
        }
        if(!d){
          this.materialIds.push({
              materialId: row.id,
              materialName: row.name
            })
          this.materialIds = this.setArr(this.materialIds)
        }
    },
    selectMaterialAll(selection){ // 材质全选时调用
      this.isMaterialSelect = true
      if(selection.length === 0){
        this.materialTable.forEach(row => {
          for(let i = 0;i<this.materialIds.length;i++){
            if(this.materialIds[i].materialId === row.id){
              this.materialIds.splice(i,1)
              break
            }
          }
        })
      }else{
        selection.forEach(item =>{
          this.materialIds.push({
            materialId: item.id,
            materialName: item.name
          })
        })
        this.materialIds = this.setArr(this.materialIds)
      }
    },
    handleSelecMaterialChange(val) { // 材质当切换页面时的作用
      if(val.length === 0 && this.materialIds.length != 0 && !this.isSelect){
        this.materialIds.forEach(row1 => {
          this.materialTable.forEach(row2 => {
            if(row1.materialId === row2.id){
              this.$refs.multipleSelectMaterial.toggleRowSelection(row2);
            }
          })
        });
        this.isSelect = true
      }
    },
    // 材质去重
    setArr(arr) {
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].materialId)// 不加类型 分不清 1 '1'
        if (!obj[ arr[i].materialId + type]) {
          temp.push(arr[i])
          obj[ arr[i].materialId + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
    },
    // 型号
    setArr2(arr) {
      const obj = {}
      const temp = []
      for (let i = 0; i < arr.length; i++) {
        const type = Object.prototype.toString.call(arr[i].modelId)// 不加类型 分不清 1 '1'
        if (!obj[ arr[i].modelId + type]) {
          temp.push(arr[i])
          obj[ arr[i].modelId + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
      }
      return temp
    },
    // 材质
    sizeChange(size) {
      this.pageInfo.page = 1
      this.pageInfo.size = size;
      this.getPictureList()
    },
    // 材质
    currentChange(page) {
      this.pageInfo.page = page;
      this.getPictureList()
    },
    // 更换物流收费
    changeLogistics () {
      if (this.formData.deliveryFeeFlag === 1) {
        this.formData.ruleScope = 2
        this.formData.orderMoney = 0
      } else {
        this.formData.ruleScope = 1
        this.formData.orderMoney = ''
      }
    },
    handleClose(){
			if(this.categoryShow && !this.focus){
				this.categoryShow = false
			}
		},
		formatStatus(row, col, val){
			switch(val) {
				case 0:
				return "停用";
				break;
				case 1:
				return "启用";
				break;
			}
		},
    getModel(){ // 获取型号列表数据
      this.$http.modelAllList(this, this.searchModel).then(res => {    
				if(res.success) {
					this.modelList = []
					if(res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0){
						this.modelList = res.data.list
            this.mtotal = res.data.total <= 10 ? undefined : res.data.total
						this.selectModelList()
					}
				}
			})
		},
		selectModelList(){
			if(this.modelList.length >0 && this.models.length >0 && this.$refs.multipleSelectMode !== undefined && this.$refs.multipleSelectMode !== null){
				this.models.forEach(row1 =>{
					this.modelList.forEach(row2 => {
						if(row1.modelId === row2.id){
							this.$nextTick( ()=> {
								this.$refs.multipleSelectMode.toggleRowSelection(row2);
							})
						}
					})
				})
			}
		},
		select(selection, row){ // 单选时调用
			this.isSelect = true
			let d = false
			for(let i = 0;i<this.models.length;i++){
				if(this.models[i].modelId === row.id){
					this.models.splice(i,1)
					d = true
					break
				}
			}
			if(!d){
				this.models.push({
          modelId: row.id,
          modelName: row.name
        })
				this.models = this.setArr2(this.models)
			}
    },
		selectAll(selection){ // 全选时调用
			this.isSelect = true
			if(selection.length === 0){
				this.modelList.forEach(row => {
					for(let i = 0;i<this.models.length;i++){
						if(this.models[i].modelId === row.id){
							this.models.splice(i,1)
							break
						}
					}
				})
			}else{
				selection.forEach(item =>{
					this.models.push({
            modelId: item.id,
            modelName: item.name
          })
				})
				this.models = this.setArr2(this.models)
			}
		},
		handleSelectionChange(val) { // 当切换页面时的作用
			if(val.length === 0 && this.models.length != 0 && !this.isSelect && this.modelCategory !== undefined && this.modelCategory !== null && this.modelCategory.length>0){
				this.models.forEach(row1 => {
					this.modelCategory.forEach(row2 => {
						if(row1.modelId === row2.id){
							this.$refs.multipleSelectMode.toggleRowSelection(row2);
						}
					})
				});
				this.isSelect = true
			}
		},
    getChildren(node, resolve){
      let parentId = node.data ? node.data.id : 0
      this.$http.modelPoList(this, { size:0,size:1000,parentId: parentId, atLastatLastTrademark: 0,openFlag: 1}).then(res => {
        if(res.success) {
          if(res.data.list !== undefined && res.data.list !== null && res.data.list.length > 0){
            for(let i = 0;i<res.data.list.length;i++){
              if(res.data.list[i].childrenList.length > 0){
                res.data.list[i].leaf  = false
              }else{
                res.data.list[i].leaf  = true
              }
            }
          }
          resolve(res.data.list)
        }
        this.loading = false
      })
		},
    // 型号
    sizeModelChange(size) {
      this.searchModel.page = 1
      this.searchModel.size = size;
      this.getModel()
    },
    // 型号
    currentModelChange(page) {
      this.searchModel.page = page;
      this.getModel()
    },
    categorySelect(data, checked, indeterminate) {
			this.searchModel.parentName = data.name
			this.searchModel.parentId = data.id
			if(this.categoryShow && !this.focus){
				this.categoryShow = false
			}
		},
		categoryFocus(){
			this.focus = true
			this.categoryShow = true
		},
    categoryFocus(){
			this.focus = true
			this.categoryShow = true
		},
    getChange(val) {
			this.formData.couponScope = val.distributorScope
			this.distributors = []
			this.distributors = this.distributors.concat(val.distributorData)
		},
    // 查看 - 初始化表单详情
    initCounponDetail(id) {
      // 禁止用户编辑
      if (Number(this.checkMsg) === 2 || Number(this.checkMsg) === 4) {
        this.isDisabled = true;
      }
      if (Number(this.checkMsg) === 4) {
        // 审批
        this.$http.promotionCheckDetail(this, {id: this.$route.query.id}).then(res => {
          if (res.success) {
            this.approveData = res.data
            this.getInfo(res.data.coupon)
          } else {
            this.$message.error(res.errMessage)
          }
        }).catch(err => {
           this.$message.error(err)
        })
      } else {
        // 详情
        this.$http.couponDetail(this, {id: id}).then(res => {
          if (res.success) {
            this.getInfo(res.data)
          } else {
            this.$message.error(res.errMessage)
          }
        }).catch(err => {
           this.$message.error(err)
        })
      }
    },
    getInfo (data) {
      this.formData = data;
      this.formData.generateCount = this.formData.generateCount === 0 ? '' : this.formData.generateCount
      this.formData.limitCount = this.formData.limitCount === 0 ? '' : this.formData.limitCount
      this.formData.validTime = [new Date(data.startTime).getTime(), new Date(data.endTime).getTime()];
      // 分销商
      this.distributors = []
      if(this.formData.couponScope === 3 && this.formData.distributors !== undefined &&
        this.formData.distributors !== null && this.formData.distributors !== '' && this.formData.distributors.length>0){// 指定分销商
        this.formData.distributors.forEach(item =>{
          this.distributors.push({
            id:item.distributorId,
            name:item.name,
            companyName:item.companyName
          })
        })
      }
      // 材质
      this.materialIds = []
      if (this.formData.materialScope === 2 && this.formData.materials && this.formData.materials.length>0) {
        this.formData.materials.forEach(item => {
          this.materialIds.push({
            materialId: item.materialId,
            materialName: item.materialName
          })
        })
      }
      // 型号
      this.models = []
      if(this.formData.modelScope === 2 && this.formData.models !== undefined &&
      this.formData.models !== null && this.formData.models !== '' && this.formData.models.length>0){
        this.formData.models.forEach(item =>{
          this.models.push({
            modelId:item.modelId,
            modelName:item.modelName
          })
        })
        this.selectModelList()
        this.selectMaterialList()
      }
    },
    // 返回优惠券列表
    handleBack() {
      // this.resetValidate("ruleForm");
      this.clickLeave();
    },
    // 页面跳转
    clickLeave() {
      this.$router.go(-1);
    },
    // 保存提交
    handleSave(formName) {
      this.$refs[formName].validate((valid) => {
        if (!valid) {
          return false;
        } else {
          if(this.formData.materialScope === 2){
            if((this.materialIds === undefined || this.materialIds === null || this.materialIds === '' || this.materialIds.length === 0)){
              this.$message.error("至少指定一个材质可用")
              this.loading = false
              return
            }
            this.formData.materials = []
            this.materialIds.forEach(item =>{
              this.formData.materials.push({
                materialId:item.materialId,
                materialName:item.materialName
              })
            })
          }
          if(this.formData.modelScope === 2){
            if((this.models === undefined || this.models === null || this.models === '' || this.models.length === 0)){
              this.$message.error("至少指定一个适用型号可用")
              this.loading = false
              return
            }
            this.formData.models = []
            this.models.forEach(item =>{
              this.formData.models.push({
                modelId:item.modelId,
                modelName:item.modelName,
              })
            })
          }
          if(this.formData.couponScope === 3){ // 指定分销商
						if(this.distributors === undefined || this.distributors === null || this.distributors === '' || this.distributors.length === 0){
							this.$message.error("至少指定一个分销商")
							this.loading = false
							return
						}
            this.formData.distributors = []
            this.distributors.forEach(item =>{
              this.formData.distributors.push({
                distributorId:item.id,
                name:item.name,
                companyName:item.companyName
              })
            })
					}
          this.formData.startTime = this.formData.validTime[0]
          this.formData.endTime = this.formData.validTime[1]
          this.formData.generateCount = this.formData.generateCount === '' ? 0 : this.formData.generateCount
          this.formData.limitCount = this.formData.limitCount === '' ? 0 : this.formData.limitCount
          if (Number(this.checkMsg) === 1) {
            // 新增
            this.$http.addCoupon(this, this.formData).then(res => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "优惠券新增成功",
                });
                this.clickLeave();
              }
            });
          } else if (Number(this.checkMsg) === 3) {
            // 编辑
            this.$http.editCoupon(this, this.formData).then(res => {  
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "优惠券保存成功",
                });
                this.clickLeave();
              }
            });
          }
        }
      });
    },
    // 满减/满折/兑换切换 - 清除验证提示
    clearValidate() {
      if (this.formData.couponMethod === 2) {
        this.$refs.reduction.clearValidate();
      } else if (this.formData.couponMethod === 1) {
        this.$refs.discount.clearValidate();
      } else {
        this.$refs.reduction.clearValidate();
        this.$refs.discount.clearValidate();
      }
    },
    // 重置表单验证
    resetValidate(formName) {
      this.$refs[formName].resetFields();
    },
    // 监听时间选择
    handleDate(value) {
      this.formData.validTime = value;
      this.$forceUpdate();
    },
    back() {
      this.$router.push({ name: 'checkCoupon' })
    },
  },
 
  watch: {
    "formData.couponType": {
      handler() {
        if (this.formData.couponType !== 1) {
          this.formData.receivedType = 1;
          this.isEdit = true;
        } else {
          this.isEdit = false;
        }
      },
      deep: true,
    },
    'searchModel.parentName': {
			handler() {
				if(this.searchModel.parentName === undefined || this.searchModel.parentName === null || this.searchModel.parentName === ''){
					this.searchModel.parentId = undefined
				}
				this.getModel()
			},
			deep: true
		},
    // 'formData.materialScope': {
		// 	handler() {
    //     if (this.formData.materialScope === 2) {
    //       this.$nextTick(function () {
    //         this.materialIds.forEach(row1 => {
    //           this.materialTable.forEach(row2 => {
    //             if(row1.materialId === row2.id){
    //               this.$refs.multipleSelectMaterial.toggleRowSelection(row2);
    //             }
    //           })
    //         })
    //       })
    //     }
		// 	},
		// 	deep: true
		// }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.add-coupon-wrap {
  background-color: #fff;
  position: relative;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    .header_h4 {
      margin: 0 0 0 30px;
    }
    h4 {
      margin-left: 30px;
      display: inline-block;
      font-weight: 400;
    }
    .btn-home {
      float: right;
      padding: 5px;
      margin-top: 7px;
      margin-right: 8px;
      margin-left: 0;
    }
  }
  .content {
    padding-top: 30px;
    padding-bottom:50px;
    min-width: 900px;
    .footbtn {
      padding-top: 30px;
      margin-bottom:40px;
      text-align: center;
      .box-btn + .box-btn {
        margin-left: 10px;
      }
    }
  }
  .couponTypeRadio{
    .el-radio{
      margin:5px 30px 5px 0;
    }
  }

  .tip-left {
    margin-bottom: 10px;
  }
  .pl-con {
    padding-left: 100px;
  }
  .order-amount {
    position: relative;
    margin: 0;
    padding: 0 20px 0 65px;
    span {
      position: absolute;
      right: 0;
      color: #606266;
    }
    .title {
      left: 0;
    }
  }
  .use-scope {
    .el-radio-group {
      position: relative;
      top: 5px;
    }
    p {
      line-height: 30px;
    }
  }
  .invalid-explain {
    position: relative;
    top: 5px;
    margin: 0;
    color: #606266;
  }
  .category-input {
		width: 193px;
	}
  .category-box {
		border: 1px solid #ccc;
		margin-left: 150px;
		width: 193px;
		padding: 10px;
		background-color: white;
		position: absolute;
		left: 0;
		top: 45px;
		z-index: 99;
		border-radius: 10px;
	}
  .modelSelect{
    width:150px;
    /deep/.el-input{
      width:150px;
    }
  }
}
</style>
