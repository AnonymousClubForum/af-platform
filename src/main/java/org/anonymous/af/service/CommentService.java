package org.anonymous.af.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.anonymous.af.model.entity.CommentEntity;
import org.anonymous.af.model.request.SaveCommentRequest;
import org.anonymous.af.model.response.CommentVo;
import org.springframework.stereotype.Service;

/**
 * 评论接口
 */
@Service
public interface CommentService extends IService<CommentEntity> {
    void createComment(SaveCommentRequest request);

    IPage<CommentVo> getCommentPage(Long pageNum, Long pageSize, Long postId, Long parentId);
}