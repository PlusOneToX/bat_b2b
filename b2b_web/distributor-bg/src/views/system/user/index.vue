<template>
  <!-- <transition name="fade" mode="out-in"> -->
      <keep-alive v-if="!$route.meta.noCache">
        <router-view></router-view>
      </keep-alive>
      <router-view v-else></router-view>
  <!-- </transition> -->
</template>
<script type="text/javascript">
export default {
    name: 'user'
}
</script>

