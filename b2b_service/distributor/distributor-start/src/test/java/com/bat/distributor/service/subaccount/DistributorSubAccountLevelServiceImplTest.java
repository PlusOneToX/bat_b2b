package com.bat.distributor.service.subaccount;

import com.bat.distributor.DistributorApplicationTests;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


class DistributorSubAccountLevelServiceImplTest extends DistributorApplicationTests {

    @Resource
    private DistributorSubAccountLevelServiceImpl distributorSubAccountLevelService;

    @Test
    void save() {
        List<String> levelNameList = new ArrayList<>();
        levelNameList.add("青铜");
        levelNameList.add("白银");
        levelNameList.add("黄金");
        levelNameList.add("铂晶");
        levelNameList.add("铂晶");
        levelNameList.add("铂晶");
        levelNameList.add("测试");
        distributorSubAccountLevelService.save(false,1879,levelNameList,"10000","admin");
    }
}