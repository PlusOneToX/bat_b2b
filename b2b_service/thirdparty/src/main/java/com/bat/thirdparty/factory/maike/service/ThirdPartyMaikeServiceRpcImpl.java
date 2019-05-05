package com.bat.thirdparty.factory.maike.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.factory.maike.common.MaikeFactoryConfig;
import com.bat.thirdparty.factory.maike.convertor.MaikeConvertor;
import com.bat.thirdparty.factory.maike.executor.MaikeCmdExe;
import com.bat.thirdparty.factory.maike.request.order.MaikeCancelCmd;
import com.bat.thirdparty.factory.maike.response.MaikeResponse;
import com.bat.thirdparty.factory.maike.validator.MaikeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.maike.api.ThirdPartyMaikeServiceRpc;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailFTPDTO;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailLocalUploadCmd;
import com.bat.dubboapi.thirdparty.maike.dto.order.PictureLocalUrlCmd;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.factory.maike.common.MaikeConfigQry;

@DubboService
public class ThirdPartyMaikeServiceRpcImpl implements ThirdPartyMaikeServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyMaikeServiceRpcImpl.class);

    @Autowired
    private MaikeConvertor maikeConvertor;

    @Resource
    private HttpUtil httpUtil;

    // FTP本地存储根目录路径
    @Value("${ftp.localFile}")
    private String rootPath;

    @Autowired
    private MaikeCmdExe maikeCmdExe;
    @Resource
    private MaikeConfigQry maikeConfigQry;

    /**
     * 向工厂取消订单 返回true表示成功、 false表示失败
     * 
     * @param factoryCode
     *            工厂代码 编码
     * @param orderId
     * @param remark
     * @return
     */
    @Override
    public Response<Boolean> cancelOrderToFactory(String factoryCode, Integer orderId, String remark) {
        MaikeCancelCmd maikeCancelCmd = new MaikeCancelCmd();
        maikeCancelCmd.setOrder_number(orderId.toString());
        maikeCancelCmd.setRemark(remark);
        MaikeFactoryConfig maikeFactoryConfig= maikeConfigQry.getTenantFactoryConfig(factoryCode);
        MaikeResponse maikeResponse =
            httpUtil.requestFor(maikeFactoryConfig.getMaikeDomain() + maikeFactoryConfig.getCancelOrderUrl(),
                HttpMethod.POST, maikeCancelCmd, MaikeResponse.class);
        if (maikeResponse != null && maikeResponse.getStatus() != null && maikeResponse.getStatus() == 1) {
            return Response.of(true);
        }
        return Response.of(false);
    }

    /**
     * 批量上传到工厂本地存储
     * 
     * @param uploadCmdList
     * @return
     */
    @Override
    public Response<List<OrderDetailFTPDTO>> updateToFTP(List<OrderDetailLocalUploadCmd> uploadCmdList) {
        try {
            MaikeValidator.validateFtpParam(uploadCmdList);
            List<OrderDetailFTPDTO> detailFTPDTOList = new ArrayList<>();
            uploadCmdList.forEach(uploadCmd -> {
                OrderDetailFTPDTO ftpdto = new OrderDetailFTPDTO();
                ftpdto.setRootPath(rootPath);
                ftpdto.setOrderGoodsDiyId(uploadCmd.getOrderGoodsDiyId());
                List<String> urlList = uploadCmd.getUrlList();
                List<PictureLocalUrlCmd> localUrlCmdList = new ArrayList<>();
                urlList.forEach(url -> {
                    String ftpFileUrl = maikeCmdExe.uploadFileToFTP(uploadCmd.getFactoryCode(), url, 0);
                    PictureLocalUrlCmd pictureLocalUrlCmd = new PictureLocalUrlCmd();
                    pictureLocalUrlCmd.setLocalUrl(ftpFileUrl);
                    pictureLocalUrlCmd.setSourceUrl(url);
                    localUrlCmdList.add(pictureLocalUrlCmd);
                });
                ftpdto.setLocalUrlCmdList(localUrlCmdList);
                detailFTPDTOList.add(ftpdto);
            });
            return Response.of(detailFTPDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), StringUtils.isBlank(MessageUtils.get(e.getMessage()))
                ? e.getMessage() : MessageUtils.get(e.getMessage()));
        }
    }
}
