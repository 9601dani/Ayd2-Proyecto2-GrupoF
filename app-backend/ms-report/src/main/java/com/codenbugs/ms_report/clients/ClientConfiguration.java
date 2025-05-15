package com.codenbugs.ms_report.clients;

import com.codenbugs.ms_report.utils.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public ErrorDecoder defaultErrorDecoder() {
        return new CustomErrorDecoder();
    }
}
