package com.example.test.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.test.domain.Company;
import com.example.test.service.CompanyService;
import com.example.test.service.JobService;
import com.example.test.service.JwtService;
import com.example.test.service.SocketService;
import com.example.test.service.TransactionService;
import org.springframework.security.oauth2.jwt.Jwt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AppGateway {

    private final SocketIOServer server;
    private final Map<String, SocketIOClient> clients = new ConcurrentHashMap<>();
    private final SocketService socketService;
    private final TransactionService transactionService;
    private final JobService jobsService;
    private final CompanyService companyService;
    private final JwtService jwtService;

    public AppGateway(SocketIOServer server, SocketService socketService, TransactionService transactionService,
            JobService jobsService, CompanyService companyService, JwtService jwtService) {
        this.server = server;
        this.socketService = socketService;
        this.transactionService = transactionService;
        this.jobsService = jobsService;
        this.companyService = companyService;
        this.jwtService = jwtService;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        try{
            Jwt jwt = jwtService.checkAccessToken(client.getHandshakeData().getSingleUrlParam("accessToken"));
            if (jwt == null) {
                client.disconnect();
                return;
            }

            String usrEmail = jwt.getClaim("sub");
            if (usrEmail != null) {
                clients.put(usrEmail, client);
                log.info("Client connected: " + usrEmail);
            }
            else{
                client.disconnect();
            }
        }
        catch(Exception e){
            client.disconnect();
            return;
        }
        
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String userIdToRemove = null;
        for (Map.Entry<String, SocketIOClient> entry : clients.entrySet()) {
            if (entry.getValue().getSessionId().equals(client.getSessionId())) {
                userIdToRemove = entry.getKey();
                break;
            }
        }
        if (userIdToRemove != null) {
            clients.remove(userIdToRemove);
        }
        log.info("Client disconnected: " + userIdToRemove);
    }

    @OnEvent("message")
    public void onMessage(SocketIOClient client, Object payload) {
        server.getBroadcastOperations().sendEvent("message", payload);
    }

    @OnEvent("typing")
    public void onTyping(SocketIOClient client, Object payload) {
        String room = client.getNamespace().getName();
        this.socketService.sendEventExceptSender(room, "typing", client, payload);
    }

    @OnEvent("stopTyping")
    public void onStopTyping(SocketIOClient client, Object payload) {
        String room = client.getNamespace().getName();
        this.socketService.sendEventExceptSender(room, "stopTyping", client, payload);
    }

    @OnEvent("checkPayment")
    public void handleTransactionSuccess(SocketIOClient client, Map<String, Object> payload) {
        String jobId = String.valueOf(payload.get("jobId"));
        String userId = String.valueOf(payload.get("userId"));
        String code = String.valueOf(payload.get("code"));

        Map result = this.transactionService.checkPayment(Map.of(
                "code", code,
                "amount", 2000));
        if ((Integer) result.get("transaction_status") == 0) {
            client.sendEvent("checkPayment", Map.of(
                    "message", "Transaction failed",
                    "status", 0));
            return;
        }

        this.jobsService.addPaidUser(jobId, userId);
        client.sendEvent("checkPayment", Map.of(
                "message", "Transaction success",
                "status", 1));
    }

    @OnEvent("createJob")
    @Transactional
    public void handleSendNotificationFromServer(SocketIOClient client, Map<String, Object> payload) {
        String senderId = (String) payload.get("senderId");
        if (senderId != null) {
            Company company = companyService.getById(senderId);
            if (company != null) {
                company.getUsersFollowed().forEach(user -> {
                    String email = user.getEmail();
                    String jobName = (String) payload.get("jobName");
                    String jobId = (String) payload.get("jobId");
                    SocketIOClient targetClient = clients.get(email);
                   
                    if (targetClient != null) {
                        String messages = "Công ty bạn đang theo dõi (" + company.getName() + ") đã tạo mới công việc " + jobName + "!";
                        targetClient.sendEvent("createJob", Map.of(
                                "message", messages,
                                "companyName", company.getName(),
                                "jobId", jobId
                        ));
                        log.info("Notification sent to user: " + email);
                    } else {
                        log.info("User with email: " + email + " not found");
                    }
                });
            }
        }
    }
}
