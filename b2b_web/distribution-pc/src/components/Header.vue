<template>
	<div class="header">
		<div class="top-content">
			<div class="all rl-margin-zero rl-clear">
				<div class="rl-fl">
					<p class="rl-text-lightWhite rl-fl" :class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'">
						<span class="rl-fl">HI～</span>
						<el-tooltip class="rl-fl username" v-if="userId" :content="userName" placement="bottom"
							effect="dark">
							<span>，{{userName}}</span>
						</el-tooltip>
						<span class="rl-fl">，{{$t('P.Welcome')}}</span>
					</p>
				</div>
				<div class="banner-notice rl-fl">
					<div class="notice-box">
						<i class="iconfont icon-notice"></i>
						<div class="notice-content">
							<div class="item-ul">
								<span class="item-text" v-for="(notice, index) in noticeList" :key="notice.id"
									@click="goNoticeDetail(notice.id, index)">{{($i18n.locale === 'zh')?notice.title:notice.titleEn}}</span>
							</div>
						</div>
					</div>
				</div>

				<div class="rl-fr header-right">
					<div class="rl-fl rl-margin-right-xxxxs">
						<div class="rl-text-white rl-text-base rl-fl"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'"
							v-if="this.userId !== ''&&  this.userId!== null && Number(this.capitalStatus) === 1">【审核中】
						</div>
						<div class="rl-text-white rl-text-base rl-fl"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'"
							v-if="this.userId !== ''&&  this.userId!== null && Number(this.capitalStatus) === 4">【审批中】
						</div>
						<div class="rl-text-white rl-text-base rl-fl"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'"
							v-if="this.userId !== ''&&  this.userId!== null && Number(this.freezeStatus) === 2">【已冻结】
						</div>
						<div @click="notPass" class="rl-text-white rl-text-base rl-fl cursor-pointer"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'"
							v-if="this.userId !== ''&&  this.userId!== null && Number(this.capitalStatus) === 3">【未通过】
						</div>
						<div @click="logOut" class="rl-text-lightWhite rl-text-base cursor-pointer rl-fl"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'"
							v-if="this.userId !== ''&&  this.userId!== null">【{{$t('P.SignOut')}}】</div>
						<div v-else>
							<div @click="logIn"
								class="rl-text-lightWhite rl-text-base cursor-pointer rl-fl rl-margin-right-xxxxs"
								:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'">{{$t('P.Login')}}</div>
							<!-- <div @click="showIntroDialog = true"
								class="rl-text-lightWhite rl-text-base cursor-pointer rl-fl rl-margin-left-double"
								:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'">{{$t('P.Register')}}</div> -->
						</div>

						<div @click="goUserCenter" class="rl-text-lightWhite rl-text-base cursor-pointer rl-fl"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'"
							v-if="userId !== ''&& userId!== null && Number(capitalStatus) === 2 && Number(freezeStatus) === 1">
							{{$t('P.MyOrder')}}
						</div>
					</div>
					<div @click="goAbout(3)"
						class="rl-fl rl-text-lightWhite rl-text-base cursor-pointer rl-margin-left-double"
						:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'">{{$t('About.ContactUs')}}</div>
					<!-- 选择币种 -->
					<div class="lang-box rl-fl rl-margin-right-default"
						:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13 rl-margin-left-double'" v-if="useLang">
						<el-select v-model="lang" @change="fLangChange" :placeholder="$t('P.Select')"
							:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'">
							<el-option v-for="item in langList" :key="item.id" :label="item.name" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div @click="goShopCart" class="rl-fl rl-text-lightWhite rl-text-base cursor-pointer shopcart-box"
						:class="$i18n.locale === 'zh' ? '' : 'nav-item-en13'">
						<i class="iconfont icon-shopcart">
							<span class="num" v-show="cartCount>0">{{cartCount}}</span>
						</i>
						{{$t('ShopCarts.ShopCar')}}
					</div>
					<div class="rl-fl rl-text-base cursor-pointer newsNav-box">
						<el-dropdown trigger="click">
							<span class="el-dropdown-link">
								<img src="../assets/images/newsIcon.png" />
								<span class="newBox-text rl-text-lightWhite">消息</span>
								<span class="num" v-if="newsNum && newsNum > 0">{{newsNum}}</span>
							</span>
							<el-dropdown-menu class="news-wrap" slot="dropdown">
								<div class="news-box">
									<div class="news-content" v-if="newDataList && newDataList.length > 0">
										<div v-for="item in newDataList" :key="item.id" class="news-line">
											<div class="news-status" @click="msgcenterReadFun(item)">
												<div class="news-status-Lf">
													<span class="news-status-red" v-if="item.readFlag!=1"></span>
													<span v-if="item.msgType==1">订单状态通知</span>
													<span v-if="item.msgType==2">发货通知</span>
													<span v-if="item.msgType==3">订单未支付提醒</span>
													<span v-if="item.msgType==4">分销商审核通知</span>
													<span v-if="item.msgType==5">新订单通知</span>
													<span v-if="item.msgType==6">自定义消息</span>
												</div>
												<div class="news-status-Rg"> <span>详情</span><img
														src="../assets/images/newDetailIcon.png"></div>
											</div>
											<div class="news-createTime">{{item.createTime}}</div>
											<div class="news-info">
												<div class="news-infoList" v-for="(items, index) in item.bodyArr"
													:key="index">
													<span class="label">{{items.title}}</span><span
														class="value">{{items.value}}</span>
												</div>
											</div>

										</div>

									</div>
									<p class="no-news-data" v-else>{{$t('P.NoData')}}~</p>
									<div class="news-btnAll" :style="{'color':isAllRead?'#0DB8CF':'#666'}"
										v-if="newsNum && newsNum > 0" @click="msgcenterReadAllFun">全部已读</div>
								</div>
							</el-dropdown-menu>
						</el-dropdown>
					</div>
				</div>
			</div>
		</div>
		<div class="head-content rl-bg-white">
			<div class="all rl-margin-zero rl-clear">
				<router-link to="/Index">
					<div class="logo-img rl-fl"></div>
				</router-link>
				<div class="rl-fl">
					<div class="nav-top-box">
						<div class="nav-list">
							<div class="nav-item"
								:class="[currentTab === 'classify' ? 'active' : '', ($i18n.locale === 'zh' ? '' : 'nav-item-en14')]"
								@click="goAllClassify">{{$t('Index.AllProductCategories')}}</div>
							<div class="nav-item"
								:class="[currentTab === 'index' ? 'active' : '', ($i18n.locale === 'zh' ? '' : 'nav-item-en14')]">
								<router-link to="/Index">{{$t('P.HomePage')}}</router-link>
							</div>
							<!-- 新品 -->
							<div class="nav-item new" :class="$i18n.locale === 'zh' ? '' : 'nav-item-en14'"
								@click="goNewProduct">
								{{$t('Index.NewProduct')}}
							</div>
							<!-- 活动 -->
							<div class="nav-item"
								:class="[currentTab === 'activity' ? 'active' : '', ($i18n.locale === 'zh' ? '' : 'nav-item-en14')]"
								v-show="(showState == 1||showState==2)&&userId!=''">
								<router-link to="/ActivityColumn">{{$t('Index.Activities')}}</router-link>
							</div>
							<div class="nav-item rl-relative"
								:class="[Number($route.query.nav_index) === index ? 'active' : '', ($i18n.locale === 'zh' ? '' : 'nav-item-en14')]"
								@mouseenter="navColumnShows(item)" @mouseleave="navColumnHides(item)"
								v-for="(item,index) in columnList" :key="item.id"
								@click="goColumnGoods(item.id,index,item.title,item.titleEn)">
								<template v-if="index <= 2 && columnList && $i18n.locale === 'zh'">
									<span v-show="$i18n.locale === 'zh' || !item.titleEn == true">{{item.title}}</span>
									<span v-show="$i18n.locale === 'en'">{{item.titleEn}}</span>
									<div class="nav-Column-service"
										v-show="navColumnShow === true && item.isYunZhiYin === 1">
										<ul>
											<li class="rl-text-white">
												<i v-if="$i18n.locale === 'zh'">云智印产品</i>
												<i v-else class="rl-text-xxs">Uzien products</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlOne">
												<i v-if="$i18n.locale === 'zh'">安装说明（C180一代机）</i>
												<i v-else class="rl-text-xxs">Installation instructions (C180 1st
													generation of Plotter)</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlTwo">
												<i v-if="$i18n.locale === 'zh'">安装说明（C180一代机）</i>
												<i v-else class="rl-text-xxs">Installation instructions (C180 2nd
													generation of Plotter)</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlThr">
												<i v-if="$i18n.locale === 'zh'">操作指南（C180一代机）</i>
												<i v-else class="rl-text-xxs">Operation guide (C210 1st generation of
													Plotter)</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlFor">
												<i v-if="$i18n.locale === 'zh'">操作指南（C210二代机）</i>
												<i v-else class="rl-text-xxs">Operation guide (C180 2nd generation of
													Plotter)</i>
											</li>
											<li class="rl-text-white" @click.stop="goUrlFiv">
												<i v-if="$i18n.locale === 'zh'">故障排除</i>
												<i v-else class="rl-text-xxs">Troubleshooting</i>
											</li>
											<li class="rl-text-white" @click.stop="goUrlSix">
												<i v-if="$i18n.locale === 'zh'">问题反馈</i>
												<i v-else class="rl-text-xxs">Feedback about problems</i>
											</li>
										</ul>
									</div>
								</template>
								<template v-if="index < 0 && columnList && $i18n.locale === 'en'">
									<span v-show="$i18n.locale === 'zh' || !item.titleEn == true">{{item.title}}</span>
									<span v-show="$i18n.locale === 'en'">{{item.titleEn}}</span>
									<div class="nav-Column-service"
										v-show="navColumnShow === true && item.isYunZhiYin === 1">
										<ul>
											<li class="rl-text-white">
												<i v-if="$i18n.locale === 'zh'">云智印产品</i>
												<i v-else class="rl-text-xxs">Uzien products</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlOne">
												<i v-if="$i18n.locale === 'zh'">安装说明（C180一代机）</i>
												<i v-else class="rl-text-xxs">Installation instructions (C180 1st
													generation of Plotter)</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlTwo">
												<i v-if="$i18n.locale === 'zh'">安装说明（C180一代机）</i>
												<i v-else class="rl-text-xxs">Installation instructions (C180 2nd
													generation of Plotter)</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlThr">
												<i v-if="$i18n.locale === 'zh'">操作指南（C180一代机）</i>
												<i v-else class="rl-text-xxs">Operation guide (C210 1st generation of
													Plotter)</i>
											</li>
											<li class="rl-text-white app-single-limit" @click.stop="goUrlFor">
												<i v-if="$i18n.locale === 'zh'">操作指南（C210二代机）</i>
												<i v-else class="rl-text-xxs">Operation guide (C180 2nd generation of
													Plotter)</i>
											</li>
											<li class="rl-text-white" @click.stop="goUrlFiv">
												<i v-if="$i18n.locale === 'zh'">故障排除</i>
												<i v-else class="rl-text-xxs">Troubleshooting</i>
											</li>
											<li class="rl-text-white" @click.stop="goUrlSix">
												<i v-if="$i18n.locale === 'zh'">问题反馈</i>
												<i v-else class="rl-text-xxs">Feedback about problems</i>
											</li>
										</ul>
									</div>
								</template>
							</div>

							<div v-if="columnList && columnList.length > 3 && $i18n.locale === 'zh'"
								class="nav-item rl-relative">
								<el-dropdown>
									<span class="el-dropdown-link">
										<i class="el-icon-s-unfold"></i>
									</span>
									<el-dropdown-menu slot="dropdown">
										<el-dropdown-item v-if="index >= 1" v-for="(item,index) in columnList"
											:key="item.id"
											:class="Number($route.query.nav_index) === index ? 'active' : ''">
											<span v-show="$i18n.locale === 'zh' || !item.titleEn == true"
												@click="goColumnGoods(item.id,index,item.title,item.titleEn)">{{item.title}}</span>
											<span v-show="$i18n.locale === 'en'"
												@click="goColumnGoods(item.id,index,item.title,item.titleEn)">{{item.titleEn}}</span>
										</el-dropdown-item>
									</el-dropdown-menu>
								</el-dropdown>
							</div>
							<div v-if="columnList && columnList.length >= 0 && $i18n.locale === 'en'"
								class="nav-item rl-relative">
								<el-dropdown>
									<span class="el-dropdown-link">
										<i class="el-icon-s-unfold"></i>
									</span>
									<el-dropdown-menu slot="dropdown">
										<el-dropdown-item v-if="index >= 0" v-for="(item,index) in columnList"
											:key="item.id"
											:class="Number($route.query.nav_index) === index ? 'active' : ''">
											<span v-show="$i18n.locale === 'zh' || !item.titleEn == true"
												@click="goColumnGoods(item.id,index,item.title,item.titleEn)">{{item.title}}</span>
											<span v-show="$i18n.locale === 'en'"
												@click="goColumnGoods(item.id,index,item.title,item.titleEn)">{{item.titleEn}}</span>
										</el-dropdown-item>
									</el-dropdown-menu>
								</el-dropdown>
							</div>
							<!--              <div v-if="this.userId !== ''&&  this.userId!== null" class="nav-item"><router-link to="/DiscountActivity">{{$t('Index.Discount')}}</router-link></div>-->
						</div>
					</div>
				</div>
				<div class="rl-fr">
					<div class="logo-box rl-clear">
						<div class="rl-fl input-box rl-bg-white">
							<input @keyup.enter="goSearch" v-model="searchContent" type="text"
								:placeholder="$i18n.locale === 'zh' ? searchHot : 'Search'" />
							<i class="iconfont icon-search" @click="goSearch"></i>
						</div>
					</div>

				</div>
			</div>
		</div>
		<div class="nav">
			<div class="all rl-margin-zero">
				<div class="rl-relative">

					<div class="nav-service" v-show="navShow === true || value === 1" @mouseenter="navShows"
						@mouseleave="navHides">
						<!--<div class="rl-text-mid rl-text-white classify rl-bg-blue-ms cursor-pointer" @click="navShows">所有商品分类</div>-->
						<ul ref="classifyList" :class="isEnter ? 'active' : ''">
							<li v-for="(item, index) in classifyListOne" :key="item.id"
								:class="activeItem === index ? 'active' : ''" @mouseenter="enter(item)"
								@mouseleave="leave(item)" @click="goClassifyOne(item)">
								<div class="nav-box">
									<span>{{($i18n.locale === 'zh' || !item.nameEn == true) ? item.name : item.nameEn}}</span>
									<i class="icon el-icon-arrow-right"></i>
								</div>
							</li>
						</ul>
						<div class="nav-ul rl-bg-white" ref="classifyItem"
							v-show="classifyListTwo.length > 0 && isEnter"
							:style="classifyListTwo.length>12?'width:717px':'width:478px'">
							<div @click.stop="goClassify(classify)" v-for="(classify, index) in classifyListTwo"
								:key="classify.id" :style="classifyListTwo.length>12?'width:33.3333%':'width:50%'"
								class="cursor-pointer nav-item">
								<img class="nav-img"
									:src="($i18n.locale === 'zh' || !classify.nameEn == true)?classify.imageUrl + '?x-oss-process=image/resize,h_80,l_80':classify.imageUrlEn + '?x-oss-process=image/resize,h_80,l_80'"
									alt />
								<span class="nav-text"
									v-if="$i18n.locale === 'zh' || !classify.nameEn == true">{{classify.name}}</span>
								<span class="nav-text" v-if="$i18n.locale === 'en'">{{classify.nameEn}}</span>
							</div>
							<div v-if="classifyListTwo.length > 12" class="cursor-pointer nav-item more">
								<!-- <span class="nav-text">
                  查看更多
                  <i class="iconfont icon-more"></i>
                </span> -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--审核未通过弹框-->
		<div class="cover" v-show="showcov === true"></div>
		<div class="pro-cover cover-box rl-padding-bottom-lllg rl-relative" v-show="showcov === true">
			<div class="img rl-margin-zero">
				<img src="../assets/images/shuts.png" alt />
			</div>
			<div class="rl-margin-bottom-xxxs rl-padding-bottom-xxxs">
				<p class="rl-tc rl-text-orange-sm rl-text-sm rl-margin-top-xxxs rl-margin-bottom-default">
					<i v-if="$i18n.locale === 'zh'">很抱歉，您的会员申请审核未通过</i>
					<i v-else>Sorry, your member application fails to be reviewed.</i>
				</p>
				<p class="rl-tc">
					<span v-if="$i18n.locale === 'zh'">您可以重新提交申请资料继续申请</span>
					<span v-else>You can resubmit the application data for continuous application.</span>
				</p>
			</div>
			<!-- <span @click="shutLog" class="shut cursor-pointer"></span>-->
			<div class="address-info rl-bg-white rl-padding-left-default rl-clear rl-margin-zero">
				<div @click="confirmModify"
					class="rl-fl rl-tc all rl-bg-blue-mm cursor-pointer rl-margin-top-default rl-text-white">
					<i v-if="$i18n.locale === 'zh'">重新申请</i>
					<i v-else>Apply again</i>
				</div>
				<div @click="shutLog"
					class="rl-fl rl-tc all right rl-margin-left-default rl-bg-gray-sm cursor-pointer rl-margin-top-default">
					{{$t('P.Back')}}
				</div>
			</div>
		</div>
		<!--被冻结弹框-->
		<div class="cover" v-show="showcovs === true"></div>
		<div class="pro-cover cover-box rl-padding-bottom-lllg rl-relative" v-show="showcovs === true">
			<div class="img rl-margin-zero">
				<img src="../assets/images/locks.png" alt />
			</div>
			<div class="rl-margin-bottom-xxxs rl-padding-bottom-xxxs">
				<p class="rl-tc rl-text-orange-sm rl-text-sm rl-margin-top-xxxs rl-margin-bottom-default">
					<i v-if="$i18n.locale === 'zh'">您的账号已被冻结</i>
					<i v-else>Your account has been frozen.</i>
				</p>
				<p class="rl-tc">
					<i v-if="$i18n.locale === 'zh'">被冻结后您只能浏览商品。如需恢复账号权限，请联系客服人员</i>
					<i v-else class="rl-text-xxs">In case of being frozen, you can only browse commodities. In case of
						restoring account authority, please contact customer service personnel.</i>
				</p>
			</div>
			<!-- <span @click="shutLog" class="shut cursor-pointer"></span>-->
			<div class="address-infos rl-bg-white rl-clear rl-margin-zero">
				<div @click="shutLogs"
					class="rl-fl rl-tc all rl-bg-blue-mm cursor-pointer rl-margin-top-default rl-text-white">
					{{$t('P.OK')}}
				</div>
			</div>
		</div>

		<!-- 产品介绍 -->
		<productIntro v-show="showIntroDialog" @closeIntro="closeProductIntro"></productIntro>

		<!-- 提示登录弹窗 -->
		<loginDialog v-show="showLoginDialog" @handleClose="closeLoginDialog" @handleLogin="logIn"></loginDialog>
	</div>
</template>

<script>
	import {
		MessageBox
	} from 'element-ui';

	import {
		getCartNum
	} from '@/api/cart';
	import GD from '@/assets/js/globalData';
	import productIntro from '@/components/dialog/productIntro';
	import loginDialog from '@/components/dialog/loginDialog';
	import {
		loginOut
	} from '@/assets/js/common.js'
	// liu--
	import {
		userInfo,
		columnList,
		noticeList,
		classifyList,
		shoppingcartList,
		currencyRate,
		msgcenterList,
		msgcenterNotRead,
		msgcenterRead,
		msgcenterReadAll
	} from '@/apiService/api'
	import {
		removeToken
	} from '@/utils/auth'
	export default {
		name: 'Header',
		props: {
			value: {
				type: Number,
				default: 0
			},
			classifyId: {
				type: Boolean
			},
			searchName: {
				type: String
			},
			searchType: {
				type: Number,
				default: 0
			},
			nav_index: {
				type: Number,
				default: 0
			},
			logOuts: {
				// 被登出状态
				type: Boolean
			},
			custom: {
				type: Boolean
			},
			activitys: {
				type: Boolean
			},
			discountActivity: {
				type: Boolean
			},
			newProduct: {
				type: Boolean
			},
			indexPage: {
				type: Boolean
			},
			shopCarPage: {
				type: Boolean
			},
			reLoad: {
				type: Boolean
			},
			loginState: {
				type: Boolean
			},
			totalCount: {
				// 购物车数量
				type: Number,
				default: 0
			},
			currentTab: {
				type: String
			}
		},
		components: {
			productIntro,
			loginDialog
		},
		inject: ['reload'],
		data() {
			return {
				cartCount: 0,
				num: 10,
				showIndex: false,
				showCustom: false,
				showCustomColumn: false,
				showActivitys: false,
				showNewProduct: false,
				showDiscountActivity: false,
				showShopCar: false,
				userId: '',
				isLogin: false,
				gradeId: '',
				userName: '',
				capitalStatus: '',
				freezeStatus: 0, // 2 冻结 1 未冻结
				navShow: false, // 控制商品分类下拉菜单
				navColumnShow: false, // 控制云之印下拉菜单
				classifyListOne: [],
				classifyListTwo: [],
				columnList: [],
				searchList: [], // 热门搜索列表
				searchContent: '', // 搜索内容
				showState: localStorage.getItem('promotionScope'),
				showcov: false, // 是否显示弹框（审核不通过）
				showcovs: false, // 是否显示弹框(冻结)
				userInfos: {
					provinceId: '', // 省市区上级id
					cityId: '',
					townId: '',
					provinceName: '',
					cityName: '',
					areaName: '',
					name: '', // 基本信息
					mobile: '',
					authCode: '',
					email: '',
					password: '',
					confirmPassword: '',
					companyName: '',
					bankDepositName: '',
					bankAccountName: '',
					bankAccount: '',
					taxpayerNumber: '',
					address: '',
					zipCode: '',
					isTaxpayer: 0, // 是否一般纳税人
					isSmallTaxpayer: 1, // 是否小规模纳税人
					isIndustrial: 1, // 是否个体工商户
					isFit: 0, // 是否散客
					recommendMan: '',
					phone: '',
					sex: 0 // 性别
				},
				searchHot: '',
				useLang: false, // 是否启用多语种 - 施义煌
				isReload: false,
				langList: GD.langList, // 语种列表 - 施义煌
				lang: 'zh-CNY', // 语种 - 施义煌
				isEnter: false, // 鼠标是否进入分类
				activeItem: '', // 鼠标当前进入分类
				noticeList: [], // 公告列表
				showIntroDialog: false, // 产品介绍
				showLoginDialog: false, // 提示登录
				loginTimer: null, // 提示登录定时器

				// -----消息中心-----
				isAllRead: false, //是否全部已读
				newDataList: [],
				newsNum: 0, // 消息未读数量

			};
		},
		created() {
			// 施义煌
			this.useLang = this.$b2bConfig.lang;
			this.lang = window.localStorage.getItem('bLang') ?
				window.localStorage.getItem('bLang') :
				'zh_CNY';

			let userId = localStorage.getItem('userId');

			if (userId && userId != '' && userId != 'undefined') {
				this.noticeData();
				this.currencyRateFun();
				this.msgcenterNotReadFun(); // 未读消息数量
				this.msgcenterListFun(); // 消息列表
			}
		},
		watch: {
			totalCount(val) {
				this.cartCount = parseInt(val);
			}
		},
		activated() {

		},
		methods: {
			// 查询消息列表
			msgcenterListFun() {
				let that = this;
				msgcenterList().then(res => {
					if (res.success) {
						that.newDataList = res.data.list;
						//  msgType 1:订单状态通知, 2:发货通知 3:订单未支付提醒 4:分销商审核通知,  5:新订单通知  6:自定义消息
						that.newDataList.forEach(item => {
							if (item.miniBody) {
								let obj1 = JSON.parse(item.miniBody);
								let arr1 = [];
								if (obj1.data) {
									if (item.msgType == 1) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string1.value
										}, {
											title: "订单状态",
											value: obj1.data.phrase2.value
										}, {
											title: "温馨提醒",
											value: obj1.data.thing11.value
										}, ]
									}
									if (item.msgType == 2) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string1.value
										}, {
											title: "快递公司",
											value: obj1.data.name3.value
										}, {
											title: "快递单号",
											value: obj1.data.character_string4.value
										}, {
											title: "发货时间",
											value: obj1.data.date10.value
										}, ]
									}
									if (item.msgType == 3) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string5.value
										}, {
											title: "订单金额",
											value: obj1.data.amount7.value
										}, {
											title: "审核结果",
											value: obj1.data.date6.value
										}, {
											title: "温馨提示",
											value: obj1.data.thing4.value
										}, ]
									}
									if (item.msgType == 4) {
										arr1 = [{
											title: "申请时间",
											value: obj1.data.date1.value
										}, {
											title: "分销商名称",
											value: obj1.data.thing5.value
										}, {
											title: "温馨提示",
											value: obj1.data.thing4.value
										}, ]
									}
									if (item.msgType == 5) {
										arr1 = [{
											title: "订单编号",
											value: obj1.data.character_string2.value
										}, {
											title: "下单用户",
											value: obj1.data.name1.value
										}, {
											title: "下单金额",
											value: obj1.data.amount3.value
										}, ]
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
			// 查询当前未读的消息的数量
			msgcenterNotReadFun() {
				msgcenterNotRead().then(res => {
					console.log('消息未读数', res.data);
					this.newsNum = res.data;
					this.isAllRead = this.newsNum > 0 ? true : false;

				})
			},
			// 消息全部标志已读
			msgcenterReadAllFun() {
				msgcenterReadAll().then(res => {
					if (res.success) {
						this.$message.success('更改成功！');
						this.msgcenterNotReadFun(); // 未读消息数量
						this.msgcenterListFun(); // 消息列表
						this.isAllRead = true;
					}
				})
			},

			// 点击详情跳转且标志已读
			msgcenterReadFun(item) {
				let baseHost = '';
				if (item.msgType != 6) {
					// 非自定义消息，链接前缀处理
					baseHost = window.location.href.split("#")[0] + "#";
				}

				if (item.readFlag != 1) {
					msgcenterRead({
						id: item.id
					}).then(res => {
						if (res.success) {
							location.href = baseHost + item.pcLink;
						}
					})
				} else {
					location.href = baseHost + item.pcLink;
				}
			},

			noticeData() {
				// 公告列表
				// this.$api.get(this, 'user/store/announcement/list')

				noticeList({
					page: 1,
					size: 500
				}).then((res) => {

					if (res.success) {
						this.noticeList = res.data.list.filter(item => item.releaseStatus == 1);
						console.log('公告列表', this.noticeList);
					}
				});
			},
			// 前往公告详情
			goNoticeDetail(id, index) {
				window.localStorage.removeItem('notice_id');
				window.localStorage.removeItem('notice_index');
				this.$router.push({
					name: 'NoticeDetail',
					query: {
						notice_id: id,
						notice_index: index
					}
				});
			},
			/**
			 * 施义煌
			 * 更换语种
			 */
			fLangChange(value) {
				window.localStorage.setItem('bLang', value);
				window.localStorage.setItem('currency', value.slice(3, 6));
				this.lang = value;
				this.$root.currency = value.slice(3, 6);
				this.$i18n.locale = value.split('_')[0];
				this.$i18n.curren = value.slice(3, 6);
				this.$emit('reloadFLang', value);
				this.isReload = true;
				location.reload();


			},
			// 获取汇率
			currencyRateFun() {
				let params = {
					cyForid: 'USD', //原币代码
					cyToid: 'CNY', //目标币代码
				}
				currencyRate(params).then(res => {
					if (res.success) {
						localStorage.setItem('exchange', res.data.reverseExRate);
					}
				})
			},

			shutLog() {
				this.showcov = false;
			},
			shutLogs() {
				this.showcovs = false;
			},
			// 云之印跳转链接
			goUrlOne() {
				window.open('http://www.uzien.com/h-col-121.html', '_blank');
			},
			goUrlTwo() {
				window.open('http://www.uzien.com/h-col-122.html', '_blank');
			},
			goUrlThr() {
				window.open('http://www.uzien.com/h-col-123.html', '_blank');
			},
			goUrlFor() {
				window.open('http://www.uzien.com/h-col-124.html', '_blank');
			},
			goUrlFiv() {
				window.open('http://www.uzien.com/h-col-109.html', '_blank');
			},
			goUrlSix() {
				window.open('http://www.uzien.com/advice.html', '_blank');
			},
			goAbout(type) {
				this.$parent.type = type;
				this.$router.push({
					name: 'About',
					query: {
						type: type
					}
				});
			},
			// 控制分类栏显隐
			navShows() {
				this.navShow = true;
				this.isEnter = true;

				// this.scrollH = this.$refs.classifyList.scrollHeight;
				// this.$refs.classifyList.style.height = this.scrollH + "px";
			},
			navHides() {
				this.navShow = false;
				this.isEnter = false;

				// 设置分类高度
				// this.activeItem = "";
				// this.$refs.classifyList.style.height = this.offsetH + "px";

				var secondsArea = 1; // 设置1秒
				this.intervalid = setInterval(() => {
					secondsArea--;
					if (secondsArea === 0) {
						this.classifyListTwo = [];
						clearInterval(this.intervalid);
					}
				}, 200);
			},
			gonCustom(type) {
				// 前往定制商品 type： 0普通 1diy
				this.$router.push({
					name: 'CustomProducts',
					query: {
						custom_type: type
					}
				});
			},
			// 前往新品
			goNewProduct() {
				this.$router.push({
					name: 'NewProduct',
					query: {
						custom_type: 3
					}
				});
			},
			navCustomShows() {
				this.showCustomColumn = true;
			},
			navCustomHides() {
				this.showCustomColumn = false;
			},
			navColumnShows(item) {
				if (item.isYunZhiYin === 1) {
					this.navColumnShow = true;
				}
			},
			navColumnHides(item) {
				if (item.isYunZhiYin === 1) {
					this.navColumnShow = false;
				}
			},
			// 所有分类商品
			goAllClassify() {
				this.$router.push({
					name: 'Classify',
					query: {
						classsity_id: '',
						classsity_name: '所有商品',
						classsity_name_en: 'All categories'
					}
				});
				if (this.classifyId) {
					location.reload();
				}
			},
			// 审核未通过
			notPass() {
				if (Number(this.capitalStatus) === 3) {
					this.showcov = true;
				}
			},
			// 前往会员中心
			goUserCenter() {
				if (this.freezeStatus === '2') {
					this.showcovs = true;
				} else {
					this.$router.push({
						name: 'OrderManage'
					});
				}
			},
			// 购物车
			goShopCart() {
				if (this.freezeStatus === '2') {
					this.showcovs = true;
				} else {
					this.$router.push({
						name: 'ShopCarts'
					});
				}
			},
			// 退出
			logOut() {
				let info = '';
				if (this.$i18n.locale === 'zh') {
					info = '此操作将退出, 是否继续?';
				} else {
					info = 'This operation will exit, continue or not';
				}
				this.$confirm(info, this.$t('P.Prompt'), {
						confirmButtonText: this.$t('P.OK'),
						cancelButtonText: this.$t('P.Cancel'),
						type: 'warning'
					})
					.then(() => {
						window.localStorage.removeItem('name');
						window.localStorage.removeItem('userId');
						window.localStorage.removeItem('userName');
						window.localStorage.removeItem('gradeId');
						window.localStorage.removeItem('capitalStatus');
						window.localStorage.removeItem('freezeStatus');
						window.sessionStorage.removeItem('hasShowPromotion')
						removeToken();
						this.$router.push({
							name: 'Login',
							params: {
								key: '-1'
							}
						});
					})
					.catch(() => {
						if (this.$i18n.locale === 'zh') {
							this.$message.info('已取消退出');
						} else {
							this.$message.info('Withdrawal canceled');
						}
					});
			},
			// 去登录页面
			logIn() {
				if (this.loginState === true) {
					this.$router.push({
						name: 'Login',
						query: {
							login_state: 1
						}
					}); // 被登出
				} else {
					this.$router.push({
						name: 'Login'
					});
				}
			},
			// 栏目列表-Y
			getColumnList() {
				columnList({
					page: 1,
					size: 300
				}).then((res) => {
					console.log(res.data);
					if (res.success) {
						this.columnList = res.data.list;
						console.log(this.columnList);
						if (this.columnList) {
							this.columnList.sort(this.theSort('sort')); // 按栏目从小到大排序
						}
					}
				});
			},
			theSort(property) {
				return function(a, b) {
					var value1 = a[property];
					var value2 = b[property];
					return value1 - value2;
				};
			},
			// 前往栏目商品列表
			goColumnGoods(id, index, title, titleEn) {
				this.$router.push({
					name: 'ColumnOne',
					query: {
						column_id: id,
						nav_index: index,
						title: title,
						titleEn: titleEn
					}
				});
				this.$emit('add', id);
				this.$emit('bdd', index);
			},
			// 分类--y
			getClassify() {
				classifyList().then((res) => {
					if (res.success) {
						this.classifyListOne = res.data;
						this.classifyListOne.sort(this.theSort('sort')); // 按分类从小到大排序
					}
				});
			},
			enter(item) {
				// classify.status = false;
				this.classifyListTwo = [];
				if (item.subClassifies) {
					this.classifyListTwo = item.subClassifies
				}

			},
			leave(classify) {
				// classify.status = true;
			},
			// 前往分类页
			goClassifyOne(item) {
				this.$router.push({
					name: 'Classify',
					query: {
						classsity_id: item.id,
						classsity_name: item.name,
						classsity_name_en: item.nameEn
					}
				});
			},
			goClassify(item) {
				this.$router.push({
					name: 'Classify',
					query: {
						classsity_id: item.id,
						classsity_name: item.name,
						classsity_name_en: item.nameEn
					}
				});
			},
			// 热门搜索列表
			hotSearchList() {
				this.$api.get(this, 'user/goods/search/list').then((res) => {
					if (res.code === 0) {
						this.searchList = res.list;
						if (this.searchList.length > 0) {
							this.searchList.forEach((item, index) => {
								if (item && index < 3) {
									this.searchHot += item.searchName + ' ';
								}
							});
						}
					}
				});
			},
			// 搜索内容-y
			goSearch() {
				if (this.searchContent.length < 2) {
					let info;
					if (this.$i18n.locale === 'zh') {
						info = '请输入两位字符以上的搜索项';
					} else {
						info = 'Please enter more than two characters of search text';
					}
					this.$message.warning(info)
					return;
				}
				this.$router.push({
					name: 'SearchPage',
					query: {
						search_name: this.searchContent
					}
				});
				window.localStorage.setItem('searchName', this.searchContent);

			},


			// 确认修改
			confirmModify() {
				this.$api.put(this, '/user/u/userStatus').then((res) => {
					if (res.code === 0) {
						if (this.$i18n.locale === 'zh') {
							this.$message.success('重新提交申请成功!');
						} else {
							this.$message.success('Submit application again successfully!');
						}
						this.showcov = false;
					}
				});
			},


			// 判断是否登录状态
			isLoginNot() {
				let id = localStorage.getItem('userId');
				userInfo({
					id: id
				}).then((res) => {
					if (res.success) {
						this.isLogin = true;
					} else {
						this.isLogin = false;
					}
				});
			},

			// 产品介绍 - 关闭
			closeProductIntro() {
				this.showIntroDialog = false;
			},
			// 提示登录 - 关闭
			closeLoginDialog() {
				this.showLoginDialog = false;
			},
			// 设置提示登录定时器
			setTimer(timer) {
				if (this.userId === '' || this.userId === null) {
					clearInterval(this.loginTimer);
					this.loginTimer = setInterval(() => {
						this.showLoginDialog = true;
					}, timer);
				}
			}
		},
		mounted() {


			if (!this.isReload) {
				this.showIndex = this.home;
				this.showCustom = this.custom;
				this.showActivitys = this.activitys;
				this.showDiscountActivity = this.discountActivity;
				this.showNewProduct = this.newProduct;
				this.showIndex = this.indexPage;
				this.showShopCar = this.shopCarPage;
				var id = window.localStorage.getItem('userId');
				var gid = window.localStorage.getItem('gradeId');
				var name = window.localStorage.getItem('name');
				var state = window.localStorage.getItem('capitalStatus');
				var fstate = window.localStorage.getItem('freezeStatus');
				var contents = window.localStorage.getItem('searchName');
				if (id && id != '' && id != 'undefined') {
					this.userId = id;
				}

				this.gradeId = gid;
				this.userName = name;
				this.capitalStatus = state;
				this.freezeStatus = fstate;
				if (this.searchType === 1) {
					this.searchContent = contents;
				}
				let value = window.localStorage.getItem('bLang') ?
					window.localStorage.getItem('bLang') :
					'zh_CNY';
				if (this.lang !== value) {
					this.lang = value;
					this.$root.currency = value.slice(3, 6);
					this.$i18n.locale = value.split('_')[0];
					this.$i18n.curren = value.slice(3, 6);
					this.$emit('reloadFLang', value);
					location.reload();
				} else {
					if (this.userId !== '' && this.userId !== null) {

						// 获取购物车商品数量--liu
						shoppingcartList().then(res => {
							if (res.success) {
								this.cartCount = res.data.length;
							}
						})

					}
					this.getColumnList();
					this.getClassify();
					// this.hotSearchList();
				}
				if ((this.userId === '' || this.userId === null) && sessionStorage.getItem('clearInterval') !==
					'loginTimer') {
					this.setTimer(300000);
				}
			} else {
				this.isReload = false;
			}
		}
	};
</script>
<style scoped="scoped" lang='less'>
	@import url("../assets/less/variable.less");

	// 消息中心
	.news-wrap.el-dropdown-menu {
		margin: 0;
		padding: 0;
		width: 400px;
		transform: translateX(0);

		/deep/ .popper__arrow::after {
			top: 4px;
			border-bottom-color: #F5F7FA;
		}
	}

	.news-box {
		background: #F5F7FA;
		border-radius: 8px;

		.news-btnAll {
			text-align: center;
			background: rgba(13, 184, 207, 0.1);
			cursor: pointer;
			height: 42px;
			line-height: 42px;
			width: 100%;
			font-size: 16px;
			border-bottom-left-radius: 8px;
			border-bottom-right-radius: 8px;
		}

		.news-content {
			max-height: 420px;
			text-align: left;
			overflow-y: auto;

			.news-line {
				padding: 24px 40px 18px;
				border-radius: 6px;

				&+.news-line {
					border-top: 1px solid #F1F1F1;
				}

				.news-status {
					position: relative;
					display: flex;
					align-items: center;
					justify-content: space-between;
					padding-bottom: 6px;

					.news-status-Lf {
						display: flex;
						align-items: center;
						font-size: 16px;

						.news-status-red {
							position: absolute;
							left: -18px;
							display: block;
							width: 6px;
							height: 6px;
							background: #ED5307;
							border-radius: 100%;
						}
					}

					.news-status-Rg {
						font-size: 14px;
						color: #0DB8CF;
						display: flex;
						align-items: center;
						cursor: pointer;

						span:nth-child(1) {
							margin-right: 5px;
						}
					}

				}

				.news-createTime {
					font-size: 12px;
					color: #999;
				}

				.news-info {
					padding-top: 15px;
					line-height: 28px;
					font-size: 14px;

					.news-infoList {
						position: relative;
						padding-left: 80px;

						.label {
							position: absolute;
							top: 0;
							left: 0;
							color: #666;
						}
					}
				}
			}
		}

		.no-news-data {
			padding: 30px;
			font-size: 16px;
			text-align: center;
		}
	}

	.border-zero {
		border: 0;
	}

	/*云之印栏目*/
	.nav-Column-service {
		position: absolute;
		top: 30px;
		left: 0;
		width: 130px;
		background-color: #585758;
		z-index: 999;
		margin-left: 124px;

		ul {
			li {
				clear: both;
				cursor: pointer;
				width: 130px;
				text-align: center;
			}

			li:hover {
				background-color: #414041 !important;
				padding-left: 0px;
				padding-right: 0px;

				span {
					font-size: 15px;
					color: #fff;
				}
			}
		}
	}

	// 公告
	@keyframes moveAni {
		0% {
			left: 0;
		}

		100% {
			left: -100%;
		}
	}

	.banner-notice {
		position: relative;
		width: 280px;
		height: 40px;
		line-height: 40px;

		.notice-box {
			position: absolute;
			top: 0;
			display: flex;
			display: -webkit-flex;
			flex-direction: row;
			flex-wrap: nowrap;
			width: 300px;
			padding: 0 20px 0 40px;
			box-sizing: border-box;

			.iconfont {
				display: inline-block;
				margin-right: 12px;
				font-size: 16px;
				color: @redLabel;
			}

			.notice-content {
				flex: 1;
				overflow: hidden;

				.item-ul {
					position: absolute;
					display: flex;
					display: -webkit-flex;
					flex-direction: row;
					width: 100%;
					top: 0;
					left: 0;
					white-space: nowrap;
					animation: moveAni 15s infinite linear normal;

					.item-text {
						margin-right: 12px;
						font-size: 14px;
						color: @redLabel;
						float: left;
						cursor: pointer;
					}
				}
			}
		}
	}

	.header {
		width: 100%;
		min-width: 1260px;
		z-index: 1;
		box-shadow: 2px 6px 14px 0px rgba(0, 0, 0, 0.06);

		.top-banner {
			width: 100%;
			height: 98px;
			background-color: #d3d4d6;
		}

		.top-content {
			width: 100%;
			background-color: #434343;
			height: 40px;
			line-height: 40px;

			.all {
				width: 1200px;
				height: 40px;
				font-size: 14px;
				overflow: hidden;

				.username {
					display: inline-block;
					max-width: 80px;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
				}

				.header-right {
					font-size: 14px;
				}

				.shopcart-box {
					font-size: 14px;

					.icon-shopcart {
						position: relative;
						display: inline-block;
						margin-right: 10px;
						font-size: 16px;
						color: #ffffff;

						.num {
							position: absolute;
							display: inline-block;
							left: 10px;
							top: 6px;
							line-height: 12px;
							padding: 1px 3px;
							text-align: center;
							font-size: 12px;
							color: #ffffff;
							background-color: #f16e7b;
							border-radius: 7px;
						}
					}
				}

				.newsNav-box {
					display: flex;
					align-items: center;
					margin-left: 20px;

					/deep/ .el-dropdown-link {
						font-size: 14px;
					}

					img {
						position: relative;
						top: 3px;
						width: 16px;
						height: 15px;
					}

					.newBox-text {
						margin-left: 10px;
					}

					.num {
						position: absolute;
						display: inline-block;
						left: 10px;
						top: 6px;
						line-height: 12px;
						padding: 1px 3px;
						text-align: center;
						font-size: 12px;
						color: #ffffff;
						background-color: #f16e7b;
						border-radius: 7px;
					}
				}
			}

			.outside {
				background: url(../../src/assets/images/shu-hao.png) no-repeat right center;
			}

			.phone {
				cursor: pointer;

				img {
					vertical-align: -2px;
				}

				background: url(../../src/assets/images/shu-hao.png) no-repeat right center;
			}
		}

		.head-content {
			width: 100%;
			height: 38px;
			padding: 18px 0;

			.all {
				width: 1200px;
			}

			.logo-img {
				display: inline-block;
				width: 99px;
				height: 25px;
				background: url("../../src/assets/images/index/logo.png") no-repeat right center;
				background-size: 100%;
				position: relative;
				top: 3px;
			}

			.nav-top-box {
				margin-top: 4px;
				margin-left: 38px;
				height: 26px;
				line-height: 26px;

				.nav-list {
					.nav-item {
						display: inline-block;
						font-size: 16px;
						color: #333333;
						cursor: pointer;

						&+.nav-item {
							margin-left: 20px;
						}

						&:hover {
							color: #ff770f;
						}

						a {
							&:hover {
								color: #ff770f;
							}
						}

						&.active {
							color: #ff770f;

							a {
								color: #ff770f;
							}
						}

						&.custom {
							padding: 0 14px;
							color: #ffffff;
							background-color: #f16e7b;
							border-radius: 100px;

							&:hover {
								background-color: rgba(241, 110, 123, 0.6);
							}
						}

						&.new {
							padding: 0 14px;
							color: #ffffff;
							background-color: #ff770f;
							border-radius: 100px;

							&:hover {
								background-color: #ff770f;
							}

							a,
							a:hover {
								color: #fff;
							}
						}
					}
				}
			}

			.logo-box {
				.input-box {
					width: 242px;
					height: 38px;
					padding: 0 0 0 20px;
					box-sizing: border-box;
					border-radius: 36px;
					border: 1px solid rgba(194, 194, 194, 1);

					input {
						width: 180px;
						height: 34px;
						line-height: 34px;
						float: left;
						/*background: url(../../src/assets/images/search.png) no-repeat top 7px right 10px;*/
					}

					.icon-search {
						display: inline-block;
						padding: 0 10px;
						line-height: 34px;
						color: #000000;
						font-size: 16px;
						cursor: pointer;
					}
				}

				.search {
					display: block;
					width: 76px;
					height: 30px;
					line-height: 30px;
					background-color: #0392a6;
				}
			}

			.search-cons {
				width: 440px;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;

				ul {
					li {
						display: inline-block;
						font-size: 12px;
						color: #fff;
						margin-right: 5px;
					}
				}
			}
		}

		.nav {
			width: 100%;
			line-height: 30px;
			background-color: #0392a6;

			.all {
				width: 1200px;
			}

			.classify {
				width: 220px;
				text-align: center;
			}

			.nav-list {
				max-width: 850px;
				height: 30px;
				z-index: 111;

				// overflow-x: auto;
				.scroll-content {
					width: auto;
					overflow-x: hidden;
					white-space: nowrap;
					z-index: 111;
				}

				li {
					padding-left: 15px;
					padding-right: 15px;
					// float: left;
					position: inherit;
					display: inline-block;

					a {
						display: block;
						width: 90px;
						height: 30px;
						line-height: 30px;
						color: #fff;
						text-align: center;
						font-size: 14px;
					}

					span {
						display: block;
						min-width: 90px;
						height: 30px;
						line-height: 30px;
						color: #fff;
						text-align: center;
						padding: 0 5px;
						cursor: pointer;
						font-size: 14px;
					}
				}

				li:hover {
					background-color: #00798a;
				}

				li.current {
					background-color: #00798a;
				}
			}

			.shop-car {
				img {
					vertical-align: -2px;
				}
			}

			.shop-car:hover {
				background-color: #00798a;
			}

			.nav-service {
				position: absolute;
				top: 10px;
				left: 0;
				height: 472px;

				ul {
					height: 100%;
					overflow-y: scroll;
					scrollbar-width: none;
					-ms-overflow-style: none;
					border-radius: 16px;
					background-color: #eef6fa;
					box-shadow: 0 -1px 10px 0 rgba(0, 0, 61, 0.06);
					z-index: 111;

					&.active {
						border-radius: 16px 0 0 16px;
					}

					&::-webkit-scrollbar {
						display: none;
					}

					li {
						padding-left: 24px;
						width: 196px;
						height: 42px;
						line-height: 42px;

						span {
							font-size: 16px;
							color: #666666;
						}

						.nav-box {
							height: 42px;

							span {
								display: inline-block;
								width: 169px;
								overflow: hidden;
								white-space: nowrap;
								text-overflow: ellipsis;
								cursor: pointer;
							}

							i {
								display: inline-block;
								float: right;
								margin-right: 10px;
								line-height: 42px;
								font-size: 16px;
								color: #000000;
								overflow: hidden;
							}
						}
					}

					li:first-child:hover {
						border-radius: 16px 0 0 0;
					}

					li:last-child:hover {
						border-radius: 0 0 0 16px;
					}

					li:hover,
					li.active {
						background-color: #ffffff;
					}
				}

				ul::-webkit-scrollbar {
					/*滚动条整体样式*/
					width: 0;
					/*高宽分别对应横竖滚动条的尺寸*/
					height: 9px;
				}

				ul::-webkit-scrollbar-thumb {
					/*滚动条里面小方块*/
					border-radius: 5px;
					-webkit-box-shadow: inset 0 0 5px #585758;
					background: rgba(0, 0, 0, 0.2);
				}

				ul::-webkit-scrollbar-track {
					/*滚动条里面轨道*/
					-webkit-box-shadow: inset 0 0 5px #585758;
					border-radius: 0;
					background: #585758;
				}

				.nav-ul {
					display: flex;
					display: -webkit-flex;
					position: absolute;
					top: 0;
					left: 220px;
					width: 478px;
					max-width: 717px;
					// max-height: 472px;
					height: 472px;
					-webkit-flex-float: row-reverse wrap;
					flex-flow: column;
					flex-wrap: wrap;
					padding: 20px 40px;
					box-sizing: border-box;
					border-radius: 0 16px 16px 0;
					z-index: 999;
					background: #fff;
					box-shadow: 8px 0 10px 0 rgba(0, 0, 61, 0.06);

					.nav-item {
						display: inline-block;
						width: 50%;
						margin-bottom: 6px;
						overflow: hidden;

						.nav-img {
							display: inline-block;
							width: 66px;
							height: 66px;
							vertical-align: middle;
						}

						.nav-text {
							display: inline-block;
							width: 100px;
							max-height: 66px;
							margin-right: 10px;
							margin-left: 12px;
							vertical-align: middle;
							text-overflow: ellipsis;
							overflow: hidden;
							line-clamp: 2;
							-webkit-line-clamp: 2;
							-webkit-box-orient: vertical;

							&:hover {
								color: #0db8cf;
							}

							&::before {
								/*content: '';*/
								/*display: inline-block;*/
								/*height: 100%;*/
								/*width:1%;*/
								/*vertical-align: middle;*/
							}
						}
					}

					.more {
						margin-top: 15px;
						margin-left: 72px;
						font-size: 14px;
						color: #666666;

						.icon-more {
							display: inline-block;
							margin-left: 6px;
							font-size: 12px;
							vertical-align: middle;
						}
					}
				}
			}
		}
	}

	/*弹框*/
	.cover {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: #000;
		z-index: 99;
		opacity: 0.5;
	}

	.pro-cover {
		padding-top: 40px;
		width: 470px;
		height: 300px;
		border: 1px solid #ccc;
		border-radius: 5px;
		z-index: 99;
		background: #fefefe;

		.img {
			width: 80px;
		}
	}

	.cover-box {
		box-sizing: border-box;
		position: fixed;
		top: 0;
		left: 0;
		bottom: 0;
		right: 0;
		margin: auto;
		z-index: 99;

		.shut {
			position: absolute;
			top: -8px;
			right: -8px;
			display: block;
			width: 18px;
			height: 18px;
			background: url("../assets/images/shut.png") no-repeat center center;
		}

		.address-info {
			width: 250px;

			.all {
				width: 100px;
				height: 30px;
				line-height: 30px;
				border-radius: 5px;
			}

			.right {
				height: 28px;
				line-height: 28px;
				border: 1px solid #ccc;
			}
		}

		.address-infos {
			width: 100px;

			.all {
				width: 100px;
				height: 30px;
				line-height: 30px;
				border-radius: 5px;
			}
		}
	}

	@media screen and (max-width: 1040px) {
		.header {
			width: 1024px;
		}
	}

	.lang-box {
		display: inline-block;
		margin-right: 100px;

		/deep/.el-input__inner {
			font-size: 14px;
			width: 130px;
			padding-left: 0;
			text-align: right;
			border: none;
			color: #afafaf;
			background: none;
		}
	}

	.el-select-dropdown {
		margin-top: 0;

		.el-select-dropdown__item {
			font-size: 14px;
		}
	}

	.el-dropdown-menu {
		display: inline-block;
		transform: translateX(50%);
		width: auto;
		padding: 10px 0;
		box-sizing: border-box;
		border-radius: 0 0 16px 16px;
		background-color: #ffffff;

		.el-dropdown-menu__item {
			display: inline-block;
			width: 100%;
			padding: 0 15px;
			box-sizing: border-box;
			color: #333333;
			cursor: pointer;
			background-color: #ffffff !important;

			&:hover,
			&.active span {
				color: #1bb8ce !important;
			}

			span {
				display: block;
				font-size: 16px !important;
				color: #333333 !important;
				white-space: nowrap;

				&:hover {
					color: #1bb8ce !important;
				}
			}
		}
	}

	// 导航 下拉菜单
	.el-dropdown-link {
		font-size: 18px;
	}

	.el-dropdown-menu {
		text-align: center;
		box-shadow: 0 6px 14px 0px rgba(0, 0, 0, 0.06);
	}

	.rl-text-lightWhite {
		color: #afafaf;
	}

	.nav-item-en14 {
		font-size: 14px !important;
	}

	.nav-item-en13 {
		font-size: 13px !important;
	}
</style>
