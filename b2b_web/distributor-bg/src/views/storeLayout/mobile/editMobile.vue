<template>
  <div class="mobile-detail">
    <header class="header">
      <h4 class="title">{{title}}</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-back" @click="cancel">返回列表</el-button>
    </header>
     <div v-loading="loading">
      <el-form ref="form" :model="formData" label-width="150px" class="editMobile-form1" :rules="rules">
        <el-form-item label="模块类型" prop="moduleType">
          <el-select v-model="formData.moduleType" placeholder="模块类型" size="mini">
            <el-option
            v-for="item in moduleTypes"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="状态" size="mini" >
            <el-option
            v-for="item in status"
            :key="item.id"
            :label="item.name"
            :value="item.id">
             </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model.trim="formData.sort" size="mini" type="number" min="0" style="width:80px"></el-input>
          <span class="place-holder">注意：序号越小，排在前面</span>
        </el-form-item>
        <el-form-item :label="titleName" prop="list" v-if="formData.moduleType===1 || formData.moduleType===2">
          <div v-if="formData.moduleType===1">
            <el-button class="mini-search-btn" @click="add(totalCount)">新增</el-button>
            <span class="place-holder">至少添加一个轮播图，最多5张，多张的的按当前页面显示的顺序在前端展示</span>
          </div>
          <div v-if="formData.moduleType===2">
            <el-select v-model="picNum" placeholder="选择图片数量" size="mini" >
              <el-option
              v-for="item in picNums"
              :key="item.id"
              :label="item.value"
              :value="item.id">
              </el-option>
            </el-select>
          </div>
          <el-table :data="formData.list" header-row-class-name="header-row" border class="tr-header" max-height="550">
            <el-table-column :label="itemName" :width="220" align="center">
              <template slot-scope="scope">
                <upload-small-img class="avatar-uploader"
                                  :item = 'scope.row'
                                  :index = 'scope.$index'
                                  :isEdit = true
                                  @getImgUrl = "getImgUrl">
                </upload-small-img>
              </template>
            </el-table-column>
            <el-table-column label="跳转路径" align="center">
              <template slot-scope="scope">
                <div>
                  <div>
                    <span>跳转类型：</span>
                    <el-select  v-model="scope.row.jumpType" size="mini" class="select-type">
                      <el-option v-for="item in jumpTypes"
                                :key="item.id"
                                :value="item.id"
                                :label="item.name"></el-option>
                    </el-select>
                  </div>
                  <div class="item-mt">
                    <span>图片宽度百分率：</span>
                    <el-input class="input-url"  v-model.trim="scope.row.widthPercentage"  placeholder="50.00"></el-input>
                  </div>
                  <div class="item-mt">
                    <span>跳转目标：</span>
                    <el-input class="input-url" v-model.trim="scope.row.jumpParams"  placeholder="http://"></el-input>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="排序" align="center" prop="subSort" :width="160" v-if="formData.moduleType===1">
              <template slot-scope="scope">
                <el-input v-model.trim="scope.row.subSort" size="mini" type="number" min="0" style="width:80px"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" :width="160" v-if="formData.moduleType===1">
              <template slot-scope="scope">
                <el-button class="mini-delete-btn" @click="handleDelete(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="样式" prop="styleType" v-if="formData.moduleType===3">
          <el-select v-model="styleType" placeholder="请选择" size="mini">
            <el-option
            v-for="item in styleTypes"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="背景图片" prop="imageUrl" v-if="formData.moduleType===3">
          <template>
            <el-upload class="avatar-uploader" :action="action" :show-file-list="false" 
                :before-upload="beforeUploadOriginImage">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </template>
        </el-form-item>
        <el-form-item label="商品列表:" prop="originImage" v-if="formData.moduleType===3 ">
            <el-button class="mini-search-btn" @click="handlerGoods">选择商品</el-button>
            <!-- <el-button class="mini-tableadd-btn" @click="batchImport(totalCount)">批量导入</el-button>
            <el-button class="mini-freeze-btn" @click="exportGoods(totalCount)">导出商品列表</el-button>
            <el-button class="mini-delete-btn" @click="delGoods(totalCount)">清空商品</el-button> -->
            <el-table :data="goodsData" header-row-class-name="header-row" border class="tr-header" max-height="550" >
              <el-table-column label="商品编号" :width="220" align="center">
                <template slot-scope="scope">
                  <span>{{scope.row.goodsNo}}</span>
                </template>
              </el-table-column>
              <el-table-column label="商品名称" align="center" >
                <template slot-scope="scope">
                  <span>{{scope.row.goodsName}}</span>
                </template>
              </el-table-column>
              <el-table-column label="排序" align="center" prop="sort" :width="160">
                <template slot-scope="scope">
                  <el-input v-model.trim="scope.row.sort" size="mini" type="number" min="0" style="width:80px"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" :width="100">
                <template slot-scope="scope">
                  <el-button class="mini-delete-btn" @click="handleDelGoods(scope.$index, scope.row)" :disabled="formData.moduleType===2">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
        </el-form-item>
        
        <!-- 商品列表 -->
        <div class="editMobile-goodBox" v-if="formData.moduleType===4">
          <el-button class="mini-search-btn editMobile-goodAdd" @click="addGoodtypeArr">新增</el-button>
          <div class="editMobile-goodDiv"  v-for="(item,index) in goodtypeArr" :key="index">
            <div class="editMobile-goodDiv-delete"><el-button class="mini-delete-btn" @click="deleteGoodtypeArr(index)">删除</el-button></div>
            <el-form-item label="分类标题" prop="imageUrl" >
              <template>
                  <el-input class="input-url" v-model.trim="item.title"  placeholder="最多输入五个字" maxlength="5"></el-input>
              </template>
            </el-form-item>
            <el-form-item label="排序" prop="imageUrl" >
              <template>
                  <el-input v-model.trim="item.sort" size="mini" type="number" min="0" style="width:80px"></el-input>
              </template>
            </el-form-item>
            <el-form-item label="商品范围" prop="imageUrl" >
              <template>
                <el-radio-group v-model="item.appointType" @change="ordioChange(item.appointType,index)">
                  <el-radio :label="4">全部商品</el-radio>
                  <el-radio :label="1">指定分类</el-radio>
                  <el-radio :label="2">指定品牌</el-radio>
                  <el-radio :label="3">指定商品</el-radio>
                </el-radio-group>
              </template>
            </el-form-item>
            <el-form-item label="指定分类" prop="imageUrl" v-if="item.appointType==1" >
              <template>
                  <el-button class="mini-search-btn" @click="choiceClassifyListFun(index)">选择分类</el-button>
                  <el-table :data="item.classifyTableList" header-row-class-name="header-row" border max-height="550" style="width: 322px">
                    <el-table-column label="分类名称" prop="name" :width="220" align="center"/> 
                    <el-table-column label="操作" align="center" :width="100">
                      <template slot-scope="scope">
                        <el-button class="mini-delete-btn" @click="deleteClassify(index,scope.row.id)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
              </template>
            </el-form-item>
            <el-form-item label="指定品牌" prop="imageUrl" v-if="item.appointType==2">
              <template>
                  <el-button class="mini-search-btn" @click="openBrandFun(index)">选择品牌</el-button>
                  <el-table :data="item.brandTableList" header-row-class-name="header-row" border max-height="550" style="width: 322px">
                    <el-table-column label="品牌名称" prop="name" :width="220" align="center"/>
                    <el-table-column label="操作" align="center" :width="100">
                      <template slot-scope="scope">
                        <el-button class="mini-delete-btn" @click="deleteBrand(index,scope.row.id)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
              </template>
            </el-form-item>
            <el-form-item label="商品列表:" prop="originImage" v-if="item.appointType==3">
              <el-button class="mini-search-btn" @click="openGoods(index,item.goodsList)">选择商品</el-button>
              <el-table :data="item.goodsList" header-row-class-name="header-row" border class="tr-header" max-height="550" >
                <el-table-column label="商品编号" :width="220" align="center">
                  <template slot-scope="scope">
                    <span>{{scope.row.goodsNo}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="商品名称" align="center" >
                  <template slot-scope="scope">
                    <span>{{scope.row.goodsName}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="排序" align="center" prop="sort" :width="160">
                  <template slot-scope="scope">
                    <el-input v-model.trim="scope.row.sort" size="mini" type="number" min="0" style="width:80px"></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" :width="100">
                  <template slot-scope="scope">
                    <el-button class="mini-delete-btn" @click="deleteGoods(index, scope.row.id)" >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </div>
          </div>
      </el-form>
     
      <div class="clearfix footbtn">
        <el-col :span="1" :offset="12">
          <el-button class="mini-search-btn" @click="handleSave('form')" >保存</el-button>
        </el-col>
      </div>
    </div>
    <!-- 指定分类的弹框 -->
    <el-dialog title="指定分类" :visible.sync="classifyVisible">
      <div class="editMobile-search">
        <el-input v-model.trim="classifySearchName" size="mini" placeholder="分类牌名称" style="width:300px"></el-input>
        <el-button class="mini-search-btn" style="margin-left:30px;" @click="searchClassifyFun">搜索</el-button>
      </div>
      <el-table :data="classifyTableData" row-key="id" border default-expand-all :tree-props="{children: 'children'}" @selection-change="classifySelectionChange">
        <el-table-column type="selection" width="55" ></el-table-column>
        <el-table-column property="name" label="分类名称" ></el-table-column>
        <el-table-column property="nameEn" label="分类英文名称" ></el-table-column>
        <el-table-column property="name" label="状态" width="200">
          <template slot-scope="scope">
							{{scope.row.openFlag==1?'启用':'停用'}}
						</template>
        </el-table-column>
      </el-table>
      <div class="editMobile-dialogPage">
        <el-pagination background row-key="id" default-expand-all @current-change="classifyCurrentChange" layout=" prev, pager, next , total ,  jumper" :total="classifyTotal" class="page"></el-pagination>
      </div>
      <div class="editMobile-btn">
         <el-button class="mini-search-btn" style="margin-left:47%;" @click="choiceClassifyConfirmFun">确定</el-button>
			   <el-button size="mini" @click="classifyVisible=false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 指定品牌的弹框 -->
    <el-dialog title="指定品牌" :visible.sync="brandVisible">
      <el-table :data="brandTableData" row-key="id" border  @selection-change="brandSelectionChange">
        <el-table-column type="selection" width="55" ></el-table-column>
        <el-table-column property="name" label="品牌名称" ></el-table-column>
        <el-table-column property="nameEn" label="品牌英文名称" ></el-table-column>
        <el-table-column property="name" label="品牌状态" width="200">
          <template slot-scope="scope">
							{{scope.row.openFlag==1?'启用':'停用'}}
						</template>
        </el-table-column>
      </el-table>
      <div class="editMobile-dialogPage">
        <el-pagination background row-key="id" default-expand-all @current-change="brandCurrentChange" layout=" prev, pager, next , total ,  jumper" :total="brandTotal" class="page"></el-pagination>
      </div>
      <div class="editMobile-btn">
         <el-button class="mini-search-btn" style="margin-left:47%;" @click="choiceBrandConfirmFun">确定</el-button>
			   <el-button size="mini" @click="brandVisible=false">取消</el-button>
      </div>
    </el-dialog>

    <!--商品推广模块-- 选择商品 -->
		<el-dialog :modal-append-to-body="false" :visible="selectShow" width="80%" :before-close="closeDialog">
      <select-goods ref="goodsList" :selectGoodsData="tmpStocks" @submit="disSubmit" @cancel="closeDialog" ></select-goods>
		</el-dialog>

    <!--商品列表模块-- 选择商品 -->
		<el-dialog :modal-append-to-body="false" :visible="goodSelectShow" width="80%" :before-close="goodCloseDialog">
      <select-goods ref="goodsList" :selectGoodsData="goodTmpStocks" @submit="goodDisSubmit" @cancel="goodCloseDialog" ></select-goods>
		</el-dialog>
  </div>
</template>

<script>
import uploadSmallImg from "@/views/storeLayout/compomemts/uploadSmallImg"
import selectGoods from "@/views/goods/components/selectGoods"
import Page from '@/components/pagination'
import {monthDay} from '@/utils/timeFormat.js'
export default {
  name: 'editMobile',
  components: {Page},
  data() {
    return {
      action: process.env.BASE_API +'system/v1/web/admin/oss/sts',
      loading: false,
      title: '',
      titleName: '选择轮播图：',
      itemName: '轮播图',
      totalCount: 0,
      picNum: 0,
      tId: undefined,
      styleType: 1, // 推广模块-样式
      imageUrl: '', // 推广模块-背景图片
      goodsData: [], // 商品列表
      selectShow: false,
      goodsDelIds: [], // 删除商品
      goodsUpIds: [], // 更新商品
      originGoods: [], // 初始数据
      tmpStocks: [], // 选中商品列表
      formData: {
        moduleType: 1,
        status: 1,
        sort: 0,
        list: [],
        goodsIds: [],
        mobileChildCmds:[], //商品列表分类
      },
      rules: {
        moduleType: [{
          required: true,
          message: '请选择模块类型',
          trigger: 'change'
        }],
        status: [{
          required: true,
          message: '请选择状态',
          trigger: 'change'
        }],
        sort: [{
          required: true,
          message: '请填写排序',
          trigger: 'blur'
        }]
      },
      moduleTypes: [
        {id: 1,name: '轮播图'},
        {id: 2,name: '图片模块'},
        {id: 3,name: '商品推广模块'},
        {id: 4,name: '商品列表模块'}
      ],
      status: [
        {id: 1,name: '显示'},
        {id: 0,name: '隐藏'}
      ],
      jumpTypes: [
        {id: 1,name: '无链接'},
        {id: 2,name: '跳转商品'},
        {id: 3,name: '跳转分类'},
        {id: 4,name: '跳转其它页面'},
        {id: 5,name: '跳转搜索结果页'}
      ],
      styleTypes: [
        {id: 1,name: '3列商品'},
        {id: 2,name: '4列商品'},
        {id: 3,name: '多列商品'},
      ],
      picNums: [
        {id: 1,value: 1},
        {id: 2,value: 2},
        {id: 3,value: 3},
        {id: 4,value: 4},
        {id: 5,value: 5},
      ],
  
      // 指定分类
      classifyVisible:false,
      classifyPage:1,
      classifyTotal:0,
      classifyContent:'',
      classifyTableData:[],
      classifyList:[],  //选择的分类
      classifyTableList:[],  //最终确认选择的分类
      classifySearchName:'',  //搜索字符
      // 指定品牌
      brandVisible:false,
      brandPage:1,
      brandTotal:0,
      brandTableData:[],
      brandList:[],  //选择的品牌
      brandTableList:[],  //最终确认选择的品牌
      // 选择商品
      goodSelectShow:false,
      goodTmpStocks:[],  //选中的商品列表
      // 商品列表模块集合
      goodtypeArr:[
        {
          appointType:4,  //指定类型 1.指定分类 2.指定品牌 3.指定商品
          sort:'',  //排序
          title:'', //分类标题
          goodsIds:[],  //商品ids集合
          mobilePoints:[],  //分类、品牌ids集合
          classifyTableList:[],  //分类列表
          brandTableList:[], //品牌列表
          goodsList:[],  //商品列表
        }
      ], 
       goodtypeArrIndex:0, //选择的第几个
      

    
    }
  },
  created () {
    this.loading = true
    let that=this;
    // 获取数据详情
    if (this.$route.query.id) {
      // 编辑
      this.title = '编辑'
      this.$http.mobileDetail(this, {id: this.$route.query.id}).then(res => {
        if(res.success) {
          this.formData = res.data
          if (this.formData.moduleType === 3) {
            this.formData.list = res.data.list
            this.imageUrl = this.formData.list[0].imageUrl
            this.styleType = this.formData.list[0].styleType
            this.tId = this.formData.list[0].id
          }
          // if (this.formData.moduleType === 3 || this.formData.moduleType === 4) {
          if (this.formData.moduleType === 3|| this.formData.moduleType === 4) {
            if(this.formData.moduleType === 3){ 
              this.$http.mobileListByIds(this, {page:1,size:10000,mobileId:this.$route.query.id,moduleType:this.formData.moduleType}).then(res2 => {
                if (res2.success) {
                  this.goodsData = res2.data.list
                  this.tmpStocks = res2.data.list
                  this.setArr(this.goodsData);
                  if (res2.data.list.length > 0) {
                    res2.data.list.forEach(item => {
                      this.originGoods.push({
                        id: item.id,
                        sort:item.sort
                      })
                    })
                  }
                  
                } 
              })
            }
            if(this.formData.moduleType === 4){
                console.log('商品模块列表：',res.data);
                let ids=[];
                if(res.data.mobileChildDTOS&&res.data.mobileChildDTOS.length>0){
                    res.data.mobileChildDTOS.forEach(item=>{
                        if(item.classifies&&item.classifies.length>0){
                            this.$set(item,'classifyTableList',item.classifies);
                        }
                        if(item.brands&&item.brands.length>0){
                            this.$set(item,'brandTableList',item.brands);
                        }
                        if(item.appointType==3){
                            ids.push(item.id)
                        }
                        
                    })
               
                    if(this.formData.moduleType==4&&ids.length>0){

                    
                      this.$http.mobileListByIdss(this, {page:1,size:10000,mobileIds :ids,moduleType:this.formData.moduleType}).then(res2 => {
                          if(res2.success){
                              console.log('商品模块列表数据--：',res2.data);
                              
                                res.data.mobileChildDTOS.forEach(item=>{
                                    if(item.appointType==3){
                                      
                                        res2.data.forEach(item2=>{
                                          if(item2.mobileId==item.id){
                                              that.$set(item,'goodsList',item2.pageInfo.list);
                                            
                                          }
                                        })
                                    }
                                })
                                
                              
                          }
                      })
                    }
                    this.goodtypeArr=res.data.mobileChildDTOS;
                }
                

            }
          }
          this.loading = false
        } else {
          this.loading = false
        }
      })
    } else {
      // 添加
      this.title = '添加'
      this.loading = false
    }
    // 获取分类数据
    this.getClassifyListFun()
  },
  mounted() {
		this.getClassifyListFun()
	},
	activated() {
	 this.getClassifyListFun()
  },
  methods: {
    // 新增商品列表模块集合
    addGoodtypeArr(){
        let objArr={
          appointType:4,  //指定类型 1.指定分类 2.指定品牌 3.指定商品
          sort:'',  //排序
          title:'', //分类标题
          goodsIds:[],  //商品ids集合
          mobilePoints:[],  //分类、品牌ids集合
          classifyTableList:[],  //分类列表
          brandTableList:[], //品牌列表
          goodsList:[],  //商品列表
        }
        this.goodtypeArr.push(objArr);
    },

    // 删除
    deleteGoodtypeArr(index){
       this.goodtypeArr=this.goodtypeArr.filter((item,indexs)=>indexs!=index);
    },
    // 单项选择分类的时候
    ordioChange(index){
         console.log(index);
         this.goodtypeArr.forEach((item,indexs)=>{
           if(index==indexs){
               this.$set(item,'mobilePoints',[])
           }
         })
    },
    // 获取分类数据
    getClassifyListFun(){
      let params={
        page:this.classifyPage,
        size:10,
        content:this.classifySearchName,
      }
       this.$http.getClassifyList(this,params).then(res => {
        //  console.log('分类列表数据：',res.tableData);
         if(res.tableData.length>0){
            this.classifyTableData = res.tableData;
         }
				
				this.classifyTableData = res.tableData
				this.classifyTotal = res.total
			})
    },
    // 搜索-分类
    searchClassifyFun(){
        this.classifyPage = 1;
			  this.getClassifyListFun();
    },
    // 选择分类
    choiceClassifyListFun(index){
         this.classifyVisible=true;
         this.classifyPage=1;
         this.classifyList=[];
         this.getClassifyListFun();
         this.goodtypeArrIndex=index;

    }, 
    // 删除选择的分类
    deleteClassify(index,id){
        this.goodtypeArr[index].classifyTableList=this.goodtypeArr[index].classifyTableList.filter(item=>item.id!=id)
    },
    // 选择分类分页
    classifyCurrentChange(page){
       this.classifyPage = page;
			 this.getClassifyListFun();
    },

    // 选择分类
    classifySelectionChange(e){
       console.log('选中的分类：',e);
       this.classifyList=[];
       e.forEach(item=>{
         let objs={
           id:item.id,
           name:item.name,
         }
         this.classifyList.push(objs);
       })
    },
    // 选择分类---确认
    choiceClassifyConfirmFun(){
      
      if(this.classifyList.length>0){
          this.goodtypeArr.forEach((item,index)=>{
            if(this.goodtypeArrIndex==index){
              this.$set(item,'classifyTableList',this.classifyList)
            }
          })
          this.classifyVisible=false;
      }else{
          this.$message('至少选择一个分类！');
      }
        
    },
    //获取品牌
    getBrandListFun(){
      let params={
        page:this.brandPage,
        size:10,
      }
      this.$http.getBrandList(this,params).then(res=>{
        this.brandTableData = res.data.list
        this.brandTotal = res.data.total
      })
    },

    // 打开指定品牌
    openBrandFun(index){
       this.brandVisible=true;
       this.brandPage=1;
       this.brandList=[];
       this.getBrandListFun();
       this.goodtypeArrIndex=index;
    },
    // 删除选择的品牌
    deleteBrand(index,id){
        this.goodtypeArr[index].brandTableList=this.goodtypeArr[index].brandTableList.filter(item=>item.id!=id)
    },
    // 删除商品
    deleteGoods(index,id){
        this.goodtypeArr[index].goodsList=this.goodtypeArr[index].goodsList.filter(item=>item.id!=id)
    },
    // 选择分类分页
    brandCurrentChange(page){
       this.brandPage = page;
			 this.getBrandListFun();
    },
    // 选择指定品牌
    brandSelectionChange(e){ 
       this.brandList=[];
       e.forEach(item=>{
         let objs={
           id:item.id,
           name:item.name,
         }
         this.brandList.push(objs);
       })
    },
    // 选择分类---确认
    choiceBrandConfirmFun(){
      if(this.brandList.length>0){
          this.goodtypeArr.forEach((item,index)=>{
            if(this.goodtypeArrIndex==index){
              this.$set(item,'brandTableList',this.brandList)
            }
          })
          this.brandVisible=false;
      }else{
          this.$message('至少选择一个品牌！');
      }
        
    },

    //选择商品--
    goodDisSubmit(msg){ // dialog确定操作
          this.goodTmpStocks = msg
          this.goodtypeArr.forEach((item,index)=>{
            if(this.goodtypeArrIndex==index){
              this.$set(item,'goodsList',this.goodTmpStocks)
            }
          })
        
        this.goodSelectShow = false
    },
    goodCloseDialog() { // 关闭dialog的X
      this.goodSelectShow = false
    },
     // 选择商品
    openGoods (index,goodsList) {
      this.goodSelectShow = true;
      this.goodtypeArrIndex=index;
      this.goodTmpStocks=[];
      if (this.$route.query.id) {
          this.goodTmpStocks=goodsList;
      }
      
    },



    
    
     // 保存
    handleSave() {
      this.$refs['form'].validate((valid) => {
        if (valid && this.validateList()) {
          if (this.formData.moduleType === 2) {
            this.formData.list.map((item, index) => {
              item.subSort = index + 1
            })
          } else if (this.formData.moduleType === 3 ) {
            if (this.goodsData.length === 0) {
              this.$message('至少要有一个指定商品！');
              return
            }
            if (this.formData.moduleType === 3) {
              this.formData.list = []
              this.formData.list.push({
                id: this.tId,
                styleType: this.styleType,
                imageUrl: this.imageUrl
              })
            } 
          }else if(this.formData.moduleType === 4){  //2018-05.25新增
                this.formData.mobileChildCmds=[];
                this.goodtypeArr.forEach(item=>{
                  let objk={
                      appointType:item.appointType,
                      sort:item.sort,
                      title:item.title,
                  }
                  if(item.appointType==1){  //分类
                      let classifyIds=[];   
                      item.classifyTableList.forEach(items=>{
                          let objs={
                            pointId:items.id, 
                          }
                          classifyIds.push(objs);
                      })
                      if(classifyIds.length>0){
                        this.$set(objk,'mobilePoints',classifyIds)
                      }
                  }else if(item.appointType==2){  //品牌
                      let brandIds=[];   
                      item.brandTableList.forEach(items=>{
                          let objs={
                            pointId:items.id, 
                          }
                          brandIds.push(objs); 
                      })
                      if(brandIds.length>0){
                        this.$set(objk,'mobilePoints',brandIds)
                      }
                  }else if(item.appointType==3){  //商品
                    let goodsIds=[];   
                    item.goodsList.forEach(items=>{
                        let objs={
                          goodsId:items.id,
                          operationType:1,  //operationType:1:新增  2.修改 3.删除  4.置顶 
                          sort:items.sort, 
                        }
                        goodsIds.push(objs);
                    })
                    if(goodsIds.length>0){
                      this.$set(objk,'goodsIds',goodsIds)
                    }
                  }
                  this.formData.mobileChildCmds.push(objk);
                  
                  
                })
              
          }
          if (this.$route.query.id) {
            // 更新
            if (this.formData.moduleType === 3 || this.formData.moduleType === 4) {
              this.goodsAddIds = []
              this.goodsUpIds = []
              // 过滤添加和修改商品
              if (this.originGoods.length > 0) {
                this.filterGoods(this.goodsData, this.originGoods)
              } else {
                if (this.goodsData.length > 0) {
                  this.formData.goodsIds = []
                  this.goodsData.forEach((item, index) => {
                    this.formData.goodsIds.push({
                      mobileId: this.formData.id,
                      goodsId: item.id,
                      sort: item.sort,
                      operationType: 1 // 添加
                    })
                  })
                }
              }
            }
            this.$http.editMobile(this, this.formData).then(res => {
              if (res.success) {
                this.$router.push({ name: 'mobile' })
              }
            })
          } else {
            // 添加
            if (this.formData.moduleType === 3 || this.formData.moduleType === 4) {
              this.formData.goodsIds = []
              this.goodsData.forEach((item, index) => {
                this.formData.goodsIds.push({
                  goodsId: item.id,
                  sort: item.sort,
                  operationType: 1 // 添加
                })
              })
            }
            console.log('添加的参数：',this.formData);
            this.$http.addMobile(this, this.formData).then(res => {
              if (res.success) {
                this.$router.push({ name: 'mobile' })
              }
            })
          }
        }
      })
    },
    // 对比数组筛选添加和修改商品
    filterGoods(item1, item2) {
      // item1 更新后的数组  item2 原始数组
      for (let j=0; j< item1.length; j++) {
        let va = item1[j]
        let isExit = false
          for (let i=0; i< item2.length; i++) {
          let item = item2[i]
            if (va.id === item.id) {
              // 判断是否更新
              if (va.sort !== item.sort) {
                this.goodsUpIds.push({
                  mobileId: this.formData.id,
                  goodsId: va.id,
                  sort: va.sort,
                  operationType: 2 // 更新
                })
              }
              isExit = true
              break
            }
        }
        if (!isExit) {
          this.goodsAddIds.push({
            mobileId: this.formData.id,
            goodsId:va.id,
            sort: va.sort,
            operationType: 1
          })
        }
      }
      this.formData.goodsIds = this.goodsAddIds.concat(this.goodsDelIds.concat(this.goodsUpIds))
    },
    // 验证轮播图或商品列表数据
    validateList() {
      let flag = true
      if (this.formData.moduleType === 1 || this.formData.moduleType === 2) {
        // 轮播图
        if(this.formData.list.length <= 0) {
          this.$message.error('至少添加一个轮播图')
          flag = false
        } else {
          this.formData.list.forEach(item => {
            if ((item.jumpType > 1 && item.jumpParams === '') || item.imageUrl === ''  || (this.formData.moduleType === 1 && item.subSort === '')) {
              this.$message.error('请完善轮播图信息')
              flag = false
            }
          })
        }
      } else if (this.formData.moduleType === 3 ) {
        // 商品推广模块、商品列表模块
        if (this.goodsData.length > 0) {
          this.goodsData.forEach(item => {
            if (item.sort === '' || item.sort === undefined) {
              this.$message.error('请完善商品列表信息')
              flag = false
            }
          })
        }
      }else if(this.formData.moduleType === 4){
          this.goodtypeArr.forEach(item=>{
             if (item.sort === '' || item.sort === undefined) {
                 this.$message.error('请完善商品列表模块排序')
                 flag = false
             }
             if(item.title==''||item.title==undefined){
                 this.$message.error('请完善商品列表模块标题')
                 flag = false
             }
             if(item.appointType==1){
                if(item.classifyTableList.length==0){
                    this.$message.error('请选择分类')
                    flag = false;
                    
                }
             }else if(item.appointType==2){
                if(item.brandTableList.length==0){
                    this.$message.error('请选择品牌')
                    flag = false
                }
             }else if(item.appointType==3){
                item.goodsList.forEach(items=>{
                    if (items.sort === '' || items.sort === undefined) {
                      this.$message.error('请完善商品列表信息')
                      flag = false
                    }
                })
             }

             
          })
      }
      return flag
    },
    // 添加轮播图
    add(totalCount) {
      if (this.formData.list.length >= 5) {
        this.$message.error("最多只能添加5张轮播图！");
      } else {
        for (var i = 0; i < this.formData.list.length; i++) {
          if (this.formData.list[i].id == undefined) {
            break;
          }
        }
        this.formData.list.push({
          imageUrl: "",
          jumpParams: "",
          jumpType: 1,
          subSort: 0
        });
        this.totalCount = totalCount + 1;
      }
    },
    delGoods(){
      this.goodsData.splice(0,this.goodsData.length);
      this.tmpStocks = this.goodsData
      this.originGoods.forEach(item => {
        this.goodsDelIds.push({
          mobileId: this.formData.id,
          goodsId: item.id,
          sort:item.sort,
          operationType: 3
        })
      })
    },
    // 删除轮播图
    handleDelete (index) {
      this.formData.list.splice(index,1)
    },
    // 删除商品列表
    handleDelGoods (index, row) {
      this.tmpStocks.splice(index, 1)
      this.goodsData = this.tmpStocks
      this.originGoods.forEach(item => {
        if (item.id === row.id) {
          this.goodsDelIds.push({
            mobileId: this.formData.id,
            goodsId: item.id,
            sort:item.sort,
            operationType: 3
          })
        }
      })
    },
    // 选择商品
    handlerGoods () {
      this.selectShow = true;
      // this.goodsData=[];
    },
    setArr(arr){
        let obj ={};
        let temp=[];
        for( let i = 0; i < arr.length; i++ ) {
        let type= Object.prototype.toString.call(arr[i].id);//不加类型 分不清 1 '1'
        if( !obj[ arr[i].id +type] ) {
          temp.push( arr[i] );
          obj[ arr[i].id+ type ] =true;//这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
        }
        this.goodsData = temp;
      }
    },
    disSubmit(msg) { // dialog确定操作
      this.tmpStocks = msg
      this.goodsData = this.tmpStocks
      this.selectShow = false
    },
    closeDialog() { // 关闭dialog的X
      this.selectShow = false
    },
    getImgUrl(index,url){
      this.formData.list[index].imageUrl = url
      console.log(this.formData.list);
    },
    // 上传
    beforeUploadOriginImage(file) {
        if (file.type != 'image/jpeg' && file.type != 'image/bmp' &&
          file.type != 'image/jpg' && file.type != 'image/jpeg' &&
          file.type != 'image/png' && file.type != 'image/gif') {
          this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
          return false
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
        }
        // 随机命名
        let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
        this.$http.getFileSts(this).then(result => {  
          const client = new OSS.Wrapper({
            region: result.data.region,
            accessKeyId: result.data.accessKeyId,
            accessKeySecret: result.data.accessKeySecret,
            stsToken: result.data.securityToken,
            bucket: result.data.bucketName,
            endpoint: result.data.endpoint,
            secure: true
          })
          // 上传
          client.multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {}).then((results) => {
            return new Promise((resolve, reject) => {
              this.imageUrl = result.data.hostname + results.name,
                resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
        })
      },
    // 随机生成文件名
    random_string(len) {
      len = len || 32;
      var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
      var maxPos = chars.length;
      var pwd = '';
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    },
    // 返回列表
    cancel() {
      this.$router.push({name: 'mobile'})
    }
  },
  components: {
    uploadSmallImg,
    selectGoods
  },
  watch: {
    'formData.moduleType': {
      handler(val) {
        if (val === 1) {
          this.titleName = '选择轮播图：'
          this.itemName = '轮播图'
          this.picNum = 0
        } else if (val === 2) {
          this.titleName = '图片数量：'
          this.itemName = '图片'
          this.picNum = this.formData.list.length > 0 ? this.formData.list.length : 1
        } else if (val === 3) {
          if (this.formData.length > 0) {
            this.tId = this.formData.list[0].tId ? this.formData.list[0].tId : undefined
            this.styleType = this.formData.list[0].styleType ? this.formData.list[0].styleType : 1
            this.imageUrl = this.formData.list[0].imageUrl ? this.formData.list[0].imageUrl : ''
          }
        }
      },
      deep: true
    },
    picNum: {
      handler(val) {
        this.picNum = val
        if (val > this.formData.list.length) {
          let num = val - this.formData.list.length
          for(let i=0; i<num; i++) {
            this.formData.list.push({
              imageUrl: "",
              jumpParams: "",
              jumpType: 1,
              subSort: 0
            })
          }
        } else if (val < this.formData.list.length) {
          this.formData.list = []
          for(let i=0; i<val; i++) {
            this.formData.list.push({
              imageUrl: "",
              jumpParams: "",
              jumpType: 1,
              subSort: 0
            })
          }
        }
      },
      deep: true
    }
  }
}
</script>

<style lang="scss">
  .mobile-detail{
    background: #fff;
    .header{
      @extend .header
    }
    .editMobile-form1{

    }
    .place-holder{
      font-size:14px;
    }
    .item-mt{
      margin-top:10px;
    }
    .input-url{
      width:200px;
    }
    .select-type{
      width:190px;
      .el-input{
        width:190px;
        padding-right: 0;
      }
    }
    .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
      border-color: rgb(33, 184, 203);
    }

    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 120px;
      height: 120px;
      line-height: 120px;
      text-align: center;
    }
    .avatar {
      width: 120px;
      height: 120px;
      display: block;
    }

    // 商品列表
    .editMobile-goodBox{
     
      .editMobile-goodAdd{
        margin-left: 100px;
        margin-bottom: 20px;
      }
      .editMobile-goodDiv{
        margin-top: 20px;
        background:  rgba(242, 242, 242, 1);
        padding: 30px 30px 30px 0;
        width: 1200px;
        margin-left: 100px;
        .editMobile-goodDiv-delete{
          display: flex;
          justify-content: flex-end;
        }
      }
    }
  }

  // 指定分类
  .editMobile-search{
     display: flex;
     align-items: center;
     justify-content: flex-start;
     margin-bottom: 20px;
  }
  .editMobile-dialogPage{
     margin-top: 20px;
     margin-left: 500px;
  }
  .editMobile-btn{
    margin-top: 20px;
    border-top: 1px solid #f5f5f5;
    padding-top: 25px;
  }
</style>