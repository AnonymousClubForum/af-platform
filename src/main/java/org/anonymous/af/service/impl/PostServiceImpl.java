package org.anonymous.af.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.anonymous.af.mapper.PostMapper;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.model.response.SimplePostVo;
import org.anonymous.af.service.PostService;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Resource
    private UserService userService;

    /**
     * 新增帖子
     */
    public Long createPost(SavePostRequest request) {
        PostEntity postEntity = new PostEntity();
        BeanUtil.copyProperties(request, postEntity, true);
        postEntity.setId(IdWorker.getId());
        postEntity.setUserId(UserContextUtil.getUserId());
        this.save(postEntity);
        return postEntity.getId();
    }

    /**
     * 更新帖子
     */
    public Long updatePost(SavePostRequest request) {
        PostEntity postEntity = this.getById(request.getId());
        BeanUtil.copyProperties(request, postEntity, true);
        this.updateById(postEntity);
        return postEntity.getId();
    }

    /**
     * 分页查询帖子
     */
    public Page<SimplePostVo> getPostPage(Long pageNum, Long pageSize, Long userId, String searchContent) {
        Page<PostEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PostEntity> queryWrapper = new LambdaQueryWrapper<PostEntity>()
                .eq(userId != null, PostEntity::getUserId, userId);
        if (StrUtil.isNotBlank(searchContent)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(PostEntity::getTitle, searchContent)
                    .or()
                    .like(PostEntity::getContent, searchContent)
            );
        }
        Page<PostEntity> postPage = this.page(page, queryWrapper);
        Page<SimplePostVo> postVoPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        postVoPage.setRecords(postPage.getRecords().stream().map((entity) -> {
            SimplePostVo vo = new SimplePostVo();
            BeanUtil.copyProperties(entity, vo, true);
            UserEntity userEntity = userService.getById(entity.getUserId());
            vo.setUserId(userEntity != null ? userEntity.getId() : -1);
            vo.setUsername(userEntity != null ? userEntity.getUsername() : "用户已注销");
            return vo;
        }).toList());
        return postVoPage;
    }

    /**
     * 查询帖子详情
     */
    public PostVo getPostById(Long id) {
        PostEntity postEntity = this.getById(id);
        PostVo postVo = new PostVo();
        BeanUtil.copyProperties(postEntity, postVo, true);
        UserEntity userEntity = userService.getById(postEntity.getUserId());
        postVo.setUserId(userEntity != null ? userEntity.getId() : -1);
        postVo.setUsername(userEntity != null ? userEntity.getUsername() : "用户已注销");
        return postVo;
    }
}
