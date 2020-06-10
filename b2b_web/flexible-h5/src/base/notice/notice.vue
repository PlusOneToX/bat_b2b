<!--
 * @Author: yaowei
 * @Date: 2019-04-06 20:49:08
 * @LastEditors: yaowei
 * @LastEditTime: 2019-04-29 18:04:53
-->
<template>
  <div class="notice-wrap">
    <i class="sprite-icon notice"></i>
    <i class="sprite-icon announcement"></i>
    <span class="title">通知</span>
    <span class="line"></span>
    <div class="move-box">
      <div class="move">
        <span
          >五一工厂放假3天，4号开始生产30号2点之后订单和1号的，5号生产2-3号的，6号生产4-5号的。7号恢复生产时效，祝大家五一节日快乐。</span
        >
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="stylus" scoped>
// 公告
@keyframes moveAni {
  0% {
    left: 70%;
  }

  100% {
    left: -1000px;
  }
}

.notice-wrap {
  display: flex;
  padding: 15px 0;
  height: 18px;
  align-items: center;

  .sprite-icon {
    margin-right: 6px;
    margin-top: -3px;
  }

  .title {
    display: inline-block;
    margin-right: 6px;
    padding: 2px;
    font-size: 12px;
    border-radius: 2px;
    box-sizing: border-box;
    border: 1px solid #0076A5;
    color: #0076A5;
    vertical-align: center;
  }

  .line {
    display: inline-block;
    margin-right: 6px;
    width: 1px;
    height: 18px;
    background-color: #D0D0D0;
  }

  .move-box {
    position: relative;
    flex: 1;
    overflow: hidden;
    white-space: nowrap;
    height: 18px;
    line-height: 18px;

    .move {
      position: absolute;
      width: 1050px;
      top: 0;
      left: 100%;
      white-space: nowrap;
      animation: moveAni 15s infinite linear normal;

      span {
        display: inline-block;
        float: left;
        width: 1000px;
        font-size: 12px;
      }
    }
  }
}
</style>