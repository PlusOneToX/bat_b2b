package com.bat.flexible.manager.index;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.index.BannerSeriesPictureRelevanceServiceI;
import com.bat.flexible.api.index.DistributorBannerRelevanceServiceI;
import com.bat.flexible.api.index.DistributorBannerServiceI;
import com.bat.flexible.api.index.DistributorSeriesThemeServiceI;
import com.bat.flexible.api.index.dto.banner.DistributorBannerDTO;
import com.bat.flexible.api.index.dto.page.DistributorBannerPageQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.index.DistributorBannerConstants;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.index.executor.DistributorBannerCmdExe;
import com.bat.flexible.manager.index.executor.DistributorBannerQryExe;
import com.bat.flexible.manager.index.validator.DistributorBannerValidator;
import com.bat.flexible.manager.index.validator.DistributorSeriesThemeValidator;
import com.bat.flexible.dao.index.co.DistributorBannerListCO;
import com.bat.flexible.dao.index.co.DistributorBannerPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DistributorBannerServiceImpl implements DistributorBannerServiceI {

    @Autowired
    private DistributorBannerCmdExe distributorBannerCmdExe;

    @Autowired
    private DistributorBannerQryExe distributorBannerQryExe;



    @Autowired
    private BannerSeriesPictureRelevanceServiceI bannerSeriesPictureRelevanceServiceI;



    @Autowired
    private DistributorBannerRelevanceServiceI distributorBannerRelevanceServiceI;

    @Autowired
    private DistributorSeriesThemeServiceI distributorSeriesThemeServiceI;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;
    /**
     * 新增
     * @param distributorBannerDTO
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response create(DistributorBannerDTO distributorBannerDTO, AdminResponse currentAdmin) {
        DistributorBannerDO banner = BeanUtils.copy(distributorBannerDTO,DistributorBannerDO.class);
        //校验状态时间
        DistributorBannerValidator.validStatus(banner,distributorBannerDTO.getPictureIdList());
        //校验分销商参数
        DistributorSeriesThemeValidator.validDistributor(distributorBannerDTO.getDistributorList());
        //设置操作人信息
        setAdminUserMsg(currentAdmin, banner);
        //校验分销商的主题系列权限
        distributorSeriesThemeServiceI.checkDistributorSeriesTheme(distributorBannerDTO.getDistributorList(),banner.getSeriesId());
        DistributorBannerDO max = distributorBannerQryExe.findMaxSortNo();
        banner.setSortNo(max==null?1:max.getSortNo()+1);
        distributorBannerCmdExe.create(banner);
        List<Integer> pictureIdList = new ArrayList<>(distributorBannerDTO.getPictureIdList());
        bannerSeriesPictureRelevanceServiceI.setBannerPictureRelevance(banner,pictureIdList,currentAdmin,true);
        startBannerEvent(banner.getStartTime(),banner.getEndTime(),banner.getId());

        distributorBannerRelevanceServiceI.add(banner.getId(),distributorBannerDTO.getDistributorList(),currentAdmin);
        return Response.buildSuccess();
    }






    /**
     * 编辑
     * @param distributorBannerDTO
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response update(DistributorBannerDTO distributorBannerDTO, AdminResponse currentAdmin) {
        DistributorBannerDO banner = distributorBannerQryExe.getById(distributorBannerDTO.getId());
        //校验分销商参数
        DistributorSeriesThemeValidator.validDistributor(distributorBannerDTO.getDistributorList());
        banner.setBannerUrl(distributorBannerDTO.getBannerUrl());
        banner.setStartTime(distributorBannerDTO.getStartTime());
        banner.setEndTime(distributorBannerDTO.getEndTime());
        banner.setRemark(distributorBannerDTO.getRemark());
        banner.setShowLocation(distributorBannerDTO.getShowLocation());
        //判断前后的类型是否一致
        if(banner.getType() - DistributorBannerConstants.typeSeriesTheme==0 && distributorBannerDTO.getType() - DistributorBannerConstants.typeSeriesActivityLink==0){
            //将主题类型修改了、删除原来的
            bannerSeriesPictureRelevanceServiceI.deleteByBannerId(banner.getId());
        }
        banner.setType(distributorBannerDTO.getType());
        banner.setSeriesId(distributorBannerDTO.getSeriesId());
        banner.setExternalLink(distributorBannerDTO.getExternalLink());
        //设置操作人信息
        setAdminUserMsg(currentAdmin, banner);
        //校验状态时间
        DistributorBannerValidator.validStatus(banner, distributorBannerDTO.getPictureIdList());
        distributorBannerCmdExe.update(banner);
        List<Integer> pictureIdList = new ArrayList<>(distributorBannerDTO.getPictureIdList());
        bannerSeriesPictureRelevanceServiceI.setBannerPictureRelevance(banner,pictureIdList,currentAdmin,false);
        startBannerEvent(banner.getStartTime(),banner.getEndTime(),banner.getId());

        distributorBannerRelevanceServiceI.save(banner.getId(),distributorBannerDTO.getDistributorList(),currentAdmin);
        return Response.buildSuccess();
    }

    @Transactional
    @Override
    public Response delete(List<Integer> list) {
        list.stream().forEach(id -> {
            DistributorBannerDO banner = distributorBannerQryExe.getById(id);
            distributorBannerCmdExe.delete(id);
            if(banner.getType() - DistributorBannerConstants.typeSeriesTheme==0){
                bannerSeriesPictureRelevanceServiceI.deleteByBannerId(id);
            }

        });
        //删除分销商banner关联
        distributorBannerRelevanceServiceI.deleteByBannerIdList(list);
        return Response.buildSuccess();
    }

    @Override
    public Response listByDistributorIdAndStatus(Integer distributorId, Short status) {
      List<Integer>  distributorIds= flexibleDistributorQryExe.getDistributorTreePaths(distributorId);
       List<DistributorBannerListCO> list = distributorBannerQryExe.listCOByDistributorIdsAndStatus(distributorIds,status);
        return Response.of(list);
    }



    @Override
    public List<DistributorBannerDO> listBySeriesId(Integer seriesId) {
        return distributorBannerQryExe.listBySeriesId(seriesId);
    }

    @Override
    public PageInfo<DistributorBannerPageCO> page(DistributorBannerPageQry distributorBannerPageQry) {
       /* ResponseBaseBean responseBaseBean = new ResponseBaseBean();
        PageInfo pageInfo = distributorBannerDataManager.page(distributorBannerPageRrequest);
        responseBaseBean.setData(pageInfo);
        return responseBaseBean;*/
        PageHelper.startPage(distributorBannerPageQry.getPage(),distributorBannerPageQry.getSize());
        List<DistributorBannerPageCO> bannerDOList = distributorBannerQryExe.listCOByCondition(distributorBannerPageQry.getShowLocation(),
                distributorBannerPageQry.getStatus(),distributorBannerPageQry.getContent());
        return new PageInfo<>(bannerDOList);
    }

    @Override
    public Response detail(Integer id) {
        DistributorBannerDO banner = distributorBannerQryExe.getById(id);
        DistributorBannerDTO distributorBannerDTO = BeanUtils.copy(banner,DistributorBannerDTO.class);

        if(banner.getType() - DistributorBannerConstants.typeSeriesTheme==0){
            List<Integer> pictureIdList = bannerSeriesPictureRelevanceServiceI.listPictureIdByBannerId(id);
            Set<Integer> set = new HashSet<>(pictureIdList);
            distributorBannerDTO.setPictureIdList(set);
        }
        List<DistributorSimpleRelaQry> distributorSimpleDTOList = distributorBannerRelevanceServiceI.listDistributorMsgByBannerId(id);
        distributorBannerDTO.setDistributorList(distributorSimpleDTOList);
        return Response.of(distributorBannerDTO);
    }

    /**
     * 设置操作人信息
     * @param currentAdmin
     * @param banner
     */
    private void setAdminUserMsg(AdminResponse currentAdmin, DistributorBannerDO banner) {
        if(banner.getId() ==null){
            banner.setCreateTime(new Date());
            banner.setCreateUserId(currentAdmin.getId());
            banner.setCreateUserName(currentAdmin.getUserName());
        }
        banner.setUpdateTime(new Date());
        banner.setUpdateUserId(currentAdmin.getId());
        banner.setUpdateUserName(currentAdmin.getUserName());
    }

    /**
     * 开启定时任务
     *
     * @param startTime
     * @param endTime
     * @param bannerId
     */
    public void startBannerEvent(Long startTime, Long endTime, Integer bannerId ) {
        Long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //使用了默认的格式创建了一个日期格式化对象。
        if (bannerId == null) {
            return;
        }
        if (currentTime < startTime) {//开始时间晚于当前时间
            Date date = new Date(startTime);
            String time = dateFormat.format(date);
            distributorBannerCmdExe.createStartEvent(time, bannerId);
        }
        if (currentTime < endTime) {//结束时间晚于当前时间
            Date date = new Date(endTime);
            String time = dateFormat.format(date);
            distributorBannerCmdExe.createEndEvent(time, bannerId);
        }
    }

  
}
