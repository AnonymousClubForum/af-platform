package org.anonymous.af.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.anonymous.af.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_user")
public class UserEntity extends BaseEntity {
    private Boolean isAdmin;

    private String username;

    private String password;

    private String gender;

    private Long avatarId;

    private Long avatarThumbNailId;
}
