// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import api from './api/api'

// rem自适应
import 'lib-flexible/flexible.js'
import 'babel-polyfill'
import 'common/styles/index.styl'

import './assets/fonts/iconfont.css'


/* vConsole */
import vConsole from 'vconsole';
if (process.env.NODE_ENV !== 'production') {
  // 非正式包，显示vConsole
  new vConsole();
}

/* eslint-disable */
Vue.prototype.$api = api
Vue.config.productionTip = false
Vue.use(vant.Lazyload, {
  lazyComponent: true
})

// 微信分享
import wxShare from './common/js/wxShare.js'
router.afterEach((to, from) => {
  setTimeout(() => {
    wxShare()
  }, 500);
})


router.beforeEach((to, from, next) => {
  store.commit('SET_TOKEN', localStorage.getItem('auth'))
  if (store.state.user.auth) {
    store.commit('SET_STATUS', 1)
  }
  next();
})

router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  const targetPath = router.history.pending.fullPath
  if (isChunkLoadFailed) {
    router.replace(targetPath)
  }
})


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
})
