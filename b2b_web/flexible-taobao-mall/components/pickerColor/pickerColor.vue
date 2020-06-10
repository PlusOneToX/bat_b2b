<template>
	<view class="pop">
		<view class="list flex_col" v-for="(item,index) in colorArr" :key="index">
			<view v-for="(v,i) in item" :key="i" :style="{'backgroundColor':v}" :data-color="v" :data-index="index" :data-i="i"
			 :class="{'active':(index==pickerArr[0] && i==pickerArr[1])}" @tap="picker"></view>
		</view>
		<view :style="{'height':(bottom+'px')}"></view>
	</view>
</template>

<script>
	export default {
		name: 'picker-color',
		props: {
			defaultColor: {
				type: String,
				default: '#98C963',
			},
			bottom: {
				type: Number,
				default: 0,
			}
		},
		data() {
			return {
				colorArr: [
					['#FFFFFF', '#828282', '#000000', '#F05D61', '#F584A6', '#E3297E', '#EC6A39'],
					['#F5992C', '#FBC900', '#E0AA3B', '#98C963', '#6BA2DA', '#95A5D7', '#7463A1']
				],
				pickerColor: '',
				pickerArr: [1, 3]
			};
		},
		mounted() {
		},
		methods: {
			picker(e) {
				let data = e.currentTarget.dataset;
				this.pickerColor = data.color;
				this.pickerArr = [data.index, data.i];
				this.setColor();
			},
			setColor() {
				this.$emit("callback", this.pickerColor);
			}
		},

	}
</script>

<style scoped lang="stylus">
	.pop {
		position: relative;
		width: 100%;
		background-color: #FFFFFF;
		padding:  0 20px;
	}

	.flex_col {
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		justify-content: flex-start;
		align-items: center;
		align-content: center;
	}

	.list {
		justify-content: space-between;

		&>view {
			width: 52upx;
			height: 52upx;
			margin-bottom: 18px;
			box-sizing: border-box;
			border-radius: 50%;
			box-shadow: 0upx 0upx 12upx 0upx rgba(223, 223, 223, 0.5);
		}

		.active {
			position: relative;
			
			&::before {
				position: absolute;
				top: 4upx;
				left: 4upx;
				content: "";
				width: 36upx;
				height: 36upx;
				border: 4upx solid #FFFFFF;
				box-shadow: inset 0upx 0upx 12upx 0upx rgba(223, 223, 223, 0.5);
				border-radius: 50%;
			}
		}
	}
</style>
