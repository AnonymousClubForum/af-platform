package org.anonymous.af.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.anonymous.af.mapper.PostMapper;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.service.CommentService;
import org.anonymous.af.service.PostService;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Resource
    private UserService userService;
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
    public IPage<PostVo> getPostPage(Long pageNum, Long pageSize, Long userId, String searchContent, Long sectionId) {
        Page<PostEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PostEntity> queryWrapper = new LambdaQueryWrapper<PostEntity>()
                .eq(userId != null, PostEntity::getUserId, userId)
                .eq(sectionId != null, PostEntity::getSectionId, sectionId);
        if (StrUtil.isNotBlank(searchContent)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(PostEntity::getTitle, searchContent)
                    .or()
                    .like(PostEntity::getContent, searchContent)
            );
        }
        queryWrapper.orderByDesc(PostEntity::getIsTop).orderByDesc(PostEntity::getUtime);
        Page<PostEntity> resultPage = baseMapper.selectPage(page, queryWrapper);

        // 找出所有对应的用户实体
        Set<Long> userIds = resultPage.getRecords().stream()
                .map(PostEntity::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserEntity> userMap = CollUtil.isEmpty(userIds) ? new HashMap<>() : userService.listByIds(userIds)
                .stream()
                .collect(Collectors.toMap(
                        UserEntity::getId, Function.identity(), (exist, replace) -> exist)
                );

        return resultPage.convert(entity -> {
            PostVo vo = new PostVo();
            // 排除content字段
            BeanUtil.copyProperties(entity, vo, "content");

            // 填充用户信息
            UserEntity userEntity = userMap.getOrDefault(entity.getUserId(), null);
            if (userEntity != null) {
                vo.setUsername(userEntity.getUsername());
                vo.setAvatarId(userEntity.getAvatarId());
            } else {
                vo.setUsername("用户已注销");
            }
            return vo;
        });
    }

    /**
     * 查询帖子详情
     */
    public PostVo getPostById(Long id) {
        PostEntity entity = baseMapper.selectById(id);
        PostVo vo = new PostVo();
        BeanUtil.copyProperties(entity, vo);

        // 填充用户信息
        UserEntity userEntity = entity.getUserId() != null ? userService.getById(entity.getUserId()) : null;
        if (userEntity != null) {
            vo.setUsername(userEntity.getUsername());
            vo.setAvatarId(userEntity.getAvatarId());
        } else {
            vo.setUsername("用户已注销");
        }
        return vo;
    }
}
