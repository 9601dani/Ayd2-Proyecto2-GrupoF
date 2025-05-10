package com.codenbugs.ms_project.dtos.cases;

public record CaseCancelledRequestDto(
        Integer id,
        String reasonCancellation
) {
}
