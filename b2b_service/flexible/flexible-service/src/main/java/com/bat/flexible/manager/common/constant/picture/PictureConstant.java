package com.bat.flexible.manager.common.constant.picture;

public class PictureConstant {

    //图片的适用范围(哪些型号可以适用这个图片信息): 1为全部可用，2为指定型号可用,3为指定类型可用
    public static final Short MODEL_SCOPE_MODEL_CATEGORY=3;

    //图片的适用范围(哪些型号可以适用这个图片信息): 1为全部可用，2为指定型号可用,3为指定类型可用
    public static final Short MODEL_SCOPE_MODEL_ASSIGN=2;

    //图片的适用范围(哪些型号可以适用这个图片信息): 1为全部可用，2为指定型号可用,3为指定类型可用
    public static final Short MODEL_SCOPE_MODEL_ALL=1;

    //分销商适用范围 1为全部，2为国内，3为国外，4 指定
    public static final Short RESELLER_SCOPE_DISTRIBUTOR_PERSONAL_ASSIGN=4;

    //分销商适用范围 1为全部，2为国内，3为国外，4 指定
    public static final Short RESELLER_SCOPE_DISTRIBUTOR_FOREIGN_ASSIGN=3;

    //分销商适用范围 1为全部，2为国内，3为国外，4 指定
    public static final Short RESELLER_SCOPE_DISTRIBUTOR_INTERNAL_ASSIGN=2;

    //分销商适用范围 1为全部，2为国内，3为国外，4 指定
    public static final Short RESELLER_SCOPE_DISTRIBUTOR_ALL=1;

    //图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图
    public static final Short PICTURE_TYPE_GENERAL=1;

    //图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图
    public static final Short PICTURE_TYPE_IP=2;

    //图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图
    public static final Short PICTURE_TYPE_TEMPLATE=3;

    //图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图
    public static final Short PICTURE_TYPE_CHARTLET=4;

    //用户自已上传的ID
    public static final Integer PICTURE_ID_USER_UPLOAD=0;

    //用户自已上传的code
    public static final String PICTURE_CODE_USER_UPLOAD="0";

    //用户自已上传的图片名称
    public static final String PICTURE_NAME_USER_UPLOAD="用户自己上传";
}
