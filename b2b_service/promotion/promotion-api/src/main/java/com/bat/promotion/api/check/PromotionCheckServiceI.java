package com.bat.promotion.api.check;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.check.dto.PromotionCheckListQry;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.check.dto.PromotionCheckCmd;
import com.bat.promotion.api.check.dto.data.PromotionCheckDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckListDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:25
 */
public interface PromotionCheckServiceI {

    /**
     * 活动审批列表
     *
     * @param qry
     * @return
     */
    public PageInfo<PromotionCheckListDTO> listPromotionCheck(PromotionCheckListQry qry, String userId);

    /**
     * 活动审批详情
     *
     * @param qry
     * @return
     */
    public PromotionCheckDTO getPromotionCheck(BaseId qry);

    /**
     * 活动审批
     *
     * @param cmd
     * @param userId
     * @param userName
     */
    public void checkPromotion(PromotionCheckCmd cmd, String userId, String userName);

}
