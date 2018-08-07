<template>
  <div class="check-lin">
    <header v-if="append">
      <h4>添加限购规则</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="addpuls()">返回限制购买量列表</el-button>
    </header>

    <header v-if="looking">
		  <h4>查看限购规则</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="addpuls"> 返回限制购买量列表 </el-button>
		</header>


    <el-tabs v-model="checkLinHead">
      <el-tab-pane label="限购信息" name="0"></el-tab-pane>
      <el-tab-pane label="操作日志" name="1" v-if="checkMsg == 1"></el-tab-pane>
    </el-tabs>

    <div>
      <addrestriction v-show="checkLinHead == 0" :checkId="this.checkId"></addrestriction>
      <operationLog v-show="checkLinHead == 1" :checkId="this.checkId"></operationLog>
    </div>
  </div>
</template>

<script>
// 引入组件
import addrestriction from "@/views/goods/restriction/addrestriction/components/addrestriction" // 限购信息
import operationLog from "@/views/goods/restriction/addrestriction/components/operationLog" // 限购信息
export default {
  name: 'checklin',
  components: {
    addrestriction, operationLog
  },
  data() {
    return {
      checkLinHead: "0",
      append: true,
      looking: false
    }
  },
  created() {
    if(this.$route.query.id) {
      this.append = false
      this.looking = true
    }else {
      this.append = true
      this.looking = false
    }
  },
  computed: {
    checkMsg() { // 限购规则详情页面判断 0 添加限购规则 1 限购规则查看
			return this.$route.query.checkMsg
    },
    checkId() {
      return this.$route.query.id
    }
  },
  methods: {
    addpuls() { // 返回限制购买量列表
      this.$router.push({ name: 'restrictionlist'})
    },
  },
}
</script>

<style lang="scss">
.check-lin {
  background-color: white;
  min-height: 100%;
  header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    } 
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
}
</style>
