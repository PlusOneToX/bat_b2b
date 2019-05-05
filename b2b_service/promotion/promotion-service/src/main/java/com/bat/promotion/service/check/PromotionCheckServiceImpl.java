package com.bat.promotion.service.check;

import javax.annotation.Resource;

import com.bat.promotion.service.check.executor.PromotionCheckCmdExe;
import com.bat.promotion.service.check.executor.PromotionCheckQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.check.PromotionCheckServiceI;
import com.bat.promotion.api.check.dto.PromotionCheckCmd;
import com.bat.promotion.api.check.dto.PromotionCheckListQry;
import com.bat.promotion.api.check.dto.data.PromotionCheckDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckListDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/9/14 14:18
 */
@Service
public class PromotionCheckServiceImpl implements PromotionCheckServiceI {

    @Resource
    PromotionCheckCmdExe checkCmdExe;
    @Resource
    PromotionCheckQryExe checkQryExe;

    @Override
    public PageInfo<PromotionCheckListDTO> listPromotionCheck(PromotionCheckListQry qry, String userId) {
        return checkQryExe.listPromotionCheck(qry, userId);
    }

    @Override
    public PromotionCheckDTO getPromotionCheck(BaseId qry) {
        return checkQryExe.getPromotionCheck(qry);
    }

    @Override
    public void checkPromotion(PromotionCheckCmd cmd, String userId, String userName) {
        checkCmdExe.checkPromotion(cmd, userId, userName);
    }
}
