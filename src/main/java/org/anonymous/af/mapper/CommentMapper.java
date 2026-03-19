package org.anonymous.af.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.anonymous.af.model.entity.CommentEntity;
import org.anonymous.af.model.response.CommentNoticeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {
    @Select("""
            SELECT c.id, c.post_id, p.title as post_title,
                   c.parent_id, parent.content as parent_content,
                   c.user_id, u.username, u.avatar_id, c.content, c.ctime
            FROM t_comment c
            LEFT JOIN t_post p ON p.id = c.post_id
            LEFT JOIN t_comment parent ON parent.id = c.parent_id
            LEFT JOIN t_user u ON u.id = c.user_id
            WHERE p.user_id = #{user_id} OR parent.user_id = #{user_id}
            ORDER BY c.ctime DESC
            """)
    IPage<CommentNoticeVo> getCommentNotificationPage(@Param("page") Page<CommentEntity> page,
                                                      @Param("user_id") Long userId);
}
