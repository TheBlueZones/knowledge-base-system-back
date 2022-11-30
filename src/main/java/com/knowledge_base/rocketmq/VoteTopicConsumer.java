// package com.knowledge_base.rocketmq;
//
// import com.knowledge_base.websocket.WebSocketServer;
// import org.apache.rocketmq.common.message.MessageExt;
// import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
// import org.apache.rocketmq.spring.core.RocketMQListener;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Service;
//
// import javax.annotation.Resource;
//
// @Service
//// 监听不同的topic，只需要修改topic的值即可
// @RocketMQMessageListener(consumerGroup = "default", topic = "VOTE_TOPIC")
// public class VoteTopicConsumer implements RocketMQListener<MessageExt> {
//
//     private static final Logger LOG = LoggerFactory.getLogger(VoteTopicConsumer.class);
//
//     @Resource
//     public WebSocketServer webSocketServer;
//
//     @Override
//     public void onMessage(MessageExt messageExt) {
//         byte[] body = messageExt.getBody();
//         LOG.info("ROCKETMQ收到消息：{}", new String(body));
//         webSocketServer.sendInfo(new String(body));
//     }
// }
