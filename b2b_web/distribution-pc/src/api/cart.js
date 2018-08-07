import request from '@/utils/request'

export function getCartNum () {
  return request({
    url: 'user/u/marketing/promotion/getShoppingCart/num',
    method: 'get',
    data: {}
  })
}
