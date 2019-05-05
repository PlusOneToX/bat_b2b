package com.bat.thirdparty.alibaba.dingtalk.service.executor;

import static com.bat.thirdparty.utils.StreamUtils.distinctByKey;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.DepartmentListSubResp;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.UserDetailListResp;
import com.bat.thirdparty.alibaba.dingtalk.manager.DingTalkManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 11:01
 */
@Component
@Slf4j
public class SystemDingTalkQryExc {

    @Resource
    private DingTalkManager dingTalkManager;

    /**
     * 缓存两小时 到期自动刷新
     * 
     * @return
     */
    @Cached(name = "thirdparty:system:SystemDingTalkQryExc.listDepartment", key = "#dingTalkDepartment",
        cacheType = CacheType.BOTH)
    @CacheRefresh(refresh = 7000, timeUnit = TimeUnit.SECONDS)
    public List<UserDetailListResp.ResultDTO.ListDTO> listDepartment(String dingTalkDepartment) {
        log.info("SystemDingTalkQryExc.listDepartment start");
        List<UserDetailListResp.ResultDTO.ListDTO> list = recursionListUser(1L);
        log.debug("SystemDingTalkQryExc.listDepartment list:{}", list);
        log.info("SystemDingTalkQryExc.listDepartment list.size():{}", list.size());
        List<UserDetailListResp.ResultDTO.ListDTO> collect = list.stream()
            .filter(distinctByKey(UserDetailListResp.ResultDTO.ListDTO::getUserid)).collect(Collectors.toList());
        log.debug("SystemDingTalkQryExc.listDepartment collect:{}", collect);
        log.info("SystemDingTalkQryExc.listDepartment collect.size():{}", collect.size());
        return collect;
    }

    /**
     * 递归部门
     * 
     * @param deptId
     * @return
     */
    private List<UserDetailListResp.ResultDTO.ListDTO> recursionListUser(Long deptId) {
        List<UserDetailListResp.ResultDTO.ListDTO> useridLis =
            new ArrayList<>(dingTalkManager.listUserDetail(deptId, 0L, 100L));
        DepartmentListSubResp departmentListSubResp = dingTalkManager.listDepartment(deptId);
        if (departmentListSubResp.getErrcode() == 0) {
            List<DepartmentListSubResp.ResultDTO> result = departmentListSubResp.getResult();
            result.forEach(resultDTO -> {
                List<UserDetailListResp.ResultDTO.ListDTO> listDTOS =
                    recursionListUser(Long.valueOf(resultDTO.getDeptId()));
                useridLis.addAll(listDTOS);
            });
        }
        return useridLis;
    }

}
