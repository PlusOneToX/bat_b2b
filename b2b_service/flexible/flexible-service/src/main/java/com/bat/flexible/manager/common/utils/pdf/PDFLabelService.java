package com.bat.flexible.manager.common.utils.pdf;

import com.alibaba.fastjson.JSON;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.bat.flexible.api.label.LabelServiceI;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.manager.common.constant.label.LabelConstant;
import com.bat.flexible.manager.common.utils.code.BarCodeUtils;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.label.dto.OrderDiyLabelDTO;
import com.bat.dubboapi.flexible.label.dto.OrderGoodsDiySimpleDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.manager.common.utils.IOUtils;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.label.LabelErrorCode;
import com.bat.flexible.manager.label.validator.LabelValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.krysalis.barcode4j.tools.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PDFLabelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFLabelService.class);

    private static String fontPath;

    @Value("${font.path}")
    public  void setFontPath(String fontPath) {
        PDFLabelService.fontPath = fontPath;
    }




    private static final String folder = "label";

    @Value("${country.china.id}")
    private Integer chinaCountryId;


    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private ModelServiceI modelServiceI;

    @Autowired
    private LabelServiceI labelServiceI;

    @DubboReference(check = false,timeout = 10000)
    private ThirdPartySystemOssServiceRpc thirdPartySystemOssServiceRpc;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    /**
     * 根据订单生成标签
     */
    public List<OrderDiyLabelDTO> generateLabelFile(String factoryNo, OrderLableCmd orderLableCmd) {
        try {
            LOGGER.info("开始给订单{}生成标签,请求参数为{}",orderLableCmd.getOrderId(), JSON.toJSONString(orderLableCmd));
            //校验订单生成标签参数
            LabelValidator.validaOrderCreateLabel(orderLableCmd);
            List<OrderDiyLabelDTO> list = new ArrayList<>();
            List<OrderGoodsDiySimpleDTO> diySimpleDTOList = orderLableCmd.getDiySimpleDTOList();
            Integer distributorId = orderLableCmd.getDistributorId();
            Integer countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(distributorId);
            Short distributorScope = LabelConstant.SCOPE_DISTRIBUTOR_INTERNAL;
            if(countryId !=chinaCountryId.intValue()){
                //国外
                distributorScope = LabelConstant.SCOPE_DISTRIBUTOR_FOREIGN;
            }
            //货品信息
            Map<Integer, GoodsItemRpcDTO> itemMap =new HashMap<>();

            if (diySimpleDTOList == null || diySimpleDTOList.size() ==0) {
                String errMessage = String.format("订单详情数据为空，订单编号(%s)", orderLableCmd.getOrderId());
                LOGGER.info(errMessage);
                return list;
            }
            Map<Integer, PictureDO> pictureDTORpcQryMap = new HashMap<>();
            for (OrderGoodsDiySimpleDTO orderGoodsDiySimpleDTO : diySimpleDTOList) {
                //生成标签
                Integer labelId = orderGoodsDiySimpleDTO.getLabelId();

                OrderDiyLabelDTO orderDiyLabelDTO = new OrderDiyLabelDTO();
                orderDiyLabelDTO.setId(orderGoodsDiySimpleDTO.getId());

                LabelDO labelDO = null;
                if(labelId ==null){
                    List<Integer> distributorIds=flexibleDistributorQryExe.getDistributorTreePaths(distributorId);
                    List<LabelDO> labelDOList = labelServiceI.listDistributorsDiyLableByCondition(distributorIds,orderGoodsDiySimpleDTO.getCategoryId(),orderGoodsDiySimpleDTO.getPictureId(),distributorScope);
                    if(labelDOList !=null && labelDOList.size()>0){
                        labelDO = labelDOList.get(0);
                    }
                }else{
                    labelDO = labelServiceI.getById(labelId);
                }
                if(labelDO ==null){
                    throw FlexibleDubboApiException.buildException("找不到可用标签，图片id"+orderGoodsDiySimpleDTO.getPictureId());
                }
                labelId = labelDO.getId();
                orderDiyLabelDTO.setLabelId(labelDO.getId());
                String templateFile = labelDO.getTemplateFile();
                PictureDO pictureDO = null;
                if(orderGoodsDiySimpleDTO.getPictureId() >0){
                    //
                    pictureDO = pictureDTORpcQryMap.get(orderGoodsDiySimpleDTO.getPictureId());
                    if(pictureDO ==null){
                        pictureDO = pictureServiceI.getById(orderGoodsDiySimpleDTO.getPictureId());
                        pictureDTORpcQryMap.put(pictureDO.getId(),pictureDO);
                    }
                }
                MaterialDO materialDO = materialServiceI.getById(orderGoodsDiySimpleDTO.getMaterialId());
                String fileName = generationFilePath(templateFile, distributorId, orderGoodsDiySimpleDTO.getId());
                LOGGER.info("生成标签文件名：{}，字体{}",fileName,fontPath);
                GoodsItemRpcDTO goodsItemRpcDTO = itemMap.get(materialDO.getItemId());
                if(goodsItemRpcDTO ==null){
                    List<Integer> itemIdList = new ArrayList<>();
                    itemIdList.add(materialDO.getItemId());
                    Response<List<GoodsItemRpcDTO>> itemResponse = goodsServiceRpc.listGoodsItemByIds(itemIdList);
                    if(itemResponse ==null || !itemResponse.isSuccess()){
                        throw FlexibleDubboApiException.buildException("调用商品服务异常");
                    }
                    if(itemResponse.getData() ==null || itemResponse.getData().size()==0){
                        throw FlexibleDubboApiException.buildException("查询不到货品信息");
                    }
                    goodsItemRpcDTO = itemResponse.getData().get(0);
                    itemMap.put(materialDO.getItemId(),goodsItemRpcDTO);
                }
                createLabel(templateFile, fileName, labelDO, orderGoodsDiySimpleDTO,  materialDO,pictureDO,goodsItemRpcDTO);
                if (StringUtils.isNotBlank(fileName)) {
                    File file = new File(fileName);
                    //如果是联辉王则需要转成jpg
                    //将文件转成jpg并得到地址
                    String jpgFileStr = PdfConversion.toJpg(file,factoryNo);
                    //删除旧文件
                    file.delete();
                    //得到jpg文件
                    file = new File(jpgFileStr);
                    if (file.exists()) {
                        String filePath = "";
                        for (int i = 0; i < 10; i++) {
                            try {
                                //将文件推送到oos
                                StringBuffer buffer = new StringBuffer();
                                buffer.append(labelId).append("/").append(orderLableCmd.getOrderId()).append("/").append(orderGoodsDiySimpleDTO.getId()).append("/").append(System.currentTimeMillis()).append("/").append(file.getName());
                                InputStream inputStream = new FileInputStream(file);
                                byte[] bytes=null;
                                try {
                                    bytes = IOUtils.toByteArray(inputStream);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_IO_EXCEPTION);
                                }
                                filePath = thirdPartySystemOssServiceRpc.uploadExtendStream(buffer.toString(),"label/",bytes);
                                LOGGER.info("上传回来标签{}",filePath);
                                if (StringUtils.isNotBlank(filePath)) {
                                    break;
                                }
                                //线程暂停5秒后重新上传
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                LOGGER.error("文件上传失败:{}", e.getMessage());
                                if (i >= 9) {
                                    throw new RuntimeException("文件上传失败、图片id"+orderGoodsDiySimpleDTO.getPictureId());
                                }
                            }
                        }
                        orderDiyLabelDTO.setLabelId(labelDO.getId());
                        orderDiyLabelDTO.setLabelUrl(filePath);
                        orderDiyLabelDTO.setOrderId(orderLableCmd.getOrderId());
                        list.add(orderDiyLabelDTO);
                        //将本地文件进行移除
                        file.delete();
                    } else {

                    }
                }
            }
            return list;
        }catch (FlexibleDubboApiException e){
            e.printStackTrace();
            LOGGER.error("标签生成失败:{}",e.getMessage());
            throw FlexibleDubboApiException.buildException(e.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("标签生成失败:{}",e.getMessage());
            throw FlexibleDubboApiException.buildException(LabelErrorCode.LABEL_ORDER_CREATE_FAIL);
        }
    }

    public void createLabel(String inFilePath, String outFilePath, LabelDO labelDO,  OrderGoodsDiySimpleDTO diySimpleDTO,
                            MaterialDO materialDO, PictureDO pictureDO,GoodsItemRpcDTO goodsItemRpcDTO) {
        Document document=null;
        try {
            PdfDocument pdf = new PdfDocument(new PdfReader(inFilePath), new PdfWriter(outFilePath));
            document = new Document(pdf);
            PdfPage firstPage = pdf.getFirstPage();
            Short type = labelDO.getType();
            String modelName = diySimpleDTO.getModelName();

            //GoodsItem goodsItem = goodsItemDBManager.findById(orderGoods.getItemId());
            String itemName = goodsItemRpcDTO.getItemName();
            String itemNameEn = goodsItemRpcDTO.getItemNameEn();
            LOGGER.info("传过来的货品{},英文{}",itemName,itemNameEn,"材质：{}",JSON.toJSONString(materialDO),"图片{}",JSON.toJSONString(pictureDO)+"label{}",JSON.toJSONString(labelDO));
            //1、货品条形码
            String barCode = goodsItemRpcDTO.getBarCode();
            BigDecimal barCodePositionHeight = labelDO.getBarCodePositionHeight();
            BigDecimal barCodePositionWidth = labelDO.getBarCodePositionWidth();

            BigDecimal barCodePositionX = labelDO.getBarCodePositionX();
            BigDecimal barCodePositionY = labelDO.getBarCodePositionY();

            //2、产品名称（中文）
           /* Float nameCodePositionLeft = label.getNameCodePositionLeft();
            Float nameCodePositionTop = label.getNameCodePositionTop();
            Float nameHeight = label.getNameHeight();
            Float nameWidth = label.getNameWidth();
            Integer nameFontSize = label.getNameFontSize();
            Integer nameFontId = label.getNameFont();*/

            BigDecimal productNamePositionX = labelDO.getProductNamePositionX();
            BigDecimal productNamePositionY = labelDO.getProductNamePositionY();
            BigDecimal productNamePositionWidth = labelDO.getProductNamePositionWidth();
            BigDecimal productNamePositionHeight = labelDO.getProductNamePositionHeight();
            Integer productNamePositionFont = labelDO.getProductNamePositionFont();
            Integer productNamePositionFontSize = labelDO.getProductNamePositionFontSize();

            //2、货品+图编名称（英文）
           /* Float enNameCodePositionLeft = label.getEnNamePositionLeft();
            Float enNameCodePositionTop = label.getEnNamePositionTop();
            Float enNameHeight = label.getEnNameHeight();
            Float enNameWidth = label.getEnNameWidth();
            Integer enNameFontSize = label.getEnNameFontSize();
            Integer enNameFontId = label.getEnNameFont();*/
            //产品名称（英文）
            BigDecimal enNamePositionX = labelDO.getEnNamePositionX();
            BigDecimal enNamePositionY = labelDO.getEnNamePositionY();
            Integer enNamePositionFont = labelDO.getEnNamePositionFont();
            Integer enNamePositionFontSize = labelDO.getEnNamePositionFontSize();
            BigDecimal enNamePositionHeight = labelDO.getEnNamePositionHeight();
            BigDecimal enNamePositionWidth = labelDO.getEnNamePositionWidth();

            //3、型号信息
            /*Float modelPositionLeft = label.getModelPositionLeft();
            Float modelPositionTop = label.getModelPositionTop();
            Float modelPositionHeight = label.getModelPositionHeight();
            Float modelPositionWidth = label.getModelPositionWidth();
            Integer modelFontSize = label.getModelFontSize();
            Integer modelFont = label.getNameFont();*/
            //型号内容
            BigDecimal modelPositionX = labelDO.getModelPositionX();
            BigDecimal modelPositionY = labelDO.getModelPositionY();
            BigDecimal modelPositionHeight = labelDO.getModelPositionHeight();
            BigDecimal modelPositionWidth = labelDO.getModelPositionWidth();
            Integer modelPositionFontSize = labelDO.getModelPositionFontSize();
            Integer modelPositionFont = labelDO.getModelPositionFont();

            //4、logo信息
           /* String logoFile = label.getLogoFile();
            Float logoPositionLeft = label.getLogoPositionLeft();
            Float logoPositionTop = label.getLogoPositionTop();
            Float logoWidth = label.getLogoWidth();
            Float logoHeight = label.getLogoHeight();
            if (nameFontId != null) {
                Font nameFontFile = fontRepository.findOne(nameFontId);
            }*/
            //材质信息
           /* Float stuffNamePositionLeft = label.getStuffNamePositionLeft();
            Float stuffNamePositionTop = label.getStuffNamePositionTop();
            Float stuffNameWidth = label.getStuffNameWidth();
            Integer stuffNameFontSize = label.getStuffNameFontSize();*/
            //材质信息（中文）
            BigDecimal stuffNamePositionX = labelDO.getStuffNamePositionX();
            BigDecimal stuffNamePositionY = labelDO.getStuffNamePositionY();
            BigDecimal stuffNamePositionWidth = labelDO.getStuffNamePositionWidth();
            BigDecimal stuffNamePositionHeight = labelDO.getStuffNamePositionHeight();
            Integer stuffNamePositionFont = labelDO.getStuffNamePositionFont();
            Integer stuffNamePositionFontSize = labelDO.getStuffNamePositionFontSize();
            //材质信息（英文）

           /* Float stuffEnNamePositionLeft = label.getStuffEnNamePositionLeft();
            Float StuffEnNamePositionTop = label.getStuffEnNamePositionTop();
            Float stuffEnNameWidth = label.getStuffEnNameWidth();
            Integer stuffEnNameFontSize = label.getStuffEnNameFontSize();*/
            //材质信息（英文）
            BigDecimal stuffEnNamePositionX = labelDO.getStuffEnNamePositionX();
            BigDecimal stuffEnNamePositionY = labelDO.getStuffEnNamePositionY();
            BigDecimal stuffEnNamePositionHeight = labelDO.getStuffEnNamePositionHeight();
            BigDecimal stuffEnNamePositionWidth = labelDO.getStuffEnNamePositionWidth();
            Integer stuffEnNamePositionFont = labelDO.getStuffEnNamePositionFont();
            Integer stuffEnNamePositionFontSize = labelDO.getStuffEnNamePositionFontSize();

            String stuffName=StringUtils.isNotBlank(materialDO.getStuffName())?materialDO.getStuffName():"";
            String stuffEnName=StringUtils.isNotBlank(materialDO.getStuffEnName())?materialDO.getStuffEnName():"";
            LOGGER.info("fontPath:"+fontPath);
            if (LabelConstant.TYPE_FLEXIBEL.equals(type)) {
                //定制標簽
                //產品名稱位置信息
                LOGGER.info("开始处理PAFCanvas");
                PdfCanvas canvas = new PdfCanvas(firstPage, true);
                canvas.beginText();
                //货品名称 + 图片名称
                if(pictureDO!=null){
                    itemName = itemName + pictureDO.getName();
                    if(StringUtils.isNotBlank(pictureDO.getEnglishName())){
                        itemNameEn = itemNameEn+pictureDO.getEnglishName();
                    }
                }
                if (productNamePositionX != null && productNamePositionY != null && StringUtils.isNotBlank(itemName)) {// 产品名称中文
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, productNamePositionFontSize).setTextMatrix(productNamePositionX.floatValue(),
                            productNamePositionY.floatValue()).setLineWidth(productNamePositionWidth.floatValue()).showText(itemName);
                }
                if (enNamePositionX != null && enNamePositionY != null && StringUtils.isNotBlank(itemNameEn)) {// 英文
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font,enNamePositionFontSize).setTextMatrix(enNamePositionX.floatValue(),
                            enNamePositionY.floatValue()).setLineWidth(enNamePositionWidth.floatValue()).showText(itemNameEn);
                }

                if (stuffNamePositionX != null && stuffNamePositionY != null && StringUtils.isNotBlank(stuffName)) {// 材质中文
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, stuffNamePositionFontSize).setTextMatrix(stuffNamePositionX.floatValue(),
                            stuffNamePositionY.floatValue()).setLineWidth(stuffNamePositionWidth.floatValue()).showText(stuffName);
                }
                if (stuffEnNamePositionX != null && stuffEnNamePositionY != null &&stuffEnNamePositionWidth!=null&&stuffEnNamePositionFontSize!=null&& StringUtils.isNotBlank(stuffEnName)) {// 材质英文
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, stuffEnNamePositionFontSize).setTextMatrix(stuffEnNamePositionX.floatValue(),
                            stuffEnNamePositionY.floatValue()).setLineWidth(stuffEnNamePositionWidth.floatValue()).showText(stuffEnName);
                }
                //產品型號位置信息
                if (modelPositionX != null && modelPositionY != null && modelPositionFontSize != null && modelPositionWidth != null&&modelPositionHeight !=null) {
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, modelPositionFontSize).setTextMatrix(modelPositionX.floatValue(),
                            modelPositionY.floatValue()).setLineWidth(modelPositionWidth.floatValue()).showText(modelName);
                } else {
                    LOGGER.info("产品型号位置或字体信息异常,生成标签程序中止");
                    throw FlexibleDubboApiException.buildException("产品型号位置或字体信息异常,生成标签程序中止");
                }
                canvas.endText();
               /*logo已废弃
                if (StringUtils.isNotBlank(logoFile) && logoPositionLeft != null && logoPositionTop != null && logoWidth != null && logoHeight != null) {
                    ImageData imageData = ImageDataFactory.create(URI.create(logoFile).toURL());
                    drawImage(firstPage, logoPositionLeft, logoPositionTop, logoWidth, logoHeight, imageData);
                }*/
            }
            if (StringUtils.isNotBlank(barCode) && barCodePositionHeight != null && barCodePositionWidth != null && barCodePositionX != null && barCodePositionY != null) {
                String property = System.getProperty("user.dir");
                String directoryPath = property.concat("/").concat("images");
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                long timeMillis = System.currentTimeMillis();
                String codeImage = directory.getPath().concat("/").concat("" + timeMillis).concat(".png");
                LOGGER.info("addCode:"+codeImage);
                addCode(firstPage, codeImage, MimeTypes.MIME_PNG, barCode, barCodePositionX.floatValue(), barCodePositionY.floatValue(), barCodePositionWidth.floatValue(),
                        barCodePositionHeight.floatValue());
            } else {
                LOGGER.info("标签条形码位置信息或条形码信息异常,生成标签程序中止");
                throw FlexibleDubboApiException.buildException("标签条形码位置信息或条形码信息异常,生成标签程序中止");
            }
        } catch (Exception e) {
            /*logger.error("生成标签文件操作执行失败: com.bat.b2b.pdf.service.PDFLabelService.createLabel:{}",e.getMessage());
            OrderLog log = new OrderLog();
            log.setOperateType(OrderConstant.OperateTypeGetToken);
            log.setOperateUid(0L);
            log.setOperateName(order.getDistributorName());
            log.setOrderId(order.getId());
            log.setCreateTime(System.currentTimeMillis());
            String remark = String.format("生成标签文件操作执行失败,订单编号: %s , 分销商编号: %s", order.getId(), order.getDistributorId());
            log.setOperateRemark(remark);
            adminDataManager.saveLog(log);*/
        }finally {
            //最后关闭文档
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    LOGGER.info("文档关闭失败:{}",e.getMessage());
                }
            }
        }
    }

    public String generationFilePath(String templateFilePath, Integer distributorId, Integer orderGoodsDiyId) {
        String basicPath = System.getProperty("user.dir");
        String filePath = basicPath.concat("/").concat(folder).concat("/").concat(distributorId.toString()).concat("/").concat(String.valueOf(orderGoodsDiyId));
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(templateFilePath);
        file.isDirectory();
        String path = folderFile.getPath().concat("/").concat(file.getName());
        return path;
    }


    public void addCode(PdfPage page, String fileName, String mine, String content, float left, float bottom, float width, float height) {
        try {
            BarCodeUtils.createEAN13(fileName, mine, content);
            ImageData png = ImageDataFactory.create(fileName);
            Image picture = new Image(png);
            picture.setWidth(width);
            picture.setHeight(height);
            Rectangle rectangle = new Rectangle(0, 0);
            new Canvas(new PdfCanvas(page), rectangle, true).add(picture.setFixedPosition(1, left, bottom));
        } catch (Exception e) {
            LOGGER.error("条形码生成失败！{}",e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("条形码生成失败");
        }

    }



    /**
     * 根据模版生成基础文件
     *
     * @param template OSS上的模版文件
     * @param labelDO    标签属性对象
     * @return 返回生成的模版文件的访问路径地址
     */
    public static String generateBasicLabel(File template, LabelDO labelDO, String outFilePath) {
        try {
            PdfDocument pdf = new PdfDocument(new PdfReader(template), new PdfWriter(outFilePath));
            LOGGER.info("OutFilePath:{}",outFilePath+"pdf{}",pdf);
            Document document = new Document(pdf);
            PdfPage firstPage = pdf.getFirstPage();
            LOGGER.info("firstPage:{}",firstPage);
            //1. 产品名称
            Short type = labelDO.getType();
            Integer productNamePositionFontSize = labelDO.getProductNamePositionFontSize();
            BigDecimal productNamePositionWidth = labelDO.getProductNamePositionWidth();
            BigDecimal productNamePositionX = labelDO.getProductNamePositionX();
            BigDecimal productNamePositionY = labelDO.getProductNamePositionY();
           /* Float nameCodePositionLeft = labelDO.getNameCodePositionLeft();
            Float nameCodePositionTop = labelDO.getNameCodePositionTop();
*/
            //2、产品名称(英文)
           /* Float enNameCodePositionLeft = labelDO.getEnNamePositionLeft();
            Float enNameCodePositionTop = labelDO.getEnNamePositionTop();
            Float enNameWidth = labelDO.getEnNameWidth();
            Integer enNameFontSize = labelDO.getEnNameFontSize();*/
            BigDecimal enNamePositionX = labelDO.getEnNamePositionX();
            BigDecimal enNamePositionY = labelDO.getEnNamePositionY();
            BigDecimal enNamePositionWidth = labelDO.getEnNamePositionWidth();
            Integer enNamePositionFontSize = labelDO.getEnNamePositionFontSize();


            //3、产品型号
         /*   Float modelPositionLeft = labelDO.getModelPositionLeft();
            Float modelPositionTop = labelDO.getModelPositionTop();
            Float modelPositionWidth = labelDO.getModelPositionWidth();
            Float modelPositionHeight = labelDO.getModelPositionHeight();
            Integer modelFontSize = labelDO.getModelFontSize();*/
            BigDecimal modelPositionX = labelDO.getModelPositionX();
            BigDecimal modelPositionY = labelDO.getModelPositionY();
            BigDecimal modelPositionWidth = labelDO.getModelPositionWidth();
            BigDecimal modelPositionHeight = labelDO.getModelPositionHeight();
            Integer modelPositionFontSize = labelDO.getModelPositionFontSize();
            //4. logo
           /* Float logoHeight = labelDO.getLogoHeight();
            Float logoWidth = labelDO.getLogoWidth();
            Float logoPositionLeft = labelDO.getLogoPositionLeft();
            Float logoPositionTop = labelDO.getLogoPositionTop();
            String logoFile = labelDO.getLogoFile();*/


            //5、条形码
          /*  Float barCodeHeight = labelDO.getBarCodeHeight();
            Float barCodeWidth = labelDO.getBarCodeWidth();
            Float barCodePositionLeft = labelDO.getBarCodePositionLeft();
            Float barCodePositionTop = labelDO.getBarCodePositionTop();*/
            BigDecimal barCodePositionHeight = labelDO.getBarCodePositionHeight();
            BigDecimal barCodePositionWidth = labelDO.getBarCodePositionWidth();
            BigDecimal barCodePositionX = labelDO.getBarCodePositionX();
            BigDecimal barCodePositionY = labelDO.getBarCodePositionY();


            //6、材质信息
          /*  Float stuffNamePositionLeft = labelDO.getStuffNamePositionLeft();
            Float stuffNamePositionTop = labelDO.getStuffNamePositionTop();
            Float stuffNameWidth = labelDO.getStuffNameWidth();
            Integer stuffNameFontSize = labelDO.getStuffNameFontSize();
            Float stuffEnNamePositionLeft = labelDO.getStuffEnNamePositionLeft();
            Float StuffEnNamePositionTop = labelDO.getStuffEnNamePositionTop();
            Float stuffEnNameWidth = labelDO.getStuffEnNameWidth();
            Integer stuffEnNameFontSize = labelDO.getStuffEnNameFontSize();*/
            //材质中文
            BigDecimal stuffNamePositionX = labelDO.getStuffNamePositionX();
            BigDecimal stuffNamePositionY = labelDO.getStuffNamePositionY();
            BigDecimal stuffNamePositionWidth = labelDO.getStuffNamePositionWidth();
            Integer stuffNamePositionFontSize = labelDO.getStuffNamePositionFontSize();

            //材质英文
            BigDecimal stuffEnNamePositionX = labelDO.getStuffEnNamePositionX();
            BigDecimal stuffEnNamePositionY = labelDO.getStuffEnNamePositionY();
            BigDecimal stuffEnNamePositionWidth = labelDO.getStuffEnNamePositionWidth();
            Integer stuffEnNamePositionFontSize = labelDO.getStuffEnNamePositionFontSize();

            if (type.equals(LabelConstant.TYPE_FLEXIBEL)) {
                PdfCanvas canvas = new PdfCanvas(firstPage, true);
                LOGGER.info("开始canvas生成标签、字体{},canvas{}",fontPath,canvas);
//                canvas.setFontAndSize(font, nameFontSize);
                canvas.beginText();
                //产品名称
                if (productNamePositionX != null && productNamePositionY != null) {
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, productNamePositionFontSize).setTextMatrix(productNamePositionX.floatValue(), productNamePositionY.floatValue()).setLineWidth(productNamePositionWidth.floatValue()).showText("产品名称位置");
                }
                //产品名称英文
                if (enNamePositionX != null && enNamePositionY != null) {// 英文
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, enNamePositionFontSize).setTextMatrix(enNamePositionX.floatValue(), enNamePositionY.floatValue()).setLineWidth(enNamePositionWidth.floatValue()).showText("产品英文名称位置");
                }

                if (stuffNamePositionX != null && stuffNamePositionY != null) {// 材质中文
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, stuffNamePositionFontSize).setTextMatrix(stuffNamePositionX.floatValue(), stuffNamePositionY.floatValue()).setLineWidth(stuffNamePositionWidth.floatValue()).showText("材质中文名称位置");
                }
                // 材质英文
                if (stuffEnNamePositionX != null && stuffEnNamePositionY != null && stuffEnNamePositionWidth!=null&&stuffEnNamePositionFontSize!=null) {
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, stuffEnNamePositionFontSize).setTextMatrix(stuffEnNamePositionX.floatValue(), stuffEnNamePositionY.floatValue()).setLineWidth(stuffEnNamePositionWidth.floatValue()).showText("材质英文名称位置");
                }

                //產品型號位置信息
                if (modelPositionX != null && modelPositionY != null && modelPositionFontSize != null && modelPositionWidth != null&&modelPositionHeight !=null) {
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
                    canvas.setFontAndSize(font, modelPositionFontSize).setTextMatrix(modelPositionX.floatValue(),modelPositionY.floatValue()).showText("产品型号位置");
                } else {
                    throw new FlexibleCustomException("产品型号位置或字体信息异常,生成标签程序中止");
                }
                canvas.endText();

            }
            if (barCodePositionHeight != null && barCodePositionWidth != null && barCodePositionX != null && barCodePositionY != null) {
                String property = System.getProperty("user.dir");
                LOGGER.info("property:"+property);
                File imageDir = new File(property + "/images/");
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                }
                String image = property + "/images/" + System.currentTimeMillis() + ".png";
                LOGGER.info("image路径："+image);
                BarCodeUtils.addCode(firstPage, image, MimeTypes.MIME_PNG, "820191000242", barCodePositionX.floatValue(), barCodePositionY.floatValue(),
                        barCodePositionWidth.floatValue(), barCodePositionHeight.floatValue());
            } else {
                throw new FlexibleCustomException("条形码位置,生成标签程序中止");
            }
            //最后关闭文档
            document.close();
        } catch (IOException e) {
            throw new RuntimeException("生成预览标签操作失败");
        }
        return outFilePath;
    }

    public static void main(String[] args) {
        Short aa=2;
        System.out.println(aa.equals(LabelConstant.TYPE_FLEXIBEL));
    }
}
