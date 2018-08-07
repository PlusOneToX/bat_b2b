package com.bat.distributor.service.common.subaccount;

public class SubAccountConstant {

    /**
     * 分账时效类型 1、实时 2、延迟
     */
    public static final Short AGING_TYPE_REALTIME=1;

    public static final Short AGING_TYPE_DELAY=2;

    /**
     * 搜索类型 1、分销商用户名 2、配置名称 3、店铺名称
     */
    public static final Short USER_CONFIG_PAGE_SEARCH_SHOP=3;

    /**
     * 身份类型 1、企业、2 个人
     */
    public static final Short SALEMAN_TYPE_COMPANY=1;

    public static final String SALEMAN_TYPE_COMPANY_NAME="企业";

    public static final Short SALEMAN_TYPE_PERSONAL=2;

    public static final String SALEMAN_TYPE_PERSONAL_NAME="个人";

}
