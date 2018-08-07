import {getCartList} from '@/api/cart'

const state = {
  cartList: {},
  cartCount: 0 // 购物车商品数量
}

const mutations = {
  setcartList (state, data) {
    for (let i = 0; i < data.productResponses.length; i++) {
      data.productResponses[i].checked = 0
    }
    state.cartList = data
  },

  // 更新购物车数量
  setCartCount (state, data) {
    state.cartCount = data;
  }
}

export default state
