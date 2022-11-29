package com.knowledge_base.service;

import com.knowledge_base.websocket.WebSocketServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {
    @Resource
    public WebSocketServer webSocketServer;

    public void sendInfo(String message){
        webSocketServer.sendInfo(message);
    }

}
