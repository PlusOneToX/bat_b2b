<template>
  <div class="index">
    <div class="oss_file">
      <el-button type='primary' size="mini" @click="handleUpload">{{title}}</el-button>
      <input type="file" v-show="show" :multiple="multiple" :id="id" @change="doUpload" />
      <el-progress :percentage="pagePercentage" v-if="pagePercentage!==0"></el-progress>
    </div>
  </div>
</template>
<script>
export default {
  name: 'upload',
  props: {
    multiple: {
      default: false
    },
    maxFiles: {
      type: Number,
      default: 1
    },
    title: {
      default: '上传文件'
    }
  },
  data () {
    return {
      show: false,
      id: 'upload',
      percentage: [],
      fileNum: 1,
      url: '',
      urls: [],
      pagePercentage: 0
    }
  },
  methods: {
    handleUpload () {
      document.getElementById('upload').click()
      // console.log(this.maxFiles)
    },

    doUpload () {
      const _this = this
      // const urls = []
      this.$api.get(this, 'user/u/file/sts').then((result) => {
        const client = new window.OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint
        })
        const files = document.getElementById(_this.id)
        if (files.files) {
          const fileLen = document.getElementById(_this.id).files
          for (var i = 0; i < fileLen.length; i++) { // 图片上传格式限制
            if (fileLen[i].type !== 'image/jpeg' && fileLen[i].type !== 'image/bmp' &&
              fileLen[i].type !== 'image/jpg' && fileLen[i].type !== 'image/jpeg' &&
              fileLen[i].type !== 'image/png' && fileLen[i].type !== 'image/gif') {
              this.$message.error('上传图片只能是bmp、jpg、jpeg、png、gif格式!')
              return false
            }
          }
          _this.fileNum = fileLen.length
          for (let i = 0; i < fileLen.length; i++) {
            const file = fileLen[i]
            // 随机命名
            let randomName = this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
            // 上传
            client.multipartUpload(result.data.pathName + '/' + randomName, file, {
              progress: function*(percentage, cpt) {
                // 上传进度
                _this.percentage[i] = percentage
                let num = 0
                _this.percentage.forEach(item => {
                  num = num + item
                })
                _this.pagePercentage = parseFloat((num / _this.fileNum * 100).toFixed(2))
              }
            }).then((results) => {
              // console.log(results)
              // 上传完成
              _this.url = result.data.hostname + results.name
              // console.log(url);
            })
          }
        }
      })
    },

    // 随机生成文件名
    random_string (len) {
      len = len || 32
      var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'
      var maxPos = chars.length
      var pwd = ''
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos))
      }
      return pwd
    }
  },
  watch: {
    urls (val) {
      this.$emit('post-url', val)
    },
    url (val) {
      this.urls = val
    }
  }
}

</script>
