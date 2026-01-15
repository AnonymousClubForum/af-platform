package org.anonymous.af.service.remote;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.config.AfProperties;
import org.anonymous.af.exception.ThirdPartyException;
import org.anonymous.af.model.request.remote.UploadImageRequest;
import org.anonymous.af.model.response.remote.UploadImageResponse;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class StorageService {
    @Resource
    private AfProperties afProperties;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 上传图片
     */
    public UploadImageResponse uploadImage(UploadImageRequest request) {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("af-storage");
            ServiceInstance instance = instances.get(RandomUtil.randomInt(instances.size()));

            ResponseEntity<BaseResponse<UploadImageResponse>> response = restTemplate.exchange(
                    URI.create(instance.getUri() + afProperties.getStorageConfig().getUploadImage()),
                    HttpMethod.GET,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (!response.getStatusCode().equals(HttpStatus.OK) || response.getBody() == null) {
                throw new ThirdPartyException("请求错误");
            }
            return response.getBody().getData();
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }
}