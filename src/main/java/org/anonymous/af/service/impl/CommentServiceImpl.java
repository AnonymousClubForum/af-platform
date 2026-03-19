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
import org.anonymous.af.mapper.CommentMapper;
import org.anonymous.af.model.entity.CommentEntity;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SaveCommentRequest;
import org.anonymous.af.model.response.CommentNoticeVo;
import org.anonymous.af.model.response.CommentVo;
import org.anonymous.af.service.CommentService;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    @Resource
    private UserService userService;

    /**
     * 新增评论
     */
    public void createComment(SaveCommentRequest request) {
        CommentEntity commentEntity = new CommentEntity();
        BeanUtil.copyProperties(request, commentEntity, true);
        commentEntity.setId(IdWorker.getId());
        commentEntity.setUserId(UserContextUtil.getUserId());
        baseMapper.insert(commentEntity);
    }

    /**
     * 删除评论
     */
    public void deleteCommentByPost(Long postId) {
        baseMapper.delete(new LambdaQueryWrapper<CommentEntity>().eq(CommentEntity::getPostId, postId));
    }

    /**
     * 分页查询评论
     */
    public IPage<CommentVo> getCommentPage(Long pageNum, Long pageSize, Long postId, Long userId, Boolean isDesc) {
        Page<CommentEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommentEntity> queryWrapper = new LambdaQueryWrapper<CommentEntity>()
                .eq(postId != null, CommentEntity::getPostId, postId)
                .eq(userId != null, CommentEntity::getUserId, userId);
        if (isDesc != null && isDesc) {
            queryWrapper.orderByDesc(CommentEntity::getCtime);
        } else {
            queryWrapper.orderByAsc(CommentEntity::getCtime);
        }
        Page<CommentEntity> resultPage = baseMapper.selectPage(page, queryWrapper);

        Set<Long> parentIds = resultPage.getRecords().stream()
                .map(CommentEntity::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, CommentEntity> commentMap = CollUtil.isEmpty(parentIds) ? new HashMap<>() : baseMapper.selectByIds(parentIds)
                .stream()
                .collect(Collectors.toMap(
                        CommentEntity::getId, Function.identity(), (exist, replace) -> exist)
                );

        // 找出所有对应的用户实体
        Set<Long> userIds = new HashSet<>();
        for (CommentEntity commentEntity : resultPage.getRecords()) {
            userIds.add(commentEntity.getUserId());
        }
        for (CommentEntity commentEntity : commentMap.values()) {
            userIds.add(commentEntity.getUserId());
        }
        userIds.remove(null);

        Map<Long, UserEntity> userMap = CollUtil.isEmpty(userIds) ? new HashMap<>() : userService.listByIds(userIds)
                .stream()
                .collect(Collectors.toMap(
                        UserEntity::getId, Function.identity(), (exist, replace) -> exist)
                );

        return resultPage.convert(entity -> {
            CommentVo vo = new CommentVo();
            BeanUtil.copyProperties(entity, vo);

            // 填充用户信息
            UserEntity userEntity = userMap.getOrDefault(entity.getUserId(), null);
            if (userEntity != null) {
                vo.setUsername(userEntity.getUsername());
                vo.setAvatarId(userEntity.getAvatarId());
            } else {
                vo.setUsername("用户已注销");
            }
            if (entity.getParentId() != null) {
                CommentVo.ParentCommentVo parentCommentVo = new CommentVo.ParentCommentVo();
                CommentEntity parentCommentEntity = commentMap.getOrDefault(entity.getParentId(), null);
                if (parentCommentEntity == null) {
                    parentCommentVo.setContent("评论已删除");
                } else {
                    BeanUtil.copyProperties(parentCommentEntity, parentCommentVo);
                    UserEntity parentUserEntity = userMap.getOrDefault(parentCommentEntity.getUserId(), null);
                    parentCommentVo.setUsername(parentUserEntity != null ? parentUserEntity.getUsername() : "用户已注销");
                }
                vo.setParentComment(parentCommentVo);
            }
            return vo;
        });
    }

    /**
     * 分页查询某个用户收到的评论
     */
    public IPage<CommentNoticeVo> getNotificationPage(Long pageNum, Long pageSize, Long userId) {
        IPage<CommentNoticeVo> resultPage = baseMapper.getCommentNotificationPage(new Page<>(pageNum, pageSize), userId);

        return resultPage.convert(vo -> {
            if (StrUtil.isBlank(vo.getUsername())) {
                vo.setUsername("用户已注销");
            }
            return vo;
        });
    }
}
