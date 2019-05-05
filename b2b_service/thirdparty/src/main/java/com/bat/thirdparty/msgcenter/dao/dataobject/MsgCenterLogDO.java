package com.bat.thirdparty.msgcenter.dao.dataobject;

import lombok.Data;

import java.util.Date;
@Data
public class MsgCenterLogDO {

    private Integer id;

    private Integer centerId;

    private Short channel;

    private String title;

    private String miniLink;

    private String pcLink;

    private Short pushSwitch;

    private Short pushFlag;

    private Short readFlag;

    private Short pushTerminal;

    private Short msgType;

    private Integer toUserId;

    private String toUsername;

    private Short userType;

    private String openId;

    private String mobile;

    private String phone;

    private Date createTime;

    private Date readTime;

    private Date updateTime;

    private String content;

    private String miniBody;

    private String sendFailError;


}