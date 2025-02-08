package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.response.ResponseStatisticDTO;
import com.example.test.service.StatisticService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/report")
    public ResponseEntity<ResponseStatisticDTO> report(
            @RequestParam(name = "type", required = false) String param)
            throws BadRequestException {
        return new ResponseEntity<>(this.statisticService.report(param), HttpStatus.OK);
    }

}
