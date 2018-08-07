package com.bat.goods.api.classify;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.classify.dto.*;
import com.bat.goods.api.classify.dto.data.ClassifyDTO;
import com.bat.goods.api.classify.dto.data.ClassifyRecommendDTO;
import com.bat.goods.api.classify.dto.*;

public interface ClassifyServiceI {
    /**
     * 添加商品分类
     * 
     * @param classifyCmd
     * @return
     */
    public void createClassify(ClassifyCmd classifyCmd);

    /**
     * 更新商品分类
     * 
     * @param classifyCmd
     * @return
     */
    public void updateClassify(ClassifyCmd classifyCmd);

    /**
     * 更新商品分类状态
     * 
     * @param cmd
     * @return
     */
    public void openClassify(ClassifyOpenCmd cmd);

    /**
     * 获取商品分类列表（分页）
     * 
     * @param classifyListQry
     * @return
     */
    public PageInfo listClassify(ClassifyListQry classifyListQry);

    /**
     * 获取子分类列表
     * 
     * @param classifySubListQry
     * @return
     */
    public List<ClassifyDTO> listSubClassify(ClassifySubListQry classifySubListQry);

    /**
     * 根据ID删除商品分类
     * 
     * @param cmd
     * @return
     */
    public void deleteClassify(ClassifyId cmd);

    /**
     * 根据商品分类id获取详情
     * 
     * @param classifyId
     * @return
     */
    public ClassifyDTO getClassify(ClassifyId classifyId);

    /**
     * 获取推荐分类信息
     */
    ClassifyRecommendDTO recommendInfo();

    /**
     * 更新推荐分类信息
     * @param cmd
     */
    void updateRecommendInfo(ClassifyRecommendCmd cmd);
}
