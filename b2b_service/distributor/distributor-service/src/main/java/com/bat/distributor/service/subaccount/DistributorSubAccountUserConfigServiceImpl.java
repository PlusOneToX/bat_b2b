package com.bat.distributor.service.subaccount;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.DistributorSubAccountLevelServiceI;
import com.bat.distributor.api.subaccount.DistributorSubAccountRatioServiceI;
import com.bat.distributor.api.subaccount.DistributorSubAccountUserConfigServiceI;
import com.bat.distributor.api.subaccount.dto.*;
import com.bat.distributor.dao.subaccount.co.SubAccountUserConfigCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountUserConfigCmdExe;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountUserConfigQryExe;
import com.bat.distributor.api.subaccount.dto.*;
import com.bat.distributor.service.common.DistributorCommonConstant;
import com.bat.distributor.service.common.subaccount.SubAccountConstant;
import com.bat.dubboapi.flexible.shop.ShopServiceRpc;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DistributorSubAccountUserConfigServiceImpl implements DistributorSubAccountUserConfigServiceI {

    @Autowired
    private DistributorSubAccountUserConfigCmdExe distributorSubAccountUserConfigCmdExe;

    @Autowired
    private DistributorSubAccountUserConfigQryExe distributorSubAccountUserConfigQryExe;

    @Autowired
    private DistributorSubAccountLevelServiceI distributorSubAccountLevelServiceI;

    @Autowired
    private DistributorSubAccountRatioServiceI distributorSubAccountRatioServiceI;

    @DubboReference(retries = 0,timeout = 7000,check = false)
    private ShopServiceRpc shopServiceRpc;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response create(SubAccountUserConfigCmd subAccountUserConfigCmd, String userId, String userName) {
        distributorSubAccountUserConfigCmdExe.create(subAccountUserConfigCmd,userId,userName);
        return Response.buildSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response update(SubAccountUserUpdateCmd subAccountUserUpdateCmd, String userId, String userName) {
        distributorSubAccountUserConfigCmdExe.update(subAccountUserUpdateCmd,userId,userName);
        return Response.buildSuccess();
    }

    @Override
    public SubAccountUserUpdateCmd detailById(Integer id) {
        DistributorSubAccountUserConfigDO userConfigDO = distributorSubAccountUserConfigQryExe.getById(id);
        SubAccountUserUpdateCmd subAccountUserUpdateCmd = BeanUtils.copy(userConfigDO,SubAccountUserUpdateCmd.class);
        List<SubAccountLevelRatioDTO> levelRatioList = distributorSubAccountRatioServiceI.listLevelRatioBySubAccountConfigId(id);
        subAccountUserUpdateCmd.setLevelRatioList(levelRatioList);
        return subAccountUserUpdateCmd;
    }

    /**
     * 分页查询分销商侧配置
     * @param distributorId
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SubAccountUserConfigPageDTO> page(String distributorId, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<SubAccountUserConfigCO> list = distributorSubAccountUserConfigQryExe.listCOByDistributorId(distributorId);
        PageInfo pageInfo = new PageInfo(list);


        if(list !=null && list.size()>0){
            List<SubAccountUserConfigPageDTO> dtoList = new ArrayList<>();
            for(int x=0;x<list.size();x++){
                SubAccountUserConfigPageDTO subAccountUserConfigPageDTO = BeanUtils.copy(list.get(x),SubAccountUserConfigPageDTO.class);
                subAccountUserConfigPageDTO.setLevelRatioList(BeanUtils.copyList(list.get(x).getLevelRatioList(),SubAccountLevelRatioDTO.class));
                dtoList.add(subAccountUserConfigPageDTO);
            }
            BigDecimal hundred = new BigDecimal("100");
            dtoList.stream().forEach(subAccountUserConfigPageDTO -> {
                //乘以100、给百分比
                subAccountUserConfigPageDTO.getLevelRatioList().forEach(subAccountLevelRatioDTO -> {
                    subAccountLevelRatioDTO.setRatio(subAccountLevelRatioDTO.getRatio().multiply(hundred).setScale(2,BigDecimal.ROUND_HALF_UP));
                });
            });
            pageInfo.setList(dtoList);
        }
        return pageInfo;
    }

    /**
     * 运营后台分页查询
     * @param subAccountUserConfigPageQry
     * @return
     */
    @Override
    public PageInfo<SubAccountUserConfigPageDTO> pageAdmin(SubAccountUserConfigPageQry subAccountUserConfigPageQry) {
        List<Integer> assignUserConfigIdList = null;
        if(subAccountUserConfigPageQry.getSearchType() !=null &&
                SubAccountConstant.USER_CONFIG_PAGE_SEARCH_SHOP.equals(subAccountUserConfigPageQry.getSearchType())
                && StringUtils.isNotBlank(subAccountUserConfigPageQry.getContent())
        ){
            //匹配店铺名称
            com.bat.dubboapi.flexible.common.Response<List<Integer>> response = shopServiceRpc.searchUserConfigIdByShopName(subAccountUserConfigPageQry.getContent());
            if(!response.isSuccess()){
                throw DistributorException.buildException(response.getErrCode(),response.getErrMessage());
            }
            List<Integer> data = response.getData();
            if(data ==null || data.size()==0){
                //没有符合条件的
                return new PageInfo<>();
            }
            assignUserConfigIdList = data;
        }
        PageHelper.startPage(subAccountUserConfigPageQry.getPage(),subAccountUserConfigPageQry.getSize());
        List<SubAccountUserConfigCO> list = distributorSubAccountUserConfigQryExe.listCOByCondition(subAccountUserConfigPageQry.getSearchType(),
                subAccountUserConfigPageQry.getContent(),assignUserConfigIdList);
        PageInfo pageInfo = new PageInfo(list);

        if(list!=null && list.size()>0){
            BigDecimal hundred = new BigDecimal("100");
            List<SubAccountUserConfigPageDTO> dtoList = new ArrayList<>();
            for(int x=0;x<list.size();x++){
                SubAccountUserConfigPageDTO subAccountUserConfigPageDTO = BeanUtils.copy(list.get(x),SubAccountUserConfigPageDTO.class);
                subAccountUserConfigPageDTO.setLevelRatioList(BeanUtils.copyList(list.get(x).getLevelRatioList(),SubAccountLevelRatioDTO.class));
                dtoList.add(subAccountUserConfigPageDTO);
            }
            dtoList.stream().forEach(subAccountUserConfigPageDTO -> {
                //乘以100、给百分比
                subAccountUserConfigPageDTO.getLevelRatioList().forEach(subAccountLevelRatioDTO -> {
                    subAccountLevelRatioDTO.setRatio(subAccountLevelRatioDTO.getRatio().multiply(hundred).setScale(2,BigDecimal.ROUND_HALF_UP));
                });
            });
            pageInfo.setList(dtoList);
        }
        return pageInfo;
    }

    /**
     * 条件查询
     * @param distributorId
     * @return
     */
    @Override
    public List<DistributorSubAccountUserConfigDO> listByCondition(Integer distributorId) {
        return distributorSubAccountUserConfigQryExe.listByCondition(distributorId);
    }

    @Override
    public void delete(Integer id, Integer userId, String userName) {
        DistributorSubAccountUserConfigDO userConfigDO = distributorSubAccountUserConfigQryExe.getById(id);
        userConfigDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_YES);
        userConfigDO.setUpdateUserId(userId);
        userConfigDO.setUpdateUserName(userName);
        userConfigDO.setUpdateTime(new Date());
        distributorSubAccountUserConfigCmdExe.updateDO(userConfigDO);
        //处理比例
        distributorSubAccountRatioServiceI.deleteBySubAccountConfigId(id,userId,userName);
        //通知门店处理
        shopServiceRpc.restoreUserConfigIdNull(id);

    }
}
