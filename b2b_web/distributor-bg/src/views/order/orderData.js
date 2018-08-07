/*
后端数据库接口*n
 */
let distributions = null // 配送模式cache

export function getOrderList(vm, params) { // 管理员(订单模块) - 订单列表
    return vm.$api.post(vm, 'admin/u/p/order/list', params)
}

export function getOrderListCount(vm, params) { // 管理员(订单模块) - 订单列表总数
    return vm.$api.post(vm, 'admin/u/p/order/count', params)
}

export function confirmOrder(vm, params) { // 管理员(订单模块) - 订单确认
    return vm.$api.put(vm, 'admin/u/p/order/confirm', params)
}

export function deleteOrder(vm, params) { // 管理员(订单模块) - 订单删除
    return vm.$api.delete(vm, 'admin/u/p/order', params)
}

// export function mergeOrder(vm, params) { // 管理员(订单模块) - 订单合并 // 作废 ================
//   return vm.$api.put(vm, 'admin/u/p/order/merge', params)
// }

/**
 * 订单操作，柯里化 已废弃
 * operateType 1确认, 2付款, 3设为未付款, 4生成发货单, 5取消发货单, 6完成, 7取消, 8无效
 * @param  {vue实例} vm 传入this
 */
// export const operateOrder = vm => (id, operateType) => {
//   return vm.$api.put(vm, 'admin/u/p/order/operate', {id, operateType})
// }

export function getOrderDetail(vm, params) { // 管理员(订单模块) - 订单详情
    return vm.$api.get(vm, 'admin/u/p/order', params)
}

// 售后服务
export function Orderlist(vm, params) { // 管理员(退货单模块) - 退货单列表查询
    return vm.$api.get(vm, 'admin/u/p/returnOrder/list', params)
}

export function getdistributions(vm, params) {
    if (distributions) { // 若已有缓存且无参数，则取出缓存
        return new Promise((resolve, reject) => {
            resolve({ distributions })
        })
    } else { // 若没有缓存，则发出请求
        return vm.$api.get(vm, 'admin/u/po/distribution/list', params || {
            page: 1,
            count: 200
        }).then(res => {
            distributions = res.distributions
            return res
        })
    }
}

// getDeliveryAddress :: deliveryId -> [address]
export function getDeliveryAddress(vm, params) {
    return vm.$api.get(vm, 'admin/u/deliveryaddress/list', params)
}

export function editOrder(vm, params) { // 管理员(订单模块) - 订单编辑
    return vm.$api.put(vm, 'admin/u/p/order', params)
}

export function getOrderDistributions(vm, params) { // 管理员(订单模块) - 订单配送列表选择
    return vm.$api.get(vm, 'admin/u/p/order/distribution/list', params)
}

export function getRegions(vm, params) { // (区域模块) - 查找区域列表
    return vm.$api.get(vm, 'region', params)
}

export function getDiscountChecks(vm, params) { // 管理员(订单审批模块) - 订单折扣审批列表
    return vm.$api.get(vm, 'admin/u/p/order/check/list', params)
}

export function getCheckDetail(vm, params) { // 管理员(订单审批模块) - 订单折扣审批详情
    return vm.$api.get(vm, 'admin/u/p/order/check/detail', params)
}

export function getUsers(vm, params) { // 管理员(用户模块) - 用户查询-根据ids
    return vm.$api.get(vm, 'admin/u/po/admin/ids', params)
}

/*
订单操作
 */
export function operateOrder(vm, params) { // 管理员(订单模块) - 订单操作
    return vm.$api.put(vm, 'admin/u/p/order/operate', params)
}

export function getlosegoodsList(vm, params) { // 管理员(缺货登记模块) - 缺货登记列表查询
    return vm.$api.get(vm, 'admin/u/p/losegoods/list', params)
}

export function getlosegoodsListCount(vm, params) { // 缺货登记列表总数查询
    return vm.$api.get(vm, 'admin/u/p/losegoods/count', params)
}

export function getlosegoodsDetail(vm, params) { // 缺货登记详情
    return vm.$api.get(vm, 'admin/u/p/losegoods', params)
}

export function getGoodsByIds(vm, params) { // 管理员(商品模块) - 商品基本信息-根据ids
    return vm.$api.get(vm, 'admin/u/po/goods/ids', params)
}

export function getDistributorByIds(vm, params) { // 管理员(分销商通用模块) - 通用分销商列表
    return vm.$api.get(vm, 'admin/u/po/distributor/common/list', params)
}

export function handleLose(vm, params) { // 缺货登记处理
    return vm.$api.put(vm, 'admin/u/p/losegoods/process', params)
}

export function handleDelete(vm, params) { // 管理员(缺货登记模块) - 缺货登记删除
    return vm.$api.delete(vm, 'admin/u/p/losegoods', params)
}

export function orderOperateLog(vm, params) { // 管理员(订单模块) - 订单操作日志
    return vm.$api.get(vm, 'admin/u/p/order/logs', params)
}

export function operateDelivery(vm, params) { // 管理员(订单发货单模块) - 发货(取消发货)操作
    return vm.$api.put(vm, 'admin/u/p/delivergoods/operate', params)
}

export function getOrderVmiList(vm, params) { // VMI订单明细列表
    return vm.$api.post(vm, 'admin/u/p/order/vmi/list', params)
}

export function getOrderVmiCount(vm, params) { // VMI订单明细总数
    return vm.$api.post(vm, 'admin/u/p/order/vmi/count', params)
}

export function searchDistributor(vm, params) { // 订单明细导出-分销商
    return vm.$api.get(vm, 'admin/u/po/order/searchDistributor', params)
}