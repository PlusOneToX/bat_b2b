/**
 * 应用场景：现存数据不全，请求完全数据来补全现存数据
 * 被拼接的对象初始化时最好初始化要拼接的属性值，否则数据没有绑定不会渲染
 * @param  {obj} vm           vue实例
 * @param  {array} stocks item组成的数组
 * @param  {array of strings} attrs 想添加的属性
 * @return {null}              无需return，直接对引用的数据进行操作
 */
export function cooperatingDatalist(vm, stocks, attrs) {
  if (stocks.length === 0) {
    return null
  }
  // 组装 param
  // idToItemObj为id到data中item的map
  const 
    ids = [],
		idToItemObj = {}
  for (let stock of stocks) {
    const id = stock.goodsId
    ids.push(id)
    idToItemObj[id] = stock
  }
  const param = ids.join(',')

  // ----------------------------------------
  return vm.$api.get(vm, 'admin/u/po/goods/ids', { ids: param })
    .then(data => {
      const items = data.goods
      for (let item of items) {
      // 从response获取itemId -> 获取itemId映射的item对象引用
        const
    	itemId = item.id, //id为第二个接口要拼接到第一个接口的对应的参数
			itemObj = idToItemObj[itemId];
        // 下面这段应改成遍历 数组[想拼接的属性*n] 逐个赋值
        for (let i = 0, len = attrs.length; i < len; i++) {
          if (item[attrs[i]] || item[attrs[i]]) {
            itemObj[attrs[i]] = item[attrs[i]] || item[attrs[i]]
          }
        }
      }
    }).catch(e => console.log(e))
}

