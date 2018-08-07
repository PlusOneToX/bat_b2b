<template>
  <div class="announcement-add-edit">
    <header>
      <h4>公告</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel"> 返回首页公告 </el-button>
    </header>
    <div v-loading="loading2">
      <el-form ref="form" :model="form" label-width="150px" class="el-form1" :rules="rules">
        <el-form-item label="公告标题：" prop="title">
            <el-input v-model.trim="form.title"></el-input>
        </el-form-item>
      <el-form-item label="是否发布：">
        <el-radio-group v-model="form.releaseStatus">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="发布区域：" prop="releaseArea">
        <el-radio v-model="form.releaseArea" :label="0">国内</el-radio>
        <el-radio v-model="form.releaseArea" :label="1">海外</el-radio>
        <el-radio v-model="form.releaseArea" :label="2">国内和海外</el-radio>
      </el-form-item>
      <el-form-item label="公告附件：" prop="attachmentName">
          <el-input v-model="form.attachmentName" type="file-name" suffix-icon="el-icon-document"></el-input>
          <input ref = "x" type="file" class="file" @change="doUpload($event)"/>
          <el-progress :percentage="percentage" v-show="upload"></el-progress>
        </el-form-item>
      </el-form>
      <el-form ref="form" :model="form" label-width="150px" class="el-form2" :rules="rules">
        <el-form-item label="公告内容：">
          <tinymce :height="800" v-model="form.content" ref="tinymce" style="margin-right: 10px;"></tinymce>
        </el-form-item>
      </el-form>
      <div class="clearfix footbtn">
        <el-col :span="1" :offset="12">
          <el-button class="mini-search-btn" @click="handleSave" >保存</el-button>
        </el-col>
      </div>
    </div>
  </div>
</template>

<script>
  import Tinymce from '@/components/Tinymce'
  import {monthDay} from '@/utils/timeFormat.js'
  export default {
    name: "editAddAnnouncement",
    components: { Tinymce },
    data() {
      return {
        loading2: false,
        form: {
          title: '',
          titleEn: '',
          attachmentName: '',
          releaseStatus: 1,
          releaseArea: 0,
          content:'',
        },
        upload:false,
        rules: {
          title: [{
              required: true,
              message: '请输入公告标题',
              trigger: 'blur'
          }]
        },
        maxFiles: {
            type: Number,
            default: 1,
        },
        percentage: 0,
      }
    },
    mounted() {
        this.getParams();
    },
    activated() {
        this.getParams();
    },
    deactivated() {
        this.$destroy()
    },
    methods: {
      handleSave(){
        if(this.upload){
            this.$message.error("正在上传附件，请稍等！")
            return
        }
        if(this.form.id != undefined){
          this.$http.editNotice(this, this.form).then(res => {    
              if(res.success) {
                  this.$message({
                      message: '保存成功',
                      type: 'success',
                      duration: 3 * 1000,
                      onClose: () => { }
                  })
                  this.loading = false
                  this.cancel()
              }
          })
        }else{
          this.$http.addNotice(this, this.form).then(res => {  
              if(res.success) {
                  this.$message({
                      message: '保存成功',
                      type: 'success',
                      duration: 3 * 1000,
                      onClose: () => { }
                  })
                  this.loading = false
                  this.cancel()
              }
          })
        }
      },
      cancel(){
        this.$router.push({ name: 'homePageAnnouncement'})
      },
      getParams(){
        if(this.$route.params.id != undefined){
          this.loading2 = true;
          this.$http.noticeDetail(this, { id:this.$route.params.id }).then(res => {
            if(res.success){
              this.form = res.data
              this.loading2 = false;
            }else{
              this.loading2 = false;
              this.$message(res.msg)
            }
          })
        }else{
            this.form.id = undefined
            this.form.title = ""
            this.form.titleEn = ""
            this.form.attachmentName = ""
            this.form.releaseStatus =1
            this.form.content = ''
        }

      },
      doUpload(event) {
        const _this = this
        _this.form.attachmentName = event.currentTarget.files[0].name;
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
            const files = _this.$refs.x
            if(files.files) {
            const fileLen = _this.$refs.x.files
            _this.fileNum = fileLen.length;
            if(fileLen.length > _this.maxFiles) {
                _this.$message.warning('最多上传' + _this.urls.length + '个文件')
            } else {
                _this.percentage = 0
                _this.upload = true
                for(let i = 0; i < fileLen.length; i++) {
                const file = fileLen[i]
                const isLt2M = file.size / 1024 / 1024 < 500;
                if (!isLt2M) {
                    _this.$message.error('上传附件大小不能超过 500MB!');
                    return isLt2M
                }
                // 随机命名
                let random_name = _this.random_string(6) + '_' + new Date().getTime() + '.' + file.name.split('.').pop()
                // 上传
                client.multipartUpload('flexible/other/' + monthDay(new Date()) + '/' + random_name, file, {
                    progress: function*(percentage, cpt) {
                      // 上传进度
                      _this.percentage = percentage*100
                      if(_this.percentage === 100){
                          _this.upload = false
                      }
                    }
                }).then((results) => {
                    // 上传完成
                    _this.percentage = 0
                    _this.form.attachmentUrl = result.data.hostname + results.name;
                    _this.$message.success('附件上传成功！');
                }).catch((err) => {
                    _this.percentage = 0
                    _this.upload = false
                    _this.$message.error('附件上传失败！');
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
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.announcement-add-edit {
  background-color: white;
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
    width: 600px;
    margin-top: 30px;
}
.el-form2{
    width: 1000px;
    margin-top: 30px;
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
    // font-size: 200px;
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
    margin-top: 40px;
    padding-bottom: 20px;
}
.header-row {
    @include table-header-color;
}
.el-form-item__content{
  /deep/.el-input--suffix{
    width:430px;
  }
  /deep/.file{
    position:absolute;
    width:80px;
    height: 40px;
    top:0;
    right: 0;
    overflow: hidden;
    cursor: pointer;
  }
}

</style>

