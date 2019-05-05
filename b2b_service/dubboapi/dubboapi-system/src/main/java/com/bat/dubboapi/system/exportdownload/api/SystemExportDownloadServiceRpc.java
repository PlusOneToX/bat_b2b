package com.bat.dubboapi.system.exportdownload.api;

import com.bat.dubboapi.system.exportdownload.dto.ExcelExportInfoRpcDTO;
import com.bat.dubboapi.system.common.Response;

public interface SystemExportDownloadServiceRpc {
    /**
     * 同步导出信息
     * 
     * @param dto
     * @return
     */
    Response syncExportInfo(ExcelExportInfoRpcDTO dto);
}
