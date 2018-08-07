package com.bat.goods.service.classify.convertor;

import static com.bat.goods.service.common.Constant.OPERATION_TYPE_1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.bat.goods.api.classify.dto.ClassifyCmd;
import com.bat.goods.api.classify.dto.data.ClassifyDTO;
import com.bat.goods.dao.classify.dataobject.ClassifyDO;

public class ClassifyConvertor {
    /**
     * 商品分类领域类和数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static ClassifyDO toClassifyDo(ClassifyCmd cmd, Short operationType) {
        ClassifyDO classifyDO = new ClassifyDO();
        BeanUtils.copyProperties(cmd, classifyDO);
        if (operationType.equals(OPERATION_TYPE_1)) {
            classifyDO.setId(null);
        }
        Date date = new Date(System.currentTimeMillis());
        if (cmd.getParentId() == null) {
            cmd.setParentId(0);
        }
        classifyDO.setCreateTime(date);
        classifyDO.setUpdateTime(date);
        return classifyDO;
    }

    public static List<ClassifyDTO> toClassifyDTOList(List<ClassifyDO> classifyDOList) {
        List<ClassifyDTO> classifyDTOList = new ArrayList<>();
        classifyDOList.forEach(classifyDO -> {
            ClassifyDTO classifyDTO = new ClassifyDTO();
            BeanUtils.copyProperties(classifyDO, classifyDTO);
            classifyDTOList.add(classifyDTO);
        });
        return classifyDTOList;
    }

    public static ClassifyDTO toClassifyDTO(ClassifyDO classifyDO) {
        ClassifyDTO classifyDTO = new ClassifyDTO();
        BeanUtils.copyProperties(classifyDO, classifyDTO);
        return classifyDTO;
    }

}
