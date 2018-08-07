<template>
	<div class="add-productline" >
		<header style="margin-bottom: 0px;">
		  <h4>标签预览</h4>
		  <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="handleCancel">
			返回
		  </el-button>
		</header>
		<iframe style="height: 100%;width: 100%;" :src="url"></iframe>
	</div>
</template>
<script>
export default {
	data() {
		return {
			url:''
		}
	},
	created() {
		if(this.$route.params.url !== undefined){
			this.url = this.$route.params.url
		}
	},
	methods: {
		handleCancel() {
			this.$router.go(-1)
		}
	}
}

</script>
<style lang="scss" scoped>
.add-productline {
	height: 100vh;
	width: 100%;
	.header-row {
		@include table-header-color;
	}
	header {
		color: white;
		height: $lineHight;
		line-height: $lineHight;
		background-color: $lakeBlue;
		
	}
	h4 {
		display: inline-block;
		font-weight: 350;
		font-size: 16px;
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
