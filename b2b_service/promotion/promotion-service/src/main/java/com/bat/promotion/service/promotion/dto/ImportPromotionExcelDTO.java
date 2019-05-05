package com.bat.promotion.service.promotion.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/11/29 9:21
 */
@Data
public class ImportPromotionExcelDTO {
    @ExcelProperty("* 活动序号")
    private String promotionNo;
    @ExcelProperty("* 商品编码/条形码/货品编号")
    private String goodsItemNo;
    @ExcelProperty("* 规则对象（单个商品填2，单个货品填3）")
    private Short ruleType;
    @ExcelProperty("* 活动类型(阶梯活动填2)")
    private Short promoType;
    @ExcelProperty("* 活动名称")
    private String name;
    @ExcelProperty("活动英文名称")
    private String nameEn;
    @ExcelProperty("* 活动描述")
    private String promoDesc;
    @ExcelProperty("*在途是否参与（1表示参与，0表示不参与）")
    private Short onWayFlag;
    @ExcelProperty("*是否支持货品混批（填1为是，填0为否）")
    private Short addUpFlag;
    @ExcelProperty("* 满减方式（指定价格填1，打折填2，减免填3）")
    private Short conditionType;
    @ExcelProperty("* 标签1")
    private String conditionName1;
    @ExcelProperty(" 英文标签1")
    private String conditionNameEn1;
    @ExcelProperty("* 条件1")
    private Integer oneBuyCount1;
    @ExcelProperty("* 满减额度1")
    private BigDecimal discountReduction1;
    @ExcelProperty("标签2")
    private String conditionName2;
    @ExcelProperty("英文标签2")
    private String conditionNameEn2;
    @ExcelProperty("条件2")
    private Integer oneBuyCount2;
    @ExcelProperty("满减额度2")
    private BigDecimal discountReduction2;
    @ExcelProperty("标签3")
    private String conditionName3;
    @ExcelProperty(" 英文标签3")
    private String conditionNameEn3;
    @ExcelProperty("条件3")
    private Integer oneBuyCount3;
    @ExcelProperty("满减额度3")
    private BigDecimal discountReduction3;
    @ExcelProperty("* 开始时间\n" + "(2019/3/23  12:00:00)")
    private Date startTime;
    @ExcelProperty("* 结束时间")
    private Date endTime;
    @ExcelProperty("提前天数（未填写为不提前）")
    private Integer advanceDay;
    @ExcelProperty("活动范围（多选，逗号(英文半角)隔开，如（1,2,3）,如果不填默认全部分销商）")
    private String departmentSStr;
}
