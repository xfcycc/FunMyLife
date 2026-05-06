package com.funmylife.fml.shared.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置 — 扫描 Mapper 接口所在的包
 */
@Configuration
@MapperScan("com.funmylife.fml.infrastructure.persistence.mapper")
public class MybatisPlusConfig {
}
