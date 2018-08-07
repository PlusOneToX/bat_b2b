<!--
 * @Author: yaowei
 * @Date: 2018-05-19 13:51:06
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-24 13:56:32
-->
<template>
  <ul class="floating-wrap">
    <li>
      <img
        src="../../assets/images/index/icon-phone.png"
        :alt="($i18n.locale === 'zh') ? 'BAT商城' : 'bat'"
      />
      <div class="phone-wrap hover-wrap">{{($i18n.locale === 'zh') ? '0755-21011282' : '0755-21011282'}}</div>
    </li>
    <li>
      <img
        src="../../assets/images/index/icon-email.png"
        :alt="($i18n.locale === 'zh') ? 'BAT商城' : 'bat'"
      />
      <div class="email-wrap hover-wrap">bat@bat.com.cn</div>
    </li>
  </ul>
</template>

<script>
export default {
  data() {
    return {};
  },
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.floating-wrap {
  position: fixed;
  right: 0;
  bottom: 100px;
  li {
    position: relative;
    width: 58px;
    height: 58px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    img {
      width: 46px;
      height: 46px;
      border-radius: 50%;
    }
    .hover-wrap {
      display: none;
      position: absolute;
      right: 60px;
      padding: 10px 15px;
      min-width: 120px;
      background-color: @white;
      border-radius: 8px;
      box-shadow: 0 2px 6px 0 @bdColor;
    }
    &:hover {
      img {
        box-shadow: 0 2px 6px 0 @bdColor;
      }
      .hover-wrap {
        display: block;
      }
    }
  }
}
</style>