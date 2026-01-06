package org.anonymous.af.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anonymous.af.model.entity.PostEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<PostEntity> {
}
