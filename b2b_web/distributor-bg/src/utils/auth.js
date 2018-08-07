import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const UserId = 'Admin-UserId'
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
export function setId(id) {
  return Cookies.set(UserId, id)
}
export function getId() {
  return Cookies.get(UserId)
}
