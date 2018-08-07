<!--
 * @Author: yaowei
 * @Date: 2018-07-11 09:55:29
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 21:52:16
-->
<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" ref="header" :totalCount="cartNum"></Header>
      <commonGoodsList
        :searchCon="searchName"
        :prosName="searchName"
        :prosNameEn="searchName"
        :cateFlag="'search'"
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
  name: "SearchPage",
  components: { Footer, Header, commonGoodsList },
  data() {
    return {
      userState: 2,
      searchName: this.$route.query.search_name,
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
