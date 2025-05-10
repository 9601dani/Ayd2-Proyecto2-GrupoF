package com.codenbugs.ms_company.exceptions.setting;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SettingNotFoundException extends SettingException {
    public SettingNotFoundException(String message) {
        super(message);
    }
}
