package fortov.egor.diploma.loggingFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Component
public class LoggingFilter extends AbstractRequestLoggingFilter {

//    private static Set<Pattern> PATTERNS = Set.of(
//            Pattern.compile("^/events$"),
//            Pattern.compile("^/events/\\d+$")
//    );
    private final String app;

    public LoggingFilter(@Value("${spring.application.name}") String app) {
        this.app = app;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
//        String uri = request.getRequestURI();
        return true;  // PATTERNS.stream().anyMatch(pattern -> pattern.matcher(uri).find());
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info("URI request: {}, ip addr: {}", request.getRequestURI(), request.getRemoteAddr());
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.info("request handled");
    }

//    @Override
//    protected void afterRequest(HttpServletRequest request, String message) {
//        statClient.hit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
//    }
}
