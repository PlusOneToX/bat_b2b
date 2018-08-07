package com.bat.goods.service.classify.executor;

import com.bat.dubboapi.goods.classify.dto.data.ClassifyRpcDTO;
import com.bat.goods.dao.classify.ClassifyMapper;
import com.bat.goods.dao.classify.dataobject.ClassifyDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClassifyRpcQryExe {

    @Resource
    private ClassifyMapper classifyMapper;

    public List<ClassifyRpcDTO> listByIds(List<Integer> classifyIds) {
        List<ClassifyDO> classifyDOS = classifyMapper.listByIds(classifyIds);
        List<ClassifyRpcDTO> classifyRpcDTOS = new ArrayList<>();
        for (ClassifyDO classifyDO : classifyDOS) {
            ClassifyRpcDTO classifyRpcDTO = new ClassifyRpcDTO();
            BeanUtils.copyProperties(classifyDO, classifyRpcDTO);
            classifyRpcDTOS.add(classifyRpcDTO);
        }
        return classifyRpcDTOS;
    }

}
