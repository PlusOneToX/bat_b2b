package com.bat.order.service.order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.constans.OrderExcelFieldEnum;
import com.bat.order.service.order.convertor.OrderCostConvertor;
import com.bat.order.service.order.convertor.OrderDeliveryConvertor;
import com.bat.order.service.order.convertor.OrderGoodsDiyConvertor;
import com.bat.order.service.order.convertor.OrderInfoConvertor;
import com.bat.order.service.order.executor.OrderCmdExe;
import com.bat.order.service.order.executor.OrderInfoQryExe;
import com.bat.order.service.order.executor.distributor.OrderInfoCmdExe;
import com.bat.order.service.order.validator.OrderInfoValidator;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderRequest;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.cost.OrderCustomerCostServiceI;
import com.bat.order.api.cost.OrderDistributorCostServiceI;
import com.bat.order.api.cost.OrderGoodsCustomerCostServiceI;
import com.bat.order.api.cost.OrderGoodsDistributorCostServiceI;
import com.bat.order.api.deliver.OrderDeliverBillDetailServiceI;
import com.bat.order.api.deliver.OrderDeliveryServiceI;
import com.bat.order.api.order.OrderGoodsDiyServiceI;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderInfoParamDTO;
import com.bat.order.api.order.dto.common.OrderPromotionDTO;
import com.bat.order.api.order.dto.distributor.OrderPromotionQry;
import com.bat.order.api.order.dto.diy.OrderDiyCmd;
import com.bat.order.api.order.dto.export.OrderInfoExportQry;
import com.bat.order.api.order.dto.goods.OrderGoodsSerialNumberDTO;
import com.bat.order.api.order.dto.orderquery.admin.*;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.co.OrderExcelCO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.local.executor.OrderDetailPictureLocalCmdExe;

@Service
public class OrderInfoServiceImpl implements OrderInfoServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    @Autowired
    private OrderInfoCmdExe orderInfoCmdExe;

    @Autowired
    private OrderInfoQryExe orderInfoQryExe;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Autowired
    private OrderGoodsDiyServiceI orderGoodsDiyServiceI;

    @Autowired
    private OrderDeliveryServiceI orderDeliveryServiceI;

    @Autowired
    private OrderCustomerCostServiceI orderCustomerCostServiceI;

    @Autowired
    private OrderDistributorCostServiceI orderDistributorCostServiceI;

    @Autowired
    private OrderGoodsDistributorCostServiceI orderGoodsDistributorCostServiceI;

    @Autowired
    private OrderGoodsCustomerCostServiceI orderGoodsCustomerCostServiceI;

    @Resource
    private OrderCmdExe orderCmdExe;

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @Autowired
    private OrderDeliverBillDetailServiceI orderDeliverBillDetailServiceI;

    @Autowired
    private OrderInfoConvertor orderInfoConvertor;

    @Resource
    private MessageSendService sendService;

    /**
     * 定制下单
     * 
     * @param orderDiyCmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response createDiyOrder(OrderDiyCmd orderDiyCmd) {
        LOGGER.info("柔性下单、参数：" + JSON.toJSONString(orderDiyCmd));
        // 校验订单参数
        OrderInfoParamDTO orderInfoParamDTO = OrderInfoValidator.validDiyOrder(orderDiyCmd);
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_UN_SHIPPED);
        orderInfoDO.setOrderTypeId(orderDiyCmd.getOrderTypeId());
        orderInfoDO.setInvoiceFlag(orderDiyCmd.getInvoiceFlag());
        orderInfoDO.setOrderSource(orderDiyCmd.getOrderSource());
        orderInfoDO.setCreateTime(new Date());
        orderInfoDO.setUpdateTime(new Date());
        orderInfoDO.setSalesId(1);
        orderInfoDO.setSalesName("ceshi");
        orderInfoDO.setCoordinatorId(2);
        orderInfoDO.setCoordinatorName("商务");
        orderInfoDO.setOrderNo(String.valueOf(System.currentTimeMillis()));
        orderInfoDO.setStockType(OrderInfoConstant.ORDER_STOCK_TYPE_OUTSOURCE);
        orderInfoCmdExe.create(orderInfoDO);
        // 新增订单商品详情
        orderGoodsServiceI.createList(orderInfoParamDTO.getOrderGoodsDOList(), orderInfoDO.getId());
        // 新增定制详情记录
        List<OrderGoodsDO> orderGoodsDOList = orderInfoParamDTO.getOrderGoodsDOList();
        OrderGoodsDiyConvertor.setOrderIdAndOrderGoodsId(orderInfoParamDTO.getDiyDOList(),
            orderInfoParamDTO.getOrderGoodsDOList());
        // 给订单明细归属客户列表或者订单明细归属分销商列表设置orderGoodsId
        OrderCostConvertor.setOrderGoodsIdByCost(orderInfoParamDTO);
        orderGoodsDiyServiceI.createList(orderInfoParamDTO.getDiyDOList());
        // 新增发货单
        OrderDeliveryDO orderDeliveryDO = OrderDeliveryConvertor.toOrderDeliveryDO(orderDiyCmd.getOrderAddress(),
            orderInfoDO.getId(), orderDiyCmd.getDistributionId(), "测试");
        orderDeliveryServiceI.create(orderDeliveryDO);

        // 保存金额
        if (orderDiyCmd.getCustomerId() != null) {
            // 处理客户的金额
            OrderCustomerCostDO orderCustomerCostDO = orderInfoParamDTO.getOrderCustomerCostDO();
            orderCustomerCostDO.setOrderId(orderInfoDO.getId());
            orderCustomerCostServiceI.create(orderCustomerCostDO);
            orderGoodsCustomerCostServiceI.createList(orderInfoParamDTO.getOrderGoodsCustomerCostDOList());
        } else {
            // 处理分销商的金额
            OrderDistributorCostDO orderDistributorCostDO = orderInfoParamDTO.getOrderDistributorCostDO();
            orderDistributorCostDO.setOrderId(orderInfoDO.getId());
            orderDistributorCostServiceI.create(orderDistributorCostDO);
            orderGoodsDistributorCostServiceI.createList(orderInfoParamDTO.getOrderGoodsDistributorCostDOList());
        }
        // 生成data数据
        OrderDistributorDataDO orderDistributorDataDO = null;
        return Response.of(orderInfoDO.getId());
    }

    @Override
    public OrderInfoDO getById(Integer orderId) {
        return orderInfoQryExe.getById(orderId);
    }

    @Override
    public void update(OrderInfoDO orderInfoDO) {
        orderInfoDO.setUpdateTime(new Date());
        orderInfoCmdExe.update(orderInfoDO);
    }

    /**
     * 订单列表
     * 
     * @param qry
     * @return
     */
    @Override
    public PageInfo<AdminOrderInfoListDTO> listDistributorOrderInfo(AdminDistributorOrderInfoListQry qry) {
        return orderInfoQryExe.listDistributorOrderInfo(qry);
    }

    /**
     * 柔性定制订单列表
     * 
     * @param qry
     * @return
     */
    @Override
    public PageInfo<AdminOrderInfoListDTO> listCustomerDiyOrderInfo(AdminCustomerOrderInfoListQry qry) {
        return orderInfoQryExe.listCustomerDiyOrderInfo(qry);
    }

    /**
     * 异常订单列表
     * 
     * @param qry
     * @return
     */
    @Override
    public PageInfo<AdminOrderInfoListDTO> listExceptionOrderInfo(AdminExceptionOrderInfoListQry qry) {
        return orderInfoQryExe.listExceptionOrderInfo(qry);
    }

    /**
     * 分销订单详情
     * 
     * @param qry
     * @return
     */
    @Override
    public AdminDistributorOrderInfoDetailDTO getDistributorOrderDetail(AdminOrderDetailQry qry) {
        return orderInfoQryExe.getDistributorOrderDetail(qry);
    }

    /**
     * 柔性订单详情
     * 
     * @param qry
     * @return
     */
    @Override
    public AdminCustomerOrderInfoDetailDTO getCustomerOrderDetail(AdminOrderDetailQry qry) {
        return orderInfoQryExe.getCustomerOrderDetail(qry);
    }

    @Override
    public List<OrderInfoDO> listByIds(List<Integer> orderIds) {
        return orderInfoQryExe.listByIds(orderIds);
    }

    @Override
    public void orderCancel(OrderCancelCmd cmd, String userId, String userName) {
        orderCmdExe.cancelOrderByOrderId(cmd.getId(), cmd.getRemark(), userId, userName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdate(List<OrderInfoDO> orderInfoDOList) {
        orderInfoCmdExe.batchUpdate(orderInfoDOList);
    }

    @Override
    public OrderPromotionDTO orderPromotion(OrderPromotionQry qry) {
        return orderInfoCmdExe.orderPromotion(qry);
    }

    @Override
    public List<Integer> listUnSyncFactory(String manufactors) {
        // 根据工厂单号查询未同步的工厂订单
        return orderGoodsDiyServiceI.listUnSyncFactory(manufactors);
    }

    @Override
    public String getOrderIdByOrderNo(Integer id) {
        return orderInfoQryExe.getOrderIdByOrderNo(id);
    }

    @Autowired
    private OrderDetailPictureLocalCmdExe orderDetailPictureLocalCmdExe;

    @Transactional
    @Override
    public Response setOrderGoodsSerialNumber(InputStream inputStream) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception ex) {
            throw OrderException.buildException("导入ERP订单信息异常");
        }
        List<OrderGoodsSerialNumberDTO> excelList = new ArrayList<>();
        for (int numSheet = 0; numSheet < 1; numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            String sheetName = hssfSheet.getSheetName();
            int lastRowNum = hssfSheet.getLastRowNum() - hssfSheet.getFirstRowNum() + 1;// 获取所有的行项目

            for (int rowNum = 1; rowNum < lastRowNum; rowNum++) {
                Row xssfRow = hssfSheet.getRow(rowNum);
                Cell secortCell = xssfRow.getCell(1);
                String orderNo = secortCell.getStringCellValue();
                Cell cell1 = xssfRow.getCell(3);
                String itemCode = cell1.getStringCellValue();
                Cell cell = xssfRow.getCell(4);
                String entryId = cell.getStringCellValue();
                OrderGoodsSerialNumberDTO orderGoodsSerialNumberDTO = new OrderGoodsSerialNumberDTO();
                orderGoodsSerialNumberDTO.setOrderNo(orderNo);
                orderGoodsSerialNumberDTO.setItemCode(itemCode);
                orderGoodsSerialNumberDTO.setSerialNumber(Integer.parseInt(entryId));
                excelList.add(orderGoodsSerialNumberDTO);
            }
        }
        // 转为key为订单id、value为列表的map
        Map<String,
            List<OrderGoodsSerialNumberDTO>> orderMap = excelList.stream()
                .collect(Collectors.toMap(OrderGoodsSerialNumberDTO::getOrderNo,
                    orderGoodsSerialNumberDTO -> Lists.newArrayList(orderGoodsSerialNumberDTO),
                    (List<OrderGoodsSerialNumberDTO> oldList, List<OrderGoodsSerialNumberDTO> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        Iterator<Map.Entry<String, List<OrderGoodsSerialNumberDTO>>> iterator = orderMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<OrderGoodsSerialNumberDTO>> entry = iterator.next();
            String orderNo = entry.getKey();
            // ERP返回的明细内码列表
            List<OrderGoodsSerialNumberDTO> excelItemList = entry.getValue();
            OrderInfoDO orderInfoDO = orderInfoQryExe.getByOrderNo(orderNo);
            if (orderInfoDO == null) {
                LOGGER.info("订单号找不到B2B系统订单" + orderNo);
            } else {
                LOGGER.info("开始处理订单号{}的行序号", orderNo);
                List<OrderGoodsDO> orderGoodsDOList = orderGoodsServiceI.listByOrderId(orderInfoDO.getId());
                if (orderGoodsDOList == null || orderGoodsDOList.size() == 0) {
                    LOGGER.info("订单id{}匹配不上B2B系统订单详情", orderNo);
                } else {
                    // 找出一个订单理多个相同的
                    Map<String,
                        List<OrderGoodsDO>> itemCodeMap = orderGoodsDOList.stream()
                            .collect(Collectors.toMap(OrderGoodsDO::getItemCode,
                                orderGoodsDO -> Lists.newArrayList(orderGoodsDO),
                                (List<OrderGoodsDO> oldList, List<OrderGoodsDO> newList) -> {
                                    oldList.addAll(newList);
                                    return oldList;
                                }));
                    // Excel传过来的
                    Map<String,
                        List<OrderGoodsSerialNumberDTO>> excelItemCodeMap = excelItemList.stream()
                            .collect(Collectors.toMap(OrderGoodsSerialNumberDTO::getItemCode,
                                orderGoodsSerialNumberDTO -> Lists.newArrayList(orderGoodsSerialNumberDTO),
                                (List<OrderGoodsSerialNumberDTO> oldList, List<OrderGoodsSerialNumberDTO> newList) -> {
                                    oldList.addAll(newList);
                                    return oldList;
                                }));
                    for (int x = 0; x < orderGoodsDOList.size(); x++) {
                        OrderGoodsDO orderGoodsDO = orderGoodsDOList.get(x);
                        List<OrderGoodsDO> goodsDOList = itemCodeMap.get(orderGoodsDO.getItemCode());
                        if (goodsDOList == null || goodsDOList.size() > 1) {
                            // 超过一条或者找不到、不处理
                            continue;
                        }
                        List<OrderGoodsSerialNumberDTO> excelOrderGoodsList =
                            excelItemCodeMap.get(orderGoodsDO.getItemCode());
                        if (excelOrderGoodsList == null || excelOrderGoodsList.size() > 1) {
                            // 超过一条或者找不到、不处理
                            continue;
                        }
                        if (orderGoodsDO.getSerialNumber() != null && orderGoodsDO.getSerialNumber() > 10000) {
                            //
                            continue;
                        }
                        orderGoodsDO.setSerialNumber(excelOrderGoodsList.get(0).getSerialNumber());
                        orderGoodsServiceI.update(orderGoodsDO);
                        List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOList =
                            orderDeliverBillDetailServiceI.listByOrderGoodsId(orderGoodsDO.getId());
                        //
                        if (orderDeliverBillDetailDOList != null && orderDeliverBillDetailDOList.size() > 0) {
                            orderDeliverBillDetailDOList.stream().forEach(orderDeliverBillDetailDO -> {
                                orderDeliverBillDetailDO.setSerialNumber(goodsDOList.get(0).getSerialNumber());
                                orderDeliverBillDetailServiceI.update(orderDeliverBillDetailDO);
                            });

                        }
                    }
                }
            }

        }
        return Response.of(excelList);
    }

    @Override
    public HSSFWorkbook createExcelByCondition(OrderInfoExportQry orderInfoExportQry, String currentLanguage) {
        LOGGER.info("订单查询条件{}、语言{}", JSON.toJSONString(orderInfoExportQry), currentLanguage);
        List<String> fieldList = orderInfoExportQry.getFieldList();
        if (fieldList == null || fieldList.size() == 0) {
            OrderExcelFieldEnum[] fieldEnums = OrderExcelFieldEnum.values();
            if (fieldList == null) {
                fieldList = new ArrayList<>();
            }
            for (int x = 0; x < fieldEnums.length; x++) {
                fieldList.add(String.valueOf(fieldEnums[x]));
            }
            orderInfoExportQry.setFieldList(fieldList);
        }
        String sql = OrderInfoConvertor.querSql(orderInfoExportQry);
        LOGGER.info("订单导出查询sql:" + sql);
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<OrderExcelCO> orderExcelCOList = orderInfoQryExe.listCOByCondition(sql,
            orderInfoExportQry.getActivityFlag(), orderInfoExportQry.getDistributorId(),
            orderInfoExportQry.getOrderSourceList(), orderInfoExportQry.getOrderStatusList(),
            orderInfoExportQry.getStartTime(), orderInfoExportQry.getEndTime(), orderInfoExportQry.getItemCode(),
            orderInfoExportQry.getOrderTypeIdList(), orderInfoExportQry.getPayStatusList(),
            orderInfoExportQry.getDeliverStatus());
        if (orderExcelCOList == null || orderExcelCOList.size() == 0) {
            throw OrderException.buildException("无符合条件");
        }

        // 如果需要导出零售价就追加零售价
        if (fieldList.contains("RETAIL_PRICE")) {
            orderExcelCOList.forEach(x -> {
                x.setRetailPrice(goodsServiceRpc.queryRetailPriceByItemId(x.getItemId()).getData());
                x.setItemId(null);
            });
        }

        // 计算订单包含的货品种类个数（合并单元格）
        OrderExcelFieldEnum.calculateItemTypeCount(orderExcelCOList);
        // 设置部门名称
        orderInfoConvertor.setDepartmentName(fieldList, orderExcelCOList);
        // 设置订单状态(中文转义)
        orderInfoConvertor.setOrderStatusName(fieldList, orderExcelCOList);
        // 设置付款状态(中文转义)
        orderInfoConvertor.setPayStatusName(fieldList, orderExcelCOList);
        // 设置订单发货状态(中文转义)
        orderInfoConvertor.setDeliverStatusName(fieldList, orderExcelCOList);
        // 设置支付方式(中文转义)
        orderInfoConvertor.setPayWayName(fieldList, orderExcelCOList);
        // 设置订单类型
        orderInfoConvertor.setOrderTypeName(fieldList, orderExcelCOList);
        // 设置ERP分销商ID
        // orderInfoConvertor.setDistributorErpId(fieldList,orderExcelCOList,orderInfoExportQry.getDistributorId());
        // 按照65536数量来切割成多个列表
        List<List<OrderExcelCO>> groupList = new ArrayList<>();
        List<OrderExcelCO> sonList = null;
        for (int x = 0; x < orderExcelCOList.size(); x++) {
            // 前面2行留空
            if (x % (65536 - 1) == 0) {
                if (x > 0) {
                    groupList.add(sonList);
                }
                sonList = new ArrayList<>();
            }
            sonList.add(orderExcelCOList.get(x));
        }
        if (sonList != null && sonList.size() > 0) {
            groupList.add(sonList);
        }
        Long index = 0L;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int z = 0; z < groupList.size(); z++) {
            List<OrderExcelCO> list = groupList.get(z);
            HSSFSheet hssfSheet = workbook.createSheet("订单导出表格第" + (z + 1) + "个");
            HSSFFont hssfFont = workbook.createFont();
            hssfFont.setColor(IndexedColors.BLACK.getIndex());
            // 设置字体
            hssfFont.setFontName("宋体");
            // 设置字号
            hssfFont.setFontHeightInPoints((short)10);
            // 设置单元格内容水平垂直居中
            HSSFCellStyle titleStyle = workbook.createCellStyle();
            // titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 4.设置单元格背景色
            // titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充单元格
            titleStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());// 设置单元格背景色
            titleStyle.setFont(hssfFont);

            HSSFRow row2 = hssfSheet.createRow(0);
            row2.setHeightInPoints(25);
            /*HSSFCell hssfCell = row2.createCell(0);
            hssfCell.setCellValue(("zh").equals(currentLanguage)? ExchangeExcelConstant.SerialNumberZH:ExchangeExcelConstant.SerialNumberEN);
            hssfCell.setCellStyle(titleStyle);*/
            HSSFCell hssfCell = null;
            hssfCell = row2.createCell(0);
            hssfCell.setCellValue("序号");
            hssfCell.setCellStyle(titleStyle);
            for (int y = 0; y < fieldList.size(); y++) {
                OrderExcelFieldEnum fieldEnum = OrderExcelFieldEnum.valueOf(fieldList.get(y));
                hssfCell = row2.createCell(y + 1);
                hssfCell.setCellValue(("zh").equals(currentLanguage) ? fieldEnum.getZh() : fieldEnum.getEn());
                hssfCell.setCellStyle(titleStyle);
            }
            // 当前订单id（用来判断是否合并单元格）
            Integer thisOrderId = 0;
            for (int x = 0; x < list.size(); x++) {

                OrderExcelCO orderExcelCO = list.get(x);

                HSSFRow row = hssfSheet.createRow(x + 1);
                for (int i = 0; i < fieldList.size(); i++) {
                    hssfCell = row.createCell(i);
                    hssfCell.setCellStyle(titleStyle);
                }
                // 序号
                if (orderExcelCO.getOrderId() - thisOrderId != 0) {
                    index++;
                    row.createCell(0).setCellValue(index);
                    if (orderExcelCO.getOrderLength() > 1) {
                        // 合并单元格
                        CellRangeAddress region = new CellRangeAddress(x + 1, x + orderExcelCO.getOrderLength(), 0, 0);
                        hssfSheet.addMergedRegion(region);
                    }
                }
                for (int k = 0; k < fieldList.size(); k++) {
                    OrderExcelFieldEnum orderExcelFieldEnum = OrderExcelFieldEnum.valueOf(fieldList.get(k));
                    if (orderExcelFieldEnum == null) {
                        continue;
                    }
                    Object value = OrderExcelFieldEnum.getValueByField(orderExcelFieldEnum, orderExcelCO);
                    if (fieldList.get(k).equals("CREATE_TIME")) {
                        // 创建时间
                        if (value == null) {
                            continue;
                        }
                        Date createTime = (Date)value;
                        String format = simpleDateFormat.format(createTime);
                        row.createCell(k + 1).setCellValue(format);
                    } else {
                        row.createCell(k + 1).setCellValue(value == null ? "" : String.valueOf(value));
                    }
                    // 判断是否合并
                    if (orderExcelFieldEnum.getMergeFlag() && orderExcelCO.getOrderId() - thisOrderId != 0
                        && orderExcelCO.getOrderLength() > 1) {
                        // 合并单元格
                        CellRangeAddress region =
                            new CellRangeAddress(x + 1, x + orderExcelCO.getOrderLength(), k + 1, k + 1);

                        hssfSheet.addMergedRegion(region);
                    }
                }
                thisOrderId = orderExcelCO.getOrderId();
            }
            // workbook.setSheetName(0,"兑换码导出");
        }
        return workbook;
    }

    @Autowired
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @Override
    public void test() {
        /* List<String> list = new ArrayList<>();
        list.add("11222");
        List<AutoReleaseLock> autoReleaseLockList = orderRedisKeyCmdExe.lockExchangeCodeList(list);
        OrderRedisKeyCmdExe.releaseLock(autoReleaseLockList);*/
        String str =
            "{\"checkTime\":0,\"createTime\":1631167419460,\"deliverOrderDetails\":[{\"entryId\":4511337,\"itemNo\":\"810507000008\",\"no\":\"1001\",\"num\":10,\"orderEntryId\":983057,\"orderNo\":\"SO1002109188452\"},{\"entryId\":4511338,\"itemNo\":\"810507000007\",\"no\":\"1001\",\"num\":2,\"orderEntryId\":983058,\"orderNo\":\"SO1002109188452\"}],\"deliverOrderNo\":\"XSCKD1002109000054\",\"documentStatus\":1}";
        ErpDeliverOrderRequest erpDeliverOrderRequest = JSONObject.parseObject(str, ErpDeliverOrderRequest.class);
        orderDeliveryServiceRpc.syncOutboundOrderFromERP(erpDeliverOrderRequest);
    }

    @Override
    public AdminJudgeOrderDTO judgeDistributorOrderStatus(AdminJudgeOrderQry qry) {
        return orderInfoQryExe.judgeDistributorOrderStatus(qry);
    }

    public static void main(String[] args) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFSheet sheet = workbook.createSheet("sheet");
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell_00 = row0.createCell(0);
        cell_00.setCellStyle(style);
        cell_00.setCellValue("日期");
        HSSFCell cell_01 = row0.createCell(1);
        cell_01.setCellStyle(style);
        cell_01.setCellValue("午别");

        HSSFRow row1 = sheet.createRow(1);
        HSSFCell cell_10 = row1.createCell(0);
        cell_10.setCellStyle(style);
        cell_10.setCellValue("20180412");
        HSSFCell cell_11 = row1.createCell(1);
        cell_11.setCellStyle(style);
        cell_11.setCellValue("上午");

        HSSFRow row2 = sheet.createRow(2);
        HSSFCell cell_21 = row2.createCell(1);
        cell_21.setCellStyle(style);
        cell_21.setCellValue("下午");

        HSSFCell cell_13 = row2.createCell(0);
        cell_13.setCellStyle(style);
        cell_13.setCellValue("20180412");

        // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
        // 行和列都是从0开始计数，且起始结束都会合并
        // 这里是合并excel中日期的两行为一行
        CellRangeAddress region = new CellRangeAddress(1, 2, 0, 0);
        sheet.addMergedRegion(region);
        CellRangeAddress region2 = new CellRangeAddress(1, 2, 0, 0);
        sheet.addMergedRegion(region2);
        File file = new File("d:\\demo.xls");
        FileOutputStream fout = new FileOutputStream(file);
        workbook.write(fout);
        fout.close();
    }
}
