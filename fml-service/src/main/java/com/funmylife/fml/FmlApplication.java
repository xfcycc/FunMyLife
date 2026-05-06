package com.funmylife.fml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FunMyLife 轻量后端服务入口 — 独立于 RuoYi，无租户/权限/菜单/登录依赖
 */
@SpringBootApplication
public class FmlApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmlApplication.class, args);
    }
}
