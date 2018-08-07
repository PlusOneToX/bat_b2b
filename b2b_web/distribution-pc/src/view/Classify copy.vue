<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header :classifyId="classifyIdType" :userState="userState" :currentTab="'classify'" ref="header"></Header>
       <!--主内容-->
      <commonGoodsList :cateFlag="'classify'"></commonGoodsList>
      
      <!--公共底部-->
      <Footer></Footer>
    </div>
  
  </div>
</template>

<script>
import Vue from 'vue';
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
import commonGoodsList from "@/components/commonGoodsList.vue";

import loading from '@/components/loading.vue';
import sha1 from 'js-sha1';
import GD from '@/assets/js/globalData';
import { getCartNum } from '@/api/cart';
// LIU
import { classifyList} from '@/apiService/api'
export default {
  name: 'Classify',
  components: { Footer, Header ,commonGoodsList},
  data () {
    return {
      userState: 2,
      classifyName: this.$route.query.classsity_name,
      classifyNameEn: this.$route.query.classsity_name_en,
      classifyIdType: true, // 分类页面刷新
    };
  },
  
  methods: {
   
  },
  created () {
   
  },
  mounted () {
    
  }
};
</script>

<style scoped="scoped" lang="less">

.index {
  width: 100%;
}

</style>
