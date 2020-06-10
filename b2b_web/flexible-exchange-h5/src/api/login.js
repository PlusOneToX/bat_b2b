import request from './request'
import api from './allApi'

// 密码登录
export function login(phone, password, otherUid, openId) {
  return request({
    url: api.login,
    method: 'post',
    data: {
      phone,
      password,
      otherUid,
      openId
    }
  })
}

// 验证码登录
export function loginCaptcha(phone, code, otherUid, openId) {
  return request({
    url: api.loginCaptcha,
    method: 'post',
    data: {
      phone,
      code,
      otherUid,
      openId
    }
  })
}
export function getInfo(userId) {
  return request({
    url: api.getInfo,
    method: 'post',
    data: {
      userId
    }
  })
}
export function logout() {
  return request({
    url: api.logout,
    method: 'put'
  })
}
