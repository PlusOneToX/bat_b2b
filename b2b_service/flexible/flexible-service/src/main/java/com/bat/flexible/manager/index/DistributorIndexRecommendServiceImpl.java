package com.bat.flexible.manager.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.DistributorIndexRecommendRelevanceServiceI;
import com.bat.flexible.api.index.DistributorIndexRecommendServiceI;
import com.bat.flexible.api.index.IndexRecommendPictureRelevanceServiceI;
import com.bat.flexible.api.index.dto.page.IndexRecommendPageQry;
import com.bat.flexible.api.index.dto.recommend.DistributorIndexRecommendDTO;
import com.bat.flexible.api.index.dto.recommend.DistributorPictureIdListDTO;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.PictureTemplateEditServiceI;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.index.executor.DistributorIndexRecommendCmdExe;
import com.bat.flexible.manager.index.executor.DistributorIndexRecommendQryExe;
import com.bat.flexible.manager.index.validator.DistributorSeriesThemeValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendDO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import com.bat.flexible.manager.error.index.DistributorRecommendErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DistributorIndexRecommendServiceImpl implements DistributorIndexRecommendServiceI {

    @Autowired
    private DistributorIndexRecommendCmdExe distributorIndexRecommendCmdExe;

    @Autowired
    private DistributorIndexRecommendQryExe distributorIndexRecommendQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private DistributorServiceRpc distributorServiceRpc;


    @Autowired
    private IndexRecommendPictureRelevanceServiceI indexRecommendPictureRelevanceServiceI;

    @Autowired
    private DistributorIndexRecommendRelevanceServiceI distributorIndexRecommendRelevanceServiceI;




    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private PictureTemplateEditServiceI pictureTemplateEditServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    /**
     * 新增
     * @param distributorPictureIdListDTO
     * @param currentAdmin
     * @return
     */
    @Transactional
    @Override
    public Response add(DistributorPictureIdListDTO distributorPictureIdListDTO, AdminResponse currentAdmin) {
        //校验是否重复
        List<Integer> distributorIdList = DistributorSeriesThemeValidator.validDistributor(distributorPictureIdListDTO.getDistributorList());
        List<DistributorIndexRecommendRelevanceDO> distributorIndexRecommendRelaList = distributorIndexRecommendRelevanceServiceI.listByDistributorIdList(distributorIdList);
        if(distributorIndexRecommendRelaList !=null && distributorIndexRecommendRelaList.size()>0){
            distributorIndexRecommendRelaList.stream().forEach(recommendRela ->{
/*                com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(recommendRela.getDistributorId());
                DistributorRpcDTO distributorRpcDTO = distributorRpcDTOResponse.getData();*/
                String msg = MessageUtils.get(DistributorRecommendErrorCode.INDEX_DISTRIBUTOR_INDEX_RECOMMEND_MORE_THEN_ONE_ERROR) +",分销商【"+recommendRela.getDistributorName()+"】已设置";
                throw new FlexibleCustomException(msg);
            } );

        }
        DistributorIndexRecommendDO distributorIndexRecommend = new DistributorIndexRecommendDO();
        setAdminMsg(currentAdmin, distributorIndexRecommend);
        List<Integer> pictureIdList = new ArrayList<>(distributorPictureIdListDTO.getPictureIdList());
        //校验分销商权限的问题
        pictureServiceI.checkDistributorPicture(distributorIdList,pictureIdList);
        distributorIndexRecommendCmdExe.create(distributorIndexRecommend);

        indexRecommendPictureRelevanceServiceI.add(pictureIdList,distributorIndexRecommend.getId(),currentAdmin);
        distributorIndexRecommendRelevanceServiceI.add(distributorPictureIdListDTO.getDistributorList(),distributorIndexRecommend.getId(),currentAdmin);
        return Response.buildSuccess();
    }

    @Override
    public Response update(DistributorPictureIdListDTO distributorPictureIdListDTO, AdminResponse currentAdmin) {
        //校验是否重复
        DistributorIndexRecommendDO distributorIndexRecommend = distributorIndexRecommendQryExe.getById(distributorPictureIdListDTO.getId());
        //校验分销商参数
        List<Integer> distributorIdList = DistributorSeriesThemeValidator.validDistributor(distributorPictureIdListDTO.getDistributorList());
        List<DistributorIndexRecommendRelevanceDO> distributorIndexRecommendRelaList = distributorIndexRecommendRelevanceServiceI.listByDistributorIdList(distributorIdList);
        if(distributorIndexRecommendRelaList !=null && distributorIndexRecommendRelaList.size()>0){
            distributorIndexRecommendRelaList.stream().forEach(recommendRela ->{
                if(recommendRela.getIndexRecommendId() - distributorIndexRecommend.getId() !=0){
                    com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(recommendRela.getDistributorId());
                    DistributorRpcDTO distributorRpcDTO = distributorRpcDTOResponse.getData();
                    String msg = MessageUtils.get(DistributorRecommendErrorCode.INDEX_DISTRIBUTOR_INDEX_RECOMMEND_MORE_THEN_ONE_ERROR) +",分销商【"+distributorRpcDTO.getName()+"】已设置";
                    throw new FlexibleCustomException(msg);
                }
            } );
        }
        setAdminMsg(currentAdmin, distributorIndexRecommend);
        List<Integer> pictureIdList = new ArrayList<>(distributorPictureIdListDTO.getPictureIdList());
        //校验分销商权限的问题
        pictureServiceI.checkDistributorPicture(distributorIdList,pictureIdList);
        distributorIndexRecommendCmdExe.update(distributorIndexRecommend);
        indexRecommendPictureRelevanceServiceI.update(pictureIdList,distributorIndexRecommend.getId(),currentAdmin);
        distributorIndexRecommendRelevanceServiceI.save(distributorPictureIdListDTO.getDistributorList(),distributorIndexRecommend.getId(),currentAdmin);
        return Response.buildSuccess();
    }



    @Override
    public Response detail(Integer id) {
        DistributorIndexRecommendDO distributorIndexRecommendDO = distributorIndexRecommendQryExe.getById(id);
        DistributorIndexRecommendDTO recommendDTO = new DistributorIndexRecommendDTO();
        recommendDTO.setId(id);
        List<IndexRecommendRelaCO> dtoList = indexRecommendPictureRelevanceServiceI.listByIndexRecommendId(id);
        recommendDTO.setPictureList(dtoList);
        List<DistributorSimpleRelaQry> distributorSimpleDTOList = distributorIndexRecommendRelevanceServiceI.listDistributorMsg(id);
        recommendDTO.setDistributorList(distributorSimpleDTOList);
        return Response.of(recommendDTO);
    }



    /**
     * 根据图片id查询其关联到首页推荐和主题系列的分销商列表

     * @return
     */
  /*  @Override
    public List<DistributorInfo> listDistributorIndexRecommendAndSeriesThemeByPictureId(Long pictureId) {
        return distributorIndexRecommendDataManager.listDistributorIndexRecommendAndSeriesThemeByPictureId(pictureId);
    }*/

    @Override
    public DistributorIndexRecommendDO getById(Integer indexRecommendId) {
        return distributorIndexRecommendQryExe.getById(indexRecommendId);
    }

    @Override
    public void update(DistributorIndexRecommendDO distributorIndexRecommend) {
        distributorIndexRecommendCmdExe.update(distributorIndexRecommend);
    }

    @Override
    public void delete(Integer id) {
        distributorIndexRecommendCmdExe.delete(id);
    }

    @Override
    public PageInfo<CommonPicturePageCO> pageByDistributorIdNew(IndexRecommendPageQry pageRequest) {
        pageRequest.setDistributorIds(flexibleDistributorQryExe.getDistributorTreePaths(pageRequest.getDistributorId()));
        PageInfo<CommonPicturePageCO> pageInfo = distributorIndexRecommendQryExe.listCOByCondition(pageRequest.getExchangeId(),
                pageRequest.getDistributorId(),pageRequest.getDistributorIds(),pageRequest.getMaterialId(),pageRequest.getModelId(),pageRequest.getPage(),
                pageRequest.getSize());
        List<CommonPicturePageCO> list = pageInfo.getList();
        if(list ==null || list.size()==0){
            return new PageInfo(list);
        }
        for (CommonPicturePageCO commonPicturePageCO : list) {
            //处理模板
            if (commonPicturePageCO.getType() == PictureConstant.PICTURE_TYPE_TEMPLATE) {
                List<PictureTemplateEditCmd> templateEditList = pictureTemplateEditServiceI.listByPictureId(commonPicturePageCO.getPictureId());
                commonPicturePageCO.setTemplateEditList(templateEditList);
            }
        }
        return pageInfo;
    }

    /**
     * 设置操作信息
     * @param currentAdmin
     * @param distributorIndexRecommendDO
     */
    private void setAdminMsg(AdminResponse currentAdmin, DistributorIndexRecommendDO distributorIndexRecommendDO) {
        if(distributorIndexRecommendDO.getId() ==null){
            distributorIndexRecommendDO.setCreateTime(new Date());
            distributorIndexRecommendDO.setCreateUserId(currentAdmin.getId());
            distributorIndexRecommendDO.setCreateUserName(currentAdmin.getUserName());
        }
        distributorIndexRecommendDO.setUpdateUserId(currentAdmin.getId());
        distributorIndexRecommendDO.setUpdateTime(new Date());
        distributorIndexRecommendDO.setUpdateUserName(currentAdmin.getUserName());
    }
}
