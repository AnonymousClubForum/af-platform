package org.anonymous.af.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.anonymous.af.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_file")
public class FileEntity extends BaseEntity {
    private String fileName;
}
