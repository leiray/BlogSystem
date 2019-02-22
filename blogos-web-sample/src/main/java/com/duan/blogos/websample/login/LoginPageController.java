package com.duan.blogos.websample.login;

import com.alibaba.dubbo.config.annotation.Reference;
import com.duan.blogos.service.dto.blogger.BloggerBriefDTO;
import com.duan.blogos.service.service.website.WebSiteStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created on 2018/2/8.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/login")
public class LoginPageController {

    @Reference
    private WebSiteStatisticsService webSiteStatisticsService;

    @RequestMapping
    public ModelAndView loginPage(@RequestParam(value = "activeBloggerCount", required = false) Integer activeCount) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("blogger/login");

        List<BloggerBriefDTO> dtos = webSiteStatisticsService.listActiveBlogger(activeCount == null ? -1 : activeCount);
        if (!CollectionUtils.isEmpty(dtos)) {
            mv.addObject("briefs", dtos);
        }

        return mv;
    }
}