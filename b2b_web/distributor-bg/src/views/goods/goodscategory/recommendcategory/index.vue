<template>
	<div class="add-category">
		<header >
		  <h4>推荐分类</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			 返回商品分类列表
		  </el-button>
		</header>
		<div class="recmmend-classify">
			<div class="recmmend-classify-title">推荐分类列表：</div>
			<div class="recmmend-classify-right">
				<div class="recmmend-classify-add"><button class="mini-batch-btn" @click="addClassifyVisibleFun">新增</button></div>
				<el-table :data="classifyTableList" header-row-class-name="header-row" border max-height="550" style="width: 762px">
                    <el-table-column label="分类名称" prop="name" :width="220" align="center"/> 
					<el-table-column label="分类英文名称" prop="nameEn" :width="220" align="center"/> 
					<el-table-column label="小程序分类名称" prop="name" :width="220" align="center"/> 
                    <el-table-column label="操作" align="center" :width="100">
                      <template slot-scope="scope">
                        <el-button class="mini-delete-btn" @click="deleteClassify(scope.row.id)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
			</div>
		</div>
        <!-- 轮播图 -->
		<div class="recmmend-classify">
			<div class="recmmend-classify-title">轮播图：</div>
			<div class="recmmend-classify-right">
				<div class="recmmend-classify-add"><button class="mini-batch-btn" @click="addBanner">新增</button><span>最多三张</span></div>
				<el-table :data="bannerList" header-row-class-name="header-row" border class="tr-header" max-height="550">
					<el-table-column label="轮播图" :width="220" align="center">
						<template slot-scope="scope">
							<upload-small-img class="avatar-uploader"	:item = 'scope.row' :index = 'scope.$index' :isEdit = true	@getImgUrl = "getImgUrl"></upload-small-img>
						</template>
					</el-table-column>
					<el-table-column label="跳转路径" align="center" :width="320" >
					<template slot-scope="scope">
						<el-input class="input-url" v-model.trim="scope.row.jumpUrl"  placeholder="http://"></el-input>
					</template>
					</el-table-column>
					<el-table-column label="排序" align="center" prop="subSort" :width="120" >
						<template slot-scope="scope">
							<el-input v-model.trim="scope.row.sort" size="mini" type="number" min="0" style="width:80px"></el-input>
						</template>
					</el-table-column>
					<el-table-column label="操作" align="center" :width="100">
					<template slot-scope="scope">
						<el-button class="mini-delete-btn" @click="bannerDelete(scope.$index)">删除</el-button>
					</template>
					</el-table-column>
				</el-table>
		    </div>
		</div>

		<div class="recmmend-classify-btn">
			<el-button class="mini-search-btn" @click="saveFun()">保存</el-button>
			<el-button class="mini-search-btn" @click="cancel">返回</el-button>
		</div>

		
		

		    <!-- 指定分类的弹框 -->
			<el-dialog title="指定分类" :visible.sync="classifyVisible">
				<div class="editMobile-search">
					<el-input v-model.trim="classifyContent" size="mini" placeholder="分类牌名称" style="width:300px"></el-input>
					<el-button class="mini-search-btn" style="margin-left:30px;" @click="searchClassifyFun">搜索</el-button>
				</div>
				<el-table ref="multipleTable" :data="classifyTableData" row-key="id" border default-expand-all :tree-props="{children: 'children'}" @selection-change="classifySelectionChange">
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
	</div>
</template>
<script type="text/javascript">
import uploadSmallImg from "@/views/storeLayout/compomemts/uploadSmallImg"
import api from '@/api/allApi'
export default {
	name: 'addcategory',
	components: {uploadSmallImg},
	data() {
		return {
		   classifyVisible:false,
		   classifyPage:1,
		   classifyContent:'',
		   classifyTableList:[],
		   classifyTableData:[],
		   classifyTotal:0,
		   bannerList:[
			//    {
			// 	   imgUrl:'',
			// 	   jumpUrl:'',
			// 	   recommendFlag:1,  //是否推荐轮播图 0否 1是
			// 	   sort:'',
			//    }
		   ]
		}
	},
	
	created() {
		
	},
	mounted(){
		
       this.getClassifyListFun();
	   this.getRecommendInfoFun();
	},
	methods: {
		// 获取推荐分类
		getRecommendInfoFun(){
			let that=this;
			let params={}
            this.$http.getRecommendInfo(this,params).then(res=>{
				console.log('推荐分类：',res.data);

				this.classifyTableList=res.data.classifies?res.data.classifies:[];
				this.bannerList=res.data.classifyBanners?res.data.classifyBanners:[];
				if(res.data.classifyBanners&&res.data.classifyBanners.length>0){
                    this.bannerList.forEach(item=>{
						that.$set(item,'imageUrl',item.imgUrl);
						
					})
				}
				
			})
		},
		// 新增分类
		addClassifyVisibleFun(){
		   let that=this;
           this.classifyVisible=true
		   setTimeout(()=>{
              that.classifyTableData.forEach(item=>{
				if(item.recommendFlag==1){
                    that.$refs.multipleTable.toggleRowSelection(item)        
				}
				item.subClassifies.forEach(item2=>{
                    if(item2.recommendFlag==1){
                      that.$refs.multipleTable.toggleRowSelection(item2)        
				    }
				})
			    
      
		      })
		   },300)
		   
		},
		
		// 返回列表
		cancel() {
		  this.$router.push({name: 'categorylist'})
		},
	    // 获取分类数据
		getClassifyListFun(){
			let params={
				page:this.classifyPage,
				size:10,
				content:this.classifyContent,
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
		
		}, 
		// 删除选择的分类
		deleteClassify(id){
			this.classifyTableList=this.classifyTableList.filter(item=>item.id!=id)
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
					nameEn:item.nameEn,
                    appletName:item.appletName,
				}
				this.classifyList.push(objs);
			})
		},
		// 选择分类---确认
		choiceClassifyConfirmFun(){
			if(this.classifyList.length>0){
				this.classifyTableList=this.classifyList;
				this.classifyVisible=false;
			}else{
				this.$message('至少选择一个分类！');
			}
			
		},
		// 轮播图--添加
		addBanner(){
			if(this.bannerList.length<3){
               let obgj={
				   imgUrl:'',
				   jumpUrl:'',
				   recommendFlag:1,  //是否推荐轮播图 0否 1是
				   sort:'',
			   }
			   this.bannerList.push(obgj)
			}else{
				this.$message('最多只能添加三张')
			}
				
           
		},
        // 获取图片接口
		getImgUrl(index,url){
		    this.bannerList.forEach((item,indexs)=>{
               if(index==indexs){
                    this.$set(item,'imageUrl',url)
                    this.$set(item,'imgUrl',url)
			   }
			})

		},
		// 删除轮播图
		bannerDelete(num){
            this.bannerList=this.bannerList.filter((item,index)=>index!=num) 
		},

		// 保存
		saveFun(){
			let classifyIds=[];
			if(this.classifyTableList.length>0){
				this.classifyTableList.forEach(item=>{
                  classifyIds.push(item.id) 
				})
                 
			}else{
				this.$message('请新增至少一个分类')
				return
			}
			let classifyBanners=[];
			if(this.bannerList.length>0){
				let flag=true;
                this.bannerList.forEach(item=>{
					if(item.imgUrl==''||item.sort==''){
                        flag=false;
					}
					classifyBanners.push(
						{
						imgUrl:item.imgUrl,
						jumpUrl:item.jumpUrl,
						recommendFlag:0,  //是否推荐轮播图 0否 1是
						sort:item.sort
						}
					)
			   })
			   if(!flag){
                   this.$message('请完善轮播图信息')
				   
				   return
			   }
			}else{
				this.$message('请新增至少一个baner图');
				return
			}
			
			let params={
				classifyBanners:classifyBanners,
				classifyIds:classifyIds,
			}
			this.$http.recommendInfo(this,params).then(res => {
				 console.log('推荐分类保存',res);
				 if(res.success){
                    this.$message('添加推荐分类成功');
					this.cancel()
				 }else{
					 this.$message('添加失败')
				 }
			})
		
		}
	}
}

</script>
<style lang="scss" >
  .recmmend-classify{
	  display: flex;
	   padding: 30px;
	  .recmmend-classify-title{
          margin-top: 5px;
		  font-size: 14px;
		  width: 100px;
		  text-align: right;
	  }
	  .recmmend-classify-right{
          .recmmend-classify-add{
			  margin-bottom: 15px;
			  
			  span{
				  color: #999;
				  font-size: 12px;
				  margin-left: 15px;
			  }
		  }
	  }
  }
  .recmmend-classify-btn{
	  margin-left: 600px;
	  margin-top: 30px;
  }
</style>
