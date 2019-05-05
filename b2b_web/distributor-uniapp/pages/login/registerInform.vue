<template>
  <view class="registerInform">
    <!-- <view class="top-moudle">
			<view class="status_bar"></view>
				<view class="top-title">
				<image src="../../static/img/back_icon.png" @click="toback"></image>
				<view>注册</view>
			</view>
		</view> -->

    <!-- 信息 -->
    <view class="rgI-form">
      <view class="rgI-form-title">公司信息</view>
      <view class="rgI-form-list1" @click="openPopup">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">公司类型</text>
        <text class="rgI-form-listThree">{{ companyTypeName }}</text>
        <text class="toImg"></text>
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">公司名称</text>
        <input
          placeholder="请输入公司名称"
          type="text"
          class="rgI-form-input"
          v-model="formInfo.companyName"
        />
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">营业执照号</text>
        <input
          placeholder="请输入统一社会信用代码或工商注册号"
          type="text"
          class="rgI-form-input"
          v-model="formInfo.certNo"
        />
      </view>
      <view class="rgI-form-title">联系方式</view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">账户操作人</text>
        <input
          placeholder="请输入联系人姓名"
          type="text"
          class="rgI-form-input"
          v-model="formInfo.accountName"
        />
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust"></text>
        <text class="rgI-form-listTitle">联系手机号</text>
        <input
          placeholder="请输入联系人手机号"
          type="text"
          class="rgI-form-input"
          maxlength="11"
          v-model="formInfo.phone"
          disabled="true"
        />
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust"></text>
        <text class="rgI-form-listTitle">电子邮件</text>
        <input
          placeholder="请输入联系人电子邮箱"
          type="text"
          class="rgI-form-input"
          v-model="formInfo.email"
        />
      </view>
      <view class="rgI-form-list1" @click="openCountry">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">国家</text>
        <text class="rgI-form-listThree">{{ countryName }}</text>
        <text class="toImg" @click="openCountry"></text>
      </view>
      <view
        class="rgI-form-list1"
        @click="openRegionPopup"
        v-if="formInfo.countryId == 37"
      >
        <text class="rgI-form-listMust"></text>
        <text class="rgI-form-listTitle">地区</text>
        <text class="rgI-form-listThree">
          {{ provinceName }} {{ cityName }} {{ districtName }}
        </text>
        <text class="toImg"></text>
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust"></text>
        <text class="rgI-form-listTitle">联系地址</text>
        <input
          placeholder="请输入详细联系地址"
          class="rgI-form-input"
          type="text"
          v-model="formInfo.address"
        />
      </view>
      <view class="rgI-form-title">账户信息</view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">用户名</text>
        <input
          placeholder="请输入用户名"
          class="rgI-form-input"
          type="text"
          v-model="formInfo.name"
        />
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">密码</text>
        <input
          type="password"
          placeholder="请输入密码，6～16个字符"
          class="rgI-form-input"
          maxlength="16"
          v-model="formInfo.password"
        />
      </view>
      <view class="rgI-form-list1">
        <text class="rgI-form-listMust">*</text>
        <text class="rgI-form-listTitle">确认密码</text>
        <input
          type="password"
          placeholder="请再次输入密码"
          class="rgI-form-input"
          maxlength="16"
          v-model="password"
        />
      </view>

      <view class="rgI-form-list2">
        <text class="rgI-form-listMust"></text>
        <text class="rgI-form-listTitle">备注</text>
        <input
          placeholder="产品意向/销售渠道/对接的业务员"
          type="text"
          class="rgI-form-input"
          v-model="formInfo.comment"
        />
      </view>

      <view class="rgI-form-next" @click="nextFun">下一步</view>

      <!-- 公司类型 -->
      <uni-popup ref="popup" type="bottom">
        <view class="rg-popupBox">
          <view class="rg-popupBox-list">
            <!-- <view><text>请选择公司类型</text><icon class="iconfont icon-delete-"></icon></view> -->
            <view
              v-for="item in companyTypeList"
              :key="item.id"
              :class="formInfo.companyType == item.id ? 'CheckColor' : ''"
              @click="choiceCompany(item)"
            >
              <text
                class="iconfont"
                style="font-size: 42rpx"
                :class="
                  formInfo.companyType == item.id
                    ? 'icon-icon_selected'
                    : 'icon-icon_selected_def'
                "
                :style="{ color: themeColor }"
              ></text>
              <text>{{ item.name }}</text>
            </view>
          </view>
        </view>
      </uni-popup>
      <!-- 国家弹框 -->
      <uni-popup ref="countryPopup" type="bottom">
        <view class="rg-popupBox">
          <scroll-view class="rg-popupBox-listBox2" :scroll-y="true">
            <view class="rg-popupBox-list">
              <view
                v-for="item in countryList"
                :key="item.id"
                :class="formInfo.countryId == item.id ? 'CheckColor' : ''"
              >
                <text
                  class="iconfont"
                  :class="
                    formInfo.countryId == item.id
                      ? 'icon-Checkthe'
                      : 'icon-uncheck'
                  "
                  @click="choiceCountry(item)"
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
              <text>省：{{ provinceName }}</text>
              <text v-if="provinceName != ''">市：{{ cityName }}</text>
              <text v-if="haveDistrict == 1">区：{{ districtName }}</text>
            </view>
            <view class="rg-popupBox-btn" @click="confirmPopup">确定</view>
          </view>
          <view class="rg-popupBox-list">
            <!-- 省 -->
            <scroll-view class="rg-popupBox-listBox2" :scroll-y="true">
              <template v-for="item in provinceList">
                <view
                  :key="item.id"
                  :class="formInfo.countryId == item.id ? 'CheckColor' : ''"
                  v-if="regionType == 2"
                >
                  <text
                    class="iconfont"
                    :class="
                      formInfo.countryId == item.id
                        ? 'icon-Checkthe'
                        : 'icon-uncheck'
                    "
                    @click="choiceRegion(item, 2)"
                  ></text>
                  <text>{{ item.regionName }}</text>
                </view>
              </template>
              <!-- 市 -->
              <template v-for="item in cityList">
                <view
                  :key="item.id"
                  :class="formInfo.cityId == item.id ? 'CheckColor' : ''"
                  v-if="regionType == 3"
                >
                  <text
                    class="iconfont"
                    :class="
                      formInfo.cityId == item.id
                        ? 'icon-Checkthe'
                        : 'icon-uncheck'
                    "
                    @click="choiceRegion(item, 3)"
                  ></text>
                  <text>{{ item.regionName }}</text>
                </view>
              </template>
              <!-- 区 -->
              <template v-for="item in districtList">
                <view
                  :key="item.id"
                  :class="formInfo.districtId == item.id ? 'CheckColor' : ''"
                  v-if="regionType == 4 && haveDistrict == 1"
                >
                  <text
                    class="iconfont"
                    :class="
                      formInfo.districtId == item.id
                        ? 'icon-Checkthe'
                        : 'icon-uncheck'
                    "
                    @click="choiceRegion(item, 4)"
                  ></text>
                  <text>{{ item.regionName }}</text>
                </view>
              </template>
            </scroll-view>
          </view>
        </view>
      </uni-popup>
      <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
    </view>
  </view>
</template>

<script>
import md5 from "js-md5";
import { regionList, oneLevelApply } from "../../common/api.js";
export default {
  data() {
    return {
      themeColor: "",
      companyTypeName: "请选择",
      companyTypeList: [
        { id: 1, name: "公司" },
        { id: 2, name: "个人商户" },
        { id: 3, name: "个人" },
      ],
      countryList: [], //国家列表
      countryName: "中国",
      provinceList: [],
      provinceName: "",
      cityList: [],
      cityName: "",
      districtList: [],
      districtName: "",
      haveDistrict: 0,
      regionType: 2, //选择地区（省：2，市：3，区：4）
      formInfo: {
        companyType: 0, //公司类型
        companyName: "", //公司名称
        certNo: "", //营业执照或身份证号
        accountName: "", //账户操作人
        phone: "", //联系人手机号
        email: "",
        countryId: 37, //国家Id
        provinceId: "", //省
        cityId: "", //市
        districtId: "", //区
        address: "", //地址
        name: "", //用户名
        password: "",
        comment: "", //备注
      },
      password: "", //确认密码
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.themeColor = uni.getStorageSync("themeColor");
    this.formInfo.phone = option.phone;
    this.formInfo.name = option.phone;
    this.getregionList(0, 1);
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
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
    // 下一步
    nextFun() {
      let data = this.formInfo;
      let that = this;
      if (data.companyType == 0) {
        this.tipFun("请选择公司类型");
        return;
      }
      if (data.companyName == "") {
        this.tipFun("请填写公司名称");
        return;
      }
      if (data.certNo == "") {
        this.tipFun("请填写营业执照号");
        return;
      }
      if (data.accountName == "") {
        this.tipFun("请填写账户操作人");
        return;
      }

      if (data.countryId == "") {
        this.tipFun("请选择国家");
        return;
      }
      if (data.name == "") {
        this.tipFun("请填写用户名");
        return;
      }
      if (
        data.password == "" ||
        (data.password != "" && data.password.length < 6)
      ) {
        this.tipFun("请输入6~16密码");
        return;
      }
      if (
        this.password == "" ||
        (this.password != "" && this.password != data.password)
      ) {
        this.tipFun("请保证两次输入的密码一致");
        return;
      }
      data.password = md5(data.password);
      oneLevelApply(data).then((res) => {
        if (res.success) {
          uni.navigateTo({
            url: "registerSuccess",
          });
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },

    // 打开弹框
    openPopup() {
      this.$refs.popup.open();
    },

    // 选择公司类型
    choiceCompany(item) {
      console.log(item);
      this.formInfo.companyType = item.id;
      this.companyTypeName = item.name;
      this.$refs.popup.close();
    },

    // 打开国家弹框
    openCountry() {
      this.$refs.countryPopup.open();
    },
    // 选择国家
    choiceCountry(item) {
      this.formInfo.countryId = item.id;
      this.countryName = item.regionName;
      this.$refs.countryPopup.close();
    },

    // 打开地区弹框
    openRegionPopup() {
      this.$refs.regionPopup.open();
      this.getregionList(37, 2);
    },

    // 选择地区
    choiceRegion(item, type) {
      if (type == 2) {
        this.formInfo.provinceId = item.id;
        this.provinceName = item.regionName;
        this.getregionList(item.id, 3);
        this.regionType = 3;
      } else if (type == 3) {
        this.formInfo.cityId = item.id;
        this.cityName = item.regionName;
        if (item.haveNext == 1) {
          this.getregionList(item.id, 4);
          this.regionType = 4;
        }
        this.haveDistrict = item.haveNext;
      } else {
        this.formInfo.districtId = item.id;
        this.districtName = item.regionName;
      }
    },
    // 确定
    confirmPopup() {
      this.$refs.regionPopup.close();
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
  },
};
</script>

<style lang="scss">
.registerInform {
  padding-bottom: 50rpx;
  font-size: 28rpx;
  .rgI-form {
    .rgI-form-title {
      height: 100rpx;
      line-height: 100rpx;
      font-size: 28rpx;
      padding-left: 30rpx;
    }
    .rgI-form-list1 {
      display: flex;
      align-items: center;
      background: #fff;
      height: 100rpx;
      line-height: 100rpx;
      padding: 0 30rpx;
      & + .rgI-form-list1 {
        border-top: 1rpx solid $opacity-border;
      }
    }
    .rgI-form-listMust {
      color: red;
      font-size: 36rpx;
      margin-top: 15rpx;
      width: 30rpx;
    }
    .rgI-form-listTitle {
      width: 155rpx;
      margin-left: 10rpx;
    }
    .rgI-form-listThree {
      width: 500rpx;
      text-align: right;
      padding-right: 30rpx;
      color: #999;
    }
    .rgI-form-input {
      width: 500rpx;
      height: 100rpx;
      line-height: 100rpx;
      font-size: 28rpx;
    }
    .rgI-form-list2 {
      display: flex;
      align-items: center;
      background: #fff;
      margin-top: 20rpx;
    }
    .rgI-form-textarea {
      width: 550rpx;
      border: 1rpx solid red;
      font-size: 28rpx;
      padding: 30rpx;
    }
  }
  .rgI-form-next {
    width: 690rpx;
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    margin-left: 30rpx;
    color: #fff;
    background: $bg-gradient-btn;
    border-radius: 36rpx;
    margin: 50rpx 30rpx;
  }
}
.rg-popupBox {
  background: #fff;
  max-height: 800rpx;
  overflow-y: auto;
  .rg-popupBox-listBox2 {
    height: 600rpx;
  }
  .rg-popupBox-list {
    view {
      display: flex;
      justify-content: space-between;
      padding: 30rpx;
      font-size: 30rpx;
      color: #999;
      border-bottom: 1rpx solid $opacity-border;
      .iconfont {
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
    background: $opacity-border;
    padding: 30rpx;
    font-size: 30rpx;
    .popupBox-list-line {
      display: flex;
      text {
        display: block;
        width: 190rpx;
      }
    }

    .rg-popupBox-btn {
      font-size: 26rpx;
      background: $base-color1;
      color: #fff;
      padding: 8rpx 20rpx;
      border-radius: 10rpx;
    }
  }
}
</style>
