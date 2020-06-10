package com.bat.flexible.manager.common.constant.label;

public class LabelConstant {

    //类型 1、普通标签 2、定制标签
    public static final Short TYPE_GENERAL =1;

    //类型 1、普通标签 2、定制标签
    public static final Short TYPE_FLEXIBEL =2;

    //分销商适用范围 1为全部，2 为国内， 3为国外，4 为指定
    public static final Short SCOPE_DISTRIBUTOR_ALL =1;

    //分销商适用范围 1为全部，2 为国内， 3为国外，4 为指定
    public static final Short SCOPE_DISTRIBUTOR_INTERNAL =2;

    //分销商适用范围 1为全部，2 为国内， 3为国外，4 为指定
    public static final Short SCOPE_DISTRIBUTOR_FOREIGN =3;

    //分销商适用范围 1为全部，2 为国内， 3为国外，4 为指定
    public static final Short SCOPE_DISTRIBUTOR_ASSIGN =4;

    //是否关联用户自己上传的图片 1、是 0、否 （定制标签才有值）
    public static final Short RELEVANCE_USER_UPLOAD_YES =1;

    public static final Short RELEVANCE_USER_UPLOAD_NO =0;
}
