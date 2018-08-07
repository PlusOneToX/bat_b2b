<!--
 * @Author: yaowei
 * @Date: 2018-05-24 11:17:57
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 16:19:57
-->
<template>
  <div class="download-wrap">
    <!--公共头部-->
    <Header :userState="userState"></Header>
    <div class="img-container">
      <el-image
        style="width: 1920px; height: 550px"
        :src="require('@/assets/images/userSelfService/banner02.png')"
        fit="contain"
      >
      </el-image>
    </div>
    <div class="download-content rl-margin-zero">
      <DownLoadContentBox></DownLoadContentBox>
    </div>
    <!-- 公共底部 -->
    <Footer></Footer>
    
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import DownLoadContentBox from './components/DownLoadContentBox'
export default {
  name: 'Download',
  components: { Header, Footer, DownLoadContentBox },
  data () {
    return {
      userState: 2,
      showLoading: false // 展示加载动画
    };
  },
  mounted () {
    this.showLoading = true
  }
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.download-wrap {
  .img-container {
    text-align: center;
  }
  .download-content {
    width: @center;
  }
}
</style>
