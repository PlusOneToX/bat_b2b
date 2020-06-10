<template>
  <div class="error">
    <img src="../../common/images/store/store-close@2x.png" alt="">
    <div class="text"> <span class="name">{{name}}</span>已关闭</div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      name: ''
    }
  },
  mounted () {
    this.name = this.$route.query.name
  }
}
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import '../../common/styles/variable.styl';
  .error
    position: fixed;
    flex-direction: column;
    width: 100%;
    top: 0;
    bottom: 0;
    padding-top:45%;
    text-align:center;
    background-color: $color-background-white;
    img
      display:inline-block;
      width:150px;
      height:150px;
    .text
      margin-top:10%
      font-size:16px
      .name
        color:#0076A5
    .btn
      display: inline-block;
      width: 215px;
      height: 40px;
      margin-top:10%;
      line-height: 40px;
      font-size: 16px;
      text-align: center;
      color: #ffffff;
      border-radius: 70px;
      &.btn-normal-blue
        background: #0076A5;
</style>