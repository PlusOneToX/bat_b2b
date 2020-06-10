package com.bat.flexible.manager.error.exchange;

public class ExchangeCardErrorCode {

    /**
     * 结束时间必须要大于开始时间
     */
    public static final String EXCHANGE_CARD_ENDTIME_LESSTHEN_STARTTIME="EXCHANGE_CARD_ENDTIME_LESSTHEN_STARTTIME";

    /**
     * 结束时间必须要大于现在
      */
    public static final String EXCHANGE_CARD_ENDTIME_LESSTHEN_NOW="EXCHANGE_CARD_ENDTIME_LESSTHEN_NOW";

    /**
     * 生成实体卡时、卡码设置不能为空
     */
    public static final String EXCHANGE_CARD_CARD_TYPE_NULL_WHEN_ENTITY="EXCHANGE_CARD_CARD_TYPE_NULL_WHEN_ENTITY";

    /**
     * 生成实体卡时、卡片码生成规则不能为空
     */
    public static final String EXCHANGE_CARD_RULE_TYPE_NULL_WHEN_ENTITY="EXCHANGE_CARD_RULE_TYPE_NULL_WHEN_ENTITY";

    /**
     * 选择使用兑换商城时、兑换商城类型不能为空
     */
    public static final String EXCHANGE_CARD_MALL_TYPE_NULL_CHOOSE_EXCHANGE="EXCHANGE_CARD_MALL_TYPE_NULL_CHOOSE_EXCHANGE";


    /**
     * 选择使用兑换商城时、兑换材质设置不能为空
     */
    public static final String EXCHANGE_CARD_MATERIAL_NULL_CHOOSE_MALL="EXCHANGE_CARD_MATERIAL_NULL_CHOOSE_MALL";


    /**
     * 选择使用兑换商城且部分可用时、定制型号设置不能为空
     */
    public static final String EXCHANGE_CARD_MODEL_NULL_CHOOSE_MALL="EXCHANGE_CARD_MODEL_NULL_CHOOSE_MALL";

    /**
     * 选择使用兑换商城且部分可用时、定制图片设置不能为空
     */
    public static final String EXCHANGE_CARD_PICTURE_NULL_CHOOSE_MALL="EXCHANGE_CARD_PICTURE_NULL_CHOOSE_MALL";


    //OrderUseThresholdLessThenZero(10061,""),
    /**
     * 订单门槛值不能小于0
     */
    public static final String EXCHANGE_CARD_ORDER_THRESHOLD_LESSTHEN_ZERO="EXCHANGE_CARD_ORDER_THRESHOLD_LESSTHEN_ZERO";



    /**
     * 用户限用数量小于1
     */
    public static final String EXCHANGE_CARD_USER_LIMIT_QUANTITY_LESS_THEN_ONE="EXCHANGE_CARD_USER_LIMIT_QUANTITY_LESS_THEN_ONE";


    /**
     * 暂不支持指定商品
     */
    public static final String EXCHANGE_CARD_NO_SUPPORT_ASSIGN_ITEM="EXCHANGE_CARD_NO_SUPPORT_ASSIGN_ITEM";

    /**
     * 活动时间已结束、不能发布
     */
    public static final String EXCHANGE_CARD_ISSUE_FAIL_ENDTIME_LESS_THAN_NOW="EXCHANGE_CARD_ISSUE_FAIL_ENDTIME_LESS_THAN_NOW";

    /**
     * 活动非进行中、不能停用
     */
    public static final String EXCHANGE_CARD_STOP_FAIL_STATUS_NOT_STARTING="EXCHANGE_CARD_ISSUE_FAIL_ENDTIME_LESS_THAN_NOW";

    /**
     * 该活动非兑换商城、无二维码
     */
    public static final String EXCHANGE_CARD_QR_CODE_NULL_BECAUSE_NOT_BELONG_MALL="EXCHANGE_CARD_QR_CODE_NULL_BECAUSE_NOT_BELONG_MALL";

    /**
     * 实体卡生成规则为空
     */
    public static final String EXCHANGE_CARD_ENTITY_RULE_NULL="EXCHANGE_CARD_ENTITY_RULE_NULL";

    /**
     * 明码生成规则选中系统随机、随机位数不能为空
     */
    public static final String EXCHANGE_CARD_RANDOM_VALUE_NULL_CHOOSE_RANDOM="EXCHANGE_CARD_RANDOM_VALUE_NULL_CHOOSE_RANDOM";

    /**
     * 明码生成规则选中按规则生成、抬头值不能为空
     */
    public static final String EXCHANGE_CARD_RISE_VALUE_NULL_CHOOSE_RULE="EXCHANGE_CARD_RISE_VALUE_NULL_CHOOSE_RULE";

    /**
     * 明码生成规则选中按规则生成、浮动值不能为空
     */
    public static final String EXCHANGE_CARD_FLOAT_VALUE_NULL_CHOOSE_RULE="EXCHANGE_CARD_FLOAT_VALUE_NULL_CHOOSE_RULE";

    /**
     * 兑换商城、兑换卡关联不能为空
     */
    public static final String EXCHANGE_CARD_CARD_NULL_CHOOSE_MALL="EXCHANGE_CARD_CARD_NULL_CHOOSE_MALL";

    /**
     * 兑换码已核销、不能作废
     */
    public static final String EXCHANGE_CARD_CODE_INVALID_FAIL_BY_USE="EXCHANGE_CARD_CODE_INVALID_FAIL_BY_USE";

    /**
     * 兑换码不是未使用状态、不能作废
     */
    public static final String EXCHANGE_CARD_CODE_INVALID_FAIL_BY_ONLY_NOT_USE="EXCHANGE_CARD_CODE_INVALID_FAIL_BY_ONLY_NOT_USE";

    /**
     * 兑换码不是作废状态、不能还原
     */
    public static final String EXCHANGE_CARD_CODE_UN_INVALID_FAIL_BY_ONLY_INVALID="EXCHANGE_CARD_CODE_UN_INVALID_FAIL_BY_ONLY_INVALID";

    /**
     * 兑换码已作废、请勿重复操作
     */
    public static final String EXCHANGE_CARD_CODE_INVALID_FAIL_BY_INVALID="EXCHANGE_CARD_CODE_INVALID_FAIL_BY_INVALID";

    /**
     * 非实体卡、生成方式不能为空
     */
    public static final String EXCHANGE_CARD_SOURCE_NULL_WHEN_VIRTUAL="EXCHANGE_CARD_SOURCE_NULL_WHEN_VIRTUAL";

    /**
     * 活动已结束、不能生成兑换码
     */
    public static final String EXCHANGE_CARD_CREATE_CODE_FAIL_BY_ACTIVITY_END="EXCHANGE_CARD_CREATE_CODE_FAIL_BY_ACTIVITY_END";

    /**
     * 导入的兑换码值不能为空
     */
    public static final String EXCHANGE_CARD_IMPORT_SECRET_NULL="EXCHANGE_CARD_IMPORT_SECRET_NULL";

    /**
     * 非实体卡生成方式为手动导入、请先导入兑换码
     */
    public static final String EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_CHOOSE_IMPORT="EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_CHOOSE_IMPORT";

    //ExchangeCodeNotNullByCardTypeImport(10087,"卡码设置为手动导入、请先导入数据"),
    /**
     * 卡码设置为手动导入、请先导入数据
     */
    public static final String EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_WHEN_CARD_TYPE_IMPORT="EXCHANGE_CARD_MUST_IMPORT_CODE_BEFORE_WHEN_CARD_TYPE_IMPORT";
    /**
     * 实体卡导入的明码不能为空
     */
    public static final String EXCHANGE_CARD_PLAIN_CODE_NULL_BY_IMPORT="EXCHANGE_CARD_PLAIN_CODE_NULL_BY_IMPORT";

    /**
     * 非实体卡不需要导入明码
     */
    public static final String EXCHANGE_CARD_VIRTUAL_NOT_NEED_IMPORT_PLAIN_CODE="EXCHANGE_CARD_VIRTUAL_NOT_NEED_IMPORT_PLAIN_CODE";

    /**
     * 生成数量不能为空
     */
    public static final String EXCHANGE_CARD_CODE_QUANTITY_NULL="EXCHANGE_CARD_CODE_QUANTITY_NULL";

    /**
     * 该兑换码原来有明码
     */
    public static final String EXCHANGE_CARD_PLAIN_CODE_NOT_NULL_BEFORE="EXCHANGE_CARD_PLAIN_CODE_NOT_NULL_BEFORE";

    /**
     * 该兑换码原来没有明码
     */
    public static final String EXCHANGE_CARD_PLAIN_CODE_NULL_BEFORE="EXCHANGE_CARD_PLAIN_CODE_NULL_BEFORE";

    /**
     * 兑换码不允许修改(已关联卡片码)
     */
    public static final String EXCHANGE_CARD_CODE_FORBIN_UPDATE="EXCHANGE_CARD_CODE_FORBIN_UPDATE";

    /**
     * 卡片码不允许修改(已关联兑换码)
     */
    public static final String EXCHANGE_CARD_PLAIN_CODE_FORBIN_UPDATE="EXCHANGE_CARD_PLAIN_CODE_FORBIN_UPDATE";

    /**
     * 无符合条件的券码
     */
    public static final String EXCHANGE_CARD_CODE_EXPORT_FAIL_BY_CODE_NULL="EXCHANGE_CARD_CODE_EXPORT_FAIL_BY_CODE_NULL";

    /**
     * 非实体卡不需要同步至工厂
     */
    public static final String EXCHANGE_CARD_VIRTUAL_NOT_NEED_SYNC_FACTORY="EXCHANGE_CARD_VIRTUAL_NOT_NEED_SYNC_FACTORY";

    /**
     * 没有待同步至工厂的兑换码
     */
    public static final String EXCHANGE_CARD_NO_CODE_NEED_SYNC_FACTRORY="EXCHANGE_CARD_NO_CODE_NEED_SYNC_FACTRORY";

    /**
     * 对应的兑换卡目前不允许出售、请先联系客服处理
     */
    public static final String EXCHANGE_CARD_CANNOT_SALE_NOW="EXCHANGE_CARD_CANNOT_SALE_NOW";

    /**
     * 该货品下单时不属于兑换卡
     */
    public static final String EXCHANGE_CARD_ORDER_ITEM_NOT_BELONG_EXCHANGE="EXCHANGE_CARD_ORDER_ITEM_NOT_BELONG_EXCHANGE";

    /**
     * 盒码错误
     */
    public static final String EXCHANGE_CARD_BOX_CODE_ERROR="EXCHANGE_CARD_BOX_CODE_ERROR";

    /**
     * 兑换码状态非未激活状态
     */
    public static final String EXCHANGE_CARD_CODE_STATUS_NOT_INIT="EXCHANGE_CARD_CODE_STATUS_NOT_INIT";

    /**
     * 请先生成兑换活动二维码
     */
    public static final String EXCHANGE_CARD_CREATE_QR_CODE_FIRST="EXCHANGE_CARD_CREATE_QR_CODE_FIRST";

    /**
     * 兑换码保存失败、请检查导入的兑换码是否已存在
     */
    public static final String EXCHANGE_CARD_CODE_SAVE_FAIL_BY_CODE_EXIST="EXCHANGE_CARD_CODE_SAVE_FAIL_BY_CODE_EXIST";

    /**
     * 退货单号已存在
     */
    public static final String EXCHANGE_CARD_REFUND_NO_EXIST="EXCHANGE_CARD_REFUND_NO_EXIST";

    /**
     * 盒码或者明码错误
     */
    public static final String EXCHANGE_CARD_PLAIN_OR_BOX_CODE_ERROR="EXCHANGE_CARD_PLAIN_OR_BOX_CODE_ERROR";

    /**
     * 明码错误
     */
    public static final String EXCHANGE_CARD_PLAIN_CODE_ERROR="EXCHANGE_CARD_PLAIN_CODE_ERROR";

    /**
     * 暗码错误
     */
    public static final String EXCHANGE_CARD_SECRET_CODE_ERROR="EXCHANGE_CARD_SECRET_CODE_ERROR";

    /**
     * 盒码全都非未使用状态
     */
    public static final String EXCHANGE_CARD_BOX_CODE_STATUS_ALL_NOT_UN_USED="EXCHANGE_CARD_BOX_CODE_STATUS_ALL_NOT_UN_USED";

    /**
     * 材质型号图片与现有物料关系不匹配
     */
    public static final String EXCHANGE_CARD_MATERIAL_MODEL_PICTURE_NOT_SAME_BY_SAME_ITEM_ERROR="EXCHANGE_CARD_MATERIAL_MODEL_PICTURE_NOT_SAME_BY_SAME_ITEM_ERROR";

    /**
     * 兑换商城、卡片型号不能为空
     */
    public static final String EXCHANGE_CARD_MODEL_NO_NULL_CHOOSE_MALL="EXCHANGE_CARD_MODEL_NO_NULL_CHOOSE_MALL";

    /**
     * 兑换码同步至工厂异常
     */
    public static final String EXCHANGE_CARD_CODE_SYNC_FACTORY_EXCEPTION="EXCHANGE_CARD_CODE_SYNC_FACTORY_EXCEPTION";

    /**
     * 使用定制商城时、型号范围不能为空
     */
    public static final String EXCHANGE_CARD_MODEL_USE_TYPE_NULL="EXCHANGE_CARD_MODEL_USE_TYPE_NULL";

    /**
     * 使用定制商城时、图片范围不能为空
     */
    public static final String EXCHANGE_CARD_PICTURE_USE_TYPE_NULL="EXCHANGE_CARD_PICTURE_USE_TYPE_NULL";

    /**
     * 只支持6位或者7位随机数字
     */
    public static final String EXCHANGE_CARD_CODE_RANDOM_NUM_LENGTH_ONLY_SIX_SEVEN="EXCHANGE_CARD_CODE_RANDOM_NUM_LENGTH_ONLY_SIX_SEVEN";

    /**
     * 位随机数字可用数量不足、请更换方式
     */
    public static final String EXCHANGE_CARD_RANDOM_INSUFFICIENT_ERROR="EXCHANGE_CARD_RANDOM_INSUFFICIENT_ERROR";

    /**
     * 同步到工厂的盒码数量必须要为10的倍数
     */
    public static final String EXCHANGE_CARD_BOX_CODE_NUM_ALIQUOT_TEN="EXCHANGE_CARD_BOX_CODE_NUM_ALIQUOT_TEN";

    /**
     * 选择实体卡、是否线上同步到工厂不能为空
     */
    public static final String EXCHANGE_CARD_IS_SYNC_FACTORY_NULL_CHOOSE_ENTITY="EXCHANGE_CARD_IS_SYNC_FACTORY_NULL_CHOOSE_ENTITY";

    /**
     * 选择实体卡、每盒数量不能为空
     */
    public static final String EXCHANGE_CARD_BOX_NUM_NULL_CHOOSE_ENTITY="EXCHANGE_CARD_BOX_NUM_NULL_CHOOSE_ENTITY";

    /**
     * 选择实体卡、每盒数量必须要大于0
     */
    public static final String EXCHANGE_CARD_BOX_NUM_LESS_THAN_ZERO_CHOOSE_ENTITY="EXCHANGE_CARD_BOX_NUM_LESS_THAN_ZERO_CHOOSE_ENTITY";

    /**
     * 该兑换卡不需要线上同步至工厂
     */
    public static final String EXCHANGE_CARD_NOT_NEED_SYNC_FACTORY_ONLINE="EXCHANGE_CARD_NOT_NEED_SYNC_FACTORY_ONLINE";

    /**
     * 分销商指定与对应货品不一致
     */
    public static final String EXCHANGE_CARD_DISTRIBUTOR_ASSIGN_ITEM_NOT_SAME="EXCHANGE_CARD_DISTRIBUTOR_ASSIGN_ITEM_NOT_SAME";

    /**
     * 兑换码保存失败、请检查导入的兑换码是否已使用
     */
    public static final String EXCHANGE_CARD_CODE_SAVE_FAIL_BY_CODE_REPEAT="EXCHANGE_CARD_CODE_SAVE_FAIL_BY_CODE_REPEAT";

    /**
     * 无符合条件盒码明码同步至ERP
     */
    public static final String EXCHANGE_CARD_CODE_SYNC_ERP_FAIL_BY_NONE="EXCHANGE_CARD_CODE_SYNC_ERP_FAIL_BY_NONE";

    /**
     * 同步失效的兑换码到erp失败
     */
    public static final String EXCHANGE_CARD_CODE_SYNC_INVALID_ERP_FAIL="EXCHANGE_CARD_CODE_SYNC_INVALID_ERP_FAIL";

    /**
     * 随机数池文件为空、请联系技术进行设置
     */
    public static final String EXCHANGE_CARD_RANDOM_NUMBER_TXT_NULL="EXCHANGE_CARD_RANDOM_NUMBER_TXT_NULL";

    /**
     * 获取随机数失败
     */
    public static final String EXCHANGE_CARD_RANDOM_NUMBER_QUERY_FAIL="EXCHANGE_CARD_RANDOM_NUMBER_QUERY_FAIL";

    /**
     * 修改随机数失败
     */
    public static final String EXCHANGE_CARD_RANDOM_NUMBER_UPDATE_FAIL="EXCHANGE_CARD_RANDOM_NUMBER_UPDATE_FAIL";

    /**
     * 同步盒码和明码到erp失败
     */
    public static final String EXCHANGE_CARD_CODE_SYNC_ERP_FAIL="EXCHANGE_CARD_CODE_SYNC_ERP_FAIL";

    /**
     * 兑换码非未使用状态、请与客服沟通联系
     */
    public static final String EXCHANGE_CARD_CODE_STATUS_NOT_BELONG_UN_USE="EXCHANGE_CARD_CODE_STATUS_NOT_BELONG_UN_USE";

    /**
     * 兑换卡已被绑定、请勿重复操作
     */
    public static final String EXCHANGE_CARD_CODE_USER_BIND_REPEAT="EXCHANGE_CARD_CODE_USER_BIND_REPEAT";

    /**
     * 兑换卡解密失败、请与客服沟通处理
     */
    public static final String EXCHANGE_CARD_CODE_DECODE_FAIL="EXCHANGE_CARD_CODE_DECODE_FAIL";

    /**
     * 兑换卡选择指定分销商时、分销商列表不能为空
     */
    public static final String EXCHANGE_CARD_DISTRIBUTOR_LIST_NULL_WHEN_SCOPE_ASSIGN="EXCHANGE_CARD_DISTRIBUTOR_LIST_NULL_WHEN_SCOPE_ASSIGN";


    /**
     * 兑换码已过期、无法完成兑换
     */
    public static final String EXCHANGE_CARD_CODE_STATUS_END="EXCHANGE_CARD_CODE_STATUS_END";

    /**
     * 生成小程序二维码失败、因为分销商尚未绑定小程序
     */
    public static final String EXCHANGE_QR_CODE_FAIL_DISTRIBUTOR_RELA_WECHAT_NULL="EXCHANGE_QR_CODE_FAIL_DISTRIBUTOR_RELA_WECHAT_NULL";

    /**
     * 生成小程序二维码失败、请与客服沟通处理
     */
    public static final String EXCHANGE_QR_CODE_FAIL="EXCHANGE_QR_CODE_FAIL";

    /**
     * 兑换卡导出数量有误
     */
    public static final String EXCHANGE_EXPORT_NUM_ERROR="EXCHANGE_EXPORT_NUM_ERROR";

    /**
     * 找不到记录
     */
    public static final String EXCHANGE_NO_RECORD="EXCHANGE_NO_RECORD";

    /**
     * 实体卡不支持导出
     */
    public static final String EXCHANGE_ENTITY_CANT_EXPORT="EXCHANGE_ENTITY_CANT_EXPORT";

    /**
     * 暗码校验异常
     */
    public static final String EXCHANGE_SECRET_CODE_VALID_EXCEPTION="EXCHANGE_SECRET_CODE_VALID_EXCEPTION";

    /**
     * 兑换码核销异常
     */
    public static final String EXCHANGE_CODE_USE_EXCEPTION="EXCHANGE_CODE_USE_EXCEPTION";

    /**
     * ，兑换卡已被领取
     */
    public static final String EXCHANGE_HAS_RECEICE="EXCHANGE_HAS_RECEICE";

    /**
     * 兑换卡系统错误
     */
    public static final String EXCHANGE_SYSTEM_ERROR="EXCHANGE_SYSTEM_ERROR";

    /**
     * 有其他记录正在导出；请稍后再试
     */
    public static final String EXCHANGE_HAS_EXPORT="EXCHANGE_HAS_EXPORT";

    /**
     * 权益码不存在
     */
    public static final String EXCHANGE_QUANYI_NOT_ESIST="EXCHANGE_QUANYI_NOT_ESIST";

    /**
     * 该订单已经被系统取消；请联系客服处理
     */
    public static final String EXCHANGE_QUANYI_SYSTEM_CANCEL="EXCHANGE_QUANYI_SYSTEM_CANCEL";
}
