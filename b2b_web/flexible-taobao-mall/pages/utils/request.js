/*
 * @Author: yaowei
 * @Date: 2019-09-10 13:45:15
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-30 10:52:04
 */
import Vue from 'vue'
//阿里云小程序 云开发sdk
import cloud from '@tbmp/mp-cloud-sdk';
cloud.init({
	env: 'online' // 正式
	// env: 'test' // 测试
});
Vue.prototype.$cloud = cloud
import store from '../../store'
export default function (config) {
	let baseUrl = "";
	if (config.url.indexOf("platform/v1/web/tenant/url") < 0) {
		baseUrl = store.state.tenantUrl;
	} else {
		baseUrl = "https://api.bat.com";
	}
	config.baseUrl = baseUrl;
	config.distributorId = store.state.distributorId
	config.platform = store.state.platform
	config.orderSource = store.state.orderSource
	config.tenantNo = store.state.tenantNo
	return new Promise((resolve, reject) => {
		//淘宝平台真机
		if (store.state.systemInfo.app === "TB" || store.state.systemInfo.app === "TM") {
			config.url = "/" + config.url
			
			cloud.function.invoke('request', config).then(res => {
				if (typeof (res) === "string") {
					res = JSON.parse(res)
				}
				if (res.error) {
					uni.showToast({
						title: res.data.error,
						icon: 'none',
						duration: 2000
					})
				}
				resolve(res)
			})
		} else {
			uni.request({
				url: baseUrl + "/" + config.url,
				data: config.data ? config.data : {},
				method: config.method ? config.method : 'GET',
				header: {
					'Access-Control-Allow-Origin': '*',
					'Accept': 'application/json',
					'Content-Type': 'application/json',
					'Version': '1.0.0',
					'Authorization': '0',
					'Cache-Control': 'no-cache',
					'Pragma': 'no-cache',
					'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
					'openId': '0',
					'distributorId': config.distributorId,
					'platform': config.platform,
					'orderSource': config.orderSource,
					'tenantNo': config.tenantNo,
				},
				success: (res) => {
					if (res.data) {
						if (res.data.error) {
							uni.showToast({
								title: res.data.error,
								icon: 'none',
								duration: 2000
							})
						}
						resolve(res.data)
					}
					resolve(res)
				},
				fail: (err) => {
					console.error(err)
					reject(err)
				}
			})
		}
	})
}