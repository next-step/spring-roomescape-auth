package roomescape.auth.support;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SecurityPathConfig {

    // 인증이 필요 없는 경로
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/signup",
            "/api/login"
    );

    private static final Map<String, PathPattern> patternCache = new ConcurrentHashMap<>();
    private static final PathPatternParser pathPatternParser = new PathPatternParser();

    public boolean isPublicPath(final String requestPath) {
        return PUBLIC_PATHS.stream()
                .anyMatch(publicPath -> getPathPattern(publicPath).matches(PathContainer.parsePath(requestPath)));
    }

    private PathPattern getPathPattern(final String path) {
        return patternCache.computeIfAbsent(path, pathPatternParser::parse);
    }
}
