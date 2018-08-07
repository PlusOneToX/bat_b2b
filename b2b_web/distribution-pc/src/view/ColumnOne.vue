<!--
 * @Author: yaowei
 * @Date: 2018-05-23 20:10:16
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-14 21:57:21
-->
<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header
        @add="addFood"
        @bdd="bddFood"
        :userState="userState"
        :totalCount="cartNum"
      ></Header>
      <commonGoodsList
        ref="commonGoods"
        :prosName="sectionName"
        :prosNameEn="sectionNameEn"
        :cateId="cateId"
        :cateFlag="'column'"
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
  name: "ColumnOne",
  components: { Footer, Header, commonGoodsList },
  data() {
    return {
      userState: 2,
      sectionName: this.$route.query.title,
      sectionNameEn: this.$route.query.titleEn,
      cateId: this.$route.query.column_id,
      cartNum: 0,
    };
  },
  methods: {
    addFood(target) {
      // 切换栏目ID
      this.tabs = "showImg";
      this.$refs.commonGoods.getSectionCateClassifyList(target);
    },
    bddFood(target) {
      // 栏目index
      this.nav_index = target + 1;
    },
  },
};
</script>

<style scoped="scoped" lang='less'>
.index {
  width: 100%;
}
</style>
