<template>
	<view class="model-content">
		<view class="title">
			<text class="title-l" @click="handleCancel()">取消</text>
			<text class="title-c">选择手机型号</text>
			<text class="title-r" :class="{ default: !curMobile }" @click="handleConfirm()">确定</text>
		</view>

		<view class="model-list">
			<view class="model-l">
				<view v-for="(item, index) in modelList" :key="index" class="model-item" :class="{ active: curIndex === index }"
				 @click="changeModel(index)">
					<text class="item">
						<text class="item-c">{{ item.name }}</text>
					</text>
				</view>
			</view>
			<view class="model-r">
				<view v-for="(model, pIndex) in modelList" :key="pIndex" class="model-list-wrap" :class="{ active: curIndex === pIndex }">
					<view v-for="(item, index) in model.childrenList" :key="index" class="model-item" :class="{
              default: item.underStockFlag,
              active:
                (!hasClicked && mobile === item.name) ||
                (hasClicked && curMobile === item.name),
            }"
					 @click="chooseModel(item)">
						<text class="item">
							<text class="item-c">{{ item.name }}{{ item.underStockFlag ? "（缺货）" : "" }}</text>
						</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "model",
		props: {
			mobile: {
				type: String,
				default: "",
			},
		},
		data() {
			return {
				curMobile: "",
				curIndex: 0, // 默认展示第一项
				curChooseModel: {}, // 当前选中机型
				hasClicked: false, // 是否点击
				modelList: [], // 材质列表
			};
		},
		watch: {
			mobile(val) {
				this.curMobile = val;
			},
			modelList(arr) {
				this.modelList = arr;
				if (arr && arr.length > 0) {
					if (this.mobile) {
						arr.forEach((brand, pIndex) => {
							if (brand.childrenList.length > 0) {
								brand.childrenList.forEach((model) => {
									// 判断已选型号是否匹配
									if (
										model.name.replace(/\s*/g, "").toLowerCase() ===
										this.mobile.replace(/\s*/g, "").toLowerCase()
									) {
										// 获取当前选中大类
										this.curIndex = pIndex;
										this.curMobile = model.name;
									}
								});
							}
						});
					}
				}
			},
		},
		methods: {
			// 切换型号
			changeModel(index) {
				this.curIndex = index;
			},
			// 选择机型
			chooseModel(item) {
				this.hasClicked = true;
				if (!item.underStockFlag) {
					this.curMobile = item.name;
				}
			},
			// 跳过
			handleCancel() {
				this.$emit("cancel");
			},
			// 确定
			handleConfirm() {
				this.modelList.forEach((brand, pIndex) => {
					if (brand.childrenList.length > 0) {
						brand.childrenList.forEach((model) => {
							if (
								model.name.replace(/\s*/g, "").toLowerCase() ===
								this.curMobile.replace(/\s*/g, "").toLowerCase()
							) {
								this.curChooseModel = model;
								this.$set(
									this.curChooseModel,
									"parentId",
									this.modelList[pIndex].modelId
								);
								this.$set(
									this.curChooseModel,
									"parentName",
									this.modelList[pIndex].name
								);
								this.$emit("confirm", this.curChooseModel);
							}
						});
					}
				});
			},
		},
	};
</script>

<style lang="stylus" scoped>
	$white=#FFFFFF;
	$dark=#333333;
	$light-gray=#999999;
	$main=#FFDA01;
	$color-border=rgba(231, 231, 231, 0.5);

	.model-content {
		padding: 14px 10px 0;
		font-size: 14px;
		line-height: 20px;

		.title {
			text-align: center;
			line-height: 32px;

			.title-c {
				font-size: 16px;
			}

			.title-l {
				float: left;
				padding: 0 10px;
				color: $light-gray;
			}

			.title-r {
				float: right;
				padding: 0 10px;
				font-weight: 500;
			}
		}

		.model-list {
			margin-top: 28px;
			margin-bottom: 15px;
			height: 294px;
			display: flex;

			.model-item {
				line-height: 35px;
				white-space: nowrap;

				&.default {
					color: $light-gray;
				}

				&.active {
					.item {
						position: relative;

						&::before {
							content: '';
							position: absolute;
							bottom: 0;
							left: -5px;
							width: calc(100% + 10px);
							height: 6px;
							background-color: $main;
							border-radius: 6px;
							z-index: 1;
						}
					}
				}

				.item-c {
					position: relative;
					font-style: normal;
					z-index: 2;
				}
			}

			.model-l {
				position: relative;
				overflow-y: scroll;
				flex: 3;
				text-align: center;

				&::-webkit-scrollbar {
					display: none;
				}
			}

			.model-r {
				position: relative;
				overflow-y: scroll;
				flex: 7;
				padding-left: 26px;
				border-left: 1px solid $color-border;

				.model-list-wrap {
					position: absolute;
					display: none;

					&.active {
						display: block;
					}
				}

				.model-item {
					&.active {
						.item {
							&::before {
								bottom: 2px;
								left: -3px;
								width: calc(100% + 5px);
							}
						}
					}
				}
			}
		}
	}
</style>
