package com.bat.flexible.manager.index;


import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorIndexRecommendRelevanceServiceI;
import com.bat.flexible.api.index.DistributorIndexRecommendServiceI;
import com.bat.flexible.api.index.IndexRecommendPictureRelevanceServiceI;
import com.bat.flexible.manager.index.executor.IndexRecommendPictureRelevanceCmdExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendDO;
import com.bat.flexible.dao.index.dataobject.IndexRecommendPictureRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.index.executor.IndexRecommendPictureRelevanceQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class IndexRecommendPictureRelevanceServiceImpl implements IndexRecommendPictureRelevanceServiceI {

    @Autowired
    private IndexRecommendPictureRelevanceCmdExe indexRecommendPictureRelevanceCmdExe;

    @Autowired
    private IndexRecommendPictureRelevanceQryExe indexRecommendPictureRelevanceQryExe;



    @Autowired
    private DistributorIndexRecommendServiceI distributorIndexRecommendServiceI;

    @Autowired
    private DistributorIndexRecommendRelevanceServiceI distributorIndexRecommendRelevanceServiceI;

    @Override
    public void setAdminMsg(IndexRecommendPictureRelevanceDO pictureRela, AdminResponse currentAdmin) {
        if(pictureRela.getId()==null){
            pictureRela.setCreateTime(new Date());
            pictureRela.setCreateUserId(currentAdmin.getId());
            pictureRela.setCreateUserName(currentAdmin.getUserName());
        }
        pictureRela.setUpdateTime(new Date());
        pictureRela.setUpdateUserId(currentAdmin.getId());
        pictureRela.setUpdateUserName(currentAdmin.getUserName());
    }

    /**
     * 新增关联关系
     * @param pictureIdList
     * @param indexRecommendId
     * @param currentAdmin
     */
    @Override
    public void add(List<Integer> pictureIdList, Integer indexRecommendId, AdminResponse currentAdmin) {
        IndexRecommendPictureRelevanceDO max = indexRecommendPictureRelevanceQryExe.findMaxSortNo();
        Integer index =1;
        if(max !=null){
            index+=max.getSortNo();
        }
        for (int x=0;x<pictureIdList.size();x++){
            IndexRecommendPictureRelevanceDO pictureRela = new IndexRecommendPictureRelevanceDO();
            pictureRela.setIndexRecommendId(indexRecommendId);
            pictureRela.setSortNo(index+x);
            pictureRela.setPictureId(pictureIdList.get(x));
            setAdminMsg(pictureRela,currentAdmin);
            indexRecommendPictureRelevanceCmdExe.create(pictureRela);
        }
    }

    @Override
    public void update(List<Integer> pictureIdList, Integer indexRecommendId, AdminResponse currentAdmin) {
        //查询现有的
        List<IndexRecommendPictureRelevanceDO> pictureRelaList = indexRecommendPictureRelevanceQryExe.findByIndexRecommendId(indexRecommendId);
        //判断删除的
/*        for(int x=0;x<pictureRelaList.size();x++){
            for(int y=0;y<pictureIdList.size();y++){
                if(pictureRelaList.get(x).getPictureId() - pictureIdList.get(y)==0){
                    pictureIdList.remove(y);
                    pictureRelaList.remove(x);
                    x--;
                    y--;
                    break;
                }
            }
        }*/
        if(pictureRelaList != null && pictureRelaList.size()>0){
            //已被删除
            pictureRelaList.stream().forEach(indexRecommendPictureRela -> {
                indexRecommendPictureRelevanceCmdExe.delete(indexRecommendPictureRela.getId());
            });
        }
        //剩下都是新增
        if(pictureIdList !=null && pictureIdList.size()>0){
            add(pictureIdList,indexRecommendId,currentAdmin);
        }
    }

    @Transactional
    @Override
    public Response updateSortNo(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        IndexRecommendPictureRelevanceDO pictureRela = indexRecommendPictureRelevanceQryExe.getById(flexibleUpOrDownDTO.getId());

        //查询受影响的数据（上移影响上一个序号、下移影响下一个数据）
        IndexRecommendPictureRelevanceDO effectPicture = indexRecommendPictureRelevanceQryExe.findEffect(pictureRela.getSortNo(),flexibleUpOrDownDTO.getFlag(),pictureRela.getIndexRecommendId());
        if(effectPicture==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effectPicture ==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        Integer temp = effectPicture.getSortNo();
        effectPicture.setSortNo(pictureRela.getSortNo());
        pictureRela.setSortNo(temp);
        setAdminMsg(effectPicture,currentAdmin);
        setAdminMsg(pictureRela,currentAdmin);
        indexRecommendPictureRelevanceCmdExe.update(pictureRela);
        indexRecommendPictureRelevanceCmdExe.update(effectPicture);
        DistributorIndexRecommendDO distributorIndexRecommend = distributorIndexRecommendServiceI.getById(pictureRela.getIndexRecommendId());
        distributorIndexRecommend.setUpdateUserName(currentAdmin.getUserName());
        distributorIndexRecommend.setUpdateTime(new Date());
        distributorIndexRecommend.setUpdateUserId(currentAdmin.getId());
        distributorIndexRecommendServiceI.update(distributorIndexRecommend);
        return Response.buildSuccess();
    }

    @Override
    public List<IndexRecommendRelaCO> listByIndexRecommendId(Integer indexRecommendId) {
        return indexRecommendPictureRelevanceQryExe.listCOByIndexRecommendId(indexRecommendId);
    }

    @Override
    @Transactional
    public Response delete(List<Integer> list) {
        list.stream().forEach(id -> {
            IndexRecommendPictureRelevanceDO pictureRela = indexRecommendPictureRelevanceQryExe.getById(id);

            indexRecommendPictureRelevanceCmdExe.delete(id);
            List<IndexRecommendPictureRelevanceDO> recommendPictureRelaList = indexRecommendPictureRelevanceQryExe.findByIndexRecommendId(pictureRela.getIndexRecommendId());
            if(recommendPictureRelaList ==null || recommendPictureRelaList.size()==0){
                //删除首页推荐
                distributorIndexRecommendServiceI.delete(pictureRela.getIndexRecommendId());
                //删除分销商关联
                distributorIndexRecommendRelevanceServiceI.deleteByIndexRecommendId(pictureRela.getIndexRecommendId());
            }
        });

        return Response.buildSuccess();
    }
}
