package com.bat.flexible.api.exchange.dto;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExchangeCardDetailDTO implements Serializable {

    /**
     * 兑换卡id
     */
    @ApiModelProperty(value = "兑换卡id")
    private Integer id;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id")
    private Integer itemId;


    /**
     * 兑换码
     */
    @ApiModelProperty(value = "兑换码")
    private String codeName;


    private String codeDesc;

    /**
     * 券码类型不能为空
     */
    private Short type;

    /**
     * 生成方式
     */
    private Short source;

    /**
     * 生成数量
     */
    @ApiModelProperty(value = "生成数量")
    private Integer codeQuantity;

    @ApiModelProperty(value = "限制使用上限、为null表示不限制")
    private Integer limitQuantity;

    @ApiModelProperty(value = "销售数量")
    private Integer saleQuantity;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 兑换方式
     */
    private Short exchangeWay;

    private BigDecimal orderUseThreshold;

    /**
     * 适用商品
     */
    private Short goodsScope;

    /**
     * 是否生成实物卡
     */
    @ApiModelProperty(value = "是否生成实体卡 1、是 0、否")
    private Short isEntity;

    /**
     * 是否使用兑换商城
     */
    @ApiModelProperty(value = "是否使用兑换商城")
    private Short isUseMall;

    //卡码设置类型 1、系统生成 2、手动导入
    @ApiModelProperty(value = "码设置类型 1、系统生成 2、手动导入")
    private Short cardType;

    //卡片码生成规则 1、系统随机 2、按规则生成
    @ApiModelProperty(value = "卡片码生成规则 1、系统随机 2、按规则生成")
    private Short ruleType;

    //抬头值
    @ApiModelProperty(value = "抬头值")
    private String riseValue;

    //浮动值
    private String floatValue;

    private Short mallType;

    /**
     * 随机位数
     */
    private Integer randomValue;

    /**
     * 兑换卡物料卡片型号（兑换商城使用）
     */
    private String modelNo;




    private List<MaterialRelaSimpleDTO> materialPageBeanList;


    private  List<ModelRelaSimpleDTO> modelBeanList;

    private List<PictureRelaSimpleDTO> pictureBeanList;

    //选中卡片

    private List<GoodsItemSimplePageBean> chooseCardItemList;

    /**
     * 指定分销商列表
     */

    private List<DistributorSimpleRelaQry> distributorList;

    private Short modelUseType;

    private Short pictureUseType;

    //实体卡是否同步工厂生产 1、是 0、否
    private Short isSyncFactory;

    //实体卡盒装数量（默认是10张、1张时取明码作为盒码）
    private Integer boxNum;

    private String headImg;


    private Short distributorScope;

    @ApiModelProperty(value = "快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）")
    private Short mailSetting;

    private BigDecimal mailFee;

    private Integer exchangeCardTransferId;

    private String transferText;

    private String transferImg;

    private String receiveText;

    private String receiveImg;

    private Short switchFlag;
}
