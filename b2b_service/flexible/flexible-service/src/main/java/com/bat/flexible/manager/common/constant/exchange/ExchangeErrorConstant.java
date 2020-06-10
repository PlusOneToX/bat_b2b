package com.bat.flexible.manager.common.constant.exchange;

/**
 * 兑换卡异常枚举类
 */
public enum ExchangeErrorConstant {

    EndTimeLessThenStartTime(10051,"结束时间必须要大于开始时间"),

    EndTimeLessThenNow(10052,"结束时间必须要大于现在"),

    CardTypeIsNotNullChooseEntity(10053,"生成实体卡时、卡码设置不能为空"),

    RuleTypeIsNotNullChooseEntity(10054,"生成实体卡时、卡片码生成规则不能为空"),

    MallTypeIsNotNullChooseMall(10055,"选择使用兑换商城时、兑换商城类型不能为空"),

    MaterialIsNotNullChooseMall(10056,"选择使用兑换商城时、兑换材质设置不能为空"),

    ModelIsNotNullChooseMall(10057,"选择使用兑换商城且部分可用时、定制型号设置不能为空"),

    PictureIsNotNullChooseMall(10058,"选择使用兑换商城且部分可用时、定制图片设置不能为空"),



    OrderUseThresholdLessThenZero(10061,"订单门槛值不能小于0"),

    UserLimitQuantityLessThenOne(10062,"用户限用数量小于1"),

    IdIsError(10063,"id错误"),

    AssignGoodsListIsNull(10064,"指定商品可用时、货品列表不能为空"),

    GoodsIdNullError(10065,"商品id不能为空"),

    ItemIdNullError(10066,"货品id不能为空"),

    UpdateSameError(10067,"请勿重复操作"),

    IssueFailByEndTimeLessThenNow(10068,"活动时间已结束、不能发布"),

    StopFailByStatusNotStarting(10069,"活动非进行中、不能停用"),

    QrCodeUrlNullByIsUseMallNo(10070,"该活动非兑换商城、无二维码"),

    EntityRuleNull(10071,"实体卡生成规则为空"),



    RandomValueNullError(10073,"明码生成规则选中系统随机、随机位数不能为空"),

    RiseValueNullError(10074,"明码生成规则选中按规则生成、抬头值不能为空"),

    FloatValueNullError(10075,"明码生成规则选中按规则生成、浮动值不能为空"),

    CardNullChooseExchangeDiy(10076,"兑换商城、兑换卡关联不能为空"),

    CodeInvalidFailByUsed(10077,"兑换码已核销、不能作废"),

    CodeInvalidFailByInvalid(10078,"兑换码已作废、请勿重复操作"),

    SourceNullByNotEntity(10079,"非实体卡、生成方式不能为空"),

    CreateCodeFailByStatusEnd(10080,"活动已结束、不能生成兑换码"),

    ImportSecretCodeNullError(10081,"兑换码值不能为空"),



    ImportDataNull(10085,"excel数据不能为空"),

    ExchangeCodeNotNullBySourceImport(10086,"非实体卡生成方式为手动导入、请先导入兑换码"),

    ExchangeCodeNotNullByCardTypeImport(10087,"卡码设置为手动导入、请先导入数据"),

    PlainCodeIsNullByEntityImport(10088,"实体卡导入的明码不能为空"),

    PlainCodeIsNotNullByNoEntityImport(10089,"非实体卡不需要导入明码"),

    CodeQuantityNullByNotImport(10090,"生成数量不能为空"),

    CodeImportFailByPlainAlready(10091,"该兑换码原来有明码"),

    CodeImportFailByNoPlain(10092,"该兑换码原来没有明码"),

    CodeImportFailBySecretCodeChange(10093,"兑换码不允许修改(已关联卡片码)"),

    CodeImportFailByPlainCodeChange(10094,"卡片码不允许修改(已关联兑换码)"),

    CodeExportFailByCodeNull(10095,"无符合条件的券码"),

    SyncFactoryFailByIsEntityNo(10096,"非实体卡不需要同步至工厂"),

    SyncFactoryFailByNoCode(10097,"没有待同步至工厂的兑换码"),

    SyncFactoryFailByDataNull(10098,"没有待同步至工厂的兑换码、请先生成兑换码"),



    ItemAssociatedExchangeStatusError(10100,"对应的兑换卡目前不允许出售、请先联系客服处理"),

    ItemNotBelongExchangeError(10101,"该货品下单时不属于兑换卡"),

    BoxCodeError(10102,"盒码错误"),

    ExchangeCodeStatusNotInitError(10103,"兑换码状态非未激活状态"),

    SyncFactoryFailByQrCodeUrlNull(10104,"请先生成兑换活动二维码"),

    ExchangeCodeSaveFailByCodeRepeat(10105,"兑换码保存失败、请检查导入的兑换码是否已使用"),

    ExchangeRefundNoExist(10106,"退货单号已存在"),

    ExchangeCodeError(10107,"盒码或者明码错误"),

    PlainCodeError(10108,"明码错误"),


    ExchangeCodeRefundFailByCodeStatusNoUnUse(10110,"盒码全都非未使用状态"),

    ExchangeCardInsueFailByParamError(10111,"材质型号图片与现有物料关系不匹配"),

    ExchangeCardSaveNeedConfirmByParamError(10112,"材质型号图片与现有物料关系不匹配、需要确认"),

    ModelNoNullChooseExchangeDiy(10113,"兑换商城、卡片型号不能为空"),

    ExchangeCodeSyncFactoryFail(10114,"兑换码同步至工厂异常"),


    ModelUseTypeNullError(10117,"使用定制商城时、型号范围不能为空"),

    PictureUseTypeNullError(10118,"使用定制商城时、图片范围不能为空"),

    ExchangeCodeRandomNumError(10119,"只支持6位或者7位随机数字"),


    ExchangeRandomInsufficientError(10123,"位随机数字可用数量不足、请更换方式"),

    ExchangeFactoryNumError(10124,"同步到工厂的盒码数量必须要为10的倍数"),


    EntityCardIsSyncFactoryNullError(10125,"选择实体卡、是否线上同步到工厂不能为空"),

    EntityCardBoxNumNullError(10126,"选择实体卡、每盒数量不能为空"),

    EntityCardBoxNumLessThenZeroError(10127,"选择实体卡、每盒数量必须要大于0"),

    ExchangeNotNeedSyncFactoryError(10128,"该兑换卡不需要线上同步至工厂"),

    ExchangeDistributorNoSameError(10129,"分销商指定与对应货品不一致"),

    ExchangeCardRefundFailError(10130,"ERP作废兑换码异常"),

    ExchangeCardRefundFailError1(10131,"ERP作废兑换码异常"),
    ;


    private Integer code;

    private String msg;

    ExchangeErrorConstant(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
