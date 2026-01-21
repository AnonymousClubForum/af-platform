package org.anonymous.af.service.remote;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.config.AfProperties;
import org.anonymous.af.exception.ThirdPartyException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class StorageService {
    @Resource
    private AfProperties afProperties;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 上传文件
     */
    public Long uploadFile(MultipartFile file) {
        log.info("UploadFile {}", file.getOriginalFilename());
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("af-storage");
            ServiceInstance instance = instances.get(RandomUtil.randomInt(instances.size()));
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("file", file.getResource());
            ResponseEntity<BaseResponse<Long>> response = restTemplate.exchange(
                    URI.create(instance.getUri() + afProperties.getStorageConfig().getUploadFile()),
                    HttpMethod.POST,
                    new HttpEntity<>(formData),
                    new ParameterizedTypeReference<>() {
                    }
            );
            log.info("uploadFile response: {}", response);
            if (!response.getStatusCode().equals(HttpStatus.OK) || response.getBody() == null) {
                throw new ThirdPartyException("请求错误");
            }
            return response.getBody().getData();
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }
}