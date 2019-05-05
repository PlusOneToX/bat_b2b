<template>
	<view>
		<web-view :src="src"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				baseUrl: '',
				src: '',
			}
		},
		onLoad(options) {
			let baseUrl = '';

			// #ifdef MP-WEIXIN
			const accountInfo = uni.getAccountInfoSync();
			let appId = accountInfo.miniProgram.appId;
			
			// wx1f43eac1f6d7a750  正式
			// wxd111f057f262d51b 测试
			
			if(appId == 'wx1f43eac1f6d7a750'){
				// 正式
				baseUrl = 'https://www.bat.com'
			}else{
				//测试
				baseUrl = 'https://test.bat.com/proscenium'
			}
			// #endif
			
			let params = options.enterParams ? JSON.parse(options.enterParams) : {};
			if (options.enterFlag === "custom") {
				uni.setNavigationBarTitle({
					title: params.goodsName
				});
				this.src = baseUrl +
					"/#/mobileDiy?goodsId=" +
					params.goodsId +
					"&itemId=" +
					params.itemId +
					"&itemCode=" +
					params.itemCode +
					"&userId=" +
					params.userId +
					"&tokenVal=" +
					params.tokenVal;
			}
		},
		methods: {

		}
	}
</script>

<style>

</style>
