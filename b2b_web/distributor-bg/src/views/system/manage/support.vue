<template>
  <div >
    <div class="cost">
          <div class="cost1">
            <span class="index">{{ count+1 }}</span>
            <span style="color: #606266;font-weight: 500;">配送地区<el-input @focus="categoryFocus" @blur="focus=false" v-model="selectedArea.dregion" placeholder="请选择配送地区" size="small" class="select-area" ></el-input></span>
              <transition name="el-zoom-in-top">
                <div class="area-box" v-if="area">
                  <el-tree v-clickoutside="handleClose" :default-checked-keys="selectArea" @check-change="areaChange" :props="regions" :data="areaList" ref="tree" show-checkbox node-key="id"></el-tree>
                  <!-- <el-col :span="4" :offset="10"> 
                    <el-button class="mini-search-btn"  @click="handleSubmit">确定</el-button>
                  </el-col> -->
                </div>
              </transition>
            
            <span><i @click="deletes" class="el-icon-error close" v-show="total > 1"></i></span>
          </div>
          <div v-if="distributionArea.formulaFlag === 0" class="cost-lump" >
            <span style="color: #606266;font-weight: 500;" class="span-header">首重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" v-model="distributionArea.firstWeightCost" placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0"></el-input>元</span>
            <span style="color: #606266;font-weight: 500;">续重费用<el-input style="margin-left: 10px;" @keyup.native="moreZero()" placeholder="" size="small" class="span-heavy" type="number" step="0.01" min="0" v-model="distributionArea.additionalWeightCost"></el-input>元</span>
            <el-button size="small" class="use-formula" @click="cFormula" style="margin-left: 10px;">使用公式</el-button>
          </div>
          <div v-else class="cost-lump" >
            <span style="color: #606266;font-weight: 500;" class="span-header">配送公式<el-input placeholder="" size="small" class="formula" v-model="distributionArea.formula"></el-input></span>
            <el-button size="small" class="use-formula" @click="verifyFormula(distributionArea.formula,billingMethod)">验证公式</el-button>
            <el-button size="small" class="use-formula" @click="dFormula" style="margin-left: 10px;">取消公式</el-button>
          </div>
    </div>
  </div>
</template>

<script>
import store from '@/store'
  export default {
    props:{
      billingMethod: {
        type: Number,
        default: 1
      },
      count: {
        type: Number,
        default:0
      },
      total:{
        type: Number,
        default:1
      },
      distributionArea: {
        firstWeightCost:0,
        additionalWeightCost:0,
        defaultFlag:0,
        formulaFlag:0,
        formula:'',
        areaList:[],
      }
      
    },
    // 获取省份
    created() {
      this.areaList = store.getters.areas
      this.isFirst = true
      this.getAreaListName(this.distributionArea.areaList);
    },
    data() {
      return {
        focus:false,
        area: false,
        firstWeightCost: 0, // 首重费用
        additionalWeightCost: 0, // 续重费用
        formula:'',//公式
        regions:{
          label: 'regionName',
          children: 'sonRegions'
        },
        selectArea:[],
        areaExpanded:[],
        areaList:[],
        selectedArea:{
          dregion:''
        },
        isLoadNode: false,
        isFirst:true
      }
    },
    methods: {
      // 验证公式
      verifyFormula(formula, billingMethod) {
        this.$router.push({name: 'verify',params:{formula:formula, billingMethod: billingMethod}})
      },
      categoryFocus(){
        this.focus = true
        this.area = true
        if(this.isFirst){
          this.isFirst = false
          this.loadNode()
        }
      },
      handleClose(){
        if(this.area && !this.focus){
          this.area = false
        }
      },
      areaChange(data, checked, indeterminate) {
        let arr = this.$refs.tree.getCheckedNodes();
        this.getAreaListName(arr)
        this.distributionArea.areaList = [];
        arr.forEach(item => {
          item.countryId = item.countryId === 37 ? 37 : item.id
          if(!item.leaf){
            this.distributionArea.areaList.push(item)
          }else{
            
          }
        })
        this.selectArea = []
        this.distributionArea.areaList.forEach(item =>{
          this.selectArea.push(item.id)
        })
      },
      handleSubmit(){
        this.area = false
        let arr = this.$refs.tree.getCheckedNodes();
        this.getAreaListName(arr)
        this.distributionArea.areaList = [];
        arr.forEach(item => {
          if(!item.leaf){
            this.distributionArea.areaList.push(item)
          }else{
            
          }
        })
      },
      getAreaListName(arr){
        this.selectedArea.dregion = ''
        let arry = []
        for(let i = 0; i<arr.length;i++){
            let b = true
            for(let j = 0;j<arr.length;j++){
              if(arr[i].districtId != undefined && arr[i].districtId != 0 && arr[j].cityId === arr[i].cityId && (arr[j].districtId === undefined || arr[j].districtId === 0)){
                b = false
                break
              }else if(arr[i].cityId != undefined && arr[i].cityId != 0 && arr[j].provinceId === arr[i].provinceId && (arr[j].cityId ===undefined || arr[j].cityId === 0)){
                b = false
                break
              }
            }
            if(b){
              arry.push(arr[i])
            }
          }

        // 数据回显，海外地址cityId处理，默认设置为countryId
        arr.forEach(item => {
          if (item.countryId !== 37 && item.provinceId === 0) {
            item.cityId = item.countryId;
          }
        })

        if(arry.length>0 && arry[0].regionName === undefined){
          if(this.areaList === undefined || this.areaList.length === 0){
            this.$http.regionList(this).then(res => {
              this.areaList = res.data
              this.$store.commit('GET_ALL_AREAS', res.data)
              arry.forEach(item =>{
                // let id = item.districtId != 0?item.districtId:(item.cityId != 0?item.cityId:item.provinceId);
                let id = (item.cityId != 0 && item.cityId != undefined)?item.cityId:item.provinceId
                let b = false
                for(let i = 0;i<this.areaList.length;i++){
                  if(id === this.areaList[i].id){
                    item.regionName = this.areaList[i].regionName
                    b = true
                  }
                  if(this.areaList[i].sonRegions != undefined && this.areaList[i].sonRegions.length >0){
                    for(let j = 0;j<this.areaList[i].sonRegions.length;j++){
                      if(id === this.areaList[i].sonRegions[j].id){
                        item.regionName = this.areaList[i].sonRegions[j].regionName
                        b = true
                      }
                      if(this.areaList[i].sonRegions[j].sonRegions != undefined && this.areaList[i].sonRegions[j].sonRegions.length >0){
                        for(let k = 0;k<this.areaList[i].sonRegions[j].sonRegions.length;k++){
                          if(id === this.areaList[i].sonRegions[j].sonRegions[k].id){
                            item.regionName = this.areaList[i].sonRegions[j].sonRegions[k].regionName
                            b = true
                          }
                        }
                      }
                    } 
                  }
                  if(b){
                    break
                  }
                }
              })
              arry.forEach(item => {
                this.selectedArea.dregion =this.selectedArea.dregion + item.regionName+","
              })
              if(this.selectedArea.dregion !== ''){
                this.selectedArea.dregion = this.selectedArea.dregion.slice(0,this.selectedArea.dregion.length-1)
              }
            })
          }else{
              arry.forEach(item =>{
                // let id = item.districtId != 0?item.districtId:(item.cityId != 0?item.cityId:item.provinceId);
                let id = (item.cityId != 0 && item.cityId != undefined)?item.cityId:item.provinceId;
                let b = false
                for(let i = 0;i<this.areaList.length;i++){
                  if(id === this.areaList[i].id){
                    item.regionName = this.areaList[i].regionName
                    b = true
                  }
                  // 取消区
                  if(this.areaList[i].sonRegions != undefined && this.areaList[i].sonRegions.length >0){
                    for(let j = 0;j<this.areaList[i].sonRegions.length;j++){
                      if(id === this.areaList[i].sonRegions[j].id){
                        item.regionName = this.areaList[i].sonRegions[j].regionName
                        b = true
                      }
                      if(this.areaList[i].sonRegions[j].sonRegions != undefined && this.areaList[i].sonRegions[j].sonRegions.length >0){
                        for(let k = 0;k<this.areaList[i].sonRegions[j].sonRegions.length;k++){
                          if(id === this.areaList[i].sonRegions[j].sonRegions[k].id){
                            item.regionName = this.areaList[i].sonRegions[j].sonRegions[k].regionName
                            b = true
                          }
                        }
                      }
                    } 
                  }
                  if(b){
                    break
                  }
                }
              })
              arry.forEach(item => {
                this.selectedArea.dregion =this.selectedArea.dregion + item.regionName+","
              })
              if(this.selectedArea.dregion !== ''){
                this.selectedArea.dregion = this.selectedArea.dregion.slice(0,this.selectedArea.dregion.length-1)
              }
          }
        }else{
          arry.forEach(item => {
            this.selectedArea.dregion =this.selectedArea.dregion + item.regionName+","
          })
          if(this.selectedArea.dregion !== ''){
            this.selectedArea.dregion = this.selectedArea.dregion.slice(0,this.selectedArea.dregion.length-1)
          }
        }
      },
      cancelB(b){
        this.area = b;
        this.loadNode()
      },
      cFormula() {
        this.distributionArea.formulaFlag = 1;
      },
      dFormula() {
        this.distributionArea.formulaFlag = 0;
      },
      deletes(index) {
        this.$emit('deletes')
      },
      loadNode() {
        if(this.areaList === undefined || this.areaList.length === 0){
          this.$http.regionList(this).then(res => {  
            this.areaList = res.data
            this.$store.commit('GET_ALL_AREAS', this.areaList)
            this.reSelectArea()
            for(let i = 0;i<this.areaList.length;i++){
              if (this.areaList[i].id === 0)
              this.areaList[i].countryId = this.areaList[i].id === 0 ? 0 : 37
              this.areaList[i].provinceId = this.areaList[i].id
              if(this.areaList[i].sonRegions != undefined && this.areaList[i].sonRegions.length >0){
                this.areaList[i].leaf = false
                for(let j = 0;j<this.areaList[i].sonRegions.length;j++){
                  this.areaList[i].sonRegions[j].provinceId = this.areaList[i].id
                  this.areaList[i].sonRegions[j].cityId = this.areaList[i].sonRegions[j].id
                  if(this.areaList[i].sonRegions[j].sonRegions != undefined && this.areaList[i].sonRegions[j].sonRegions.length >0){
                    this.areaList[i].sonRegions[j].leaf = false
                    for(let k = 0;k<this.areaList[i].sonRegions[j].sonRegions.length;k++){
                      this.areaList[i].sonRegions[j].sonRegions[k].provinceId = this.areaList[i].id
                      this.areaList[i].sonRegions[j].sonRegions[k].cityId = this.areaList[i].sonRegions[j].id
                      // this.areaList[i].sonRegions[j].sonRegions[k].districtId = this.areaList[i].sonRegions[j].sonRegions[k].id
                      this.areaList[i].sonRegions[j].sonRegions[k].leaf = false
                    }
                  }
                } 
              }
            }
          })
        }else{
          this.reSelectArea()
          for(let i = 0;i<this.areaList.length;i++){
            this.areaList[i].countryId = this.areaList[i].id === 0 ? 0 : 37
            this.areaList[i].provinceId = this.areaList[i].id
            if(this.areaList[i].sonRegions != undefined && this.areaList[i].sonRegions.length >0){
              this.areaList[i].leaf = false
                for(let j = 0;j<this.areaList[i].sonRegions.length;j++){
                  this.areaList[i].sonRegions[j].provinceId = this.areaList[i].id
                  this.areaList[i].sonRegions[j].cityId = this.areaList[i].sonRegions[j].id
                  if(this.areaList[i].sonRegions[j].sonRegions != undefined && this.areaList[i].sonRegions[j].sonRegions.length >0){
                    this.areaList[i].sonRegions[j].sonRegions = undefined
                    this.areaList[i].sonRegions[j].leaf = false
                    for(let k = 0;k<this.areaList[i].sonRegions[j].sonRegions.length;k++){
                      this.areaList[i].sonRegions[j].sonRegions[k].provinceId = this.areaList[i].id
                      this.areaList[i].sonRegions[j].sonRegions[k].cityId = this.areaList[i].sonRegions[j].id
                      this.areaList[i].sonRegions[j].sonRegions[k].districtId = this.areaList[i].sonRegions[j].sonRegions[k].id
                      this.areaList[i].sonRegions[j].sonRegions[k].leaf = false
                    }
                  }
                }
            }
          }
        }
          
      },
      reSelectArea(){
        this.selectArea = []
        this.distributionArea.areaList.forEach(item =>{//重新定位已选城市
          if(item.districtId != undefined && item.districtId != 0){
            this.selectArea.push(item.districtId)
          }else if(item.cityId != undefined && item.cityId != 0 ){
            this.selectArea.push(item.cityId)
          }else{
            this.selectArea.push(item.provinceId)
          }
        })
      // this.$refs.tree.setCheckedKeys([]);
      // this.$refs.tree.setCheckedKeys(this.selectArea);
      },
      moreZero(){//大于零
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.distributionArea.firstWeightCost) && this.distributionArea.firstWeightCost != undefined && this.distributionArea.firstWeightCost != ""){
          this.distributionArea.firstWeightCost = 0
        // 　　alert('金额输入格式不正确!');
        }
        if(!(/^[0-9]\d*(\.\d+)?$/).test(this.distributionArea.additionalWeightCost) && this.distributionArea.additionalWeightCost != undefined && this.distributionArea.additionalWeightCost != ""){
          this.distributionArea.additionalWeightCost = 0
        // 　　alert('金额输入格式不正确!');
        }
      }
    },
    directives:{ //事件绑定
          clickoutside:{
              bind:function(el,binding,vnode){
                  function documentHandler(e){
                      // 这里判断点击的元素是否是本身，是本身，则返回
                      if(el.contains(e.target)){
                          return false;
                      }
                      // 判断指令中是否绑定了函数
                      if(binding.expression){
                          // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
                          binding.value(e)
                      }
                  }
                  // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
                  el._vueClickOutside_ = documentHandler;
                  document.addEventListener('click',documentHandler);
              },
              unbind:function(el,binding){
                  document.removeEventListener('click',el._vueClickOutside_);
                  delete el._vueClickOutside_;
              }
          }
    },
    watch: {
      distributionArea: {
          handler() {
            this.distributionArea.defaultFlag = 0
            this.$emit('changeDistributionArea',this.distributionArea,this.count);
            this.getAreaListName(this.distributionArea.areaList);
            this.reSelectArea()
          },
          deep: true
      },
      'distributionArea.defaultFlag': {
        handler (val) {
        },
        deep:true
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
    .close {
      top: 10px;
      float: right;
      right: 15px;
      color: #bfbfbf;
    }
    .cost {
      padding: 0 !important;
      position: relative;
      
    }
    .cost1{
      position: relative;
    }
    .index{
      display:block;
      float:left;
      width:30px;
    }
    .cost-lump{
      margin-left: 30px;
    }
    .select-area{
      width:400px;
      margin:0 10px;
    }
    .area-box {
        border: 1px solid #ccc;
        width: 300px;
        padding: 10px;
        overflow: auto;
        background-color: white;
        position: absolute;
        margin-left: 95px;
        max-height: 300px;
        left: 0;
        top: 45px;
        z-index: 99;
        border-radius: 10px;
    }
</style>