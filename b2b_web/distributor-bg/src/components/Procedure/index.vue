<template>
  <section class="procedure">
    <div v-for="flow in flows" :key="flow.id">
      <div class="information">
        <div class="user"> {{flow.checkUserName}} </div>
        <div class="check-status"> {{statusFormatter(flow.checkStatus)}} </div>
      </div>
      <div class="icon" v-if="flows.indexOf(flow) != flows.length - 1">
        <i class="el-icon-arrow-right"></i>
      </div>
    </div>
    <div class="clear-float"></div>
  </section>
</template>

<script>
const delta = 15

export default {
  name: 'procedure',
  data() {
    return {
    }
  },
  mounted(){

  },
  props: {
    flows: {
      type: Array, //type: constructor
    },
    statusFormatter: {
      type: Function, //type: constructor
      default: _ => '加载中'
    },
    user: {
      type: Object, //type: constructor
      default: null
    },

  },
  methods: {
    userFormatter(){
      if(this.user){
        return 0;
      }else{
        return '加载中...';
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  section.procedure{
    border-top: 1px $tableColor solid;
    border-bottom: 1px $tableColor solid;
    background-color: $bg;
    padding: 70px 60px;
    text-align: center;
    div.information{
      border-box: box-sizing;
      width: 150px;
      float: left;
      margin-right: 50px;
      div.user{
        padding: 20px;
        border-radius: 30px;
        color: white;
        background-color: $lakeBlue;
      }
      div.check-status{
        margin-top: 15px;
      }
    }
    div.icon{
      border-box: box-sizing;
      width: 70px;
      height: 70px;
      font-size: 40px;
      color: grey;
      float: left;
      margin-right: 50px;
    }
    div.clear-float{
      clear:both;
    }
  }
</style>
