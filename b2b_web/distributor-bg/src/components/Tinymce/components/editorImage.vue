<template>
  <div class="upload-container">
    <el-button class="mini-search-btn" icon='el-icon-upload' :style="{background:color,borderColor:color}" @click=" dialogVisible=true" type="primary">本地上传
    </el-button>
  <!-- <transition name="fade"> -->
    <el-dialog :modal-append-to-body="false" :visible.sync="dialogVisible">
      <el-upload class="editor-slide-upload" :action="action" :multiple="true" :file-list="fileList" :show-file-list="true"
        list-type="picture-card" :on-remove="handleRemove" :on-success="handleSuccess" :before-upload="beforeUpload">
        <img v-if="fileImage" :src="fileImage" class="avatar">
        <!-- <i v-else class="el-icon-plus avatar-uploader-icon"></i> -->
        <el-button v-else class="mini-search-btn">点击上传</el-button>
      </el-upload>
      <el-button class="mini-search-btn" @click="dialogVisible = false">取 消</el-button>
      <el-button class="mini-search-btn" @click="handleSubmit" :loading="loading">确 定</el-button>
    </el-dialog>
  <!-- </transition> -->
  </div>
</template>

<script>
import {monthDay} from '@/utils/timeFormat.js'
export default {
  name: 'editorSlideUpload',
  props: {
    color: {
      type: String,
      default: '#1890ff'
    }
  },
  data() {
    return {
      action: process.env.BASE_API + 'system/v1/web/admin/oss/sts',
      dialogVisible: false,
      listObj: {},
      fileList: [],
      client:{},
      loading:false,
      fileImage: ''
    }
  },
  methods: {
    checkAllSuccess() {
      return Object.keys(this.listObj).every(item => this.listObj[item].hasSuccess)
    },
    handleSubmit() {
      this.dialogVisible = false
      const arr = Object.keys(this.listObj).map(v => this.listObj[v])
      if (!this.checkAllSuccess()) {
        this.$message('请等待所有图片上传成功 或 出现了网络问题，请刷新页面重新上传！')
        return
      }
      this.listObj = {}
      this.fileList = []
      this.successCBK(arr)
    },
    successCBK(arr){
      this.$emit('successCBK', arr)
    },
    handleSuccess(response, file) {
      const uid = file.uid
      const objKeyArr = Object.keys(this.listObj)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObj[objKeyArr[i]].uid === uid) {
          this.listObj[objKeyArr[i]].hasSuccess = true
          return
        }
      }
    },
    handleRemove(file) {
      const uid = file.uid
      const objKeyArr = Object.keys(this.listObj)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObj[objKeyArr[i]].uid === uid) {
          delete this.listObj[objKeyArr[i]]
          return
        }
      }
    },
    beforeUpload(file) {
      if(file.type != 'image/jpeg' && file.type != 'image/bmp' && 
      file.type != 'image/jpg' && file.type != 'image/jpeg' && 
      file.type != 'image/png' && file.type != 'image/gif'){
        this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
        return false
      }
      const _self = this
      const fileName = file.uid
      this.listObj[fileName] = {}
      // 随机命名
      let random_name = _self.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
      _self.$http.getFileSts(this).then(result => {
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
            // 上传完成
            this.listObj[fileName].url = result.data.hostname + results.name;
            this.fileImage = result.data.hostname + results.name;
            return new Promise((resolve, reject) => {
              _self.listObj[fileName] = { hasSuccess: true, uid: file.uid, width: this.width, height: this.height,url: result.data.hostname + results.name}
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

<style rel="stylesheet/scss" lang="scss" scoped>
.upload-container {
  .editor-slide-upload {
    margin-bottom: 20px;
  }
  .el-upload{
    .avatar{
      margin:0 auto;
      height: 100%;
      width:auto;
    }
  }
}
</style>
