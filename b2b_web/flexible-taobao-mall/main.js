import Vue from 'vue'
import App from './App'
//引入vuex
import store from './store'
//把vuex定义成全局组件
Vue.prototype.$store = store
// 图标
import "./static/common/css/icon.css";
Vue.config.productionTip = false
//uview 组件
import uView from 'uview-ui';
Vue.use(uView);

App.mpType = 'app'

//#ifdef H5
import Vconsole from 'vconsole';
new Vconsole();
//#endif
const app = new Vue({
    ...App
})
app.$mount()
