import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

export default new Router({
    // base: '/proscenium/',
    // 去除#号  mode: 'history',
    routes: [
        {
            path: '/',
            redirect: '/Index',
        },
        {
            path: '/Login',
            name: 'Login',
            title: '登录',
            component: () =>
                import('@/view/loginRegister/Login.vue')
        },
        {
            path: '/Register',
            name: 'Register',
            title: '注册',
            component: () =>
                import('@/view/loginRegister/Register.vue')
        },
        {
            path: '/VerifyMobile',
            name: 'VerifyMobile',
            title: '注册--验证手机号',
            component: () =>
                import('@/view/loginRegister/VerifyMobile.vue')
        },
        {
            path: '/RegisterSuccess',
            name: 'RegisterSuccess',
            title: '注册成功',
            component: () =>
                import('@/view/loginRegister/RegisterSuccess.vue')
        },
        {
            path: '/Retrieval',
            name: 'Retrieval',
            title: '找回密码',
            component: () =>
                import('@/view/loginRegister/Retrieval.vue')
        },
        {
            path: '/ResetPassword',
            name: 'ResetPassword',
            title: '重置密码',
            component: () =>
                import('@/view/loginRegister/ResetPassword.vue')
        },
        {
            path: '/Index',
            name: 'Index',
            title: '首页',
            component: () =>
                import('@/view/Index.vue')
        },
        {
            path: '/UserCenter',
            name: 'UserCenter',
            title: '会员中心首页',
            component: () =>
                import('@/view/userCenter/UserCenter.vue')
        },
        {
            path: '/UserInfos',
            name: 'UserInfos',
            title: '个人信息',
            component: () =>
                import('@/view/userCenter/UserInfos.vue')
        },
        {
            path: '/AccountManage',
            name: 'AccountManage',
            title: '账号管理',
            component: () =>
                import('@/view/userCenter/AccountManage.vue')
        },
        {
            path: '/WantBook',
            name: 'WantBook',
            title: '缺货登记',
            component: () =>
                import('@/view/userCenter/WantBook.vue')
        },
        {
            path: '/Quotation',
            name: 'Quotation',
            title: '报价单',
            component: () =>
                import('@/view/userCenter/Quotation.vue')
        },
        {
            path: '/BatchImport',
            name: 'BatchImport',
            title: '批量导入',
            component: () =>
                import('@/view/userCenter/BatchImport.vue')
        },
        {
            path: '/batchChannelDetail',
            name: 'batchChannelDetail',
            title: '订单详情',
            component: () =>
                import('@/view/userCenter/batchChannelDetail.vue')
        },
        {
            path: '/ImportOrder',
            name: 'ImportOrder',
            title: '导入订单',
            component: () =>
                import('@/view/userCenter/ImportOrder.vue')
        },
        {
            path: '/Withdrawal',
            name: 'Withdrawal',
            title: '提现',
            component: () =>
                import('@/view/userCenter/Withdrawal.vue')
        },
        {
            path: '/Recharge',
            name: 'Recharge',
            title: '充值',
            component: () =>
                import('@/view/userCenter/Recharge.vue')
        },
        {
            path: '/SubAccount',
            name: 'SubAccount',
            title: '分账记录',
            component: () =>
                import('@/view/userCenter/SubAccount.vue')
        },
        {
            path: '/MoneyDetails',
            name: 'MoneyDetails',
            title: '资金管理',
            component: () =>
                import('@/view/userCenter/MoneyDetails.vue')
        },
        {
            path: '/Voucher',
            name: 'Voucher',
            title: '代金券',
            component: () =>
                import('@/view/userCenter/Voucher.vue')
        },
        {
            path: '/StoreManage',
            name: 'StoreManage',
            title: '店铺管理',
            component: () =>
                import('@/view/userCenter/StoreManage.vue')
        },
        {
            path: '/StoreAdd',
            name: 'StoreAdd',
            title: '新增店铺',
            component: () =>
                import('@/view/userCenter/StoreAdd.vue')
        },
        {
            path: '/StoreOrder',
            name: 'StoreOrder',
            title: '店铺订单',
            component: () =>
                import('@/view/userCenter/StoreOrder.vue')
        },
        {
            path: '/SubAccountConfig',
            name: 'SubAccountConfig',
            title: '分账配置',
            component: () =>
                import('@/view/userCenter/SubAccountConfig.vue')
        },
        {
            path: '/SubAccountSalesman',
            name: 'SubAccountSalesman',
            title: '分账业务员',
            component: () =>
                import('@/view/userCenter/SubAccountSalesman.vue')
        },
        {
            path: '/Coupons',
            name: 'Coupons',
            title: '优惠券',
            component: () =>
                import('@/view/userCenter/Coupons.vue')
        },
        {
            path: '/distributorsList',
            name: 'distributorsList',
            title: '分销管理',
            component: () =>
                import('@/view/userCenter/distributorsList.vue')
        },
        {
            path: '/distributorsOrder',
            name: 'distributorsOrder',
            title: '分销订单',
            component: () =>
                import('@/view/userCenter/distributorsOrder.vue')
        },
        {
            path: '/distributorsOrderDetail',
            name: 'distributorsOrderDetail',
            title: '分销订单详情',
            component: () =>
                import('@/view/userCenter/distributorsOrderDetail.vue')
        },
        {
            path: '/priceLevel',
            name: 'priceLevel',
            title: '价格等级',
            component: () =>
                import('@/view/userCenter/priceLevel.vue')
        },
        {
            path: '/priceLevelDetail',
            name: 'priceLevelDetail',
            title: '价格等级详情',
            component: () =>
                import('@/view/userCenter/priceLevelDetail.vue')
        },
        {
            path: '/OrderManage',
            name: 'OrderManage',
            title: '订单管理',
            component: () =>
                import('@/view/userCenter/OrderManage.vue')
        },
        {
            path: '/Invoice',
            name: 'Invoice',
            title: '发票',
            component: () =>
                import('@/view/userCenter/Invoice.vue')
        },
        {
            path: '/InvoiceDetail',
            name: 'InvoiceDetail',
            title: '发票详情',
            component: () =>
                import('@/view/userCenter/InvoiceDetail.vue')
        },
        {
            path: '/Statements',
            name: 'Statements',
            title: '对账单',
            component: () =>
                import('@/view/userCenter/Statements.vue')
        },
        {
            path: '/StatementsDetail',
            name: 'StatementsDetail',
            title: '对账单详情',
            component: () =>
                import('@/view/userCenter/StatementsDetail.vue')
        },
        {
            path: '/MyCustom',
            name: 'MyCustom',
            title: '我的收藏',
            component: () =>
                import('@/view/userCenter/MyCustom.vue')
        },
        {
            path: '/MyCollect',
            name: 'MyCollect',
            title: '我的DIY作品',
            component: () =>
                import('@/view/userCenter/MyCollect.vue')
        },
        {
            path: '/Address',
            name: 'Address',
            title: '收货地址',
            component: () =>
                import('@/view/userCenter/Address.vue')
        },
        {
            path: '/AddressModify',
            name: 'AddressModify',
            title: '收货地址修改',
            component: () =>
                import('@/view/userCenter/AddressModify.vue')
        },
        {
            path: '/AfterApplication',
            name: 'AfterApplication',
            title: '售后申请',
            component: () =>
                import('@/view/userCenter/AfterApplication.vue')
        },
        {
            path: '/AfterApplicationDetail',
            name: 'AfterApplicationDetail',
            title: '售后申请详情',
            component: () =>
                import('@/view/userCenter/AfterApplicationDetail.vue')
        },
        {
            path: '/ModifyPassword',
            name: 'ModifyPassword',
            title: '修改密码',
            component: () =>
                import('@/view/userCenter/ModifyPassword.vue')
        },
        {
            path: '/ShopCar',
            name: 'ShopCar',
            title: '购物车',
            component: () =>
                import('@/view/ShopCar.vue')
        },
        {
            path: '/ExchangeScore',
            name: 'ExchangeScore',
            title: '积分兑换',
            component: () =>
                import('@/view/ExchangeScore.vue')
        },
        {
            path: '/ConsigneeInfor',
            name: 'ConsigneeInfor',
            title: '收货人信息',
            component: () =>
                import('@/view/ConsigneeInfor.vue')
        },
        {
            path: '/WaitPayment',
            name: 'WaitPayment',
            title: '待付款',
            component: () =>
                import('@/view/WaitPayment.vue')
        },
        {
            path: '/Information',
            name: 'Information',
            title: '资讯',
            component: () =>
                import('@/view/Information.vue')
        },
        {
            path: '/Activity',
            name: 'Activity',
            title: '活动',
            component: () =>
                import('@/view/Activity.vue')
        },
        {
            path: '/PieceTogether',
            name: 'PieceTogether',
            title: '活动-凑单',
            component: () =>
                import('@/view/PieceTogether.vue')
        },
        {
            path: '/Classify',
            name: 'Classify',
            title: '分类页',
            component: () =>
                import('@/view/Classify.vue')
        },
        {
            path: '/ShopDetail',
            name: 'ShopDetail',
            title: '商品详情',
            component: () =>
                import('@/view/ShopDetail.vue')
        },
        {
            path: '/ColumnOne',
            name: 'ColumnOne',
            title: '栏目一',
            component: () =>
                import('@/view/ColumnOne.vue')
        },
        {
            path: '/ColumnTwo',
            name: 'ColumnTwo',
            title: '栏目二',
            component: () =>
                import('@/view/ColumnTwo.vue')
        },
        {
            path: '/SectionCate',
            name: 'SectionCate',
            title: '版块更多',
            component: () =>
                import('@/view/SectionCate.vue')
        },
        {
            path: '/HelpCenter',
            name: 'HelpCenter',
            title: '帮助中心',
            component: () =>
                import('@/view/HelpCenter.vue')
        },
        {
            path: '/NoticeList',
            name: 'NoticeList',
            title: '公告列表',
            component: () =>
                import('@/view/NoticeList.vue')
        },
        {
            path: '/NoticeDetail',
            name: 'NoticeDetail',
            title: '公告详情',
            meta: { keepAlive: false },
            component: () =>
                import('@/view/NoticeDetail.vue')
        },
        {
            path: '/SearchPage',
            name: 'SearchPage',
            title: '搜索页面',
            component: () =>
                import('@/view/SearchPage.vue')
        },
        {
            path: '/CustomProducts',
            name: 'CustomProducts',
            title: '定制商品',
            component: () =>
                import('@/view/CustomProducts.vue')
        },
        {
            path: '/DiscountActivity',
            name: 'DiscountActivity',
            title: '折扣活动',
            component: () =>
                import('@/view/DiscountActivity.vue')
        },
        {
            path: '/OrderSuccess',
            name: 'OrderSuccess',
            title: '下单成功',
            component: () =>
                import('@/view/OrderSuccess.vue')
        },
        {
            path: '/diyPage',
            name: 'diyPage',
            title: '下单成功',
            component: () =>
                import('@/view/diyPage.vue')
        },
        {
            path: '/customizePage',
            name: 'customizePage',
            title: '下单成功',
            component: () =>
                import('@/view/customizePage.vue')
        },
        {
            path: '/ActivityColumn',
            name: 'ActivityColumn',
            title: '活动栏目',
            component: () =>
                import('@/view/ActivityColumn.vue')
        },
        {
            path: '/NewProduct',
            name: 'NewProduct',
            title: '新品',
            component: () =>
                import('@/view/NewProduct.vue')
        },
        {
            path: '/ShopCarts',
            name: 'ShopCarts',
            title: '购物车',
            component: () =>
                import('@/view/ShopCarts.vue')
        },
        {
            path: '/Customize',
            name: 'Customize',
            title: '个性定制',
            component: () =>
                import('@/view/Customize.vue')
        },
        {
            path: '/CustomizeDiy',
            name: 'CustomizeDiy',
            title: 'DIY 个性定制',
            component: () =>
                import('@/view/customizeDiy.vue')
        },
        {
            path: '/Training',
            name: 'Training',
            title: '培训中心',
            component: () =>
                import('@/view/userSelfService/Training.vue')
        },
        {
            path: '/Download',
            name: 'Download',
            title: '下载中心',
            component: () =>
                import('@/view/userSelfService/Download.vue')
        },
        {
            path: '/GroupBuying',
            name: 'GroupBuying',
            title: '拼团尝鲜',
            component: () =>
                import('@/view/GroupBuyingN.vue')
        },
        {
            path: '/About',
            name: 'About',
            title: '关于我们',
            component: () =>
                import('@/view/companyInfo/About.vue')
        },
        {
            path: '/QuickOrder',
            name: 'QuickOrder',
            title: '快速订货',
            component: () =>
                import('@/view/QuickOrder.vue')
        },
        {
            path: '/mobileDiy',
            name: 'MobileDiy',
            title: '快速订货',
            component: () =>
                import('@/view/mobileDiy/index.vue')
        },
    ]
})
