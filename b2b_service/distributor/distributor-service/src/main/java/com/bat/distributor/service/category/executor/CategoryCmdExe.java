package com.bat.distributor.service.category.executor;

import static com.bat.distributor.service.common.Constant.OPEN_NO;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.category.dto.CategoryCmd;
import com.bat.distributor.api.category.dto.CategoryId;
import com.bat.distributor.api.category.dto.CategoryOpenCmd;
import com.bat.distributor.dao.category.CategoryMapper;
import com.bat.distributor.dao.category.dataobject.CategoryDO;
import com.bat.distributor.service.category.convertor.CategoryConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CategoryCmdExe {

    @Resource
    private CategoryMapper mapper;

    /**
     * 创建分销商类别
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(CategoryCmd cmd) {
        CategoryDO categoryDO = CategoryConvertor.toCategoryDo(cmd);
        mapper.insert(categoryDO);
    }

    /**
     * 更新分销商类别
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryCmd cmd) {
        CategoryDO categoryDO = CategoryConvertor.toCategoryDo(cmd);
        mapper.updateByPrimaryKey(categoryDO);
    }

    /**
     * 更新分销商类别状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openCategory(CategoryOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(OPEN_NO)) {
            // 停用分销商类别前，确保分销商类别下的分销商都已转移或删除
            Integer count = mapper.getCategoryDistributorsCount(cmd.getId());
            if (count > 0) {
                throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CATEGORY_DISTRIBUTORNOTNUL);
            }
        }
        mapper.openCategory(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 根据Id删除分销商类别
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(CategoryId cmd) {
        CategoryDO categoryDO = mapper.selectByPrimaryKey(cmd.getId());
        if (categoryDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CATEGORY_NULL);
        }
        // 停用的分销商类别才允许删除
        if (!categoryDO.getOpenFlag().equals(OPEN_NO)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CATEGORY_DELETE_OPEN_ERROR);
        }
        mapper.deleteByPrimaryKey(cmd.getId());
    }

}
