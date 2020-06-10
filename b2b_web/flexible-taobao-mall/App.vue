<script>
	import {
		mapState,
		mapGetters,
		mapActions,
		mapMutations
	} from 'vuex'
	export default {
		onLaunch: function(options) {
			//淘宝环境下 存储开发数据
			// #ifdef MP-ALIPAY
			const { query = {} } = options;
			const { params = "{}" } = query;
			const paramJson = JSON.parse(params);
			this.SET_TAOBAO_PARAMS(paramJson)
			// #endif
		},
		methods:{
			...mapMutations([
				'SET_TAOBAO_PARAMS'
			]),
		}
	}
</script>

<style lang="stylus">
	/*每个页面公共css */
		@import '@/common/styles/index.styl';
</style>
<style lang="scss">
		@import "uview-ui/index.scss";
</style>
