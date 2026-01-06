package org.anonymous.af.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anonymous.af.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
