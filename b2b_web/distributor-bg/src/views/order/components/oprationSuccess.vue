<template>
  <div class="success-page">
    <div>
      <i class="el-icon-warning"></i>
      <span class="msg">操作成功</span>
    </div>
    <div class="section2">
      <span>如果你不做出任何选择，将在</span>
      <span>{{second}}</span>
      <span>秒后跳到第一个链接地址</span>
    </div>
    <div>
      <i class="el-icon-d-arrow-right arrow"></i>
      <i class="el-icon-d-arrow-right arrow"></i>
      <span class="to-path" @click="toPath">订单详情</span>
    </div>
  </div>
</template>
<script>
// 引入eventBus是为了完成编辑后通知上层组件更新数据
import eventBus from '@/views/order/eventBus'

export default {
  props: {
    expire: Number,
    goHome: Function,
  },
  mounted(){
    if(this.expire){
      this.second = this.expire;
    }
    this.activated();
  },
  data() {
    return {
      second: 3,
      pathName: {
        name: '首页',
        link: 'dashboard',
      }
    }
  },
  methods: {
    activated() {
      // this.second = 3;
      // count down per second
      let interval = setInterval(() => {
        if(this.second == 0) {
          clearInterval(interval);
          if(this.goHome){ // 如果有父组件给的callback,则执行它
            this.goHome();
          }else{ // 如果没有父组件给的callback,则路由跳转
            this.$router.push({ name: this.pathName.link })
          }

          eventBus.$emit('shouldUpdateData');
          this.$emit('alreadyGoHome') // 告诉父组件“已跳转”，应调节页面状态
        } else if(this.second > 0) {
          this.second--;
        }
      }, 1000)
      if(this.$route.query.pathName) {
        this.pathName = this.$route.query.pathName;
      }

    },
    // 链接的点击事件
    toPath() {
      if(this.goHome){
        this.goHome();
      }else{
        this.$router.push({ name: this.pathName.link })
      }
    }
  }
}

</script>
<style lang="scss" scoped>
.success-page {
  background-color: white;
  min-height: 100%;
  padding-top: 20px;
  padding-left: 30px;
  .el-icon-warning {
    font-size: 35px;
    color: #E6A23C;
  }
  .msg {
    font-size: 30px;
    margin-left: 10px;
  }
  .section2 {
    margin: 20px 0;
  }
  .to-path {
    color: #409EFF;
    cursor: pointer;
    margin-left:10px;
  }
  .arrow{
    position:relative;
    animation:to-right 1s infinite linear;
  }
  @keyframes to-right{
    0% {
      opacity:1;
      left:0;
    }
    50%{
      opacity:0.3;
      left:10px;
    }
    100%{
      opacity:1;
      left:0;
    }
  }

}

</style>
