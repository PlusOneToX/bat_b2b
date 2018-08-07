package com.bat.system.api.storesetting.dto;

import com.bat.system.api.base.BaseSearchQry;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:16
 */
@Data
public class NoticeQry extends BaseSearchQry {
    private Short releaseStatus = 1;
}
