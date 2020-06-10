<template>
  <div
    class="editAddress"
    :class="{ 'ry-style': Number(distributorId) === 4378 }"
  >
    <v-header :back="true" :title="title" @back="toback"></v-header>
    <div class="content">
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
        placeholder="地区信息"
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
      <div class="check-box">
        <van-checkbox v-model="checked">设置为默认地址</van-checkbox>
      </div>
      <div class="btn-wrap">
        <van-button
          round
          block
          type="info"
          native-type="submit"
          class="btn-submit"
          @click="onSubmit"
          >保存收货地址</van-button
        >
        <van-button
          v-show="addressId"
          round
          block
          type="info"
          native-type="default"
          class="btn-del"
          @click="onDel"
          >删除地址</van-button
        >
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import { getLocalStorageItem } from "common/js/common";
import AreaList from "common/js/area.js";
import api from "common/js/allApi.js";
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
      distributorId: "", // 分销商ID
    };
  },
  mounted() {
    this.orderFlag = this.$route.query.orderFlag;
    this.addressId = this.$route.query.id;
    this.userNo = getLocalStorageItem("userNo");

    this.distributorId = parseInt(localStorage.getItem("distributorId"));

    this.addressForm.customerId = getLocalStorageItem("userId");
    if (this.addressId) {
      // 获取地址详情
      let addrInfo = JSON.parse(this.$route.query.addrInfo);

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
        customerId: getLocalStorageItem("userId"),
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
    VHeader,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
.editAddress {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 0;
  background-color: #F6F6F6;

  .content {
    height: 100%;
    margin: 12px 12px 0;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;

    &::-webkit-scrollbar {
      display: none;
    }

    .van-cell {
      &:first-child {
        border-radius: 8px 8px 0 0;
      }

      &:nth-child(4) {
        border-radius: 0 0 8px 8px;
      }

      &::after {
        border-bottom: none;
      }
    }

    .check-box {
      display: block;
      margin-top: 12px;
      width: 100%;
      height: 45px;
      line-height: 45px;
      padding: 0 10px;
      box-sizing: border-box;
      font-size: 14px;
      border-radius: 8px;
      background-color: #ffffff;
      box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

      .van-checkbox {
        height: 45px;
        line-height: 45px;
        color: #4A4A4A;

        >>>.van-checkbox__icon {
          font-size: 16px;

          &.van-checkbox__icon--checked {
            .van-icon {
              background-color: #0076A5;
              border-color: #0076A5;
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
      width: 250px;
      margin: 90px auto 40px;
    }

    .van-button {
      width: 100%;
      height: 50px;
      line-height: 50px;
      font-size: 16px;
      border: none;

      &.btn-submit {
        background-color: #0076A5;
      }

      &.btn-del {
        margin-top: 24px;
        background-color: #EF6B52;
      }
    }
  }
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;

// 荣耀
.ry-style {
  background-color: $color-bg;

  .content {
    .check-box {
      .van-checkbox {
        >>>.van-checkbox__icon {
          &.van-checkbox__icon--checked {
            .van-icon {
              background-color: $color-main;
              border-color: $color-main;
            }
          }
        }
      }
    }

    .van-button {
      &.btn-submit {
        background-color: $color-main;
      }
    }
  }
}
</style>