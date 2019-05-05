package com.bat.promotion.api.groupseckill;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillListDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.groupseckill.dto.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:25
 */
public interface GroupSeckillServiceI {
    /**
     * 根据搜索条件查找拼团秒杀列表(分页)
     *
     * @param qry
     * @return
     */
    PageInfo<GroupSeckillListDTO> listGroupSeckill(GroupSeckillListQry qry);

    /**
     * 新增拼团秒杀活动
     *
     * @param cmd
     */
    void createGroupSeckill(GroupSeckillCmd cmd, String userId, String userName);

    /**
     * 修改拼团秒杀活动(草稿状态的拼团秒杀活动修改)
     *
     * @param cmd
     */
    void updateGroupSeckill(GroupSeckillCmd cmd, String userId, String userName);

    /**
     * 根据id查询拼团秒杀活动详情
     *
     * @param qry
     * @return
     */
    GroupSeckillDTO getGroupSeckill(BaseId qry);

    /**
     * 根据id删除拼团秒杀活动
     *
     * @param cmd
     */
    void deleteGroupSeckill(BaseId cmd);

    /**
     * 根据拼团秒杀活动id变更状态
     * 
     * @param cmd
     */
    void updateGroupSeckillStatus(GroupSeckillStatusCmd cmd);

    /**
     * 拼团秒杀活动排序更新
     * 
     * @param cmds
     */
    void updateGroupSeckillSort(List<GroupSeckillSortCmd> cmds);

    /**
     * 拼团秒杀商品排序更新
     * 
     * @param cmds
     */
    void updateGroupSeckillGoodsSort(List<GroupSeckillGoodsSortCmd> cmds);
}
