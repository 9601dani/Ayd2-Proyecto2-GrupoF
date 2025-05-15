package com.codenbugs.ms_report.services;


import com.codenbugs.ms_report.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_report.dtos.report.*;
import com.codenbugs.ms_report.dtos.user.UserResponseWithName;
import com.codenbugs.ms_report.dtos.utils.CaseUserReportDto;
import com.codenbugs.ms_report.dtos.utils.TopProjectByCancelledCasesDto;
import com.codenbugs.ms_report.dtos.utils.TopProjectByCompletedCasesDto;
import com.codenbugs.ms_report.dtos.utils.TopWorkerByHoursDto;
import com.codenbugs.ms_report.exceptions.user.UserNotFoundException;

import java.util.List;

public interface ReportService {

    List<Report1Dto> getReport1(Boolean isEnabled);

    List<Report2Dto> getReport2(Integer projectId) throws UserNotFoundException;

    List<Report3Dto> getReport3(Integer userId) throws UserNotFoundException;

    List<Report4Dto> getReport4(Integer typeId) throws UserNotFoundException;

    List<UserResponseWithName> getReport6();

    List<ProjectResponseWithoutUser> getReport7();

    Report8Dto getReport8() throws UserNotFoundException;

    Report9Dto getReport9() throws UserNotFoundException;

    TopProjectByCompletedCasesDto getReport10() throws UserNotFoundException;

    TopProjectByCancelledCasesDto getReport11() throws UserNotFoundException;

    List<CaseUserReportDto> getReport13(Integer userId);

    List<CaseUserReportDto> getReport14(Integer typeId);

}
