package com.example.test.service;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;

@Service
public class SocketService {
    public void sendEventExceptSender(String room, String eventName, SocketIOClient senderClient,
            Object payload) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName, payload);
            }
        }
    }
}
