<template>
  <div class="countDown" v-if="false">
    <div class="timer" id="timer" v-show="flag">{{$t('P.Surplus')}}<span>{{day}}</span>{{$t('P.Day')}}<span>{{hours}}</span>{{$t('P.Hours')}}<span>{{minutes}}</span>{{$t('P.Minutes')}}  {{$t('P.AutoOff')}}</div>
  </div>
</template>

<script>
export default {
  props: {
    actEndTime: {
      type: Number
    },
    payStatus: {
      type: Number
    },
    orderStatus: {
      type: Number
    },
    orderId: {
      type: Number
    },
    createTime: {
      type: Number
    }
  },
  data () {
    return {
      obj: null,
      day: 0,
      hours: 0,
      minutes: 0,
      flag: false,
      isclose: true,
      now: 1583424000000
    }
  },
  created () {
      // this.countDown()
  },
  methods: {
    timeFormat (param) {
      return param < 10 ? '0' + param : param
    },
    countDown () {
      if (this.actEndTime > 0 && this.orderStatus !== 5) {
        let interval = setInterval(() => {
          // 获取当前时间，同时得到订单付款截止时间数组
          let newTime = new Date().getTime()
          // 对截止时间进行处理渲染到页面
          let endTime = this.actEndTime
          // 计算相差天数
          let diff = this.now - this.createTime
          let diffDay = Math.floor(diff / (24 * 3600 * 1000))
          // 如果截止时间未结束，对时间进行处理
          if (endTime - newTime > 0 && diffDay < 2) {
            this.flag = true
            let time = (endTime - newTime) / 1000
            // 获取天、时、分、秒
            let day = parseInt(time / (60 * 60 * 24))
            let hou = parseInt((time % (60 * 60 * 24)) / 3600)
            let min = parseInt(((time % (60 * 60 * 24)) % 3600) / 60)
            // let sec = parseInt(((time % (60 * 60 * 24)) % 3600) % 60)
              this.day = this.timeFormat(day)
              this.hours = this.timeFormat(hou)
              this.minutes = this.timeFormat(min)
          } else {
            // 截止时间已结束，自动关闭
            this.$api.put(this, 'user/u/order/close', {id: this.orderId, type: 1}).then((res) => {
              if (res.code === 0) {
                this.flag = false
              }
            })
            clearInterval(interval)
          }
        }, 1000)
      }
    }
  },
  watch: {
    orderId (val) {
      this.orderId = val
    },
    actEndTime (val) {
      this.actEndTime = val
    },
    payStatus (val) {
      this.payStatus = val
    },
    orderStatus (val) {
      this.orderStatus = val
    },
    createTime (val) {
      this.createTime = val
      // this.countDown()
    }
  }
}
</script>

<style scoped="scoped" lang='less'>
  .countDown {
    display: block;
    width: 100%;
    float:right;
    text-align: left;
    .timer {
      font-size: 12px;
      span {
        color: #FF7900;
      }
    }
  }
</style>
