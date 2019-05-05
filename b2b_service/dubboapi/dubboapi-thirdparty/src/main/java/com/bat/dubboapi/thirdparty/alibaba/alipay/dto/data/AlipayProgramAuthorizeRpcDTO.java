package com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/12/4 20:29
 */
@Data
public class AlipayProgramAuthorizeRpcDTO implements Serializable {
    private String userId;
    private String avatar;
    private String province;
    private String city;
    private String nickName;
    private String gender;
    private String mobile;
    private String phone;
}
