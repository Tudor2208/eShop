package org.sdi.productmanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client")
public class ClientProperties {

    private Client userManager;

    @Getter
    @Setter
    public static class Client {
        private String baseUrl;
    }
}
