package com.bat.thirdparty.wx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/thirdparty/lk")
public class LinkController {

    /**
     * 链接调整
     * @param response
     * @param id
     * @throws IOException
     */
    @GetMapping
    public void response(HttpServletResponse response, @RequestParam(name = "id") Integer id) throws IOException {
        response.sendRedirect("https://icloud-8gxt9ei452797572-1303252339.tcloudbaseapp.com/jump-mp.html?sign=1cebc6c40b82b5de469049da1082d543&t=1631072913&id=" + id);
    }


}
