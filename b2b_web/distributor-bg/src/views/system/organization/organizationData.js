/*
 * @Author: Jason
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   Jason
 * @Last Modified time: 2018-05-10
 */

// 管理员(销售组织) - 销售组织列表
export function getOrganizationList(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/organization/list', params)
}
// 管理员(销售组织) - 组织查询-根据ids
export function getOrganizationIds(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/organization/ids', params)
}
// 管理员(销售组织) - 销售组织列表总数
export function columnData(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/organization/count', params)
}
// 管理员(销售组织) - 销售组织添加
export function saveOrganization(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/organization', params)
}
// 管理员(销售组织) - 销售组织编辑
export function editOrganization(vm, params) {
    return vm.$api.put(vm, 'admin/u/p/organization', params)
}
// 管理员(销售组织) - 刪除销售组织
export function deleteOrganizationIds(vm, params) {
    return vm.$api.delete(vm, 'admin/u/p/organization', params)
}

// 管理员(部门管理模块) - 部门列表
export function getDepartmentList(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/department/list', params)
}
// 管理员(部门管理模块) - 部门详情
export function getDepartment(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/department', params)
}
// 管理员(部门管理模块) - 部门列表总数
export function columnDepData(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/department/count', params)
}
// 管理员(部门管理模块) - 部门添加
export function saveDepartment(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/department', params)
}
// 管理员(部门管理模块) - 部门修改
export function editDepartment(vm, params) {
    return vm.$api.put(vm, 'admin/u/p/department', params)
}
// 管理员(部门管理模块) - 部门删除
export function deleteDepartment(vm, params) {
    return vm.$api.delete(vm, 'admin/u/p/department', params)
}

// 管理员(事业部管理模块) - 事业部列表
export function getBusinessUnitList(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/businessUnit/list', params)
}
// 管理员(事业部管理模块) - 事业部详情
export function getBusinessUnit(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/businessUnit', params)
}
// 管理员(事业部管理模块) - 事业部列表总数
export function columnBusinessUnitData(vm, params) {
    return vm.$api.get(vm, 'admin/u/p/businessUnit/count', params)
}
// 管理员(事业部管理模块) - 事业部添加
export function saveBusinessUnit(vm, params) {
    return vm.$api.post(vm, 'admin/u/p/businessUnit', params)
}
// 管理员(事业部管理模块) - 事业部修改
export function editBusinessUnit(vm, params) {
    return vm.$api.put(vm, 'admin/u/p/businessUnit', params)
}
// 管理员(事业部管理模块) - 事业部删除
export function deleteBusinessUnit(vm, params) {
    return vm.$api.delete(vm, 'admin/u/p/businessUnit', params)
}