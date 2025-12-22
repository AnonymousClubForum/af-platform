package org.anonymous.af.model.request.remote;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageRequest {
    private MultipartFile file;
    private Integer width;
    private Integer height;
}
