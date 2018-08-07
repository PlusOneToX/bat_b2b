/*
 * @Author: 陈良顺
 * @Date:   2018-03-31 09:42:07
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-04-We 06:59:51
 */
import api from '@/api/allApi'

export function getBrandList(vue, params) {
  const a = vue.$api.get(vue, api.getBrandList, params).then(res => {
    if (res.code === 0) {
      return res.brands
    }
  })
  const b = vue.$api.get(vue, 'admin/u/p/brand/count').then(res => {
    if (res.code === 0) {
      return res.count
    }
  })
  return Promise.all([a, b]).then(res => {
    return {
      tableData: res[0],
      total: res[1]
    }
  })
}

export function getBrandPoList(vue) {
  const a = vue.$api.get(vue, api.getBrandPoList, { page: 1, count: 10000 }).then(res => {
    if (res.code === 0) {
      return res.brands
    }
  })
  const b = vue.$api.get(vue, 'admin/u/po/brand/count').then(res => {
    if (res.code === 0) {
      return res.count
    }
  })
  return Promise.all([a, b]).then(res => {
    return {
      tableData: res[0],
      total: res[1]
    }
  })
}
