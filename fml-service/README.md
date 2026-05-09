# fml-service

`fml-service` 是 FunMyLife / Life Manager 的轻量后端服务，独立于 `admin-service`。它只承接 Life Manager 业务接口，不依赖 RuoYi 的租户、菜单、权限、登录和后台管理模块。

## 当前职责

- 项目基础信息查询
- 功能块实例配置查询与批量保存
- Life Capability 注册和查询
- 无限暖暖项目的目标、版本活动、素材、图册、资产、笔记、时间轴和 AI 摘要接口
- 统一响应包装和基础异常处理

当前服务采用 `POST + JSON body` 作为 Life Manager 接口约定，`projectId` 放在请求体中，不放 URL path。

## 技术栈

- JDK 21
- Spring Boot 3.5
- MyBatis-Plus
- MySQL
- Lombok
- Maven

## 本地启动

默认配置位于 `src/main/resources/application.yml`：

```yaml
server:
  port: 8082
```

数据库默认连接：

```text
jdbc:mysql://localhost:3306/fml
```

账号密码可通过环境变量覆盖：

```bash
export FML_DB_USERNAME=root
export FML_DB_PASSWORD=123456
```

初始化数据库：

```bash
mysql -uroot -p -e "create database if not exists fml default character set utf8mb4 collate utf8mb4_general_ci;"
mysql -uroot -p fml < ../admin-service/script/sql/lm_life.sql
```

启动服务：

```bash
mvn spring-boot:run
```

编译验证：

```bash
mvn -DskipTests compile
```

## 前端联调

本地前端在 `admin-web` 下启动。联调 `fml-service` 时，建议在 `admin-web/.env.dev.local` 中设置：

```env
VITE_SERVICE_BASE_URL=http://localhost:8082
```

然后启动前端：

```bash
cd ../admin-web
pnpm dev
```

前端访问地址通常是 `http://localhost:9527`。

## 接口文档

无限暖暖项目当前接口见：

```text
../docs/infinity-nikki-api.md
```

后续新增接口时保持以下边界：

- Controller 返回 VO，不直接暴露持久化 Entity。
- 查询和写入都使用 POST。
- 项目级接口统一从请求体读取 `projectId`。
- 跨项目数据修改必须校验业务数据是否属于当前 `projectId`。
