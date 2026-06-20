package org.anonymous.af.controller;

import jakarta.annotation.Resource;
import org.anonymous.af.model.entity.FileEntity;
import org.anonymous.af.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * 文件接口控制器
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("id") Long id) throws FileNotFoundException {
        FileEntity entity = fileService.getById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + new String(entity.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .body(new InputStreamResource(fileService.getFileInputStream(entity)));
    }
}