package org.anonymous.af.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anonymous.af.model.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {
}
