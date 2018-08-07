<template>
  <div class="buy-sum rl-clear">
    <div @click.stop.prevent="handleReduce" class="rl-fl buyac buya rl-text-gray cursor-pointer" v-if="Number(this.items.num) > 1">-</div>
    <div class="rl-fl buyac buya rl-text-gray" v-else>-</div>
    <div class="rl-fl buyb"><input class="rl-tc" @focus="clearSum" @blur="changeNum" type="text" v-model="items.num" maxlength="8" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
    <div @click.stop.prevent="handleAdd" class="rl-fl buyac buyc rl-text-gray cursor-pointer" v-if="this.items.canBuy >= this.items.num || this.itemsCount.orderType === 2 || this.itemsCount.orderType === 3">+</div>
    <div class="rl-fl buyac buyc rl-text-gray" v-else>+</div>
  </div>
</template>

<script>
import Vue from 'vue'
export default {
  name: 'shopQiu',
  props: {
    items: {
      type: Object
    },
    itemsCount: {
      type: Object
    }
  },
  data () {
    return {
    }
  },
  watch: {
    'items.num': { // 预售商品
      handler () {
        if (this.items.orderType === 2) {
          if (this.items.num < Number(this.items.moq)) {
            this.$message.warning('订购数量不可小于' + this.items.moq)
          }
        }
      }
    }
  },
  methods: {
    handleAdd () {
      if (Number(this.items.canBuy) === Number(this.items.num)) {
        this.$message.warning('购买数量已达库存数量!')
        return false
      }
      this.items.num++
      if (this.itemsCount.promotionRule !== null) {
        if (this.itemsCount.promotionRule.oneBuyCount === undefined || this.itemsCount.promotionRule.oneBuyCount === null) {
          this.itemsCount.totalCount = 0
          if (this.items.orderType === 1) { // 普通订单
            if (this.items.num <= this.items.realnumInWarehouse) {
              this.itemsCount.totalPrice += this.items.salePrice
            } else {
              if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                this.itemsCount.totalPrice += this.items.salePrice
              }
            }
          } else if (this.items.orderType === 2 || this.items.orderType === 3) { // 预售和定制商品
            this.itemsCount.totalPrice += this.items.salePrice
          }
          // console.log(this.itemsCount.totalCount)
          // console.log(this.itemsCount.totalPrice)
        } else if (this.itemsCount.promotionRule.oneBuyMoney === undefined || this.itemsCount.promotionRule.oneBuyMoney === null) {
          if (this.items.orderType === 1) { // 普通订单
            if (this.items.num <= this.items.realnumInWarehouse) { // 在库数量大于等于购买数量
              this.itemsCount.totalCount++
            } else {
              if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                this.itemsCount.totalCount++
              }
            }
          } else if (this.items.orderType === 2 || this.items.orderType === 3) { // 预售和定制商品
            this.itemsCount.totalCount++
          }
          // console.log(this.itemsCount.totalCount)
          this.itemsCount.totalPrice = 0
          // console.log(this.itemsCount.totalPrice)
        }
      } else if (this.itemsCount.gradeDiscountsRule !== null) { // 折扣活动
        if (this.itemsCount.gradeDiscountsRule.oneBuyCount === undefined || this.itemsCount.gradeDiscountsRule.oneBuyCount === null) {
          this.itemsCount.totalCount = 0
          if (this.items.num <= this.items.realnumInWarehouse) {
            this.itemsCount.totalPrice += this.items.salePrice
            if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
              this.itemsCount.gradeDiscountTotalPrice += this.items.salePrice * (this.items.discount / 100)
            }
          } else {
            if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
              this.itemsCount.totalPrice += this.items.salePrice
              if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
                this.itemsCount.gradeDiscountTotalPrice += this.items.salePrice * (this.items.discount / 100)
              }
            }
          }
        } else if (this.itemsCount.gradeDiscountsRule.oneBuyMoney === undefined || this.itemsCount.gradeDiscountsRule.oneBuyMoney === null) {
          if (this.items.num <= this.items.realnumInWarehouse) { // 在库数量大于等于购买数量
            this.itemsCount.totalCount++
          } else {
            if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
              this.itemsCount.totalCount++
            }
          }
          this.itemsCount.totalPrice = 0
        }
      }
      if (Number(this.items.realnumInWarehouse) > Number(this.items.num)) { // 在库数量大于购买数量
        this.items.numInWarehouse++
        this.items.stockItemCount = 0
      } else {
        this.items.numInWarehouse = this.items.realnumInWarehouse
        this.items.stockItemCount = this.items.num - this.items.realnumInWarehouse
      }
      this.$emit('input', event.target)
      // 购物车数量变更变更
      this.$api.put(this, 'user/u/shoppingCart', {id: this.items.id, num: this.items.num}).then(res => {
        if (res.code === 0) {
          // this.$emit('changePresent', this.itemsCount.rule.reduceOrPresent)
        }
      })
    },
    handleReduce () {
      if (this.items.canBuy === 0) {
        this.$message.warning('库存数量为0,不能购买!')
        this.items.num = 0
        return false
      }
      if (this.items.num > 1) {
        this.items.num--
        if (this.itemsCount.promotionRule !== null) {
          if (this.itemsCount.promotionRule.oneBuyCount === undefined || this.itemsCount.promotionRule.oneBuyCount === null) {
            this.itemsCount.totalCount = 0
            // console.log(this.itemsCount.totalCount)
            if (this.items.orderType === 1) { // 普通订单
              if (this.items.num <= this.items.realnumInWarehouse) {
                this.itemsCount.totalPrice = this.itemsCount.totalPrice - this.items.salePrice
              } else {
                if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                  this.itemsCount.totalPrice = this.itemsCount.totalPrice - this.items.salePrice
                }
              }
            } else if (this.items.orderType === 2 || this.items.orderType === 3) { // 预售和定制商品
              this.itemsCount.totalPrice = this.itemsCount.totalPrice - this.items.salePrice
            }
            // console.log(this.itemsCount.totalPrice)
          } else if (this.itemsCount.promotionRule.oneBuyMoney === undefined || this.itemsCount.promotionRule.oneBuyMoney === null) {
            if (this.items.orderType === 1) { // 普通订单
              if (this.items.num <= this.items.realnumInWarehouse) {
                this.itemsCount.totalCount--
              } else {
                if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                  this.itemsCount.totalCount--
                }
              }
            } else if (this.items.orderType === 2 || this.items.orderType === 3) { // 预售和定制商品
              this.itemsCount.totalCount--
            }
            // console.log(this.itemsCount.totalCount)
            // this.itemsCount.totalPrice = 0
            // console.log(this.itemsCount.totalPrice)
          }
          // if ((this.itemsCount.totalCount < this.itemsCount.rule.oneBuyCount || this.itemsCount.totalPrice < this.itemsCount.rule.oneBuyMoney) && this.itemsCount.rule.reduceOrPresent === 2 && this.itemsCount.presentItems.length > 0) {
          // }
          console.log(parseInt(this.itemsCount.promotionRule.presentCount * (this.itemsCount.totalPrice / this.itemsCount.promotionRule.oneBuyMoney)))
          console.log(parseInt(this.itemsCount.promotionRule.presentCount * (this.itemsCount.totalCount / this.itemsCount.promotionRule.oneBuyCount)))
          console.log(this.itemsCount.presentItemsCount)
          if (parseInt(this.itemsCount.promotionRule.presentCount * (this.itemsCount.totalPrice / this.itemsCount.promotionRule.oneBuyMoney)) < this.itemsCount.presentItemsCount || parseInt(this.itemsCount.promotionRule.presentCount * (this.itemsCount.totalCount / this.itemsCount.promotionRule.oneBuyCount)) < this.itemsCount.presentItemsCount) {
            this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.itemsCount.promotionRule.id}).then(res => { // 满赠数量减少删除赠品
              if (res.code === 0) {
                this.itemsCount.presentItems = []
                this.$emit('changePresent', this.itemsCount.presentItemsCount)
              }
            })
          } else if ((this.itemsCount.promotionRule.oneBuyMoney === undefined || this.itemsCount.promotionRule.oneBuyMoney === null) && (this.itemsCount.totalCount < this.itemsCount.promotionRule.oneBuyCount)) {
            this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.itemsCount.promotionRule.id}).then(res => { // 不满足满赠条件删除赠品
              if (res.code === 0) {
                this.itemsCount.presentItems = []
                this.$emit('changePresent', this.itemsCount.presentItemsCount)
              }
            })
          } else if ((this.itemsCount.promotionRule.oneBuyCount === undefined || this.itemsCount.promotionRule.oneBuyCount === null) && (this.itemsCount.totalPrice < this.itemsCount.promotionRule.oneBuyMoney)) {
            this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.itemsCount.promotionRule.id}).then(res => { // 不满足满赠条件删除赠品
              if (res.code === 0) {
                this.itemsCount.presentItems = []
                this.$emit('changePresent', this.itemsCount.presentItemsCount)
              }
            })
          }
        } else if (this.itemsCount.gradeDiscountsRule !== null) { // 折扣活动
          if (this.itemsCount.gradeDiscountsRule.oneBuyCount === undefined || this.itemsCount.gradeDiscountsRule.oneBuyCount === null) {
            this.itemsCount.totalCount = 0
            // console.log(this.itemsCount.totalCount)
            if (this.items.num <= this.items.realnumInWarehouse) {
              this.itemsCount.totalPrice = this.itemsCount.totalPrice - this.items.salePrice
              if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
                this.itemsCount.gradeDiscountTotalPrice = this.itemsCount.gradeDiscountTotalPrice - this.items.salePrice * (this.items.discount / 100)
              }
            } else {
              if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                this.itemsCount.totalPrice = this.itemsCount.totalPrice - this.items.salePrice
                if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
                  this.itemsCount.gradeDiscountTotalPrice = this.itemsCount.gradeDiscountTotalPrice - this.items.salePrice * (this.items.discount / 100)
                }
              }
            }
          } else if (this.itemsCount.gradeDiscountsRule.oneBuyMoney === undefined || this.itemsCount.gradeDiscountsRule.oneBuyMoney === null) {
            if (this.items.num <= this.items.realnumInWarehouse) {
              this.itemsCount.totalCount--
            } else {
              if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                this.itemsCount.totalCount--
              }
            }
            this.itemsCount.totalPrice = 0
          }
        }
        if (Number(this.items.realnumInWarehouse) > Number(this.items.num)) {
          this.items.numInWarehouse--
          this.items.stockItemCount = 0
        } else {
          this.items.numInWarehouse = this.items.realnumInWarehouse
          this.items.stockItemCount = this.items.num - this.items.realnumInWarehouse
        }
        this.$emit('input', event.target)
        // 购物车数量变更变更
        this.$api.put(this, 'user/u/shoppingCart', {id: this.items.id, num: this.items.num}).then(res => {
          if (res.code === 0) {
            // this.$emit('changePresent', this.itemsCount.rule.reduceOrPresent)
          }
        })
      }
    },
    changeNum () { // 购物车数量变更
      var secondsAreas = 2 // 设置时间
      this.items.limit = true
      this.$forceUpdate()
      if (this.items.canBuy === 0) {
        this.intervalids = setInterval(() => {
          secondsAreas--
          if (secondsAreas === 0) {
            this.$message.warning('库存数量为0,不能购买!')
            this.items.num = 0
            this.$emit('limits', this.items.limit)
            clearInterval(this.intervalids)
          }
        }, 1000)
        return false
      }
      if (Number(this.items.num) === 0) {
        this.intervalids = setInterval(() => {
          secondsAreas--
          if (secondsAreas === 0) {
            this.$message.warning('购买数量不能为0!')
            this.items.num = 1
            this.$api.put(this, 'user/u/shoppingCart', {id: this.items.id, num: this.items.num}).then(res => {
              if (res.code === 0) {
                this.items.numInWarehouse = 1
                this.items.stockItemCount = 0
                // this.$emit('changePresent', this.itemsCount.rule.reduceOrPresent)
              }
            })
            this.$emit('limits', this.items.limit)
            clearInterval(this.intervalids)
          }
        }, 1000)
        return false
      }
      if (this.items.canBuy < this.items.num) { // canBuyUp在途+ 在库库存
        this.intervalids = setInterval(() => {
          secondsAreas--
          if (secondsAreas === 0) {
            this.$message.warning('购买数量不能大于库存!')
            this.items.num = this.items.canBuy
            this.$api.put(this, 'user/u/shoppingCart', {id: this.items.id, num: this.items.num}).then(res => {
              if (res.code === 0) {
                this.items.numInWarehouse = this.items.realnumInWarehouse
                this.items.stockItemCount = this.items.num - this.items.realnumInWarehouse
                if (this.itemsCount.promotionRule !== null) {
                  if (this.itemsCount.rule.oneBuyCount === undefined) {
                    this.itemsCount.totalCount = 0
                    this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.num)
                  } else if (this.itemsCount.promotionRule.oneBuyMoney === undefined) {
                    this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.num)
                    this.itemsCount.totalPrice = 0
                  }
                }
                // this.$emit('changePresent', this.itemsCount.rule.reduceOrPresent)
              }
            })
            this.$emit('limits', this.items.limit)
            clearInterval(this.intervalids)
          }
        }, 1000)
        return false
      }
      if (this.itemsCount.promotionRule !== null) {
        if (this.itemsCount.promotionRule.oneBuyCount === undefined || this.itemsCount.promotionRule.oneBuyCount === null) {
          this.itemsCount.totalCount = 0
          // console.log(this.itemsCount.totalCount)
          if (this.items.orderType === 1) { // 普通订单
            if (this.items.num <= this.items.realnumInWarehouse) {
              this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.num)
            } else {
              if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.num)
              } else {
                this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.realnumInWarehouse)
              }
            }
          } else if (this.items.orderType === 2 || this.items.orderType === 3) { // 预售和定制商品
            this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.num)
          }
          // console.log(this.itemsCount.totalPrice)
        } else if (this.itemsCount.promotionRule.oneBuyMoney === undefined || this.itemsCount.promotionRule.oneBuyMoney === null) {
          if (this.items.orderType === 1) { // 普通订单
            if (this.items.num <= this.items.realnumInWarehouse) {
              this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.num)
            } else {
              if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
                this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.num)
              } else {
                this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.realnumInWarehouse)
              }
            }
          } else if (this.items.orderType === 2 || this.items.orderType === 3) { // 预售和定制商品
            this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.num)
          }
          // console.log(this.itemsCount.totalCount)
          this.itemsCount.totalPrice = 0
          // console.log(this.itemsCount.totalPrice)
        }
        if (parseInt(this.itemsCount.promotionRule.presentCount * (this.itemsCount.totalPrice / this.itemsCount.promotionRule.oneBuyMoney)) < this.itemsCount.presentItemsCount || parseInt(this.itemsCount.promotionRule.presentCount * (this.itemsCount.totalCount / this.itemsCount.promotionRule.oneBuyCount)) < this.itemsCount.presentItemsCount) {
          this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.itemsCount.promotionRule.id}).then(res => { // 满赠数量减少删除赠品
            if (res.code === 0) {
              this.itemsCount.presentItems = []
              this.$emit('changePresent', this.itemsCount.presentItemsCount)
            }
          })
        } else if ((this.itemsCount.promotionRule.oneBuyMoney === undefined || this.itemsCount.promotionRule.oneBuyMoney === null) && (this.itemsCount.totalCount < this.itemsCount.promotionRule.oneBuyCount)) {
          this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.itemsCount.promotionRule.id}).then(res => { // 不满赠满赠条件删除赠品
            if (res.code === 0) {
              this.itemsCount.presentItems = []
              this.$emit('changePresent', this.itemsCount.presentItemsCount)
            }
          })
        } else if ((this.itemsCount.promotionRule.oneBuyCount === undefined || this.itemsCount.promotionRule.oneBuyCount === null) && (this.itemsCount.totalPrice < this.itemsCount.promotionRule.oneBuyMoney)) {
          this.$api.delete(this, 'user/u/shoppingCart/delPresent', {ruleId: this.itemsCount.promotionRule.id}).then(res => { // 不满赠满赠条件删除赠品
            if (res.code === 0) {
              this.itemsCount.presentItems = []
              this.$emit('changePresent', this.itemsCount.presentItemsCount)
            }
          })
        }
      } else if (this.itemsCount.gradeDiscountsRule !== null) { // 折扣活动
        if (this.itemsCount.gradeDiscountsRule.oneBuyCount === undefined || this.itemsCount.gradeDiscountsRule.oneBuyCount === null) {
          this.itemsCount.totalCount = 0
          if (this.items.num <= this.items.realnumInWarehouse) {
            this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.num)
            if (this.itemsCount.gradeDiscountTotalPrice || this.itemsCount.gradeDiscountTotalPrice === 0) { // 折扣活动后计算商品总价
              this.itemsCount.gradeDiscountTotalPrice = this.items.salePrice * this.items.num * (this.items.discount / 100)
            }
          } else {
            if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
              this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.num)
              if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
                this.itemsCount.gradeDiscountTotalPrice = this.itemsCount.gradeDiscountTotalPrice + this.items.salePrice * this.items.num * (this.items.discount / 100)
              }
            } else {
              this.itemsCount.totalPrice = this.itemsCount.totalPrice + (this.items.salePrice * this.items.numInWarehouse)
              if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
                this.itemsCount.gradeDiscountTotalPrice = this.itemsCount.gradeDiscountTotalPrice + this.items.salePrice * this.items.realnumInWarehouse * (this.items.discount / 100)
              }
            }
          }
        } else if (this.itemsCount.gradeDiscountsRule.oneBuyMoney === undefined || this.itemsCount.gradeDiscountsRule.oneBuyMoney === null) {
          if (this.items.num <= this.items.realnumInWarehouse) {
            this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.num)
          } else {
            if (this.items.onWayAttendEventFlag === 1) { // 在途商品参与活动
              this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.num)
            } else {
              this.itemsCount.totalCount = Number(this.itemsCount.totalCount) + Number(this.items.realnumInWarehouse)
            }
          }
          this.itemsCount.totalPrice = 0
        }
      }
      if (Number(this.items.realnumInWarehouse) > Number(this.items.num)) {
        this.items.numInWarehouse = this.items.num
        this.items.stockItemCount = 0
      } else {
        this.items.numInWarehouse = this.items.realnumInWarehouse
        this.items.stockItemCount = this.items.num - this.items.realnumInWarehouse
      }
      this.$api.put(this, 'user/u/shoppingCart', {id: this.items.id, num: this.items.num}).then(res => {
        if (res.code === 0) {
          // this.$emit('changePresent', this.itemsCount.rule.reduceOrPresent)
        }
      })
      this.$emit('limits', this.items.limit)
    },
    clearSum () {
      Vue.set(this.items, 'limit', false) // 限制去结账按钮
      if (this.itemsCount.promotionRule !== null) {
        if (this.itemsCount.promotionRule.oneBuyCount === undefined || this.itemsCount.promotionRule.oneBuyCount === null) {
          this.itemsCount.totalCount = 0
          // console.log(this.itemsCount.totalCount)
          this.itemsCount.totalPrice = this.itemsCount.totalPrice - (this.items.salePrice * this.items.num)
          // console.log(this.itemsCount.totalPrice)
        } else if (this.itemsCount.promotionRule.oneBuyMoney === undefined || this.itemsCount.promotionRule.oneBuyMoney === null) {
          this.itemsCount.totalCount = Number(this.itemsCount.totalCount) - Number(this.items.num)
          // console.log(this.itemsCount.totalCount)
          this.itemsCount.totalPrice = 0
          // console.log(this.itemsCount.totalPrice)
        }
      } else if (this.itemsCount.gradeDiscountsRule !== null) { // 折扣活动
        if (this.itemsCount.gradeDiscountsRule.oneBuyCount === undefined || this.itemsCount.gradeDiscountsRule.oneBuyCount === null) {
          this.itemsCount.totalCount = 0
          if (this.items.onWayAttendEventFlag === 0) { // 在途不参与活动
            this.itemsCount.totalPrice = this.itemsCount.totalPrice - (this.items.salePrice * this.items.numInWarehouse)
          } else {
            this.itemsCount.totalPrice = this.itemsCount.totalPrice - (this.items.salePrice * this.items.num)
          }
          if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
            this.itemsCount.gradeDiscountTotalPrice = this.itemsCount.gradeDiscountTotalPrice - (this.items.salePrice * this.items.num) * (this.items.discount / 100)
          }
        } else if (this.itemsCount.gradeDiscountsRule.oneBuyMoney === undefined || this.itemsCount.gradeDiscountsRule.oneBuyMoney === null) {
          this.itemsCount.totalCount = Number(this.itemsCount.totalCount) - Number(this.items.num)
          this.itemsCount.totalPrice = 0
          if (this.itemsCount.gradeDiscountTotalPrice) { // 折扣活动后计算商品总价
            this.itemsCount.gradeDiscountTotalPrice = 0
          }
        }
      }
      this.$emit('limits', this.items.limit)
    }
  },
  mounted () {
    console.log(this.items)
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
