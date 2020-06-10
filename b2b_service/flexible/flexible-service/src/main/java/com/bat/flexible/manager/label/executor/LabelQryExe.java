package com.bat.flexible.manager.label.executor;

import com.alibaba.fastjson.JSON;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.label.LabelDOMapper;
import com.bat.flexible.dao.label.co.LabelPageListQry;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.label.LabelErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LabelQryExe {

    @Autowired
    private LabelDOMapper labelDOMapper;

    public List<LabelDO> listByLabelIdList(List<Integer> labelIdList) {
        List<LabelDO> labelDOList = labelDOMapper.listByLabelIdList(labelIdList);
        if (labelDOList == null || labelDOList.size() == 0) {
            String msg = MessageUtils.get(LabelErrorCode.LABEL_ERROR_MSG_CODE) + "【" + JSON.toJSONString(labelIdList) + "】" + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
            throw new FlexibleCustomException(msg);
        }
        if (labelIdList.size() - labelDOList.size() == 0) {
            return labelDOList;
        }
        labelIdList.stream().forEach(labelId -> {
            Boolean flag = false;
            for (int x = 0; x < labelDOList.size(); x++) {
                if (labelDOList.get(x).getId() - labelId == 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                String msg = MessageUtils.get(LabelErrorCode.LABEL_ERROR_MSG_CODE) + "【" + labelId + "】" + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
                throw new FlexibleCustomException(msg);
            }
        });
        return labelDOList;
    }

    public LabelDO getById(Integer id) {
        System.out.println("id: " + id);

        LabelDO labelDO = labelDOMapper.selectByPrimaryKey(id);

        System.out.println("labelDO: " + labelDO);

        if (labelDO == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(labelDO.getDelFlag())) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return labelDO;
    }

    public List<LabelPageListQry> listByCondition(Short type, Short openFlag, String content) {
        return labelDOMapper.listByCondition(type, openFlag, content);
    }

    /**
     * 条件查询图片关联的标签列表
     *
     * @param distributorIds   分销商id
     * @param categoryId       产品类型id
     * @param pictureId        图片id
     * @param distributorScope 分销商属于国内还是国外 2、国内 3、国外
     * @return
     */
    public List<LabelDO> listDiyLableByCondition(List<Integer> distributorIds, Integer categoryId, Integer pictureId, Short distributorScope) {
        List<LabelDO> list = new ArrayList<>();
        //自定义传图处理
        if (pictureId == 0) {
            //优先根据分销商胡选择
            list = labelDOMapper.listUsableDiyLabelByConditionAndDistributorIds(distributorIds, categoryId, distributorScope, pictureId);
            if (list.size() == 0) {
                list = labelDOMapper.listUsableDiyLabelByCondition(distributorIds, categoryId, distributorScope, pictureId);
            }
        } else {
            //ip图处理
            list = labelDOMapper.listUsableDiyLabelByCondition(distributorIds, categoryId, distributorScope, pictureId);
        }
        return list;
    }
}
