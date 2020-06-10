/*
 * @Author: yaowei
 * @Date: 2019-05-13 19:03:59
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-13 19:04:22
 */
export function aliPay (data) {
  const div = document.createElement('div')
  div.innerHTML = data
  document.body.appendChild(div)
  document.forms[0].submit()
}
