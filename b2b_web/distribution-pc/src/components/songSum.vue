<template>
  <div class="buy-sum rl-clear rl-fr">
    <div @click.stop.prevent="handleReduce" class="rl-fl buyac buya rl-text-gray">-</div>
    <div class="rl-fl buyb">
      <input class="rl-tc" type="text"  @blur="changeNum" v-model="songShop.num" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
    </div>
    <div @click.stop.prevent="handleAdd" class="rl-fl buyac buyc rl-text-gray">+</div>
  </div>
</template>

<script>
export default {
  name: "songSum",
  props: {
    songShop: {
      type: Object,
    },
    maxSongSum: {
      type: Number,
      default: 0,
    },
    hasChoose: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {};
  },
  methods: {
    // 添加
    handleAdd() {
      // numInWarehouse   在库库存
      // 该赠品对应分销商库存数量为0
      if (this.songShop.numInWarehouse <= 0) { 
        this.$message.warning(this.$t("Activitys.NotEnoughGifts"));
        return false;
      } else if (this.songShop.num > this.songShop.numInWarehouse) { // 已选赠品数量大于该赠品对应分销商库存数量    
        if (this.songShop.numInWarehouse >= this.songShop.totalCount) {
          this.$message.warning( this.$t("Activitys.OnlyGift1") + this.songShop.totalCount + this.$t("Activitys.OnlyGift2")
          );
        } else {
          this.$message.warning( this.$t("Activitys.OnlyGift1") + this.songShop.numInWarehouse + this.$t("Activitys.OnlyGift2"));
        }
        this.songShop.num = this.songShop.numInWarehouse;
      } else {
        if (this.songShop.totalCount > this.songShop.num) {
          // 单个赠品货品数量
          if (this.maxSongSum > this.hasChoose) {
            this.songShop.num++;
            this.$emit("input", event.target);
          } else {
            this.$message.warning(this.$t("Activitys.UpperLimitGifts"));
          }
        } else {
          if (this.songShop.numInWarehouse >= this.songShop.totalCount) {
            this.$message.warning(this.$t("Activitys.OnlyGift1") + this.songShop.totalCount + this.$t("Activitys.OnlyGift2"));
          } else {
            this.$message.warning( this.$t("Activitys.OnlyGift1") + this.songShop.numInWarehouse + this.$t("Activitys.OnlyGift2"));
          }
        }
      }
    },
    handleReduce() {
      if (this.songShop.num > 0) {
        this.songShop.num--;
        this.$emit("input", event.target);
      }
    },
    changeNum() {
      if (this.songShop.numInWarehouse <= 0) {
        // 该赠品对应分销商库存数量为0
        this.$message.warning(this.$t("Activitys.NotEnoughGifts"));
        this.songShop.num = 0;
        return false;
      } else if (this.songShop.num > this.songShop.numInWarehouse) {
        // 已选赠品数量大于该赠品对应分销商库存数量
        if (this.songShop.numInWarehouse >= this.songShop.totalCount) {
          this.$message.warning(
            this.$t("Activitys.OnlyGift1") +
              this.songShop.totalCount +
              this.$t("Activitys.OnlyGift2")
          );
        } else {
          this.$message.warning(
            this.$t("Activitys.OnlyGift1") +
              this.songShop.numInWarehouse +
              this.$t("Activitys.OnlyGift2")
          );
        }
        this.songShop.num = 0;
      } else {
        if (this.songShop.presentCount > this.songShop.num) {
          // 单个赠品货品数量
          if (this.maxSongSum >= this.hasChoose) {
            this.maxSongSum += this.songShop.num;
            this.$emit("input", event.target);
          } else {
            this.songShop.num = 0;
            this.$message.warning(this.$t("Activitys.UpperLimitGifts"));
          }
        } else {
          this.songShop.num = this.songShop.totalCount;
          if (this.songShop.numInWarehouse >= this.songShop.totalCount) {
            this.$message.warning(
              this.$t("Activitys.OnlyGift1") +
                this.songShop.totalCount +
                this.$t("Activitys.OnlyGift2")
            );
          } else {
            this.$message.warning(
              this.$t("Activitys.OnlyGift1") +
                this.songShop.numInWarehouse +
                this.$t("Activitys.OnlyGift2")
            );
          }
          if (this.maxSongSum >= this.hasChoose) {
            this.maxSongSum += this.songShop.num;
            this.$emit("input", event.target);
          } else {
            this.songShop.num = 0;
            this.$message.warning(this.$t("Activitys.UpperLimitGifts"));
          }
        }
      }
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
