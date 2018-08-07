package com.bat.financial.api.basesetting.dto;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 13:54
 */
@Data
@ApiModel(value = "CurrencyRateListQry", description = "汇率查询")
public class CurrencyRateListQry extends BaseSearchQry {}
