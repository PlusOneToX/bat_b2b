<template>
  <div class="loading">
    <div class="spinner">
      <div class="spinner-container container1">
        <div class="circle1"></div>
        <div class="circle2"></div>
        <div class="circle3"></div>
        <div class="circle4"></div>
      </div>
      <div class="spinner-container container2">
        <div class="circle1"></div>
        <div class="circle2"></div>
        <div class="circle3"></div>
        <div class="circle4"></div>
      </div>
      <div class="spinner-container container3">
        <div class="circle1"></div>
        <div class="circle2"></div>
        <div class="circle3"></div>
        <div class="circle4"></div>
      </div>
    </div>
    <div class="text">{{message}}</div>
  </div>
</template>

<script>
    export default {
      name: 'loading',
      data () {
        return {}
      },
      props: {
        message: {
          type: String,
          default: ''
        }
      },
      watch: {
        message (value) {
          this.message = value
        }
      }
    }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  .loading
    position: fixed
    top: 50%
    left: 50%
    transform:translate3d(-50%, -50%, 0)
    padding: 30px 30px 20px
    border-radius: 10px
    background: rgba(0,0,0,0.8)
    display: flex
    flex-direction:column
    align-items: center
    justify-content: center
    z-index: 998
    pointer-events: none
    .text
      display: block
      margin-top:30px
      font-size:16px
      color:#ffffff
    .spinner
      display: block
      width: 30px
      height: 30px
      position: relative
    .container1 > div, .container2 > div, .container3 > div
      width: 6px
      height: 6px
      background-color: #FFFFFF
      border-radius: 100%
      position: absolute
      -webkit-animation: bouncedelay 1.2s infinite ease-in-out
      animation: bouncedelay 1.2s infinite ease-in-out
      -webkit-animation-fill-mode: both
      animation-fill-mode: both
    .spinner .spinner-container
      position: absolute
      width: 100%
      height: 100%
    .container2
      -webkit-transform: rotateZ(45deg)
      transform: rotateZ(45deg)
    .container3
      -webkit-transform: rotateZ(90deg)
      transform: rotateZ(90deg)
    .circle1
      top: 0
      left: 0
    .circle2
      top: 0
      right: 0
    .circle3
      right: 0
      bottom: 0
    .circle4
      left: 0
      bottom: 0
    .container2 .circle1
      -webkit-animation-delay: -1.1s
      animation-delay: -1.1s
    .container3 .circle1
      -webkit-animation-delay: -1.0s
      animation-delay: -1.0s
    .container1 .circle2
      -webkit-animation-delay: -0.9s
      animation-delay: -0.9s
    .container2 .circle2
      -webkit-animation-delay: -0.8s
      animation-delay: -0.8s
    .container3 .circle2
      -webkit-animation-delay: -0.7s
      animation-delay: -0.7s
    .container1 .circle3
      -webkit-animation-delay: -0.6s
      animation-delay: -0.6s
    .container2 .circle3
      -webkit-animation-delay: -0.5s
      animation-delay: -0.5s
    .container3 .circle3
      -webkit-animation-delay: -0.4s
      animation-delay: -0.4s
    .container1 .circle4
      -webkit-animation-delay: -0.3s
      animation-delay: -0.3s
    .container2 .circle4
      -webkit-animation-delay: -0.2s
      animation-delay: -0.2s
    .container3 .circle4
      -webkit-animation-delay: -0.1s
      animation-delay: -0.1s
    @-webkit-keyframes bouncedelay {
      0%, 80%, 100% { -webkit-transform: scale(0.0) }
      40% { -webkit-transform: scale(1.0) }
    }
    @keyframes bouncedelay {
      0%, 80%, 100% {
        transform: scale(0.0);
        -webkit-transform: scale(0.0);
      }
      40% {
      transform: scale(1.0);
      -webkit-transform: scale(1.0);
      }
    }
</style>
