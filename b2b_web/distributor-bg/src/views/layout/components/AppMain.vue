<template>
  <section class="main">
    <!-- <transition name="fade" mode="out-in"> -->
      <!-- <router-view :key="key"></router-view> -->
      <div>
        <keep-alive >
          <router-view v-if="!this.$route.meta.noCache"></router-view>
        </keep-alive>
        <router-view v-if="this.$route.meta.noCache"></router-view>
      </div>
    <!-- </transition> -->
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
     
  }
}
</script>
<style lang="scss" scoped>
  .main{
   height:99%;
   width:99%;
   margin-left: 0.5%;
   margin-top: 0.5%;
   /* overflow:auto; */
  }

</style>