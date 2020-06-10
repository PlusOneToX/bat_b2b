import Router from 'vue-router'

export default new Router({
  routes: [
    /**
     * 默认主页
     */
    {
      path: '/',
      redirect: '/phone',
      component: () => import('views/phone/index')
    },
    { // 定制 - 移动版
      path: '/phone',
      component: () => import('views/phone/index'),
      meta: {
        keepAlive: true
      }
    },
    { // 预览
      path: '/preview',
      component: () => import('views/preview/index'),
      meta: {
        keepAlive: false
      }
    },
    { // 定制 - PC版
      path: '/webCustom',
      component: () => import('views/webCustom/index'),
      meta: {
        keepAlive: false
      }
    },
  ]
})
