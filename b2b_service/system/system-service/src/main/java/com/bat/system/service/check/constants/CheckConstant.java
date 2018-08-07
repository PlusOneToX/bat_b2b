package com.bat.system.service.check.constants;

/**
 * Created by apple on 2018/1/17.
 */
public class CheckConstant {

    public static final int Goods_Shelves_Type = 1; // 商品管理上下架审批
    public static final int Distributor_Type = 2; // 分销商审批
    public static final int Stock_Adjust_Type = 3; // 仓库库存调整审批
    public static final int Stock_Reserve_Type = 4; // 仓库库存预留审批
    public static final int Order_Price_Type = 5; // 订单商品价格审批
    public static final int Order_discount_Type = 6; // 订单对账折扣审批
    public static final int MarketingPromotionType = 7; // 促销活动新增审批
    public static final int MarketingPromotionEditType = 8; // 促销活动编辑审批
    public static final int BillOrderDiscountType = 9; // 订单折扣申请编辑审批

    public static final int Policy_Grade_Change = 10; // 等级变更审批
    public static final int Policy_Grade_Discount = 11; // 商品等级折扣审批
    public static final int Policy_PickUp_Rate = 12; // 提货增长返利审批

    public static final int Check_Type_Max = 12;
    public static final int Check_Type_Min = 1;

    public final static short Check_UnProcess = 0; // 未审批
    public final static short Check_Handing = 1; // 审批中
    public final static short Check_Success = 2; // 审批通过
    public final static short Check_Failure = 3; // 审批未通过

    public final static short Check_Status_Max = 3;
    public final static short Check_Status_Min = 1;

    public final static short Check_Apply = 1; // 我发起的的审批
    public final static short Check_Todo = 2; // 待我审批的审批
    public final static short Check_Finish = 3; // 我审批过的审批

    public final static short Check_Open = 1; // 审批开启
    public final static short Check_Close = 2; // 审批关闭

    public final static short CheckRelationGoods = 1; // 商品
    public final static short CheckRelationOrder = 2; // 订单

}
