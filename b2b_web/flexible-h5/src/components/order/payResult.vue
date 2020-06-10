<template>
    <div class="payResult">
      <v-header :back="true" :title=title @back="toback"></v-header>
      <div class="container" v-show="show">
<!--        <img class="img img-success" src="" alt="">-->
        <div class="img" :class="payFlag?'img-success':'img-fail'"></div>
        <div class="text" v-show="payFlag">订单提交成功哦～</div>
        <div class="text" v-show="!payFlag">订单提交失败啦～</div>
        <div class="remind">
          <div class="title">温馨提示</div>
          <div class="item-ul" v-show="payFlag">
            <div class="item-li">您的订单已进入工厂备料，最迟48小时内发货</div>
            <div class="item-li">定制产品无质量问题不提供退换货服务</div>
          </div>
          <div class="item-ul" v-show="!payFlag">
            <div class="item-li">您的订单支付失败，请稍后重新支付</div>
          </div>
          <div class="btn btn-submit" @click="submit">确认</div>
        </div>
      </div>
      <div class="mask" v-show="showResult">
        <div class="result">
          <div class="top">请确认支付是否已完成</div>
          <div class="middle" @click="result()">已完成支付</div>
          <div class="bottom" @click="result()">支付遇到问题，重新支付</div>
        </div>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import VHeader from 'components/v-header/v-header'
  import api from 'common/js/allApi.js'
  export default {
    name: 'payResult',
    data () {
      return {
        title: '支付结果',
        payFlag: false,
        showResult: true,
        show: false
      }
    },
    methods: {
      submit () {
        this.$router.push('/orderList')
      },
      result () {
        this.show = true
        let id = this.$route.query.id
        // 查询订单状态
        this.$api.get(this, api.orderStatus, {ids: id}).then(res => {
          if (res.status === 200) {
              let status = res.data
            this.showResult = false
              if (status === 1 || status === 7) {
                // 待付款或已取消，fail
                this.payFlag = false
              } else if (status === 2) {
                // 待发货，success
                this.payFlag = true
              }
          } else {
            this.$toast.fail(res.error)
          }
        }).catch(error => {
          console.log(error)
        })
      },
      toback () {
        this.$router.push('/orderList')
      }
    },
    beforeRouteEnter (to, from, next) {
      // if (from.name === 'orderList' || from.name === 'manager') {
      if (from.name === 'orderList' || from.name === 'mine') {
        next({
          // path: '/manager'
          path: '/mine'
        })
      } else {
        next()
      }
    },
    components: {
      VHeader
    }
  }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import '~common/styles/variable'
  @import '~common/styles/mixin.styl'
  .payResult
    position: fixed
    width: 100%
    top: 45px
    bottom: 0
    background-color:$color-background
    .container
      height:100%
      padding: 48px 28px 0
      box-sizing:border-box
      text-align:center
      .img
        display:inline-block
        width:240px
        height:140px
        background-repeat:no-repeat
        background-size:240px 140px
        background-position:center
        &.img-success
          background-image:url('../../common/images/pay-success.png')
        &.img-fail
          background-image:url('../../common/images/pay-fail.png')
      .text
        display:block
        margin-top:6px
        font-size:$font-size-medium
        color:$color-theme
      .remind
        margin-top:40px
        text-align:left
        .title
          font-size:$font-size-medium
          font-weight:bold
          color:$color-text
        .item-ul
          margin-top:12px
          .item-li
            position: relative
            display:block
            padding-left:12px
            line-height:1.5
            font-size:$font-size-small
            color:$color-text
            &:after
              content: ' '
              position: absolute
              left: 0
              top: 50%
              width: 4px
              height: 4px
              transform:translateY(-50%)
              background-color: #D0021B
              border-radius:100px
      .btn
        position:fixed
        bottom:40px
        left:50%
        transform:translateX(-50%)
        btn-submit()
    .mask
      position:fixed
      top:0
      left:0
      right:0
      bottom:0
      z-index: 99
      background: rgba(0, 0, 0, .2)
      .result
        position:absolute
        display: inline-block
        top:50%
        left:50%
        width:80%
        padding: 10px 0
        font-size:$font-size-medium
        line-height:3
        text-align:center
        border-radius:10px
        transform:translate3d(-50%, -50%, 0)
        background-color:$color-background-white
        .top
          color:#333333
        .middle
          height:40px
          line-height 40px
          color:red
          border-top:1px solid #F5F5F5
          border-bottom:1px solid #F5F5F5
        .bottom
          color:#999999
</style>
