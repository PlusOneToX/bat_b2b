<template>
  <div class="shopcart-wrap" v-cloak>
    <v-header :back="true" :title="title"></v-header>
    <!-- 购物车为空 -->
    <div class="shopcart-null fixed" v-if="noPro">
      <img src="../../common/images/shopcart-null.png" alt="空空如也～" />
      <p>空空如也～</p>
    </div>
    <!-- 购物车非空 -->
    <div v-else class="shopcart-content">
      <div class="item-title">
        <div class="item-title-l" @click="goTo()">
          <i class="icon-shop"></i>
          <div class="title">定制商城</div>
          <van-icon name="arrow" />
        </div>
        <template v-if="goods.length > 0">
          <span
            class="item-title-r"
            @click="editStatus = !editStatus"
            v-show="!editStatus"
          >
            编辑
          </span>
          <span
            class="item-title-r"
            @click="editStatus = !editStatus"
            v-show="editStatus"
          >
            完成
          </span>
        </template>
      </div>
      <div class="shopcart-main" :class="{ 'has-handle': goods.length > 0 }">
        <!-- 商品列表为空 -->
        <div class="shopcart-null" v-if="goods.length <= 0">
          <img src="../../common/images/shopcart-null.png" alt="空空如也～" />
          <p>空空如也～</p>
        </div>
        <!-- 商品列表非空 -->
        <div class="pro-wrap" v-else>
          <div
            class="pro-list shop-list"
            v-for="(item, index) in mGoods"
            :key="index"
          >
            <div class="pro-detail" v-for="(v, k) in item" :key="k">
              <span class="checkbox isStock" v-if="v.isStockOut"></span>
              <input
                v-else
                type="checkbox"
                class="checkbox"
                @click="handleSelectSingle(v.id)"
                :checked="allSelectedGoods.indexOf(v.id) >= 0"
              />
              <template v-if="v.diy">
                <div class="pro-img">
                  <img :src="v.diy.previewImage" alt="" />
                </div>
                <div class="pro-intro">
                  <h5 class="pro-title">{{ v.itemName }}</h5>
                  <p class="pro-spe">
                    规格：{{ v.diy.modelName }} - {{ v.diy.materialName }}
                  </p>
                  <!--金科-->
                  <p v-show="distributorId === 1217">
                    图片ID：{{ v.diy.pictureId }}
                  </p>
                  <p class="pro-price">
                    ￥<span>{{ v.salePrice }}</span>
                  </p>
                </div>
              </template>
              <div class="isStock" v-if="v.isStockOut">
                <span>缺货</span>
              </div>
              <div class="stepper" v-else>
                <span
                  class="stepper-minus"
                  @click="changeNum(v.id, v.itemCount, -1)"
                  >-</span
                >
                <input
                  class="stepper-input"
                  type="number"
                  :value="v.itemCount"
                  readonly
                  :data-id="v.id"
                  @click="handleShowKeyboard"
                />
                <span
                  class="stepper-plus"
                  @click="changeNum(v.id, v.itemCount, 1)"
                  >+</span
                >
              </div>
            </div>
          </div>
        </div>

        <!-- 数字键盘  -->
        <van-number-keyboard
          v-model="value"
          :show="showKeyboard"
          close-button-text="完成"
		  theme="custom" 
          @blur="showKeyboard = false"
          @input="handleKeyInput"
          @delete="handleDelInput"
          @close="handleCloseKeyboard"
        />

        <!-- 失效商品 -->
        <div class="invalid-pro" v-show="invalidGoods.length > 0">
          <div class="item-title">
            <div class="item-title-l">失效商品</div>
            <span class="item-title-r red" @click="handleEmpty"> 清空 </span>
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
                    <p class="pro-spe">
                      规格：{{ item.diy.modelName }} -
                      {{ item.diy.materialName }}
                    </p>
                    <!--金科-->
                    <p v-show="distributorId === 1217">
                      图片ID：{{ item.diy.pictureId }}
                    </p>
                    <p class="pro-price">
                      ￥<span>{{ item.salePrice }}</span>
                    </p>
                  </div>
                </template>
              </div>
            </div>
          </div>
        </div>

        <!-- 没有更多了 -->
        <p class="no-more">没有更多啦～</p>
      </div>
      <!-- 底部操作栏 -->
      <div class="handle-wrap" v-if="goods.length > 0">
        <label class="label">
          <input
            type="checkbox"
            class="checkbox"
            @click="handleSelectAll()"
            :checked="allSelectedGoods.length == goods.length && goods"
          />
          全选
        </label>
        <van-button
          round
          type="info"
          color="#0076A5"
          v-show="!editStatus"
          @click="handleSubmit"
          >结算</van-button
        >
        <van-button
          round
          type="info"
          color="#EF6B52"
          @click="handleDeleteAll(allSelectedGoods)"
          v-show="editStatus"
          >删除
        </van-button>
        <p class="price-total">
          合计：<span class="red"><i>￥</i>{{ totalPrice || 0 }}</span>
        </p>
      </div>
    </div>
	
	<!-- 柔性关闭弹窗 -->
	<div class="flexible-dialog" v-show="showFlexible">
	  <div class="flexible-dialog-content">
	      <div class="flexible-cotent-top">
	      	<img class="flexible-cotent-top-img" src="../../assets/images/flexible-logo.png" mode="widthFix"/>
	      </div>
		  
		  <div class="flexible-cotent-middle-title">
		  	 通知提醒
		  </div>
		  <div class="flexible-cotent-middle-info">
		  	尊敬的客户，本商城将于6月30日停止运营，仅保留订单查询服务。感谢您的信任和支持。
		  </div>
		  <div class="flexible-cotent-bottom" @click="clickFlexible">
		  	 知道了
		  </div>
	  </div>
	</div>
	
    <!-- 底部tab -->
    <tabs :curTab="'shopcart'" :curVersion="curVersion" :userNo="userNo"></tabs>
  </div>
</template>

<script type="text/javascript">
import Tabs from "view/components/tabs.vue";
import api from "common/js/allApi.js";
import { getLocalStorageItem } from "common/js/common";
export default {
  name: "shopcart",
  data() {
    return {
      title: "购物车",
      pid: 0,
      distributorId: "",
      flag: false,
      isTabIn: false, // 是否是点击tab进入
      noPro: true, // 购物车是否为空
      editStatus: false, // 是否是编辑状态
      allSelectedGoods: [], // 已选商品
      allChecked: false, // 全选
      goods: [], // 购物车商品
      mGoods: [], // 购物车拆单商品
      invalidGoods: [], // 失效商品
      showKeyboard: false, // 是否显示数字键盘
      value: "", // 数字键盘输入值
      originVal: "", // 输入前的值
      currentId: false, // 当前修改数量的商品id
      curVersion: "",
      userNo: "",
			
	  distributorId: "", // 分销商ID
	  showFlexible: false //是否显示柔性关闭弹窗
    };
  },
  components: {
    "v-header": (resolve) =>
      require(["@/components/v-header/v-header"], resolve),
    Tabs,
  },
  created() {
    this.curVersion = localStorage.getItem("curVersion"); // 获取当前进入版本
    this.userNo = getLocalStorageItem("userNo");
    this.initData();
  },
  methods: {
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
			
			// 设置默认选中项，加入购物车
			this.allSelectedGoods = [];

            // 设置是否选中属性
			if(this.pid){
				this.allSelectedGoods.push(this.pid);
			}

            // 根据货品编码分组显示
            this.mGoods = this.arrayGroupBy(this.goods, "itemCode");

          } else {
            this.noPro = true;
          }
		  
		  console.log(this.goods);
		  console.log(this.allSelectedGoods);
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
              this.$toast("不能购买更多商品了~");
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
            this.$toast(res.errMessage);
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
	  console.log("输入: " + key);
      this.value = (this.value + key).replace(/^0|\D/g,"");
	  if(Number(this.value) <= 0){
		this.$toast("购买商品最低数量需为1");
		this.value = 1;
	  }
	  
      if (Number(this.value) >= 1000) {
        this.$toast("不能购买更多商品了~");
        this.value = 1000;
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
		  if(this.value == ""){
			this.$toast("商品数量最低为1");
			this.value = 1
		  }
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
      if (!event.currentTarget.checked) {
		this.allSelectedGoods = [];
      } else {
		this.allSelectedGoods = []; // 先置空，然后再重新添加
		this.goods.forEach((v, k) => {
		  if (!v.isStockOut) {
		    this.allSelectedGoods.push(v.id);
		  }
		});
      }
    },
    // 单选
    handleSelectSingle(id) {
      if (event.currentTarget.checked) {
        this.allSelectedGoods.push(id);
      } else {
        for (var i = 0; i < this.allSelectedGoods.length; i++) {
          if (id === this.allSelectedGoods[i]) {
            this.allSelectedGoods.splice(i, 1);
			break;
          }
        }
      }
    },
    // 结算
    handleSubmit() {
		 if (this.allSelectedGoods.length > 0 && this.totalPrice > 0) {
		   // 结算提示
		   this.$dialog
			.confirm({
			  title: "温馨提示",
			  message:
				"您的勾选存在专属定制的商品，非质量问题不支持无理由退换喔，是否继续提交结算？",
			  className: "confirm-v-dialog tl",
			  confirmButtonText: "确认提交",
			  confirmButtonColor: "#0076A5",
			  cancelButtonText: "我再想想",
			})
			.then(() => {
			  let goodsArr = [];
			  this.goods.forEach((good) => {
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
	  if (ids.length <= 0) {
		this.$toast("请选择要删除的商品~");
	  }else{
		this.$dialog
		  .confirm({
		    message: "确定要删除选中的商品吗?",
		    className: "confirm-v-dialog",
		    confirmButtonColor: "#0076A5",
		  })
		  .then(() => {
		    this.deleteData(ids);
		  })
		  .catch((error) => {
		    console.log(error);
		  });  
	  }
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
			this.$route.query.pid = 0;
            this.initData();
          } else {
            this.$toast(res.errMessage);
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
          confirmButtonColor: "#0076A5",
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
    // 页面跳转
    goTo(page) {
      if (this.curVersion === "new") {
        this.$router.push("/recommend");
      } else {
        this.$router.push("/index");
      }
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
	
	//点击知道了隐藏柔性关闭弹窗
	clickFlexible(){
	  this.showFlexible = false;
	}
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
      return totalprice.toFixed(2);
    },
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '../../common/styles/mixin.styl';
@import '../../common/styles/variable.styl';

$bg = #F6F6F6;
$white = #fff;
$black = #000;
$lightBlack = #333;
$lighterBlack = #4A4A4A;
$gray = #979797;
$red = #D0021B;
$blue = #1CB8CE;
$darkBlue = #0076A5;
$inputBg = #EFEFEF;
$labelBg = #EDEDED;

.checkbox {
  width: 20px;
  height: 20px;
  border: 1px solid #cccccc;
  border-radius: 50%;

  &.isStock {
    width: 18px;
    height: 18px;
    background-color: #cccccc;
  }

  &:checked {
    position: relative;
    border-color: $darkBlue;
    background-color: $darkBlue;

    &::after {
      content: '';
      position: absolute;
      top: 5px;
      left: 4px;
      display: inline-block;
      width: 9px;
      height: 4px;
      border-bottom: 2px solid $white;
      border-left: 2px solid $white;
      transform: rotate(-45deg);
    }
  }
}

.shopcart-wrap {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 50px;
  background-color: $bg;

  .shopcart-content {
    position: relative;
    width: 100%;
    height: 100%;

    .item-title {
      height: 46px;
      line-height: 46px;
      padding: 0 12px;
      font-size: 16px;
      box-sizing: border-box;
      color: $black;
      background-color: $white;

      .icon-shop {
        display: inline-block;
        width: 18px;
        height: 18px;
        position: relative;
        top: 4px;
        bg-image('shop');
        background-size: 18px 18px;
      }

      .title {
        display: inline-block;
        font-size: 16px;
        color: $black;
      }

      .van-icon {
        top: 2px;
      }

      .item-title-l {
        float: left;
      }

      .item-title-r {
        float: right;
        padding-right: 10px;

        &.red {
          padding-right: 0;
          color: $red;
        }
      }
    }

    .shopcart-main {
      width: 100%;
      height: calc(100% - 46px);
      box-sizing: border-box;
      overflow-y: scroll;
      -webkit-overflow-scrolling: touch;

      &::-webkit-scrollbar {
        display: none;
      }

      &.has-handle {
        height: calc(100% - 102px);
      }

      .pro-wrap {
        display: block;
      }

      .pro-list {
        &.shop-list {
          margin-top: 16px;
          padding: 0 12px;

          .pro-detail {
            padding-left: 32px;
            border-top: 1px solid $bg;
            border-radius: 0;

            &+.pro-detail {
              margin-top: 0;
            }

            &:first-child {
              border-radius: 8px 8px 0 0;
            }

            &:last-child {
              border-radius: 0 0 8px 8px;
            }

            &:first-child:last-child {
              border-radius: 8px;
            }
          }

          .pro-img {
            position: relative;
            width: 90px;
            height: 90px;
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
            height: 90px;

            .pro-price {
              left: 100px;
            }

            .pro-spe {
              font-size: 12px;
              line-height: 16px;
            }
          }
        }

        .pro-detail {
          position: relative;
          padding: 16px 8px 16px 52px;
          background-color: $white;
          border-radius: 8px;

          &+.pro-detail {
            margin-top: 12px;
          }
        }

        .checkbox {
          position: absolute;
          top: 50%;
          left: 8px;
          transform: translateY(-50%);
        }

        .invalid-label {
          position: absolute;
          top: 50%;
          left: 8px;
          transform: translateY(-50%);
          width: 40px;
          height: 18px;
          font-size: 10px;
          color: $gray;
          text-align: center;
          line-height: 18px;
          background-color: $inputBg;
          border-radius: 20px;
        }

        .pro-img {
          position: relative;
          float: left;
          width: 80px;
          height: 80px;
          margin-right: 10px;

          img {
            position: absolute;
            display: inline-block;
            left: 50%;
            width: auto;
            height: 100%;
            transform: translateX(-50%);
          }
        }

        .pro-intro {
          position: relative;
          height: 80px;

          h5 {
            font-size: 14px;
            color: $lightBlack;
            line-height: 18px;
            overflow: hidden;
            -webkit-line-clamp: 2;
            line-clamp: 2;
            -webkit-box-orient: vertical;
            box-orient: vertical;
            display: -webkit-box;
            display: box;
          }

          p {
            margin-top: 6px;
            font-size: 12px;
            color: $lightBlack;
          }

          .pro-price {
            position: absolute;
            bottom: 0;
            left: 90px;
            color: $red;

            span {
              font-size: 16px;
            }
          }
        }

        .stepper {
          position: absolute;
          right: 8px;
          bottom: 16px;

          .stepper-input {
            margin: 0 10px;
            width: 43px;
            height: 20px;
            font-size: 14px;
            color: $lighterBlack;
            text-align: center;
            background-color: $inputBg;
            border-radius: 2px;
          }

          .stepper-minus, .stepper-plus {
            position: relative;
            top: 2px;
            font-size: 24px;
            color: $gray;
            background-color: $white;
          }

          .stepper-minus {
            font-size: 28px;
          }
        }

        .isStock {
          position: absolute;
          right: 15px;
          bottom: 16px;
          font-size: 14px;
          color: $red;
        }
      }

      .invalid-pro {
        margin-top: 18px;
        padding: 0 12px;

        .item-title {
          border-top-left-radius: 8px;
          border-top-right-radius: 8px;
        }

        .pro-wrap {
          border-bottom-left-radius: 8px;
          border-bottom-right-radius: 8px;
          overflow: hidden;
        }

        .pro-detail {
          border-radius: 0;
        }
      }

      .no-more {
        padding: 20px 0;
        font-size: 12px;
        color: $lightBlack;
        text-align: center;
      }
    }

    .handle-wrap {
      position: absolute;
      right: 0;
      bottom: 0;
      left: 0;
      height: 40px;
      padding: 8px 12px 8px 20px;
      line-height: 40px;
      background-color: $white;

      .label {
        font-size: 14px;
        color: $lighterBlack;
      }

      .checkbox {
        float: left;
        position: relative;
        top: 10px;
        margin-right: 5px;
      }

      .van-button {
        float: right;
        width: 96px;
        height: 40px;
        line-height: 1;
      }

      .price-total {
        float: right;
        font-size: 12px;
        color: $lighterBlack;
        margin-right: 12px;

        .red {
          font-size: 16px;
          color: $red;

          i {
            font-size: 12px;
            font-style: normal;
          }
        }
      }
    }
  }

  .shopcart-null {
    width: 50%;
    margin: 40px auto 60px;

    &.fixed {
      position: fixed;
      top: 130px;
      left: 50%;
      width: 70%;
      transform: translateX(-50%);
    }

    img {
      width: 100%;
    }

    p {
      margin-top: 6px;
      font-size: 14px;
      color: $blue;
      text-align: center;
    }
  }
}

.flexible-dialog{
  	position: fixed;
  	top: 0;
  	left: 0;
  	width: 100%;
  	height: 100%;
  	background-color: rgba(0, 0, 0, 0.5);
  	z-index: 1000;
	display: flex;
	justify-content center;
	align-items center;
	.flexible-dialog-content {
	  position: absolute;
	  width: 90%;
	  background-size: 100% 100%;
	  height: 390px;
	  background-color: #ffffff;
	  border-radius: 12px
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  flex-direction: column;
	  .flexible-cotent-top{
		  width: 90%;
		  height: 100px;
	      display: flex;
		  align-items: center;
		  justify-content: center;
		  .flexible-cotent-top-img{
			  width: 100%;
			  height: auto;
		  }
	  }
	  
	  .flexible-cotent-middle-title{
		  width: 85%;
		  height: 50px;
	      display: flex;
		  align-items: center;
		  justify-content: center;
		  margin-top: 20px;
		  font-size: 18px;
		  color: #333333;
		  font-weight: bold;
	  }
	  
	  .flexible-cotent-middle-info{
		  width: 85%;
	      display: flex;
		  justify-content space-between;
		  font-size: 15px;
		  color: #666666;
		  letter-spacing: 0.8px;
		  margin-top: 5px;
		  line-height: 22.5px;
		  text-align: center;
	  }
	  
	  .flexible-cotent-bottom{
		  width: 195px;
		  height: 43px;
		  line-height: 43px;
		  text-align: center;
	      display: flex;
		  justify-content: center;
		  font-size: 18px;
		  color: #ffffff;
		  background-color: #0076A5;
		  border-radius: 21.5px
		  margin-top: 40px;
	  }
	}
}
</style>
<style lang="stylus" rel="stylesheet/stylus">
@import '../../common/styles/mixin.styl';

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
      font-size: 14px;
      color: #666666;
      text-align: left;
      line-height: 24px;
    }

    .van-dialog__header {
      padding-left: 11px;
      line-height: 25px;
      bg-image('remind');
      background-size: 20px 20px;
      background-position: calc(50% - 44px) 20px !important;
    }
  }

  .van-dialog__header {
    padding-top: 18px;
    font-size: 18px;
    color: #4A4A4A;
  }

  .van-button--default {
    font-size: 18px;
    color: #4A4A4A;
  }
}
</style>
