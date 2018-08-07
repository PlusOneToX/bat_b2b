/*
 * @Author: oliver
 * @Date:   2018-03-08 15:35:13
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-05-Fr 03:58:27
 */

export function timeFormat(data) { // 返回年月日时分秒
  if (data) {
    const date = new Date(data)
    const year = date.getFullYear()
    const month = (date.getMonth() + 1) > 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
    const day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate()
    const hour = date.getHours() > 9 ? date.getHours() : '0' + date.getHours()
    const minutes = date.getMinutes() > 9 ? date.getMinutes() : '0' + date.getMinutes()
    const seconds = date.getSeconds() > 9 ? date.getSeconds() : '0' + date.getSeconds()
    return year + '-' + month + '-' + day + ' ' + hour + ':' + minutes + ':' + seconds
  }
}

export function monthDay(data) { // 返回年月日
  if (data) {
    const date = new Date(data)
    const year = date.getFullYear()
    const month = (date.getMonth() + 1) > 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
    const day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate()
    return year + '-' + month + '-' + day + ''
  }
}

export function timeMonth(data) { // 返回年月
  if (data) {
    const date = new Date(data)
    const year = date.getFullYear()
    const month = (date.getMonth() + 1) > 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
    return year + '-' + month
  }
}

export function timeHours(data) { // 返回时分秒
  if (data) {
    const date = new Date(data)
    const hour = date.getHours() > 9 ? date.getHours() : '0' + date.getHours()
    const minutes = date.getMinutes() > 9 ? date.getMinutes() : '0' + date.getMinutes()
    const seconds = date.getSeconds() > 9 ? date.getSeconds() : '0' + date.getSeconds()
    return hour + ':' + minutes + ':' + seconds
  }
}
