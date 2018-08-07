import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // 验权

const whiteList = ['/login'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  NProgress.start() // 进度条启动
  if (getToken()) { // 有token，已登录状态
    if (to.path === '/login') {
      next()
    } else {
      if (JSON.stringify(store.getters.userinfo) === '{}') {
        store.dispatch('GetInfo').then(res => { // 若本地无用户信息，则拉取用户信息
          next()
        }).catch(() => {
          store.dispatch('FedLogOut').then(() => {
            Message.error('验证失败,请重新登录')
            next({ path: '/login' })
            // next()
          })
        })
      } else {
        next()
      }
    }
  } else { // 没token
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
