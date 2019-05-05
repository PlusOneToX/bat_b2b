package com.bat.thirdparty.erp.api.request.dto;

import java.util.List;

/**
 * Created by apple on 2019/7/1.
 */
public class SaleOrderChangeAddEntry {

    private String FBillNo;                         //单据编号
    private String FB2B_BillNo;                //b2b单据编号
    private FSaleOrgId FSaleOrgId;                //销售组织
    private String FDate;                         //日期
    private FCustId FCustId;                      //客户编码
    private FSaleDeptId FSaleDeptId;             //销售部门
    private FSalerId FSalerId;                    //销售员
    private String FNote;               //变更原因
    private boolean FIsDirectChange = true;    //  是否直接变更过程中
    private F_LHR_Division F_LHR_Division;      //事业部

    private F_LHR_Assistant F_LHR_Assistant;  //变更类型
    private List<FSaleOrderEntry> FSaleOrderEntry;
    //财务信息
    private FSaleOrderFinance FSaleOrderFinance;
    private String FBillTypeIdX; //单据类型
    private String FPKIDX_H;
    private String FDirectShipment;   //是否直运
    private String FDirectShipment1;   //是否MTO订单
    private String F_SOZF;              //   是否直发订单
    private String FBusinessType;       //   销售类型
    private String FContact;            //   联系人
    private String FContactPhone;         //   联系电话
    private String FContactAddress;        //   联系地址

    public String getFBillNo() {
        return FBillNo;
    }

    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }

    public String getFBillTypeIdX() {
        return FBillTypeIdX;
    }

    public void setFBillTypeIdX(String FBillTypeIdX) {
        this.FBillTypeIdX = FBillTypeIdX;
    }

    public String getFBusinessType() {
        return FBusinessType;
    }

    public void setFBusinessType(String FBusinessType) {
        this.FBusinessType = FBusinessType;
    }

    public String getFDirectShipment() {
        return FDirectShipment;
    }

    public void setFDirectShipment(String FDirectShipment) {
        this.FDirectShipment = FDirectShipment;
    }

    public String getFDirectShipment1() {
        return FDirectShipment1;
    }

    public void setFDirectShipment1(String FDirectShipment1) {
        this.FDirectShipment1 = FDirectShipment1;
    }

    public String getF_SOZF() {
        return F_SOZF;
    }

    public void setF_SOZF(String f_SOZF) {
        F_SOZF = f_SOZF;
    }

    public String getFPKIDX_H() {
        return FPKIDX_H;
    }

    public String getFB2B_BillNo() {
        return FB2B_BillNo;
    }

    public void setFB2B_BillNo(String FB2B_BillNo) {
        this.FB2B_BillNo = FB2B_BillNo;
    }

    public void setFPKIDX_H(String FPKIDX_H) {
        this.FPKIDX_H = FPKIDX_H;
    }

    public SaleOrderChangeAddEntry.FSaleOrgId getFSaleOrgId() {
        return FSaleOrgId;
    }

    public void setFSaleOrgId(SaleOrderChangeAddEntry.FSaleOrgId FSaleOrgId) {
        this.FSaleOrgId = FSaleOrgId;
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public FSalerId getFSalerId() {
        return FSalerId;
    }

    public void setFSalerId(FSalerId FSalerId) {
        this.FSalerId = FSalerId;
    }

    public String getFContact() {
        return FContact;
    }

    public void setFContact(String FContact) {
        this.FContact = FContact;
    }

    public String getFContactPhone() {
        return FContactPhone;
    }

    public void setFContactPhone(String FContactPhone) {
        this.FContactPhone = FContactPhone;
    }

    public String getFContactAddress() {
        return FContactAddress;
    }

    public void setFContactAddress(String FContactAddress) {
        this.FContactAddress = FContactAddress;
    }

    public String getFNote() {
        return FNote;
    }

    public void setFNote(String FNote) {
        this.FNote = FNote;
    }

    public SaleOrderChangeAddEntry.FSaleOrderFinance getFSaleOrderFinance() {
        return FSaleOrderFinance;
    }

    public void setFSaleOrderFinance(SaleOrderChangeAddEntry.FSaleOrderFinance FSaleOrderFinance) {
        this.FSaleOrderFinance = FSaleOrderFinance;
    }

    /**
     * 变更类型
     */
    public static class F_LHR_Assistant {

        private String FNumber;

        public F_LHR_Assistant(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    /**
     * 事业部
     */
    public static class F_LHR_Division {

        private String FNUMBER;

        public F_LHR_Division(String FNUMBER) {
            this.FNUMBER = FNUMBER;
        }

        public String getFNUMBER() {
            return FNUMBER;
        }

        public void setFNUMBER(String FNUMBER) {
            this.FNUMBER = FNUMBER;
        }
    }

    /**
     * 销售部
     */
    public static class FSaleDeptId {

        private String FNUMBER;

        public FSaleDeptId(String FNUMBER) {
            this.FNUMBER = FNUMBER;
        }

        public String getFNUMBER() {
            return FNUMBER;
        }

        public void setFNUMBER(String FNUMBER) {
            this.FNUMBER = FNUMBER;
        }
    }

    /**
     * 客户id
     */
    public static class FCustId {

        private String FNumber;

        public FCustId(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }
//    /**
//     * 单据类型
//     */
//    public static class FBillTypeID{
//
//        private String FNumber;
//
//        public FBillTypeID(String FNumber) {
//            this.FNumber = FNumber;
//        }
//
//        public String getFNumber() {
//            return FNumber;
//        }
//
//        public void setFNumber(String FNumber) {
//            this.FNumber = FNumber;
//        }
//    }
//    /**
//     * 单据类型
//     */
//    public static class FBillTypeIdX{
//
//        private String FNumber;
//
//        public FBillTypeIdX(String FNumber) {
//            this.FNumber = FNumber;
//        }
//
//        public String getFNumber() {
//            return FNumber;
//        }
//
//        public void setFNumber(String FNumber) {
//            this.FNumber = FNumber;
//        }
//    }

    /**
     * 使用组织
     */
    public static class FSaleOrgId {

        private String FNumber;

        public FSaleOrgId(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    /**
     * 结算币别
     */
    public static class FSettleCurrId {

        private String FNumber;

        public FSettleCurrId(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    /**
     * 财务信息
     */
    public static class FSaleOrderFinance {
        private FSettleCurrId FSettleCurrId;
        private Double FExchangeRate;

        public FSaleOrderFinance(SaleOrderChangeAddEntry.FSettleCurrId FSettleCurrId, Double FExchangeRate) {
            this.FSettleCurrId = FSettleCurrId;
            this.FExchangeRate = FExchangeRate;
        }

        public SaleOrderChangeAddEntry.FSettleCurrId getFSettleCurrId() {
            return FSettleCurrId;
        }

        public void setFSettleCurrId(SaleOrderChangeAddEntry.FSettleCurrId FSettleCurrId) {
            this.FSettleCurrId = FSettleCurrId;
        }

        public Double getFExchangeRate() {
            return FExchangeRate;
        }

        public void setFExchangeRate(Double FExchangeRate) {
            this.FExchangeRate = FExchangeRate;
        }
    }

    /**
     * 财务信息
     */
    public static class FSalOrderTrace {
        private String FCarryBillNo;

        public FSalOrderTrace(String FCarryBillNo) {
            this.FCarryBillNo = FCarryBillNo;
        }
    }

    /**
     * 仓库
     */
    public static class FSOStockId {

        private String FNUMBER;

        public FSOStockId(String FNUMBER) {
            this.FNUMBER = FNUMBER;
        }

        public String getFNUMBER() {
            return FNUMBER;
        }

        public void setFNUMBER(String FNumber) {
            this.FNUMBER = FNUMBER;
        }
    }

    /**
     * 子物料
     */
    public static class FSaleOrderEntry {

        private FMaterialId FMaterialId; //物料编码
        private FUnitID FUnitID;        // 销售单位
        private Double FTaxPrice;    //新含税单价
        private FSettleOrgIds FSettleOrgIds; // 结算组织
        private String FQty;     // 销售数量
        private String FDeliveryDate; // 要货日期
        private String FSrcType; //源单类型
        private String FSrcBillNo; //源单编号
        private String FReserveType; // 预留类型
        private String FOUTLMTUNIT;  //超发控制单位类型
        private FSOStockId FSOStockId; //仓库
        private String FChangeType;   //变更类型
        private String FPKIDX;//明细主键
        private List<FSaleOrderEntry_Link> FSaleOrderEntry_Link;

        public SaleOrderChangeAddEntry.FMaterialId getFMaterialId() {
            return FMaterialId;
        }

        public void setFMaterialId(SaleOrderChangeAddEntry.FMaterialId FMaterialId) {
            this.FMaterialId = FMaterialId;
        }

        public SaleOrderChangeAddEntry.FUnitID getFUnitID() {
            return FUnitID;
        }

        public void setFUnitID(SaleOrderChangeAddEntry.FUnitID FUnitID) {
            this.FUnitID = FUnitID;
        }

        public Double getFTaxPrice() {
            return FTaxPrice;
        }

        public void setFTaxPrice(Double FTaxPrice) {
            this.FTaxPrice = FTaxPrice;
        }

        public SaleOrderChangeAddEntry.FSettleOrgIds getFSettleOrgIds() {
            return FSettleOrgIds;
        }

        public void setFSettleOrgIds(SaleOrderChangeAddEntry.FSettleOrgIds FSettleOrgIds) {
            this.FSettleOrgIds = FSettleOrgIds;
        }

        public String getFPKIDX() {
            return FPKIDX;
        }

        public void setFPKIDX(String FPKIDX) {
            this.FPKIDX = FPKIDX;
        }

        public String getFSrcType() {
            return FSrcType;
        }

        public void setFSrcType(String FSrcType) {
            this.FSrcType = FSrcType;
        }

        public String getFSrcBillNo() {
            return FSrcBillNo;
        }

        public void setFSrcBillNo(String FSrcBillNo) {
            this.FSrcBillNo = FSrcBillNo;
        }

        public String getFDeliveryDate() {
            return FDeliveryDate;
        }

        public void setFDeliveryDate(String FDeliveryDate) {
            this.FDeliveryDate = FDeliveryDate;
        }

        public String getFReserveType() {
            return FReserveType;
        }

        public void setFReserveType(String FReserveType) {
            this.FReserveType = FReserveType;
        }

        public String getFOUTLMTUNIT() {
            return FOUTLMTUNIT;
        }

        public void setFOUTLMTUNIT(String FOUTLMTUNIT) {
            this.FOUTLMTUNIT = FOUTLMTUNIT;
        }

        public String getFChangeType() {
            return FChangeType;
        }

        public void setFChangeType(String FChangeType) {
            this.FChangeType = FChangeType;
        }

        public String getFQty() {
            return FQty;
        }

        public void setFQty(String FQty) {
            this.FQty = FQty;
        }

        public SaleOrderChangeAddEntry.FSOStockId getFSOStockId() {
            return FSOStockId;
        }

        public void setFSOStockId(SaleOrderChangeAddEntry.FSOStockId FSOStockId) {
            this.FSOStockId = FSOStockId;
        }

        public List<SaleOrderChangeAddEntry.FSaleOrderEntry_Link> getFSaleOrderEntry_Link() {
            return FSaleOrderEntry_Link;
        }

        public void setFSaleOrderEntry_Link(List<SaleOrderChangeAddEntry.FSaleOrderEntry_Link> FSaleOrderEntry_Link) {
            this.FSaleOrderEntry_Link = FSaleOrderEntry_Link;
        }
    }

    public static class FUnitID {
        private String FNumber;

        public FUnitID(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    public static class FMaterialId {
        private String FNumber;

        public FMaterialId(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    public static class FSalerId {
        private String FNumber;

        public FSalerId(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    public static class FSettleOrgIds {
        private String FNumber;

        public FSettleOrgIds(String FNumber) {
            this.FNumber = FNumber;
        }

        public String getFNumber() {
            return FNumber;
        }

        public void setFNumber(String FNumber) {
            this.FNumber = FNumber;
        }
    }

    public List<SaleOrderChangeAddEntry.FSaleOrderEntry> getFSaleOrderEntry() {
        return FSaleOrderEntry;
    }

    public void setFSaleOrderEntry(List<SaleOrderChangeAddEntry.FSaleOrderEntry> FSaleOrderEntry) {
        this.FSaleOrderEntry = FSaleOrderEntry;
    }

    public SaleOrderChangeAddEntry.FCustId getFCustId() {
        return FCustId;
    }

    public void setFCustId(SaleOrderChangeAddEntry.FCustId FCustId) {
        this.FCustId = FCustId;
    }

    public boolean isFIsDirectChange() {
        return FIsDirectChange;
    }

    public void setFIsDirectChange(boolean FIsDirectChange) {
        this.FIsDirectChange = FIsDirectChange;
    }

    public SaleOrderChangeAddEntry.F_LHR_Division getF_LHR_Division() {
        return F_LHR_Division;
    }

    public void setF_LHR_Division(SaleOrderChangeAddEntry.F_LHR_Division f_LHR_Division) {
        F_LHR_Division = f_LHR_Division;
    }

    public SaleOrderChangeAddEntry.F_LHR_Assistant getF_LHR_Assistant() {
        return F_LHR_Assistant;
    }

    public void setF_LHR_Assistant(SaleOrderChangeAddEntry.F_LHR_Assistant f_LHR_Assistant) {
        F_LHR_Assistant = f_LHR_Assistant;
    }

    public SaleOrderChangeAddEntry.FSaleDeptId getFSaleDeptId() {
        return FSaleDeptId;
    }

    public void setFSaleDeptId(SaleOrderChangeAddEntry.FSaleDeptId FSaleDeptId) {
        this.FSaleDeptId = FSaleDeptId;
    }

    /**
     * 子物料
     */
    public static class FSaleOrderEntry_Link {

        private String FLinkId; //实体主键
        private String FSaleOrderEntry_Link_FFlowId; //业务流程图
        private String FSaleOrderEntry_Link_FFlowLineId; //推进路线
        private String FSaleOrderEntry_Link_FRuleId; //转换规则
        private String FSaleOrderEntry_Link_FSTableId; //源单表内码
        private String FSaleOrderEntry_Link_FSTableName; //源单表
        private String FSaleOrderEntry_Link_FSBillId; //源单内码
        private String FSaleOrderEntry_Link_FSId; //源单分录内码
        private String FSaleOrderEntry_Link_FBaseUnitQtyOld; //原始携带量
        private String FSaleOrderEntry_Link_FBaseUnitQty; //修改携带量

//        private String FChangeType;   //变更类型 private String  FLinkId ; //实体主键
//        private FUnitID FUnitID;        // 销售单位
//        private Double FTaxPrice;    //新含税单价
//        private FSettleOrgIds FSettleOrgIds; // 结算组织
//        private Long FQty;     // 销售数量
//        private String FDeliveryDate; // 要货日期
//        private String FSrcType; //源单类型
//        private String FSrcBillNo; //源单编号
//        private String FReserveType; // 预留类型
//        private String FOUTLMTUNIT;  //超发控制单位类型
//        private FSOStockId FSOStockId; //仓库

//        public SaleOrderChangeAddBean.FMaterialId getFMaterialId() {
//            return FMaterialId;
//        }

        public String getFLinkId() {
            return FLinkId;
        }

        public void setFLinkId(String FLinkId) {
            this.FLinkId = FLinkId;
        }

        public String getFSaleOrderEntry_Link_FFlowId() {
            return FSaleOrderEntry_Link_FFlowId;
        }

        public void setFSaleOrderEntry_Link_FFlowId(String FSaleOrderEntry_Link_FFlowId) {
            this.FSaleOrderEntry_Link_FFlowId = FSaleOrderEntry_Link_FFlowId;
        }

        public String getFSaleOrderEntry_Link_FFlowLineId() {
            return FSaleOrderEntry_Link_FFlowLineId;
        }

        public void setFSaleOrderEntry_Link_FFlowLineId(String FSaleOrderEntry_Link_FFlowLineId) {
            this.FSaleOrderEntry_Link_FFlowLineId = FSaleOrderEntry_Link_FFlowLineId;
        }

        public String getFSaleOrderEntry_Link_FRuleId() {
            return FSaleOrderEntry_Link_FRuleId;
        }

        public void setFSaleOrderEntry_Link_FRuleId(String FSaleOrderEntry_Link_FRuleId) {
            this.FSaleOrderEntry_Link_FRuleId = FSaleOrderEntry_Link_FRuleId;
        }

        public String getFSaleOrderEntry_Link_FSTableId() {
            return FSaleOrderEntry_Link_FSTableId;
        }

        public void setFSaleOrderEntry_Link_FSTableId(String FSaleOrderEntry_Link_FSTableId) {
            this.FSaleOrderEntry_Link_FSTableId = FSaleOrderEntry_Link_FSTableId;
        }

        public String getFSaleOrderEntry_Link_FSTableName() {
            return FSaleOrderEntry_Link_FSTableName;
        }

        public void setFSaleOrderEntry_Link_FSTableName(String FSaleOrderEntry_Link_FSTableName) {
            this.FSaleOrderEntry_Link_FSTableName = FSaleOrderEntry_Link_FSTableName;
        }

        public String getFSaleOrderEntry_Link_FSBillId() {
            return FSaleOrderEntry_Link_FSBillId;
        }

        public void setFSaleOrderEntry_Link_FSBillId(String FSaleOrderEntry_Link_FSBillId) {
            this.FSaleOrderEntry_Link_FSBillId = FSaleOrderEntry_Link_FSBillId;
        }

        public String getFSaleOrderEntry_Link_FSId() {
            return FSaleOrderEntry_Link_FSId;
        }

        public void setFSaleOrderEntry_Link_FSId(String FSaleOrderEntry_Link_FSId) {
            this.FSaleOrderEntry_Link_FSId = FSaleOrderEntry_Link_FSId;
        }

        public String getFSaleOrderEntry_Link_FBaseUnitQtyOld() {
            return FSaleOrderEntry_Link_FBaseUnitQtyOld;
        }

        public void setFSaleOrderEntry_Link_FBaseUnitQtyOld(String FSaleOrderEntry_Link_FBaseUnitQtyOld) {
            this.FSaleOrderEntry_Link_FBaseUnitQtyOld = FSaleOrderEntry_Link_FBaseUnitQtyOld;
        }

        public String getFSaleOrderEntry_Link_FBaseUnitQty() {
            return FSaleOrderEntry_Link_FBaseUnitQty;
        }

        public void setFSaleOrderEntry_Link_FBaseUnitQty(String FSaleOrderEntry_Link_FBaseUnitQty) {
            this.FSaleOrderEntry_Link_FBaseUnitQty = FSaleOrderEntry_Link_FBaseUnitQty;
        }
    }

}
