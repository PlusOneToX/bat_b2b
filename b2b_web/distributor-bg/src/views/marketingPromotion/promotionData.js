/*
 * @Author: Jason
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   Jason
 * @Last Modified time: 2018-05-10
 */

// 管理员(营销推广-活动促销) - 活动促销列表
export function getPromotionList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/list', params)
}

// 管理员(营销推广-活动促销) - 活动促销审批列表
export function getPromotionCheckList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/check/list', params)
}
// 管理员(促销政策) - 统一政策审批列表
export function getPolicyCheckList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/policy/check/list', params)
}

// 管理员(营销推广-活动促销) - 活动促销列表总数
export function promotionData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/count', params)
}

// 管理员(营销推广-活动促销) - 活动促销审批
export function promotionCheck(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/marketing/promotion/check', params)
}

// 管理员(促销政策) - 统一政策审批
export function policyCheck(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/policy/check', params)
}

// 管理员(营销推广-活动促销) - 活动促销审批列表总数
export function promotionCheckData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/check/count', params)
}
// 管理员(促销政策) - 统一政策审批列表总数
export function policyCheckData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/policy/check/count', params)
}

// 管理员(营销推广-活动促销) - 活动促销删除
export function promotionDelete(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/marketing/promotion', params)
}

// 管理员(营销推广-活动促销) - 活动促销修改
export function promotionPut(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/marketing/promotion', params)
}
// 管理员(营销推广-活动促销) - 活动促销编辑审批
export function promotionCheckPut(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/marketing/promotion/edit/check', params)
}
// 管理员(营销推广-活动促销) - 活动促销添加
export function promotionPost(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/marketing/promotion', params)
}
// 管理员(营销推广-活动促销) - 活动促销审批详情
export function promotionCheckGet(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/check/detail', params)
}
// 管理员(促销政策) - 统一政策审批详情
export function policyCheckGet(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/policy/check/detail', params)
}
// 管理员(营销推广-活动促销) - 活动促销详情
export function promotionGet(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion', params)
}
// 管理员(分类模块) - 分类商品查询列表
export function getCategoryGoods(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/category/goods/ids', params)
}
// 管理员(商品模块) - 商品基本信息-根据ids
export function getGoods(vm, params) {
  return vm.$api.get(vm, 'admin/u/po/goods/ids', params)
}
// 管理员(商品模块) - 货品基本信息-根据ids
export function getItems(vm, params) {
  return vm.$api.get(vm, 'admin/u/po/goods/item/ids', params)
}
// 管理员(商品模块) - 货品库存总数-根据ids
export function getItemsStock(vm, params) {
  return vm.$api.get(vm, 'admin/u/po/warehouse/stock/itemIds', params)
}
// 管理员(促销政策) - 统一政策(等级折扣)添加
export function addGradeDiscount(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/policy/gradeDiscount', params)
}
// 管理员(促销政策) - 统一政策(等级折扣)详情
export function getradeDiscount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/policy/gradeDiscount', params)
}
// 管理员(促销政策) - 提货增长规则添加
export function addPickupRate(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/policy/pickupRate', params)
}
// 管理员(促销政策) - 提货增长规则详情
export function getPickupRate(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/policy/pickupRate', params)
}

// 优惠券 - 优惠券列表
export function getCouponList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/coupon/list', params)
}
// 优惠券 - 优惠券列表总数
export function getCouponCount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/coupon/count', params)
}
// 优惠券 - 新增优惠券
export function addCoupon(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/coupon', params)
}
// 优惠券 - 查看优惠券
export function getCouponDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/coupon/detailed', params)
}
// 优惠券 - 编辑优惠券
export function editCoupon(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/coupon', params)
}
// 优惠券 - 发布优惠券
export function releaseCoupon(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/coupon/release', params)
}
// 优惠券 - 作废优惠券
export function invalidCoupon(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/coupon/invalid', params)
}
// 优惠券 - 删除优惠券
export function deleteCoupon(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/coupon', params)
}
// 优惠券 - 删除优惠券
export function hideCoupon(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/coupon/hide', params)
}
// 优惠券 - 码库列表
export function getCounponCode(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/coupon/user/list', params)
}
// 优惠券 - 码库列表总数
export function getCounponCodeCount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/coupon/user/count', params)
}
// 优惠券 - 码库作废
export function invalidCouponCode(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/coupon/user/invalid', params)
}

