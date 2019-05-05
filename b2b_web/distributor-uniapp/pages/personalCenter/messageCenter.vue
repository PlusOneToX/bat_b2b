<template>
	<view class="messageCenter">
		<view class="messageCenter-center">
			<view class="messageCenter-allRead" :style="{'color':isAllRead?'#666':'themeColor'}"
				@click="msgcenterReadAllFun">全部已读</view>
			<view class="messageCenter-box">
				<view class="messageCenter-line" v-for="item in newDataList" :key="item.id">
					<view class="messageCenter-status">
						<text class="messageCenter-status-red" v-if="item.readFlag!=1"></text>
						<text v-if="item.msgType==1">订单状态通知</text>
						<text v-if="item.msgType==2">发货通知</text>
						<text v-if="item.msgType==3">订单未支付提醒</text>
						<text v-if="item.msgType==4">分销商审核通知</text>
						<text v-if="item.msgType==5">新订单通知</text>
						<text v-if="item.msgType==6">{{item.title}}</text>
					</view>
					<view class="messageCenter-info">
						<view class="messageCenter-infoList" v-for="(items, index) in item.bodyArr" :key="index">
							<text class="label">{{items.title}}</text><text class="value">{{items.value}}</text>
						</view>
					</view>
					<view class="messageCenter-detail" @click="msgcenterReadFun(item)">
						<text>{{item.createTime}}</text>
						<text :style="{'color':themeColor}">详情</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		msgcenterList,
		msgcenterNotRead,
		msgcenterRead,
		msgcenterReadAll
	} from '../../common/api.js'
	export default {
		data() {
			return {
				themeColor: '',
				tipTextShow: false,
				tipText: '',
				isAllRead: false,
				newDataList: [],
			}
		},
		onLoad() {
			this.themeColor = uni.getStorageSync('themeColor');
			this.msgcenterListFun();
		},
		methods: {
			//返回
			toback() {
				uni.navigateBack({
					delta: 1
				});
			},
			// 轻提示弹框
			tipFun(text) {
				let that = this;
				this.tipText = text;
				this.tipTextShow = true;
				setTimeout(function() {
					that.tipTextShow = false;
				}, 3000)
			},

			// 消息列表
			msgcenterListFun() {
				let that = this;
				msgcenterList().then(res => {
					console.log('消息中心', res);
					if (res.success) {
						that.newDataList = res.data.list;
						//  msgType 1:订单状态通知, 2:发货通知 3:订单未支付提醒 4:分销商审核通知,  5:新订单通知  6:自定义消息
						that.newDataList.forEach(item => {
							if (item.miniBody) {
								let obj1 = JSON.parse(item.miniBody);
								let arr1 = [];
								if (obj1.data) {
									let pageUrl = obj1.page;
									that.$set(item, 'miniLink', pageUrl);
									if (item.msgType == 1) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string1.value
										},{
											title: "订单状态",
											value: obj1.data.phrase2.value
										},{
											title: "温馨提醒",
											value: obj1.data.thing11.value
										},]
									}
									if (item.msgType == 2) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string1.value
										},{
											title: "快递公司",
											value: obj1.data.name3.value
										},{
											title: "快递单号",
											value: obj1.data.character_string4.value
										},{
											title: "发货时间",
											value: obj1.data.date10.value
										},]
									}
									if (item.msgType == 3) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string5.value
										},{
											title: "订单金额",
											value: obj1.data.amount7.value
										},{
											title: "审核结果",
											value: obj1.data.date6.value
										},{
											title: "温馨提示",
											value: obj1.data.thing4.value
										},]
									}
									if (item.msgType == 4) {
										arr1 = [{
											title: "申请时间",
											value: obj1.data.date1.value
										},{
											title: "分销商名称",
											value: obj1.data.thing5.value
										},{
											title: "温馨提示",
											value: obj1.data.thing4.value
										},]
									}
									if (item.msgType == 5) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string2.value
										},{
											title: "下单用户",
											value: obj1.data.name1.value
										},{
											title: "下单金额",
											value: obj1.data.amount3.value
										},]
									}
								} else {
									if (item.msgType == 6) {
										arr1 = [{
											title: "消息内容",
											value: item.content
										}]
									}
								}
								
								that.$set(item, 'bodyArr', arr1);
							}
						})
					}
				})
			},
			// 点击详情跳转且标志已读
			msgcenterReadFun(item) {
				let isTabLink = false;
				if (item.miniLink.indexOf("/pages/index/index") != -1 ||
					item.miniLink.indexOf("/pages/classify/classify") != -1 ||
					item.miniLink.indexOf("/pages/quickOrder/quickOrder") != -1 || 
					item.miniLink.indexOf("/pages/shoppingCart/shoppingCart") != -1 || 
					item.miniLink.indexOf("/pages/personalCenter/personalCenter") != -1
				) {
					isTabLink = true;
				}
				if (item.readFlag != 1) {
					msgcenterRead({
						id: item.id
					}).then(res => {
						if (res.success) {
							if (isTabLink) {
								uni.switchTab({
									url: item.miniLink
								})
							} else {
								uni.navigateTo({
									url: item.miniLink
								})
							}
							this.msgcenterListFun();
						}
					})
				} else {
					if (isTabLink) {
						uni.switchTab({
							url: item.miniLink
						})
					} else {
						uni.navigateTo({
							url: item.miniLink
						})
					}
				}
			},
			// 消息全部标志已读
			msgcenterReadAllFun() {
				let that = this;
				msgcenterReadAll().then(res => {
					if (res.success) {
						that.tipFun('更改成功！')
						that.isAllRead = true;
						that.msgcenterListFun();
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.messageCenter {
		position: fixed;
		width: 100%;
		height: 100%;
		.messageCenter-center {
			position: absolute;
			top: 0;
			bottom: 100rpx;
			padding: 30rpx 24rpx;
			width: 100%;
			box-sizing: border-box;
			overflow-y: scroll;

			.messageCenter-allRead {
				font-size: 28rpx;
				height: 100rpx;
				line-height: 100rpx;
				position: fixed;
				bottom: 0;
				left: 0;
				width: 100%;
				text-align: center;
				background: rgba($color: #0076A5, $alpha: 0.2);
			}

			.messageCenter-box {
				.messageCenter-line {
					background: #fff;
					border-radius: 8rpx;
					// padding:0 24rpx;
					font-size: 28rpx;

					.messageCenter-status {
						padding: 24rpx 24rpx 0 24rpx;
						display: flex;
						align-items: center;
						font-size: 30rpx;

						.messageCenter-status-red {
							width: 14rpx;
							height: 14rpx;
							background: #ED5307;
							border-radius: 100%;
							margin-right: 10rpx;
						}
					}

					.messageCenter-info {
						padding: 24rpx;
						
						.messageCenter-infoList {
							position: relative;
							padding-left: 180rpx;
							line-height: 48rpx;
							font-size: 26rpx;
						}

						.label {
							position: absolute;
							top: 0;
							left: 0;
							color: #999999;
						}
					}

					.messageCenter-detail {
						padding: 24rpx;
						border-top: 1rpx solid #f5f5f5;
						color: #999999;
						display: flex;
						align-items: center;
						justify-content: space-between;
					}

					&+.messageCenter-line {
						margin-top: 20rpx;
					}
				}
			}
		}
	}
</style>
