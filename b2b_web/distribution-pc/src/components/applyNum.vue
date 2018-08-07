<template>
  <div class="buy-sum rl-clear">
    <div @click.stop.prevent="handleReduce" class="rl-fl buyac buya rl-text-gray">-</div>
    <div class="rl-fl buyb"><input class="rl-tc" type="text" v-model="confirmShop.applyCount" maxlength="3" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
    <div @click.stop.prevent="handleAdd" class="rl-fl buyac buyc rl-text-gray">+</div>
  </div>
</template>

<script>
export default {
  name: 'applyNum',
  props: {
    confirmShop: {
      type: Object
    }
  },
  data () {
    return {
    }
  },
  methods: {
    handleAdd () {
      this.confirmShop.applyCount++
      this.$emit('input', event.target)
    },
    handleReduce () {
      if (this.confirmShop.applyCount > 0) {
        this.confirmShop.applyCount--
        this.$emit('input', event.target)
      }
    }
  },
  mounted () {
  }
}
</script>

<style scoped lang='less'>
  .buy-sum{
    width: 92px;
    height: 22px;
    line-height: 22px;
    border: 1px solid #EBEFF5;
    div{
      height: 22px;
      box-sizing: border-box;
      background-color: #fff;
      input {
        width: 62px;
      }
    }
    .buyac{
      width: 22px;
      font-size: 22px;
      color: #9B9B9B;
      cursor: pointer;
      text-align: center;
    }
    .buyb{
      width: 48px;
      line-height: 22px;
      color: #3A3A3A;
      border-left: 1px solid #EBEFF5;
      border-right: 1px solid #EBEFF5;
      input{
        width: 46px;
      }
    }
  }
</style>
