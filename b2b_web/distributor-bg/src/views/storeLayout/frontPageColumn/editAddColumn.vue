<template>
  <div class="section-add-edit" @click="cancelB(false)">
    <header>
      <h4>首页栏目</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
        返回首页栏目
      </el-button>
    </header>
    <div v-loading="loading">
      <el-form ref="formData" :model="formData" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item label="版本：" prop="columnArea">
          <el-radio v-model="formData.columnArea" :label="0">国内</el-radio>
          <el-radio v-model="formData.columnArea" :label="1">海外</el-radio>
          <el-radio v-model="formData.columnArea" :label="2">国内和海外</el-radio>
        </el-form-item>
        <el-form-item label="栏目标题：" prop="title">
            <el-input v-model.trim="formData.title"></el-input>
        </el-form-item>
        <el-form-item label="栏目英文标题：" prop="titleEn">
          <el-input v-model="formData.titleEn"></el-input>
        </el-form-item>

        <el-form-item label="序号：" prop="sort" label-width="150px">
          <el-input v-model="formData.sort" type="number" min="0" style="width:80px" @keyup.native="proving" />
          <span class="place-holder">注意：序号越小，排在前面</span>
        </el-form-item>

        <el-form-item label="是否发布：">
          <el-radio-group v-model="formData.releaseStatus">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="指定商品：">
          <el-button class="mini-search-btn" @click="goodsShow=true">添加商品</el-button>
          <!-- <el-button class="mini-search-btn" @click.stop="cancelB(true)">导入活动</el-button> -->
          <el-button class="mini-freeze-btn" v-if="columnId" :loading="importGoodsLoading" @click.stop="importGoods()">一键导入清仓商品</el-button>
          <el-button class="mini-delete-btn" @click.stop="clearGoods()">一键清除</el-button>
          <!-- <transition name="el-zoom-in-top">
              <div class="promotion-box" v-if="promotionShow">
                  <el-tree :data='promotionlist' ref="tree" node-key="id" :default-checked-keys="promotionIds" @node-click="handleNodeClick"></el-tree>
                  <el-col :span="4" :offset="10">
                      <el-button class="mini-search-btn" id='cate-click'  @click="getCheckedNodes" @click.stop="cancelB(false)">确定</el-button>
                  </el-col>
              </div>
          </transition> -->
        </el-form-item>
        <el-form-item>
          <el-row style="width:700px">
            <el-table :data="goods" border header-row-class-name="header-row" class="goods-table" height="300" >
              <el-table-column  label="商品编号" prop="goodsNo" align="center"> </el-table-column>
              <el-table-column  label="商品名称" prop="goodsName" align="center"> </el-table-column>
              <el-table-column align="center" label="操作" width="220">
                <template slot-scope="scope">
                  <el-button class="mini-tableadd-btn" @click="handleUp(scope.$index,scope.row)">上移</el-button>
                  <el-button class="mini-freeze-btn" @click="handleDown(scope.$index,scope.row)">下移</el-button>
                  <el-button class="mini-delete-btn" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
        </el-form-item>
        <div class="place-holder place-holder-danger">*商品名称、商品分类默认显示为中文名称，海外版新增的栏目则前端页面会自动显示为英文商品名称及商品分类名称。</div>
        <el-form-item label="banner图片：" prop="bannerImg" style="margin-bottom: 10px;">
          <template>
            <el-upload
              class="avatar-uploader"
              :action="action"
              :show-file-list="false"
              :before-upload="beforeUpload">
              <img v-if="formData.bannerImg" :src="formData.bannerImg" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <!-- <span class="img-size">图片建议尺寸（386*200）</span> -->
          </template>
        </el-form-item>
        <!-- 可视范围-->
        <storedistributor ref="distributor" 
          :key="formData.id" 
          :type="formData.distributorScope" 
          :gIds="formData.gradeIds" 
          :dIds="formData.distributorIds" 
          :departmentIds="formData.departmentIds" 
          :adminIds="formData.userIds" 
          @change="getChange"
          ></storedistributor>
      </el-form>
      <div class="clearfix footbtn">
        <el-col :span="1" :offset="9">
          <el-button class="mini-search-btn" @click="handleSave('formData')">保存</el-button>
        </el-col>
      </div>
      <!-- 指定商品"-->
      <el-dialog :modal-append-to-body="false" :visible="goodsShow" width="80%" :before-close="closeDialog">
        <select-goods ref="selectGoods" 
          :selectGoodsData="goods"
          :saleStatus = "3"
          :isNew = "isNew"
          @submit="submit"
          @cancel="goodsShow=false" 
          ></select-goods>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import selectGoods from "@/views/goods/components/selectGoods"
import { swapItem } from "@/utils/index";
import storedistributor from "@/views/storeLayout/compomemts/storeDistributor"
import {monthDay} from '@/utils/timeFormat.js'
  export default {
    data() {
      return {
        loading: true,
        importGoodsLoading: false,
        promotionShow: false,
        goodsShow: false,
        columnId: this.$route.params.id,
        distributorData: [],
        count:0,
        goodsAddIds: [],  // 添加商品
        goodsDelIds: [], // 删除商品
        goodsUpIds: [], // 更新商品
        originGoods: [], // 初始数据
        formData: {
          columnArea: 2,
          title: '',
          titleEn: '',
          releaseStatus: 1,
          sort: 0,
          goodsIds:[],
          bannerImg: '',
          distributorScope: 1,
          gradeIds: [],
          distributorIds: [],
          departmentIds: [],
          userIds: []
        },
        rules: {
          columnArea: [{
            required: true,
            message: '请选择版本',
            trigger: 'blur'
          }],
          title: [{
              required: true,
              message: '请输入栏目标题',
              trigger: 'blur'
          }],
          titleEn: [{
            required: true,
            message: '请输入栏目英文标题',
            trigger: 'blur'
          }],
          bannerImg: [{
            required: true,
            message: '请选择banner图片',
            trigger: 'blur'
          }]
        },
        maxFiles: {
          type: Number,
          default: 1,
        },
        percentage: [],
        goods:[],
        promotionIds: [],
        promotion: [],
        isEdit: true,
        promotionlist: [],
        isNew: true,
        action: process.env.BASE_API +'system/v1/web/admin/oss/sts'
      }
    },
    components: {
      selectGoods,
      storedistributor
    },
    created() {
      // this.getParams();
      // getPromotionList(this).then(res => {
      //     this.promotionlist = res;
      // })
      // 促销活动列表
      // this.$http.promotionList(this, {page:1, size:10, applyStatus: 2}).then(res => {
      //   if (res.success) {
      //     this.promotionlist = res;
      //   }
      // })
    },
    mounted() {
      this.$nextTick(() => {
        this.getParams();
        this.isNew = true;
      })
    },
    methods: {
      importGoods(){
        this.importGoodsLoading = true
        this.$http.clearanceGoodsStoreColumn(this, {id: this.columnId}).then(res => {
          if (res.success) {
            this.importGoodsLoading = false
            if (res.data && res.data.length > 0) {
              this.goods = res.data
              this.setArr(this.goods)
            } else {
              this.$message.error('暂无清仓商品')
            }
          }
        })
      },
      clearGoods(){
        this.goods.forEach(item => {
          this.goodsDelIds.push({
            sectionId: this.formData.id,
            goodsId: item.id,
            sort:1,
            operationType: 3
          })
        })
        this.goods.splice(0,this.goods.length);
      },
      getChange(val) { //..可视范围选中数据
        this.formData.distributorScope = val.distributorScope; //..可视范围
        this.formData.gradeIds = val.gradeIds; //..指定分销商等级
        this.formData.departmentIds = val.departmentIds // 指定部门
        this.formData.userIds = val.distributorAdminIds // 指定业务员
        this.formData.distributorIds = val.distributorIds; //..指定分销商用户ID
        this.distributorData = val.distributorData;
        if(this.count !== 0){
          this.count--
        }
      },
      proving(){
        this.formData.sort=this.formData.sort.replace(/[^\.\d]/g,'');
        this.formData.sort=this.formData.sort.replace('.','');
      },
      cancelB(b){
        this.promotionShow = b;
        this.promotionIds = [];
      },
      // handleNodeClick(raw) {
      //   let goodsIds = "";
      //   getPromotionGoods(this, { ids: raw.id }).then(res => {
      //       if(res.goodsIds.length != 0){
      //           goodsIds = res.goodsIds.join(",");
      //           // ids获取商品
      //           this.$http.columnListByIds(this, {page:1,size:10000,columnId:goodsIds}).then(res => {
      //             if (res.success) {
      //                 this.goods = res.data.list
      //                 this.setArr(this.goods);
      //             }
      //           })
      //       }
      //   })
      // },
      // 保存
      handleSave(data){
        this.$refs[data].validate(valid => {
          if (valid) {
            if(this.formData.distributorScope === 1){
              this.formData.gradeIds = undefined
              this.formData.distributorIds = undefined
              this.formData.departmentIds = undefined
              this.formData.userIds = undefined
            }
            this.count = 4
            this.formData.gradeIds = this.formData.gradeIds !== undefined && this.formData.gradeIds !== null ? this.formData.gradeIds: undefined
            if(this.formData.distributorScope === 2 && (this.formData.gradeIds === undefined || this.formData.gradeIds.length === 0)){
              this.$message.error("请先指定分销商等级范围")
              this.loading = false
              return
            }else if(this.formData.distributorScope === 2){
              this.formData.distributorIds = undefined
              this.formData.departmentIds = undefined
              this.formData.userIds = undefined
            }
            this.formData.departmentIds = this.formData.departmentIds !== undefined && this.formData.departmentIds !== null ? this.formData.departmentIds : undefined
            if(this.formData.distributorScope === 4 && (this.formData.departmentIds === undefined || this.formData.departmentIds.length === 0)){
              this.$message.error("请先指定销售部门范围")
              this.loading = false
              return
            }else if(this.formData.distributorScope === 4){
              this.formData.gradeIds = undefined
              this.formData.distributorIds = undefined
              this.formData.userIds = undefined
            }
            this.formData.userIds = this.formData.userIds !== undefined && this.formData.userIds !== null ? this.formData.userIds : undefined
            if(this.formData.distributorScope === 5 && (this.formData.userIds === undefined || this.formData.userIds.length === 0)){
              this.$message.error("请先指定业务员范围")
              this.loading = false
              return
            }else if(this.formData.distributorScope === 5){
              this.formData.gradeIds = undefined
              this.formData.distributorIds = undefined
              this.formData.departmentIds = undefined
            }
            if(this.formData.distributorScope === 6 && (this.formData.distributorIds === undefined || this.formData.distributorIds.length === 0)){
              this.$message.error("请先指定分销商范围")
              this.loading = false
              return
            }else if(this.formData.distributorScope === 6){
              this.formData.distributorIds = []
              for(let i = 0; i<this.distributorData.length;i++){
                  this.formData.distributorIds.push(this.distributorData[i].id)
              }
              this.formData.gradeIds = undefined
              this.formData.departmentIds = undefined
              this.formData.userIds = undefined
            }
            if(this.formData.title == undefined || this.formData.title.trim == ''){
              this.$message('栏目标题不能为空！');
              return
            }
            if(this.goods.length === 0){
              this.$message('至少要有一个指定商品！');
              return
            }
           
            if(this.formData.id != undefined){
              // 修改
              this.goods.map((item, index) => { // 排序
                item.sort = index + 1
              })
              this.goodsAddIds = []
              this.goodsUpIds = []
              // 过滤添加和修改商品
              this.filterGoods(this.goods, this.originGoods)

              this.$http.editColumn(this, this.formData).then(res => {    
                if(res.success) {
                  this.$message({
                    message: '保存成功',
                    type: 'success',
                    duration: 3 * 1000,
                    onClose: () => { }
                  })
                  this.cancel()
                }
              })
            }else{
              // 添加
              this.formData.goodsIds = []
              for(let i = 0; i<this.goods.length;i++){
                this.formData.goodsIds.push({
                  goodsId: this.goods[i].id,
                  sort: i + 1,
                  operationType: 1 // 添加
                })
              }
              this.$http.addColumn(this, this.formData).then(res => {  
                if(res.success) {
                  this.$message({
                    message: '保存成功',
                    type: 'success',
                    duration: 3 * 1000,
                    onClose: () => { }
                  })
                  this.cancel()
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
                    sectionId: this.formData.id,
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
              sectionId: this.formData.id,
              goodsId:va.id,
              sort: va.sort,
              operationType: 1
            })
          }
        }
        this.formData.goodsIds = this.goodsAddIds.concat(this.goodsDelIds.concat(this.goodsUpIds))
      },
      getCheckedNodes() {
        let ary1 = [];
        let ary2 = [];
        this.$refs.tree.getCheckedNodes().forEach(item => {
            ary1.push(item.id)
            ary2.push(item.label)
        })
        this.promotionName = ary2.join(',')
        this.promotionIds = ary1
        this.promotionShow = false
      },
      cancel(){
        this.$router.push({name: 'frontPageColumn'})
      },
      closeDialog() {
        this.$refs.selectGoods.handleCancel()
      },
      submit(val) {
        this.goods = []
        this.goods = this.goods.concat(val);
        this.setArr(this.goods);
        this.goodsShow = false;
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
        }
        this.goods = temp;
      },
      handleUp(index, row) {
        if(this.goods.length > 1 && index !== 0) {
            this.goods = swapItem(this.goods, index, index - 1)　　
        }
      },
      handleDown(index, row) {
        if(this.goods.length > 1 && index !== (this.goods.length - 1)) {
            this.goods = swapItem(this.goods, index, index + 1)　　
        }
      },
      handleDelete(index, row) {
        this.goods.splice(index, 1)
        // 删除商品
        this.originGoods.forEach(item => {
          if (item.id === row.id) {
            this.goodsDelIds.push({
              sectionId: this.formData.id,
              goodsId: item.id,
              sort:1,
              operationType: 3
            })
          }
        })
      },
      getParams(){
        this.goods = []
        if(this.$route.params.id != undefined){
          this.loading = true;
          this.$http.columnDetail(this, {id: this.$route.params.id}).then(res => { 
            if(res.data.distributorScope === undefined || res.data.distributorScope === null){
              res.data.distributorScope = 1
            }
            this.formData = res.data
            this.goodsAddIds = []
            this.goodsDelIds = []
            this.$http.columnListByIds(this, {page:1,size:10000,columnId:this.$route.params.id}).then(res2 => {  
              if (res2.success) {
                  this.goods = res2.data.list
                  this.setArr(this.goods);
                  res2.data.list.forEach((item, index) => {
                    this.originGoods.push({
                      id: item.id,
                      sort: index + 1
                    })
                  })
              }
              res2.success ? this.loading = false : this.loading = false
            })
            if(this.formData.distributorScope === 1){
              this.count = 4
            }else{
              this.count = 5
            }
          })
        }else{
          this.count = 0
          this.loading = false
          this.formData.id = undefined
          this.formData.title= ''
          this.formData.titleEn= ''
          this.formData.bannerImg = ''
          this.formData.releaseStatus= 1
          this.formData.distributorScope= 1
          this.formData.sort= 0
          this.formData.gradeIds = undefined
          this.formData.distributorIds = undefined
          this.formData.departmentIds = undefined
          this.formData.userIds = undefined
        }
      },
      beforeUpload(file) {
        this.upload(file)
      },
      upload(file) {
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
          this.$http.getFileSts(this).then(result => { 	
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
            client.multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {
            }).then((results) => {
              return new Promise((resolve, reject) => {
                this.formData.bannerImg = result.data.hostname + results.name
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
    },
    watch:{
      promotions(val){
        this.formData.promotionIds=val.map(v=>{return v.promotionId})
        this.promotionShow=true;
        setTimeout(function(){
        document.getElementById('cate-click').click();
        },500)
      }
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell{
  overflow: hidden;
  white-space:nowrap;
}
.section-add-edit {
  background-color: white;
  min-height: 100%;
  width: 100%;
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
  .btn-home{
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
}
.el-form1{
    width: 700px;
    margin-top: 30px;
    padding-left: 20px;
}
.el-form2{
    margin-right: 100px;
    margin-top: 30px;
    padding-left: 20px;
}

.file{
    position: absolute;
    width: 290px;
    height: 40px;
    top: 0;
    opacity: 0;
    padding: 0px;
    filter: alpha(opacity=0);
    cursor: pointer
}
.file-name{
    top: 0;
    position: relative;
    display: inline-block;
    overflow: hidden;
}

.el-icon-document::before {
    content: url("/src/assets/images/attachment.png");
}
.footbtn {
    margin-top: 30px;
}
.place-holder {
    margin-left: 10px;
    color: #ccc;
    font-size: 14px;
}
.promotion-box {
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
.img-content{
      display: flex;
      margin-top: 15px;
      .img-uploader-is{
         display: inline;
        .avatar-uploader {
          margin-left: 20px;
          margin-bottom: 15px;
          background-color:#bfbfbf;
          border-radius: 5px;
          width: 300px;
          height: 160px;
          cursor: pointer;
          overflow: hidden;
          text-align: center;
          display: flex;
          justify-content: center;
          align-items: center;
        }
      }
      .img-url{
        display: flex;
        height: 30px;
        width: 70%;
        .input-hint{
          height: 30px;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 12px;
          font-weight: bold;
          width: 100px;
        }
        .input-url{
          height: 30px;
          font-size: 14px;
          margin-right: 15px;
          border-radius: 5px;
          border: 1px solid #ccc;
          font-weight: bold;
          width: 100%;
        }

      }
    }
.upload-image{
   border-radius: 5px;
   border: 1px solid #ccc;
   background-color: #f8f8f8;
   }
.place-holder-danger{
  width: 1000px;
  margin-left: 150px;
  margin-bottom:20px;
  color: #F56C6C;
}
.avatar-uploader {
  display: inline-block;
  /deep/.el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 160px;
      height: 160px;
      line-height: 160px;
      text-align: center;
    }
    .avatar {
      width: 160px;
      height: 160px;
      display: block;
    }
  }
}
.img-size{
  display: inline-block;
  height:45px;
  margin-left:20px;
  vertical-align: bottom;
}
</style>

