package com.bat.thirdparty.erp.api.request;

import java.util.List;


public class InvalidOrderRequest {
//client.ExcuteOperation("SAL_OUTSTOCK","Cancel","{"CreateOrgId":0,"Numbers":[],"Ids":""}");

    //    private String FormId = "SAL_OUTSTOCK";
//    private String opNumber = "Cancel";
//    private Data data;
    String CreateOrgId;
    List<String> Numbers;
    String Ids;

    public InvalidOrderRequest(String s, List<String> numbers, String s1) {
        super();
    }

    public String getCreateOrgId() {
        return CreateOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        CreateOrgId = createOrgId;
    }

    public List<String> getNumbers() {
        return Numbers;
    }

    public void setNumbers(List<String> numbers) {
        Numbers = numbers;
    }

    public String getIds() {
        return Ids;
    }

    public void setIds(String ids) {
        Ids = ids;
    }
}
