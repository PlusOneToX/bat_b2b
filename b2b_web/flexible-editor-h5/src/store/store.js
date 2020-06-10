/*
 * @Author: yaowei
 * @Date: 2019-09-30 10:19:00
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-04 08:29:13
 */
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
const state = {
  isLoading: false,
}

const getters = {}

const actions = {}

const mutations = {
  showLoading(state) {
    state.isLoading = true
  },
  hideLoading(state) {
    state.isLoading = false
  },
}
/* eslint-disable */
export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})
