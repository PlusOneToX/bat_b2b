package com.bat.promotion.service.promotion;

import java.io.InputStream;

import javax.annotation.Resource;

import com.bat.promotion.api.promotion.dto.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.api.promotion.PromotionServiceI;
import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.api.promotion.dto.data.PromotionDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionListDTO;
import com.bat.promotion.service.promotion.executor.PromotionCmdExe;
import com.bat.promotion.service.promotion.executor.PromotionQryExe;

/**
 * 促销活动service实现
 * 
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:10
 */
@Service
public class PromotionServiceImpl implements PromotionServiceI {

    @Resource
    private PromotionCmdExe cmdExe;

    @Resource
    private PromotionQryExe qryExe;

    @Override
    public PageInfo<PromotionListDTO> listPromotion(PromotionListQry qry) {
        return qryExe.listPromotion(qry);
    }

    @Override
    public void createPromotion(PromotionCmd cmd, String userId, String userName) {
        cmdExe.createPromotion(cmd, userId, userName);
    }

    @Override
    public void updatePromotion(PromotionCmd cmd, String userId, String userName) {
        cmdExe.updatePromotion(cmd, userId, userName);
    }

    @Override
    public PromotionDTO getPromotion(BaseId qry) {
        return qryExe.getPromotion(qry);
    }

    @Override
    public void deletePromotion(BaseId cmd) {
        cmdExe.deletePromotion(cmd.getId());
    }

    @Override
    public void deletePromotions(BaseIds cmd) {
        cmdExe.deletePromotions(cmd.getIds());
    }

    @Override
    public void updatePromotionStatus(PromotionStatusCmd cmd) {
        cmdExe.updatePromotionStatus(cmd);
    }

    @Override
    public String getTempUrl() {
        return qryExe.getTempUrl();
    }

    @Override
    public void promotionImport(InputStream inputStream, String userId, String userName) {
        cmdExe.promotionImport(inputStream, userId, userName);
    }

    @Override
    public void promotionSubmits(PromotionSubmitsCmd cmd, String userId, String userName) {
        cmdExe.promotionSubmits(cmd, userId, userName);
    }

    @Override
    public HSSFWorkbook promotionExport(PromotionExportQry qry) {
        HSSFWorkbook sheets = qryExe.promotionExport(qry);
        return sheets;
    }
}
