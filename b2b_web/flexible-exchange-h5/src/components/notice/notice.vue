<!--
 * @Author: yaowei
 * @Date: 2019-04-06 20:49:08
 * @LastEditors: yaowei
 * @LastEditTime: 2019-07-23 11:07:24
-->
<template>
  <div class="notice-wrap">
    <span class="sprite-icon icon-notice"></span>
    <div class="bar">
      <div class="bartext" id="marquee">{{ noticeData }}</div>
    </div>
  </div>
</template>

<script>
// api
import api from "api/allApi.js";

export default {
  name: "Notice",
  data() {
    return {
      noticeData: "", // 公告
    };
  },
  created() {
    // 获取公告
    this.initNotice();
  },
  mounted() {},
  methods: {
    // 获取公告
    initNotice() {
      this.$api.get(this, api.getNotice).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let noticeData = res.data;
            this.noticeData = "";
            // 获取当前时间戳
            let now = new Date().getTime();
            noticeData.forEach((item) => {
              if (
                item.status === 1 &&
                now >= item.startTime &&
                now <= item.endTime
              ) {
                // 判断大于开始时间并且小于结束时间，拼接公告内容
                this.noticeData += item.content;
              }
            });

            if (this.noticeData) {
              this.$parent.showNotice = true;
            } else {
              this.$parent.showNotice = false;
            }

            setTimeout(() => {
              // 公告滚动
              this.scrollNotice();
            }, 500);
          } else {
            this.$parent.showNotice = false;
          }
        } else {
          this.$toast.fail(res.errMessage);
        }
      });
    },
    // 公告滚动
    scrollNotice() {
      // 获取内容区宽度
      let width = document.getElementById("marquee").scrollWidth;
      let marquee = document.getElementById("marquee");
      let speed = 0; // 位移距离
      // 设置位移
      setInterval(function () {
        speed = speed - 1;
        // 如果位移超过文字宽度，则回到起点
        if (-speed >= width) {
          speed = window.screen.width - 15;
        }
        marquee.style.transform = "translateX(" + speed + "px)";
      }, 35);
    },
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.notice-wrap {
  display: flex;
  padding: $spacing-sm 0;
  height: 18px;
  align-items: center;

  .sprite-icon {
    width: 16px;
    margin-right: 12px;
    margin-top: -6px;
  }

  .bar {
    width: 100%;
    overflow: hidden;
  }

  .bartext {
    display: inline-block;
    font-size: $font-xm;
    white-space: nowrap;
  }
}
</style>