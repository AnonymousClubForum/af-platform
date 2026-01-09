# 阶段1：构建应用
FROM maven:3.8.8-openjdk-21-slim AS builder
WORKDIR /app
# 复制pom.xml先下载依赖（利用Docker缓存，依赖不变时无需重新下载）
COPY pom.xml .
RUN mvn dependency:go-offline -B
# 复制所有源码
COPY src ./src
# 打包
RUN mvn clean package

# 阶段2：运行应用
FROM openjdk:21-jdk-slim
WORKDIR /app
# 从构建阶段复制打包好的jar包
COPY --from=builder /app/target/*.jar app.jar
# 暴露应用端口（根据Spring Boot配置修改，比如8080）
EXPOSE 8080
# 设置启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]