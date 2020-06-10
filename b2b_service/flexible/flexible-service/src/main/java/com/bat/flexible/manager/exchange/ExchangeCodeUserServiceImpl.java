package com.bat.flexible.manager.exchange;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.order.*;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeUserCmdExe;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeUserQryExe;
import com.bat.flexible.manager.exchange.validator.ExchangeCodeUserValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.ExchangeCodeUserServiceI;
import com.bat.flexible.api.exchange.ExchangeMaterialRelevanceServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserCmd;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserDetailDTO;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserPageQry;
import com.bat.flexible.api.exchange.dto.order.*;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.dao.exchange.co.ExchangeCodeUserCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeUserDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.utils.code.AESUtil;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeCodeUserServiceImpl implements ExchangeCodeUserServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCodeUserServiceImpl.class);

    @Autowired
    private ExchangeCodeUserQryExe exchangeCodeUserQryExe;

    @Autowired
    private ExchangeCodeUserCmdExe exchangeCodeUserCmdExe;

    @Autowired
    private ExchangeCodeUserValidator exchangeCodeUserValidator;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeMaterialRelevanceServiceI exchangeMaterialRelevanceServiceI;


    @Override
    public PageInfo<ExchangeCodeUserCO> page(ExchangeCodeUserPageQry exchangeCodeUserPageQry) {
        PageHelper.startPage(exchangeCodeUserPageQry.getPage(),exchangeCodeUserPageQry.getSize());
        List<ExchangeCodeUserCO> exchangeCodeUserCOList = exchangeCodeUserQryExe.listCOByUserIdAndStatus(exchangeCodeUserPageQry.getUserId(),exchangeCodeUserPageQry.getStatus());
        for(ExchangeCodeUserCO exchangeCodeBean:exchangeCodeUserCOList){
            try {
                String secretCode = AESUtil.decrypt(exchangeCodeBean.getSecretCode());
                exchangeCodeBean.setSecretCode(secretCode);
            }catch (Exception e){
                LOGGER.error("卡包解密异常:{}",e);
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_DECODE_FAIL);
            }
        }
        return new PageInfo(exchangeCodeUserCOList);
    }

    @Override
    public ExchangeCodeUserDetailDTO findByExchangeCodeId(Integer id) {
        ExchangeCodeUserCO exchangeCodeUserCO = exchangeCodeUserQryExe.findByExchangeCodeId(id);
        try {
            String secretCode = AESUtil.decrypt(exchangeCodeUserCO.getSecretCode());
            exchangeCodeUserCO.setSecretCode(secretCode);
        } catch (Exception e) {
            LOGGER.error("卡包解密异常:{}", e);
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_DECODE_FAIL);
        }
        //设置商品关联
        List<MaterialRelaSimpleDTO> materialRelaSimpleDTOS = exchangeMaterialRelevanceServiceI.listDTOByExchangeId(exchangeCodeUserCO.getExchangeId());
        if(materialRelaSimpleDTOS==null){
            materialRelaSimpleDTOS=new ArrayList<>();
        }
        ExchangeCodeUserDetailDTO exchangeCodeUserDetail=new ExchangeCodeUserDetailDTO();
        exchangeCodeUserDetail.setExchangeCodeUser(exchangeCodeUserCO);
        exchangeCodeUserDetail.setMaterialRelas(materialRelaSimpleDTOS);
        return exchangeCodeUserDetail;
    }

    @Override
    public Response create(ExchangeCodeUserCmd exchangeCodeUserCmd) {
        //校验暗码
        ExchangeCodeDO exchangeCodeDO = exchangeCodeUserValidator.valid(exchangeCodeUserCmd.getSecretCode());
        ExchangeCodeUserDO exchangeCodeUserDO= new ExchangeCodeUserDO();
        exchangeCodeUserDO.setUserId(exchangeCodeUserCmd.getUserId());
        exchangeCodeUserDO.setExchangeCodeId(exchangeCodeDO.getId());
        exchangeCodeUserDO.setCreateTime(new Date());
        exchangeCodeUserCmdExe.create(exchangeCodeUserDO);
        return Response.buildSuccess();
    }

    @Override
    public Integer count(ExchangeCodeUserPageQry exchangeCodeUserPageQry) {
        exchangeCodeUserValidator.validCountParam(exchangeCodeUserPageQry);
        return exchangeCodeUserQryExe.countByCondition(exchangeCodeUserPageQry.getUserId(),exchangeCodeUserPageQry.getStatus(),exchangeCodeUserPageQry.getMaterialId());
    }

    @Override
    public ExchangePreReturnDTO orderPreAdd(ExchangePreAddCmd exchangePreAddCmd) {
        //找出当前用户未被使用的兑换码
        List<ExchangeCodeUserCO> exchangeCodeUserCOList = exchangeCodeUserQryExe.listCOByUserIdAndStatus(exchangePreAddCmd.getUserId(), ExchangeCodeConstant.StatusUnUse);
        if (exchangeCodeUserCOList.size() == 0) {
            return new ExchangePreReturnDTO();
        }
        for(ExchangeCodeUserCO exchangeCodeUserCO:exchangeCodeUserCOList){
            try {
                String secretCode = AESUtil.decrypt(exchangeCodeUserCO.getSecretCode());
                exchangeCodeUserCO.setSecretCode(secretCode);
            }catch (Exception e){
                exchangeCodeUserCO.setSecretCode("");
                LOGGER.error("卡包解密异常:{}",e);
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_DECODE_FAIL);
            }
        }
        //得到兑换码id列表
        List<Integer> exchangeCodeIds = exchangeCodeUserCOList.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<ComposeQry> composes=new ArrayList<>();
        for (MaterialModelNumCmd materialModel : exchangePreAddCmd.getMaterialModels()) {
            //相同组合个数
            int num = materialModel.getNum();
            //得到适合当前机型与材料的兑换码
            List<ExchangeCodeDO> exchangeCodes = exchangeCodeServiceI.listByExchangeCodeIdListAndMaterialIdAndModelId(materialModel.getMaterialId(), materialModel.getModelId(), exchangeCodeIds);
            if (exchangeCodes.size() == 0) {
                continue;
            }
            for(ExchangeCodeUserCO exchangeCodeUserCO:exchangeCodeUserCOList){
                for(ExchangeCodeDO exchangeCodeDO:exchangeCodes){
                    if(exchangeCodeUserCO.getId().equals(exchangeCodeDO.getId())){
                        exchangeCodeUserCO.setCanSelect((short)1);
                        break;
                    }
                }
            }
            List<ExchangeCodeSelectQry> selectQryList=new ArrayList<>();
            //被选中的兑换码
            List<Integer> selectedExchangeCodeIds = new ArrayList<>();
            for (ExchangeCodeDO exchangeCodeDO : exchangeCodes) {
                ExchangeCodeSelectQry select = new ExchangeCodeSelectQry();
                select.setMaterialId(materialModel.getMaterialId());
                select.setModelId(materialModel.getModelId());
                select.setExchangeCodeId(exchangeCodeDO.getId());
                try {
                    String secretCode = AESUtil.decrypt(exchangeCodeDO.getSecretCode());
                    select.setSecretCode(secretCode);
                }catch (Exception e){
                    LOGGER.error("卡包解密异常:{}",e);
                    throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_DECODE_FAIL);
                }
                selectQryList.add(select);

                for(ExchangeCodeUserCO exchangeCodeUserCO:exchangeCodeUserCOList){
                    if(exchangeCodeUserCO.getId().equals(exchangeCodeDO.getId())){
                        exchangeCodeUserCO.setHasSelect((short)1);
                        break;
                    }
                }
                selectedExchangeCodeIds.add(exchangeCodeDO.getId());
                num = num - 1;
                if (num == 0) {
                    break;
                }
            }
            ComposeQry compose=new ComposeQry();
            compose.setSelects(selectQryList);
            //相同组合个数有剩余，视为缺少兑换卡
            if (num > 0) {
                compose.setNeedNum(num);
            }
            composes.add(compose);
            //刪除已经被选中的兑换码
            Iterator<Integer> iterator = exchangeCodeIds.iterator();
            while (iterator.hasNext()) {
                Integer exchangeCodeId = iterator.next();
                if (selectedExchangeCodeIds.contains(exchangeCodeId)) {
                    iterator.remove();
                }
            }
            //兑换码无元素则退出
            if (exchangeCodeIds.size() == 0) {
                break;
            }
        }
        ExchangePreReturnDTO exchangePreReturnDTO =new ExchangePreReturnDTO();
        exchangePreReturnDTO.setExchangeCodeBeans(exchangeCodeUserCOList);
        exchangePreReturnDTO.setComposes(composes);

        return exchangePreReturnDTO;
    }

    /**
     * 前台用户进行兑换码解绑
     * @param exchangeUnboundCmd
     * @return
     */
    public Response unbound(ExchangeUnboundCmd exchangeUnboundCmd) {
        try{
            LOGGER.info("兑换码原数据：" + exchangeUnboundCmd);
            ExchangeCodeUserDO exchangeCodeUserDO = new ExchangeCodeUserDO();
            exchangeCodeUserDO.setUserId(exchangeUnboundCmd.getUserId());
            exchangeCodeUserDO.setExchangeCodeId(exchangeUnboundCmd.getExchangeCodeId());
            exchangeCodeUserCmdExe.unbound(exchangeCodeUserDO);
            return Response.buildSuccess();
        }catch (Exception e){
            LOGGER.error("前台用户进行兑换码解绑异常");
            return Response.buildFailure("500","前台用户进行兑换码解绑异常");
        }

    }
}
