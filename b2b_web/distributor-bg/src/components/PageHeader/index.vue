<template>
	<div class="page-header">
		<el-row class="header">
		<header>
			<h4>{{title}}</h4>
			<el-button class="mini-search-btn btn-home" icon="el-icon-plus" @click="handleClick"> {{button}} </el-button>
		</header>
	</el-row>
	</div>
</template>
<script>
	export default {
		props: {
			title:{
				type:String,
				required:true
			},
			button:{
				type:String,
			},
			icon:{
				type:String,
			}
		},
		methods:{
			handleClick(){
				this.$emit('handle-click')
			}
		}
	}
</script>
<style lang="scss" scoped>
	.page-header{
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