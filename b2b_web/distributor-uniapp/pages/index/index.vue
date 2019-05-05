<template>
	<view class="index">
		<view class="top-moudle index-logo" id="logoWrap">
			<view class="status_bar"></view>
			<view class="logo-image">
				<image src="../../static/imgs/logo.png"></image>  
			</view>
		</view>
		<!--moduleType 1：轮播图  2：图片模块  3：商品推广模块  4：商品列表模块 -->
		<!-- 搜索 --> 
		<view class="index-box">
			<!-- 断网操作 "-->
			<view class="index-errNetwork" v-if="noneWork">
				<image src="../../static/imgs/err_icon.png"></image>
				<text>网络连接不可用</text>
			</view>
			<view v-for="(item,index1) in mobileList" :key="item.id">
				<!-- 轮播 -->
				<view class="index-swiper" v-if="item.moduleType==1">
					<swiper class="swiper" :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval"
						:previous-margin="previousMargin" :next-margin="nextMargin"
						:indicator-active-color="dotActiveColor" :indicator-color="dotColor" @change="swiperChange"
						circular>
						<swiper-item v-for="(items,index) in item.list" :key="items.id">
							<image :src="items.imageUrl" class="swiperImg" :class="current==index?'swiperImg-active':''"
								@click="toFumpParamsFun(items.jumpParams,items.visible)"></image>
						</swiper-item>
					</swiper>
				</view>
				<!-- 类 -->
				<view class="index-type" v-if="item.moduleType==2&&item.list.length==6">
					<view class="index-type-line" v-for="items in item.list" :key="items.id">
						<view class="index-type-img" @click="toFumpParamsFun(items.jumpParams)">
							<image :src="items.imageUrl" mode="scaleToFill"></image>
						</view>
					</view>
					<view style="clear: both;"></view>
				</view>
				<!-- 图片模块 1-4 -->
				<!-- :style="{width:'calc(100% / ' + indexImageMobleViews[index1][index2] +')'}" -->
				<view v-if="item.moduleType==2&&item.list.length>0&&item.list.length<6" class="index-image-moble">
					<view v-for="(items,index2) in item.list" :key="items.id"
						:style="{width: indexImageMobleViews[index1][index2]+'%'}">
						<image :src="items.imageUrl" mode="widthFix" @click="toFumpParamsFun(items.jumpParams)"
							v-if="item.list.length==4" style="width:100.8%;height:100.8%"></image>
						<image :src="items.imageUrl" mode="widthFix" @click="toFumpParamsFun(items.jumpParams)" v-else>
						</image>
						<!-- <view v-if="index==0" class="index-news" @click="toNewPage">
              <text v-if="isNoRead"></text>		  
            </view> -->
					</view>
					<image src="../../static/imgs/newsIcon.png"></image>
				</view>

				<view style="clear: both;"></view>

				<!-- 0： 无效 ，1： 3列商品 ，2： 4列商品 ，3： 多列商品 -->
				<!-- 三列商品图 -->
				<view class="index-goods1" v-if="item.moduleType==3&&item.list[0].styleType==1">
					<image :src="item.list[0].imageUrl" mode="widthFix" class="index-goods1-image"></image>
					<view class="index-goods1-box">
						<view class="index-goods1-line" v-for="items in item.threeGoosList" :key="items.id"
							@click="toGoodsDetails(items)">
							<image :src="items.imageUrl1" mode="scaleToFill"></image>
							<view class="index-goods1-btm">
								<view>{{items.goodsName}}</view>
								<view v-if="userId==''" class="index-no-login">登录后查看</view>
								<view v-else class="index-login"
									:class="items.minPrice?'':(items.maxPrice?'':'noPrice')">
									<text>￥</text>{{items.minPrice?items.minPrice:(items.maxPrice?items.maxPrice:'暂未定价')}}
								</view>
							</view>
						</view>
					</view>
				</view>

				<!-- 四列商品 -->
				<view class="index-goodGoods" v-if="item.moduleType==3&&item.list[0].styleType==2">
					<image :src="item.list[0].imageUrl" mode="widthFix" class="index-goods1-image"></image>
					<view class="index-goodGoods-boxV">
						<view class="index-goodGoods-line">
							<view class="index-goodGoods-box">
								<template v-for="(item,index) in fourGoosList">
									<view class="index-goodGoods-boxLine" :key="item.id" v-if="index<2">
										<image :src="item.imageUrl1"></image>
										<text>{{item.goodsName}}</text>
									</view>
								</template>
							</view>
						</view>
						<view class="index-goodGoods-line">
							<view class="index-goodGoods-box">
								<template v-for="(item,index) in fourGoosList">
									<view class="index-goodGoods-boxLine" :key="item.id" v-if="index>1">
										<image :src="item.imageUrl1"></image>
										<text>{{item.goodsName}}</text>
									</view>
								</template>
							</view>
						</view>
					</view>
				</view>

				<!-- 你可能感兴趣的产品 -->
				<view class="index-interest" v-if="item.moduleType==3&&item.list[0].styleType==3">
					<scroll-view class="scroll" scroll-x="true">
						<view class="index-interest-box" :style="{width: interestWidth*interestedList.length+30+'rpx'}">
							<view class="index-interest-boxLine" v-for="item in interestedList" :key="item.id"
								@click="toGoodsDetails(item)">
								<image :src="item.imageUrl1"></image>
								<view>{{item.goodsName}}</view>
								<view class="price-view-right" v-if="userId==''"><text>登录后查看</text></view>
								<view class="price-view-right" v-else><text>￥</text>{{item.minPrice}}</view>
							</view>
						</view>
					</scroll-view>
				</view>

				<!-- 2019.11.3更多商品 -->
				<view v-if="item.moduleType==4" class="index-moreGoods2" id="fixedWrap" :class="isFixed ? 'fixed' : ''">
					<!-- tab选项 -->
					<view class="index-moreGoods2-tab" v-if="indexTypeList.length>1">
						<scroll-view class="scroll" scroll-x="true">
							<view :style="{'width':(190*indexTypeList.length)+'rpx'} " class="index-moreGoods2-view">
								<view v-for="(typItem, index) in indexTypeList" :key="index"
									@click="selectTypeGetGood(typItem)">
									<text class="index-moreGoods2-item"
										:class="(typItemId==typItem.id)? 'index-moreGoods2-hover':''">{{typItem.title}}</text>
									<text class="index-moreGoods2-hoverLine" :style="{'background':themeColor}"
										v-if="typItemId==typItem.id"></text>
								</view>
							</view>
						</scroll-view>
						<view class="index-moreGoods2-tabMore" @click="toClassifyFun" v-if="indexTypeList.length>0">
							<image src="../../static/imgs/icon_moreBase2.png"></image>
						</view>
					</view>


					<!-- 列表 -->
					<view class="index-moreGoods2-list" v-if="goodsList.length>0">
						<view class="index-moreGoods2-listContent">
							<view class="index-moreGoods2-listItem" v-for="(item, index) of goodsList" :key="index"
								:style="{'height':(userId!='')?'500rpx':'478rpx'}" @click="toGoodsDetails(item)">
								<image :src="item.imageUrl1"></image>
								<view class="index-moreGoods2-tip" :style="{'background':themeColor}"
									v-if="item.promotionFlag==1">热销</view>
								<view class="index-moreGoods2-tip" v-if="item.newFlag==1">新品</view>
								<view class="index-moreGoods2-listName">{{item.goodsName}}</view>
								<!-- 活动 -->
								<view class="index-moreGoods2-promition" v-if="userId!=''">
									<text v-for="(tagItem, index) in item.tags" :key="index">{{tagItem.name}}</text>
								</view>
								<view v-if="userId!=''"
									:class="item.minPrice?'index-moreGoods2-price':'index-moreGoods2-priceNo'">
									{{item.minPrice?('￥'+item.minPrice):'暂未定价'}}</view>
								<view v-else class="index-moreGoods2-priceNo">登录后查看</view>
							</view>

							<view style="clear: both;"></view>
						</view>

						<view class="index-moreGoods2-btmTip" v-if="page==totalpage">您已翻到底啦！</view>
					</view>
					<!-- 无数据 -->
					<view class="index-nodata" v-if="isNoGoodList">
						<image src="../../static/imgs/no-data.png"></image>
						<view>暂无数据</view>
					</view>
				</view>
			</view>
		</view>

		<!--加载动画-->
		<loading v-if="loadingFlag" :message="message"></loading>

		<!-- 到顶部的虚浮框-->
		<view class="index-float-top" v-if="topState" @click="toTopFun">
			<image src="../../static/imgs/icon_top.png"></image>
		</view>

		<!-- 虚浮登录 -->
		<!--    <view class="index-float-btm" v-if="userId==''">  
      <view class="index-float-btm-item1">
        <image src="../../static/imgs/index_login.png" mode="aspectFit"></image>
      </view>
      <view class="index-float-btm-item2" >欢迎来到BAT商城</view>
      <view class="index-float-btm-item3">
        <view class="index-float-btm-item3-btn" @click="toLoginFun">立即登录</view> 
      </view>
    </view> -->

		<!-- 提示框 -->
		<view class="tipText" v-show="tipTextShow">{{tipText}}</view>

		<!-- 产品推广 -->
		<productPromotion v-if="showProPromotion"></productPromotion>
	</view>
</template>

<script>
	import waterfallsFlow from "@/components/maramlee-waterfalls-flow/maramlee-waterfalls-flow.vue";
	import {
		userMoblieList,
		priceGoodsList,
		mobileGoodsList,
		loginByOpenId,
		userInfo,
		mobileGoodsListP,
		goodsLabels,
		msgcenterNotRead,
	} from "../../common/api.js";
	import loading from "../../components/myComponents/loading.vue";
	import productPromotion from "../../components/myComponents/productPromotion.vue";
	export default {
		components: {
			waterfallsFlow,
			loading,
			productPromotion,
		},
		data() {
			return {
				message: "加载中",
				themeColor: "",
				share: {
					title: "BAT官方商城",
					path: "/pages/index/index",
					imageUrl: "",
					desc: "BAT官方商城",
					content: "终端商户微信小程序自助下单、分销商城",
				},
				page: 1,
				totalpage: 0,
				size: 4,
				mobileList: [], //全部模块列表
				searchImg: [],
				threeGoosList: [], //三图商品列表
				fourGoosList: [], //三图商品列表
				interestedList: [], //感兴趣的商品
				goodsList: [], //商品列表模块
				list: [],
				indicatorDots: true,
				autoplay: true,
				interval: 3000,
				previousMargin: "30rpx",
				nextMargin: "30rpx",
				current: 0,
				dotColor: "#ffffff",
				dotActiveColor: "",
				interestWidth: 160,
				userId: "", //分销商id
				moreId: "", //更多商品的模块id
				tipTextShow: false,
				tipText: "",
				topState: false, //回滚顶部
				noneWork: false, //监听网络
				loadingFlag: true, //数据加载
				indexTypeList: [], //首页分类
				typItemId: "",
				typItem: {},
				moduleType: "",
				isNoGoodList: false,
				isNoRead: false, //是否有未读
				showProPromotion: false,
				tabTopVal: 0, // tab top 值
				topLogoHeight: 0, // 顶部 logo 高度
				tabInitTopVal: true, // 初始化tab top值，默认true（不吸顶）
				indexImageMobleViews: [] // 图片样式数组（样式大小）
			};
		},

		onReachBottom() {
			this.getList();
		},

		onShow(option) {

			// 监听网络连接--start
			uni.onNetworkStatusChange(function(res) {
				console.log(
					"网络类型:" + res.networkType + ",网络连接:" + res.isConnected
				);
				if (!res.isConnected) {
					this.noneWork = true;
				}
			});
			// 监听网络连接--end

			let userId = uni.getStorageSync("userId");
			if (userId && userId != "" && userId != "undefined") {
				this.userId = userId;
				// this.msgcenterNotReadFun();

				this.showProPromotion = true;
			}

			let indexGetPrice = uni.getStorageSync("indexGetPrice");
			if (indexGetPrice != 1) {
				// let openId = uni.getStorageSync("openId");
				// if (openId != "" && userId == "") {
				//   this.openIdLoginFun(openId);
				// }
				this.getMobileData();
			}
		},
		onPageScroll(e) {
			this.tabTopVal = e.scrollTop;
			const query = uni.createSelectorQuery();
			// 获取初始化tab top值
			query
				.select("#fixedWrap")
				.boundingClientRect()
				.exec((res) => {
					let top = res[0].top;
					this.tabInitTopVal = top + this.tabTopVal;
				});
			// 获取顶部logo高
			query
				.select("#logoWrap")
				.boundingClientRect()
				.exec((res) => {
					this.topLogoHeight = res[1].height;
				});

			//根据距离顶部距离是否显示回到顶部按钮
			if (e.scrollTop > 500) {
				//当距离大于500时显示回到顶部按钮
				this.topState = true;
			} else {
				//当距离小于500时显示回到顶部按钮
				this.topState = false;
			}
		},
		onLoad() {
			this.dotActiveColor = uni.getStorageSync("themeColor");

			// let openId = uni.getStorageSync("openId");
			// let userId = uni.getStorageSync("userId");
			// if (openId != "" && userId == "") {
			//   this.openIdLoginFun(openId);
			// }

			this.getMobileData();
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.page = 1; // 重置分类商品分页
			this.getMobileData();
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 500);
		},
		methods: {
			// 轻提示弹框
			tipFun(text) {
				let that = this;
				this.tipText = text;
				this.tipTextShow = true;
				setTimeout(function() {
					that.tipTextShow = false;
				}, 3000);
			},
			// 查询当前未读的消息的数量
			msgcenterNotReadFun() {
				msgcenterNotRead().then((res) => {
					this.isNoRead = res.data > 0 ? true : false;
				});
			},
			// 跳转到消息页
			toNewPage() {
				uni.navigateTo({
					url: "/pages/personalCenter/messageCenter",
				});
			},
			//回滚顶部
			toTopFun() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300,
				});
			},
			// 跳转到分类
			toClassifyFun() {
				uni.switchTab({
					url: "/pages/classify/classify",
				});
			},
			// 跳转到注册
			toRegisterFun() {
				uni.navigateTo({
					url: "/pages/login/register",
				});
			},
			// 跳转到登录
			toLoginFun() {
				uni.navigateTo({
					url: "/pages/login/login",
				});
			},
			// openid登录
			openIdLoginFun(openId) {
				let params = {
					openId: openId,
					platform: "GF60002",
				};
				loginByOpenId(params).then((res) => {
					let data = res.data;
					if (res.success) {
						uni.setStorageSync("userId", res.data.id);
						uni.setStorageSync("userName", res.data.userName); //分销商名称
						uni.setStorageSync("autoDelivery", res.data.autoDelivery); // 是否直发客户：1 是，0 否
						uni.setStorageSync("companyName", res.data.companyName); //分销商公司名称
						uni.setStorageSync("onWayFlag", res.data.onWayFlag); //是否支持在途库存 1是 0否 默认是1
						uni.setStorageSync("capitalStatus", res.data
						.applyStatus); //'资质状态 0,未提交 1,申请中,2,合作中 3,申请失败',
						uni.setStorageSync("freezeStatus", res.data.freezeStatus); //冻结状态 2,冻结  1,未冻结
						uni.setStorageSync("distributionFlag", res.data.distributionFlag); //是否开启分销模式 0 不开启, 1 开启
						// uni.setStorageSync('openId', openId);
						this.userInfoFun(res.data.id);
						this.getMobileData();
					} else {
						this.getMobileData();
						this.tipFun("--", res.errMessage);
					}
				});
			},
			// 查看分销商个人信息
			userInfoFun(id) {
				userInfo({
					id: id
				}).then((res) => {
					if (res.success) {
						if (res.data.phone) {
							// this.$store.commit('setPhone',res.data.phone)  //登录的手机号
							uni.setStorageSync("userPhone", res.data.phone);
						}
					}
				});
			},

			// 轮播图change
			swiperChange(e) {
				this.current = e.detail.current;
			},

			// 跳转页
			toFumpParamsFun(url, visible) {
				console.log("点击轮播图 = " + "url:" + url + " - visible:" + visible);
				if (visible != undefined) {
					if (visible == 1) {
						uni.navigateTo({
							url: url,
						});
					}
				} else {
					uni.navigateTo({
						url: url,
					});
				}
			},

			// 加载分页
			getList() {
				if (this.page < this.totalpage) {
					this.page += 1;
					this.getSectionGoodsListPost(
						this.typItem.id,
						this.typItem.appointType,
						this.typItem.pointIds,
						4
					);
				}
			},

			// 进入搜索
			toSearch() {
				uni.navigateTo({
					url: "search",
				});
			},

			// 获取各个模块数据
			getMobileData(type) {
				this.goodsList = [];
				this.interestedList = [];
				this.fourGoosList = [];
				this.threeGoosList = [];
				this.themeColor = uni.getStorageSync("themeColor");
				userMoblieList({
					page: 1,
					size: 500,
					status: 1
				}).then((res) => {
					let list = res.data.list;
					console.log("查询首页数据");
					console.log(list);
					// 设置图片样式
					list.forEach((item, index1) => {
						if (item.moduleType === 2 && item.list.length > 0 && item.list.length < 6) {
							this.indexImageMobleViews[index1] = []
							item.list.forEach((img, index2) => {
								this.indexImageMobleViews[index1][index2] = img.widthPercentage
							})
						}
					})
					this.mobileList = list;
					for (let i = 0; i < list.length; i++) {
						if (list[i].moduleType == 3 && list[i].list[0].styleType == 1) {
							this.getSectionGoodsList(3, list[i].id, 3, list[i].moduleType);
						}
						if (list[i].moduleType == 3 && list[i].list[0].styleType == 2) {
							this.getSectionGoodsList(4, list[i].id, 4, list[i].moduleType);
						}

						if (list[i].moduleType == 3 && list[i].list[0].styleType == 3) {
							this.getSectionGoodsList(100, list[i].id, 5, list[i].moduleType);
						}

						if (list[i].moduleType == 4) {
							this.moreId = list[i].id;
							let id = "";
							if (list[i].id) {
								// id=list[i].id;
								id = "";
								if (
									list[i].mobileChildDTOS &&
									list[i].mobileChildDTOS[0] &&
									list[i].mobileChildDTOS[0].appointType == 3
								) {
									id = list[i].mobileChildDTOS[0].id;
								}
							}
							this.moduleType = list[i].moduleType;
							if (list[i].mobileChildDTOS && list[i].mobileChildDTOS.length > 0) {
								this.indexTypeList = list[i].mobileChildDTOS;
								this.typItemId = list[i].mobileChildDTOS[0].id;
								this.typItem = list[i].mobileChildDTOS[0];
								this.getSectionGoodsListPost(
									id,
									list[i].mobileChildDTOS[0].appointType,
									list[i].mobileChildDTOS[0].pointIds,
									list[i].moduleType
								);
							}

						}
					}
					this.loadingFlag = false;
				});
			},
			// 点击分类获取商品
			selectTypeGetGood(item) {
				// 固定tab，数据从头开始显示
				if (this.isFixed) {
					uni.pageScrollTo({
						scrollTop: this.tabInitTopVal - this.topLogoHeight,
						duration: 0,
					});
				}

				this.page = 1;
				this.typItemId = item.id;
				this.typItem = item;
				this.goodsList = [];

				this.getSectionGoodsListPost(
					item.parentId,
					item.appointType,
					item.pointIds,
					this.moduleType
				);
			},

			// 首页底部商品post获取接口
			// 根据板块id获取商品
			getSectionGoodsListPost(id, appointType, pointIds, moduleType) {
				let that = this;
				let parmas = {
					page: that.page,
					size: 4,
					mobileId: id,
					appointType: appointType, //指定类型 1.指定分类 2.指定品牌 3.指定商品 4.全部商品
					moduleType: moduleType,
					pointIds: pointIds.length > 0 ? pointIds : "", //指定ids(分类id或者品牌ids)
				};
				mobileGoodsListP(parmas).then((res) => {
					if (res.success && res.data.list) {
						let list = res.data.list;
						let goodsIdList = [];
						if (list.length == 0) {
							that.isNoGoodList = true;
						} else {
							that.isNoGoodList = false;
						}
						if (list.length > 0) {
							for (let i = 0; i < list.length; i++) {
								goodsIdList.push(list[i].id);
							}

							that.totalpage = res.data.pages;
							// let goodList2=JSON.parse(JSON.stringify(this.goodsList))
							that.goodsList = [...that.goodsList, ...list];

							if (that.userId != "") {
								that.getPriceData(goodsIdList, "more");
								that.goodsLabelsFun(goodsIdList);
							}
						}
					} else {
						that.isNoGoodList = true;
					}
				});
			},

			// 获取商品标签
			goodsLabelsFun(ids) {
				let that = this;
				goodsLabels({
					ids: ids
				}).then((res) => {
					if (res.success) {
						if (res.data.length > 0) {
							let list = res.data;
							that.goodsList.forEach((item) => {
								list.forEach((items) => {
									if (item.id == items.goodsId) {
										that.$set(item, "tags", items.tags);
									}
								});
							});
						}
					}
				});
			},

			// 根据板块id获取商品
			getSectionGoodsList(size, id, type, moduleType) {
				let that = this;
				let parmas = {
					page: that.page,
					size: size,
					mobileId: id,
					appointType: 3,
					moduleType: moduleType,
				};
				mobileGoodsList(parmas).then((res) => {
					if (res.success && res.data.list) {
						let list = res.data.list;
						let goodsIdList = [];
						if (list.length > 0) {
							for (let i = 0; i < list.length; i++) {
								goodsIdList.push(list[i].id);
							}
							if (type == 3) {
								//三列图
								// that.threeGoosList=list;
								if (this.userId != "") {
									priceGoodsList({
										goodsIds: goodsIdList
									}).then((PriceRes) => {
										list.forEach((item) => {
											for (let i = 0; i < PriceRes.data.length; i++) {
												if (item.id === PriceRes.data[i].goodsId) {
													that.$set(item, "minPrice", PriceRes.data[i]
														.minPrice);
													that.$set(item, "maxPrice", PriceRes.data[i]
														.maxPrice);
												}
											}
										});
									});
								}

								this.mobileList.forEach((item) => {
									if (item.id == id) {
										this.$set(item, "threeGoosList", list);
									}
								});
							}
							if (type == 4) {
								//四列
								that.fourGoosList = list;
							}
							if (type == 5) {
								//感兴趣的商品
								that.interestedList = list;
							}
							if (type == "more") {
								//更多商品
								// that.totalpage=res.data.pages;
								// let goodList2=JSON.parse(JSON.stringify(this.goodsList))
								that.goodsList = [...that.goodsList, ...list];
							}
							if (that.userId != "") {
								that.getPriceData(goodsIdList, type);
							}
						}
					}
				});
			},

			// 根据ids查询价格
			getPriceData(goodsIdList, type) {
				uni.setStorageSync("indexGetPrice", "1");
				let that = this;
				priceGoodsList({
					goodsIds: goodsIdList
				}).then((res) => {
					if (res.success) {
						let list = [];
						if (type == 3) {
							list = that.threeGoosList;
						} else if (type == 4) {
							list = that.fourGoosList;
						} else if (type == 5) {
							list = that.interestedList;
						} else {
							//更多商品
							list = that.goodsList;
						}

						list.forEach((item) => {
							for (let i = 0; i < res.data.length; i++) {
								if (item.id === res.data[i].goodsId) {
									that.$set(item, "minPrice", res.data[i].minPrice);
									that.$set(item, "maxPrice", res.data[i].maxPrice);
								}
							}
						});
					}
				});
			},

			// 进入详情
			toGoodsDetails(item) {
				uni.navigateTo({
					url: "/pages/classify/goodsDetails?id=" + item.id,
				});
			},
		},
		computed: {
			isFixed() {
				return this.tabTopVal >= this.tabInitTopVal - this.topLogoHeight;
			},
		},
	};
</script>

<style lang="scss">
	.index-nodata {
		text-align: center;

		image {
			width: 502rpx;
			height: 496rpx;
			margin: 100rpx auto 0;
		}

		view {
			font-size: 28rpx;
			color: #737373;
		}
	}

	// 网络错误
	.index-errNetwork {
		background: #ffeded;
		text-align: center;
		height: 80rpx;
		line-height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 24rpx;
			height: 24rpx;
			margin-right: 10rpx;
		}

		text {
			font-size: 24rpx;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;
			color: #999999;
		}
	}

	.index-image {
		width: 100%;
		height: 100%;
		display: block;
	}

	.index {
		font-size: 26rpx;
		overflow-x: hidden;
	}

	.index-logo {
		border: none;
		.logo-image {
			display: inline-block;

			image {
				position: relative;
				bottom: 6px;
				width: 220rpx;
				height: 80rpx;
			}
		}

		.logo-text {
			font-size: 36rpx;
		}
	}

	.index-box {
		padding: 160rpx 0 0;
		// #ifdef H5
		padding: 94rpx 0 0;
		// #endif
		background: #fff;
		border-bottom: 1rpx solid #f3f4f8;

		.logo-text {
			font-size: 40rpx;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 500;
			color: #424242;
			height: 77rpx;
			padding-top: 43rpx;
		}
	}

	.no-more {
		margin: 0;
		padding-top: 0;
		padding-bottom: 45rpx;
		background-color: #fff;
	}

	.index-swiper {
		width: 100%;

		.swiper {
			height: 340rpx;
			background: #ffffff !important;
		}

		.swiperImg {
			height: 252rpx !important;
			width: 96% !important;
			border-radius: 16rpx;
			margin: 44rpx 2% 0 !important;
		}

		.swiperImg-active {
			height: 334rpx !important;
			margin-top: 0 !important;
			//box-shadow: 0rpx 8rpx 12rpx 0rpx rgba(0, 0, 0, 0.21);
		}

	}

	.index-type {
		background: #fff;

		.index-type-line {
			float: left;
			text-align: center;

			.index-type-img {
				width: 120rpx;
				height: 120rpx;

				image {
					width: 120rpx;
					height: 120rpx;
				}
			}
		}
	}

	// 图片模块
	.index-image-moble {
		position: relative;

		view {
			float: left;
			font-size: 0;
			padding: 0;
			margin: 0;
		}

		.index-news {
			background: url(../../static/imgs/newsIcon.png);
			background-size: 52rpx 52rpx;
			width: 52rpx;
			height: 52rpx;
			right: 26rpx;
			top: 35rpx;
			position: absolute;

			text {
				display: block;
				background: #f94021;
				width: 10rpx;
				height: 10rpx;
				border-radius: 100%;
				position: absolute;
				right: 7rpx;
				top: 4rpx;
			}
		}

		.index-image-mobleView1 {
			width: 100%;
		}

		.index-image-mobleView2 {
			width: calc(100% / 2);
		}

		.index-image-mobleView3 {
			width: calc(100% / 3);
		}

		.index-image-mobleView4 {
			width: calc(100% / 4);
		}

		.index-image-mobleView5 {
			width: calc(100% / 5);
		}

		image {
			width: 100%;
			height: 100%;
			display: block;
		}
	}

	.index-goods1-image {
		width: 100%;
		font-size: 0;
	}

	.index-goods1 {
		font-size: 0;
		position: relative;

		.index-goods1-imgBg {
			position: absolute;
			width: 100%;
		}

		.index-goods1-box {
			position: absolute;
			top: 0;
			display: flex;
			padding: 0 6%;
		}

		.index-goods1-line {
			width: calc(100% / 3);
			font-size: 0;

			image {
				width: 188rpx;
				height: 188rpx;
				border-radius: 10rpx 10rpx 0 0;
				overflow: hidden;
				margin-left: 15rpx;
			}

			.index-goods1-btm {
				font-size: 24rpx;
				padding: 15rpx 30rpx;

				view:nth-child(1) {
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
				}

				.index-no-login {
					color: $base-color2;
					font-size: 22rpx;
					margin-top: 10rpx;
				}

				.index-login {
					font-size: 28rpx;
					font-weight: bold;
					color: $base-color2;
					margin-top: 8rpx;

					&.noPrice {
						font-size: 24rpx;
						font-weight: normal;

						text {
							display: none;
						}
					}

					text {
						font-size: 22rpx;
					}
				}
			}
		}
	}

	.index-activity {
		display: flex;
		align-items: center;
		background: #fff;
		margin: 20rpx 30rpx;
		border-radius: 10rpx;
		padding: 30rpx;

		.index-activityLf {
			width: 260rpx;
			height: 410rpx;
		}

		.index-activity-LF {
			width: 260rpx;
			font-size: 0;

			image {
				width: 260rpx;
				height: 190rpx;
			}
		}

		.index-activity-rg {
			width: 340rpx;
			margin-left: 30rpx;

			image {
				width: 340rpx;
				height: 190rpx;
			}

			image:nth-child(1) {
				margin-bottom: 25rpx;
			}
		}
	}

	.index-qiangGou {
		width: 630rpx;
		background: #fff;
		border-radius: 10rpx;
		padding: 30rpx;
		margin: 0 30rpx;

		.index-qiangGou-title {
			font-size: 32rpx;
			font-weight: bold;
			color: $base-color1;
			text-align: center;
		}

		.index-qiangGou-list {
			display: flex;
			margin-top: 20rpx;

			.index-qiangGou-line {
				width: 210rpx;
				text-align: center;

				image {
					width: 144rpx;
					height: 210rpx;
				}

				.index-qiangGou-xz {
					background: rgba($color: $base-color1, $alpha: 0.1);
					color: $base-color1;
					width: 190rpx;
					border-radius: 10rpx;
					padding: 10rpx 0;
				}

				.index-qiangGou-box {
					margin-top: 20rpx;
				}

				.index-qiangGou-name {
					font-size: 22rpx;
					margin-top: 10rpx;
				}

				.index-qiangGou-info {
					display: flex;
					align-items: center;
					margin-top: 10rpx;

					.index-qiangGou-price {
						color: $base-color2;
						font-size: 32rpx;
						font-weight: bold;

						text {
							font-size: 22rpx;
						}
					}

					.index-qiangGou-btn {
						background: linear-gradient(0deg, #ed5307 0%, #ffab81 100%);
						border-radius: 10rpx;
						color: #fff;
						font-size: 18rpx;
						padding: 5rpx;
						margin-left: 10rpx;
					}
				}
			}
		}
	}

	.index-goodGoods {
		width: 100%;
		position: relative;

		.index-goodGoods-bg {
			width: 100%;
			position: absolute;
		}

		.index-goodGoods-boxV {
			top: 0;
			margin-left: 30rpx;
			position: absolute;
			display: flex;
		}

		.index-goodGoods-line {
			background: #fff;
			padding: 30rpx;
			width: 275rpx;

			.index-goodGoods-tip {
				text:nth-child(1) {
					font-size: 28rpx;
					font-weight: bold;
				}

				text:nth-child(2) {
					font-size: 22rpx;
					color: $base-color2;
					margin-left: 15rpx;
				}
			}

			.index-goodGoods-box {
				display: flex;
				justify-content: flex-start;

				.index-goodGoods-boxLine {
					text-align: center;
					width: 125rpx;

					image {
						width: 100rpx;
						height: 100rpx;
					}

					text {
						font-size: 22rpx;
						margin-top: 20rpx;
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2;
						overflow: hidden;
					}
				}

				.index-goodGoods-boxLine:nth-child(2) {
					margin-left: 20rpx;
				}
			}
		}

		.index-goodGoods-line:nth-child(2) {
			margin-left: 20rpx;
		}
	}

	.index-interest {
		background: #fff;
		padding: 30rpx 0;

		.index-interest-title {
			font-size: 28rpx;
			font-weight: bold;
			padding: 30rpx;
		}

		.index-interest-box {
			display: flex;

			.index-interest-boxLine {
				width: 130rpx;
				text-align: center;
				margin-left: 30rpx;

				image {
					width: 130rpx;
					height: 130rpx;
					border-radius: 10rpx;
					overflow: hidden;
				}

				view {
					font-size: 22rpx;
					margin-top: 10rpx;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
				}

				.price-view-right {
					font-size: 28rpx;
					color: $base-color2;
					margin-top: 8rpx;
					text-align: left;

					text {
						font-size: 22rpx;
					}
				}
			}
		}
	}

	.index-moreGoods {
		background: #fff;

		.index-moreGoods-title {
			font-size: 30rpx;
			font-weight: bold;
			padding: 30rpx;
		}

		.index-moreGoods-box {
			padding: 20rpx;

			.index-moreGoods-price {
				font-size: 28rpx;
				font-weight: bold;
				color: $base-color2;
				margin-top: 10rpx;

				text {
					font-size: 22rpx;
				}
			}

			.index-moreGoods-price2 {
				font-size: 24rpx;
				color: $base-color2;
				margin-top: 10rpx;
			}
		}
	}

	.index-moreGoods2 {
		background: #fff;
		min-height: calc(100vh - 180rpx);
		// #ifdef H5
		min-height: calc(100vh - 114rpx - var(--window-bottom));
		// #endif

		&.fixed {
			.index-moreGoods2-tab {
				position: fixed;
				top: 180rpx;
				// #ifdef H5
				top: 102rpx;
				// #endif
				z-index: 100;
			}

			.index-moreGoods2-list {
				min-height: 100vh;
				padding-top: 104rpx;
				box-sizing: border-box;
			}

			.index-nodata {
				padding-top: 104rpx;
				box-sizing: border-box;
			}
		}

		.index-moreGoods2-tab {
			position: relative;
			width: 100%;
			padding: 0 30rpx 9rpx;
			height: 104rpx;
			background: #fff;
			box-sizing: border-box;

			.index-moreGoods2-view {
				display: flex;
				align-items: center;

				scroll-view::-webkit-scrollbar {
					display: none;
				}

				view {
					margin-right: 60rpx;

					.index-moreGoods2-item {
						font-size: 32rpx;
						font-family: PingFangSC-Regular, PingFang SC;
						font-weight: 400;
						color: #333333;
						padding: 20rpx 0;
						display: block;
						white-space: nowrap;
					}

					.index-moreGoods2-hover {
						font-size: 36rpx;
						font-weight: 600;
					}

					.index-moreGoods2-hoverLine {
						display: block;
						height: 6rpx;
						width: 76rpx;
						margin: 0 auto;
						border-radius: 20rpx;
					}
				}
			}

			.index-moreGoods2-tabMore {
				position: absolute;
				right: 30rpx;
				top: 50%;
				width: 110rpx;
				background: linear-gradient(90deg,
						rgba(255, 255, 255, 0.36) 0%,
						rgba(255, 255, 255, 0.92) 25%,
						#ffffff 100%);
				text-align: right;
				transform: translateY(-50%);

				image {
					width: 44rpx;
					height: 44rpx;
				}
			}
		}

		.index-moreGoods2-list {
			padding-top: 9rpx;

			.index-moreGoods2-listContent {
				padding: 0 12rpx;

				.index-moreGoods2-listItem {
					width: 336rpx;
					border: 2rpx solid #F7F7F7;
					border-radius: 16rpx;
					margin-top: 18rpx;
					float: left;
					margin-left: 18rpx;
					padding-bottom: 16rpx;
					position: relative;

					image {
						width: 336rpx;
						height: 336rpx;
						border-top-left-radius: 16rpx;
						border-top-right-radius: 16rpx;
						background: #EFEFEF;
					}

					.index-moreGoods2-tip {
						width: 68rpx;
						height: 32rpx;
						background: #f94021;
						border-radius: 16rpx 0rpx 16rpx 0rpx;
						color: #fff;
						font-size: 24rpx;
						text-align: center;
						position: absolute;
						top: 0;
						left: 0;
					}

					.index-moreGoods2-listName {
						color: #6C6C6C;
						font-size: 26rpx;
						padding: 0 32rpx;
						font-family: PingFangSC-regular;
						font-weight: 400;
						line-height: 32rpx;
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2;
						overflow: hidden;
						margin-top: 15rpx;
					}

					.index-moreGoods2-promition {
						display: flex;
						align-items: center;
						padding: 10rpx 32rpx 0;

						text {
							display: block;
							border-radius: 4rpx;
							border: 1rpx solid #f94021;
							padding: 3rpx 10rpx;
							color: #f94021;
							font-size: 20rpx;
						}

						text:nth-child(2) {
							margin-left: 10rpx;
						}
					}

					.index-moreGoods2-price {
						font-size: 30rpx;
						font-family: ArialMT;
						font-weight: bold;
						color: #f94021;
						padding: 20rpx 32rpx 0;
					}

					.index-moreGoods2-priceNo {
						font-size: 24rpx;
						font-family: PingFangSC-Regular, PingFang SC;
						font-weight: 400;
						color: #f94021;
						padding: 20rpx 32rpx;
					}
				}
			}
		}

		.index-moreGoods2-btmTip {
			font-size: 24rpx;
			font-family: PingFangSC-Regular, PingFang SC;
			color: #5f5f5f;
			text-align: center;
			margin-top: 48rpx;
			padding-bottom: 148rpx;
		}
	}

	.index-float-top {
		width: 96rpx;
		height: 96rpx;
		background: #ffffff;
		box-shadow: 0rpx 4rpx 14rpx 0rpx rgba(0, 0, 0, 0.25);
		position: fixed;
		bottom: 98rpx;
		// #ifdef H5
		bottom: 213rpx;
		// #endif
		right: 20rpx;
		border-radius: 100%;
		z-index: 102;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 44rpx;
			height: 44rpx;
		}
	}

	.index-float-btm {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		// #ifdef H5
		bottom: var(--window-bottom);
		// #endif
		background-color: rgba(0, 0, 0, 0.72);
		height: 90rpx;
		line-height: 90rpx;
		font-size: 28rpx;
		color: #fff;
		text-align: right;
		padding: 0 30rpx;
		display: flex;
		align-items: center;
		box-sizing: border-box;
		z-index: 101;

		.index-float-btm-item1 {
			margin-right: 20rpx;
			width: 70rpx;
			height: 90rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				margin-top: 1rpx;
				height: 80rpx;
			}
		}

		.index-float-btm-item2 {
			width: 50%;
			height: 90rpx;
			line-height: 90rpx;
			text-align: left;
			font-size: 28rpx;
		}

		.index-float-btm-item3 {
			position: absolute;
			top: 16rpx;
			right: 30rpx;

			.index-float-btm-item3-btn {
				width: 172rpx;
				height: 60rpx;
				line-height: 60rpx;
				background-color: #f94021;
				font-size: 28rpx;
				text-align: center;
				border-radius: 60rpx;
			}
		}
	}
</style>
