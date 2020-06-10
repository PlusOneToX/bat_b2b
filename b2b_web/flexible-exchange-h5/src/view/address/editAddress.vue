<template>
  <div class="editAddress" :class="{ hiddenHeader: isMiniProgram }">
    <THeader class="header-wrap" :title="'编辑地址'" :hasBg="true"></THeader>
    <div class="content">
      <div class="input-wrap">
        <van-field
          v-model="addressForm.userName"
          label="收货人姓名"
          placeholder="请输入收货人姓名"
          clearable
        />
        <van-field
          v-model="addressForm.phone"
          type="digit"
          maxlength="11"
          label="手机号码"
          placeholder="请输入11位手机号码"
          clearable
        />
        <van-field
          v-model="area"
          label="所在地区"
          placeholder="请选择所在地区"
          right-icon="arrow"
          readonly
          @click="areaPop = true"
        />
        <van-popup v-model="areaPop" position="bottom">
          <van-area
            :area-list="areaList"
            value="110101"
            @cancel="areaPop = false"
            @confirm="confirmArea"
          />
        </van-popup>
        <van-field
          v-model="addressForm.address"
          label="详细地址"
          placeholder="请输入详细地址"
          clearable
        />
      </div>
      <div class="check-box">
        <van-checkbox v-model="checked">设为默认地址</van-checkbox>
      </div>
      <div class="btn-wrap">
        <van-button
          round
          block
          type="info"
          native-type="submit"
          @click="onSubmit"
          >保存收货地址</van-button
        >
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
// 组件
import THeader from "components/tHeader/tHeader";
// js
import AreaList from "common/js/area.js";
// api
import api from "api/allApi.js";

export default {
  name: "editAddress",
  data() {
    return {
      title: "添加地址",
      addressForm: {
        userName: "",
        phone: "",
        defaultFlag: 0, // 默认 0-否 1-是
        provinceId: 0,
        provinceName: "",
        cityId: 0,
        cityName: "",
        districtId: 0,
        districtName: "",
        address: "",
        customerId: "",
      },
      checked: false,
      addressId: "",
      userNo: "",
      area: "",
      areaPop: false,
      areaList: AreaList,
      orderFlag: "", // 进入路径：order 订单进入
      isMiniProgram: false, // 是否是小程序
    };
  },
  mounted() {
    this.orderFlag = this.$route.query.orderFlag;
    this.addressId = this.$route.query.id;
    this.userNo = localStorage.getItem("userNo");

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.addressForm.customerId = localStorage.getItem("userId");
    if (this.addressId) {
      // 获取地址详情
      let addrInfo = JSON.parse(this.$route.query.addrInfo);
      console.log(addrInfo);

      this.addressForm = {
        userName: addrInfo.userName,
        phone: addrInfo.phone,
        defaultFlag: addrInfo.defaultFlag,
        provinceId: addrInfo.provinceId,
        provinceName: addrInfo.provinceName,
        cityId: addrInfo.cityId,
        cityName: addrInfo.cityName,
        districtId: addrInfo.districtId,
        districtName: addrInfo.districtName,
        address: addrInfo.address,
        customerId: localStorage.getItem("userId"),
      };
      this.area =
        addrInfo.provinceName +
        "-" +
        addrInfo.cityName +
        "-" +
        addrInfo.districtName;
      this.checked = Number(addrInfo.defaultFlag) === 1 ? true : false;
    }
  },
  methods: {
    validateForm() {
      let flag = true;
      let reg = /^1[3456789]\d{9}$/;
      if (this.addressForm.userName === "") {
        this.$toast("请填写收货姓名");
        flag = false;
        return;
      }
      if (this.addressForm.phone === "") {
        this.$toast("请填写手机号");
        flag = false;
        return;
      } else {
        if (!reg.test(this.addressForm.phone)) {
          this.$toast("请填写正确的手机号");
          this.addressForm.phone = "";
          flag = false;
          return;
        }
      }
      if (this.area === "") {
        this.$toast("请选择所在地区");
        flag = false;
        return;
      }
      if (this.addressForm.address === "") {
        this.$toast("请填写详细地址");
        flag = false;
        return;
      }
      return flag;
    },
    onSubmit() {
      if (this.validateForm()) {
        if (this.addressId) {
          // 修改
          this.addressForm.id = this.addressId;
          this.$api.put(this, api.updateAddr, this.addressForm).then((res) => {
            if (res.success) {
              this.$toast.success("修改成功");
              this.$router.replace({
                path: "/address",
                query: {
                  orderFlag: this.orderFlag,
                },
              });
            } else {
              this.$toast.fail(res.errMessage);
            }
          });
        } else {
          // 添加
          this.$api.post(this, api.updateAddr, this.addressForm).then((res) => {
            if (res.success) {
              this.$toast.success("新增成功");
              this.$router.replace({
                path: "/address",
                query: {
                  orderFlag: this.orderFlag,
                },
              });
            } else {
              this.$toast.fail(res.errMessage);
            }
          });
        }
      }
    },
    onDel() {
      this.$dialog
        .confirm({
          message: "此操作将删除, 是否继续?",
        })
        .then(() => {
          this.$api
            .delete(this, api.updateAddr, {
              id: this.addressId,
            })
            .then((res) => {
              if (res.success) {
                this.$toast.success("删除成功");
                this.$router.replace({
                  path: "/address",
                  query: {
                    orderFlag: this.orderFlag,
                  },
                });
              } else {
                this.$toast.fail(res.errMessage);
              }
            });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    confirmArea(val) {
      this.area = `${val[0].name}-${val[1].name}-${val[2].name}`;
      this.addressForm.provinceId = val[0].code;
      this.addressForm.provinceName = val[0].name;
      this.addressForm.cityId = val[1].code;
      this.addressForm.cityName = val[1].name;
      this.addressForm.districtId = val[2].code;
      this.addressForm.districtName = val[2].name;
      this.areaPop = false;
    },
    toback() {
      this.$router.replace({
        path: "/address",
        query: {
          orderFlag: this.orderFlag,
        },
      });
    },
  },
  watch: {
    checked(val) {
      this.addressForm.defaultFlag = val ? 1 : 0;
    },
  },
  components: {
    THeader,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.editAddress {
  position: fixed;
  width: 100%;
  top: 44px;
  bottom: 0;
  background-color: $color-bg;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .content {
    height: 100%;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;

    &::-webkit-scrollbar {
      display: none;
    }

    .input-wrap {
      padding: $spacing-lg 0;
      background-color: $color-bg-white;
    }

    .van-cell {
      padding: $spacing-lg $spacing-base;

      &::after {
        border-bottom: none;
      }
    }

    .check-box {
      display: block;
      margin-top: $spacing-sm;
      width: 100%;
      lineHeight(50px);
      padding: 0 $spacing-base;
      font-size: $font-base;
      background-color: $color-bg-white;

      .van-checkbox {
        lineHeight(50px);
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
    }

    .btn-wrap {
      padding: 60px 30px;
    }

    .van-button {
      width: 100%;
      lineHeight(55px);
      font-size: $font-lg;
      color: $color-font-base;
      border: none;
      background-color: $color-main;
    }
  }
}
</style>
