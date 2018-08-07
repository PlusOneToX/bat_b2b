<template>
  <div class="index_big">
    <div class="oss_file_big">
      <!-- <div class="img-uploader" @click="handleUpload"></div> -->
      <span v-if = "isEdit">
        <img v-show="curUrl"
             :src="curUrl"
             class="avatar-is-big"
             @click="handleUpload" >
        <img v-show="!curUrl" :src="add_img_default" @click="handleUpload">
      </span>
      <span v-else >
        <img v-show="curUrl"
             :src="curUrl"
             class="avatar-no-big" >
        <img v-show="!curUrl" :src="add_img_default" >
      </span>
      <input ref = "x" type="file" v-show="show" :multiple="multiple"  @change="doUpload" />
      <!-- <el-progress :percentage="pagePercentage" v-if="pagePercentage!==0"></el-progress> -->
      <!-- <hr> -->
      <!--  <p>
        上传进度：{{percentage}}
      {{percentage===1?"success!":(percentage===0?'waiting...':'uploading')}}
      </p> -->
      <!-- <hr> -->
      <!--  <ul>
        <li v-for="u in urls">{{u}}</li>
      </ul> -->
    </div>
  </div>
</template>
<script>
import add_img_default from '@/assets/images/add_img_default.png'
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
    },
    isEdit: Boolean,
    index: Number,
    curUrl: String,
    area: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      add_img_default,
      show: false,
      percentage: [],
      fileNum: 1,
      url: '',
      urls: [],
      pagePercentage: 0,
      imageUrl: 'https://car3.autoimg.cn/cardfs/product/g25/M01/02/A4/autohomecar__wKgHIlrtPnuAL69ZAAgkGGye7Aw346.jpg#pvareaid=2042293'
    }
  },
  methods: {
    handleUpload() {
      this.$refs.x.click()
      // this.doUpload();
    },
    doUpload() {
      const _this = this
      const urls = [];
      _this.$http.getFileSts(this).then(result => { 	
        const client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure:true
        })
        const files = this.$refs.x
        if(files.files) {
          const fileLen = this.$refs.x.files
          _this.fileNum = fileLen.length;
          if(fileLen.length > _this.maxFiles) {
            _this.$message.warning('最多上传' + _this.urls.length + '个文件')
          } else {
            let resultUpload = ''
            for(let i = 0; i < fileLen.length; i++) {
              const file = fileLen[i]
              // 随机命名
              let random_name = _this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
              // 上传
              client.multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {
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
                _this.$emit('getImgUrl',_this.index,_this.url, this.area);
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

<style lang="scss" scoped>
.avatar-is-big {
  top:0;
	bottom:0;
	left:0;
	right:0;
	width:100%;
  height: 100%;
	margin:auto;
	z-index: -1;
  cursor: pointer;
  vertical-align: middle
}
.avatar-no-big {
  top:0;
	bottom:0;
	left:0;
	right:0;
	width:100%;
  height: 100%;
	margin:auto;
	z-index: -1;
  vertical-align: middle
}
.img-default-big {
  width: 100px;
  height: 60px;
  cursor: pointer;
  // display: block;
}
</style>

