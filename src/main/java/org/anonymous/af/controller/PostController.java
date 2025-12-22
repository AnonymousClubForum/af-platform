package org.anonymous.af.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.model.request.QueryPostPageRequest;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.model.response.SimplePostVo;
import org.anonymous.af.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @PostMapping("/save")
    public BaseResponse<String> createPost(@RequestBody SavePostRequest request) {
        postService.createPost(request);
        return BaseResponse.success("发布成功");
    }

    @PostMapping("/update")
    public BaseResponse<String> updatePost(@RequestBody SavePostRequest request) {
        postService.updatePost(request);
        return BaseResponse.success("发布成功");
    }

    @GetMapping("/get")
    public BaseResponse<PostVo> getPost(@RequestParam Long id) {
        return BaseResponse.success(postService.getPostById(id));
    }

    @GetMapping("/get_page")
    public BaseResponse<Page<SimplePostVo>> getPostPage(@RequestBody QueryPostPageRequest request) {
        return BaseResponse.success(postService.getPostPage(request));
    }
}
