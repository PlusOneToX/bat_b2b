<template>
	<div class="upload-img">
		<!-- 上传到OSS组件 -->
    <div slot="tip" class="place-holder">中文图片 (图片建议尺寸800*800,最多上传6张)</div>
    <el-upload
      :action="action"
      list-type="picture-card"
      :multiple="true"
      :show-file-list="true"
      :limit="6"
      :on-exceed="handleExceed"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :before-upload="beforeUpload"
      :file-list="fileList">
      <i class="el-icon-plus"></i>
    </el-upload>
    <div slot="tip" class="place-holder">英文图片 (图片建议尺寸800*800,最多上传6张)</div>
    <el-upload
      :action="action"
      list-type="picture-card"
      :multiple="true"
      :show-file-list="true"
      :limit="6"
      :on-exceed="handleExceed"
      :on-remove="handleRemoveEn"
      :on-success="handleSuccessEn"
      :before-upload="beforeUploadEn"
      :file-list="fileListEn">
      <i class="el-icon-plus"></i>
    </el-upload>
	</div>
</template>
<script>
import uploadFile from "@/components/uploadFile";
import {monthDay} from '@/utils/timeFormat.js'
export default {
  props: ["goods"],
  data() {
    return {
      index: 0,
      url: [],
      dialogImageUrl: "",
      dialogVisible: false,
      fileList:[],
      listObj: {},
      params: {
        action: "http://119.23.161.109:9997/admin/u/file/sts",
        data: {
          // 放置阿里云请求回来拼接好的路径名
        }
      },
      urlEn: [],
      fileListEn:[],
      listObjEn: {},
      action: process.env.BASE_API +'system/v1/web/admin/oss/sts'
    };
  },
  components: { uploadFile },
  computed: {
    showImg() {
      return this.url[this.index];
    },
    maxFiles() {
      return 6 - this.url.length;
    },
  },
  methods: {
    handleExceed(files, fileList) {
      if((files.length + fileList.length) >6){
        this.$message.error('最多上传6个文件');
        return false
      }
    },
    handleSuccess(response, file) {
      const objKeyArr = Object.keys(this.listObj)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObj[objKeyArr[i]].uid === file.uid) {
          this.listObj[objKeyArr[i]].hasSuccess = true
          return
        }
      }
    },
    handleRemove(file) {
      for (let i = 0; i < this.url.length; i++) {
        if (file.url.search(this.url[i].url) !== -1) {
          this.url.splice(i,1)
          return
        }
      }
      const objKeyArr = Object.keys(this.listObj)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObj[objKeyArr[i]].uid === file.uid) {
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
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return isLt2M
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
        client.multipartUpload('goods/' + monthDay(new Date()) + '/' + random_name, file, {
          }).then((results) => {
            return new Promise((resolve, reject) => {
              _self.listObj[fileName] = {
                hasSuccess: false,
                uid: file.uid,
                width: this.width,
                height: this.height,
                url1: result.data.hostname + results.name,
                url: result.data.hostname + results.name + '?x-oss-process=image/resize,h_146,l_146'
              }
              resolve(true)
            })
          }).catch((err) => {
            console.log(err)
          })
      })
    },
    /**
     * 英文图片上传成功
     * 施义煌
     * @param response
     * @param file
     */
    handleSuccessEn(response, file) {
      const objKeyArr = Object.keys(this.listObjEn)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObjEn[objKeyArr[i]].uid === file.uid) {
          this.listObjEn[objKeyArr[i]].hasSuccess = true
          return
        }
      }
    },
    /**
     * 英文图片删除
     * 施义煌
     * @param file
     */
    handleRemoveEn(file) {
      for (let i = 0; i < this.urlEn.length; i++) {
        if (file.url.search(this.urlEn[i].url) !== -1) {
          this.urlEn.splice(i,1)
          return
        }
      }
      const objKeyArr = Object.keys(this.listObjEn)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObjEn[objKeyArr[i]].uid === file.uid) {
          delete this.listObjEn[objKeyArr[i]]
          return
        }
      }
    },
    /**
     * 英文图片上传前的校验
     * 施义煌
     * @param file
     * @return {boolean}
     */
    beforeUploadEn(file) {
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
      const _self = this
      const fileName = file.uid
      this.listObjEn[fileName] = {}
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
        client.multipartUpload('goods/' + monthDay(new Date()) + '/' + random_name, file, {
        }).then((results) => {
          return new Promise((resolve, reject) => {
            // this.url.push({uid: file.uid, url: result.data.hostname + results.name})
            _self.listObjEn[fileName] = {
              hasSuccess: false,
              uid: file.uid,
              width: this.width,
              height: this.height,
              url1: result.data.hostname + results.name,
              url: result.data.hostname + results.name + '?x-oss-process=image/resize,h_146,l_146'
            }
            resolve(true)
          })
        }).catch((err) => {})
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
    beforeAvatarUpload(file) {
      let Xls = file.name.split(".");
      if (Xls[1] === "jpg" || Xls[1] === "png") {
        return file;
      } else {
        this.$message.error("上传文件只能是 jpg/png 格式!");
        return false;
      }
    },
    // =============
    handleLeft() {
      // 轮播图 < 按键
      this.index = this.index == 0 ? this.url.length - 1 : this.index - 1;
    },
    handleRight() {
      // 轮播图 > 按键
      this.index = this.index == this.url.length - 1 ? 0 : this.index + 1;
    },
    magnify(index) { // 放大操作
      this.index = index;
      this.dialogVisible = true;
    },
    handleDelete(index) { // 删除操作
      this.$confirm("删除此图片?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true
      }).then(() => {
        this.url.splice(index, 1); // 删除单张图片
        if (this.index >= this.url.length - 1) {
          this.index = this.url.length - 1;
        }
      });
    },
    handleClick(index) {
      this.index = index;
    },
  },
  watch: {
    goods(val) {
      for (let i = 1; i < 7; i++) {
        let n = "imageUrl" + i;
        let n1 = "imageUrl" + (i+i*10);
        if (val[n] != "" && val[n] != undefined ) {
          this.fileList.push({url:val[n1]})
          this.url.push({url1:val[n],status:"success"})
        }
        // 英文图片处理 - syh
        let nEn = "imageUrl" + i + 'en';
        if (val[nEn]) {
          this.fileListEn.push({url:val[nEn]});
          this.urlEn.push({url1:val[nEn],status:"success"});
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.upload-img {
  margin-left: 20px;
  margin-right: 20px;
  .place-holder {
    margin-bottom: 10px;
    margin-top: 10px;
    color: #ccc;
    font-size: 12px;
  }
  .img-div {
    width: 148px;
    height: 148px;
    display: table-cell;
    vertical-align: middle;
    text-align: center;
    // background-color: #bfbfbf;
    border: 1px dashed #c0ccda;
    border-radius: 6px;
    .img-default {
      width: 50px;
      height: 50px;
    }
  }
  .box {
    margin-top: 20px;
    padding: 10px;
    border: 2px solid #ccc;
  }
  .left-side {
    width: 68%;
    float: left;
    height: 100%;
    margin-right: 2%;
  }
  .left-side img {
    height: 345px;
  }

  .arrow {
    font-size: 30px;
    position: absolute;
    opacity: 0.5;
    &:hover {
      opacity: 1;
      transform: scale(1.5);
    }
  }
  .show-img {
    position: relative;
  }
  .el-icon-arrow-left {
    left: 5%;
    top: 50%;
  }
  .el-icon-arrow-right {
    right: 5%;
    top: 50%;
  }
  .img-content {
    width: 148px !important;
    height: 148px;
    border-radius: 6px;
    border: 1px solid #ccc;
    text-align: center;
    margin: 0 10px;
    display: inline-block;
    position: relative;
  }
  img {
    width: 100%;
    height: 100%;
  }
  .delete {
    position: absolute;
    right: 1px;
    bottom: 1px;
    // opacity: 0.5;
    color: #fab42d;
    &:hover {
      opacity: 1.5;
      transform: scale(1.5);
    }
  }
  .magnifyCss {
    position: absolute;
    right: 20px;
    bottom: 1px;
    // opacity: 0.5;
    color: #08bbcd;
    &:hover {
      opacity: 1.5;
      transform: scale(1.5)
    }
  }
}
</style>
