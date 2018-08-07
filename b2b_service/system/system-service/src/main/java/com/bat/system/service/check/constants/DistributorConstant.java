package com.bat.system.service.check.constants;

/**
 * Created by apple on 2018/1/7.'审核状态 0,未审批 1,审批中,2,审批通过 3,审批未通过'
 */
public class DistributorConstant {

    public final static short Status_Start = 1; // 启用状态
    public final static short Status_Stop = 2; // 停用状态

    public final static short Freeze_Close = 2; // 未冻结
    public final static short Freeze_Open = 1; // 冻结

    public final static short Status_Max = 2;
    public final static short Status_Min = 1;

    public final static short Check_Submit_Hand = 1; // 审批中
    public final static short Check_Success = 2; // 审批通过
    public final static short Check_Failure = 3; // 审批未通过

    public final static short Capital_Un_Submit = 0; // 未提交
    public final static short Capital_Submit_Hand = 1; // 申请中
    public final static short Capital_Success = 2; // 合作中
    public final static short Capital_Failure = 3; // 申请失败
    public final static short Capital_Check = 4; // 审核中

    public final static short Check_Status_Max = 3;
    public final static short Common_Status_Max = 1;
    public final static short Trade_Status_Max = 4;

    public final static short ApplyAdminType = 1;
    public final static short ApplyUserType = 2;

    public final static short LogOperateCreateType = 1;// 1为创建,2为编辑,3同步erp,4冻结,5提交审核
    public final static short LogOperateEditType = 2;
    public final static short LogOperateErpType = 3;
    public final static short LogOperateFreezeType = 4;
    public final static short LogOperateCheckType = 5;
    public final static short LogOperateEXPORTPRICEType = 6;// 导出报价
    /**
     * 冻结状态
     */
    public final static Short Freeze_Status = 1;
    /**
     * 未冻结状态
     */
    public final static Short UnFreeze_Status = 0;
    /**
     * 品牌
     */
    public final static String Deal_Of_Brand = "1";
    /**
     * 产品线
     */
    public final static String Deal_Of_ProductLine = "2";
    /**
     * 分销商审批类型
     */
    public final static Short DistributorAdd = 1;
    public final static Short DistributorEdit = 2;
    /**
     * 拒绝的分销商是否可编辑
     */
    public final static Short IsEdit = 1;
    public final static Short IsNotEdit = 2;

    /**
     * 拒绝的分销商是否可编辑
     */
    public final static String DistributorMdTypeBase = "1";
    public final static String DistributorMdTypeAccount = "2";
    public final static String DistributorMdTypeRight = "3";

    // 参与活动 0-不参与活动 1-全部活动 2-指定活动类型
    public final static Short PromotionScopeForbidJoin = 0;

    // 参与活动 0-不参与活动 1-全部活动 2-指定活动类型
    public final static Short PromotionScopeAll = 1;

    // 参与活动 0-不参与活动 1-全部活动 2-指定活动类型
    public final static Short PromotionScopeAssign = 2;

    // 拼团活动、活动类型3
    public final static String PromotionTypeGroup = "3";

    // 自动下推出库 1.是 2.否
    public final static Short AutoDeliveryYes = 1;

    public final static Short AutoDeliveryNo = 2;

    // b2b新增客户同步到erp、固定传B2B
    public final static String F_PAEZ_KHGS_B2B = "B2B";

    public final static String PaymentBeforeDeliveryEN = "款到发货";

    public final static String PaymentBeforeDeliveryYes = "1";

    public final static String PaymentBeforeDeliveryNo = "0";

    // 结算币种 2、美元
    public final static Short CoinTypeDollar = 2;

    // 国家编号 1、是国内
    public static final Integer CountryIdInternal = 1;

    // B2B商品出库、发短信提醒客户 1、是 2、否、默认是2
    public static final Byte LogisticsSmsSwitchOpen = 1;

    /**
     * 是否支持在途 1、是 2、否
     */
    public static final Byte onWayFlagYes = 1;
    /**
     * 是否支持在途 1、是 2、否
     */
    public static final Byte onWayFlagNo = 2;

}
