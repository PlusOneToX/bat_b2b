import api from '@/api/api'
import url from '@/api/allUrl.js'

const distributor = {
  state: {
    saleareas: [],
    distributors: []
  },
  mutations: {
    WRITE_SALEAREAS: (state, payLoad) => {
      state.saleareas = payLoad
    },
    GET_DISTRIBUTORS: (state, info) => {
      state.distributors = info
    }
  },
  actions: {
    updateSaleareas({
      commit
    }, payLoad) {
      // return api.get(null, 'admin/u/p/distributor/salearea/list', {
      return api.get(null, url.getSalesareaList, {
        page: 1,
        size: 10000
      }).then(res => {
        commit('WRITE_SALEAREAS', res.data.list)
      })
    },
    updateDistributors({
      commit
    }, payLoad) {
      // return api.get(null, 'admin/u/po/distributor/list').then(res => {
      return api.get(null, url.getDistributorPoList, { page: 1, size: 10000, freezeStatus: 1, profileStatus: 2 }).then(res => {
        commit('GET_DISTRIBUTORS', res.data.list)
      })
    }
  }
}

export default distributor
