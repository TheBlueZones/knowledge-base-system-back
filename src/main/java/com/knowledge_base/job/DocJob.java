package com.knowledge_base.job;

import com.knowledge_base.service.DocService;
import com.knowledge_base.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    @Resource
    private DocService docService;
    @Resource
    private SnowFlake snowFlake;
   private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

   /**
    * 每30秒更新一次电子书的数据
    */
   @Scheduled(cron = "5/30 * * * * ?")
   public void cron() { // 增加日志流水号
       MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
       LOG.info("更新电子书下的文档数据开始");
       long start =System.currentTimeMillis();
     docService.updateEbookInfo();
     LOG.info("更新电子书下的文档数据,耗时：{}毫秒",System.currentTimeMillis()-start);

   }

}
