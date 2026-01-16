package org.anonymous.af.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.model.request.SavePostRequest;
import org.anonymous.af.model.response.PostVo;
import org.anonymous.af.model.response.SimplePostVo;
import org.anonymous.af.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/save")
    public BaseResponse<String> createPost(@RequestBody SavePostRequest request) {
        postService.createPost(request);
        return BaseResponse.success("发布成功");
    }

    @PostMapping("/update")
    public BaseResponse<String> updatePost(@RequestBody SavePostRequest request) {
        postService.updatePost(request);
        return BaseResponse.success("编辑成功");
    }

    @DeleteMapping("/delete")
    public BaseResponse<String> deletePost(@RequestParam Long id) {
        postService.removeById(id);
        return BaseResponse.success("删除成功");
    }

    @GetMapping("/get")
    public BaseResponse<PostVo> getPost(@RequestParam Long id) {
        return BaseResponse.success(postService.getPostById(id));
    }

    @GetMapping("/get_page")
    public BaseResponse<IPage<SimplePostVo>> getPostPage(@RequestParam Long pageNum,
                                                         @RequestParam Long pageSize,
                                                         @RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) String searchContent) {
        return BaseResponse.success(postService.getPostPage(pageNum, pageSize, userId, searchContent));
    }
}
