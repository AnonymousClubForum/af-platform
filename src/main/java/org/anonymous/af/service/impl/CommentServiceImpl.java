package org.anonymous.af.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.anonymous.af.model.response.CommentVo;
import org.anonymous.af.service.CommentService;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;

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
    public void deleteComment(Long id) {
        baseMapper.deleteById(id);
        baseMapper.delete(new LambdaQueryWrapper<CommentEntity>().eq(CommentEntity::getParentId, id));
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
    public IPage<CommentVo> getCommentPage(Long pageNum, Long pageSize, Long postId, Long parentId) {
        Page<CommentEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CommentEntity> queryWrapper = new LambdaQueryWrapper<CommentEntity>()
                .eq(postId != null, CommentEntity::getPostId, postId)
                .eq(parentId != null, CommentEntity::getParentId, parentId)
                .isNull(parentId == null, CommentEntity::getParentId)
                .orderByAsc(CommentEntity::getCtime);
        return baseMapper.selectPage(page, queryWrapper).convert(entity -> {
            CommentVo vo = new CommentVo();
            BeanUtil.copyProperties(entity, vo, true);
            UserEntity userEntity = userService.getById(entity.getUserId());
            if (userEntity != null) {
                vo.setUsername(userEntity.getUsername());
                if (userEntity.getAvatarId() != null) {
                    vo.setAvatarId(userEntity.getAvatarId().toString());
                }
            } else {
                vo.setUsername("用户已注销");
            }
            return vo;
        });
    }
}
