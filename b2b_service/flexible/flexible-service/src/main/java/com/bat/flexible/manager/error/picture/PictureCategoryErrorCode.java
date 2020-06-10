package com.bat.flexible.manager.error.picture;

public class PictureCategoryErrorCode {

    /**
     * 父子类型不一致
     */
    public static final String P_CATEGORY_TYPE_DIFFERENT_FROM_WITH_PARENT="P_CATEGORY_TYPE_DIFFERENT_FROM_WITH_PARENT";


    /**
     * 分类还有子类、非最底级分类
     */
    public static final String P_CATEGORY_NOT_BELONG_TO_LOWEST="P_CATEGORY_NOT_BELONG_TO_LOWEST";

    /**
     * 分类还有子类、不能删除
     */
    public static final String P_CATEGORY_DELETE_SON_FIRST="P_CATEGORY_DELETE_SON_FIRST";

    /**
     * 分类还有子类启用、不能禁用
     */
    public static final String P_CATEGORY_DISABLE_SON_FIRST="P_CATEGORY_DISABLE_SON_FIRST";

    /**
     * 分类还有图片关联、不能修改为非最终分类
     */
    public static final String P_CATEGORY_UPDATE_AT_LAST_NO_FAIL_PICTURE_RELA="P_CATEGORY_UPDATE_AT_LAST_NO_FAIL_PICTURE_RELA";

    /**
     * 分类还有关联图片、不能删除
     */
    public static final String P_CATEGORY_DELETE_PICTURE_RELEVANCE="P_CATEGORY_DELETE_PICTURE_RELEVANCE";

    /**
     * 分类还有关联图片、不能禁用
     */
    public static final String P_CATEGORY_DISABLE_PICTURE_RELEVANCE="P_CATEGORY_DISABLE_PICTURE_RELEVANCE";
}
