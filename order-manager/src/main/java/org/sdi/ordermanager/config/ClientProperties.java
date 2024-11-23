package org.sdi.ordermanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client")
public class ClientProperties {

    private Client productManager;

    @Getter
    @Setter
    public static class Client {
        private String baseUrl;
    }
}
