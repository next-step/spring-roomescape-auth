package roomescape.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.member.domain.MemberRoleName;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    @GetMapping
    @NeedMemberRole(MemberRoleName.ADMIN)
    public String adminIndex() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    @NeedMemberRole(MemberRoleName.ADMIN)
    public String adminReservationPage() {
        return "/admin/reservation";
    }

    @GetMapping("/time")
    @NeedMemberRole(MemberRoleName.ADMIN)
    public String adminTimePage() {
        return "/admin/time";
    }

    @GetMapping("/theme")
    @NeedMemberRole(MemberRoleName.ADMIN)
    public String adminThemes() {
        return "/admin/theme";
    }
}
