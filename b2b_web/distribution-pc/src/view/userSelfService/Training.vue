<!--
 * @Author: yaowei
 * @Date: 2018-05-24 11:17:57
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 16:21:59
-->
<template>
  <div class="training-wrap">
    <!--公共头部-->
    <Header :userState="userState"></Header>
    <div class="img-container">
      <el-image
        style="width: 1920px; height: 550px"
        :src="require('@/assets/images/userSelfService/banner01.png')"
        fit="contain"
      >
      </el-image>
    </div>
    <div class="training-content rl-margin-zero">
      <TrainContentBox></TrainContentBox>
    </div>
    <!-- 公共底部 -->
    <Footer></Footer>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import TrainContentBox from './components/TrainContentBox'
export default {
  name: 'Training',
  components: { Header, Footer, TrainContentBox },
  data () {
    return {
      userState: 2
    };
  }
};
</script>

<style lang="less">
@import url("../../assets/less/variable.less");
.training-wrap {
  .img-container {
    text-align: center;
  }
  .training-content {
    width: @center;
  }
}
</style>
