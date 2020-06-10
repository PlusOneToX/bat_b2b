<!--
 * @Author: yaowei
 * @Date: 2019-05-10 10:58:44
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-14 18:24:09
-->
<template>
  <div class="divider">
    <van-divider :style="{ borderColor: '#D8D8D8', padding: '0 15px' }"
      ><span class="text">{{ text }}</span></van-divider
    >
  </div>
</template>

<script>
export default {
  name: "Divider",
  props: {
    text: {
      type: String,
      default: "",
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="stylus" scoped></style>