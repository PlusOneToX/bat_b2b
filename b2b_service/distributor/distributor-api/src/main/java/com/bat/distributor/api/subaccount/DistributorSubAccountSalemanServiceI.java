package com.bat.distributor.api.subaccount;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanCmd;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import com.bat.distributor.api.base.DistributorOpenFlagCmd;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanPageDTO;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanPageQry;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanWechatCmd;

import java.io.InputStream;
import java.util.List;

public interface DistributorSubAccountSalemanServiceI {


    void updateLevelIdNull(Integer levelId, Integer userId, String userName);

    /**
     * 运营后台分页查询
     * @param subAccountSalemanPageQry
     * @return
     */
    PageInfo<SubAccountSalemanPageDTO> pageAdmin(SubAccountSalemanPageQry subAccountSalemanPageQry);

    /**
     * 新增
     * @param subAccountSalemanCmd
     * @param userId
     * @param userName
     */
    void create(SubAccountSalemanCmd subAccountSalemanCmd, String userId, String userName);

    /**
     * 修改
     * @param subAccountSalemanCmd
     * @param userId
     * @param userName
     */
    void update(SubAccountSalemanCmd subAccountSalemanCmd, String userId, String userName);

    void updateOpenFlag(DistributorOpenFlagCmd subAccountSalemanCmd, String userId, String userName);

    /**
     * 分销商业务员绑定微信小程序
     * @param subAccountSalemanWechatCmd
     */
    DistributorSubAccountSalemanDO bindWechat(SubAccountSalemanWechatCmd subAccountSalemanWechatCmd);

    /**
     * 根据分销商ID和状态查询业务员
     * @param distributorId
     * @param openFlag
     * @param sonLevelId 子级分账等级ID
     * @return
     */
    List<DistributorSubAccountSalemanDO> listByCondition(Integer distributorId,String mobile, Short openFlag,Integer sonLevelId);

    /**
     * 获取分销商绑定业务员二维码（小程序）
     * @param distributorId
     * @return
     */
    String getWechatProgramCodeUrl(Integer distributorId);

    /**
     * 获取分销商绑定业务员二维码（H5）
     * @param distributorId
     * @return
     */
    String getWechatH5CodeUrl(Integer distributorId);

    void test();

    /**
     * 业务员excel导入
     * @param inputStream
     * @return
     */
    Response importSaleman(InputStream inputStream,Integer distributorId,String distributorName);

}
