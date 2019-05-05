package com.bat.promotion.api.rebatevoucher;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherCmd;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherListQry;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherUsageRecordListQry;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherUsageRecordDTO;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:37
 */
public interface RebateVoucherService {
    /**
     * 新增返利代金券
     * 
     * @param cmd
     * @param userId
     * @param userName
     */
    void createRebateVoucher(RebateVoucherCmd cmd, String userId, String userName);

    /**
     * 批量新增返利代金券
     * 
     * @param cmds
     * @param userId
     * @param userName
     */
    void batchCreateRebateVoucher(List<RebateVoucherCmd> cmds, String userId, String userName);

    /**
     * 根据搜索条件查找返利代金券列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<RebateVoucherDTO> listRebateVoucher(RebateVoucherListQry qry);

    /**
     * 更新代金券
     * 
     * @param cmd
     */
    void updateRebateVoucher(RebateVoucherCmd cmd);

    /**
     * 代金券使用记录列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<RebateVoucherUsageRecordDTO> listRebateVoucherUsageRecord(RebateVoucherUsageRecordListQry qry);

    /**
     * 查询分销商可用代金券总额
     * 
     * @param distributorId
     * @return
     */
    BigDecimal getAvailableRebateVoucherBalanceSum(Integer distributorId);

    /**
     * 获取代金券导入模板URL
     * 
     * @return
     */
    String getTempUrl();

    /**
     * 导入返利代金券
     * 
     * @param inputStream
     * @param userId
     * @param userName
     */
    void importRebateVoucher(InputStream inputStream, String userId, String userName);
}
