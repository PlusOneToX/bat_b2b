import Vue from 'vue'
import { getOrderDetail } from '@/views/order/orderData'

const order = {
  state: {
    orderId: '',
    orderDetail: {}
    // distributions: []
  },
  mutations: { // 更改值的方式方法
    UPDATE_ORDERID: (state, info) => { // info传给orderId的值
      state.orderId = info
    },
    UPDATE_ORDER_DETAIL: (state, info) => {
      state.orderDetail = info
    },
    UPDATE_DISTR: (state, info) => {
      state.distributions = info
    }
  },
  actions: {
    // 更新Id自动触发更新详情
    updateOrderId({ commit }, info) {
      // 更新Id
      commit('UPDATE_ORDERID', info) // commit调用更改的方法
      // 更新详情
      return getOrderDetail(new Vue(), { id: this.state.order.orderId })
        .then(res => commit('UPDATE_ORDER_DETAIL', res.order))
    }
    // updateDistributions({ commit }, info) {
    //   commit('UPDATE_DISTR', info)
    // }
  }
}

export default order
