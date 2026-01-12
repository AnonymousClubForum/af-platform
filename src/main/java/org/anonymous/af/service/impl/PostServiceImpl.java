package org.anonymous.af.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.anonymous.af.mapper.PostMapper;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.QueryPostPageRequest;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.model.response.SimplePostVo;
import org.anonymous.af.service.PostService;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Resource
    private UserService userService;

    /**
     * 新增帖子
     */
    public void createPost(SavePostRequest request) {
        PostEntity postEntity = new PostEntity();
        BeanUtil.copyProperties(request, postEntity, true);
        postEntity.setId(IdWorker.getId());
        postEntity.setUserId(UserContextUtil.getUser().getId());
        this.save(postEntity);
    }

    /**
     * 更新帖子
     */
    public void updatePost(SavePostRequest request) {
        PostEntity postEntity = this.getById(request.getId());
        BeanUtil.copyProperties(request, postEntity, true);
        this.save(postEntity);
    }

    /**
     * 分页查询帖子
     */
    public Page<SimplePostVo> getPostPage(QueryPostPageRequest request) {
        Page<PostEntity> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<PostEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getUsername())) {
            LambdaQueryWrapper<UserEntity> userEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userEntityLambdaQueryWrapper.like(UserEntity::getUsername, request.getUsername());
            List<UserEntity> userEntityList = userService.list(userEntityLambdaQueryWrapper);
            queryWrapper.in(CollUtil.isNotEmpty(userEntityList), PostEntity::getUserId, userEntityList);
        }
        queryWrapper.like(StrUtil.isNotBlank(request.getTitle()), PostEntity::getTitle, request.getTitle());
        queryWrapper.like(StrUtil.isNotBlank(request.getContent()), PostEntity::getContent, request.getContent());
        Page<PostEntity> postPage = this.page(page, queryWrapper);
        Page<SimplePostVo> postVoPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        postVoPage.setRecords(postPage.getRecords().stream().map((entity) -> {
            SimplePostVo vo = new SimplePostVo();
            BeanUtil.copyProperties(entity, vo, true);
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
        return postVo;
    }
}
