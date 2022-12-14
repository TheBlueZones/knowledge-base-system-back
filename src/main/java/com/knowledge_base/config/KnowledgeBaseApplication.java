package com.knowledge_base.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan("com.knowledge_base")
@MapperScan(basePackages = "com.knowledge_base.model.dao")
@EnableScheduling//定时器
@EnableAsync
public class KnowledgeBaseApplication {
    private static final Logger LOG = LoggerFactory.getLogger(KnowledgeBaseApplication.class);
    /*这里导入的包都是org.slf4j*/
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KnowledgeBaseApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址: \thttp://127.0.0.1:{}", env.getProperty("server.port"));
    }


}
