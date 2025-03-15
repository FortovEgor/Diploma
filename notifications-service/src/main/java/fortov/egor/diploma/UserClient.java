package fortov.egor.diploma;

import fortov.egor.diploma.user.UserFullInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

@Slf4j
public class UserClient {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofMillis(500L);
    public static final Duration DEFAULT_READ_TIMEOUT = Duration.ofMillis(500L);

    private final RestTemplate rest;

    public UserClient(String baseUrl) {
        rest = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setReadTimeout(DEFAULT_READ_TIMEOUT)
                .build();
    }

    public UserClient(RestTemplate rest) {
        this.rest = rest;
    }

    public UserFullInfoDto getUser(Long userId) {
        if (userId < 0) {
            log.info("user id can not be negative; http request aborted");
            return null;
        }
        ResponseEntity<UserFullInfoDto> response = rest
                .exchange("/users/" + userId, HttpMethod.GET, null, new ParameterizedTypeReference<UserFullInfoDto>() {});
        return response.getBody();
    }
}