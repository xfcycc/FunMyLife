package com.funmylife.fml.shared.core;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器 — 区分 404/405/500，避免把未实现路径吞成 HTTP 200 + code 500
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 路径不存在 → HTTP 404 */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<R<Void>> handleNotFound(Exception e) {
        return ResponseEntity.status(404).body(R.fail(404, "Not Found"));
    }

    /** HTTP 方法不允许 → HTTP 405 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405).body(R.fail(405, "Method Not Allowed"));
    }

    /** 业务异常 → HTTP 200 + code 500（前端统一处理） */
    @ExceptionHandler(Exception.class)
    public R<Void> handle(Exception e) {
        return R.fail(e.getMessage());
    }
}
