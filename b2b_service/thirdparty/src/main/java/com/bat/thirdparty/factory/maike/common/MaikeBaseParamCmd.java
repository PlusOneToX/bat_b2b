package com.bat.thirdparty.factory.maike.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MaikeBaseParamCmd {
    /**
     * 密钥
     */

    public static String secret;

    /**
     * 公司 id
     */
    public static Integer company_id;

    @Value("${maike.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${maike.company.id:0}")
    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public String getSecret() {
        return secret;
    }

    public Integer getCompany_id() {
        return company_id;
    }

}