package org.anonymous.af.exception;

import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.common.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数错误异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数错误异常：", e);
        return BaseResponse.badRequest(e.getMessage());
    }

    /**
     * 用户信息校验异常
     */
    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<?> handleTokenException(TokenException e) {
        log.error("用户信息校验异常：", e);
        return BaseResponse.error(e.getMessage());
    }

    /**
     * 第三方请求错误异常
     */
    @ExceptionHandler(ThirdPartyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleThirdPartyException(ThirdPartyException e) {
        log.error("第三方请求错误异常：", e);
        return BaseResponse.error(e.getMessage());
    }

    /**
     * Af异常
     */
    @ExceptionHandler(AfException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleAfException(AfException e) {
        log.error("业务逻辑异常：", e);
        return BaseResponse.error(e.getMessage());
    }

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return BaseResponse.error("服务器内部错误：" + e.getMessage());
    }
}