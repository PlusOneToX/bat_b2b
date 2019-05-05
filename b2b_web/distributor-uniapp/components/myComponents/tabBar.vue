<template>
	 <view class="tabBar-componets">
		 <view class="tabBar-componets-line" @click="toPage('/pages/index/index')">
			 <text class="iconfont icon-tab_to_home_pre" :style="{'color':themeColor}" v-if="pageIndex==1"></text>
			 <text class="iconfont icon-tab_to_shopping tabBar-componets-text" v-else ></text>
			 <view :style="{'color':pageIndex==1?themeColor:'#666'}">首页</view>
		 </view>
		 <view class="tabBar-componets-line" :style="{'color':themeColor}" @click="toPage('/pages/classify/classify')">
			 <text class="iconfont icon-tab_to_class_pre" v-if="pageIndex==2"></text>
			 <text class="iconfont icon-tab_to_class tabBar-componets-text" v-else></text>
			 <view :style="{'color':pageIndex==2?themeColor:'#666'}">分类</view>
		 </view>
		 <view class="tabBar-componets-line" :style="{'color':themeColor}" @click="toPage('/pages/quickOrder/quickOrder')"> 
			 <text class="iconfont icon-tab_to_speed_pre" v-if="pageIndex==3"></text>
			 <text class="iconfont icon-tab_to_speed tabBar-componets-text" v-else></text>
			 <view :style="{'color':pageIndex==3?themeColor:'#666'}">快速订货</view>
		 </view>
		 <view class="tabBar-componets-line" :style="{'color':themeColor}" @click="toPage('/pages/shoppingCart/shoppingCart')">
			 <text class="iconfont icon-tab_to_shoppingp_pre" v-if="pageIndex==4"></text>
			  <text class="iconfont icon-tab_to_shopping tabBar-componets-text" v-else></text>
			 <view :style="{'color':pageIndex==4?themeColor:'#666'}">购物车</view>
		 </view>
		 <view class="tabBar-componets-line" :style="{'color':themeColor}" @click="toPage('/pages/personalCenter/personalCenter')">
			 <text class="iconfont icon-tab_to_my" v-if="pageIndex==5"></text>
			 <text class="iconfont icon-tab_to_my tabBar-componets-text" v-else></text>
			 <view :style="{'color':pageIndex==5?themeColor:'#666'}">我的</view>
		 </view>
	 </view>
</template>

<script>
	import {addShoppingcart,} from '../../common/api.js'
	import {isH5,isMpWeixin } from '../../common/common.js'
	export default {
		props: {
		    pageIndex: {
		      
		    },
			themeColor:{
				
			}
		    
		},
		watch:{
			
			


		},
		data() {
			return {
			  pageType:1,
			 
			}
		},
		onShow(){
			
		},
		onLoad() {
	     
		},
		methods: {
			toPage(url){
				console.log('11')
				uni.redirectTo({
					url:url
				})
			}
		

		
		},
	}
</script>

<style  lang="scss">
.tabBar-componets{
	font-family: PingFangSC-Regular, PingFang SC;
	position: fixed;
	width: 720rpx;
	background: #fff;
	border-top: 1rpx solid #F3F4F8;
	bottom: 0;
	left: 0;
	height: 112rpx;
	z-index: 9999;
	display: flex;
	justify-content: space-around;
	padding: 10rpx 15rpx;
	.tabBar-componets-line{
		text-align: center;
		text{
			font-size: 68rpx;
		}
		.tabBar-componets-text{
			color: #666;
		}
		view{
			font-size:26rpx ;
		}
	}
}
</style>
