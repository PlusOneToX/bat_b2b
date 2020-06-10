package com.bat.flexible.manager.picture.executor;

import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.flexible.dao.picture.PictureCategoryDOMapper;
import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureCategoryQryExe {

    @Autowired
    private PictureCategoryDOMapper pictureCategoryDOMapper;

    @Autowired
    private PictureCategoryTreeQryExe pictureCategoryTreeQryExe;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Value("${distributor.id.sanxing}")
    private Integer sanxingDistributorId;

    @Cached(name = FlexibleKeyConstant.PICTURE_CATEGORY_DO_PRE,key = "#id")
    public PictureCategoryDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        PictureCategoryDO pictureCategoryDO = pictureCategoryDOMapper.selectByPrimaryKey(id);
        if(pictureCategoryDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return pictureCategoryDO;
    }

    public List<PictureCategoryDO> listByParentIdAndOpenFlag(Integer parentId,Short openFlag) {
        return pictureCategoryDOMapper.listByParentIdAndOpenFlag(parentId,openFlag);
    }

    public List<PictureCategoryPageCO> listCOByCondition(Integer parentId, Short type, Short openFlag, Short atLastTrademark,
                                                         String content) {
        //先获取树结构
        List<PictureCategoryPageCO> list = pictureCategoryTreeQryExe.treeAll(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        if(list ==null){
            return new ArrayList<>();
        }
        //递归处理parentId
        list = filterParentId(parentId,list);
        //处理openFlag
        list =filterOpenFlagRecurise(list,openFlag);
        //处理type
        list = filterTypeRecurise(type,list);
        //过滤atLastTrademark
        list =filterAtLastTrademarkRecurise(atLastTrademark,list);
        //过滤最终分类
        list =filterAtLastTrademarkYes(atLastTrademark,list);
        //过滤搜索关键词
        list =filterContentRecurise(content,list);
        return list;
    }

    private List<PictureCategoryPageCO> filterAtLastTrademarkYes(Short atLastTrademark, List<PictureCategoryPageCO> list) {
        if(list ==null || list.size()==0 || atLastTrademark ==null || FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(atLastTrademark)){
            return list;
        }
        //开始处理最终分类
        List<PictureCategoryPageCO> sonList = new ArrayList<>();
        getSonRecurise(list,sonList);
        return sonList;
    }

    /**
     * 递归获取最终分类
     * @param list
     * @param sonList
     */
    private void getSonRecurise( List<PictureCategoryPageCO> list, List<PictureCategoryPageCO> sonList) {
        if(list == null || list.size()==0){
            return ;
        }
        for(int x=0;x<list.size();x++){
            PictureCategoryPageCO pictureCategoryPageCO = list.get(x);
            List<PictureCategoryPageCO> childrenList = pictureCategoryPageCO.getChildrenList();
            if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(pictureCategoryPageCO.getAtLastTrademark())){
                //属于最终分类
                sonList.add(pictureCategoryPageCO);
                continue;
            }
            //该分类id非父类id、查看子类列表是否存在
            if(childrenList !=null && childrenList.size() >0){
                //查看子类是否存在
                getSonRecurise(childrenList,sonList);
            }
        }
    }

    private List<PictureCategoryPageCO> filterContentRecurise(String content, List<PictureCategoryPageCO> list) {
        if(StringUtils.isBlank(content)){
            return list;
        }
        if(list == null || list.size()==0){
            return null;
        }
        for(int x=0;x<list.size();x++){
            PictureCategoryPageCO pictureCategoryPageCO = list.get(x);
            List<PictureCategoryPageCO> childrenList = pictureCategoryPageCO.getChildrenList();
            if(pictureCategoryPageCO.getName().indexOf(content)>-1 || pictureCategoryPageCO.getEnglishName().indexOf(content)>-1){
                //找到父节点的子类列表、直接返回子列表
                continue;
            }
            //该分类id非父类id、查看子类列表是否存在
            if(childrenList ==null || childrenList.size()==0){
                //没有下一级
                list.remove(x);
                x--;
            }else{
                //查看子类是否存在
                childrenList =filterContentRecurise(content,childrenList);
                if(childrenList !=null){
                    //找到了
                    return childrenList;
                }
            }
        }
        return list;
    }



    /**
     * 递归查询非最终分类
     * @param atLastTrademark
     * @param list
     * @return
     */
    private List<PictureCategoryPageCO> filterAtLastTrademarkRecurise(Short atLastTrademark, List<PictureCategoryPageCO> list) {
        if(atLastTrademark ==null || FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(atLastTrademark)){
            return list;
        }
        if(list == null || list.size()==0){
            return null;
        }
        for(int x=0;x<list.size();x++){
            PictureCategoryPageCO pictureCategoryPageCO = list.get(x);
            List<PictureCategoryPageCO> childrenList = pictureCategoryPageCO.getChildrenList();
            if(pictureCategoryPageCO.getAtLastTrademark() - atLastTrademark==0 && FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(atLastTrademark)){
                //属于最终分类、且匹配上
                continue;
            }
            if(pictureCategoryPageCO.getAtLastTrademark() - atLastTrademark==0 &&
                    FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(atLastTrademark) && (childrenList==null || childrenList.size()==0)){
                //属于非最终分类、且没有子级列表、且匹配上
                continue;
            }

            if(childrenList ==null || childrenList.size()==0 ){
                //没有子节点列表
                list.remove(x);
                x--;
            }else{
                //查看子类是否存在
                childrenList =filterAtLastTrademarkRecurise(atLastTrademark,childrenList);
               /* if(childrenList !=null){
                    //找到了
                    return childrenList;
                }*/
            }
        }
        return list;
    }

    private List<PictureCategoryPageCO> filterOpenFlagRecurise(List<PictureCategoryPageCO> list,Short openFlag) {
        if (openFlag ==null){
            return list;
        }
        if(list ==null || list.size()==0){
            return null;
        }
        for(int x=0;x<list.size();x++){
            PictureCategoryPageCO pictureCategoryPageCO = list.get(x);
            List<PictureCategoryPageCO> childrenList = pictureCategoryPageCO.getChildrenList();

            if( pictureCategoryPageCO.getOpenFlag() - openFlag==0){
                //状态一样
               continue;
            }
            //该分类id非父类id、查看子类列表是否存在
            if(childrenList ==null || childrenList.size()==0){
                //没有下一级
                list.remove(x);
                x--;
            }else{
                //过滤子类列表
                childrenList =filterOpenFlagRecurise(childrenList,openFlag);
                if(childrenList ==null || childrenList.size()==0){
                    //子类列表都不满足条件、剔除该节点
                    list.remove(x);
                    x--;
                }
            }
        }
        return list;
    }

    private List<PictureCategoryPageCO> filterTypeRecurise(Short type, List<PictureCategoryPageCO> list) {
        if(type ==null){
            return list;
        }
        if(list ==null || list.size()==0){
            return null;
        }
        for(int x=0;x<list.size();x++){
            PictureCategoryPageCO pictureCategoryPageCO = list.get(x);
            List<PictureCategoryPageCO> childrenList = pictureCategoryPageCO.getChildrenList();
            if( pictureCategoryPageCO.getType() - type==0){
                continue;
            }

            //该分类id非父类id、查看子类列表是否存在
            if(childrenList ==null || childrenList.size()==0){
                //没有下一级
                list.remove(x);
                x--;
            }else{
                //过滤子类列表
                childrenList =filterTypeRecurise(type,childrenList);
                if(childrenList ==null || childrenList.size()==0){
                    //子类列表都不满足条件、剔除该节点
                    list.remove(x);
                    x--;
                }
            }
        }
        return list;
    }

    private List<PictureCategoryPageCO> filterParentId(Integer parentId,List<PictureCategoryPageCO> list) {
        if(parentId ==FlexibleCommonConstant.COMMON_PARENT_ID){
            //返回的列表、就是0的子类
            return list;
        }
        if(list == null || list.size()==0){
            return null;
        }
        for(int x=0;x<list.size();x++){
            PictureCategoryPageCO pictureCategoryPageCO = list.get(x);
            List<PictureCategoryPageCO> childrenList = pictureCategoryPageCO.getChildrenList();
            if(pictureCategoryPageCO.getId() - parentId==0){
                //找到父节点的子类列表、直接返回子列表
                list =childrenList;
                return childrenList;
            }
            //该分类id非父类id、查看子类列表是否存在
            if(childrenList ==null || childrenList.size()==0){
                //没有下一级
                list.remove(x);
                x--;
            }else{
                //查看子类是否存在
                childrenList =filterParentId(parentId,childrenList);
                if(childrenList !=null){
                    //找到了
                    return childrenList;
                }
            }
        }
        return list;
    }

    public PictureCategoryDO findEffectByUpOrDown(Integer parentId, Boolean flag, BigDecimal sequence) {
        return pictureCategoryDOMapper.findEffectByUpOrDown(parentId,flag,sequence);
    }

    public List<PictureCategoryDO> listByCondition(Short openFlag) {
        return pictureCategoryDOMapper.listByCondition(openFlag);
    }

    public List<PictureCategoryDO> listByPictureThemeId(Integer themeId) {
        return pictureCategoryDOMapper.listByPictureThemeId(themeId);
    }

    public List<PictureCategoryDO> detailTabMappList(ThemeDTO themeDTO) {

        Integer countryId = null;
        try {
            countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(themeDTO.getDistributorId());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(e.getMessage());
        }
        //判断是否是
        boolean isSanXing=false;
        if(themeDTO.getDistributorId()==sanxingDistributorId.intValue()){
            isSanXing=true;
        }

        return pictureCategoryDOMapper.listByTypeAndDistributorIdsAndMaterialIdAndModelId(themeDTO.getType(),themeDTO.getDistributorIds(),isSanXing,countryId,
                themeDTO.getMaterialId(),themeDTO.getModelId());
    }

    public List<PictureCategoryDO> listByIdList(List<Integer> categoryIdList) {
        return pictureCategoryDOMapper.listByIdList(categoryIdList);
    }

    public List<PictureCategoryDO> listByParentId(Integer parentId) {
        return pictureCategoryDOMapper.listByParentId(parentId);
    }
}
