package com.codenbugs.ms_report.controllers;

import com.codenbugs.ms_report.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_report.dtos.report.*;
import com.codenbugs.ms_report.dtos.user.UserResponseWithName;
import com.codenbugs.ms_report.dtos.utils.CaseUserReportDto;
import com.codenbugs.ms_report.dtos.utils.TopProjectByCancelledCasesDto;
import com.codenbugs.ms_report.dtos.utils.TopProjectByCompletedCasesDto;
import com.codenbugs.ms_report.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_report.services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/reports")
@AllArgsConstructor
public class ReportController {

    private ReportService reportService;

    @GetMapping("/report1")
    public List<Report1Dto> getReport1(@RequestParam(required = false) Boolean isEnabled) {
        return reportService.getReport1(isEnabled);
    }

    @GetMapping("/report2")
    public List<Report2Dto> getReport2(@RequestParam(required = false) Integer projectId) throws UserNotFoundException {
        return reportService.getReport2(projectId);
    }

    @GetMapping("/report3")
    public List<Report3Dto> getReport3(@RequestParam(required = false) Integer userId) throws UserNotFoundException {
        return reportService.getReport3(userId);
    }

    @GetMapping("/report4")
    public List<Report4Dto> getReport4(@RequestParam(required = false) Integer typeId) throws UserNotFoundException {
        return reportService.getReport4(typeId);
    }

    @GetMapping("/report6")
    public List<UserResponseWithName> getReport6() {
        return reportService.getReport6();
    }

    @GetMapping("/report7")
    public List<ProjectResponseWithoutUser> getReport7() {
        return reportService.getReport7();
    }

    @GetMapping("/report8")
    public Report8Dto getReport8() throws UserNotFoundException {
        return reportService.getReport8();
    }

    @GetMapping("/report9")
    public Report9Dto getReport9() throws UserNotFoundException {
        return reportService.getReport9();
    }

    @GetMapping("/report10")
    public TopProjectByCompletedCasesDto getReport10() throws UserNotFoundException {
        return reportService.getReport10();
    }

    @GetMapping("/report11")
    public TopProjectByCancelledCasesDto getReport11() throws UserNotFoundException {
        return reportService.getReport11();
    }

    @GetMapping("/report13")
    public List<CaseUserReportDto> getReport13(@RequestParam(required = false) Integer userId) throws UserNotFoundException {
        return reportService.getReport13(userId);
    }

    @GetMapping("/report14")
    public List<CaseUserReportDto> getReport14(@RequestParam(required = false) Integer typeId) throws UserNotFoundException {
        return reportService.getReport14(typeId);
    }



}
