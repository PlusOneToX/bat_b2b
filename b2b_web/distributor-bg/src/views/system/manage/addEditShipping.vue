<template>
    <div class="manage-add" >
      <div class="pay-list">
        <header>
          <h4 v-if="!isEdit">添加配送方式</h4>
          <h4 v-else>查看配送方式</h4>
          <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancle">
            返回配送方式列表
          </el-button>
        </header>
      </div>
      <div class="addfunction" v-loading="loading2">
        <el-form label-width='150px' label-position="right" ref="tableData" :model="tableData" :rules="rules">
          <el-form-item label="配送方式名称:" prop="name">
            <el-input v-model="tableData.name" placeholder="不超过40个字" size="small" maxlength="40" />
          </el-form-item>
          <el-form-item label="ERP配送方式ID:" prop="logisticsErpId">
            <el-input v-model="tableData.logisticsErpId" placeholder="不超过40个字" maxlength="40" size="small" />
          </el-form-item>
          <el-form-item label="快递鸟快递公司编码" prop="logisticsKdnCode">
            <el-input v-model="tableData.logisticsKdnCode" placeholder="" size="small" />
          </el-form-item>
          <el-form-item label="快递公司名称" prop="logisticsKdnName">
            <el-input v-model="tableData.logisticsKdnName" placeholder="" size="small" />
          </el-form-item>
          <el-form-item v-if="useRanges.indexOf('2') > -1" label="工厂配送方式ID:" prop="logisticsFactoryId">
            <el-input v-model="tableData.logisticsFactoryId" placeholder="不超过40个字" maxlength="40" size="small" />
          </el-form-item>
          <el-form-item label="适用商品:" prop="useRange" >
            <el-checkbox-group v-model="useRanges">
              <el-checkbox v-for="range in ranges" :label="range.id" :key="range.id">{{range.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="适用工厂:" prop="materialId" v-if="useRanges.indexOf('2') > -1">
            <el-checkbox-group v-model="useManufactors">
              <el-checkbox v-for="m in manufactors" :label="m.id" :key="m.id">{{m.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <!-- <el-form-item label="适用材质:" prop="materialId" v-if="useRanges.indexOf('2') > -1">
            <el-checkbox-group v-model="userMaterials">
              <el-checkbox v-for="m in materials" :label="m.materialId" :key="m.materialId">{{m.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item> -->
          <el-form-item label="配送费用限制:">
            <span class="span-header" style="color: #606266;font-weight: 500;">最低费用
              <el-input style="margin-left: 10px;" @keyup.native="moreZero()" @blur="fillZero" v-model="tableData.minCost" placeholder="最低费用为0" size="small" class="span-heavy" type="number" step="0.1" min="0" max="100"></el-input>元</span>
          </el-form-item>
          <el-form-item label="配送费计算方式:">
            <el-radio-group v-model="tableData.billingMethod">
              <el-radio :label="1">按重量</el-radio>
              <el-radio :label="2">按体积</el-radio>
            </el-radio-group>
          </el-form-item>
          <div v-if="tableData.billingMethod === 1">
            <el-form-item label="配送重量限制:">
              <span class="span-header" style="color: #606266;font-weight: 500;">最低重量<el-input style="margin-left: 10px;" @keyup.native="moreZero()" @blur="fillZero" v-model="tableData.minWeight" placeholder="留空默认不限制" size="small" class="span-heavy" type="number" step="0.1" min="0" max="100"></el-input>公斤</span>
              <span style="color: #606266;font-weight: 500;">最高重量
                <el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="tableData.maxWeight" placeholder="留空默认不限制" size="small" class="span-heavy" type="number" step="0.1" min="0"></el-input>公斤</span>
            </el-form-item>
            <el-form-item label="重量设置:" prop="firstWeight">
              <span class="span-header" style="color: #606266;font-weight: 500;">首重重量<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="tableData.firstWeight" placeholder="不能为空" size="small" class="span-heavy" type="number" step="0.1" min="0" max="100"></el-input>公斤</span>
              <span style="color: #606266;font-weight: 500;">续重单位<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="tableData.additionalWeight" placeholder="不能为空" size="small" class="span-heavy" type="number" step="0.1" min="0"></el-input>公斤</span>
            </el-form-item>
          </div>
         <div v-else>
           <el-form-item label="配送体积限制:">
              <span class="span-header" style="color: #606266;font-weight: 500;">最低体积<el-input style="margin-left: 10px;" @keyup.native="moreZero()" @blur="fillZero" v-model="tableData.minVolume" placeholder="留空默认不限制" size="small" class="span-heavy" type="number" step="0.1" min="0" max="100"></el-input>立方米</span>
              <span style="color: #606266;font-weight: 500;">最高体积<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="tableData.maxVolume" placeholder="留空默认不限制" size="small" class="span-heavy" type="number" step="0.1" min="0"></el-input>立方米</span>
            </el-form-item>
            <el-form-item label="体积设置:" prop="firstVolume">
              <span class="span-header" style="color: #606266;font-weight: 500;">首重体积<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="tableData.firstVolume" placeholder="不能为空" size="small" class="span-heavy" type="number" step="0.1" min="0" max="100"></el-input>立方米</span>
              <span style="color: #606266;font-weight: 500;">续重体积<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="tableData.additionalVolume" placeholder="不能为空" size="small" class="span-heavy" type="number" step="0.1" min="0"></el-input>立方米</span>
            </el-form-item>
         </div>
          <el-form-item label="地区费用类型:">
            <el-radio-group v-model="tableData.appointAreaFlag">
              <el-radio :label="0">统一设置</el-radio>
              <el-radio :label="1">指定配送地区和费用</el-radio>
            </el-radio-group>
          </el-form-item>

          <div v-if="tableData.appointAreaFlag === 0">
            <el-form-item  v-if="appointDistributionArea.formulaFlag === 0" label="统一配送费用:">
              <div class="cost-item">
                <span v-if="tableData.billingMethod === 1" class="span-header" style="color: #606266;font-weight: 500;">首重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="appointDistributionArea.firstWeightCost" placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <span v-else class="span-header" style="color: #606266;font-weight: 500;">首重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="appointDistributionArea.firstVolumeCost" placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <span v-if="tableData.billingMethod === 1" style="color: #606266;font-weight: 500;">续重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="appointDistributionArea.additionalWeightCost"   placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <span v-else style="color: #606266;font-weight: 500;">续重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="appointDistributionArea.additionalVolumeCost"   placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <el-button class="mini-search-btn" @click="useFormula" style="margin-left: 10px;">使用公式</el-button>
              </div>
            </el-form-item>
            <!-- 使用公式的UI -->
            <el-form-item  v-else label="统一配送费用:">
              <div class="cost-item">
                <span class="span-header" style="color: #606266;font-weight: 500;">配送公式 <el-input v-model="appointDistributionArea.formula"  placeholder="" size="small" class="formula"></el-input></span>
                <el-button size="small" class="mini-search-btn" @click="verifyFormula(appointDistributionArea.formula, tableData.billingMethod)">验证公式</el-button>
                <el-button size="small" class="mini-search-btn" @click="cancelFormula">取消公式</el-button>
              </div>
            </el-form-item>
          </div>
        <!-- 指定配送地区和费用  == 块 == -->
        <div v-else>
          <el-form-item >
            <el-tooltip content="注意：未启用默认费用时，不在指定配送地区的顾客不能使用本配送方式下单" placement="right">
              <el-radio-group v-model="tableData.defaultFlag">
                <el-radio :label="1">启用默认费用</el-radio>
                <el-radio :label="0">取消启用默认费用</el-radio>                  
              </el-radio-group>
            </el-tooltip>
          </el-form-item>
          <div v-show="tableData.defaultFlag === 1">
            <el-form-item  v-if="defaultDistributionArea.formulaFlag === 0" label="默认配送费用:">
              <div class="cost-item">
                <span v-if="tableData.billingMethod === 1" class="span-header" style="color: #606266;font-weight: 500;">首重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="defaultDistributionArea.firstWeightCost" placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <span v-else class="span-header" style="color: #606266;font-weight: 500;">首重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="defaultDistributionArea.firstVolumeCost" placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <span v-if="tableData.billingMethod === 1" style="color: #606266;font-weight: 500;">续重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="defaultDistributionArea.additionalWeightCost"   placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <span v-else style="color: #606266;font-weight: 500;">续重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="defaultDistributionArea.additionalVolumeCost"   placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
                <el-button class="mini-search-btn" @click="aFormula" style="margin-left: 10px;">使用公式</el-button>
              </div>
            </el-form-item>

            <el-form-item  v-else label="默认配送费用:">
              <div class="cost-item">
                <span style="color: #606266;font-weight: 500;" class="span-header">配送公式 <el-input v-model="defaultDistributionArea.formula" placeholder="" size="small" class="formula"></el-input></span>
                <el-button class="mini-search-btn" @click="verifyFormula(defaultDistributionArea.formula, tableData.billingMethod)">验证公式</el-button>
                <el-button class="mini-search-btn" @click="bFormula">取消公式</el-button>
              </div>
            </el-form-item>
          </div>

          <el-form-item label="支持的配送地区:">
            <div class="designated-area" > <!-- 添加为指定地区设置运费组件 -->
                <support  v-for="(item, index) in brandlist" :billingMethod="tableData.billingMethod" :count = 'index' :total = "brandlist.length" :key="item.id" :distributionArea = "item" @deletes="deletes(index)" @changeDistributionArea="changeDistributionArea"></support>
                <div class="add-designated-area">
                  <el-button class="mini-search-btn" icon="el-icon-plus" @click="appoint">为指定区域设置运费</el-button>
                </div>
            </div>
          </el-form-item>
        </div>

        <!-- ============ -->
          <el-form-item label="排序:" style="margin-top:25px;">
              <el-input v-model="tableData.sort" placeholder="" size="small" style="width: 100px;" type="number" min="0" @keyup.native="proving">></el-input>
          </el-form-item>

          <el-form-item label="状态:" prop="">
            <el-radio-group v-model="tableData.enable">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">关闭</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="详细介绍">
            <el-input v-model="tableData.description" placeholder="不超过100个字" type="textarea" maxlength="100" :rows="5"></el-input>
          </el-form-item>
        </el-form>

        <el-form ref="formData" :model="tableData"  label-width="150px" :rules="rules">
          <!-- 可视范围 -->
          <managedistributor ref="distributor" :qId="qId" :type="disData.distributorScope" :gIds="disData.gradeIds" :dIds="disData.distributorIds" :departmentIds="disData.departmentIds" :adminIds="disData.userIds"></managedistributor>
        </el-form>

        <el-button :loading="loading" style="margin-top: 20px;margin-left:47%;" class="mini-search-btn" @click="handleSubmit('tableData')" > 保存 </el-button>
        <el-button size="mini" @click="handleCancle" style="margin-top: 20px;"> 返回 </el-button>
      </div>
    </div>
</template>

<script>
  import support from "@/views/system/manage/support";
  import verify from "@/views/system/manage/verify";
  import managedistributor from "@/views/system/components/manageDistributor"
  export default {
    name: 'expressReadct',
    props: {
      pageState: String,
    },
    components: {
      support,
      verify,
      managedistributor
    },
    data() {
      return {
        loading2: false,
        loading:false,
        disData: {
          distributorScope: 1,
          gradeIds: [],
          distributorIds: [],
          departmentIds: [],
          userIds: []
        },
        tableData: {
          name:'',
          firstWeight:'',
          firstVolume:'',
          additionalWeight:'',
          sort:0,
          logisticsErpId:'',
          logisticsKdnCode: '',
          description:'',
          logisticsCost:[],
          appointAreaFlag:0,
          billingMethod: 1,
          enable:1,
          defaultFlag:1,
          // distributorScope: 1,
          gradeIds: [],
          // distributorIds: [],
          departmentIds: [],
          userIds: [],
          materialId: '',
          useRange: '',
          minCost: 0,
          minWeight: 0,
          // maxWeight: 0,
          minVolume: 0,
          // maxVolume: 0s
        },
        brandlist:[],
        appointDistributionArea: {
          firstWeightCost:0,
          additionalWeightCost:0,
          firstVolumeCost:0,
          additionalVolumeCost:0,
          defaultFlag:0,
          formulaFlag:0,
          formula:'',
          areaList:[],
        },
        defaultDistributionArea: {
          firstWeightCost:0,
          additionalWeightCost:0,
          firstVolumeCost:0,
          additionalVolumeCost:0,
          defaultFlag:1,
          formulaFlag:0,
          formula:'',
          areaList:[]
        },
        rules: {
          name: [{
              required: true,
              message: '请输入配送方式名称',
              trigger: 'change'
          }],
          logisticsErpId: [{
              required: true,
              message: '请输入ERP配送方式ID',
              trigger: 'change'
          }],
          logisticsFactoryId: [{
              required: true,
              message: '请输入工厂配送方式ID',
              trigger: 'change'
          }],
          // useRange: [{
          //     required: true,
          //     message: '请选择适用商品',
          //     trigger: 'change'
          // }],
          // materialId: [{
          //     required: true,
          //     message: '请选择适用材质',
          //     trigger: 'change'
          // }],
          manufactors: [{
              required: true,
              message: '请选择适用工厂',
              trigger: 'change'
          }],
          logisticsKdnCode: [{
              required: true,
              message: '请输入快递鸟快递公司编码',
              trigger: 'change'
          }],
          logisticsKdnName: [{
              required: true,
              message: '请输入快递公司名称',
              trigger: 'change'
          }],
          firstWeight: [{
              required: true,
              message: '重量设置不能为空',
              trigger: 'change'
          }],
          firstVolume: [{
              required: true,
              message: '体积设置不能为空',
              trigger: 'change'
          }]
        },
        isEdit:false,
        ranges:[
          {id:'1',name:'普通商品'},
          {id:'2',name:'定制商品'}
        ],
        materials:[
          // {id:'1',name:'TPU'},
          // {id:'2',name:'玻璃壳'},
          // {id:'3',name:'热升华'}
        ],
        useRanges:[],
        // userMaterials:[],
        useManufactors:[],
        manufactors:[
		  {id:'KDS_FK',name:'壳大师(飞快)'},
          {id:'YC',name:'云创'},
          {id:'LHW',name:'联辉王'},
          {id:'MK',name:'麦客'},
          {id:'DH_OLK',name:'多鸿欧丽科'},
        ],
        distributorData: [],
        count:0,
        qId: this.$route.query.id
      }
    },
    activated () {
      // 获取材质顶级列表
      // this.$http.materialTreeList(this, {atLastTrademark: 0, openFlag:1, parentId: 0}).then(res => {
      //   if (res.success) {
      //     this.materials = res.data
      //   }
      // })
      this.$nextTick(() => {
        if(this.$route.query.type !== undefined && this.$route.query.type === 'verify'){
        }else{
          this.useRanges = ['1']
          this.useManufactors = ['KDS_FK']
          this.distribution()
        }
        
      })
    },
    methods: {
      // 使用公式
      useFormula() {
        this.appointDistributionArea.formulaFlag = 1;
      },
      // 取消公式
      cancelFormula() {
        this.appointDistributionArea.formulaFlag = 0;
      },
      // 验证公式
      verifyFormula(formula, billingMethod) {
        this.$router.push({name: 'verify',params:{formula:formula,billingMethod: billingMethod}})
      },
      aFormula() {
        this.defaultDistributionArea.formulaFlag = 1;
      },
      bFormula() {
        this.defaultDistributionArea.formulaFlag = 0;
      },
      cFormula() {
        this.dshow = true
        this.cshow = false
      },
      dFormula() {
        this.dshow = false
        this.cshow = true
      },
      appoint() {
        this.brandlist.push({
          firstWeightCost:0,
          additionalWeightCost:0,
          firstVolumeCost:0,
          additionalVolumeCost:0,
          defaultFlag:1,
          formulaFlag:0,
          formula:'',
          areaList:[],
        })
      },
      deletes(index) {
        this.brandlist.splice(index, 1);
      },
      // 详情数据distribution
      distribution() {
          let query = this.$route.query;
          this.brandlist = [];
          this.isEdit = false
          if(query.id != undefined){
            this.loading2 = true;
            this.isEdit = true
            this.$http.logisticsDetail(this, { id: query.id }).then(res =>{  
                res.data.defaultFlag = 1
                this.disData = res.data
                this.tableData = res.data
                this.disData.distributorScope = res.data.distributorScope
                console.log('distributorScope==', this.disData.distributorScope);
                // if(this.tableData.distributorScope === 1){
                //   this.count = 4
                // }else{
                //   this.count = 5
                // }
                if(this.tableData.maxWeight === 0){
                  this.tableData.maxWeight = ""
                }
                if(this.tableData.maxVolume === 0){
                  this.tableData.maxVolume = ""
                }
                if(this.tableData.useRange !== undefined && this.tableData.useRange !== ''){
                  if (this.tableData.useRange > 2) {
                    this.useRanges = ['1', '2']
                  } else {
                    this.useRanges = []
                    this.useRanges.push(this.tableData.useRange.toString())
                  }
                } else {
                  this.useRanges = []
                  this.tableData.useRange = ''
                }
                // 材质
                // if(this.tableData.materialId !== undefined && this.tableData.materialId !== null && this.tableData.materialId !== ''){
                //   this.userMaterials = this.tableData.materialId.split(',')
                // }else {
                //   this.userMaterials = []
                // }
                if(this.tableData.manufactors !== undefined && this.tableData.manufactors !== null && this.tableData.manufactors !== ''){
                  this.useManufactors = this.tableData.manufactors.split(',')
                }else {
                  this.useManufactors = []
                }
                if(this.tableData.appointAreaFlag === 0){
                  if(this.tableData.logisticsCost && this.tableData.logisticsCost.length >0){
                    this.appointDistributionArea = this.tableData.logisticsCost[0];
                  }
                }else{
                  let isB = false;
                  for(var i = 0;i<this.tableData.logisticsCost.length;i++){
                    if(this.tableData.logisticsCost[i].defaultFlag === 1){
                      this.defaultDistributionArea = this.tableData.logisticsCost[i];
                      isB = true;
                    }else{
                      this.brandlist.push(this.tableData.logisticsCost[i]);
                    }
                  }
                 
                  if(!isB && this.brandlist.length != 0){
                    this.tableData.defaultFlag = 0;
                  }else{
                    this.tableData.defaultFlag = 1;
                  }
                }
                if(this.brandlist.length === 0){
                  this.brandlist.push({ //默认一个值
                    firstWeightCost:0,
                    additionalWeightCost:0,
                    firstVolumeCost:0,
                    additionalVolumeCost:0,
                    defaultFlag:1,
                    formulaFlag:0,
                    formula: '',
                    areaList:[],
                  })
                }
                if(res.success) {
                  this.loading2 = false
                }else {
                  this.loading2 = false
                }
            })
          }else{
            this.disData = {
              distributorScope: 1,
              gradeIds: [],
              distributorIds: [],
              departmentIds: [],
              userIds: []
            }
            this.tableData = {
              name:'',
              firstWeight:'',
              logisticsErpId:'',
              logisticsKdnCode: '',
              logisticsFactoryId:'',
              additionalWeight:'',
              billingMethod: 1,
              sort:0,
              description:'',
              logisticsCost:[],
              appointAreaFlag:0,
              enable:1,
              defaultFlag:1,
              distributorScope: 1,
              gradeIds: [],
              distributorIds: [],
              departmentIds: [],
              userIds: [],
              useRange: '',
              materialId: null,
              minCost: 0,
              minWeight: 0,
              maxWeight: undefined,
              minVolume: 0,
              maxVolume: undefined
            }
            this.appointDistributionArea = {
              firstWeightCost:0,
              additionalWeightCost:0,
              firstVolumeCost:0,
              additionalVolumeCost:0,
              defaultFlag:1,
              formulaFlag:0,
              formula:'',
              areaList:[],
            }
            this.defaultDistributionArea =  {
              firstWeightCost:0,
              additionalWeightCost:0,
              firstVolumeCost:0,
              additionalVolumeCost:0,
              defaultFlag:1,
              formulaFlag:0,
              formula:'',
              areaList:[]
            }
             
            if(this.brandlist.length === 0){
              this.brandlist.push({ //默认一个值
                firstWeightCost:0,
                additionalWeightCost:0,
                firstVolumeCost:0,
                additionalVolumeCost:0,
                defaultFlag:1,
                formulaFlag:0,
                formula:'',
                areaList:[],
              })
           }
          }
      },
      // 添加配送方式
      handleSubmit(formName) {
        this.tableData.distributorScope = this.$refs.distributor.formData.distributorType
        this.tableData.gradeIds = this.$refs.distributor.formData.distributorGradeIds
        if(this.tableData.distributorScope === 2 && (this.tableData.gradeIds === undefined || this.tableData.gradeIds === '')){
          this.$message.error("请先指定分销商等级范围")
          this.loading = false
          return
        }else if(this.tableData.distributorScope === 2){
          this.tableData.distributorIds = undefined
          this.tableData.departmentIds = undefined
          this.tableData.userIds = undefined
        }
        this.tableData.departmentIds = this.$refs.distributor.formData.distributorDepartmentIds
        if(this.tableData.distributorScope === 4 && (this.tableData.departmentIds === undefined || this.tableData.departmentIds === '')){
          this.$message.error("请先指销售部门范围")
          this.loading = false
          return
        }else if(this.tableData.distributorScope === 4){
          this.tableData.gradeIds = undefined
          this.tableData.distributorIds = undefined
          this.tableData.userIds = undefined
        }
        this.tableData.userIds = this.$refs.distributor.formData.distributorAdminIds
        if(this.tableData.distributorScope === 5 && (this.tableData.userIds === undefined || this.tableData.userIds === '')){
          this.$message.error("请先指定业务员范围")
          this.loading = false
          return
        }else if(this.tableData.distributorScope === 5){
          this.tableData.gradeIds = undefined
          this.tableData.distributorIds = undefined
          this.tableData.departmentIds = undefined
        }
        this.tableData.distributorIds = this.$refs.distributor.formData.distributorIds
        if(this.tableData.distributorScope === 6 && (this.tableData.distributorIds === undefined || this.tableData.distributorIds === '')){
          this.$message.error("请先指定分销商范围")
          this.loading = false
          return
        }else if(this.tableData.distributorScope === 6){
          this.tableData.gradeIds = undefined
          this.tableData.departmentIds = undefined
          this.tableData.userIds = undefined
        }
        this.loading = true
        if(this.tableData.maxWeight !== undefined && this.tableData.maxWeight > 0 && this.tableData.maxWeight < this.tableData.minWeight){
          this.$message.error("最高重量应大于等于最低重量!")
          this.loading = false
          return
        }
        if(this.tableData.maxVolume !== undefined && this.tableData.maxVolume > 0 && this.tableData.maxVolume < this.tableData.minVolume){
          this.$message.error("最高体积应大于等于最低体积!")
          this.loading = false
          return
        }
        this.$refs[formName].validate(valid => {
          if(valid){
            if (this.tableData.useRange === '' || this.tableData.useRange === undefined) {
              this.$message.error("请选择适用商品")
              this.loading = false
              return
            } else if (Number(this.tableData.useRange) === 2 || Number(this.tableData.useRange) === 3) {
              if (this.tableData.manufactors === '' || this.tableData.manufactors === undefined) {
                this.$message.error("请选择适用工厂")
                this.loading = false
                return
              }
            }
            if(this.tableData.logisticsErpId != undefined && this.tableData.logisticsErpId.trim() === ''){
            this.tableData.logisticsErpId = undefined
            }
            this.tableData.logisticsCost = [];
           
            if(this.tableData.appointAreaFlag === 0){
              this.tableData.logisticsCost.push(this.appointDistributionArea)
            }else{
              if(this.tableData.defaultFlag === 1){
                this.tableData.logisticsCost.push(this.defaultDistributionArea)
                this.tableData.logisticsCost = this.tableData.logisticsCost.concat(this.brandlist)
              }else{
                this.tableData.logisticsCost = this.brandlist
              }
            }

            // 保存时，海外地址cityId处理，默认设置为0
            this.tableData.logisticsCost.forEach((item) => {
              if (item.areaList && item.areaList.length > 0) {
                item.areaList.forEach(area => {
                  if (area.countryId !== 37 && area.provinceId === 0) {
                    area.cityId = 0;
                  }
                })
              }
            })

            console.log('logisticsCost==', this.tableData);
            let _tableData = JSON.parse(JSON.stringify(this.tableData))
              if(!this.tableData.maxWeight){
                _tableData.maxWeight=0;
              }
              if(!this.tableData.maxVolume){
                _tableData.maxVolume=0;
              }
            if(this.tableData.id == undefined){
              this.$http.addLogistics(this, _tableData).then(res => {  
                  if(res.success) {
                      this.$message({
                          message: '保存成功',
                          type: 'success',
                          // duration: 3 * 1000,
                          onClose: () => { }
                      })
                      this.loading = false
                      this.handleCancle()
                  }else{
                    this.loading = false
                  }
              })
            }else{
              this.$http.editLogistics(this, _tableData).then(res => {  
                  if(res.success) {
                      this.$message({
                          message: '保存成功',
                          type: 'success',
                          duration: 3 * 1000,
                          onClose: () => { }
                      })
                      this.loading = false
                      this.handleCancle()
                  }else{
                    this.loading = false
                  }
              })
            }
          }else{
            this.$message.error("请检查必填项是否有填写完整！") 
            this.loading = false
          }
        })
      },
      // 返回上个页面
      handleCancle() {
        this.$router.push({ name: 'managelist'})
      },
      changeDistributionArea(distributionArea,count){//支持的地区修改
        this.brandlist[count] = distributionArea;
      },
      proving(){
        this.tableData.sort=this.tableData.sort.replace(/[^\.\d]/g,'');
        this.tableData.sort=this.tableData.sort.replace('.','');
      },
      moreZero(){//大于零
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.defaultDistributionArea.firstWeightCost) && this.defaultDistributionArea.firstWeightCost != undefined && this.defaultDistributionArea.firstWeightCost != ""){
          this.defaultDistributionArea.firstWeightCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.defaultDistributionArea.additionalWeightCost) && this.defaultDistributionArea.additionalWeightCost != undefined && this.defaultDistributionArea.additionalWeightCost != ""){
          this.defaultDistributionArea.additionalWeightCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.appointDistributionArea.firstWeightCost) && this.appointDistributionArea.firstWeightCost != undefined && this.appointDistributionArea.firstWeightCost != ""){
          this.appointDistributionArea.firstWeightCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.appointDistributionArea.additionalWeightCost) && this.appointDistributionArea.additionalWeightCost != undefined && this.appointDistributionArea.additionalWeightCost != ""){
          this.appointDistributionArea.additionalWeightCost = 0
        // 　　alert('金额输入格式不正确!');
        }

        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.defaultDistributionArea.firstVolumeCost) && this.defaultDistributionArea.firstVolumeCost != undefined && this.defaultDistributionArea.firstVolumeCost != ""){
          this.defaultDistributionArea.firstVolumeCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.defaultDistributionArea.additionalVolumeCost) && this.defaultDistributionArea.additionalVolumeCost != undefined && this.defaultDistributionArea.additionalVolumeCost != ""){
          this.defaultDistributionArea.additionalVolumeCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.appointDistributionArea.firstVolumeCost) && this.appointDistributionArea.firstVolumeCost != undefined && this.appointDistributionArea.firstVolumeCost != ""){
          this.appointDistributionArea.firstVolumeCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.appointDistributionArea.additionalVolumeCost) && this.appointDistributionArea.additionalVolumeCost != undefined && this.appointDistributionArea.additionalVolumeCost != ""){
          this.appointDistributionArea.additionalVolumeCost = 0
        // 　　alert('金额输入格式不正确!');
        }

        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.firstWeight) && this.tableData.firstWeight != undefined && this.tableData.firstWeight!= ""){
          this.$message.error("首重重量应大于0，不超过100")
          this.tableData.firstWeight = 0
        // 　　alert('金额输入格式不正确!');
        }else{
          if(this.tableData.firstWeight > 100){
            this.$message.error("首重重量应大于0，不超过100")
            this.tableData.firstWeight = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.firstVolume) && this.tableData.firstVolume != undefined && this.tableData.firstVolume!= ""){
          this.$message.error("首重体积应大于0，不超过100")
          this.tableData.firstVolume = 0
        // 　　alert('金额输入格式不正确!');
        }else{
          if(this.tableData.firstVolume > 100){
            this.$message.error("首重体积应大于0，不超过100")
            this.tableData.firstVolume = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.additionalWeight) && this.tableData.additionalWeight != undefined && this.tableData.additionalWeight!= ""){
          this.$message.error("续重单位应大于0，不超过100")
          this.tableData.additionalWeight = 0
        }else{
          if(this.tableData.additionalWeight > 100){
            this.$message.error("续重单位应大于0，不超过100")
            this.tableData.additionalWeight = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.additionalVolume) && this.tableData.additionalVolume != undefined && this.tableData.additionalVolume!= ""){
          this.$message.error("续重体积应大于0，不超过100")
          this.tableData.additionalVolume = 0
        }else{
          if(this.tableData.additionalVolume > 100){
            this.$message.error("续重体积应大于0，不超过100")
            this.tableData.additionalVolume = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.minWeight) && this.tableData.minWeight != undefined && this.tableData.minWeight!= ""){
          this.$message.error("最低重量应不小于0，不超过500000")
          this.tableData.minWeight = 0
        }else{
          if(this.tableData.minWeight > 500000){
            this.$message.error("最低重量应不小于0，不超过500000")
            this.tableData.minWeight = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.minVolume) && this.tableData.minVolume != undefined && this.tableData.minVolume!= ""){
          this.$message.error("最低体积应不小于0，不超过500000")
          this.tableData.minVolume = 0
        }else{
          if(this.tableData.minVolume > 500000){
            this.$message.error("最低体积应不小于0，不超过500000")
            this.tableData.minVolume = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.maxWeight) && this.tableData.maxWeight != undefined && this.tableData.maxWeight!= ""){
          this.$message.error("最高重量应不小于0，不超过500000")
          this.tableData.maxWeight = 0
          // alert('金额输入格式不正确!');
        }else{
          if(this.tableData.maxWeight > 500000){
            this.$message.error("最高重量应不小于0，不超过500000")
            this.tableData.maxWeight = 0
          }
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.tableData.maxVolume) && this.tableData.maxVolume != undefined && this.tableData.maxVolume!= ""){
          this.$message.error("最高体积应不小于0，不超过500000")
          this.tableData.maxVolume = 0
          // alert('金额输入格式不正确!');
        }else{
          if(this.tableData.maxVolume > 500000){
            this.$message.error("最高体积应不小于0，不超过500000")
            this.tableData.maxVolume = 0
          }
        }
      },
      //填充零
      fillZero(){
        if(!this.tableData.minCost){
            this.tableData.minCost=0
        }
        if(!this.tableData.minWeight){
          this.tableData.minWeight=0
        }
        if(!this.tableData.minVolume){
            this.tableData.minVolume=0
        }
      }
    },
    watch:{
      useRanges: {
        handler(val) {
          this.useRanges = val
          if (val.length>1) {
            this.tableData.useRange = 3
          } else if (val.length===1) {
            this.tableData.useRange = val[0]
          } else {
            this.tableData.useRange = ''
          }
        },
        deep: true
      },
      useManufactors: {
        handler() {
          this.tableData.manufactors = this.useManufactors.length>0 ? this.useManufactors.join(','):''
        },
        deep: true
      },
      // '$route'(to, from) {
      //   console.log('9999====');
      //   if (this.$route.query.id) {
      //     this.useRanges = ['1']
      //     this.useManufactors = ['YC']
      //   }
      //   this.distribution()
      // }
      //  userMaterials: {
      //   handler(val) {
      //     this.tableData.materialId = val.length>0 ? this.userMaterials.join(','):''
      //   },
      //   deep: true
      // }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
* {
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}
.main {
  background-color: #fff
}

.manage-add {
  background-color: #fff;
  padding-bottom: 30px;
  .pay-list {
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
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .addfunction {
    border-top: 0px;
    background-color: #fff;
    padding: 50px;
    width: 80%;
    .span-header {
      margin-right: 20px;
    }
    .span-heavy {
      width:140px;
      margin:0 5px 0 10px;
    }
    .cost-item {
        background-color: $bg;
        border-radius: 10px;
        padding: 20px 20px;
      }
    .cost {
      background-color: $bg;
      border-radius: 10px;
      padding: 20px 20px;
    }
    
    .use-formula {
      background-color: $lakeBlue;
      color: white;
      border-radius: 5px;
    }
    .course {
      margin-bottom: 20px;
    }
    .designated-area{
      border-radius: 10px 10px 10px 10px;
      background-color: $bg !important;
      padding: 20px;
    }
    .formula{
      width:290px;
      margin:0 10px;
    }
  }
}
</style>
