<template>
  <div
    class="address-wrapper"
    :class="{ 'ry-style': Number(distributorId) === 4378 }"
  >
    <v-header :back="true" :title="title" @back="toback"></v-header>
    <div class="content" v-show="addressList.length > 0">
      <div class="address-list">
        <div
          class="address-item"
          v-for="(item, index) in addressList"
          :key="item.id"
          @click="chooseAddr(item.id)"
          :class="{ curIndex: orderFlag && index === 0 }"
        >
          <input
            type="radio"
            class="radio"
            checked
            v-if="orderFlag && index === 0"
          />
          <div class="name-tel">
            <span class="name">{{ item.userName }}</span>
            <span class="tel">{{ item.phone }}</span>
          </div>
          <div class="address">
            <div class="info">
              <span class="tag" v-show="item.defaultFlag === 1">默认</span
              ><span class="text">{{ item.detail }}</span>
            </div>
            <i class="iconfont icon icon-c-edit" @click.stop="onEdit(item)"></i>
          </div>
        </div>
      </div>
      <div class="btn-add" @click="onAdd">
        <van-icon name="plus" v-show="Number(distributorId) !== 4378"></van-icon
        >添加收货地址
      </div>
    </div>
    <!----无数据---->
    <div class="no-content" v-show="addressList.length <= 0">
      <div class="context">
        <div class="empty-img"></div>
        <div class="text">暂无收货地址</div>
        <div class="btn-add" @click="onAdd">
          <van-icon
            name="plus"
            v-show="Number(distributorId) !== 4378"
          ></van-icon
          >添加收货地址
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import { getLocalStorageItem } from "common/js/common";
import api from "common/js/allApi.js";
export default {
  data() {
    return {
      title: "地址管理",
      userId: "",
      chosenAddressId: "1",
      addressList: [],
      orderFlag: "", // 进入路径：order 订单进入
      addrId: "", // 地址ID
      page: 1,
      size: 10,
      distributorId: "", // 分销商ID
    };
  },
  mounted() {
    this.orderFlag = this.$route.query.orderFlag;
    this.addrId = this.$route.query.addrId;
    this.userId = getLocalStorageItem("userId");

    this.distributorId = parseInt(localStorage.getItem("distributorId"));

    if (this.userId) {
      this.$api
        .get(this, api.getAddrList, {
          id: this.userId,
          page: this.page,
          size: this.size,
        })
        .then((res) => {
          if (res.success) {
            if (res.data.list && res.data.list.length > 0) {
              res.data.list.forEach((item) => {
                this.$set(
                  item,
                  "detail",
                  item.provinceName +
                    item.cityName +
                    item.districtName +
                    item.address
                );
                if (this.orderFlag === "order" && this.addrId == item.id) {
                  this.addressList.unshift(item);
                } else {
                  this.addressList.push(item);
                }
              });
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    }
  },
  methods: {
    onAdd() {
      this.$router.replace({
        path: "/editAddress",
        query: {
          orderFlag: this.orderFlag,
        },
      });
    },
    onEdit(item) {
      this.$router.replace({
        path: "/editAddress",
        query: {
          id: item.id,
          orderFlag: this.orderFlag,
          addrInfo: JSON.stringify(item),
        },
      });
    },
    toback() {
      this.$router.go(-1);
    },
    // 选择地址
    chooseAddr(id) {
      if (this.orderFlag === "order") {
        this.$router.push({
          path: "/order",
          query: {
            addrId: id,
          },
        });
      }
    },
  },
  components: {
    VHeader,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '../../common/styles/mixin.styl';

.address-wrapper {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 0;
  background-color: #F6F6F6;
  overflow-y: scroll;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }

  .content {
    width: 100%;
    height: 100%;
    overflow: hidden;
    display: inline-block;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;

    &::-webkit-scrollbar {
      display: none;
    }

    .address-list {
      display: inline-block;
      width: 100%;
      padding: 0 12px 45px;
      box-sizing: border-box;

      .address-item {
        display: inline-block;
        width: 100%;
        padding: 16px 16px 16px 10px;
        margin-top: 12px;
        box-sizing: border-box;
        background: #ffffff;
        border-radius: 8px;
        color: #4A4A4A;

        &.curIndex {
          position: relative;
          padding-left: 42px;

          .radio, .radio:checked {
            check-style();
            position: absolute !important;
            top: 50%;
            left: 12px;
            transform: translateY(-50%);
          }
        }

        .name-tel {
          font-size: 16px;
          line-height: 25px;
          font-weight: 600;

          .tel {
            margin-left: 24px;
            font-size: 18px;
          }
        }

        .address {
          margin-top: 9px;
          display: flex;
          display: -webkit-flex;

          .info {
            flex: 1;

            .tag {
              display: inline;
              margin-right: 10px;
              padding: 0 2px;
              height: 20px;
              line-height: 20px;
              text-align: center;
              font-size: 14px;
              color: #3EC6E1;
              background-color: rgba(62, 198, 225, 0.1);
              border-radius: 8px;
            }

            .text {
              display: inline;
              font-size: 14px;
              line-height: 20px;
              flex: 1;
            }
          }

          .icon {
            margin-left: 10px;
            font-size: 20px;
            color: #3EC6E1;
          }
        }
      }
    }
  }

  .btn-add {
    position: relative;
    left: 50%;
    bottom: 0;
    margin-bottom: 15px;
    width: 212px;
    height: 40px;
    line-height: 40px;
    font-size: 16px;
    border-radius: 28px;
    text-align: center;
    transform: translateX(-50%);
    color: #ffffff;
    background-color: #15BED7;
    vertical-align: middle;

    .van-icon {
      display: inline-block;
      margin-right: 4px;
      vertical-align: text-top;
    }
  }

  .no-content {
    width: 100%;
    height: 100%;
    text-align: center;
    overflow: hidden;
    display: inline-block;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;
    background-color: #ffffff;

    &::-webkit-scrollbar {
      display: none;
    }

    .context {
      display: inline-block;
      height: auto;
      margin: 40px 0 60px;
      padding: 36px 30px 45px;
      text-align: center;
      box-sizing: border-box;
      background-color: #ffffff;
      border-radius: 8px;

      .empty-img {
        display: inline-block;
        width: 236px;
        height: 200px;
        bg-image('empty');
        background-size: 236px 200px;
        background-repeat: no-repeat;
      }

      .text {
        display: block;
        margin-top: 28px;
        text-align: center;
        font-size: 16px;
        color: $color-text-d;
      }

      .btn-add {
        display: inline-block;
        margin: 112px auto 20px;
      }
    }
  }
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;
$color-main-opacity = rgba(37, 111, 255, 0.2);

// 荣耀
.ry-style {
  background-color: $color-bg;

  .content {
    .address-list {
      .address-item {
        &.curIndex {
          .radio:checked, .radio:checked:checked {
            border-color: $color-main;
            background-color: $color-main;
          }
        }

        .address {
          .info .tag {
            color: $color-main;
            background-color: $color-main-opacity;
          }

          .icon {
            color: $color-main;
          }
        }
      }
    }
  }

  .no-content {
    .context {
      .empty-img {
        width: 174px;
        height: 174px;
        background: url('../../assets/images/ry/no-address.png') no-repeat center;
        background-size: 174px 174px;
        background-repeat: no-repeat;
      }
    }
  }

  .btn-add {
    background-color: $color-main;
  }
}
</style>