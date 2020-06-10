<!--
 * @Author: yaowei
 * @Date: 2019-11-20 15:20:28
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-04 11:05:52
-->
<template>
  <div class="theme-item">
    <div class="img-wrap">
      <img class="img" :src="itemData.thumbnail?itemData.thumbnail:itemData.originImage+'?x-oss-process=image/resize,h_400,l_400'" :alt="itemData.pictureName" />
    </div>
    <p class="title">{{ itemData.pictureName }}</p>
    <span class="btn" @click="goPath(itemData)">去定制</span>
  </div>
</template>

<script>
export default {
  name: 'themeItem',
  props: ['itemData'],
  data () {
    return {}
  },
  methods: {
    // 页面跳转
    goPath (params) {
      sessionStorage.setItem('imageInfo', JSON.stringify(params))
      let platform = parseInt(localStorage.getItem('platform'))
      let path = platform === 7 ? '/phone' : '/model'
      if (platform === 7) {
        // type 1-普通图片 2-IP图
        this.$router.push({
          path: '/phone',
          query: { pid: params.pictureId, url: params.originImage, type: 1 }
        })
      } else {
        this.$router.push({
          path: path,
          query: {
            comingFlag: 'goCustom'
          }
        })
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
$white = #fff;
$text-color = #5A5A5A;
$blue = #0076A5;

.theme-item {
  padding: 15px 0;
  text-align: center;
  background-color: $white;
  display: inline-block;

  .img-wrap {
    height: 130px;
  }

  .img {
    max-width: 100%;
    max-height: 100%;
  }

  .title {
    margin-top: 15px;
    font-size: 14px;
    color: $text-color;
  }

  .btn {
    margin-top: 9px;
    display: inline-block;
    width: 89px;
    height: 28px;
    font-size: 14px;
    color: $white;
    line-height: 28px;
    background-color: $blue;
    border-radius: 28px;
  }
}
</style>