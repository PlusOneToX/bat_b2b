<template>
  <div class="index">
    <div class="oss_file">
      <el-button class="mini-search-btn" @click="handleUpload">{{title}}</el-button>
      <input type="file" v-show="show" :multiple="multiple" :id="id" @change="doUpload" />
      <span slot="tip" class="place-holder">（图片建议尺寸800*800,最多上传6张）</span>
      <el-progress :percentage="pagePercentage" v-if="pagePercentage!==0"></el-progress>
    </div>
  </div>
</template>
<script>
import {monthDay} from '@/utils/timeFormat.js'
export default {
  name: 'upload',
  props: {
    multiple: {
      default: false
    },
    maxFiles: {
      type: Number,
      default: 1,
    },
    title:{
      default:'上传文件'
    }
  },
  data() {
    return {
      show: false,
      id: 'upload',
      percentage: [],
      fileNum: 1,
      url: '',
      urls: [],
      pagePercentage: 0,
    }
  },
  methods: {
    handleUpload() {
      document.getElementById('upload').click()
    },

    doUpload() {
      const _this = this
      const urls = [];
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
        const files = document.getElementById(_this.id)
        if(files.files) {
          const fileLen = document.getElementById(_this.id).files
          _this.fileNum = fileLen.length;
          if(fileLen.length > _this.maxFiles) {
            _this.$message.warning('最多上传' + _this.urls.length + '个文件')
          } else {
            let resultUpload = ''
            for(let i = 0; i < fileLen.length; i++) {
              const file = fileLen[i]
              //判断图片格式问题
              if(file.type != 'image/jpeg' && file.type != 'image/bmp' && 
                file.type != 'image/jpg' && file.type != 'image/jpeg' && 
                file.type != 'image/png' && file.type != 'image/gif'){
                  this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
                  return false
                }
              // 随机命名
              let random_name = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
              client.
              // 上传
              client.multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, { // multipartUpload阿里云的上传方法
                progress: function*(percentage, cpt) {
                  // 上传进度
                  _this.percentage[i] = percentage
                  let num=0;
                  _this.percentage.forEach(item => {
                    num = num + item
                  })
                  _this.pagePercentage = parseFloat((num / _this.fileNum * 100).toFixed(2));
                }
              }).then((results) => {
                // 上传完成
                _this.url = result.data.hostname + results.name;
              }).catch((err) => {
                
              })
            }
          }
        }
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
  watch: {
    urls(val){
      this.$emit('post-url',val)
    },
    url(val) {
      if(val) {
        this.urls.push(val);
      }
    }
  }
}

</script>

<style rel="stylesheet/scss" lang="scss">
  .place-holder {
    color: #ccc;
    font-size: 12px;
  }
</style>