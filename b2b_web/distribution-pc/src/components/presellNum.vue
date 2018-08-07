<template>
  <div class="buy-sum rl-clear">
    <div @click.stop.prevent="handlePresellReduce(item)" class="rl-fl buyac buya rl-text-gray">-</div>
    <div class="rl-fl buyb"><input @blur="changeSum" v-model="quantitys" class="rl-tc" type="text" maxlength="8" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
    <div @click.stop.prevent="handlePresellAdd(item)" class="rl-fl buyac buyc rl-text-gray">+</div>
  </div>
</template>

<script>
import Vue from 'vue'
export default {
  name: 'presellNnum',
  props: {
    item: {
      type: Object
    }
  },
  data () {
    return {
      quantitys: 0
    }
  },
  watch: {
    quantitys (val) {
      if (this.quantitys < this.item.moq) {
        this.$message.warning('订购数量不可小于' + this.item.moq)
        this.item.count = this.quantitys
      } else {
        this.item.count = this.quantitys
      }
    }
  },
  methods: {
    handlePresellReduce () {
      if (this.quantitys > this.item.moq) {
        this.quantitys--
        this.item.count = this.quantitys
      } else {
        this.$message.warning('订购数量不可小于' + this.item.moq)
      }
    },
    handlePresellAdd () {
      this.quantitys++
      this.item.count = this.quantitys
    },
    changeSum () {
      if (this.quantitys <= this.item.moq) {
        this.$message.warning('订购数量不可小于' + this.item.moq)
        this.item.count = this.quantitys
      } else {
        this.item.count = this.quantitys
      }
    }
  },
  mounted () {
    Vue.set(this.item, 'count', this.item.moq) // 设置对象的属性
    this.quantitys = this.item.moq
  }
}
</script>

<style scoped="scoped" lang='less'>
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
