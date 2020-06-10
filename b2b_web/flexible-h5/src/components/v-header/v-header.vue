<template>
  <div class="vheader">
    <i v-if="back" class="icon icon-left" @click="toback"></i>
    <span class="text">{{title}}</span>
  </div>
</template>
<script type="text/ecmascript-6">
  export default {
    name: 'vheader',
    props: {
      back: {
        type: Boolean,
        default: false
      },
      title: {
        type: String,
        default: ''
      }
    },
    methods: {
      toback () {
        if (this.$listeners['back']) {
          // 如果提供执行
          this.$emit('back')
        } else {
          this.$router.back('-1')
        }
      }
    }
  }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  .vheader
    position:fixed
    top:0
    left:0
    width:100%
    height:45px
    line-height:45px
    padding:0 15px
    box-sizing:border-box
    text-align:center
    justify-items: center
    background-color:#ffffff
    z-index:99
    .icon
      display: inline-block
      margin-top: -2px
      margin-left: - 15px
      font-size:36px
      line-height:45px
      float:left
      color: #4a4a4a
    .text
      display:inline-block
      margin-left:-18px
      height 45px
      line-height: 45px
      font-size:18px
      color:#4A4A4A
</style>
