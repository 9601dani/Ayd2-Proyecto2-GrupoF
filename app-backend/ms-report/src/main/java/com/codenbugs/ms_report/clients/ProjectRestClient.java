package com.codenbugs.ms_report.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-project", url = "http://localhost:8002/v1/projects")
public interface ProjectRestClient {
}
