/*
 * @Author: yaowei
 * @Date: 2018-06-12 11:12:17
 * @LastEditors: yaowei
 * @LastEditTime: 2018-06-12 11:16:49
 */

export function apiTypeFormatter(apiType) { // 事务类型
  switch (apiType) {
    case 1:
      return '物流信息推送'
    case 2:
      return '订单信息推送'
    case 3:
      return '定制信息推送'
    case 4:
      return '获取定制价格'
    default:
      return '其他'
  }
}

export function developSourceFormatter(developSource) { // 开发源头类型
  switch (developSource) {
    default:
      return '其他'
  }
}
