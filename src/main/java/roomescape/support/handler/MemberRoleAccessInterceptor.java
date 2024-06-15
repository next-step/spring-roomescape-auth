package roomescape.support.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import roomescape.apply.auth.application.JwtTokenManager;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.auth.application.exception.IllegalTokenException;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleNames;
import roomescape.support.ServletRequestTokenFinder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class MemberRoleAccessInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    public MemberRoleAccessInterceptor(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean hasAnnotation = hasNeedMemberRoleAnnotation(handler);
        if (!hasAnnotation) {
            return true;
        }

        String token = ServletRequestTokenFinder.getTokenByRequestCookies(request);

        try {
            jwtTokenManager.validateToken(token);
        } catch (IllegalTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String roleNames = jwtTokenManager.getRoleNameFromToken(token);
        Set<MemberRoleName> memberRoleNames = MemberRoleNames.getMemberRolesByRoleNames(roleNames);
        Set<MemberRoleName> requiredRoleNames = getRequiredMemberRolesInMethod(handler);
        boolean containRole = requiredRoleNames.stream().anyMatch(memberRoleNames::contains);
        if (!containRole) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        return containRole;
    }

    private boolean hasNeedMemberRoleAnnotation(Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        if (handler instanceof HandlerMethod handlerMethod) {
            return handlerMethod.hasMethodAnnotation(NeedMemberRole.class);
        }

        return false;
    }

    private Set<MemberRoleName> getRequiredMemberRolesInMethod(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            NeedMemberRole needAccountRole = handlerMethod.getMethodAnnotation(NeedMemberRole.class);
            return needAccountRole != null ? Arrays.stream(needAccountRole.value()).collect(toSet())
                    : Collections.emptySet();
        }

        return Collections.emptySet();
    }
}
