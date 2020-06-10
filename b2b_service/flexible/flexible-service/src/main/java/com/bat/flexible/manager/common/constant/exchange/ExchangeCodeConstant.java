package com.bat.flexible.manager.common.constant.exchange;

public class ExchangeCodeConstant {

    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
    public static final Short StatusInit=0;

    public static final String StatusInitName="未激活";

    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
    public static final Short StatusInvalid=4;

    public static final String StatusInvalidName="作废";

    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
    public static final Short StatusUnUse=1;

    public static final String StatusUnUseName="未使用";

    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
    public static final Short StatusUsed=2;

    public static final String StatusUsedName="已核销";

    //核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
    public static final Short StatusEnd=3;

    public static final String StatusEndName="已过期";

    //同步工厂状态 2、已同步成功 0、未同步 1、同步中 3、同步失败
    public static final Short SyncFactoryStatusUnSync=0;

    //同步工厂状态 2、已同步成功 0、未同步 1、同步中 3、同步失败
    public static final Short SyncFactoryStatusRunning=1;

    //同步工厂状态 2、已同步成功 0、未同步 1、同步中 3、同步失败
    public static final Short SyncFactoryStatusSuccess=2;
}
