<template>
  <section class="procedure">
  <div class="title">审批流程</div>
    <div v-for="item in flow" :key="item.id">
      <div class="information">
        <div class="user"> {{item.checkUserName}} </div>
        <div class="check-status"> {{formatStatus(item.checkStatus)}} </div>
      </div>
      <div class="icon" v-if="flow.indexOf(item) != flow.length - 1">
        <i class="el-icon-arrow-right"></i>
      </div>
    </div>
    <div class="clear-float"></div>
  </section>
</template>
<script>
export default {
  name: 'procedure',
  data() {
    return {
      flow:[],
    }
  },
  created() {
    this.getParams();
  },
  props: {
    flows: {
      type: Array, //type: constructor
      default: []
    },
  },
  methods: {
    getParams(){
      let ids = [];
      let ary = this.flows;
      this.flows.forEach(item => {
        ids.push(item.checkUser);
      })
      this.$api.get(this, 'admin/u/po/admin/ids', { ids: ids.join(',') }).then(res => {
        if(res.code == 0) {
          ary.forEach(item => {
            res.admins.forEach(val => {
              if(val.id == item.checkUser) {
                item.checkUserName = val.name;
              }
            })
          })
          this.flow=ary;
        }
      })
    },
    formatStatus(type) {
      switch(type) {
        case 0 :
        return "未审批";
        break;
        case 1:
          return '审批中';
          break;
        case 2:
          return '已通过';
          break;
        case 3:
          return '已拒绝';
          break;
        case 5:
          return '发起人';
          break;
        default:
          return '-'
      }
    }
  },
  watch:{
    flows: {
      handler() {
        this.getParams()
      },
      deep: true
    }
  }
}

</script>
<style rel="stylesheet/scss" lang="scss" scoped>
section.procedure {
  border-top: 1px $tableColor solid;
  border-bottom: 1px $tableColor solid;
  background-color: $bg;
  padding:20px;
  padding-left: 60px;
  padding-right: 60px;
  text-align: center;
  .title{
      font-weight:700;
      margin-bottom:10px;
  }
  div.information {
    width: 190px;
    float: left;
    margin-right: 50px;
    .user {
      padding: 0px;
      border-radius: 30px;
      color: white;
      background-color: $lakeBlue;
    }
    .check-status {
      margin-top: 15px;
    }
  }
  div.icon {
    width: 70px;
    height: 70px;
    font-size: 40px;
    color: grey;
    float: left;
    margin-right: 50px;
  }
  div.clear-float {
    clear: both;
  }
}

</style>
