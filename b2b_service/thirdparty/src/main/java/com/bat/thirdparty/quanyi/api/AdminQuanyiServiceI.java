package com.bat.thirdparty.quanyi.api;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiCancelCmd;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiLogQry;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiQry;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiUnCancelCmd;
import com.bat.thirdparty.quanyi.dao.co.ThirdQuanyiLogPageCO;
import com.bat.thirdparty.quanyi.dao.co.ThirdQuanyiPageCO;

import java.util.List;

public interface AdminQuanyiServiceI {

    PageInfo<List<ThirdQuanyiPageCO>> page(ThirdQuanyiQry thirdQuanyiQry);

    PageInfo<List<ThirdQuanyiLogPageCO>> logPage(ThirdQuanyiLogQry thirdQuanyiLogQry);

    void cancel(ThirdQuanyiCancelCmd cmd);

    void unCancel(ThirdQuanyiUnCancelCmd cmd);
}
