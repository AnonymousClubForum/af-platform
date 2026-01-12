package org.anonymous.af.service.remote;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.config.AfProperties;
import org.anonymous.af.constants.ResponseConstants;
import org.anonymous.af.exception.AfException;
import org.anonymous.af.exception.ThirdPartyException;
import org.anonymous.af.model.request.remote.UploadImageRequest;
import org.anonymous.af.model.response.remote.UploadImageResponse;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    @Resource
    private AfProperties afProperties;

    /**
     * 上传图片
     */
    public UploadImageResponse uploadImage(UploadImageRequest request) {
        String requestUrl = afProperties.getStorageConfig().getUrl() + afProperties.getStorageConfig().getUploadImage();
        try (HttpResponse response = HttpRequest.post(requestUrl).body(JSONUtil.toJsonStr(request)).execute()) {
            String responseBody = response.body();
            BaseResponse<UploadImageResponse> baseResponse = JSONUtil.toBean(responseBody, new TypeReference<>() {
            }, false);
            if (!baseResponse.getCode().equals(ResponseConstants.SUCCESS) || baseResponse.getData() == null) {
                throw new AfException("请求错误");
            }
            return baseResponse.getData();
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }
}