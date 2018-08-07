<template>
  <div id="" class="home-promotion-list">
    <el-row class="header">
        <el-col :span="2"> 首页推广 </el-col>
    </el-row>
    <div class="function">
          <el-button size="mini" @click="add(totalCount)" type="success" style="background-color:#21b8cb">添加</el-button>
    </div>
    <div class="upload-image">
      <div v-if="totalCount > 0" >
        <component @deleteImg = "deleteImg" :is="item.component" :totalCount='totalCount' v-for="(item,index) in items"
        :key="item.ids" :currentCount='index+1' v-model="items" :item = 'item' @getItems = "getItems" >
        </component>
      </div>
      <div v-else class="no-image">暂时没有添加推广图片，请先添加推广图片</div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import addImg from './addImg'
import { getExtensionList } from './extensionData'

export default {
   components: {
    addImg,
   },
   data () {
    return {
      items: [],
      totalCount: 0
    }
   },
   mounted() {
     this.getItems()
   },
   methods: {
    add (totalCount) {
      if(this.items.length > 6){
        this.$message('最多只能添加6张推广图片！');
      } else {
        let isEdit = false;
        for(var i = 0;i<this.items.length;i++){
          if(this.items[i].id === 0){
            isEdit = true
            break
          }
        }
        if(isEdit){
          this.$message('请先保存新添加的推广图片！');
          return
        }
        this.items.push({
            id: 0,
            component: "add-img",
            imageUrl:"",
            extensionUrl:"",
        })
        this.totalCount = totalCount+1
      }
    },
    deleteImg(index){
        this.items.splice(index-1,1);
    },
    getItems(){
      getExtensionList(this).then(res => {
        this.items.splice(0,this.items.length);
        res.extensions.forEach(item =>{
          this.items.push({
            id: item.id,
            component: "add-img",
            imageUrl: item.imageUrl,
            extensionUrl: item.extensionUrl
          })
        })
        this.totalCount = this.items.length
      })
    }
   }
}



</script>

<style lang="scss" scoped>
.home-promotion-list{
    background-color: white;
    min-height: 100%;
    .header {
    background-color: $lakeBlue;
    line-height: 40px;
    color: white;
    padding-left: 20px;
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
.function{
      padding-left: 20px;
      margin-bottom: 10px;
      padding-bottom: 10px;
      padding-top: 10px;
      background-color: white;
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
       
</style>



