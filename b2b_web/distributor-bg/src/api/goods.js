/*
* @Author: 陈良顺
* @Date:   2018-03-23 13:36:45
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2018-04-22 14:22:13
*/

// eslint-disable-next-line no-unused-vars
import request from '@/utils/request'

export function getGoodsList(vue, param) {
  return vue.$api.get(vue, 'admin/u/p/goods/list', param)
}

export function getGoodsTotal(vue) {
  return vue.$api.get(vue, 'admin/u/p/goods/count')
}

export function getGoodsInfo(vue, ids) {
  return vue.$api.get(vue, 'admin/u/po/goods/ids', ids)
}
