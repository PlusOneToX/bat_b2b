<template>
	<div class="good-stop-production">
		<header >
		  <h4>添加停产产品</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onCancel">
			返回商品等级列表
		  </el-button>
		</header>
	</div>
</template>
<script type="text/javascript">
export default {
	name: 'goodStopProduction',
	data() {
		
	},
	created() {
		this.getParams();
	},
	activated() {
     this.getParams();
  	},
	methods: {
		getParams(){
			
		},
	}
}

</script>
<style lang="scss" >
.good-stop-production {
    background-color: white;
	padding-bottom: 20px;
	header {
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
	  margin-bottom: 20px;
    }
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

</style>
