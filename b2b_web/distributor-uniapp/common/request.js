
import store from '../store'
import {isH5,isMpWeixin } from '../common/common.js'

let baseUrl = '';
let basrUrlV = '';
if (isH5) {
	let href = window.location.href;
	if (href.indexOf('www.bat.com/h5') > 0) {
		// 线上
		baseUrl = 'https://api.bat.com/'
		basrUrlV = 'https://api.bat.com'
	} else {
		// 测试
		baseUrl = 'https://test.bat.com/'
		basrUrlV = 'https://test.bat.com'
	}
} else {
	const accountInfo = uni.getAccountInfoSync();
	let appId=accountInfo.miniProgram.appId;
	
	
	// 测试
	//baseUrl = 'https://test.bat.com/'
	//basrUrlV = 'https://test.bat.com'
	
	// 线上
	baseUrl = 'https://api.bat.com/'
	basrUrlV = 'https://api.bat.com'
}

export const request = (url ,  type ,date,
header = {
		'Access-Control-Allow-Origin': '*',
		'Accept': 'application/json',
		'Content-Type': 'application/json',
		'Platform': isH5?'GF60003':'GF60002',  //平台编码 
		'Version': '1.0.0',
		'Authorization': '',
		'Cache-Control': 'no-cache',
		'Pragma': 'no-cache',
		'appkey': '',
		'openId': '',
		'language-currency': ''
}) => {
	let tokenVal=uni.getStorageSync('Font-Token');
	if (tokenVal === undefined) {
		header['token'] = ''
	} else {
		header['token'] = tokenVal// 让每个请求携带自定义token 请根据实际情况自行修改-
	}
	let value = uni.getStorageSync('bLang')
	if (value) {
		header['language-currency'] = value
	}
	let basrUrlHost=uni.getStorageSync('tenantHost')
	
	if(basrUrlHost){

		basrUrlV='https://'+basrUrlHost;
		header['tenantNo'] =uni.getStorageSync('tenantNo');
	}
    return new Promise((resolve, reject) => {
        let timeS=null;
        uni.request({
            method: type,
            url: basrUrlV + url,
            data: date,
            header: header,
            dataType: 'json',
        }).then((response) => {

			
			if(response[1].data.errCode=='B_PLATFORM_GAIN_URL_NULL'||response[1].data.errCode=='B_TENANT_ERROR'||response[1].data.errCode=='B_PLATFORM_QRY_URL_ERROR'){
               const accountInfo = uni.getAccountInfoSync();
               let appId=accountInfo.miniProgram.appId;
				let params={
				  gainUrlType:6,
				  wxProgramAppId:appId
				}
				
				uni.request({
				    method: 'get',
				    url: baseUrl + 'platform/v1/web/tenant/url',
				    data: params,			   
				    dataType: 'json',
				}).then((res) => {
					uni.removeStorageSync('tenantNo');
					uni.removeStorageSync('tenantHost');
					if(res[1].data.success){
						let data=res[1].data.data;
						 uni.setStorageSync('tenantId',data.tenantId);
						 uni.setStorageSync('tenantNo',data.tenantNo);
						 uni.setStorageSync('tenantHost',data.host);
					}
				})

			}
			

			if(response[1].statusCode==401||response[1].statusCode==403){  //用户信息过期
			uni.showToast({title:'服务器异常',icon:'none',duration: 2500});
			    uni.showToast({title:response[1].data.errMessage,icon:'none',});
				uni.removeStorageSync('Font-Token');
				uni.removeStorageSync('userId');
				uni.removeStorageSync('userName');
				uni.removeStorageSync('onWayFlag');
				uni.removeStorageSync('freezeStatus');
				setTimeout(function() {
					uni.navigateTo({
						url:'/pages/login/login'
					})
				},1000)
				uni.setStorageSync('loginStatus','0');

			}else if(response[1].statusCode==500||response[1].statusCode==503){
				uni.showToast({title:'服务器异常',icon:'none',duration: 2500});
			}else if(response[1].statusCode==200){
				
				const headers = response[1].header
				uni.setStorageSync('Font-Token',headers.token);   //所有接口统一存储token
				uni.setStorageSync('loginStatus','1');  //记录登录状态  （1：登录 ，0：未登录）		
				setTimeout(function() {
					uni.hideLoading();
				}, 200);
				
				clearInterval(timeS)
				timeS=setInterval(function(){
				    uni.removeStorageSync('Font-Token');
					uni.removeStorageSync('userId');
					uni.removeStorageSync('userName');
					uni.removeStorageSync('onWayFlag');
					uni.removeStorageSync('freezeStatus');
				},24*60*60*1000)
				// 24*60*60*1000
				let [error, res] = response;
				resolve(res.data);
			}
        }).catch(error => {
			
            let [err, res] = error;
            reject(err)
        })
    });
}
