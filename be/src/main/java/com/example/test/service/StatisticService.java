package com.example.test.service;

import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.response.ResponseStatisticDTO;
import com.example.test.utils.constant.StatisticTypeEnum;

@Service
public class StatisticService {
    private final UserService userService;
    private final JobService jobService;
    private final CompanyService companyService;

    public StatisticService(UserService userService, JobService jobService, CompanyService companyService) {
        this.userService = userService;
        this.jobService = jobService;
        this.companyService = companyService;
    }

    public ResponseStatisticDTO report(String param) throws BadRequestException {
        ResponseStatisticDTO response = new ResponseStatisticDTO();

        try {
            StatisticTypeEnum.valueOf(param.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid type");
        }

        response.setTotalUsers(this.userService.countUsers());
        response.setTotalJobs(this.jobService.countJobs());
        response.setTotalCompanies(this.companyService.countCompanies());

        switch (param.toLowerCase()) {
            case "week":
                response.setTotalCompaniesPast(this.companyService.countCompanyByWeek());
                response.setTotalJobsPast(this.jobService.countJobByWeek());
                response.setTotalUsersPast(this.userService.countUserByWeek());
                break;
            case "month":
                response.setTotalCompaniesPast(this.companyService.countCompanyByMonth());
                response.setTotalJobsPast(this.jobService.countJobByMonth());
                response.setTotalUsersPast(this.userService.countUserByMonth());
                break;

            case "year":
                response.setTotalCompaniesPast(this.companyService.countCompanyByYear());
                response.setTotalJobsPast(this.jobService.countJobByYear());
                response.setTotalUsersPast(this.userService.countUserByYear());
                break;

            default:
                break;
        }

        return response;
    }
}
