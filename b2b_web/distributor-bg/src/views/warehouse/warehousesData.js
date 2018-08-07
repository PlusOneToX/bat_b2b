// eslint-disable-next-line one-var
let
  categoryList = null,
  brandList = null

export function getWarehousesData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/list?isRecycle=0', params)
}

export function postReserveData(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/warehouse/stock/reserved', params)
}

export function getStockNum(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/goods', params)
}

/**
 * 应用场景：现存数据不全，请求完全数据来补全现存数据
 * 被拼接的对象初始化时最好初始化要拼接的属性值，否则数据没有绑定不会渲染
 * @param  {obj} vm           vue实例
 * @param  {array} stocks item组成的数组
 * @param  {array of strings} attrs 想添加的属性
 * @return {null}              无需return，直接对引用的数据进行操作
 */
export function getDetailByItemIds(vm, stocks, attrs) {
  if (stocks.length === 0) {
    return null
  }
  // 组装 param
  // idToItemObj为id到data中item的map
  // eslint-disable-next-line one-var
  const
    ids = [],
    idToItemObj = {}

  for (const stock of stocks) {
    const id = stock.itemId || stock.id
    ids.push(id)
    idToItemObj[id] = stock
  }

  const param = ids.join(',')

  // ----------------------------------------
  return vm.$api.get(vm, 'admin/u/po/goods/item/ids', { ids: param }).then(data => {
    const items = data.items
    for (const item of items) {
      // 从response获取itemId -> 获取itemId映射的item对象引用
      // eslint-disable-next-line one-var
      const
        itemId = item.item.id,
        itemObj = idToItemObj[itemId]
      // 下面这段应改成遍历 数组[想拼接的属性*n] 逐个赋值
      for (let i = 0, len = attrs.length; i < len; i++) {
        if (item.item[attrs[i]] || item.goods[attrs[i]]) {
          itemObj[attrs[i]] = item.item[attrs[i]] || item.goods[attrs[i]]
        }
      }
    }
  }).catch(e => console.log(e))
}

export function getStockNumByItemIds(vm, stocks, attrs) {
  if (stocks.length === 0) {
    return null
  }
  // 组装 param
  // idToItemObj为id到data中item的map
  // eslint-disable-next-line one-var
  const
    ids = [],
    idToItemObj = {}

  for (const stock of stocks) {
    const id = stock.itemId || stock.id
    idToItemObj[id] = stock
  }

  for (let i = 0, len = stocks.length; i < len; i++) {
    const { warehouseId, goodsId, itemId } = stocks[i]
    ids.push({
      itemId,
      goodsId,
      warehouseId
    })
  }

  const params = { ids }
  // ----------------------------------------
  vm.$api.post(vm, 'admin/u/p/warehouse/stock/ids', params).then(data => {
    const responseStocks = data.stocks
    for (let i = 0, len = responseStocks.length; i < len; i++) {
      // eslint-disable-next-line one-var
      const
        itemId = responseStocks[i].itemId,
        itemObj = idToItemObj[itemId]
      for (let j = 0, len = attrs.length; j < len; j++) {
        itemObj[attrs[j]] = responseStocks[i][attrs[j]]
      }
    }
  }).catch(e => console.log(e))
}

export function editWarehouse(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/warehouse', params)
}

// \/ 调拨系列
export function getAllocationList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/allocate/list', params)
}

export function getAllocatsCount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/allocate/count', params)
}

export function confirmAllocatsCount(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/warehouse/stock/allocate/confirm', params)
}

export function addAllocate(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/warehouse/stock/allocate', params)
}

// 审批系列
export function getReserveChecklist(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/reserved/checklist', params)
}

export function getReserveChecklistCount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/reserved/count', params)
}

export function getCheckDetail(vm, params) { // 关于预留审批
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/reserved', params)
}

export function getAdjustCheckDetail(vm, params) { // 关于调整审批
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/adjust', params)
}

export function adjustCheck(vm, params) { // 调整审批
  return vm.$api.put(vm, 'admin/u/p/warehouse/stock/adjust/check', params)
}

export function reservedCheck(vm, params) { // 预留审批
  return vm.$api.put(vm, 'admin/u/p/warehouse/stock/reserved/check', params)
}

export function getAdjustChecklist(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/adjust/checklist', params)
}

export function getAdjustChecklistCount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/warehouse/stock/adjust/count', params)
}

// 其他模块数据系列
export function getCategoryDetail(vm, params) { // depreacted
  return vm.$api.get(vm, 'admin/u/p/category', params)
}

export function getAdmins(vm, flows) {
  // 从flows中获得审批相关用户ids，请求用户信息数据
  // idToItemObj is a map, id to flow
  // eslint-disable-next-line one-var
  const
    idToItemObj = {},
    ids = []

  for (let i = 0, len = flows.length; i < len; i++) {
    idToItemObj[flows[i].checkUser] = flows[i]
    ids.push(flows[i].checkUser)
  }

  const idsString = ids.join()
  return vm.$api.get(vm, 'admin/u/po/admin/ids', { ids: idsString }).then(res => {
    for (let i = 0, len = res.admins.length; i < len; i++) {
      idToItemObj[res.admins[i].id].checkUserName = res.admins[i].name
    }
  })
}

export function getBrandDetail(vm, params) { // depreacted
  return vm.$api.get(vm, 'admin/u/p/brand', params)
}

export function getCategoryList(vm, params) { // depreacted
  if (categoryList) {
    return new Promise((resolve, reject) => {
      resolve(categoryList)
    })
  } else {
    return vm.$api.get(vm, 'admin/u/p/category/list', params).then(res => {
      categoryList = res.categorys
      return categoryList
    })
  }
}

export function getBrandList(vm, params) { // depreacted
  if (brandList) {
    return new Promise((resolve, reject) => {
      resolve(brandList)
    })
  } else {
    return vm.$api.get(vm, 'admin/u/p/brand/list', params).then(res => {
      brandList = res.brands
      return brandList
    })
  }
}
