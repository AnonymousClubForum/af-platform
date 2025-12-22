package org.anonymous.af.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.anonymous.af.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_post")
public class PostEntity extends BaseEntity {
    private String username;

    private String title;

    private String content;
}
