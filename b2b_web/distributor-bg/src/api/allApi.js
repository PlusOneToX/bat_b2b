const baseURL = 'https://api.bat.com/' // 正式服

module.exports = {
  login: baseURL + 'admin/login', // 帐号密码登录
  getUserInfo: baseURL + 'admin/u/admin', // 获取用户信息
  logout: baseURL + 'admin/u/login', // 登出帐户

  // 商品接口
  goods: baseURL + 'admin/u/p/goods', // 添加、修改、删除商品、获取详情
  freezeStatus: baseURL + 'admin/u/p/goods/freeze', // 冻结、解冻商品
  getGoodsList: baseURL + 'admin/u/p/goods/list', // 根据搜索条件查找商品列表(分页)
  updateGoodsaleStatus: baseURL + 'admin/u/p/goods/shelve', // 上架、下架商品
  updateItemsaleStatus: baseURL + 'admin/u/p/goods/item/shelve', // 上架、下架货品

  // 商品属性接口
  getGoodsSpecList: baseURL + 'admin/u/p/specification/list', // 商品属性列表
  updateSpecStatus: baseURL + 'admin/u/p/specification/isEnable', // 更改属性状态
  goodsSpec: baseURL + 'admin/u/p/specification', // 删除、修改、添加
  getSpecById: baseURL + 'admin/u/p/specification/id', // 详情

  // 商品标签接口
  getLabelList: 'admin/u/p/goods/label/list', // 标签列表
  goodsLable: baseURL + 'admin/u/p/goods/label/bindGoods', // 添加
  deleteLable: baseURL + 'admin/u/p/goods/label/delete', // 删除
  getLabelDetail: baseURL + 'admin/u/p/goods/label/detail', // 详情
  updateLabelStatus: baseURL + 'admin/u/p/goods/label/modify', // 更改标签状态

  // 品牌接口
  getBrandPoList: baseURL + 'admin/u/po/brand/list', // 获取品牌列表
  getBrandList: baseURL + 'admin/u/p/brand/list', // 品牌列表
  updateBrandStatus: baseURL + 'admin/u/p/brand/open', // 更新品牌状态
  brand: baseURL + 'admin/u/p/brand', // 添加、修改、删除品牌、获取详情

  // 品类接口
  syncCategory: baseURL + 'admin/u/p/productline/syncCategory', // erp同步类目
  getProductlineList: baseURL + 'admin/u/p/productline/list', // 获取品类列表
  updateProductlineStatus: baseURL + 'admin/u/p/productline/open', // 更新品类状态
  productline: baseURL + 'admin/u/p/productline', // 添加、修改、删除品类、获取详情
  getProductline: baseURL + 'admin/u/po/productline/list', // 获取品类列表

  // 商品分类接口
  getGoodsClassifyList: baseURL + 'admin/u/p/category/goods/list', // 获取商品分类列表
  goodsClassify: baseURL + 'admin/u/p/category/goods', // 添加商品分类
  Classify: baseURL + 'admin/u/p/category', // 删除商品分类 ,更新状态,详情,添加修改

  // 价格等级接口
  getGradeList: baseURL + 'admin/u/p/distributor/grade/list', // 价格等级列表
  DisGrade: baseURL + 'admin/u/p/distributor/grade', // 添加、修改、删除
  updateGradeStatus: 'admin/u/p/distributor/grade/status', // 更改价格等级状态

  getLastChoose: baseURL + 'admin/u/po/chooseRecord/getLastChoose', //
  exportPriceExcel: baseURL + 'admin/u/p/goods/exportPriceExcel', // 导出报价单
  promotionExport: baseURL + 'promotion/v1/web/admin/promotion/export', // 促销活动导出
  getGoodsItemCount: baseURL + 'admin/u/p/goodsItem/count', // 导出列表总数
  getCheckDetail: baseURL + 'admin/u/po/checkDetail', // 审核详情

  getMateriallist: baseURL + 'admin/u/p/material/getMateriallist/', // 获取手机材质列表
  changeGroupSort: baseURL + 'admin/u/p/goods/changeGroupSort', // 商品-扩展数据-更改货品分组序列
  syncGoodsPrice: baseURL + 'admin/u/p/goods/syncGoodsPrice', // 同步价格

  syncCYGoods: baseURL + 'admin/u/p/goods/syncCYGoods', // 同步工厂
  getDisGradeList: baseURL + 'admin/u/po/distributor/grade/list', // 获取分销价列表
  getItemCodeList: baseURL + 'admin/u/p/goodsItemBoxInfo/listByItemCode', // 获取批量下单规格形式列表
  goodsItemBoxUp: baseURL + 'admin/u/p/goodsItemBoxInfo/up?id=', // 批量下单形式上移
  goodsItemBoxDown: baseURL + 'admin/u/p/goodsItemBoxInfo/down?id=', // 批量下单形式下移
  updateIsDefault: baseURL + 'admin/u/p/goodsItemBoxInfo/updateIsDefault?id=', // 设置默认下单形式
  getSpecificationList: baseURL + 'admin/u/po/specification/list', // 获取规格列表
  getItemPrice: baseURL + 'admin/u/p/goods/items/price', // 获取等级价格明细
  getWarehouseList: baseURL + 'admin/u/p/warehouse/stock/goodsList', // 获取库存明细

  synCollect: baseURL + 'admin/u/p/brand/synCollect', // 同步到收单工具
  fileUpload: baseURL + 'admin/u/file/sts' // 文件上传

}
