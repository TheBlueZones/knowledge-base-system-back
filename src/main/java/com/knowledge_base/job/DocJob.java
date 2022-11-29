package com.knowledge_base.job;

import com.knowledge_base.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    @Resource
    private DocService docService;
   private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

   /**
    * 每30秒更新一次电子书的数据
    */
   @Scheduled(cron = "5/30 * * * * ?")
   public void cron() {
       LOG.info("更新电子书下的文档数据开始");
       long start =System.currentTimeMillis();
     docService.updateEbookInfo();
     LOG.info("更新电子书下的文档数据,耗时：{}毫秒",System.currentTimeMillis()-start);

   }

}
