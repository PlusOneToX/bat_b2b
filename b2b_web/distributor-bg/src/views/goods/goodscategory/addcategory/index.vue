<template>
	<div class="add-category">
		<header >
		  <h4>商品分类</h4>
		  <el-button class="btn-home" icon="el-icon-d-arrow-left" @click="cancel">
			返回商品分类列表
		  </el-button>
		</header>
		<el-form label-width="150px" :rules="rules" ref="formData" :model="formData" v-loading="loading">
			<el-row>
				<el-col :span="10">
					<el-form-item label="分类名称" prop="name">
						<el-input v-model="formData.name" placeholder="不超过40个字" maxlength="40">
						</el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="10">
				<el-form-item label="分类英文名称" prop="nameEn">
					<el-input v-model="formData.nameEn" placeholder="不超过80个字" maxlength="80">
					</el-input>
				</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="10">
				<el-form-item label="小程序分类名称" prop="appletName">
					<el-input v-model="formData.appletName" placeholder="不超过5个字" maxlength="5">
					</el-input>
				</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="8">
					<el-form-item label="上级分类" prop="parentId">
						<el-select v-model="formData.parentId">
							<!-- <el-option :value="1" label="顶级分类" v-if="formData.parentId==1"></el-option> -->
							<el-option :value="parentOption.parentId" :label="parentOption.name"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="10">
				<el-form-item label="是否推荐分类" prop="recommendFlag">
					<el-radio-group v-model="formData.recommendFlag">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="8">
					<el-form-item label="排序" prop="sort">
						<el-input v-model="formData.sort" placeholder="不超过100000" type="number" step="1" min="0" max="100000" @keyup.native="proving"> </el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="8">
				<el-form-item label="中文图片" prop="imageUrl">
					<el-upload
					class="avatar-uploader"
					:action="action"
					:show-file-list="false"
					:before-upload="beforeUploadOriginImage">
					<img v-if="formData.imageUrl" :src="formData.imageUrl" class="avatar">
					<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				</el-col>
			</el-row>
			
			<el-row>
				<el-col :span="8">
				<el-form-item label="英文图片" prop="imageUrlEn">
					<el-upload
					class="avatar-uploader"
					:action="action"
					:show-file-list="false"
					:before-upload="beforeUploadOriginImageEn">
					<img v-if="formData.imageUrlEn" :src="formData.imageUrlEn" class="avatar">
					<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="18" >
					<!-- 轮播图 -->
					<div class="addcategory-classify">
						<div class="addcategory-classify-title">轮播图：</div>
						<div class="addcategory-classify-right">
							<div class="addcategory-classify-add"><button class="mini-batch-btn" @click="addBanner">新增</button><span>最多三张</span></div>
							<el-table :data="formData.banners" header-row-class-name="header-row" border class="tr-header" max-height="550">
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
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="18">
					<el-form-item label="分类描述" prop="description">
						<el-input placeholder="不超过200个字" maxlength="200" type="textarea" :rows="5" v-model="formData.description">
						</el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="2" :offset="9">
					<el-button class="mini-search-btn" @click="handleSubmit('formData')" :loading="loading">保存</el-button>
				</el-col>
				<el-col :span="2" :offset="1">
					<el-button class="mini-search-btn" @click="cancel">返回</el-button>
				</el-col>
			</el-row>
		</el-form>
	</div>
</template>
<script type="text/javascript">
import { isNumber } from "@/utils/validate";
import api from '@/api/allApi'
import uploadSmallImg from "@/views/storeLayout/compomemts/uploadSmallImg"
import {monthDay} from '@/utils/timeFormat.js'
export default {
	name: 'addcategory',
	components: {uploadSmallImg},
	data() {
		const validateNumber = (rule, value, callback) => {
			if(!isNumber(value)) {
				callback(new Error('只能输入数字'))
			} else {
				callback()
			}
		}
		return {
            action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
			loading: false,
			parentId:0,
			id:'',
			formData: {
				name: '',
                nameEn: '',
				parentId: 0,
				sort: 0,
				imageUrl: '',
				imageUrlEn: '',
				description: '',
				openFlag: 0,
				recommendFlag:1,  //是否推荐分类 0否 1是
				appletName:'',  //小程序分类名称
				banners:[],
			},
			rules: {
				name: [{required: true,message: '请输入分类名称',trigger: 'blur'}],
				nameEn: [{required: true,message: '请输入分类英文名称',trigger: 'blur'}],
				appletName: [{required: true,message: '请小程序分类名称',trigger: 'blur'}],
				recommendFlag:[{required: true,message: '请选择分类',trigger: 'blur'}],
				sort: [{validator: validateNumber,}],
				imageUrl:[{required: true,message: '请中文原图',trigger: 'blur'}],
				imageUrlEn:[{required: true,message: '请英文原图',trigger: 'blur'}]
			},
			parentOption: {
				parentId: 0,
				name: '顶级分类'
			}
		}
	},
	created() {
		this.getParams();
	},
	activated() {
	 this.getParams();
  	},
	methods: {
		// 轮播图--添加
		addBanner(){
			console.log('--:',this.formData);
			if(this.formData.banners.length<3){
               let obgj={
				   imgUrl:'',
				   jumpUrl:'',
				   sort:'',
			   }
			   this.formData.banners.push(obgj)
			}else{
				this.$message('最多只能添加三张')
			}  
		},
		// 获取图片接口
		getImgUrl(index,url){
		    this.formData.banners.forEach((item,indexs)=>{
               if(index==indexs){
                    this.$set(item,'imageUrl',url)
                    this.$set(item,'imgUrl',url)
			   }
			})

		},
		// 删除轮播图
		bannerDelete(num){
            this.formData.banners=this.formData.banners.filter((item,index)=>index!=num) 
		},

		proving(){
			this.formData.sort=this.formData.sort.replace(/[^\.\d]/g,'');
			this.formData.sort=this.formData.sort.replace('.','');
			if(Number(this.formData.sort) >100000){
				this.formData.sort = 100000
			}
		},
		getParams(){
			this.parentId = this.$route.query.parentId
			this.id = this.$route.query.id
			if(this.parentId != undefined && this.parentId !== 0 && this.parentId != "0") {
				this.$http.getClassifyDetail(this, { id: this.parentId }).then(res => {
					this.parentOption = {
						parentId: res.data.id,
						name: res.data.name
					}
				})
			}
			if(this.id == '' || this.id == undefined) {
				if(this.parentId === undefined){
					this.parentOption = {
						parentId: 0,
						name: '顶级分类'
					}
					this.formData.parentId = 0
				}else{
					this.formData.parentId = this.parentId
				}
				this.formData.name = ''
				this.formData.sort = 0
				this.formData.description = ''
				
			} else {
				this.loading = true
				this.$http.getClassifyDetail(this, { id: this.id }).then(res => {
					if (res.success) {
						res.data.sort = Number(res.data.sort)
						res.data.parentId = Number(res.data.parentId)
						this.formData.parentId = Number(res.data.parentId)
						if(res.data.imageUrl === undefined || res.data.imageUrlEn === undefined){
							res.data.imageUrl = ''
							res.data.imageUrlEn = ''
						}
						
						this.formData = res.data;
						this.formData.banners.forEach(item=>{
							this.$set(item,'imageUrl',item.imgUrl)
						})
					}
					this.loading = false
				})
			}
		},
		verify1(formData) {  // 验证只能输入数字（排序）
			this.formData.sort = this.formData.sort.replace(/\D/g,'')
		},
		cancel() { // 返回操作
			this.$router.push({name:'categorylist'})
		},
		handleSubmit(data) {
			this.$refs[data].validate(valid => {
				if(valid) {
					
					if(this.formData.banners.length>0){
						let bannerFlag=true;
                        this.formData.banners.forEach(item=>{
							if(item.sort==''||item.imgUrl==''){
                               bannerFlag=false
							}
						})
						if(!bannerFlag){
                            this.$message('请完善轮播图信息');
							return
						}
						
                        
					}
					this.loading = true;
					if(this.id == '' || this.id == undefined) {
						this.$http.addClassify(this, this.formData).then(res => {
							this.loading = false
							if (res.success) {
								this.$message.success({
									message: '添加成功',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.cancel()
							}
						})
					} else {
						this.formData.id = this.id
						this.$http.editClassify(this, this.formData).then(res => {
							this.loading = false
							if (res.success) {
								this.$message.success({
									message: '修改成功',
									duration: 3 * 1000,
									onClose: () => { }
								})
								this.cancel()
							}
						})
					}
				} else {
					return false;
				}
			})
		},
    beforeUploadOriginImage(file) {
      if(file.type != 'image/jpeg' && file.type != 'image/bmp' &&
        file.type != 'image/jpg' && file.type != 'image/jpeg' &&
        file.type != 'image/png' && file.type != 'image/gif'){
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
			this.$http.getFileSts(this).then((result) => {	
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure:true
        })
        // 上传
        client.multipartUpload('goods/' + monthDay(new Date()) + '/' + random_name, file, {
        }).then((results) => {
          return new Promise((resolve, reject) => {
            this.formData.imageUrl = result.data.hostname + results.name,
              resolve(true)
          })
        }).catch((err) => {
          console.log(err)
        })
      })
    },
    beforeUploadOriginImageEn(file) {
      if(file.type != 'image/jpeg' && file.type != 'image/bmp' &&
        file.type != 'image/jpg' && file.type != 'image/jpeg' &&
        file.type != 'image/png' && file.type != 'image/gif'){
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
			this.$http.getFileSts(this).then((result) => {		
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure:true
        })
        // 上传
        client.multipartUpload('goods/' + monthDay(new Date()) + '/' + random_name, file, {
        }).then((results) => {
          return new Promise((resolve, reject) => {
            this.formData.imageUrlEn = result.data.hostname + results.name,
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
      for(let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    }
	}
}

</script>
<style lang="scss" >
.addcategory-classify{
	  display: flex;
	   padding: 30px;
	  .addcategory-classify-title{
          margin-top: 5px;
		  font-size: 14px;
		  width: 120px;
		  text-align: right;
	  }
	  .addcategory-classify-right{
          .addcategory-classify-add{
			  margin-bottom: 15px;
			  
			  span{
				  color: #999;
				  font-size: 12px;
				  margin-left: 15px;
			  }
		  }
	  }
  }
.add-category {
	background-color: white;
	padding-bottom: 20px;
	header {
	  color: white;
	  height: $lineHight;
	  line-height: $lineHight;
	  background-color: $lakeBlue;
	  margin-bottom: 20px;
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
  .avatar-uploader{
    .el-upload{
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      img{
        display: block;
        width:120px;
        height: 120px;
      }
    }
    .avatar-uploader-icon{
      display: inline-block;
      width:120px;
      height: 120px;
      line-height: 120px;
      text-align: center;
      font-size: 28px;
      color: #8c939d;
    }
  }
}

</style>
