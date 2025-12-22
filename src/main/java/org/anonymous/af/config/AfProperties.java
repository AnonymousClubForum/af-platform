package org.anonymous.af.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "org.anonymous")
public class AfProperties {
    private StorageConfig storageConfig;

    @Data
    public static class StorageConfig {
        private String url;
        private String uploadFile = "/file/upload/file";
        private String uploadImage = "/file/upload/image";
        private String downloadFile = "/file/download";
    }
}