package com.bat.thirdparty.xxljob.api.response;

import com.bat.thirdparty.xxljob.api.response.dto.XxlJobDTO;
import lombok.Data;

import java.util.List;

@Data
public class XxlJobListResponse extends XxlJobResponse {
    private List<XxlJobDTO> data;
    private Integer recordsFiltered;
    private Integer recordsTotal;

}
