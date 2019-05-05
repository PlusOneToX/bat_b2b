<template>
  <view class="addressEdit">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>{{ typeName }}收货地址</view>
      </view>
    </view>

    <view class="addressEdit-form">
      <view class="addressEdit-form-input1 border-top">
        <text>收货人</text>
        <input
          type="text"
          placeholder="请输入收货人"
          v-model="formInfo.userName"
        />
      </view>
      <view class="addressEdit-form-input1">
        <text>手机号码</text>
        <input
          type="text"
          placeholder="请输入手机号"
          maxlength="11"
          v-model="formInfo.phone"
        />
      </view>
      <view class="addressEdit-form-input2" @click="openCountry">
        <text>国家</text>
        <view>
          <input
            type="text"
            placeholder="请选择"
            :value="countryName"
            disabled="true"
          />
          <image src="../../static/img/auditTo_icon.png" class="toIcon"></image>
        </view>
      </view>
      <view
        class="addressEdit-form-input2"
        @click="openRegionPopup"
        v-if="countryId == 37"
      >
        <text>所在地区</text>
        <view>
          <input
            type="text"
            placeholder="请选择"
            disabled="true"
            :value="regionName"
          />
          <image src="../../static/img/auditTo_icon.png" class="toIcon"></image>
        </view>
      </view>
      <view class="addressEdit-form-input3">
        <text>详细地址</text>
        <textarea
          type="text"
          placeholder="如街道、门牌号、楼栋号、单元室等"
          v-model="formInfo.address"
        ></textarea>
      </view>
      <view class="addressEdit-form-input4">
        <text>设置为默认地址</text>
        <switch
          :checked="defaultVal"
          color="#09D5EE"
          style="transform: scale(0.7)"
          @change="defaultCahnge"
      /></view>
    </view>

    <view
      class="add-address"
      :style="{ background: themeColor }"
      @click="saveFun"
      >保存
    </view>

    <!-- 国家弹框 -->
    <uni-popup ref="countryPopup" type="bottom">
      <view class="rg-popupBox">
        <view class="rg-popupBox-title">请选择国家</view>
        <scroll-view class="rg-popupBox-listBox2" :scroll-y="true">
          <view class="rg-popupBox-list">
            <view
              v-for="item in countryList"
              :key="item.id"
              :style="{
                color: formInfo.countryId == item.id ? themeColor : '',
              }"
              @click="choiceCountry(item)"
            >
              <text
                class="iconfont"
                :class="
                  formInfo.countryId == item.id
                    ? 'icon-icon_selected'
                    : 'icon-icon_selected_def'
                "
              ></text>
              <text>{{ item.regionName }}</text>
            </view>
          </view>
        </scroll-view>
      </view>
    </uni-popup>
    <!-- 地区列表弹框 -->
    <uni-popup ref="regionPopup" type="bottom">
      <view class="rg-popupBox">
        <view class="rg-popupBox-list2">
          <view class="popupBox-list-line">
            <view class="popupBox-list-lineView">
              <text>省：</text>
              <view>
                <text>{{ provinceName }}</text>
                <!-- <text class="iconfont icon-Popover-cancel" v-if="provinceName!=''" @click="provinceNameDeleteFun"></text> -->
                <image
                  src="../../static/imgs/icon_close.png"
                  v-if="provinceName != ''"
                  @click="provinceNameDeleteFun"
                ></image>
              </view>
            </view>
            <!-- v-if="provinceName!=''" -->
            <view v-if="provinceName != ''" class="popupBox-list-lineView">
              <text>市：</text>
              <view>
                <text>{{ cityName }}</text>
                <!-- <text class="iconfont icon-Popover-cancel" v-if="cityName!=''" @click="cityNameDeleteFun"></text> -->
                <image
                  src="../../static/imgs/icon_close.png"
                  v-if="cityName != ''"
                  @click="cityNameDeleteFun"
                ></image>
              </view>
            </view>
            <!-- v-if="haveDistrict==1"  -->
            <view v-if="haveDistrict == 1" class="popupBox-list-lineView">
              <text>区：</text>
              <view>
                <text>{{ districtName }}</text>
                <!-- <text class="iconfont icon-Popover-cancel" v-if="districtName!=''" @click="districtNameDeleteFun"></text> -->
                <image
                  src="../../static/imgs/icon_close.png"
                  v-if="districtName != ''"
                  @click="districtNameDeleteFun"
                ></image>
              </view>
            </view>
          </view>
        </view>
        <scroll-view class="rg-popupBox-listBox" :scroll-y="true">
          <view class="rg-popupBox-list">
            <!-- 省 -->
            <template v-for="item in provinceList">
              <view
                :key="item.id"
                :style="{ color: countryId == item.id ? themeColor : '' }"
                v-if="regionType == 2"
                @click="choiceRegion(item, 2)"
              >
                <text
                  class="iconfont"
                  :class="
                    countryId == item.id
                      ? 'icon-icon_selected'
                      : 'icon-icon_selected_def'
                  "
                ></text>
                <text>{{ item.regionName }}</text>
              </view>
            </template>
            <!-- 市 -->
            <template v-for="item in cityList">
              <view
                :key="item.id"
                :style="{ color: cityId == item.id ? themeColor : '' }"
                v-if="regionType == 3"
                @click="choiceRegion(item, 3)"
              >
                <text
                  class="iconfont"
                  :class="
                    cityId == item.id
                      ? 'icon-icon_selected'
                      : 'icon-icon_selected_def'
                  "
                ></text>
                <text>{{ item.regionName }}</text>
              </view>
            </template>
            <!-- 区 -->
            <template v-for="item in districtList">
              <view
                :key="item.id"
                :style="{ color: districtId == item.id ? themeColor : '' }"
                v-if="regionType == 4 && haveDistrict == 1"
                @click="choiceRegion(item, 4)"
              >
                <text
                  class="iconfont"
                  :class="
                    districtId == item.id
                      ? 'icon-icon_selected'
                      : 'icon-icon_selected_def'
                  "
                ></text>
                <text>{{ item.regionName }}</text>
              </view>
            </template>
          </view>
        </scroll-view>
        <view
          class="rg-popupBox-btn"
          :style="{ background: themeColor }"
          @click="confirmPopup"
          >确定
        </view>
      </view>
    </uni-popup>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { regionList, addressSet, region } from "../../common/api.js";
export default {
  data() {
    return {
      themeColor: "",
      typeName: "",
      formInfo: {
        distributorId: uni.getStorageSync("userId"),
        userName: "",
        phone: "",
        countryId: 37,
        provinceId: "",
        cityId: "",
        districtId: "",
        address: "",
        addressType: 2,
        defaultFlag: 0,
        id: "", //地区id
      },
      countryList: [], //国家列表
      countryName: "中国",
      countryId: 37,
      provinceList: [],
      provinceName: "",
      provinceId: "",
      cityList: [],
      cityName: "",
      cityId: "",
      districtList: [],
      districtName: "",
      districtId: "",
      haveDistrict: 0,
      regionType: 2, //选择地区（省：2，市：3，区：4）
      regionName: "",
      defaultVal: false, //是否默认地址
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.themeColor = uni.getStorageSync("themeColor");
    if (option.type == "1") {
      this.typeName = "新增";
    } else {
      this.typeName = "编辑";
      let info = JSON.parse(option.info);
      this.formInfo.userName = info.userName;
      this.formInfo.phone = info.phone;
      this.formInfo.countryId = info.countryId;
      this.formInfo.provinceId = info.provinceId;
      this.formInfo.cityId = info.cityId;
      this.formInfo.districtId = info.districtId;
      this.formInfo.address = info.address;
      this.countryName = info.countryName;
      this.formInfo.id = info.id;
      // 判断是否有下级区域
      if (info.haveNext == 1) {
        this.regionName = info.provinceName + info.cityName + info.districtName;
      } else {
        this.regionName = info.provinceName + info.cityName;
      }
      // 默认地址判断
      this.formInfo.defaultFlag = info.defaultFlag;
      if (info.defaultFlag == 1) {
        this.defaultVal = true;
      } else {
        this.defaultVal = false;
      }
    }
    console.log("分销商id：", this.formInfo.distributorId);
    this.getregionList(0, 1);
    // this.formInfo.distributorId=uni.getStorageSync('userId')
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        url: "addressList",
      });
    },
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // 设置默认地址
    defaultCahnge(e) {
      console.log(e.detail);
      this.defaultVal = e.detail.value;
      if (e.detail.value) {
        this.formInfo.defaultFlag = 1;
      } else {
        this.formInfo.defaultFlag = 0;
      }
    },
    // 保存
    saveFun() {
      let that = this;
      let params = this.formInfo;
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if (params.distributorId == "") {
        this.tipFun("请重新登陆");
        return;
      }
      if (params.userName == "") {
        this.tipFun("收货人不能为空");
        return;
      }
      if (params.phone == "") {
        this.tipFun("手机号不能为空");
        return;
      }
      if (params.phone != "" && !myreg.test(params.phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      if (params.countryId == 37 && params.provinceId == "") {
        this.tipFun("请选择地区");
        return;
      }
      let methods = "";
      if (this.typeName == "新增") {
        methods = "POST";
      } else {
        methods = "PUT";
      }
      addressSet(methods, params).then((res) => {
        if (res.success) {
          that.tipFun(that.typeName + "成功");
          uni.navigateTo({
            url: "addressList",
          });
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },
    // 打开国家弹框
    openCountry() {
      this.$refs.countryPopup.open();
    },
    // 选择国家
    choiceCountry(item) {
      this.formInfo.countryId = item.id;
      this.countryId = item.id;
      this.countryName = item.regionName;
      this.$refs.countryPopup.close();
    },

    // 打开地区弹框
    openRegionPopup() {
      this.$refs.regionPopup.open();
      this.getregionList(37, 2);
      this.regionType = 2;
    },

    // 选择地区
    choiceRegion(item, type) {
      let that = this;
      if (type == 2) {
        let that = this;
        this.provinceId = item.id;
        this.provinceName = item.regionName;
        this.getregionList(item.id, 3);
        this.regionType = 3;
      } else if (type == 3) {
        this.cityId = item.id;
        this.cityName = item.regionName;
        if (item.haveNext == 1) {
          this.getregionList(item.id, 4);
          this.regionType = 4;
        }
        this.haveDistrict = item.haveNext;
      } else {
        this.districtId = item.id;
        this.districtName = item.regionName;
      }
    },
    // 获取地区列表
    getregionList(parentId, type) {
      let that = this;
      regionList({ parentId: parentId, page: 1, size: 3000 }).then((res) => {
        if (res.success) {
          let list = res.data.list;
          if (type == 1) {
            that.countryList = list;
          } else if (type == 2) {
            that.provinceList = list;
          } else if (type == 3) {
            that.cityList = list;
          } else if (type == 4) {
            that.districtList = list;
          }
        }
      });
    },
    // 确定
    confirmPopup() {
      this.formInfo.provinceId = this.provinceId;
      this.formInfo.cityId = this.cityId;
      this.formInfo.districtId = this.districtId;
      this.$refs.regionPopup.close();
      this.regionName = this.provinceName + this.cityName + this.districtName;
    },
    // 省删除
    provinceNameDeleteFun() {
      this.provinceName = "";
      this.provinceId == "";
      this.cityName = "";
      this.cityId = "";
      this.districtName = "";
      this.districtId = "";
      this.regionType = 2;
      this.haveDistrict = 0;
    },
    // 城市删除
    cityNameDeleteFun() {
      this.cityName = "";
      this.cityId = "";
      this.districtName = "";
      this.districtId = "";
      this.regionType = 3;
      this.haveDistrict = 0;
    },

    // 区删除
    districtNameDeleteFun() {
      this.districtName = "";
      this.districtId = "";
    },
  },
};
</script>

<style lang="scss">
.addressEdit {
  font-size: 28rpx;
  .addressEdit-form {
    background: #fff;
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
    input {
      padding: 30rpx;
      font-size: 28rpx;
    }
    .addressEdit-form-input1 {
      display: flex;
      align-items: center;
      padding: 0 30rpx;
      border-bottom: 1rpx solid $opacity-border;

      &.border-top {
        border-top: 20rpx solid $opacity-border;
      }
      text {
        width: 150rpx;
      }
      input {
        width: 460rpx;
      }
    }
    .addressEdit-form-input2 {
      padding: 0 30rpx;
      display: flex;
      align-items: center;
      border-bottom: 1rpx solid $opacity-border;
      text {
        width: 150rpx;
      }
      view {
        display: flex;
        align-items: center;

        input {
          width: 450rpx;
          text-align: right;
        }
      }
    }
    .addressEdit-form-input3 {
      display: flex;
      padding: 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      text {
        width: 150rpx;
      }
      textarea {
        width: 500rpx;
        font-size: 28rpx;
        height: 150rpx;
      }
    }
    .addressEdit-form-input4 {
      padding: 20rpx 30rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }

  .add-address {
    width: 690rpx;
    height: 100rpx;
    line-height: 100rpx;
    text-align: center;
    color: #fff;
    border-radius: 50rpx;
    margin: 100rpx auto;
  }
}
.rg-popupBox {
  background: #fff;
  .rg-popupBox-title {
    text-align: center;
    font-size: 30rpx;
    background: #fff;
    padding: 30rpx 0;
    border-bottom: 1rpx solid $opacity-border;
  }
  .rg-popupBox-listBox2 {
    height: 700rpx;
  }
  .rg-popupBox-listBox {
    height: 700rpx;
    padding-bottom: 120rpx;
  }

  .rg-popupBox-list {
    view {
      display: flex;
      justify-content: space-between;
      padding: 30rpx;
      font-size: 30rpx;
      color: #999;
      border-bottom: 1rpx solid $opacity-border;
      text:nth-child(1) {
        font-size: 40rpx;
      }
    }
  }
  .CheckColor {
    color: $base-color1 !important;
  }

  .rg-popupBox-list2 {
    display: flex;
    justify-content: space-between;
    background: #fff;
    padding: 20rpx;
    font-size: 30rpx;
    width: 690rpx;
    border-bottom: 1rpx solid $opacity-border;
    .popupBox-list-line {
      display: flex;
      align-items: center;
      .popupBox-list-lineView {
        display: flex;
        align-items: center;
        margin-right: 20rpx;
        view {
          border: 1rpx solid $opacity-border;
          width: 145rpx;
          height: 60rpx;
          line-height: 60rpx;
          border-radius: 10rpx;
          padding: 0 10rpx;
          font-size: 24rpx;
          display: flex;
          align-items: center;
          justify-content: space-between;
          text:nth-child(1) {
            width: 120rpx;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          text:nth-child(2) {
            font-size: 36rpx;
          }
          image {
            width: 32rpx;
            height: 32rpx;
          }
        }
      }
    }
  }
  .rg-popupBox-btn {
    font-size: 28rpx;

    color: #fff;
    height: 90rpx;
    line-height: 90rpx;
    width: 690rpx;
    text-align: center;
    border-radius: 60rpx;
    position: fixed;
    bottom: 30rpx;
    left: 30rpx;
  }
}
</style>
