package org.anonymous.af.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.config.AfProperties;
import org.anonymous.af.exception.AfException;
import org.anonymous.af.mapper.FileMapper;
import org.anonymous.af.model.entity.FileEntity;
import org.anonymous.af.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件服务实现类
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {
    @Resource
    private AfProperties afProperties;

    /**
     * 生成唯一文件名（UUID）
     *
     * @param fileName 原始文件名
     * @return 生成的文件名
     */
    private String generateFileName(String fileName) {
        return IdUtil.fastSimpleUUID() + '.' + FileUtil.getSuffix(fileName);
    }

    private String getFullPath(String fileName) {
        return afProperties.getRootSavePath() + File.separator + fileName;
    }

    /**
     * 存储文件
     *
     * @param path        存储路径
     * @param inputStream 文件输入流
     */
    private void storageFile(String path, InputStream inputStream) throws IOException {
        Path targetPath = Paths.get(path);

        // 获取父目录路径，确保目录存在（多级目录会自动创建）
        Path parentDir = targetPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        // 将输入流写入文件
        try (inputStream) {
            Files.copy(inputStream, targetPath);
        }
    }

    /**
     * 通用文件上传
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty() || StrUtil.isBlank(file.getOriginalFilename())) {
            throw new AfException("上传文件为空");
        }
        // 保存文件
        String fileName = generateFileName(file.getOriginalFilename());
        String filePath = getFullPath(fileName);
        storageFile(filePath, file.getInputStream());

        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(IdWorker.getId());
        fileEntity.setFileName(fileName);
        save(fileEntity);

        return fileEntity.getId().toString();
    }

    @Override
    public InputStream getFileInputStream(FileEntity entity) throws FileNotFoundException {
        String fileName = entity.getFileName();
        String filePath = getFullPath(fileName);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new AfException("文件不存在或不是有效文件");
        }
        return new FileInputStream(file);
    }
}