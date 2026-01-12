package org.anonymous.af.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 统一返回结果封装
 */
@Data
public class BaseResponse<T> {
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    private BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应（有数据）
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), "操作成功", data);
    }

    // 失败响应
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    // 请求错误响应
    public static <T> BaseResponse<T> badRequest(String message) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}