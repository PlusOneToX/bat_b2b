<!--
 * @Author: yaowei
 * @Date: 2018-05-21 09:16:07
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-25 16:11:55
-->
<template>
  <div class="exchange-code">
    <header>
      <h4>兑换码列表</h4>
    </header>
    <codeList :enterFlag="'exchangeCode'"></codeList>
  </div>
</template>

<script>
import codeList from "@/views/marketingPromotion/exchange//components/codeList";
export default {
  name: "exchangeCode",
  components: { codeList },
  data() {
    return {};
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.exchange-code {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    }
  }
}
</style>