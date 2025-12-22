package org.anonymous.af.common;

import lombok.Data;

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
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    private BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应（有数据）
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseConstants.SUCCESS, "操作成功", data);
    }

    // 失败响应
    public static <T> BaseResponse<T> error(String msg) {
        return new BaseResponse<>(ResponseConstants.ERROR, msg, null);
    }

    // 请求错误响应
    public static <T> BaseResponse<T> badRequest(String msg) {
        return new BaseResponse<>(ResponseConstants.BAD_REQUEST, msg, null);
    }
}