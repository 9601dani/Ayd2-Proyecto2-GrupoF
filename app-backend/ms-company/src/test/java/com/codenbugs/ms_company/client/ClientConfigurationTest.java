package com.codenbugs.ms_company.client;

import com.codenbugs.ms_company.utils.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientConfigurationTest {
    @Test
    void defaultErrorDecoder_returnsCustomErrorDecoderInstance() {
        // Arrange
        ClientConfiguration config = new ClientConfiguration();

        // Act
        ErrorDecoder decoder = config.defaultErrorDecoder();

        // Assert
        assertThat(decoder).isInstanceOf(CustomErrorDecoder.class);
    }
}
