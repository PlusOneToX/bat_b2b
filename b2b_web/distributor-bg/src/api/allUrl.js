module.exports = {
  // 获取域名api
  tenantUrl: 'platform/v1/web/tenant/url', // 用户列表
  // 用户列表
  getUserList: 'system/v1/web/admin/user/list', // 用户列表
  getUserPoList: 'system/v1/web/admin/user/po/list', // 用户列表(不受权限控制)
  getUserIds: 'system/v1/web/admin/user/ids', // 通过ID集合查询多个后台用户
  login: 'system/v1/web/admin/user/login', // 登录
  logout: 'system/v1/web/admin/user/logout', // 登出
  user: 'system/v1/web/admin/user', // 添加、修改、删除用户、获取详情
  userStatus: 'system/v1/web/admin/user/status', // 用户状态更新
  rockAccountInfoList: 'system/v1/web/admin/user/po/rockAccountInfoList', // 账户中心员工信息列表
  salesList: 'system/v1/web/admin/user/po/list/salesman', // 用户列表(分页)(仅返回id名称)

  // 商品
  goods: 'goods/v1/web/admin/goods', // 添加、修改、删除商品、获取详情
  freezeStatus: 'goods/v1/web/admin/goods/freezestatus', // 冻结、解冻商品
  getGoodsList: 'goods/v1/web/admin/goods/list', // 根据搜索条件查找商品列表
  getGoodsPoList: 'goods/v1/web/admin/goods/po/list', // 根据搜索条件查找商品列表（不受权限控制）
  getGoodsItemList: 'goods/v1/web/admin/goods/item/list', // 根据搜索条件查找货品列表
  getGoodsItemPoList: 'goods/v1/web/admin/goods/po/item/list', // 根据搜索条件查找货品列表（不受权限控制）
  updateGoodsaleStatus: 'goods/v1/web/admin/goods/salestatus', // 上架、下架商品
  updateItemsaleStatus: 'goods/v1/web/admin/goods/item/salestatus', // 上架、下架货品
  goodsErpList: 'goods/v1/web/admin/goods/item/erp/list', // 查找erp货品列表
  goodsByDistributorId: 'goods/v1/web/admin/goods/po/pageAssignByDistributorId', // 分销商指定的货品列表(兑换卡关联商品)
  diyItemList: 'goods/v1/web/admin/goods/po/pageDiyItem', // 货品列表（定制商品）
  goodsItemBox: 'goods/v1/web/admin/goods/item/box', // 货品(SKU)装箱信息列表、修改

  // 商品属性
  getAttributeList: 'goods/v1/web/admin/attribute/list', // 商品属性列表
  getAttributePoList: 'goods/v1/web/admin/attribute/po/list', // 不受限制控制商品属性列表
  getAllvalueList: 'goods/v1/web/admin/attribute/allvalue/list', // 根据商品属性ID查找所有商品属性值列表(不分页)
  attributeStatus: 'goods/v1/web/admin/attribute/open', // 更改属性状态
  attribute: 'goods/v1/web/admin/attribute', // 删除、修改、添加、详情

  // 商品标签
  getTagList: 'goods/v1/web/admin/tag/list', // 标签列表
  getTagPoList: 'goods/v1/web/admin/tag/po/list', // 标签列表
  goodsTag: 'goods/v1/web/admin/tag', // 添加、修改、删除标签、获取详情
  updateTagStatus: 'goods/v1/web/admin/tag/open', // 更改标签状态
  tabPreview: 'flexible/v1/web/admin/u/p/label/preview', // 标签预览
  createLable: 'thirdparty/v1/web/admin/order/createLable', // 手动生成标签

  // 品牌
  getBrandList: 'goods/v1/web/admin/brand/list', // 品牌列表
  getBrandPoList: 'goods/v1/web/admin/brand/po/list', // 不受限制品牌列表
  brandStatus: 'goods/v1/web/admin/brand/open', // 更新品牌状态
  brand: 'goods/v1/web/admin/brand', // 添加、修改、删除品牌、获取详情

  // 品类
  // syncCategory:   'admin/u/p/productline/syncCategory', // erp同步类目
  getCategoryList: 'goods/v1/web/admin/category/list', // 获取品类列表
  getCategoryPoList: 'goods/v1/web/admin/category/po/list', // 获取不受权限控制品类列表
  categoryStatus: 'goods/v1/web/admin/category/open', // 更新品类状态
  category: 'goods/v1/web/admin/category', // 添加、修改、删除品类、获取详情

  // 商品分类
  getClassifyList: 'goods/v1/web/admin/classify/list', // 获取商品分类列表
  getClassifyPoList: 'goods/v1/web/admin/classify/po/list', // 获取不受权限控制商品分类列表
  getClassifySubList: 'goods/v1/web/admin/classify/sub/list', // 根据商品分类id获取子分类列表
  getClassifyPoSubList: 'goods/v1/web/admin/classify/po/sub/list', // 获取不受权限控制商品子分类列表
  classifyStatus: 'goods/v1/web/admin/classify/open', // 更新商品分类状态
  Classify: 'goods/v1/web/admin/classify', // 添加、修改、删除商品分类、获取详情
  recommendInfo: 'goods/v1/web/admin/classify/recommend/info', // 更新商品推荐分类信息
  getRecommendInfo: 'goods/v1/web/admin/classify/recommend/info', // 更新商品推荐分类信息



  // 价格等级
  getGradeList: 'goods/v1/web/admin/scaleprice/list', // 价格等级列表
  getGradePoList: 'goods/v1/web/admin/scaleprice/po/list', // 不受权限限制价格等级列表
  disGrade: 'goods/v1/web/admin/scaleprice', // 添加、修改、删除
  gradeStatus: 'goods/v1/web/admin/scaleprice/open', // 更改价格等级状态

  // 分销商
  getDistributorFList: 'distributor/v1/web/admin/distributor/one/list', // 分销商列表(一级)
  getDistributorNList: 'distributor/v1/web/admin/distributor/next/list', // 分销商列表(多级)
  getDistributorPoList: 'distributor/v1/web/admin/distributor/po/one/list', // 分销商列表(一级，不受权限控制)
  getDistributorNPoList: 'distributor/v1/web/admin/distributor/po/list', // 分销商列表(多级，不受权限控制)
  distributorStatus: 'distributor/v1/web/admin/distributor/open', // 冻结、解冻分销商
  distributor: 'distributor/v1/web/admin/distributor', // 分销商添加、修改、删除、详情
  disNextCheck: 'distributor/v1/web/admin/distributor/next/check', // 下级分销商审核
  getSalesareaList: 'distributor/v1/web/admin/salesarea/list', // 销售区域列表(分页)
  getSalesareaPoList: 'distributor/v1/web/admin/salesarea/po/list', // 销售区域列表(不受权限控制,分页)
  salesarea: 'distributor/v1/web/admin/salesarea', // 销售区域添加、修改、删除、详情
  salesareaStatus: 'distributor/v1/web/admin/salesarea/open', // 销售区域状态更新
  getDistCategoryList: 'distributor/v1/web/admin/category/list', // 分销商类别列表
  getDistCategoryPoList: 'distributor/v1/web/admin/category/po/list', // 分销商类别列表(不受权限控制)
  distCategory: 'distributor/v1/web/admin/category', // 分销商类别添加、修改、删除、详情
  distCategoryStatus: 'distributor/v1/web/admin/category/open', // 分销商类别状态更新
  getDistGroupList: 'distributor/v1/web/admin/group/list', // 销商分组列表
  getDistGroupPoList: 'distributor/v1/web/admin/group/po/list', // 销商分组列表(不受权限控制)
  distGroup: 'distributor/v1/web/admin/group', // 分销商分组添加、修改、删除、详情
  distGroupStatus: 'distributor/v1/web/admin/group/open', // 分销商分组状态更新
  refuseDistributor: 'distributor/v1/web/admin/distributor/refuse', // 根据分销商id拒绝申请中的分销商
  distributorCheckList: 'distributor/v1/web/admin/distributor/check/list', // 分销商资料审批列表
  distributorCheck: 'distributor/v1/web/admin/distributor/check', // 分销商资料审批详情、审批

  // 店铺分账
  subAccount: 'distributor/v1/web/admin/subAccountAdminConfig', // 新增、修改分账及分账详情
  // 分账记录

  orderSubAccountList: 'financial/v1/web/admin/orderSubAccount/page', // 分账记录列表
  orderSubAccountDetail: 'financial/v1/web/admin/orderSubAccountBill/listByOrderSubAccountId', // 查看分账记录详情
  // 分账配置
  subAccountConfigList: 'distributor/v1/web/admin/subAccountUserConfig/page', // 分账配置列表
  subConfigListByCondition: 'distributor/v1/web/admin/subAccountUserConfig/listByCondition', // 分账配置下拉列表
  subSalesmanByCondition: 'distributor/v1/web/admin/subAccountSaleman/listByCondition', // 获取分销商业务员
  // 分账业务员
  subAccountSaleList: 'distributor/v1/web/admin/subAccountSaleman/page',


  // 分销商联系人角色
  getDisRoleList: 'distributor/v1/web/admin/role/po/list', // 分销商联系人角色列表(不受权限控制)

  // 定制信息
  distributorCustomPrice: 'distributor/v1/web/admin/u/po/distributorCustomPrice', // 保存、查询分销商定制价格

  // 查询设置
  baseSetting: 'system/v1/web/admin/baseSetting', // 查询设置

  // 分销商收款条件
  tradeList: 'distributor/v1/web/admin/trade/list', // 分销商收款条件列表
  tradePoList: 'distributor/v1/web/admin/trade/po/list', // 分销商收款条件列表(不受权限控制)
  trade: 'distributor/v1/web/admin/trade', // 收款条件添加、修改、删除、详情
  tradeStatus: 'distributor/v1/web/admin/trade/open', // 收款条件状态

  // 系统
  // 销售组织
  getOrganizationList: 'system/v1/web/admin/organization/list', // 销售组织列表
  getOrganizationPoList: 'system/v1/web/admin/organization/po/list', // 销售组织列表(不受权限控制)
  organization: 'system/v1/web/admin/organization', // 销售组织添加、修改、删除、详情
  getOrganizationByIds: 'system/v1/web/admin/organization/ids', // 通过ID集合查询多个多个组织

  // 销售部门
  getDepartmentList: 'system/v1/web/admin/department/list', // 销售部门列表
  getDepartmentPoList: 'system/v1/web/admin/department/po/list', // 销售部门列表(不受权限控制)
  department: 'system/v1/web/admin/department', // 销售部门添加、修改、删除、详情
  getDepartmentPoById: 'system/v1/web/admin/department/po/list/id', // 组织下的部门列表
  departmentSync: 'system/v1/web/admin/department/sync', // 与ERP进行同步

  // 角色列表
  getRoleList: 'system/v1/web/admin/role/list', // 角色列表
  role: 'system/v1/web/admin/role', // 角色添加、修改、删除、详情
  permissionPoList: 'system/v1/web/admin/permission/po/list/tree', // 权限列表（不受权限控制）
  menuPoList: 'system/v1/web/admin/menu/po/list/tree', // 菜单列表(不受权限控制)
  roleIds: 'system/v1/web/admin/role/ids', // 根据角色id 查询权限

  // 审批配置
  checkDetail: 'system/v1/web/admin/check/checkDetail', // 审批配置详情
  checkPoDetail: 'system/v1/web/admin/check/po/checkDetail', // 审批配置详情（不受权限控制）
  check: 'system/v1/web/admin/check', // 审批配置修改

  // 配送管理
  logisticsList: 'system/v1/web/admin/logistics/list', // 配送列表
  logisticsPoList: 'system/v1/web/admin/logistics/po/list', // 配送列表（不受权限控制）
  logistics: 'system/v1/web/admin/logistics', // 配送添加、修改、删除、详情
  formula: 'system/v1/web/admin/logistics/formula', // 公式校验

  // 区域
  regionList: 'system/v1/web/admin/region/list/tree/old', // 区域递归树形列表
  regionListById: 'system/v1/web/admin/region/list', // 区域列表

  // 仓库列表
  warehouseList: 'warehouse/v1/web/admin/u/p/warehouse/page', // 仓库列表
  warehousePoList: 'warehouse/v1/web/admin/u/po/warehouse/page', // 仓库列表（不受权限控制）
  warehouse: 'warehouse/v1/web/admin/u/p/warehouse', // 仓库添加、修改、删除、详情
  warehouseStockList: 'warehouse/v1/web/admin/u/p/warehouse/stock/page', // 货品库存列表
  warehouseUoD: 'warehouse/v1/web/admin/u/p/warehouse/upOrDown', // 仓库上下移动
  warehouseStatus: 'warehouse/v1/web/admin/u/p/warehouse/updateStatus', // 启用、禁用仓库
  syncStock: 'warehouse/v1/web/admin/u/p/warehouse/stock/syncStock', // 同步库存

  // 仓库设置
  warehouseSettingList: 'warehouse/v1/web/admin/u/p/warehouseSetting/list', // 仓库设置列表
  warehouseSetting: 'warehouse/v1/web/admin/u/p/warehouseSetting', // 仓库设置添加、修改

  // 库存预留
  stockReservedList: 'warehouse/v1/web/admin/u/p/warehouse/stock/reserved/page', // 库存预留分页
  stockReserved: 'warehouse/v1/web/admin/u/p/warehouse/stock/reserved', // 库存预留详情
  stockRelease: 'warehouse/v1/web/admin/u/p/warehouse/stock/reserved/release', // 释放预留

  // 货品库存
  stockListByitemId: 'warehouse/v1/web/admin/u/po/warehouse/stock/listStockByCondition', // 货品id列表查询货品库存
  syncStockByItemErpId: 'warehouse/v1/web/admin/u/p/warehouse/stock/syncStockByItemErpId', // 同步单个库存
  resetLockStock: 'warehouse/v1/web/admin/u/resetLockStock', // 校正锁库数量

  // 商店配置
  bannerList: 'system/v1/web/admin/banner/list', // 推广列表
  banner: 'system/v1/web/admin/banner', // 推广添加、修改、删除、详情
  bannerDown: 'system/v1/web/admin/banner/sort/down', // 下移
  bannerUp: 'system/v1/web/admin/banner/sort/up', // 上移

  // 首页公告
  noticeList: 'system/v1/web/admin/notice/list', // 公告列表
  notice: 'system/v1/web/admin/notice', // 公告添加、修改、删除、详情
  noticeStatus: 'system/v1/web/admin/notice/release', // 发布、取消公告
  // 首页栏目
  columnList: 'system/v1/web/admin/column/list', // 栏目列表
  column: 'system/v1/web/admin/column', // 栏目添加、修改、删除、详情
  columnSort: 'system/v1/web/admin/column/goods/sort', // 上下移
  columnStatus: 'system/v1/web/admin/column/release', // 发布、取消栏目
  columnListByIds: 'goods/v1/web/admin/goods/list/column', // 栏目id获取商品列表(分页)
  clearanceGoodsStoreColumn: 'system/v1/web/admin/column/clearanceGoodsStoreColumn', // 一键导入清仓商品（栏目）
  // 首页板块
  sectionList: 'system/v1/web/admin/section/list', // 板块列表
  section: 'system/v1/web/admin/section', // 板块添加、修改、删除、详情
  sectionSort: 'system/v1/web/admin/section/goods/sort', // 上下移
  sectionStatus: 'system/v1/web/admin/section/release', // 发布、取消板块
  sectionListByIds: 'goods/v1/web/admin/goods/list/section', // 板块id获取商品列表(分页)
  getCategoryGoods: 'goods/v1/web/admin/goods/po/list/classifyIds', // 根据分类ids获取商品列表
  getGoodsByIds: 'goods/v1/web/admin/goods/po/list/ids', // 根据商品ids获取商品列表
  // 移动端首页
  mobileList: 'system/v1/web/admin/mobile/list', // 移动端首页列表
  mobile: 'system/v1/web/admin/mobile', // 移动端首页添加、修改、删除、详情
  mobileListByIds: 'goods/v1/web/admin/goods/list/mobile', // 移动端配置id获取商品列表(分页)
  mobileListByIdss: 'goods/v1/web/admin/goods/list/mobiles', // 移动端配置ids获取商品列表(分页)

  // 购物设置
  shopSetting: 'system/v1/web/admin/shopSetting', // 查询、更新购物设置
  // 工厂设置
  factorySetting: 'system/v1/web/admin/factorySetting', // 查询、更新、添加、删除工厂推送时效设置
  // 基本设置
  loginSetting: 'system/v1/web/admin/baseSetting/loginSetting', // 查询、更新登录设置
  // 协议设置
  agreementSettingList: 'system/v1/web/admin/agreementSetting/list', // 协议设置列表(分页)
  agreementSetting: 'system/v1/web/admin/agreementSetting', // 查询、更新、添加、删除协议设置
  checkBrand: 'system/v1/web/admin/agreementSetting/checkBrand', // 查询可用品牌

  // 培训中心
  trainingList: 'system/v1/web/admin/trainingCenter/list', // 培训中心（分页）
  trainingPoList: 'system/v1/web/admin/trainingCenter/list/menu', // 培训中心（不分页）
  training: 'system/v1/web/admin/trainingCenter', // 培训中心添加、修改、删除、详情
  trainingUp: 'system/v1/web/admin/trainingCenter/sort/up', // 上移
  trainingDown: 'system/v1/web/admin/trainingCenter/sort/down', // 上移
  // 下载中心
  downloadList: 'system/v1/web/admin/downloadCenter/list', // 下载中心（分页）
  downloadPoList: 'system/v1/web/admin/downloadCenter/list/menu', // 下载中心（不分页）
  download: 'system/v1/web/admin/downloadCenter', // 下载中心添加、修改、删除、详情
  downloadUp: 'system/v1/web/admin/downloadCenter/sort/up', // 上移
  downloadDown: 'system/v1/web/admin/downloadCenter/sort/down', // 下移

  // 获取OSS
  getFileSts: 'system/v1/web/admin/oss/sts',

  // 柔性管理
  // 产品类型
  productCategoryList: 'flexible/v1/web/admin/u/p/productCategory/page', // 产品类型列表
  productCategoryPoList: 'flexible/v1/web/admin/u/po/productCategory/page', // 产品类型列表(不受权限控制)
  productUsableList: 'flexible/v1/web/admin/u/po/productCategory/listUsable', // 产品类型下拉列表
  productCategory: 'flexible/v1/web/admin/u/p/productCategory', // 产品类型添加、修改
  // 图库管理
  pictureCategoryList: 'flexible/v1/web/admin/u/p/pictureCategory/page', // 图片分类列表
  pictureCategoryPoList: 'flexible/v1/web/admin/u/po/pictureCategory/page', // 图片分类列表(不受权限控制)
  pictureCategory: 'flexible/v1/web/admin/u/p/pictureCategory', // 图片分类添加、修改、删除、详情
  pictureCategorySort: 'flexible/v1/web/admin/u/p/pictureCategory/upOrDown', // 图片分类上移、下移
  pictureCategoryStatus: 'flexible/v1/web/admin/u/p/pictureCategory/updateOpenFlag', // 图片分类启用、禁用
  pictureList: 'flexible/v1/web/admin/u/p/picture/page', // 图片列表
  picturePoList: 'flexible/v1/web/admin/u/po/picture/page', // 图片列表(不受权限控制)
  picture: 'flexible/v1/web/admin/u/p/picture', // 图片添加、修改、删除、详情
  pictureSort: 'flexible/v1/web/admin/u/p/picture/upOrDown', // 图片上移、下移
  pictureStatus: 'flexible/v1/web/admin/u/p/picture/updateOpenFlag', // 图片启用、禁用
  picListByDistributor: 'flexible/v1/web/admin/u/po/picture/pageByDistributor', // 分销商图片列表
  // 标签管理
  labelList: 'flexible/v1/web/admin/u/p/label/page', // 标签列表
  label: 'flexible/v1/web/admin/u/p/label', // 标签添加、修改、删除、详情
  labelStatus: 'flexible/v1/web/admin/u/p/label/upOpenFlag', // 标签启用、禁用
  // 字体管理
  fontList: 'flexible/v1/web/admin/u/p/font/page', // 字体列表
  font: 'flexible/v1/web/admin/u/p/font', // 字体添加、修改、删除、详情
  fontStatus: 'flexible/v1/web/admin/u/p/font/updateOpenFlag', // 字体启用、禁用
  fontSort: 'flexible/v1/web/admin/u/p/font/upOrDown', // 字体上移、下移
  // 型号管理
  modelList: 'flexible/v1/web/admin/u/p/model/page', // 型号列表
  modelPoList: 'flexible/v1/web/admin/u/po/model/page', // 型号列表(不受权限控制)
  modelAllList: 'flexible/v1/web/admin/u/p/model/pageSimpleCOByCondition', // 非树形所有型号列表
  model: 'flexible/v1/web/admin/u/p/model', // 型号添加、修改、删除、详情
  modelSort: 'flexible/v1/web/admin/u/p/model/upOrDown', // 型号上移、下移
  modelStatus: 'flexible/v1/web/admin/u/p/model/updateOpenFlag', // 型号启用、禁用
  modelExport: 'flexible/v1/web/admin/u/export', // 导出
  // 材质管理
  materialList: 'flexible/v1/web/admin/u/p/material/page', // 材质列表
  materialPoList: 'flexible/v1/web/admin/u/po/material/page', // 材质列表(不受权限控制)
  materialLowestList: 'flexible/v1/web/admin/u/po/material/listLowest', // 最底级材质列表
  materialTreeList: 'flexible/v1/web/admin/u/po/material/treeAdmin', // 材质树列表
  material: 'flexible/v1/web/admin/u/p/material', // 材质添加、修改、删除、详情
  materialSort: 'flexible/v1/web/admin/u/p/material/upOrDown', // 材质上移、下移
  materialStatus: 'flexible/v1/web/admin/u/p/material/updateOpenFlag', // 材质启用、禁用
  materialUnTree: 'flexible/v1/web/admin/u/po/material/listWithoutTree', // 材质列表（下拉列表，非分页）
  materialPageTree: 'flexible/v1/web/admin/u/po/material/pageWithoutTree', // 材质列表（分页）-兑换活动
  // 型号材质关联管理
  modelAmaterialList: 'flexible/v1/web/admin/u/p/modelMaterialRelevance/page', // 型号材质关联列表
  modelAmaterial: 'flexible/v1/web/admin/u/p/modelMaterialRelevance', // 型号材质关联修改、删除、详情
  modelAmaterialStatus: 'flexible/v1/web/admin/u/p/modelMaterialRelevance/updateOpenFlag', // 型号材质关联启用、禁用
  // 第三方材质型号关联
  thirdSkuList: 'flexible/v1/web/admin/u/p/thirdSkuRelevance/page', // 第三方材质型号关联列表
  thirdSkuExport: 'flexible/v1/web/admin/u/p/thirdSkuRelevance/export', // 第三方材质型号关联导出
  thirdSkuImport: 'flexible/v1/web/admin/u/p/thirdSkuRelevance/import', // 第三方材质型号关联导入
  thirdSkutempDownload: 'flexible/v1/web/admin/u/p/thirdSkuRelevance/tempDownLoad', // 第三方材质型号关联下载模板
  thirdSku: 'flexible/v1/web/admin/u/p/thirdSkuRelevance/batch', // 批量删除
  thirdSkuStatus: 'flexible/v1/web/admin/u/p/thirdSkuRelevance/updateOpenFlag', // 启用、禁用

  // 门店
  shopList: 'flexible/v1/web/admin/u/p/shop/page', // 门店列表
  // shopPoList:   'flexible/v1/web/admin/u/po/model/page', // 门店列表(不受权限控制)
  shop: 'flexible/v1/web/admin/u/p/shop', // 门店添加、修改、删除
  shopDetail: 'flexible/v1/web/admin/u/p/shop/detailById', // 门店详情
  shopExport: 'flexible/v1/web/admin/u/p/shop/export', // 门店导出
  shopStatus: 'flexible/v1/web/admin/u/p/shop/updateOpenFlag', // 门店启用、禁用
  shopTempDownLoad: 'flexible/v1/web/admin/u/p/importShop/tempDownLoad', // 门店模板导出
  shopImport: 'flexible/v1/web/admin/u/p/shop/import', // 门店excel导入
  shoplistBycId: 'flexible/v1/web/admin/u/listByCondition', // 根据分账配置Id查询门店列表

  // 轮播图管理
  disBannerList: 'flexible/v1/web/admin/u/p/distributorBanner/page', // 轮播图列表
  disBanner: 'flexible/v1/web/admin/u/p/distributorBanner', // 轮播图新增、修改
  disBannerDetail: 'flexible/v1/web/admin/u/p/distributorBanner/detail', // 轮播图详情
  delDisBanner: 'flexible/v1/web/admin/u/p/distributorBanner/deleteById', // 轮播图删除
  batchDelDisBanner: 'flexible/v1/web/admin/u/p/distributorBanner/batchDelete', // 轮播图批量删除

  // 系列展示管理
  disThemeList: 'flexible/v1/web/admin/u/p/distributorSeriesTheme/page', // 系列展示列表（分页）
  disThemePoList: 'flexible/v1/web/admin/u/p/distributorSeriesTheme/list', // 系列展示列表（不分页）
  disThemeListByCondition: 'flexible/v1/web/admin/u/p/distributorSeriesTheme/listSimpleByCondition', // 查询系列展示列表
  disThemePicList: 'flexible/v1/web/admin/u/p/distributorSeriesTheme/list', // 系列展示图片列表
  disThemeDetail: 'flexible/v1/web/admin/u/p/distributorSeriesTheme/detail', // 系列展示详情
  disTheme: 'flexible/v1/web/admin/u/p/distributorSeriesTheme', // 系列展示新增、修改、删除
  picRelevanceMove: 'flexible/v1/web/admin/u/p/seriesPictureRelevance/upOrDown', // 图片关联上下移动
  delPicRelevance: 'flexible/v1/web/admin/u/p/seriesPictureRelevance/deleteById', // 图片关联删除
  batchPicRelevance: 'flexible/v1/web/admin/u/p/seriesPictureRelevance/batchDelete', // 图片关联批量删除

  // 推荐管理
  disRecommendList: 'flexible/v1/web/admin/u/p/distributorIndexRecommend/page', // 推荐列表
  disRecommend: 'flexible/v1/web/admin/u/p/distributorIndexRecommend', // 推荐新增、修改
  disRecommendDetail: 'flexible/v1/web/admin/u/p/distributorIndexRecommend/detail', // 推荐详情
  disRecommendMove: 'flexible/v1/web/admin/u/p/indexRecommendPictureRelevance/upOrDown', // 推荐图片关联上下移动
  delDisRecommend: 'flexible/v1/web/admin/u/p/indexRecommendPictureRelevance/delete', // 推荐图片关联删除
  batchDelDisRecommend: 'flexible/v1/web/admin/u/p/indexRecommendPictureRelevance/batchDelete', // 推荐图片关联批量删除

  // 官方主题配置
  pictureThemeList: 'flexible/v1/web/admin/u/pictureTheme/page', // 官方主题列表
  pictureTheme: 'flexible/v1/web/admin/u/pictureTheme', // 官方主题详情、添加、修改、删除
  pictureThemeRelation: 'flexible/v1/web/admin/u/pictureTheme/relation', // 官方主题与分类进行关联

  // 兑换卡(兑换活动)
  exchangeCardList: 'flexible/v1/web/admin/u/p/exchangeCard/page', // 兑换活动列表
  exchangeCardPoList: 'flexible/v1/web/admin/u/po/exchangeCard/page', // 兑换活动列表（不受权限控制）
  exchangeCard: 'flexible/v1/web/admin/u/p/exchangeCard', // 兑换活动添加、修改、删除
  exchangeCardDetail: 'flexible/v1/web/admin/u/p/exchangeCard/detail', // 兑换活动详情
  exchangeCardStatus: 'flexible/v1/web/admin/u/p/exchangeCard/updateStatus', // 兑换活动状态更新
  qrCode: 'flexible/v1/web/admin/u/p/exchangeCard/qrCode', // 生成二维码
  exchangeItemList: 'flexible/v1/web/admin/u/p/exchangeGeneralItemPool/page', // 分销商通用兑换卡关联的货品信息
  // 兑换码
  exchangeCodeList: 'flexible/v1/web/admin/u/p/exchangeCode/page', // 兑换码列表
  createCode: 'flexible/v1/web/admin/u/p/exchangeCode/createCode', // 生成兑换码
  exchangeCodeExport: 'flexible/v1/web/admin/u/p/exchangeCode/export', // 导出券码
  exchangeCodeInvalid: 'flexible/v1/web/admin/u/p/exchangeCode/invalid', // 作废
  exchangeCodeInvalidBatch: 'flexible/v1/web/admin/u/p/exchangeCode/invalidBatch', // 批量作废
  exchangeTempDownLoad: 'flexible/v1/web/admin/u/p/exchangeCode/tempDownLoad', // 兑换码模板导出
  exchangeImport: 'flexible/v1/web/admin/u/p/exchangeCode/import', // 兑换码excel导入
  exchangeEncodeList: 'flexible/v1/web/admin/u/p/exchangeCode/pageEncode', // 查看暗码列表
  exchangeOrderList: 'order/v1/web/admin/exchangeCard/pageExchange', // 兑换码数据列表
  // 兑换码通知（公告管理）
  exchangeNoticeList: 'flexible/v1/web/admin/u/p/exchangeNotice/page', // 兑换码通知列表
  exchangeNotice: 'flexible/v1/web/admin/u/p/exchangeNotice', // 兑换码通知添加、修改、删除、详情

  // 营销推广
  // 促销活动
  promotionList: 'promotion/v1/web/admin/promotion/list', // 促销活动列表
  promotion: 'promotion/v1/web/admin/promotion', // 促销活动添加、修改、删除、详情
  promotionStatus: 'promotion/v1/web/admin/promotion/status', // 促销活动状态变更
  getPromotionTempUrl: 'promotion/v1/web/admin/promotion/tempDownLoad', // 获取促销活动导入模板URL
  promotionImport: 'promotion/v1/web/admin/promotion/import', // 促销活动导入
  promotionExport: 'promotion/v1/web/admin/promotion/export', // 促销活动导出
  promotionSubmits: 'promotion/v1/web/admin/promotion/submits', // 促销活动批量提交
  deletePromotions: 'promotion/v1/web/admin/promotion/ids', // 促销活动批量删除
  // 优惠券
  couponList: 'promotion/v1/web/admin/coupon/list', // 优惠券列表
  coupon: 'promotion/v1/web/admin/coupon', // 优惠券添加、修改、删除、详情
  couponStatus: 'promotion/v1/web/admin/coupon/status', // 优惠券状态更改
  couponCount: 'promotion/v1/web/admin/coupon/count', // 修改优惠券发放总数量和限购数量
  couponNoList: 'promotion/v1/web/admin/coupon/no', // 券码列表
  couponNoStatus: 'promotion/v1/web/admin/coupon/customer/status', // 根据券码变更客户优惠券状态

  // 拼团秒杀
  groupseckillList: 'promotion/v1/web/admin/groupseckill/list', // 拼团列表
  groupseckill: 'promotion/v1/web/admin/groupseckill', // 拼团添加、修改、删除、详情
  groupseckillStatus: 'promotion/v1/web/admin/groupseckill/status', // 拼团秒杀活动变更状态
  groupseckillSort: 'promotion/v1/web/admin/groupseckill/sort', // 拼团秒杀活动排序

  // 活动审批
  promotionCheckList: 'promotion/v1/web/admin/promotion/check/list', // 促销活动审批
  promotionCheck: 'promotion/v1/web/admin/promotion/check', // 促销活动审批、详情

  // 汇率
  currencyRateList: 'financial/v1/web/admin/currencyRate/list', // 汇率列表
  currencyRate: 'financial/v1/web/admin/currencyRate', // 汇率添加、修改、删除、详情
  currencyRateSync: 'financial/v1/web/admin/currencyRate/po/sync', // 同步汇率
  // 币别
  currencyList: 'financial/v1/web/admin/currency/list', // 币别列表
  currencySync: 'financial/v1/web/admin/currency/po/sync', // 同步币别

  // 平台帐户-微信支付
  platAccountWxList: 'financial/v1/web/admin/platform/accountWx/list', // 微信支付账户列表
  platAccountWx: 'financial/v1/web/admin/platform/accountWx', // 微信支付账户添加、修改、删除、详情
  payTrade: 'financial/v1/web/admin/pay/createTrade', // 支付测试
  accountWxById: 'financial/v1/web/admin/distributor/accountWx/distributorId', // 根据分销商查询收款帐户

  // 支付宝账户
  platAccountAlipayList: 'financial/v1/web/admin/platform/accountAlipay/list', // 支付宝账户列表
  platAccountAlipay: 'financial/v1/web/admin/platform/accountAlipay', // 支付宝账户添加、修改、删除、详情
  // 快钱账户
  platAccountQuickList: 'financial/v1/web/admin/platform/KuaiQian/list', // 快钱账户列表
  platAccountQuick: 'financial/v1/web/admin/platform/KuaiQian', // 快钱账户添加、修改、删除、详情
  // 线下支付账户
  platAccountOfflineList: 'financial/v1/web/admin/platform/offline/list', // 线下支付账户列表
  platAccountOffline: 'financial/v1/web/admin/platform/offline', // 线下支付账户添加、修改、删除、详情

  // 分销商账户-微信支付账户
  accountWxList: 'financial/v1/web/admin/distributor/accountWx/list', // 微信支付账户列表
  accountWx: 'financial/v1/web/admin/distributor/accountWx', // 微信支付账户添加、修改、删除
  accountDetail: 'financial/v1/web/admin/distributor/accountWx/appId', // 微信支付账户详情
  checkDistributorWx: 'financial/v1/web/admin/distributor/accountWx/checkDistributor', // 检查分销商关联微信情况

  // 支付宝账户
  accountAlipayList: 'financial/v1/web/admin/distributor/accountAlipay/list', // 支付宝账户列表
  accountAlipay: 'financial/v1/web/admin/distributor/accountAlipay', // 支付宝账户添加、修改、删除、详情
  checkDistributorAlipay: 'financial/v1/web/admin/distributor/accountAlipay/checkDistributor', // 检查分销商关联支付宝情况
  // 预存款
  rechargeList: 'financial/v1/web/admin/deposit/receipt/list', // 收款列表
  withdrawal: 'financial/v1/web/admin/deposit/withdrawal/list', // 提现列表
  freezingList: 'financial/v1/web/admin/deposit/freezing/list', // 冻结列表
  batchFreezing: 'financial/v1/web/admin/deposit/freezings', // 批量添加冻结
  freezingThaw: 'financial/v1/web/admin/deposit/freezing/thaw', // 解除冻结
  freezingBalance: 'financial/v1/web/admin/deposit/freezing/balance', // 冻结列表获取分销商余额信息

  // 预存款明细
  depositDetailList: 'financial/v1/web/admin/deposit/detail/list', // 明细账列表
  depositAvailableList: 'financial/v1/web/admin/deposit/available/list', // 余额列表
  depositAvailable: 'financial/v1/web/admin/deposit/available/detail', // 余额详情
  depositAvailableSync: 'financial/v1/web/admin/deposit/sync', // 余额同步

  // 销售往来单据
  voucherList: 'financial/v1/web/admin/voucher/list', // 收款单列表
  voucher: 'financial/v1/web/admin/voucher', // 收款单详情
  refundList: 'financial/v1/web/admin/refund/list', // 退款单列表
  refund: 'financial/v1/web/admin/refund', // 退款单添加、修改、删除、详情
  refundApplyList: 'financial/v1/web/admin/refundApply/distributor/list', // 退款单申请列表(分销商申请)
  refundApplyCustomerList: 'financial/v1/web/admin/refundApply/customer/list', // 退款单申请列表(用户申请)
  refundApply: 'financial/v1/web/admin/refundApply/manualConfirmRefundApply', // 退款单申请更新 /退回账户余额/其他操作/拒绝操作
  refundApplyById: 'financial/v1/web/admin/refundApply/list', // 根据订单ID查询退款单订单

  // 订单管理
  orderList: 'order/v1/web/admin/erpOrder/list', // erp订单列表
  orderDetail: 'order/v1/web/admin/erpOrder/detail', // erp订单详情
  getOrderIdByOrderNo: 'order/v1/web/admin/erpOrder/po/getOrderIdByOrderNo', // 根据订单Id查询订单编码
  distributionOrderList: 'order/v1/web/admin/distributionOrder/list', // 分销订单列表
  distributionOrderDetail: 'order/v1/web/admin/distributionOrder/detail', // 分销订单详情
  orderCancel: 'order/v1/web/admin/distributionOrder/cancel', // 根据订单ID取消订单
  orderPromotion: 'order/v1/web/admin/distributionOrder/promotion', // 根据订单活动条件ids获取活动信息
  // C端订单
  customerDiyOrderList: 'order/v1/web/admin/customerDiyOrder/list', // 柔性定制订单列表
  customerDiyOrderDetail: 'order/v1/web/admin/customerDiyOrder/detail', // 柔性定制订单详情
  customerSyncOrderList: 'thirdparty/v1/web/admin/orderBusinessLog/pageSyncOrderLog', // 柔性同步订单列表
  // customerSyncOrderDetail:   'order/v1/web/admin/customerSyncOrder/detail', // 柔性同步订单详情
  // 异常订单
  syncERPFailList: 'order/v1/web/admin/syncERPFail/list', // 同步erp失败订单列表
  syncERPFailDetail: 'order/v1/web/admin/syncERPFail/detail', // 同步erp失败订单详情
  syncERPFailExport: 'order/v1/web/admin/syncERPFail/export', // 导出同步erp失败订单
  syncFactoryFailList: 'order/v1/web/admin/syncFactoryFail/list', // 同步工厂失败订单列表
  syncFactoryFailDetail: 'order/v1/web/admin/syncFactoryFail/detail', // 同步工厂失败订单详情
  syncFactoryFailExport: 'order/v1/web/admin/syncFactoryFail/export', // 导出同步工厂失败订单
  syncUndeliveredFailList: 'order/v1/web/admin/syncUndeliveredFail/list', // 长时间未发货订单列表
  syncUndeliveredFailDetail: 'order/v1/web/admin/syncUndeliveredFail/detail', // 长时间未发货订单详情
  syncUndeliveredFailExport: 'order/v1/web/admin/syncUndeliveredFail/export', // 导出长时间未发货订单

  // 库存锁库订单列表
  orderGoodsStockList: 'order/v1/web/admin/orderGoodsStock/list', // 条件查询库存锁库订单列表

  // 订单统计
  vimOrderList: 'order/v1/web/admin/vimOrderDetail/list', // VMI订单明细列表
  vimOrderDetail: 'order/v1/web/admin/vimOrderDetail/detail', // VMI订单明细详情
  orderInfoExport: 'order/v1/web/admin/orderInfo/export', // 订单明细导出

  // 单据管理
  orderDeliverBillList: 'order/v1/web/admin/orderDeliverBill/list', // 发货单列表
  orderDeliverBill: 'order/v1/web/admin/orderDeliverBill/detail', // 发货单详情
  orderLogistics: 'order/v1/web/admin/orderDeliverBill/logistics/view', // 订单物流查看

  // 订单类型
  orderTypeList: 'order/v1/web/admin/orderType/list', // 订单类型列表
  orderType: 'order/v1/web/admin/orderType', // 订单类型添加、修改、删除、详情

  // 客户管理
  // 合作平台管理 - 系统平台列表（分销商平台接口）
  getSysPlatformList: 'distributor/v1/web/admin/platform/list', // 系统平台列表
  getSysPlatformPoList: 'distributor/v1/web/admin/platform/po/list', // 系统平台列表（不受权限控制）
  sysPlatformDetail: 'distributor/v1/web/admin/platform', // 系统平台详情/添加/更新/删除
  updateSysPlatformStatus: 'distributor/v1/web/admin/platform/open', // 启用/停用系统平台
  // 合作平台管理 - 自有平台列表（分销商系统平台接口）
  getOwnPlatformList: 'distributor/v1/web/admin/sysplatform/list', // 自有平台列表
  getOwnPlatformPoList: 'distributor/v1/web/admin/sysplatform/po/list', // 自有平台列表（不受权限控制）
  ownPlatformDetail: 'distributor/v1/web/admin/sysplatform', // 自有平台详情/添加/更新/删除
  // 合作平台管理 - 微信公众平台列表（分销商微信平台接口）
  getWxPlatformList: 'distributor/v1/web/admin/wxplatform/list', // 微信公众平台列表
  getWxPlatformPoList: 'distributor/v1/web/admin/wxplatform/po/list', // 微信公众平台列表（不受权限控制）
  wxPlatformDetail: 'distributor/v1/web/admin/wxplatform', // 微信公众平台详情/添加/更新/删除

  // 用户管理 - 柔性用户列表（C端客户后台接口）
  getCutomerList: 'distributor/v1/web/admin/customer/list', // 柔性用户列表
  updataCustomerStatus: 'distributor/v1/web/admin/customer/status', // 解冻/冻结柔性用户

  // 系统日志
  systemLogList: 'thirdparty/v1/web/admin/log/list', // 系统日志列表
  operationLogList: 'thirdparty/v1/web/admin/log/distributor/list', // 分销商用户操作日志列表
  orderLogList: 'thirdparty/v1/web/admin/log/order/list', // 订单日志列表
  orderDeliverBillLogList: 'thirdparty/v1/web/admin/log/orderDeliverBill/list', // 发货单日志列表
  voucherLogList: 'thirdparty/v1/web/admin/log/voucher/list', // 收货单日志列表
  refundLogList: 'thirdparty/v1/web/admin/log/refund/list', // 退款单日志列表
  withdrawApplyLogList: 'thirdparty/v1/web/admin/log/withdrawApply/list', // 分销客户提现申请日志列表
  orderBusinessLogList: 'thirdparty/v1/web/admin/orderBusinessLog/page', // 业务日志列表--接口调用日志
  updateAddress: 'thirdparty/v1/web/admin/orderBusinessLog/updateAddress', // 修改订单地址
  msgcenterLogList: '/thirdparty/v1/web/admin/msgcenter/log/list',   //消息发送日志列表
  msgcenterDetail: '/thirdparty/v1/web/admin/msgcenter/detail', //消息详情
  msgcenterSendAgain: '/thirdparty/v1/web/admin/msgcenter/log/send/again',  //消息再次推送
  msgcenterList: '/thirdparty/v1/web/admin/msgcenter/list',  //消息列表
  addMsgcenter: '/thirdparty/v1/web/admin/msgcenter',  //添加消息
  putMsgcenter: '/thirdparty/v1/web/admin/msgcenter',  //修改消息
  deleteMsgcenter: '/thirdparty/v1/web/admin/msgcenter',  //删除消息
  templatelist: '/thirdparty/v1/web/admin/msgcenter/wechat/template/list',  //微信消息模板
  templatePut: '/thirdparty/v1/web/admin/msgcenter/wechat/template',  //微信消息模板更新

  // 订单同步
  syncOrderToERP: 'thirdparty/v1/web/admin/order/syncOrderToERP', // 同步销售单给ERP
  syncOrderToFactory: 'thirdparty/v1/web/admin/order/syncOrderToFactory', // 同步订单到工厂
  syncVouchersToERP: 'thirdparty/v1/web/admin/financial/syncVouchersToERP', // 同步收款单给ERP
  syncDiyDeliveryOrderToERP: 'thirdparty/v1/web/admin/order/syncDiyDeliveryOrderToERP', // 同步发货单给ERP
  syncLogisticsToThird: 'thirdparty/v1/web/admin/order/syncLogisticsToThird', // 同步订单物流给第三方
  syncDistributorToERP: 'thirdparty/v1/web/admin/distributor/sync/distributor', // ERP分销商同步
  syncExpressNoToERP: 'thirdparty/v1/web/admin/order/sync/expressNo', // 同步ERP快递单号
  orderPushAgain: 'thirdparty/v1/web/admin/orderBusinessLog/pushAgain', // 同步订单-柔性
  syncAllItem: 'goods/v1/web/admin/goods/sync/all/item', // 全量同步货品信息
  syncAllItemBox: 'goods/v1/web/admin/goods/sync/all/item/box', // 全量同步货品装箱信息
  syncAllPrice: 'goods/v1/web/admin/goods/sync/all/price', // 全量同步商品价格
  syncBatchItem: 'goods/v1/web/admin/goods/sync/batch/item', // 同步货品信息
  syncBatchItemBox: 'goods/v1/web/admin/goods/sync/batch/item/box', // 同步货品装箱信息
  syncBatchPrice: 'goods/v1/web/admin/goods/sync/batch/price', // 同步商品价格
  syncRefundBillToERP: 'thirdparty/v1/web/admin/financial/syncRefundBillToERP', // 同步退款单给ERP

  // 兑换卡营销专题
  exchangeSpecial: 'flexible/v1/web/admin/u/p/exchangeSpecial', // 新增/修改/启用/停用专题
  exchangeSpecialList: 'flexible/v1/web/admin/u/p/exchangeSpecial/page', // 专题列表
  exchangeSpecialDetail: 'flexible/v1/web/admin/u/p/exchangeSpecial/detail', // 专题详情
  exchangeSpecialDistriList: 'flexible/v1/web/admin/u/p/exchangeSpecial/distributor/page', // 专题列表 - 查看分销商
  exchangeSpecialDistri: 'flexible/v1/web/admin/u/p/exchangeSpecial/distributor', // 专题修改 - 添加/启用/停用分销商
  exchangeSpecialDistriQrcode: 'flexible/v1/web/admin/u/p/exchangeSpecial/specialDistributor/qrCodeUrl', // 查看二维码
  exchangeSpecialDistriShortLink: 'flexible/v1/web/admin/u/p/exchangeSpecial/specialDistributor/shortLink', // 查看短链接
  exchangeSpecialDistriLink: 'flexible/v1/web/admin/u/p/exchangeSpecial/specialDistributor/link', // 查看专题链接

  // 兑换卡转发活动配置
  exchangeShare: 'flexible/v1/web/admin/u/p/exchangeShare', // 新增/修改/启用/停用/删除转发活动
  exchangeShareList: 'flexible/v1/web/admin/u/p/exchangeShare/page', // 转发活动列表
  exchangeShareDetail: 'flexible/v1/web/admin/u/p/exchangeShare/detail', // 转发活动详情


  // 电子兑换卡导出
  exchangeExport: 'flexible/v1/web/admin/u/p/exchangeExport', // 新增/修改/审核/删除电子兑换卡导出
  exchangeExportList: 'flexible/v1/web/admin/u/p/exchangeExport/page', // 电子兑换卡导出列表
  exchangeExportOut: 'flexible/v1/web/admin/u/p/exchangeExport/out', // 导出电子兑换卡

  getDistributorByIds: 'distributor/v1/web/admin/distributor/po/ids', // 根据ids查找分销商基本数据


  // 批量导入订单
  importOrderDetail: 'order/v1/web/admin/importOrder', // 导入订单详情/修改
  deleteImportOrder: 'order/v1/web/admin/importOrder/delete', // 删除导入项
  importOrder: 'order/v1/web/admin/importOrder/import', // 订单导入接口
  getImportOrder: 'order/v1/web/admin/importOrder/list', // 导入订单列表
  ordersImportOrder: 'order/v1/web/admin/importOrder/orders', // 导入订单批量下单
  getTempDownload: 'order/v1/web/admin/importOrder/tempDownLoad', // 获取下载模板URL

  // 权益兑换列表
  getEquityList: 'thirdparty/v1/web/admin/quanyi/page', // 权益兑换列表
  getEquityLogList: 'thirdparty/v1/web/admin/quanyi/log/page', // 权益兑换日志列表
  handleEquityStatus: 'thirdparty/v1/web/admin/quanyi/cancel', // 权益作废
  handleEquityStatusCancel: 'thirdparty/v1/web/admin/quanyi/unCancel', // 权益恢复

  // 判断当前订单状态
  getOrderStatus: 'order/v1/web/admin/orderInfo/judgeDistributorOrderStatus', // 判断当前订单状态

  // 停发列表（快递停发区域）
  getDeliveryStopList: 'order/v1/web/admin/orderDeliverStopPlace/list', // 停发列表
  handleDeliveryStop: 'order/v1/web/admin/orderDeliverStopPlace', // 添加/更新/删除

  // 代金券
  batchCreateVoucher: 'promotion/v1/web/admin/rebateVoucher/batchCreate',  // 批量新增返利代金券
  getVoucherList: 'promotion/v1/web/admin/rebateVoucher/list',  // 根据搜索条件查找返利代金券列表(分页)
  handleVoucherStatus: 'promotion/v1/web/admin/rebateVoucher',  // 更新代金券（冻结/解冻）
  getVoucherUsedList: 'promotion/v1/web/admin/rebateVoucher/listUsageRecord',  // 代金券使用记录列表(分页)
  voucherDownloadTemp: 'promotion/v1/web/admin/rebateVoucher/tempDownLoad', // 获取下载模板URL
  voucherImport: 'promotion/v1/web/admin/rebateVoucher/import', // 返利金导入接口

  // 兑换码
  exchangeUnbound: 'flexible/v1/web/admin/u/p/exchangeCode/unboundExchange', // 解绑

  // 产品推广（商店配置 - 商品推广）
  proPromotionList: 'system/v1/web/admin/promotion/goodsPromotionList', // 商品推广列表
  addProPromotion: 'system/v1/web/admin/promotion/addGoodsPromotion', // 添加商品推广
  proPromotionDetail: 'system/v1/web/admin/promotion/getGoodsPromotionDescById', // 根据推广商品id获取推广商品详情
  handleProPromotion: 'system/v1/web/admin/promotion/updateGoodsPromotion', // 修改商品推广
  deleteProPromotion: 'system/v1/web/admin/promotion/deleteGoodsPromotionById', // 根据商品推广id删除商品推广记录
  invalidProPromotion: 'system/v1/web/admin/promotion/invalidGoodsPromotion', // 商品推广记录作废

  // 同步物流
  handleSyncLogistics: 'thirdparty/v1/web/open/common/deliverOrder', // 手动输入同步物流信息
  handleSyncLogisticsById: 'thirdparty/v1/web/admin/order/synchronizedLogisticsByOrderId', // 根据订单Id同步物流信息
}
