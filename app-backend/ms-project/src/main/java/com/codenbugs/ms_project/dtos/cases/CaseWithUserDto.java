package com.codenbugs.ms_project.dtos.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaseWithUserDto (
         Integer id,
         String name,
         String description,
         Integer fkProject,
         BigDecimal progressPercentage,
         Integer fkCaseType,
         Integer fkUser,
         LocalDateTime limitDate,
         Boolean isEnabled,
         Boolean isCancelled,
         String reasonCancellation
){
}
