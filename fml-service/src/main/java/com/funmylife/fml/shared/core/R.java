package com.funmylife.fml.shared.core;

import lombok.Data;

/**
 * 统一响应封装 — 前端通过 code === 200 判断成功
 */
@Data
public class R<T> {

    /** 状态码：200=成功 */
    private int code;

    /** 提示信息 */
    private String msg;

    /** 响应数据 */
    private T data;

    /** 成功响应（带数据） */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    /** 成功响应（无数据） */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /** 失败响应（默认 500） */
    public static <T> R<T> fail(String msg) {
        return fail(500, msg);
    }

    /** 失败响应（自定义状态码） */
    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
