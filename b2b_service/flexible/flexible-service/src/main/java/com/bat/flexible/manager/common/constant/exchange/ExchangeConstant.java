package com.bat.flexible.manager.common.constant.exchange;

public class ExchangeConstant {

    //是否生成实体卡 1、是 0、否
    public static final Short IsEntityYes = 1;

    //是否生成实体卡 1、是 0、否
    public static final Short IsEntityNo = 0;

    //是否使用兑换商城 1、是 0、否
    public static final Short IsMallYes = 1;

    //是否使用兑换商城 1、是 0、否
    public static final Short IsMallNo = 0;

    //状态 0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束
    public static final Short StatusInit = 0;

    //状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束
    public static final Short StatusUnStart = 1;

    //状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束
    public static final Short StatusStarting = 2;

    //状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束
    public static final Short StatusStop = 3;

    //状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束
    public static final Short StatusEnd = 4;

    //适用商品 1、全部商品可用2、指定商品
    public static final Short GoodsScopeAll = 1;

    //适用商品 1、全部商品可用2、指定商品
    public static final Short GoodsScopeAssign = 2;

    //兑换商城类型 1、定制商城
    public static final Short MallTypeExchangeDiy = 1;

    //卡片码生成规则 1、系统随机 2、按照规则
    public static final Short RuleTypeSystemRandom = 1;

    //卡片码生成规则 1、系统随机 2、按照规则
    public static final Short RuleTypeRule = 2;

    //同步工厂 1、云创
    public static final Short FactoryYunChuang = 1;



    //兑换卡excel导入模板 5表示虚拟卡
    public static final String  ExcelTempExchangeVirtual = "5";

    public static final String  ExcelTempExchangeVirtualFileName = "虚拟卡导入示例.xls";

    //兑换卡excel导入模板 6表示实体卡
    public static final String  ExcelTempExchangeEntity = "6";

    public static final String  ExcelTempExchangeEntityFileName = "实体卡导入示例.xls";

    //生成来源 1、系统生成 2、人工导入（is_entity为0时、根据这个来判断）
    public static final Short SourceSystem = 1;

    //生成来源 1、系统生成 2、人工导入（is_entity为0时、根据这个来判断）
    public static final Short SourceImport = 2;

    //卡码设置类型 1、系统生成 2、手动导入
    public static final Short CardTypeImport = 2;

    //卡码设置类型 1、系统生成 2、手动导入
    public static final Short CardTypeSystem = 1;

    public static final String InvalidCodeStatusByUpdateCard="修改兑换卡活动、删除原有的兑换码";

    //退货类型 1、erp 2、B2B
    public static final Short RefundOrderTypeERP = 1;

    //退货类型 1、erp 2、B2B
    public static final Short RefundOrderTypeB2B = 2;

    //作废来源类型 1、erp 2、B2B
    public static final Short ExchangeCodeInvalidTypeERP = 1;

    //作废来源类型 1、erp 2、B2B
    public static final Short ExchangeCodeInvalidTypeB2B = 2;

    public static final String ExchangeRandomLeng = "Exchange_random_leng";

    public static final String ExchangeRandomListStr = "Exchange_random_list_str";

    //使用范围 1、全部使用 2、部分可用（picture_id有值）
    public static final Short ExchangePictureUserTypeAll = 1;

    //使用范围 1、全部使用 2、部分可用（picture_id有值）
    public static final Short ExchangePictureUserTypeSome = 2;

    //型号使用范围 1、全部使用 2、部分可用（model_id有值）
    public static final Short ExchangeModelUserTypeAll = 1;

    //型号使用范围 1、全部使用 2、部分可用（model_id有值）
    public static final Short ExchangeModelUserTypeSome = 2;

    //兑换卡txt
    public static final String ExchangeCodeRandomTxtPath = "CODE";
    public static final String ExchangeCodeRandomTxt = ExchangeCodeRandomTxtPath+"/"+"ExchangeCodeRandom_forbinDelete";

    //兑换方式  1、兑换 2、权益
    public static final Short ExchangeWayExchange1 = 1;

    //兑换方式  1、兑换 2、权益
    public static final Short ExchangeWayExchange2 = 2;

    //是否在线同步工厂生产 1、是 0、否
    public static final Short isSyncFactoryYes=1;

    public static final Short isSyncFactoryNo=0;

    //是否需要确认
    public static final Short IS_CONFIRM_YES=1;

    public static final Short IS_CONFIRM_NO=0;

    //分销商范围 1、全部可用 2、指定分销商
    public static final Short EXCHANGE_DISTRIBUTOR_SCOPE_ALL=1;

    public static final Short EXCHANGE_DISTRIBUTOR_SCOPE_ASSIGN=2;

    //快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
    public static final Short EXCHANGE_MAIL_TYPE_GENERAL=1;

    public static final Short EXCHANGE_MAIL_TYPE_COLLECT_FREIGHT=2;

    //活动所属平台 1兑换商城 2定制商城
    public static final short EXCHANGE_ACTIVITY_PLATFORM_EX=1;

    public static final short EXCHANGE_ACTIVITY_PLATFORM_CU=2;

    //活动位置 1确认订单页 2订单详情页
    public static final short EXCHANGE_SEAT_ORDER=1;

    public static final short EXCHANGE_SEAT_DETAIL=2;

    //打开类型 1二次转发查看 2普通查看
    public static final short EXCHANGE_SHARE_OPEN_TYPE_TWO=1;

    public static final short EXCHANGE_SHARE_OPEN_ORDINARY=2;

}
