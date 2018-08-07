/*
 * @Author: 施义煌
 * @Date: 2018/10/15 16:53
 * @Last Modified by: 施义煌
 * @Last Modified time: 2018/10/15 16:53
 */
module.exports = {
    P: { // 通用/公共
        PCSCTN: 'PCS/CTN', // 装箱规格
        Carton: 'Inner / Master carton', // 箱子类型
        DIM: 'DIM （L/W/H）', // 尺寸(长*宽*高)
        QTY: 'QTY', // 装箱数量(个)
        WeighG: 'Weight', // 重量
        SoldOut: 'Sold out', // 下架
        OutStock: 'out of stock', // 缺货
        CanBook: 'Can to booking', // 可预售
        Prompt: 'Prompt', // 提示
        ConfirmLogout: 'Confirm logout', // 确定登出
        ConfirmOrder: 'Confirm the order', // 确定下单
        LogAgain: 'Log in again', // 重新登录
        RS: 'bat', // bat
        HomePage: 'Home Page', // 首页
        Login: 'Log in', // 账号登录
        Register: 'Register', // 注册
        AccountFrozen: 'Frozen', // （账号）已冻结
        Order: 'Order', // 订单
        Hello: 'Hello', // 您好
        More: 'More', // 更多
        Search: 'Search', // 搜索
        Cart: 'Cart', // 我的购物车
        Email: 'Email', // 邮箱
        WeChatOA: 'WeChat Official Account', // 微信公众号
        ForgotPassword: 'Forgot password', // 忘记密码
        CustomerService: 'Customer Service', // 客服中心
        SignOut: 'Sign Out', // 登出
        MemberCenter: 'Member Center', // 会员中心
        Visitor: 'Visitor', // 游客
        Detail: 'Detail', // 查看详情
        Username: 'Username', // 用户名
        Mobile: 'Mobile', // 手机号
        Password: 'Password', // 密码
        Select: 'Select', // 请选择
        Collect: 'Collect', // 收藏，
        CancelCollect: 'Cancel Collect', // 取消收藏
        Buy: 'Buy', // 下单
        NoPricing: 'No pricing', // 暂未定价
        Calculating: 'Calculating', // 正在计算
        BuyNow: 'Buy Now', // 立即购买
        AddToCart: 'Add To Cart', // 加入购物车
        ViewAfterLogin: 'View after login', // 登录后查看
        OK: 'Submit', // 提交
        Cancel: 'Cancel', // 取消
        Canceled: 'Canceled', // 已取消
        Next: 'Next', // 下一步
        Back: 'Back', // 返回
        BackHome: 'Back Homepage', // 返回首页
        PleaseEnter: 'please enter', // 请输入
        pleaseSelect: 'please select', // 请选择
        ConTran: 'Containing in-transit inventory', // 包含在途库存
        Inventory: 'Inventory', // 库存
        Dimension: 'Dimension', // 尺寸
        Weight: 'Weight', // 重量
        Total: 'Total', // 合计
        ChooseGift: 'Gifts List', // 选择赠品
        Receive: 'You can receive', // 可领取
        MostPieces: 'pieces at most', // 最多件
        RMB: '$', // 元
        Pieces: 'pieces', // 件
        HaveSelected: 'and you have selected', // 已选
        NoData: 'No data', // 暂无数据
        ShortInventory: 'short of inventory', // 库存不足
        Commodities: 'Commodities', // 商品
        NoCommodities: 'No commodities', // 暂无商品
        AdvanceSales: ' Advance Sales', // 预售
        Payment: 'payment', // 付款
        Payments: 'payment', // 支付
        Alipay: 'Alipay', // 支付宝
        WechatPay: 'Wechat Pay', // 微信支付
        RangeCheckout: 'Range checkout', // 区间结算
        BankTransfer: 'Bank transfer', // 银行转账
        OfflineTransfer: 'Offline Transfer', // 线下支付
        AggregatePriceA: 'Total price of commodities', // 商品总价
        AggregatePriceB: 'Discount amount of goods', // 商品折扣金额
        AggregatePriceC: 'Category discount of goods', // 商品分类折扣
        AggregatePriceD: 'Discount amount of order', // 订单折扣金额
        AggregatePriceE: 'Increasing rebate on pick-up', // 提货增长返利
        AggregatePriceF: 'Delivery charge', // 配送费用
        AggregatePriceG: 'Total order amount', // 订单总金额
        FreeShipping: 'Free shipping', // 包邮
        Default: 'Default', // 默认
        Yes: 'Yes', // 是
        No: 'No', // 否
        Modify: 'Modify', // 修改
        Save: 'Save', // 保存
        AllResults: 'All results', // 全部结果
        DafaultSort: 'Dafault Sort', // 综合排序
        LaunchTime: 'Launch Time', // 上架时间
        Bestselling: 'Bestselling', // 销量
        PictureDisplay: 'Picture Display', // 图片展示
        QuickOrder: 'Quick Order', // 快速订货
        AddShippingAddress: 'Add Shipping Address', // 添加收货地址
        ModifyShippingAddress: 'Modify Shipping Address', // 修改收货地址
        EditShippingAddress: 'Edit Shipping Address', // 编辑收货地址
        GoodsItemNull: 'The order quantity of products cannot be null.', //货品订购数量不能为空
        GoodsItemQuantityError: 'The order quantity of products shall not be greater than inventory.', //货物数量不能大于库存
        RequestTimedOut: 'Request timed out, please try again!', // 请求超时，请重试！
        NetworkAnomaly: 'Network abnormality, please check the network!', // 网络异常,请检查网络
    },
    Login: { // 登录页
    },
    Register: { // 注册页
        RegistrationAgreement: 'Registration Agreement', // 会员注册协议
        AgreeAndContinue: 'Agree and continue', // 同意并继续
        AlreadyAccount: 'Already have an account ?', // 已有账号
        Step: { // 注册步骤
            One: 'Verify Mailbox', // 验证邮箱
            OneOther: 'Reset Password', // 重置密码
            Two: 'Enter Basic Information',
            Three: 'Enter Account Information',
            Four: 'Register Successfully',
            FourOther: 'Modify Successfully'
        },
        BasicInformation: 'Basic Information', // 基本信息
        AccountInformation: 'Account Information', // 账户信息
        EnterPhoneCode: 'Please enter E-mail verification code', // 请输入手机验证码
        GetVerificationCode: 'Get verification code', // 获取验证码
        LoginMobile: 'Login mobile', // 登录手机
        Username: 'Username', // 用户名
        Password: 'Password', // 密码
        ComfirmPassword: 'Comfirm Password', // 确认密码
        CustomerName: 'Customer Name', // 客户名称
        BusinessLicense: 'Business License Number/ ID No', // 营业执照号/身份证号
        Contact: 'Contact', // 客户联系人
        Gerder: 'Gerder', // 性别
        Male: 'Male', // 男
        Female: 'Female', // 女
        ContactPhone: 'Contact Phone', // 客户联系人电话
        Country: 'Country', // 国家
        ProvincesCity: 'Provinces and cities', // 省市
        ContactAddress: 'Contact Address', // 客户联系地址
        EmailAddress: 'Email Address', // 电子邮件
        ZipCode: 'Zip Code', // 邮编
        CompanyType: 'Company Type', // 公司类型
        Company: 'Company', // 公司
        IndividualBusiness: 'Individual Business', // 个体工商户
        Individual: 'Individual', // 个人
        TaxType: 'Tax receipt type', // 税票类型
        GeneralTaxpayer: 'General taxpayer', // 一般纳税人
        SmallTaxpayer: 'Small-scale taxpayer', // 小规模纳税人
        ReconciliationInformation: 'Reconciliation information', // 对账信息
        ReconciliationName: 'Reconciliation personnel name', // 对账人姓名
        ReconciliationPhone: 'Reconciliation personnel phone', // 对帐人联系电话
        ReconciliationEmail: 'Reconciliation personnel e-mail', // 对帐人邮箱
        BillingInformation: 'Billing information', // 开票信息
        IdentificationNumber: 'Taxpayer Identification Number', // 纳税人识别号
        Telephone: 'Telephone', // 固定电话
        Address: 'Address', // 地址
        Bank: 'Bank', // 银行
        BankInformation: 'Bank account information', // 银行账户信息
        BankAccountName: 'Bank Account Name', // 银行账户名
        BankDeposit: 'Bank of Deposit', // 开户行
        BankAccount: 'Bank Account' // 银行账户
    },
    Index: { // 首页
        NewMessage: 'New Message', // 最新公告
        AllProductCategories: 'All product categories', // 所有商品分类
        Customized: 'Customized', // 定制
        NewProduct: 'New product', // 新品
        Activities: 'Activities', // 活动
        Discount: 'Discount', // 折扣活动
        Reach: 'Reach', // 已满
        Target: 'Target' // 目标收货人
    },
    Foot: { // 底部
        ShoppingDirectory: 'Shopping Directory', // 购物指南
        ShoppingProcess: 'Shopping Process', // 购物流程
        CustomerReading: 'Customer Reading', // 顾客必读
        MemberIntroduction: 'Member Introduction', // 会员介绍
        DeliveryMethod: 'Delivery Method', // 配送方式
        DeliveryService: 'Delivery Service', // 配送服务
        DeliveryServiceQuery: 'Delivery Service Query', // 配送服务查询
        DeliveryChargeStandard: 'Delivery Charge Standard', // 配送费收取标准
        PaymentMethod: 'Payment Method', // 支付方式
        OnlinePayment: 'Online Payment', // 在线支付
        BillingDetails: 'Billing Details', // 开票细节
        AfterSalesService: 'After-sales Service', // 售后服务
        CustomerService: 'Customer Service', // 客服中心
        AfterSalesPolicy: 'After-sales Policy', // 售后政策
        AboutUs: 'About us', // 关于我们
        LatestAnnouncement: ' Latest Announcement', // 最新公告
        PromotionInformation: 'Promotion Information', // 促销信息
        Service: 'Service', // 服务区
        ServiceScope: '', // 服务范围
        CompanyInfoHead: '', // 公司信息头
        CompanyInfo: '' 
    },
    FormCheck: { // 各类表单验证
        MobileEmpty: 'Please enter your username', // 请输入用户名或手机号
        PasswordEmpty: 'Please enter your password', // 请输入密码
        PasswordLength: 'Password length cannot be less than 6 digits' // 密码长度不能少于6位数
    },
    HelpCenter: { // 客户服务中心
        CustomerService: 'Customer Service', // 客服服务
        T0: 'Shopping Process', // 购物流程
        T1: 'Customer Reading', // 顾客必读
        T2: 'Member Introduction', // 会员介绍
        T3: '', // 配送服务
        T4: '', // 配送服务查询
        T5: '', // 配送费收取标准
        T6: 'Payment Method', // 支付方式
        T7: 'Billing Details', // 开票细节
        T8: '', // 售后政策
        T9: '', // 退款说明
        T10: 'Customer Service Center', // 客服中心
        T11: 'After-sale Policy' // 售后政策
    },
    ShopCarts: { // 购物车
        SelectAll: 'Select All', // 全选
        ShopCar: 'Cart', // 购物车
        Common: 'Common Commodities', // 普通商品
        AdvanceSales: 'Commodities Subject to Advance Sales', // 预售商品
        Customized: 'Customized Commodities', // 定制商品
        HaveBeenAdded: 'Commodities already in the shopping cart', // 已放入购物车的商品
        Picture: 'Picture', // 图片
        ItemNo: 'Product No', // 货品编码
        ItemName: 'Product Name', // 货品名称
        BarCode: 'Bar Code', // 条形码
        Spe: 'Specification', // 规格
        Colors: 'Color', // 颜色
        MemPrice: 'Member Price', // 会员价
        RecommendedPrice: 'Recommended retail price', // 建议零售价
        DiscountPrice: 'Discount Price', // 优惠价
        Total: 'Total', // 合计
        Quantity: 'Quantity', // 数量
        InventoryQuantity: 'Inventory quantity', // 在库数量
        IntransitQuantity: 'In-transit quantity', // 在途数量
        AllQuantity: 'Quantity/Inventory quantity In-transit quantity', // 在库、在途数量
        Operation: 'Operation', // 操作
        Expired: 'expired', // 失效
        BatDelete: 'Delete in batches', // 批量删除
        Update: 'Update shopping cart', // 更新购物车
        Empty: 'Empty shopping cart', // 清空购物车
        ShopcarEmpty: 'The shopping cart is absolutely empty.', // 购物车空空如也
        Around: 'Shopping around', // 到处逛逛
        ConShopping: 'Continue shopping', // 继续购物
        TotalWeight: 'Total weight', // 商品总重
        ke: 'g', // 克
        ShopSum: 'product quantity', // 商品数量
        TotalSuch: 'Total of such commodities', // 此笔商品总计
        Checkout: 'Go to the checkout', // 去结账
        GoAdd: 'Go to add', // 去凑单
        Delete: 'Delete', // 删除
        Switch: 'Switch', // 切换
        SwitchSum: 'Choose Promotion', // 切换活动
        View: 'View images', // 查看图片
        ViewGifts: 'View gifts', // 查看赠品
        Ordered: 'Quantity Ordered', // 调整数量
        DeleteItem: 'DELETE', // 删除货品
        Rule: 'Rule', // 规则
        RuleDetails: 'Rule Details', // 详细规则
        RuleName: 'Rule', // 规则名称
        RuleDescription: 'Rule description', // 规则描述
        Condition: 'Condition ', // 条件
        Participate: 'Participating commodities', // 参与规则商品
        NoAvailableA: 'In-transit goods No available sales campaign', // 在途商品不参与活动
        NoAvailableB: 'Commodities Subject to Advance Sales No available sales campaign', // 预售商品不参与活动
        NoAvailableC: 'Customized Commodities No available sales campaign', // 定制商品不参与活动
        Enjoy: 'enjoy from order activities', // 与订单活动同享
        Different: 'Different enjoy from order activities', // 与订单活动不同享
        EmptyShopCarts: 'This operation will empty the shopping cart, whether to continue', // 此操作将清空购物车, 是否继续
        SubmitGiveaway: 'Submit giveaway successfully', //提交赠品成功
        SuccessfullyDeleted: 'Successfully deleted', // 删除成功
        DatchesDeleteoOperation: 'This operation will delete the goods in batches. Do you want to continue?', // 此操作将批量删除货品, 是否继续?
        PleaseTickShoppingCart: 'Please tick the item', // 请勾选购物车货品
        UpdateCompleted: 'update completed!', // 更新成功!
        SelectProductsHint: 'Sorry, please select the products you need to buy first!', // 抱歉，请先选择需要购买的商品！
        OrderSeparatelyHint: 'General merchandise, custom merchandise and pre-sale merchandise are not allowed to place orders at the same time, please order separately', //普通商品、定制商品和预售商品不允许同时下单，请分开下单
    },
    Activitys: { // 活动栏目
        Activity: 'Activity', // 活动
        InTransitGoods: 'In-transit goods', // 在途商品
        CommoditiesAdvanceSales: 'Commodities Subject to Advance Sales', // 预售商品
        CustomizedCommodities: 'Customized Commodities', // 定制商品
        ActivityList: 'List of activities', // 活动列表
        Search: 'Search for product name/No', // 搜索货品名称/编号/商品名称
        NoAvailable: 'no available sales campaign, please check carefully when making a list.', // 不参与活动，凑单时请仔细检查。
        OnlyGift1: 'only',
        OnlyGift2: 'gift left',
        NotEnoughGifts: 'Not enough gifts left!', // 剩余赠品数不足!
        UpperLimitGifts: 'The number of gifts has reached the upper limit!', // 赠品数量已达上限!
    },
    ConfirmOrder: {
        Edit: 'Edit', // 编辑
        MoreAddress: 'More address', // 更多地址
        ShippingAddress: 'Shipping Address', // 填写收货人信息
        OrderingList: 'Ordering List', // 购买的商品列表
        ChooseDeliveryMethod: 'Choose Delivery Method', // 选择配送方式
        ChoosePaymentMethod: 'Choose Payment Method', // 选择支付方式
        DeliveryCharge: 'Delivery charge', // 配送费
        Remarks: 'Remarks', // 订单留言
        DefaultAddress: 'Default', // 默认地址
        AmountInstock: 'Order amount of in-stock goods', // 在库商品总额
        AmountIntransit: 'Order amount of in-transit goods', // 在途商品总额
        SubmitOrder: 'Submit Order', // 确认无误，下订单
        BalancePaid: 'balance paid', // 余额支付
        SelectConsigneeAddress: 'Please select the consignee address information', // 请选择收货人地址信息
        ShippingAddress: 'The current shipping address does not support this shipping method, please select a shipping method again', //当前收货地址不支持该配送方式，请重新选择配送方式
    },
    OrderSuccess: {
        SendMan: 'RecipientName', // 收货人
        Amount: 'Order Amount', // 订单金额
        OrderInstock: 'Order of in-stock goods', // 在库订单
        OrderIntransit: 'Order of in-transit goods', // 在途订单
        OrderCustomized: 'Order of customized goods', // 定制订单
        More: 'More', // 查看更多
        TotalOrder: 'Total order', // 订单总额
        MergePayments: 'Merge payments', // 合并付款
        PayResults: 'Pay for results', // 支付结果
        PaySuccess: 'Pay for success', // 支付成功
        OrderAdvance: 'Order subject to advance sales', // 预售订单
        LogisticsNo: 'Logistics order No.', // 物流单号
        NoLogisticsInfo: 'No logistics information', // 暂无物流信息
        ExportList: 'Export purchasing list', // 导出购买清单
        ViewActivityDetails: 'View detail of the activity', // 查看活动详情
        ActivityDetails: 'Detail of the activity', // 活动详情
        UnableList: 'Unable to purchase list', // 无法购买列表
        Status: 'Status', // 状态
        Title: 'Order successfully', // 下单成功
        orderSuccess: 'Order submitted successfully', // 订单提交成功 
        OrderPlaced: 'Order placed, please pay', // 您的订单提交成功！请将款项汇至以下银行账户
        BeneficiaryCompanyName: '', // 收款人全称
        BankName: "", // 收款人银行名称
        Beneficiary: "", //银行账号
        BankAddress: "", // 收款人银行地址
        ContactCustomer: 'Please contact your dedicated customer service and we will deal with it as soon as possible.', //汇款完成后请联系您的专属客服，我们将尽快为您处理。
        PaymentInformation: 'Payment Information', // 付款信息
    },
    UserCenter: { // 个人中心
        PurchaseCenter: 'Purchase Center', // 采购中心
        Quotation: 'Quotation', // 报价单
        OrderCenter: 'Order Center', // 订单中心
        FollowCenter: 'Following Center', // 关注中心
        Favorites: 'My Favorites', // 我的收藏
        DIY: 'My DIY Creations', // 我的DIY作品
        Asset: 'Asset Center', // 资产中心
        AssetDetails: 'Asset Details', // 资产明细
        Recharge: 'Recharge', // 充值
        Cash: 'Cash Withdrawal', // 提现
        Set: 'Personal Settings', // 个人设置
        Information: 'Personal Information', // 个人信息
        AccountManagement:'Account management',   //账号管理
        Address: 'Shipping Address', // 收货地址
        Password: 'Modify the Password', // 修改密码
        OrderNo: 'Order No', // 订单号
        RecipientName: 'Recipient Name', // 收货人姓名
        Data: 'Data', // 起始时间
        Tel: 'Tel', // 联系电话
        Phone: 'phone number', // 联系手机
        ContactAddress: 'Address', // 联系地址
        OrderQuery: 'Order query', // 查询订单
        AdvancedSearch: 'Advanced search', // 高级搜索
        AllOrders: 'All Orders', // 全部订单
        PendingPayment: 'Pending payment', // 待付款
        PartOfPayment: 'Part of payment', // 部分付款
        ToBeConfirmed: 'To be confirmed', // 待确认
        ToBeShipped: 'To be shipped', // 待发货
        PartOfShipped: 'Part of shipped', // 部分发货
        Shipped: 'shipped', // 已发货
        Paid: 'Paid', // 已付款
        Refunded: 'Refunded', // 已退款
        PartOfRefunded: 'Part of Refunded', // 部分退款
        Refunding: 'Refunding', // 退款中
        Closed: 'Closed', // 已关闭
        Completed: 'completed', // 已完成
        OrderDate: 'Order Date', // 下单日期
        OrderTime: 'Order Time', // 下单时间
        TotalAmount: 'Total Amount', // 总金额
        OrderStatus: 'Order Status', // 订单状态
        PaymentStatus: 'Payment Status', // 付款状态
        Operation: 'Operation', // 操作
        OrderAgain: 'Order again', // 再来一单
        ViewDetails: 'View details', // 查看详情
        OrderDetails: 'Order Details', // 订单详情
        SongShop: 'Gift', // 满送商品
        SendInformation: 'Recipient Information', // 收货人信息
        ShippingAddress: 'Shipping Address', // 配送地区
        ZipCode: 'Zip Code', // 邮编
        DeliveryMethod: 'Delivery Method', // 配送方式
        CommodityWeight: 'Commodity Weight', // 商品重量
        PaymentCurrency: 'Payment Currency', // 付款方式
        Remarks: 'Remarks', // 订单留言
        RegistrationTime: 'Registration Time', // 登记时间
        ProcessingRemarks: 'Processing Remarks', // 处理备注
        CreationTime: 'Creation Time', // 创建时间
        Alls: 'All Orders', // 所有
        ExpenditureAmount: 'Expenditure amount', // 退款
        Time: 'Time', // 时间
        Event: 'Event', // 事件
        DepositMmount: 'Deposit amount', // 存入金额
        ExpenditureMmount: 'Expenditure amount', // 支出金额
        AccountBalance: 'Account Balance', // 当前金额
        AccountMoney: 'Account Balance', // 账户余额
        AvailableBalance: 'Available Balance', // 可用余额
        rechargeMmount: 'recharge amount', // 充值金额
        CashWithdrawalAmount: 'Cash Withdrawal Amount', // 提现金额
        Remarkes: 'Remarks', // 备注
        CashWithdrawalAccount: 'Cash Withdrawal Account', // 提现账号
        ChoosePayment: 'Choose Payment Method', // 选择支付方式
        PaymentAccount: 'Payment account', // 收款账号
        PaymentNo: 'Payment voucher No', // 支付凭证序号
        PaymentVoucher: 'Payment voucher', // 支付凭证
        UploadVoucher: 'Upload voucher', // 上传图片
        SubmitApplication: 'Submit Application', // 提交申请
        ResetForm: 'Reset form ', // 重置表单
        MakeDefault: 'Make Default', // 设为默认
        Location: 'Location', // 所在地区
        OriginalPassword: 'Original Password', // 原密码
        NewPassword: 'New Password', // 新密码
        ConfirmPassword: 'Confirm Password', // 确认密码
        ConfirmModify: 'Confirm modification', // 确认修改
        Empty: 'Empty', // 清空
        Shortage: 'Shortage Registration', // 缺货登记
        DeletedProductCollection: 'Product collection deleted successfully!', // 商品收藏删除成功!
        OperationDeletedProductCollection: 'This operation deletes the product collection. Do you want to continue?', //此操作将该商品收藏删除, 是否继续?
        OperationDeleteDiy: 'This operation will delete the Diy work. Do you want to continue?', // 此操作将删除该Diy作品, 是否继续?
        OperationDeletedProductShortage: 'This operation will delete the item. Do you want to continue?', // 此操作将删除该货品, 是否继续?
        RechargeSuccess: 'Submit your application successfully, please be patient!', //提交申请成功,请耐心等待！
        PaymentVoucher: 'Please upload payment voucher!', //请上传支付凭证！
        PaymentVoucherNumber: 'Please enter the payment voucher serial number!', // 请输入支付凭证序号！
        RechargeAmount: 'Please enter the recharge amount!', // 请输入充值金额！
        PaymentSuccessful: 'Payment successful!', //支付成功！
        PaymentTimedOut: 'Payment timed out!', // 支付超时！
        SelectionField: 'Selection field：', // 选择字段
        InventoryNameNo: 'Inventory name / inventory No', // 存货名称/存货编号
        CommodityClassification: 'Commodity classification', // 商品分类
        CommodityType: 'Commodity type', // 商品类型
        ExportProductList: 'Export product list', // 导出商品列表
        SelectAll: 'Select all', // 全部选择
        InventoryNo: 'Inventory No', // 存货编码
        InventoryName: 'Inventory Name', // 存货名称
        PriceRelated: 'Price related', // 价格相关
        SeeMoreParameters: 'See more parameters', // 查看更多参数
        Ordinary: 'Ordinary', // 普通
        Customized: 'Customized', // 定制
        InventoryCodeRequired: 'Inventory code required', //存货编码必选
        ExportRequest: 'The quotation export request has been submitted successfully. The data volume may be large. Please do not close the browser during data download, but you can do other things at the same time. Thank you!', // 报价单导出请求提交成功，数据量可能较大，数据下载期间，请勿关闭浏览器，但您可同时做其他事项，谢谢！
        QuotationDetails: 'Quotation details', //报价单明细
        PleaseSelect: 'Please select the fields to export first', //请先选择需要导出的字段
        SelectPage: 'Select page：', // 选择页码
    },
    ShopDetail: {
        Customized: 'Custom', // 定制
        Proceed: 'You can proceed', // 您可以进行
        OrChoose: 'Or choose', // 或选择
        Promotion: 'Promotion', // 参与活动
        CommodityDes: 'Commodity Description', // 商品描述
        PurchasingList: 'Purchasing List', // 采购清单
        SalesVolume: 'Sales volume', // 销量
        LaunchTime: 'Launch time', // 上架时间
        Brand: 'Brand', // 品牌
        Category: 'Category', // 品类
        CommodityName: 'Commodity Name', // 商品名称
        CommodityNo: 'Commodity No', // 商品编号
        CommodityPicture: 'Commodity Picture', //商品图片
        PackingPicture: 'Packing Picture', // 包装图
    },
    Message: {
        Confirm: 'OK', // 确定
        Cancel: 'Cancel', // 取消
        One: 'You have not chosen the gift yet. You can cancel and re-choose the gif', // 您还未选择赠品，可以取消重新选择赠品
        Two: ' or make sure you do not need the gif' // 确定不需要赠品
    },
    Promotion: {
        SelectPromotion: 'choice activity', //选择活动
        RuleDetails: 'Rule details', //规则详情
        SurplusQuantity: 'Surplus quantity', //剩余数量
        MinimumPurchase: 'Minimum purchase', //最小购买量
        TotalQuantity: 'Total Quantity', //总购买数量
        NoActivity: 'Products not participating in the activity', //未参与活动商品
        PromotionHint: 'PromPlease allocate products within the selected activities. Unassigned products and quantities will be allocated by the system when the order is submitted. For the activities shared with the order, please give priority to the commodity activities. The system will automatically synchronize during settlement.otion', // 请在选择参加的活动内分配产品，未分配的产品及数量，会在提交订单时由系统分配活动。与订单同享的活动，请优先设置商品活动，结算时，系统会自动同步。
        AllQuantity: 'Quantity/Inventory quantity In-transit quantity', //数量/在库 在途
        BeforeDiscount: 'If the amount before discount reaches', //折扣前金额满
        AfterDiscount: 'When the amount after discount reaches', //折扣后金额满
        ParticipateDiscount: 'yuan,participate in discount activities', // 元,参与折扣活动
        QuantityReaches: 'When the quantity reaches', // 数量满
        ParticipatePieces: 'pieces,participate in discount activities', //件,参与折扣活动
    }
}