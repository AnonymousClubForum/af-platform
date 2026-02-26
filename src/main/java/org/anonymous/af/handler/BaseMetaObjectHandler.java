package org.anonymous.af.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class BaseMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        if (metaObject.hasGetter("ctime")) {
            this.setFieldValByName("ctime", now, metaObject);
        }
        if (metaObject.hasGetter("utime")) {
            this.setFieldValByName("utime", now, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("utime")) {
            this.setFieldValByName("utime", new Date(), metaObject);
        }
    }
}