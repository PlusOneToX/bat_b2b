// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import api from './api/api'

/* vConsole */
import vConsole from 'vconsole';
if (process.env.NODE_ENV !== 'production') {
  // 非正式包，显示vConsole
  new vConsole();
}

// rem自适应
import 'lib-flexible/flexible.js'
import 'babel-polyfill'
import 'common/styles/index.styl'

/* eslint-disable */
Vue.prototype.$api = api
Vue.config.productionTip = false

Vue.use(vant.Lazyload, {
  lazyComponent: true
})

router.beforeEach((to, from, next) => {
  store.commit('SET_TOKEN', localStorage.getItem('auth'))
  if (store.state.user.auth) {
    store.commit('SET_STATUS', 1)
  }

  // 设置页面 title
  if (to.meta.title) {
    document.title = to.meta.title;
  }

  if (to.meta.requireAuth) {
    if (to.query.auth) {
      localStorage.setItem('auth', to.query.auth)
    }
    next()
  } else {
    next()
  }
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
