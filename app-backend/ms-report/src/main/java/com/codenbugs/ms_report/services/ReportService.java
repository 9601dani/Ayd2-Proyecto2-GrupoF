package com.codenbugs.ms_report.services;


import com.codenbugs.ms_report.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_report.dtos.report.Report1Dto;
import com.codenbugs.ms_report.dtos.report.Report2Dto;
import com.codenbugs.ms_report.dtos.report.Report3Dto;
import com.codenbugs.ms_report.dtos.report.Report4Dto;
import com.codenbugs.ms_report.dtos.user.UserResponseWithName;
import com.codenbugs.ms_report.exceptions.user.UserNotFoundException;

import java.util.List;

public interface ReportService {

    List<Report1Dto> getReport1(Boolean isEnabled);

    List<Report2Dto> getReport2(Integer projectId) throws UserNotFoundException;

    List<Report3Dto> getReport3(Integer userId) throws UserNotFoundException;

    List<Report4Dto> getReport4(Integer typeId) throws UserNotFoundException;

    List<UserResponseWithName> getReport6();

    List<ProjectResponseWithoutUser> getReport7();
}
