<template>
	<div class="protocol">
		<v-header :back="true" :title=title @back="toback"></v-header>
		<div class="container">
			此处放置用户协议内容，请自行修改
		</div>
		<div class="btn-box">
			<!--      <div class="btn btn-normal-c" v-show="show">倒计时{{count}}秒</div>-->
			<!--      <div class="btn btn-normal" v-show="!show" @click="handleSubmit">同意</div>-->
		</div>
	</div>
</template>

<script type="text/ecmascript-6">
	import VHeader from 'components/v-header/v-header'
	export default {
		name: 'protocol',
		data() {
			return {
				title: '用户协议',
				userInfo: {},
				count: 5,
				timer: null,
				show: true,
				confirm: 0,
				location: "",
			}
		},
		mounted() {
			// if (!this.timer) {
			//   this.timer = setInterval(() => {
			//     if (this.count > 0 && this.count <= 5) {
			//       this.count--
			//     } else {
			//       clearInterval(this.timer)
			//       this.timer = null
			//       this.show = false
			//     }
			//   }, 1000)
			// }
			this.location = window.location.host;
		},
		methods: {
			// 同意协议
			handleSubmit() {
				this.$emit('agree')
				// let phone = localStorage.getItem('phone')
				// let userNo = localStorage.getItem('userNo')
				// // 获取用户信息
				// this.$store.dispatch('GetInfo', {phone: phone, no: userNo}).then((res) => {
				//   if (res.status === 200) {
				//     this.userInfo = res.data
				//     this.userInfo.confirm = 1
				//     // 同意,修改状态
				//     this.confirm = 1
				//     this.$api.put(this, api.updateInfo, this.userInfo).then(res => {
				//       if (res.status === 200) {
				//         localStorage.removeItem('confirm')
				//         this.$router.push('/index')
				//       } else {
				//         this.$toast(res.error)
				//       }
				//     })
				//   } else if (res.status === 500) {
				//     this.$toast.fail(res.error)
				//   }
				// })
			},
			toback() {
				this.$emit('close')
			}
		},
		components: {
			VHeader
		}
	}
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
	@import '~common/styles/variable'
	@import "~common/styles/mixin"
	.protocol position: fixed width: 100% top: 45px bottom: 0 background-color:$color-background-white z-index:99 .container height:100% padding: 40px 40px 120px box-sizing:border-box overflow-y:scroll -webkit-overflow-scrolling:touch color:$color-text font-size:$font-size-medium text-align:justify line-height:1.5 &::-webkit-scrollbar display:none h1 font-size:$font-size-large-x font-weight:bold h3 font-size:$font-size-medium font-weight:bold line-height:1.8 div font-size:$font-size-medium margin-bottom:10px .bold font-weight:bold .no display: inline-block .text-indent-double text-indent:20px .margin-l-double margin-left:10px .margin-b-double margin-bottom:30px .margin-b-m margin-bottom:15px .text-align-r text-align:right .text-align-c text-align:center .bold font-weight:bold .btn-box position:fixed left:0 right:0 bottom:30px text-align:center .btn-normal btn-normal() .btn-normal-c btn-normal-c()
</style>
