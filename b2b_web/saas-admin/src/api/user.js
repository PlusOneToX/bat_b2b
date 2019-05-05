import request from '@/utils/request'
import api from '@/api/allApi'

// 登录
export function login(data) {
  return request({
    url: api.login,
    method: 'post',
    data
  })
}

// 用户详情
export function getInfo(data) {
  return request({
    url: api.userDetail,
    method: 'get',
    params: data
  })
}
