package com.bat.order.service.importOrder.dto;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/2 21:05
 */
@Data
public class ImportOrderExcelDTO {

    @ExcelProperty("* 订单序号")
    private Integer serialNumber;

    @ExcelProperty("* 存货编码")
    private String itemCode;

    @ExcelProperty("* 购买数量")
    private Integer itemCount;

    @ExcelProperty("* 收货人姓名")
    private String userName;

    @ExcelProperty("* 移动电话")
    private String mobile;

    @ExcelProperty("* 收货地址国家")
    private String countryName;

    @ExcelProperty("收货地址省份")
    private String provinceName;

    @ExcelProperty("收货地址城市")
    private String cityName;

    @ExcelProperty("收货地址区/县")
    private String districtName;

    @ExcelProperty("* 收货详细地址")
    private String address;

    @ExcelProperty("* 配送方式")
    private String logisticsName;

    @ExcelProperty("* 支付方式")
    private String payWayName;

    @ExcelProperty("* 币种(人民币填CNY，美元填USD)")
    private String currencyType;

    @ExcelProperty("* 是否拆单(不拆单填0，拆单填1)")
    private Short onWaySplitFlag;

    @ExcelProperty("特殊订单标识：\n" + "直运订单填1，MTO订单填2，其他订单类型留空")
    private Integer specialOrderFlag;

    @ExcelProperty("订单附言")
    private String remark;

    @ExcelProperty("* B2B编码")
    private Integer distributorId;

    /**
     * 定制参数 diy开头，方便校验时过滤
     */
    @ExcelProperty("* 定制类型/编码")
    private Short diyCustomizeCode;

    @ExcelProperty("* 材质名称/编码")
    private String diyMaterialName;

    @ExcelProperty("* 型号名称/编码")
    private String diyModelName;

    @ExcelProperty("* 图片编码")
    private Integer diyPictureId;

}
