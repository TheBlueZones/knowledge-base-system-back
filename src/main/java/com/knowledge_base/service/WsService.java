package com.knowledge_base.service;

import com.knowledge_base.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {
    @Resource
    public WebSocketServer webSocketServer;

    public void sendInfo(String message,String logId){
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }

}
