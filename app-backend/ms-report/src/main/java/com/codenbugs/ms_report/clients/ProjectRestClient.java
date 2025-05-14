package com.codenbugs.ms_report.clients;

import com.codenbugs.ms_report.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_report.dtos.report.Report1Dto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-project", url = "http://localhost:8002/v1/projects")
public interface ProjectRestClient {

    @GetMapping("/report1")
    List<Report1Dto> getProjectReport();

    @GetMapping("/all")
    List<ProjectResponseWithoutUser> getAllProjects();
}
