<template>
	<div class="entry-wrap rl-clear">
		<div class="entry-l rl-fl">
			<div class="user-handle-box-login" v-if="this.userId !== ''&&  this.userId !== null">
				<div class="user-img-box-login">
					<img class="user-img-login" src="../../assets/images/index/img-head.png" />
				</div>
				<div class="user-handle-login">
					<p>HI~，{{userName}}</p>
					<p>{{$t('P.Welcome')}}</p>
				</div>
			</div>
			<div class="user-handle-box"  v-else>
				<div class="user-img-box">
					<img class="user-img" src="../../assets/images/index/img-head.png" />
				</div>
				<div class="user-handle">
					<span class="item-handle" @click="goPath('/Login')">{{$t('P.Login')}}</span>
				</div>
			</div>

		</div>
		<ul class="entry-m rl-fl">
			<!-- 我的订单 -->
			<li class="entry-list" @click="goPath('/OrderManage')" @mouseover="imgIn(1)" @mouseleave="imgOut(1)">
				<span class="img-wrap">
					<img :src="iconOrder" id="icon_order" />
				</span>
				<p>{{$t('P.MyOrder')}}</p>
			</li>
			<!-- 快速下单 -->
			<li class="entry-list" @click="goPath('/Classify')" @mouseover="imgIn(2)" @mouseleave="imgOut(2)">
				<span class="img-wrap">
					<img :src="iconDH" id="icon_dinghuo" />
				</span>
				<p>{{$t('P.QuickOrder')}}</p>
			</li>
			<!-- 购物车 -->
			<li class="entry-list" @click="goPath('/ShopCarts')" @mouseover="imgIn(3)" @mouseleave="imgOut(3)">
				<span class="img-wrap">
					<img :src="iconSP" id="icon_shopcart" />
				</span>
				<p>{{$t('ShopCarts.ShopCar')}}</p>
			</li>
			<!-- 培训中心 -->
			<!-- <li class="entry-list" @click="goPath('/Training')">
				<span class="img-wrap">
					<img src="../../assets/images/index/icon-peixun.png"
						:alt="($i18n.locale === 'zh') ? ('BAT商城-' + $t('QuickEntry.Training')) : ('BAT-' + $t('QuickEntry.Training'))" />
				</span>
				<el-tooltip class="item" effect="dark" :content="($i18n.locale === 'zh') ? '正在开发中' : 'developing'"
					placement="bottom">
					<p>{{$t('QuickEntry.Training')}}</p>
				</el-tooltip>
			</li> -->
			<!-- 下载中心 -->
			<!-- <li class="entry-list" @click="goPath('/Download')">
				<span class="img-wrap">
					<img src="../../assets/images/index/icon-download.png"
						:alt="($i18n.locale === 'zh') ? ('BAT商城-' + $t('QuickEntry.Download')) : ('BAT-' + $t('QuickEntry.Download'))" />
				</span>
				<el-tooltip class="item" effect="dark" :content="($i18n.locale === 'zh') ? '正在开发中' : 'developing'"
					placement="bottom">
					<p>{{$t('QuickEntry.Download')}}</p>
				</el-tooltip>
			</li> -->
			<!-- 常见问题 -->
			<!-- <li class="entry-list" @click="goPath('/About', {type: 2})">
				<span class="img-wrap">
					<img src="../../assets/images/index/icon-question.png"
						:alt="($i18n.locale === 'zh') ? ('BAT商城-' + $t('QuickEntry.Question')) : ('BAT-' + $t('QuickEntry.Question'))" />
				</span>
				<p>{{$t('QuickEntry.Question')}}</p>
			</li> -->
		</ul>
		<!-- 拼团 -->
		<!-- <ul class="entry-r rl-fl">
			<li class="item-list ping" @click="goPath('/GroupBuying')">{{$t('GroupBuy.GroupBuying')}}</li>
		</ul> -->

		<!-- 产品介绍 -->
		<productIntro v-show="showIntroDialog" @closeIntro="closeProductIntro"></productIntro>
	</div>
</template>

<script>
	import productIntro from '@/components/dialog/productIntro';
	export default {
		data() {
			return {
				userId: '',
				userName: '',
				showIntroDialog: false,
				iconOrder: 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-order.png',
				iconDH: 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-dinghuo.png',
				iconSP: 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-shopcart.png'
			};
		},
		components: {
			productIntro
		},
		props: ['hui_id'],
		created() {
			this.userId = window.localStorage.getItem('userId');
			this.userName = window.localStorage.getItem('name');
		},
		methods: {
			// 页面跳转
			goPath(path, query) {
				if (query) {
					this.$router.push({
						path: path,
						query: query
					});
				} else {
					this.$router.push({
						path: path
					});
				}
			},
			// 产品介绍 - 关闭
			closeProductIntro() {
				this.showIntroDialog = false;
			},

			//鼠标经过
			imgIn(id) {
				console.log('鼠标经过 = ' + id);
				if (id === 1) {
					this.iconOrder = 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-order-select.png';
				} else if (id === 2) {
					this.iconDH = 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-dinghuo-select.png';
				} else if (id === 3) {
					this.iconSP = 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-shopcart-select.png';
				}
			},

			//鼠标离开
			imgOut(id) {
				console.log('鼠标离开 = ' + id);
				if (id === 1) {
					this.iconOrder = 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-order.png';
				} else if (id === 2) {
					this.iconDH = 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-dinghuo.png';
				} else if (id === 3) {
					this.iconSP = 'https://bat.oss-cn-hangzhou.aliyuncs.com/icon/icon-shopcart.png';
				}
			}
		}
	};
</script>

<style lang="less" scoped>
	@import url("../../assets/less/variable.less");

	.entry-wrap {
		position: absolute;
		bottom: 0;
		left: 50%;
		width: @center;
		height: 90px;
		margin-left: -600px;
		background: rgba(255, 255, 255, 0.95);
		box-shadow: 0px 7px 16px 2px rgba(0, 0, 0, 0.14);
		border-radius: 16px;

		.entry-l {
			position: relative;
			width: 265px;
			height: 100%;
			box-sizing: border-box;
			display: flex;
			align-items: center;
			
			.user-handle-box{
				width: 100%;
				display: flex;
				.user-img-box {
					width: 55%;
					height: 100%;
					display: flex;
					align-items: center;
					justify-content: flex-end;
					.user-img {
						width: 36px;
						height: 36px;
						border-radius: 50%;
						padding-right: 15px;
					}
				}
				
				.user-handle {
					width: 45%;
					display: flex;
					align-items: center;
				
					.item-handle {
						cursor: pointer;
				
						&:hover {
							color: @bat_vi;
						}
					}
				}
			}
			
			.user-handle-box-login{
				width: 100%;
				display: flex;
				.user-img-box-login {
					width: 35%;
					height: 100%;
					display: flex;
					align-items: center;
					justify-content: flex-end;
					.user-img-login {
						width: 36px;
						height: 36px;
						border-radius: 50%;
						padding-top: 3px;
					}
				}
				
				.user-handle-login {
					width: 65%;
				    line-height: 20px;
					padding-left: 10px;
				}
			}
		}

		.entry-m {
			position: relative;
			width: 410px;
			height: 100%;
			display: flex;
			align-items: center;
			justify-content: center;
			box-sizing: border-box;

			&::before,
			&::after {
				content: "";
				position: absolute;
				top: 15px;
				left: 0;
				width: 1px;
				height: 60px;
				background-color: @entryBd;
			}

			&::after {
				right: 0;
				left: inherit;
			}

			.entry-list {
				flex: 1;
				text-align: center;
				cursor: pointer;

				.img-wrap {
					width: 40px;
					height: 40px;
					display: inline-block;

					img {
						width: 100%;
						height: 100%;
						transform: scale(0.8);
						transition: all 0.3s;
					}
				}

				&:hover {
					img {
						transform: scale(1);
					}

					p {
						color: @bat_vi;
					}
				}

				p {
					margin-bottom: 5px;
					font-size: 12px;
					color: @black;
				}
			}
		}

		.entry-r {
			width: 265px;
			height: 100%;
			padding: 0 15px;
			display: flex;
			align-items: center;
			justify-content: center;
			box-sizing: border-box;

			.item-list {
				padding-left: 35px;
				height: 30px;
				line-height: 30px;
				flex: 1;
				font-size: 12px;
				color: @lightBlack;
				cursor: pointer;

				&:hover {
					color: @blue;
				}

				&.ping {
					background: url("../../assets/images/index/icon-ping.png") no-repeat left center;
					background-size: auto 100%;
				}

				&.hui {
					background: url("../../assets/images/index/icon-hui.png") no-repeat left center;
					background-size: auto 100%;
				}
			}
		}

		.user-handle {
			font-size: 0;

			p {
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			span {
				font-size: 14px;
				color: @lightBlack;
			}
		}
	}
</style>
