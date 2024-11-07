package org.sdi.productmanager.client;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.config.ClientProperties;
import org.sdi.productmanager.dto.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final ClientProperties clientProperties;
    private static final String GET_USERS_PATH = "/api/v1/users/{email}";

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(value = "usersCache")
    public UserDetails getUserDetails(String email) {
        try {
            final String url = buildUrl(email);
            return restTemplate.getForObject(url, UserDetails.class);
        } catch(RestClientException ex) {
            throw new RuntimeException(String.format("Error while getting user with email=%s from user-manager", email), ex);
        }
    }

    private String buildUrl(String email) {
        String baseUrl = clientProperties.getUserManager().getBaseUrl();
        return baseUrl + GET_USERS_PATH.replace("{email}", email);
    }
}
