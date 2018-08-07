<template>
  <div class="default-info">
    <el-form label-width="150px" :model="formData" :rules="rules" ref="formData">
      <el-form-item label="所属分类" prop="classifyName">
        <el-input @focus="categoryFocus" @blur="focus=false"  readonly placeholder="请选择" class="category-input" v-model="formData.classifyName" :disabled="disableIsShow"></el-input>
        <transition name="el-zoom-in-top">
          <div v-clickoutside="handleClose" class="category-box" v-if="categoryShow">
            <el-tree @check-change="categoryChange" :data='categorylist' check-strictly show-checkbox ref="tree" node-key="id" :default-checked-keys="formData.classifyIds" :default-expanded-keys="categoryExpanded"></el-tree>
            <!-- <el-button class="mini-search-btn" id='cate-click' @click="getCheckedNodes">确定</el-button> -->
          </div>
        </transition>
      </el-form-item>

      <el-form-item label="商品名称" class="form-item" prop="goodsName">
        <el-input v-model="formData.goodsName" :disabled="disableIsShow" placeholder="不超过100个字" maxlength="100"></el-input>
      </el-form-item>

      <el-form-item label="英文名称" class="form-item" prop="goodsNameEn">
        <el-input v-model="formData.goodsNameEn" :disabled="disableIsShow" placeholder="不超过200个字" maxlength="200"></el-input>
      </el-form-item>

      <!-- <el-tooltip content="商品编号需同ERP中对应商品一致，如果不一致，无法同步ERP中商品信息" placement="right"> -->
        <el-form-item label="商品编号" class="form-item" prop="goodsNo" v-show="compile">
          <el-input v-model="formData.goodsNo" :disabled="compile"></el-input>
          <!-- <span class="place-holder">建议您不要经常修改商品编号，如果下游没有及时同步您的信息，则可能导致下单不成功</span> -->
        </el-form-item>
      <!-- </el-tooltip> -->

      <el-form-item label="商品关键词" class="form-item" prop="keywords">
      <el-tooltip content="多个关键词用半角'|'分开" placement="right">
        <el-input v-model="formData.keywords" :disabled="disableIsShow" placeholder="不超过200个字" maxlength="200"></el-input>
      </el-tooltip>
    </el-form-item>

      <el-form-item label="所属品牌" class="form-item" prop="brandId" placeholder="请先选择所属品牌">
        <el-select v-model="formData.brandId"  :disabled="disableIsShow" @change="brandChange"> <!-- @change="brandChange" -->
          <el-option v-for="item in brandlist" :key="item.id" :value="item.id" :label="item.name"></el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item v-show="false" label="品类" class="form-item">
        <el-select v-model="formData.productlineId" placeholder="请先选择品牌" :disabled="disableIsShow" clearable>
          <el-option v-for="item in productlinelist" :key="item.id" :value="item.id" :label="item.name"></el-option>
        </el-select>
      </el-form-item> -->

      <!-- <el-form-item label="规格" class="form-item" prop="specificationId">
        <el-select v-model="formData.specificationId" @change="brandChange">
          <el-option v-for="item in specificationlist" :key="item.id" :value="item.id" :label="item.name"></el-option>
        </el-select>
      </el-form-item> -->

      <el-form-item label="商品简介" class="form-item">
        <el-input type="textarea" v-model="formData.introduce" :disabled="disableIsShow" placeholder="不超过200个字" maxlength="200"></el-input>
      </el-form-item>
      <el-form-item label="英文简介" class="form-item">
        <el-input type="textarea" v-model="formData.introduceEn" :disabled="disableIsShow" placeholder="不超过200个字" maxlength="200"></el-input>
      </el-form-item>

      <!-- <el-form-item label="是否支持预售" v-if="overSoldShow" class="form-item">
        <el-tooltip content="若支持预售，则该商品允许下MTO订单" placement="top" effect="light" >
          <span class="el-icon-question good_icon"></span>
        </el-tooltip>
        <el-radio-group v-model="formData.advanceSaleFlag">
          <el-radio :label="1" :disabled="disableIsShow"> 是 </el-radio>
          <el-radio :label="0" :disabled="disableIsShow"> 否 </el-radio>
        </el-radio-group>
      </el-form-item> -->

      <el-form-item label="仓库" v-show="false" class="form-item" prop="warehouseIds">
        <!-- <el-checkbox :indeterminate="isIndeterminate" v-model="checkall" @change="handleCheckAllChange">全选</el-checkbox> -->
        <el-checkbox label="全选" v-model="checkall" :indeterminate="isIndeterminate" @change="handleCheckAll"></el-checkbox>
        <el-checkbox-group v-model="warehouseIds" @change="handleCheckBrand">
          <el-checkbox v-for="item in warehouselist" :key="item.id" :label="item.id">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- <el-form-item label="是否上架销售" class="form-item" prop="saleStatus">
        <el-radio-group v-model="formData.saleStatus">
          <el-radio :label="1" :disabled="disableIsShow"> 是 </el-radio>
          <el-radio :label="2" :disabled="disableIsShow"> 否 </el-radio>
        </el-radio-group>
      </el-form-item> -->

      <el-form-item v-show="false" label="是否包邮" class="form-item" prop="exemptionPostage">
        <el-radio-group v-model="formData.exemptionPostage">
          <el-radio :label="2" :disabled="disableIsShow"> 是 </el-radio>
          <el-radio :label="1" :disabled="disableIsShow"> 否 </el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 可视范围 -->
      <goodsDistributor 
        v-if="disShow" 
        ref="distributor" 
        :type="formData.distributorScope" 
        :gIds="formData.scalePriceIds" 
        :dIds="formData.distributorIds"
        :gdIds="formData.distributorGroupIds"
        :departmentIds="formData.departmentIds" 
        :adminIds="formData.adminIds" 
        @change="getChange"
      ></goodsDistributor>
    </el-form>
  </div>
</template>
<script type="text/javascript">
import goodsDistributor from "@/views/goods/components/goodsDistributor"
import eventBus from '@/views/order/eventBus'
export default {
  props:['updownSaleStatus','categorys','distributorGroups', 'distributors','goods','productlines','grades','warehouses','compile','productlinelist','departments','admins'],
  data() {
    return {
      checkall: false,
      isIndeterminate: false,
      disableIsShow: false,
      categoryShow: false,
      disShow: true,
      focus:false,
      isFirst:true,
      overSoldShow: true,
      categorylist: [],
      brandlist: [],
      specificationlist: [],
      warehouselist: [],
      distributorData: [],
      warehouseIds: [],
      categoryExpanded: [],
      formData: {
        classifyName: '',
        classifyIds: [],
        goodsName: '', //..商品名称
        goodsNameEn: '', //..英文名称
        goodsNo: '', //..商品编号
        keywords: '', //..商品关键词
        brandId: '', //..所属品牌
        productlineId:'',
        productlineIds: [],
        specificationId: '', //..规格
        introduce: '', //..商品简介
        introduceEn: '', //..商品英文简介
        warehouseIds: '',
        saleStatus: '', //..是否上架销售
        advanceSaleFlag : 0, //..是否支持预售
        exemptionPostage: 1, //..是否包邮
        distributorScope: 0, //..分销商可是范围 0 不指定 1 全部分销商 2 指定分销商等级 3指定分销商 4指定销售部门 5指定业务员
        distributorScopeNo: 0, //..分销商可是范围 0 不指定 1 全部分销商 2 指定分销商等级 3指定分销商 4指定销售部门 5指定业务员
        scalePriceIds: [], // 分销商等级
        departmentIds: [],
        distributorIds: [],
        adminIds: [],
        distributorGroupIds: []
      },
      rules: {
        classifyName: [{ required: true, message: '请选择分类', trigger: 'change' }],
        goodsName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        goodsNameEn: [{ required: true, message: '请输入英文名称', trigger: 'blur' }],
        goodsNo: [{ required: true, message: '请输入商品编号', trigger: 'blur' }],
        brandId: [{ required: true, message: '请选择品牌', trigger: 'change' }],
        specificationId: [{ required: true, message: '请选择规格', trigger: 'blur' }],
        // introduce: [{ required: true, message: '请输入商品简介', trigger: 'blur' }],
        // warehouseIds: [{ required: true, message: '请选择仓库', trigger: 'change' }],
      }
    }
  },
  components: { goodsDistributor },
  created() {
    eventBus.$on('goodTypeData',payLoad => { //..1 普通商品 2 定制商品
			if(payLoad.goodTypeData == 2) { //..此时隐藏是否
        this.overSoldShow = false
      }else {
        this.overSoldShow = true
      }
		})
    this.isFirst = true
    // 品牌列表
    this.$http.getBrandPoList(this, { 
      page: 1,
      size: 10000
      }).then(res => {
        if (res.success) {
          let ary = [];
          res.data.list.forEach(item => {
            if(item.openFlag) {
              ary.push(item)
            }
          })
          this.brandlist = ary
        }
    })
    // 所属分类
    this.$http.getAllClassifyPoList(this).then(res => {
      this.categorylist = res;
    })
    
    if(this.$route.query.saleStatus == 1 || this.$route.query.saleStatus == undefined) { // 默认是否上架销售
      this.formData.saleStatus = 1
    }else {
      this.formData.saleStatus = this.$route.query.saleStatus
    }

  },
  methods: {
    categoryFocus(){
      this.focus = true
      this.categoryShow = true
    },
    handleClose(){
      if(this.categoryShow && !this.focus){
        this.categoryShow = false
      }
    },
    categoryChange(data, checked, indeterminate) {
      let ary1 = [];
      let ary2 = [];
      if(checked && data.parentId !== undefined && data.parentId != null){
        this.$refs.tree.setChecked(data.parentId,true)
      }
      this.$refs.tree.getCheckedNodes().forEach(item => {
        ary1.push(item.id)
        ary2.push(item.label)
      })
      this.formData.classifyName = ary2.join(',')
      this.formData.classifyIds = ary1
    },
    handleCheckAll(val) {
      if(val){
        this.warehouseIds = [];
        this.warehouselist.forEach(item => {
          this.warehouseIds.push(item.id)
        })
      }else{
        this.warehouseIds = []
      }
      this.isIndeterminate = false
    },
    handleCheckBrand(val) {
      this.checkall = val.length === this.warehouselist.length;
      this.isIndeterminate = val.length > 0 && val.length < this.warehouselist.length
      this.warehouseIds = val ? val : []
    },
    getCheckedNodes() {
      let ary1 = [];
      let ary2 = [];
      this.$refs.tree.getCheckedNodes().forEach(item => {
        ary1.push(item.id)
        ary2.push(item.label)
      })
      this.formData.classifyName = ary2.join(',')
      this.formData.classifyIds = ary1
      // this.categoryShow = false
    },
    getChange(val) { //..可视范围选中数据
      this.formData.distributorScope = val.distributorType; //..可视范围
      this.formData.distributorScopeNo = 0; //..不可视范围
      this.formData.scalePriceIds = val.gradeIds; //..指定分销商等级
      this.formData.departmentIds = val.DepartmentIds // 指定部门
      this.formData.adminIds = val.adminIds // 指定业务员
      this.formData.distributorIds = val.distributorIds; //..指定分销商用户ID
       this.formData.distributorGroupIds = val.distributorGroupIds; //..指定分销商分组用户ID
      this.distributorData = val.distributorData;
    },
    handleSubmit(data) {
      if(this.distributorData.length > 0) {
        let ary = []
        this.distributorData.forEach(item => {
          ary.push(item.id)
        })
        this.formData.distributorIds = ary;
      }
      this.$refs['formData'].validate(valid => {
        if(valid) {
          // this.$emit('postData',this.formData)
          this.formData.distributorScope = this.$refs.distributor.formData.distributorType; //..可视范围
          this.formData.distributorScopeNo = 0; //..不可视范围
          this.formData.scalePriceIds = this.$refs.distributor.formData.gradeIds; //..指定分销商等级
          this.formData.departmentIds = this.$refs.distributor.formData.DepartmentIds // 指定部门
          this.formData.adminIds = this.$refs.distributor.formData.adminIds // 指定业务员
          this.formData.distributorIds = this.$refs.distributor.formData.distributorIds; //..指定分销商用户ID
          this.formData.distributorGroupIds = this.$refs.distributor.formData.distributorGroupIds; //..指定分销商分组
        } else {
          return false;
        }
      })
    },

    brandChange(val) { //..品牌change事件
      if(this.isFirst){
        this.isFirst = false
      }else{
        this.formData.productlineId = ''
      }
    },
  },
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
  watch:{
    updownSaleStatus(val) {
      this.updownSaleStatus = val
    },

    categorys(val){
      this.formData.classifyIds = val
      //if(this.categorylist.length === 0){
        this.categoryExpanded = []
        this.$http.getAllClassifyPoList(this).then(res => {
          this.categorylist = res;
          let ary = [];
          this.formData.classifyIds.forEach(item =>{
            for(let i=0;i<this.categorylist.length;i++){
              if(item === this.categorylist[i].id){
                ary.push(this.categorylist[i].label)
                break
              }else{
                if(this.categorylist[i].parentId === 0){
                  for(let j=0;j<this.categorylist[i].children.length;j++){
                    if(item === this.categorylist[i].children[j].id){
                      ary.push(this.categorylist[i].children[j].label)
                      this.categoryExpanded.push(this.categorylist[i].children[j].parentId)
                      break
                    }
                  }
                }
              }
            }
          })
          this.formData.classifyName = ary.join(',')
        })
      //}
	  // else{
   //      let ary = [];
   //      this.formData.classifyIds.forEach(item =>{
   //        for(let i=0;i<this.categorylist.length;i++){
   //          if(item === this.categorylist[i].id){
   //            ary.push(this.categorylist[i].label)
   //            break
   //          }else{
   //            if(this.categorylist[i].parentId === 0){
   //              for(let j=0;j<this.categorylist[i].children.length;j++){
   //                if(item === this.categorylist[i].children[j].id){
   //                  ary.push(this.categorylist[i].children[j].label)
   //                  this.categoryExpanded.push(this.categorylist[i].children[j].parentId)
   //                  break
   //                }
   //              }
   //            }
   //          }
   //        }
   //      })
   //      this.formData.classifyName = ary.join(',')
   //    }
    },
    goods(val){ //..通用信息详情
      this.formData.goodsName = val.goodsName; //..商品名称
      this.formData.goodsNo = val.goodsNo; //..商品编号
      this.formData.keywords = val.keywords; //..商品关键词
      this.formData.goodsNameEn = val.goodsNameEn; //..英文名称
      this.formData.introduce = val.introduce; //..商品简介
      this.formData.introduceEn = val.introduceEn; //..商品简介
      this.formData.brandId = parseInt(val.brandId); //..所属品牌
      this.formData.specificationId = parseInt(val.specificationId); //..规格
      this.formData.saleStatus = this.$route.query.saleStatus; //..是否上架销售
      this.formData.advanceSaleFlag  = val.advanceSaleFlag  //..是否支持预售
      this.formData.exemptionPostage = val.exemptionPostage; //..是否包邮
      this.formData.distributorScope = val.distributorScope; //..分销商可视范围
      this.formData.distributorScopeNo = 0; //..分销商不可是范围
      if (val.distributors) {
        this.formData.distributorIds = []
        val.distributors.forEach(item => {
          this.formData.distributorIds.push({
            id: item.distributorId,
            companyName: item.companyName,
            name: item.name
          })
        })
        this.formData.distributors = undefined
      }
      this.brandChange(this.formData.brandId)
      this.$refs.distributor.changeDistributorType(this.formData.distributorScope)
    },
    productlines(val){
      this.formData.productlineIds = val.map(v=>{return v.productlineId})
      if(this.formData.productlineIds.length>0){
        this.formData.productlineId = this.formData.productlineIds[0]
      }
    },
    distributors(val){
      this.formData.distributorIds = val.map(v=>{return v})
      this.$refs.distributor.changeDistributorIds(this.formData.distributorIds)
    },
    distributorGroups(val){
      this.formData.distributorGroupIds = val.map(v=>{return v})
      this.$refs.distributor.changeDistributorGroupIds(this.formData.distributorGroupIds)
    },
    grades(val){
      this.formData.scalePriceIds = val.map(v=>{return v})
      this.$refs.distributor.changeGradeIds(this.formData.scalePriceIds)
    },
    departments(val){
      this.formData.departmentIds = val.map(v=>{return v})
      this.$refs.distributor.changeDepartmentIds(this.formData.departmentIds)
    },
    admins(val){
      this.formData.adminIds = val.map(v=>{return v})
      this.$refs.distributor.changeAdminIds(this.formData.adminIds)
    },
    warehouses(val){  // 仓库数据
      this.warehouseIds = val
      this.checkall = this.warehouseIds.length >= this.warehouselist.length;
    },
  },
}

</script>
<style lang="scss" scoped>
.default-info {
  padding-top: 20px;
  .form-item {
    width: 70%;
    .el-checkbox {
      margin-left: 30px;
    }
  }
  .good_icon {
    position: absolute;
    top: 5px;
    left: -14px;
  }
  .place-holder {
    color: #ccc;
    font-size: 12px;
  }
  .category-input {
    width: 300px;
  }
  .category-box {
    border: 1px solid #ccc;
    width: 300px;
    padding: 10px;
    background-color: white;
    position: absolute;
    left: 0;
    top: 45px;
    z-index: 99;
    border-radius: 10px;
  }
}

</style>
