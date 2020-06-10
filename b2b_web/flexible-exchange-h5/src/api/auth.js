/*
 * @Author: yaowei
 * @Date: 2019-05-08 09:24:27
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-08 09:45:46
 */

const TokenKey = 'auth'

export function getToken () {
  return localStorage.getItem(TokenKey) ? localStorage.getItem(TokenKey) : ''
}

export function setToken (token) {
  return localStorage.setItem(TokenKey, token)
}

export function removeToken (token) {
  return localStorage.removeItem(TokenKey)
}
