/*
 * @Author: yaowei
 * @Date: 2019-12-11 10:45:00
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-11 11:14:51
 */
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// import Vue from 'vue'
import App from './App'
import router from './router'
// import VueRouter from 'vue-router'
import api from './api/api'
import axios from 'axios'
import store from './store/store'
import AwesomePicker from 'vue-awesome-picker'
import VueLazyload from 'vue-lazyload'

import {
  sync
} from 'vuex-router-sync'
import 'common/stylus/reset.styl'

/* vConsole */
import vConsole from 'vconsole';
if (process.env.NODE_ENV !== 'production') {
  // 非正式包，显示vConsole
  new vConsole();
}

/* eslint-disable */
Vue.prototype.$axios = axios
Vue.prototype.$api = api
Vue.config.productionTip = false
sync(store, router)
Vue.use(AwesomePicker)
Vue.use(VueLazyload, {
  loading: require('common/images/default.png')
})


new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
  // render: h => h(App)
})
