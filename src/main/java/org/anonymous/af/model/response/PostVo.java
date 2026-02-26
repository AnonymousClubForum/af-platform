package org.anonymous.af.model.response;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.annotation.Resource;
import lombok.Data;
import org.anonymous.af.model.entity.PostEntity;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.service.UserService;

import java.util.Date;

@Data
public class PostVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String username;

    private String avatarId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date utime;

    @Resource
    private UserService userService;

    public PostVo(PostEntity entity) {
        BeanUtil.copyProperties(entity, this, true);
        UserEntity userEntity = userService.getById(entity.getUserId());
        if (userEntity != null) {
            this.setUsername(userEntity.getUsername());
            if (userEntity.getAvatarId() != null) {
                this.setAvatarId(userEntity.getAvatarId().toString());
            }
        } else {
            this.setUsername("用户已注销");
        }
    }
}
