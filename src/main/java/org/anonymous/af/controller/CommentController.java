package org.anonymous.af.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.model.request.SaveCommentRequest;
import org.anonymous.af.model.response.CommentVo;
import org.anonymous.af.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/save")
    public BaseResponse<String> createComment(@RequestBody SaveCommentRequest request) {
        commentService.createComment(request);
        return BaseResponse.success("发布成功");
    }

    @DeleteMapping("/delete")
    public BaseResponse<String> deleteComment(@RequestParam Long id) {
        commentService.deleteComment(id);
        return BaseResponse.success("删除成功");
    }

    @GetMapping("/get_page")
    public BaseResponse<IPage<CommentVo>> getCommentPage(@RequestParam Long pageNum,
                                                         @RequestParam Long pageSize,
                                                         @RequestParam Long postId,
                                                         @RequestParam(required = false) Long userId,
                                                         @RequestParam Boolean isDesc) {
        return BaseResponse.success(commentService.getCommentPage(pageNum, pageSize, postId, userId, isDesc));
    }
}
