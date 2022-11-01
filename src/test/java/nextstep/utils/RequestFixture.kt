package nextstep.utils

import nextstep.auth.TokenRequest
import nextstep.member.MemberRequest
import nextstep.member.Role
import nextstep.reservation.ReservationRequest
import nextstep.schedule.ScheduleRequest
import nextstep.theme.ThemeRequest

class RequestFixture {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun memberRequest(
            role: Role = Role.USER,
            userName: String = "username",
            password: String = "password",
            name: String = "name",
            phoneNumber: String = "010-1234-1234",
        ) = MemberRequest(userName, password, name, phoneNumber, role)

        @JvmStatic
        @JvmOverloads
        fun tokenRequest(
            memberRequest: MemberRequest
        ) = TokenRequest(memberRequest.username, memberRequest.password)

        @JvmStatic
        @JvmOverloads
        fun themeRequest(
            name: String = "테마이름",
            decs: String = "테마설명",
            price: Int = 22000
        ) = ThemeRequest(name, decs, price)

        @JvmStatic
        @JvmOverloads
        fun scheduleRequest(
            themeId: Long = 1L,
            date: String = "2022-08-11",
            time: String = "13:00"
        ) = ScheduleRequest(themeId, date, time)

        @JvmStatic
        @JvmOverloads
        fun reservationRequest(
            scheduleId: Long = 1L,
        ) = ReservationRequest(scheduleId)
    }
}
