package com.codenbugs.ms_company.exceptions.setting;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SettingNotUpdatedException extends SettingException {
    public SettingNotUpdatedException(String message) {
        super(message);
    }
}
