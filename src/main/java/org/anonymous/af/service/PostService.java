package org.anonymous.af.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.request.QueryPostPageRequest;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.model.response.SimplePostVo;

/**
 * 帖子接口
 */
public interface PostService extends IService<PostEntity> {
    void createPost(SavePostRequest request);

    void updatePost(SavePostRequest request);

    void updateUsername(String oldUsername, String newUsername);

    Page<SimplePostVo> getPostPage(QueryPostPageRequest request);

    PostVo getPostById(Long id);
}