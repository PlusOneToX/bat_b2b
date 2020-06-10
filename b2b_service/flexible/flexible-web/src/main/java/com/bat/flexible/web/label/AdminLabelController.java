package com.bat.flexible.web.label;

import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.label.LabelServiceI;
import com.bat.flexible.api.label.dto.LabelCmd;
import com.bat.flexible.api.label.dto.LabelDetailQry;
import com.bat.flexible.api.label.dto.LabelPageQry;
import com.bat.flexible.api.util.file.FileUtils;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;

@Controller
@RequestMapping(value = "/flexible/v1/web/admin/u")
@Api(value = "AdminLabelController", tags = "标签后台管理接口")
public class AdminLabelController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLabelController.class);

    @Autowired
    private LabelServiceI labelServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLabel, value = CommonLogTypeConstantDTO.AdminLabelAdd)
    @PostMapping(value = "/p/label")
    @ApiOperation(value = "新增标签")
    @ResponseBody
    public Response createLabel(@Valid @RequestBody LabelCmd labelCmd) {
        return labelServiceI.create(labelCmd, getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLabel, value = CommonLogTypeConstantDTO.AdminLabelUpdate)
    @PutMapping(value = "/p/label")
    @ApiOperation(value = "修改标签")
    @ResponseBody
    public Response updateLabel(@Valid @RequestBody LabelCmd labelCmd) {
        return labelServiceI.update(labelCmd, getCurrentAdmin());
    }

    @GetMapping(value = "/p/label")
    @ApiOperation(value = "查询标签详情")
    @ResponseBody
    public Response<LabelDetailQry> detailById(@Valid FlexibleIdDTO flexibleIdDTO) {
        LabelDetailQry labelDetailQry = labelServiceI.detailById(flexibleIdDTO.getId());
        return Response.of(labelDetailQry);
    }

    @GetMapping(value = "/p/label/page")
    @ApiOperation(value = "分页查询标签")
    @ResponseBody
    public Response page(@Valid LabelPageQry labelPageQry) {
        return labelServiceI.page(labelPageQry);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLabel, value = CommonLogTypeConstantDTO.AdminLabelUpdateStatus)
    @PutMapping(value = "/p/label/upOpenFlag")
    @ApiOperation(value = "启用、禁用")
    @ResponseBody
    public Response up(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO) {
        return labelServiceI.upOrDown(flexibleUpdateStatusDTO, getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLabel, value = CommonLogTypeConstantDTO.AdminLabelDelete)
    @DeleteMapping(value = "/p/label")
    @ApiOperation(value = "删除")
    @ResponseBody
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        return labelServiceI.delete(flexibleIdDTO.getId(), getCurrentAdmin());
    }

    @GetMapping("/p/label/preview")
    @ApiOperation(value = "预览标签")
    public void preview(@Valid FlexibleIdDTO flexibleIdDTO, HttpServletResponse response, HttpServletRequest request) {

        try {
            LabelDO label = labelServiceI.getById(flexibleIdDTO.getId());

            System.out.println("预览标签: " + label);

            if (label == null) {
                throw new RuntimeException("标签信息不存在");
            }
            String templateFile = label.getTemplateFile();

            if (StringUtils.isBlank(templateFile)) {
                throw new RuntimeException("此标签未配置模版文件");
            }
            String folder = FileUtils.getSystemDirectory("label");
            //获取生成的文件名
            String templateFileName = FileUtils.getFileName(templateFile);
            FileUtils.download(templateFile, templateFileName, folder);
            LOGGER.info("本地路径" + folder + templateFileName);
            File file = new File(folder + templateFileName);
            if (file.exists()) {
                String userDir = System.getProperty("user.dir").concat("/label/pull/");
                File dir = new File(userDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = dir.getPath().concat("\\").concat("" + System.currentTimeMillis()).concat(".pdf");

                String basicLabel = labelServiceI.generateBasicLabel(file, label, fileName);
                String filename = file.getName();
                response.setCharacterEncoding(request.getCharacterEncoding());
                response.setContentType("application/force-download");
                // 设置信息给客户端不解析
                String type = new MimetypesFileTypeMap().getContentType(filename);
                // 设置contenttype，即告诉客户端所发送的数据属于什么类型
                response.setHeader("Content-type", type);
                // 设置编码
                String hehe = new String(filename.getBytes("utf-8"), "iso-8859-1");

                // response.setHeader(Header.CONTENT_DISPOSITION.toString(), "attachment; filename="+hehe);
                //
                response.setHeader("Content-Disposition", "attachment; filename=" + hehe);
                File outFile = new File(basicLabel);
                if (!outFile.exists()) {
                    throw new RuntimeException("生成预览文件失败");
                }
                FileInputStream inputStream = new FileInputStream(outFile);
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                byte[] bytes = new byte[1024];
                int count;
                while ((count = bis.read(bytes)) > 0) {
                    bos.write(bytes, 0, count);
                }
                bis.close();
                bos.flush();
                bos.close();
            } else {
                throw new RuntimeException("模版文件不存在");
            }

        } catch (IOException e) {
            throw new RuntimeException("异常信息:" + e.getMessage());
        }
    }
}
