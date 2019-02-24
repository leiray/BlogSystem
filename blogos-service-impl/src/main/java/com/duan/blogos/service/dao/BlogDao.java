package com.duan.blogos.service.dao;

import com.duan.blogos.service.common.enums.BlogSortRule;
import com.duan.blogos.service.common.enums.BlogStatusEnum;
import com.duan.blogos.service.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2017/12/12.
 * 对博文的CRUD
 *
 * @author DuanJiaNing
 */
@Repository
public interface BlogDao extends BaseDao<Blog> {

    /**
     * 根据博主id和文章状态查询其发布的文章
     *
     * @param bloggerId 博主id
     * @param status    博文状态
     * @return 查询到的文章
     * @see BlogStatusEnum
     */
    List<Blog> listBlog(@Param("bloggerId") Long bloggerId,
                        @Param("status") int status);

    /**
     * 根据博主id和文章状态查询其发布的指定偏移位置和数量的博文
     *
     * @param bloggerId 博主id
     * @param status    博文状态
     * @param offset    偏移位置
     * @param rows      行数
     * @return
     * @see BlogStatusEnum
     */
    List<Blog> listBlogWithLimit(@Param("bloggerId") Long bloggerId,
                                 @Param("status") int status,
                                 @Param("offset") int offset,
                                 @Param("rows") int rows);

    /**
     * 查询出所有的博文
     *
     * @return
     */
    List<Blog> listAll();

    /**
     * 根据博文id查询博文
     *
     * @param ids    博文id
     * @param status 博文状态
     * @return 查询结果
     */
    List<Blog> listBlogByBlogIds(@Param("ids") List<Long> ids, @Param("status") int status,
                                 @Param("sortRule") BlogSortRule sortRule);

    /**
     * 查询博文id
     *
     * @param bloggerId 博主id
     * @param title     博文标题（同一博主的标题不能重复）
     * @return 查询结果
     */
    Long getBlogIdByUniqueKey(@Param("bloggerId") Long bloggerId,
                              @Param("title") String title);

    /**
     * 通过博文id查询博文
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Blog getBlogById(Long blogId);

    /**
     * 通过博文id查询博文id，该方法只为检查博文是否存在
     *
     * @param blogId 博文id
     * @return 查询结果
     */
    Integer getBlogIdById(Long blogId);

    /**
     * 查询出指定博主的所有博文包含的标签
     *
     * @param bloggerId 博主id
     * @return 只查询了博文标签和博文id的结果集
     */
    List<Blog> listAllLabelByBloggerId(Long bloggerId);

    /**
     * 统计指定博主的博文数量
     *
     * @param bloggerId 博主id
     * @return 数量
     */
    Integer countBlogByBloggerId(@Param("bloggerId") Long bloggerId,
                                 @Param("state") int state);

    /**
     * 查询指定博主的所有博文，限定查询博文的内容为 md 或 html。
     *
     * @param bloggerId 博主id
     * @param format    md 或 html
     * @return 查询结果，format 为 md 时只查询 content_md，为 html 时只查询 content
     */
    List<Blog> listAllByFormat(@Param("bloggerId") Long bloggerId,
                               @Param("format") int format);

    /**
     * 查询博主的所有博文的id
     *
     * @param bloggerId 博主id
     * @return 查询结果，只查询 id
     */
    List<Blog> listAllIdByBloggerId(Long bloggerId);
}
