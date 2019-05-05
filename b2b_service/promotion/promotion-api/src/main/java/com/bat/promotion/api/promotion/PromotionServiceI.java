package com.bat.promotion.api.promotion;

import java.io.InputStream;

import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.api.promotion.dto.data.PromotionDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionListDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.api.promotion.dto.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:25
 */
public interface PromotionServiceI {
    /**
     * 根据搜索条件查找促销活动列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<PromotionListDTO> listPromotion(PromotionListQry qry);

    /**
     * 新增促销活动
     * 
     * @param cmd
     */
    void createPromotion(PromotionCmd cmd, String userId, String userName);

    /**
     * 修改促销活动(草稿状态的促销活动修改)
     * 
     * @param cmd
     */
    void updatePromotion(PromotionCmd cmd, String userId, String userName);

    /**
     * 根据id查询促销活动详情
     * 
     * @param qry
     * @return
     */
    PromotionDTO getPromotion(BaseId qry);

    /**
     * 根据id删除促销活动
     * 
     * @param cmd
     */
    void deletePromotion(BaseId cmd);

    /**
     * 根据ids删除促销活动
     * 
     * @param cmd
     */
    public void deletePromotions(BaseIds cmd);

    /**
     * 根据促销活动id变更状态
     * 
     * @param cmd
     */
    void updatePromotionStatus(PromotionStatusCmd cmd);

    /**
     * 获取活动导入模板URL
     * 
     * @return
     */
    String getTempUrl();

    /**
     * 促销活动导入接口
     *
     * @param inputStream
     */
    void promotionImport(InputStream inputStream, String userId, String userName);

    /**
     * 批量提交促销活动
     * 
     * @param cmd
     */
    void promotionSubmits(PromotionSubmitsCmd cmd, String userId, String userName);

    /**
     * 促销活动导出接口
     * 
     * @param qry
     * @param output
     * @return
     */
    HSSFWorkbook promotionExport(PromotionExportQry qry);

}
