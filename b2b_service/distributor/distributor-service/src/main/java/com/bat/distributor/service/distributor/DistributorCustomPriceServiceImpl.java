package com.bat.distributor.service.distributor;

import com.alibaba.fastjson.JSON;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.distributor.DistributorCustomPriceServiceI;
import com.bat.distributor.api.distributor.dto.DistributorCustomPriceCmd;
import com.bat.distributor.api.distributor.dto.DistributorCustomPriceListCmd;
import com.bat.distributor.api.distributor.dto.data.DistributorCustomPriceDTO;
import com.bat.distributor.dao.distributor.dataobject.DistributorCustomPriceDO;
import com.bat.distributor.service.distributor.convertor.DistributorCustomPriceConvertor;
import com.bat.distributor.service.distributor.executor.DistributorCustomPriceCmdExe;
import com.bat.distributor.service.distributor.executor.DistributorCustomPriceQryExe;
import com.bat.distributor.service.distributor.validator.DistributorCustomPriceValidtor;
import com.bat.distributor.service.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分销商定制价格业务层
 */
@Service
public class DistributorCustomPriceServiceImpl implements DistributorCustomPriceServiceI {

    @Autowired
    private DistributorCustomPriceQryExe distributorCustomPriceQryExe;

    @Autowired
    private DistributorCustomPriceCmdExe distributorCustomPriceCmdExe;

    @Autowired
    private DistributorCustomPriceConvertor distributorCustomPriceConvertor;

    @Autowired
    private DistributorCustomPriceValidtor distributorCustomPriceValidtor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response saveList(DistributorCustomPriceCmd distributorCustomPriceCmd, String userId, String userName) {
        List<DistributorCustomPriceListCmd> customPriceListCmdList = distributorCustomPriceCmd.getItemPriceList();
        List<DistributorCustomPriceDO> priceDOList =
            distributorCustomPriceQryExe.listByCondition(distributorCustomPriceCmd.getDistributorId(),null);
        // 定义标记、新增还是修改操作
        Boolean isAdd = true;
        if (priceDOList != null && priceDOList.size() > 0) {
            // 修改
            isAdd = false;
        }
        DistributorCustomPriceValidtor.checkParam(customPriceListCmdList, isAdd);
        //处理修改
        DealWithByUpdate(customPriceListCmdList, priceDOList, isAdd,userId,userName);
        if ((customPriceListCmdList == null || customPriceListCmdList.size() == 0) && !isAdd) {
            return Response.buildSuccess();
        }

        createList(customPriceListCmdList, distributorCustomPriceCmd.getDistributorId(),userId,userName);
        return Response.buildSuccess();
    }

    @Override
    public Response<List<DistributorCustomPriceDTO>> listDTOByDistributorId(Integer distributorId) {
        List<DistributorCustomPriceDO> priceDOList = distributorCustomPriceQryExe.listByCondition(distributorId,null);
        if (priceDOList == null || priceDOList.size() == 0) {
            return Response.buildSuccess();
        }
        List<DistributorCustomPriceDTO> list = distributorCustomPriceConvertor.toDistributorCustomPriceDTOList(priceDOList);
        return Response.of(list);
    }

    private void createList(List<DistributorCustomPriceListCmd> customPriceListCmdList, Integer distributorId,String userId,String userName) {
        if (customPriceListCmdList == null || customPriceListCmdList.size() == 0) {
            return;
        }
        customPriceListCmdList.stream().forEach(distributorCustomPriceListCmd -> {
            DistributorCustomPriceDO customPriceDO = DistributorCustomPriceConvertor.toCreateDistributorCustomPriceDO(distributorId,distributorCustomPriceListCmd.getPrice(),
                    distributorCustomPriceListCmd.getItemId(),Integer.parseInt(userId),userName);
            //校验是否重复
            distributorCustomPriceValidtor.validDistributorItemExist(distributorId,distributorCustomPriceListCmd.getItemId());
            distributorCustomPriceCmdExe.create(customPriceDO);
        });
    }

    // 处理编辑时、是否删除了
    private void DealWithByUpdate(List<DistributorCustomPriceListCmd> customPriceListCmdList,
        List<DistributorCustomPriceDO> priceDOList, Boolean isAdd,  String userId, String userName) {
        if (isAdd) {
            return;
        }
        if (priceDOList != null && priceDOList.size() > 0) {
            for (int x = 0; x < priceDOList.size(); x++) {
                DistributorCustomPriceDO customPriceDO = priceDOList.get(x);
                for (int y = 0; y < customPriceListCmdList.size(); y++) {
                    if (customPriceListCmdList.get(y).getItemId() - customPriceDO.getItemId() == 0) {
                        // 没有删除、修改价格
                        customPriceDO.setPrice(customPriceListCmdList.get(y).getPrice());
                        // 处理操作人信息
                        customPriceDO.setUpdateUserId(Integer.parseInt(userId));
                        customPriceDO.setUpdateUserName(userName);
                        customPriceDO.setUpdateTime(new Date());
                        distributorCustomPriceCmdExe.update(customPriceDO);
                        priceDOList.remove(x);
                        x--;
                        customPriceListCmdList.remove(y);
                        y--;
                        break;
                    }
                }
            }
            //
            if (priceDOList != null && priceDOList.size() > 0) {
                priceDOList.stream().forEach(distributorCustomPriceDO -> {
                    distributorCustomPriceDO.setDelFlag(Constant.COMMON_DEL_FLAG_YES);
                    // 处理操作人信息
                    distributorCustomPriceDO.setUpdateUserId(Integer.parseInt(userId));
                    distributorCustomPriceDO.setUpdateUserName(userName);
                    distributorCustomPriceDO.setUpdateTime(new Date());
                    distributorCustomPriceCmdExe.update(distributorCustomPriceDO);
                });
            }
        }
    }

    public static void main(String[] args) {
        List<DistributorCustomPriceDO> list = new ArrayList<>();
        DistributorCustomPriceDO customPriceDO = new DistributorCustomPriceDO();
        customPriceDO.setItemId(1);
        customPriceDO.setId(111);
        list.add(customPriceDO);
        DistributorCustomPriceDO customPriceDO2 = new DistributorCustomPriceDO();
        customPriceDO2.setItemId(2);
        customPriceDO2.setId(111);
        list.add(customPriceDO2);
        List<Integer> integerList = list.stream().map(DistributorCustomPriceDO::getItemId).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(integerList));
    }

}
