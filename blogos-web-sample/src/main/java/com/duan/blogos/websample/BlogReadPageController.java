package com.duan.blogos.websample;

import com.duan.blogos.service.OnlineService;
import com.duan.blogos.service.blog.OperateService;
import com.duan.blogos.service.blog.StatisticsService;
import com.duan.blogos.service.blogger.*;
import com.duan.blogos.service.common.dto.blog.BlogBaseStatisticsDTO;
import com.duan.blogos.service.common.dto.blog.BlogDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerAccountDTO;
import com.duan.blogos.service.common.dto.blogger.BloggerStatisticsDTO;
import com.duan.blogos.service.common.restful.ResultModel;
import com.duan.blogos.service.common.util.Utils;
import com.duan.blogos.websample.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2018/3/7.
 *
 * @author DuanJiaNing
 */
@Controller
@RequestMapping("/{bloggerNameBase64}/blog/{blogNameBase64}")
public class BlogReadPageController {

    @Autowired
    private BloggerAccountService accountService;

    @Autowired
    private BloggerBlogService blogService;

    @Autowired
    private BloggerStatisticsService bloggerStatisticsService;

    @Autowired
    private OnlineService onlineService;

    @Autowired
    private OperateService operateService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private BloggerLikeBlogService likeService;

    @Autowired
    private BloggerCollectBlogService collectBlogService;

    @RequestMapping
    public ModelAndView page(HttpServletRequest request,
                             @PathVariable String bloggerNameBase64,
                             @PathVariable String blogNameBase64) {
        ModelAndView mv = new ModelAndView();

        // 博文作者博主账户
        String bloggerName = Utils.decodeUrlBase64(bloggerNameBase64);
        BloggerAccountDTO account = accountService.getAccount(bloggerName);

        if (account == null) {
            request.setAttribute("code", 500);
            mv.setViewName("/blogger/register");
            return mv;
        }

        String blogName = Utils.decodeUrlBase64(blogNameBase64);
        Long blogId = blogService.getBlogId(account.getId(), blogName);
        if (blogId == null || blogId == -1) {
            mv.setViewName("error/error");
            mv.addObject("code", 5);
            mv.addObject("errorMsg", "博文不存在！");
            return mv;
        }

        // 博文浏览次数自增 1
        operateService.updateBlogViewCountPlus(blogId);

        ResultModel<BlogBaseStatisticsDTO> statistics = statisticsService.getBlogStatisticsCount(blogId);
        ResultModel<BlogDTO> blog = blogService.getBlog(blogId);

        mv.addObject("blogOwnerBloggerId", account.getId());
        mv.addObject("blogOwnerBloggerName", bloggerName);
        mv.addObject("blogOwnerBloggerNameBase64", bloggerNameBase64);
        mv.addObject("main", blog.getData());
        mv.addObject("stat", statistics.getData());

        // 登陆博主 id
        Long loginBloggerId = onlineService.getLoginBloggerId(Util.getToken());
        if (loginBloggerId != null && loginBloggerId != -1) {

            ResultModel<BloggerStatisticsDTO> loginBgStat = bloggerStatisticsService.getBloggerStatistics(loginBloggerId);
            mv.addObject("loginBgStat", loginBgStat.getData());

            if (likeService.getLikeState(loginBloggerId, blogId)) {
                mv.addObject("likeState", true);
            }

            if (collectBlogService.getCollectState(loginBloggerId, blogId)) {
                mv.addObject("collectState", true);
            }
        }

        mv.setViewName("blogger/read_blog");
        return mv;
    }

}
