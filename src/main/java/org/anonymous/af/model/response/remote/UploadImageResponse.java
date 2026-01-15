package org.anonymous.af.model.response.remote;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UploadImageResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imageId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long thumbnailId;
}
