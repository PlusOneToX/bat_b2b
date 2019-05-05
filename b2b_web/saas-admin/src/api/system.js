/*
 * @Author: yaowei
 * @Date: 2019-10-19 16:14:18
 * @LastEditors: yaowei
 * @LastEditTime: 2019-10-19 17:24:22
 */
import request from '@/utils/request'
import api from '@/api/allApi'

/** 管理员 */
// 添加用户
export function addUser(data) {
  return request({
    url: api.addUser,
    method: 'post',
    data
  })
}

// 用户详情
export function userDetail(data) {
  return request({
    url: api.userDetail,
    method: 'get',
    params: data
  })
}

// 删除用户
export function deleteUser(data) {
  return request({
    url: api.deleteUser,
    method: 'delete',
    data
  })
}

// 查询平台用户列表
export function userList(data) {
  return request({
    url: api.userList,
    method: 'get',
    params: data
  })
}


// 更新用户
export function updateUser(data) {
  return request({
    url: api.updateUser,
    method: 'put',
    data
  })
}
/** 管理员 */