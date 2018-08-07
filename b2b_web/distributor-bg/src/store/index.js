/*
 * @Author: yaowei
 * @Date: 2018-05-23 19:48:32
 * @LastEditors: yaowei
 * @LastEditTime: 2018-03-15 15:00:48
 */
import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import system from './modules/system'
import goods from './modules/goods'
import order from './modules/order'
import distributor from './modules/distributor'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import getters from './getters'
// import eltabpane from './modules/eltabpane'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    system,
    goods,
    order,
    distributor,
    tagsView,
    permission
    // eltabpane
  },
  getters,
  strict: false // vuex提示开关，生产模式下要关闭
})

export default store
