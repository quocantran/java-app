package com.example.test.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {
    @Value("${transaction.url}")
    private String transactionUrl;

    @Value("${transaction.api.key}")
    private String transactionApiKey;

    private final RestTemplate restTemplate;

    public TransactionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCurrentDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    @CircuitBreaker(name = "checkTransaction", fallbackMethod = "fallBackCircuitBreaker")
    public Map<String, Object> checkPayment(Map<String, Object> body) {

        String code = (String) body.get("code");
        Integer amount = (Integer) body.get("amount");

        String currentDate = getCurrentDate();
        String url = transactionUrl + "?fromDate=" + currentDate;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "apikey " + transactionApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> data = response.getBody();
        Map<String, Object> dataMap = (Map<String, Object>) data.get("data");
        List<Map<String, Object>> recordsPaid = (List<Map<String, Object>>) dataMap.get("records");

        for (Map<String, Object> record : recordsPaid) {
            Integer recordAmount = (Integer) record.get("amount");
            String description = (String) record.get("description");
            if (recordAmount >= amount && description.contains(code)) {
                return Map.of(
                        "status", "success",
                        "transaction_status", 1);
            }
        }

        return Map.of(
                "status", "success",
                "transaction_status", 0);

    }

    public Map<String, Object> fallBackCircuitBreaker(Throwable t) {
        log.error("Circuit breaker triggered, fallback method called", t);
        return Map.of(
                "status", "failed",
                "transaction_status", 0);
    }
}
