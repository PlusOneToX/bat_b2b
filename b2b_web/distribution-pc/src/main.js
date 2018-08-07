// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import router from './router'
import VueRouter from 'vue-router'
import store from './store'
import api from '@/api/api'
import 'es6-promise/auto'
import 'babel-polyfill'
import b2bConfig from '@/common/b2bConfig/index' // 施义煌 - b2b配置信息
import VueI18n from 'vue-i18n' // 施义煌 - 多语种
import langEn from '../static/lang/en' // 施义煌 - 多语种
import langZh from '../static/lang/zh' // 施义煌 - 多语种
import elementEn from 'element-ui/lib/locale/lang/en' // 施义煌 - 多语种
import elementZh from 'element-ui/lib/locale/lang/zh-CN' // 施义煌 - 多语种
import ElementLocale from 'element-ui/lib/locale' // 施义煌 - 多语种
import vcolorpricker from 'vcolorpicker' // 取色（IE 11 报错，页面空白）
import VueQriously from 'vue-qriously'


Vue.prototype.$b2bConfig = b2bConfig // 施义煌 - b2b配置信息
Vue.prototype.$api = api
Vue.prototype.$loadingProgress = null // 下载进度条
Vue.prototype.$AppID = '3893019696' // 正式服:3893019696 测试服：5652338659
Vue.prototype.$AppKey = 'ac7c381094a149a48be7b6c1a826c74b' // 正式服:ac7c381094a149a48be7b6c1a826c74b 测试服：14b94847d9744cf6988526fcac9b6107
Vue.config.productionTip = false
Vue.use(ElementUI, { elementEn, elementZh })
Vue.use(router)
Vue.use(VueRouter)
Vue.use(VueI18n) // 施义煌 - 多语种
Vue.use(vcolorpricker)
Vue.use(VueQriously)

let hostUrl = "https://api.bat.com/"; //正式服
//let hostUrl = "https://test.bat.com/"; //测试服
if (process.env.NODE_ENV == "development") {
    hostUrl = "https://api.bat.com/";
}
Vue.prototype.locationUrl_L = hostUrl;
/**
 * 判断是否有默认语言，如果没有则设置中文为默认语言
 */
if (!window.localStorage.getItem('bLang')) { // 如果未缓存语言情况
    if (navigator.language === undefined || navigator.language.indexOf("zh") != -1) {
        window.localStorage.setItem('bLang', 'zh_CNY');
    } else {
        window.localStorage.setItem('bLang', 'en_USD');
    }
}
if (!window.localStorage.getItem('currency')) { // 如果未缓存语言情况
    if (navigator.language === undefined || navigator.language.indexOf("zh") != -1) {
        window.localStorage.setItem('currency', 'CNY');
    } else {
        window.localStorage.setItem('currency', 'USD');
    }
}

// if (!window.localStorage.getItem('bLang')) {
//     window.localStorage.setItem('bLang', 'en_USD');
// }
// if (!window.localStorage.getItem('currency')) {
//     window.localStorage.setItem('currency', 'USD');
// }

// alert(navigator.language);
// const locationUrl_L='http://127.0.0.1:8083/';
// const locationUrl_L='https://api.bat.com/';
const i18n = new VueI18n({
    locale: localStorage.getItem('bLang').split('_')[0] || 'zh',
    // locale: localStorage.getItem('bLang').split('_')[0] || 'en',
    messages: {
        'en': {
            ...langEn,
            ...elementEn
        },
        'zh': {
            ...langZh,
            ...elementZh
        }
    }
})
ElementLocale.i18n((key, value) => i18n.t(key, value))
/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    i18n, //  - 多语种
    components: { App },
    template: '<App/>',
    data: {
        token: '',
        currency: 'CNY'
    },
    // data: {
    //     token: '',
    //     currency: 'USD'
    // },
    methods: {},
    beforeMount() {
        this.token = window.sessionStorage.getItem('token')
        if (localStorage.getItem('currency')) {
            this.currency = localStorage.getItem('currency')
        }
    }
})
router.beforeEach(function (to, from, next) {
    window.scrollTo(0, 0)
    next()
})
    // window.onunload = function () { // 浏览器关闭清除Storage缓存
    // localStorage.clear()
    //  }
