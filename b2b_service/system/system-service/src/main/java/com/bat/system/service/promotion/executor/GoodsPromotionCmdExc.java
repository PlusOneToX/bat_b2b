package com.bat.system.service.promotion.executor;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.promotion.GoodsPromotionId;
import com.bat.system.api.promotion.GoodsPromotionListQry;
import com.bat.system.api.promotion.dto.GoodPromotionId;
import com.bat.system.api.promotion.dto.GoodsPromotionCmd;
import com.bat.system.api.promotion.dto.GoodsPromotionCreateCmd;
import com.bat.system.api.promotion.dto.data.GoodsPromotionDTO;
import com.bat.system.api.promotion.dto.data.GoodsPromotionDistributorScopeDTO;
import com.bat.system.dao.promotion.GoodsPromotionMapper;
import com.bat.system.dao.promotion.dataobject.DistributorGoodsPromotionRelevanceDO;
import com.bat.system.dao.promotion.dataobject.GoodsPromotionDO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.system.api.base.SystemException;
import com.bat.system.service.common.CommonRpcQryExe;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/26 19:54
 */
@Component
public class GoodsPromotionCmdExc {
    @Resource
    GoodsPromotionMapper goodsPromotionMapper;

    @Resource
    private CommonRpcQryExe commonRpcQryExe;

    @Resource
    private GoodsPromotionRpcCmdExe rpcCmdExe;

    public PageInfo<GoodsPromotionCmd> goodsPromotionList(GoodsPromotionListQry qry) {
        List<GoodsPromotionDO> goodsPromotionDOS =
            goodsPromotionMapper.goodsPromotionList((qry.getPage() - 1) * qry.getSize(), qry.getSize());

        // 给商品推广状态进行赋值
        List<GoodsPromotionCmd> goodsPromotionCmds = goodsPromotionStatus(goodsPromotionDOS);

        PageInfo<GoodsPromotionCmd> pageInfo = new PageInfo<>(goodsPromotionCmds);
        return pageInfo;
    }

    /**
     * <h2>添加商品推广</h2>
     * 
     * @param cmd
     */
    public void addGoodsPromotion(GoodsPromotionCreateCmd cmd) {
        GoodsPromotionDO goodsPromotionDO = new GoodsPromotionDO();

        BeanUtils.copyProperties(cmd, goodsPromotionDO);
        goodsPromotionDO.setCreateTime(new Date());
        goodsPromotionDO.setUpdateTime(new Date());

        // 添加商品推广
        goodsPromotionMapper.addGoodsPromotion(goodsPromotionDO);
        // 保存推广与可视关系
        saveGoodsScope(goodsPromotionDO.getId(), cmd);
    }

    /**
     * <h2>删除该商品的所有可视</h2>
     *
     */
    private void deleteGoodsPromotionScope(GoodsPromotionDO goodsPromotionDO) {
        goodsPromotionMapper.deleteGoodsScalePriceRelevanceList(goodsPromotionDO.getId());
        goodsPromotionMapper.deleteGoodsDistributorRelevanceList(goodsPromotionDO.getId());
        goodsPromotionMapper.deleteGoodsDepartmentRelevanceList(goodsPromotionDO.getId());
        goodsPromotionMapper.deleteGoodsAdminRelevanceList(goodsPromotionDO.getId());
    }

    /**
     * <h2>保存推广商品可视</h2>
     */
    private void saveGoodsScope(Integer id, GoodsPromotionCreateCmd cmd) {

        // 查询出最新的那条推广记录
        List<GoodsPromotionDO> promotionDOTwo = goodsPromotionMapper.queryGoodsPromotionDescTwo();
        if (promotionDOTwo.size() > 1) {
            // 最近一条记录的结束时间
            Calendar date = Calendar.getInstance();
            date.setTime(promotionDOTwo.get(1).getExtensionEndTime());

            // 新增记录的开始时间
            Calendar begin = Calendar.getInstance();
            begin.setTime(promotionDOTwo.get(0).getExtensionStartTime());

            // 当前时间
            Calendar currentTime = Calendar.getInstance();
            currentTime.setTime(new Date());

            // 如果新增的开始时间在上一条记录的结束时间之前或者当前时间在新增开始时间之后，则有问题
            if (begin.before(date) || begin.equals(date) || currentTime.after(begin) || currentTime.equals(begin)) {
                throw SystemException.buildException(ErrorCode.B_GOODS_PROMOTION_DATE_BEFORE);
            }
        }

        Short distributorScope = cmd.getDistributorScope();

        if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
            goodsPromotionMapper.createGoodsScalePriceRelevanceList(id, cmd.getScalePriceIds());
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
            goodsPromotionMapper.createGoodsDistributorRelevanceList(id, cmd.getDistributorIds());
        } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
            goodsPromotionMapper.createGoodsDepartmentRelevanceList(id, cmd.getDepartmentIds());
        } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
            goodsPromotionMapper.createGoodsAdminRelevanceList(id, cmd.getAdminIds());
        }

        // 更新分销商商品可视数据
        rpcCmdExe.distributorGoodsRelevance(id, cmd.getDistributorScope(), cmd.getScalePriceIds(),
            cmd.getDistributorIds(), cmd.getDepartmentIds(), cmd.getAdminIds());
    }

    public GoodsPromotionDTO getGoodsPromotion(GoodPromotionId qry) {
        // 根据id查询商品推广信息
        GoodsPromotionDO goodsPromotion = goodsPromotionMapper.getGoodsPromotion(qry.getId());

        if (ObjectUtils.isEmpty(goodsPromotion)) {
            throw SystemException.buildException(ErrorCode.B_GOODSPROMOTIONDO_NOTNULL);
        }

        GoodsPromotionDTO goodsPromotionDTO = new GoodsPromotionDTO();
        BeanUtils.copyProperties(goodsPromotion, goodsPromotionDTO);
        // 根据商品推广记录id查询可视人员信息
        getGoodsScopeIds(goodsPromotionDTO);
        return goodsPromotionDTO;
    }

    private void getGoodsScopeIds(GoodsPromotionDTO goodsPromotionDTO) {
        // 取出商品推广id和商品可视范围标识
        Integer goodsId = goodsPromotionDTO.getId();
        Short distributorScope = goodsPromotionDTO.getDistributorScope();

        if (goodsPromotionDTO.getDistributorScope() == null) {
            return;
        }
        // 根据商品推广可视范围标识去获取不同范围表内的信息
        if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
            goodsPromotionDTO.setScalePriceIds(goodsPromotionMapper.listGoodsScalePriceRelevanceId(goodsId));
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorIds = goodsPromotionMapper.listGoodsDistributorRelevanceId(goodsId);

            List<DistributorNameRpcDTO> distributorNameByDistributorIds =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorIds);

            List<GoodsPromotionDistributorScopeDTO> goodsPromotionDistributorScopeDTOS = new ArrayList<>();
            distributorNameByDistributorIds.forEach(distributorNameRpcDTO -> {
                GoodsPromotionDistributorScopeDTO goodsPromotionDistributorScopeDTO =
                    new GoodsPromotionDistributorScopeDTO();
                goodsPromotionDistributorScopeDTO.setDistributorId(distributorNameRpcDTO.getId());
                goodsPromotionDistributorScopeDTO.setName(distributorNameRpcDTO.getName());
                goodsPromotionDistributorScopeDTO.setCompanyName(distributorNameRpcDTO.getCompanyName());
                goodsPromotionDistributorScopeDTOS.add(goodsPromotionDistributorScopeDTO);
            });
            goodsPromotionDTO.setDistributors(goodsPromotionDistributorScopeDTOS);
        } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
            goodsPromotionDTO.setDepartmentIds(goodsPromotionMapper.listGoodsDepartmentRelevanceId(goodsId));
        } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
            goodsPromotionDTO.setAdminIds(goodsPromotionMapper.listGoodsAdminRelevanceId(goodsId));
        }
    }

    /**
     * <h2>根据用户id查询用户是否在可视范围内</h2>
     *
     * @param userId
     */
    public GoodsPromotionDTO promotionsquery(Integer userId) {
        // 查询该用户最大的分销商
        Integer distributorMaxId = rpcCmdExe.goodsPromotionMaximumDistributor(userId);

        // 如果等于空，则说明是一级分销商,返回的就是一级分销商的ID
        if (distributorMaxId == null) {
            distributorMaxId = userId;
        }

        List<DistributorGoodsPromotionRelevanceDO> promotionsquerys =
            goodsPromotionMapper.promotionsquery(distributorMaxId);

        List<GoodsPromotionDO> goodsPromotionDOList = null;

        if (promotionsquerys.size() == 0) {
            goodsPromotionDOList = goodsPromotionMapper.getGoodsPromotionDesc();
        } else {
            // 根据分销商可视表查询出该分销商所有可视的商品推广的id
            List<Integer> goodsPromotionIdList = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(promotionsquerys)) {
                promotionsquerys.forEach(distributorGoodsPromotionRelevanceDO -> {
                    String goodsPromotionId = distributorGoodsPromotionRelevanceDO.getGoodsPromotionId();
                    goodsPromotionId = goodsPromotionId.substring(1, goodsPromotionId.length() - 1);
                    String[] split = goodsPromotionId.split(",");
                    for (String id : split) {
                        goodsPromotionIdList.add(Integer.parseInt(id));
                    }
                });
            }

            // 根据商品推广id查询所有记录
            goodsPromotionDOList = goodsPromotionMapper.getGoodsPromotionListByIds(goodsPromotionIdList);

            if (ObjectUtils.isEmpty(goodsPromotionDOList)) {
                return null;
            }
        }

        // 计算商品推广的状态
        List<GoodsPromotionCmd> goodsPromotionCmds = goodsPromotionStatus(goodsPromotionDOList);

        // 过滤出商品状态为进行中的
        List<GoodsPromotionCmd> inprogress = goodsPromotionCmds.stream().filter(x -> x.getGoodsPromotionStatus() == 1)
            .sorted(Comparator.comparing(GoodsPromotionCmd::getExtensionStartTime)).collect(Collectors.toList());
        GoodsPromotionDTO goodsPromotionDTO = new GoodsPromotionDTO();
        List<GoodsPromotionDO> collect = goodsPromotionDOList.stream()
            .filter(x -> x.getId().equals(inprogress.get(0).getId())).collect(Collectors.toList());

        if (ObjectUtils.isNotEmpty(collect)) {
            BeanUtils.copyProperties(collect.get(0), goodsPromotionDTO);
        }
        return goodsPromotionDTO;
    }

    /**
     * <h2>计算商品推广的状态</h2>
     * 
     * @param goodsPromotionDOList
     * @return
     */
    public List<GoodsPromotionCmd> goodsPromotionStatus(List<GoodsPromotionDO> goodsPromotionDOList) {
        List<GoodsPromotionCmd> resultList = new ArrayList<>();

        goodsPromotionDOList.forEach(goodsPromotionDO -> {
            GoodsPromotionCmd goodsPromotionCmd = new GoodsPromotionCmd();

            BeanUtils.copyProperties(goodsPromotionDO, goodsPromotionCmd);

            if (goodsPromotionDO.getStatus() == 0) {
                // 获取当前时间
                Calendar date = Calendar.getInstance();
                date.setTime(new Date());

                // 活动开始时间
                Calendar begin = Calendar.getInstance();
                begin.setTime(goodsPromotionDO.getExtensionStartTime());

                // 活动结束时间
                Calendar end = Calendar.getInstance();
                end.setTime(goodsPromotionDO.getExtensionEndTime());

                // 未开始
                if (date.before(begin)) {
                    goodsPromotionCmd.setGoodsPromotionStatus(0);
                }

                // 已结束
                if (date.after(end)) {
                    goodsPromotionCmd.setGoodsPromotionStatus(2);
                }

                // 进行中
                if (date.after(begin) && date.before(end)) {
                    goodsPromotionCmd.setGoodsPromotionStatus(1);
                }
            } else {
                goodsPromotionCmd.setGoodsPromotionStatus(3);
            }
            resultList.add(goodsPromotionCmd);
        });
        return resultList;
    }

    /**
     * <h2>修改商品推广</h2>
     *
     * @param cmd
     */
    public void updateGoodsPromotion(GoodsPromotionCreateCmd cmd) {
        // 根据商品推广id查询商品推广信息
        // GoodsPromotionDO goodsPromotionDO = goodsPromotionMapper.getGoodsPromotion(cmd.getId());
        GoodsPromotionDO goodsPromotionDO = new GoodsPromotionDO();
        BeanUtils.copyProperties(cmd, goodsPromotionDO);

        // 修改商品推广
        goodsPromotionMapper.updateGoodsPromotion(goodsPromotionDO);
        // 删除该商品的所有可视
        deleteGoodsPromotionScope(goodsPromotionDO);
        // 保存推广与可视关系
        saveGoodsScope(goodsPromotionDO.getId(), cmd);
    }

    /**
     * <h2>商品推广记录删除</h2>
     * 
     * @param cmd
     */
    public void deleteGoodsPromotion(GoodsPromotionId cmd) {
        GoodsPromotionDO goodsPromotion = goodsPromotionMapper.getGoodsPromotion(cmd.getId());
        if (goodsPromotion == null) {
            throw SystemException.buildException(ErrorCode.B_GOODSPROMOTION_DELETE_ERROR);
        }
        // 删除记录
        goodsPromotionMapper.deleteGoodsPromotionById(cmd.getId());
        // 删除对应关系
        deleteGoodsPromotionScope(goodsPromotion);
        // 删除可视关系
        goodsPromotionMapper.deleteRelevance("," + cmd.getId() + ",");
    }

    /**
     * <h2>商品推广记录作废</h2>
     * 
     * @param cmd
     */
    public void invalidGoodsPromotion(GoodsPromotionId cmd) {
        GoodsPromotionDO goodsPromotion = goodsPromotionMapper.getGoodsPromotion(cmd.getId());
        if (goodsPromotion == null) {
            throw SystemException.buildException(ErrorCode.B_GOODSPROMOTION_DELETE_ERROR);
        }
        goodsPromotionMapper.invalidGoodsPromotion(cmd.getId());
    }
}
