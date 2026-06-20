package org.anonymous.af.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.anonymous.af.model.entity.FileEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件服务接口
 */
@Service
public interface FileService extends IService<FileEntity> {
    /**
     * 通用文件上传
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 获取文件
     */
    InputStream getFileInputStream(FileEntity entity) throws FileNotFoundException;
}