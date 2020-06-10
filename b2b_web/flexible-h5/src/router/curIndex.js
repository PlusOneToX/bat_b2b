/*
 * @Author: yaowei
 * @Date: 2019-11-16 14:44:20
 * @LastEditors: yaowei
 * @LastEditTime: 2019-01-05 14:19:43
 */

export default [{ // 定制
  path: '/recommend',
  name: 'recommend',
  component: () => import('view/recommend/recommend'),
  meta: {
    keepAlive: false,
    requireAuth: false
  }
}, { // 系列展示
  path: '/category',
  name: 'category',
  component: () => import('view/category/category'),
  meta: {
    keepAlive: false,
    requireAuth: false
  }
}, { // 选择型号
  path: '/model',
  name: 'model',
  component: () => import('view/model/model'),
  meta: {
    keepAlive: false,
    requireAuth: false
  }
}, { // 选择材质
  path: '/material',
  name: 'material',
  component: () => import('view/material/material'),
  meta: {
    keepAlive: false,
    requireAuth: false
  }
}, { // 预览
  path: '/previewer',
  name: 'previewer',
  component: () => import('view/preview/preview'),
  meta: {
    keepAlive: false,
    requireAuth: false
  }
}, { // 我的
  path: '/mine',
  name: 'mine',
  component: () => import('view/mine/mine'),
  meta: {
    keepAlive: false,
    requireAuth: false
  }
}, { // 定制
  path: '/custom',
  name: 'custom',
  component: () => import('view/custom/custom'),
  meta: {
    keepAlive: true,
    requireAuth: false
  }
}, { // 业务员绑定
  path: '/bindSaleman',
  name: 'bindSaleman',
  component: () => import('view/bindSaleman/bindSaleman'),
  meta: {
    keepAlive: true,
    requireAuth: false
  }
}, { // 业务员绑定成功
  path: '/bindSuccess',
  name: 'bindSuccess',
  component: () => import('view/bindSaleman/bindSuccess'),
  meta: {
    keepAlive: true,
    requireAuth: false
  }
}]
