import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
const store = new Vuex.Store({
	state: {
		distributorId: undefined,
		platform: undefined,
		orderSource: undefined,
		tenantNo: undefined,
		tenantUrl: undefined,
		outerId: 82,
		systemInfo: {},
		taobao: {
			onLaunchParams: {
				sellerNick: "交易开放测试账号2",
				itemId: "624268058716",
				buyNow: "true",
				quantity: "1",
				mixUserId: "AAH9iY0aANHzAbqcnP_QQPmg",
				sign: "89181D90864BDAC6ADB06389DF89534A",
				tradeToken: "DetailActivity224022019",
				"from": "native",
				isMobile: "true",
				skuId: "0"
			}
		}
	},
	mutations: {
		SET_DISTRIBUTOR_ID: (state, distributorId) => {
			state.distributorId = distributorId
		},
		SET_PLATFORM: (state, platform) => {
			state.platform = platform
		},
		SET_ORDERSOURCE: (state, orderSource) => {
			state.orderSource = orderSource
		},
		SET_TENANTNO: (state, tenantNo) => {
			state.tenantNo = tenantNo
		},
		SET_TENANTURL: (state, tenantUrl) => {
			state.tenantUrl = tenantUrl
		},
		SET_SYSTEMINFO: (state, systemInfo) => {
			state.systemInfo = systemInfo
		},
		SET_TAOBAO_PARAMS: (state, onLaunchParams) => {
			if(onLaunchParams&&onLaunchParams.sellerNick&&onLaunchParams.sellerNick.substring(0,8)=="交易开放测试账号"){
				onLaunchParams.sellerNick="bat旗舰店"
			}
			state.taobao.onLaunchParams = onLaunchParams;
		}
	},
	actions: {}
})
export default store
