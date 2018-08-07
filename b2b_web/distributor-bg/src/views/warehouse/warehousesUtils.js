//  Deprecated!!!
//  get API from warehousesData
//  数据接口已迁移至warehousesData
//  此地存放方法

export function getWarehousesData(vm, params) { //  Deprecated!!!
  return vm.$api.get(vm, 'admin/u/p/warehouse/list?isRecycle=0', params)
}

export function postReserveData(vm, params) { //  Deprecated!!!
  return vm.$api.post(vm, 'admin/u/p/warehouse/stock/reserved', params)
}

/**
 * 应用场景：现存数据不全，请求完全数据来补全现存数据
 * 被拼接的对象初始化时最好初始化要拼接的属性值，否则数据没有绑定不会渲染
 * @param  {obj} vm           vue实例
 * @param  {array} stocks item组成的数组
 * @param  {array of strings} attrs 想添加的属性
 * @return {null}              无需return，直接对引用的数据进行操作
 */
export function getDetailByItemIds(vm, stocks, attrs) { //  Deprecated!!!
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

  vm.$api.get(vm, 'admin/u/po/goods/item/ids', { ids: param })
    .then(data => {
      const items = data.items
      for (const item of items) {
        // 从response获取itemId -> 获取itemId映射的item对象引用
        // eslint-disable-next-line one-var
        const
          itemId = item.item.id,
          itemObj = idToItemObj[itemId]
        // 下面这段应改成遍历 数组[想拼接的属性*n] 逐个赋值
        for (let i = 0, len = attrs.length; i < len; i++) {
          itemObj[attrs[i]] = item.item[attrs[i]] || item.goods[attrs[i]]
        }
      }
    })
    .catch(e => console.log(e))
}

export function editWarehouse(vm, params) { //  Deprecated!!!
  return vm.$api.post(vm, 'admin/u/p/warehouse', params)
}

export function getAllocationList(vm, params) { //  Deprecated!!!
  return vm.$api.get(vm, 'admin/u/p/warehouse', params)
}

// return '20180330'
export function dateToNum(date) {
  return date.toISOString().substring(0, 10).replace(/-/g, '')
}

export function calculationFormatter(stateCode) { // 布尔值解释
  switch (stateCode) {
    case 0:
      return '否'
    case 1:
      return '是'
    default:
      return stateCode
  }
}
