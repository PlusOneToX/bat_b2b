package com.bat.flexible.manager.shop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.shop.ShopServiceI;
import com.bat.flexible.api.shop.dto.ShopCmd;
import com.bat.flexible.api.shop.dto.ShopExcelQry;
import com.bat.flexible.api.shop.dto.ShopPageQry;
import com.bat.flexible.api.shop.dto.UserShopPageQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.util.excel.ExcelUtil;
import com.bat.flexible.manager.common.ConfigQry;
import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.dubbo.DistributorConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeErrorConstant;
import com.bat.flexible.manager.common.constant.shop.ShopConstant;
import com.bat.flexible.manager.common.utils.qr.QrCodeUtil;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import com.bat.flexible.manager.error.shop.ShopErrorCode;
import com.bat.flexible.manager.shop.executor.ShopCmdExe;
import com.bat.flexible.manager.shop.executor.ShopQryExe;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.dubboapi.thirdparty.qrcode.api.ThirdPartyQrCodeServiceRpc;
import com.bat.dubboapi.thirdparty.qrcode.dto.ShopQrCodeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.flexible.dao.shop.co.ShopPageCO;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import com.bat.flexible.manager.shop.convertor.ShopConvertor;

@Service
public class ShopServiceImpl implements ShopServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopCmdExe shopCmdExe;

    @Autowired
    private ShopQryExe shopQryExe;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private ThirdPartySystemOssServiceRpc thirdPartySystemOssServiceRpc;

    @Resource
    private ConfigQry configQry;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private ThirdPartyQrCodeServiceRpc thirdPartyQrCodeServiceRpc;

    @Value("${shop.importShop1}")
    private String importShopByAdmin;

    @Value("${shop.importShop2}")
    private String importShopByUser;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WarehouseServiceRpc warehouseServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WxServiceRpc wxServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @Override
    @Transactional
    public Response create(ShopCmd shopCmd, AdminResponse currentAdmin, Boolean fromAdmin) {
        try {
            ShopDO shopDO = BeanUtils.copy(shopCmd, ShopDO.class);
            shopDO.setCompanyName(shopCmd.getDistributorCompanyName());
            setAdminMsg(shopDO, currentAdmin);
            shopDO.setSource(fromAdmin ? ShopConstant.SOURCE_ADMIN : ShopConstant.SOURCE_FRONT_DESK);
            ShopDO shop = shopQryExe.getByDistributorIdAndShopCode(shopCmd.getDistributorId(), shopCmd.getShopCode());
            if (shop != null) {
                throw new FlexibleCustomException("该分销商已有相同编号的门店");
            }
            shopCmdExe.create(shopDO);
            LOGGER.info("新增门店、返回{}", JSON.toJSONString(shopDO));
            createUrl(shopDO.getAppId(), shopDO);
            createExtendParam(shopDO);
            shopCmdExe.update(shopDO);
            return Response.buildSuccess();
        } catch (FlexibleCustomException e) {
            e.printStackTrace();
            LOGGER.error("FlexibleCustomException,{}", e.getMessage());
            return Response.buildFailure(e.getMessage(), MessageUtils.get(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("新增门店、系统异常{}", e);
            return Response.buildFailure("SYSTEM_EXCEPTION", "系统异常");
        }

    }

    @Override
    @Transactional
    public Response update(ShopCmd shopCmd, AdminResponse currentAdmin) {
        ShopDO shopDO = shopQryExe.getById(shopCmd.getId());
        shopDO.setOpenFlag(shopCmd.getOpenFlag());
        shopDO.setExtendParam(shopCmd.getExtendParam());
        shopDO.setRemark(shopCmd.getRemark());
        shopDO.setShopName(shopCmd.getShopName());
        setAdminMsg(shopDO, currentAdmin);
        createUrl(shopDO.getAppId(),shopDO);
        createExtendParam(shopDO);
        shopDO.setUserConfigId(shopCmd.getUserConfigId());
        shopDO.setUserConfigName(shopCmd.getUserConfigName());
        shopDO.setSalemanId(shopCmd.getSalemanId());
        shopDO.setSalemanName(shopCmd.getSalemanName());
        shopCmdExe.update(shopDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response updateCreate(ShopCmd shopCmd, AdminResponse currentAdmin) {
        ShopDO shopDO = shopQryExe.getById(shopCmd.getId());
        setAdminMsg(shopDO, currentAdmin);
        createUrl(shopDO.getAppId(),shopDO);
        createExtendParam(shopDO);
        shopCmdExe.update(shopDO);
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<ShopPageCO> page(ShopPageQry shopPageQry) {
        PageHelper.startPage(shopPageQry.getPage(), shopPageQry.getSize());
        List<ShopPageCO> list =
            shopQryExe.listCOByCondition(shopPageQry.getOpenFlag(), shopPageQry.getContent(), null, null, null, null);

        return new PageInfo<>(list);
    }

    @Override
    public Response<ShopPageCO> detailById(Integer id) {
        ShopDO shopDO = shopQryExe.getById(id);
        ShopPageCO copy = BeanUtils.copy(shopDO, ShopPageCO.class);
        copy.setDistributorCompanyName(shopDO.getCompanyName());
        return Response.of(copy);
    }

    @Override
    public HSSFWorkbook export(ShopExcelQry shopExcelQry) {
        List<ShopPageCO> list = shopQryExe.listCOByCondition(shopExcelQry.getOpenFlag(), shopExcelQry.getContent(),
            shopExcelQry.getDistributorId(), shopExcelQry.getAppName(), shopExcelQry.getShopCode(),
            shopExcelQry.getShopName());

        if (list == null || list.size() == 0) {
            return null;
        }

        return ShopConvertor.toHSSFWorkbook(list);
    }

    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        ShopDO shopDO = shopQryExe.getById(flexibleUpdateStatusDTO.getId());
        if (shopDO.getOpenFlag() - flexibleUpdateStatusDTO.getOpenFlag() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        shopDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(shopDO, currentAdmin);
        shopCmdExe.update(shopDO);
        return Response.buildSuccess();
    }

    /**
     * 设置操作人信息
     *
     * @param shopDO
     * @param currentAdmin
     */
    private void setAdminMsg(ShopDO shopDO, AdminResponse currentAdmin) {
        if (shopDO.getId() == null) {
            shopDO.setCreateTime(new Date());
            shopDO.setCreateUserName(currentAdmin.getUserName());
            shopDO.setCreateUserId(currentAdmin.getId());
            shopDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        }
        shopDO.setUpdateTime(new Date());
        shopDO.setUpdateUserId(currentAdmin.getId());
        shopDO.setUpdateUserName(currentAdmin.getUserName());
    }

    /**
     * 拼接部分url 地址
     *
     * @param appId
     * @param shopDO
     * @return
     */
    public void createUrl(String appId, ShopDO shopDO) {
        //判断是公众号还是小程序
        if(ShopConstant.SHOP_TYPE_WECHAT_SMALL_PROGRAMS.equals(shopDO.getType())){
            //小程序
            String scene ="platform="+shopDO.getPlatform()+"&distributorId="+shopDO.getDistributorId()+"&appid="+appId+"+&shopId="+shopDO.getId()+"&wxPlatformId="+shopDO.getWxPlatformId();
            com.bat.dubboapi.thirdparty.common.Response<String> response = wxServiceRpc.createWechatProgramQrCode(shopDO.getAppId(), scene, null,
                    "qrcode_"+shopDO.getShopCode()+ ".jpg", "shop/qr/",null);
            if(response ==null || !response.isSuccess() || StringUtils.isBlank(response.getData())){
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL);
            }
            shopDO.setQrUrl(response.getData());
            shopDO.setUrl(null);
            return;
        }
        // 去获取appid（第三方服务）
        FlexibleConfig flexibleConfig=configQry.getTenantShopUrl();
        String url = MessageFormat.format(flexibleConfig.getWxurl(), shopDO.getPlatform() + "",
            shopDO.getDistributorId() + "", appId);
        url += "&shopId=" + shopDO.getId() + "&wxPlatformId=" + shopDO.getWxPlatformId();
        /*       ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        QrCodeUtil.createQrCodeExtendStream(url,byteArrayOut,shopDO.getShopCode());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOut.toByteArray());
        String qrCodeUrl = thirdPartySystemOssServiceRpc.uploadExtendStream(shopDO.getId()+"_"+shopDO.getShopCode() + ".jpg", "shop/qr/", inputStream);*/
        ShopQrCodeRpcCmd shopQrCodeRpcCmd = new ShopQrCodeRpcCmd();
        shopQrCodeRpcCmd.setShopCode(shopDO.getShopCode());
        shopQrCodeRpcCmd.setUrl(url);
        LOGGER.info("开始生成门店编码二维码：{}", JSON.toJSONString(shopQrCodeRpcCmd));
        try {
            com.bat.dubboapi.thirdparty.common.Response<String> response =
                thirdPartyQrCodeServiceRpc.shopQrCode(shopQrCodeRpcCmd);
            LOGGER.info("生成门店二维码、返回{}", JSON.toJSONString(response));
            if (!response.isSuccess()) {
                throw new FlexibleCustomException(response.getErrMessage());
            }
            shopDO.setQrUrl(response.getData());
            shopDO.setUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("生成门店二维码URL系统异常{}", e.getMessage());
            throw new FlexibleCustomException(ShopErrorCode.S_SHOP_CODE_CREATE_FAIL);
        }

    }

    /**
     * 设置扩展字段
     *
     * @param shopDO
     */
    public void createExtendParam(ShopDO shopDO) {
        if (StringUtils.isBlank(shopDO.getExtendParam())) {
            shopDO.setThirdQrUrl(null);
            return;
        }
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        QrCodeUtil.createQrCodeExtendStream(shopDO.getExtendParam(), byteArrayOut,
            shopDO.getShopName() + ":" + shopDO.getShopCode());
        // ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOut.toByteArray());
        byte[] bytes = byteArrayOut.toByteArray();
        LOGGER.info("上传第三方拓展参数二维码：{}", bytes.length);
        String thirdQrUrl = thirdPartySystemOssServiceRpc.uploadExtendStream(
            shopDO.getId() + "_" + shopDO.getShopName() + ":" + shopDO.getShopCode() + ".jpg", "shop/qr/third/", bytes);
        shopDO.setThirdQrUrl(thirdQrUrl);
    }

    @Override
    @Transactional
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        ShopDO shopDO = shopQryExe.getById(id);
        setAdminMsg(shopDO, currentAdmin);
        shopDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        shopCmdExe.update(shopDO);
        return Response.buildSuccess();
    }

    @Override
    public InputStream tempDownLoad(Boolean fromAdmin) {
        String fileName = "店铺管理.xls";
        String ossKey = importShopByAdmin;
        if (!fromAdmin) {
            // PC端导出
            ossKey = importShopByUser;
        }
        try {
            // InputStream inputStream = thirdPartySystemOssServiceRpc.getExcelTemp(ossKey, fileName);
            byte[] bytes = thirdPartySystemOssServiceRpc.getExcelTempByteArr(ossKey, fileName);
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以后要优化
     *
     * 本方法导入excel表，解析店铺信息
     *
     * @param inputStream
     * @param adminResponse
     * @param fromAdmin
     *            操作来源 true 表示后台 false、表示PC前台
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Response importShop(InputStream inputStream, AdminResponse adminResponse, Boolean fromAdmin) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception ex) {
            LOGGER.error("excel 创建失败");
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_IMPORT_ERROE);
        }
        // 第二工作薄存放上传数据
        Sheet hssfSheet = workbook.getSheetAt(1);
        if (hssfSheet == null) {
            LOGGER.error("上传工作簿不存在");
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_EXCEL_IMPORT_ERROE);
        }
        // 获取所有的行数
        String sheetName = hssfSheet.getSheetName();
        int lastRowNum = hssfSheet.getLastRowNum() - hssfSheet.getFirstRowNum() + 1;
        for (int rowNum = 1; rowNum < lastRowNum; rowNum++) {
            try {
                Row xssfRow = hssfSheet.getRow(rowNum);
                String id = ExcelUtil.getValue(xssfRow.getCell(0));
                Cell cell = xssfRow.getCell(5);
                // 前台后台导入模板不一致 前台 没有B2Bid
                Integer distributorId;
                String shopCode;
                String shopName;
                String appId;
                String appName;
                String extendParam;
                if (!fromAdmin) {
                    // 前台
                    distributorId = adminResponse.getId();
                    shopCode = ExcelUtil.getValue(xssfRow.getCell(1));
                    shopName = ExcelUtil.getValue(xssfRow.getCell(2));
                    appId = ExcelUtil.getValue(xssfRow.getCell(3));
                    appName = ExcelUtil.getValue(xssfRow.getCell(4));
                    extendParam = ExcelUtil.getValue(xssfRow.getCell(5));
                } else {
                    distributorId = Integer.parseInt(ExcelUtil.getValue(xssfRow.getCell(1)));
                    shopCode = ExcelUtil.getValue(xssfRow.getCell(2));
                    shopName = ExcelUtil.getValue(xssfRow.getCell(3));
                    appId = ExcelUtil.getValue(xssfRow.getCell(4));
                    appName = ExcelUtil.getValue(xssfRow.getCell(5));
                    extendParam = ExcelUtil.getValue(xssfRow.getCell(6));
                }
                if (!StringUtils.isNoneBlank(id, String.valueOf(distributorId), shopCode, shopName, appId, appName)) {
                    LOGGER.error("参数不合规：" + sheetName + "表第" + (rowNum + 1) + "行，数据错误");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new FlexibleCustomException(
                        sheetName + "表第" + (rowNum + 1) + ExchangeErrorConstant.ImportSecretCodeNullError.getMsg());
                }
                // 检查分销商是否正确 是否存在
                com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse =
                    distributorServiceRpc.distributorById(distributorId);
                if (!distributorRpcDTOResponse.isSuccess() || distributorRpcDTOResponse.getData() == null) {
                    LOGGER.error("分销商id：" + distributorId + "对应的分销商不存在");
                    throw new FlexibleCustomException(
                        MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_DISTRIBUTOR)
                            + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
                }
                ShopDO shopDO = shopQryExe.getByDistributorIdAndShopCode(distributorId, shopCode);
                // 检查店铺编码与分销商组合 是否唯一
                if (shopDO != null) {
                    LOGGER.error("分销商：" + distributorId + "下已经存在同名店铺");
                    throw new FlexibleCustomException(sheetName + "表第" + (rowNum + 1) + "行,编号" + shopCode + "已存在");
                }
                // 校验appId 与 appName 是否正确
                /*  List<DistributorWxPlatformList> appIdList = getAppIdList(one.getName());
                boolean b = appIdList.stream()
                        .anyMatch(distributorWxPlatformList -> distributorWxPlatformList.getAppId().equals(appId)
                                && distributorWxPlatformList.getPlatformName().equals(appName));*/
                Boolean b = true;
                if (!b) {
                    LOGGER.error(sheetName + "表第" + (rowNum + 1) + "行,excel导入 appId 与 appName错误");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new FlexibleCustomException(sheetName + "表第" + (rowNum + 1) + "行,excel导入 appId 与 appName错误");
                }

                // 数据正常
                ShopDO shop = new ShopDO();
                shop.setAppId(appId);
                shop.setAppName(appName);
                shop.setDistributorId(distributorId);
                shop.setUpdateTime(new Date());
                shop.setCreateTime(new Date());
                shop.setUpdateUserId(adminResponse.getId());
                shop.setCreateUserId(adminResponse.getId());
                shop.setShopCode(shopCode);
                shop.setShopName(shopName);
                shop.setOpenFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
                shop.setCompanyName(distributorRpcDTOResponse.getData().getCompanyName());
                shop.setDistributorName(distributorRpcDTOResponse.getData().getName());
                shop.setExtendParam(extendParam);
                shop.setSource(fromAdmin ? ShopConstant.SOURCE_ADMIN : ShopConstant.SOURCE_FRONT_DESK);
                // 因为要获取当前存储id 拼接到url中，所以先存储后更新，
                // 这是个大事务 因为赶时间 加上不是高频操作 以后必须要优化的
                // 优化：暂时不存储 url地址，在查询的时候设置url地址
                // 优化: 不在循环中 操作数据库
                // 待优化 解析excel 多线程
                shop.setDelFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO);
                com.bat.dubboapi.distributor.common.Response<List<WxPlatformRpcDTO>> wxPlatformResponse= wxPlatformServiceRpc.listByDistributorIdAndType(distributorId, DistributorConstant.DISTRIBUTOR_WECHAT_TYPE_OFFICIAL_ACCOUNT);

                boolean hasAppIdRecord=false;
                if(wxPlatformResponse.isSuccess()||wxPlatformResponse.getData()!=null){
                    List<WxPlatformRpcDTO> wxPlatforms=wxPlatformResponse.getData();
                    for(WxPlatformRpcDTO wxPlatformRpcDTO:wxPlatforms){
                        if(wxPlatformRpcDTO.getAppId().equals(appId)){
                            hasAppIdRecord=true;
                            shop.setPlatform(wxPlatformRpcDTO.getPlatform());
                            shop.setWxPlatformId(wxPlatformRpcDTO.getId());
                        }

                    }

                }
                if(!hasAppIdRecord){
                    throw new FlexibleCustomException(sheetName + "表第" + (rowNum + 1) + "行,excel导入 appId 找不到分销商与其对应的配置记录");
                }

                shopCmdExe.create(shop);
                createUrl(shop.getAppId(), shop);
                createExtendParam(shop);
                shopCmdExe.update(shop);
            } catch (FlexibleCustomException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new FlexibleCustomException(e.getMessage());
            }

        }
        return Response.buildSuccess();
    }

    @Override
    public ShopDO getById(Integer id) {
        return shopQryExe.getById(id);
    }

    @Override
    public ShopDO getShopByDistributorIdAndShopCode(Integer distributorId, String shopCode) {
        return shopQryExe.getByDistributorIdAndShopCode(distributorId, shopCode);
    }

    @Override
    public Response batchDelete(List<Integer> idList, AdminResponse currentAdmin) {
        idList.stream().forEach(id -> {
            deleteById(id, currentAdmin);
        });
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<ShopPageCO> pageByUser(UserShopPageQry userShopPageQry) {
        PageHelper.startPage(userShopPageQry.getPage(), userShopPageQry.getSize());
        List<ShopPageCO> list = shopQryExe.listCOByCondition(userShopPageQry.getOpenFlag(),
            userShopPageQry.getContent(), userShopPageQry.getDistributorId(), userShopPageQry.getAppName(),
            userShopPageQry.getShopCode(), userShopPageQry.getShopName());
        return new PageInfo<>(list);
    }

    /**
     * 条件查询门店列表
     * @param userConfigId
     * @return
     */
    @Override
    public List<ShopDO> listByCondition(Integer userConfigId) {
        return shopQryExe.listByCondition(userConfigId);
    }
}
