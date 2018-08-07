<!--
 * @Author: litian
 * @Date: 2018-05-16 10:39:06
 * @LastEditors: litian
 * @LastEditTime: 2018-05-18 10:01:27
 * @Description: file content
 * @FilePath: \B2B_WEB_BACKGROUND\src\views\goods\addgoods\components\detail.vue
-->
<template>
  <div class="components-container">
    <div>
      <p class="place-holder title">中文详情</p>
      <tinymce :height="800" v-model="content" ref="tinymce"></tinymce>
    </div>
    <div>
      <p class="place-holder title">英文详情</p>
      <tinymce :height="800" v-model="contentEn" ref="tinymceEn"></tinymce>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'

export default {
  name: 'tinymce-demo',
  components: { Tinymce },
  props:['goods'],
  data() {
    return {
      content:'',
      contentEn: ''
    }
  },
  watch:{
    goods(val){
      this.content=val.contentUrl
      this.contentEn = val.contentUrlEn
    }
  }
}
</script>

<style scoped>
.editor-content{
  margin-top: 20px;
}
.title{
  padding: 10px;
  color: #000;
}
</style>


