<template>
  <div class="user">
    <v-header :back="true" :title="title" @back="toback"></v-header>
    <div class="container">
      <div class="avatar-box">
        <van-uploader class="avatar" acceot="image/*" :after-read="afterRead">
          <img :src="userInfo.headPortrait" alt="" />
        </van-uploader>
      </div>
      <van-cell-group>
        <van-field
          type="text"
          v-model="userInfo.nikeName"
          label="修改昵称"
          placeholder="请输入用户名"
          @click="setName"
          right-icon="arrow"
          readonly
        ></van-field>
        <van-field
          v-model="sexValue"
          label="性别"
          placeholder="选择性别"
          @click="sexPop = true"
          right-icon="arrow"
          readonly
        >
        </van-field>
        <van-popup v-model="sexPop" position="bottom">
          <van-picker
            show-toolbar
            :columns="columns"
            @confirm="confirmSex"
            @cancel="sexPop = false"
          />
        </van-popup>
        <van-field
          v-model="userInfo.birthday"
          label="生日"
          placeholder="选择时间"
          @click="datePop = true"
          right-icon="arrow"
          readonly
        ></van-field>
        <van-popup v-model="datePop" position="bottom">
          <van-datetime-picker
            v-model="currentDate"
            type="date"
            title="选择年月日"
            :min-date="minDate"
            :max-date="maxDate"
            @confirm="confirmPicker"
            @cancel="datePop = false"
          />
        </van-popup>
      </van-cell-group>
      <van-cell-group>
        <van-field
          ref="inputPhone"
          v-model="phoneSub"
          label="绑定手机号"
          right-icon="arrow"
          @click="setPhone"
        />
        <van-field
          ref="inputPwd"
          readonly
          label="修改密码"
          right-icon="arrow"
          @click="setPwd"
        />
      </van-cell-group>
      <div
        class="login-out"
        @click="logout"
        v-show="platform !== '7' && phoneSub"
      >
        退出登录
      </div>
      <!---修改昵称---->
      <div class="setName-wrapper" :class="fadeName ? 'show' : ''">
        <v-header :back="true" :title="title" @back="prev"></v-header>
        <div class="content">
          <van-cell-group>
            <van-field
              type="text"
              v-model="newName"
              maxlength="20"
              placeholder="请输入用户名"
              clearable
            ></van-field>
          </van-cell-group>
          <div class="confirm btn-normal" @click="onClickName">确认</div>
        </div>
      </div>
      <!-- 新用户绑定手机号 -->
      <div class="bind-mobile" :class="bindPhone && !fadephone ? 'show' : ''">
        <v-header :back="true" :title="title" @back="prev"></v-header>
        <div class="content">
          <div class="newphone">
            <van-field
              type="digit"
              maxlength="11"
              v-model="newPhone"
              placeholder="请输入手机号"
              class="input"
              clearable
            />
            <div class="box">
              <van-field
                class="code"
                v-model="reCode"
                center
                clearable
                placeholder="请输入短信验证码"
              >
                <template #button>
                  <van-button size="small" type="primary" @click="getCode(2)">{{
                    reCodeStr
                  }}</van-button>
                </template>
              </van-field>
            </div>
            <div class="btn-normal" @click="onClickPhone()">确认</div>
          </div>
        </div>
      </div>
      <!-- 手机验证，更换手机号 -->
      <div
        class="setphone-wrapper"
        :class="fadephone && !bindPhone ? 'show' : ''"
      >
        <v-header :back="true" :title="title" @back="prev"></v-header>
        <div class="content">
          <div class="captcha" v-show="show">
            <div class="tip">
              以您的手机号为<span class="tel">{{ phoneNum }}</span
              >的手机发送验证码
            </div>
            <div class="again" @click="getCode(1)">{{ CodeStr }}</div>
            <van-field
              @blur.prevent="checkValue"
              type="digit"
              maxlength="6"
              v-model="code"
              placeholder="请输入验证码"
              class="input"
              clearable
            />
            <div class="btn-normal" @click="onCheck">确认</div>
          </div>
          <div class="newphone" v-show="!show">
            <van-field
              type="digit"
              maxlength="11"
              v-model="newPhone"
              placeholder="请输入新的手机号"
              class="input"
              clearable
            />
            <div class="box">
              <van-field
                class="code"
                v-model="reCode"
                center
                clearable
                placeholder="请输入短信验证码"
              >
                <template #button>
                  <van-button size="small" type="primary" @click="getCode(2)">{{
                    reCodeStr
                  }}</van-button>
                </template>
              </van-field>
            </div>
            <div class="btn-normal" @click="onClickPhone()">确认</div>
          </div>
        </div>
      </div>
      <!---设置密码---->
      <div class="setpwd-wrapper" :class="fadeIn ? 'show' : ''">
        <v-header :back="true" :title="title" @back="prev"></v-header>
        <div class="content">
          <van-cell-group>
            <van-field
              @blue="blue()"
              :type="newPwdShow ? 'text' : 'password'"
              v-model="newpwd"
              placeholder="请输入新密码"
              :right-icon="newPwdShow ? 'eye-o' : 'closed-eye'"
              @click-right-icon="onClickIcon"
              clearable
            />
          </van-cell-group>
          <van-cell-group>
            <van-field
              @blue="blue()"
              :type="rePwdShow ? 'text' : 'password'"
              v-model="repwd"
              placeholder="请重复新密码"
              :right-icon="rePwdShow ? 'eye-o' : 'closed-eye'"
              @click-right-icon="onClickReIcon"
              clearable
            />
          </van-cell-group>
          <div class="confirm btn-normal" @click="onClickPwd">确认</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import md5 from "js-md5";
import { inputBlur,setLocalStorageItem,getLocalStorageItem } from "../../common/js/common";
import api from "common/js/allApi.js";
import { mapMutations } from "vuex";
const TIME_COUNT = 60;
export default {
  name: "user",
  data() {
    return {
      title: "个人信息",
      userInfo: {
        id: 0,
        name: "",
        nikeName: "游客",
        sex: 0,
        birthday: "",
        headPortrait: "./static/images/user.png",
        phone: "",
        password: "",
      },
      newName: "",
      sexValue: "",
      columns: ["女", "男"],
      newpwd: "",
      repwd: "",
      phoneNum: "",
      newPhone: "",
      phoneSub: "",
      code: "",
      reCode: "",
      CodeStr: "重新获取",
      reCodeStr: "获取验证码",
      count: 0,
      timer: null,
      datePop: false,
      sexPop: false,
      minDate: new Date(1900, 0, 1),
      maxDate: new Date(2025, 10, 1),
      currentDate: new Date(),
      fadeIn: false,
      fadephone: false,
      bindPhone: false,
      fadeName: false,
      show: true,
      newPwdShow: false,
      rePwdShow: false,
      platform: "", 
    };
  },
  mounted() {
    if (this.$store.state.user.firstLoadOSS) {
      // 阿里云 OSS JS
      let oss = document.createElement("script");
      oss.type = "text/javascript";
      oss.src = "http://gosspublic.alicdn.com/aliyun-oss-sdk-4.4.4.min.js";
      document.body.appendChild(oss);

      this.$store.commit("handleNotFirstLoadOSS");
    }

    this.platform = localStorage.getItem("platform");
    this.userInfo.no = getLocalStorageItem("userNo") || "";
    this.userInfo.id = getLocalStorageItem("userId");
    let auth = getLocalStorageItem("auth");
    if (auth) {
      // 获取用户信息
      this.getUserInfo();
    }
  },
  methods: {
    checkValue() {
      inputBlur();
      this.$emit("checkValue");
    },
    ...mapMutations({
      setNikeName: "SET_NIKENAME",
      setPhone: "SET_PHONE",
      setAvatar: "SET_AVATAR",
    }),
    async afterRead(data) {
      let file = data.file;
      if (
        file.type !== "image/jpeg" &&
        file.type !== "image/bmp" &&
        file.type !== "image/jpg" &&
        file.type !== "image/jpeg" &&
        file.type !== "image/png" &&
        file.type !== "image/gif"
      ) {
        this.$toast.fail("上传图片只能是bmp、jpg、jpeg、png、gif格式!");
        return false;
      }
      // this.compressPic(file)
      const isLt4M = file.size / 1024 / 1024 < 8;
      if (!isLt4M) {
        this.$toast.fail("上传头像图片大小不能超过 4MB!");
        return isLt4M;
      }

      // 随机命名
      let randomName =
        this.random_string(6) +
        "_" +
        new Date().getTime() +
        "." +
        file.name.split(".").pop();
      this.$api.get(this, api.uploadFile).then((result) => {
        /* eslint-disable */
        let client = new OSS.Wrapper({
          region: result.data.region,
          accessKeyId: result.data.accessKeyId,
          accessKeySecret: result.data.accessKeySecret,
          stsToken: result.data.securityToken,
          bucket: result.data.bucketName,
          endpoint: result.data.endpoint,
          secure: true,
        });

        // 上传头像
        client
          .multipartUpload(result.data.pathName + "/" + randomName, file, {})
          .then((results) => {
            return new Promise((resolve, reject) => {
              // 更新头像
              this.userInfo.headPortrait = result.data.hostname + results.name;
              this.updateUserInfo(this.userInfo, 4);
              resolve(true);
            });
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    handleImgScale(imgObj) {
      let img;
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");
      // 图片压缩
      if (imgObj && imgObj.width > 1000 && imgObj.height > 1000) {
        imgObj.width = imgObj.width / 6;
        imgObj.height = imgObj.height / 6;
      }
      ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
      img = canvas.toDataURL("image/jpeg");
      return img;
    },
    // 加载图片
    loadImage(url) {
      let img = new Image();
      img.crossOrigin = "anonymous";
      if (url.indexOf("data:") === 0) {
        img.src = url;
      } else {
        img.src = url + "?t=" + new Date().getTime();
      }
      return new Promise((resolve) => {
        if (img.complete) {
          resolve(img);
        }
        img.onload = () => resolve(img);
      });
    },
    // 随机生成文件名
    random_string(len) {
      len = len || 32;
      let chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
      let maxPos = chars.length;
      let pwd = "";
      for (let i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
      }
      return pwd;
    },
    // 通过canvas压缩图片
    compressPic(file) {
      let reader = new FileReader();
      reader.readAsDataURL(file);
      let img = new Image();
      reader.onload = function (e) {
        let width = 1080, // 图像大小
          quality = 0.8, // 图像质量
          canvas = document.createElement("canvas"),
          drawer = canvas.getContext("2d");
        img.src = file.name;
        img.onload = function () {
          canvas.width = width;
          canvas.height = width * (img.height / img.width);
          drawer.drawImage(img, 0, 0, canvas.width, canvas.height);
          img.src = canvas.toDataURL("image/png", quality);
        };
      };
      return img;
    },
    // 获取用户信息
    getUserInfo() {
      this.$api
        .get(this, api.getUserInfo, {
          id: this.userInfo.id,
        })
        .then((res) => {
          if (res.success) {
            this.userInfo = res.data;

            setLocalStorageItem("phone", this.userInfo.phone);
            setLocalStorageItem("userNo", this.userInfo.no);
            setLocalStorageItem("userId", this.userInfo.id);

            // 手机号
            if (this.userInfo.phone) {
              this.phoneSub = this.userInfo.phone.replace(
                /^(\d{3})\d*(\d{4})$/,
                "$1****$2"
              );
            }

            // 生日
            if (this.userInfo.birthday) {
              this.userInfo.birthday = this.userInfo.birthday
                .trim()
                .split(" ")[0];
            }

            // 设置默认头像
            if (
              this.userInfo.headPortrait === "" ||
              this.userInfo.headPortrait === null ||
              this.userInfo.headPortrait === undefined
            ) {
              this.userInfo.headPortrait = "./static/images/user.png";
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    updateUserInfo(userInfo, type) {
      this.$api
        .put(this, api.updateUserInfo, userInfo)
        .then((res) => {
          if (res.success) {
            if (type && type === 1) {
              // 更新昵称
              this.title = "个人信息";
              this.fadeName = false;
              this.setNikeName(res.data.nikeName);
            } else if (type === 2) {
              // 设置密码
              this.fadeIn = false;
              let platform = localStorage.getItem("platform");
              let distributorId = localStorage.getItem("distributorId");
              if (platform && platform === "7") {
                localStorage.setItem("isflag", "1");
                // } else {
                //   this.$store.dispatch('LogOut').then(() => {
                //     if (platform && distributorId) {
                //       this.$router.push({
                //         path: '/login',
                //         query: {
                //           platform: platform,
                //           distributorId: distributorId
                //         }
                //       })
                //     } else {
                //       this.$router.push('/login')
                //     }
                //   })
              }
            } else if (type === 3) {
              this.code = "";
              this.reCode = "";
              this.newPhone = "";
              this.fadephone = false;
              this.bindPhone = false;
              this.show = false;
              this.userInfo = res.data;
              if (this.userInfo.phone) {
                this.phoneSub = this.userInfo.phone.replace(
                  /^(\d{3})\d*(\d{4})$/,
                  "$1****$2"
                );
              }
              setLocalStorageItem("phone", this.userInfo.phone);
              setLocalStorageItem("userNo", this.userInfo.no);
              setLocalStorageItem("userId", this.userInfo.id);
              if (
                this.userInfo.headPortrait === "" ||
                this.userInfo.headPortrait === null ||
                this.userInfo.headPortrait === undefined
              ) {
                this.userInfo.headPortrait = "./static/images/user.png";
              }
            } else if (type === 4) {
              // 头像
              this.setAvatar(res.data.headPortrait);
            }
          } else {
            this.$toast(res.error);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    logout() {
      this.$dialog
        .confirm({
          message: "此操作将退出, 是否继续?",
        })
        .then(() => {
          this.$store
            .dispatch("LogOut")
            .then(() => {
              let platform = localStorage.getItem("platform");
              let distributorId = localStorage.getItem("distributorId");
              if (platform && platform === "7") {
                let sa = localStorage.getItem("sa");
                this.$router.replace({
                  name: "login",
                  query: {
                    platform: platform,
                    distributorId: distributorId,
                    sa: sa,
                  },
                });
              } else {
                this.$router.replace({
                  name: "login",
                  query: {
                    platform: platform,
                    distributorId: distributorId,
                    key: "-1",
                  },
                });
              }
            })
            .catch((error) => {
              console.log(error);
            });
        });
    },
    getCode(type) {
      let phone, codeType;
      let flag = true;
      if (type === 2) {
        flag = this.validatePhone();
        phone = this.newPhone;
        codeType = 5;
      } else {
        phone = this.userInfo.phone;
        codeType = 3;
      }
      // 获取验证码
      if (flag) {
        this.getVerify(phone, codeType, type);
      }
    },
    onClickName() {
      if (
        this.newName !== "" &&
        this.newName !== undefined &&
        this.newName !== null
      ) {
        this.userInfo.nikeName = this.newName;
        this.updateUserInfo(this.userInfo, 1);
      } else {
        this.$toast("请填写用户名");
      }
    },
    onCheck() {
      this.onClickCaptcha(this.userInfo.phone, this.code, 1);
    },
    onClickCaptcha(phone, code, type) {
      if (code !== "" && code !== undefined && code !== null) {
        // 验证验证码是否有效
        this.$api
          .post(this, api.captchaCheck, {
            phone: phone,
            captcha: code,
            type: 2,
          })
          .then((res) => {
            if (res.status === 200) {
              if (type === 1) {
                // 旧手机验证
                this.show = false;
                this.code = "";
                clearInterval(this.timer);
                this.timer = null;
              } else {
                // 绑定/更换手机
                this.userInfo.phone = phone;
                this.updateUserInfo(this.userInfo, 3);
                clearInterval(this.timer);
                this.timer = null;
              }
            } else {
              this.$toast(res.error);
              this.code = "";
              this.reCode = "";
            }
          });
      } else {
        this.$toast("请填写验证码");
      }
    },
    onClickPwd() {
      if (
        this.newpwd !== "" &&
        this.newpwd !== undefined &&
        this.newpwd !== undefined
      ) {
        if (
          this.repwd !== "" &&
          this.repwd !== undefined &&
          this.repwd !== undefined
        ) {
          if (this.newpwd === this.repwd) {
            this.userInfo.password = md5(this.repwd);
            this.updateUserInfo(this.userInfo, 2);
          } else {
            this.$toast.fail("密码不一致");
            this.newpwd = "";
            this.repwd = "";
          }
        } else {
          this.$toast("请重复新密码");
        }
      } else {
        this.$toast("请填写新密码");
      }
    },
    confirmSex(val, index) {
      this.userInfo.sex = index === 0 ? 2 : 1;
      this.sexPop = false;
      this.updateUserInfo(this.userInfo);
    },
    confirmPicker(val) {
      let year = val.getFullYear();
      let month = val.getMonth() + 1;
      let day = val.getDate();
      this.userInfo.birthday = `${year}-${month}-${day}`;
      this.datePop = false;
      this.updateUserInfo(this.userInfo);
    },
    onClickPhone() {
      if (this.validatePhone()) {
        if (this.reCode !== "") {
          this.onClickCaptcha(this.newPhone, this.reCode, 2);
        } else {
          this.$toast("请输入短信验证码");
        }
      }
    },
    onClickIcon() {
      this.newPwdShow = !this.newPwdShow;
    },
    onClickReIcon() {
      this.rePwdShow = !this.rePwdShow;
    },
    setName() {
      this.title = "更改名字";
      this.newName = this.userInfo.nikeName;
      this.fadeName = true;
    },
    setPwd() {
      this.$refs.inputPwd.blur();
      this.title = "设置密码";
      this.fadeName = false;
      this.fadephone = false;
      this.bindPhone = false;
      this.fadeIn = true;
    },
    setPhone() {
      if (!this.phoneSub) {
        this.bindPhone = true;
        this.fadephone = false;
        this.title = "绑定手机号";
      } else {
        this.$refs.inputPhone.blur();
        this.title = "手机验证";
        this.fadephone = true;
        this.bindPhone = false;
        this.show = true;
        this.phoneNum = this.userInfo.phone.substring(
          this.userInfo.phone.length - 4
        );
        // 获取验证码
        this.getCode(1);
      }
    },
    validatePhone() {
      let flag = true;
      let reg = /^1[3456789]\d{9}$/;
      if (
        this.newPhone !== "" &&
        this.newPhone !== undefined &&
        this.newPhone !== null
      ) {
        if (!reg.test(this.newPhone)) {
          this.$toast("请输入正确的手机号");
          this.newPhone = "";
          flag = false;
          return;
        }
        if (this.newPhone === this.userInfo.phone) {
          this.$toast("手机号重复,请重新填写");
          this.newPhone = "";
          flag = false;
          return;
        }
      } else {
        this.$toast("请填写新的手机号");
        flag = false;
        return;
      }
      return flag;
    },
    toback() {
      this.$router.push("/mine");
    },
    prev() {
      this.title = "个人信息";
      this.fadeIn = false;
      this.fadephone = false;
      this.bindPhone = false;
      this.fadeName = false;
      this.show = true;
      this.reCode = "";
      clearInterval(this.timer);
      this.timer = null;
    },
    // 获取验证码
    getVerify(phone, codeType, type) {
      // codeType: 1 注册申请 2 B端验证码修改密码 3 C端修改手机号 4 C端客户验证码登录 5 C端验证码修改手机号
      this.$api
        .post(this, api.getVerify, {
          phone: phone,
          codeType: codeType,
        })
        .then((res) => {
          if (res.success) {
            this.$toast.success("验证码已发送");
            // 倒计时
            if (!this.timer) {
              this.count = TIME_COUNT;
              this.timer = setInterval(() => {
                if (this.count > 0 && this.count <= TIME_COUNT) {
                  this.count--;
                  if (type === 1) {
                    this.CodeStr = "剩余 " + this.count + "s";
                  } else {
                    this.reCodeStr = "剩余 " + this.count + "s";
                  }
                } else {
                  clearInterval(this.timer);
                  this.timer = null;
                  if (type === 1) {
                    this.CodeStr = "重新获取";
                  } else {
                    this.reCodeStr = "重新获取";
                  }
                }
              }, 1000);
            }
          } else {
            // 重置倒计时
            clearInterval(this.timer);
            this.CodeStr = "重新获取";
            this.reCodeStr = "重新获取";
            this.count = TIME_COUNT;
            this.$toast.fail(res.errMessage);
          }
        });
    },
  },
  watch: {
    "userInfo.sex": {
      handler(value) {
        // 0-女 1-男  2-全部
        let sex =
          value !== null && value !== undefined && value !== ""
            ? value.toString()
            : "";
        if (sex === "2") {
          this.sexValue = "女";
        } else if (sex === "1") {
          this.sexValue = "男";
        } else {
          this.sexValue = "";
        }
      },
      deep: true,
    },
    "userInfo.phone": {
      handler(value) {
        this.userInfo.phone = value;
      },
      deep: true,
    },
  },
  components: {
    VHeader,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin';

.user {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 0;
  background-color: #F6F6F6;

  .container {
    height: 100%;
    padding: 0 12px 0;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .avatar-box {
      display: inline-block;
      width: 100%;
      margin: 40px 0 26px;
      text-align: center;

      .avatar {
        display: inline-block;
        width: 58px;
        height: 58px;
        border-radius: 50%;
        border: 2px solid #ffffff;
        background-color: #ffffff;
        overflow: hidden;

        >>>.van-uploader__wrapper {
          width: 100%;
          height: 100%;

          .van-uploader__input-wrapper {
            margin: 0;
            width: 100%;
            height: 100%;
            border-radius: 50%;
            overflow: hidden;

            img {
              display: inline-block;
              width: 100%;
              height: 100%;
            }

            .van-icon {
              display: none;
            }
          }
        }
      }
    }

    >>>.van-cell-group {
      display: block;
      margin-bottom: 15px;
      font-size: $font-size-medium;
      border-radius: 8px;
    }

    >>>.van-cell {
      height: 56px;
      line-height: 36px;
      box-sizing: border-box;
      color: #383838;
      background-color: transparent;

      .van-field__control {
        font-size: $font-size-medium-x;
        text-align: right;
      }
    }

    .login-out {
      margin-top: 5px;
      margin-bottom: 40px;
      display: block;
      height: 56px;
      line-height: 56px;
      border-radius: 8px;
      text-align: center;
      font-size: $font-size-medium-x;
      color: #383838;
      background-color: #ffffff;
    }

    .setName-wrapper {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      box-sizing: border-box;
      text-align: center;
      background-color: #F6F6F6;
      transform: translate3d(100%, 0, 0);
      animation: all 10s linear 1;
      z-index: 100;

      &.show {
        transform: translate3d(0, 0, 0);
      }

      .content {
        padding-top: 121px;

        >>>.van-cell-group {
          display: inline-block;
          margin-bottom: 20px;
          width: 300px;
          height: 50px;
          line-height: 50px;
          border-radius: 8px;

          .van-cell {
            height: 50px;
            line-height: 30px;
            background-color: transparent;

            .van-field__control {
              font-size: $font-size-medium-x;
              text-align: center;
            }
          }

          .confirm {
            margin-top: 62px;
            btn-normal();
          }
        }
      }
    }

    .setpwd-wrapper {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      box-sizing: border-box;
      text-align: center;
      background-color: #F6F6F6;
      transform: translate3d(100%, 0, 0);
      animation: all 10s linear 1;
      z-index: 100;

      &.show {
        transform: translate3d(0, 0, 0);
      }

      .content {
        height: 100%;
        padding-top: 121px;
        box-sizing: border-box;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        scrollbar-width: none;
        -ms-overflow-style: none;

        &::-webkit-scrollbar {
          display: none;
        }

        >>>.van-cell-group {
          display: inline-block;
          margin-bottom: 20px;
          width: 300px;
          height: 50px;
          line-height: 50px;
          border-radius: 8px;

          .van-cell {
            height: 50px;
            line-height: 30px;
            background-color: transparent;

            .van-field__control {
              font-size: $font-size-medium-x;
              text-align: center;
            }
          }

          .confirm {
            margin: 62px 0 40px;
            btn-normal();
          }
        }
      }
    }

    .setphone-wrapper, .bind-mobile {
      position: fixed;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      box-sizing: border-box;
      text-align: center;
      background-color: #F6F6F6;
      transform: translate3d(100%, 0, 0);
      animation: all 10s linear 1;
      z-index: 100;

      &.show {
        transform: translate3d(0, 0, 0);
      }

      .content {
        height: 100%;
        padding-top: 112px;
        text-align: center;
        box-sizing: border-box;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        scrollbar-width: none;
        -ms-overflow-style: none;

        &::-webkit-scrollbar {
          display: none;
        }

        .captcha {
          .tip {
            display: inline-block;
            height: 20px;
            font-size: 16px;
            color: #9B9B9B;

            .tel {
              display: inline-block;
              margin: 0 4px;
              font-size: 18px;
              color: #4A4A4A;
            }
          }

          .again {
            display: block;
            width: 30%;
            text-align: center;
            margin: 20px auto 0;
            height: 30px;
            line-height: 30px;
            font-size: 14px;
            color: $color-theme;
          }

          .input {
            display: inline-block;
            margin-top: 30px;
            padding: 10px 16px;
            width: 300px;
            height: 50px;
            line-height: 30px;
            box-sizing: border-box;
            font-size: 16px;
            color: #9B9B9B;
            text-align: center;
            border-radius: 8px;
            background-color: #ffffff;

            &:after {
              border: none;
            }

            >>>.van-field__control {
              font-size: $font-size-medium-x;
              text-align: center;
            }
          }
        }

        .newphone {
          margin-top: 80px;

          .van-cell {
            &.code {
              display: block;
              margin: 22px auto 0;
              padding: 0 10px;
              width: 300px;
              height: 50px;
              line-height: 50px;
              box-sizing: border-box;
              color: #9B9B9B;
              background-color: #ffffff;
              border-radius: 8px;

              .van-button--primary {
                width: 80px;
                background-color: #e6e6e6;
                border: 1px solid #e6e6e6;
                color: #4A4A4A;
              }

              >>>.van-field__control {
                padding: 0;
                width: 100%;
                height: 50px;
                line-height: 50px;
                text-align: center;
                font-size: $font-size-medium-x;
                border: none;
              }
            }
          }

          .input {
            display: inline-block;
            margin-top: 30px;
            padding: 0 10px;
            width: 300px;
            height: 50px;
            line-height: 50px;
            font-size: 16px;
            color: #9B9B9B;
            text-align: center;
            border-radius: 8px;
            background-color: #ffffff;

            &:after {
              border: none;
            }

            >>>.van-field__control {
              font-size: $font-size-medium-x;
              text-align: center;
            }
          }
        }
      }
    }

    .btn-normal {
      margin: 82px 0 40px;
      btn-normal();
    }
  }
}
</style>
