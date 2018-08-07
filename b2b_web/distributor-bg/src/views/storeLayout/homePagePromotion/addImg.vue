<template >
  <div class = "add-img">
    <div class="add-img-operation">
      <el-button v-if = "isEdit" type="success" style="background-color:#21b8cb" size="mini" @click="editSave()">保存</el-button>
      <el-button type="danger" size="mini" @click="handleDelete(currentCount)">删除</el-button>
    </div> 
  <div class = "img-content"> 
    <div :class = "isEdit ?'img-uploader-is':'img-uploader-no'" >
      <upload-img class="avatar-uploader" :item = 'item' :isEdit ='isEdit' @getImgUrl = "getImgUrl"></upload-img>
    </div>
    <div class="img-url">
      <span class="input-hint">点击跳转</span>
      <input class = "input-url" v-model='item.extensionUrl' placeholder="http://" :disabled = "!isEdit"/>
    </div>
  </div>
  </div>
</template>

<script>
import uploadImg from "@/views/storeLayout/compomemts/uploadImg"
import { saveExtension } from "./extensionData"
export default {
   props: {
     totalCount: {
       type: Number,
       default: 0
     },
     currentCount: {
       type: Number,
       default: 0
     },
     item: {}
   },
   data(){
     return {
       isEdit: false
     }
   },
   components: { uploadImg },
   mounted() {
     if(this.item.id === 0){
       this.isEdit = true
     }else{
       this.isEdit = false
     }
   },
   methods: {
      editSave(){
        if(this.item.imageUrl.replace(/(^s*)|(s*$)/g, "").length === 0){
          this.$message('请先选择推广图片！');
        }else if (this.item.extensionUrl.replace(/(^s*)|(s*$)/g, "").length === 0){
          this.$message('请先输入点击跳转链接！');
        }else{
          saveExtension(this,{imageUrl: this.item.imageUrl, extensionUrl: this.item.extensionUrl}).then(res => {
            if(res.code === 0){
              this.$message('保存成功！');
              this.$emit('getItems');
              this.isEdit = false
            }else{
              this.$message('保存失败！');
            }
          })
        }
      },
      deleteImg(index){
        this.$emit('deleteImg', index);
      },
      getImgUrl(url){
        this.item.imageUrl = url
      },
      handleDelete(index) {
        if(this.item.id != 0){
            this.$confirm('删除此推广图片?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                this.$api.delete(this, 'admin/u/p/store/extension', { ids: this.item.id }).then(res => {
                    if(res.code == 0) {
                        this.$message.success({
                            message: '删除成功',
                            duration: 3 * 1000,
                            onClose: () => { }
                        })
                        this.deleteImg(index)
                    } else {
                        this.$confirm(
                            item.id + '此推广图片删除失败',
                            '提示', {
                                center: true,
                                showClose: false,
                                showCancelButton: false
                            })
                    }
                })
            })
        }else {
          this.deleteImg(index)
        }
      }
   }
}

</script>

<style lang="scss" scoped>
  .add-img{
    border-bottom: 1px solid #ccc;
    .img-content{
      display: flex;
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
      .img-uploader-no{
         display: inline;
        .avatar-uploader {
          margin-left: 20px;
          margin-bottom: 15px;
          background-color:#bfbfbf;
          border-radius: 5px;
          width: 300px;
          height: 160px;
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
          border-radius: 5px;
          border: 1px solid #ccc;
          font-weight: bold;
          width: 100%;
        }
       
      }

    }
  }
 
  
  
    // .avatar-uploader:hover {
    // border-color: #409EFF;
    // }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 300px;
      height: 160px;
      line-height: 160px;
      text-align: center;
      
    }
    .avatar {
      width: 300px;
      height: 160px;
      display: block;
    }
    .img-default{
      display: inline-block;
      
    }
  .add-img-operation{
    margin-top: 10px;
    margin-bottom: 10px;
    margin-left: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #ccc;
    .btn-delete{
        color: #fff;
        background-color: #e6a23c;
        border-radius: 6px;
      }
    .btn-add{
        color: #fff;
        background-color: #21b8cb;
        border-radius: 6px;
      }
  }

  .upload-image{
   margin-left: 10px;
   margin-right: 10px;
   border-radius: 5px;
   border: 1px solid #ccc;
   background-color: #f8f8f8;
   .btn-delete{
        color: #fff;
        background-color: #e6a23c;
        border-radius: 6px;
    }
    .btn-add{
      color: #fff;
      background-color: #21b8cb;
      border-radius: 6px;
    }
    .no-image{
      margin: 20px;
    }
 }
</style>



