<!--
 * @Author: yaowei
 * @Date: 2018-05-23 20:10:16
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 21:57:49
-->
<!--
 * @Author: yaowei
 * @Date: 2018-05-23 20:10:16
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 21:57:41
-->

<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :custom="custom" :logOuts="logOut" :totalCount="cartNum"></Header>
      <!--主内容-->
      <commonGoodsList
        :diyType="diyType"
        :cateFlag="'custom'"
      ></commonGoodsList>
      <!--公共底部-->
      <Footer></Footer>
    </div>
  </div> 
</template>

<script>
import commonGoodsList from "@/components/commonGoodsList.vue";
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
export default {
  name: "CustomProducts",
  components: { Footer, Header, commonGoodsList },
  data() {
    return {
      custom: true,
      logOut: true,
      diyType: this.$route.query.custom_type,
      cartNum: 0,
    };
  },
  methods: {},
  mounted() {},
};
</script>

<style scoped="scoped" lang='less'>
.index {
  width: 100%;
}
</style>
