<!--
 * @Author: yaowei
 * @Date: 2018-05-26 16:16:58
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-15 14:54:33
-->
<template>
	<div class="about-wrap">
		<!--公共头部-->
		<Header :userState="userState"></Header>
		<div class="banner-wrap">
			<img src="../../assets/images/company/about-banner.jpg" />
			<div class="list-wrap">
				<ul>
					<li :class="{'active': Number(type) === 3}" @click="goAbout(3)">{{$t('About.ContactUs')}}</li>
					<li :class="{'active': Number(type) === 1}" @click="goAbout(1)">{{$t('About.HowToBuy')}}</li>
					<li :class="{'active': Number(type) === 2}" @click="goAbout(2)">{{$t('QuickEntry.Question')}}</li>
					<li :class="{'active': Number(type) === 0}" @click="goAbout(0)">{{$t('Foot.AboutUs')}}</li>

					<li :class="{'active': Number(type) === 4}" @click="goAbout(4)">{{$t('Foot.AfterSalesPolicy')}}</li>
				</ul>
			</div>
		</div>
		<!-- 关于我们 -->
		<div class="about-content" :class="{'active': Number(type) === 0}">
			<div class="title-wrap">
				<h6 class="title">{{$t('Foot.AboutUs')}}</h6>
			</div>
			<div class="con" v-if="$i18n.locale === 'zh'">
				<p style="text-indent:2em">
					BAT是一家专注于3C智能周边产品研发、渠道运营、销售一体的科技创新型企业，注重产品的原创设计感和科技感，着力打造以创新工艺、创新材料为研发基因的高品质好物。
				</p>
				<p style="text-indent:2em">
					BAT致力于为终端商户提供高附加值、高销售力的数码产品；致力于为终端消费者提供精致、质感、有科技力的数码产品。BAT各类产品已销往美欧等数十个国家和地区，深得合作伙伴的好评。</p>
			</div>
			<div class="con" v-else>
				<p>
					BAT is a technology innovation oriented enterprise that focuses on the research and development,
					channel operation, and sales of 3C intelligent peripheral products. It emphasizes the original
					design and technology of products, and focuses on creating high-quality products with innovative
					processes and materials as the research and development genes.
					BAT is committed to providing digital products with high added value and high sales power for end
					merchants; Committed to providing end consumers with exquisite, textured, and technologically
					powerful digital products. BAT's various products have been sold to dozens of countries and regions,
					including the United States and Europe, and have received high praise from our partners.
				</p>
			</div>
		</div>
		<!-- 新手上路 -->
		<div class="about-content" :class="{'active': Number(type) === 1}">
			<div class="title-wrap">
				<h6 class="title">{{$t('About.HowToBuy')}}</h6>
			</div>
			<div class="con" v-if="$i18n.locale === 'zh'">
				<img src="../../assets/images/company/how-to-buy.png" />
			</div>
			<div class="con" v-else>
				<img src="../../assets/images/company/how-to-buy-en.png" />
			</div>
		</div>
		<!-- 常见问题 -->
		<div class="about-content" :class="{'active': Number(type) === 2}">
			<div class="title-wrap">
				<h6 class="title">{{$t('QuickEntry.Question')}}</h6>
			</div>
			<div class="con faqs-con rl-clear" v-if="$i18n.locale === 'zh'">
				<div class="con-l rl-fl">
					<p class="black">1. 为什么看不到价格和库存数量？</p>
					<p class="rl-padding-left-mid">只有注册用户且登录后才能看到产品的售价和对应的库存。</p>
					<p class="black rl-margin-top-lllg">2. 如何进行注册？</p>
					<p class="rl-padding-left-mid">联系我们，提供您的电话号码、企业名称，我们将派专人联系您并为您注册。注册成功后我们将把账号、密码发给您。</p>
					<p class="black rl-margin-top-lllg">3. 忘记账号密码怎么办？</p>
					<p class="rl-padding-left-mid">在登录页面，点击忘记密码，填写您的注册手机号码，系统将会发验证码，您输入验证码之后可以重新设置密码。</p>
				</div>
				<div class="con-r rl-fr">
					<img src="../../assets/images/company/faqs.png" />
				</div>
			</div>
			<div class="con faqs-con rl-clear" v-else>
				<div class="con-l rl-fl">

				</div>
				<div class="con-r rl-fr">
					<img src="../../assets/images/company/faqs.png" />
				</div>
			</div>
		</div>
		<!-- 联系我们 -->
		<div class="about-content" :class="{'active': Number(type) === 3}">
			<div class="title-wrap">
				<h6 class="title">{{$t('About.ContactUs')}}</h6>
			</div>
			<div class="con contact-wrap rl-clear" v-if="$i18n.locale === 'zh'">
				<div class="con-l rl-fl">
					<p class="black">手机扫描二维码关注我们，体验快捷服务！</p>
					<p>电话：</p>
				</div>
				<div class="con-r rl-fr">
					<img src="../../assets/images/login/qrcode.jpg" />
				</div>
			</div>

			<div class="con contact-wrap rl-clear" v-else>
				<div class="con-l rl-fl">
					<p>Email: </p>
					<p>Facebook：</p>
					<p>Instragram: </p>
				</div>
			</div>
		</div>
		<!-- 售后政策 -->
		<div class="about-content" :class="{'active': Number(type) === 4}">
			<div class="title-wrap">
				<h6 class="title">{{$t('Foot.AfterSalesPolicy')}}</h6>
			</div>
			<div class="con" v-if="$i18n.locale === 'zh'">
				<p>1. 在本商城购买的商品，签收7天内(以快递/物流的签收时间为准)
					商品出现自身原因导致的功能故障，经由本公司售后部门/厂商特约维修中心检测确定，可选择退货退款、换货或维修服务。收到货(以快递/物流的签收时间为准)
					8-15天内商品出现自身原因导致的功能故障，经由本公司售后部门/厂商特约维修中心检测确定，可选择退货退款、换货或维修服务。换货时，仅限调换同型号同规格的产品。</p>
				<br>
				<p>2. 在本商城购买的商品，自签收之日起12个月内(以快递/物流的签收时间为准) ，商品出现自身原因导致的功能故障，经由本公司售后部门/厂商特约维修中心检测确定，本公司承诺免费保修。</p>
				<br>
				<p>3. 在三包有效期内修理两次，仍不能正常使用的商品，凭制造商提供的维修记录和证明，本公司为消费者免费调换同型号同规格的产品或者按 第1条的规定退货。</p>
				<br>
				<p>4.
					在三包有效期内，符合换货条件的，销售商无同型号同规格产品，消费者不愿调换其他型号、规格产品而要求退货的，本公司予以退货；有同型号同规格产品，消费者不愿调换而一定要求退货的，本公司予以退货，但会对已使用过的商品按国家相关法规收取折旧费。折旧费计算自开具发票之日起退货之日止，其中应当扣除修理占用和待修的时间。
				</p>
				<br>
				<p>5.
					对于以下情况，不实行保修服务，包括：未经授权的维修、误用、碰撞、疏忽、滥用、进液、事故、改动、不正确的使用非本产品配件，或撕毁、涂改条码标贴、防伪标记导致无法识别；不能出示保修凭证，或保修凭证与实物不符；产品的自然损耗，磨损，老化等现象。
				</p>
				<br>
				<p>6. 已超过保修期；雷击、用电系统造成的损坏；因不可抗力造成的损坏；非本平台授权正规渠道销售的产品；未按说明书使用、保管、维护而造成的损坏。</p>
				<br>
				<p>7. 物流费用：按照谁发货谁付费的原则，拒绝接受到付，客户邮寄维修时的运费由客户承担，商品维修后寄出的物流费用由维修方承担。</p>
			</div>
			<div class="con" v-else>
				<p>I. ......</p>
			</div>
		</div>

		<!-- 公共底部 -->
		<Footer></Footer>
	</div>
</template>

<script>
	import Header from "@/components/Header.vue";
	import Footer from "@/components/Footer.vue";
	export default {
		name: "About",
		components: {
			Header,
			Footer
		},
		data() {
			return {
				userState: 2,
				type: this.$route.query.type,
			};
		},
		methods: {
			goAbout(type) {
				this.type = type;
				this.$router.push({
					name: "About",
					query: {
						type: type
					}
				});
			},
		},
	};
</script>

<style lang="less" scoped>
	@import url("../../assets/less/variable.less");

	.about-wrap {
		max-width: 1920px;
		margin: 0 auto;

		.banner-wrap {
			position: relative;
			min-width: 1260px;
			width: 100%;
			height: 320px;
			text-align: center;
			overflow: hidden;

			.list-wrap {
				position: absolute;
				right: 0;
				bottom: 0;
				left: 0;
				height: 53px;
				line-height: 53px;
				font-size: 16px;
				color: @white;
				background-color: @bat_vi;

				ul {
					display: inline-block;
					margin: 0 auto;
				}

				li {
					float: left;
					position: relative;
					padding: 0 25px;
					cursor: pointer;

					&+li::after {
						content: "";
						position: absolute;
						top: 50%;
						left: 0;
						display: block;
						margin-top: -8px;
						height: 16px;
						border-left: 1px solid @white;
					}

					&.active {
						color: @bat_logo;
						background-color: @white;

						&+li::after {
							border-left: none;
						}
					}
				}
			}
		}

		.about-content {
			padding-top: 30px;
			width: @center;
			margin: 0 auto;
			display: none;

			&.active {
				display: block;
			}

			.title-wrap {
				position: relative;
				height: 63px;
				text-align: center;
				-moz-user-select: none;
				/*火狐*/
				-webkit-user-select: none;
				/*webkit浏览器*/
				-ms-user-select: none;
				/*IE10*/
				-khtml-user-select: none;
				/*早期浏览器*/
				user-select: none;

				p {
					font-size: 37px;
					color: rgba(191, 191, 191, 0.29);
					line-height: 52px;
					letter-spacing: 7px;
				}

				.title {
					position: absolute;
					left: 50%;
					bottom: 0;
					font-size: 24px;
					color: @bat_vi;
					line-height: 48px;
					transform: translateX(-50%);

					&::after {
						content: "";
						position: absolute;
						left: 50%;
						bottom: 0;
						margin-left: -7px;
						width: 14px;
						height: 6px;
						background-color: @bat_vi;
					}
				}
			}

			.con {
				margin-top: 20px;
				padding: 30px 126px;

				h6 {
					font-size: 16px;
					color: @black;

					&+p {
						margin-top: 20px;
					}
				}

				p {
					font-size: 14px;
					color: @darkGray;
					line-height: 22px;

					&+h6 {
						margin-top: 30px;
					}

					&.black {
						color: @black;
					}
				}

				table {
					margin-top: 20px;
					margin-bottom: 15px;
					margin-left: 2em;
					text-align: center;
					border-top: 1px solid @lighterGray;
					border-right: 1px solid @lighterGray;

					td {
						padding: 10px 40px;
						border-left: 1px solid @lighterGray;
						border-bottom: 1px solid @lighterGray;
					}
				}

				.text-indent {
					text-indent: 30px;
				}

				&.history {
					background: linear-gradient(232deg, @darkBlue 0%, @blue 100%);

					p {
						color: @white;
					}
				}
			}

			.faqs-con {
				margin-top: 20px;
				padding: 30px 0;

				.con-l {
					width: 525px;
				}

				.con-r {
					width: 545px;

					img {
						width: 100%;
					}
				}
			}

			.contact-wrap {
				width: 550px;
				margin: 20px auto 0;

				.black {
					margin-top: 45px;
					margin-bottom: 25px;
				}

				.con-r {
					img {
						width: 204px;
					}
				}
			}
		}
	}
</style>
