<!--
 * @Author: yaowei
 * @Date: 2019-11-25 14:26:49
 * @LastEditors: yaowei
 * @LastEditTime: 2019-01-12 16:04:54
-->
<template>
	<view class="picture-wrap">
		<view class="picture-category">
			<view class="item-list">
				<view class="item" :class="{curItem: curIndex === index}" v-for="(category, index) in picCategory" :key="index" @click="chooseTab(index)">
					{{category.name}}
					<view class="copyright" v-if="category.type === 2 && curIndex === index"><span class="text">版权</span></view>
				</view>
			</view>
		</view>
		
		<view class="pic-list" v-show="picData && picData.length > 0">
			<view class="img-list">
				<view class="img-wrap" v-for="(picture, picIndex) in picData" :key="picIndex" @click.stop="selectPic(picture)">
					<view class="img">
						<u-lazy-load :image="picture.originImage ? (picture.originImage + '?x-oss-process=image/resize,h_400,l_400') : picture.thumbnail"></u-lazy-load>
					</view>
				</view>
			</view>
		</view>
		<view class="no-data-wrap" v-show="picData && picData.length <= 0">
			<view class="no-data">
				<view class="empty-img"></view>
				<view class="text">暂无数据</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "pictureList",
		data() {
			return {
				curIndex: 0,
				picCategory: [], // 图库分类
				picData: [], // 图库
			};
		},
		created() {},
		methods: {
			// 选择分类
			chooseTab(index) {
				if (!this.$parent.isClick) {
					this.curIndex = index;
					let curCategory = this.picCategory[index].id;
					this.$emit("changeCategory", curCategory, 1, "show");
				}
			},
			// 选择图片
			selectPic(item) {
				if (!this.$parent.isClick) {
					this.$emit("changeSelect", item);
				}
			},
		},
		watch: {
			picCategory(data) {
				this.picCategory = data;
			},
			picData(data) {
				this.picData = data;
			},
		},
	};
</script>

<style lang="stylus" scoped>
	$white=#fff;
	$dark=#333;
	$light-gray=#999999;

	.picture-wrap {
		position: relative;
		width: 100%;
		height: 100%;
		
		.picture-category {
			position: relative;
			width: 100%;
			height: 50px;
			background-color: $white;
			overflow: hidden;
			
			&::before,
			&::after {
				content: "";
				position: absolute;
				top: 0;
				width: 15px;
				height: 100%;
				background-color: $white;
				z-index: 1;
			}
			
			&::before {
				left: 0;
			}
			
			&::after {
				right: 0;
			}
			.item-list {
				position: absolute;
				top: 0;
				right: 0;
				left: 0;
				padding-top: 18px;
				height: 50px;
				white-space: nowrap;
				overflow-x: scroll;

				&::-webkit-scrollbar {
					display: none;
				}
			}
			.item {
				position: relative;
				display: inline-block;
				padding: 0 15px;
				font-size: 14px;
				color: $light-gray;
				
				.copyright {
					position: absolute;
					top: -18px;
					left: 50%;
					width: 32px;
					height: 18px;
					font-size: 12px;
					text-align: center;
					background: url("../../static/common/image/banquan.png") no-repeat;
					background-size: 100% 100%;
					
					.text {
						position: absolute;
						top: 2px;
						left: 4px;
						line-height: 1;
						color: $dark;
						transform: scale(0.8);
					}
				}
				
				&.curItem {
					font-size: 16px;
					color: $dark;
					
					&::after {
						content: "";
						position: absolute;
						left: 50%;
						bottom: -12px;
						width: 20px;
						height: 10px;
						background: url("../../static/common/image/home-tab.png") no-repeat;
						background-size: 100% 100%;
						transform: translateX(-50%);
					}
				}
			}
		}
		
		.pic-list {
			position: absolute;
			top: 50px;
			bottom: 40px;
			padding: 10px 7px;
			width: 100%;
			
			.img-list {
				position: absolute;
				top: 0;
				right: 7px;
				bottom: 0;
				left: 7px;
				overflow: hidden;
			
				&::-webkit-scrollbar {
					display: none;
				}
			}
			
			.img-wrap {
				float: left;
				width: calc((100% / 3));
				padding: 7px;
			
				.img {
					position: relative;
					width: 100%;
					height: 180px;
					border-radius: 10px;
					overflow: hidden;
				}
				
				>>>.u-wrap,
				>>>.u-lazy-item {
					position: absolute !important;
					top: 50%;
					left: 50%;
					min-width: 100%;
					height: 180px !important;
					background-color: $white;
					transform: translate(-50%, -50%);
					border-radius: 10px !important;
					overflow: hidden;
				}
			}
		}
	}
	
	.no-data {
	  position: absolute;
	  width: 100%;
	  padding: 0 30px;
	  top: 50%;
	  left: 50%;
		text-align: center;
	  transform: translate(-50%, -50%);
	
	  .empty-img {
	    display: inline-block;
	    width: 180px;
			height: 132px;
			background: url("../../static/common/image/no-data.png") no-repeat;
			background-size: 100% 100%;
	  }
	
	  .text {
	    display: block;
	    margin-top: $spacing-sm;
	    font-size: $font-lg;
	    color: $color-font-base;
	    line-height: 45px;
	  }
	}
</style>
