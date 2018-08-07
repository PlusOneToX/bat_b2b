package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.subaccount.DistributorSubAccountRatioServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountLevelRatioDTO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountRatioDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountRatioCmdExe;
import com.bat.distributor.service.common.DistributorCommonConstant;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountRatioQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DistributorSubAccountRatioServiceImpl implements DistributorSubAccountRatioServiceI {

    @Autowired
    private DistributorSubAccountRatioCmdExe distributorSubAccountRatioCmdExe;

    @Autowired
    private DistributorSubAccountRatioQryExe distributorSubAccountRatioQryExe;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(Integer subAccountConfigId, String userId, String userName, List<SubAccountLevelRatioDTO> levelRatioList) {
        BigDecimal hundred = new BigDecimal("100");
        List<DistributorSubAccountRatioDO> ratioDOList = new ArrayList<>();
        levelRatioList.stream().forEach(subAccountLevelRatioDTO -> {
            //
            DistributorSubAccountRatioDO  accountRatioDO = new DistributorSubAccountRatioDO();
            accountRatioDO.setSubAccountConfigId(subAccountConfigId);
            accountRatioDO.setLevelId(subAccountLevelRatioDTO.getLevelId());
            //除以100、保留4位小数
            accountRatioDO.setRatio(subAccountLevelRatioDTO.getRatio().divide(hundred,4,BigDecimal.ROUND_HALF_UP));
            accountRatioDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_NO);
            setAdminMsg(accountRatioDO,userId,userName);
            ratioDOList.add(accountRatioDO);
        });
        distributorSubAccountRatioCmdExe.batchCreate(ratioDOList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Integer subAccountConfigId, String userId, String userName, List<SubAccountLevelRatioDTO> levelRatioList) {
        BigDecimal hundred = new BigDecimal("100");
        //新增
        List<DistributorSubAccountRatioDO> ratioDOList = new ArrayList<>();


        List<DistributorSubAccountRatioDO> accountRatioDOList = distributorSubAccountRatioQryExe.listBySubAccountConfigId(subAccountConfigId);
        Map<Integer, DistributorSubAccountRatioDO> collect = accountRatioDOList.stream().collect(Collectors.toMap(DistributorSubAccountRatioDO::getLevelId,
                distributorSubAccountRatioDO -> distributorSubAccountRatioDO));
        levelRatioList.stream().forEach(subAccountLevelRatioDTO -> {
            //
            DistributorSubAccountRatioDO  accountRatioDO =collect.get(subAccountLevelRatioDTO.getLevelId());
            if(accountRatioDO ==null){
                accountRatioDO = new DistributorSubAccountRatioDO();
            }
            accountRatioDO.setSubAccountConfigId(subAccountConfigId);
            accountRatioDO.setLevelId(subAccountLevelRatioDTO.getLevelId());
            //除以100、保留4位小数
            accountRatioDO.setRatio(subAccountLevelRatioDTO.getRatio().divide(hundred,4,BigDecimal.ROUND_HALF_UP));
            accountRatioDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_NO);
            setAdminMsg(accountRatioDO,userId,userName);
            if(accountRatioDO.getId() ==null){
                ratioDOList.add(accountRatioDO);
            }else{
                //修改

            }
        });
        if(ratioDOList.size()>0){
            distributorSubAccountRatioCmdExe.batchCreate(ratioDOList);
        }
        distributorSubAccountRatioCmdExe.batchUpdate(accountRatioDOList);
    }

    @Override
    public List<SubAccountLevelRatioDTO> listLevelRatioBySubAccountConfigId(Integer subAccountConfigId) {
        return distributorSubAccountRatioQryExe.listLevelRatioBySubAccountConfigId(subAccountConfigId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByLevelId(Integer levelId,Integer userId,String userName) {
        distributorSubAccountRatioCmdExe.deleteByLevelId(levelId,userId,userName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBySubAccountConfigId(Integer subAccountConfigId, Integer userId, String userName) {
        List<DistributorSubAccountRatioDO> ratioDOList = distributorSubAccountRatioQryExe.listBySubAccountConfigId(subAccountConfigId);
        if(ratioDOList ==null || ratioDOList.size() ==0){
            return;
        }
        ratioDOList.stream().forEach(distributorSubAccountRatioDO -> {
            setAdminMsg(distributorSubAccountRatioDO,String.valueOf(userId),userName);
            distributorSubAccountRatioDO.setDeleteFlag(DistributorCommonConstant.COMMON_DELETE_FLAG_YES);
        });
        distributorSubAccountRatioCmdExe.batchUpdate(ratioDOList);
    }

    private void setAdminMsg(DistributorSubAccountRatioDO accountRatioDO, String userId, String userName) {
        if(accountRatioDO.getId() ==null){
            accountRatioDO.setCreateTime(new Date());
            accountRatioDO.setCreateUserId(Integer.parseInt(userId));
            accountRatioDO.setCreateUserName(userName);
        }
        accountRatioDO.setUpdateTime(new Date());
        accountRatioDO.setUpdateUserId(Integer.parseInt(userId));
        accountRatioDO.setUpdateUserName(userName);
    }

    public static void main(String[] args) {
        DistributorSubAccountRatioDO accountRatioDO = new DistributorSubAccountRatioDO();
        accountRatioDO.setId(1);
        List<DistributorSubAccountRatioDO> accountRatioDOList = new ArrayList<>();
        accountRatioDOList.add(accountRatioDO);
        DistributorSubAccountRatioDO accountRatioDO2 = new DistributorSubAccountRatioDO();
        accountRatioDO2.setId(1);
        accountRatioDOList.add(accountRatioDO2);
        List<Integer> collect = accountRatioDOList.stream().map(DistributorSubAccountRatioDO::getId).collect(Collectors.toList());
        System.out.println(collect);
        accountRatioDO.setUpdateUserId(22);
        System.out.println(accountRatioDO.getUpdateUserId().equals(accountRatioDO2.getCreateUserId()));
    }
}
