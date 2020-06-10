<template>
  <div class="shopcart-wrap" :class="{ hiddenHeader: isMiniProgram }" v-cloak>
    <THeader
      class="header-wrap"
      :title="'购物车'"
      :hasBg="true"
      @back="goBack"
    ></THeader>
    <!-- 购物车为空 -->
    <div class="shopcart-null" v-if="noPro">
      <NoData :flagType="'no-cart'"></NoData>
    </div>
    <!-- 购物车非空 -->
    <div v-else class="shopcart-content">
      <div class="item-title">
        <div class="item-title-l">
          <label class="label" @click="handleSelectAll()">
            <van-checkbox v-model="checkAll">全选</van-checkbox>
          </label>
        </div>

        <div class="item-title-r" @click="handleDeleteAll(allSelectedGoods)">
          <span class="sprite-icon icon-dashbox"></span>
        </div>
      </div>
      <div class="shopcart-main" :class="{ 'has-handle': goods.length > 0 }">
        <div class="pro-wrap">
          <div
            class="pro-list shop-list"
            v-for="(item, index) in mGoods"
            :key="index"
          >
            <div class="pro-detail" v-for="(v, k) in item" :key="k">
              <span class="checkbox isStock" v-if="v.isStockOut"></span>
              <span class="checkbox" v-else @click="handleSelectSingle(v)">
                <van-checkbox v-model="v.isChecked"></van-checkbox>
              </span>
              <template v-if="v.diy">
                <div class="pro-img">
                  <img :src="v.diy.previewImage" alt="" />
                </div>
                <div class="pro-intro">
                  <h5 class="pro-title">{{ v.itemName }}</h5>
                  <div class="pro-spe">
                    <span>规格：</span>
                    <p>{{ v.diy.modelName }}</p>
                    <p>{{ v.diy.materialName }}</p>
                  </div>
                  <!-- 金科 -->
                  <div class="pro-spe" v-show="distributorId === 1217">
                    <span>图片ID：</span>
                    <p>{{ v.diy.pictureId }}</p>
                  </div>
                  <p class="pro-price" v-if="orderSource !== 28">
                    ¥<span>{{ v.salePrice }}</span>
                  </p>
                </div>
              </template>
              <div class="isStock" v-if="v.isStockOut">
                <span>缺货</span>
              </div>
              <div class="stepper" v-else>
                <div
                  class="stepper-minus"
                  @click="changeNum(v.id, v.itemCount, -1)"
                >
                  <span class="sprite-icon icon-step_minus"></span>
                </div>
                <input
                  class="stepper-input"
                  type="number"
                  :value="v.itemCount"
                  readonly
                  :data-id="v.id"
                  @click="handleShowKeyboard"
                />
                <div
                  class="stepper-plus"
                  @click="changeNum(v.id, v.itemCount, 1)"
                >
                  <span class="sprite-icon icon-step_plus"></span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 失效商品 -->
        <div class="invalid-pro" v-show="invalidGoods.length > 0">
          <div class="item-title">
            <div class="item-title-l">失效商品</div>
            <span class="item-title-r text-r" @click="handleEmpty">
              清空失效商品
            </span>
          </div>
          <div class="pro-wrap">
            <div class="pro-list" v-for="item in invalidGoods" :key="item.id">
              <div class="pro-detail">
                <p class="invalid-label">失效</p>
                <template v-if="item.diy">
                  <div class="pro-img">
                    <img :src="item.diy.previewImage" alt="" />
                  </div>
                  <div class="pro-intro">
                    <h5 class="pro-title">{{ item.itemName }}</h5>
                    <div class="pro-spe">
                      <span>规格：</span>
                      <p>{{ item.diy.modelName }}</p>
                      <p>{{ item.diy.materialName }}</p>
                    </div>
                    <!-- 金科 -->
                    <div class="pro-spe" v-show="distributorId === 1217">
                      <span>图片ID：</span>
                      <p>{{ item.diy.pictureId }}</p>
                    </div>
                    <p class="pro-price" v-if="orderSource !== 28">
                      ¥<span>{{ item.salePrice }}</span>
                    </p>
                  </div>
                </template>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 底部操作栏 -->
      <div class="handle-wrap" v-if="goods.length > 0">
        <p class="total" v-if="orderSource !== 28">
          合计：<span class="price"><i>¥</i>{{ totalPrice || 0 }}</span>
        </p>
        <!-- <p class="total" v-else>
          共需使用<span class="num">{{ totalCount }}</span
          >张兑换卡
        </p> -->
        <div class="btn" @click="handleSubmit">
          {{ orderSource === 28 ? "立即兑换" : "结算" }}
        </div>
      </div>
    </div>

    <!-- 数字键盘 -->
    <van-number-keyboard
      v-model="value"
      :show="showKeyboard"
      extra-key="00"
      close-button-text="完成"
      @blur="showKeyboard = false"
      @input="handleKeyInput"
      @delete="handleDelInput"
      @close="handleCloseKeyboard"
    />

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/javascript">
// 组件
import THeader from "components/tHeader/tHeader";
import NoData from "components/noData/noData";
import Loading from "components/loading/loading";
// api
import api from "api/allApi.js";

export default {
  name: "Shopcart",
  data() {
    return {
      pid: 0,
      distributorId: "", // 分销商id
      flag: false,
      isTabIn: false, // 是否是点击tab进入
      noPro: false, // 购物车是否为空
      allSelectedGoods: [], // 已选商品
      goods: [], // 购物车商品
      mGoods: [], // 购物车拆单商品
      invalidGoods: [], // 失效商品
      showKeyboard: false, // 是否显示数字键盘
      value: "", // 数字键盘输入值
      originVal: "", // 输入前的值
      currentId: false, // 当前修改数量的商品id
      orderSource: "", // 订单来源
      isLoading: true, // 加载
      message: "数据加载中", // 加载内容
      checkAll: false, // 全选
      isMiniProgram: false, // 是否是小程序
    };
  },
  created() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "shopcart") {
      var params = this.getQueryVariable("enterParams");
      var enterParams = JSON.parse(params);
      localStorage.setItem("userId", enterParams.userId);
      localStorage.setItem("phone", enterParams.phone);
      localStorage.setItem("userNo", enterParams.userNo);
      localStorage.setItem("auth", enterParams.auth);
      localStorage.setItem("openId", enterParams.openid);
      localStorage.setItem("distributorId", enterParams.distributorId);
      localStorage.setItem("exchangeId", enterParams.exchangeId);
      localStorage.setItem("platform", enterParams.platform);
      localStorage.setItem("orderSource", enterParams.orderSource);

      this.pid = parseInt(enterParams.pid);

      this.isMiniProgram = true;
      sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
    } else {
      this.pid = parseInt(this.$route.query.pid);
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.distributorId = parseInt(localStorage.getItem("distributorId"));
    this.orderSource = parseInt(localStorage.getItem("orderSource")) || 28;

    this.initData();
  },
  methods: {
    getQueryVariable(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      } else {
        return null;
      }
    },
    // 初始化数据
    initData() {
      this.pid = parseInt(this.$route.query.pid);
      this.$api.get(this, api.getShopcartList).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.noPro = false;
            let goodsData = res.data;
            // 未返回价格，默认设置为0
            goodsData.forEach((item) => {
              if (!item.salePrice) {
                this.$set(item, "salePrice", 0);
              }
            });

            // 根据 openFlag 判断商品是否失效（1 有效，0 无效）
            let validData = goodsData.filter((item) => {
              return item.openFlag;
            });
            let invalidData = goodsData.filter((item) => {
              return !item.openFlag;
            });
            this.goods = validData.sort(this.dataDown);
            this.invalidGoods = invalidData; // 失效商品

            // 设置是否选中属性
            this.goods.forEach((v, k) => {
              this.$set(v, "isChecked", false);

              if (this.pid === v.id) {
                this.$set(v, "isChecked", true);
              }
            });

            // 根据货品编码分组显示
            this.mGoods = this.arrayGroupBy(this.goods, "itemCode");

            // 设置默认选中项，加入购物车
            this.allSelectedGoods = [];
            if (this.pid) {
              this.allSelectedGoods.push(this.pid);
            }
          } else {
            this.noPro = true;
          }
          this.isLoading = false;
          this.message = "";
        } else {
          this.isLoading = false;
          this.message = "";
          this.$toast.fail(res.errMessage);
        }
      });
    },
    // 增减数量
    changeNum(k, num, type) {
      // 如果是减少，要判断 > 1，最少是1
      if (type === -1) {
        this.goods.forEach((item) => {
          if (k === item.id) {
            if (item.itemCount > 1) {
              item.itemCount = parseInt(item.itemCount) + type;
              // 更新数据
              this.refreshData(k, item.goodsType, item.itemCount);
            } else {
              this.handleDelete([k]);
            }
          }
        });
      } else {
        this.goods.forEach((item) => {
          if (k === item.id) {
            // 设置商品增加上限为 1000
            if (item.itemCount < 1000) {
              item.itemCount = parseInt(item.itemCount) + parseInt(type);
              // 更新数据
              this.refreshData(k, item.goodsType, item.itemCount);
            } else {
              this.$this.$toast("不能购买更多商品了~");
              return false;
            }
          }
        });
      }
    },
    // 更新数据
    refreshData(id, goodsType, itemCount) {
      this.$api
        .put(this, api.updataShopcart, {
          id: id,
          goodsType: goodsType,
          itemCount: itemCount,
          itemType: 1, // 是否赠品：1 非赠品，2 赠品
        })
        .then((res) => {
          if (!res.success) {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 显示数字键盘
    handleShowKeyboard(e) {
      this.currentId = Number(e.currentTarget.dataset.id);
      this.showKeyboard = true;
      this.goods.forEach((good) => {
        if (good.id === this.currentId) {
          this.originVal = good.itemCount;
        }
      });

      this.value = "";
    },
    // 数字键盘输入
    handleKeyInput(key) {
      this.value = this.value + "" + key;
      if (Number(this.value) >= 1000) {
        this.$toast("不能购买更多商品了~");
        this.value = 1000;
      }

      if (Number(this.value) <= 0) {
        this.$toast("购买数量不能小于1~");
        this.value = 1;
      }

      this.goods.forEach((good) => {
        if (good.id === this.currentId) {
          this.$set(good, "itemCount", this.value);
        }
      });
    },
    // 数字键盘删除
    handleDelInput() {
      this.goods.forEach((good) => {
        if (good.id === this.currentId) {
          this.value = good.itemCount + "";
          this.value = this.value.substring(0, this.value.length - 1);
          this.$set(good, "itemCount", this.value);
        }
      });
    },
    // 关闭数字键盘
    handleCloseKeyboard() {
      this.showKeyboard = false;

      let curGoodsTye = "";
      let curItemCount = "";
      this.goods.forEach((good) => {
        if (good.id === this.currentId) {
          if (good.itemCount === 0) {
            this.$set(good, "itemCount", this.originVal);
          }

          curGoodsTye = good.goodsType;
          curItemCount = good.itemCount;
        }
      });

      this.refreshData(this.currentId, curGoodsTye, curItemCount);
    },
    // 全选
    handleSelectAll() {
      if (this.checkAll) {
        this.checkAll = false;
      } else {
        this.checkAll = true;
      }
      this.allSelectedGoods = [];
      this.goods.forEach((v, k) => {
        if (this.checkAll) {
          if (!v.isStockOut) {
            this.allSelectedGoods.push(v.id);
          }
          this.$set(v, "isChecked", true);
        } else {
          this.$set(v, "isChecked", false);
        }
      });
    },
    // 单选
    handleSelectSingle(item) {
      if (item.isChecked) {
        this.checkAll = false;
        this.$set(item, "isChecked", false);
      } else {
        this.$set(item, "isChecked", true);
      }
      this.allSelectedGoods = [];
      this.goods.forEach((v, k) => {
        if (!v.isStockOut && v.isChecked) {
          this.allSelectedGoods.push(v.id);
        }
      });

      if (this.allSelectedGoods.length >= this.goods.length) {
        this.checkAll = true;
      }
    },
    // 结算
    handleSubmit() {
      if (this.allSelectedGoods.length > 0) {
        // 结算提示
        this.$dialog
          .confirm({
            title: "温馨提示",
            message:
              "您的勾选存在专属定制的商品，非质量问题不支持无理由退换喔，是否继续提交结算？",
            className: "confirm-v-dialog tl",
            confirmButtonText: "确认提交",
            cancelButtonText: "我再想想",
            confirmButtonColor: "#333333",
            cancelButtonColor: "#999999",
          })
          .then(() => {
            let goodsArr = [];

            this.goods.forEach((good) => {
              if (Number(this.orderSource) === 28) {
                // 立即兑换
                good.secretCodes = "";
              }
              if (this.allSelectedGoods.indexOf(good.id) >= 0) {
                goodsArr.push(good);
              }
            });
            localStorage.setItem("goods", JSON.stringify(goodsArr));
            this.$router.push("/order");
          })
          .catch((error) => {
            console.log(error);
          });
      } else {
        this.$toast("请选择需要购买的商品");
      }
    },
    // 删除确认
    handleDelete(ids) {
      this.$dialog
        .confirm({
          message: "确定要删除选中的商品吗？",
          className: "confirm-v-dialog",
          confirmButtonColor: "#333333",
          cancelButtonColor: "#999999",
        })
        .then(() => {
          this.deleteData(ids);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 删除数据
    deleteData(ids) {
      this.$api
        .delete(this, api.deleteShopcart, {
          ids: ids,
        })
        .then((res) => {
          if (res.success) {
            this.$toast("删除成功！");
            this.initData();
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch(() => {});
    },
    // 删除全部
    handleDeleteAll(ids) {
      if (ids.length <= 0) {
        this.$toast("未选择任何商品~");
        return false;
      } else {
        this.handleDelete(ids);
      }
    },
    // 清空失效
    handleEmpty() {
      var ids = [];
      this.invalidGoods.forEach((v, k) => {
        ids.push(v.id);
      });
      this.$dialog
        .confirm({
          message: "确定要清空失效商品吗？",
          className: "confirm-v-dialog",
          confirmButtonColor: "#333333",
          cancelButtonColor: "#999999",
        })
        .then(() => {
          this.deleteData(ids);
        })
        .catch(() => {});
    },
    // 倒序
    dataDown(x, y) {
      return y.id - x.id;
    },
    // 分组函数
    groupBy(array, f) {
      let groups = {};
      array.forEach((o) => {
        var group = JSON.stringify(f(o));
        groups[group] = groups[group] || [];
        groups[group].push(o);
      });
      return Object.keys(groups).map((group) => {
        return groups[group];
      });
    },
    arrayGroupBy(list, groupId) {
      let sorted = this.groupBy(list, (item) => {
        return [item[groupId]];
      });
      return sorted;
    },
    // 返回首页
    goBack() {
      this.$router.replace("/index");
    },
  },
  computed: {
    // totalPrice 计算总价
    totalPrice() {
      var totalprice = 0;
      this.goods.forEach((v, k) => {
        if (this.allSelectedGoods.indexOf(v.id) !== -1) {
          totalprice += v.salePrice * v.itemCount;
        }
      });
      return totalprice;
    },
    // totalCount 需要兑换卡数量
    totalCount() {
      var count = 0;
      this.goods.forEach((v, k) => {
        if (this.allSelectedGoods.indexOf(v.id) !== -1) {
          count += v.itemCount;
        }
      });
      return count;
    },
  },
  components: {
    THeader,
    NoData,
    Loading,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.checkbox {
  &.isStock {
    check-style();
    background-color: $color-bg;
    border: 1px solid $color-bg;
  }
}

.shopcart-wrap {
  position: fixed;
  width: 100%;
  top: 44px;
  bottom: 0;
  background-color: $bg;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .shopcart-content {
    position: relative;
    height: 100%;

    .item-title {
      height: 48px;
      padding: 16px $spacing-lg;
      font-size: $font-base;
      color: $color-font-base;
      background-color: $color-bg;

      .title {
        display: inline-block;
      }

      .van-icon {
        top: 1px;
      }

      .item-title-l {
        float: left;

        .checkbox {
          position: relative;
          top: -1px;
        }
      }

      .item-title-r {
        float: right;
        padding-right: 5px;

        .icon-dashbox {
          top: -2px;
          transform: scale(0.8);
        }

        &.text-r {
          padding-right: 0;
          font-size: $font-base;
          color: $color-orange;
        }
      }
    }

    .shopcart-main {
      height: calc(100% - 55px);
      overflow-y: scroll;
      -webkit-overflow-scrolling: touch;

      &::-webkit-scrollbar {
        display: none;
      }

      &.has-handle {
        padding-bottom: $spacing-base;
        height: calc(100% - 100px);
      }

      .pro-wrap {
        display: block;

        .checkbox {
          position: absolute;
          top: 50%;
          left: $spacing-base;
          transform: translateY(-50%);
        }
      }

      .pro-img {
        position: relative;
        float: left;
        width: 70px;
        height: 129px;
        margin-right: 24px;
        overflow: hidden;

        img {
          position: absolute;
          display: inline-block;
          top: 50%;
          left: 50%;
          max-width: 100%;
          max-height: 100%;
          transform: translate(-50%, -50%);
        }
      }

      .pro-intro {
        position: relative;
        padding-left: 98px;
        height: 129px;

        .pro-title {
          padding-top: 8px;
          font-size: $font-base;
          color: $color-font-base;
          overflow: hidden;
          -webkit-line-clamp: 2;
          line-clamp: 2;
          -webkit-box-orient: vertical;
          box-orient: vertical;
          display: -webkit-box;
          display: box;
        }

        .pro-spe {
          position: relative;
          margin-top: 8px;
          padding-left: 3.5em;
          font-size: $font-sm;
          color: $color-font-grey;
          line-height: 14px;

          span {
            position: absolute;
            top: 0;
            left: 0;
          }
        }

        .pro-price {
          position: absolute;
          bottom: 0;
          left: 0;
          font-size: $font-sm;
          color: $red;

          span {
            font-size: $font-lg;
          }
        }
      }

      .stepper {
        position: absolute;
        right: $spacing-lg;
        bottom: 31px;
        width: 120px;
        height: 30px;
        font-size: $font-lg;
        color: $color-font-base;
        background-color: $color-bg-white;
        border-radius: $radius-xs;
        border: 1px solid $color-line-opacity;

        .stepper-input {
          float: left;
          width: 49px;
          height: 100%;
          align(center);
          border-left: 1px solid $color-line-opacity;
          border-right: 1px solid $color-line-opacity;
        }

        .stepper-minus, .stepper-plus {
          position: relative;
          float: left;
          width: 34px;
          height: 100%;

          .sprite-icon {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
          }
        }
      }

      .isStock {
        position: absolute;
        right: $spacing-lg;
        bottom: 31px;
        font-size: $font-base;
        color: $color-orange;
      }

      .pro-detail {
        position: relative;
        padding: $spacing-lg $spacing-lg $spacing-lg $spacing-base;
        background-color: $color-font-white;
        border-radius: 4px;
      }

      .pro-list {
        &.shop-list {
          margin-bottom: $spacing-base;
          padding: 0 $spacing-base;

          .pro-detail {
            padding-left: 46px;
            border-top: 1px solid $bg;
            border-radius: $radius-base;

            & + .pro-detail {
              margin-top: $spacing-base;
            }
          }
        }
      }

      .invalid-pro {
        margin: 0 $spacing-lg;
        background-color: $color-bg-white;
        border-radius: $radius-base;
        overflow: hidden;

        .item-title {
          height: 50px;
          padding-bottom: $spacing-sm;
          background-color: $color-bg-white;
        }

        .invalid-label {
          position: absolute;
          top: 50%;
          left: $spacing-base;
          transform: translateY(-50%);
          width: 40px;
          height: 24px;
          font-size: $font-sm;
          color: $color-font-darkGrey;
          align(center);
          lineHeight(24px);
          background-color: $color-bg-input;
          border-radius: 20px;
        }

        .pro-img {
          opacity: $opacity-base;
        }

        .pro-detail {
          padding-left: 65px;

          .pro-title {
            color: $color-font-grey;
          }
        }
      }

      .no-more {
        padding: 10px 0;
        font-size: 6px;
        color: $lightBlack;
        text-align: center;
      }
    }

    .handle-wrap {
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;
      padding: $spacing-sm $spacing-base;
      text-align: right;
      background-color: $color-bg-white;
      box-shadow: $color-bt-shadow;
      z-index: 99;

      .btn {
        display: inline-block;
        width: 165px;
        lineHeight(45px);
        align(center);
        font-size: $font-lg;
        background: $color-main;
        border-radius: 45px;
      }

      .total {
        display: inline-block;
        margin-right: $spacing-base;
        width: calc(100% - 190px);
        font-size: $font-sm;
        color: $color-font-grey;
        align(center);

        .price {
          color: $color-orange;

          em {
            font-style: normal;
          }
        }

        .num {
          padding: 0 3px;
          color: $color-orange;
        }
      }
    }
  }

  .shopcart-null {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: $color-bg-white;
  }
}

[v-cloak] {
  display: none !important;
}

.van-overlay {
  background-color: rgba(0, 0, 0, 0.5);
}

.confirm-v-dialog {
  width: 90%;
  border-radius: 8px;

  &.tl {
    .van-dialog__message {
      padding: 12px 24px;
      font-size: $font-base;
      color: $color-font-darkGrey;
      text-align: left;
      line-height: 24px;
    }

    .van-dialog__header {
      padding-left: 11px;
      line-height: 25px;
      // bg-image('remind');
      background-size: 20px 20px;
      background-position: calc(50% - 44px) 20px !important;
    }
  }

  .van-dialog__header {
    padding-top: 18px;
    font-size: $font-lg;
    color: $color-font-base;
  }

  .van-button--default {
    font-size: $font-lg;
    color: $color-font-grey;
  }
}

.van-number-keyboard {
  z-index: 100;
}

.van-checkbox {
  color: $color-font-grey;

  >>>.van-checkbox__icon {
    font-size: $font-lg;

    &.van-checkbox__icon--checked {
      .van-icon {
        color: $color-font-base;
        background-color: transparent;
        border-color: $color-font-base;
      }
    }

    .van-icon {
      width: 16px;
      height: 16px;
    }
  }
}
</style>
