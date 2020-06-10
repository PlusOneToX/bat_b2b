package com.bat.flexible.manager.picture;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.BaseSequenceQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureCategoryCmd;
import com.bat.flexible.api.picture.dto.PictureCategoryDetailQry;
import com.bat.flexible.api.picture.dto.page.PictureCategoryPageQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.picture.executor.PictureCatergoryCmdExe;
import com.bat.flexible.manager.picture.validtor.PictureCategoryValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.picture.PictureCategoryConstant;
import com.bat.flexible.manager.common.utils.code.NumUtils;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.picture.executor.PictureCategoryQryExe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PictureCategoryServiceImpl  implements PictureCategoryServiceI {


    private static final Logger LOGGER = LoggerFactory.getLogger(PictureCategoryServiceImpl.class);

    @Autowired
    private PictureCatergoryCmdExe pictureCatergoryCmdExe;

    @Autowired
    private PictureCategoryQryExe pictureCategoryQryExe;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private PictureCategoryValidator pictureCategoryValidtor;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;




    @Override
    @Transactional
    public Response create(PictureCategoryCmd pictureCategoryCmd, AdminResponse currentAdmin) {
        PictureCategoryDO parentDO = pictureCategoryValidtor.checkParam(pictureCategoryCmd,true);
        PictureCategoryDO pictureCategoryDO = BeanUtils.copy(pictureCategoryCmd,PictureCategoryDO.class);
        setAdminMsg(pictureCategoryDO,currentAdmin);
        //处理序号
        BaseSequenceQry sequenceQry = NumUtils.getSequence(parentDO==null?BigDecimal.ZERO:parentDO.getSequence());
        //将同一个父分类下的分类序号下移一位
        pictureCatergoryCmdExe.updateSequenceAddByParentId(pictureCategoryCmd.getParentId(),sequenceQry.getSequenceAdd(),sequenceQry.getParentSequence());
        pictureCategoryDO.setSequence(sequenceQry.getSequenceStart());
        pictureCategoryDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        pictureCatergoryCmdExe.create(pictureCategoryDO);
        //将父类是否最终分类改为否
       // setParentAtLashTrademark(pictureCategoryDO,currentAdmin);
        return Response.buildSuccess();
    }



    @Transactional
    @Override
    public Response update(PictureCategoryCmd pictureCategoryCmd, AdminResponse currentAdmin) {
        PictureCategoryDO pictureCategoryDO = pictureCategoryQryExe.getById(pictureCategoryCmd.getId());
        PictureCategoryDO parentDO=pictureCategoryValidtor.checkParam(pictureCategoryCmd,false);
        //处理父类修改时、排序号问题
        updateSequenceByParentIdChange(pictureCategoryDO,pictureCategoryCmd,parentDO);
        pictureCategoryDO.setParentId(pictureCategoryCmd.getParentId());
        pictureCategoryDO.setName(pictureCategoryCmd.getName());
        pictureCategoryDO.setEnglishName(pictureCategoryCmd.getEnglishName());
        pictureCategoryDO.setImage(pictureCategoryCmd.getImage());
        pictureCategoryDO.setDescription(pictureCategoryCmd.getDescription());
        //判断是否修改是否最终分类
        pictureCategoryValidtor.checkAtLastTrademark(pictureCategoryCmd,pictureCategoryDO);
        pictureCategoryDO.setAtLastTrademark(pictureCategoryCmd.getAtLastTrademark());
        pictureCategoryDO.setType(pictureCategoryCmd.getType());
        pictureCategoryDO.setOpenFlag(pictureCategoryCmd.getOpenFlag());
        setAdminMsg(pictureCategoryDO,currentAdmin);
        pictureCatergoryCmdExe.update(pictureCategoryDO);
        return Response.buildSuccess();
    }



    @Override
    public Response<PictureCategoryDetailQry> detailById(Integer id) {
        PictureCategoryDO pictureCategoryDO = pictureCategoryQryExe.getById(id);
        PictureCategoryDetailQry pictureCategoryDetailQry = BeanUtils.copy(pictureCategoryDO,PictureCategoryDetailQry.class);
        if(pictureCategoryDO.getParentId()==0){
            pictureCategoryDetailQry.setParentName("顶级分类");
            pictureCategoryDetailQry.setParentEnglistName("TOP CATEGORY");
        }else{
            PictureCategoryDO parentDO = pictureCategoryQryExe.getById(pictureCategoryDO.getParentId());
            pictureCategoryDetailQry.setParentName(parentDO.getName());
            pictureCategoryDetailQry.setParentEnglistName(parentDO.getEnglishName());
        }
        return Response.of(pictureCategoryDetailQry);
    }

    /**
     * 分页查询图片分类
     * @param categoryPageQry
     * @return
     */
    @Override
    public Response<PageInfo<PictureCategoryPageCO>> page(PictureCategoryPageQry categoryPageQry) {

        List<PictureCategoryPageCO> list = pictureCategoryQryExe.listCOByCondition(categoryPageQry.getParentId(),categoryPageQry.getType(),categoryPageQry.getOpenFlag(),
                categoryPageQry.getAtLastTrademark(),categoryPageQry.getContent());
        PageInfo<PictureCategoryPageCO> pageInfo = new PageInfo<>();
        pageInfo.setTotal(0);
        if(list !=null && list.size()>0){
            pageInfo.setTotal(list.size());
            //截取列表
            Integer startIndex =(categoryPageQry.getPage()-1)*categoryPageQry.getSize();

            if(startIndex >=list.size()){
                //已过了最后一页
                list=new ArrayList<>();
            }else if(categoryPageQry.getPage()*categoryPageQry.getSize() >list.size()){
                list = list.subList((categoryPageQry.getPage()-1)*categoryPageQry.getSize(),list.size());
                pageInfo.setIsLastPage(true);
            }else{
                list = list.subList((categoryPageQry.getPage()-1)*categoryPageQry.getSize(),(categoryPageQry.getPage()*categoryPageQry.getSize()));
                pageInfo.setIsLastPage(false);
            }

            pageInfo.setList(list);
        }
        return Response.of(pageInfo);
    }

    @Override
    @Transactional
    public Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        PictureCategoryDO pictureCategoryDO = pictureCategoryQryExe.getById(flexibleUpOrDownDTO.getId());
        PictureCategoryDO effect = pictureCategoryQryExe.findEffectByUpOrDown(pictureCategoryDO.getParentId(),flexibleUpOrDownDTO.getFlag(),pictureCategoryDO.getSequence());
        if(effect==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effect==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        //对调排序号
        BigDecimal temp = pictureCategoryDO.getSequence();
        pictureCategoryDO.setSequence(effect.getSequence());
        //序号差
        BigDecimal sequence = pictureCategoryDO.getSequence().subtract(effect.getSequence());
        //操作的分类、将其子分类减去序号差
        pictureCatergoryCmdExe.updateSequenceAddByParentId(pictureCategoryDO.getId(),sequence.negate(),BigDecimal.ZERO);
        //收到影响的分类、将其子分类序号加序号差
        pictureCatergoryCmdExe.updateSequenceAddByParentId(effect.getId(),sequence,BigDecimal.ZERO);
        effect.setSequence(temp);
        setAdminMsg(pictureCategoryDO,currentAdmin);
        setAdminMsg(effect,currentAdmin);
        pictureCatergoryCmdExe.update(pictureCategoryDO);
        pictureCatergoryCmdExe.update(effect);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        PictureCategoryDO categoryDO = pictureCategoryQryExe.getById(flexibleUpdateStatusDTO.getId());
        if(categoryDO.getOpenFlag()-flexibleUpdateStatusDTO.getOpenFlag()==0){
            throw  new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        //校验禁用
        pictureCategoryValidtor.checkWhenDisable(categoryDO,flexibleUpdateStatusDTO.getOpenFlag());
        categoryDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(categoryDO,currentAdmin);
        pictureCatergoryCmdExe.update(categoryDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        PictureCategoryDO pictureCategoryDO = pictureCategoryQryExe.getById(id);
        //判断下面还有没有子分类
        pictureCategoryValidtor.checkWhenDelete(pictureCategoryDO);
        setAdminMsg(pictureCategoryDO,currentAdmin);
        pictureCategoryDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        pictureCatergoryCmdExe.update(pictureCategoryDO);
        return Response.buildSuccess();
    }

    @Override
    public PictureCategoryDO getById(Integer categoryId) {
        return pictureCategoryQryExe.getById(categoryId);
    }

    @Override
    public List<PictureCategoryDO> listByParentIdAndOpenFlag(Integer parentId,Short openFlag) {
        return pictureCategoryQryExe.listByParentIdAndOpenFlag(parentId,openFlag);
    }

    @Override
    public List<PictureCategoryDO> listByPictureThemeId(Integer themeId) {
        return pictureCategoryQryExe.listByPictureThemeId(themeId);
    }

    @Override
    public Response detailTabMappList(ThemeDTO themeDTO) {
        if(themeDTO.getDistributorId()==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        themeDTO.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(themeDTO.getDistributorId()));
        themeDTO.setType(PictureCategoryConstant.TYPE_CHARTLET);
        List<PictureCategoryDO> pictureCategories = pictureCategoryQryExe.detailTabMappList(themeDTO);
        return Response.of(pictureCategories);
    }

    @Override
    public List<PictureCategoryDO> listByIdList(List<Integer> categoryIds) {
        return pictureCategoryQryExe.listByIdList(categoryIds);
    }

    @Override
    public List<PictureCategoryDO> listByCondition(Short openFlag) {
        return pictureCategoryQryExe.listByCondition(openFlag);
    }

    @Override
    public List<PictureCategoryPageCO> detailPictureCategory(Integer categoryId) {
        List<PictureCategoryDO> list = pictureCategoryQryExe.listByParentId(categoryId);
        List<PictureCategoryPageCO> pictureCategoryPageCOS = new ArrayList<>();
        for (PictureCategoryDO pictureCategoryDO : list) {
            PictureCategoryPageCO pictureCategoryPageCO = new PictureCategoryPageCO();
            org.springframework.beans.BeanUtils.copyProperties(pictureCategoryDO, pictureCategoryPageCO);
            pictureCategoryPageCOS.add(pictureCategoryPageCO);
        }

        return pictureCategoryPageCOS;
    }

    /**
     * 父类变更时、重置父类排序号
     * @param pictureCategoryDO
     * @param pictureCategoryCmd
     * @param parentDO
     */
    private void updateSequenceByParentIdChange(PictureCategoryDO pictureCategoryDO, PictureCategoryCmd pictureCategoryCmd, PictureCategoryDO parentDO) {
        if(pictureCategoryCmd.getParentId() - pictureCategoryDO.getParentId() ==0){
            //父分类没有变化
            return;
        }
        BigDecimal newParent =parentDO==null? BigDecimal.ZERO:parentDO.getSequence();
        BaseSequenceQry sequenceQry = NumUtils.getSequence(newParent);
        //新的分类全部加1
        pictureCatergoryCmdExe.updateSequenceAddByParentId(pictureCategoryCmd.getParentId(), sequenceQry.getSequenceAdd(),sequenceQry.getParentSequence());
        BaseSequenceQry qry = NumUtils.getSequence(pictureCategoryDO.getSequence());
        //旧的分类大于这个序号、减1
        pictureCatergoryCmdExe.updateSequenceAddByParentId(pictureCategoryDO.getParentId(), qry.getSequenceAdd().negate(),pictureCategoryDO.getSequence());
        //排在第一位
        pictureCategoryDO.setSequence(sequenceQry.getSequenceStart());

    }



    private void setAdminMsg(PictureCategoryDO pictureCategoryDO, AdminResponse currentAdmin) {
        if(pictureCategoryDO.getId() ==null){
            pictureCategoryDO.setCreateTime(new Date());
            pictureCategoryDO.setCreateUserId(currentAdmin.getId());
            pictureCategoryDO.setCreateUserName(currentAdmin.getUserName());
        }
        pictureCategoryDO.setUpdateTime(new Date());
        pictureCategoryDO.setUpdateUserId(currentAdmin.getId());
        pictureCategoryDO.setUpdateUserName(currentAdmin.getUserName());
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list = list.subList(0,3);
        System.out.println(JSON.toJSONString(list));
    }

}
