<!--
 * @Author: yaowei
 * @Date: 2018-07-11 09:55:29
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 21:54:37
-->
<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState" :totalCount="cartNum"></Header>
      <commonGoodsList
        :prosName="sectionName"
        :prosNameEn="sectionNameEn"
        :cateId="cateId"
        :cateFlag="'section'"
        :classifyType="'classify'"
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
  name: "SectionCate",
  components: { Footer, Header, commonGoodsList },
  data() {
    return {
      userState: 2,
      sectionName: this.$route.query.section_name,
      sectionNameEn: this.$route.query.section_name_en,
      cateId: this.$route.query.section_id,
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
