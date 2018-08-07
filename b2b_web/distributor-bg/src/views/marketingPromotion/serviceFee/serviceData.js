// 新增服务费活动
export function addServiceFee(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/appoint', params)
}

// 编辑服务费活动
export function editServiceFee(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/appoint', params)
}

// 服务费活动详情
export function getServiceFeeDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/appoint/detail', params)
}

// 服务费活动列表
export function getServiceFeeList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/appoint/list', params)
}

// 上、下移、删除服务费活动
export function putServiceFeeList(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/appoint', params)
}
