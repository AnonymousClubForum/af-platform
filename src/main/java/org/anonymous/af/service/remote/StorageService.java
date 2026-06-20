package org.anonymous.af.service.remote;

import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.config.AfProperties;
import org.anonymous.af.exception.AfException;
import org.anonymous.af.exception.ThirdPartyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class StorageService {
    @Resource
    private AfProperties afProperties;

    /**
     * 上传文件
     */
    public Long uploadFile(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        BytesResource fileResource = new BytesResource(fileBytes, fileName);
        log.info("UploadFile {}", file.getOriginalFilename());
        try (HttpResponse response = HttpRequest.post(afProperties.getStorageConfig().getUrl()
                        + afProperties.getStorageConfig().getUploadFile()).form("file", fileResource).execute()) {
            log.info("uploadFile response: {}", response);
            String responseBody = response.body();
            BaseResponse<String> baseResponse = JSONUtil.toBean(responseBody, new TypeReference<>() {
            }, false);
            if (!baseResponse.getCode().equals(HttpStatus.OK.value()) || baseResponse.getData() == null) {
                throw new AfException("请求错误");
            }
            return Long.valueOf(baseResponse.getData());
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }
}