<template>
  <div class="add-train">
    <header class="header">
      <h4 class="title">{{title}}</h4>
      <el-button class="mini-add-btn btn-right" icon="el-icon-back" @click="cancel">返回列表</el-button>
    </header>
    <div v-loading="loading">
      <el-form ref="form" :model="formData" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item label="一级菜单：" prop="fid">
          <el-select v-model="formData.fid" placeholder="一级菜单"  size="mini" @change="handleSelectFirst">
            <el-option
            v-show="item.titleZh || item.titleEn"
            v-for="item in firstMenu"
            :key="item.id"
            :label="item.titleZh ? item.titleZh: item.titleEn"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="二级菜单：" prop="parentId">
          <el-select v-model="formData.parentId" placeholder="二级菜单" :disabled="disSmenu"  size="mini" @change="handleSelectSecond">
            <el-option
            v-show="item.titleZh || item.titleEn"
            v-for="item in secondMenu"
            :key="item.id"
            :label="item.titleZh ? item.titleZh: item.titleEn"
            :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题中文名称：" prop="titleZh">
          <el-input v-model.trim="formData.titleZh"></el-input>
        </el-form-item>
        <el-form-item label="标题英文名称：" prop="titleEn">
          <el-input v-model="formData.titleEn"></el-input>
        </el-form-item>
        <el-form-item label="缩略图(中文)：" prop="thumbnailUrlZh" style="margin-bottom: 10px;">
          <template>
            <el-upload
              class="avatar-uploader"
              :action="action"
              :show-file-list="false"
              :before-upload="beforeUploadThumbnailZh">
              <img v-if="formData.thumbnailUrlZh" :src="formData.thumbnailUrlZh" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <span class="img-size">图片建议尺寸（386*200）</span>
          </template>
        </el-form-item>
        <el-form-item label="缩略图(英文)：" prop="thumbnailUrlEn" style="margin-bottom: 10px;">
          <template>
            <el-upload
              class="avatar-uploader"
              :action="action"
              :show-file-list="false"
              :before-upload="beforeUploadThumbnailEn">
              <img v-if="formData.thumbnailUrlEn" :src="formData.thumbnailUrlEn" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <span class="img-size">图片建议尺寸（386*200）</span>
          </template>
        </el-form-item>
        <el-form-item label="国内视频链接：" prop="contentUrlZh">
            <el-input v-model.trim="formData.contentUrlZh"></el-input>
        </el-form-item>
        <el-form-item label="国外视频链接：" prop="contentUrlEn">
          <el-input v-model="formData.contentUrlEn"></el-input>
        </el-form-item>
      </el-form>
     
      <div class="clearfix footbtn">
        <el-col :span="1" :offset="12">
          <el-button class="mini-search-btn" @click="handleSave('form')" >保存</el-button>
        </el-col>
      </div>
    </div>
  </div>
</template>

<script>
import {monthDay} from '@/utils/timeFormat.js'
export default {
  data() {
    var validateTitleZh = (rule, value, callback) => {
      if (value === '' && this.formData.titleEn === '') {
        callback(new Error('请输入中文标题'))
      }
    }
    var validateTitleEn = (rule, value, callback) => {
      if (value === '' && this.formData.titleZh === '') {
        callback(new Error('请输入英文标题'))
      }
    }
    return {
      loading: false,
      id: null,
      title: '',
      action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
      formData: {},
      rules: {
        fid: [{
          required: true,
          message: '请选择一级菜单',
          trigger: 'blur'
        }],
        parentId: [{
          required: true,
          message: '请选择二级菜单',
          trigger: 'blur'
        }],
        titleZh: [{
          required: false,
          // validator: validateTitleZh,
          // message: '请输入标题中文名称',
          trigger: 'blur'
        }],
        titleEn: [{
          required: false,
          // message: '请输入标题英文名称',
          // validator: validateTitleEn,
          trigger: 'blur'
        }],
        contentUrlZh: [{
          required: false,
          message: '请输入国内视频链接',
          trigger: 'blur'
        }],
        contentUrlEn: [{
          required: false,
          message: '请输入国外视频链接',
          trigger: 'blur'
        }]
      },
      firstMenu: [],
      secondMenu: [],
      selectFid: null,
      disSmenu: false
    }
  },
  created() {
    this.loading = true
    // 获取菜单数据列表
    this.id = this.$route.params.id
    if (this.id) {
      // 编辑
      this.title = '编辑'
      this.getFMenu(0).then(res => {
        if(res.success) {
          this.getDataById(this.id)
        }
      })
    } else {
      this.getFMenu(0)
      // 添加
      this.title = '添加'
      this.formData = {
        fid: null,
        parentId: null,
        titleZh: '',
        titleEn: '',
        contentUrlZh: '',
        contentUrlEn: '',
        thumbnailUrlZh: '',
        thumbnailUrlEn: ''
      }
    }
  },
  methods: {
    // 获取一级菜单列表
    getFMenu(id) {
      return new Promise((resolve, reject) => {
        this.$http.trainingPoList(this, {    
          parentId: id
        }).then(res => {
        if (res.success) {
          let fid = this.$route.params.fid
          this.firstMenu = res.data
          if(this.firstMenu.length>0) {
            this.selectFid = fid ? fid : this.firstMenu[0].id
            this.formData.fid = this.selectFid
            this.getSMenu(this.selectFid).then(res2 => {
              if (res2.success) {
                resolve(res2)
              } else {
                reject(res2)
              }
            })
          } else {
            this.loading = false
            this.$message.error('请先添加分类列表')
          }
        } else {
          this.loading = false
          this.$message.error(res.msg)
          reject()
        }
      })
      })
    },
    // 获取二级菜单列表
    getSMenu(id) {
      return new Promise((resolve, reject) => {
        this.$http.trainingPoList(this, {
          parentId: id
        }).then(res => {
          if (res.success) {
            this.disSmenu = false
            this.secondMenu = res.data
            if (this.secondMenu === undefined || this.secondMenu === null || this.secondMenu.length === 0) {
              this.formData.parentId = null
              this.secondMenu = []
              this.$message.warning('暂无数据')
              reject()
            } else {
              this.loading = false
              this.formData.parentId = this.secondMenu[0].id
              resolve(res)
            }
          } else {
            this.loading = false
            reject()
          }
        })
      })
    },
    // 根据Id获取数据信息
    getDataById(id) {
      return new Promise((resolve, reject) => {
        this.$http.trainingDetail(this, {    
          id: id
        }).then(res => {
          if (res.success) {
            this.loading = false
            this.formData = res.data
            this.formData.fid = this.selectFid
            resolve()
          } else {
            this.loading = false
            reject()
          }
        })
      })
    },
    // 保存
    handleSave() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          // if(this.formData.titleZh === '' && this.formData.titleEn === '') {
          //   this.$message.warning('请输入标题名称')
          //   return false
          // } else {
            if (this.id) {
              // 更新
              this.updateTrain()
            } else {
              // 添加
              this.addTrain()
            }
          // }
        } else {
          return false;
        }
      })
    },
    // 添加
    addTrain() {
      this.$http.addTraining(this, this.formData).then(res => {  
        if (res.success) {
          this.$router.push({ name: 'trainList' })
          this.$message.success('添加成功')
        }
      })
    },
    // 更新
    updateTrain() {
      this.$http.editTraining(this, {    
        id: this.id,
        parentId: this.formData.parentId,
        titleZh: this.formData.titleZh,
        titleEn: this.formData.titleEn,
        contentUrlZh: this.formData.contentUrlZh,
        contentUrlEn: this.formData.contentUrlEn,
        thumbnailUrlZh: this.formData.thumbnailUrlZh,
        thumbnailUrlEn: this.formData.thumbnailUrlEn
       }).then(res => {
          if(res.success) {
            this.$router.push({ name: 'trainList' })
            this.$message.success('更新成功')
          }
      })
    },
    handleSelectFirst(val) {
      this.disSmenu = true
      this.getSMenu(val)
    },
    handleSelectSecond(val) {
      this.formData.parentId = val
    },
    // 中文缩略图上传
    beforeUploadThumbnailZh(file) {
      this.upload(file, 1)
    },
    // 英文缩略图上传
    beforeUploadThumbnailEn(file) {
      this.upload(file, 2)
    },
    upload(file, type) {
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
              if (type === 1) {
                // 中文
                this.formData.thumbnailUrlZh = result.data.hostname + results.name
              } else {
                this.formData.thumbnailUrlEn = result.data.hostname + results.name
              }
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
    },
    // 返回列表
    cancel() {
      this.$router.push({name: 'trainList'})
    }
  }
}
</script>

<style lang="scss">
 .add-train{
    .header{
      @extend .header
    }
    .avatar-uploader {
      display: inline-block;
    }
    .img-size{
      display: inline-block;
      height:45px;
      margin-left:20px;
      vertical-align: bottom;
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
  }
</style>