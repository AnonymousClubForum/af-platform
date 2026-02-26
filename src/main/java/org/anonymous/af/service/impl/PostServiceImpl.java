package org.anonymous.af.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.anonymous.af.mapper.PostMapper;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.service.CommentService;
import org.anonymous.af.service.PostService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Resource
    private CommentService commentService;

    /**
     * 新增帖子
     */
    public void createPost(SavePostRequest request) {
        PostEntity postEntity = new PostEntity();
        BeanUtil.copyProperties(request, postEntity, true);
        postEntity.setId(IdWorker.getId());
        postEntity.setUserId(UserContextUtil.getUserId());
        baseMapper.insert(postEntity);
    }

    /**
     * 更新帖子
     */
    public void updatePost(SavePostRequest request) {
        PostEntity postEntity = baseMapper.selectById(request.getId());
        BeanUtil.copyProperties(request, postEntity, true);
        baseMapper.updateById(postEntity);
    }

    /**
     * 删除帖子
     */
    @Transactional
    public void deletePost(Long id) {
        baseMapper.deleteById(id);
        commentService.deleteCommentByPost(id);
    }

    /**
     * 分页查询帖子
     */
    public IPage<PostVo> getPostPage(Long pageNum, Long pageSize, Long userId, String searchContent) {
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
        queryWrapper.orderByDesc(PostEntity::getUtime);
        return baseMapper.selectPage(page, queryWrapper).convert(entity -> {
            String content = entity.getContent();
            if (content.length() > 100) {
                entity.setContent(content.substring(0, 100));
            }
            return new PostVo(entity);
        });
    }

    /**
     * 查询帖子详情
     */
    public PostVo getPostById(Long id) {
        PostEntity postEntity = baseMapper.selectById(id);
        return new PostVo(postEntity);
    }
}
