<template>
  <div class="buy-sum rl-clear">
    <div @click="handleReduce" class="rl-fl buyac buya rl-text-gray cursor-pointer">-</div>
    <div class="rl-fl buyb">
      <input
        class="rl-tc rl-text-xxs"
        @blur="changeNum"
        type="text"
        v-model="counts.quantity"
        maxlength="8"
        onkeyup="this.value=this.value.replace(/\D/g,'')"
      />
    </div>
    <div @click="handleAdd" class="rl-fl buyac buyc rl-text-gray cursor-pointer">+</div>
  </div>
</template>

<script>
import Vue from "vue";
export default {
  name: "GoodsBuySum",
  props: {
    item: {
      type: Object,
    },
  },
  data() {
    return {
      counts: {
        quantity: 0,
      },
    };
  },
  methods: {
    handleAdd() {
      // 子组件通过emit将数据传递出去
      if (!this.item.count) {
        Vue.set(this.item, "count", 1); // 设置对象的属性。
        this.counts.quantity++;
        this.$emit("itemInput", this.item);
      } else {
        this.item.count++;
        this.counts.quantity++;
        this.$emit("itemInput", this.item);
      }
    },
    handleReduce() {
      if (this.counts.quantity > 0) {
        this.item.count--;
        this.counts.quantity = this.counts.quantity - 1;
        this.$emit("input", this.counts.quantity);
        this.$emit("itemInput", this.item);
      }
    },
    changeNum() {
      if (!this.item.count) {
        Vue.set(this.item, "count", 1);
      }
      this.item.count = this.counts.quantity;
      this.$emit("itemInput", this.item);
    },
  },
  mounted() {},
};
</script>

<style scoped="scoped" lang='less'>
.buy-sum {
  width: 92px;
  height: 22px;
  line-height: 22px;
  border: 1px solid #ebeff5;
  div {
    height: 22px;
    box-sizing: border-box;
    background-color: #fff;
    input {
      width: 62px;
    }
  }
  .buyac {
    width: 22px;
    font-size: 22px;
    color: #9b9b9b;
    cursor: pointer;
    text-align: center;
  }
  .buyb {
    width: 48px;
    line-height: 22px;
    color: #3a3a3a;
    border-left: 1px solid #ebeff5;
    border-right: 1px solid #ebeff5;
    input {
      width: 46px;
    }
  }
}
</style>
