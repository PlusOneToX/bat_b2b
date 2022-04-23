package com.platform;

import com.platform.modules.job.task.BonusTask;
import com.platform.modules.job.task.OrderPreTask;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.MailService;
import com.platform.modules.utils.WebHttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlatformAdminApplicationTests {
    @Autowired
    private MailService mailService;
    @Autowired
    private WebHttpUtil webHttpUtil;
    @Autowired
    private MallUserDao mallUserDao;
    @Autowired
    private OrderPreTask orderPreTask;
    @Autowired
    private BonusTask bonusTask;

    @Test
    public void testHtmlMail() {
       /* String content = "<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件1111111!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendMail("lipengjun.3713469@163.com,740129569@qq.com", "test html mail", content);*/
//        bonusTask.gainBonus();
//        orderPreTask.exect();
        orderPreTask.exect1();
    }

    @Test
    public void sendAttachmentsMail() {
        String filePath = "C:\\uddi-address.xml";
        mailService.sendAttachmentsMail("lipengjun.3713469@163.com,740129569@qq.com", "主题：带附件的邮件", "有附件，请查收！", filePath);
    }

    @Test
    public void sendInlineResourceMail() {
        String rscId = "neo006";
        String content = "<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        String imgPath = "F:\\image\\IMG_4049.JPG";

        mailService.sendInlineResourceMail("lipengjun.3713469@163.com,740129569@qq.com", "主题：这是有图片的邮件", content, imgPath, rscId);
    }


}
