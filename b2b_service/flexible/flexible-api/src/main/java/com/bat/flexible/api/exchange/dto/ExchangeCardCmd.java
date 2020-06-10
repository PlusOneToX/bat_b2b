package com.bat.flexible.api.exchange.dto;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Data
public class ExchangeCardCmd implements Serializable {

    private static final long serialVersionUID = 8723175138992123264L;

    /**
     * 兑换卡id
     */
    private Integer id;

    /**
     * 货品id
     */
    /*@NotNull(message = "货品id不能为空")
    @Min(value = 0l,message = "货品id错误")*/
    private Integer itemId;

    /**
     * 兑换码
     */
    @NotBlank(message = "兑换码名称不能为空")
    private String codeName;


    private String codeDesc;

    @NotNull(message = "券码类型不能为空")
    private Short type;


    private Short source;


    private Integer codeQuantity;


    private Integer limitQuantity;

    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    private Long endTime;

    @NotNull(message = "兑换方式不能为空")
    private Short exchangeWay;

    private BigDecimal orderUseThreshold;

    @NotNull(message = "适用商品不能为空")
    private Short goodsScope=1;

    @NotNull(message = "是否生成实物卡不能为空")
    @Min(value = 0L,message = "是否生成实物卡值错误")
    @Max(value = 1L,message = "是否生成实物卡值错误")
    private Short isEntity;

    @NotNull(message = "是否使用兑换商城不能为空")
    @Min(value = 0L,message = "是否使用兑换商城值错误")
    @Max(value = 1L,message = "是否使用兑换商城值错误")
    private Short isUseMall;

    //卡码设置类型 1、系统生成 2、手动导入
    private Short cardType;

    //卡片码生成规则 1、系统随机 2、按规则生成
    private Short ruleType;

    //抬头值
    private String riseValue;

    //浮动值
    private String floatValue;

    private Short mallType;

    //随机位数
    private Integer randomValue;

    //兑换卡物料卡片型号（兑换商城使用）
    private String modelNo;


    private Set<Integer> modelIdList;


    private Set<Integer> pictureIdList;


    private Set<Integer> materialIdList;

    //手动导入的编码列表key

    private String codeKey;


    private List<ExchangeCodeImportBean> importBeanList;

    //是否确认、修改时使用 1、表示用户确定 null表示未确定、用于修改该物料对应的所有的材质
    private Short isConfirm;

    //型号使用范围 1、全部使用 2、部分可用（
    private Short modelUseType;

    //型号使用范围 1、全部使用 2、部分可用
    private Short pictureUseType;

    //实体卡是否同步工厂生产 1、是 0、否
    private Short isSyncFactory;

    //实体卡盒装数量（默认是10张、1张时取明码作为盒码）
    private Integer boxNum;

    private String headImg;

    @ApiModelProperty(value = "分销商范围 1、全部可用 2、指定分销商")
    @NotNull(message = "EXCHANGE_CARD_DISTRIBUTOR_SCOPE_NULL")
    private Short distributorScope;

    @ApiModelProperty(value = "快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）")
    @NotNull(message = "EXCHANGE_CARD_MAIL_SETTING_NULL")
    private Short mailSetting;

    @ApiModelProperty(value = "运费、mailSetting选择收运费时必填")
    private BigDecimal mailFee;

    /**
     * 选择指定分销商可用的
     */
    @ApiModelProperty(value = "指定分销商列表")
    private List<DistributorSimpleRelaQry> distributorList;

    @ApiModelProperty(value = "转赠配置")
    private Transfer transfer;

    @Data
    public static class Transfer {

        private Integer id;

        private String transferText;

        private String transferImg;

        private String receiveText;

        private String receiveImg;

        private Short switchFlag;
    }

}
