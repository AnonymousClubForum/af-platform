package org.anonymous.af.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.model.response.SimplePostVo;
import org.springframework.stereotype.Service;

/**
 * 帖子接口
 */
@Service
public interface PostService extends IService<PostEntity> {
    void createPost(SavePostRequest request);

    void updatePost(SavePostRequest request);

    IPage<SimplePostVo> getPostPage(Long pageNum, Long pageSize, Long userId, String searchContent);

    PostVo getPostById(Long id);
}