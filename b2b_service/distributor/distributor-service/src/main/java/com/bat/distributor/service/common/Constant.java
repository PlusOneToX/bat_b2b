package com.bat.distributor.service.common;

public class Constant {

    /**
     * 可视范围常量 0 不指定 1 指定全部 2 指定分销商等级 3 指定分销商 4 指定销售部门 5 指定业务员
     */
    public static final Short SCOPE_NULL = 0;
    public static final Short SCOPE_ALL = 1;
    public static final Short SCOPE_SCALE_PRICE = 2;
    public static final Short SCOPE_DISTRIBUTOR = 3;
    public static final Short SCOPE_DEPARTMENT = 4;
    public static final Short SCOPE_ADMIN = 5;

    /**
     * 停用启用状态 1 启用 0 停用
     */
    public final static Short OPEN_NO = 0;
    public final static Short OPEN_YES = 1;

    /**
     * 操作类型 1 新增 2 修改 3 删除
     */
    public final static Short OPERATION_TYPE_1 = 1;
    public final static Short OPERATION_TYPE_2 = 2;
    public final static Short OPERATION_TYPE_3 = 3;

    /**
     * 货品规格类型 1.规格，2.颜色
     */
    public final static Short ATTRIBUTE_TYPE_1 = 1;
    public final static Short ATTRIBUTE_TYPE_2 = 2;

    /**
     * 上下架状态 1未上架，2审批中，3已上架
     */
    public final static Short SALE_STATUS_1 = 1;
    public final static Short SALE_STATUS_2 = 2;
    public final static Short SALE_STATUS_3 = 2;

    /**
     * 冻结状态，1未冻结，2冻结
     */
    public final static Short FREEZE_STATUS_1 = 1;
    public final static Short FREEZE_STATUS_2 = 2;

    /**
     * 来源类型 1.后台添加 2.前台注册申请 3.分销邀请
     */
    public final static Short APPLY_TYPE_1 = 1;
    public final static Short APPLY_TYPE_2 = 2;
    public final static Short APPLY_TYPE_3 = 3;

    /**
     * 资质申请状态 0未提交 1申请中 2申请通过 3申请失败
     */
    public final static Short APPLY_STATUS_0 = 0;
    public final static Short APPLY_STATUS_1 = 1;
    public final static Short APPLY_STATUS_2 = 2;
    public final static Short APPLY_STATUS_3 = 3;

    /**
     * 资料审核状态 0未提交 1资料审核中 2资料审核通过 3资料审核失败
     */
    public final static Short PROFILE_STATUS_0 = 0;
    public final static Short PROFILE_STATUS_1 = 1;
    public final static Short PROFILE_STATUS_2 = 2;
    public final static Short PROFILE_STATUS_3 = 3;

    /**
     * 地址类型 1.公司地址 2.收货地址
     */
    public final static Short ADDRESS_TYPE_1 = 1;
    public final static Short ADDRESS_TYPE_2 = 2;

    /**
     * 货币类型 1-人民币 2-美元
     */
    public final static Short COIN_TYPE_1 = 1;
    public final static Short COIN_TYPE_2 = 2;

    /**
     * 是否账号拥有: 0 否, 1 是
     */
    public final static Short OWNER_FLAG_0 = 0;
    public final static Short OWNER_FLAG_1 = 1;

    /**
     * 性别 0,未设置, 1,男 2,女
     */
    public final static Short SEX_0 = 0;
    public final static Short SEX_1 = 1;
    public final static Short SEX_2 = 2;

    /**
     * 信息是否同步到erp 1.是 0.否
     */
    public final static Short ERP_FLAG_0 = 0;
    public final static Short ERP_FLAG_1 = 1;

    /**
     * ERP余额是否同步到 1.是 0.否
     */
    public final static Short ERP_BALANCE_FLAG_0 = 0;
    public final static Short ERP_BALANCE_FLAG_1 = 1;

    /**
     * 登录账号类型：1 分销商主账号 2 分销商联系人账号
     */
    public final static Short ACCOUNT_TYPE_1 = 1;
    public final static Short ACCOUNT_TYPE_2 = 2;
    /**
     * 修改方式：1 原密码修改 2 验证码修改
     */
    public final static Short CHANGE_PASSWORD_TYPE_1 = 1;
    public final static Short CHANGE_PASSWORD_TYPE_2 = 2;

    /**
     * 是否默认收货地址 0.否 1.是
     */
    public final static Short DEFAULT_FLAG_0 = 0;
    public final static Short DEFAULT_FLAG_1 = 1;

    /**
     * 是否开启分销模式 0 不开启, 1 开启
     */
    public final static Short DISTRIBUTION_FLAG_0 = 0;
    public final static Short DISTRIBUTION_FLAG_1 = 1;

    /**
     * 是否开启C端模式 0 不开启, 1 开启
     */
    public final static Short CUSTOMER_FLAG_0 = 0;
    public final static Short CUSTOMER_FLAG_1 = 1;

    /**
     * 分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)
     */
    public final static Short DISTRIBUTION_PROMOTION_FLAG_0 = 0;
    public final static Short DISTRIBUTION_PROMOTION_FLAG_1 = 1;
    /**
     * 参与活动 0-不参与活动 1-全部活动 2-指定活动类型
     */
    public final static Short PROMOTION_SCOPE_0 = 0;
    public final static Short PROMOTION_SCOPE_1 = 1;
    public final static Short PROMOTION_SCOPE_2 = 2;

    /**
     * 可参与活动类型 1-营销活动 2-阶梯活动 3-拼团活动
     */
    public final static String PROMOTION_TYPE_1 = "1";
    public final static String PROMOTION_TYPE_2 = "2";
    public final static String PROMOTION_TYPE_3 = "3";

    /**
     * 分销订单是否自动审核： 1 是 2 否（注意：下级分销订单自动审核）
     */
    public final static Short AUTO_FLAG_0 = 0;
    public final static Short AUTO_FLAG_1 = 1;

    /**
     * 分销结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)
     */
    public final static Short DISTRIBUTION_MODE_1 = 1;
    public final static Short DISTRIBUTION_MODE_2 = 2;
    public final static Short DISTRIBUTION_MODE_3 = 3;

    /**
     * C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)
     */
    public final static Short CUSTOMER_MODE_1 = 1;
    public final static Short CUSTOMER_MODE_2 = 2;
    public final static Short CUSTOMER_MODE_3 = 3;

    /**
     * 是否有特殊公式, 1是,0否
     */
    public final static Short SPECIAL_FLAG_0 = 0;
    public final static Short SPECIAL_FLAG_1 = 1;

    /**
     * 验证码类型：1 注册申请 2 忘记密码 3 修改手机号
     */
    public final static Short CODE_TYPE_1 = 1;
    public final static Short CODE_TYPE_2 = 2;
    public final static Short CODE_TYPE_3 = 3;

    /**
     * 是否删除 1、是 0、否
     */
    public final static Short COMMON_DEL_FLAG_NO = 0;

    /**
     * 是否删除 1、是 0、否
     */
    public final static Short COMMON_DEL_FLAG_YES = 1;
    /**
     * 此用户是否已经被冻结 1为否,2为已冻结
     */
    public final static Short CUSTOMER_STATUS_1 = 1;
    public final static Short CUSTOMER_STATUS_2 = 2;

    /**
     * 确认协议:1 已确认 0未确认
     */
    public final static Short AGREEMENT_FLAG_0 = 0;
    public final static Short AGREEMENT_FLAG_1 = 1;
    /**
     * 用户类型 1、有效用户(有手机号码) 2、游客（没有填写手机号码）
     */
    public final static Short CUSTOMER_TYPE_1 = 1;
    public final static Short CUSTOMER_TYPE_2 = 2;
    /**
     * 结算方式，1为立即支付，2为期间结算
     */
    public final static Short PAY_WAY_1 = 1;
    public final static Short PAY_WAY_2 = 2;
    /**
     * 是否支持线下转账： 1支持 0不支持
     */
    public final static Short OFFLINE_FLAG_0 = 0;
    public final static Short OFFLINE_FLAG_1 = 1;

    /**
     * 微信平台类型：1 公众号 2 小程序
     */
    public final static Short WX_PLATFORM_TYPE_1 = 1;
    public final static Short WX_PLATFORM_TYPE_2 = 2;

    /**
     * 抖音平台类型：1 小程序
     */
    public final static Short DY_PLATFORM_TYPE_1 = 1;

    /**
     * 支付宝平台类型：1 小程序
     */
    public final static Short ZFB_PLATFORM_TYPE_1 = 1;

    /**
     * 分销商模块审批标识
     */
    public final static Short DISTRIBUTOR_CHECK = 2;

    /**
     * 审批类型: 1 新增 2 修改
     */
    public final static Short CHECK_TYPE_1 = 1;
    public final static Short CHECK_TYPE_2 = 2;

    /**
     * 审批状态 0,未审批 1,审批通过，2审批未通过
     */
    public final static Short CHECK_STATUS_0 = 0;
    public final static Short CHECK_STATUS_1 = 1;
    public final static Short CHECK_STATUS_2 = 2;

    /**
     * 是否开启审批: 0 关闭 1 开启
     */
    public final static Short OPEN_FLAG_0 = 0;
    public final static Short OPEN_FLAG_1 = 1;

    /**
     * 标签类型：1,我发起的 2,待我审批 3,我审批的
     */
    public final static Short LABEL_TYPE_1 = 1;
    public final static Short LABEL_TYPE_2 = 2;
    public final static Short LABEL_TYPE_3 = 3;
    /**
     * 公司类型 1-公司 2-个体商户 3-个人
     */
    public final static Short COMPANY_TYPE_1 = 1;
    public final static Short COMPANY_TYPE_2 = 2;
    public final static Short COMPANY_TYPE_3 = 3;

    /**
     * flag 1 是 0 否
     */
    public final static Short FLAG_0 = 0;
    public final static Short FLAG_1 = 1;

    /**
     * 是否能导出报价 1.能 0.不能
     */
    public final static Short CAN_EXPORT_PRICE_0 = 0;
    public final static Short CAN_EXPORT_PRICE_1 = 1;

    /**
     * 税种类型 1-一般纳税人 2-小规模纳税人 3-个人
     */
    public final static Short TAX_TYPE_1 = 1;
    public final static Short TAX_TYPE_2 = 2;
    public final static Short TAX_TYPE_3 = 3;

    /**
     * 服务模块：2-客户服务
     */
    public static final Short MODEL_TYPE2 = 2;

    /**
     * 1 微信公众号
     */
    public static final Short PLATFORM_1 = 1;
}
