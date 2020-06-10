<template>
	<div class="protocol">
		<div class="container">
			此处放置用户协议内容，请自行修改
		</div>
		<div class="btn-box">
			<div class="btn btn-normal-c" v-show="timeCount && show">
				倒计时{{ count }}秒
			</div>
			<div class="btn btn-normal" v-show="!timeCount || !show" @click="handleClose">
				同意
			</div>
		</div>
	</div>
</template>

<script type="text/ecmascript-6">
	export default {
		name: "protocol",
		props: {
			timeCount: {
				type: Number,
				default: 5,
			},
		},
		data() {
			return {
				title: "用户协议",
				count: 5,
				timer: null,
				show: true,
				confirm: 0,
				diyTitle: "BAT小程序", // 小程序名
				wechatName: "BAT公众号", // 微信公众号
				companyName: "", // 公司名
				creditCode: "", // 信用代码
			};
		},
		created() {
			let baseUrl = window.location.href;
			if (baseUrl.indexOf("diy.bat.com") > 0) {
				this.diyTitle = "BAT小程序";
				this.wechatName = "BAT公众号";
				this.companyName = "";
				this.creditCode = "";
			}
		},
		watch: {
			timeCount(val) {
				this.show = true;
				this.count = val;
				// 倒计时
				if (!this.timer) {
					this.timer = setInterval(() => {
						if (this.count > 0 && this.count <= val) {
							this.count--;
						} else {
							clearInterval(this.timer);
							this.timer = null;
							this.show = false;
						}
					}, 1000);
				}
			},
		},
		methods: {
			handleClose() {
				this.$emit("agree");
			},
		},
		components: {},
	};
</script>

<style scoped lang="stylus">
	@import '~common/styles/variable';
	@import '~common/styles/mixin';

	.protocol {
		position: fixed;
		width: 100%;
		top: 0;
		bottom: 0;
		background-color: $color-bg-white;

		.container {
			height: 100%;
			padding: 40px $spacing-base 120px;
			overflow-y: scroll;
			-webkit-overflow-scrolling: touch;
			color: $color-font-base;
			font-size: $font-base;
			text-align: justify;
			line-height: 1.5;

			&::-webkit-scrollbar {
				display: none;
			}

			h1 {
				font-size: $font-lg;
				font-weight: bold;
			}

			h3 {
				font-size: $font-base;
				font-weight: bold;
				line-height: 1.8;
			}

			div {
				font-size: $font-base;
				margin-bottom: 10px;

				.bold {
					font-weight: bold;
				}

				.no {
					display: inline-block;
				}
			}

			.text-indent-double {
				text-indent: 2em;
			}

			.margin-l-double {
				margin-left: 10px;
			}

			.margin-b-double {
				margin-bottom: 30px;
			}

			.margin-b-m {
				margin-bottom: 15px;
			}

			.text-align-r {
				text-align: right;
			}

			.text-align-c {
				text-align: center;
			}

			.bold {
				font-weight: bold;
			}
		}

		.btn-box {
			position: fixed;
			left: 0;
			right: 0;
			bottom: 0;
			text-align: center;
			padding: $spacing-sm 0;
			background-color: $color-bg-white;

			.btn-normal {
				btn-normal();
			}

			.btn-normal-c {
				btn-normal-c();
			}
		}
	}
</style>
