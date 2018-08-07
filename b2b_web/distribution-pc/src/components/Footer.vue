<template>
	<div class="footer" :class="{'no-padding': (cateFlag === 'group' || cateFlag === 'activity')}">
		<div class="footer-top">
			<div class="service rl-clear rl-padding-horizontal-lllg">
				<div class="service-all">
					<div class="footer-nav rl-fl">
						<div class="rl-fl-img">
							<img src="https://bat.oss-cn-hangzhou.aliyuncs.com/icon/sh_bz.png">
						</div>
						<div class="text">
							<span class="name">{{$t('Foot.Various')}}</span>
							<span class="tip">{{$t('Foot.ConShopping')}}</span>
						</div>
					</div>
					<div class="footer-nav rl-fl">
						<div class="rl-fl-img">
							<img src="https://bat.oss-cn-hangzhou.aliyuncs.com/icon/bj_zf.png">
						</div>
						<div class="text">
							<span class="name">{{$t('Foot.ReliableQuality')}}</span>
							<span class="tip">{{$t('Foot.FreeSelection')}}</span>
						</div>
					</div>
					<div class="footer-nav rl-fl footer-guarantee">
						<div class="rl-fl-img">
							<img src="https://bat.oss-cn-hangzhou.aliyuncs.com/icon/kj_xd.png">
						</div>
						<div class="text">
							<span class="name">{{$t('Foot.GuaranteedService')}}</span>
							<span class="tip">{{$t('Foot.QuickResponse')}}</span>
						</div>
					</div>

					<div class="footer-nav rl-fl">
						<div class="rl-fl-img">
							<img src="https://bat.oss-cn-hangzhou.aliyuncs.com/icon/ks_wl.png">
						</div>
						<div class="text">
							<span class="name">{{$t('Foot.kuaisuExpress')}}</span>
							<span class="tip">{{$t('Foot.kuaisuExpressInfo')}}</span>
						</div>
					</div>

				</div>
			</div>
			<div class="all rl-bg-white">
				<div class="content rl-margin-zero">
					<dl class="cons rl-fl">
						<dt class="rl-text-xs">
							<i class="iconfont icon-shop-guide"></i>
							{{$t('Foot.ShoppingDirectory')}}
						</dt>
						<span class="line"></span>
						<div class="cons-dd">
							<dd @click="goAbout(0)">{{$t('Foot.AboutUs')}}</dd>
							<dd @click="goAbout(1)">{{$t('About.HowToBuy')}}</dd>
						</div>
					</dl>
					<dl class="cons rl-fl" v-if="$i18n.locale === 'zh'">
						<dt class="rl-text-xs">
							<i class="iconfont icon-logistics"></i>
							{{$t('Foot.DeliveryMethod')}}
						</dt>
						<span class="line"></span>
						<div class="cons-dd">
							<dd @click="goHelpCenter(3)">{{$t('Foot.DeliveryService')}}</dd>
							<dd @click="goHelpCenter(4)">{{$t('Foot.DeliveryServiceQuery')}}</dd>
							<dd @click="goHelpCenter(5)">{{$t('Foot.DeliveryChargeStandard')}}</dd>
						</div>
					</dl>
					<dl class="cons rl-fl">
						<dt class="rl-text-xs">
							<i class="iconfont icon-pay"></i>
							{{$t('Foot.PaymentMethod')}}
						</dt>
						<span class="line"></span>
						<div class="cons-dd">
							<dd @click="goHelpCenter(6)">{{$t('Foot.OnlinePayment')}}</dd>
							<dd @click="goHelpCenter(7)">{{$t('Foot.BillingDetails')}}</dd>
						</div>
					</dl>
					<dl class="cons rl-fl">
						<dt class="rl-text-xs">
							<i class="iconfont icon-after-service"></i>
							{{$t('Foot.AfterSalesService')}}
						</dt>
						<span class="line"></span>
						<div class="cons-dd">
							<dd @click="goAbout(3)">{{$t('About.ContactUs')}}</dd>
							<dd @click="goAbout(2)">{{$t('QuickEntry.Question')}}</dd>
							<dd @click="goAbout(4)">{{$t('Foot.AfterSalesPolicy')}}</dd>
						</div>
					</dl>
				</div>
			</div>
		</div>
		<div class="footer-mid rl-bg-white">
			<div class="rl-tc rl-text-xxs" v-if="$i18n.locale === 'zh'">感谢您光临BAT官方商城！祝您生意兴隆！</div>
			<div class="rl-tc rl-text-xxs" v-else>Thanks for visiting bat mall and we wish you all the best!</div>
		</div>
		<div class="footer-bottom rl-tc rl-bg-white">
			<div class="rl-text-xxs">
				<a href="http://beian.miit.gov.cn/" target="_blank">{{$t('Foot.CompanyInfoHead')}}</a>
				<span>©{{$t('Foot.CompanyInfo')}}</span>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Footer",
		props: {
			cateFlag: {
				type: String,
			},
		},
		data() {
			return {};
		},
		methods: {
			goHelpCenter(type) {
				this.$parent.activeTab = type;
				this.$router.push({
					name: "HelpCenter",
					query: {
						index: type
					}
				});
			},
			goPath(name) {
				this.$router.push({
					name: name
				});
			},
			goAbout(type) {
				this.$parent.type = type;
				this.$router.push({
					name: "About",
					query: {
						type: type
					}
				});
			},
		},
		mounted() {},
	};
</script>

<style scoped="scoped" lang='less'>
	@import url("../assets/less/variable.less");

	.all {
		width: 100%;
		margin: 0 auto;
	}

	.footer {
		padding-top: 60px;
		min-width: 1260px;

		&.no-padding {
			padding-top: 0;
		}

		.footer-top {
			.service {
				height: 118px;
				padding: 30px 0;
				box-sizing: border-box;
				background: #f6f7f8;

				.service-all {
					display: flex;
					display: -webkit-flex;
					flex-direction: row;
					flex-wrap: nowrap;
					width: 1200px;
					margin: 8px auto 0 auto;
					height: 54px;
					text-align: center;

					.footer-nav {
						flex: 1;
						display: flex;
						.rl-fl-img{
							width: 30%;
							display: flex;
							align-items: center;
							justify-content: center;
							img{
								width: 55px;
								height: 55px;
							}
						}
						
						.iconfont {
							display: inline-block;
							font-size: 54px;
							color: @blue;
							vertical-align: middle;

							&.icon-authentic-guarantee {
								font-size: 64px;
							}
						}

						.text {
							width: 70%;
							text-align: left;
							display: flex;
							flex-direction: column;
							
							.name {
								display: block;
								font-size: 17px;
								color: #2a2a2a;
							}

							.tip {
								display: block;
								margin-top: 6px;
								font-size: 14px;
								color: #2a2a2a;
							}
						}
					}
				}
			}

			.content {
				width: 1200px;
				height: 100px;
				margin-top: 70px;
				display: flex;
				flex-wrap: wrap;
				justify-content: space-between;

				dl {
					dt {
						line-height: 24px;
						font-size: 14px;
						color: @lightBlack;
						vertical-align: middle;

						.iconfont {
							display: inline-block;
							margin-right: 6px;
							font-size: 14px;
							color: @lightBlack;
							vertical-align: middle;
						}
					}

					.line {
						display: inline-block;
						width: 1px;
						height: 58px;
						margin: 8px 16px 0 6px;
						background-color: @lightGray;
						vertical-align: top;
					}

					.cons-dd {
						display: inline-block;
						margin-top: 8px;
						vertical-align: top;

						dd {
							font-size: 13px;
							line-height: 20px;
							color: @lightGray;
							cursor: pointer;
						}

						dd:hover {
							color: @blue;
						}
					}
				}
			}
		}

		.footer-mid {
			height: 22px;
			margin-top: 86px;
			overflow: hidden;

			a {
				font-size: 14px;
				color: @lighterBlack;

				&:hover {
					color: @blue;
				}
			}
		}

		.footer-bottom {
			margin: 0 auto;
			padding: 12px 0 35px;

			span {
				font-size: 12px;
				color: @lighterBlack;
			}

			a:hover {
				color: @blue;
			}

			.qq-img {
				width: 79px;

				img {
					vertical-align: -8px;
				}
			}
		}
	}

	@media screen and (max-width: 1040px) {
		.footer {
			width: 1024px;
		}
	}
</style>
