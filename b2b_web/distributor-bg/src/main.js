import Vue from 'vue'
import 'normalize.css/normalize.css'// A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import { MessageBox } from 'element-ui'
// 适配ie
require('es6-promise').polyfill()
import '@/styles/index.scss' // global css
import App from './App'
import router from './router'
import store from './store'
import api from './api/api'
import service from './api/service'
import { isNumber } from './utils/common'

Vue.prototype.$api = api
Vue.prototype.$http = service

import Viewer from 'v-viewer'
Vue.use(Viewer)
Viewer.setDefaults({
  Options: {
    'inline': true, // 启用 inline 模式
    'button': true, // 显示右上角关闭按钮
    'navbar': true, // 显示缩略图导航
    'title': true, // 显示当前图片的标题
    'toolbar': true, // 显示工具栏
    'tooltip': true, // 显示缩放百分比
    'movable': true, // 图片是否可移动
    'zoomable': true, // 图片是否可缩放
    'rotatable': true, // 图片是否可旋转
    'scalable': true, // 图片是否可翻转
    'transition': true, // 使用 CSS3 过度
    'fullscreen': true, // 播放时是否全屏
    'keyboard': true, // 是否支持键盘
    'url': 'data-source' // 设置大图片的 url
  }
})
import 'viewerjs/dist/viewer.css'

import '@/icons' // icon
import '@/permission' // permission control
// 引入插件
Vue.use(ElementUI)
// 关闭生产模式的提示
import { splitnum } from '@/utils/index'
// eslint-disable-next-line no-extend-native
String.prototype.splitnum = splitnum

window.addEventListener('offline', function() { // 是否联网系统资源监听
  MessageBox.confirm('网络异常，请检查网络后重新操作。', {
    confirmButtonText: '好的',
    cancelButtonText: '取消',
    showClose: false,
    showCancelButton: false, // 取消按钮隐藏
    closeOnClickModal: false, // 禁止点击遮罩层关闭提示
    type: 'warning'
  }).then(() => { })
  // eslint-disable-next-line quotes
  localStorage.setItem('offlineTemp', false)
})

window.addEventListener('online', function() {
  localStorage.setItem('offlineTemp', true)
})

Vue.filter('NumFormat', function(value) {
  if (!isNumber(value)) return '-'
  if (!value) return '0.00'
  var intPart = parseInt(value) // 获取整数部分
  value = value.toString()
  // intPart = intPart.substring(0, intPart.lastIndexOf('.'))
  var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
  var floatPart = '.00' // 预定义小数部分
  var value2Array = value.split('.')
  // =2表示数据有小数位
  if (value2Array.length === 2) {
    floatPart = value2Array[1].toString() // 拿到小数部分
    if (floatPart.length === 1) { // 补0,实际上用不着
      return intPartFormat + '.' + floatPart + '0'
    } else {
      return intPartFormat + '.' + floatPart
    }
  } else {
    return intPartFormat + floatPart
  }
})

Vue.config.productionTip = false
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})