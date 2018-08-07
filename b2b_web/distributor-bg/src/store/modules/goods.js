/*
* @Author: 陈良顺
* @Date:   2018-03-23 10:00:55
* @Last Modified by:   陈良顺
* @Last Modified time: 2018-04-03 14:02:21
*/

const goods = {
  state: {
    specification: ''
  },
  mutations: {
    GET_SPECIFICATION: (state, info) => {
      state.specification = info
    }
  },
  actions: {
    getSpecification({ commit }, info) {
      commit('GET_SPECIFICATION', info)
    }
  }
}

export default goods
